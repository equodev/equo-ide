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

import dev.equo.solstice.Solstice;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import org.eclipse.core.runtime.internal.adaptor.EclipseAppLauncher;
import org.eclipse.equinox.app.IApplicationContext;
import org.eclipse.osgi.service.runnable.ApplicationLauncher;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.application.IWorkbenchConfigurer;
import org.eclipse.ui.internal.ide.application.DelayedEventsProcessor;
import org.eclipse.ui.internal.ide.application.IDEWorkbenchAdvisor;
import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.service.application.ApplicationException;

class IdeMainUi {
	static int main(BundleContext osgiShim, IdeHook.InstantiatedList ideHooks)
			throws InvalidSyntaxException {
		var appServices =
				osgiShim.getServiceReferences(
						org.osgi.service.application.ApplicationDescriptor.class,
						"(service.pid=org.eclipse.ui.ide.workbench)");
		if (appServices.size() != 1) {
			throw new IllegalArgumentException("Expected exactly one application, got " + appServices);
		}

		var appDescriptor = osgiShim.getService(appServices.iterator().next());

		var appLauncher = new EclipseAppLauncher(osgiShim, false, false, null, null);
		var emptyDict = new Hashtable<String, Object>();
		osgiShim.registerService(ApplicationLauncher.class, appLauncher, emptyDict);

		Map<String, Object> appProps = new HashMap<>();
		appProps.put(IApplicationContext.APPLICATION_ARGS, new String[] {});
		try {
			var appHandle = appDescriptor.launch(appProps);
		} catch (ApplicationException e) {
			throw new RuntimeException(e);
		}
		var display = PlatformUI.createDisplay();

		// processor must be created before we start event loop
		var processor = new DelayedEventsProcessor(display);
		return PlatformUI.createAndRunWorkbench(
				display,
				new IDEWorkbenchAdvisor(processor) {
					@Override
					public void initialize(IWorkbenchConfigurer configurer) {
						if (osgiShim instanceof Solstice) {
							((Solstice) osgiShim).startAllWithLazy(true);
						}
						super.initialize(configurer);
						ideHooks.forEach(IdeHookInstantiated::initialize);
					}

					@Override
					public void preStartup() {
						super.preStartup();
						ideHooks.forEach(IdeHookInstantiated::preStartup);
					}

					@Override
					public void postStartup() {
						super.postStartup();
						ideHooks.forEach(IdeHookInstantiated::postStartup);
					}

					@Override
					public boolean preShutdown() {
						boolean shutdownAllowed = super.preShutdown();
						var iter = ideHooks.iterator();
						while (shutdownAllowed && iter.hasNext()) {
							shutdownAllowed = iter.next().preShutdown();
						}
						return shutdownAllowed;
					}

					@Override
					public void postShutdown() {
						super.postShutdown();
						ideHooks.forEach(IdeHookInstantiated::postShutdown);
					}
				});
	}
}
