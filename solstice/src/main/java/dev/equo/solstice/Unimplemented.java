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

import java.net.URL;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.osgi.framework.FrameworkListener;
import org.osgi.framework.Version;
import org.osgi.framework.wiring.BundleRequirement;
import org.osgi.resource.Capability;
import org.osgi.resource.Requirement;
import org.osgi.resource.Wire;
import org.osgi.service.packageadmin.ExportedPackage;
import org.osgi.service.packageadmin.RequiredBundle;

@SuppressWarnings("deprecation")
class Unimplemented {
	static RuntimeException onPurpose() {
		return onPurpose("Solstice believes that this method is not actually needed.");
	}

	static RuntimeException onPurpose(String reason) {
		return new UnsupportedOperationException(reason);
		//		try {
		//			throw new UnsupportedOperationException(reason);
		//		} catch (UnsupportedOperationException e) {
		//			e.printStackTrace();
		//			System.exit(1);
		//			return e;
		//		}
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
		public List<org.osgi.framework.wiring.BundleWire> getRequiredWires(String namespace) {
			throw onPurpose();
		}

		@Override
		public boolean isInUse() {
			throw onPurpose();
		}

		@Override
		public List<org.osgi.framework.wiring.BundleWire> getProvidedWires(String namespace) {
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

	interface FrameworkStartLevel extends org.osgi.framework.startlevel.FrameworkStartLevel {
		@Override
		default int getStartLevel() {
			throw onPurpose();
		}

		@Override
		default void setStartLevel(int startlevel, FrameworkListener... listeners) {
			throw onPurpose();
		}

		@Override
		default int getInitialBundleStartLevel() {
			throw onPurpose();
		}

		@Override
		default void setInitialBundleStartLevel(int startlevel) {
			throw onPurpose();
		}

		@Override
		default org.osgi.framework.Bundle getBundle() {
			throw onPurpose();
		}
	}

	static class BundleWire implements org.osgi.framework.wiring.BundleWire {
		@Override
		public org.osgi.framework.wiring.BundleCapability getCapability() {
			throw onPurpose();
		}

		@Override
		public BundleRequirement getRequirement() {
			throw onPurpose();
		}

		@Override
		public org.osgi.framework.wiring.BundleWiring getProviderWiring() {
			throw onPurpose();
		}

		@Override
		public org.osgi.framework.wiring.BundleWiring getRequirerWiring() {
			throw onPurpose();
		}

		@Override
		public org.osgi.framework.wiring.BundleRevision getProvider() {
			throw onPurpose();
		}

		@Override
		public org.osgi.framework.wiring.BundleRevision getRequirer() {
			throw onPurpose();
		}
	}
}
