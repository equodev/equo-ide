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
import java.util.ArrayList;
import org.eclipse.swt.widgets.Shell;

class IdeHookLockFile implements IdeHook {
	public static IdeHookLockFile forWorkspaceDirAndClasspath(
			File workspaceDir, ArrayList<File> classpath) {
		return new IdeHookLockFile(workspaceDir, classpath);
	}

	private File workspaceDir;
	private ArrayList<File> classpath;

	private IdeHookLockFile(File workspaceDir, ArrayList<File> classpath) {
		this.workspaceDir = workspaceDir;
		this.classpath = classpath;
	}

	@Override
	public IdeHookInstantiated instantiate() {
		return new Instantiated();
	}

	class Instantiated implements IdeHookInstantiated {
		private Shell splash;

		public boolean isClean() {
			if (workspaceDir != null) {
				var lockfile = IdeLockFile.forWorkspaceDir(workspaceDir);
				boolean isClean = !lockfile.hasClasspath();
				if (isClean) {
					lockfile.writeClasspath(classpath);
				}
				return isClean;
			} else {
				return false;
			}
		}

		@Override
		public void postStartup() {
			if (workspaceDir != null) {
				IdeLockFile.forWorkspaceDir(workspaceDir).savePid();
			}
		}
	}
}
