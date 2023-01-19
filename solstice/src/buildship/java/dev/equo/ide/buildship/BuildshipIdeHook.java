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
package dev.equo.ide.buildship;

import dev.equo.solstice.IdeHookInstantiated;
import java.io.File;
import org.eclipse.buildship.core.GradleBuild;
import org.eclipse.buildship.core.GradleCore;
import org.eclipse.buildship.core.GradleDistribution;
import org.eclipse.buildship.ui.internal.util.gradle.GradleDistributionViewModel;
import org.eclipse.buildship.ui.internal.wizard.project.ProjectImportConfiguration;

public class BuildshipIdeHook implements IdeHookInstantiated {
	File rootDir;
	boolean isOffline;

	/**
	 * Logic comes from {@link org.eclipse.buildship.ui.internal.wizard.project.ProjectImportWizard}
	 */
	public BuildshipIdeHook(File rootDir, boolean isOffline) {
		this.rootDir = rootDir;
		this.isOffline = isOffline;
	}

	@Override
	public void initialize() {
		var configuration = new ProjectImportConfiguration();
		configuration.setProjectDir(rootDir);
		configuration.setOverwriteWorkspaceSettings(false);
		configuration.setDistribution(GradleDistributionViewModel.from(GradleDistribution.fromBuild()));
		configuration.setOfflineMode(isOffline);

		var internalBuildConfiguration = configuration.toInternalBuildConfiguration();
		boolean showExecutionsView =
				internalBuildConfiguration.getWorkspaceConfiguration().isShowExecutionsView();
		var buildConfiguration = configuration.toBuildConfiguration();
		GradleBuild gradleBuild = GradleCore.getWorkspace().createBuild(buildConfiguration);

		// TODO: very incomplete.
	}
}
