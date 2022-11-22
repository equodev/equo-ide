package pkg;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
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
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkEvent;
import org.osgi.service.log.LogLevel;

public interface EquinotConfiguration {
	default LogLevel logLevel() {
		return LogLevel.INFO;
	}

	default List<String> startOrder() {
		return Arrays.asList("org.eclipse.equinox.registry");
	}

	default void bootstrapServices(Bundle systemBundle, BundleContext context)
			throws MalformedURLException {
		File userDir = new File(System.getProperty("user.dir") + "/build");
		context.registerService(EnvironmentInfo.class, new ShimEnvironmentInfo(), Dictionaries.empty());
		context.registerService(
				Location.class,
				new OsgiShim.ShimLocation(userDir.toURI().toURL()),
				Dictionaries.of(Location.SERVICE_PROPERTY_TYPE, Location.INSTANCE_AREA_TYPE));
		context.registerService(
				SAXParserFactory.class, SAXParserFactory.newInstance(), Dictionaries.empty());

		var logReaderFactory = new ExtendedLogReaderServiceFactory(99, LogLevel.INFO);
		var logWriterFactory = new ExtendedLogServiceFactory(logReaderFactory, false);
		context.registerService(
				ExtendedLogService.class,
				logWriterFactory.getService(systemBundle, null),
				Dictionaries.empty());
		context.registerService(
				ExtendedLogReaderService.class,
				logReaderFactory.getService(systemBundle, null),
				Dictionaries.empty());

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
