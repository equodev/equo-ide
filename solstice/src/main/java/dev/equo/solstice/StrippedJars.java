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

import dev.equo.solstice.p2.CacheLocations;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * Mechanism for stripping signatures from jars.
 *
 * <p>If a given package contains classes from multiple jars, all of those jars must have the same
 * signature. This is especially a problem with nested jars - under real OSGi, nested jars are
 * loaded from within the parent jar. But under Solstice and its {@link NestedJars} functionality,
 * you sometimes have an unsigned nested jar contributing classes to the same package as its signed
 * parent. The solution is to simply strip the signature off of all jars involved, since <a
 * href="https://quanttype.net/posts/2020-07-26-signing-jars-is-worthless.html">signing isn't very
 * helpful anyway</a>.
 *
 * <p>If you encounter the unusual situation where a jar needs to have its signature stripped due to
 * its nested jars, feel free to submit a PR to update the <code>needsStrip</code> variable.
 */
public class StrippedJars {
	private static final List<String> needsStrip =
			List.of(
					"org.eclipse.m2e.maven.indexer_1.18.1.20211011-2139.jar",
					"org.eclipse.m2e.maven.indexer_1.18.1.20211011-2139.jar__lucene-core-5.5.5.jar__fHOSNXkw66HmcZm23lqBVA--.jar");

	public static void strip(ArrayList<File> file) {
		if (!CacheLocations.strippedJars().isDirectory()) {
			CacheLocations.strippedJars().mkdirs();
		}
		file.replaceAll(
				f -> {
					if (needsStrip.contains(f.getName())) {
						File strippedJar = new File(CacheLocations.strippedJars(), f.getName());
						try {
							var strippedBytes = strip(f);
							if (!strippedJar.exists() || strippedJar.length() != strippedBytes.length) {
								Files.write(strippedJar.toPath(), strippedBytes);
							}
						} catch (IOException e) {
							throw new RuntimeException(e);
						}
						return strippedJar;
					}
					return f;
				});
	}

	private static byte[] strip(File input) throws IOException {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		try (ZipInputStream zipInput =
						new ZipInputStream(new BufferedInputStream(new FileInputStream(input)));
				ZipOutputStream zipOutput = new ZipOutputStream(output)) {
			while (true) {
				// read the next entry
				ZipEntry entry = zipInput.getNextEntry();
				if (entry == null) {
					break;
				}
				if (entry.getName().endsWith(".SF")
						|| entry.getName().endsWith(".RSA")
						|| entry.getName().endsWith(".DSA")) {
					continue;
				}
				// if it isn't being modified, just copy the file stream straight-up
				ZipEntry newEntry = new ZipEntry(entry);
				newEntry.setCompressedSize(-1);
				zipOutput.putNextEntry(newEntry);
				zipInput.transferTo(zipOutput);

				// close the entries
				zipInput.closeEntry();
				zipOutput.closeEntry();
			}
		}
		return output.toByteArray();
	}
}
