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

import java.io.File;
import javax.annotation.Nullable;

public class IdeLockFile {
	private File workspaceDir;

	private IdeLockFile(File workspaceDir) {
		this.workspaceDir = workspaceDir;
	}

	public static IdeLockFile forWorkspaceDir(File workspaceDir) {
		return new IdeLockFile(workspaceDir);
	}

	private static final String TOKEN_FILENAME = "pid";
	private static final long NO_TOKEN_FILE = -1L;

	public void savePid() {
		writeToken(ProcessHandle.current().pid());
	}

	private void writeToken(long pid) {
		if (pid == NO_TOKEN_FILE) {
			FileMisc.delete(new File(workspaceDir, TOKEN_FILENAME));
		} else {
			FileMisc.writeToken(workspaceDir, TOKEN_FILENAME, Long.toString(pid));
		}
	}

	private long readToken() {
		return FileMisc.readToken(workspaceDir, TOKEN_FILENAME)
				.map(Long::parseLong)
				.orElse(NO_TOKEN_FILE);
	}

	public @Nullable ProcessHandle ideAlreadyRunning() {
		long running = readToken();
		if (running == NO_TOKEN_FILE) {
			return null;
		}
		var alreadyRunning =
				ProcessHandle.allProcesses()
						.filter(processHandle -> processHandle.pid() == running)
						.findAny()
						.orElse(null);
		if (alreadyRunning == null) {
			writeToken(NO_TOKEN_FILE);
			return null;
		}
		return alreadyRunning;
	}
}
