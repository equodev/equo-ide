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
package dev.equo.ide;

import org.eclipse.buildship.core.GradleBuild;
import org.eclipse.buildship.core.GradleCore;
import org.eclipse.buildship.core.GradleDistribution;
import org.eclipse.buildship.ui.internal.util.gradle.GradleDistributionViewModel;
import org.eclipse.buildship.ui.internal.wizard.project.ProjectImportConfiguration;
import org.osgi.framework.BundleContext;

class Buildship implements IdeHookInstantiated {
	IdeHookBuildship data;

	/**
	 * Logic comes from {@link org.eclipse.buildship.ui.internal.wizard.project.ProjectImportWizard}
	 */
	Buildship(IdeHookBuildship data) {
		this.data = data;
	}

	boolean isClean;

	/**
	 * The very first method to be called, called as soon as the command line arguments have been
	 * parsed.
	 */
	@Override
	public void isClean(boolean isClean) {
		this.isClean = isClean;
	}

	BundleContext context;

	/** Called after the OSGi container is created and populated. */
	@Override
	public void afterOsgi(BundleContext context) {
		this.context = context;
	}

	/**
	 * This method is called during workbench initialization prior to any windows being opened.
	 *
	 * <p>{@link org.eclipse.ui.application.WorkbenchAdvisor#initialize}
	 */
	@Override
	public void initialize() {
		if (isClean) {
			// we only need to import the project when the IDE is fresh
			return;
		}
		var configuration = new ProjectImportConfiguration();
		configuration.setProjectDir(data.rootDir);
		configuration.setOverwriteWorkspaceSettings(false);
		configuration.setDistribution(GradleDistributionViewModel.from(GradleDistribution.fromBuild()));
		configuration.setOfflineMode(data.isOffline);

		var internalBuildConfiguration = configuration.toInternalBuildConfiguration();
		boolean showExecutionsView =
				internalBuildConfiguration.getWorkspaceConfiguration().isShowExecutionsView();
		var buildConfiguration = configuration.toBuildConfiguration();
		GradleBuild gradleBuild = GradleCore.getWorkspace().createBuild(buildConfiguration);

		// TODO: very incomplete.
	}
}
