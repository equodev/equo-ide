/*******************************************************************************
 * Copyright (c) 2022 EquoTech, Inc. and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     EquoTech, Inc. - initial API and implementation
 *******************************************************************************/
package dev.equo.ide.gradle;

import au.com.origin.snapshots.Expect;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.regex.Pattern;
import org.assertj.core.api.AbstractStringAssert;
import org.assertj.core.api.Assertions;
import org.gradle.testkit.runner.GradleRunner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.io.TempDir;

public class GradleHarness {
	/**
	 * On OS X, the temp folder is a symlink, and some of gradle's stuff breaks symlinks. By only
	 * accessing it through the {@link #rootFolder()} and {@link #newFile(String)} apis, we can
	 * guarantee there will be no symlink problems.
	 */
	@TempDir File folderDontUseDirectly;

	@BeforeEach
	void copyPluginUnderTestMetadata() throws IOException {
		var content = Files.readAllBytes(Paths.get(DepsResolve.METADATA_PATH));
		setFile(DepsResolve.METADATA_PATH).toContent(content);
		setFile("settings.gradle").toContent("rootProject.name = 'under-test'");
	}

	/** Returns the root folder (canonicalized to fix OS X issue) */
	protected File rootFolder() throws IOException {
		return folderDontUseDirectly.getCanonicalFile();
	}

	/** Returns a new child of the root folder. */
	protected File newFile(String subpath) throws IOException {
		return new File(rootFolder(), subpath);
	}

	protected WriteAsserter setFile(String path) throws IOException {
		return new WriteAsserter(newFile(path));
	}

	protected static class WriteAsserter {
		private File file;

		private WriteAsserter(File file) {
			file.getParentFile().mkdirs();
			this.file = file;
		}

		public File toContent(byte[] content) throws IOException {
			Files.write(file.toPath(), content);
			return file;
		}

		public File toContent(String content) throws IOException {
			return toContent(content.getBytes(StandardCharsets.UTF_8));
		}

		public File toLines(String... lines) throws IOException {
			return toContent(String.join("\n", Arrays.asList(lines)));
		}
	}

	protected AbstractStringAssert<?> runAndAssert(String... args) throws IOException {
		var output = gradleRunner().withArguments(args).build().getOutput().replace("\r", "");
		return Assertions.assertThat(output);
	}

	protected Output run(String... args) throws IOException {
		return new Output(gradleRunner().withArguments(args).build().getOutput());
	}

	protected Output runAndFail(String... args) throws IOException {
		return new Output(gradleRunner().withArguments(args).buildAndFail().getOutput());
	}

	public static class Output {
		private final String output;

		Output(String output) {
			this.output = output.replace("\r", "");
		}

		public void snapshotBetween(String before, String after, Expect expect) {
			var pattern =
					Pattern.compile(Pattern.quote(before) + "(.*)" + Pattern.quote(after), Pattern.DOTALL);
			var matcher = pattern.matcher(output);
			matcher.find();
			expect.toMatchSnapshot(matcher.group(1).trim());
		}
	}

	protected AbstractStringAssert<?> runFailAndAssert(String... args) throws IOException {
		var output = gradleRunner().withArguments(args).buildAndFail().getOutput().replace("\r", "");
		return Assertions.assertThat(output);
	}

	protected GradleRunner gradleRunner() throws IOException {
		return GradleRunner.create()
				.withGradleVersion(oldestGradleForJre())
				.withProjectDir(rootFolder())
				.withTestKitDir(rootFolder())
				.withPluginClasspath();
	}

	private static String oldestGradleForJre() {
		switch (jreVersion()) {
			case 11:
				// return "5.0";
			case 12:
				// return "5.4";
				// to support these older versions, we'll need to do some clever stuff
				// in EquoIdeGradlePlugin to detect which EquoIdeTask to run,
			case 13:
				return "6.0";
			case 14:
				return "6.3";
			case 15:
				return "6.7";
			case 16:
				return "7.0";
			case 17:
				return "7.3";
			case 18:
				return "7.5";
			case 19:
				return "7.6";
			default:
				throw new UnsupportedOperationException(
						"Update from https://docs.gradle.org/current/userguide/compatibility.html");
		}
	}

	private static int jreVersion() {
		String jre = System.getProperty("java.version");
		int firstDot = jre.indexOf('.');
		int major = Integer.parseInt(jre.substring(0, firstDot));
		if (major < 11) {
			throw new IllegalArgumentException("Only supports Java 11 and newer, this was " + jre);
		}
		return major;
	}
}
