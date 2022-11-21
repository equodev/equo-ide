package pkg;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.zip.ZipFile;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import org.apache.felix.scr.impl.inject.internal.ComponentMethodsImpl;
import org.apache.felix.scr.impl.logger.BundleLogger;
import org.apache.felix.scr.impl.logger.ComponentLogger;
import org.apache.felix.scr.impl.logger.InternalLogger;
import org.apache.felix.scr.impl.logger.NoOpLogger;
import org.apache.felix.scr.impl.manager.AbstractComponentManager;
import org.apache.felix.scr.impl.manager.ComponentActivator;
import org.apache.felix.scr.impl.manager.ComponentContainer;
import org.apache.felix.scr.impl.manager.DependencyManager;
import org.apache.felix.scr.impl.manager.ExtendedServiceEvent;
import org.apache.felix.scr.impl.manager.ExtendedServiceListener;
import org.apache.felix.scr.impl.manager.RegionConfigurationSupport;
import org.apache.felix.scr.impl.manager.ScrConfiguration;
import org.apache.felix.scr.impl.manager.SingleComponentManager;
import org.apache.felix.scr.impl.metadata.ComponentMetadata;
import org.apache.felix.scr.impl.xml.XmlHandler;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.xml.sax.SAXException;

class ShimDS {
	static List<String> starDotXML(String jarFile) throws IOException {
		String prefix = "jar:file:";
		if (!jarFile.startsWith(prefix)) {
			throw new IllegalArgumentException("jar does not start with expected prefix: " + jarFile);
		}
		if (!jarFile.endsWith("!")) {
			throw new IllegalArgumentException("jar does not end with expected suffix: !");
		}
		List<String> dotXml = new ArrayList<>();
		try (ZipFile file = new ZipFile(jarFile.substring(prefix.length(), jarFile.length() - 1))) {
			var entries = file.entries();
			while (entries.hasMoreElements()) {
				var entry = entries.nextElement().getName();
				if (entry.startsWith("OSGI-INF/") && entry.endsWith(".xml")) {
					dotXml.add(entry);
				}
			}
		}
		return dotXml;
	}

	public static void register(OsgiShim.ShimBundle bundle)
			throws ParserConfigurationException, SAXException, ClassNotFoundException {
		var saxFactory = bundle.getService(bundle.getServiceReference(SAXParserFactory.class));
		var handler = new XmlHandler(bundle, new NoOpLogger(), false, false, null);
		for (String s : bundle.osgiDS) {
			try (InputStream stream = bundle.getEntry(s).openStream()) {
				saxFactory.newSAXParser().parse(stream, handler);
			} catch (IOException e) {
				System.out.println(" url=" + bundle.getEntry(s));
				System.out.println("  Parse error for " + bundle + " " + e.getMessage());
			}
		}
		final NoOpLogger logger = new NoOpLogger();
		final ComponentActivator activator =
				new ComponentActivatorUnsupported() {
					@Override
					public <T> boolean enterCreate(ServiceReference<T> reference) {
						// returning false means no circular dependency
						return false;
					}

					@Override
					public <T> void leaveCreate(ServiceReference<T> reference) {}

					@Override
					public BundleContext getBundleContext() {
						return bundle;
					}

					@Override
					public ScrConfiguration getConfiguration() {
						return new ShimScrConfiguration();
					}
				};

		for (var metadata : handler.getComponentMetadataList()) {
			final ComponentMetadata metadataFinal = metadata;
			var container =
					new ComponentContainer() {
						@Override
						public ComponentMetadata getComponentMetadata() {
							return metadataFinal;
						}

						@Override
						public ComponentActivator getActivator() {
							return activator;
						}

						@Override
						public ComponentLogger getLogger() {
							return logger;
						}

						@Override
						public void disposed(SingleComponentManager component) {
							throw new UnsupportedOperationException();
						}
					};
			var methods = new ComponentMethodsImpl();
			var componentManager = new SingleComponentManager(container, methods);
			var serviceMetadata = metadataFinal.getServiceMetadata();
			if (serviceMetadata == null) {
				// this means it should just be instantiated, it's not part of the normal "service" thing
				var registration =
						bundle.registerService(
								metadataFinal.getImplementationClassName(),
								componentManager,
								componentManager.getServiceProperties());
				Object instance = bundle.getService(registration.getReference());
				System.out.println("  instantiated " + instance);
			} else {
				String[] provides = metadataFinal.getServiceMetadata().getProvides();
				System.out.println(
						"  register "
								+ Arrays.asList(provides)
								+ " with "
								+ metadataFinal.getImplementationClassName());
				if (provides.length == 1) {
					bundle.registerService(
							Class.forName(provides[0]),
							componentManager,
							componentManager.getServiceProperties());
				} else {
					for (String p : provides) {
						bundle.registerService(
								Class.forName(p), componentManager, componentManager.getServiceProperties());
					}
				}
			}
		}
	}

