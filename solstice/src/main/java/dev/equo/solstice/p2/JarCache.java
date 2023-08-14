/*******************************************************************************
 * Copyright (c) 2022-2023 EquoTech, Inc. and others.
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
import java.io.IOException;
import java.nio.file.Files;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okio.Okio;

class JarCache {
	final File bundlePool = CacheLocations.p2bundlePool();
	final OkHttpClient client = new OkHttpClient.Builder().build();
	final P2ClientCache cachingPolicy;

	JarCache(P2ClientCache cachingPolicy) {
		this.cachingPolicy = cachingPolicy;
		FileMisc.mkdirs(bundlePool);
	}

	public File download(P2Unit unit) throws IOException {
		File repoDir = new File(bundlePool, OfflineCache.filenameSafe(unit.getRepoUrl()));
		File jar = new File(repoDir, unit.id + "_" + unit.version + ".jar");
		if (jar.isFile()) {
			return jar;
		}
		if (cachingPolicy.networkAllowed()) {
			if (!repoDir.isDirectory()) {
				FileMisc.mkdirs(repoDir);
				FileMisc.writeToken(repoDir, ".url", unit.getRepoUrl());
			}
			var request = P2Client.buildRequest(unit.getJarUrl());
			var tempFile = File.createTempFile(unit.id, ".jar");
			try (var response = client.newCall(request).execute()) {
				if (response.code() == 200) {
					try (var sink = Okio.buffer(Okio.sink(tempFile))) {
						sink.writeAll(response.body().source());
					}
				} else {
					throw new IllegalArgumentException(response.code() + " at " + unit.getJarUrl());
				}
			}
			Files.move(tempFile.toPath(), jar.toPath());
			return jar;
		} else {
			throw new IllegalStateException(
					"No cached version of "
							+ unit.getJarUrl()
							+ " available, you must turn off offline mode.");
		}
	}
}
