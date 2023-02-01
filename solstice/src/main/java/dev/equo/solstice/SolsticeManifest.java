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
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.jar.Manifest;
import javax.annotation.Nullable;
import org.eclipse.osgi.util.ManifestElement;
import org.osgi.framework.BundleException;
import org.osgi.framework.Constants;
import org.slf4j.Logger;

/**
 * Parses a jar manifest, removing some fine-grained details for the purpose of simplifying the
 * developer experience.
 *
 * <ul>
 *   <li>optional imports and requirements are removed
 *   <li>version constraints are removed
 * </ul>
 */
public class SolsticeManifest {
	/** Represents a closed universe of OSGi bundles. */
	public static class BundleSet {
		private final List<SolsticeManifest> bundles;

		private BundleSet(List<SolsticeManifest> bundles) {
			this.bundles = bundles;
		}

		public List<SolsticeManifest> getBundles() {
			return Collections.unmodifiableList(bundles);
		}

		public Map<String, List<SolsticeManifest>> bySymbolicName() {
			return groupBundles(manifest -> Collections.singletonList(manifest.getSymbolicName()));
		}

		public Map<String, List<SolsticeManifest>> byExportedPackage() {
			Map<String, List<SolsticeManifest>> manifests = groupBundles(SolsticeManifest::getPkgExports);
			// fragments export the same packages as their parent, no need to report those conflicts
			manifests.replaceAll(
					(pkg, bundles) -> {
						if (bundles.size() > 1) {
							long numNonFragments =
									bundles.stream().filter(SolsticeManifest::isNotFragment).count();
							if (numNonFragments == 1) {
								return Collections.singletonList(
										bundles.stream().filter(SolsticeManifest::isNotFragment).findFirst().get());
							}
						}
						return bundles;
					});
			return manifests;
		}

		public Map<String, List<SolsticeManifest>> calculateMissingBundles(Set<String> available) {
			return calculateMissing(available, SolsticeManifest::getRequiredBundles);
		}

		public Map<String, List<SolsticeManifest>> calculateMissingPackages(Set<String> available) {
			return calculateMissing(available, SolsticeManifest::getPkgImports);
		}

		private Map<String, List<SolsticeManifest>> calculateMissing(
				Set<String> available, Function<SolsticeManifest, List<String>> neededFunc) {
			TreeMap<String, List<SolsticeManifest>> map = new TreeMap<>();
			for (SolsticeManifest bundle : bundles) {
				for (var needed : neededFunc.apply(bundle)) {
					if (!available.contains(needed)) {
						mapAdd(map, needed, bundle);
					}
				}
			}
			return map;
		}

		private Map<String, List<SolsticeManifest>> groupBundles(
				Function<SolsticeManifest, List<String>> groupBy) {
			TreeMap<String, List<SolsticeManifest>> map = new TreeMap<>();
			for (SolsticeManifest bundle : bundles) {
				for (String extracted : groupBy.apply(bundle)) {
					mapAdd(map, extracted, bundle);
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

		/**
		 * Sends warnings to logger, then modifies every manifest to resolve all these warnings.
		 *
		 * <ul>
		 *   <li>multiple bundles with same symbolic name (resolved by first one on the classpath, since
		 *       that's what <code>Class.forName</code> will do)
		 *   <li>multiple bundles which export the same package (resolved by first one on the classpath,
		 *       since that's what <code>Class.forName</code> will do)
		 *   <li>required bundle which is not present (resolved by removing requirement from the
		 *       SolsticeManifest)
		 *   <li>imported package which is not present (resolved by removing import from the
		 *       SolsticeManifest)
		 * </ul>
		 */
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
							int numSplitPkgs = 0;
							for (SolsticeManifest manifest : manifests) {
								if (pkg.startsWith("META-INF.versions.")) {
									// just an artifact of multijar
									return;
								}
								var element =
										manifest.pkgExportsRaw().stream()
												.filter(e -> e.getValue().equals(pkg))
												.findFirst()
												.get();
								String mandatory = element.getDirective("mandatory");
								if (mandatory != null) {
									if ("split".equals(element.getAttribute(mandatory))) {
										++numSplitPkgs;
									}
								}
							}
							if (numSplitPkgs < manifests.size() - 1) {
								logger.warn("Multiple bundles exporting the same package: " + pkg);
								for (SolsticeManifest manifest : manifests) {
									logger.warn("  - " + manifest.jarUrl);
								}
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
						if (missing.equals("kotlin") || missing.startsWith("kotlin.")) {
							return;
						}
						if (missing.startsWith("javax.") || missing.startsWith("java.")) {
							return;
						}
						if (missing.startsWith("org.xml.") || missing.startsWith("org.w3c.")) {
							return;
						}
						logger.warn("Missing imported package " + missing + " imported by " + neededBy);
					});
			for (var bundle : bundles) {
				bundle.requiredBundles.removeAll(missingBundles.keySet());
				bundle.pkgImports.removeAll(missingPackages.keySet());
			}
		}
	}

