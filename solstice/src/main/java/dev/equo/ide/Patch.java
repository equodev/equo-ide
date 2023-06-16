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
package dev.equo.ide;

import dev.equo.solstice.NestedJars;
import java.io.File;
import java.net.URL;
import java.util.List;

class Patch {
	private static final String DOT_JAR = ".jar";

	static String detectVersion(List<File> classpath, String symbolicName) {
		var prefix = symbolicName + "-";
		for (File jar : classpath) {
			var name = jar.getName();
			if (name.endsWith(DOT_JAR) && name.startsWith(prefix)) {
				return name.substring(prefix.length(), name.length() - DOT_JAR.length());
			}
		}
		return null;
	}

	static void patch(List<File> classpathSorted, File nestedJarFolder, String patchJar) {
		var overrides =
				new NestedJars() {
					@Override
					protected List<URL> listNestedJars() {
						return List.of(getClass().getResource("/" + patchJar + ".jar"));
					}
				}.extractAllNestedJars(nestedJarFolder);
		if (overrides.size() != 1) {
			throw new IllegalArgumentException("Expected exactly one element, had " + overrides);
		}
		classpathSorted.add(0, overrides.get(0).getValue());
	}
}
