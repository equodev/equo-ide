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
import java.net.URL;
import org.eclipse.osgi.service.datalocation.Location;
import org.osgi.framework.BundleContext;

class ShimLocation extends Unimplemented.Location {
	static void set(BundleContext context, URL dir, String type) {
		context.registerService(
				Location.class,
				new ShimLocation(dir),
				Dictionaries.of(SERVICE_PROPERTY_TYPE, type, "url", dir.toExternalForm()));
	}

	final URL url;

	ShimLocation(URL url) {
		this.url = url;
	}

	@Override
	public URL getURL() {
		return url;
	}

	@Override
	public Location getParentLocation() {
		return null;
	}

	@Override
	public boolean isReadOnly() {
		return false;
	}

	@Override
	public boolean isSet() {
		return true;
	}

	@Override
	public URL getDataArea(String path) throws IOException {
		return new URL(url.toExternalForm() + path);
	}

	@Override
	public String toString() {
		return "ShimLocation[" + url + "]";
	}
}
