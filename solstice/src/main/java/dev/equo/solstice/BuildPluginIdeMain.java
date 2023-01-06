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
import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.function.Function;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;
import org.osgi.framework.InvalidSyntaxException;

/**
 * A main method for launching an IDE using Solstice. It has a verbose command line interface which
 * is optimized for integration with the EquoIDE Gradle and Maven build plugins, but it can be used
 * in other contexts as well.
 */
public class BuildPluginIdeMain {
	public enum DebugClasspath {
		disabled,
		names,
		paths;

		public void doAction() throws IOException {
			switch (this) {
				case disabled:
					return;
				case names:
				case paths:
					Enumeration<URL> manifestURLs =
							SolsticeManifest.class.getClassLoader().getResources(SolsticeManifest.MANIFEST_PATH);
					while (manifestURLs.hasMoreElements()) {
						String url = manifestURLs.nextElement().toExternalForm();
						String jarPath =
								url.substring(
										0, url.length() - (SolsticeManifest.SLASH_MANIFEST_PATH.length() + 1));
						if (this == paths) {
							System.out.println(jarPath);
						} else {
							int lastSlash = Math.max(jarPath.lastIndexOf('/'), jarPath.lastIndexOf('\\'));
							System.out.println(jarPath.substring(lastSlash + 1));
						}
					}
					System.exit(0);
				default:
					throw new IllegalArgumentException("Unexpected enum value " + this);
			}
		}
	}

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

	public static void main(String[] args)
			throws InvalidSyntaxException, BundleException, IOException {
		File installDir = parseArg(args, "-installDir", File::new, defaultDir());
		boolean useAtomos = parseArg(args, "-useAtomos", Boolean::parseBoolean, false);
		boolean initOnly = parseArg(args, "-initOnly", Boolean::parseBoolean, false);
		DebugClasspath debugClasspath =
				parseArg(args, "-debugClasspath", DebugClasspath::valueOf, DebugClasspath.disabled);
		debugClasspath.doAction();

		BundleContext context;
		if (useAtomos) {
			// the spelled-out package is on purpose so that Atomos can remain an optional component
			// works together with
			// https://github.com/equodev/equo-ide/blob/aa7d30cba9988bc740ff4bc4b3015475d30d187c/solstice/build.gradle#L16-L22
			context = new dev.equo.solstice.AtomosFrontend(installDir).getBundleContext();
		} else {
			context = Solstice.initialize(new SolsticeInit(installDir));
		}
		if (initOnly) {
			System.out.println(
					"Loaded "
							+ context.getBundles().length
							+ " bundles "
							+ (useAtomos ? "using Atomos" : "not using Atomos"));
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
