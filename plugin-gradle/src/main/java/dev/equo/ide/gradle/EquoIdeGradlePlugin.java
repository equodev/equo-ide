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

import com.diffplug.common.swt.os.SwtPlatform;
import dev.equo.solstice.P2AsMaven;
import java.io.File;
import java.io.IOException;
import org.gradle.api.GradleException;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.artifacts.ModuleVersionSelector;
import org.gradle.api.attributes.Bundling;

public class EquoIdeGradlePlugin implements Plugin<Project> {
	static final String MINIMUM_GRADLE = "5.0";

	private static final String EQUO_IDE = "equoIde";
	private static final String $_OSGI_PLATFORM = "${osgi.platform}";

	@Override
	public void apply(Project project) {
		EquoIdeExtension extension = project.getExtensions().create(EQUO_IDE, EquoIdeExtension.class);
		var configuration =
				project
						.getConfigurations()
						.create(
								EQUO_IDE,
								config -> {
									config
											.getAttributes()
											.attribute(
													Bundling.BUNDLING_ATTRIBUTE,
													project.getObjects().named(Bundling.class, Bundling.EXTERNAL));
									config
											.getResolutionStrategy()
											.eachDependency(
													details -> {
														ModuleVersionSelector req = details.getRequested();
														if (req.getName().contains($_OSGI_PLATFORM)) {
															String running = SwtPlatform.getRunning().toString();
															details.useTarget(
																	req.getGroup()
																			+ ":"
																			+ req.getName().replace($_OSGI_PLATFORM, running)
																			+ ":"
																			+ req.getVersion());
														}
													});
								});

		project.getRepositories().mavenCentral();

		try {
			for (var dep : DepsResolve.resolveFiles()) {
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
		for (String dep : P2AsMaven.jdtDeps()) {
			project.getDependencies().add(EQUO_IDE, dep);
		}
		var installDir = new File(project.getBuildDir(), EQUO_IDE);
		project
				.getTasks()
				.register(
						EQUO_IDE,
						EquoIdeTask.class,
						task -> {
							task.setGroup("IDE");
							task.setDescription("Launches EquoIDE");

							task.getExtension().set(extension);
							task.getClassPath().set(configuration);
							task.getInstallDir().set(installDir);
						});
	}
}
