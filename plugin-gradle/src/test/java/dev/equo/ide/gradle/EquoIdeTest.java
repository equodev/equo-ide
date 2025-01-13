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

import java.io.IOException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class EquoIdeTest extends GradleHarness {
	@Test
	public void tasks() throws IOException {
		setFile("build.gradle").toLines("plugins { id 'dev.equo.ide' }", "equoIde {", "}");
		run("tasks").expectSnapshotBetween("IDE tasks", "To see all tasks").toMatchDisk();
	}

	@Test
	public void configCache() throws IOException {
		setFile("build.gradle")
				.toLines(
						"plugins { id 'dev.equo.ide' }", "equoIde {", "}", "repositories { mavenCentral() }");
		run("equoIde", "--debug-classpath=names", "--configuration-cache", "--warning-mode=fail")
				.assertOutput()
				.contains("0 problems were found storing the configuration cache.");
	}

	@Test
	public void help() throws IOException {
		setFile("build.gradle").toContent("plugins { id 'dev.equo.ide' }");
		run("-q", "help", "--task", "equoIde").expectSnapshot().toMatchDisk();
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
		runAndFail("equoIde")
				.expectSnapshotBetween("A problem occurred evaluating root project 'under-test'", "* Try:")
				.toMatchDisk("no-slash");
		setFile("build.gradle")
				.toLines(
						"plugins { id 'dev.equo.ide' }",
						"equoIde {",
						"  p2repo 'https://somerepo//'",
						"  install 'org.eclipse.swt'",
						"}");
		runAndFail("equoIde")
				.expectSnapshotBetween("A problem occurred evaluating root project 'under-test'", "* Try:")
				.toMatchDisk("double-slash");
	}

	@Test
	public void initOnly() throws IOException {
		// useAtomos = false in buildscript
		setFile("build.gradle")
				.toLines(
						"plugins { id 'dev.equo.ide' }",
						"repositories { mavenCentral() }",
						"equoIde {",
						"  p2repo 'https://download.eclipse.org/eclipse/updates/4.26/'",
						"  install 'org.apache.felix.scr'",
						"  install 'org.eclipse.ui.ide.application'",
						"  useAtomos = false",
						"}");
		runAndAssert("equoIde", "--init-only", "--use-atomos=false", "--stacktrace")
				.matches("(?s)(.*)Loaded (\\d+) bundles not using Atomos(.*)");

		// command line overrides buildscript
		setFile("build.gradle")
				.toLines(
						"plugins { id 'dev.equo.ide' }",
						"repositories { mavenCentral() }",
						"equoIde {",
						"  p2repo 'https://download.eclipse.org/eclipse/updates/4.26/'",
						"  install 'org.apache.felix.scr'",
						"  install 'org.eclipse.platform'",
						"  install 'org.eclipse.ui.ide.application'",
						"  useAtomos = false",
						"}");
		runAndAssert("equoIde", "--init-only", "--use-atomos=true", "--stacktrace")
				.matches("(?s)(.*)Loaded (\\d+) bundles using Atomos(.*)");
	}

	@Test
	public void equoIdeWithDependency() throws IOException {
		setFile("build.gradle")
				.toLines(
						"plugins { id 'dev.equo.ide' }",
						"equoIde {",
						"  jdt()",
						"}",
						"tasks.register('dependsOnEquo') {",
						"  dependsOn 'equoIde'",
						"}");
		var build =
				gradleRunner()
						.withArguments("dependsOnEquo", "--stacktrace")
						.forwardOutput()
						.buildAndFail();
		Assertions.assertThat(build.getOutput())
				.contains(
						"> You must call `equoIde` directly, you cannot call a task which depends on `equoIde`.");
	}

	@Test
	public void equoIdeNoRepositories() throws IOException {
		setFile("build.gradle").toLines("plugins { id 'dev.equo.ide' }", "equoIde {", "  jdt()", "}");
		var build =
				gradleRunner().withArguments("equoIde", "--stacktrace").forwardOutput().buildAndFail();
		Assertions.assertThat(build.getOutput())
				.contains("`repositories { mavenCentral() }` or something similar to your `build.gradle`");
	}
}
