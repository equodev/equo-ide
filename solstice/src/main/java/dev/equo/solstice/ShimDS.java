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

import java.util.Collections;
import java.util.List;
import org.osgi.framework.wiring.BundleWire;

class ShimDS {

	static class BundleWiring extends Unimplemented.BundleWiring {
		@Override
		public List<BundleWire> getRequiredWires(String namespace) {
			return Collections.emptyList();
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
}
