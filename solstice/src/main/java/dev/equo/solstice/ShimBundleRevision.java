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

import java.util.Objects;
import org.osgi.framework.Bundle;

class ShimBundleRevision extends Unimplemented.BundleRevision {
	private final Bundle bundle;

	public ShimBundleRevision(Bundle bundle) {
		this.bundle = Objects.requireNonNull(bundle);
	}

	@Override
	public Bundle getBundle() {
		return bundle;
	}
}
