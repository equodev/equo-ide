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
import java.util.function.Function;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;
import org.osgi.framework.InvalidSyntaxException;

/**
 * A main method for launching an IDE using Solstice. It supports the following command line
 * arguments
 *
 * <ul>
 *   <li>{@code -installDir C:\SomeDir} determines where the IDE will store its data.
 *   <li>{@code -initOnly true} signals that instead of running an IDE, just activate all bundles
 *       then shut down. Useful for integration testing.
 * </ul>
 */
public class IdeMain {
	private static <T> T parseArg(
			String[] args, String arg, Function<String, T> parser, T defaultValue) {
		for (int i = 0; i < args.length - 1; ++i) {
			if (arg.equals(args[i])) {
				return parser.apply(args[i + 1]);
			}
		}
		return defaultValue;
	}

	static File defaultDir() {
		var userDir = System.getProperty("user.dir");
		if (userDir.endsWith("equo-ide")) {
			return new File(userDir + "/solstice/build/testSetup");
		} else {
			return new File(userDir + "/build/testSetup");
		}
	}

	public static void main(String[] args) throws InvalidSyntaxException, BundleException {
		File installDir = parseArg(args, "-installDir", File::new, defaultDir());
		boolean useAtomos = parseArg(args, "-useAtomos", Boolean::parseBoolean, false);
		boolean initOnly = parseArg(args, "-initOnly", Boolean::parseBoolean, false);

		BundleContext context;
		if (useAtomos) {
			context = new AtomosFrontend().getBundleContext();
		} else {
			context = Solstice.initialize(new SolsticeInit(installDir));
		}
		if (initOnly) {
			System.out.println("Loaded " + context.getBundles().length + " bundles");
			System.exit(0);
			return;
		}

		int exitCode = IdeMainUi.main(context);
		if (exitCode == 0) {
			System.exit(0);
		} else {
			System.err.println("Unexpected exit code: " + exitCode);
			System.exit(1);
		}
	}
}
