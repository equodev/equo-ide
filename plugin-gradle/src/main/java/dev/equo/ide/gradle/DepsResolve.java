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

import dev.equo.solstice.NestedBundles;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import org.gradle.plugin.devel.tasks.PluginUnderTestMetadata;

class DepsResolve {
	static List<Object> resolveFiles() throws IOException {
		var solsticeJar =
				NestedBundles.class.getResource(NestedBundles.class.getSimpleName() + ".class").toString();
		if (!solsticeJar.startsWith("jar")) {
			throw new IllegalArgumentException("");
		}
		var url = new URL(solsticeJar);
		var jarConnection = (JarURLConnection) url.openConnection();
		var manifest = jarConnection.getManifest();
		var implVersion = manifest.getMainAttributes().getValue("Implementation-Version");

		if (!implVersion.endsWith("-SNAPSHOT")) {
			return Collections.singletonList("dev.equo.ide:solstice:" + implVersion);
		} else {
			var file = new File("build/pluginUnderTestMetadata/plugin-under-test-metadata.properties");
			var props = new Properties();
			try (var input = new FileInputStream(file)) {
				props.load(input);
			}
			var cp =
					props
							.get(PluginUnderTestMetadata.IMPLEMENTATION_CLASSPATH_PROP_KEY)
							.toString()
							.split(":");
			var result = new ArrayList<>();
			for (var jarOrFolder : cp) {
				if (jarOrFolder.endsWith(".jar")) {
					result.add(new File(jarOrFolder));
				}
			}
			return result;
		}
	}
}
