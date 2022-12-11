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
import dev.equo.solstice.NestedBundles;
import dev.equo.solstice.p2.JdtSetup;
import dev.equo.solstice.p2.P2Client;
import dev.equo.solstice.p2.P2Session;
import dev.equo.solstice.p2.WorkspaceRegistry;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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

	@Parameter(property = "equoTestOnly")
	private String equoTestOnly;

	@Parameter(property = "release")
	private String release;

	@Parameter(defaultValue = "${project.basedir}", required = true, readonly = true)
	private File baseDir;

	@Parameter(defaultValue = "${repositorySystemSession}", required = true, readonly = true)
	private RepositorySystemSession repositorySystemSession;

	@Parameter(defaultValue = "${project.remotePluginRepositories}", required = true, readonly = true)
	private List<RemoteRepository> repositories;

	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
		try {
			System.setProperty("osgi.platform", SwtPlatform.getRunning().toString());
			boolean equoTestOnlyTrue = "true".equals(equoTestOnly);

			List<Dependency> deps = new ArrayList<>();
			deps.add(
					new Dependency(
							new DefaultArtifact("dev.equo.ide:solstice:" + NestedBundles.solsticeVersion()),
							null));

			// not sure why, but we get errors from an old SWT with bad metadata, and this exclusion fixes
			// it
			// https://bugs.eclipse.org/bugs/show_bug.cgi?id=570685
			var excludeTransitive =
					Arrays.asList(
							new Exclusion("com.sun.jna", "com.sun.jna", "*", "*"),
							new Exclusion("com.sun.jna", "com.sun.jna.platform", "*", "*"),
							new Exclusion("org.apache.lucene", "org.apache.lucene.analyzers-common", "*", "*"),
							new Exclusion("org.apache.lucene", "org.apache.lucene.misc", "*", "*"),
							new Exclusion("org.apache.lucene", "org.apache.lucene.analyzers-smartcn", "*", "*"),
							new Exclusion("javax.annotation", "javax.annotation-api", "*", "*"),
							new Exclusion("org.eclipse.platform", "org.eclipse.swt.gtk.linux.aarch64", "*", "*"),
							new Exclusion("org.eclipse.platform", "org.eclipse.swt.gtk.linux.arm", "*", "*"));

			var workspaceDir = WorkspaceRegistry.instance().workspaceDir(baseDir);
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
			query.setPlatform(SwtPlatform.getRunning());
			if (equoTestOnlyTrue) {
				query.resolve("org.eclipse.swt");
			} else {
				JdtSetup.mavenCoordinate(query, session);
			}
			for (var coordinate : query.getJarsOnMavenCentral()) {
				deps.add(new Dependency(new DefaultArtifact(coordinate), null, null, excludeTransitive));
			}
			CollectRequest collectRequest = new CollectRequest(deps, null, repositories);
			DependencyRequest dependencyRequest = new DependencyRequest(collectRequest, null);
			DependencyResult dependencyResult =
					repositorySystem.resolveDependencies(repositorySystemSession, dependencyRequest);

			List<File> files = new ArrayList<>();
			for (var artifact : dependencyResult.getArtifactResults()) {
				files.add(artifact.getArtifact().getFile());
			}

			var nestedJarFolder = new File(workspaceDir, NestedBundles.DIR);
			for (var nested : NestedBundles.inFiles(files).extractAllNestedJars(nestedJarFolder)) {
				files.add(nested.getValue());
			}

			String result =
					NestedBundles.javaExec(
							"dev.equo.solstice.IdeMain",
							files,
							"-installDir",
							workspaceDir.getAbsolutePath(),
							"-equoTestOnly",
							Boolean.toString(equoTestOnlyTrue));
			System.out.println(result);
		} catch (DependencyResolutionException | IOException | InterruptedException e) {
			throw new RuntimeException(e);
		}
	}
}
