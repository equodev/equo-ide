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

public class EquoListTest extends GradleHarness {
	@Test
	public void help() throws IOException {
		setFile("build.gradle").toContent("plugins { id 'dev.equo.ide' }");
		run("-q", "help", "--task", "equoList").expectSnapshot().toMatchDisk();
	}

	@Test
	public void configCache() throws IOException {
		setFile("build.gradle")
				.toLines(
						"plugins { id 'dev.equo.ide' }", "equoIde {", "}", "repositories { mavenCentral() }");
		run("equoList", "--request", "--configuration-cache", "--warning-mode=fail")
				.assertOutput()
				.contains("Configuration cache entry discarded with 1 problem.");
	}

	@Test
	public void defaultP2() throws IOException {
		setFile("build.gradle")
				.toLines(
						"plugins { id 'dev.equo.ide' }",
						"equoIde {",
						"  p2repo 'https://download.eclipse.org/eclipse/updates/4.26/'",
						"  install 'org.eclipse.releng.java.languages.categoryIU'",
						"  install 'org.eclipse.platform.ide.categoryIU'",
						"  addFilter 'platform-neutral', {",
						"    platformNone()",
						"  }",
						"}");
		run("equoList", "--installed", "--stacktrace")
				.expectSnapshotBetween("Task :equoList", "BUILD SUCCESSFUL")
				.toMatchDisk("installed");
		run("equoList", "--problems", "--stacktrace")
				.expectSnapshotBetween("Task :equoList", "BUILD SUCCESSFUL")
				.toMatchDisk("problems");
		run("equoList", "--optional", "--stacktrace")
				.expectSnapshotBetween("Task :equoList", "BUILD SUCCESSFUL")
				.toMatchDisk("optional");
	}

	@Test
	public void installedEmpty() throws IOException {
		setFile("build.gradle")
				.toLines(
						"plugins { id 'dev.equo.ide' }",
						"equoIde {",
						"  p2repo 'https://download.eclipse.org/eclipse/updates/4.26/'",
						"}");
		run("equoList", "--installed")
				.expectSnapshotBetween("Task :equoList", "BUILD SUCCESSFUL")
				.toMatchDisk("installed");
		run("equoList", "--problems")
				.expectSnapshotBetween("Task :equoList", "BUILD SUCCESSFUL")
				.toMatchDisk("problems");
	}

	@Test
	public void installedSwt() throws IOException {
		setFile("build.gradle")
				.toLines(
						"plugins { id 'dev.equo.ide' }",
						"equoIde {",
						"  p2repo 'https://download.eclipse.org/eclipse/updates/4.26/'",
						"  install 'org.eclipse.swt'",
						"  addFilter 'platform-neutral', {",
						"    platformNone()",
						"  }",
						"}");
		run("equoList", "--installed")
				.expectSnapshotBetween("Task :equoList", "BUILD SUCCESSFUL")
				.toMatchDisk("installed");
		run("equoList", "--problems")
				.expectSnapshotBetween("Task :equoList", "BUILD SUCCESSFUL")
				.toMatchDisk("problems");
	}

	@Test
	public void installedSwtCsv() throws IOException {
		setFile("build.gradle")
				.toLines(
						"plugins { id 'dev.equo.ide' }",
						"equoIde {",
						"  p2repo 'https://download.eclipse.org/eclipse/updates/4.26/'",
						"  install 'org.eclipse.swt'",
						"  addFilter 'platform-neutral', {",
						"    platformNone()",
						"  }",
						"}");
		run("equoList", "--installed", "--format=csv")
				.expectSnapshotBetween("Task :equoList", "BUILD SUCCESSFUL")
				.toMatchDisk("installed");
		run("equoList", "--problems", "--format=csv")
				.expectSnapshotBetween("Task :equoList", "BUILD SUCCESSFUL")
				.toMatchDisk("problems");
	}

	@Test
	public void allFeatures() throws IOException {
		setFile("build.gradle")
				.toLines(
						"plugins { id 'dev.equo.ide' }",
						"equoIde {",
						"  p2repo 'https://download.eclipse.org/eclipse/updates/4.26/'",
						"  install 'org.eclipse.swt'",
						"  addFilter 'platform-neutral', {",
						"    platformNone()",
						"  }",
						"}");
		run("equoList", "--all=features")
				.expectSnapshotBetween("Task :equoList", "BUILD SUCCESSFUL")
				.toMatchDisk();
	}

	@Test
	public void allCategories() throws IOException {
		setFile("build.gradle")
				.toLines(
						"plugins { id 'dev.equo.ide' }",
						"equoIde {",
						"  p2repo 'https://download.eclipse.org/eclipse/updates/4.26/'",
						"  install 'org.eclipse.swt'",
						"  addFilter 'platform-neutral', {",
						"    platformNone()",
						"  }",
						"}");
		run("equoList", "--all=categories")
				.expectSnapshotBetween("Task :equoList", "BUILD SUCCESSFUL")
				.toMatchDisk();
	}

	@Test
	public void allJars() throws IOException {
		setFile("build.gradle")
				.toLines(
						"plugins { id 'dev.equo.ide' }",
						"equoIde {",
						"  p2repo 'https://download.eclipse.org/eclipse/updates/4.26/'",
						"  install 'org.eclipse.swt'",
						"  addFilter 'platform-neutral-no-source', {",
						"    platformNone()",
						"    excludeSuffix '.source'  // no source bundles",
						"    excludePrefix 'tooling'  // ignore internal tooling",
						"    exclude 'org.apache.sshd.osgi' // we don't want sshd",
						"  }",
						"}");
		run("equoList", "--all=jars", "--format=csv")
				.expectSnapshotBetween("Task :equoList", "BUILD SUCCESSFUL")
				.toMatchDisk();
	}

	@Test
	public void detail() throws IOException {
		setFile("build.gradle")
				.toLines(
						"plugins { id 'dev.equo.ide' }",
						"equoIde {",
						"  p2repo 'https://download.eclipse.org/eclipse/updates/4.26/'",
						"  install 'org.eclipse.jdt.annotation'",
						"  addFilter 'platform-neutral', {",
						"    platformNone()",
						"  }",
						"}");
		run("equoList", "--detail=org.eclipse.jdt.annotation")
				.expectSnapshotBetween("Task :equoList", "BUILD SUCCESSFUL")
				.toMatchDisk();
	}

	@Test
	public void raw() throws IOException {
		setFile("build.gradle")
				.toLines(
						"plugins { id 'dev.equo.ide' }",
						"equoIde {",
						"  p2repo 'https://download.eclipse.org/eclipse/updates/4.26/'",
						"  install 'org.eclipse.jdt.annotation'",
						"  addFilter 'platform-neutral', {",
						"    platformNone()",
						"  }",
						"}");
		run("equoList", "--raw=org.eclipse.jdt.annotation")
				.expectSnapshotBetween("Task :equoList", "BUILD SUCCESSFUL")
				.toMatchDisk();
	}
}
