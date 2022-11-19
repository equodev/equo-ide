package pkg;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Dictionary;
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
	Map<Class<?>, List<?>> services = new HashMap<>();

	private <S> List<ShimServiceReference<S>> servicesForInterface(Class<S> interfase) {
		List<?> list = services.get(interfase);
		if (list == null) {
			list = new ArrayList<>();
			services.put(interfase, list);
		}
		return (List<ShimServiceReference<S>>) list;
	}

	@Override
	public final ServiceRegistration<?> registerService(
			String clazz, Object service, Dictionary<String, ?> properties) {
		try {
			Class<Object> clazzObj = (Class<Object>) Class.forName(clazz);
			return registerService(clazzObj, service, properties);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public final synchronized <S> ServiceRegistration<S> registerService(
			Class<S> clazz, S service, Dictionary<String, ?> properties) {
		List<ShimServiceReference<S>> services = servicesForInterface(clazz);
		services.add(new ShimServiceReference<>(service));
		return null;
	}

	@Override
	public final <S> S getService(ServiceReference<S> reference) {
		if (reference instanceof ShimServiceReference<S>) {
			return ((ShimServiceReference<S>) reference).service;
		} else {
			throw new RuntimeException("Unexpected class " + reference);
		}
	}

	@Override
	public final ServiceReference<?> getServiceReference(String clazz) {
		try {
			return getServiceReference(Class.forName(clazz));
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public final synchronized <S> ServiceReference<S> getServiceReference(Class<S> clazz) {
		List<ShimServiceReference<S>> services = servicesForInterface(clazz);
		return services.isEmpty() ? null : services.get(0);
	}

	@Override
	public final ServiceReference<?>[] getAllServiceReferences(String clazz, String filter)
			throws InvalidSyntaxException {
		try {
			return getServiceReferences(Class.forName(clazz), filter).toArray(new ServiceReference<?>[0]);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public final ServiceReference<?>[] getServiceReferences(String clazz, String filter)
			throws InvalidSyntaxException {
		try {
			return getServiceReferences(Class.forName(clazz), filter).toArray(new ServiceReference<?>[0]);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public final <S> Collection<ServiceReference<S>> getServiceReferences(
			Class<S> clazz, String filter) throws InvalidSyntaxException {
		try {
			List<ShimServiceReference<S>> services;
			if (clazz != null) {
				services = servicesForInterface(clazz);
			} else {
				FilterImpl filterParsed = FilterImpl.newInstance(filter);
				Class<S> clazzFilter = (Class<S>) Class.forName(filterParsed.getRequiredObjectClass());
				services = servicesForInterface(clazzFilter);
			}
			return Collections.unmodifiableList(servicesForInterface(clazz));
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Filter createFilter(String filter) throws InvalidSyntaxException {
		return FilterImpl.newInstance(filter);
	}

	static class ShimServiceReference<S> implements ServiceReference<S> {
		final S service;

		ShimServiceReference(S service) {
			this.service = service;
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
			throw new UnsupportedOperationException();
		}

		@Override
		public String[] getPropertyKeys() {
			throw new UnsupportedOperationException();
		}

		@Override
		public Bundle getBundle() {
			throw new UnsupportedOperationException();
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
		public Dictionary<String, Object> getProperties() {
			throw new UnsupportedOperationException();
		}

		@Override
		public <A> A adapt(Class<A> type) {
			throw new UnsupportedOperationException();
		}
	}
}
