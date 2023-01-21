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

import com.diffplug.common.swt.os.OS;
import dev.equo.solstice.NestedJars;
import dev.equo.solstice.SerializableMisc;
import dev.equo.solstice.Solstice;
import dev.equo.solstice.SolsticeInit;
import dev.equo.solstice.SolsticeManifest;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;
import org.eclipse.swt.widgets.Display;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;
import org.osgi.framework.InvalidSyntaxException;

/**
 * A main method for launching an IDE using Solstice. It has a verbose command line interface which
 * is optimized for integration with the EquoIDE Gradle and Maven build plugins, but it can be used
 * in other contexts as well.
 */
public class BuildPluginIdeMain {
	public static class Caller {
		public IdeLockFile lockFile;
		public IdeHook.List ideHooks;
		public File workspaceDir;
		public ArrayList<File> classpath;
		public BuildPluginIdeMain.DebugClasspath debugClasspath;
		public Boolean initOnly, showConsole, useAtomos;
		public String showConsoleFlag;

		public void launch() throws IOException, InterruptedException {
			Objects.requireNonNull(lockFile);
			Objects.requireNonNull(ideHooks);
			Objects.requireNonNull(workspaceDir);
			Objects.requireNonNull(classpath);
			Objects.requireNonNull(debugClasspath);
			Objects.requireNonNull(initOnly);
			Objects.requireNonNull(showConsole);
			Objects.requireNonNull(useAtomos);
			Objects.requireNonNull(showConsoleFlag);

			var nestedJarFolder = new File(workspaceDir, NestedJars.DIR);
			for (var nested : NestedJars.inFiles(classpath).extractAllNestedJars(nestedJarFolder)) {
				classpath.add(nested.getValue());
			}

			var ideHooksFile = new File(workspaceDir, "ide-hooks");
			var ideHooksCopy = ideHooks.copy();
			ideHooksCopy.add(IdeHookLockFile.forWorkspaceDir(workspaceDir));
			SerializableMisc.toFile(ideHooksCopy, ideHooksFile);

			var classpathSorted = Launcher.copyAndSortClasspath(classpath);
			debugClasspath.printWithHead(
					"jars about to be launched", classpathSorted.stream().map(File::getAbsolutePath));
			boolean isBlocking =
					initOnly || showConsole || debugClasspath != BuildPluginIdeMain.DebugClasspath.disabled;
			var vmArgs = new ArrayList<String>();
			if (OS.getRunning().isMac()) {
				vmArgs.add("-XstartOnFirstThread");
			}
			vmArgs.add("-Dorg.slf4j.simpleLogger.defaultLogLevel=" + (isBlocking ? "info" : "error"));

			Consumer<Process> monitorProcess;
			if (isBlocking) {
				monitorProcess = null;
			} else {
				// if we're spawning a new IDE, record the lockfile before it launches
				long lockFileBeforeLaunch = lockFile.read();
				monitorProcess =
						process -> {
							// sleep over and over until the lockfile changes
							while (lockFile.read() == lockFileBeforeLaunch) {
								try {
									Thread.sleep(10);
								} catch (InterruptedException e) {
									// ignore
								}
							}
							// kill the console that we've been waiting on as a solution to
							// https://github.com/equodev/equo-ide/issues/44
							process.destroyForcibly();
						};
			}
			var exitCode =
					Launcher.launchJavaBlocking(
							isBlocking,
							classpathSorted,
							vmArgs,
							BuildPluginIdeMain.class.getName(),
							monitorProcess,
							"-installDir",
							workspaceDir.getAbsolutePath(),
							"-useAtomos",
							Boolean.toString(useAtomos),
							"-initOnly",
							Boolean.toString(initOnly),
							"-debugClasspath",
							debugClasspath.name(),
							"-ideHooks",
							ideHooksFile.getAbsolutePath());
			if (!isBlocking) {
				System.out.println("NEED HELP? If the IDE doesn't appear, try adding " + showConsoleFlag);
			}
			if (exitCode != 0) {
				System.out.println("WARNING! Exit code: " + exitCode);
			}
		}
	}

	public enum DebugClasspath {
		disabled,
		names,
		paths;

		public void printWithHead(String header, Stream<String> paths) {
			switch (this) {
				case disabled:
					return;
				case names:
				case paths:
					System.out.println("/ " + header);
					if (this == names) {
						paths =
								paths.map(
										path -> {
											int lastSlash = Math.max(path.lastIndexOf('/'), path.lastIndexOf('\\'));
											return path.substring(lastSlash + 1);
										});
					}
					paths.forEach(System.out::println);
					System.out.println("\\ " + header);
					return;
				default:
					throw new IllegalArgumentException("Unexpected enum value " + this);
			}
		}

		private void printAndExitIfEnabled() throws IOException {
			switch (this) {
				case disabled:
					return;
				case names:
				case paths:
					Enumeration<URL> manifestURLs =
							SolsticeManifest.class.getClassLoader().getResources(SolsticeManifest.MANIFEST_PATH);
					List<String> paths = new ArrayList<>();
					while (manifestURLs.hasMoreElements()) {
						String url = manifestURLs.nextElement().toExternalForm();
						paths.add(
								url.substring(
										0, url.length() - (SolsticeManifest.SLASH_MANIFEST_PATH.length() + 1)));
					}
					printWithHead("jars with manifests inside runtime", paths.stream());
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
		File hookListFile = parseArg(args, "-ideHooks", File::new, null);
		IdeHook.List ideHooksParsed;
		if (hookListFile == null) {
			ideHooksParsed = new IdeHook.List();
		} else {
			ideHooksParsed = SerializableMisc.fromFile(IdeHook.List.class, hookListFile);
		}
		debugClasspath.printAndExitIfEnabled();

		IdeHook.InstantiatedList ideHooks = ideHooksParsed.instantiate();
		if (!initOnly) {
			ideHooks.forEach(IdeHookInstantiated::beforeDisplay);
			var display = Display.getDefault();
			ideHooks.forEach(IdeHookInstantiated::afterDisplay, display);
			ideHooks.forEach(IdeHookInstantiated::beforeOsgi);
		}
		BundleContext context;
		if (useAtomos) {
			// the spelled-out package is on purpose so that Atomos can remain an optional component
			// works together with
			// https://github.com/equodev/equo-ide/blob/aa7d30cba9988bc740ff4bc4b3015475d30d187c/solstice/build.gradle#L16-L22
			context = new dev.equo.solstice.AtomosFrontend(installDir).getBundleContext();
		} else {
			context = Solstice.initialize(new SolsticeInit(installDir));
		}
		if (!initOnly) {
			ideHooks.forEach(IdeHookInstantiated::afterOsgi, context);
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

		int exitCode = IdeMainUi.main(context, ideHooks);
		if (exitCode == 0) {
			System.exit(0);
		} else {
			System.err.println("Unexpected exit code: " + exitCode);
			System.exit(1);
		}
	}
}
