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
import java.lang.reflect.Constructor;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import javax.annotation.Nullable;
import org.jetbrains.annotations.NotNull;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.Constants;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.Version;
import org.osgi.framework.startlevel.BundleStartLevel;
import org.osgi.framework.startlevel.FrameworkStartLevel;
import org.osgi.framework.wiring.BundleRevision;
import org.osgi.framework.wiring.BundleWiring;
import org.osgi.framework.wiring.FrameworkWiring;
import org.osgi.service.packageadmin.PackageAdmin;

public class ShimBundle implements Bundle {
	final long bundleId;
	final SolsticeManifest manifest;
	final @Nullable String activator;
	final Hashtable<String, String> headers = new Hashtable<>();
	private final BundleContextDelegate context;

	ShimBundle(long bundleId, BundleContextShim context, SolsticeManifest manifest) {
		this.bundleId = bundleId;
		this.context =
				new BundleContextDelegate(context) {
					@Override
					public org.osgi.framework.Bundle getBundle() {
						return ShimBundle.this;
					}
				};
		this.manifest = manifest;
		activator = manifest.getHeadersOriginal().get(Constants.BUNDLE_ACTIVATOR);
		manifest
				.getHeadersOriginal()
				.forEach(
						(key, value) -> {
							if (!Constants.IMPORT_PACKAGE.equals(key) && !Constants.EXPORT_PACKAGE.equals(key)) {
								headers.put(key, value);
							}
						});
	}

	@Override
	public BundleContext getBundleContext() {
		return context;
	}

	BundleContextShim getRootBundleContext() {
		return context.delegate;
	}

	// bundle stuff
	@Override
	public String toString() {
		return manifest.toString();
	}

	@Override
	public Class<?> loadClass(String name) throws ClassNotFoundException {
		return Class.forName(name);
	}

	//////////////////////////
	// BundleContext overrides
	//////////////////////////
	@Override
	public long getBundleId() {
		return bundleId;
	}

	@Override
	public Version getVersion() {
		return Version.emptyVersion;
	}

	@Override
	public void start(int options) {
		start();
	}

	@Override
	public void start() {
		activate();
	}

	@Override
	public void stop(int options) {
		stop();
	}

	///////////////////
	// Bundle overrides
	///////////////////
	int state = INSTALLED;

	private boolean activateHasBeenCalled = false;

	/** Returns false if the bundle cannot activate yet because of "requiresWorkbench" */
	private void activate() {
		if (activateHasBeenCalled) {
			return;
		}
		activateHasBeenCalled = true;
		state = STARTING;
		context.delegate.notifyBundleListeners(BundleEvent.STARTING, this);

		for (var cap : manifest.capProvides) {
			context.delegate.capabilities.put(cap, this);
		}

		if (!BundleContextShim.DONT_ACTIVATE.contains(getSymbolicName()) && activator != null) {
			try {
				var c = (Constructor<BundleActivator>) Class.forName(activator).getConstructor();
				var bundleActivator = c.newInstance();
				bundleActivator.start(context);
			} catch (Exception e) {
				context.delegate.logger.warn("Error in activator of " + getSymbolicName(), e);
			}
		}
		state = ACTIVE;
		context.delegate.notifyBundleListeners(BundleEvent.STARTED, this);
	}

	@Override
	public int getState() {
		return state;
	}

	@Override
	public Dictionary<String, String> getHeaders() {
		return getHeaders("");
	}

	@Override
	public Dictionary<String, String> getHeaders(String locale) {
		return headers;
	}

	@Override
	public String getSymbolicName() {
		return manifest.getSymbolicName();
	}

	private String stripLeadingSlash(String path) {
		if (path.startsWith("/")) {
			return path.substring(1);
		} else {
			return path;
		}
	}

	private String stripLeadingAddTrailingSlash(String path) {
		path = stripLeadingSlash(path);
		if (path.isEmpty() || path.equals("/")) {
			return "";
		} else {
			return path.endsWith("/") ? path : (path + "/");
		}
	}

	@Override
	public URL getEntry(String path) {
		try {
			ZipEntry entry = parseFromZip(zip -> zip.getEntry(stripLeadingSlash(path)));
			if (entry != null) {
				return new URL(manifest.getJarUrl() + "/" + stripLeadingSlash(path));
			}
			return null;
		} catch (MalformedURLException e) {
			throw Unchecked.wrap(e);
		}
	}

	private <T> T parseFromZip(Function<ZipFile, T> function) {
		String prefix = "jar:file:";
		if (!manifest.getJarUrl().startsWith(prefix)) {
			throw new IllegalArgumentException(
					"Must start with " + prefix + " was " + manifest.getJarUrl());
		}
		if (!manifest.getJarUrl().endsWith("!")) {
			throw new IllegalArgumentException("Must end with ! was " + manifest.getJarUrl());
		}
		try (var zipFile =
				new ZipFile(
						manifest.getJarUrl().substring(prefix.length(), manifest.getJarUrl().length() - 1))) {
			return function.apply(zipFile);
		} catch (IOException e) {
			throw Unchecked.wrap(e);
		}
	}

