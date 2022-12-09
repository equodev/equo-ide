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
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class EquoIdeTest extends GradleHarness {
	@Test
	public void tasks() throws IOException {
		setFile("build.gradle").toLines("plugins { id 'dev.equo.ide' }", "equoIde {", "}");
		runAndAssert("tasks", "--stacktrace")
				.contains(
						"IDE tasks\n"
								+ "---------\n"
								+ "equoIde - Launches EquoIDE\n"
								+ "equoList - Lists the p2 dependencies of EquoIDE\n\n");
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
		runFailAndAssert("equoIde")
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
		runFailAndAssert("equoIde")
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
		runAndAssert("equoIde", "-PequoTestOnly=true", "--stacktrace")
				.contains("exit code: 0")
				.matches("(?s)(.*)stdout: Loaded (\\d+) bundles(.*)");
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
