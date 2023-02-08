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
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import javax.annotation.Nullable;
import org.eclipse.core.internal.runtime.InternalPlatform;
import org.eclipse.osgi.framework.log.FrameworkLog;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.BundleListener;
import org.osgi.framework.Constants;
import org.osgi.framework.FrameworkEvent;
import org.osgi.framework.FrameworkListener;
import org.osgi.framework.SynchronousBundleListener;
import org.osgi.framework.Version;
import org.osgi.framework.namespace.IdentityNamespace;
import org.osgi.framework.startlevel.BundleStartLevel;
import org.osgi.framework.startlevel.FrameworkStartLevel;
import org.osgi.framework.wiring.BundleCapability;
import org.osgi.framework.wiring.BundleRevision;
import org.osgi.framework.wiring.BundleWiring;
import org.osgi.framework.wiring.FrameworkWiring;
import org.osgi.resource.Requirement;
import org.osgi.service.packageadmin.PackageAdmin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A single-classloader implementation of OSGi which eagerly loads all the OSGi plugins it can find
 * on the classpath.
 */
public class BundleContextSolstice extends ServiceRegistry {
	private final Logger logger = LoggerFactory.getLogger(BundleContextSolstice.class);

	private static final Set<String> DONT_ACTIVATE = Set.of("org.eclipse.osgi");

	private static BundleContextSolstice instance;

	public static BundleContextSolstice hydrate(Solstice bundleSet, File configDir) {
		if (instance != null) {
			throw new IllegalStateException("Solstice has already been initialized");
		}
		instance = new BundleContextSolstice(bundleSet, configDir);
		return instance;
	}

	private final ShimStorage storage;

	private BundleContextSolstice(Solstice bundleSet, File configDir) {
		this.storage = new ShimStorage(configDir);
		Handler.install(this);
		SolsticeFrameworkUtilHelper.initialize(this);
		bundleSet.hydrateFrom(
				manifest -> {
					var bundle = new ShimBundle(manifest);
					bundles.add(bundle);
					return bundle;
				});
		for (var b : bundles) {
			notifyBundleListeners(BundleEvent.INSTALLED, b);
			b.state = Bundle.INSTALLED;
		}
		for (var b : bundles) {
			notifyBundleListeners(BundleEvent.RESOLVED, b);
			b.state = Bundle.RESOLVED;
		}
		for (var b : bundles) {
			if (b.manifest.lazy) {
				notifyBundleListeners(BundleEvent.LAZY_ACTIVATION, b);
				b.state = Bundle.STARTING;
			}
		}
	}

	@Override
	public Bundle systemBundle() {
		return systemBundle;
	}

	private final List<ShimBundle> bundles = new ArrayList<>();

	public ShimBundle bundleForSymbolicName(String name) {
		for (ShimBundle bundle : bundles) {
			if (name.equals(bundle.getSymbolicName())) {
				return bundle;
			}
		}
		return null;
	}

