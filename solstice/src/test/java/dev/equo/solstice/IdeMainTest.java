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
package dev.equo.solstice;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.swing.text.html.Option;
import org.apache.felix.atomos.Atomos;
import org.apache.felix.atomos.AtomosContent;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;
import org.osgi.framework.Constants;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.launch.Framework;
import org.slf4j.simple.SimpleLogger;

public class IdeMainTest {
	static boolean useSolstice = false;

	private static Optional<Map<String, String>> headerProvider(
			String location, Map<String, String> existingHeaders) {
		return new ModifiedHeaders(existingHeaders)
				.removeReqBundle("org.junit")
//				.removeImpPkg("org.junit")
				.headers();
//		return Optional.empty();
	}

	static class ModifiedHeaders {
		final Map<String, String> headers;

		ModifiedHeaders(Map<String, String> existingHeaders) {
			this.headers = new LinkedHashMap<>(existingHeaders);
		}

		public ModifiedHeaders removeReqBundle(String... toRemove) {
			return removeXXX(Constants.REQUIRE_BUNDLE, toRemove);
		}

		public ModifiedHeaders removeImpPkg(String... toRemove) {
			return removeXXX(Constants.IMPORT_PACKAGE, toRemove);
		}

		private ModifiedHeaders removeXXX(String header, String... toRemove) {
			String headerUnparsed = headers.get(header);
			if (headerUnparsed == null) {
				return this;
			}
			List<String> headerList = Solstice.parseManifestHeaderSimple(headerUnparsed);
			headerList.removeAll(Arrays.asList(toRemove));
			headers.put(header, headerList.stream().collect(Collectors.joining(",")));
			return this;
		}

		public Optional<Map<String, String>> headers() {
			return Optional.of(headers);
		}
	}

	public static void main(String[] args) throws InvalidSyntaxException, BundleException {
		System.setProperty(SimpleLogger.DEFAULT_LOG_LEVEL_KEY, "INFO");
		System.setProperty(SimpleLogger.LOG_FILE_KEY, "System.out");

		BundleContext context;
		if (useSolstice) {
			SolsticeInit init = new SolsticeInit();
			context = Solstice.initialize(init);
		} else {
			Atomos atomos = Atomos.newAtomos(IdeMainTest::headerProvider);
			// Set atomos.content.install to false to prevent automatic bundle installation
			Framework framework = atomos.newFramework(Map.of("atomos.content.install", "false"));
			// framework must be initialized before any bundles can be installed
			framework.init();
			List<Bundle> bundles = new ArrayList<>();
			for (AtomosContent content : atomos.getBootLayer().getAtomosContents()) {
				// The resulting bundle will use a bundle location of
				// "atomos:" + atomosContent.getAtomosLocation();
				bundles.add(content.install());
			}
			for (Bundle b : bundles) {
				try {
					if (b.getHeaders().get("Fragment-Host") == null) {
						b.start();
					}
				} catch (BundleException e) {
					System.err.println("BUNDLE " + b.getSymbolicName());
					e.printStackTrace();
					if (e.getMessage() == null) {
						System.err.println("  " + e.getClass());
					} else {
						String[] lines = e.getMessage().split("\n");
						System.err.println("  " + lines[0]);
						for (int i = 1; i < Math.min(5, lines.length); ++i) {
							System.err.println("  " + lines[i]);
						}
						var headers = b.getHeaders();
						var keys = headers.keys();
						while (keys.hasMoreElements()) {
							var key = keys.nextElement();
							System.err.println("  HEADER " + key + " = " + headers.get(key));
						}
					}
				}
			}
			// The installed bundles will not actually activate until the framework is started
			framework.start();
			context = framework.getBundleContext();
		}
		IdeMainUi.main(context);
	}
}
