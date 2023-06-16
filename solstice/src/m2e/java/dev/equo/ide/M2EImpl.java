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
package dev.equo.ide;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.m2e.core.MavenPlugin;
import org.eclipse.m2e.core.embedder.MavenModelManager;
import org.eclipse.m2e.core.project.LocalProjectScanner;
import org.eclipse.m2e.core.project.MavenProjectInfo;
import org.eclipse.m2e.core.project.ProjectImportConfiguration;
import org.eclipse.m2e.core.ui.internal.wizards.ImportMavenProjectsJob;
import org.eclipse.ui.IWorkingSet;
import org.eclipse.ui.internal.Workbench;
import org.osgi.framework.BundleContext;

class M2EImpl implements IdeHookInstantiated {
	IdeHookM2E data;

	M2EImpl(IdeHookM2E data) {
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

	private LocalProjectScanner instantiateScanner() {
		boolean basedirRemameRequired = false;
		var list = List.of(data.rootDir.getAbsolutePath());
		try {
			try {
				// 2.1.2 (Java 17+)
				var c =
						LocalProjectScanner.class.getConstructor(
								List.class, boolean.class, MavenModelManager.class);
				return c.newInstance(list, basedirRemameRequired, MavenPlugin.getMavenModelManager());
			} catch (NoSuchMethodException e) {
				// 1.20.1 (Java 11+)
				var c =
						LocalProjectScanner.class.getConstructor(
								File.class, List.class, boolean.class, MavenModelManager.class);
				return c.newInstance(
						data.rootDir, list, basedirRemameRequired, MavenPlugin.getMavenModelManager());
			}
		} catch (InvocationTargetException
				| InstantiationException
				| IllegalAccessException
				| NoSuchMethodException e) {
			throw new RuntimeException(e);
		}
	}

	private void doImport(IProgressMonitor monitor)
			throws InvocationTargetException, InterruptedException {
		var discoverMonitor = monitor.slice(20);
		var importMonitor = monitor.slice(80);

		var scanner = instantiateScanner();
		scanner.run(discoverMonitor);

		var importConfiguration = new ProjectImportConfiguration();
		var workingSets = List.<IWorkingSet>of();

		var projects = new HashSet<MavenProjectInfo>();
		for (var p : scanner.getProjects()) {
			addAll(p, projects);
		}
		var job = new ImportMavenProjectsJob(projects, workingSets, importConfiguration);
		var status = job.runInWorkspace(importMonitor);
		if (!status.isOK()) {
			throw new InvocationTargetException(new CoreException(status));
		}
		monitor.done();
	}

	private void addAll(MavenProjectInfo proj, Collection<MavenProjectInfo> projects) {
		projects.add(proj);
		for (var child : proj.getProjects()) {
			addAll(child, projects);
		}
	}
}
