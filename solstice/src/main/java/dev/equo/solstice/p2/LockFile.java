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
package dev.equo.solstice.p2;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

class LockFile implements AutoCloseable {
	final FileOutputStream lock;
	final File lockFile;

	LockFile(File dir) throws IOException {
		FileMisc.mkdirs(dir);
		lockFile = new File(dir, ".lock");
		FileMisc.retry(
				lockFile,
				f -> {
					if (f.exists()) {
						throw new IllegalStateException(
								"P2 operation already in progress, close other clients or delete stale lockfile at "
										+ lockFile.getAbsolutePath());
					}
				});

		lock = new FileOutputStream(lockFile);
		lock.write(Long.toString(ProcessHandle.current().pid()).getBytes());
		lock.flush();
	}

	@Override
	public void close() throws IOException {
		lock.close();
		FileMisc.delete(lockFile);
	}
}
