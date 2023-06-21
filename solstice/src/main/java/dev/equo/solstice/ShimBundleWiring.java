/*******************************************************************************
 * Copyright (c) 2022-2023 EquoTech, Inc. and others.
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
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import org.osgi.framework.namespace.HostNamespace;
import org.osgi.framework.wiring.BundleWire;
import org.osgi.framework.wiring.BundleWiring;

class ShimBundleWiring extends Unimplemented.BundleWiring {
	private final ShimBundle bundle;

	ShimBundleWiring(ShimBundle bundle) {
		this.bundle = Objects.requireNonNull(bundle);
	}

	@Override
	public List<BundleWire> getRequiredWires(String namespace) {
		if (namespace.equals(HostNamespace.HOST_NAMESPACE) && bundle.manifest.isFragment()) {
			var host =
					bundle.getRootBundleContext().bundleForSymbolicName(bundle.manifest.fragmentHost());
			return Collections.singletonList(
					new Unimplemented.BundleWire() {
						@Override
						public BundleWiring getProviderWiring() {
							return host.adapt(BundleWiring.class);
						}
					});
		}
		return Collections.emptyList();
	}

	@Override
	public org.osgi.framework.wiring.BundleRevision getRevision() {
		return new ShimBundleRevision(bundle);
	}

	@Override
	public Collection<String> listResources(String path, String filePattern, int options) {
		boolean recurse = (options & LISTRESOURCES_RECURSE) == LISTRESOURCES_RECURSE;
		var finder = new ShimBundle.Finder(path, filePattern, recurse);
		List<URL> urls = new ArrayList<>();

		var alreadySearched = new HashSet<String>();
		var toSearch = new ArrayDeque<ShimBundle>();
		toSearch.add(this.bundle);
		alreadySearched.add(this.bundle.manifest.getSymbolicName());

		while (!toSearch.isEmpty()) {
			finder.addEntriesIn(urls, toSearch.poll());
			for (var required : bundle.manifest.totalRequiredBundles()) {
				// TODO: this should respect whether a required bundle is re-exported or not
				if (!alreadySearched.add(required)) {
					toSearch.add(bundle.getRootBundleContext().bundleForSymbolicName(required));
				}
			}
		}

		List<String> strings = new ArrayList<>(urls.size());
		for (var url : urls) {
			strings.add(url.toExternalForm());
		}
		return strings;
	}

	@Override
	public boolean isInUse() {
		return true;
	}

	@Override
	public List<BundleWire> getProvidedWires(String namespace) {
		return Collections.emptyList();
	}

	@Override
	public ClassLoader getClassLoader() {
		return ShimBundleWiring.class.getClassLoader();
	}
}