	/** Finds every OSGi bundle on the classpath and returns it inside a {@link BundleSet}. */
	public static BundleSet discoverBundles() {
		Enumeration<URL> manifestURLs =
				Unchecked.get(() -> SolsticeManifest.class.getClassLoader().getResources(MANIFEST_PATH));

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

	public static final String MANIFEST_PATH = "META-INF/MANIFEST.MF";
	public static final String SLASH_MANIFEST_PATH = "/" + MANIFEST_PATH;

	private final String jarUrl;
	private final int classpathOrder;
	private final @Nullable String symbolicName;
	private final LinkedHashMap<String, String> headersOriginal = new LinkedHashMap<>();
	private final List<String> requiredBundles;
	private final List<String> pkgImports;
	private final List<String> pkgExports;

	SolsticeManifest(URL manifestURL, int classpathOrder) {
		this.classpathOrder = classpathOrder;
		var externalForm = manifestURL.toExternalForm();
		if (!externalForm.endsWith(SLASH_MANIFEST_PATH)) {
			throw new IllegalArgumentException(
					"Expected manifest to end with " + SLASH_MANIFEST_PATH + " but was " + externalForm);
		}
		jarUrl = externalForm.substring(0, externalForm.length() - SLASH_MANIFEST_PATH.length());
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

		var symbolicNameRaw = parseAndStrip(Constants.BUNDLE_SYMBOLICNAME);
		symbolicName = symbolicNameRaw.isEmpty() ? null : symbolicNameRaw.get(0);

		requiredBundles = parseAndStrip(Constants.REQUIRE_BUNDLE);

		pkgExports = parseAndStrip(Constants.EXPORT_PACKAGE);
		pkgImports = parseAndStrip(Constants.IMPORT_PACKAGE);
		// if we export a package, we don't actually have to import it, that's just for letting
		// multiple bundles define the same classes, which is a dubious feature to support
		// https://access.redhat.com/documentation/en-us/red_hat_jboss_fuse/6.3/html/managing_osgi_dependencies/importexport
		pkgImports.removeAll(pkgExports);
	}

	private List<ManifestElement> pkgExportsRaw;

	private List<ManifestElement> pkgExportsRaw() {
		if (pkgExportsRaw == null) {
			pkgExportsRaw =
					Arrays.asList(
							Unchecked.get(
									() ->
											ManifestElement.parseHeader(
													Constants.EXPORT_PACKAGE,
													headersOriginal.get(Constants.EXPORT_PACKAGE))));
		}
		return pkgExportsRaw;
	}

	private boolean isNotFragment() {
		return !headersOriginal.containsKey(Constants.FRAGMENT_HOST);
	}

	public String getSymbolicName() {
		return symbolicName;
	}

	public String getJarUrl() {
		return jarUrl;
	}

	private List<String> parseAndStrip(String key) {
		String attribute = headersOriginal.get(key);
		if (attribute == null) {
			return Collections.emptyList();
		}
		return parseAndStripManifestHeader(key, attribute);
	}

	@Override
	public String toString() {
		if (symbolicName != null) {
			return symbolicName;
		} else {
			return jarUrl;
		}
	}

	/**
	 * Parses a jar manifest header, ignoring versions and removing anything with <code>
	 * resolution:=optional</code>.
	 */
	static List<String> parseAndStripManifestHeader(String key, String in) {
		try {
			ManifestElement[] elements = ManifestElement.parseHeader(key, in);
			List<String> stripped = new ArrayList<>(elements.length);
			for (var e : elements) {
				if ("optional".equals(e.getDirective("resolution"))) {
					continue;
				}
				stripped.add(e.getValue());
			}
			// remove duplicate entries: e.g. commons-io exports the same packages at multiple versions
			if (stripped.size() > 1) {
				stripped.sort(Comparator.naturalOrder());
				var iter = stripped.iterator();
				var prev = iter.next();
				while (iter.hasNext()) {
					var next = iter.next();
					if (prev.equals(next)) {
						iter.remove();
					} else {
						prev = next;
					}
				}
			}
			return stripped;
		} catch (BundleException e) {
			throw Unchecked.wrap(e);
		}
	}

	public List<String> getRequiredBundles() {
		return Collections.unmodifiableList(requiredBundles);
	}

	public List<String> getPkgImports() {
		return Collections.unmodifiableList(pkgImports);
	}

	public List<String> getPkgExports() {
		return Collections.unmodifiableList(pkgExports);
	}

	/** Returns the original headers, unmodified by our parsing. */
	public Map<String, String> getHeadersOriginal() {
		return Collections.unmodifiableMap(headersOriginal);
	}
}
