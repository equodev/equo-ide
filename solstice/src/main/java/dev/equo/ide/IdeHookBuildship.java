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

@SuppressWarnings("serial")
public class IdeHookBuildship implements IdeHook {
	File rootDir;
	boolean isOffline;

	public IdeHookBuildship(File rootDir, boolean isOffline) {
		this.rootDir = rootDir;
		this.isOffline = isOffline;
	}

	@Override
	public IdeHookInstantiated instantiate() throws Exception {
		return IdeHook.usingReflection("dev.equo.ide.BuildshipImpl", this);
	}
}
