/*******************************************************************************
 * Copyright (c) 2022-2023 EquoTech, Inc. and others.
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
public class EquoListTest extends GradleHarness {
	@Test
	public void help(Expect expect) throws IOException {
		setFile("build.gradle").toContent("plugins { id 'dev.equo.ide' }");
		run("-q", "help", "--task", "equoList").snapshot(expect);
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
	public void defaultP2(Expect expect) throws IOException {
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
				.snapshotBetween("Task :equoList", "BUILD SUCCESSFUL", expect.scenario("installed"));
		run("equoList", "--problems", "--stacktrace")
				.snapshotBetween("Task :equoList", "BUILD SUCCESSFUL", expect.scenario("problems"));
		run("equoList", "--optional", "--stacktrace")
				.snapshotBetween("Task :equoList", "BUILD SUCCESSFUL", expect.scenario("optional"));
	}

	@Test
	public void installedEmpty(Expect expect) throws IOException {
		setFile("build.gradle")
				.toLines(
						"plugins { id 'dev.equo.ide' }",
						"equoIde {",
						"  p2repo 'https://download.eclipse.org/eclipse/updates/4.26/'",
						"}");
		run("equoList", "--installed")
				.snapshotBetween("Task :equoList", "BUILD SUCCESSFUL", expect.scenario("installed"));
		run("equoList", "--problems")
				.snapshotBetween("Task :equoList", "BUILD SUCCESSFUL", expect.scenario("problems"));
	}

	@Test
	public void installedSwt(Expect expect) throws IOException {
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
				.snapshotBetween("Task :equoList", "BUILD SUCCESSFUL", expect.scenario("installed"));
		run("equoList", "--problems")
				.snapshotBetween("Task :equoList", "BUILD SUCCESSFUL", expect.scenario("problems"));
	}

	@Test
	public void installedSwtCsv(Expect expect) throws IOException {
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
				.snapshotBetween("Task :equoList", "BUILD SUCCESSFUL", expect.scenario("installed"));
		run("equoList", "--problems", "--format=csv")
				.snapshotBetween("Task :equoList", "BUILD SUCCESSFUL", expect.scenario("problems"));
	}

	@Test
	public void allFeatures(Expect expect) throws IOException {
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
		run("equoList", "--all=features").snapshotBetween("Task :equoList", "BUILD SUCCESSFUL", expect);
	}

	@Test
	public void allCategories(Expect expect) throws IOException {
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
				.snapshotBetween("Task :equoList", "BUILD SUCCESSFUL", expect);
	}

	@Test
	public void allJars(Expect expect) throws IOException {
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
				.snapshotBetween("Task :equoList", "BUILD SUCCESSFUL", expect);
	}

	@Test
	public void detail(Expect expect) throws IOException {
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
				.snapshotBetween("Task :equoList", "BUILD SUCCESSFUL", expect);
	}

	@Test
	public void raw(Expect expect) throws IOException {
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
				.snapshotBetween("Task :equoList", "BUILD SUCCESSFUL", expect);
	}
}
