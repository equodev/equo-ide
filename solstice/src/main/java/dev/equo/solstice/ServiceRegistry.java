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
import java.util.Collections;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicLong;
import org.eclipse.osgi.framework.log.FrameworkLog;
import org.eclipse.osgi.internal.framework.BundleContextImpl;
import org.eclipse.osgi.internal.framework.EquinoxBundle;
import org.eclipse.osgi.internal.framework.EquinoxContainer;
import org.eclipse.osgi.internal.framework.FilterImpl;
import org.osgi.framework.Bundle;
import org.osgi.framework.Constants;
import org.osgi.framework.Filter;
import org.osgi.framework.FrameworkEvent;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceFactory;
import org.osgi.framework.ServiceListener;
import org.osgi.framework.ServiceObjects;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The *only* reason we extend BundleContextImpl is to pass an instanceof check in <a
 * href=https://github.com/eclipse-equinox/equinox/blob/db101f974f02a52383b4a8358f74aee4fa33e837/bundles/org.eclipse.osgi/container/src/org/eclipse/osgi/internal/log/LogServiceManager.java#L78">
 * LogServiceManager</a>. If we can't pass that instance check, then we have to reimplement the
 * logging ourselves, and that's a pain.
 */
abstract class ServiceRegistry extends BundleContextImpl {
	ServiceRegistry(EquinoxBundle bundle, EquinoxContainer container) {
		super(bundle, container);
	}

	private final Logger logger = LoggerFactory.getLogger(ServiceRegistry.class);
	final Map<String, List<AbstractServiceReference<?>>> services = new HashMap<>();

	protected abstract Bundle systemBundle();

	@Override
	public final ServiceRegistration<?> registerService(
			String clazz, Object service, Dictionary<String, ?> properties) {
		return registerService(new String[] {clazz}, service, properties);
	}

	@SuppressWarnings("unchecked")
	@Override
	public final <S> ServiceRegistration<S> registerService(
			Class<S> clazz, ServiceFactory<S> factory, Dictionary<String, ?> properties) {
		return (ServiceRegistration<S>)
				registerService(new String[] {clazz.getName()}, factory, properties);
	}

	@Override
	public synchronized ServiceRegistration<?> registerService(
			String[] clazzes, Object service, Dictionary<String, ?> properties) {
		logger.debug(
				"{} implemented by service {} with {}",
				Arrays.asList(clazzes),
				service.getClass(),
				propertiesToString(properties));
		AbstractServiceReference<?> newService;
		if (service instanceof ServiceFactory<?>) {
			newService =
					new ShimServiceFactoryReference<>((ServiceFactory<?>) service, clazzes, properties);
		} else {
			newService = new ShimServiceReference<>(service, clazzes, properties);
		}
		for (String clazz : clazzes) {
			services.computeIfAbsent(clazz, k -> new ArrayList<>()).add(newService);
		}
		notifyListeners(ServiceEvent.REGISTERED, newService);
		return newService;
	}

	private static String propertiesToString(Dictionary<String, ?> dict) {
		if (dict == null) {
			return "{null}";
		}
		// sort the keys
		TreeMap<String, String> sorted = new TreeMap<>();
		var keys = dict.keys();
		while (keys.hasMoreElements()) {
			var key = keys.nextElement();
			var value = dict.get(key);
			String valueStr;
			if (value instanceof String[]) {
				valueStr = Arrays.asList((String[]) value).toString();
			} else {
				valueStr = value.toString();
			}
			sorted.put(key, valueStr);
		}

		StringBuilder builder = new StringBuilder();
		builder.append('{');
		sorted.forEach(
				(key, valueStr) -> {
					builder.append(key);
					builder.append('=');
					builder.append(valueStr);
					builder.append(',');
				});
		builder.setLength(builder.length() - 1);
		return builder.toString();
	}

	private final List<ListenerEntry> serviceListeners = new ArrayList<>();

	@Override
	public synchronized void removeServiceListener(ServiceListener listener) {
		serviceListeners.removeIf(entry -> entry.listener == listener);
	}

	@Override
	public final synchronized void addServiceListener(ServiceListener listener, String filter) {
		logger.debug("add listener {} with {}", listener.getClass(), filter);
		serviceListeners.add(
				new ListenerEntry(listener, Unchecked.get(() -> FilterImpl.newInstance(filter))));
	}

