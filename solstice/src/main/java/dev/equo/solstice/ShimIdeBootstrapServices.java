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
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.PropertyResourceBundle;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.SAXParserFactory;
import org.eclipse.core.internal.runtime.CommonMessages;
import org.eclipse.osgi.internal.location.BasicLocation;
import org.eclipse.osgi.internal.location.EquinoxLocations;
import org.eclipse.osgi.service.datalocation.Location;
import org.eclipse.osgi.service.debug.DebugOptions;
import org.eclipse.osgi.service.environment.EnvironmentInfo;
import org.eclipse.osgi.service.localization.BundleLocalization;
import org.eclipse.osgi.service.urlconversion.URLConverter;
import org.eclipse.osgi.util.NLS;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;
import org.osgi.framework.Constants;
import org.osgi.framework.ServiceFactory;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.condition.Condition;
import org.osgi.service.packageadmin.PackageAdmin;

/** Controls the initialization of the {@link BundleContextShim} runtime. */
@SuppressWarnings("deprecation")
public class ShimIdeBootstrapServices {
	public static void apply(Map<String, String> props, BundleContext contextUntyped) {
		BundleContextShim context = (BundleContextShim) contextUntyped;
		// in particular, we need services normally provided by
		// org.eclipse.osgi.internal.framework.SystemBundleActivator::start
		// Provided by org.eclipse.osgi
		// - [ ] org.eclipse.osgi.signedcontent.SignedContentFactory
		// NO-OP

		// - [ ] org.osgi.service.log.LogReaderService
		// - [ ] org.eclipse.equinox.log.ExtendedLogReaderService
		// - [ ] org.osgi.service.log.LoggerFactory
		// - [ ] org.osgi.service.log.LogService
		// - [ ] org.eclipse.equinox.log.ExtendedLogService
		// - [ ] org.osgi.service.log.admin.LoggerAdmin
		// - [ ] org.eclipse.osgi.framework.log.FrameworkLog
		try {
			context.container.getLogServices().start(context);
		} catch (BundleException e) {
			context.logger.error("error instantiating logging", e);
		}

		// - [ ] org.osgi.service.condition.Condition,osgi.condition.id=true
		context.registerService(
				Condition.class,
				Condition.INSTANCE,
				Dictionaries.of(Condition.CONDITION_ID, Condition.CONDITION_ID_TRUE));

		// - [ ] org.eclipse.osgi.service.datalocation.Location,type=osgi.user.area
		// - [ ] org.eclipse.osgi.service.datalocation.Location,type=osgi.instance.area
		// - [ ] org.eclipse.osgi.service.datalocation.Location,type=osgi.configuration.area
		// - [ ] org.eclipse.osgi.service.datalocation.Location,type=osgi.install.area
		// - [ ] org.eclipse.osgi.service.datalocation.Location,type=eclipse.home.location
		registerLocations(context, context.container.getLocations());

		// - [ ] org.eclipse.osgi.service.environment.EnvironmentInfo
		// - [ ] org.osgi.service.packageadmin.PackageAdmin
		context.registerService(EnvironmentInfo.class, context.container.getConfiguration(), null);
		Bundle systemBundle = context.getBundle(Constants.SYSTEM_BUNDLE_LOCATION);
		context.registerService(
				PackageAdmin.class, systemBundle.adapt(PackageAdmin.class), Dictionaries.empty());
		// - [ ] org.osgi.service.startlevel.StartLevel
		// - [ ] org.osgi.service.permissionadmin.PermissionAdmin
		// - [ ] org.osgi.service.condpermadmin.ConditionalPermissionAdmin
		// - [ ] org.osgi.service.resolver.Resolver
		// NO-OP

		// - [ ] org.eclipse.osgi.service.debug.DebugOptions
		// - [ ] org.eclipse.osgi.service.urlconversion.URLConverter
		context.registerService(
				DebugOptions.class, context.container.getConfiguration().getDebugOptions(), null);
		var instanceDir =
				Unchecked.get(() -> new File(new URI(context.getProperty(Location.INSTANCE_AREA_TYPE))));
		context.registerService(
				URLConverter.class,
				new JarUrlResolver(new File(instanceDir, "JarUrlResolver")),
				Dictionaries.of("protocol", "jar"));

		// - [ ] org.eclipse.osgi.service.localization.BundleLocalization
		context.registerService(
				BundleLocalization.class,
				(bundle, locale) -> {
					// TODO: we don't handle locale
					String localization = bundle.getHeaders().get(Constants.BUNDLE_LOCALIZATION);
					if (localization == null) {
						throw new MissingResourceException(
								NLS.bind(CommonMessages.activator_resourceBundleNotFound, locale),
								bundle.getSymbolicName(),
								""); //$NON-NLS-1$
					}
					URL url = bundle.getEntry(localization + ".properties");
					try (InputStream input = url.openStream()) {
						return new PropertyResourceBundle(input);
					} catch (IOException e) {
						throw Unchecked.wrap(e);
					}
				},
				Dictionaries.empty());

		// - [ ] XML parsing
		context.registerService(SAXParserFactory.class, new XMLFactory<>(true), null);
		context.registerService(DocumentBuilderFactory.class, new XMLFactory<>(false), null);
	}

