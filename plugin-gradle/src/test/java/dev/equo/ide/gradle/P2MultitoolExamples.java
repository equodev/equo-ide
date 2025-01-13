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
import org.junit.jupiter.api.Test;

public class P2MultitoolExamples extends GradleHarness {
	@Test
	public void _01_minimal_allCategories() throws IOException {
		setFile("build.gradle")
				.toLines(
						"plugins { id 'dev.equo.ide' }",
						"equoIde {",
						"  p2repo 'https://download.eclipse.org/eclipse/updates/4.26/'",
						"  addFilter 'platform-neutral', {",
						"    platformNone()",
						"  }",
						"}");
		run("-q", "equoList", "--all=categories").expectSnapshot().toMatchDisk();
	}

	@Test
	public void _02_minimal_installed_empty() throws IOException {
		setFile("build.gradle")
				.toLines(
						"plugins { id 'dev.equo.ide' }",
						"equoIde {",
						"  p2repo 'https://download.eclipse.org/eclipse/updates/4.26/'",
						"  addFilter 'platform-neutral', {",
						"    platformNone()",
						"  }",
						"}");
		run("-q", "equoList", "--installed").expectSnapshot().toMatchDisk();
	}

	@Test
	public void _02_minimal_installed() throws IOException {
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
		run("-q", "equoList", "--installed").expectSnapshot().toMatchDisk();
	}

	@Test
	public void _03_corrosion_allCategories() throws IOException {
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
		run("-q", "equoList", "--all=categories").expectSnapshot().toMatchDisk();
	}

	@Test
	public void _04_corrosion_installed() throws IOException {
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
		run("-q", "equoList", "--installed").expectSnapshot().toMatchDisk();
	}

	@Test
	public void _05_corrosion_problems() throws IOException {
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
		run("-q", "equoList", "--problems").expectSnapshot().toMatchDisk();
	}

	@Test
	public void _06_corrosion_cdt_installed() throws IOException {
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
		run("-q", "equoList", "--installed").expectSnapshot().toMatchDisk();
	}

	@Test
	public void _05() throws IOException {
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
				.expectSnapshot()
				.toMatchDisk("apt");
		run("-q", "equoList", "--detail=org.eclipse.jdt.core.compiler.batch", "--stacktrace")
				.expectSnapshot()
				.toMatchDisk("batch");
	}

	@Test
	public void _06() throws IOException {
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
				.expectSnapshot()
				.toMatchDisk("batch");
	}
}
