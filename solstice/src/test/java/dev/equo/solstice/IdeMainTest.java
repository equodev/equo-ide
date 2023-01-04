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

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.felix.atomos.Atomos;
import org.apache.felix.atomos.AtomosContent;
import org.eclipse.osgi.service.urlconversion.URLConverter;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;
import org.osgi.framework.Constants;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.launch.Framework;
import org.slf4j.simple.SimpleLogger;

public class IdeMainTest {
	static boolean useSolstice = true;

	private static Optional<Map<String, String>> headerProvider(
			String location, Map<String, String> existingHeaders) {
		return new ModifiedHeaders(existingHeaders)
				.removeAll(Constants.REQUIRE_CAPABILITY)
				.removeFrom(Constants.REQUIRE_BUNDLE, "slf4j.api", "system.bundle")
				.removeFrom(
						Constants.IMPORT_PACKAGE,
						"java.awt",
						"java.beans",
						"java.io",
						"java.lang",
						"java.lang.annotation",
						"java.lang.invoke",
						"java.lang.management",
						"java.lang.module",
						"java.lang.ref",
						"java.lang.reflect",
						"java.net",
						"java.nio",
						"java.nio.channels",
						"java.nio.charset",
						"java.nio.file",
						"java.nio.file.attribute",
						"java.security",
						"java.security.cert",
						"java.sql",
						"java.text",
						"java.time",
						"java.time.format",
						"java.time.temporal",
						"java.util",
						"java.util.concurrent",
						"java.util.concurrent.atomic",
						"java.util.concurrent.locks",
						"java.util.function",
						"java.util.jar",
						"java.util.regex",
						"java.util.stream",
						"java.util.zip",
						"javax.crypto",
						"javax.imageio",
						"javax.naming",
						"javax.naming.ldap",
						"javax.net.ssl",
						"javax.security.auth.callback",
						"javax.security.auth.login",
						"javax.security.auth.spi",
						"javax.xml.datatype",
						"javax.xml.namespace",
						"javax.xml.parsers",
						"javax.xml.transform",
						"javax.xml.transform.dom",
						"javax.xml.transform.stream",
						"kotlin.comparisons",
						"kotlin.coroutines",
						"kotlin.coroutines.intrinsics",
						"kotlin.coroutines.jvm.internal",
						"kotlin.io",
						"kotlin.sequences",
						"kotlin.text",
						"org.apache.commons.beanutils",
						"org.eclipse.equinox.internal.p2.jarprocessor",
						"org.eclipse.equinox.jsp.jasper",
						"org.eclipse.jetty.jmx",
						"org.ietf.jgss",
						"org.jdom",
						"org.jdom.input",
						"org.w3c.dom.css",
						"org.w3c.dom.views",
						"org.xml.sax.ext",
						"org.xml.sax.helpers")
				.headers();
	}

	static class ModifiedHeaders {
		final Map<String, String> headers;

		ModifiedHeaders(Map<String, String> existingHeaders) {
			this.headers = new LinkedHashMap<>(existingHeaders);
		}

		public ModifiedHeaders removeAll(String header) {
			headers.remove(header);
			return this;
		}

		private ModifiedHeaders removeFrom(String header, String... toRemove) {
			String headerUnparsed = headers.get(header);
			if (headerUnparsed == null) {
				return this;
			}
			List<String> headerList = Solstice.parseManifestHeaderSimple(headerUnparsed);
			headerList.removeAll(Arrays.asList(toRemove));
			if (headerList.isEmpty()) {
				return removeAll(header);
			} else {
				headers.put(header, headerList.stream().collect(Collectors.joining(",")));
				return this;
			}
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
			Framework framework =
					atomos.newFramework(
							Map.of(
									"atomos.content.install",
									"false",
									Constants.FRAMEWORK_STORAGE_CLEAN,
									Constants.FRAMEWORK_STORAGE_CLEAN_ONFIRSTINIT));
			// framework must be initialized before any bundles can be installed
			framework.init();
			List<Bundle> bundles = new ArrayList<>();
			for (AtomosContent content : atomos.getBootLayer().getAtomosContents()) {
				// The resulting bundle will use a bundle location of
				// "atomos:" + atomosContent.getAtomosLocation();
				bundles.add(content.install());
			}
			bundles.sort(Comparator.comparing(Bundle::getSymbolicName));
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

			// Atomos is backing our bundles with ConnectBundleFile
			// versus Eclipse which backs them with FileBundleEntry
			// and that causes problems in org.eclipse.core.internal.runtime.PlatformURLConverter
			// We'll workaround for now with this surgery
			{
				Collection<ServiceReference<URLConverter>> converters =
						context.getServiceReferences(URLConverter.class, "(protocol=platform)");
				for (ServiceReference<URLConverter> toRemove : converters) {
					((org.eclipse.osgi.internal.serviceregistry.ServiceReferenceImpl) toRemove)
							.getRegistration()
							.unregister();
				}
				context.registerService(
						URLConverter.class,
						new URLConverter() {
							@Override
							public URL toFileURL(URL url) {
								return url;
							}

							@Override
							public URL resolve(URL url) {
								return url;
							}
						},
						Dictionaries.of("protocol", "platform"));
			}
		}
		IdeMainUi.main(context);
	}
}
