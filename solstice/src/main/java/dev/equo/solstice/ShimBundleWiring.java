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
	private final Solstice.ShimBundle bundle;

	ShimBundleWiring(Solstice.ShimBundle bundle) {
		this.bundle = Objects.requireNonNull(bundle);
	}

	@Override
	public List<BundleWire> getRequiredWires(String namespace) {
		if (namespace.equals(HostNamespace.HOST_NAMESPACE)) {
			var host = bundle.fragmentHostBundle();
			if (host != null) {
				return Collections.singletonList(new Unimplemented.BundleWire() {
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
		var urls = bundle.findEntries(path, filePattern, recurse);
		List<String> asStrings = new ArrayList<>();
		while (urls.hasMoreElements()) {
			var url = urls.nextElement();
			asStrings.add(url.toExternalForm());
		}
		return asStrings;
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
