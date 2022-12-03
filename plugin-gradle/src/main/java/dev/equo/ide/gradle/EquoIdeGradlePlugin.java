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
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.gradle.api.GradleException;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.artifacts.ModuleVersionSelector;

public class EquoIdeGradlePlugin implements Plugin<Project> {
	static final String MINIMUM_GRADLE = "6.0";

	private static final String EQUO_IDE = "equoIde";
	private static final String $_OSGI_PLATFORM = "${osgi.platform}";

	@Override
	public void apply(Project project) {
		if (gradleIsTooOld(project)) {
			throw new GradleException("equoIde requires Gradle 6.0 or later");
		}
		EquoIdeExtension extension = project.getExtensions().create(EQUO_IDE, EquoIdeExtension.class);
		var configuration =
				project
						.getConfigurations()
						.create(
								EQUO_IDE,
								config -> {
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
