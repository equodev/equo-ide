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

import dev.equo.solstice.p2.CacheLocations;
import java.io.File;
import java.lang.reflect.Field;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.gradle.api.GradleException;
import org.gradle.api.Project;

class GradleCommon {
	static final String MINIMUM_GRADLE = "6.0";

	static void initialize(Project project, String name) {
		if (gradleIsTooOld(project)) {
			throw new GradleException(name + " requires Gradle " + MINIMUM_GRADLE + " or later");
		}
		setCacheLocations(project);
	}

	private static boolean gradleIsTooOld(Project project) {
		return badSemver(project.getGradle().getGradleVersion()) < badSemver(MINIMUM_GRADLE);
	}

	private static void setCacheLocations(Project project) {
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

	static boolean anyArgMatching(Project project, Predicate<String> predicate) {
		return project.getGradle().getStartParameter().getTaskRequests().stream()
				.flatMap(taskReq -> taskReq.getArgs().stream())
				.filter(predicate)
				.findAny()
				.isPresent();
	}

	static boolean anyArgEquals(Project project, String arg) {
		return anyArgMatching(project, a -> a.equals(arg));
	}
}
