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
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.jar.Manifest;
import javax.annotation.Nullable;
import org.eclipse.osgi.util.ManifestElement;
import org.jetbrains.annotations.NotNull;
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
	final List<Capability> capProvides;
	final List<Capability> capRequires;
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

		capProvides =
				parseCapability(Constants.PROVIDE_CAPABILITY, SolsticeManifest::parseProvideCapability);
		capRequires =
				parseCapability(Constants.REQUIRE_CAPABILITY, SolsticeManifest::parseRequireCapability);
		if (!capProvides.isEmpty()) {
			System.out.println("++ " + symbolicName + " provides");
			for (var cap : capProvides) {
				System.out.println(cap.toString());
			}
		}
		if (!capRequires.isEmpty()) {
			System.out.println("-- " + symbolicName + " requires");
			for (var cap : capRequires) {
				System.out.println(cap.toString());
			}
		}
		if (headersOriginal.containsKey(Constants.FRAGMENT_HOST)
				&& (!capRequires.isEmpty() || !capProvides.isEmpty())) {
			throw Unimplemented.onPurpose(
					"Solstice does not currently support OSGi capabilities in fragment bundles, but a PR is welcome.");
		}

		String activationPolicy = headersOriginal.get(Constants.BUNDLE_ACTIVATIONPOLICY);
		lazy = activationPolicy == null ? false : activationPolicy.contains("lazy");
	}

	private static void parseProvideCapability(CapabilityParsed parsed, ArrayList<Capability> total) {
		for (var attr : parsed.attributes.entrySet()) {
			var key = attr.getKey();
			if (key.endsWith(Capability.LIST_STR)) {
				key = key.substring(0, key.length() - Capability.LIST_STR.length());
				String[] values = attr.getValue().split(",");
				for (String value : values) {
					total.add(new Capability(parsed.namespace, key, value));
				}
			} else {
				total.add(new Capability(parsed.namespace, key, attr.getValue()));
			}
		}
	}

	private static void parseRequireCapability(CapabilityParsed parsed, ArrayList<Capability> total) {
		var filter = parsed.directives.get(Constants.FILTER_DIRECTIVE);
		int equalsSpot = filter.indexOf('=');
		if (!filter.startsWith("(")
				|| !filter.endsWith(")")
				|| equalsSpot == -1
				|| filter.indexOf('=', equalsSpot + 1) != -1) {
			throw Unimplemented.onPurpose(
					"Require-Capability supports (key=value) only, this was " + filter);
		}
		String key = filter.substring(1, equalsSpot);
		String value = filter.substring(equalsSpot + 1, filter.length() - 1);
		total.add(new Capability(parsed.namespace, key, value));
	}

	private List<Capability> parseCapability(
			String header, BiConsumer<CapabilityParsed, ArrayList<Capability>> parser) {
		var parsed = parseAndStripCapability(header);
		if (parsed.isEmpty()) {
			return Collections.emptyList();
		}
		var capabilities = new ArrayList<Capability>();
		var raws = parseAndStripCapability(header);
		for (var raw : raws) {
			parser.accept(raw, capabilities);
		}
		return capabilities;
	}

	private List<CapabilityParsed> parseAndStripCapability(String header) {
		try {
			String capability = headersOriginal.get(header);
			if (capability == null) {
				return Collections.emptyList();
			}
			// org.eclipse.ecf.identity has these gunky quotes
			capability = capability.replace('”', '"');
			ManifestElement[] elements = ManifestElement.parseHeader(header, capability);
			List<CapabilityParsed> capabilities = new ArrayList<>(elements.length);
			for (ManifestElement element : elements) {
				if (Capability.IGNORED_NAMESPACES.contains(element.getValue())) {
					continue;
				}
				capabilities.add(new CapabilityParsed(element));
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

	public static class Capability implements Comparable<Capability> {
		private static final String LIST_STR = ":List<String>";
		private static final Set<String> IGNORED_NAMESPACES = Set.of("osgi.ee");
		private static final Set<String> IGNORED_ATTRIBUTES = Set.of("version:Version");

		final String namespace;
		final String key;
		final String value;

		public Capability(String namespace, String key, String value) {
			this.namespace = namespace;
			this.key = key;
			this.value = value;
		}

		@Override
		public int compareTo(@NotNull Capability o) {
			int result = namespace.compareTo(o.namespace);
			if (result != 0) {
				return result;
			}
			result = key.compareTo(o.key);
			if (result != 0) {
				return result;
			}
			return value.compareTo(value);
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			return o instanceof Capability ? compareTo((Capability) o) == 0 : false;
		}

		@Override
		public int hashCode() {
			return Objects.hash(namespace, key, value);
		}

		@Override
		public String toString() {
			return namespace + ":" + key + "=" + value;
		}
	}

	private static class CapabilityParsed {
		String namespace;
		Map<String, String> attributes = new TreeMap<>();
		Map<String, String> directives = new TreeMap<>();

		public CapabilityParsed(ManifestElement element) {
			namespace = element.getValue();
			var keys = element.getKeys();
			if (keys != null) {
				while (keys.hasMoreElements()) {
					String key = keys.nextElement();
					if (!Capability.IGNORED_ATTRIBUTES.contains(key)) {
						this.attributes.put(key, element.getAttribute(key));
					}
				}
			}
			var directives = element.getDirectiveKeys();
			if (directives != null) {
				while (directives.hasMoreElements()) {
					String key = directives.nextElement();
					if (key.equals(Constants.FILTER_DIRECTIVE)) {
						this.directives.put(
								Constants.FILTER_DIRECTIVE,
								stripVersionsFromFilter(element.getDirective(Constants.FILTER_DIRECTIVE)));
					} else {
						this.directives.put(key, element.getDirective(key));
					}
				}
			}
		}

		private String stripVersionsFromFilter(String filter) {
			var removeVersionGt = filter.replaceAll("\\(version>=(.*?)\\)", "");
			var removeEmptyNots = removeVersionGt.replace("(!)", "");
			if (removeEmptyNots.startsWith("(&(") && removeEmptyNots.endsWith("))")) {
				return removeEmptyNots.substring(2, removeEmptyNots.length() - 1);
			} else {
				return removeEmptyNots;
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
				List<String> toAdd = getter.apply(fragment);
				for (String e : toAdd) {
					if (!total.contains(e)) {
						total.add(e);
					}
				}
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
