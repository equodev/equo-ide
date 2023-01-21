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

import java.io.IOException;
import org.osgi.framework.BundleException;
import org.osgi.framework.InvalidSyntaxException;
import org.slf4j.simple.SimpleLogger;

public class IdeMainBuildPluginTest {
	public static void main(String[] args)
			throws InvalidSyntaxException, BundleException, IOException {
		boolean useAtomos = false;

		System.setProperty(SimpleLogger.DEFAULT_LOG_LEVEL_KEY, "INFO");
		System.setProperty(SimpleLogger.LOG_FILE_KEY, "System.out");

		BuildPluginIdeMain.main(
				new String[] {
					"-installDir",
					BuildPluginIdeMain.defaultDir().getAbsolutePath(),
					"-useAtomos",
					Boolean.toString(useAtomos),
					"-initOnly",
					"false",
					"-debugClasspath",
					BuildPluginIdeMain.DebugClasspath.disabled.name()
				});
	}
}
