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
import java.util.Comparator;
import java.util.Map;
import java.util.Optional;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AtomosFrontend {
	private final BundleContext bundleContext;

	public AtomosFrontend() throws BundleException, InvalidSyntaxException {
		Logger logger = LoggerFactory.getLogger(Solstice.class);
		var bundleSet = SolsticeManifest.discoverBundles();
		bundleSet.warnAndModifyManifestsToFix(logger);
		var bySymbolicName = bundleSet.bySymbolicName();
		Atomos.HeaderProvider headerProvider =
				new Atomos.HeaderProvider() {
					@Override
					public Optional<Map<String, String>> apply(
							String location, Map<String, String> existingHeaders) {
						var symbolicNameHeader = existingHeaders.get(Constants.BUNDLE_SYMBOLICNAME);
						if (symbolicNameHeader == null) {
							// represents a jar that didn't have OSGi metadata. no problem!
							return Optional.empty();
						}
						String symbolicName =
								SolsticeManifest.parseManifestHeaderSimple(
												existingHeaders.get(Constants.BUNDLE_SYMBOLICNAME))
										.get(0);
						var candidates = bySymbolicName.get(symbolicName);
						if (candidates == null) {
							if (!symbolicName.startsWith("java.") && !symbolicName.startsWith("jdk.")) {
								logger.warn("No manifest for bundle " + symbolicName + " at " + location);
							}
							return Optional.empty();
						}
						var manifest = bySymbolicName.get(symbolicName).get(0);
						return Optional.of(manifest.atomosHeaders());
					}
				};
		Atomos atomos = Atomos.newAtomos(headerProvider);
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
		var bundles = new ArrayList<Bundle>();
		for (AtomosContent content : atomos.getBootLayer().getAtomosContents()) {
			bundles.add(content.install());
		}
		bundles.sort(Comparator.comparing(Bundle::getSymbolicName));
		for (Bundle b : bundles) {
			if (b.getHeaders().get("Fragment-Host") == null) {
				b.start();
			}
		}
		// The installed bundles will not actually activate until the framework is started
		framework.start();
		bundleContext = framework.getBundleContext();

		// Atomos is backing our bundles with ConnectBundleFile
		// versus Eclipse which backs them with FileBundleEntry
		// and that causes problems in org.eclipse.core.internal.runtime.PlatformURLConverter
		// We'll workaround for now with this surgery
		{
			var converters =
					bundleContext.getServiceReferences(URLConverter.class, "(protocol=platform)");
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

	public BundleContext getBundleContext() {
		return bundleContext;
	}
}
