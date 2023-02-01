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
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.function.Function;
import org.slf4j.Logger;

/** Represents a closed universe of OSGi bundles. */
public class BundleSet {
	private final List<SolsticeManifest> bundles;

	BundleSet(List<SolsticeManifest> bundles) {
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
						long numNonFragments = bundles.stream().filter(SolsticeManifest::isNotFragment).count();
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
							logger.warn("  - " + manifest.getJarUrl());
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
							String uses = element.getDirective("uses");
							if (uses != null) {
								++numSplitPkgs;
							}
						}
						if (numSplitPkgs < manifests.size() - 1) {
							logger.warn("Multiple bundles exporting the same package: " + pkg);
							for (SolsticeManifest manifest : manifests) {
								logger.warn("  - " + manifest.getJarUrl());
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
