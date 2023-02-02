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
	final List<String> requiredBundles;
	final List<String> pkgImports;
	private final List<String> pkgExports;
	final boolean lazy;

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

		String activationPolicy = headersOriginal.get(Constants.BUNDLE_ACTIVATIONPOLICY);
		lazy = activationPolicy == null ? false : activationPolicy.contains("lazy");
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
