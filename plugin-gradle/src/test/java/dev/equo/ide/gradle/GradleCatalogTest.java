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
import org.junit.jupiter.api.Test;
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
	public void simple(Expect expect) throws IOException {
		test("jdt()", expect.scenario("jdt"));
		test("gradleBuildship()", expect.scenario("gradleBuildship"));
	}

	@Test
	public void versionOverride(Expect expect) throws IOException {
		test("jdt('4.25')", expect.scenario("jdt-spec"));
		test("platform()\njdt('4.25')", expect.scenario("platform-neutral-jdt-spec"));
		test("platform('4.25')\njdt()", expect.scenario("platform-spec"));
		test("platform('4.25')\njdt('4.25')", expect.scenario("both-spec"));
	}
}
