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
import dev.equo.solstice.p2.P2QueryResult;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.inject.Inject;
import org.gradle.api.DefaultTask;
import org.gradle.api.GradleException;
import org.gradle.api.file.FileCollection;
import org.gradle.api.model.ObjectFactory;
import org.gradle.api.provider.Property;
import org.gradle.api.tasks.Internal;
import org.gradle.api.tasks.TaskAction;
import org.gradle.api.tasks.options.Option;

public abstract class EquoIdeTask extends DefaultTask {
	@Internal
	public abstract Property<P2QueryResult> getQuery();

	@Internal
	public abstract Property<FileCollection> getMavenDeps();

	@Internal
	public abstract Property<File> getProjectDir();

	@Internal
	public abstract Property<Boolean> getUseAtomos();

	@Internal
	public abstract Property<Boolean> getEquoIdeWasCalledDirectly();

	@Internal IdeHook.List ideHooks;

	public IdeHook.List getIdeHooks() {
		return ideHooks;
	}

	private boolean clean = false;

	@Option(
			option = "clean",
			description = "Wipes all IDE settings and state before rebuilding and launching.")
	void clean(boolean clean) {
		this.clean = clean;
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

	private Boolean useAtomosOverride = null;

	@Option(
			option = "use-atomos",
			description =
					"Determines whether to use Atomos+Equinox or only Solstice's built-in OSGi shim")
	void useAtomos(String useAtomos) {
		this.useAtomosOverride = Boolean.parseBoolean(useAtomos);
	}

	private boolean debugIde = false;

	@Option(option = "debug-ide", description = "The IDE will suspend until you attach a debugger.")
	void debugIde(boolean debugIde) {
		this.debugIde = debugIde;
	}

	@Inject
	public abstract ObjectFactory getObjectFactory();

	@TaskAction
	public void launch() throws IOException, InterruptedException {
		if (!getEquoIdeWasCalledDirectly().get()) {
			throw new GradleException(
					"You must call `equoIde` directly, you cannot call a task which depends on `equoIde`.");
		}
		var caller = BuildPluginIdeMain.Caller.forProjectDir(getProjectDir().get(), clean);

		var classpath = new ArrayList<File>();
		try {
			var jarsNotOnMaven =
					getObjectFactory().fileCollection().from(getQuery().get().getJarsNotOnMavenCentral());
			var p2AndMavenDeps = jarsNotOnMaven.plus(getMavenDeps().get());
			p2AndMavenDeps.forEach(classpath::add);
		} catch (Exception e) {
			throw new GradleException(
					"Unable to download Equo dependencies. You probably need to add\n"
							+ "`repositories { mavenCentral() }` or something similar to your `build.gradle`.",
					e);
		}

		if (classpath.isEmpty()) {
			throw new GradleException(
					"EquoIDE has nothing to install!\n\n"
							+ "We recommend starting with this:\n"
							+ "equoIde {\n"
							+ "  gradleBuildship()\n"
							+ "}");
		}

		caller.ideHooks = ideHooks;
		caller.classpath = classpath;
		caller.debugClasspath = debugClasspath;
		caller.initOnly = initOnly;
		caller.showConsole = showConsole;
		caller.useAtomos = useAtomosOverride != null ? useAtomosOverride : getUseAtomos().get();
		caller.debugIde = debugIde;
		caller.equoChromium = P2ModelDsl.isChromiumEnabled();
		caller.showConsoleFlag = "--show-console";
		caller.cleanFlag = "--clean";
		caller.launch();
	}
}
