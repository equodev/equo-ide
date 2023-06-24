/*******************************************************************************
 * Copyright (c) 2023 EquoTech, Inc. and others.
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

import dev.equo.ide.IdeHook;
import dev.equo.ide.Launcher;
import dev.equo.ide.WorkspaceInit;
import dev.equo.solstice.NestedJars;
import dev.equo.solstice.SignedJars;
import dev.equo.solstice.p2.P2Model;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.gradle.api.Action;
import org.gradle.api.Project;
import org.gradle.api.artifacts.Configuration;
import org.gradle.api.artifacts.ModuleDependency;
import org.gradle.api.artifacts.ModuleVersionSelector;

public class P2DepsExtension {
	private final Project project;

	public P2DepsExtension(Project project) {
		this.project = project;
	}

	public void into(String configuration, Action<P2ModelDslWithCatalog> p2) {
		into(List.of(configuration), p2);
	}

	public void into(Configuration configuration, Action<P2ModelDslWithCatalog> p2) {
		into(List.of(configuration), p2);
	}

	public void into(Collection<?> configs, Action<P2ModelDslWithCatalog> p2) {
		var configurations = new ArrayList<Configuration>();
		for (var config : configs) {
			if (config instanceof Configuration) {
				configurations.add((Configuration) config);
			} else if (config instanceof String) {
				configurations.add(project.getConfigurations().maybeCreate((String) config));
			} else {
				throw new IllegalArgumentException("Expected Configuration or String, got " + config);
			}
		}
		P2ModelDslWithCatalog dsl = new P2ModelDslWithCatalog(project);
		p2.execute(dsl);
		var workspaceInitUnused = new WorkspaceInit();
		dsl.catalog.putInto(dsl.model, new IdeHook.List(), workspaceInitUnused);
		dsl.model.applyNativeFilterIfNoPlatformFilter();
		dsl.model.validateFilters();
		for (var config : configurations) {
			var existing = this.configurations.put(config.getName(), dsl.model);
			if (existing != null) {
				throw new IllegalArgumentException(
						"Configuration "
								+ config.getName()
								+ " has multiple extensions.\n  existing="
								+ existing
								+ "\n  new="
								+ dsl.model);
			}
		}
	}

	private final Map<String, P2Model> configurations = new HashMap<>();

	void configure() {
		var clientCaching = P2ModelDsl.clientCaching(project);
		var queryCaching = P2ModelDsl.queryCaching(project);
		for (Map.Entry<String, P2Model> entry : configurations.entrySet()) {
			String config = entry.getKey();
			P2Model model = entry.getValue();
			// add the pure-maven deps
			addPureMavenDeps(model, project, config);
			// then the maven-resolved deps
			var query = model.query(clientCaching, queryCaching);
			for (String mavenCoord : query.getJarsOnMavenCentral()) {
				ModuleDependency dep = (ModuleDependency) project.getDependencies().add(config, mavenCoord);
				dep.setTransitive(false);
			}
			// and the p2 ones
			var nonMavenClasspath = new ArrayList<File>();
			nonMavenClasspath.addAll(query.getJarsNotOnMavenCentral());
			var classpathSorted = Launcher.copyAndSortClasspath(nonMavenClasspath);
			SignedJars.stripIfNecessary(classpathSorted);
			for (var nested : NestedJars.inFiles(classpathSorted).extractAllNestedJars()) {
				classpathSorted.add(nested.getValue());
			}
			SignedJars.stripIfNecessary(classpathSorted);
			project.getDependencies().add(config, project.files(classpathSorted));
			replace$osgiplatformWith(project.getConfigurations().getByName(config), "");
		}
	}

	/** Add the pure-maven deps, for which we respect transitivity. */
	static void addPureMavenDeps(P2Model model, Project project, String configuration) {
		// add the pure maven deps
		for (String mavenCoord : model.getPureMaven()) {
			project.getDependencies().add(configuration, convertPureMaven(project, mavenCoord));
		}
	}

	private static Object convertPureMaven(Project project, String coordinate) {
		if (coordinate.startsWith(":")) {
			return project.project(coordinate);
		} else {
			return coordinate;
		}
	}

	private static final String $_OSGI_PLATFORM = "${osgi.platform}";

	/**
	 * Replace the ${osgi.platform} artifacts, either with the running platform or the "parent"
	 * artifact to effectively remove it.
	 */
	static void replace$osgiplatformWith(Configuration config, String replacement) {
		config
				.getResolutionStrategy()
				.eachDependency(
						details -> {
							ModuleVersionSelector req = details.getRequested();
							if (req.getName().contains($_OSGI_PLATFORM)) {
								details.useTarget(
										req.getGroup()
												+ ":"
												+ req.getName().replace($_OSGI_PLATFORM, replacement)
												+ ":"
												+ req.getVersion());
							}
						});
	}
}
