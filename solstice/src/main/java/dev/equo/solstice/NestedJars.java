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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.JarURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;
import org.osgi.framework.Constants;

/**
 * Unwraps nested bundles to be friendly to a normal classloader, see <a
 * href="https://github.com/equodev/equo-ide/pull/7">equodev/equo-ide#7</a>
 *
 * <p>Known limitation: if the nested bundle has no {@code META-INF/MANIFEST.MF}, then {@link
 * #confirmAllNestedJarsArePresentOnClasspath(File)} will fail erroneously.
 */
public abstract class NestedJars {
	/** Reads the version of the Solstice jar from the classpath. */
	public static String solsticeVersion() throws IOException {
		var solsticeJar =
				NestedJars.class.getResource(NestedJars.class.getSimpleName() + ".class").toString();
		if (!solsticeJar.startsWith("jar")) {
			throw new IllegalArgumentException("");
		}
		var url = new URL(solsticeJar);
		var jarConnection = (JarURLConnection) url.openConnection();
		var manifest = jarConnection.getManifest();
		return manifest.getMainAttributes().getValue("Implementation-Version");
	}

	public static final String DIR = "nested-jars";
	private static final Attributes.Name CLASSPATH = new Attributes.Name(Constants.BUNDLE_CLASSPATH);

	private static void addNestedJarsFromManifest(
			List<URL> nestedJars, String jarUrl, InputStream stream) throws IOException {
		Manifest manifest = new Manifest(stream);
		var cp = manifest.getMainAttributes().getValue(CLASSPATH);
		if (cp != null && !".".equals(cp)) {
			var lines = cp.split(",");
			for (var line : lines) {
				var nestedJar = Unchecked.get(() -> new URL(jarUrl + "/" + line));
				nestedJars.add(nestedJar);
			}
		}
	}

	public static NestedJars onClassPath() {
		return new NestedJars() {
			@Override
			protected List<URL> listNestedJars() {
				List<URL> nestedJars = new ArrayList<>();
				Enumeration<URL> manifests =
						Unchecked.get(
								() ->
										NestedJars.class
												.getClassLoader()
												.getResources(Solstice.MANIFEST_PATH.substring(1)));
				while (manifests.hasMoreElements()) {
					var manifestUrl = manifests.nextElement();
					var fullUrl = manifestUrl.toExternalForm();
					var jarUrl = fullUrl.substring(0, fullUrl.length() - Solstice.MANIFEST_PATH.length());
					try (InputStream stream = manifestUrl.openStream()) {
						addNestedJarsFromManifest(nestedJars, jarUrl, stream);
					} catch (IOException e) {
						throw Unchecked.wrap(e);
					}
				}
				return nestedJars;
			}
		};
	}

	public static NestedJars inFiles(Iterable<File> files) {
		return new NestedJars() {
			@Override
			protected List<URL> listNestedJars() {
				List<URL> nestedJars = new ArrayList<>();
				for (File file : files) {
					try (var jarFile = new JarFile(file)) {
						var zipEntry = jarFile.getEntry(Solstice.MANIFEST_PATH.substring(1));
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

	private static final String JAR_COLON_FILE_COLON = "jar:file:";

	public void confirmAllNestedJarsArePresentOnClasspath(File nestedJarFolder) {
		var entries = extractAllNestedJars(nestedJarFolder);
		Enumeration<URL> manifests =
				Unchecked.get(
						() ->
								NestedJars.class
										.getClassLoader()
										.getResources(Solstice.MANIFEST_PATH.substring(1)));
		while (manifests.hasMoreElements()) {
			var fullUrl = manifests.nextElement().toExternalForm();
			var jarUrl = fullUrl.substring(0, fullUrl.length() - Solstice.MANIFEST_PATH.length());
			if (!jarUrl.endsWith("!")) {
				throw new IllegalArgumentException("Expected " + jarUrl + " to end with !");
			}
			if (!jarUrl.startsWith(JAR_COLON_FILE_COLON)) {
				throw new IllegalArgumentException(
						"Expected " + jarUrl + " to start with " + JAR_COLON_FILE_COLON);
			}
			var jarFile =
					new File(
							jarUrl.substring(
									JAR_COLON_FILE_COLON.length(), jarUrl.length() - 1)); // -1 removes the !
			entries.removeIf(e -> e.getValue().equals(jarFile));
		}

		if (!entries.isEmpty()) {
			var msg = new StringBuilder();
			msg.append("The following nested jars are missing:\n");
			for (var entry : entries) {
				msg.append("  ");
				msg.append(entry.getKey().toExternalForm());
				msg.append('\n');
			}
			throw new IllegalStateException(msg.toString());
		}
	}

	private static Map.Entry<URL, File> extractNestedJar(
			String parentJar, URL entry, File nestedJarFolder) {
		try (var toRead = entry.openStream()) {
			var content = toRead.readAllBytes();

			var md5 = MessageDigest.getInstance("MD5");
			md5.update(content);

			var jarPath = entry.getPath();
			var lastSep = Math.max(jarPath.lastIndexOf('!'), jarPath.lastIndexOf('/'));
			var jarSimpleName = jarPath.substring(lastSep + 1);

			var filename = parentJar + "__" + jarSimpleName + "__" + bytesToHex(md5.digest()) + ".jar";
			var jarToAdd = new File(nestedJarFolder, filename);
			if (!jarToAdd.exists() || jarToAdd.length() != content.length) {
				nestedJarFolder.mkdirs();
				try (var output = new FileOutputStream(jarToAdd)) {
					output.write(content);
				}
			}
			return Map.entry(entry, jarToAdd);
		} catch (IOException | NoSuchAlgorithmException e) {
			throw Unchecked.wrap(e);
		}
	}

	private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();

	private static String bytesToHex(byte[] bytes) {
		char[] hexChars = new char[bytes.length * 2];
		for (int j = 0; j < bytes.length; j++) {
			int v = bytes[j] & 0xFF;
			hexChars[j * 2] = HEX_ARRAY[v >>> 4];
			hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
		}
		return new String(hexChars);
	}
}
