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
import java.util.List;
import java.util.Map;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkListener;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.Version;
import org.osgi.framework.wiring.BundleRequirement;
import org.osgi.framework.wiring.BundleWire;
import org.osgi.resource.Capability;
import org.osgi.resource.Requirement;
import org.osgi.resource.Wire;
import org.osgi.service.packageadmin.ExportedPackage;
import org.osgi.service.packageadmin.RequiredBundle;

class Unimplemented {
	static RuntimeException onPurpose() {
		//		com.diffplug.common.debug.StackDumper.dump("About to die");
		//		System.exit(1);
		return new UnsupportedOperationException(
				"Solstice believes that this method is not actually needed.");
	}

	interface Bundle extends org.osgi.framework.Bundle {
		@Override
		default BundleContext getBundleContext() {
			throw onPurpose();
		}

		@Override
		default String getSymbolicName() {
			throw onPurpose();
		}

		@Override
		default URL getEntry(String path) {
			throw onPurpose();
		}

		@Override
		default int getState() {
			throw onPurpose();
		}

		@Override
		default void start(int options) {
			throw onPurpose();
		}

		@Override
		default void start() {
			start(0);
		}

		@Override
		default void stop(int options) {
			throw onPurpose();
		}

		@Override
		default void stop() {
			stop(0);
		}

		@Override
		default void update(InputStream input) {
			throw onPurpose();
		}

		@Override
		default void update() {
			throw onPurpose();
		}

		@Override
		default void uninstall() {
			throw onPurpose();
		}

		@Override
		default Dictionary<String, String> getHeaders(String locale) {
			throw onPurpose();
		}

		@Override
		default Dictionary<String, String> getHeaders() {
			return getHeaders("");
		}

		@Override
		default long getBundleId() {
			throw onPurpose();
		}

		@Override
		default String getLocation() {
			throw onPurpose();
		}

		@Override
		default ServiceReference<?>[] getRegisteredServices() {
			throw onPurpose();
		}

		@Override
		default ServiceReference<?>[] getServicesInUse() {
			throw onPurpose();
		}

		@Override
		default boolean hasPermission(Object permission) {
			throw onPurpose();
		}

		@Override
		default Class<?> loadClass(String name) throws ClassNotFoundException {
			throw onPurpose();
		}

		@Override
		default long getLastModified() {
			throw onPurpose();
		}

		@Override
		default File getDataFile(String filename) {
			return null;
		}

		@Override
		default Map<X509Certificate, List<X509Certificate>> getSignerCertificates(int signersType) {
			throw onPurpose();
		}

		@Override
		default Version getVersion() {
			throw onPurpose();
		}

		@Override
		default <A> A adapt(Class<A> type) {
			throw onPurpose();
		}

		@Override
		default int compareTo(org.osgi.framework.Bundle o) {
			return getSymbolicName().compareTo(o.getSymbolicName());
		}
	}

	static class PackageAdmin implements org.osgi.service.packageadmin.PackageAdmin {
		@Override
		public ExportedPackage[] getExportedPackages(org.osgi.framework.Bundle bundle) {
			throw onPurpose();
		}

		@Override
		public ExportedPackage[] getExportedPackages(String name) {
			throw onPurpose();
		}

		@Override
		public ExportedPackage getExportedPackage(String name) {
			throw onPurpose();
		}

		@Override
		public void refreshPackages(org.osgi.framework.Bundle[] bundles) {
			throw onPurpose();
		}

		@Override
		public boolean resolveBundles(org.osgi.framework.Bundle[] bundles) {
			throw onPurpose();
		}

		@Override
		public RequiredBundle[] getRequiredBundles(String symbolicName) {
			throw onPurpose();
		}

		@Override
		public org.osgi.framework.Bundle[] getBundles(String symbolicName, String versionRange) {
			throw onPurpose();
		}

		@Override
		public org.osgi.framework.Bundle[] getFragments(org.osgi.framework.Bundle bundle) {
			throw onPurpose();
		}

		@Override
		public org.osgi.framework.Bundle[] getHosts(org.osgi.framework.Bundle bundle) {
			throw onPurpose();
		}

		@Override
		public org.osgi.framework.Bundle getBundle(Class<?> clazz) {
			throw onPurpose();
		}

		@Override
		public int getBundleType(org.osgi.framework.Bundle bundle) {
			throw onPurpose();
		}
	}

	static class FrameworkWiring implements org.osgi.framework.wiring.FrameworkWiring {
		@Override
		public void refreshBundles(
				Collection<org.osgi.framework.Bundle> bundles, FrameworkListener... listeners) {
			throw onPurpose();
		}

		@Override
		public boolean resolveBundles(Collection<org.osgi.framework.Bundle> bundles) {
			throw onPurpose();
		}

		@Override
		public Collection<org.osgi.framework.Bundle> getRemovalPendingBundles() {
			throw onPurpose();
		}

		@Override
		public Collection<org.osgi.framework.Bundle> getDependencyClosure(
				Collection<org.osgi.framework.Bundle> bundles) {
			throw onPurpose();
		}

		@Override
		public Collection<org.osgi.framework.wiring.BundleCapability> findProviders(
				Requirement requirement) {
			throw onPurpose();
		}

		@Override
		public org.osgi.framework.Bundle getBundle() {
			throw onPurpose();
		}
	}

