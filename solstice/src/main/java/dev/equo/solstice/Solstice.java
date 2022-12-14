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
import java.util.Collection;
import java.util.Collections;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Function;
import java.util.jar.Attributes;
import java.util.jar.Manifest;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import javax.annotation.Nullable;
import org.eclipse.core.internal.runtime.InternalPlatform;
import org.eclipse.osgi.internal.framework.FilterImpl;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.BundleListener;
import org.osgi.framework.Constants;
import org.osgi.framework.FrameworkListener;
import org.osgi.framework.Version;
import org.osgi.framework.namespace.IdentityNamespace;
import org.osgi.framework.startlevel.BundleStartLevel;
import org.osgi.framework.startlevel.FrameworkStartLevel;
import org.osgi.framework.wiring.BundleCapability;
import org.osgi.framework.wiring.BundleRevision;
import org.osgi.framework.wiring.BundleWiring;
import org.osgi.framework.wiring.FrameworkWiring;
import org.osgi.resource.Namespace;
import org.osgi.resource.Requirement;
import org.osgi.service.packageadmin.PackageAdmin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A single-classloader implementation of OSGi which eagerly loads all the OSGi plugins it can find
 * on the classpath.
 */
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

		SolsticeFrameworkUtilHelper.initialize(this);
		cfg.bootstrapServices(systemBundle, this);
		logger.info("Bootstrap services installed");

		discoverAndSortBundles();
		logger.info("Confirming that nested jars have been extracted");
		NestedJars.onClassPath().confirmAllNestedJarsArePresentOnClasspath(cfg.nestedJarFolder());
		logger.info("All bundles found and sorted.");
		for (var b : bundles) {
			logger.info("  {}", b);
		}
		for (var bundle : bundles) {
			try {
				bundle.tryActivate();
			} catch (ActivatorException e) {
				if (cfg.okayIfActivatorFails().contains(bundle.getSymbolicName())) {
					logger.warn(e.bundle + " activator exception but okayIfActivatorFails", e);
				} else {
					throw Unchecked.wrap(e);
				}
			}
		}
	}

	public void activateWorkbenchBundles() {
		if (workbenchIsActive) {
			throw new IllegalStateException("Can only be called once!");
		}
		workbenchIsActive = true;
		logger.info("Workbench has been instantiated, activating bundles which require the workbench");
		for (var bundle : workbenchQueue) {
			bundle.activating = false;
		}
		for (var bundle : workbenchQueue) {
			try {
				bundle.tryActivate();
			} catch (ActivatorException e) {
				if (cfg.okayIfActivatorFails().contains(bundle.getSymbolicName())) {
					logger.warn(e.bundle + " activator exception but okayIfActivatorFails", e);
				} else {
					throw Unchecked.wrap(e);
				}
			}
		}
	}

	private void addToWorkbenchQueue(ShimBundle bundle) {
		if (workbenchIsActive) {
			throw new IllegalStateException(
					"This should only happen before the workbench has activated.");
		}
		workbenchQueue.add(bundle);
	}

	private boolean workbenchIsActive = false;
	private Set<ShimBundle> workbenchQueue = new LinkedHashSet<>();

	@Override
	protected Bundle systemBundle() {
		return systemBundle;
	}

	private final List<ShimBundle> bundles = new ArrayList<>();
	private final TreeSet<String> pkgs = new TreeSet<>();

	private void discoverAndSortBundles() {
		Enumeration<URL> manifests =
				Unchecked.get(() -> getClass().getClassLoader().getResources("META-INF/MANIFEST.MF"));
		while (manifests.hasMoreElements()) {
			bundles.add(new ShimBundle(manifests.nextElement()));
		}
		List<String> startOrder = cfg.startOrder();
		bundles.sort(
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
		for (var bundle : bundles) {
			var host = bundle.fragmentHost();
			if (host != null) {
				var hostBundle = bundleByName(host);
				if (hostBundle == null) {
					throw new IllegalArgumentException("Fragment " + bundle + " needs missing " + host);
				}
				hostBundle.addFragment(bundle);
			}
		}
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

	private ShimBundle bundleForPkg(String targetPkg) {
		for (var bundle : bundles) {
			if (bundle.pkgExports.contains(targetPkg)) {
				return bundle;
			}
		}
		return null;
	}

	final Bundle systemBundle =
			new Unimplemented.Bundle() {
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
				public URL getResource(String name) {
					return bundles.get(0).getResource(name);
				}

				@Override
				public Enumeration<URL> getResources(String name) throws IOException {
					return bundles.get(0).getResources(name);
				}

				@Override
				public Enumeration<String> getEntryPaths(String path) {
					return Dictionaries.enumeration();
				}

				@Override
				public Enumeration<URL> findEntries(String path, String filePattern, boolean recurse) {
					return Dictionaries.enumeration();
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
					} else if (type.equals(FrameworkStartLevel.class)) {
						// TODO: figure out who needs this and why
						return (A) new Unimplemented.FrameworkStartLevel() {};
					} else {
						throw new UnsupportedOperationException(type.getName());
					}
				}

				@Override
				public String toString() {
					return "SystemBundle";
				}
			};

	final PackageAdmin packageAdmin =
			new Unimplemented.PackageAdmin() {
				@Override
				public int getBundleType(org.osgi.framework.Bundle bundle) {
					if (bundle instanceof ShimBundle && ((ShimBundle) bundle).fragmentHost() != null) {
						return BUNDLE_TYPE_FRAGMENT;
					}
					return 0;
				}

				@Override
				public Bundle[] getBundles(String symbolicName, String versionRange) {
					var bundle = bundleByName(symbolicName);
					return (bundle == null) ? new Bundle[0] : new Bundle[] {bundle};
				}

				@Override
				public Bundle[] getHosts(Bundle bundle) {
					if (bundle instanceof ShimBundle) {
						var fragmentHost = ((ShimBundle) bundle).fragmentHost();
						if (fragmentHost != null) {
							return getBundles(fragmentHost, null);
						}
					}
					return new Bundle[0];
				}

				@Override
				public Bundle[] getFragments(Bundle bundle) {
					List<Bundle> fragments = new ArrayList<>();
					for (var candidate : bundles) {
						if (Objects.equals(candidate.fragmentHost(), bundle.getSymbolicName())) {
							fragments.add(candidate);
						}
					}
					return fragments.toArray(new Bundle[0]);
				}
			};

	final FrameworkWiring frameworkWiring =
			new Unimplemented.FrameworkWiring() {
				@Override
				public Collection<BundleCapability> findProviders(Requirement requirement) {
					String filterSpec =
							requirement.getDirectives().get(Namespace.REQUIREMENT_FILTER_DIRECTIVE);
					try {
						var requirementFilter = FilterImpl.newInstance(filterSpec).getStandardOSGiAttributes();
						var requiredBundle = requirementFilter.get(IdentityNamespace.IDENTITY_NAMESPACE);
						var bundle = bundleByName(requiredBundle);
						return Collections.singleton(new ShimBundleCapability(bundle));
					} catch (Exception e) {
						throw Unimplemented.onPurpose();
					}
				}
			};

	@Override
	public org.osgi.framework.Bundle getBundle() {
		return systemBundle;
	}

	@Override
	public Bundle installBundle(String location, InputStream input) {
		throw Unimplemented.onPurpose();
	}

	@Override
	public Bundle installBundle(String location) {
		throw Unimplemented.onPurpose();
	}

	@Override
	public org.osgi.framework.Bundle getBundle(String location) {
		if (Constants.SYSTEM_BUNDLE_LOCATION.equals(location)) {
			return systemBundle;
		} else {
			throw Unimplemented.onPurpose();
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

	private final CopyOnWriteArrayList<BundleListener> bundleListeners = new CopyOnWriteArrayList<>();

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
	static final Attributes.Name IMPORT_PACKAGE = new Attributes.Name(Constants.IMPORT_PACKAGE);
	static final Attributes.Name EXPORT_PACKAGE = new Attributes.Name(Constants.EXPORT_PACKAGE);

	static final String MANIFEST_PATH = "/META-INF/MANIFEST.MF";

	public class ShimBundle extends BundleContextDelegate implements Unimplemented.Bundle {
		final String jarUrl;
		final @Nullable String activator;
		final @Nullable String symbolicName;
		final List<String> requiredBundles;
		final Hashtable<String, String> headers;
		final List<ShimBundle> fragments = new ArrayList<>();
		final List<String> pkgImports, pkgExports;

		ShimBundle(URL manifestURL) {
			super(Solstice.this);
			var externalForm = manifestURL.toExternalForm();
			if (!externalForm.endsWith(MANIFEST_PATH)) {
				throw new IllegalArgumentException(
						"Expected manifest to end with " + MANIFEST_PATH + " but was " + externalForm);
			}
			jarUrl = externalForm.substring(0, externalForm.length() - MANIFEST_PATH.length());
			Manifest manifest;
			try (InputStream stream = manifestURL.openStream()) {
				manifest = new Manifest(stream);
			} catch (IOException e) {
				throw Unchecked.wrap(e);
			}
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
			pkgImports = parsePackages(manifest.getMainAttributes().get(IMPORT_PACKAGE));
			pkgExports = parsePackages(manifest.getMainAttributes().get(EXPORT_PACKAGE));
			// if we export a package, we don't actually have to import it, that's just for letting
			// multiple bundles define the same classes, which is a dubious feature to support
			// https://access.redhat.com/documentation/en-us/red_hat_jboss_fuse/6.3/html/managing_osgi_dependencies/importexport
			pkgImports.removeAll(pkgExports);

			headers = new Hashtable<>();
			manifest
					.getMainAttributes()
					.forEach(
							(key, value) -> {
								String keyStr = key.toString();
								if (!Constants.IMPORT_PACKAGE.equals(keyStr)
										&& !Constants.EXPORT_PACKAGE.equals(keyStr)) {
									headers.put(keyStr, value.toString());
								}
							});
		}

		private void addFragment(ShimBundle bundle) {
			fragments.add(bundle);
		}

		String fragmentHost() {
			var host = headers.get(Constants.FRAGMENT_HOST);
			if (host == null) {
				return null;
			}
			var idx = host.indexOf(';');
			return idx == -1 ? host : host.substring(0, idx);
		}

		private List<String> parsePackages(Object attribute) {
			if (attribute == null) {
				return Collections.emptyList();
			}
			return parseManifestHeaderSimple(attribute.toString());
		}

		private List<String> requiredBundles(Manifest manifest) {
			String requireBundle = manifest.getMainAttributes().getValue(REQUIRE_BUNDLE);
			if (requireBundle == null) {
				return Collections.emptyList();
			}
			return parseManifestHeaderSimple(requireBundle);
		}

		/** Parse out a manifest header, and ignore the versions */
		private List<String> parseManifestHeaderSimple(String in) {
			String[] bundlesAndVersions = in.split(",");
			List<String> required = new ArrayList<>(bundlesAndVersions.length);
			for (String s : bundlesAndVersions) {
				int attrDelim = s.indexOf(';');
				if (attrDelim == -1) {
					attrDelim = s.length();
				}
				String bundle = s.substring(0, attrDelim);
				if (bundle.indexOf('"') == -1) {
					required.add(bundle.trim());
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
		public void start(int options) {
			try {
				tryActivate();
			} catch (ActivatorException e) {
				throw Unchecked.wrap(e);
			}
		}

		///////////////////
		// Bundle overrides
		///////////////////
		private int state = INSTALLED;

		private boolean activating = false;

		/** Returns false if the bundle cannot activate yet because of "requiresWorkbench" */
		private boolean tryActivate() throws ActivatorException {
			if (activating) {
				return true;
			}
			if (!workbenchIsActive) {
				if (cfg.requiresWorkbench().contains(symbolicName)) {
					logger.info("Activating {} will wait for workbench", this);
					addToWorkbenchQueue(this);
					return false;
				}
			}
			activating = true;

			logger.info("Request activate {}", this);
			pkgs.addAll(pkgExports);
			if ("org.eclipse.osgi".equals(symbolicName)) {
				state = ACTIVE;
				// skip org.eclipse.osgi on purpose
				logger.info("  skipping because of shim implementation");
				return true;
			}
			String pkg = missingPkg();
			while (pkg != null) {
				var bundle = bundleForPkg(pkg);
				logger.info("{} import {} package {}", this, bundle, pkg);
				if (bundle == null) {
					if (cfg.okayIfMissingPackage(pkg)) {
						pkgs.add(pkg);
						logger.info("  missing but okayIfMissingPackage so no problem");
					} else {
						throw new IllegalArgumentException(
								"MISSING PACKAGE "
										+ pkg
										+ " imported by "
										+ this
										+ " (add it to okayIfMissingPackage)");
					}
				} else {
					if (!bundle.tryActivate()) {
						addToWorkbenchQueue(this);
						logger.info("Activating {} delayed because an Import-Package needs workbench", this);
						return false;
					}
				}
				pkg = missingPkg();
			}
			for (var required : requiredBundles) {
				logger.info("{} requires {}", this, required);
				ShimBundle bundle = bundleByName(required);
				if (bundle != null) {
					if (!bundle.tryActivate()) {
						addToWorkbenchQueue(this);
						logger.info("Activating {} delayed because a Require-Bundle needs workbench", this);
						return false;
					}
				} else {
					if (!cfg.okayIfMissingBundle().contains(required)) {
						throw new IllegalArgumentException(
								"MISSING BUNDLE "
										+ required
										+ " needed by "
										+ this
										+ " (add it to okayIfMissingBundle)");
					} else {
						logger.info("  missing but okayIfMissingBundle so no problem");
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
				try {
					var c = (Constructor<BundleActivator>) Class.forName(activator).getConstructor();
					var bundleActivator = c.newInstance();
					bundleActivator.start(this);
				} catch (Exception e) {
					if (cfg.okayIfActivatorFails().contains(getSymbolicName())) {
						logger.warn(this + " Bundle-Activator failed but okayIfActivatorFails", e);
					} else {
						throw new ActivatorException(this, e);
					}
				}
			}
			state = ACTIVE;
			notifyBundleListeners(BundleEvent.STARTED, this);
			logger.info("\\FINISH ACTIVATE {}", this);
			return true;
		}

		private String missingPkg() {
			for (var pkg : pkgImports) {
				if (!pkgs.contains(pkg)) {
					return pkg;
				}
			}
			return null;
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

		private String stripLeadingSlash(String path) {
			if (path.startsWith("/")) {
				return path.substring(1);
			} else {
				return path;
			}
		}

		private String stripLeadingAddTrailingSlash(String path) {
			path = stripLeadingSlash(path);
			if (path.isEmpty() || path.equals("/")) {
				return "";
			} else {
				return path.endsWith("/") ? path : (path + "/");
			}
		}

		@Override
		public URL getEntry(String path) {
			try {
				return new URL(jarUrl + "/" + stripLeadingSlash(path));
			} catch (MalformedURLException e) {
				throw Unchecked.wrap(e);
			}
		}

		private <T> T parseFromZip(Function<ZipFile, T> function) {
			String prefix = "jar:file:";
			if (!jarUrl.startsWith(prefix)) {
				throw new IllegalArgumentException("Must start with " + prefix + " was " + jarUrl);
			}
			if (!jarUrl.endsWith("!")) {
				throw new IllegalArgumentException("Must end with ! was " + jarUrl);
			}
			try (var zipFile = new ZipFile(jarUrl.substring(prefix.length(), jarUrl.length() - 1))) {
				return function.apply(zipFile);
			} catch (IOException e) {
				throw Unchecked.wrap(e);
			}
		}

		@Override
		public URL getResource(String name) {
			ZipEntry entry = parseFromZip(zip -> zip.getEntry(stripLeadingSlash(name)));
			if (entry != null) {
				return getEntry(name);
			} else {
				return Solstice.class.getClassLoader().getResource(name);
			}
		}

		@Override
		public Enumeration<URL> getResources(String name) throws IOException {
			return Solstice.class.getClassLoader().getResources(name);
		}

		@Override
		public Enumeration<String> getEntryPaths(String path) {
			var pathFinal = stripLeadingAddTrailingSlash(path);
			return parseFromZip(
					zipFile -> {
						List<String> zipPaths = new ArrayList<>();
						var entries = zipFile.entries();
						while (entries.hasMoreElements()) {
							var entry = entries.nextElement();
							if (entry.getName().startsWith(pathFinal)) {
								zipPaths.add(entry.getName());
							}
						}
						return Collections.enumeration(zipPaths);
					});
		}

		@Override
		public Enumeration<URL> findEntries(String path, String filePattern, boolean recurse) {
			var urls = new ArrayList<URL>();
			findEntries(urls, path, filePattern, recurse);
			return Collections.enumeration(urls);
		}

		private void findEntries(List<URL> urls, String path, String filePattern, boolean recurse) {
			for (var fragment : fragments) {
				fragment.findEntries(urls, path, filePattern, recurse);
			}
			var pathFinal = stripLeadingAddTrailingSlash(path);
			var pattern = Pattern.compile(filePattern.replace(".", "\\.").replace("*", ".*"));
			var pathsWithinZip =
					parseFromZip(
							zipFile -> {
								List<String> zipPaths = new ArrayList<>();
								var entries = zipFile.entries();
								while (entries.hasMoreElements()) {
									var entry = entries.nextElement();
									if (entry.getName().startsWith(pathFinal)) {
										var after = entry.getName().substring(pathFinal.length());
										int lastSlash = after.lastIndexOf('/');
										if (lastSlash == -1) {
											if (pattern.matcher(after).matches()) {
												zipPaths.add(entry.getName());
											}
										} else if (recurse) {
											var name = after.substring(lastSlash + 1);
											if (pattern.matcher(name).matches()) {
												zipPaths.add(entry.getName());
											}
										}
									}
								}
								return zipPaths;
							});
			try {
				for (var withinZip : pathsWithinZip) {
					urls.add(new URL(jarUrl + "/" + withinZip));
				}
			} catch (MalformedURLException e) {
				throw Unchecked.wrap(e);
			}
		}

		@Override
		public String getLocation() {
			return jarUrl;
		}

		// implemented for OSGi DS
		@Override
		public <A> A adapt(Class<A> type) {
			if (BundleWiring.class.equals(type)) {
				return state == Bundle.INSTALLED ? null : (A) new ShimBundleWiring(this);
			} else if (BundleStartLevel.class.equals(type)) {
				return (A)
						new Unimplemented.BundleStartLevel() {
							@Override
							public boolean isActivationPolicyUsed() {
								return true;
							}
						};
			} else if (BundleRevision.class.equals(type)) {
				return (A) new ShimBundleRevision(this);
			} else {
				return null;
			}
		}
	}

	static class ActivatorException extends Exception {
		final ShimBundle bundle;

		ActivatorException(ShimBundle bundle, Exception cause) {
			super(cause);
			this.bundle = bundle;
		}
	}
}
