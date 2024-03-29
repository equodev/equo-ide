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
package dev.equo.ide.maven;

import com.diffplug.common.swt.os.OS;
import dev.equo.ide.BuildPluginIdeMain;
import dev.equo.ide.Catalog;
import dev.equo.ide.IdeHook;
import dev.equo.ide.IdeHookBranding;
import dev.equo.ide.IdeHookWelcome;
import dev.equo.ide.WorkspaceInit;
import dev.equo.solstice.NestedJars;
import dev.equo.solstice.p2.P2ClientCache;
import dev.equo.solstice.p2.P2QueryCache;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.eclipse.aether.RepositorySystem;
import org.eclipse.aether.RepositorySystemSession;
import org.eclipse.aether.artifact.DefaultArtifact;
import org.eclipse.aether.collection.CollectRequest;
import org.eclipse.aether.graph.Dependency;
import org.eclipse.aether.graph.Exclusion;
import org.eclipse.aether.repository.RemoteRepository;
import org.eclipse.aether.resolution.DependencyRequest;
import org.eclipse.aether.resolution.DependencyResolutionException;
import org.eclipse.aether.resolution.DependencyResult;

/** Launches an Eclipse-based IDE for this project. */
@Mojo(name = "launch", aggregator = true)
public class LaunchMojo extends AbstractP2MojoWithCatalog {
	@Parameter private Branding branding = new Branding();

	@Parameter private Welcome welcome = new Welcome();

	/** Wipes all IDE settings and state before rebuilding and launching. */
	@Parameter(property = "clean", defaultValue = "false")
	private boolean clean;

	/** Initializes the runtime to check for errors then exits. */
	@Parameter(property = "initOnly", defaultValue = "false")
	private boolean initOnly;

	/** Adds a visible console to the launched application. */
	@Parameter(property = "showConsole", defaultValue = "false")
	private boolean showConsole;

	/**
	 * Replaces the standard SWT browser with Equo Chromium.
	 *
	 * @deprecated use equoChromium instead
	 */
	@Parameter(property = "useChromium", defaultValue = "false")
	@Deprecated
	private boolean useChromium;

	/** Dumps the classpath (in order) without starting the application. */
	@Parameter(property = "debugClasspath", defaultValue = "disabled")
	private BuildPluginIdeMain.DebugClasspath debugClasspath;

	/** Determines whether to use Solstice's built-in OSGi runtime or instead Atomos+Equinox. */
	@Parameter(property = "useAtomos", defaultValue = "false")
	private boolean useAtomos;

	/** Blocks IDE startup to help you attach a debugger. */
	@Parameter(property = "debugIde", defaultValue = "false")
	private boolean debugIde;

	/** Sets properties in the Eclipse workspace. */
	@Parameter(property = "workspaceProps")
	private List<WorkspaceProp> workspaceProps = new ArrayList<>();

	@Parameter(defaultValue = "${project.basedir}", required = true, readonly = true)
	protected File baseDir;

	@Component protected RepositorySystem repositorySystem;

	@Parameter(defaultValue = "${repositorySystemSession}", required = true, readonly = true)
	protected RepositorySystemSession repositorySystemSession;

	@Parameter(defaultValue = "${project.remotePluginRepositories}", required = true, readonly = true)
	protected List<RemoteRepository> repositories;

	private static final List<Exclusion> EXCLUDE_ALL_TRANSITIVES =
			Collections.singletonList(new Exclusion("*", "*", "*", "*"));

	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
		try {
			if (useChromium) {
				getLog().warn("<useChromium>true</useChromium> is deprecated, use <equoChromium/> instead");
				if (equoChromium == null) {
					equoChromium = new EquoChromium();
				}
			}
			var caller = BuildPluginIdeMain.Caller.forProjectDir(baseDir, clean);

			var ideHooks = new IdeHook.List();
			ideHooks.add(
					new IdeHookBranding().title(branding.title).icon(branding.icon).splash(branding.splash));
			if (welcome != null) {
				var welcomeHook = new IdeHookWelcome();
				welcomeHook.openUrl(welcome.openUrl).perspective(welcome.perspective);
				ideHooks.add(welcomeHook);
			}

			List<Dependency> deps = new ArrayList<>();
			deps.add(
					new Dependency(
							new DefaultArtifact("dev.equo.ide:solstice:" + NestedJars.solsticeVersion()), null));
			var workspaceInit = new WorkspaceInit();
			boolean isOffline = false;
			var clientCaching = P2ClientCache.defaultIfOfflineIsAndForceRecalculateIs(isOffline, clean);
			var model = super.prepareModel(ideHooks, workspaceInit);
			var query =
					model.query(clientCaching, clean ? P2QueryCache.FORCE_RECALCULATE : P2QueryCache.ALLOW);
			for (var dep : NestedJars.transitiveDeps(useAtomos, NestedJars.CoordFormat.MAVEN, query)) {
				deps.add(new Dependency(new DefaultArtifact(dep), null, null, EXCLUDE_ALL_TRANSITIVES));
			}
			for (var coord : model.getPureMaven()) {
				deps.add(new Dependency(new DefaultArtifact(coord), null));
			}
			for (var dep : query.getJarsOnMavenCentral()) {
				deps.add(new Dependency(new DefaultArtifact(dep), null, null, EXCLUDE_ALL_TRANSITIVES));
			}
			if (Catalog.EQUO_CHROMIUM.isEnabled(model)) {
				repositories.add(
						new RemoteRepository.Builder("chromium", "default", Catalog.EQUO_CHROMIUM.mavenRepo())
								.build());
			}

			CollectRequest collectRequest = new CollectRequest(deps, null, repositories);
			DependencyRequest dependencyRequest = new DependencyRequest(collectRequest, null);
			DependencyResult dependencyResult =
					repositorySystem.resolveDependencies(repositorySystemSession, dependencyRequest);
			for (var workspaceProp : workspaceProps) {
				workspaceInit.setProperty(workspaceProp.path, workspaceProp.key, workspaceProp.value);
			}
			var files = new ArrayList<File>();
			for (var artifact : dependencyResult.getArtifactResults()) {
				files.add(artifact.getArtifact().getFile());
			}
			for (File downloadedJar : query.getJarsNotOnMavenCentral()) {
				files.add(downloadedJar);
			}

			if (!OS.getNative().isMac()) {
				System.setProperty("equo-ide-maven-workarounds", "true");
			}
			caller.ideHooks = ideHooks;
			caller.workspaceInit = workspaceInit;
			caller.classpath = files;
			caller.debugClasspath = debugClasspath;
			caller.initOnly = initOnly;
			caller.showConsole = showConsole;
			caller.useAtomos = useAtomos;
			caller.debugIde = debugIde;
			caller.showConsoleFlag = "-DshowConsole";
			caller.cleanFlag = "-Dclean";
			caller.launch();
		} catch (DependencyResolutionException | IOException | InterruptedException e) {
			throw new RuntimeException(e);
		}
	}
}
