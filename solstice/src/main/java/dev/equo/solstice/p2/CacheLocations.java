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
import java.nio.file.Path;
import java.util.Optional;

/**
 * There are a few things which EquoIDE needs to cache on the developer's machine. They are
 * described exhaustively by this class.
 *
 * <ul>
 *   <li>{@link #p2metadata()}
 *   <li>{@link #p2bundlePool()}
 *   <li>{@link #ideWorkspaces()}
 *   <li>{@link #p2Queries()}
 * </ul>
 *
 * <p>All these values can be overridden by setting the value of the `public static
 * override_whatever` variable.
 *
 * <p>If you happen to be using Gradle, you can override these by setting any of the following in
 * your {@code ~/.gradle/gradle.properties} file.
 *
 * <ul>
 *   <li>{@code equo_override_p2metadata}
 *   <li>{@code equo_override_p2bundlePool}
 *   <li>{@code equo_override_ideWorkspaces}
 *   <li>{@code equo_override_p2Queries}
 * </ul>
 */
public class CacheLocations {
	private CacheLocations() {}

	private static final String ROOT = ".equo";

	/**
	 * {@link P2Client} can use HTTP caching to speed up browsing of p2 metadata, and also to allow
	 * offline p2 queries.
	 *
	 * <p>Rather than downloading this metadata over and over, we only download it once, and cache the
	 * results here.
	 */
	public static File p2metadata() {
		return defOverride(ROOT + "/p2-metadata", override_p2metadata);
	}

	public static File override_p2metadata = null;

	/**
	 * Bundle pool used for caching jars and assembling disjoint eclipse installs:
	 * `~/.equo/bundle-pool`
	 *
	 * <p>Oomph does this by default in the given location.
	 */
	public static File p2bundlePool() {
		return defOverride(ROOT + "/p2-bundle-pool", override_p2bundlePool);
	}

	public static File override_p2bundlePool = null;

	/**
	 * When EquoIDE creates an IDE for you, it must also create an Eclipse workspace. Unfortunately,
	 * that workspace cannot be a subdirectory of the project directory (an eclipse limitation). This
	 * is a problem for single-project builds.
	 *
	 * <p>As a workaround, we put all eclipse workspaces in a central location, which is tied to their
	 * project directory. Whenever a new workspace is created, we do a quick check to make sure there
	 * aren't any stale workspaces. If the workspace has gone stale, we delete it.
	 */
	public static File ideWorkspaces() {
		return defOverride(ROOT + "/ide-workspaces", override_ideWorkspaces);
	}

	public static File override_ideWorkspaces = null;

	/** Directory used to cache p2 queries: `~/.equo/p2-queries` */
	public static File p2Queries() {
		return defOverride(ROOT + "/p2-queries", override_p2Queries);
	}

	public static File override_p2Queries;

	private static File defOverride(String userHomeRelative, File override) {
		return Optional.ofNullable(override)
				.orElseGet(
						() -> {
							return userHome().resolve(userHomeRelative).toFile();
						});
	}

	private static Path userHome() {
		return new File(System.getProperty("user.home")).toPath();
	}
}
