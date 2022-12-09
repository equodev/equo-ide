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
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * Maintains a registry of EquoIDE workspaces.
 *
 * <p>So here's the problem. Let say you have a multiproject build, such as this:
 *
 * <pre>
 * libA/
 *   .project
 * libB/
 *   .project
 * ide/
 *   build/oomph-ide/
 *     eclipse.exe
 *     workspace/
 * </pre>
 *
 * Everything works great! But in a single project build:
 *
 * <pre>
 * lib/
 *   .project
 *   build/oomph-ide/
 *     eclipse.exe
 *     workspace/
 * </pre>
 *
 * It breaks. Why? Because the `workspace` is a subdirectory of the `.project` folder. And eclipse
 * does not support that. GAH!
 *
 * <p>So, to fix that, we need to maintain the workspaces in a central registry. This class
 * maintains that registry, and cleans out old workspaces when the IDE they were created for gets
 * deleted.
 *
 * <p>The registry lives in {@link CacheLocations#ideWorkspaces()}. It names the workspace folders
 * as such:
 *
 * <pre>
 * gradle root project's name-hashcode of ide directory absolute path/
 * gradle root project's name-hashcode of ide directory absolute path-owner   [file containing absolute path of ide folder]
 * </pre>
 */
public class WorkspaceRegistry {
	public static WorkspaceRegistry instance() {
		return new WorkspaceRegistry(CacheLocations.ideWorkspaces());
	}

	final File root;
	/** Map from the ide directory to a workspace directory. */
	final Map<File, File> ownerToWorkspace = new HashMap<>();

	static final String OWNER_PATH = "-owner";

	WorkspaceRegistry(File root) {
		this.root = Objects.requireNonNull(root);
		mkdirs(root);
		for (File workspace : root.listFiles()) {
			if (workspace.isDirectory()) {
				Optional<String> ownerPath = readToken(root, workspace.getName() + OWNER_PATH);
				if (!ownerPath.isPresent()) {
					// if there's no token, delete it
					deleteWorkspace(workspace, "missing token " + OWNER_PATH + ".");
				} else {
					ownerToWorkspace.put(new File(ownerPath.get()), workspace);
				}
			}
		}
	}

	/** Returns the workspace directory appropriate for the given name and file. */
	public File workspaceDir(File ideDir) {
		return ownerToWorkspace.computeIfAbsent(
				ideDir,
				owner -> {
					File workspace =
							new File(root, ideDir.getName() + "-" + owner.getAbsolutePath().hashCode());
					mkdirs(workspace);
					writeToken(root, workspace.getName() + OWNER_PATH, ideDir.getAbsolutePath());
					return workspace;
				});
	}

	/** Removes all workspace directories for which their owning workspace is no longer present. */
	public void clean() {
		Iterator<Map.Entry<File, File>> iter = ownerToWorkspace.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry<File, File> entry = iter.next();
			File ownerDir = entry.getKey();
			File workspaceDir = entry.getValue();
			if (!ownerDir.exists()) {
				deleteWorkspace(workspaceDir, "owner " + ownerDir + " no longer exists.");
				iter.remove();
			}
		}
	}

	/**
	 * Tries to delete folder. If it fails, it prints a warning but keeps going. No reason to break a
	 * build over spilled diskspace.
	 */
	private void deleteWorkspace(File workspace, String reason) {
		try {
			delete(workspace);
			File token = new File(root, workspace.getName() + OWNER_PATH);
			delete(token);
		} catch (Exception e) {
			System.err.println(
					"Tried to delete workspace " + workspace.getAbsolutePath() + " because " + reason);
			e.printStackTrace();
		}
	}

	private static void delete(File fileOrDir) {
		retry(
				fileOrDir,
				f -> {
					if (f.isDirectory()) {
						Files.walkFileTree(
								f.toPath(),
								new SimpleFileVisitor<Path>() {
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

	private static Optional<String> readToken(File dir, String name) {
		try {
			File token = new File(dir, name);
			if (!token.isFile()) {
				return Optional.empty();
			} else {
				return Optional.of(new String(Files.readAllBytes(token.toPath()), StandardCharsets.UTF_8));
			}
		} catch (IOException e) {
			e.printStackTrace();
			return Optional.empty();
		}
	}

	private static void writeToken(File dir, String name, String value) {
		if (!dir.isDirectory()) {
			throw new IllegalArgumentException(
					"Need to create directory first! " + dir.getAbsolutePath());
		}
		File token = new File(dir, name);
		retry(
				token,
				f -> {
					Files.write(f.toPath(), value.getBytes(StandardCharsets.UTF_8));
				});
	}

	private static void mkdirs(File file) {
		retry(
				file,
				dir -> {
					java.nio.file.Files.createDirectories(dir.toPath());
				});
	}

	/**
	 * Retries an action every ms, for 500ms, until it finally works or fails.
	 *
	 * <p>Makes FS operations more reliable.
	 */
	private static void retry(File input, ThrowingConsumer<File> function) {
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
		throw new RuntimeException(lastException);
	}

	private static final int MS_RETRY = 500;

	private interface ThrowingConsumer<T> {
		void accept(T input) throws Exception;
	}
}
