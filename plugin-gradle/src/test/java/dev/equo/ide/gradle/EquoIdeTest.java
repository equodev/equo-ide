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
import au.com.origin.snapshots.junit5.SnapshotExtension;
import java.io.IOException;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith({SnapshotExtension.class})
public class EquoIdeTest extends GradleHarness {
	@Test
	public void tasks(Expect expect) throws IOException {
		setFile("build.gradle").toLines("plugins { id 'dev.equo.ide' }", "equoIde {", "}");
		run("tasks").snapshotBetween("IDE tasks", "To see all tasks", expect);
	}

	@Test
	public void p2repoArgCheck(Expect expect) throws IOException {
		setFile("build.gradle")
				.toLines(
						"plugins { id 'dev.equo.ide' }",
						"equoIde {",
						"  p2repo 'https://somerepo'",
						"  install 'org.eclipse.swt'",
						"}");
		runAndFail("equoIde")
				.snapshotBetween(
						"A problem occurred evaluating root project 'under-test'",
						"* Try:",
						expect.scenario("no-slash"));
		setFile("build.gradle")
				.toLines(
						"plugins { id 'dev.equo.ide' }",
						"equoIde {",
						"  p2repo 'https://somerepo//'",
						"  install 'org.eclipse.swt'",
						"}");
		runAndFail("equoIde")
				.snapshotBetween(
						"A problem occurred evaluating root project 'under-test'",
						"* Try:",
						expect.scenario("double-slash"));
	}

	@Test
	public void initOnly() throws IOException {
		setFile("build.gradle")
				.toLines(
						"plugins { id 'dev.equo.ide' }",
						"equoIde {",
						"  p2repo 'https://download.eclipse.org/eclipse/updates/4.26/'",
						"  install 'org.eclipse.swt'",
						"}");
		runAndAssert("equoIde", "--init-only")
				.contains("exit code: 0")
				.matches("(?s)(.*)Loaded (\\d+) bundles using Atomos(.*)");

		// useAtomos = false in buildscript
		setFile("build.gradle")
				.toLines(
						"plugins { id 'dev.equo.ide' }",
						"equoIde {",
						"  p2repo 'https://download.eclipse.org/eclipse/updates/4.26/'",
						"  install 'org.eclipse.swt'",
						"  useAtomos = false",
						"}");
		runAndAssert("equoIde", "--init-only")
				.contains("exit code: 0")
				.matches("(?s)(.*)Loaded (\\d+) bundles not using Atomos(.*)");

		// --dont-use-atomos at command line
		setFile("build.gradle")
				.toLines(
						"plugins { id 'dev.equo.ide' }",
						"equoIde {",
						"  p2repo 'https://download.eclipse.org/eclipse/updates/4.26/'",
						"  install 'org.eclipse.swt'",
						"}");
		runAndAssert("equoIde", "--init-only", "--dont-use-atomos")
				.contains("exit code: 0")
				.matches("(?s)(.*)Loaded (\\d+) bundles not using Atomos(.*)");
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
