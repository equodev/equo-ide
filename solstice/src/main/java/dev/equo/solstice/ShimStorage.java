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

import java.io.File;

class ShimStorage {
	private final File configDir;

	ShimStorage(File configDir) {
		this.configDir = configDir;
	}

	File getDataFileBundle(BundleContextSolstice.ShimBundle bundle, String filename) {
		File dir = new File(configDir, bundle.getSymbolicName() + "/" + bundle.getBundleId() + "/data");
		dir.mkdirs();
		return new File(dir, filename);
	}

	File getDataFileSystemBundle(BundleContextSolstice context, String filename) {
		return getDataFileBundle(context.bundleForSymbolicName("org.eclipse.osgi"), filename);
	}

	File getDataFileRootContext(BundleContextSolstice context, String filename) {
		return getDataFileSystemBundle(context, filename);
	}
}
