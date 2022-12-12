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
@Disabled
public class P2MultitoolExamples extends GradleHarness {
	@Test
	public void _01(Expect expect) throws IOException {
		setFile("build.gradle")
				.toLines(
						"plugins { id 'dev.equo.ide' }",
						"equoIde {",
						"  p2repo 'https://download.eclipse.org/eclipse/updates/4.26/'",
						"}");
		run("-q", "equoList", "--all=categories").snapshot(expect);
	}

	@Test
	public void _02(Expect expect) throws IOException {
		setFile("build.gradle")
				.toLines(
						"plugins { id 'dev.equo.ide' }",
						"equoIde {",
						"  p2repo 'https://download.eclipse.org/eclipse/updates/4.26/'",
						"}");
		run("-q", "equoList", "--installed").snapshot(expect);
	}

	@Test
	public void _03(Expect expect) throws IOException {
		setFile("build.gradle")
				.toLines(
						"plugins { id 'dev.equo.ide' }",
						"equoIde {",
						"  p2repo 'https://download.eclipse.org/eclipse/updates/4.26/'",
						"  install 'org.eclipse.releng.java.languages.categoryIU'",
						"}");
		run("-q", "equoList", "--installed").snapshot(expect);
	}

	@Test
	public void _04(Expect expect) throws IOException {
		setFile("build.gradle")
				.toLines(
						"plugins { id 'dev.equo.ide' }",
						"equoIde {",
						"  p2repo 'https://download.eclipse.org/eclipse/updates/4.26/'",
						"  install 'org.eclipse.releng.java.languages.categoryIU'",
						"}");
		run("-q", "equoList", "--problems").snapshot(expect);
	}

	@Test
	public void _05(Expect expect) throws IOException {
		setFile("build.gradle")
				.toLines(
						"plugins { id 'dev.equo.ide' }",
						"equoIde {",
						"  p2repo 'https://download.eclipse.org/eclipse/updates/4.26/'",
						"  install 'org.eclipse.releng.java.languages.categoryIU'",
						"}");
		run("-q", "equoList", "--detail=org.eclipse.jdt.compiler.apt", "--stacktrace")
				.snapshot(expect.scenario("apt"));
		run("-q", "equoList", "--detail=org.eclipse.jdt.core.compiler.batch", "--stacktrace")
				.snapshot(expect.scenario("batch"));
	}

	@Test
	public void _06(Expect expect) throws IOException {
		setFile("build.gradle")
				.toLines(
						"plugins { id 'dev.equo.ide' }",
						"equoIde {",
						"  p2repo 'https://download.eclipse.org/eclipse/updates/4.26/'",
						"  install 'org.eclipse.releng.java.languages.categoryIU'",
						"}");
		run("-q", "equoList", "--raw=org.eclipse.jdt.core.compiler.batch", "--stacktrace")
				.snapshot(expect.scenario("batch"));
	}
}
