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
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.PropertyResourceBundle;
import java.util.TreeMap;
import javax.xml.parsers.SAXParserFactory;
import org.eclipse.osgi.framework.log.FrameworkLog;
import org.eclipse.osgi.service.datalocation.Location;
import org.eclipse.osgi.service.debug.DebugOptions;
import org.eclipse.osgi.service.debug.DebugTrace;
import org.eclipse.osgi.service.environment.EnvironmentInfo;
import org.eclipse.osgi.service.localization.BundleLocalization;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.service.log.LogLevel;
import org.osgi.service.packageadmin.PackageAdmin;

/** Controls the initialization of the {@link Solstice} runtime. */
public class SolsticeInit {
	private File installDir;
	private final MissingPolicy packages = new MissingPolicy.Permissive("packages");
	private final MissingPolicy bundles = new MissingPolicy.Permissive("bundles");

	public SolsticeInit(File installDir) {
		this.installDir = installDir;
	}

	public SolsticeInit() {
		this(defaultDir());
	}

	private static File defaultDir() {
		var userDir = System.getProperty("user.dir");
		if (userDir.endsWith("equo-ide")) {
			return new File(userDir + "/solstice/build/testSetup");
		} else {
			return new File(userDir + "/build/testSetup");
		}
	}

	public Map<String, List<String>> additionalDeps() {
		var additional = new TreeMap<String, List<String>>();
		additional.put(
				"org.eclipse.e4.ui.services",
				Arrays.asList(
						"org.eclipse.equinox.event",
						"org.eclipse.e4.ui.di",
						"org.eclipse.e4.ui.workbench.swt"));
		return additional;
	}

	public File nestedJarFolder() {
		return new File(installDir, NestedJars.DIR);
	}

	public List<String> requiresWorkbench() {
		return Arrays.asList("org.eclipse.debug.ui", "org.eclipse.help.ui");
	}

	public MissingPolicy bundlePolicy() {
		return bundles;
	}

	public MissingPolicy packagePolicy() {
		return packages;
	}

	public void bootstrapServices(Bundle systemBundle, BundleContext context) {
		// in particular, we need services normally provided by
		// org.eclipse.osgi.internal.framework.SystemBundleActivator::start
		context.registerService(
				BundleLocalization.class,
				(bundle, locale) -> {
					// TODO: we don't handle locale
					String localization = bundle.getHeaders().get(Constants.BUNDLE_LOCALIZATION);
					if (localization == null) {
						throw new IllegalArgumentException("No localization for " + bundle);
					}
					URL url = bundle.getEntry(localization + ".properties");
					try (InputStream input = url.openStream()) {
						return new PropertyResourceBundle(input);
					} catch (IOException e) {
						throw Unchecked.wrap(e);
					}
				},
				Dictionaries.empty());
		context.registerService(EnvironmentInfo.class, new ShimEnvironmentInfo(), Dictionaries.empty());
		context.registerService(
				PackageAdmin.class, systemBundle.adapt(PackageAdmin.class), Dictionaries.empty());

		ShimLocation.set(context, new File(installDir, "instance"), Location.INSTANCE_AREA_TYPE);
		ShimLocation.set(context, new File(installDir, "install"), Location.INSTALL_AREA_TYPE);
		ShimLocation.set(context, new File(installDir, "config"), Location.CONFIGURATION_AREA_TYPE);
		context.registerService(
				SAXParserFactory.class, SAXParserFactory.newInstance(), Dictionaries.empty());

		var serviceManager = new ShimLogServiceManager(100, LogLevel.INFO, false);
		serviceManager.start(context);

		context.registerService(
				org.osgi.service.condition.Condition.class,
				new org.osgi.service.condition.Condition() {},
				Dictionaries.of("osgi.condition.id", "true"));
		context.registerService(FrameworkLog.class, new ShimFrameworkLog(), Dictionaries.empty());

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
}
