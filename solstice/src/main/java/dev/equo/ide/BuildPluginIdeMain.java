/*******************************************************************************
 * Copyright (c) 2022-2023 EquoTech, Inc. and others.
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
import dev.equo.solstice.ShimIdeBootstrapServices;
import dev.equo.solstice.SignedJars;
import dev.equo.solstice.Solstice;
import dev.equo.solstice.SolsticeManifest;
import dev.equo.solstice.p2.WorkspaceRegistry;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;
import org.eclipse.osgi.internal.location.EquinoxLocations;
import org.eclipse.swt.widgets.Display;
import org.osgi.framework.BundleException;
import org.osgi.framework.Constants;
import org.osgi.framework.InvalidSyntaxException;

/**
 * A main method for launching an IDE using Solstice. It has a verbose command line interface which
 * is optimized for integration with the EquoIDE Gradle and Maven build plugins, but it can be used
 * in other contexts as well.
 */
public class BuildPluginIdeMain {
	public static class Caller {
		public static Caller forProjectDir(File projectDir, boolean clean)
				throws IOException, InterruptedException {
			var caller = new Caller();

			var workspaceRegistry = WorkspaceRegistry.instance();
			caller.workspaceDir = workspaceRegistry.workspaceDirForProjectDir(projectDir);
			workspaceRegistry.removeAbandoned();

			caller.lockFile = IdeLockFile.forWorkspaceDir(caller.workspaceDir);
			var alreadyRunning = caller.lockFile.ideAlreadyRunning();
			if (IdeLockFile.alreadyRunningAndUserRequestsAbort(alreadyRunning)) {
				return null;
			}

			if (clean) {
				workspaceRegistry.cleanWorkspaceDir(caller.workspaceDir);
			}
			return caller;
		}

		private Caller() {}

		public File workspaceDir;
		public IdeLockFile lockFile;
		public IdeHook.List ideHooks;
		public WorkspaceInit workspaceInit;
		public ArrayList<File> classpath;
		public BuildPluginIdeMain.DebugClasspath debugClasspath;
		public Boolean initOnly, showConsole, useAtomos, debugIde;
		public String showConsoleFlag, cleanFlag;

