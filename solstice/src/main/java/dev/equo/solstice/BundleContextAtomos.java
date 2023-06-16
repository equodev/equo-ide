/*******************************************************************************
 * Copyright (c) 2023 EquoTech, Inc. and others.
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
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.apache.felix.atomos.Atomos;
import org.eclipse.osgi.service.urlconversion.URLConverter;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;
import org.osgi.framework.Constants;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.Version;
import org.osgi.framework.launch.Framework;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Starts an OSGi context using Atomos. */
public class BundleContextAtomos implements Atomos.HeaderProvider {
	final Map<String, List<SolsticeManifest>> bySymbolicName;
	final Logger logger;

	BundleContextAtomos(Solstice bundleSet, Logger logger) {
		bySymbolicName = bundleSet.bySymbolicName();
		this.logger = logger;
	}

	@Override
	public Optional<Map<String, String>> apply(String location, Map<String, String> existingHeaders) {
		var symbolicNameHeader = existingHeaders.get(Constants.BUNDLE_SYMBOLICNAME);
		if (symbolicNameHeader == null) {
			// represents a jar that didn't have OSGi metadata. no problem!
			return Optional.empty();
		}
		String symbolicName =
				SolsticeManifest.parseAndStripManifestHeader(
								Constants.BUNDLE_SYMBOLICNAME, existingHeaders.get(Constants.BUNDLE_SYMBOLICNAME))
						.get(0);
		var candidates = bySymbolicName.get(symbolicName);
		if (candidates == null || candidates.isEmpty()) {
			if (!symbolicName.startsWith("java.") && !symbolicName.startsWith("jdk.")) {
				logger.warn("No manifest for bundle " + symbolicName + " at " + location);
			}
			return Optional.empty();
		}
		var manifest = bySymbolicName.get(symbolicName).get(0);
		return Optional.of(atomosHeaders(manifest));
	}

	public Map<String, String> atomosHeaders(SolsticeManifest manifest) {
		Map<String, String> atomos = new LinkedHashMap<>(manifest.getHeadersOriginal());
		setHeader(atomos, Constants.IMPORT_PACKAGE, manifest.pkgImports);
		setHeader(atomos, Constants.EXPORT_PACKAGE, manifest.pkgExports);
		setHeader(atomos, Constants.REQUIRE_BUNDLE, manifest.requiredBundles);
		setHeader(
				atomos,
				Constants.BUNDLE_CLASSPATH,
				Collections.emptyList()); // NestedJars makes this unnecessary
		// TODO: Atomos will work better if we finish https://github.com/equodev/equo-ide/issues/74
		// setHeader(atomos, Constants.REQUIRE_CAPABILITY, manifest.TODO);
		return atomos;
	}

	private static void setHeader(Map<String, String> map, String key, List<String> values) {
		if (values.isEmpty()) {
			map.remove(key);
		} else {
			map.put(key, String.join(",", values));
		}
	}

	public static BundleContext hydrate(Solstice bundleSet, Map<String, String> props)
			throws BundleException {
		Logger logger = LoggerFactory.getLogger(BundleContextAtomos.class);
		Atomos atomos = Atomos.newAtomos(new BundleContextAtomos(bundleSet, logger));
		Framework framework = atomos.newFramework(props);
		framework.start();
		var bundleContext = framework.getBundleContext();
		bundleSet.hydrateFrom(
				manifest -> {
					for (var bundle : bundleContext.getBundles()) {
						if (bundle.getSymbolicName().equals(manifest.getSymbolicName())) {
							return bundle;
						}
					}
					throw new IllegalArgumentException("No bundle for " + manifest.getSymbolicName());
				});
		return bundleContext;
	}

	/** https://github.com/eclipse-equinox/equinox/issues/179 */
	public static void urlWorkaround(Solstice solstice) throws InvalidSyntaxException {
		var version =
				new Version(
						solstice
								.bundleForSymbolicName("org.eclipse.osgi")
								.getHeadersOriginal()
								.get(Constants.BUNDLE_VERSION));
		var fixedInVersion = new Version("3.18.300");
		if (version.compareTo(fixedInVersion) >= 0) {
			return;
		}
		var bundleContext = solstice.getContext();
		var converters = bundleContext.getServiceReferences(URLConverter.class, "(protocol=platform)");
		for (ServiceReference<URLConverter> toRemove : converters) {
			((org.eclipse.osgi.internal.serviceregistry.ServiceReferenceImpl<URLConverter>) toRemove)
					.getRegistration()
					.unregister();
		}
		bundleContext.registerService(
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
