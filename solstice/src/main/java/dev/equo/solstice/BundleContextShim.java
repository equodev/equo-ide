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
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.CopyOnWriteArrayList;
import org.eclipse.core.internal.runtime.InternalPlatform;
import org.eclipse.osgi.framework.log.FrameworkLog;
import org.eclipse.osgi.internal.framework.EquinoxBundle;
import org.eclipse.osgi.internal.framework.EquinoxContainer;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.BundleListener;
import org.osgi.framework.Constants;
import org.osgi.framework.FrameworkEvent;
import org.osgi.framework.FrameworkListener;
import org.osgi.framework.SynchronousBundleListener;
import org.osgi.framework.namespace.IdentityNamespace;
import org.osgi.framework.wiring.BundleCapability;
import org.osgi.framework.wiring.FrameworkWiring;
import org.osgi.resource.Requirement;
import org.osgi.service.packageadmin.PackageAdmin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A single-classloader implementation of OSGi which eagerly loads all the OSGi plugins it can find
 * on the classpath.
 */
@SuppressWarnings("deprecation")
public class BundleContextShim extends ServiceRegistry {
	final Logger logger = LoggerFactory.getLogger(BundleContextShim.class);

	static final Set<String> DONT_ACTIVATE = Set.of("org.eclipse.osgi");

	private static BundleContextShim instance;

	public static BundleContextShim hydrate(Solstice bundleSet, Map<String, String> props) {
		if (instance != null) {
			throw new IllegalStateException("Solstice has already been initialized");
		}
		// see ServiceRegistry javadoc for explanation of why we need this container
		// TL;DR to use the built-in logging we need to pass an instanceof check
		EquinoxContainer container = new EquinoxContainer(props, null);
		EquinoxBundle fakeBundle = new EquinoxBundle(-1L, "FAKE LOCATION", null, null, -1, container);
		instance = new BundleContextShim(bundleSet, props, container, fakeBundle);
		return instance;
	}

	final EquinoxContainer container;
	private final Map<String, String> props;
	final ShimStorage storage;
	private ShimBundle systemBundle;

	private BundleContextShim(
			Solstice bundleSet,
			Map<String, String> props,
			EquinoxContainer fakeContainer,
			EquinoxBundle fakeBundle) {
		super(fakeBundle, fakeContainer);
		this.container = fakeContainer;
		this.props = new TreeMap<>(props);
		this.props.replaceAll(
				(key, value) -> {
					if (ShimIdeBootstrapServices.locationKeys().contains(key)) {
						if (!value.startsWith("file:")) {
							value = new File(value).toURI().toString();
						}
						if (!value.endsWith("/")) {
							value = value + "/";
						}
					}
					return value;
				});
		this.storage = new ShimStorage(props, logger);
		Handler.install(this);
		SolsticeFrameworkUtilHelper.initialize(this);
		bundleSet.hydrateFrom(
				manifest -> {
					long bundleId;
					if ("org.eclipse.osgi".equals(manifest.getSymbolicName())) {
						bundleId = 0;
					} else {
						bundleId = manifest.classpathOrder + 1;
					}
					var bundle = new ShimBundle(bundleId, this, manifest);
					bundles.add(bundle);
					if (bundleId == 0) {
						systemBundle = bundle;
					}
					return bundle;
				});
		Objects.requireNonNull(systemBundle);
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

	Capability.SupersetMap<ShimBundle> capabilities = new Capability.SupersetMap<>();

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
						if (bundle == null) {
							return Collections.emptyList();
						} else {
							return Collections.singleton(new ShimBundleCapability(bundle));
						}
					} else {
						var cap = new Capability(req.getNamespace(), filter.getKey(), filter.getValue());
						var result = capabilities.getAnySupersetOf(cap);
						if (result == null) {
							return Collections.emptyList();
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
		return storage.getDataFileBundle(systemBundle, filename);
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
			String prop = props.get(key);
			if (prop != null) {
				return prop;
			} else {
				return System.getProperty(key);
			}
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

	synchronized void notifyBundleListeners(int type, ShimBundle bundle) {
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
		if (id == 0) {
			return systemBundle;
		} else {
			for (var bundle : bundles) {
				if (bundle.getBundleId() == id) {
					return bundle;
				}
			}
			return null;
		}
	}
}
