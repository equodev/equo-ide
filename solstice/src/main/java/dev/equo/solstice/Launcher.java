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

import com.diffplug.common.swt.os.OS;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Thanks to Thipor Kong for his workaround for Gradle's windows problems.
 *
 * <p>https://discuss.gradle.org/t/javaexec-fails-for-long-classpaths-on-windows/15266
 */
public class Launcher {
	public static int launchJavaBlocking(
			boolean blocking, String mainClass, List<File> cp, String... args)
			throws IOException, InterruptedException {
		String javaHome = System.getProperty("java.home");
		String javaBin = javaHome + File.separator + "bin" + File.separator + "java";
		String javaCmd;
		if (new File(javaBin).exists()) {
			javaCmd = javaBin;
		} else {
			javaCmd = "java";
		}

		File classpathJar = Launcher.toJarWithClasspath(cp);
		classpathJar.deleteOnExit();

		List<String> command = new ArrayList<>();
		command.add(javaCmd);
		if (OS.getRunning().isMac()) {
			command.add("-XstartOnFirstThread");
		}
		command.add("-classpath");
		command.add(classpathJar.getAbsolutePath());
		command.add(mainClass);
		command.addAll(Arrays.asList(args));

		if (blocking) {
			return launchAndInheritIO(null, command);
		} else {
			ScriptExec.script(ScriptExec.quoteAll(command)).execSeparate();
			return 0;
		}
	}

	public static int launchAndInheritIO(File cwd, List<String> args)
			throws IOException, InterruptedException {
		var builder = new ProcessBuilder(args);
		if (cwd != null) {
			builder.directory(cwd);
		}
		var process = builder.start();
		var outPumper = new StreamPumper(process.getInputStream(), System.out);
		var errPumper = new StreamPumper(process.getErrorStream(), System.err);
		int exitCode = process.waitFor();
		process.getOutputStream().flush();
		outPumper.join();
		errPumper.join();
		return exitCode;
	}

	static class StreamPumper extends Thread {
		private final InputStream in;
		private final PrintStream out;

		private StreamPumper(InputStream in, PrintStream out) {
			this.in = in;
			this.out = out;
			start();
		}

		@Override
		public void run() {
			byte[] buf = new byte[1024];
			try {
				int numRead;
				while ((numRead = in.read(buf)) != -1) {
					out.write(buf, 0, numRead);
					out.flush();
				}
			} catch (IOException e) {
				throw Unchecked.wrap(e);
			}
		}
	}

	private static final String LONG_CLASSPATH_JAR_PREFIX = "long-classpath";

	/** Creates a jar with a Class-Path entry to workaround the windows classpath limitation. */
	private static File toJarWithClasspath(List<File> files) throws IOException {
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
						String.join("\n ", bufferClassPath.toString().split(MATCH_CHUNKS_OF_70_CHARACTERS)));
			}
		}
		return jarFile;
	}

	public static List<File> copyAndSortClasspath(Iterable<File> files) {
		List<File> copy = new ArrayList<>();
		files.forEach(copy::add);
		copy.sort(
				Comparator.comparing(File::getName).reversed()); // TODO: reversed() fixes a signing problem
		return copy;
	}

	private static final String MATCH_CHUNKS_OF_70_CHARACTERS = "(?<=\\G.{70})";
}
