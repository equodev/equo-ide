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
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.BiConsumer;

/**
 * Shelling out to a process is harder than it ought to be in Java. If you don't read stdout and
 * stderr on their own threads, you risk deadlock on a clogged buffer.
 *
 * <p>ProcessRunner allocates two threads specifically for the purpose of flushing stdout and stderr
 * to buffers. These threads will remain alive until the ProcessRunner is closed, so it is
 * especially useful for repeated calls to an external process.
 */
class ProcessRunner implements AutoCloseable {
	private final ExecutorService threadStdOut = Executors.newSingleThreadExecutor();
	private final ExecutorService threadStdErr = Executors.newSingleThreadExecutor();
	private final ByteArrayOutputStream bufStdOut = new ByteArrayOutputStream();
	private final ByteArrayOutputStream bufStdErr = new ByteArrayOutputStream();
	private File cwd;

	public ProcessRunner() {
		this(null);
	}

	public ProcessRunner(File cwd) {
		this.cwd = cwd;
	}

	/** Executes the given shell command (using {@code cmd} on windows and {@code sh} on unix). */
	public Result shell(String cmd) throws IOException, InterruptedException {
		return shellWinUnix(cmd, cmd);
	}

	/** Executes the given shell command (using {@code cmd} on windows and {@code sh} on unix). */
	public Result shellWinUnix(String cmdWin, String cmdUnix)
			throws IOException, InterruptedException {
		List<String> args;
		if (OS.getNative().isWindows()) {
			args = Arrays.asList("cmd", "/c", cmdWin);
		} else {
			args = Arrays.asList("sh", "-c", cmdUnix);
		}
		return exec(args);
	}

	/** Creates a process with the given arguments. */
	public Result exec(String... args) throws IOException, InterruptedException {
		return exec(Arrays.asList(args));
	}

	/**
	 * Creates a process with the given arguments, the given byte array is written to stdin
	 * immediately.
	 */
	public Result exec(byte[] stdin, String... args) throws IOException, InterruptedException {
		return exec(stdin, Arrays.asList(args));
	}

	/** Creates a process with the given arguments. */
	public Result exec(List<String> args) throws IOException, InterruptedException {
		return exec(new byte[0], args);
	}

	/**
	 * Creates a process with the given arguments, the given byte array is written to stdin
	 * immediately.
	 */
	public Result exec(byte[] stdin, List<String> args) throws IOException, InterruptedException {
		var builder = new ProcessBuilder(args);
		if (cwd != null) {
			builder.directory(cwd);
		}
		var process = builder.start();
		Future<byte[]> outputFut =
				threadStdOut.submit(() -> drainToBytes(process.getInputStream(), bufStdOut));
		Future<byte[]> errorFut =
				threadStdErr.submit(() -> drainToBytes(process.getErrorStream(), bufStdErr));
		// write stdin
		process.getOutputStream().write(stdin);
		process.getOutputStream().close();
		// wait for the process to finish
		int exitCode = process.waitFor();
		try {
			// collect the output
			return new Result(args, exitCode, outputFut.get(), errorFut.get());
		} catch (ExecutionException e) {
			throw new RuntimeException(e);
		}
	}

	private static void drain(InputStream input, OutputStream output) throws IOException {
		byte[] buf = new byte[1024];
		int numRead;
		while ((numRead = input.read(buf)) != -1) {
			output.write(buf, 0, numRead);
		}
	}

	private static byte[] drainToBytes(InputStream input, ByteArrayOutputStream buffer)
			throws IOException {
		buffer.reset();
		drain(input, buffer);
		return buffer.toByteArray();
	}

	@Override
	public void close() {
		threadStdOut.shutdown();
		threadStdErr.shutdown();
	}

	public static class Result {
		private final List<String> args;
		private final int exitCode;
		private final byte[] stdOut, stdErr;

		public Result(List<String> args, int exitCode, byte[] stdOut, byte[] stdErr) {
			this.args = args;
			this.exitCode = exitCode;
			this.stdOut = stdOut;
			this.stdErr = stdErr;
		}

		public List<String> args() {
			return args;
		}

		public int exitCode() {
			return exitCode;
		}

		public byte[] stdOut() {
			return stdOut;
		}

		public byte[] stdErr() {
			return stdErr;
		}

		/** Returns true if the exit code was not zero. */
		public boolean exitNotZero() {
			return exitCode != 0;
		}

		/**
		 * Asserts that the exit code was zero, and if so, returns the content of stdout encoded with
		 * the given charset.
		 *
		 * <p>If the exit code was not zero, throws an exception with useful debugging information.
		 */
		public String assertExitZero(Charset charset) {
			if (exitCode == 0) {
				return new String(stdOut, charset);
			} else {
				throw new RuntimeException(toString());
			}
		}

		@Override
		public String toString() {
			StringBuilder builder = new StringBuilder();
			builder.append("> arguments: " + args + "\n");
			builder.append("> exit code: " + exitCode + "\n");
			BiConsumer<String, byte[]> perStream =
					(name, content) -> {
						String string = new String(content, Charset.defaultCharset()).trim();
						if (string.isEmpty()) {
							builder.append("> " + name + ": (empty)\n");
						} else {
							String[] lines = string.replace("\r", "").split("\n");
							if (lines.length == 1) {
								builder.append("> " + name + ": " + lines[0] + "\n");
							} else {
								builder.append("> " + name + ": (below)\n");
								for (String line : lines) {
									builder.append("> ");
									builder.append(line);
									builder.append('\n');
								}
							}
						}
					};
			perStream.accept("   stdout", stdOut);
			perStream.accept("   stderr", stdErr);
			return builder.toString();
		}
	}
}
