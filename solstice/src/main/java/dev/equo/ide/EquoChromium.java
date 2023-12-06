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

import com.diffplug.common.swt.os.OS;
import com.diffplug.common.swt.os.SwtPlatform;
import dev.equo.solstice.p2.P2Model;
import java.io.File;
import java.util.Collection;
import java.util.List;

/**
 * Utilities for setting up EquoChromium as the default browser implementation in the EquoIDE build
 * plugins.
 */
public class EquoChromium extends Catalog.PureMaven {

	private static final String EQUO_CHROMIUM_VERSION = "106.0.13";

	EquoChromium() {
		super(
				"equoChromium",
				jre11(EQUO_CHROMIUM_VERSION),
				List.of(
						"com.equo:com.equo.chromium:" + V,
						"com.equo:com.equo.chromium.cef." + SwtPlatform.getRunning() + ":" + V),
				PLATFORM);
	}

	public String mavenRepo() {
		return "https://dl.equo.dev/chromium-swt-ee/equo-gpl/mvn";
	}

	public boolean isEnabled(Collection<File> classpath) {
		return classpath.stream().anyMatch(file -> file.getName().startsWith("com.equo.chromium"));
	}

	public boolean isEnabled(P2Model model) {
		return model.getPureMaven().stream()
				.anyMatch(coord -> coord.startsWith("com.equo:com.equo.chromium:"));
	}

	public static String getUserAgent() {
		String chromiumMajor = EQUO_CHROMIUM_VERSION.split("\\.")[0];
		if (OS.getRunning().isLinux()) {
			return "Mozilla/5.0 (X11\\; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/"
					+ chromiumMajor
					+ ".0.0.0";
		} else if (OS.getRunning().isWindows()) {
			return "Mozilla/5.0 (Windows NT 10.0\\; Win64\\; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/"
					+ chromiumMajor
					+ ".0.0.0";
		} else {
			String osVersion = "10_0_0";
			String osVersionProperty = System.getProperty("os.version");
			if (osVersionProperty != null) {
				osVersion = osVersionProperty.replace(".", "_");
			}
			return "Mozilla/5.0 (Macintosh\\; Intel Mac OS X "
					+ osVersion
					+ ") AppleWebKit/537.36 (KHTML, like Gecko) Chrome/"
					+ chromiumMajor
					+ ".0.0.0";
		}
	}
}
