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
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.function.Function;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Represents a closed universe of OSGi bundles. */
public class Solstice {
	/**
	 * We have at least one case where a bundle requires another bundle but that requirement is not
	 * expressed in the manifest. This map is an easy way to fix cases such as this.
	 */
	private static Map<String, List<String>> knownMissingBundleDependencies() {
		return Map.of(
				"org.eclipse.equinox.p2.reconciler.dropins", List.of("org.eclipse.equinox.p2.updatesite"));
	}

	public static Solstice findBundlesOnClasspath() {
		Enumeration<URL> manifestURLs =
				Unchecked.get(
						() ->
								SolsticeManifest.class
										.getClassLoader()
										.getResources(SolsticeManifest.MANIFEST_PATH));

		int classpathOrder = 0;
		List<SolsticeManifest> manifests = new ArrayList<>();
		while (manifestURLs.hasMoreElements()) {
			var manifest = new SolsticeManifest(manifestURLs.nextElement(), ++classpathOrder);
			if (manifest.getSymbolicName() != null) {
				manifests.add(manifest);
			}
		}
		return new Solstice(manifests);
	}

	private final Logger logger = LoggerFactory.getLogger(Solstice.class);
	private final List<SolsticeManifest> bundles;

	private final TreeSet<String> pkgs = new TreeSet<>();
	private final Capability.SupersetSet caps = new Capability.SupersetSet();
	private BundleContext context;

	private Solstice(List<SolsticeManifest> bundles) {
		this.bundles = bundles;
		for (var fragment : bundles) {
			var host = fragment.fragmentHost();
			if (host != null) {
				var hostBundle = bundleByName(host);
				if (hostBundle == null) {
					throw new IllegalArgumentException("Fragment " + fragment + " needs missing " + host);
				}
				hostBundle.fragments.add(fragment);
			}
		}

		var glassfish = bundleByName(GLASSFISH);
		var jdtCore = bundleByName(JDT_CORE);
		if (glassfish != null && jdtCore != null) {
			if (glassfish.classpathOrder < jdtCore.classpathOrder) {
				throw new IllegalArgumentException(
						"Eclipse has multiple jars which define `ICompilationUnit`, and it is important for the classpath order to put "
								+ JDT_CORE
								+ " before "
								+ GLASSFISH);
			}
		}
	}

	private static final String GLASSFISH = "org.apache.jasper.glassfish";
	private static final String JDT_CORE = "org.eclipse.jdt.core";

	public Map<String, List<SolsticeManifest>> bySymbolicName() {
		return groupBundlesIncludeFragments(
				true, manifest -> Collections.singletonList(manifest.getSymbolicName()));
	}

	public Map<String, List<SolsticeManifest>> byExportedPackage() {
		return groupBundlesIncludeFragments(false, SolsticeManifest::totalPkgExports);
	}

	public Map<String, List<SolsticeManifest>> calculateMissingBundles(Set<String> available) {
		return calculateMissingNoFragments(available, SolsticeManifest::totalRequiredBundles);
	}

	public Map<String, List<SolsticeManifest>> calculateMissingPackages(Set<String> available) {
		return calculateMissingNoFragments(available, SolsticeManifest::totalPkgImports);
	}

	private Map<String, List<SolsticeManifest>> calculateMissingNoFragments(
			Set<String> available, Function<SolsticeManifest, List<String>> neededFunc) {
		TreeMap<String, List<SolsticeManifest>> map = new TreeMap<>();
		for (SolsticeManifest bundle : bundles) {
			if (!bundle.isFragment()) {
				for (var needed : neededFunc.apply(bundle)) {
					if (!available.contains(needed)) {
						mapAdd(map, needed, bundle);
					}
				}
			}
		}
		return map;
	}

