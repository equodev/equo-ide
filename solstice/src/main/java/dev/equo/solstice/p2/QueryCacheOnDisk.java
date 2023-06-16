/*******************************************************************************
 * Copyright (c) 2023 EquoTech, Inc. and others.
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

import dev.equo.solstice.NestedJars;
import dev.equo.solstice.SerializableMisc;
import java.io.File;
import java.util.Optional;
import javax.annotation.Nullable;

class QueryCacheOnDisk {
	final File rootDir;
	final String key;

	QueryCacheOnDisk(File rootDir, P2Model model) {
		this.rootDir = rootDir;
		this.key = NestedJars.solsticeVersion() + model.hashCode();
		if (!FileMisc.readToken(rootDir, VERSION).equals(Optional.of(VERSION_VALUE))) {
			if (rootDir.exists()) {
				FileMisc.delete(rootDir);
			}
			FileMisc.mkdirs(rootDir);
			FileMisc.writeToken(rootDir, VERSION, VERSION_VALUE);
		}
	}

	private static final String VERSION = "version";
	private static final String VERSION_VALUE = "1";

	private static final String CONTENT = "content";

	public @Nullable P2QueryResult get() {
		var dir = new File(rootDir, OfflineCache.filenameSafe(key));
		if (dir.isDirectory()) {
			return SerializableMisc.fromFile(P2QueryResult.class, new File(dir, CONTENT));
		} else {
			return null;
		}
	}

	public void put(P2QueryResult query) {
		var dir = new File(rootDir, OfflineCache.filenameSafe(key));
		FileMisc.mkdirs(dir);
		SerializableMisc.toFile(query, new File(dir, CONTENT));
	}
}
