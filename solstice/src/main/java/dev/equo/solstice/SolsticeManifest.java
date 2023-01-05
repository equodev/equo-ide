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

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.jar.Manifest;
import javax.annotation.Nullable;
import org.osgi.framework.Constants;
import org.slf4j.Logger;

public class SolsticeManifest {
	public static class BundleSet {
		private final List<SolsticeManifest> bundles;

		private BundleSet(List<SolsticeManifest> bundles) {
			this.bundles = bundles;
		}

		public List<SolsticeManifest> getBundles() {
			return bundles;
		}

		public Map<String, List<SolsticeManifest>> bySymbolicName() {
			TreeMap<String, List<SolsticeManifest>> map = new TreeMap<>();
			for (SolsticeManifest bundle : bundles) {
				mapAdd(map, bundle.getSymbolicName(), bundle);
			}
			return map;
		}

		public Map<String, List<SolsticeManifest>> byExportedPackage() {
			TreeMap<String, List<SolsticeManifest>> map = new TreeMap<>();
			for (SolsticeManifest bundle : bundles) {
				for (String pkg : bundle.getPkgExports()) {
					mapAdd(map, pkg, bundle);
				}
			}
			return map;
		}

		public Map<String, List<SolsticeManifest>> calculateMissingBundles(
				Set<String> availableBundles) {
			TreeMap<String, List<SolsticeManifest>> map = new TreeMap<>();
			for (SolsticeManifest bundle : bundles) {
				for (var required : bundle.getRequiredBundles()) {
					if (!availableBundles.contains(required)) {
						mapAdd(map, required, bundle);
					}
				}
			}
			return map;
		}

		public Map<String, List<SolsticeManifest>> calculateMissingPackages(
				Set<String> availablePackages) {
			TreeMap<String, List<SolsticeManifest>> map = new TreeMap<>();
			for (SolsticeManifest bundle : bundles) {
				for (var importedPkg : bundle.getPkgImports()) {
					if (!availablePackages.contains(importedPkg)) {
						mapAdd(map, importedPkg, bundle);
					}
				}
			}
			return map;
		}

		private static <K> void mapAdd(
				Map<K, List<SolsticeManifest>> map, K key, SolsticeManifest value) {
			var oldValue = map.putIfAbsent(key, Collections.singletonList(value));
			if (oldValue != null) {
				if (oldValue.size() == 1) {
					var newList = new ArrayList<SolsticeManifest>(2);
					newList.addAll(oldValue);
					oldValue = newList;
					map.put(key, newList);
				}
				oldValue.add(value);
			}
		}

		public void warnAndModifyManifestsToFix(Logger logger) {
			var bySymbolicName = bySymbolicName();
			var byExportedPackage = byExportedPackage();
			bySymbolicName.forEach(
					(symbolicName, manifests) -> {
						if (manifests.size() > 1) {
							logger.warn("Multiple bundles with same symbolic name: " + symbolicName);
							for (SolsticeManifest manifest : manifests) {
								logger.warn("  - " + manifest.jarUrl);
							}
						}
					});
			byExportedPackage.forEach(
					(pkg, manifests) -> {
						if (manifests.size() > 1) {
							logger.warn("Multiple bundles exporting the same package: " + pkg);
							for (SolsticeManifest manifest : manifests) {
								logger.warn("  - " + manifest.jarUrl);
							}
						}
					});
			var missingBundles = calculateMissingBundles(bySymbolicName.keySet());
			missingBundles.forEach(
					(missing, neededBy) -> {
						logger.warn("Missing required bundle " + missing + " needed by " + neededBy);
					});
			var missingPackages = calculateMissingPackages(byExportedPackage.keySet());
			missingPackages.forEach(
					(missing, neededBy) -> {
						logger.warn("Missing imported package " + missing + " imported by " + neededBy);
					});

			for (var bundle : bundles) {
				bundle.requiredBundles.removeAll(missingBundles.keySet());
				bundle.pkgImports.removeAll(missingPackages.keySet());
			}
		}
	}

