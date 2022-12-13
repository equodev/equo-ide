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
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okio.Okio;

class JarCache implements AutoCloseable {
	final File bundlePool = CacheLocations.p2bundlePool();
	final P2Client.Caching cachingPolicy;
	final OkHttpClient client;
	final FileOutputStream lock;
	final File lockFile;

	JarCache(P2Client.Caching cachingPolicy) throws IOException {
		this.cachingPolicy = cachingPolicy;
		this.client = new OkHttpClient.Builder().build();
		FileMisc.mkdirs(bundlePool);

		lockFile = new File(bundlePool, ".lock");
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
			var request = new Request.Builder().url(unit.getJarUrl()).build();
			var tempFile = File.createTempFile(unit.id, ".jar");
			try (var response = client.newCall(request).execute();
					var sink = Okio.buffer(Okio.sink(tempFile))) {
				sink.writeAll(response.body().source());
			}
			Files.move(tempFile.toPath(), jar.toPath(), StandardCopyOption.ATOMIC_MOVE);
			return jar;
		} else {
			throw new IllegalStateException(
					"No cached version of "
							+ unit.getJarUrl()
							+ " available, you must turn off offline mode.");
		}
	}

	@Override
	public void close() throws IOException {
		lock.close();
		FileMisc.delete(lockFile);
	}
}
