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

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Thanks to Thipor Kong for his workaround for Gradle's windows problems.
 *
 * <p>https://discuss.gradle.org/t/javaexec-fails-for-long-classpaths-on-windows/15266
 */
class JavaExecWinFriendly {
	public static final String LONG_CLASSPATH_JAR_PREFIX = "long-classpath";

	/** Creates a jar with a Class-Path entry to workaround the windows classpath limitation. */
	static File toJarWithClasspath(Iterable<File> files) throws IOException {
		File jarFile = File.createTempFile(LONG_CLASSPATH_JAR_PREFIX, ".jar");
		try (ZipOutputStream zip =
				new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(jarFile)))) {
			zip.putNextEntry(new ZipEntry("META-INF/MANIFEST.MF"));
			try (PrintWriter pw =
					new PrintWriter(
							new BufferedWriter(new OutputStreamWriter(zip, StandardCharsets.UTF_8)))) {
				pw.println("Manifest-Version: 1.0");
				StringBuilder bufferClassPath = new StringBuilder("Class-Path: ");
				for (File file : files) {
					if (bufferClassPath.length() != 0) {
						bufferClassPath.append(' ');
					}
					bufferClassPath.append(file.toURI());
				}
				pw.println(
						Arrays.stream(bufferClassPath.toString().split(MATCH_CHUNKS_OF_70_CHARACTERS))
								.collect(Collectors.joining("\n ")));
			}
		}
		return jarFile;
	}

	private static final String MATCH_CHUNKS_OF_70_CHARACTERS = "(?<=\\G.{70})";
}
