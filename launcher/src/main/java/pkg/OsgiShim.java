package pkg;

import java.io.IOException;
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
import java.util.jar.Attributes;
import java.util.jar.Manifest;
import javax.annotation.Nullable;
import org.eclipse.core.internal.runtime.InternalPlatform;
import org.eclipse.osgi.internal.framework.FilterImpl;
import org.eclipse.osgi.service.environment.EnvironmentInfo;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleListener;
import org.osgi.framework.Constants;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceListener;
import org.osgi.framework.ServiceRegistration;
import org.osgi.framework.namespace.IdentityNamespace;
import org.osgi.framework.wiring.BundleCapability;
import org.osgi.framework.wiring.BundleRevision;
import org.osgi.framework.wiring.FrameworkWiring;
import org.osgi.resource.Namespace;
import org.osgi.resource.Requirement;
import org.osgi.service.packageadmin.PackageAdmin;

public class OsgiShim extends ShimBundleContextWithServiceRegistry {
	private static final OsgiShim instance = new OsgiShim();

	public static OsgiShim instance() {
		return instance;
	}

	private List<ShimBundle> bundles = new ArrayList<ShimBundle>();

	private ShimBundle bundleByName(String name) {
		for (ShimBundle bundle : bundles) {
			if (name.equals(bundle.getSymbolicName())) {
				return bundle;
			}
		}
		return null;
	}

	private OsgiShim() {
		try {
			registerService(EnvironmentInfo.class, new ShimEnvironmentInfo(), new Hashtable<>());
			InternalPlatform.getDefault().start(this);

			Enumeration<URL> resources = getClass().getClassLoader().getResources("META-INF/MANIFEST.MF");
			while (resources.hasMoreElements()) {
				bundles.add(new ShimBundle(this, resources.nextElement()));
			}

			for (ShimBundle bundle : bundles) {
				bundle.activate();
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	final Bundle systemBundle = new SystemBundle();

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

	class SystemBundle extends Shims.PackageAdminUnsupported implements Shims.BundleUnsupported {
		@Override
		public int getState() {
			// this signals InternalPlatform.isRunning() to be true
			return Bundle.ACTIVE;
		}

		public <A> A adapt(Class<A> type) {
			if (type.equals(PackageAdmin.class)) {
				return (A) packageAdmin;
			} else if (type.equals(FrameworkWiring.class)) {
				return (A) frameworkWiring;
			} else {
				throw new UnsupportedOperationException(type.getName());
			}
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
	public void addServiceListener(ServiceListener listener, String filter)
			throws InvalidSyntaxException {
		// TODO: this no-op *might* work
	}

	public static class ShimBundle extends Shims.BundleContextDelegate
			implements Shims.BundleUnsupported {
		static final Attributes.Name SYMBOLIC_NAME = new Attributes.Name("Bundle-SymbolicName");
		static final Attributes.Name ACTIVATOR = new Attributes.Name("Bundle-Activator");
		static final String MANIFEST_PATH = "/META-INF/MANIFEST.MF";

		final String jarFile;
		final @Nullable String activator;
		final @Nullable String symbolicName;

		ShimBundle(OsgiShim rootCtx, URL manifestURL) throws IOException {
			super(rootCtx);
			var externalForm = manifestURL.toExternalForm();
			if (!externalForm.endsWith(MANIFEST_PATH)) {
				throw new RuntimeException(
						"Expected manifest to end with " + MANIFEST_PATH + " but was " + externalForm);
			}
			jarFile = externalForm.substring(0, externalForm.length() - MANIFEST_PATH.length());
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
			if (!jarFile.endsWith("!")) {
				throw new IllegalArgumentException(
						"Must end with !  SEE getEntry if this changes  " + jarFile);
			}
		}

		@Override
		public String toString() {
			if (symbolicName != null) {
				return symbolicName;
			} else {
				return jarFile;
			}
		}

		private void activate() throws Exception {
			if (activator != null) {
				System.out.println("ACTIVATE " + symbolicName);
				var c = (Constructor<BundleActivator>) Class.forName(activator).getConstructor();
				if (c == null) {
					throw new IllegalArgumentException("No activator for " + jarFile + " " + activator);
				}
				var bundleActivator = c.newInstance();
				bundleActivator.start(this);
			}
		}

		//////////////////////////
		// BundleContext overrides
		//////////////////////////
		@Override
		public org.osgi.framework.Bundle getBundle() {
			return this;
		}

		@Override
		public void addBundleListener(BundleListener listener) {
			// TODO: no-op, which might be a problem later. See for example:
			// https://github.com/eclipse-platform/eclipse.platform.ui/blob/4507d1fc873a70b5c74f411b2f7de70d37bd8d0a/bundles/org.eclipse.ui.workbench/Eclipse%20UI/org/eclipse/ui/plugin/AbstractUIPlugin.java#L508-L528
		}

		@Override
		public <S> ServiceRegistration<S> registerService(
				Class<S> clazz, S service, Dictionary<String, ?> properties) {
			// TODO: no-op, which might be a problem later
			return null;
		}

		///////////////////
		// Bundle overrides
		///////////////////
		@Override
		public BundleContext getBundleContext() {
			return this;
		}

		@Override
		public String getSymbolicName() {
			return symbolicName;
		}

		@Override
		public int getState() {
			return Bundle.ACTIVE;
		}

		@Override
		public URL getEntry(String path) {
			try {
				if (path.startsWith("/")) {
					throw new IllegalArgumentException("Path must not start with /");
				}
				return new URL(jarFile + "/" + path);
			} catch (MalformedURLException e) {
				throw new RuntimeException(e);
			}
		}
	}
}
