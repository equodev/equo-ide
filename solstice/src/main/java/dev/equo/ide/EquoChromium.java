/*******************************************************************************
 * Copyright (c) 2023 EquoTech, Inc. and others.
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
import dev.equo.solstice.p2.P2Model;
import java.io.File;
import java.util.Collection;
import java.util.List;

/**
 * Utilities for setting up EquoChromium as the default browser implementation in the EquoIDE build
 * plugins.
 */
public class EquoChromium extends Catalog.PureMaven {

	private boolean enabled = false;

	EquoChromium() {
		super(
				"equoChromium",
				jre11("106.0.9"),
				List.of(
						"com.equo:com.equo.chromium:" + V,
						"com.equo:com.equo.chromium.cef." + SwtPlatform.getRunning() + ":" + V),
				PLATFORM);
	}

	public String mavenRepo() {
		return "https://dl.equo.dev/chromium-swt-ee/equo-gpl/mvn";
	}

	public boolean isEnabled(Collection<File> classpath) {
		if (classpath.stream().anyMatch(file -> file.getName().startsWith("com.equo.chromium"))) {
			this.enabled = true;
			return true;
		}
		return false;
	}

	public boolean isEnabled(P2Model model) {
		if (model.getPureMaven().stream()
				.anyMatch(coord -> coord.startsWith("com.equo:com.equo.chromium:"))) {
			this.enabled = true;
			return true;
		}
		return false;
	}

	public boolean isEnabled() {
		return this.enabled;
	}
}
