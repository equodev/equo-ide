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
package dev.equo.ide.maven;

import com.diffplug.common.swt.os.SwtPlatform;
import dev.equo.solstice.BuildPluginIdeMain;
import dev.equo.solstice.Launcher;
import dev.equo.solstice.NestedJars;
import dev.equo.solstice.p2.JdtSetup;
import dev.equo.solstice.p2.P2Client;
import dev.equo.solstice.p2.P2Session;
import dev.equo.solstice.p2.P2Unit;
import dev.equo.solstice.p2.WorkspaceRegistry;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.maven.plugin.AbstractMojo;
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

@Mojo(name = "launch")
public class LaunchMojo extends AbstractMojo {
	@Component private RepositorySystem repositorySystem;

	@Parameter(property = "initOnly", defaultValue = "false")
	private boolean initOnly;

	@Parameter(property = "useAtomos", defaultValue = "true")
	private boolean useAtomos;

	@Parameter(property = "showConsole", defaultValue = "false")
	private boolean showConsole;

	@Parameter(property = "debugClasspath", defaultValue = "disabled")
	private BuildPluginIdeMain.DebugClasspath debugClasspath;

	@Parameter(property = "release")
	private String release;

	@Parameter(defaultValue = "${project.basedir}", required = true, readonly = true)
	private File baseDir;

	@Parameter(defaultValue = "${repositorySystemSession}", required = true, readonly = true)
	private RepositorySystemSession repositorySystemSession;

	@Parameter(defaultValue = "${project.remotePluginRepositories}", required = true, readonly = true)
	private List<RemoteRepository> repositories;

	private static final List<Exclusion> EXCLUDE_ALL_TRANSITIVES =
			Collections.singletonList(new Exclusion("*", "*", "*", "*"));

	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
		try {
			List<Dependency> deps = new ArrayList<>();
			deps.add(
					new Dependency(
							new DefaultArtifact("dev.equo.ide:solstice:" + NestedJars.solsticeVersion()), null));
			deps.add(
					new Dependency(
							new DefaultArtifact("org.slf4j:slf4j-api:2.0.6"),
							null,
							null,
							EXCLUDE_ALL_TRANSITIVES));
			deps.add(
					new Dependency(
							new DefaultArtifact("org.slf4j:slf4j-simple:2.0.6"),
							null,
							null,
							EXCLUDE_ALL_TRANSITIVES));

			var workspaceRegistry = WorkspaceRegistry.instance();
			var workspaceDir = workspaceRegistry.workspaceDir(baseDir);
			workspaceRegistry.clean();

			var session = new P2Session();
			try (var client = new P2Client()) {
				if (release == null) {
					release = JdtSetup.DEFAULT_VERSION;
				}
				session.populateFrom(client, JdtSetup.URL_BASE + release + "/");
			} catch (Exception e) {
				throw new RuntimeException(e);
			}

			var query = session.query();
			query.platform(SwtPlatform.getRunning());
			if (initOnly) {
				query.install("org.eclipse.swt");
			} else {
				JdtSetup.mavenCoordinate(query, session);
			}

			for (var coordinate : query.getJarsOnMavenCentral()) {
				deps.add(
						new Dependency(new DefaultArtifact(coordinate), null, null, EXCLUDE_ALL_TRANSITIVES));
			}
			CollectRequest collectRequest = new CollectRequest(deps, null, repositories);
			DependencyRequest dependencyRequest = new DependencyRequest(collectRequest, null);
			DependencyResult dependencyResult =
					repositorySystem.resolveDependencies(repositorySystemSession, dependencyRequest);

			List<File> files = new ArrayList<>();
			for (var artifact : dependencyResult.getArtifactResults()) {
				files.add(artifact.getArtifact().getFile());
			}
			try (var client = new P2Client()) {
				for (P2Unit unit : query.getJarsNotOnMavenCentral()) {
					files.add(client.download(unit));
				}
			}

			var nestedJarFolder = new File(workspaceDir, NestedJars.DIR);
			for (var nested : NestedJars.inFiles(files).extractAllNestedJars(nestedJarFolder)) {
				files.add(nested.getValue());
			}

			var classpath = Launcher.copyAndSortClasspath(files);
			debugClasspath.printWithHead(
					"jars about to be launched", classpath.stream().map(File::getAbsolutePath));
			var exitCode =
					Launcher.launchJavaBlocking(
							initOnly
									|| showConsole
									|| debugClasspath != BuildPluginIdeMain.DebugClasspath.disabled,
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
							"-Dorg.slf4j.simpleLogger.defaultLogLevel=INFO");
			if (exitCode != 0) {
				System.out.println("WARNING! Exit code: " + exitCode);
			}
		} catch (DependencyResolutionException | IOException | InterruptedException e) {
			throw new RuntimeException(e);
		}
	}
}
