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

import java.io.File;
import java.util.Arrays;
import org.osgi.framework.InvalidSyntaxException;

/**
 * A main method for launching an IDE using Solstice. It supports the following command line
 * arguments
 *
 * <ul>
 *   <li>{@code -installDir C:\SomeDir} determines where the IDE will store its data.
 *   <li>{@code -equoTestOnly true} signals that instead of running an IDE, just activate all
 *       bundles then shut down. Useful for integration testing.
 * </ul>
 */
public class IdeMain {
	public static void main(String[] args) throws InvalidSyntaxException {
		var argList = Arrays.asList(args);
		int idx = argList.indexOf("-installDir");
		SolsticeInit init;
		if (idx == -1) {
			init = new SolsticeInit();
		} else {
			init = new SolsticeInit(new File(args[idx + 1]));
		}
		var solstice = Solstice.initialize(init);

		var dontRunIdx = argList.indexOf("-equoTestOnly");
		if (dontRunIdx != -1 && Boolean.parseBoolean(argList.get(dontRunIdx + 1))) {
			System.out.println("Loaded " + solstice.getBundles().length + " bundles");
			System.exit(0);
			return;
		}

		int exitCode = IdeMainUi.main(solstice);
		if (exitCode == 0) {
			System.exit(0);
		} else {
			System.err.println("Unexpected exit code: " + exitCode);
			System.exit(1);
		}
	}
}
