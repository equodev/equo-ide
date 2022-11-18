package pkg;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.cert.X509Certificate;
import java.util.Collection;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.osgi.framework.Bundle;
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
import org.osgi.framework.Version;

class Shims {
	static class BundleContextUnsupported implements BundleContext {
		@Override
		public String getProperty(String key) {
			throw new UnsupportedOperationException();
		}

		@Override
		public org.osgi.framework.Bundle getBundle() {
			throw new UnsupportedOperationException();
		}

		@Override
		public org.osgi.framework.Bundle installBundle(String location, InputStream input)
				throws BundleException {
			throw new UnsupportedOperationException();
		}

		@Override
		public org.osgi.framework.Bundle installBundle(String location) throws BundleException {
			throw new UnsupportedOperationException();
		}

		@Override
		public org.osgi.framework.Bundle getBundle(long id) {
			throw new UnsupportedOperationException();
		}

		@Override
		public org.osgi.framework.Bundle[] getBundles() {
			throw new UnsupportedOperationException();
		}

		@Override
		public void addServiceListener(ServiceListener listener, String filter)
				throws InvalidSyntaxException {
			throw new UnsupportedOperationException();
		}

		@Override
		public void addServiceListener(ServiceListener listener) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void removeServiceListener(ServiceListener listener) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void addBundleListener(BundleListener listener) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void removeBundleListener(BundleListener listener) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void addFrameworkListener(FrameworkListener listener) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void removeFrameworkListener(FrameworkListener listener) {
			throw new UnsupportedOperationException();
		}

		@Override
		public ServiceRegistration<?> registerService(
				String[] clazzes, Object service, Dictionary<String, ?> properties) {
			throw new UnsupportedOperationException();
		}

		@Override
		public ServiceRegistration<?> registerService(
				String clazz, Object service, Dictionary<String, ?> properties) {
			throw new UnsupportedOperationException();
		}

		@Override
		public <S> ServiceRegistration<S> registerService(
				Class<S> clazz, S service, Dictionary<String, ?> properties) {
			throw new UnsupportedOperationException();
		}

		@Override
		public <S> ServiceRegistration<S> registerService(
				Class<S> clazz, ServiceFactory<S> factory, Dictionary<String, ?> properties) {
			throw new UnsupportedOperationException();
		}

		@Override
		public ServiceReference<?>[] getServiceReferences(String clazz, String filter)
				throws InvalidSyntaxException {
			throw new UnsupportedOperationException();
		}

		@Override
		public ServiceReference<?>[] getAllServiceReferences(String clazz, String filter)
				throws InvalidSyntaxException {
			throw new UnsupportedOperationException();
		}

		@Override
		public ServiceReference<?> getServiceReference(String clazz) {
			throw new UnsupportedOperationException();
		}

		@Override
		public <S> ServiceReference<S> getServiceReference(Class<S> clazz) {
			throw new UnsupportedOperationException();
		}

		@Override
		public <S> Collection<ServiceReference<S>> getServiceReferences(Class<S> clazz, String filter)
				throws InvalidSyntaxException {
			throw new UnsupportedOperationException();
		}

		@Override
		public <S> S getService(ServiceReference<S> reference) {
			throw new UnsupportedOperationException();
		}

		@Override
		public boolean ungetService(ServiceReference<?> reference) {
			throw new UnsupportedOperationException();
		}

		@Override
		public <S> ServiceObjects<S> getServiceObjects(ServiceReference<S> reference) {
			throw new UnsupportedOperationException();
		}

		@Override
		public File getDataFile(String filename) {
			throw new UnsupportedOperationException();
		}

		@Override
		public Filter createFilter(String filter) throws InvalidSyntaxException {
			throw new UnsupportedOperationException();
		}

		@Override
		public org.osgi.framework.Bundle getBundle(String location) {
			throw new UnsupportedOperationException();
		}
	}

	static class BundleContextDelegate implements BundleContext {
		final BundleContext delegate;

		BundleContextDelegate(BundleContext delegate) {
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

	interface BundleUnsupported extends Bundle {
		@Override
		default BundleContext getBundleContext() {
			throw new UnsupportedOperationException();
		}

		@Override
		default String getSymbolicName() {
			throw new UnsupportedOperationException();
		}

		@Override
		default URL getEntry(String path) {
			throw new UnsupportedOperationException();
		}

		@Override
		default int getState() {
			throw new UnsupportedOperationException();
		}

		@Override
		default void start(int options) throws BundleException {
			throw new UnsupportedOperationException();
		}

		@Override
		default void start() throws BundleException {
			start(0);
		}

		@Override
		default void stop(int options) throws BundleException {
			throw new UnsupportedOperationException();
		}

		@Override
		default void stop() throws BundleException {
			stop(0);
		}

		@Override
		default void update(InputStream input) throws BundleException {
			throw new UnsupportedOperationException();
		}

		@Override
		default void update() throws BundleException {
			throw new UnsupportedOperationException();
		}

		@Override
		default void uninstall() throws BundleException {
			throw new UnsupportedOperationException();
		}

		@Override
		default Dictionary<String, String> getHeaders(String locale) {
			throw new UnsupportedOperationException();
		}

		@Override
		default Dictionary<String, String> getHeaders() {
			return getHeaders(Locale.ROOT.getDisplayName());
		}

		@Override
		default long getBundleId() {
			throw new UnsupportedOperationException();
		}

		@Override
		default String getLocation() {
			throw new UnsupportedOperationException();
		}

		@Override
		default ServiceReference<?>[] getRegisteredServices() {
			throw new UnsupportedOperationException();
		}

		@Override
		default ServiceReference<?>[] getServicesInUse() {
			throw new UnsupportedOperationException();
		}

		@Override
		default boolean hasPermission(Object permission) {
			throw new UnsupportedOperationException();
		}

		@Override
		default URL getResource(String name) {
			throw new UnsupportedOperationException();
		}

		@Override
		default Class<?> loadClass(String name) throws ClassNotFoundException {
			throw new UnsupportedOperationException();
		}

		@Override
		default Enumeration<URL> getResources(String name) throws IOException {
			throw new UnsupportedOperationException();
		}

		@Override
		default Enumeration<String> getEntryPaths(String path) {
			throw new UnsupportedOperationException();
		}

		@Override
		default long getLastModified() {
			throw new UnsupportedOperationException();
		}

		@Override
		default Enumeration<URL> findEntries(String path, String filePattern, boolean recurse) {
			throw new UnsupportedOperationException();
		}

		@Override
		default Map<X509Certificate, List<X509Certificate>> getSignerCertificates(int signersType) {
			throw new UnsupportedOperationException();
		}

		@Override
		default Version getVersion() {
			throw new UnsupportedOperationException();
		}

		@Override
		default <A> A adapt(Class<A> type) {
			throw new UnsupportedOperationException();
		}

		@Override
		default int compareTo(Bundle o) {
			return getSymbolicName().compareTo(o.getSymbolicName());
		}
	}
}