	@Override
	public URL getResource(String name) {
		ZipEntry entry = parseFromZip(zip -> zip.getEntry(stripLeadingSlash(name)));
		if (entry != null) {
			return getEntry(name);
		} else {
			return BundleContextShim.class.getClassLoader().getResource(name);
		}
	}

	@Override
	public Enumeration<URL> getResources(String name) throws IOException {
		return BundleContextShim.class.getClassLoader().getResources(name);
	}

	@Override
	public Enumeration<String> getEntryPaths(String path) {
		var pathFinal = stripLeadingAddTrailingSlash(path);
		return parseFromZip(
				zipFile -> {
					List<String> zipPaths = new ArrayList<>();
					var entries = zipFile.entries();
					while (entries.hasMoreElements()) {
						var entry = entries.nextElement();
						if (entry.getName().startsWith(pathFinal)) {
							zipPaths.add(entry.getName());
						}
					}
					return Collections.enumeration(zipPaths);
				});
	}

	@Override
	public Enumeration<URL> findEntries(String path, String filePattern, boolean recurse) {
		var urls = new ArrayList<URL>();
		findEntries(urls, path, filePattern, recurse);
		return Collections.enumeration(urls);
	}

	private void findEntries(List<URL> urls, String path, String filePattern, boolean recurse) {
		for (var fragments : manifest.fragments) {
			((ShimBundle) fragments.hydrated).findEntries(urls, path, filePattern, recurse);
		}
		var pathFinal = stripLeadingAddTrailingSlash(path);
		var pattern = Pattern.compile(filePattern.replace(".", "\\.").replace("*", ".*"));
		var pathsWithinZip =
				parseFromZip(
						zipFile -> {
							List<String> zipPaths = new ArrayList<>();
							var entries = zipFile.entries();
							while (entries.hasMoreElements()) {
								var entry = entries.nextElement();
								if (entry.getName().startsWith(pathFinal)) {
									var after = entry.getName().substring(pathFinal.length());
									int lastSlash = after.lastIndexOf('/');
									if (lastSlash == -1) {
										if (pattern.matcher(after).matches()) {
											zipPaths.add(entry.getName());
										}
									} else if (recurse) {
										var name = after.substring(lastSlash + 1);
										if (pattern.matcher(name).matches()) {
											zipPaths.add(entry.getName());
										}
									}
								}
							}
							return zipPaths;
						});
		try {
			for (var withinZip : pathsWithinZip) {
				urls.add(new URL(manifest.getJarUrl() + "/" + withinZip));
			}
		} catch (MalformedURLException e) {
			throw Unchecked.wrap(e);
		}
	}

	@Override
	public String getLocation() {
		return manifest.getJarUrl();
	}

	@Override
	public File getDataFile(String filename) {
		return context.delegate.storage.getDataFileBundle(this, filename);
	}

	// implemented for OSGi DS
	@Override
	public <A> A adapt(Class<A> type) {
		if (bundleId == 0) {
			if (type.equals(PackageAdmin.class)) {
				return (A) context.delegate.packageAdmin;
			} else if (type.equals(FrameworkWiring.class)) {
				return (A) context.delegate.frameworkWiring;
			} else if (type.equals(BundleRevision.class)) {
				return null;
			} else if (type.equals(FrameworkStartLevel.class)) {
				return (A) new Unimplemented.FrameworkStartLevel() {};
			} else {
				throw new UnsupportedOperationException(type.getName());
			}
		}
		if (BundleWiring.class.equals(type)) {
			return state == Bundle.INSTALLED ? null : (A) new ShimBundleWiring(this);
		} else if (BundleStartLevel.class.equals(type)) {
			return (A)
					new Unimplemented.BundleStartLevel() {
						@Override
						public boolean isActivationPolicyUsed() {
							return true;
						}
					};
		} else if (BundleRevision.class.equals(type)) {
			return (A) new ShimBundleRevision(this);
		} else {
			return null;
		}
	}

	//////////////////////////////
	// Unimplemented below here //
	//////////////////////////////
	@Override
	public Map<X509Certificate, List<X509Certificate>> getSignerCertificates(int signersType) {
		throw Unimplemented.onPurpose();
	}

	@Override
	public ServiceReference<?>[] getRegisteredServices() {
		throw Unimplemented.onPurpose();
	}

	@Override
	public ServiceReference<?>[] getServicesInUse() {
		throw Unimplemented.onPurpose();
	}

	@Override
	public boolean hasPermission(Object permission) {
		throw Unimplemented.onPurpose();
	}

	@Override
	public void stop() {
		throw Unimplemented.onPurpose();
	}

	@Override
	public void update(InputStream input) {
		throw Unimplemented.onPurpose();
	}

	@Override
	public void update() {
		throw Unimplemented.onPurpose();
	}

	@Override
	public void uninstall() {
		throw Unimplemented.onPurpose();
	}

	@Override
	public long getLastModified() {
		throw Unimplemented.onPurpose();
	}

	@Override
	public int compareTo(@NotNull Bundle o) {
		throw Unimplemented.onPurpose();
	}
}
