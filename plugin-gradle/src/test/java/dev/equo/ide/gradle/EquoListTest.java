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

import java.io.IOException;
import org.junit.jupiter.api.Test;

public class EquoListTest extends GradleHarness {
	@Test
	public void p2repoArgCheck() throws IOException {
		setFile("build.gradle")
				.toLines(
						"plugins { id 'dev.equo.ide' }",
						"equoIde {",
						"  p2repo 'https://download.eclipse.org/eclipse/updates/4.26/'",
						"  install 'org.eclipse.swt'",
						"  filter {",
						"    setPlatform(null)",
						"  }",
						"}");
		runAndAssert("equoList")
				.contains(
						"+-------------------------------------------------------------------+---------------+\n"
								+ "| maven coordinate / p2 id                                          | repo          |\n"
								+ "+-------------------------------------------------------------------+---------------+\n"
								+ "| org.eclipse.platform:org.eclipse.swt.cocoa.macosx.aarch64:3.122.0 | mavenCentral? |\n"
								+ "| org.eclipse.platform:org.eclipse.swt.cocoa.macosx.x86_64:3.122.0  | mavenCentral? |\n"
								+ "| org.eclipse.platform:org.eclipse.swt.gtk.linux.aarch64:3.122.0    | mavenCentral? |\n"
								+ "| org.eclipse.platform:org.eclipse.swt.gtk.linux.ppc64le:3.122.0    | mavenCentral? |\n"
								+ "| org.eclipse.platform:org.eclipse.swt.gtk.linux.x86_64:3.122.0     | mavenCentral? |\n"
								+ "| org.eclipse.platform:org.eclipse.swt.win32.win32.x86_64:3.122.0   | mavenCentral? |\n"
								+ "| org.eclipse.platform:org.eclipse.swt:3.122.0                      | mavenCentral? |\n"
								+ "+-------------------------------------------------------------------+---------------+\n");
	}
}
