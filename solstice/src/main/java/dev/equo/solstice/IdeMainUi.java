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
package dev.equo.solstice;

import java.util.HashMap;
import java.util.Map;
import org.eclipse.core.runtime.internal.adaptor.EclipseAppLauncher;
import org.eclipse.equinox.app.IApplicationContext;
import org.eclipse.osgi.service.runnable.ApplicationLauncher;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.application.IWorkbenchConfigurer;
import org.eclipse.ui.internal.ide.application.DelayedEventsProcessor;
import org.eclipse.ui.internal.ide.application.IDEWorkbenchAdvisor;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.service.application.ApplicationException;

public class IdeMainUi {
	static int main(Solstice osgiShim) throws InvalidSyntaxException {
		var appServices =
				osgiShim.getServiceReferences(
						org.osgi.service.application.ApplicationDescriptor.class,
						"(service.pid=org.eclipse.ui.ide.workbench)");
		if (appServices.size() != 1) {
			throw new IllegalArgumentException("Expected exactly one application, got " + appServices);
		}

		var appDescriptor = osgiShim.getService(appServices.iterator().next());

		var appLauncher = new EclipseAppLauncher(osgiShim, false, false, null, null);
		osgiShim.registerService(ApplicationLauncher.class, appLauncher, Dictionaries.empty());

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
						osgiShim.activateWorkbenchBundles();
						super.initialize(configurer);
					}
				});
	}
}
