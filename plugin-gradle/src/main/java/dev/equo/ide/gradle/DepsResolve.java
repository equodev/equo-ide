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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import org.gradle.plugin.devel.tasks.PluginUnderTestMetadata;

class DepsResolve {
	static final String METADATA_PATH =
			"build/pluginUnderTestMetadata/plugin-under-test-metadata.properties";

	static List<Object> resolveSolsticeAndTransitives() throws IOException {
		var implVersion = NestedBundles.solsticeVersion();
		if (!implVersion.endsWith("-SNAPSHOT")) {
			return Collections.singletonList("dev.equo.ide:solstice:" + implVersion);
		} else {
			var file = new File(METADATA_PATH);
			if (!file.exists()) {
				// the ../.. is needed to keep testkit happy at commandline vs within IDE
				file = new File("../../" + METADATA_PATH);
			}
			var props = new Properties();
			try (var input = new FileInputStream(file)) {
				props.load(input);
			}
			var cp =
					props
							.get(PluginUnderTestMetadata.IMPLEMENTATION_CLASSPATH_PROP_KEY)
							.toString()
							.split(File.pathSeparator);
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
