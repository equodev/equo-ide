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

import dev.equo.solstice.p2.CacheLocations;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.JarURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;
import javax.annotation.Nullable;
import org.slf4j.LoggerFactory;

/**
 * Unwraps nested bundles to be friendly to a normal classloader, see <a
 * href="https://github.com/equodev/equo-ide/pull/7">equodev/equo-ide#7</a>
 */
public abstract class NestedJars {
	/** Reads the version of the Solstice jar from the classpath. */
	public static String solsticeVersion() {
		var solsticeJar =
				NestedJars.class.getResource(NestedJars.class.getSimpleName() + ".class").toString();
		if (!solsticeJar.startsWith("jar")) {
			throw new IllegalArgumentException("");
		}
		try {
			var url = new URL(solsticeJar);
			var jarConnection = (JarURLConnection) url.openConnection();
			var manifest = jarConnection.getManifest();
			return manifest.getMainAttributes().getValue("Implementation-Version");
		} catch (IOException e) {
			throw Unchecked.wrap(e);
		}
	}

	/**
	 * Returns the full maven coordinates of Solstice's transitive dependencies in
	 * `group:artifact:version` form.
	 */
	public static Collection<String> transitiveDeps(boolean useAtomos, CoordFormat format) {
		var coords = new ArrayList<String>();
		String VER_SLF4J = "1.7.36";
		coords.add(format.format("org.slf4j", "slf4j-api", VER_SLF4J, null));
		coords.add(format.format("org.slf4j", "slf4j-simple", VER_SLF4J, null));
		if (useAtomos) {
			coords.add(format.format("org.apache.felix", "org.apache.felix.atomos", "1.0.0", null));
			coords.add(format.format("org.apache.felix.atomos", "osgi.core", "8.0.0", "AtomosEquinox"));
		}
		return coords;
	}

	public enum CoordFormat {
		GRADLE,
		MAVEN;

		private String format(String g, String a, String version, @Nullable String c) {
			if (c == null) {
				return g + ":" + a + ":" + version;
			} else {
				if (this == GRADLE) {
					return g + ":" + a + ":" + version + ":" + c;
				} else if (this == MAVEN) {
					return g + ":" + a + ":jar:" + c + ":" + version;
				} else {
					throw new IllegalArgumentException("Unknown format " + this);
				}
			}
		}
	}

	public static final String DIR = "nested-jars";
	private static final Attributes.Name CLASSPATH = new Attributes.Name("Bundle-ClassPath");

	private static void addNestedJarsFromManifest(
			List<URL> nestedJars, String jarUrl, InputStream stream) throws IOException {
		Manifest manifest = new Manifest(stream);
		var cp = manifest.getMainAttributes().getValue(CLASSPATH);
		if (cp != null) {
			var lines = cp.split(",");
			for (var lineRaw : lines) {
				var line = lineRaw.trim();
				if (line.startsWith("../")) {
					LoggerFactory.getLogger(NestedJars.class)
							.warn("Ignoring unexpected nested jar " + line + " inside " + jarUrl);
				} else if (!line.equals(".")) {
					var nestedJar = Unchecked.get(() -> new URL(jarUrl + "/" + line));
					nestedJars.add(nestedJar);
				}
			}
		}
	}

	public static OnClassPath onClassPath() {
		if (onClassPath == null) {
			onClassPath = new OnClassPath();
		}
		return onClassPath;
	}

	private static OnClassPath onClassPath;

	public static class OnClassPath extends NestedJars {
		private OnClassPath() {}

		@Override
		protected List<URL> listNestedJars() {
			var nestedJars = new ArrayList<URL>();
			Enumeration<URL> manifests =
					Unchecked.get(
							() -> NestedJars.class.getClassLoader().getResources(SolsticeManifest.MANIFEST_PATH));
			while (manifests.hasMoreElements()) {
				var manifestUrl = manifests.nextElement();
				var fullUrl = manifestUrl.toExternalForm();
				var jarUrl =
						fullUrl.substring(0, fullUrl.length() - SolsticeManifest.SLASH_MANIFEST_PATH.length());
				try (InputStream stream = manifestUrl.openStream()) {
					addNestedJarsFromManifest(nestedJars, jarUrl, stream);
				} catch (IOException e) {
					throw Unchecked.wrap(e);
				}
			}
			return nestedJars;
		}

		private static final String JAR_COLON_FILE_COLON = "jar:file:";
		private Set<File> nestedJarsOnClasspath;

