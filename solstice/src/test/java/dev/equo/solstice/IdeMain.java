package dev.equo.solstice;

import java.util.HashMap;
import java.util.Map;
import org.eclipse.core.runtime.internal.adaptor.EclipseAppLauncher;
import org.eclipse.equinox.app.IApplicationContext;
import org.eclipse.osgi.service.runnable.ApplicationLauncher;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.ide.application.DelayedEventsProcessor;
import org.eclipse.ui.internal.ide.application.IDEWorkbenchAdvisor;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.service.application.ApplicationException;
import org.slf4j.simple.SimpleLogger;

class IdeMain {
	public static void main(String[] args) throws InvalidSyntaxException, ApplicationException {
		System.setProperty(SimpleLogger.DEFAULT_LOG_LEVEL_KEY, "INFO");
		System.setProperty(SimpleLogger.LOG_FILE_KEY, "System.out");

		var osgiShim = OsgiShim.initialize(new EquinotConfiguration() {});
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
		var appHandle = appDescriptor.launch(appProps);

		var display = PlatformUI.createDisplay();
		// processor must be created before we start event loop
		var processor = new DelayedEventsProcessor(display);
		int exitCode = PlatformUI.createAndRunWorkbench(display, new IDEWorkbenchAdvisor(processor));
		if (exitCode == PlatformUI.RETURN_OK) {
			System.exit(0);
		} else {
			System.err.println("Unexpected exit code: " + exitCode);
			System.exit(1);
		}
	}
}
