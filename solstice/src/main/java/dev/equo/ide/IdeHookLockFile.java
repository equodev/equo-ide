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
import org.eclipse.swt.widgets.Shell;

class IdeHookLockFile implements IdeHook {
	public static IdeHookLockFile forWorkspaceDir(File workspaceDir) {
		return new IdeHookLockFile(workspaceDir);
	}

	private File workspaceDir;

	private IdeHookLockFile(File workspaceDir) {
		this.workspaceDir = workspaceDir;
	}

	@Override
	public IdeHookInstantiated instantiate() {
		return new Instantiated();
	}

	class Instantiated implements IdeHookInstantiated {
		private Shell splash;

		@Override
		public void afterDisplay(org.eclipse.swt.widgets.Display display) {
			if (workspaceDir != null) {
				IdeLockFile.forWorkspaceDir(workspaceDir).savePid();
			}
		}
	}
}
