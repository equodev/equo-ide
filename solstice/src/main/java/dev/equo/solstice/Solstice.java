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
import java.util.List;
import java.util.Objects;
import java.util.TreeSet;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Function;
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

	public static Solstice initialize(SolsticeInit init, BundleSet bundleSet) {
		if (instance != null) {
			throw new IllegalStateException("Solstice has already been initialized");
		}
		instance = new Solstice(init, bundleSet);
		instance.bundleByName("org.eclipse.ui.ide.application").start();
		return instance;
	}

	private final Logger logger = LoggerFactory.getLogger(Solstice.class);
	private final SolsticeInit init;

	private Solstice(SolsticeInit init, BundleSet bundleSet) {
		Handler.install(this);

		this.init = init;

		SolsticeFrameworkUtilHelper.initialize(this);
		init.bootstrapServices(systemBundle, this);
		logger.info("Bootstrap services installed");
		bundleSet.hydrateFrom(
				manifest -> {
					var bundle = new ShimBundle(manifest);
					bundles.add(bundle);
					return bundle;
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
		startAllWithLazy(false);
	}

	public void startAllWithLazy(boolean lazyValue) {
		for (var solstice : bundles) {
			if (solstice.manifest.isNotFragment() && solstice.manifest.lazy == lazyValue) {
				try {
					solstice.activate();
				} catch (ActivatorException e) {
					throw Unchecked.wrap(e);
				}
			}
		}
	}

	@Override
	public Bundle systemBundle() {
		return systemBundle;
	}

	private final List<ShimBundle> bundles = new ArrayList<>();
	private final TreeSet<String> pkgs = new TreeSet<>();

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
			if (sourceString.equals(bundle.manifest.getJarUrl())) {
				return bundle;
			}
		}
		return null;
	}

	private List<ShimBundle> bundlesForPkg(String targetPkg) {
		Object bundleForPkg = null;
		for (var bundle : bundles) {
			if (bundle.manifest.getPkgExports().contains(targetPkg)) {
				if (bundleForPkg == null) {
					bundleForPkg = bundle;
				} else {
					if (bundleForPkg instanceof ArrayList) {
						((ArrayList) bundleForPkg).add(bundle);
					} else {
						var list = new ArrayList<ShimBundle>();
						list.add((ShimBundle) bundleForPkg);
						list.add(bundle);
					}
				}
			}
		}
		if (bundleForPkg == null) {
			return Collections.emptyList();
		} else if (bundleForPkg instanceof ShimBundle) {
			return Collections.singletonList((ShimBundle) bundleForPkg);
		} else {
			return (List<ShimBundle>) bundleForPkg;
		}
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
					return "solstice-system-bundle";
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
		return bundles.toArray(new Bundle[0]);
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
		var event = new BundleEvent(type, bundle);
		for (BundleListener listener : bundleListeners) {
			listener.bundleChanged(event);
		}
	}

	@Override
	public Bundle getBundle(long id) {
		return id == -1 ? systemBundle : bundles.get((int) id);
	}

	public class ShimBundle extends BundleContextDelegate implements Unimplemented.Bundle {
		final SolsticeManifest manifest;
		final @Nullable String activator;
		final List<ShimBundle> fragments = new ArrayList<>();
		final Hashtable<String, String> headers = new Hashtable<>();

		ShimBundle(SolsticeManifest manifest) {
			super(Solstice.this);
			this.manifest = manifest;
			activator = manifest.getHeadersOriginal().get(Constants.BUNDLE_ACTIVATOR);
			manifest
					.getHeadersOriginal()
					.forEach(
							(key, value) -> {
								if (!Constants.IMPORT_PACKAGE.equals(key)
										&& !Constants.EXPORT_PACKAGE.equals(key)) {
									headers.put(key, value);
								}
							});
		}

		private void addFragment(ShimBundle bundle) {
			fragments.add(bundle);
		}

		String fragmentHost() {
			var host = manifest.getHeadersOriginal().get(Constants.FRAGMENT_HOST);
			if (host == null) {
				return null;
			}
			var idx = host.indexOf(';');
			return idx == -1 ? host : host.substring(0, idx);
		}

		ShimBundle fragmentHostBundle() {
			var host = fragmentHost();
			return host == null ? null : bundleByName(host);
		}

		public ShimBundle bundleByName(String name) {
			return Solstice.this.bundleByName(name);
		}

		@Override
		public String toString() {
			return manifest.toString();
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
				activate();
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
		private void activate() throws ActivatorException {
			if (activating) {
				return;
			}
			activating = true;

			logger.info("Request activate {}", this);
			pkgs.addAll(manifest.getPkgExports());
			if ("org.eclipse.osgi".equals(getSymbolicName())) {
				state = ACTIVE;
				// skip org.eclipse.osgi on purpose
				logger.info("  skipping because of shim implementation");
				return;
			}
			String pkg;
			while ((pkg = missingPkg()) != null) {
				var bundles = bundlesForPkg(pkg);
				if (bundles.isEmpty()) {
					throw new IllegalArgumentException(manifest + " imports missing package " + pkg);
				} else {
					for (var bundle : bundles) {
						bundle.activate();
					}
				}
			}
			var requiredBundles = new ArrayList<>(manifest.getRequiredBundles());
			var additional = init.additionalDeps().get(getSymbolicName());
			if (additional != null) {
				requiredBundles.addAll(additional);
			}
			for (var required : requiredBundles) {
				ShimBundle bundle = bundleByName(required);
				if (bundle != null) {
					bundle.activate();
				} else {
					throw new IllegalArgumentException(this + " requires missing bundle " + required);
				}
			}
			state = RESOLVED;
			notifyBundleListeners(BundleEvent.RESOLVED, this);

			state = STARTING;
			notifyBundleListeners(BundleEvent.STARTING, this);
			if (activator != null) {
				try {
					var c = (Constructor<BundleActivator>) Class.forName(activator).getConstructor();
					var bundleActivator = c.newInstance();
					bundleActivator.start(this);
				} catch (Exception e) {
					e.printStackTrace();
					//					throw new ActivatorException(this, e);
				}
			}
			state = ACTIVE;
			notifyBundleListeners(BundleEvent.STARTED, this);
		}

		private String missingPkg() {
			for (var pkg : manifest.getPkgImports()) {
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
			return manifest.getSymbolicName();
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
				return new URL(manifest.getJarUrl() + "/" + stripLeadingSlash(path));
			} catch (MalformedURLException e) {
				throw Unchecked.wrap(e);
			}
		}

		private <T> T parseFromZip(Function<ZipFile, T> function) {
			String prefix = "jar:file:";
			if (!manifest.getJarUrl().startsWith(prefix)) {
				throw new IllegalArgumentException(
						"Must start with " + prefix + " was " + manifest.getJarUrl());
			}
			if (!manifest.getJarUrl().endsWith("!")) {
				throw new IllegalArgumentException("Must end with ! was " + manifest.getJarUrl());
			}
			try (var zipFile =
					new ZipFile(
							manifest.getJarUrl().substring(prefix.length(), manifest.getJarUrl().length() - 1))) {
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
					urls.add(new URL(manifest.getJarUrl() + "/" + withinZip));
				}
			} catch (MalformedURLException e) {
				throw Unchecked.wrap(e);
			}
		}

		@Override
		public String getLocation() {
			return manifest.getJarUrl();
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

	/** Parse out a manifest header, and ignore the versions */
	static List<String> parseManifestHeaderSimple(String in) {
		String[] bundlesAndVersions = in.split(",");
		List<String> attrs = new ArrayList<>();
		List<String> required = new ArrayList<>(bundlesAndVersions.length);
		for (String s : bundlesAndVersions) {
			attrs.clear();
			int attrDelim = s.indexOf(';');
			if (attrDelim == -1) {
				attrDelim = s.length();
			} else {
				int start = attrDelim;
				while (start < s.length()) {
					int next = s.indexOf(';', start + 1);
					if (next == -1) {
						next = s.length();
					}
					attrs.add(s.substring(start + 1, next).trim());
					start = next;
				}
			}
			if (attrs.contains("resolution:=optional")) {
				// skip everything optional
				continue;
			}
			String simple = s.substring(0, attrDelim);
			if (simple.indexOf('"') == -1) {
				required.add(simple.trim());
			}
		}
		return required;
	}
}
