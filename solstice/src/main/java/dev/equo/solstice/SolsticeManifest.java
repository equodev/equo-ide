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
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.jar.Manifest;
import javax.annotation.Nullable;
import org.eclipse.osgi.util.ManifestElement;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleException;
import org.osgi.framework.Constants;

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
	public static final String MANIFEST_PATH = "META-INF/MANIFEST.MF";
	public static final String SLASH_MANIFEST_PATH = "/" + MANIFEST_PATH;

	private final String jarUrl;
	final int classpathOrder;
	private final @Nullable String symbolicName;
	private final LinkedHashMap<String, String> headersOriginal = new LinkedHashMap<>();
	private final List<String> requiredBundles;
	private final List<String> pkgImports;
	private final List<String> pkgExports;
	private final List<Capability> capProvides;
	private final List<Capability> capRequires;
	final boolean lazy;

	final List<SolsticeManifest> fragments = new ArrayList<>();

	Bundle hydrated;

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

		capProvides = parseAndStripCapability(Constants.PROVIDE_CAPABILITY);
		capRequires = parseAndStripCapability(Constants.REQUIRE_CAPABILITY);

		if (headersOriginal.containsKey(Constants.FRAGMENT_HOST)
				&& (!capRequires.isEmpty() || !capProvides.isEmpty())) {
			throw Unimplemented.onPurpose(
					"Solstice does not currently support OSGi capabilities in fragment bundles.");
		}

		String activationPolicy = headersOriginal.get(Constants.BUNDLE_ACTIVATIONPOLICY);
		lazy = activationPolicy == null ? false : activationPolicy.contains("lazy");
	}

	private List<Capability> parseAndStripCapability(String header) {
		try {
			String capability = headersOriginal.get(Constants.REQUIRE_CAPABILITY);
			if (capability == null) {
				return Collections.emptyList();
			}
			ManifestElement[] elements =
					ManifestElement.parseHeader(Constants.REQUIRE_CAPABILITY, capability);
			List<Capability> capabilities = new ArrayList<>(elements.length);
			for (ManifestElement element : elements) {
				if (Capability.IGNORED_NAMESPACES.contains(element.getValue())) {
					continue;
				}
				capabilities.add(new Capability(element));
			}
			if (capabilities.isEmpty()) {
				return Collections.emptyList();
			} else {
				return capabilities;
			}
		} catch (BundleException e) {
			throw Unchecked.wrap(e);
		}
	}

	static class Capability {
		private static Set<String> IGNORED_NAMESPACES = Set.of("osgi.ee");
		private static Set<String> IGNORED_ATTRIBUTES = Set.of("version:Version");

		String namespace;
		Map<String, String> attributes = new TreeMap<>();
		Map<String, String> directives = new TreeMap<>();

		public Capability(ManifestElement element) {
			namespace = element.getValue();
			var keys = element.getKeys();
			if (keys != null) {
				while (keys.hasMoreElements()) {
					String key = keys.nextElement();
					if (!IGNORED_ATTRIBUTES.contains(key)) {
						this.attributes.put(key, element.getAttribute(key));
					}
				}
			}
			var directives = element.getDirectiveKeys();
			if (directives != null) {
				while (directives.hasMoreElements()) {
					String key = directives.nextElement();
					this.directives.put(key, element.getDirective(key));
				}
			}
		}

		@Override
		public String toString() {
			return namespace + ": " + attributes + " " + directives;
		}
	}

	private List<ManifestElement> pkgExportsRaw;

	List<ManifestElement> pkgExportsRaw() {
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

	boolean isNotFragment() {
		return !headersOriginal.containsKey(Constants.FRAGMENT_HOST);
	}

	String fragmentHost() {
		var host = headersOriginal.get(Constants.FRAGMENT_HOST);
		if (host == null) {
			return null;
		}
		var idx = host.indexOf(';');
		return idx == -1 ? host : host.substring(0, idx);
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
		return total(m -> m.requiredBundles);
	}

	public List<String> getPkgImports() {
		return total(m -> m.pkgImports);
	}

	public List<String> getPkgExports() {
		return total(m -> m.pkgExports);
	}

	private List<String> total(Function<SolsticeManifest, List<String>> getter) {
		if (fragments.isEmpty()) {
			return Collections.unmodifiableList(getter.apply(this));
		} else {
			var total = new ArrayList<String>();
			total.addAll(getter.apply(this));
			for (var fragment : fragments) {
				total.addAll(getter.apply(fragment));
			}
			return total;
		}
	}

	/** Returns the original headers, unmodified by our parsing. */
	public Map<String, String> getHeadersOriginal() {
		return Collections.unmodifiableMap(headersOriginal);
	}

	void removeFromRequiredBundles(Collection<String> toRemove) {
		requiredBundles.removeAll(toRemove);
	}

	void removeFromPkgImports(Collection<String> toRemove) {
		pkgImports.removeAll(toRemove);
	}
}
