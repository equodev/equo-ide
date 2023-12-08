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
package dev.equo.ide.gradle;

import com.diffplug.common.swt.os.SwtPlatform;
import dev.equo.ide.Catalog;
import dev.equo.ide.WorkspaceInit;
import dev.equo.solstice.NestedJars;
import java.io.File;
import java.io.IOException;
import org.gradle.api.GradleException;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.artifacts.Configuration;
import org.gradle.api.artifacts.ModuleDependency;
import org.gradle.api.attributes.Bundling;
import org.gradle.api.tasks.TaskProvider;

public class EquoIdeGradlePlugin implements Plugin<Project> {
	private static final String TASK_GROUP = "IDE";
	static final String EQUO_IDE = "equoIde";
	private static final String EQUO_LIST = "equoList";

	private static final String USE_ATOMOS_FLAG = "--use-atomos=";

	@Override
	public void apply(Project project) {
		GradleCommon.initialize(project, "equoIde");

		var extension = project.getExtensions().create(EQUO_IDE, EquoIdeExtension.class, project);
		extension.branding.title(project.getName());

		boolean equoIdeWasCalledDirectly = GradleCommon.anyArgEquals(project, EQUO_IDE);
		var equoIde = createConfiguration(project, EQUO_IDE);
		var equoIdeTask =
				project
						.getTasks()
						.register(
								EQUO_IDE,
								EquoIdeTask.class,
								task -> {
									task.setGroup(TASK_GROUP);
									task.setDescription("Launches an Eclipse-based IDE for this project");
									task.getEquoIdeWasCalledDirectly().set(equoIdeWasCalledDirectly);
								});
		project
				.getTasks()
				.register(
						EQUO_LIST,
						EquoListTask.class,
						task -> {
							task.setGroup(TASK_GROUP);
							task.setDescription("Lists the p2 dependencies of an Eclipse application");

							task.getClientCaching().set(P2ModelDsl.clientCaching(project));
							task.getExtension().set(extension);
						});
		if (equoIdeWasCalledDirectly) {
			configureEquoTasks(project, extension, equoIde, equoIdeTask);
		}
	}

	private static void configureEquoTasks(
			Project project,
			EquoIdeExtension extension,
			Configuration equoIde,
			TaskProvider<EquoIdeTask> equoIdeTask) {
		try {
			for (var dep : DepsResolve.resolveSolsticeAndTransitives()) {
				if (dep instanceof File) {
					project.getDependencies().add(EQUO_IDE, project.files(dep));
				} else if (dep instanceof String) {
					project.getDependencies().add(EQUO_IDE, dep);
				} else {
					throw new IllegalArgumentException("Expected String or File, got " + dep);
				}
			}
		} catch (IOException e) {
			throw new GradleException("Unable to determine solstice version", e);
		}
		project.afterEvaluate(
				unused -> {
					try {
						var workspace = new WorkspaceInit();
						var model = extension.prepareModel(workspace);
						var query =
								model.query(P2ModelDsl.clientCaching(project), P2ModelDsl.queryCaching(project));
						workspace.copyAllFrom(extension.workspace);
						boolean useAtomosOverrideTrue =
								GradleCommon.anyArgMatching(
										project,
										arg ->
												arg.startsWith(USE_ATOMOS_FLAG)
														&& Boolean.parseBoolean(arg.substring(USE_ATOMOS_FLAG.length())));
						boolean useAtomos = extension.useAtomos || useAtomosOverrideTrue;
						for (var dep :
								NestedJars.transitiveDeps(useAtomos, NestedJars.CoordFormat.GRADLE, query)) {
							project.getDependencies().add(EQUO_IDE, dep);
						}
						// add the pure-maven deps
						P2DepsExtension.addPureMavenDeps(model, project, EQUO_IDE);
						// then the p2-resolved maven deps
						for (var coordinate : query.getJarsOnMavenCentral()) {
							ModuleDependency dep =
									(ModuleDependency) project.getDependencies().add(EQUO_IDE, coordinate);
							dep.setTransitive(false);
						}
						if (Catalog.EQUO_CHROMIUM.isEnabled(model)) {
							project.getRepositories().maven((a) -> a.setUrl(Catalog.EQUO_CHROMIUM.mavenRepo()));
						}
						equoIdeTask.configure(
								task -> {
									task.getQuery().set(query);
									task.getMavenDeps().set(equoIde);
									task.getProjectDir().set(project.getProjectDir());
									// extension.useAtomos is on purpose, override is parsed inside the task
									task.getUseAtomos().set(extension.useAtomos);
									task.ideHooks = extension.getIdeHooks();
									task.workspace = workspace;
								});
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
				});
	}

	private Configuration createConfiguration(Project project, String name) {
		return project
				.getConfigurations()
				.create(
						name,
						config -> {
							config.attributes(
									attr -> {
										attr.attribute(
												Bundling.BUNDLING_ATTRIBUTE,
												project.getObjects().named(Bundling.class, Bundling.EXTERNAL));
									});
							config.setCanBeConsumed(false);
							config.setVisible(false);
							P2DepsExtension.replace$osgiplatformWith(config, SwtPlatform.getRunning().toString());
						});
	}
}