	/** Returns an array list of all bundles on the classpath. */
	public static BundleSet discoverBundles() {
		Enumeration<URL> manifestURLs =
				Unchecked.get(
						() -> SolsticeManifest.class.getClassLoader().getResources("META-INF/MANIFEST.MF"));

		int classpathOrder = 0;
		List<SolsticeManifest> manifests = new ArrayList<>();
		while (manifestURLs.hasMoreElements()) {
			var manifest = new SolsticeManifest(manifestURLs.nextElement(), ++classpathOrder);
			if (manifest.symbolicName != null) {
				manifests.add(manifest);
			}
		}
		return new BundleSet(manifests);
	}

	static final String MANIFEST_PATH = "/META-INF/MANIFEST.MF";

	private final String jarUrl;
	private final int classpathOrder;
	private final @Nullable String symbolicName;
	final LinkedHashMap<String, String> headersOriginal = new LinkedHashMap<>();
	private final List<String> requiredBundles;
	private final List<String> pkgImports;
	private final List<String> pkgExports;

	SolsticeManifest(URL manifestURL, int classpathOrder) {
		this.classpathOrder = classpathOrder;
		var externalForm = manifestURL.toExternalForm();
		if (!externalForm.endsWith(MANIFEST_PATH)) {
			throw new IllegalArgumentException(
					"Expected manifest to end with " + MANIFEST_PATH + " but was " + externalForm);
		}
		jarUrl = externalForm.substring(0, externalForm.length() - MANIFEST_PATH.length());
		if (!jarUrl.endsWith("!")) {
			if (jarUrl.endsWith("build/resources/main")) {
				// we're inside a Gradle build/test, no worries
			} else {
				throw new IllegalArgumentException(
						"Must end with !  SEE getEntry if this changes  " + jarUrl);
			}
		}

		Manifest manifest;
		try (InputStream stream = manifestURL.openStream()) {
			manifest = new Manifest(stream);
		} catch (IOException e) {
			throw Unchecked.wrap(e);
		}
		for (Map.Entry<Object, Object> entry : manifest.getMainAttributes().entrySet()) {
			headersOriginal.put(entry.getKey().toString(), entry.getValue().toString());
		}

		var symbolicNameRaw = parseOriginal(Constants.BUNDLE_SYMBOLICNAME);
		symbolicName = symbolicNameRaw.isEmpty() ? null : symbolicNameRaw.get(0);

		requiredBundles = parseOriginal(Constants.REQUIRE_BUNDLE);

		pkgImports = parseOriginal(Constants.IMPORT_PACKAGE);
		pkgExports = parseOriginal(Constants.EXPORT_PACKAGE);
		// if we export a package, we don't actually have to import it, that's just for letting
		// multiple bundles define the same classes, which is a dubious feature to support
		// https://access.redhat.com/documentation/en-us/red_hat_jboss_fuse/6.3/html/managing_osgi_dependencies/importexport
		getPkgImports().removeAll(getPkgExports());
	}

	public String getSymbolicName() {
		return symbolicName;
	}

	public String getJarUrl() {
		return jarUrl;
	}

	private List<String> parseOriginal(String key) {
		String attribute = headersOriginal.get(key);
		if (attribute == null) {
			return Collections.emptyList();
		}
		return parseManifestHeaderSimple(attribute);
	}

	@Override
	public String toString() {
		if (symbolicName != null) {
			return symbolicName;
		} else {
			return jarUrl;
		}
	}

	/** Parse out a manifest header, and ignore the versions */
	static List<String> parseManifestHeaderSimple(String in) {
		String[] bundlesAndVersions = in.split(",");
		List<String> attrs = new ArrayList<>();
		List<String> required = new ArrayList<>(bundlesAndVersions.length);
		for (String s : bundlesAndVersions) {
			attrs.clear();
			int attrDelim = s.indexOf(';');
			if (attrDelim == -1) {
				attrDelim = s.length();
			} else {
				int start = attrDelim;
				while (start < s.length()) {
					int next = s.indexOf(';', start + 1);
					if (next == -1) {
						next = s.length();
					}
					attrs.add(s.substring(start + 1, next).trim());
					start = next;
				}
			}
			if (attrs.contains("resolution:=optional")) {
				// skip everything optional
				continue;
			}
			String simple = s.substring(0, attrDelim);
			if (simple.indexOf('"') == -1) {
				required.add(simple.trim());
			}
		}
		return required;
	}

	public List<String> getRequiredBundles() {
		return requiredBundles;
	}

	public List<String> getPkgImports() {
		return pkgImports;
	}

	public List<String> getPkgExports() {
		return pkgExports;
	}
}
