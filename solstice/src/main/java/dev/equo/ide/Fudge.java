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

import dev.equo.solstice.Solstice;
import java.util.List;

class Fudge {
	/** See {@link IdeMainUi#earlyStartupWorkaround(Solstice)} */
	static List<String> earlyStartupWorkaround() {
		return List.of("org.eclipse.tm.terminal.view.ui");
	}

	/**
	 * Called by {@link dev.equo.ide.BuildPluginIdeMain} to activate without their transitives before
	 * the application launches.
	 */
	static List<String> activateEagerWithoutTransitives() {
		return List.of(
				"com.github.gradusnikov.eclipse.plugin.assistai.main",
				"org.eclipse.egit.core",
				"org.eclipse.jsch.core");
	}
}
