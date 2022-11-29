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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.instrument.Instrumentation;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.jar.JarFile;
import org.eclipse.osgi.service.datalocation.Location;
import org.osgi.framework.BundleContext;

public class SolsticeClassPathAgent {
	private static Instrumentation instrumentation;

	public static void premain(String agentArgs, Instrumentation inst) {
		instrumentation = inst;
	}

	public static void agentmain(String agentArgs, Instrumentation inst) {
		instrumentation = inst;
	}

	static void extract(BundleContext context, URL entry) {
		if (instrumentation == null) {
			throw new IllegalStateException(
					"Expected SolsticeClassPathAgent to be setup by Agent-Class entry in solstice.jar MANIFEST.MF");
		}
		File jarToAdd;
		try (InputStream toRead = entry.openStream()) {
			var content = toRead.readAllBytes();

			var md5 = MessageDigest.getInstance("MD5");
			md5.update(content);

			var jarPath = entry.getPath();
			var lastSep = Math.max(jarPath.lastIndexOf('!'), jarPath.lastIndexOf('/'));
			var jarSimpleName = jarPath.substring(lastSep + 1);

			var filename = bytesToHex(md5.digest()) + "_" + jarSimpleName;

			var installDir = ShimLocation.get(context, Location.INSTALL_AREA_TYPE);
			var nestedJars = new File(installDir, "nested-jars");
			if (!nestedJars.exists()) {
				nestedJars.mkdirs();
			}

			jarToAdd = new File(nestedJars, filename);
			if (!jarToAdd.exists() || jarToAdd.length() != content.length) {
				try (var output = new FileOutputStream(jarToAdd)) {
					output.write(content);
				}
			}

			instrumentation.appendToSystemClassLoaderSearch(new JarFile(jarToAdd));
		} catch (IOException | NoSuchAlgorithmException e) {
			throw Unchecked.wrap(e);
		}
	}

	private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();

	private static String bytesToHex(byte[] bytes) {
		char[] hexChars = new char[bytes.length * 2];
		for (int j = 0; j < bytes.length; j++) {
			int v = bytes[j] & 0xFF;
			hexChars[j * 2] = HEX_ARRAY[v >>> 4];
			hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
		}
		return new String(hexChars);
	}
}
