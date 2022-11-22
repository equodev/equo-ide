package pkg;

import java.io.IOException;
import java.io.InputStream;
import java.util.PropertyResourceBundle;
import org.eclipse.core.internal.registry.ExtensionRegistry;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.RegistryFactory;
import org.eclipse.core.runtime.spi.RegistryContributor;
import org.osgi.framework.BundleException;
import org.slf4j.Logger;

class ShimPluginXml {
	private static final String PLUGIN_XML = "plugin.xml";
	private static final String PLUGIN_PROPERTIES = "plugin.properties";

	static boolean hasPluginXml(OsgiShim.ShimBundle bundle) {
		try {
			InputStream stream = bundle.getEntry("plugin.xml").openStream();
			stream.close();
			return true;
		} catch (IOException e) {
			return false;
		}
	}

	static void register(OsgiShim.ShimBundle bundle) throws IOException, BundleException {
		IExtensionRegistry reg = RegistryFactory.getRegistry();

		var contributor =
				new RegistryContributor(
						Long.toString(bundle.getBundleId()), bundle.getSymbolicName(), null, null);
		var properties = new PropertyResourceBundle(bundle.getEntry(PLUGIN_PROPERTIES).openStream());
		var token = ((ExtensionRegistry) reg).getTemporaryUserToken();
		boolean added =
				reg.addContribution(
						bundle.getEntry(PLUGIN_XML).openStream(), contributor, false, null, properties, token);
		if (!added) {
			throw new BundleException(
					"Could not add plugin: " + bundle.getSymbolicName(), BundleException.REJECTED_BY_HOOK);
		}
	}

	static void logExtensions(Logger logger) throws IOException, BundleException {
		var points = RegistryFactory.getRegistry().getExtensionPoints();
		for (var point : points) {
			logger.info("{} point {}", point.getContributor().getName(), point.getLabel());
			for (var extension : point.getExtensions()) {
				logger.info(
						"  {} extension {}", extension.getContributor().getName(), extension.getLabel());
				for (var cfg : extension.getConfigurationElements()) {
					logger.info("    {}={}", cfg.getName(), cfg.getValue());
					for (var key : cfg.getAttributeNames()) {
						logger.info("    {}={}", key, cfg.getAttribute(key));
					}
				}
				logger.info(
						"  {} extension {}", extension.getContributor().getName(), extension.getLabel());
			}
		}
	}
}
