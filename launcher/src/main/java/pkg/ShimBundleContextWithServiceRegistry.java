package pkg;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.eclipse.osgi.internal.framework.FilterImpl;
import org.osgi.framework.Bundle;
import org.osgi.framework.Filter;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;

public class ShimBundleContextWithServiceRegistry extends Shims.BundleContextUnsupported {
	Map<String, List<ShimServiceReference>> services = new HashMap<>();

	private List<ShimServiceReference> servicesForInterface(String interfase) {
		List<ShimServiceReference> list = services.get(interfase);
		if (list == null) {
			list = new ArrayList<>();
			services.put(interfase, list);
		}
		return list;
	}

	@Override
	public final synchronized ServiceRegistration<?> registerService(
			String clazz, Object service, Dictionary<String, ?> properties) {
		List<ShimServiceReference> references = servicesForInterface(clazz);
		references.add(new ShimServiceReference(service, properties));
		return null;
	}

	@Override
	public final <S> ServiceRegistration<S> registerService(
			Class<S> clazz, S service, Dictionary<String, ?> properties) {
		return (ServiceRegistration<S>) registerService(clazz.getName(), service, properties);
	}

	@Override
	public final <S> S getService(ServiceReference<S> reference) {
		if (reference instanceof ShimServiceReference) {
			return (S) ((ShimServiceReference) reference).service;
		} else {
			throw new RuntimeException("Unexpected class " + reference);
		}
	}

	@Override
	public final boolean ungetService(ServiceReference<?> reference) {
		return true;
	}

	@Override
	public final synchronized ServiceReference<?> getServiceReference(String clazz) {
		List<ShimServiceReference> services = servicesForInterface(clazz);
		return services.isEmpty() ? null : services.get(0);
	}

	@Override
	public final synchronized <S> ServiceReference<S> getServiceReference(Class<S> clazz) {
		return (ServiceReference<S>) getServiceReference(clazz.getName());
	}

	@Override
	public final ServiceReference<?>[] getAllServiceReferences(String clazz, String filter)
			throws InvalidSyntaxException {
		// no difference because we only have one classloader
		return getServiceReferences(clazz, filter);
	}

	@Override
	public final synchronized ServiceReference<?>[] getServiceReferences(String clazz, String filter)
			throws InvalidSyntaxException {
		List<ShimServiceReference> services;
		if (clazz != null) {
			services = servicesForInterface(clazz);
		} else {
			FilterImpl filterParsed = FilterImpl.newInstance(filter);
			services = servicesForInterface(filterParsed.getRequiredObjectClass());
		}
		// TODO: deal with filter
		return services.toArray(new ServiceReference<?>[0]);
	}

	@Override
	public final <S> Collection<ServiceReference<S>> getServiceReferences(
			Class<S> clazz, String filter) throws InvalidSyntaxException {
		return (List<ServiceReference<S>>)
				(Object) Arrays.asList(getServiceReferences(clazz.getName(), filter));
	}

	@Override
	public Filter createFilter(String filter) throws InvalidSyntaxException {
		return FilterImpl.newInstance(filter);
	}

	static class ShimServiceReference implements ServiceReference {
		final Object service;
		final Dictionary<String, Object> properties;

		ShimServiceReference(Object service, Dictionary<String, ?> properties) {
			this.service = service;
			this.properties = (Dictionary<String, Object>) properties;
		}

		@Override
		public boolean isAssignableTo(Bundle bundle, String className) {
			try {
				return Class.forName(className).isInstance(service);
			} catch (ClassNotFoundException e) {
				throw new RuntimeException(e);
			}
		}

		@Override
		public Object getProperty(String key) {
			return properties.get(key);
		}

		@Override
		public String[] getPropertyKeys() {
			List<String> keys = new ArrayList<>();
			Enumeration<String> keysEnum = properties.keys();
			while (keysEnum.hasMoreElements()) {
				keys.add(keysEnum.nextElement());
			}
			return keys.toArray(new String[0]);
		}

		@Override
		public Dictionary<String, Object> getProperties() {
			return properties;
		}

		@Override
		public Bundle getBundle() {
			// TODO: we could determine this for real using ShimFrameworkUtilHelper
			return null;
		}

		@Override
		public Bundle[] getUsingBundles() {
			throw new UnsupportedOperationException();
		}

		@Override
		public int compareTo(Object reference) {
			throw new UnsupportedOperationException();
		}

		@Override
		public Object adapt(Class type) {
			throw new UnsupportedOperationException();
		}
	}
}
