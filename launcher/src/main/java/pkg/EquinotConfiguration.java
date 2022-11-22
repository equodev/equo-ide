package pkg;

import java.io.File;
import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.List;
import javax.xml.parsers.SAXParserFactory;
import org.eclipse.equinox.log.ExtendedLogReaderService;
import org.eclipse.equinox.log.ExtendedLogService;
import org.eclipse.osgi.internal.log.ExtendedLogReaderServiceFactory;
import org.eclipse.osgi.internal.log.ExtendedLogServiceFactory;
import org.eclipse.osgi.service.datalocation.Location;
import org.eclipse.osgi.service.environment.EnvironmentInfo;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
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
	}

	default List<String> okayIfMissing() {
		return Arrays.asList(
				"javax.annotation",
				"org.eclipse.ant.core",
				"org.eclipse.jdt.annotation",
				"org.apache.batik.css");
	}
}
