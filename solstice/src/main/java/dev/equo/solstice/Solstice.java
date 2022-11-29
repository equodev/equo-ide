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

import com.diffplug.common.swt.os.SwtPlatform;
import dev.equo.solstice.platform.Handler;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.jar.Attributes;
import java.util.jar.Manifest;
import javax.annotation.Nullable;
import org.eclipse.core.internal.runtime.InternalPlatform;
import org.eclipse.osgi.internal.framework.FilterImpl;
import org.eclipse.osgi.service.datalocation.Location;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.BundleException;
import org.osgi.framework.BundleListener;
import org.osgi.framework.Constants;
import org.osgi.framework.FrameworkListener;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.Version;
import org.osgi.framework.namespace.IdentityNamespace;
import org.osgi.framework.startlevel.BundleStartLevel;
import org.osgi.framework.wiring.BundleCapability;
import org.osgi.framework.wiring.BundleRevision;
import org.osgi.framework.wiring.BundleWiring;
import org.osgi.framework.wiring.FrameworkWiring;
import org.osgi.resource.Namespace;
import org.osgi.resource.Requirement;
import org.osgi.service.packageadmin.PackageAdmin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Solstice extends ServiceRegistry {
	private static Solstice instance;

	public static Solstice initialize(SolsticeConfiguration config) {
		if (instance != null) {
			throw new IllegalStateException("Equinot has already been initialized");
		}
		instance = new Solstice(config);
		return instance;
	}

	private final Logger logger = LoggerFactory.getLogger(Solstice.class);
	private final SolsticeConfiguration cfg;

	private Solstice(SolsticeConfiguration cfg) {
		Handler.install(this);

		this.cfg = cfg;
		try {
			SolsticeFrameworkUtilHelper.initialize(this);
			cfg.bootstrapServices(systemBundle, this);
			logger.info("Bootstrap services installed");

			discoverAndSortBundles();
			logger.info("Bundles found and sorted.");
			for (var b : bundles) {
				logger.info("  {}", b);
			}
			for (ShimBundle bundle : bundles) {
				try {
					bundle.activate();
				} catch (Exception e) {
					logger.warn("Error while activating " + bundle, e);
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	protected Bundle systemBundle() {
		return systemBundle;
	}

	private List<ShimBundle> bundles = new ArrayList<ShimBundle>();

	private void discoverAndSortBundles() throws IOException {
		Enumeration<URL> resources = getClass().getClassLoader().getResources("META-INF/MANIFEST.MF");
		while (resources.hasMoreElements()) {
			bundles.add(new ShimBundle(resources.nextElement()));
		}
		List<String> startOrder = cfg.startOrder();
		Collections.sort(
				bundles,
				(o1, o2) -> {
					int start1 = startOrder.indexOf(o1.symbolicName);
					int start2 = startOrder.indexOf(o2.symbolicName);
					if (start1 != -1 || start2 != -1) {
						if (start1 == -1) {
							start1 = Integer.MAX_VALUE;
						}
						if (start2 == -1) {
							start2 = Integer.MAX_VALUE;
						}
						return start1 - start2;
					}
					if ((o1.symbolicName != null) == (o2.symbolicName != null)) {
						// sort based on name for the same "types"
						return o1.toString().compareTo(o2.toString());
					} else {
						// otherwise put the symbolic names first
						return o1.symbolicName != null ? -1 : 1;
					}
				});
	}

	public ShimBundle bundleByName(String name) {
		for (ShimBundle bundle : bundles) {
			if (name.equals(bundle.getSymbolicName())) {
				return bundle;
			}
		}
		return null;
	}

	public Bundle bundleForURL(URL source) {
		String sourceString = "jar:" + source.toExternalForm() + "!";
		for (ShimBundle bundle : bundles) {
			if (sourceString.equals(bundle.jarUrl)) {
				return bundle;
			}
		}
		return null;
	}

	static class ShimLocation extends Shims.LocationUnsupported {
		static void set(BundleContext context, File dir, String type) throws MalformedURLException {
			context.registerService(
					Location.class,
					new Solstice.ShimLocation(dir.toURI().toURL()),
					Dictionaries.of(
							SERVICE_PROPERTY_TYPE, type, "url", dir.toURI().toURL().toExternalForm()));
		}

		final URL url;

		ShimLocation(URL url) {
			this.url = url;
		}

		@Override
		public URL getURL() {
			return url;
		}

		@Override
		public Location getParentLocation() {
			return null;
		}

		@Override
		public boolean isReadOnly() {
			return false;
		}

		@Override
		public boolean isSet() {
			return true;
		}

		@Override
		public URL getDataArea(String path) throws IOException {
			return new URL(url.toExternalForm() + path);
		}
	}

	final Bundle systemBundle =
			new Shims.BundleUnsupported() {
				@Override
				public long getBundleId() {
					return 0;
				}

				@Override
				public int getState() {
					// this signals InternalPlatform.isRunning() to be true
					return ACTIVE;
				}

				@Override
				public String getSymbolicName() {
					return "osgi-shim-system-bundle";
				}

				@Override
				public Version getVersion() {
					return null;
				}

				@Override
				public String getLocation() {
					return null;
				}

				@Override
				public BundleContext getBundleContext() {
					return Solstice.this;
				}

				@Override
				public <A> A adapt(Class<A> type) {
					if (type.equals(PackageAdmin.class)) {
						return (A) packageAdmin;
					} else if (type.equals(FrameworkWiring.class)) {
						return (A) frameworkWiring;
					} else {
						throw new UnsupportedOperationException(type.getName());
					}
				}

				@Override
				public String toString() {
					return "SystemBundle";
				}
			};

	final PackageAdmin packageAdmin = new Shims.PackageAdminUnsupported() {};

	final FrameworkWiring frameworkWiring =
			new Shims.FrameworkWiringUnsupported() {
				@Override
				public Collection<BundleCapability> findProviders(Requirement requirement) {
					String filterSpec =
							requirement.getDirectives().get(Namespace.REQUIREMENT_FILTER_DIRECTIVE);
					try {
						var requirementFilter = FilterImpl.newInstance(filterSpec).getStandardOSGiAttributes();
						var requiredBundle = requirementFilter.get(IdentityNamespace.IDENTITY_NAMESPACE);
						var bundle = bundleByName(requiredBundle);
						return Collections.singleton(new ShimBundleCapability(bundle));
					} catch (InvalidSyntaxException e) {
						throw new IllegalArgumentException("Filter specifiation invalid:\n" + filterSpec, e);
					}
				}
			};

	private static class ShimBundleCapability extends Shims.BundleCapabilityUnsupported {
		private final ShimBundleRevision revision;

		public ShimBundleCapability(Bundle bundle) {
			this.revision = new ShimBundleRevision(bundle);
		}

		@Override
		public BundleRevision getRevision() {
			return revision;
		}
	}

	private static class ShimBundleRevision extends Shims.BundleRevisionUnsupported {
		private final Bundle bundle;

		public ShimBundleRevision(Bundle bundle) {
			this.bundle = Objects.requireNonNull(bundle);
		}

		@Override
		public Bundle getBundle() {
			return bundle;
		}
	}

	@Override
	public org.osgi.framework.Bundle getBundle() {
		return systemBundle;
	}

	@Override
	public Bundle installBundle(String location, InputStream input) throws BundleException {
		throw new UnsupportedOperationException();
	}

	@Override
	public Bundle installBundle(String location) throws BundleException {
		throw new UnsupportedOperationException();
	}

	@Override
	public org.osgi.framework.Bundle getBundle(String location) {
		if (Constants.SYSTEM_BUNDLE_LOCATION.equals(location)) {
			return systemBundle;
		} else {
			throw new UnsupportedOperationException();
		}
	}

	@Override
	public Bundle[] getBundles() {
		return bundles.stream().filter(bundle -> bundle.symbolicName != null).toArray(Bundle[]::new);
	}

	@Override
	public String getProperty(String key) {
		if (InternalPlatform.PROP_OS.equals(key)) {
			return SwtPlatform.getRunning().getOs();
		} else if (InternalPlatform.PROP_WS.equals(key)) {
			return SwtPlatform.getRunning().getWs();
		} else {
			return null;
		}
	}

	@Override
	public File getDataFile(String filename) {
		return null;
	}

	private CopyOnWriteArrayList<BundleListener> bundleListeners = new CopyOnWriteArrayList<>();

	@Override
	public synchronized void addBundleListener(BundleListener listener) {
		bundleListeners.add(listener);
	}

	@Override
	public synchronized void removeBundleListener(BundleListener listener) {
		bundleListeners.remove(listener);
	}

	@Override
	public void addFrameworkListener(FrameworkListener listener) {
		// TODO: not sure if we can survive without FrameworkEvents
	}

	@Override
	public void removeFrameworkListener(FrameworkListener listener) {
		// TODO: not sure if we can survive without FrameworkEvents
	}

	private synchronized void notifyBundleListeners(int type, ShimBundle bundle) {
		if (bundle.symbolicName == null) {
			return;
		}
		var event = new BundleEvent(type, bundle);
		for (BundleListener listener : bundleListeners) {
			listener.bundleChanged(event);
		}
	}

	@Override
	public Bundle getBundle(long id) {
		return id == -1 ? systemBundle : bundles.get((int) id);
	}

	static final Attributes.Name SYMBOLIC_NAME = new Attributes.Name(Constants.BUNDLE_SYMBOLICNAME);
	static final Attributes.Name ACTIVATOR = new Attributes.Name(Constants.BUNDLE_ACTIVATOR);
	static final Attributes.Name REQUIRE_BUNDLE = new Attributes.Name(Constants.REQUIRE_BUNDLE);
	static final List<String> IGNORED_HEADERS =
			Arrays.asList(Constants.IMPORT_PACKAGE, Constants.EXPORT_PACKAGE);
	static final String MANIFEST_PATH = "/META-INF/MANIFEST.MF";

	public class ShimBundle extends BundleContextDelegate implements Shims.BundleUnsupported {
		final String jarUrl;
		final @Nullable String activator;
		final @Nullable String symbolicName;
		final List<String> requiredBundles;
		final Hashtable<String, String> headers;

		ShimBundle(URL manifestURL) throws IOException {
			super(Solstice.this);
			var externalForm = manifestURL.toExternalForm();
			if (!externalForm.endsWith(MANIFEST_PATH)) {
				throw new RuntimeException(
						"Expected manifest to end with " + MANIFEST_PATH + " but was " + externalForm);
			}
			jarUrl = externalForm.substring(0, externalForm.length() - MANIFEST_PATH.length());
			Manifest manifest = new Manifest(manifestURL.openStream());
			activator = manifest.getMainAttributes().getValue(ACTIVATOR);
			String symbolicNameRaw = manifest.getMainAttributes().getValue(SYMBOLIC_NAME);
			if (symbolicNameRaw == null) {
				symbolicName = null;
			} else {
				// strip off singleton stuff
				int firstDirective = symbolicNameRaw.indexOf(';');
				if (firstDirective == -1) {
					symbolicName = symbolicNameRaw;
				} else {
					symbolicName = symbolicNameRaw.substring(0, firstDirective);
				}
			}
			if (!jarUrl.endsWith("!")) {
				if (jarUrl.endsWith("build/resources/main")) {
					// we're inside a Gradle build/test, no worries
				} else {
					throw new IllegalArgumentException(
							"Must end with !  SEE getEntry if this changes  " + jarUrl);
				}
			}
			requiredBundles = requiredBundles(manifest);
			if (symbolicName != null) {
				var additional = cfg.additionalDeps().get(symbolicName);
				if (additional != null) {
					requiredBundles.addAll(additional);
				}
			}
			headers = new Hashtable<>();
			manifest
					.getMainAttributes()
					.forEach(
							(key, value) -> {
								String keyStr = key.toString();
								if (ShimDS.SERVICE_COMPONENT.equals(keyStr)) {
									String valueStr = value.toString().trim();
									headers.put(ShimDS.SERVICE_COMPONENT, ShimDS.cleanHeader(jarUrl, valueStr));
								} else if (!IGNORED_HEADERS.contains(keyStr)) {
									headers.put(keyStr, value.toString());
								}
							});
			var classpath = headers.get(Constants.BUNDLE_CLASSPATH);
		}

		private List<String> requiredBundles(Manifest manifest) {
			String requireBundle = manifest.getMainAttributes().getValue(REQUIRE_BUNDLE);
			if (requireBundle == null) {
				return Collections.emptyList();
			}

			String[] bundlesAndVersions = requireBundle.split(",");
			List<String> required = new ArrayList<>(bundlesAndVersions.length);
			for (String s : bundlesAndVersions) {
				int attrDelim = s.indexOf(';');
				if (attrDelim == -1) {
					attrDelim = s.length();
				}
				String bundle = s.substring(0, attrDelim);
				if (bundle.indexOf('"') == -1) {
					required.add(bundle);
				}
			}
			return required;
		}

		@Override
		public String toString() {
			if (symbolicName != null) {
				return symbolicName;
			} else {
				return jarUrl;
			}
		}

		@Override
		public Class<?> loadClass(String name) throws ClassNotFoundException {
			return Class.forName(name);
		}

		//////////////////////////
		// BundleContext overrides
		//////////////////////////
		@Override
		public org.osgi.framework.Bundle getBundle() {
			return this;
		}

		@Override
		public long getBundleId() {
			if (this == systemBundle) {
				return systemBundle.getBundleId();
			} else {
				int idx = bundles.indexOf(this);
				if (idx == -1) {
					throw new IllegalStateException("This bundle wasn't known at startup.");
				}
				return idx + 1;
			}
		}

		@Override
		public Version getVersion() {
			return Version.emptyVersion;
		}

		@Override
		public void start(int options) throws BundleException {
			try {
				activate();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}

		///////////////////
		// Bundle overrides
		///////////////////
		private int state = INSTALLED;

		private boolean activating = false;

		private void activate() throws Exception {
			if (activating) {
				return;
			}
			activating = true;

			logger.info("Request activate {}", this);
			if ("org.eclipse.osgi".equals(symbolicName)) {
				state = ACTIVE;
				// skip org.eclipse.osgi on purpose
				logger.info("  skipping because of shim implementation");
				return;
			}

			for (var required : requiredBundles) {
				logger.info("{} requires {}", this, required);
				ShimBundle bundle = bundleByName(required);
				if (bundle != null) {
					try {
						bundle.activate();
					} catch (Exception e) {
						logger.error(
								"{} failed to activate, was required by {}, caused by {}", required, this, e);
					}
				} else {
					if (!cfg.okayIfMissing().contains(required)) {
						logger.error("{} is missing, was required by {}", required, this);
						throw new IllegalArgumentException(required + " IS MISSING, needed by " + this);
					} else {
						logger.info("  missing but okayIfMissing so no problem");
					}
				}
			}
			logger.info("/START ACTIVATE {}", this);
			state = RESOLVED;
			notifyBundleListeners(BundleEvent.RESOLVED, this);

			state = STARTING;
			notifyBundleListeners(BundleEvent.STARTING, this);
			if (activator != null) {
				logger.info("{} Bundle-Activator {}", this, activator);
				var c = (Constructor<BundleActivator>) Class.forName(activator).getConstructor();
				if (c == null) {
					throw new IllegalArgumentException("No activator for " + jarUrl + " " + activator);
				}
				var bundleActivator = c.newInstance();
				bundleActivator.start(this);
			}
			state = ACTIVE;
			notifyBundleListeners(BundleEvent.STARTED, this);
			logger.info("\\FINISH ACTIVATE {}", this);
		}

		@Override
		public int getState() {
			return state;
		}

		@Override
		public Dictionary<String, String> getHeaders(String locale) {
			return headers;
		}

		@Override
		public BundleContext getBundleContext() {
			return this;
		}

		@Override
		public String getSymbolicName() {
			return symbolicName;
		}

		@Override
		public URL getEntry(String path) {
			try {
				if (path.startsWith("/")) {
					return new URL(jarUrl + path);
				} else {
					return new URL(jarUrl + "/" + path);
				}
			} catch (MalformedURLException e) {
				throw new RuntimeException(e);
			}
		}

		@Override
		public URL getResource(String name) {
			// TODO: according to spec, we should search in this bundle first,
			// and then after that from the classloader, but we are only using
			// this bundle
			return getEntry(name);
		}

		@Override
		public String getLocation() {
			return jarUrl;
		}

		// implemented for OSGi DS
		@Override
		public <A> A adapt(Class<A> type) {
			if (BundleWiring.class.equals(type)) {
				return (A) new ShimDS.BundleWiringImpl();
			} else if (BundleStartLevel.class.equals(type)) {
				return (A)
						new Shims.BundleStartLevel() {
							@Override
							public boolean isActivationPolicyUsed() {
								return true;
							}
						};
			} else {
				return null;
			}
		}

		// implemented for OSGi DS
		@Override
		public Enumeration<URL> findEntries(String path, String filePattern, boolean recurse) {
			if (recurse) {
				throw new UnsupportedOperationException();
			}
			return Dictionaries.enumeration(getEntry(path + "/" + filePattern));
		}
	}
}
