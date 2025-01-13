/*******************************************************************************
 * Copyright (c) 2022-2025 EquoTech, Inc. and others.
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
package dev.equo.solstice.p2;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;

class LockFile implements AutoCloseable {
	final FileOutputStream lock;
	final File lockFile;
	private static final int WAIT_FOR_BUSY = 5_000;
	private static final int WAIT_FOR_BUSY_CI = 50_000;
	private static final Object LOCK_MONITOR = new Object();
	private static volatile long currentLockingPid = -1;

	LockFile(File dir) throws IOException {
		FileMisc.mkdirs(dir);
		lockFile = new File(dir, ".lock").getCanonicalFile();

		synchronized (LOCK_MONITOR) {
			while (true) {
				if (lockFile.exists()) {
					try {
						long pid = Long.parseLong(Files.readString(lockFile.toPath()).trim());
						if (pid == ProcessHandle.current().pid()) {
							// If this process already holds the lock, wait
							LOCK_MONITOR.wait();
							continue;
						} else {
							// If another process holds the lock, use timeout-based retry
							int timeout =
									System.getProperty("lockFileGenerousTimeout") != null
											? WAIT_FOR_BUSY_CI
											: WAIT_FOR_BUSY;
							FileMisc.retry(
									lockFile,
									f -> {
										if (f.exists()) {
											throw new IllegalStateException(
													"P2 operation already in progress, close other clients or delete stale lockfile at "
															+ lockFile.getAbsolutePath());
										}
									},
									timeout);
						}
					} catch (InterruptedException e) {
						Thread.currentThread().interrupt();
						throw new IOException("Interrupted while waiting for lock", e);
					}
				}

				// Create the lock file
				lock = new FileOutputStream(lockFile);
				long currentPid = ProcessHandle.current().pid();
				lock.write(Long.toString(currentPid).getBytes());
				lock.flush();
				currentLockingPid = currentPid;
				break;
			}
		}
	}

	@Override
	public void close() throws IOException {
		synchronized (LOCK_MONITOR) {
			lock.close();
			FileMisc.delete(lockFile);
			currentLockingPid = -1;
			LOCK_MONITOR.notifyAll();
		}
	}
}
