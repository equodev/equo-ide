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

import java.io.File;
import java.io.InputStream;
import java.util.Collection;
import java.util.Dictionary;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;
import org.osgi.framework.BundleListener;
import org.osgi.framework.Filter;
import org.osgi.framework.FrameworkListener;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceFactory;
import org.osgi.framework.ServiceListener;
import org.osgi.framework.ServiceObjects;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;

class BundleContextDelegate implements BundleContext {
	final BundleContextShim delegate;

	BundleContextDelegate(BundleContextShim delegate) {
		this.delegate = delegate;
	}

	@Override
	public String getProperty(String key) {
		return delegate.getProperty(key);
	}

	@Override
	public org.osgi.framework.Bundle getBundle() {
		return delegate.getBundle();
	}

	@Override
	public org.osgi.framework.Bundle installBundle(String location, InputStream input)
			throws BundleException {
		return delegate.installBundle(location, input);
	}

	@Override
	public org.osgi.framework.Bundle installBundle(String location) throws BundleException {
		return delegate.installBundle(location);
	}

	@Override
	public org.osgi.framework.Bundle getBundle(long id) {
		return delegate.getBundle(id);
	}

	@Override
	public org.osgi.framework.Bundle[] getBundles() {
		return delegate.getBundles();
	}

	@Override
	public void addServiceListener(ServiceListener listener, String filter)
			throws InvalidSyntaxException {
		delegate.addServiceListener(listener, filter);
	}

	@Override
	public void addServiceListener(ServiceListener listener) {
		delegate.addServiceListener(listener);
	}

	@Override
	public void removeServiceListener(ServiceListener listener) {
		delegate.removeServiceListener(listener);
	}

	@Override
	public void addBundleListener(BundleListener listener) {
		delegate.addBundleListener(listener);
	}

	@Override
	public void removeBundleListener(BundleListener listener) {
		delegate.removeBundleListener(listener);
	}

	@Override
	public void addFrameworkListener(FrameworkListener listener) {
		delegate.addFrameworkListener(listener);
	}

	@Override
	public void removeFrameworkListener(FrameworkListener listener) {
		delegate.removeFrameworkListener(listener);
	}

	@Override
	public ServiceRegistration<?> registerService(
			String[] clazzes, Object service, Dictionary<String, ?> properties) {
		return delegate.registerService(clazzes, service, properties);
	}

	@Override
	public ServiceRegistration<?> registerService(
			String clazz, Object service, Dictionary<String, ?> properties) {
		return delegate.registerService(clazz, service, properties);
	}

	@Override
	public <S> ServiceRegistration<S> registerService(
			Class<S> clazz, S service, Dictionary<String, ?> properties) {
		return delegate.registerService(clazz, service, properties);
	}

	@Override
	public <S> ServiceRegistration<S> registerService(
			Class<S> clazz, ServiceFactory<S> factory, Dictionary<String, ?> properties) {
		return delegate.registerService(clazz, factory, properties);
	}

	@Override
	public ServiceReference<?>[] getServiceReferences(String clazz, String filter)
			throws InvalidSyntaxException {
		return delegate.getServiceReferences(clazz, filter);
	}

	@Override
	public ServiceReference<?>[] getAllServiceReferences(String clazz, String filter)
			throws InvalidSyntaxException {
		return delegate.getAllServiceReferences(clazz, filter);
	}

	@Override
	public ServiceReference<?> getServiceReference(String clazz) {
		return delegate.getServiceReference(clazz);
	}

	@Override
	public <S> ServiceReference<S> getServiceReference(Class<S> clazz) {
		return delegate.getServiceReference(clazz);
	}

	@Override
	public <S> Collection<ServiceReference<S>> getServiceReferences(Class<S> clazz, String filter)
			throws InvalidSyntaxException {
		return delegate.getServiceReferences(clazz, filter);
	}

	@Override
	public <S> S getService(ServiceReference<S> reference) {
		return delegate.getService(reference);
	}

	@Override
	public boolean ungetService(ServiceReference<?> reference) {
		return delegate.ungetService(reference);
	}

	@Override
	public <S> ServiceObjects<S> getServiceObjects(ServiceReference<S> reference) {
		return delegate.getServiceObjects(reference);
	}

	@Override
	public File getDataFile(String filename) {
		return delegate.getDataFile(filename);
	}

	@Override
	public Filter createFilter(String filter) throws InvalidSyntaxException {
		return delegate.createFilter(filter);
	}

	@Override
	public org.osgi.framework.Bundle getBundle(String location) {
		return delegate.getBundle(location);
	}
}
