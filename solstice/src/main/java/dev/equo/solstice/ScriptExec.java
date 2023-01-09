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
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiConsumer;

/**
 * Facility for creating and executing scripts. All scripts are invisible to the user (in a gui
 * sense). Scripts can spawned as child processes or totally separate. You MUST specify the content
 * of the script to run.
 */
class ScriptExec {
	/** Creates a new ScriptExec instance. */
	public static ScriptExec script(String script) {
		return new ScriptExec(script);
	}

	private final String script;
	private Optional<File> directory = Optional.empty();

	private ScriptExec(String script) {
		this.script = Objects.requireNonNull(script);
	}

	/** Sets the directory that the script will be run in. */
	public ScriptExec directory(File directory) {
		this.directory = Optional.of(directory);
		return this;
	}

	/** Spawns the script as a separate task. */
	public void execSeparate() throws IOException, InterruptedException {
		exec(true);
	}

	/** Spawns the script as a child task. */
	public void execBlocking() throws IOException, InterruptedException {
		exec(false);
	}

	private void exec(boolean isSeparate) throws IOException, InterruptedException {
		// create the self-deleting script file
		File scriptFile = createSelfDeletingScript(script);

		// get the right arguments
		List<String> fullArgs = getPlatformCmds(scriptFile, isSeparate);

		// set the cmds
		var processBuilder = new ProcessBuilder(fullArgs);
		if (directory.isPresent()) {
			processBuilder.directory(directory.get());
		}
		int exitValue = processBuilder.start().waitFor();
		if (exitValue != EXIT_VALUE_SUCCESS) {
			throw new RuntimeException("'" + script + "' exited with " + exitValue);
		}
	}

	/** The integer value which marks that a process exited successfully. */
	private static final int EXIT_VALUE_SUCCESS = 0;

	/**
	 * Creates a .bat or .sh file which will: - cd into the correct directory - run the script -
	 * delete itself
	 */
	private static File createSelfDeletingScript(String script) throws IOException {
		String extension = OS.getRunning().isWindows() ? ".bat" : ".sh";
		String header = OS.getRunning().isWindows() ? "" : "#!/bin/sh";
		String callRobust = OS.getRunning().isWindows() ? "call " : "";

		// put the script that we're going to run in its own file
		File targetScriptFile =
				createGenericScript(
						extension,
						(file, printer) -> {
							printer.println(header);
							printer.println(script);
						});
		// create a script which will call this script then delete it and itself
		return createGenericScript(
				extension,
				(file, printer) -> {
					// add the unix header
					printer.println(header);
					// call the script that we want to run
					printer.println(callRobust + quote(targetScriptFile));
					// make it self-deleting
					if (OS.getNative().isWindows()) {
						// http://stackoverflow.com/a/20333575/1153071
						// start /b starts new application without a new window
						// (https://technet.microsoft.com/en-us/library/bb491005.aspx)
						// delete ourselves and the targetScript
						printer.println(
								"start /b \"\" cmd /c del " + quote(targetScriptFile) + " " + quote(file));
						// make 100% sure that the script exits at the end
						printer.println("exit");
					} else {
						// http://stackoverflow.com/a/8981176/1153071
						printer.println("rm " + quote(targetScriptFile) + " " + quote(file));
					}
				});
	}

	/**
	 * Creates a temporary file with the given extension. The client is responsible for making the
	 * script self-deleting.
	 */
	private static File createGenericScript(String extension, BiConsumer<File, StringPrinter> client)
			throws IOException {
		// create the file
		File file = File.createTempFile("DiffPlugScript", extension);
		if (!OS.getNative().isWindows()) {
			file.setExecutable(true);
		}

		// create the command buffer, and let the client populate it
		StringPrinter fullScript = new StringPrinter();
		client.accept(file, fullScript);

		// write the script
		return writeFlushed(file, fullScript.toString().getBytes(StandardCharsets.UTF_8));
	}

	/**
	 * Writes all of byte[] content to the given file, being sure to flush the file to the OS before
	 * returning.
	 */
	private static File writeFlushed(File file, byte[] content) throws IOException {
		try (FileOutputStream output = new FileOutputStream(file)) {
			output.write(content);
			output.flush();
			output.getFD().sync();
		}
		return file;
	}

	/** Returns the arguments needed to run the scriptFile with the given properties. */
	private static List<String> getPlatformCmds(File scriptFile, boolean isSeparate)
			throws IOException {
		if (OS.getNative().isWindows()) {
			// wscript.exe invisible.vbs script.bat
			return List.of(
					"wscript.exe",
					createInvisibleVbs(isSeparate).getAbsolutePath(),
					scriptFile.getAbsolutePath());
		} else {
			// use sh to execute
			if (isSeparate) {
				File spawningScript =
						createSelfDeletingScript("nohup " + quote(scriptFile) + " &" + "\n" + "disown");
				return List.of("/bin/bash", spawningScript.getAbsolutePath());
			} else {
				return List.of("/bin/sh", scriptFile.getAbsolutePath());
			}
		}
	}

	/** Creates a .vbs file which will execute a batch command and then delete itself. */
	private static File createInvisibleVbs(boolean isSeparate) throws IOException {
		return createGenericScript(
				".vbs",
				(file, printer) -> {
					// args are at http://ss64.com/vb/run.html
					String windowStyle = "0";
					String waitOnReturn = isSeparate ? "False" : "True";
					// open the shell
					printer.println(
							String.format(
									"CreateObject(\"Wscript.Shell\").Run \"\"\"\" & WScript.Arguments(0) & \"\"\"\", %s, %s",
									windowStyle, waitOnReturn));
					// then delete ourselves
					printer.println(
							"CreateObject(\"Scripting.FileSystemObject\").DeleteFile(\""
									+ file.getAbsolutePath()
									+ "\")");
				});
	}

	/** Returns the file's absolute path, quoted if necessary. */
	public static String quote(File file) {
		return quote(file.getAbsolutePath());
	}

	/** Returns the file's absolute path, quoted if necessary. */
	public static String quote(String arg) {
		return arg.contains(" ") ? "\"" + arg + "\"" : arg;
	}

	/** Quotes either a String or a File. */
	private static String quoteObj(Object arg) {
		if (arg instanceof String) {
			return quote((String) arg);
		} else if (arg instanceof File) {
			return quote((File) arg);
		} else {
			throw new IllegalArgumentException("Unexpected class " + arg.getClass());
		}
	}

	/** Quotes a list of String or File objects. */
	public static String quoteAll(List<?> args) {
		StringBuilder completeArgs = new StringBuilder();
		if (args.size() == 0) {
			return "";
		} else {
			completeArgs.append(quoteObj(args.get(0)));
			for (int i = 1; i < args.size(); ++i) {
				completeArgs.append(" ");
				completeArgs.append(quoteObj(args.get(i)));
			}
			return completeArgs.toString();
		}
	}

	private static class StringPrinter {
		StringBuilder builder = new StringBuilder();

		void println(String line) {
			builder.append(line);
			builder.append('\n');
		}

		public String toString() {
			return builder.toString();
		}
	}
}
