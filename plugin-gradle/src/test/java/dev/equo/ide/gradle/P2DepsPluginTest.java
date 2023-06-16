/*******************************************************************************
 * Copyright (c) 2023 EquoTech, Inc. and others.
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

public class P2DepsPluginTest extends GradleHarness {
	@Test
	public void install() throws IOException {
		setFile("build.gradle")
				.toLines(
						"plugins { id 'dev.equo.p2deps' }",
						"repositories { mavenCentral() }",
						"p2deps {",
						"  into 'implementation', {",
						"    p2repo 'https://download.eclipse.org/eclipse/updates/4.26/'",
						"    install 'org.eclipse.platform.ide.categoryIU'",
						"  }",
						"  into 'buildshipImplementation', {",
						"    p2repo 'https://download.eclipse.org/eclipse/updates/4.26/'",
						"    p2repo 'https://download.eclipse.org/buildship/updates/e423/releases/3.x/3.1.6.v20220511-1359/'",
						"    install 'org.eclipse.buildship.feature.group'",
						"  }",
						"}",
						"apply plugin: 'java'",
						"sourceSets.register('buildship') {",
						"  compileClasspath += sourceSets.main.output",
						"  runtimeClasspath += sourceSets.main.output",
						"}",
						"tasks.register('BuildshipTest', JavaExec) {",
						"  main = 'pkg.BuildshipTest'",
						"  classpath = sourceSets.buildship.runtimeClasspath",
						"}");
		setFile("src/main/java/pkg/Test.java")
				.toLines(
						"",
						"package pkg;",
						"import org.eclipse.ui.ide.IDE;",
						"public class Test {",
						"  public static void main(String[] args) {",
						"    System.out.println(IDE.EDITOR_ID_ATTR);",
						"  }",
						"}");
		setFile("src/buildship/java/pkg/BuildshipTest.java")
				.toLines(
						"",
						"package pkg;",
						"import org.eclipse.buildship.core.GradleBuild;",
						"public class BuildshipTest {",
						"  public static void main(String[] args) {",
						"    Test.main(args);",
						"    System.out.println(GradleBuild.class.getName());",
						"  }",
						"}");
		String output =
				gradleRunner().withArguments("-q", "BuildshipTest").build().getOutput().replace("\r", "");
		Assertions.assertThat(output)
				.isEqualTo("org.eclipse.ui.editorID\norg.eclipse.buildship.core.GradleBuild\n");
	}
}
