package pkg;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.TreeMap;
import javax.xml.parsers.SAXParserFactory;
import org.eclipse.equinox.log.ExtendedLogReaderService;
import org.eclipse.equinox.log.ExtendedLogService;
import org.eclipse.osgi.framework.log.FrameworkLog;
import org.eclipse.osgi.framework.log.FrameworkLogEntry;
import org.eclipse.osgi.internal.log.ExtendedLogReaderServiceFactory;
import org.eclipse.osgi.internal.log.ExtendedLogServiceFactory;
import org.eclipse.osgi.service.datalocation.Location;
import org.eclipse.osgi.service.debug.DebugOptions;
import org.eclipse.osgi.service.debug.DebugTrace;
import org.eclipse.osgi.service.environment.EnvironmentInfo;
import org.eclipse.osgi.service.localization.BundleLocalization;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.framework.FrameworkEvent;
import org.osgi.service.log.LogLevel;
import org.osgi.service.log.LogService;
import org.osgi.service.log.LoggerFactory;

public interface EquinotConfiguration {
	default LogLevel logLevel() {
		return LogLevel.INFO;
	}

	default List<String> startOrder() {
		return Arrays.asList(
				"org.eclipse.equinox.registry",
				"org.apache.felix.scr",
				"org.eclipse.equinox.cm",
				"org.eclipse.core.runtime");
	}

	default Map<String, List<String>> additionalDeps() {
		var additional = new TreeMap<String, List<String>>();
		additional.put(
				"org.eclipse.e4.ui.services",
				Arrays.asList(
						"org.eclipse.equinox.event",
						"org.eclipse.e4.ui.di",
						"org.eclipse.e4.ui.workbench.swt"));
		return additional;
	}

	default void bootstrapServices(Bundle systemBundle, BundleContext context)
			throws MalformedURLException {
		// in particular, we need services normally provided by
		// org.eclipse.osgi.internal.framework.SystemBundleActivator::start
		context.registerService(
				BundleLocalization.class,
				new BundleLocalization() {
					@Override
					public ResourceBundle getLocalization(Bundle bundle, String locale) {
						String localization = bundle.getHeaders().get(Constants.BUNDLE_LOCALIZATION);
						if (localization == null) {
							throw new IllegalArgumentException("No localization for " + bundle);
						}
						URL url = bundle.getEntry(localization + ".properties");
						try (InputStream input = url.openStream()) {
							return new PropertyResourceBundle(input);
						} catch (IOException e) {
							throw new RuntimeException(e);
						}
					}
				},
				Dictionaries.empty());
		File userDir = new File(System.getProperty("user.dir") + "/build");
		context.registerService(EnvironmentInfo.class, new ShimEnvironmentInfo(), Dictionaries.empty());
		context.registerService(
				Location.class,
				new OsgiShim.ShimLocation(userDir.toURI().toURL()),
				Dictionaries.of(
						Location.SERVICE_PROPERTY_TYPE,
						Location.INSTANCE_AREA_TYPE,
						"url",
						userDir.toURI().toURL().toExternalForm()));
		context.registerService(
				SAXParserFactory.class, SAXParserFactory.newInstance(), Dictionaries.empty());

		var logReaderFactory = new ExtendedLogReaderServiceFactory(99, LogLevel.INFO);
		var logWriterFactory = new ExtendedLogServiceFactory(logReaderFactory, false);
		context.registerService(
				new String[] {
					ExtendedLogService.class.getName(),
					LogService.class.getName(),
					LoggerFactory.class.getName()
				},
				logWriterFactory.getService(systemBundle, null),
				Dictionaries.empty());
		context.registerService(
				ExtendedLogReaderService.class,
				logReaderFactory.getService(systemBundle, null),
				Dictionaries.empty());

		context.registerService(
				org.osgi.service.condition.Condition.class,
				new org.osgi.service.condition.Condition() {},
				Dictionaries.of("osgi.condition.id", "true"));

		context.registerService(
				FrameworkLog.class,
				new FrameworkLog() {
					@Override
					public void log(FrameworkEvent frameworkEvent) {}

					@Override
					public void log(FrameworkLogEntry logEntry) {}

					@Override
					public void setWriter(Writer newWriter, boolean append) {}

					@Override
					public void setFile(File newFile, boolean append) throws IOException {}

					@Override
					public File getFile() {
						return null;
					}

					@Override
					public void setConsoleLog(boolean consoleLog) {}

					@Override
					public void close() {}
				},
				Dictionaries.empty());

		context.registerService(
				DebugOptions.class,
				new DebugOptions() {
					File file;

					@Override
					public void setFile(File newFile) {
						this.file = newFile;
					}

					@Override
					public File getFile() {
						return file;
					}

					@Override
					public boolean isDebugEnabled() {
						return false;
					}

					@Override
					public String getOption(String option) {
						return null;
					}

					@Override
					public boolean getBooleanOption(String option, boolean defaultValue) {
						return defaultValue;
					}

					@Override
					public DebugTrace newDebugTrace(String bundleSymbolicName) {
						return null;
					}

					@Override
					public DebugTrace newDebugTrace(String bundleSymbolicName, Class<?> traceEntryClass) {
						return null;
					}

					@Override
					public String getOption(String option, String defaultValue) {
						throw new UnsupportedOperationException();
					}

					@Override
					public int getIntegerOption(String option, int defaultValue) {
						throw new UnsupportedOperationException();
					}

					@Override
					public Map<String, String> getOptions() {
						throw new UnsupportedOperationException();
					}

					@Override
					public void setOption(String option, String value) {
						throw new UnsupportedOperationException();
					}

					@Override
					public void setOptions(Map<String, String> options) {
						throw new UnsupportedOperationException();
					}

					@Override
					public void removeOption(String option) {
						throw new UnsupportedOperationException();
					}

					@Override
					public void setDebugEnabled(boolean value) {
						throw new UnsupportedOperationException();
					}
				},
				Dictionaries.empty());
	}

	default List<String> okayIfMissing() {
		return Arrays.asList(
				"javax.annotation",
				"org.eclipse.ant.core",
				"org.eclipse.jdt.annotation",
				"org.apache.batik.css");
	}
}
