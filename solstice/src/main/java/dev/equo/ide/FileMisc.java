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
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Optional;

class FileMisc {
	private static final int MS_RETRY = 500;

	static void delete(File fileOrDir) {
		retry(
				fileOrDir,
				f -> {
					if (f.isDirectory()) {
						Files.walkFileTree(
								f.toPath(),
								new SimpleFileVisitor<>() {
									@Override
									public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
											throws IOException {
										Files.delete(file);
										return FileVisitResult.CONTINUE;
									}

									@Override
									public FileVisitResult postVisitDirectory(Path dir, IOException exc)
											throws IOException {
										if (exc != null) {
											throw exc;
										}
										Files.delete(dir);
										return FileVisitResult.CONTINUE;
									}
								});
					} else {
						Files.deleteIfExists(f.toPath());
					}
				});
	}

	static Optional<String> readToken(File dir, String name) {
		try {
			File token = new File(dir, name);
			if (!token.isFile()) {
				return Optional.empty();
			} else {
				return Optional.of(Files.readString(token.toPath()));
			}
		} catch (IOException e) {
			e.printStackTrace();
			return Optional.empty();
		}
	}

	static void writeToken(File dir, String name, String value) {
		if (!dir.isDirectory()) {
			throw new IllegalArgumentException(
					"Need to create directory first! " + dir.getAbsolutePath());
		}
		File token = new File(dir, name);
		retry(token, f -> Files.write(f.toPath(), value.getBytes(StandardCharsets.UTF_8)));
	}

	/**
	 * Retries an action every ms, for 500ms, until it finally works or fails.
	 *
	 * <p>Makes FS operations more reliable.
	 */
	static void retry(File input, ThrowingConsumer<File> function) {
		long start = System.currentTimeMillis();
		Throwable lastException;
		do {
			try {
				function.accept(input);
				return;
			} catch (Throwable e) {
				lastException = e;
				try {
					Thread.sleep(1);
				} catch (InterruptedException ex) {
					// ignore
				}
			}
		} while (System.currentTimeMillis() - start < MS_RETRY);
		if (lastException instanceof RuntimeException) {
			throw (RuntimeException) lastException;
		} else {
			throw new RuntimeException(lastException);
		}
	}

	interface ThrowingConsumer<T> {
		void accept(T input) throws Exception;
	}
}