		public void confirmAllNestedJarsArePresentOnClasspath(File nestedJarFolder) {
			nestedJarsOnClasspath = new HashSet<>();
			var entries = extractAllNestedJars(nestedJarFolder);
			entries.removeIf(
					entry -> {
						if (entry.getValue().exists()) {
							try (var jarFile = new JarFile(entry.getValue())) {
								var firstResource = jarFile.entries().nextElement().getName();
								var onTheClasspath = NestedJars.class.getClassLoader().getResources(firstResource);
								while (onTheClasspath.hasMoreElements()) {
									var url = onTheClasspath.nextElement().toExternalForm();
									if (url.startsWith(
											JAR_COLON_FILE_COLON + entry.getValue().getAbsolutePath() + "!")) {
										nestedJarsOnClasspath.add(entry.getValue());
										return true;
									}
								}
							} catch (IOException e) {
								throw Unchecked.wrap(e);
							}
						}
						return false;
					});
			if (!entries.isEmpty()) {
				var msg = new StringBuilder();
				msg.append(
						"The following nested jars are missing from "
								+ nestedJarFolder.getAbsolutePath()
								+ "\n");
				for (var entry : entries) {
					msg.append("  ");
					msg.append(entry.getKey().toExternalForm());
					msg.append('\n');
				}
				if (warnOnly) {
					System.err.println(msg);
				} else {
					throw new IllegalStateException(msg.toString());
				}
			}
		}

		public boolean isNestedJar(SolsticeManifest manifest) {
			if (nestedJarsOnClasspath == null) {
				throw new IllegalStateException(
						"You must call `confirmAllNestedJarsArePresentOnClasspath` first.");
			}
			if (manifest.getJarUrl().startsWith(JAR_COLON_FILE_COLON)
					&& manifest.getJarUrl().endsWith("!")) {
				var innerUrl =
						manifest
								.getJarUrl()
								.substring(JAR_COLON_FILE_COLON.length(), manifest.getJarUrl().length() - 1);
				return nestedJarsOnClasspath.contains(new File(innerUrl));
			}
			return false;
		}
	}

	public static NestedJars inFiles(Iterable<File> files) {
		return new NestedJars() {
			@Override
			protected List<URL> listNestedJars() {
				List<URL> nestedJars = new ArrayList<>();
				for (File file : files) {
					try (var jarFile = new JarFile(file)) {
						var zipEntry = jarFile.getEntry(SolsticeManifest.MANIFEST_PATH);
						if (zipEntry != null) {
							var jarUrl = "jar:" + file.toURI().toURL().toExternalForm() + "!";
							try (var input = jarFile.getInputStream(zipEntry)) {
								addNestedJarsFromManifest(nestedJars, jarUrl, input);
							}
						}
					} catch (IOException e) {
						throw Unchecked.wrap(e);
					}
				}
				return nestedJars;
			}
		};
	}

	protected abstract List<URL> listNestedJars();

	/** Extracts nested jars into {@link dev.equo.solstice.p2.CacheLocations#nestedJars()}. */
	public List<Map.Entry<URL, File>> extractAllNestedJars() {
		return extractAllNestedJars(CacheLocations.nestedJars());
	}

	public List<Map.Entry<URL, File>> extractAllNestedJars(File nestedJarFolder) {
		var files = new ArrayList<Map.Entry<URL, File>>();
		for (var url : listNestedJars()) {
			int lastExclamation = url.getPath().indexOf('!');
			int slashBeforeThat = url.getPath().lastIndexOf('/', lastExclamation);
			files.add(
					extractNestedJar(
							url.getPath().substring(slashBeforeThat + 1, lastExclamation), url, nestedJarFolder));
		}
		files.sort(Comparator.comparing(e -> e.getKey().getPath()));
		return files;
	}

	private static boolean warnOnly = false;

	public static void setToWarnOnly() {
		warnOnly = true;
	}

	private static Map.Entry<URL, File> extractNestedJar(
			String parentJar, URL entry, File nestedJarFolder) {
		try (var toRead = entry.openStream()) {
			var content = toRead.readAllBytes();

			var jarPath = entry.getPath();
			var lastSep = Math.max(jarPath.lastIndexOf('!'), jarPath.lastIndexOf('/'));
			var jarSimpleName = jarPath.substring(lastSep + 1);

			var filename = parentJar + "__" + jarSimpleName + "__" + filenameSafeHash(content) + ".jar";
			var jarToAdd = new File(nestedJarFolder, filename);
			if (!jarToAdd.exists() || jarToAdd.length() != content.length) {
				nestedJarFolder.mkdirs();
				try (var output = new FileOutputStream(jarToAdd)) {
					output.write(content);
				}
			}
			return Map.entry(entry, jarToAdd);
		} catch (IOException e) {
			throw Unchecked.wrap(e);
		}
	}

	static String filenameSafeHash(String content) {
		return filenameSafeHash(content.getBytes(StandardCharsets.UTF_8));
	}

	static String filenameSafeHash(byte[] content) {
		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			md5.update(content);
			String encoded =
					new String(Base64.getEncoder().encode(md5.digest()), StandardCharsets.US_ASCII);
			return encoded.replace('/', '-').replace('=', '-');
		} catch (NoSuchAlgorithmException e) {
			throw Unchecked.wrap(e);
		}
	}
}
