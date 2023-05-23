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
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * There are a few things which EquoIDE needs to cache on the developer's machine. They are
 * described exhaustively by this class.
 *
 * <ul>
 *   <li>{@link #ideWorkspaces()} defaults to {@code ~/.equo/ide-workspaces}, can override with
 *       {@link #override_ideWorkspaces}
 *   <li>{@link #p2data()} defaults to {@code ~/.m2/repository/dev/equo/p2-data/}, can override with
 *       {@link #override_p2data}
 *       <ul>
 *         <li>If we get any errors creating the default, it also tries {@code
 *             GRADLE_USER_HOME/caches/p2-data}
 *       </ul>
 * </ul>
 */
public class CacheLocations {
	private CacheLocations() {}

	private static Path userHome() {
		return new File(System.getProperty("user.home")).toPath();
	}

	/**
	 * When EquoIDE creates an IDE for you, it must also create an Eclipse workspace. Unfortunately,
	 * that workspace cannot be a subdirectory of the project directory (an eclipse limitation). This
	 * is a problem for single-project builds.
	 *
	 * <p>As a workaround, we put all eclipse workspaces in a central location, which is tied to their
	 * project directory. Whenever a new workspace is created, we do a quick check to make sure there
	 * aren't any stale workspaces. If the workspace has gone stale, we delete it.
	 *
	 * <p>Default value is {@code ~/.equo}, override by setting {@link #override_ideWorkspaces}.
	 */
	public static File ideWorkspaces() {
		if (ideWorkspaces == null) {
			ideWorkspaces = override_ideWorkspaces;
			if (ideWorkspaces == null) {
				ideWorkspaces = userHome().resolve(".equo").resolve("ide-workspaces").toFile();
			}
		}
		return ideWorkspaces;
	}

	private static File ideWorkspaces = null;
	public static File override_ideWorkspaces = null;

	private static final String P2_DATA_WITHIN_M2 = "repository/dev/equo/p2-data/";
	private static final String P2_DATA_GRADLE_USER_HOME = "caches/p2-data";

	/**
	 * Bundle pool used for caching jars and assembling disjoint eclipse installs:
	 * `~/.equo/bundle-pool`
	 *
	 * <p>Oomph does this by default in the given location.
	 */
	public static File p2data() {
		if (p2data == null) {
			p2data = override_p2data;
			if (p2data == null) {
				try {
					Path m2 = userHome().resolve(".m2");
					if (!Files.exists(m2)) {
						Files.createDirectories(m2);
					}
					Path p2Data = m2.resolve(P2_DATA_WITHIN_M2);
					p2data = p2Data.resolve("repository/dev/equo/p2-data").toFile();
				} catch (Exception e) {
					var gradleUserHome = System.getenv("GRADLE_USER_HOME");
					if (gradleUserHome == null) {
						throw Unchecked.wrap(e);
					} else {
						p2data = new File(gradleUserHome).toPath().resolve(P2_DATA_GRADLE_USER_HOME).toFile();
					}
				}
			}
		}
		return p2data;
	}

	private static File p2data = null;
	public static File override_p2data = null;

	static File p2Queries() {
		return new File(p2data(), "queries");
	}

	static File p2metadata() {
		return new File(p2data(), "metadata");
	}

	static File p2bundlePool() {
		return new File(p2data(), "bundle-pool");
	}

	public static File p2nestedJars() {
		return new File(p2data(), "nested-jars");
	}
}