		public void launch() throws IOException, InterruptedException {
			Objects.requireNonNull(workspaceDir);
			Objects.requireNonNull(lockFile);
			Objects.requireNonNull(ideHooks);
			Objects.requireNonNull(workspaceInit);
			Objects.requireNonNull(classpath);
			Objects.requireNonNull(debugClasspath);
			Objects.requireNonNull(initOnly);
			Objects.requireNonNull(showConsole);
			Objects.requireNonNull(useAtomos);
			Objects.requireNonNull(debugIde);
			Objects.requireNonNull(showConsoleFlag);
			Objects.requireNonNull(cleanFlag);

			ArrayList<File> classpathSorted = Launcher.copyAndSortClasspath(classpath);
			SignedJars.stripIfNecessary(classpathSorted);
			var nestedJarFolder = new File(workspaceDir, NestedJars.DIR);
			for (var nested : NestedJars.inFiles(classpathSorted).extractAllNestedJars(nestedJarFolder)) {
				classpathSorted.add(nested.getValue());
			}
			var vmArgs = new ArrayList<String>();
			var environmentVars = new LinkedHashMap<String, String>();
			if (Catalog.EQUO_CHROMIUM.isEnabled(classpath)) {
				List<String> chromiumArgs = new ArrayList<String>();

				// This property is used to fix the error in the setUrl method.
				chromiumArgs.add("--disable-site-isolation-trials");

				// This property is used to fix the error when logging in with a third party.
				chromiumArgs.add("--user-agent=" + EquoChromium.getUserAgent());

				vmArgs.add("-Dchromium.args=" + String.join(";", chromiumArgs));

				// This property improve loading time of setText for large resources.
				vmArgs.add("-Dchromium.setTextAsUrl=file:");
				// Fix graphics on linux
				if (OS.getRunning().isLinux()) {
					environmentVars.put("GDK_BACKEND", "x11");
				}
				// Patch in our browser replacement, which requires stripping the signature from the SWT
				// packages, and activate the license
				Patch.patch(classpathSorted, nestedJarFolder, "patch-chromium-swt");
				SignedJars.stripIf(classpathSorted, fileName -> fileName.startsWith("org.eclipse.swt."));
				vmArgs.add(
						"-Dchromium.activate_equo_chromium=This distribution of the Equo browser is licensed only for use with an IDE launched by the EquoIDE build plugin");
			}
			if (useAtomos) {
				// for Eclipse 4.27, we have patched the EquinoxBundle class so that it handles
				// `getEntry("/")`
				var version = Patch.detectVersion(classpathSorted, "org.eclipse.osgi");
				if ("3.18.300".equals(version)) {
					Patch.patch(classpathSorted, nestedJarFolder, "patch-equinox-4.27");
					SignedJars.stripIf(classpathSorted, jarName -> jarName.startsWith("org.eclipse.osgi"));
				}
			}
			SignedJars.stripIfNecessary(classpathSorted);

			if (lockFile.hasClasspath() && !classpathSorted.equals(lockFile.readClasspath())) {
				System.out.println("WARNING! The classpath has changed since this IDE was setup.");
				System.out.println(
						"         Recommend closing the IDE and retrying with this flag: " + cleanFlag);
			}

			var ideHooksFile = new File(workspaceDir, "ide-hooks");
			var ideHooksCopy = ideHooks.copy();
			// add any IdeHooks which were declared in jar manifests
			for (var jar : classpathSorted) {
				var ideHook = SolsticeManifest.parseJar(jar).getHeadersOriginal().get("Bundle-IdeHook");
				if (ideHook != null) {
					ideHooksCopy.add(new IdeHookReflected(ideHook));
				}
			}

			ideHooksCopy.add(IdeHookLockFile.forWorkspaceDirAndClasspath(workspaceDir, classpathSorted));
			SerializableMisc.toFile(ideHooksCopy, ideHooksFile);

			workspaceInit.applyTo(workspaceDir);

			var installDir = workspaceDir.toPath().resolve("install");
			Files.createDirectories(installDir);
			var bundlesInfo =
					workspaceDir
							.toPath()
							.resolve("config/org.eclipse.equinox.simpleconfigurator/bundles.info");
			Files.createDirectories(bundlesInfo.getParent());
			Files.writeString(bundlesInfo, bundlesDotInfo(classpathSorted));

			debugClasspath.printWithHead(
					"jars about to be launched", classpathSorted.stream().map(File::getAbsolutePath));
			boolean isBlocking =
					initOnly
							|| showConsole
							|| debugClasspath != BuildPluginIdeMain.DebugClasspath.disabled
							|| debugIde;

			if (OS.getRunning().isMac()) {
				vmArgs.add("-XstartOnFirstThread");
			}
			vmArgs.add("-Dorg.slf4j.simpleLogger.defaultLogLevel=" + (isBlocking ? "info" : "error"));

			if (debugIde) {
				vmArgs.add("-Xdebug");
				vmArgs.add("-Xrunjdwp:transport=dt_socket,address=8000,server=y,suspend=y");
				System.out.println("IDE will block until you attach a jdb debugger to port 8000");
				System.out.println("  e.g. jdb -attach localhost:8000");
			}

			Consumer<Process> monitorProcess;
			if (isBlocking) {
				monitorProcess = null;
			} else {
				// if we're spawning a new IDE, record the lockfile before it launches
				long lockFileBeforeLaunch = lockFile.readPidToken();
				monitorProcess =
						process -> {
							// sleep over and over until the lockfile changes
							while (lockFile.readPidToken() == lockFileBeforeLaunch) {
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

	private static String bundlesDotInfo(List<File> cp) {
		var buffer = new StringBuilder();
		var newline = "\n";
		// for a "real" file these should be different in different places...
		var startLevel = "0";
		var markedAsStarted = "false";

		buffer.append("#encoding=UTF-8");
		buffer.append(newline);
		buffer.append("#version=1");
		buffer.append(newline);
		for (var file : cp) {
			try {
				SolsticeManifest manifest = SolsticeManifest.parseJar(file);
				if (manifest == null
						|| manifest.getSymbolicName() == null
						|| manifest.getVersion() == null) {
					continue;
				}
				buffer.append(manifest.getSymbolicName());
				buffer.append(',');
				buffer.append(manifest.getVersion());
				buffer.append(',');
				buffer.append(file.toURI());
				buffer.append(',');
				buffer.append(startLevel);
				buffer.append(',');
				buffer.append(markedAsStarted);
				buffer.append(newline);
			} catch (Exception e) {
				// do nothing
			}
		}
		return buffer.toString();
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

		NestedJars.onClassPath()
				.confirmAllNestedJarsArePresentOnClasspath(new File(installDir, NestedJars.DIR));
		var solstice = Solstice.findBundlesOnClasspath();
		solstice.warnAndModifyManifestsToFix();

		IdeHook.InstantiatedList ideHooks = ideHooksParsed.instantiate();
		var lockFileHook = ideHooks.find(IdeHookLockFile.Instantiated.class);
		boolean isClean = lockFileHook == null || lockFileHook.isClean();
		if (!initOnly) {
			ideHooks.forEach(IdeHookInstantiated::isClean, isClean);
			var display = Display.getDefault();
			ideHooks.forEach(IdeHookInstantiated::afterDisplay, display);
		}

		var props = new LinkedHashMap<String, String>();
		props.put(
				"org.eclipse.equinox.simpleconfigurator.configUrl",
				"file: broken on purpose to disable simpleconfigurator");
		props.put("gosh.args", "--quiet --noshutdown");
		props.put("osgi.nl", "en_US");
		props.put("eclipse.noRegistryFlushing", "true");
		props.put(Constants.FRAMEWORK_STORAGE_CLEAN, Constants.FRAMEWORK_STORAGE_CLEAN_ONFIRSTINIT);
		props.put(
				EquinoxLocations.PROP_INSTANCE_AREA, new File(installDir, "instance").getAbsolutePath());
		props.put(
				EquinoxLocations.PROP_INSTALL_AREA, new File(installDir, "install").getAbsolutePath());
		props.put(EquinoxLocations.PROP_CONFIG_AREA, new File(installDir, "config").getAbsolutePath());
		props.put(EquinoxLocations.PROP_USER_AREA, new File(installDir, "user").getAbsolutePath());
		File eclipseHome = new File(installDir, "eclipse-home");
		props.put(EquinoxLocations.PROP_HOME_LOCATION_AREA, eclipseHome.getAbsolutePath());
		System.setProperty(EquinoxLocations.PROP_HOME_LOCATION_AREA, eclipseHome.toURI().toString());
		if (useAtomos) {
			props.put("atomos.content.start", "false");
			solstice.openAtomos(props);
		} else {
			solstice.openShim(props);
			ShimIdeBootstrapServices.apply(props, solstice.getContext());
		}
		ShimIdeBootstrapServices.shimAndAtomos(props, solstice.getContext());

		solstice.start("org.apache.felix.scr");
		solstice.startAllWithLazy(false);
		for (var eagerStart :
				solstice.bundlesOnClasspathOutOf(Fudge.activateEagerWithoutTransitives())) {
			solstice.startWithoutTransitives(eagerStart);
		}
		solstice.start("org.eclipse.ui.ide.application");
		if (useAtomos) {
			// the spelled-out package is on purpose so that Atomos can remain an optional component
			// works together with
			// https://github.com/equodev/equo-ide/blob/aa7d30cba9988bc740ff4bc4b3015475d30d187c/solstice/build.gradle#L16-L22
			dev.equo.solstice.BundleContextAtomos.urlWorkaround(solstice);
		}
		if (!initOnly) {
			ideHooks.forEach(IdeHookInstantiated::afterOsgi, solstice.getContext());
		}

		if (initOnly) {
			System.out.println(
					"Loaded "
							+ solstice.getContext().getBundles().length
							+ " bundles "
							+ (useAtomos ? "using Atomos" : "not using Atomos"));
			System.exit(0);
			return;
		}

		int exitCode = IdeMainUi.main(solstice, ideHooks);
		if (exitCode == 0) {
			System.exit(0);
		} else {
			System.err.println("Unexpected exit code: " + exitCode);
			System.exit(1);
		}
	}
}
