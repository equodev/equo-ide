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
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Optional;
import javax.annotation.Nullable;

class OfflineCache {
	final File rootDir;

	OfflineCache(File rootDir) {
		this.rootDir = rootDir;
		if (FileMisc.readToken(rootDir, VERSION).equals(Optional.of(VERSION_VALUE))) {
			if (rootDir.exists()) {
				FileMisc.delete(rootDir);
			}
			FileMisc.mkdirs(rootDir);
			FileMisc.writeToken(rootDir, VERSION, VERSION_VALUE);
		}
	}

	private static final String VERSION = "version";
	private static final String VERSION_VALUE = "1";

	private static final int MAX_FILE_LENGTH = 92;
	private static final int ABBREVIATED = 40;

	/**
	 * Returns either the filename safe URL, or (first40)--(Base64 filenamesafe)(last40). Originally
	 * from <a
	 * href="https://github.com/diffplug/blowdryer/blob/e67212d51e6c499005d58b873eb6b7f008f85188/src/main/java/com/diffplug/blowdryer/Blowdryer.java#L229-L247>
	 * blowdryer</a>.
	 */
	static String filenameSafe(String url) {
		String allSafeCharacters = url.replaceAll("[^a-zA-Z0-9-+_.]", "-");
		String noDuplicateDash = allSafeCharacters.replaceAll("-+", "-");
		if (noDuplicateDash.length() <= MAX_FILE_LENGTH) {
			return noDuplicateDash;
		} else {
			int secondPoint = noDuplicateDash.length() - ABBREVIATED;
			String first = noDuplicateDash.substring(0, ABBREVIATED);
			String middle = noDuplicateDash.substring(ABBREVIATED, secondPoint);
			String end = noDuplicateDash.substring(secondPoint);

			byte[] hash;
			try {
				var md5 = MessageDigest.getInstance("MD5");
				md5.update(middle.getBytes(StandardCharsets.UTF_8));
				hash = md5.digest();
			} catch (NoSuchAlgorithmException e) {
				throw new RuntimeException(e);
			}
			String hashed = Base64.getEncoder().encodeToString(hash).replace('/', '-').replace('=', '-');
			return first + "--" + hashed + end;
		}
	}

	private static final String URL = "url";
	private static final String CONTENT = "content";

	public void put(String url, byte[] data) throws IOException {
		var dir = new File(rootDir, filenameSafe(url));
		FileMisc.mkdirs(dir);
		FileMisc.writeToken(dir, URL, url);
		Files.write(new File(dir, CONTENT).toPath(), data);
	}

	public void put404(String url) throws IOException {
		put(url, _404);
	}

	public @Nullable byte[] get(String url) throws IOException {
		var dir = new File(rootDir, filenameSafe(url));
		if (dir.isDirectory() && FileMisc.readToken(dir, URL).equals(Optional.of(url))) {
			return Files.readAllBytes(new File(dir, CONTENT).toPath());
		} else {
			return null;
		}
	}

	public static boolean is404(byte[] data) {
		return Arrays.equals(_404, data);
	}

	private static final byte[] _404 = new byte[] {0x04, 0x00, 0x04};
}
