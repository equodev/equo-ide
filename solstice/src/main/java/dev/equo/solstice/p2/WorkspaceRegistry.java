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
		FileMisc.mkdirs(root);
		for (File workspace : root.listFiles()) {
			if (workspace.isDirectory()) {
				Optional<String> ownerPath = FileMisc.readToken(root, workspace.getName() + OWNER_PATH);
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
					FileMisc.mkdirs(workspace);
					FileMisc.writeToken(root, workspace.getName() + OWNER_PATH, ideDir.getAbsolutePath());
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
			FileMisc.delete(workspace);
			File token = new File(root, workspace.getName() + OWNER_PATH);
			FileMisc.delete(token);
		} catch (Exception e) {
			System.err.println(
					"Tried to delete workspace " + workspace.getAbsolutePath() + " because " + reason);
			e.printStackTrace();
		}
	}
}
