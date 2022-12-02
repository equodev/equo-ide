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

import dev.equo.solstice.NestedBundles;
import dev.equo.solstice.P2AsMaven;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
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
import org.eclipse.aether.repository.RemoteRepository;
import org.eclipse.aether.resolution.ArtifactResult;
import org.eclipse.aether.resolution.DependencyRequest;
import org.eclipse.aether.resolution.DependencyResolutionException;
import org.eclipse.aether.resolution.DependencyResult;

@Mojo(name = "launch")
public class LaunchMojo extends AbstractMojo {
	@Component private RepositorySystem repositorySystem;

	@Parameter(defaultValue = "${project.build.directory}", required = true, readonly = true)
	private File buildDir;

	@Parameter(defaultValue = "${repositorySystemSession}", required = true, readonly = true)
	private RepositorySystemSession repositorySystemSession;

	@Parameter(defaultValue = "${project.remotePluginRepositories}", required = true, readonly = true)
	private List<RemoteRepository> repositories;

	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
		try {
			List<Dependency> deps = new ArrayList<>();
			deps.add(
					new Dependency(
							new DefaultArtifact("dev.equo.ide:solstice:" + NestedBundles.solsticeVersion()),
							null));
			for (var coordinate : P2AsMaven.jdtDeps()) {
				deps.add(new Dependency(new DefaultArtifact(coordinate), null));
			}
			CollectRequest collectRequest = new CollectRequest(deps, null, repositories);
			DependencyRequest dependencyRequest = new DependencyRequest(collectRequest, null);
			DependencyResult dependencyResult =
					repositorySystem.resolveDependencies(repositorySystemSession, dependencyRequest);

			List<File> files = new ArrayList<>();
			for (var artifact : dependencyResult.getArtifactResults()) {
				logResolved(artifact);
				files.add(artifact.getArtifact().getFile());
			}

			var installDir = new File(buildDir, "equoIde");
			for (var nested : NestedBundles.inFiles(files).extractAllNestedJars(installDir)) {
				files.add(nested.getValue());
			}

			javaExec("dev.equo.solstice.SolsticeIDE", files, "-installDir", installDir.getAbsolutePath());
		} catch (DependencyResolutionException | IOException | InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	private void logResolved(ArtifactResult artifactResult) {
		if (getLog().isDebugEnabled()) {
			getLog().debug("Resolved artifact: " + artifactResult);
		}
	}

	private static int javaExec(String mainClass, List<File> cp, String... args)
			throws IOException, InterruptedException {
		String javaHome = System.getProperty("java.home");
		String javaBin = javaHome + File.separator + "bin" + File.separator + "java";

		List<String> command = new ArrayList<>();
		command.add(javaBin);
		command.add("-cp");
		command.add(cp.stream().map(File::getAbsolutePath).collect(Collectors.joining(";")));
		command.add(mainClass);
		for (var arg : args) {
			command.add(arg);
		}

		var builder = new ProcessBuilder(command);
		Process process = builder.inheritIO().start();
		process.waitFor();
		return process.exitValue();
	}
}