	public Bundle bundleForUrl(URL source) {
		String sourceString = "jar:" + source.toExternalForm() + "!";
		for (ShimBundle bundle : bundles) {
			if (sourceString.equals(bundle.manifest.getJarUrl())) {
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
					return "solstice-system-bundle";
				}

				@Override
				public File getDataFile(String filename) {
					return storage.getDataFileSystemBundle(BundleContextSolstice.this, filename);
				}

				@Override
				public URL getResource(String name) {
					return bundles.get(0).getResource(name);
				}

				@Override
				public Class<?> loadClass(String name) throws ClassNotFoundException {
					return Class.forName(name);
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
					return Version.emptyVersion;
				}

				@Override
				public String getLocation() {
					return "SYSTEM_BUNDLE_LOCATION";
				}

				@Override
				public BundleContextSolstice getBundleContext() {
					return BundleContextSolstice.this;
				}

				@Override
				public <A> A adapt(Class<A> type) {
					if (type.equals(PackageAdmin.class)) {
						return (A) packageAdmin;
					} else if (type.equals(FrameworkWiring.class)) {
						return (A) frameworkWiring;
					} else if (type.equals(BundleRevision.class)) {
						return null;
					} else if (type.equals(FrameworkStartLevel.class)) {
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
					if (bundle instanceof ShimBundle
							&& ((ShimBundle) bundle).manifest.fragmentHost() != null) {
						return BUNDLE_TYPE_FRAGMENT;
					}
					return 0;
				}

				@Override
				public Bundle[] getBundles(String symbolicName, String versionRange) {
					var bundle = bundleForSymbolicName(symbolicName);
					return (bundle == null) ? new Bundle[0] : new Bundle[] {bundle};
				}

				@Override
				public Bundle[] getHosts(Bundle bundle) {
					if (bundle instanceof ShimBundle) {
						var fragmentHost = ((ShimBundle) bundle).manifest.fragmentHost();
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
						if (Objects.equals(candidate.manifest.fragmentHost(), bundle.getSymbolicName())) {
							fragments.add(candidate);
						}
					}
					return fragments.toArray(new Bundle[0]);
				}
			};

	private Capability.SupersetMap<ShimBundle> capabilities = new Capability.SupersetMap<>();

	final FrameworkWiring frameworkWiring =
			new Unimplemented.FrameworkWiring() {
				@Override
				public Collection<BundleCapability> findProviders(Requirement req) {
					if (!Set.of(Constants.FILTER_DIRECTIVE).equals(req.getDirectives().keySet())) {
						throw Unimplemented.onPurpose(
								"Solstice supports only filter, this was " + req.getDirectives());
					}
					String filterRaw = req.getDirectives().get(Constants.FILTER_DIRECTIVE);
					var filter = SolsticeManifest.parseSingleFilter(filterRaw);

					if (req.getNamespace().equals(IdentityNamespace.IDENTITY_NAMESPACE)) {
						if (!filter.getKey().equals(IdentityNamespace.IDENTITY_NAMESPACE)) {
							throw Unimplemented.onPurpose(
									"Solstice expected " + IdentityNamespace.IDENTITY_NAMESPACE + ", was " + filter);
						}
						var bundle = bundleForSymbolicName(filter.getValue());
						Objects.requireNonNull(bundle, filter.getValue());
						return Collections.singleton(new ShimBundleCapability(bundle));
					} else {
						var cap = new Capability(req.getNamespace(), filter.getKey(), filter.getValue());
						var result = capabilities.getAnySupersetOf(cap);
						if (result == null) {
							throw new NoSuchElementException("No capability matching " + cap);
						}
						return Collections.singleton(new ShimBundleCapability(result));
					}
				}
			};

	@Override
	public org.osgi.framework.Bundle getBundle() {
		return systemBundle;
	}

	@Override
	public File getDataFile(String filename) {
		return storage.getDataFileRootContext(this, filename);
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
			try {
				boolean synchronousOnly =
						type == BundleEvent.STARTING
								|| type == BundleEvent.LAZY_ACTIVATION
								|| type == BundleEvent.STOPPING;
				if (synchronousOnly) {
					if (listener instanceof SynchronousBundleListener) {
						listener.bundleChanged(event);
					}
				} else {
					listener.bundleChanged(event);
				}
			} catch (Exception e) {
				getService(getServiceReference(FrameworkLog.class))
						.log(new FrameworkEvent(FrameworkEvent.ERROR, bundle, e));
			}
		}
	}

	@Override
	public Bundle getBundle(long id) {
		return id == -1 ? systemBundle : bundles.get((int) id);
	}

	public class ShimBundle extends BundleContextDelegate implements Unimplemented.Bundle {
		final SolsticeManifest manifest;
		final @Nullable String activator;
		final Hashtable<String, String> headers = new Hashtable<>();

		ShimBundle(SolsticeManifest manifest) {
			super(BundleContextSolstice.this);
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

		// BundleContext stuff
		@Override
		public org.osgi.framework.Bundle getBundle() {
			return this;
		}

		@Override
		public BundleContext getBundleContext() {
			return this;
		}

		BundleContextSolstice getRootBundleContext() {
			return BundleContextSolstice.this;
		}

		// bundle stuff
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
		public long getBundleId() {
			if (this == systemBundle) {
				return systemBundle.getBundleId();
			} else {
				return manifest.classpathOrder + 1;
			}
		}

		@Override
		public Version getVersion() {
			return Version.emptyVersion;
		}

		@Override
		public void start(int options) {
			activate();
		}

		///////////////////
		// Bundle overrides
		///////////////////
		private int state = INSTALLED;

		private boolean activateHasBeenCalled = false;

		/** Returns false if the bundle cannot activate yet because of "requiresWorkbench" */
		private void activate() {
			if (activateHasBeenCalled) {
				return;
			}
			activateHasBeenCalled = true;
			state = STARTING;
			notifyBundleListeners(BundleEvent.STARTING, this);

			for (var cap : manifest.capProvides) {
				capabilities.put(cap, this);
			}

			if (!DONT_ACTIVATE.contains(getSymbolicName()) && activator != null) {
				try {
					var c = (Constructor<BundleActivator>) Class.forName(activator).getConstructor();
					var bundleActivator = c.newInstance();
					bundleActivator.start(this);
				} catch (Exception e) {
					logger.warn("Error in activator of " + getSymbolicName(), e);
				}
			}
			state = ACTIVE;
			notifyBundleListeners(BundleEvent.STARTED, this);
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
				return BundleContextSolstice.class.getClassLoader().getResource(name);
			}
		}

		@Override
		public Enumeration<URL> getResources(String name) throws IOException {
			return BundleContextSolstice.class.getClassLoader().getResources(name);
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
			for (var fragments : manifest.fragments) {
				((ShimBundle) fragments.hydrated).findEntries(urls, path, filePattern, recurse);
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

		@Override
		public File getDataFile(String filename) {
			return storage.getDataFileBundle(this, filename);
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
}
