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

import dev.equo.solstice.BundleContextSolstice;
import dev.equo.solstice.BundleSet;
import dev.equo.solstice.SolsticeInit;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.osgi.framework.Bundle;
import org.slf4j.simple.SimpleLogger;

public class SolsticeSmokeTest {
	@Test
	public void bundleIds() {
		System.setProperty(SimpleLogger.DEFAULT_LOG_LEVEL_KEY, "WARN");
		var bundleSet = BundleSet.discoverOnClasspath();
		var solstice =
				BundleContextSolstice.open(new SolsticeInit(BuildPluginIdeMain.defaultDir()), bundleSet);
		Assertions.assertThat(solstice.systemBundle().getBundleId()).isEqualTo(0);
		int count = 0;
		for (Bundle bundle : solstice.getBundles()) {
			++count;
			Assertions.assertThat(bundle.getBundleId()).isEqualTo(count);
		}
		Assertions.assertThat(count).isGreaterThan(10);
	}
}
