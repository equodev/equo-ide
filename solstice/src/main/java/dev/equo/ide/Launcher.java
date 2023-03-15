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
import java.util.function.Consumer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.annotation.Nullable;

/**
 * Thanks to Thipor Kong for his workaround for Gradle's windows problems.
 *
 * <p>https://discuss.gradle.org/t/javaexec-fails-for-long-classpaths-on-windows/15266
 */
public class Launcher {
	public static int launchJavaBlocking(
			boolean blocking,
			List<File> cp,
			List<String> vmArgs,
			String mainClass,
			@Nullable Consumer<Process> monitorProcess,
			String... args)
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
		command.addAll(vmArgs);
		command.add("-classpath");
		command.add(classpathJar.getAbsolutePath());
		command.add(mainClass);
		command.addAll(Arrays.asList(args));

		if (blocking) {
			return launchAndInheritIO(null, command, monitorProcess);
		} else {
			ScriptExec.script(ScriptExec.quoteAll(command)).execSeparate(monitorProcess);
			return 0;
		}
	}

	public static int launchAndInheritIO(File cwd, List<String> args)
			throws IOException, InterruptedException {
		return launchAndInheritIO(cwd, args, null);
	}

	public static int launchAndInheritIO(
			File cwd, List<String> args, @Nullable Consumer<Process> monitorProcess)
			throws IOException, InterruptedException {
		var builder = new ProcessBuilder(args);
		if (cwd != null) {
			builder.directory(cwd);
		}
		var process = builder.start();
		var outPumper = new StreamPumper(process, process.getInputStream(), System.out);
		var errPumper = new StreamPumper(process, process.getErrorStream(), System.err);
		if (monitorProcess != null) {
			new Thread(() -> monitorProcess.accept(process)).start();
		}
		int exitCode = process.waitFor();
		process.getOutputStream().flush();
		outPumper.join();
		errPumper.join();
		return exitCode;
	}

	static class StreamPumper extends Thread {
		private final Process process;
		private final InputStream in;
		private final PrintStream out;

		private StreamPumper(Process process, InputStream in, PrintStream out) {
			this.process = process;
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
				if (process.isAlive()) {
					// only report the exception if the process is alive
					throw new RuntimeException(e);
				}
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

	public static ArrayList<File> copyAndSortClasspath(Iterable<File> files) {
		var copy = new ArrayList<File>();
		List<File> addToEnd = new ArrayList<>();
		for (File f : files) {
			if (f.getName().startsWith("slf4j-nop")) {
				continue;
			}
			if (f.getName().startsWith("org.apache.jasper.glassfish")
					|| f.getName().startsWith("biz.aQute.bndlib")) {
				addToEnd.add(f);
				continue;
			}
			copy.add(f);
		}
		copy.sort(Comparator.comparing(File::getName).reversed());
		copy.addAll(addToEnd);
		return copy;
	}

	private static final String MATCH_CHUNKS_OF_70_CHARACTERS = "(?<=\\G.{70})";
}
