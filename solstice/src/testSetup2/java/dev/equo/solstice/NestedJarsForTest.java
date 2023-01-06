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
package dev.equo.solstice;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class NestedJarsForTest {
	/**
	 * This creates the File `nestedJarsForTest` which is a list of extracted jars which are needed
	 * for the test task to run. This needs to be run if the jars in testSetupImplementation change.
	 */
	public static void main(String[] args) throws IOException {
		var nestedJarFolder = new SolsticeInit(BuildPluginIdeMain.defaultDir()).nestedJarFolder();
		var nestedJars = NestedJars.onClassPath().extractAllNestedJars(nestedJarFolder);
		var content = new StringBuilder();
		var base = nestedJarFolder.getParentFile().getParentFile().getParentFile().getAbsolutePath();
		for (var nestedJar : nestedJars) {
			content.append(
					nestedJar.getValue().getAbsolutePath().substring(base.length() + 1).replace('\\', '/'));
			content.append('\n');
		}
		Files.write(
				Paths.get("nestedJarsForTest"), content.toString().getBytes(StandardCharsets.UTF_8));
	}
}
