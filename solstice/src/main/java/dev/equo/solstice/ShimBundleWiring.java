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
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import org.osgi.framework.namespace.HostNamespace;
import org.osgi.framework.wiring.BundleWire;
import org.osgi.framework.wiring.BundleWiring;

class ShimBundleWiring extends Unimplemented.BundleWiring {
	private final BundleContextSolstice.ShimBundle bundle;

	ShimBundleWiring(BundleContextSolstice.ShimBundle bundle) {
		this.bundle = Objects.requireNonNull(bundle);
	}

	@Override
	public List<BundleWire> getRequiredWires(String namespace) {
		if (namespace.equals(HostNamespace.HOST_NAMESPACE)) {
			var host = bundle.fragmentHostBundle();
			if (host != null) {
				return Collections.singletonList(
						new Unimplemented.BundleWire() {
							@Override
							public BundleWiring getProviderWiring() {
								return host.adapt(BundleWiring.class);
							}
						});
			}
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
		List<String> asStrings = new ArrayList<>();
		listResourcesHelper(asStrings, bundle, path, filePattern, recurse);
		return asStrings;
	}

	private static void listResourcesHelper(
			List<String> asStrings,
			BundleContextSolstice.ShimBundle bundle,
			String path,
			String filePattern,
			boolean recurse) {
		var urls = bundle.findEntries(path, filePattern, recurse);
		while (urls.hasMoreElements()) {
			var url = urls.nextElement();
			asStrings.add(url.toExternalForm());
		}
		for (var required : bundle.manifest.getRequiredBundles()) {
			// TODO: this should respect whether a required bundle is re-exported or not
			listResourcesHelper(
					asStrings,
					bundle.getBundleContext().bundleForSymbolicName(required),
					path,
					filePattern,
					recurse);
		}
	}

	@Override
	public boolean isInUse() {
		return true;
	}

	@Override
	public List<BundleWire> getProvidedWires(String namespace) {
		return Collections.emptyList();
	}
}
