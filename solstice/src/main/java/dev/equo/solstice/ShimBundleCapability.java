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

import org.osgi.framework.wiring.BundleRevision;

class ShimBundleCapability extends Unimplemented.BundleCapability {
	private final ShimBundleRevision revision;

	ShimBundleCapability(Solstice.ShimBundle bundle) {
		this.revision = new ShimBundleRevision(bundle);
	}

	@Override
	public BundleRevision getRevision() {
		return revision;
	}
}
