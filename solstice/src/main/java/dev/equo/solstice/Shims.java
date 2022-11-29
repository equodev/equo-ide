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
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.cert.X509Certificate;
import java.util.Collection;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import org.eclipse.osgi.service.datalocation.Location;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;
import org.osgi.framework.FrameworkListener;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.Version;
import org.osgi.framework.startlevel.BundleStartLevel;
import org.osgi.framework.wiring.BundleCapability;
import org.osgi.framework.wiring.BundleRequirement;
import org.osgi.framework.wiring.BundleRevision;
import org.osgi.framework.wiring.BundleWiring;
import org.osgi.framework.wiring.FrameworkWiring;
import org.osgi.resource.Capability;
import org.osgi.resource.Requirement;
import org.osgi.service.packageadmin.ExportedPackage;
import org.osgi.service.packageadmin.PackageAdmin;
import org.osgi.service.packageadmin.RequiredBundle;

class Shims {

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
			return getHeaders("");
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
		default File getDataFile(String filename) {
			return null;
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

	static class PackageAdminUnsupported implements PackageAdmin {
		@Override
		public ExportedPackage[] getExportedPackages(Bundle bundle) {
			throw new UnsupportedOperationException();
		}

		@Override
		public ExportedPackage[] getExportedPackages(String name) {
			throw new UnsupportedOperationException();
		}

		@Override
		public ExportedPackage getExportedPackage(String name) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void refreshPackages(Bundle[] bundles) {
			throw new UnsupportedOperationException();
		}

		@Override
		public boolean resolveBundles(Bundle[] bundles) {
			throw new UnsupportedOperationException();
		}

		@Override
		public RequiredBundle[] getRequiredBundles(String symbolicName) {
			throw new UnsupportedOperationException();
		}

		@Override
		public Bundle[] getBundles(String symbolicName, String versionRange) {
			throw new UnsupportedOperationException();
		}

		@Override
		public Bundle[] getFragments(Bundle bundle) {
			throw new UnsupportedOperationException();
		}

		@Override
		public Bundle[] getHosts(Bundle bundle) {
			throw new UnsupportedOperationException();
		}

		@Override
		public Bundle getBundle(Class<?> clazz) {
			throw new UnsupportedOperationException();
		}

		@Override
		public int getBundleType(Bundle bundle) {
			throw new UnsupportedOperationException();
		}
	}

	static class FrameworkWiringUnsupported implements FrameworkWiring {
		@Override
		public void refreshBundles(Collection<Bundle> bundles, FrameworkListener... listeners) {
			throw new UnsupportedOperationException();
		}

		@Override
		public boolean resolveBundles(Collection<Bundle> bundles) {
			throw new UnsupportedOperationException();
		}

		@Override
		public Collection<Bundle> getRemovalPendingBundles() {
			throw new UnsupportedOperationException();
		}

		@Override
		public Collection<Bundle> getDependencyClosure(Collection<Bundle> bundles) {
			throw new UnsupportedOperationException();
		}

		@Override
		public Collection<BundleCapability> findProviders(Requirement requirement) {
			throw new UnsupportedOperationException();
		}

		@Override
		public Bundle getBundle() {
			throw new UnsupportedOperationException();
		}
	}

	static class BundleCapabilityUnsupported implements BundleCapability {
		@Override
		public BundleRevision getRevision() {
			throw new UnsupportedOperationException();
		}

		@Override
		public String getNamespace() {
			throw new UnsupportedOperationException();
		}

		@Override
		public Map<String, String> getDirectives() {
			throw new UnsupportedOperationException();
		}

		@Override
		public Map<String, Object> getAttributes() {
			throw new UnsupportedOperationException();
		}

		@Override
		public BundleRevision getResource() {
			throw new UnsupportedOperationException();
		}
	}

	static class BundleRevisionUnsupported implements BundleRevision {
		@Override
		public String getSymbolicName() {
			throw new UnsupportedOperationException();
		}

		@Override
		public Version getVersion() {
			throw new UnsupportedOperationException();
		}

		@Override
		public List<BundleCapability> getDeclaredCapabilities(String namespace) {
			throw new UnsupportedOperationException();
		}

		@Override
		public List<BundleRequirement> getDeclaredRequirements(String namespace) {
			throw new UnsupportedOperationException();
		}

		@Override
		public int getTypes() {
			throw new UnsupportedOperationException();
		}

		@Override
		public BundleWiring getWiring() {
			throw new UnsupportedOperationException();
		}

		@Override
		public List<Capability> getCapabilities(String namespace) {
			throw new UnsupportedOperationException();
		}

		@Override
		public List<Requirement> getRequirements(String namespace) {
			throw new UnsupportedOperationException();
		}

		@Override
		public Bundle getBundle() {
			throw new UnsupportedOperationException();
		}
	}

	static class LocationUnsupported implements Location {
		@Override
		public boolean allowsDefault() {
			throw new UnsupportedOperationException();
		}

		@Override
		public URL getDefault() {
			throw new UnsupportedOperationException();
		}

		@Override
		public Location getParentLocation() {
			throw new UnsupportedOperationException();
		}

		@Override
		public URL getURL() {
			throw new UnsupportedOperationException();
		}

		@Override
		public boolean isSet() {
			throw new UnsupportedOperationException();
		}

		@Override
		public boolean isReadOnly() {
			throw new UnsupportedOperationException();
		}

		@Override
		public boolean setURL(URL value, boolean lock) throws IllegalStateException {
			throw new UnsupportedOperationException();
		}

		@Override
		public boolean set(URL value, boolean lock) throws IllegalStateException, IOException {
			throw new UnsupportedOperationException();
		}

		@Override
		public boolean set(URL value, boolean lock, String lockFilePath)
				throws IllegalStateException, IOException {
			throw new UnsupportedOperationException();
		}

		@Override
		public boolean lock() throws IOException {
			throw new UnsupportedOperationException();
		}

		@Override
		public void release() {
			throw new UnsupportedOperationException();
		}

		@Override
		public boolean isLocked() throws IOException {
			throw new UnsupportedOperationException();
		}

		@Override
		public Location createLocation(Location parent, URL defaultValue, boolean readonly) {
			throw new UnsupportedOperationException();
		}

		@Override
		public URL getDataArea(String path) throws IOException {
			throw new UnsupportedOperationException();
		}
	}

	static class BundleStartLevelImpl implements BundleStartLevel {
		@Override
		public boolean isActivationPolicyUsed() {
			return true;
		}

		@Override
		public int getStartLevel() {
			throw new UnsupportedOperationException();
		}

		@Override
		public void setStartLevel(int startlevel) {
			throw new UnsupportedOperationException();
		}

		@Override
		public boolean isPersistentlyStarted() {
			throw new UnsupportedOperationException();
		}

		@Override
		public Bundle getBundle() {
			throw new UnsupportedOperationException();
		}
	}
}
