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

import com.diffplug.selfie.Selfie;
import com.diffplug.selfie.StringSelfie;
import java.io.IOException;
import org.junit.jupiter.api.Test;

public class GradleCatalogTest extends GradleHarness {
	private StringSelfie test(String content) throws IOException {
		setFile("build.gradle")
				.toLines(
						"plugins { id 'dev.equo.ide' }",
						"equoIde {",
						content,
						"  addFilter 'platform-neutral', { platformNone() }",
						"}");
		return run("equoList", "--request", "--stacktrace")
				.expectSnapshotBetween("Task :equoList", "BUILD SUCCESSFUL");
	}

	@Test
	public void simple() throws IOException {
		test("jdt('4.27')").toMatchDisk("jdt");
		if (Runtime.version().feature() > 17) {
			test("platform('4.27')\ngradleBuildship()").toMatchDisk("gradleBuildship");
		} else {
			Selfie.preserveSelfiesOnDisk("gradleBuildship");
		}
	}

	@Test
	public void versionOverride() throws IOException {
		test("jdt('4.25')").toMatchDisk("jdt-spec");
		test("platform()\njdt('4.25')").toMatchDisk("platform-neutral-jdt-spec");
		test("platform('4.25')\njdt()").toMatchDisk("platform-spec");
		test("platform('4.25')\njdt('4.25')").toMatchDisk("both-spec");
	}

	@Test
	public void wrongOrder() throws IOException {
		setFile("build.gradle")
				.toLines(
						"plugins { id 'dev.equo.ide' }",
						"equoIde {",
						"  jdt()",
						"  platform()",
						"  addFilter 'platform-neutral', { platformNone() }",
						"}");
		runAndFail("equoList", "--request", "--stacktrace")
				.expectSnapshotBetween("A problem occurred evaluating root project 'under-test'.", "* Try:")
				.toMatchDisk();
	}
}
