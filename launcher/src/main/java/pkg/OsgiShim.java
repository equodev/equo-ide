package pkg;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.Attributes;
import java.util.jar.Manifest;
import javax.annotation.Nullable;
import org.eclipse.core.internal.runtime.InternalPlatform;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleListener;
import org.osgi.framework.Constants;
import org.osgi.framework.ServiceRegistration;

public class OsgiShim extends Shims.BundleContextUnsupported {
	private static final OsgiShim instance = new OsgiShim();

	public static OsgiShim instance() {
		return instance;
	}

	private List<ShimBundle> bundles = new ArrayList<ShimBundle>();

	private OsgiShim() {
		try {
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

	@Override
	public org.osgi.framework.Bundle getBundle(String location) {
		if (Constants.SYSTEM_BUNDLE_LOCATION.equals(location)) {
			// TODO: return system Bundle
		}
		throw new UnsupportedOperationException();
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
			symbolicName = manifest.getMainAttributes().getValue(SYMBOLIC_NAME);
		}

		private void activate() throws Exception {
			if (activator != null) {
				var c = (Constructor<BundleActivator>) Class.forName(activator).getConstructor();
				if (c == null) {
					System.out.println("No activator for " + jarFile + " " + activator);
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
		public URL getEntry(String path) {
			try {
				return new URL(jarFile + path);
			} catch (MalformedURLException e) {
				throw new RuntimeException(e);
			}
		}
	}
}
