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
package dev.equo.ide.gradle;

import dev.equo.ide.BuildPluginIdeMain;
import dev.equo.ide.IdeHook;
import dev.equo.solstice.Launcher;
import dev.equo.solstice.NestedJars;
import dev.equo.solstice.SerializableMisc;
import dev.equo.solstice.p2.P2Client;
import dev.equo.solstice.p2.P2Query;
import dev.equo.solstice.p2.WorkspaceRegistry;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.stream.Collectors;
import javax.inject.Inject;
import org.gradle.api.DefaultTask;
import org.gradle.api.file.FileCollection;
import org.gradle.api.model.ObjectFactory;
import org.gradle.api.provider.Property;
import org.gradle.api.tasks.Internal;
import org.gradle.api.tasks.TaskAction;
import org.gradle.api.tasks.options.Option;

public abstract class EquoIdeTask extends DefaultTask {
	@Internal
	public abstract Property<P2Client.Caching> getCaching();

	@Internal
	public abstract Property<P2Query> getQuery();

	@Internal
	public abstract Property<FileCollection> getMavenDeps();

	@Internal
	public abstract Property<File> getProjectDir();

	@Internal
	public abstract Property<Boolean> getUseAtomos();

	@Internal IdeHook.List ideHooks;

	public IdeHook.List getIdeHooks() {
		return ideHooks;
	}

	private boolean initOnly = false;

	@Option(
			option = "init-only",
			description = "Initializes the runtime to check for errors then exits.")
	void setInitOnly(boolean initOnly) {
		this.initOnly = initOnly;
	}

	private boolean showConsole = false;

	@Option(
			option = "show-console",
			description = "Adds a visible console to the launched application.")
	void showConsole(boolean showConsole) {
		this.showConsole = showConsole;
	}

	private BuildPluginIdeMain.DebugClasspath debugClasspath =
			BuildPluginIdeMain.DebugClasspath.disabled;

	@Option(
			option = "debug-classpath",
			description = "Dumps the classpath (in order) without starting the application.")
	void debugClasspath(BuildPluginIdeMain.DebugClasspath debugClasspath) {
		this.debugClasspath = debugClasspath;
	}

	private boolean dontUseAtomosOverride = false;

	@Option(
			option = "dont-use-atomos",
			description = "Uses Solstice's built-in OSGi runtime instead of Atomos+Equinox.")
	void dontUseAtomos(boolean dontUseAtomos) {
		this.dontUseAtomosOverride = dontUseAtomos;
	}

	private boolean clean = false;

	@Option(
			option = "clean",
			description = "Wipes all IDE settings and state before rebuilding and launching.")
	void clean(boolean clean) {
		this.clean = clean;
	}

	@Inject
	public abstract ObjectFactory getObjectFactory();

	@TaskAction
	public void launch() throws IOException, InterruptedException {
		var workspaceRegistry = WorkspaceRegistry.instance();
		var workspaceDir = workspaceRegistry.workspaceDir(getProjectDir().get(), clean);
		workspaceRegistry.removeAbandoned();

		var mavenDeps = getMavenDeps().get();

		var p2files = new ArrayList<File>();
		var query = getQuery().get();
		try (var client = new P2Client(getCaching().get())) {
			for (var unit : query.getJarsNotOnMavenCentral()) {
				p2files.add(client.download(unit));
			}
		}

		var p2deps = getObjectFactory().fileCollection().from(p2files);
		var p2AndMavenDeps = p2deps.plus(mavenDeps);

		var nestedJarFolder = new File(workspaceDir, NestedJars.DIR);
		var nestedJars =
				NestedJars.inFiles(p2AndMavenDeps).extractAllNestedJars(nestedJarFolder).stream()
						.map(e -> e.getValue())
						.collect(Collectors.toList());
		var nestedDefs = getObjectFactory().fileCollection().from(nestedJars);

		boolean useAtomos = dontUseAtomosOverride ? false : getUseAtomos().get();

		var ideHooksFile = new File(workspaceDir, "ide-hooks");
		SerializableMisc.toFile(ideHooks, ideHooksFile);

		var classpath = Launcher.copyAndSortClasspath(p2AndMavenDeps.plus(nestedDefs));
		debugClasspath.printWithHead(
				"jars about to be launched", classpath.stream().map(File::getAbsolutePath));
		boolean isBlocking =
				initOnly || showConsole || debugClasspath != BuildPluginIdeMain.DebugClasspath.disabled;
		var exitCode =
				Launcher.launchJavaBlocking(
						isBlocking,
						BuildPluginIdeMain.class.getName(),
						classpath,
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
			System.out.println("NEED HELP? If the IDE doesn't appear, try adding --show-console");
		}
		if (exitCode != 0) {
			System.out.println("WARNING! Exit code: " + exitCode);
		}
	}
}
