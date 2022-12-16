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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.felix.atomos.Atomos;
import org.apache.felix.atomos.AtomosContent;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleException;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.launch.Framework;
import org.slf4j.simple.SimpleLogger;

public class IdeMainTest {
	public static void main(String[] args) throws InvalidSyntaxException, BundleException {
		System.setProperty(SimpleLogger.DEFAULT_LOG_LEVEL_KEY, "INFO");
		System.setProperty(SimpleLogger.LOG_FILE_KEY, "System.out");
		//		IdeMain.main(args);

		Atomos atomos = Atomos.newAtomos();
		// Set atomos.content.install to false to prevent automatic bundle installation
		Framework framework = atomos.newFramework(Map.of("atomos.content.install", "false"));
		// framework must be initialized before any bundles can be installed
		framework.init();
		List<Bundle> bundles = new ArrayList<>();
		for (AtomosContent content : atomos.getBootLayer().getAtomosContents()) {
			// The resulting bundle will use a bundle location of
			// "atomos:" + atomosContent.getAtomosLocation();
			bundles.add(content.install());
		}
		for (Bundle b : bundles) {
			System.out.println("b " + b.getSymbolicName());
			try {
				b.start();
			} catch (BundleException e) {
				if (e.getMessage() == null) {
					System.out.println("  problem: " + e.getClass());
				} else {
					String[] lines = e.getMessage().split("\n");
					System.out.println("  problem: " + lines[0]);
					for (int i = 1; i < Math.min(5, lines.length); ++i) {
						System.out.println("    " + lines[i]);
					}
				}
			}
		}
		// The installed bundles will not actually activate until the framework is started
		framework.start();
		IdeMainUi.main(framework.getBundleContext());
	}
}
