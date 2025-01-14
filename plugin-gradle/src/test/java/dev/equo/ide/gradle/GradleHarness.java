/*******************************************************************************
 * Copyright (c) 2022-2025 EquoTech, Inc. and others.
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

import static com.diffplug.selfie.Selfie.expectSelfie;

import com.diffplug.selfie.StringSelfie;
import dev.equo.ide.ResourceHarness;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.regex.Pattern;
import org.assertj.core.api.AbstractStringAssert;
import org.assertj.core.api.Assertions;
import org.gradle.testkit.runner.GradleRunner;
import org.junit.jupiter.api.BeforeEach;

public class GradleHarness extends ResourceHarness {
	@BeforeEach
	void copyPluginUnderTestMetadata() throws IOException {
		var content = Files.readAllBytes(Paths.get(DepsResolve.METADATA_PATH));
		setFile(DepsResolve.METADATA_PATH).toContent(content);
		setFile("settings.gradle").toContent("rootProject.name = 'under-test'");
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

		public StringSelfie expectSnapshotBetween(String before, String after) {
			var pattern =
					Pattern.compile(Pattern.quote(before) + "(.*)" + Pattern.quote(after), Pattern.DOTALL);
			var matcher = pattern.matcher(output);
			if (matcher.find()) {
				var toMatch = matcher.group(1).trim();
				return expectSelfie(toMatch);
			} else {
				throw new AssertionError(
						"Could not find `" + before + "` -> `" + after + "` in:\n" + output);
			}
		}

		public StringSelfie expectSnapshot() {
			return expectSelfie(output.trim());
		}

		public AbstractStringAssert<?> assertOutput() {
			return Assertions.assertThat(output);
		}
	}

	protected AbstractStringAssert<?> runFailAndAssert(String... args) throws IOException {
		var output = gradleRunner().withArguments(args).buildAndFail().getOutput().replace("\r", "");
		return Assertions.assertThat(output);
	}

	protected GradleRunner gradleRunner() throws IOException {
		var env = new HashMap<>(System.getenv());
		env.put("EQUO_GRADLE_HARNESS", "true");
		return GradleRunner.create()
				.withGradleVersion(oldestGradleForJre())
				.withProjectDir(rootFolder())
				.withPluginClasspath()
				.withEnvironment(env);
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
				//				return "6.0";
			case 14:
				//				return "6.3";
			case 15:
				//				return "6.7";
			case 16:
				//				return "7.0";
			case 17:
				//				return "7.3";
			case 18:
				return "7.5";
			case 19:
				return "7.6";
			case 20:
				return "8.3";
			case 21:
				return "8.5";
			case 22:
				return "8.8";
			case 23:
				return "8.10";
			default:
				throw new UnsupportedOperationException(
						"Update GradleHarness.oldestGradleForJre from https://docs.gradle.org/current/userguide/compatibility.html");
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
