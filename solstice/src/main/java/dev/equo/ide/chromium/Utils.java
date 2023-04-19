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
package dev.equo.ide.chromium;

import dev.equo.solstice.NestedJars;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Utils {

	private static String OS = System.getProperty("os.name").toLowerCase();

	public static final String CHROMIUM_REPO = "https://dl.equo.dev/chromium-swt-ce/oss/mvn";

	public static final String CHROMIUM_ARTIFACT = "com.equo:com.equo.chromium:106.0.0";

	public static final String CHROMIUM_CEF_ARTIFACT =
			"com.equo:com.equo.chromium.cef." + getOsPlatform() + ":106.0.0";

	public static final String CHROMIUM_SOLSTICE_ARTIFACT =
			"dev.equo.ide:z-chromium-solstice:" + chromiumSolsticeVersion();

	public static boolean isMac() {
		return (OS.indexOf("mac") >= 0);
	}

	public static boolean isWindows() {
		return (OS.indexOf("win") >= 0);
	}

	private static String getArch() {
		String osArch = System.getProperty("os.arch").toLowerCase();
		if (osArch.equals("arm")) return "aarch64";
		if (osArch.equals("amd64")) return "x86_64";
		return osArch;
	}

	public static String getOsPlatform() {
		String arch = getArch();
		if (isMac()) {
			return "cocoa.macosx." + arch;
		} else if (isWindows()) {
			return "win32.win32." + arch;
		} else {
			return "gtk.linux." + arch;
		}
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

	public static String chromiumSolsticeVersion() {
		return NestedJars.chromiumSolsticeVersion();
	}
}