	static Collection<String> locationKeys() {
		return List.of(
				EquinoxLocations.PROP_USER_AREA,
				EquinoxLocations.PROP_INSTANCE_AREA,
				EquinoxLocations.PROP_CONFIG_AREA,
				EquinoxLocations.PROP_INSTALL_AREA,
				EquinoxLocations.PROP_HOME_LOCATION_AREA);
	}

	private static void registerLocations(BundleContext bc, EquinoxLocations locs) {
		registerLocation(bc, locs.getUserLocation(), EquinoxLocations.PROP_USER_AREA);
		registerLocation(bc, locs.getInstanceLocation(), EquinoxLocations.PROP_INSTANCE_AREA);
		registerLocation(bc, locs.getConfigurationLocation(), EquinoxLocations.PROP_CONFIG_AREA);
		registerLocation(bc, locs.getInstallLocation(), EquinoxLocations.PROP_INSTALL_AREA);
		registerLocation(bc, locs.getEclipseHomeLocation(), EquinoxLocations.PROP_HOME_LOCATION_AREA);
	}

	private static void registerLocation(BundleContext bc, BasicLocation location, String type) {
		if (location != null) {
			location.register(bc);
		}
	}

	static class JarUrlResolver implements URLConverter {
		private final File dir;

		JarUrlResolver(File dir) {
			this.dir = dir;
			if (!dir.exists()) {
				dir.mkdirs();
			}
		}

		@Override
		public URL toFileURL(URL url) throws IOException {
			var file = new File(dir, filenameSafe(url.toExternalForm()));
			if (!file.exists()) {
				byte[] content;
				try (var read = url.openStream()) {
					content = read.readAllBytes();
				}
				Files.write(file.toPath(), content);
			}
			return file.toURI().toURL();
		}

		@Override
		public URL resolve(URL url) throws IOException {
			throw Unimplemented.onPurpose();
		}

		private static final String DOT_JAR_EX_SLASH = ".jar!/";

		private static String filenameSafe(String url) {
			var dotJarIdx = url.indexOf(DOT_JAR_EX_SLASH);
			if (dotJarIdx == -1) {
				throw Unimplemented.onPurpose(
						"This is only for " + DOT_JAR_EX_SLASH + " urls, this was " + url);
			}

			var jarNameStart = url.lastIndexOf('/', dotJarIdx);

			var beforeJar = url.substring(0, jarNameStart);
			var jar = url.substring(jarNameStart + 1, dotJarIdx);
			var inZip = url.substring(dotJarIdx + DOT_JAR_EX_SLASH.length());

			return NestedJars.filenameSafeHash(beforeJar) + "--" + safe(jar) + "--" + safe(inZip);
		}

		private static String safe(String in) {
			String allSafeCharacters = in.replaceAll("[^a-zA-Z0-9-+_.]", "-");
			String noDuplicateDash = allSafeCharacters.replaceAll("-+", "-");
			return noDuplicateDash;
		}
	}

	static class XMLFactory<T> implements ServiceFactory<T> {
		private final boolean isSax;

		public XMLFactory(boolean isSax) {
			this.isSax = isSax;
		}

		@SuppressWarnings("unchecked")
		@Override
		public T getService(Bundle bundle, ServiceRegistration<T> registration) {
			if (isSax) {
				return (T) SAXParserFactory.newInstance();
			} else {
				return (T) DocumentBuilderFactory.newInstance();
			}
		}

		@Override
		public void ungetService(Bundle bundle, ServiceRegistration<T> registration, T service) {}
	}
}
