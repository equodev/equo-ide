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
package dev.equo.ide;

import com.diffplug.common.swt.os.SwtPlatform;
import dev.equo.ide.chromium.RemoveJarSigner;
import dev.equo.solstice.NestedJars;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Utilities for setting up EquoChromium as the default browser implementation in the EquoIDE build
 * plugins.
 */
public class EquoChromium {
	public static String mavenRepo() {
		return "https://dl.equo.dev/chromium-swt-ce/oss/mvn";
	}

	public static List<String> mavenCoordinates() {
		return Arrays.asList(
				"com.equo:com.equo.chromium:106.0.0",
				"com.equo:com.equo.chromium.cef." + SwtPlatform.getRunning() + ":106.0.0",
				"dev.equo.ide:z-chromium-solstice:" + NestedJars.chromiumSolsticeVersion());
	}

	public static void removeSwtSigner(ArrayList<File> files) {
		for (File file : files) {
			if (file.toString().contains("org.eclipse.swt.")) {
				try {
					File unsignedJarFile = RemoveJarSigner.removeJarSigner(file);
					files.remove(file);
					files.add(unsignedJarFile);
					break;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
