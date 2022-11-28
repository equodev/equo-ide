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

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Arrays;
import org.gradle.testkit.runner.GradleRunner;
import org.junit.jupiter.api.io.TempDir;

public class GradleHarness {
	/**
	 * On OS X, the temp folder is a symlink, and some of gradle's stuff breaks symlinks. By only
	 * accessing it through the {@link #rootFolder()} and {@link #newFile(String)} apis, we can
	 * guarantee there will be no symlink problems.
	 */
	@TempDir File folderDontUseDirectly;

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

		public File toLines(String... lines) throws IOException {
			return toContent(String.join("\n", Arrays.asList(lines)));
		}

		public File toContent(String content) throws IOException {
			Files.write(file.toPath(), content.getBytes(StandardCharsets.UTF_8));
			return file;
		}
	}

	protected GradleRunner gradleRunner() throws IOException {
		return GradleRunner.create()
				.withGradleVersion(oldestGradleForJre())
				.withProjectDir(rootFolder())
				.withPluginClasspath();
	}

	private static String oldestGradleForJre() {
		switch (jreVersion()) {
			case 11:
				return "5.0";
			case 12:
				return "5.4";
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
