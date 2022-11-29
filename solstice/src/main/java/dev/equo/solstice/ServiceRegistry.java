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
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.framework.Filter;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceFactory;
import org.osgi.framework.ServiceListener;
import org.osgi.framework.ServiceObjects;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

abstract class ServiceRegistry implements BundleContext {
	private final Logger logger = LoggerFactory.getLogger(ServiceRegistry.class);
	Map<String, List<AbstractServiceReference>> services = new HashMap<>();

	private List<AbstractServiceReference> servicesForInterface(String interfase) {
		List<AbstractServiceReference> list = services.get(interfase);
		if (list == null) {
			list = new ArrayList<>();
			services.put(interfase, list);
		}
		return list;
	}

	protected abstract Bundle systemBundle();

	@Override
	public final ServiceRegistration<?> registerService(
			String clazz, Object service, Dictionary<String, ?> properties) {
		return registerService(new String[] {clazz}, service, properties);
	}

	@Override
	public final <S> ServiceRegistration<S> registerService(
			Class<S> clazz, ServiceFactory<S> factory, Dictionary<String, ?> properties) {
		return (ServiceRegistration<S>)
				registerService(new String[] {clazz.getName()}, factory, properties);
	}

	@Override
	public synchronized ServiceRegistration<?> registerService(
			String[] clazzes, Object service, Dictionary<String, ?> properties) {
		logger.info(
				"{} implemented by service {} with {}", Arrays.asList(clazzes), service, properties);
		AbstractServiceReference<?> newService;
		if (service instanceof ServiceFactory<?>) {
			newService =
					new ShimServiceFactoryReference<>((ServiceFactory<?>) service, clazzes, properties);
		} else {
			newService = new ShimServiceReference<>(service, clazzes, properties);
		}
		for (String clazz : clazzes) {
			servicesForInterface(clazz).add(newService);
		}
		notifyListeners(ServiceEvent.REGISTERED, newService);
		return newService;
	}

	private final List<ListenerEntry> serviceListeners = new ArrayList<>();

	@Override
	public synchronized void removeServiceListener(ServiceListener listener) {
		var iter = serviceListeners.iterator();
		while (iter.hasNext()) {
			if (iter.next().listener == listener) {
				iter.remove();
			}
		}
	}

	@Override
	public final synchronized void addServiceListener(ServiceListener listener, String filter) {
		try {
			logger.info("add listener {} with {}", listener, filter);
			serviceListeners.add(new ListenerEntry(listener, FilterImpl.newInstance(filter)));
		} catch (InvalidSyntaxException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public final synchronized void addServiceListener(ServiceListener listener) {
		logger.info("add listener {} with no filter", listener);
		serviceListeners.add(new ListenerEntry(listener, null));
	}

	private synchronized void notifyListeners(int type, AbstractServiceReference serviceReference) {
		var event = new ServiceEvent(type, serviceReference);
		for (var listener : serviceListeners) {
			if (listener.filter == null || listener.filter.match(serviceReference)) {
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

		@Override
		public String toString() {
			return filter.toString();
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
			return (S) cast.factory.getService(systemBundle(), cast);
		} else {
			throw new RuntimeException("Unexpected class " + reference);
		}
	}

	@Override
	public <S> ServiceObjects<S> getServiceObjects(ServiceReference<S> reference) {
		throw new UnsupportedOperationException();
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
			if (interfaze != null) {
				return servicesForInterface(interfaze).stream()
						.filter(service -> filterParsed.match(service))
						.toArray(ServiceReference[]::new);
			} else {
				return services.values().stream()
						.flatMap(list -> list.stream())
						.filter(service -> filterParsed.match(service))
						.toArray(ServiceReference[]::new);
			}
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

		ShimServiceReference(S service, String[] clazzes, Dictionary<String, ?> properties) {
			super(clazzes, properties);
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
		public String toString() {
			return service.toString();
		}
	}

	class ShimServiceFactoryReference<S> extends AbstractServiceReference<S> {
		final Class<?>[] clazzes;
		final ServiceFactory<S> factory;

		ShimServiceFactoryReference(
				ServiceFactory<S> factory, String[] clazzes, Dictionary<String, ?> properties) {
			super(clazzes, properties);
			this.factory = factory;
			this.clazzes = new Class[clazzes.length];
			for (int i = 0; i < clazzes.length; ++i) {
				try {
					this.clazzes[i] = Class.forName(clazzes[i]);
				} catch (ClassNotFoundException e) {
					logger.warn("unable to resolve class {} for ServiceFactory {}", clazzes[i], factory);
					this.clazzes[i] = ShimServiceFactoryReference.class;
				}
			}
		}

		@Override
		public boolean isAssignableTo(Bundle bundle, String className) {
			for (String clazz : objectClass) {
				if (clazz.equals(className)) {
					return true;
				}
			}
			try {
				var target = Class.forName(className);
				for (Class<?> clazz : clazzes) {
					if (target.isAssignableFrom(clazz)) {
						return true;
					}
				}
				return false;
			} catch (ClassNotFoundException e) {
				throw new RuntimeException(e);
			}
		}
	}

	static AtomicLong globalId = new AtomicLong();

	abstract class AbstractServiceReference<S>
			implements ServiceReference<S>, ServiceRegistration<S> {
		final String[] objectClass;
		final long id;
		Dictionary<String, Object> properties;

		AbstractServiceReference(String[] objectClass, Dictionary<String, ?> properties) {
			this.id = globalId.getAndIncrement();
			this.objectClass = objectClass;
			if (properties != null) {
				this.properties = (Dictionary<String, Object>) properties;
			} else {
				this.properties = Dictionaries.empty();
			}
		}

		@Override
		public synchronized Object getProperty(String key) {
			if (key.equals(Constants.OBJECTCLASS)) {
				return objectClass;
			} else if (key.equals(Constants.SERVICE_ID)) {
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
			return systemBundle();
		}

		@Override
		public void unregister() {
			notifyListeners(ServiceEvent.UNREGISTERING, this);
			synchronized (ServiceRegistry.this) {
				for (String clazz : objectClass) {
					servicesForInterface(clazz).remove(this);
				}
			}
		}

		@Override
		public int compareTo(Object reference) {
			if (reference instanceof AbstractServiceReference) {
				return (int) (id - ((AbstractServiceReference) reference).id);
			} else {
				throw new UnsupportedOperationException();
			}
		}

		@Override
		public Bundle[] getUsingBundles() {
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
	}
}
