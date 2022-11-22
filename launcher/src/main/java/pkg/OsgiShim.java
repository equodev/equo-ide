package pkg;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.List;
import java.util.Objects;
import java.util.jar.Attributes;
import java.util.jar.Manifest;
import javax.annotation.Nullable;
import org.eclipse.core.internal.runtime.InternalPlatform;
import org.eclipse.osgi.internal.framework.FilterImpl;
import org.eclipse.osgi.service.datalocation.Location;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;
import org.osgi.framework.BundleListener;
import org.osgi.framework.Constants;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceListener;
import org.osgi.framework.Version;
import org.osgi.framework.namespace.IdentityNamespace;
import org.osgi.framework.wiring.BundleCapability;
import org.osgi.framework.wiring.BundleRevision;
import org.osgi.framework.wiring.FrameworkWiring;
import org.osgi.resource.Namespace;
import org.osgi.resource.Requirement;
import org.osgi.service.packageadmin.PackageAdmin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OsgiShim extends ShimBundleContextWithServiceRegistry {
	private static OsgiShim instance;

	public static OsgiShim initialize(EquinotConfiguration config) {
		if (instance != null) {
			throw new IllegalStateException("Equinot has already been initialized");
		}
		instance = new OsgiShim(config);
		return instance;
	}

	private final Logger logger = LoggerFactory.getLogger(OsgiShim.class);
	private final EquinotConfiguration cfg;

	private OsgiShim(EquinotConfiguration cfg) {
		this.cfg = cfg;
		try {
			ShimFrameworkUtilHelper.initialize(this);
			cfg.bootstrapServices(systemBundle, this);
			logger.info("Bootstrap services installed");

			discoverAndSortBundles();
			logger.info("Bundles found and sorted.");
			for (var b : bundles) {
				logger.info("  {}", b);
			}

			InternalPlatform.getDefault().start(this);
			logger.info("InternalPlatform started.");
			for (ShimBundle bundle : bundles) {
				try {
					bundle.activate();
				} catch (Exception e) {
					logger.warn("Error while activating " + bundle, e);
				}
			}
			ShimPluginXml.logExtensions(logger);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
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

	private ShimBundle bundleByName(String name) {
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
			return true;
		}

		@Override
		public boolean isSet() {
			return true;
		}
	}

	final Bundle systemBundle =
			new Shims.BundleUnsupported() {
				@Override
				public int getState() {
					// this signals InternalPlatform.isRunning() to be true
					return Bundle.ACTIVE;
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
				public <A> A adapt(Class<A> type) {
					if (type.equals(PackageAdmin.class)) {
						return (A) packageAdmin;
					} else if (type.equals(FrameworkWiring.class)) {
						return (A) frameworkWiring;
					} else {
						throw new UnsupportedOperationException(type.getName());
					}
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
	public org.osgi.framework.Bundle getBundle(String location) {
		if (Constants.SYSTEM_BUNDLE_LOCATION.equals(location)) {
			return systemBundle;
		} else {
			throw new UnsupportedOperationException();
		}
	}

	@Override
	public Bundle[] getBundles() {
		return bundles.toArray(new Bundle[0]);
	}

	@Override
	public String getProperty(String key) {
		// TODO: users might want to set various properties
		return null;
	}

	@Override
	public Bundle getBundle(long id) {
		return id == -1 ? systemBundle : bundles.get((int) id);
	}

	public class ShimBundle extends Shims.BundleContextDelegate implements Shims.BundleUnsupported {
		static final Attributes.Name SYMBOLIC_NAME = new Attributes.Name("Bundle-SymbolicName");
		static final Attributes.Name ACTIVATOR = new Attributes.Name("Bundle-Activator");
		static final Attributes.Name REQUIRE_BUNDLE = new Attributes.Name("Require-Bundle");
		static final Attributes.Name SERVICE_COMPONENT = new Attributes.Name("Service-Component");
		static final String MANIFEST_PATH = "/META-INF/MANIFEST.MF";

		final String jarUrl;
		final @Nullable String activator;
		final @Nullable String symbolicName;
		final List<String> requiredBundles;
		final List<String> osgiDS;

		ShimBundle(URL manifestURL) throws IOException {
			super(OsgiShim.this);
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
				throw new IllegalArgumentException(
						"Must end with !  SEE getEntry if this changes  " + jarUrl);
			}
			requiredBundles = requiredBundles(manifest);
			var serviceComponents = manifest.getMainAttributes().getValue(SERVICE_COMPONENT);
			if (serviceComponents == null) {
				osgiDS = Collections.emptyList();
			} else {
				String[] entries = serviceComponents.split(",");
				for (int i = 0; i < entries.length; ++i) {
					entries[i] = entries[i].trim(); // some have a leading space
				}
				if (entries.length == 1 && entries[0].trim().equals("OSGI-INF/*.xml")) {
					osgiDS = ShimDS.starDotXML(jarUrl);
				} else {
					osgiDS = Arrays.asList(entries);
				}
			}
		}

		private static List<String> requiredBundles(Manifest manifest) {
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
				return -1;
			} else {
				int idx = bundles.indexOf(this);
				if (idx == -1) {
					throw new IllegalStateException("This bundle wasn't known at startup.");
				}
				return idx;
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

		@Override
		public void addBundleListener(BundleListener listener) {
			// TODO: no-op, which might be a problem later. See for example:
			// https://github.com/eclipse-platform/eclipse.platform.ui/blob/4507d1fc873a70b5c74f411b2f7de70d37bd8d0a/bundles/org.eclipse.ui.workbench/Eclipse%20UI/org/eclipse/ui/plugin/AbstractUIPlugin.java#L508-L528
		}

		@Override
		public void removeBundleListener(BundleListener listener) {
			// TODO: no-op
		}

		@Override
		public void removeServiceListener(ServiceListener listener) {
			// TODO: no-op
		}

		///////////////////
		// Bundle overrides
		///////////////////
		private boolean isActivated = false;

		private void activate() throws Exception {
			if (isActivated) {
				return;
			}
			logger.info("Request activate {}", this);
			if ("org.eclipse.osgi".equals(symbolicName) || "org.apache.felix.scr".equals(symbolicName)) {
				// skip org.eclipse.osgi on purpose
				logger.info("  skipping because of shim implementation");
				return;
			}
			isActivated = true;
			for (var required : requiredBundles) {
				logger.info("{} requires {}", this, required);
				ShimBundle bundle = bundleByName(required);
				if (bundle != null) {
					bundle.activate();
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
			if (activator != null) {
				logger.info("{} Bundle-Activator {}", this, activator);
				var c = (Constructor<BundleActivator>) Class.forName(activator).getConstructor();
				if (c == null) {
					throw new IllegalArgumentException("No activator for " + jarUrl + " " + activator);
				}
				var bundleActivator = c.newInstance();
				bundleActivator.start(this);
			}

			if (ShimPluginXml.hasPluginXml(this)) {
				logger.info("{} plugin.xml", this);
				ShimPluginXml.register(this);
			}
			if (!osgiDS.isEmpty()) {
				ShimDS.register(logger, this);
			}
			logger.info("\\FINISH ACTIVATE {}", this);
		}

		@Override
		public int getState() {
			return isActivated ? Bundle.ACTIVE : Bundle.STARTING;
		}

		@Override
		public Dictionary<String, String> getHeaders(String locale) {
			// TODO: this should be from the MANIFEST.MF
			return Dictionaries.empty();
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
					throw new IllegalArgumentException("Path must not start with /");
				}
				return new URL(jarUrl + "/" + path);
			} catch (MalformedURLException e) {
				throw new RuntimeException(e);
			}
		}

		@Override
		public String getLocation() {
			return jarUrl;
		}
	}
}