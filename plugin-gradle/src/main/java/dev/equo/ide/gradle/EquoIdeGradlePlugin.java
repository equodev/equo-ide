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

import dev.equo.solstice.p2.CacheLocations;
import dev.equo.solstice.p2.P2Client;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.gradle.api.GradleException;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.artifacts.Configuration;
import org.gradle.api.artifacts.ModuleDependency;
import org.gradle.api.attributes.Bundling;

public class EquoIdeGradlePlugin implements Plugin<Project> {
	static final String MINIMUM_GRADLE = "6.0";

	private static final String TASK_GROUP = "IDE";
	private static final String EQUO_IDE = "equoIde";

	private static String USE_ATOMOS_FLAG = "--use-atomos=";

	@Override
	public void apply(Project project) {
		if (gradleIsTooOld(project)) {
			throw new GradleException("equoIde requires Gradle 6.0 or later");
		}
		setCacheLocations(project);

		EquoIdeExtension extension =
				project.getExtensions().create(EQUO_IDE, EquoIdeExtension.class, project);
		extension.branding.title(project.getName());
		Configuration equoIde = createConfigurationWithTransitives(project, EQUO_IDE);

		project.getRepositories().mavenCentral();
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
		project.getDependencies().add(EQUO_IDE, "org.slf4j:slf4j-simple:1.7.36");

		P2Client.Caching caching = P2ModelDsl.caching(project);
		var equoIdeTask =
				project
						.getTasks()
						.register(
								EQUO_IDE,
								EquoIdeTask.class,
								task -> {
									task.setGroup(TASK_GROUP);
									task.setDescription("Launches an Eclipse-based IDE for this project");
								});
		project.afterEvaluate(
				unused -> {
					try {
						boolean useAtomosOverride =
								project.getGradle().getStartParameter().getTaskRequests().stream()
										.flatMap(taskReq -> taskReq.getArgs().stream())
										.filter(
												arg ->
														arg.startsWith(USE_ATOMOS_FLAG)
																&& Boolean.parseBoolean(arg.substring(USE_ATOMOS_FLAG.length())))
										.findAny()
										.isPresent();
						if (extension.useAtomos || useAtomosOverride) {
							project
									.getDependencies()
									.add(EQUO_IDE, "org.apache.felix:org.apache.felix.atomos:1.0.0");
							project
									.getDependencies()
									.add(EQUO_IDE, "org.apache.felix.atomos:osgi.core:8.0.0:AtomosEquinox");
						}
						var query = extension.performQuery(caching);
						query
								.getJarsOnMavenCentral()
								.forEach(
										coordinate -> {
											ModuleDependency dep =
													(ModuleDependency) project.getDependencies().add(EQUO_IDE, coordinate);
											dep.setTransitive(false);
										});
						equoIdeTask.configure(
								task -> {
									task.getCaching().set(caching);
									task.getQuery().set(query);
									task.getMavenDeps().set(equoIde);
									task.getProjectDir().set(project.getProjectDir());
									task.getUseAtomos().set(extension.useAtomos);
									task.ideHooks = extension.getIdeHooks();
								});
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
				});
		project
				.getTasks()
				.register(
						"equoList",
						EquoListTask.class,
						task -> {
							task.setGroup(TASK_GROUP);
							task.setDescription("Lists the p2 dependencies of an Eclipse application");

							task.getCaching().set(caching);
							task.getExtension().set(extension);
						});
	}

	private Configuration createConfigurationWithTransitives(Project project, String name) {
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
						});
	}

	static void setCacheLocations(Project project) {
		// let the user override cache locations from ~/.gradle/gradle.properties
		for (Field field : CacheLocations.class.getFields()) {
			Object value = project.findProperty("equo_" + field.getName());
			if (value != null) {
				try {
					field.set(null, new File(value.toString()));
				} catch (IllegalAccessException e) {
					throw new RuntimeException(e);
				}
			}
		}
	}

	private static final Pattern BAD_SEMVER = Pattern.compile("(\\d+)\\.(\\d+)");

	static boolean gradleIsTooOld(Project project) {
		return badSemver(project.getGradle().getGradleVersion()) < badSemver(MINIMUM_GRADLE);
	}

	private static int badSemver(String input) {
		Matcher matcher = BAD_SEMVER.matcher(input);
		if (!matcher.find() || matcher.start() != 0) {
			throw new IllegalArgumentException("Version must start with " + BAD_SEMVER.pattern());
		}
		String major = matcher.group(1);
		String minor = matcher.group(2);
		return badSemver(Integer.parseInt(major), Integer.parseInt(minor));
	}

	/** Ambiguous after 2147.483647.blah-blah */
	private static int badSemver(int major, int minor) {
		return major * 1_000_000 + minor;
	}
}
