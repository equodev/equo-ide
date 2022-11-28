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

import org.gradle.api.Plugin;
import org.gradle.api.Project;

public class EquoIdeGradlePlugin implements Plugin<Project> {
	static final String MINIMUM_GRADLE = "5.0";

	@Override
	public void apply(Project project) {
		EquoIdeExtension extension = project.getExtensions().create("equoIde", EquoIdeExtension.class);
		project
				.getTasks()
				.register(
						"equoIde",
						EquoIdeTask.class,
						task -> {
							task.setGroup("IDE");
							task.setDescription("Launches EquoIDE");
							task.extension = extension;
						});
	}
}
