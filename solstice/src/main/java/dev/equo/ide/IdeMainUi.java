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
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.internal.adaptor.EclipseAppLauncher;
import org.eclipse.equinox.app.IApplicationContext;
import org.eclipse.osgi.service.runnable.ApplicationLauncher;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.application.IWorkbenchConfigurer;
import org.eclipse.ui.internal.ide.application.DelayedEventsProcessor;
import org.eclipse.ui.internal.ide.application.IDEWorkbenchAdvisor;
import org.eclipse.ui.statushandlers.StatusManager;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceListener;
import org.osgi.service.application.ApplicationException;

class IdeMainUi {
	static int main(Solstice solstice, IdeHook.InstantiatedList ideHooks)
			throws InvalidSyntaxException {
		var appServices =
				solstice
						.getContext()
						.getServiceReferences(
								org.osgi.service.application.ApplicationDescriptor.class,
								"(service.pid=org.eclipse.ui.ide.workbench)");
		if (appServices.size() != 1) {
			throw new IllegalArgumentException("Expected exactly one application, got " + appServices);
		}

		var appDescriptor = solstice.getContext().getService(appServices.iterator().next());

		var appLauncher = new EclipseAppLauncher(solstice.getContext(), false, false, null, null);
		var emptyDict = new Hashtable<String, Object>();
		solstice.getContext().registerService(ApplicationLauncher.class, appLauncher, emptyDict);

		Map<String, Object> appProps = new HashMap<>();
		appProps.put(IApplicationContext.APPLICATION_ARGS, new String[] {});
		try {
			appDescriptor.launch(appProps);
		} catch (ApplicationException e) {
			throw new RuntimeException(e);
		}
		var display = PlatformUI.createDisplay();
		ideHooks.setErrorLogger(
				(hook, exception) ->
						StatusManager.getManager().handle(Status.error("IdeHook failure " + hook, exception)));

		// processor must be created before we start event loop
		var processor = new DelayedEventsProcessor(display);
		earlyStartupWorkaround(solstice);
		return PlatformUI.createAndRunWorkbench(
				display,
				new IDEWorkbenchAdvisor(processor) {
					@Override
					public void initialize(IWorkbenchConfigurer configurer) {
						solstice.startAllWithLazy(true);
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
							var hook = iter.next();
							try {
								shutdownAllowed = hook.preShutdown();
							} catch (Exception e) {
								ideHooks.logError(hook, e);
							}
						}
						return shutdownAllowed;
					}

					@Override
					public void postShutdown() {
						super.postShutdown();
						ideHooks.forEach(IdeHookInstantiated::postShutdown);
					}

					@Override
					public void eventLoopException(Throwable exception) {
						exception.printStackTrace();
						super.eventLoopException(exception);
					}
				});
	}

	/**
	 * Except for a very few lazy plugins, activating them in {@link
	 * IDEWorkbenchAdvisor#initialize(IWorkbenchConfigurer)} works perfectly. But there are a few
	 * which need to activate before that. Those are handled here.
	 */
	private static void earlyStartupWorkaround(Solstice solstice) throws InvalidSyntaxException {
		List<String> needsEarlyActivation = List.of("org.eclipse.tm.terminal.view.ui");
		var inSystem = solstice.bySymbolicName();
		var toActivateEarly =
				needsEarlyActivation.stream().filter(inSystem::containsKey).collect(Collectors.toList());
		if (toActivateEarly.isEmpty()) {
			return;
		}
		ServiceListener listener =
				new ServiceListener() {
					private boolean alreadyActivated = false;

					@Override
					public void serviceChanged(ServiceEvent event) {
						if (alreadyActivated) {
							return;
						}
						alreadyActivated = true;
						toActivateEarly.forEach(solstice::start);
					}
				};
		// there are two services we can hook to start a bit earlier than normal
		// - org.eclipse.osgi.service.runnable.StartupMonitor (first)
		// - org.osgi.service.event.EventHandler (second)
		solstice
				.getContext()
				.addServiceListener(
						listener,
						"(objectClass="
								+ org.eclipse.osgi.service.runnable.StartupMonitor.class.getName()
								+ ")");
	}
}