	@Override
	public final synchronized void addServiceListener(ServiceListener listener) {
		logger.debug("add listener {} with no filter", listener);
		serviceListeners.add(new ListenerEntry(listener, null));
	}

	private synchronized void notifyListeners(
			int type, AbstractServiceReference<?> serviceReference) {
		var event = new ServiceEvent(type, serviceReference);
		for (var listener : serviceListeners) {
			if (listener.filter == null || listener.filter.match(serviceReference)) {
				try {
					listener.listener.serviceChanged(event);
				} catch (Exception e) {
					getService(getServiceReference(FrameworkLog.class))
							.log(new FrameworkEvent(FrameworkEvent.ERROR, serviceReference.getBundle(), e));
				}
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

	@SuppressWarnings("unchecked")
	@Override
	public final <S> ServiceRegistration<S> registerService(
			Class<S> clazz, S service, Dictionary<String, ?> properties) {
		return (ServiceRegistration<S>) registerService(clazz.getName(), service, properties);
	}

	@Override
	public final <S> S getService(ServiceReference<S> reference) {
		if (reference instanceof ShimServiceReference) {
			return ((ShimServiceReference<S>) reference).service;
		} else if (reference instanceof ShimServiceFactoryReference) {
			var cast = (ShimServiceFactoryReference<S>) reference;
			return cast.factory.getService(systemBundle(), cast);
		} else {
			throw new RuntimeException("Unexpected class " + reference);
		}
	}

	@Override
	public <S> ServiceObjects<S> getServiceObjects(ServiceReference<S> reference) {
		throw Unimplemented.onPurpose();
	}

	@Override
	public final boolean ungetService(ServiceReference<?> reference) {
		return true;
	}

	@Override
	public final synchronized ServiceReference<?> getServiceReference(String clazz) {
		List<AbstractServiceReference<?>> servicesForClazz = services.get(clazz);
		return (servicesForClazz == null || servicesForClazz.isEmpty())
				? null
				: servicesForClazz.get(0);
	}

	@SuppressWarnings("unchecked")
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
			return services
					.getOrDefault(clazz, Collections.emptyList())
					.toArray(new ServiceReference<?>[0]);
		} else {
			FilterImpl filterParsed = FilterImpl.newInstance(filter);
			String interfaze = clazz != null ? clazz : filterParsed.getRequiredObjectClass();
			if (interfaze != null) {
				return services.getOrDefault(interfaze, Collections.emptyList()).stream()
						.filter(filterParsed::match)
						.toArray(ServiceReference[]::new);
			} else {
				return services.values().stream()
						.flatMap(Collection::stream)
						.filter(filterParsed::match)
						.toArray(ServiceReference[]::new);
			}
		}
	}

	@SuppressWarnings("unchecked")
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
			return Unchecked.classForName(className).isInstance(service);
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
			var target = Unchecked.classForName(className);
			for (Class<?> clazz : clazzes) {
				if (target.isAssignableFrom(clazz)) {
					return true;
				}
			}
			return false;
		}
	}

	static AtomicLong globalId = new AtomicLong();

	abstract class AbstractServiceReference<S>
			implements ServiceReference<S>, ServiceRegistration<S> {
		final String[] objectClass;
		final long id;
		Dictionary<String, ?> properties;

		AbstractServiceReference(String[] objectClass, Dictionary<String, ?> properties) {
			this.id = globalId.getAndIncrement();
			this.objectClass = objectClass;
			if (properties != null) {
				this.properties = Dictionaries.copy(properties);
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

		@SuppressWarnings("unchecked")
		@Override
		public synchronized Dictionary<String, Object> getProperties() {
			return (Dictionary<String, Object>) properties;
		}

		@Override
		public synchronized void setProperties(Dictionary<String, ?> properties) {
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
					services.get(clazz).remove(this);
				}
			}
		}

		@Override
		public int compareTo(Object reference) {
			if (reference instanceof AbstractServiceReference) {
				return (int) (id - ((AbstractServiceReference<?>) reference).id);
			} else {
				throw Unimplemented.onPurpose();
			}
		}

		@Override
		public Bundle[] getUsingBundles() {
			throw Unimplemented.onPurpose();
		}

		@Override
		public <A> A adapt(Class<A> type) {
			throw Unimplemented.onPurpose();
		}

		// ServiceRegistration overrides
		@Override
		public ServiceReference<S> getReference() {
			return this;
		}
	}
}
