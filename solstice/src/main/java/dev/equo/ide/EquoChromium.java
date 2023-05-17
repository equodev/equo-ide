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
package dev.equo.ide;

import com.diffplug.common.swt.os.SwtPlatform;

/**
 * Utilities for setting up EquoChromium as the default browser implementation in the EquoIDE build
 * plugins.
 */
public class EquoChromium implements IdeHook {
	public static String mavenRepo() {
		return "https://dl.equo.dev/chromium-swt-ee/equo-gpl/mvn";
	}

	public static java.util.List<String> mavenCoordinates() {
		return java.util.List.of(
				"com.equo:com.equo.chromium:106.0.3",
				"com.equo:com.equo.chromium.cef." + SwtPlatform.getRunning() + ":106.0.3");
	}

	public static boolean isEnabled(IdeHook.List hooks) {
		return hooks.stream().anyMatch(hook -> hook instanceof EquoChromium);
	}

	@Override
	public IdeHookInstantiated instantiate() throws Exception {
		return new Instantiated();
	}

	class Instantiated implements IdeHookInstantiated {}
}
