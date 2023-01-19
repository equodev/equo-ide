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

public class BrandingIdeHook implements IdeHook {
	@Override
	public IdeHookInstantiated instantiate() {
		return new Instantiated();
	}

	class Instantiated implements IdeHookInstantiated {
		private transient long epoch;

		@Override
		public void beforeOsgi() {
			epoch = System.currentTimeMillis();
		}

		private void printMs(String label) {
			System.out.println("%%% " + label + " " + (System.currentTimeMillis() - epoch) + "ms");
		}

		@Override
		public void afterOsgi(org.osgi.framework.BundleContext context) {
			printMs("afterOsgi");
		}

		@Override
		public void beforeDisplay() {
			printMs("beforeDisplay");
		}

		@Override
		public void afterDisplay(org.eclipse.swt.widgets.Display display) {
			printMs("afterDisplay");
		}

		@Override
		public void initialize() {
			printMs("initialize");
		}

		@Override
		public void preStartup() {
			printMs("preStartup");
		}

		@Override
		public void postStartup() {
			printMs("postStartup");
		}

		@Override
		public boolean preShutdown() {
			printMs("preShutdown");
			return true;
		}

		@Override
		public void postShutdown() {
			printMs("postShutdown");
		}
	}
}