	static class ComponentActivatorUnsupported implements ComponentActivator {
		@Override
		public BundleLogger getLogger() {
			throw new UnsupportedOperationException();
		}

		@Override
		public BundleContext getBundleContext() {
			throw new UnsupportedOperationException();
		}

		@Override
		public boolean isActive() {
			throw new UnsupportedOperationException();
		}

		@Override
		public ScrConfiguration getConfiguration() {
			throw new UnsupportedOperationException();
		}

		@Override
		public void schedule(Runnable runnable) {
			throw new UnsupportedOperationException();
		}

		@Override
		public long registerComponentId(AbstractComponentManager<?> sAbstractComponentManager) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void unregisterComponentId(AbstractComponentManager<?> sAbstractComponentManager) {
			throw new UnsupportedOperationException();
		}

		@Override
		public <T> boolean enterCreate(ServiceReference<T> reference) {
			throw new UnsupportedOperationException();
		}

		@Override
		public <T> void leaveCreate(ServiceReference<T> reference) {
			throw new UnsupportedOperationException();
		}

		@Override
		public <S, T> void registerMissingDependency(
				DependencyManager<S, T> dependencyManager,
				ServiceReference<T> serviceReference,
				int trackingCount) {
			throw new UnsupportedOperationException();
		}

		@Override
		public <T> void missingServicePresent(ServiceReference<T> serviceReference) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void enableComponent(String name) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void disableComponent(String name) {
			throw new UnsupportedOperationException();
		}

		@Override
		public RegionConfigurationSupport setRegionConfigurationSupport(ServiceReference reference) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void unsetRegionConfigurationSupport(RegionConfigurationSupport rcs) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void updateChangeCount() {
			throw new UnsupportedOperationException();
		}

		@Override
		public ServiceReference<?> getTrueCondition() {
			throw new UnsupportedOperationException();
		}

		@Override
		public void addServiceListener(
				String serviceFilterString, ExtendedServiceListener<ExtendedServiceEvent> listener) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void removeServiceListener(
				String serviceFilterString, ExtendedServiceListener<ExtendedServiceEvent> listener) {
			throw new UnsupportedOperationException();
		}
	}

	static class ShimScrConfiguration implements ScrConfiguration {
		@Override
		public boolean isFactoryEnabled() {
			return false;
		}

		@Override
		public boolean keepInstances() {
			return false;
		}

		@Override
		public boolean infoAsService() {
			return false;
		}

		@Override
		public long lockTimeout() {
			return 0;
		}

		@Override
		public long stopTimeout() {
			return 0;
		}

		@Override
		public boolean globalExtender() {
			return false;
		}

		@Override
		public long serviceChangecountTimeout() {
			return 0;
		}

		@Override
		public boolean cacheMetadata() {
			return false;
		}

		@Override
		public InternalLogger.Level getLogLevel() {
			return InternalLogger.Level.INFO;
		}

		@Override
		public boolean isLogEnabled() {
			return false;
		}

		@Override
		public boolean isLogExtensionEnabled() {
			return false;
		}
	}
}
