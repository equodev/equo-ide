package pkg;

import com.diffplug.spotless.extra.eclipse.base.SpotlessEclipseConfig;
import com.diffplug.spotless.extra.eclipse.base.SpotlessEclipsePluginConfig;
import com.diffplug.spotless.extra.eclipse.base.SpotlessEclipseServiceConfig;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.ide.application.DelayedEventsProcessor;
import org.eclipse.ui.internal.ide.application.IDEWorkbenchAdvisor;

class Main {
	static class FrameworkConfig implements SpotlessEclipseConfig {
		@Override
		public void registerServices(SpotlessEclipseServiceConfig config) {
			config.applyDefault();
			config.useSlf4J(Main.class.getPackage().getName());
		}

		@Override
		public void activatePlugins(SpotlessEclipsePluginConfig config) {
			config.applyDefault();
		}
	}

	public static void main(String[] args) {
		OsgiShim.instance();

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
