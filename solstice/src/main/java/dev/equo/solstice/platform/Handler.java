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
package dev.equo.solstice.platform;

import dev.equo.solstice.BundleContextShim;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;

/** This allows us to intercept platform:// URLs (the name is determine by our package). */
public class Handler extends URLStreamHandler {
	private static final String PKG = "dev.equo.solstice";
	private static final String CONTENT_PATH_PROP = "java.protocol.handler.pkgs";

	public static void install(BundleContextShim solstice) {
		Handler.solstice = solstice;
		String handlerPkgs = System.getProperty(CONTENT_PATH_PROP, "");
		if (!handlerPkgs.contains(PKG)) {
			if (handlerPkgs.isEmpty()) {
				handlerPkgs = PKG;
			} else {
				handlerPkgs += "|" + PKG;
			}
			System.setProperty(CONTENT_PATH_PROP, handlerPkgs);
		}
	}

	private static BundleContextShim solstice;

	private static final String SLASH_PLUGIN = "/plugin/";

	@Override
	protected URLConnection openConnection(URL u) throws IOException {
		String path = u.getPath();
		if (path.startsWith(SLASH_PLUGIN)) {
			String after = path.substring(SLASH_PLUGIN.length());
			int nextSlash = after.indexOf('/');
			String plugin = after.substring(0, nextSlash);
			String resource = after.substring(nextSlash + 1);
			var bundle = solstice.bundleForSymbolicName(plugin);
			if (bundle == null) {
				return null;
			}
			var entry = bundle.getEntry(resource);
			if (entry == null) {
				throw new IOException("No such URL " + u);
			}
			return entry.openConnection();
		} else {
			throw new IllegalArgumentException("Expected " + SLASH_PLUGIN + " got " + u.getPath());
		}
	}
}
