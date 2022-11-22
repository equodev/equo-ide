package pkg;

import java.util.Collections;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.ide.application.DelayedEventsProcessor;
import org.eclipse.ui.internal.ide.application.IDEWorkbenchAdvisor;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.service.application.ApplicationException;

class Main {
	public static void main(String[] args) throws InvalidSyntaxException, ApplicationException {
		System.setProperty(org.slf4j.simple.SimpleLogger.DEFAULT_LOG_LEVEL_KEY, "INFO");
		var osgiShim = OsgiShim.initialize(new EquinotConfiguration() {});

		var appServices =
				osgiShim.getServiceReferences(
						org.osgi.service.application.ApplicationDescriptor.class,
						"(service.pid=org.eclipse.ui.ide.workbench)");
		if (appServices.size() != 1) {
			throw new IllegalArgumentException("Expected exactly one application, got " + appServices);
		}
		var appDescriptor = osgiShim.getService(appServices.iterator().next());
		var appHandle = appDescriptor.launch(Collections.emptyMap());

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
