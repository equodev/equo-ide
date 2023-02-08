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

import java.lang.reflect.InvocationTargetException;
import org.eclipse.ui.internal.Workbench;
import org.osgi.framework.BundleContext;

class BuildshipImpl implements IdeHookInstantiated {
	IdeHookBuildship data;

	/**
	 * Logic comes from {@link org.eclipse.buildship.ui.internal.wizard.project.ProjectImportWizard}
	 */
	BuildshipImpl(IdeHookBuildship data) {
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
	public void postStartup() {
		if (!isClean) {
			// we only need to import the project when the IDE is fresh
			return;
		}
		try {
			Workbench.getInstance()
					.getActiveWorkbenchWindow()
					.run(
							true,
							true,
							monitor -> {
								monitor.beginTask("Testing", 5);
								for (int i = 0; i < 5; ++i) {
									Thread.sleep(1_000);
									monitor.worked(1);
									monitor.subTask(Integer.toString(i) + " of 5");
								}
								monitor.done();
							});
		} catch (InvocationTargetException e) {
			throw new RuntimeException(e);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}

		//		var configuration = new ProjectImportConfiguration();
		//		configuration.setProjectDir(data.rootDir);
		//		configuration.setOverwriteWorkspaceSettings(false);
		//
		//	configuration.setDistribution(GradleDistributionViewModel.from(GradleDistribution.fromBuild()));
		//		configuration.setOfflineMode(data.isOffline);
		//
		//		var internalBuildConfiguration = configuration.toInternalBuildConfiguration();
		// TODO: very incomplete.
	}
}
