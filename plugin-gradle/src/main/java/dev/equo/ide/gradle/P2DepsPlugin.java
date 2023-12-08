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

import org.gradle.api.GradleException;
import org.gradle.api.Plugin;
import org.gradle.api.Project;

public class P2DepsPlugin implements Plugin<Project> {
	static final String MINIMUM_GRADLE = "6.0";

	private static final String P2DEPS = "p2deps";

	@Override
	public void apply(Project project) {
		if (GradleCommon.gradleIsTooOld(project)) {
			throw new GradleException("dev.equo.p2deps requires Gradle 6.0 or later");
		}
		GradleCommon.setCacheLocations(project);

		var p2deps = project.getExtensions().create(P2DEPS, P2DepsExtension.class, project);
		project.afterEvaluate(
				unused -> {
					try {
						p2deps.configure();
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
				});
	}
}