	private Map<String, List<SolsticeManifest>> groupBundlesIncludeFragments(
			boolean includeFragments, Function<SolsticeManifest, List<String>> groupBy) {
		TreeMap<String, List<SolsticeManifest>> map = new TreeMap<>();
		for (SolsticeManifest bundle : bundles) {
			if (includeFragments || !bundle.isFragment()) {
				for (String extracted : groupBy.apply(bundle)) {
					mapAdd(map, extracted, bundle);
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
	public void warnAndModifyManifestsToFix() {
		Logger logger = LoggerFactory.getLogger(Solstice.class);
		var bySymbolicName = bySymbolicName();

		var missingMap = knownMissingBundleDependencies();
		for (var entry : missingMap.entrySet()) {
			var inSystem = bySymbolicName.get(entry.getKey());
			if (inSystem != null) {
				for (var manifest : inSystem) {
					for (var missing : entry.getValue()) {
						if (!manifest.requiredBundles.contains(missing)) {
							logger.info(
									"Modifying " + manifest.getSymbolicName() + " to add required bundle " + missing);
							manifest.requiredBundles.add(missing);
						}
					}
				}
			}
		}

		var byExportedPackage = byExportedPackage();
		bySymbolicName.forEach(
				(symbolicName, manifests) -> {
					if (manifests.size() > 1) {
						logger.warn("Multiple bundles with the same symbolic name: " + symbolicName);
						for (SolsticeManifest manifest : manifests) {
							logger.warn("  - " + manifest.getJarUrl());
						}
					}
				});
		byExportedPackage.forEach(
				(pkg, manifests) -> {
					if (pkg.startsWith("META-INF.versions.")) {
						// just an artifact of multijar
						return;
					}
					if (manifests.size() > 1) {
						int okayToExport = 1;
						for (SolsticeManifest manifest : manifests) {
							if (pkgExportIsNotDuplicate(pkg, manifest, manifests)) {
								++okayToExport;
							}
						}
						if (manifests.size() > okayToExport) {
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
					logger.warn("Missing imported package " + missing + " needed by " + neededBy);
				});
		for (var bundle : bundles) {
			bundle.removeFromRequiredBundles(missingBundles.keySet());
			bundle.removeFromPkgImports(missingPackages.keySet());
		}

		// warn about missing requirements
		var allCapabilities = new Capability.SupersetSet();
		for (var bundle : bundles) {
			allCapabilities.addAll(bundle.capProvides);
		}
		var missingCapability = new TreeSet<Capability>();
		for (var bundle : bundles) {
			for (var cap : bundle.capRequires) {
				if (!allCapabilities.containsAnySupersetOf(cap)) {
					logger.warn("Missing required capability " + cap + " needed by " + bundle);
					missingCapability.add(cap);
				}
			}
		}
		for (var bundle : bundles) {
			bundle.removeRequiredCapabilities(missingCapability);
		}
	}

	private boolean pkgExportIsNotDuplicate(
			String pkg, SolsticeManifest thisManifest, List<SolsticeManifest> allManifestsForPkg) {
		if (thisManifest.totalPkgImports().contains(pkg)) {
			return true;
		}
		var element =
				thisManifest.pkgExportsRaw().stream()
						.filter(e -> e.getValue().equals(pkg))
						.findFirst()
						.get();
		String mandatory = element.getDirective("mandatory");
		if (mandatory != null) {
			if ("split".equals(element.getAttribute(mandatory))) {
				return true;
			}
		}
		String uses = element.getDirective("uses");
		if (uses != null) {
			return true;
		}
		String friends = element.getDirective("x-friends");
		if (friends != null) {
			for (String friend : friends.split(",")) {
				for (SolsticeManifest otherManifest : allManifestsForPkg) {
					if (otherManifest != thisManifest && otherManifest.getSymbolicName().equals(friend)) {
						return true;
					}
				}
			}
		}
		return false;
	}

	private void assertContextInitialized(boolean isInitialized) {
		if (isInitialized) {
			if (context == null) {
				throw new IllegalStateException("Call `openAtomos` or `openSolstice` first");
			}
		} else {
			if (context != null) {
				throw new IllegalStateException("`openAtomos` or `openSolstice` can only be called once");
			}
		}
	}

	public void openAtomos(Map<String, String> props) throws BundleException {
		assertContextInitialized(false);
		// the spelled-out package is on purpose so that Atomos can remain an optional
		// component
		// works together with
		// https://github.com/equodev/equo-ide/blob/aa7d30cba9988bc740ff4bc4b3015475d30d187c/solstice/build.gradle#L16-L22
		context = dev.equo.solstice.BundleContextAtomos.hydrate(this, props);
	}

	public void openShim(Map<String, String> props) {
		assertContextInitialized(false);
		context = BundleContextShim.hydrate(this, props);
	}

	public BundleContext getContext() {
		assertContextInitialized(true);
		return context;
	}

	/** Hydrates the bundle field of all manifests from the given context. */
	void hydrateFrom(Function<SolsticeManifest, Bundle> bundleCreator) {
		for (var bundle : bundles) {
			bundle.hydrated = bundleCreator.apply(bundle);
		}
	}

	/** Starts all hydrated manfiests. */
	public void startAllWithLazy(boolean lazyValue) {
		for (var solstice : bundles) {
			if (!solstice.isFragment() && solstice.lazy == lazyValue) {
				start(solstice);
			}
		}
	}

	private Set<SolsticeManifest> activatingBundles = new HashSet<>();

	public void start(String symbolicName) {
		for (var bundle : bundles) {
			if (bundle.getSymbolicName().equals(symbolicName)) {
				start(bundle);
			}
		}
	}

	public void start(SolsticeManifest manifest) {
		boolean newAddition = activatingBundles.add(manifest);
		if (!newAddition) {
			return;
		}
		logger.info("prepare {}", manifest);
		pkgs.addAll(manifest.totalPkgExports());
		caps.addAll(manifest.capProvides);
		String pkg;
		while ((pkg = missingPkg(manifest)) != null) {
			var bundles = unactivatedBundlesForPkg(pkg);
			if (bundles.isEmpty()) {
				throw new IllegalArgumentException(manifest + " imports missing package " + pkg);
			} else {
				for (var bundle : bundles) {
					start(bundle);
				}
			}
		}
		Capability cap;
		while ((cap = missingCap(manifest)) != null) {
			var bundles = unactivatedBundlesForCap(cap);
			if (bundles.isEmpty()) {
				throw new IllegalArgumentException(manifest + " requires missing capability " + cap);
			} else {
				for (var bundle : bundles) {
					start(bundle);
				}
			}
		}
		for (var required : manifest.totalRequiredBundles()) {
			var bundle = bundleByName(required);
			if (bundle == null) {
				throw new IllegalArgumentException(manifest + " required missing bundle " + bundle);
			} else {
				start(bundle);
			}
		}
		// this happens when multiple with same version
		try {
			logger.info("activate {}", manifest);
			manifest.hydrated.start();
		} catch (BundleException e) {
			logger.warn("error in " + manifest, e);
		}
	}

	private Capability missingCap(SolsticeManifest manifest) {
		for (var cap : manifest.capRequires) {
			if (!caps.containsAnySupersetOf(cap)) {
				return cap;
			}
		}
		return null;
	}

	private List<SolsticeManifest> unactivatedBundlesForCap(Capability targetCap) {
		Object bundlesForCap = null;
		for (var bundle : bundles) {
			if (bundle.isFragment() || activatingBundles.contains(bundle)) {
				// targetCap wouldn't be missing if this bundle had it
				continue;
			}
			if (targetCap.isSubsetOfElementIn(bundle.capProvides)) {
				bundlesForCap = fastAdd(bundlesForCap, bundle);
			}
		}
		return fastAddGet(bundlesForCap);
	}

	private String missingPkg(SolsticeManifest manifest) {
		for (var pkg : manifest.totalPkgImports()) {
			if (!pkgs.contains(pkg)) {
				return pkg;
			}
		}
		return null;
	}

	private List<SolsticeManifest> unactivatedBundlesForPkg(String targetPkg) {
		Object bundlesForPkg = null;
		for (var bundle : bundles) {
			if (bundle.isFragment() || activatingBundles.contains(bundle)) {
				// targetPkg wouldn't be missing if this bundle had it
				continue;
			}
			if (bundle.totalPkgExports().contains(targetPkg)) {
				bundlesForPkg = fastAdd(bundlesForPkg, bundle);
			}
		}
		return fastAddGet(bundlesForPkg);
	}

	@SuppressWarnings("unchecked")
	private Object fastAdd(Object currentValue, SolsticeManifest toAdd) {
		if (currentValue == null) {
			return toAdd;
		} else if (currentValue instanceof ArrayList) {
			((ArrayList<SolsticeManifest>) currentValue).add(toAdd);
			return currentValue;
		} else {
			var list = new ArrayList<SolsticeManifest>();
			list.add((SolsticeManifest) currentValue);
			list.add(toAdd);
			return list;
		}
	}

	@SuppressWarnings("unchecked")
	private List<SolsticeManifest> fastAddGet(Object currentValue) {
		if (currentValue == null) {
			return Collections.emptyList();
		} else if (currentValue instanceof ArrayList) {
			return (ArrayList<SolsticeManifest>) currentValue;
		} else {
			return Collections.singletonList((SolsticeManifest) currentValue);
		}
	}

	SolsticeManifest bundleByName(String name) {
		for (var bundle : bundles) {
			if (name.equals(bundle.getSymbolicName())) {
				return bundle;
			}
		}
		return null;
	}
}