	static class BundleCapability implements org.osgi.framework.wiring.BundleCapability {
		@Override
		public org.osgi.framework.wiring.BundleRevision getRevision() {
			throw onPurpose();
		}

		@Override
		public String getNamespace() {
			throw onPurpose();
		}

		@Override
		public Map<String, String> getDirectives() {
			throw onPurpose();
		}

		@Override
		public Map<String, Object> getAttributes() {
			throw onPurpose();
		}

		@Override
		public org.osgi.framework.wiring.BundleRevision getResource() {
			throw onPurpose();
		}
	}

	static class BundleRevision implements org.osgi.framework.wiring.BundleRevision {
		@Override
		public String getSymbolicName() {
			throw onPurpose();
		}

		@Override
		public Version getVersion() {
			throw onPurpose();
		}

		@Override
		public List<org.osgi.framework.wiring.BundleCapability> getDeclaredCapabilities(
				String namespace) {
			throw onPurpose();
		}

		@Override
		public List<BundleRequirement> getDeclaredRequirements(String namespace) {
			throw onPurpose();
		}

		@Override
		public int getTypes() {
			throw onPurpose();
		}

		@Override
		public org.osgi.framework.wiring.BundleWiring getWiring() {
			throw onPurpose();
		}

		@Override
		public List<Capability> getCapabilities(String namespace) {
			throw onPurpose();
		}

		@Override
		public List<Requirement> getRequirements(String namespace) {
			throw onPurpose();
		}

		@Override
		public org.osgi.framework.Bundle getBundle() {
			throw onPurpose();
		}
	}

	static class Location implements org.eclipse.osgi.service.datalocation.Location {
		@Override
		public boolean allowsDefault() {
			throw onPurpose();
		}

		@Override
		public URL getDefault() {
			throw onPurpose();
		}

		@Override
		public org.eclipse.osgi.service.datalocation.Location getParentLocation() {
			throw onPurpose();
		}

		@Override
		public URL getURL() {
			throw onPurpose();
		}

		@Override
		public boolean isSet() {
			throw onPurpose();
		}

		@Override
		public boolean isReadOnly() {
			throw onPurpose();
		}

		@Override
		public boolean setURL(URL value, boolean lock) throws IllegalStateException {
			throw onPurpose();
		}

		@Override
		public boolean set(URL value, boolean lock) throws IllegalStateException {
			throw onPurpose();
		}

		@Override
		public boolean set(URL value, boolean lock, String lockFilePath) throws IllegalStateException {
			throw onPurpose();
		}

		@Override
		public boolean lock() {
			throw onPurpose();
		}

		@Override
		public void release() {
			throw onPurpose();
		}

		@Override
		public boolean isLocked() {
			throw onPurpose();
		}

		@Override
		public org.eclipse.osgi.service.datalocation.Location createLocation(
				org.eclipse.osgi.service.datalocation.Location parent, URL defaultValue, boolean readonly) {
			throw onPurpose();
		}

		@Override
		public URL getDataArea(String path) throws IOException {
			throw onPurpose();
		}
	}

	static class BundleStartLevel implements org.osgi.framework.startlevel.BundleStartLevel {
		@Override
		public boolean isActivationPolicyUsed() {
			throw onPurpose();
		}

		@Override
		public int getStartLevel() {
			throw onPurpose();
		}

		@Override
		public void setStartLevel(int startlevel) {
			throw onPurpose();
		}

		@Override
		public boolean isPersistentlyStarted() {
			throw onPurpose();
		}

		@Override
		public org.osgi.framework.Bundle getBundle() {
			throw onPurpose();
		}
	}

	static class BundleWiring implements org.osgi.framework.wiring.BundleWiring {
		@Override
		public List<BundleWire> getRequiredWires(String namespace) {
			throw onPurpose();
		}

		@Override
		public boolean isInUse() {
			throw onPurpose();
		}

		@Override
		public List<BundleWire> getProvidedWires(String namespace) {
			throw onPurpose();
		}

		@Override
		public boolean isCurrent() {
			throw onPurpose();
		}

		@Override
		public List<org.osgi.framework.wiring.BundleCapability> getCapabilities(String namespace) {
			throw onPurpose();
		}

		@Override
		public List<BundleRequirement> getRequirements(String namespace) {
			throw onPurpose();
		}

		@Override
		public org.osgi.framework.wiring.BundleRevision getRevision() {
			throw onPurpose();
		}

		@Override
		public ClassLoader getClassLoader() {
			throw onPurpose();
		}

		@Override
		public List<URL> findEntries(String path, String filePattern, int options) {
			throw onPurpose();
		}

		@Override
		public Collection<String> listResources(String path, String filePattern, int options) {
			throw onPurpose();
		}

		@Override
		public List<Capability> getResourceCapabilities(String namespace) {
			throw onPurpose();
		}

		@Override
		public List<Requirement> getResourceRequirements(String namespace) {
			throw onPurpose();
		}

		@Override
		public List<Wire> getProvidedResourceWires(String namespace) {
			throw onPurpose();
		}

		@Override
		public List<Wire> getRequiredResourceWires(String namespace) {
			throw onPurpose();
		}

		@Override
		public org.osgi.framework.wiring.BundleRevision getResource() {
			throw onPurpose();
		}

		@Override
		public org.osgi.framework.Bundle getBundle() {
			throw onPurpose();
		}
	}
}
