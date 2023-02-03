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

import java.io.File;
import java.net.URL;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.felix.atomos.Atomos;
import org.eclipse.osgi.service.datalocation.Location;
import org.eclipse.osgi.service.urlconversion.URLConverter;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;
import org.osgi.framework.Constants;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.launch.Framework;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Starts an OSGi context using Atomos. */
public class AtomosFrontend implements Atomos.HeaderProvider {
	final Map<String, List<SolsticeManifest>> bySymbolicName;
	final Logger logger;

	AtomosFrontend(BundleSet bundleSet, Logger logger) {
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
		setHeader(atomos, Constants.IMPORT_PACKAGE, manifest.getPkgImports());
		setHeader(atomos, Constants.EXPORT_PACKAGE, manifest.getPkgExports());
		setHeader(atomos, Constants.REQUIRE_BUNDLE, manifest.getRequiredBundles());
		setHeader(atomos, Constants.REQUIRE_CAPABILITY, Collections.emptyList());
		return atomos;
	}

	private static void setHeader(Map<String, String> map, String key, List<String> values) {
		if (values.isEmpty()) {
			map.remove(key);
		} else {
			map.put(key, values.stream().collect(Collectors.joining(",")));
		}
	}

	public static BundleContext open(File installDir, BundleSet bundleSet) throws BundleException {
		Logger logger = LoggerFactory.getLogger(AtomosFrontend.class);
		Atomos atomos = Atomos.newAtomos(new AtomosFrontend(bundleSet, logger));
		// Set atomos.content.install to false to prevent automatic bundle installation
		var props = new LinkedHashMap<String, String>();
		props.put(Constants.FRAMEWORK_STORAGE_CLEAN, Constants.FRAMEWORK_STORAGE_CLEAN_ONFIRSTINIT);
		props.put(Location.INSTANCE_AREA_TYPE, new File(installDir, "instance").getAbsolutePath());
		props.put(Location.INSTALL_AREA_TYPE, new File(installDir, "install").getAbsolutePath());
		props.put(Location.CONFIGURATION_AREA_TYPE, new File(installDir, "config").getAbsolutePath());
		props.put("atomos.content.start", "false");

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

		// start all the bundles that don't have jars (e.g. java.base -> jdk.zipfs)
		for (var bundle : bundleContext.getBundles()) {
			if (bundleSet.bundleByName(bundle.getSymbolicName()) == null) {
				bundle.start();
			}
		}
		return bundleContext;
	}

	/** https://github.com/eclipse-equinox/equinox/issues/179 */
	public static void urlWorkaround(BundleContext bundleContext) throws InvalidSyntaxException {
		var converters = bundleContext.getServiceReferences(URLConverter.class, "(protocol=platform)");
		for (ServiceReference<URLConverter> toRemove : converters) {
			((org.eclipse.osgi.internal.serviceregistry.ServiceReferenceImpl) toRemove)
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
