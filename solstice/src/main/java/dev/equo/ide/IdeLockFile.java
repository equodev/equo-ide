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

import dev.equo.solstice.SerializableMisc;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.annotation.Nullable;

public class IdeLockFile {
	private File workspaceDir;

	private IdeLockFile(File workspaceDir) {
		this.workspaceDir = workspaceDir;
	}

	public static IdeLockFile forWorkspaceDir(File workspaceDir) {
		return new IdeLockFile(workspaceDir);
	}

	/////////////////////
	// classpath stuff //
	/////////////////////
	private static final String CLASSPATH_FILENAME = "classpath";

	void writeClasspath(@Nullable ArrayList<File> files) {
		if (files == null) {
			FileMisc.delete(new File(workspaceDir, CLASSPATH_FILENAME));
		} else {
			SerializableMisc.toFile(files, new File(workspaceDir, CLASSPATH_FILENAME));
		}
	}

	public boolean hasClasspath() {
		return new File(workspaceDir, CLASSPATH_FILENAME).exists();
	}

	public ArrayList<File> readClasspath() {
		return SerializableMisc.fromFile(ArrayList.class, new File(workspaceDir, CLASSPATH_FILENAME));
	}

	///////////////
	// PID stuff //
	///////////////
	private static final String PID_FILENAME = "pid";
	private static final long NO_TOKEN_FILE = -1L;

	void savePid() {
		writePidToken(ProcessHandle.current().pid());
	}

	private void writePidToken(long pid) {
		if (pid == NO_TOKEN_FILE) {
			FileMisc.delete(new File(workspaceDir, PID_FILENAME));
		} else {
			FileMisc.writeToken(workspaceDir, PID_FILENAME, Long.toString(pid));
		}
	}

	long readPidToken() {
		return FileMisc.readToken(workspaceDir, PID_FILENAME)
				.map(str -> str.isEmpty() ? NO_TOKEN_FILE : Long.parseLong(str))
				.orElse(NO_TOKEN_FILE);
	}

	public @Nullable ProcessHandle ideAlreadyRunning() {
		long running = readPidToken();
		if (running == NO_TOKEN_FILE) {
			return null;
		}
		var alreadyRunning =
				ProcessHandle.allProcesses()
						.filter(processHandle -> processHandle.pid() == running)
						.findAny()
						.orElse(null);
		if (alreadyRunning == null) {
			writePidToken(NO_TOKEN_FILE);
			return null;
		}
		return alreadyRunning;
	}

	public static boolean alreadyRunningAndUserRequestsAbort(ProcessHandle running)
			throws IOException, InterruptedException {
		if (running == null) {
			return false;
		}
		System.out.println("There is already an IDE running with PID " + running.pid());
		System.out.println("Shut it down yourself or press");
		System.out.println("  (k + enter) to kill it");
		System.out.println("  (a + enter) to abort");
		while (running.isAlive()) {
			Thread.sleep(10);
			if (System.in.available() > 0) {
				char c = Character.toLowerCase((char) System.in.read());
				if (c == 'k') {
					System.out.println();
					System.out.println("Attempting to kill " + running.pid() + "...");
					running.destroyForcibly();
				} else if (c == 'a') {
					return true;
				}
			}
		}
		System.out.println("The duplicate IDE has shut down. Starting a new one...");
		return false;
	}
}
