package pkg;

import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.ide.application.DelayedEventsProcessor;
import org.eclipse.ui.internal.ide.application.IDEWorkbenchAdvisor;

class Main {
	public static void main(String[] args) {
		System.setProperty(org.slf4j.simple.SimpleLogger.DEFAULT_LOG_LEVEL_KEY, "INFO");
		OsgiShim.initialize(new EquinotConfiguration() {});

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
