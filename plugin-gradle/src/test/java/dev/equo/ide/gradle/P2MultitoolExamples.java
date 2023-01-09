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
public class P2MultitoolExamples extends GradleHarness {
	@Test
	public void _01_minimal_allCategories(Expect expect) throws IOException {
		setFile("build.gradle")
				.toLines(
						"plugins { id 'dev.equo.ide' }",
						"equoIde {",
						"  p2repo 'https://download.eclipse.org/eclipse/updates/4.26/'",
						"  addFilter 'platform-neutral', {",
						"    platformNone()",
						"  }",
						"}");
		run("-q", "equoList", "--all=categories").snapshot(expect);
	}

	@Test
	public void _02_minimal_installed_empty(Expect expect) throws IOException {
		setFile("build.gradle")
				.toLines(
						"plugins { id 'dev.equo.ide' }",
						"equoIde {",
						"  p2repo 'https://download.eclipse.org/eclipse/updates/4.26/'",
						"  addFilter 'platform-neutral', {",
						"    platformNone()",
						"  }",
						"}");
		run("-q", "equoList", "--installed").snapshot(expect);
	}

	@Test
	public void _02_minimal_installed(Expect expect) throws IOException {
		setFile("build.gradle")
				.toLines(
						"plugins { id 'dev.equo.ide' }",
						"equoIde {",
						"  p2repo 'https://download.eclipse.org/eclipse/updates/4.26/'",
						"  addFilter 'platform-neutral', {",
						"    platformNone()",
						"  }",
						"  install 'org.eclipse.platform.ide.categoryIU'",
						"}");
		run("-q", "equoList", "--installed").snapshot(expect);
	}

	@Test
	public void _03_corrosion_allCategories(Expect expect) throws IOException {
		setFile("build.gradle")
				.toLines(
						"plugins { id 'dev.equo.ide' }",
						"equoIde {",
						"  p2repo 'https://download.eclipse.org/eclipse/updates/4.26/'",
						"  addFilter 'platform-neutral', {",
						"    platformNone()",
						"  }",
						"  install 'org.eclipse.platform.ide.categoryIU'",
						"",
						"  p2repo 'https://download.eclipse.org/corrosion/releases/1.2.4/'",
						"}");
		run("-q", "equoList", "--all=categories").snapshot(expect);
	}

	@Test
	public void _04_corrosion_installed(Expect expect) throws IOException {
		setFile("build.gradle")
				.toLines(
						"plugins { id 'dev.equo.ide' }",
						"equoIde {",
						"  p2repo 'https://download.eclipse.org/eclipse/updates/4.26/'",
						"  addFilter 'platform-neutral', {",
						"    platformNone()",
						"  }",
						"  install 'org.eclipse.platform.ide.categoryIU'",
						"",
						"  p2repo 'https://download.eclipse.org/corrosion/releases/1.2.4/'",
						"  install '202206282034.org.eclipse.corrosion.category'",
						"}");
		run("-q", "equoList", "--installed").snapshot(expect);
	}

	@Test
	public void _05_corrosion_problems(Expect expect) throws IOException {
		setFile("build.gradle")
				.toLines(
						"plugins { id 'dev.equo.ide' }",
						"equoIde {",
						"  p2repo 'https://download.eclipse.org/eclipse/updates/4.26/'",
						"  addFilter 'platform-neutral', {",
						"    platformNone()",
						"  }",
						"  install 'org.eclipse.platform.ide.categoryIU'",
						"",
						"  p2repo 'https://download.eclipse.org/corrosion/releases/1.2.4/'",
						"  install '202206282034.org.eclipse.corrosion.category'",
						"}");
		run("-q", "equoList", "--problems").snapshot(expect);
	}

	@Test
	public void _06_corrosion_cdt_installed(Expect expect) throws IOException {
		setFile("build.gradle")
				.toLines(
						"plugins { id 'dev.equo.ide' }",
						"equoIde {",
						"  p2repo 'https://download.eclipse.org/eclipse/updates/4.26/'",
						"  addFilter 'platform-neutral', {",
						"    platformNone()",
						"  }",
						"  install 'org.eclipse.platform.ide.categoryIU'",
						"  // corrosion",
						"  p2repo 'https://download.eclipse.org/corrosion/releases/1.2.4/'",
						"  install '202206282034.org.eclipse.corrosion.category'",
						"  // cdt transitives for corrosion",
						"  p2repo 'https://download.eclipse.org/tools/cdt/releases/11.0/cdt-11.0.0/'",
						"}");
		run("-q", "equoList", "--installed").snapshot(expect);
	}

	@Test
	public void _05(Expect expect) throws IOException {
		setFile("build.gradle")
				.toLines(
						"plugins { id 'dev.equo.ide' }",
						"equoIde {",
						"  p2repo 'https://download.eclipse.org/eclipse/updates/4.26/'",
						"  addFilter 'platform-neutral', {",
						"    platformNone()",
						"  }",
						"  install 'org.eclipse.platform.ide.categoryIU'",
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
						"  addFilter 'platform-neutral', {",
						"    platformNone()",
						"  }",
						"  install 'org.eclipse.platform.ide.categoryIU'",
						"}");
		run("-q", "equoList", "--raw=org.eclipse.jdt.core.compiler.batch", "--stacktrace")
				.snapshot(expect.scenario("batch"));
	}
}
