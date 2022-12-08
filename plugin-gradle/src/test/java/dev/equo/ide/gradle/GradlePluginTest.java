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

import java.io.IOException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class GradlePluginTest extends GradleHarness {
	@Test
	public void tasks() throws IOException {
		setFile("build.gradle").toLines("plugins { id 'dev.equo.ide' }", "equoIde {", "}");
		String output =
				gradleRunner().withArguments("tasks", "--stacktrace").build().getOutput().replace("\r", "");
		Assertions.assertThat(output)
				.contains("IDE tasks\n" + "---------\n" + "equoIde - Launches EquoIDE");
	}

	@Test
	public void p2repoArgCheck() throws IOException {
		setFile("build.gradle")
				.toLines(
						"plugins { id 'dev.equo.ide' }",
						"equoIde {",
						"  p2repo 'https://somerepo'",
						"  install 'org.eclipse.swt'",
						"}");
		var noSlash =
				gradleRunner().withArguments("equoIde").buildAndFail().getOutput().replace("\r", "");
		Assertions.assertThat(noSlash)
				.contains(
						"> Must end with /\n"
								+ "  p2repo(\"https://somerepo\")   <- WRONG\n"
								+ "  p2repo(\"https://somerepo/\")  <- CORRECT");
		setFile("build.gradle")
				.toLines(
						"plugins { id 'dev.equo.ide' }",
						"equoIde {",
						"  p2repo 'https://somerepo//'",
						"  install 'org.eclipse.swt'",
						"}");
		var doubleSlash =
				gradleRunner().withArguments("equoIde").buildAndFail().getOutput().replace("\r", "");
		Assertions.assertThat(doubleSlash)
				.contains(
						"> Must end with a single /\n"
								+ "  p2repo(\"https://somerepo//\")  <- WRONG\n"
								+ "  p2repo(\"https://somerepo/\")   <- CORRECT");
	}

	@Test
	public void equoIdeTestOnly() throws IOException {
		setFile("build.gradle")
				.toLines(
						"plugins { id 'dev.equo.ide' }",
						"equoIde {",
						"  p2repo 'https://download.eclipse.org/eclipse/updates/4.26/'",
						"  install 'org.eclipse.swt'",
						"}");
		var output =
				gradleRunner()
						.withArguments("equoIde", "-PequoTestOnly=true", "--stacktrace")
						.build()
						.getOutput();
		Assertions.assertThat(output).contains("exit code: 0");
		Assertions.assertThat(output).matches("(?s)(.*)stdout: Loaded (\\d+) bundles(.*)");
	}

	@Test
	@Disabled
	public void equoIde() throws IOException {
		setFile("build.gradle")
				.toLines(
						"plugins { id 'dev.equo.ide' }",
						"equoIde {",
						"  release '4.26'",
						"}",
						"// (placeholder so GPJ formats this nicely)");
		gradleRunner().withArguments("equoIde", "--stacktrace").forwardOutput().build();
	}
}
