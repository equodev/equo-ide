/*******************************************************************************
 * Copyright (c) 2022 EquoTech, Inc. and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     EquoTech, Inc. - initial API and implementation
 *******************************************************************************/
package dev.equo.solstice;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.PropertyResourceBundle;
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

public interface SolsticeConfiguration {
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

	default void bootstrapServices(Bundle systemBundle, BundleContext context) {
		// in particular, we need services normally provided by
		// org.eclipse.osgi.internal.framework.SystemBundleActivator::start
		context.registerService(
				BundleLocalization.class,
				(bundle, locale) -> {
					String localization = bundle.getHeaders().get(Constants.BUNDLE_LOCALIZATION);
					if (localization == null) {
						throw new IllegalArgumentException("No localization for " + bundle);
					}
					URL url = bundle.getEntry(localization + ".properties");
					try (InputStream input = url.openStream()) {
						return new PropertyResourceBundle(input);
					} catch (IOException e) {
						throw Unchecked.rethrow(e);
					}
				},
				Dictionaries.empty());
		context.registerService(EnvironmentInfo.class, new ShimEnvironmentInfo(), Dictionaries.empty());

		File userDir = new File(System.getProperty("user.dir") + "/build");
		ShimLocation.set(context, new File(userDir, "instance"), Location.INSTANCE_AREA_TYPE);
		ShimLocation.set(context, new File(userDir, "install"), Location.INSTALL_AREA_TYPE);
		ShimLocation.set(context, new File(userDir, "config"), Location.CONFIGURATION_AREA_TYPE);
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
					public void setFile(File newFile, boolean append) {}

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
						throw Unimplemented.onPurpose();
					}

					@Override
					public int getIntegerOption(String option, int defaultValue) {
						throw Unimplemented.onPurpose();
					}

					@Override
					public Map<String, String> getOptions() {
						throw Unimplemented.onPurpose();
					}

					@Override
					public void setOption(String option, String value) {
						throw Unimplemented.onPurpose();
					}

					@Override
					public void setOptions(Map<String, String> options) {
						throw Unimplemented.onPurpose();
					}

					@Override
					public void removeOption(String option) {
						throw Unimplemented.onPurpose();
					}

					@Override
					public void setDebugEnabled(boolean value) {
						throw Unimplemented.onPurpose();
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
