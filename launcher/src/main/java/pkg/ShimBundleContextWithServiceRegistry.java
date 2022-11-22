package pkg;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import org.eclipse.osgi.internal.framework.FilterImpl;
import org.osgi.framework.Bundle;
import org.osgi.framework.Constants;
import org.osgi.framework.Filter;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceFactory;
import org.osgi.framework.ServiceListener;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ShimBundleContextWithServiceRegistry extends Shims.BundleContextUnsupported {
	private final Logger logger = LoggerFactory.getLogger(ShimBundleContextWithServiceRegistry.class);
	Map<String, List<AbstractServiceReference>> services = new HashMap<>();

	private List<AbstractServiceReference> servicesForInterface(String interfase) {
		List<AbstractServiceReference> list = services.get(interfase);
		if (list == null) {
			list = new ArrayList<>();
			services.put(interfase, list);
		}
		return list;
	}

	@Override
	public final synchronized ServiceRegistration<?> registerService(
			String clazz, Object service, Dictionary<String, ?> properties) {
		logger.info("{} implemented by {} with {}", clazz, service, properties);
		var newService = new ShimServiceReference<>(service, properties);
		servicesForInterface(clazz).add(newService);
		notifyListeners(ServiceEvent.REGISTERED, newService);
		return newService;
	}

	@Override
	public final synchronized <S> ServiceRegistration<S> registerService(
			Class<S> clazz, ServiceFactory<S> factory, Dictionary<String, ?> properties) {
		logger.info("{} implemented by factory {} with {}", clazz, factory, properties);
		var newService = new ShimServiceFactoryReference<>(clazz, factory, properties);
		servicesForInterface(clazz.getName()).add(newService);
		notifyListeners(ServiceEvent.REGISTERED, newService);
		return newService;
	}

	private final List<ListenerEntry> serviceListeners = new ArrayList<>();

	@Override
	public final synchronized void addServiceListener(ServiceListener listener, String filter) {
		try {
			logger.info("add listener {} with {}", listener, filter);
			serviceListeners.add(new ListenerEntry(listener, FilterImpl.newInstance(filter)));
		} catch (InvalidSyntaxException e) {
			throw new RuntimeException(e);
		}
	}

	private void notifyListeners(int type, AbstractServiceReference serviceReference) {
		var event = new ServiceEvent(type, serviceReference);
		for (var listener : serviceListeners) {
			if (listener.filter.match(serviceReference)) {
				listener.listener.serviceChanged(event);
			}
		}
	}

	static class ListenerEntry {
		final ServiceListener listener;
		final FilterImpl filter;

		ListenerEntry(ServiceListener listener, FilterImpl filter) {
			this.listener = listener;
			this.filter = filter;
		}
	}

	@Override
	public synchronized ServiceRegistration<?> registerService(
			String[] clazzes, Object service, Dictionary<String, ?> properties) {
		if (clazzes.length == 1) {
			return registerService(clazzes[0], service, properties);
		} else {
			var newService = new ShimServiceReference<>(service, properties);
			for (String clazz : clazzes) {
				servicesForInterface(clazz).equals(newService);
			}
			notifyListeners(ServiceEvent.REGISTERED, newService);
			return newService;
		}
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
		} else if (reference instanceof ShimServiceFactoryReference) {
			ShimServiceFactoryReference cast = (ShimServiceFactoryReference) reference;
			return (S) cast.factory.getService(null, cast);
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
		List<AbstractServiceReference> services = servicesForInterface(clazz);
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
		if (clazz != null && filter == null) {
			return servicesForInterface(clazz).toArray(new ServiceReference<?>[0]);
		} else {
			FilterImpl filterParsed = FilterImpl.newInstance(filter);
			String interfaze = clazz != null ? clazz : filterParsed.getRequiredObjectClass();
			return servicesForInterface(interfaze).stream()
					.filter(service -> filterParsed.match(service))
					.toArray(ServiceReference[]::new);
		}
	}

	@Override
	public final <S> Collection<ServiceReference<S>> getServiceReferences(
			Class<S> clazz, String filter) throws InvalidSyntaxException {
		return (List<ServiceReference<S>>)
				(Object) Arrays.asList(getServiceReferences(clazz.getName(), filter));
	}

	@Override
	public final Filter createFilter(String filter) throws InvalidSyntaxException {
		return FilterImpl.newInstance(filter);
	}

	class ShimServiceReference<S> extends AbstractServiceReference<S> {
		final S service;

		ShimServiceReference(S service, Dictionary<String, ?> properties) {
			super(properties);
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
	}

	class ShimServiceFactoryReference<S> extends AbstractServiceReference<S> {
		final Class<S> clazz;
		final ServiceFactory<S> factory;

		ShimServiceFactoryReference(
				Class<S> clazz, ServiceFactory<S> factory, Dictionary<String, ?> properties) {
			super(properties);
			this.clazz = clazz;
			this.factory = factory;
		}

		@Override
		public boolean isAssignableTo(Bundle bundle, String className) {
			if (className.equals(clazz.getName())) {
				return true;
			} else {
				try {
					return Class.forName(className).isAssignableFrom(clazz);
				} catch (ClassNotFoundException e) {
					throw new RuntimeException(e);
				}
			}
		}
	}

	static AtomicLong globalId = new AtomicLong();

	abstract class AbstractServiceReference<S>
			implements ServiceReference<S>, ServiceRegistration<S> {
		final long id;
		Dictionary<String, Object> properties;

		AbstractServiceReference(Dictionary<String, ?> properties) {
			this.id = globalId.getAndIncrement();
			if (properties != null) {
				this.properties = (Dictionary<String, Object>) properties;
			} else {
				this.properties = Dictionaries.empty();
			}
		}

		@Override
		public synchronized Object getProperty(String key) {
			if (key.equals(Constants.SERVICE_ID)) {
				return id;
			} else {
				return properties.get(key);
			}
		}

		@Override
		public synchronized String[] getPropertyKeys() {
			List<String> keys = new ArrayList<>();
			Enumeration<String> keysEnum = properties.keys();
			while (keysEnum.hasMoreElements()) {
				keys.add(keysEnum.nextElement());
			}
			return keys.toArray(new String[0]);
		}

		@Override
		public synchronized Dictionary<String, Object> getProperties() {
			return properties;
		}

		@Override
		public synchronized void setProperties(Dictionary properties) {
			this.properties = properties;
			notifyListeners(ServiceEvent.MODIFIED, this);
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

		// ServiceRegistration overrides
		@Override
		public ServiceReference getReference() {
			return this;
		}

		@Override
		public void unregister() {
			throw new UnsupportedOperationException();
		}
	}
}
