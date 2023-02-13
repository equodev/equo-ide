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

import com.google.common.collect.ImmutableList;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.eclipse.buildship.core.GradleCore;
import org.eclipse.buildship.core.GradleDistribution;
import org.eclipse.buildship.core.SynchronizationResult;
import org.eclipse.buildship.core.internal.DefaultGradleBuild;
import org.eclipse.buildship.core.internal.workspace.NewProjectHandler;
import org.eclipse.buildship.ui.internal.util.gradle.GradleDistributionViewModel;
import org.eclipse.buildship.ui.internal.util.workbench.WorkbenchUtils;
import org.eclipse.buildship.ui.internal.util.workbench.WorkingSetUtils;
import org.eclipse.buildship.ui.internal.wizard.project.ProjectImportConfiguration;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkingSet;
import org.eclipse.ui.IWorkingSetManager;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.Workbench;
import org.gradle.tooling.GradleConnector;
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
	public void postStartup() throws Exception {
		if (!isClean) {
			// we only need to import the project when the IDE is fresh
			return;
		}
		Workbench.getInstance().getActiveWorkbenchWindow().run(true, true, this::doImport);
	}

	private void doImport(IProgressMonitor monitor) throws InvocationTargetException {
		var importCfg = new ProjectImportConfiguration();
		importCfg.setProjectDir(data.rootDir);
		importCfg.setOfflineMode(data.isOffline);

		var distribution = GradleDistribution.fromBuild();
		importCfg.setDistribution(GradleDistributionViewModel.from(distribution));

		importCfg.setAutoSync(true);
		importCfg.setApplyWorkingSets(true);
		importCfg.setBuildScansEnabled(false);
		importCfg.setShowConsoleView(true);
		importCfg.setShowExecutionsView(true);
		importCfg.setOverwriteWorkspaceSettings(true);
		importCfg.setWorkingSets(Collections.emptyList());

		var buildCfg = importCfg.toBuildConfiguration();
		var gradleBuild = GradleCore.getWorkspace().createBuild(buildCfg);
		var workingSetsAddingNewProjectHandler =
				new ImportWizardNewProjectHandler(
						NewProjectHandler.IMPORT_AND_MERGE,
						importCfg,
						importCfg.getShowExecutionsView().getValue());
		SynchronizationResult result =
				((DefaultGradleBuild) gradleBuild)
						.synchronize(
								workingSetsAddingNewProjectHandler,
								GradleConnector.newCancellationTokenSource(),
								monitor);
		if (!result.getStatus().isOK()) {
			throw new InvocationTargetException(new CoreException(result.getStatus()));
		}
	}

	private static final class ImportWizardNewProjectHandler implements NewProjectHandler {
		private final ProjectImportConfiguration configuration;
		private final NewProjectHandler importedBuildDelegate;
		private final boolean showExecutionsView;
		private volatile boolean gradleViewsVisible;

		private ImportWizardNewProjectHandler(
				NewProjectHandler delegate,
				ProjectImportConfiguration configuration,
				boolean showExecutionsView) {
			this.importedBuildDelegate = delegate;
			this.configuration = configuration;
			this.showExecutionsView = showExecutionsView;
		}

		public boolean shouldImportNewProjects() {
			return this.importedBuildDelegate.shouldImportNewProjects();
		}

		public void afterProjectImported(IProject project) {
			this.importedBuildDelegate.afterProjectImported(project);
			this.addWorkingSets(project);
			this.ensureGradleViewsAreVisible();
		}

		private void addWorkingSets(IProject project) {
			List<String> workingSetNames =
					configuration.getApplyWorkingSets().getValue()
							? ImmutableList.copyOf(
									(Collection<String>) this.configuration.getWorkingSets().getValue())
							: ImmutableList.of();
			IWorkingSetManager workingSetManager = PlatformUI.getWorkbench().getWorkingSetManager();
			IWorkingSet[] workingSets = WorkingSetUtils.toWorkingSets(workingSetNames);
			workingSetManager.addToWorkingSets(project, workingSets);
		}

		private void ensureGradleViewsAreVisible() {
			Display.getDefault()
					.asyncExec(
							() -> {
								WorkbenchUtils.showView(
										"org.eclipse.buildship.ui.views.taskview", (String) null, 1);
								WorkbenchUtils.showView(
										"org.eclipse.buildship.ui.views.executionview", (String) null, 2);
							});
		}
	}
}
