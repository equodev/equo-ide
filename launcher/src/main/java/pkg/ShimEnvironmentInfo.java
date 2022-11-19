package pkg;

import com.diffplug.common.swt.os.SwtPlatform;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import org.eclipse.osgi.service.environment.EnvironmentInfo;

class ShimEnvironmentInfo implements EnvironmentInfo {
	@Override
	public String[] getCommandLineArgs() {
		return new String[0];
	}

	@Override
	public String[] getFrameworkArgs() {
		return new String[0];
	}

	@Override
	public String[] getNonFrameworkArgs() {
		return new String[0];
	}

	@Override
	public String getNL() {
		return Locale.getDefault().getLanguage();
	}

	@Override
	public String getOSArch() {
		return SwtPlatform.getRunning().getArch();
	}

	@Override
	public String getOS() {
		return SwtPlatform.getRunning().getOs();
	}

	@Override
	public String getWS() {
		return SwtPlatform.getRunning().getWs();
	}

	@Override
	public boolean inDebugMode() {
		return false;
	}

	@Override
	public boolean inDevelopmentMode() {
		return false;
	}

	private Map<String, String> properties = new HashMap<>();

	@Override
	public synchronized String getProperty(String key) {
		return properties.get(key);
	}

	@Override
	public synchronized String setProperty(String key, String value) {
		return properties.put(key, value);
	}
}
