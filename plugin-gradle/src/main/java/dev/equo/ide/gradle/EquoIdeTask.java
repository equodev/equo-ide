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
import dev.equo.solstice.p2.P2Client;
import dev.equo.solstice.p2.P2Query;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.inject.Inject;
import org.gradle.api.DefaultTask;
import org.gradle.api.file.ConfigurableFileCollection;
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
		var caller = BuildPluginIdeMain.Caller.forProjectDir(getProjectDir().get(), clean);

		var query = getQuery().get();

		ConfigurableFileCollection p2deps;
		{
			var p2files = new ArrayList<File>();
			try (var client = new P2Client(getCaching().get())) {
				for (var unit : query.getJarsNotOnMavenCentral()) {
					p2files.add(client.download(unit));
				}
			}
			p2deps = getObjectFactory().fileCollection().from(p2files);
		}

		var p2AndMavenDeps = p2deps.plus(getMavenDeps().get());
		var classpath = new ArrayList<File>();
		p2AndMavenDeps.forEach(classpath::add);

		caller.ideHooks = ideHooks;
		caller.classpath = classpath;
		caller.debugClasspath = debugClasspath;
		caller.initOnly = initOnly;
		caller.showConsole = showConsole;
		caller.useAtomos = useAtomosOverride != null ? useAtomosOverride : getUseAtomos().get();
		caller.debugIde = debugIde;
		caller.showConsoleFlag = "--show-console";
		caller.cleanFlag = "--clean";
		caller.launch();
	}
}
