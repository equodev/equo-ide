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

import dev.equo.solstice.p2.CacheLocations;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;

public class RemoveJarSigner {

	public static File removeJarSigner(File file) throws IOException {
		createCacheFolderFolderIfNotExists();

		String unsignedJarName = file.getName().replaceAll("\\.jar$", "") + "-unsigned.jar";
		File unsignedFile = new File(CacheLocations.unsignedJars(), unsignedJarName);
		if (unsignedFile.exists()) {
			return unsignedFile;
		}

		try (JarFile jar = new JarFile(file);
				JarOutputStream jos =
						new JarOutputStream(new FileOutputStream(unsignedFile), jar.getManifest())) {
			Manifest manifest = jar.getManifest();
			Attributes attrs = manifest.getMainAttributes();
			attrs.remove(new Attributes.Name("Signature-Version"));
			attrs.remove(new Attributes.Name("Created-By"));
			attrs.remove(new Attributes.Name("SHA1-Digest-Manifest"));
			attrs.remove(new Attributes.Name("SHA1-Digest"));

			Enumeration<JarEntry> entries = jar.entries();
			while (entries.hasMoreElements()) {
				JarEntry entry = entries.nextElement();
				if (!entry.getName().startsWith("META-INF/")) {
					try (InputStream is = jar.getInputStream(entry)) {
						jos.putNextEntry(new JarEntry(entry.getName()));
						is.transferTo(jos);
						jos.closeEntry();
					}
				}
			}
		}
		return unsignedFile;
	}

	private static void createCacheFolderFolderIfNotExists() {
		File cacheFolder = CacheLocations.unsignedJars();
		if (!cacheFolder.exists()) {
			cacheFolder.mkdirs();
		}
	}
}
