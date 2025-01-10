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

import static org.junit.jupiter.api.condition.JRE.JAVA_17;

import au.com.origin.snapshots.Expect;
import au.com.origin.snapshots.junit5.SnapshotExtension;
import java.io.IOException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledForJreRange;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith({SnapshotExtension.class})
public class GradleCatalogTest extends GradleHarness {
	private void test(String content, Expect expect) throws IOException {
		setFile("build.gradle")
				.toLines(
						"plugins { id 'dev.equo.ide' }",
						"equoIde {",
						content,
						"  addFilter 'platform-neutral', { platformNone() }",
						"}");
		run("equoList", "--request", "--stacktrace")
				.snapshotBetween("Task :equoList", "BUILD SUCCESSFUL", expect);
	}

	@Test
	@EnabledForJreRange(min = JAVA_17)
	public void simple(Expect expect) throws IOException {
		test("jdt('4.27')", expect.scenario("jdt"));
		test("platform('4.27')\ngradleBuildship()", expect.scenario("gradleBuildship"));
	}

	@Test
	public void versionOverride(Expect expect) throws IOException {
		test("jdt('4.25')", expect.scenario("jdt-spec"));
		test("platform()\njdt('4.25')", expect.scenario("platform-neutral-jdt-spec"));
		test("platform('4.25')\njdt()", expect.scenario("platform-spec"));
		test("platform('4.25')\njdt('4.25')", expect.scenario("both-spec"));
	}

	@Test
	public void wrongOrder(Expect expect) throws IOException {
		setFile("build.gradle")
				.toLines(
						"plugins { id 'dev.equo.ide' }",
						"equoIde {",
						"  jdt()",
						"  platform()",
						"  addFilter 'platform-neutral', { platformNone() }",
						"}");
		runAndFail("equoList", "--request", "--stacktrace")
				.snapshotBetween(
						"A problem occurred evaluating root project 'under-test'.", "* Try:", expect);
	}
}
