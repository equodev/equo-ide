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
package dev.equo.solstice.p2;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.osgi.framework.Version;
import org.tukaani.xz.XZInputStream;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

public class P2Client implements AutoCloseable {
	private final File cacheDir;
	private final Cache cache;
	private final OkHttpClient client;

	P2Client(File cacheDir) {
		this.cacheDir = cacheDir;
		long maxSize = 50L * 1024L * 1024L; // 50 MiB
		cache = new Cache(cacheDir, maxSize);
		client = new OkHttpClient.Builder().cache(cache).build();
	}

	public Dir open(String url) throws IOException, NotFoundException {
		return new Dir(url);
	}

	private String getString(String url) throws IOException, NotFoundException {
		var request = new Request.Builder().url(url).build();
		try (var response = client.newCall(request).execute()) {
			if (response.code() == 404) {
				throw new NotFoundException();
			}
			return response.body().string();
		}
	}

	private byte[] getBytes(String url) throws IOException, NotFoundException {
		var request = new Request.Builder().url(url).build();
		try (var response = client.newCall(request).execute()) {
			if (response.code() == 404) {
				throw new NotFoundException();
			}
			return response.body().bytes();
		}
	}

	static class NotFoundException extends Exception {}

	private static final Pattern p2metadata =
			Pattern.compile("metadata\\.repository\\.factory\\.order=(.*),");
	private static final Pattern p2artifact =
			Pattern.compile("artifact\\.repository\\.factory\\.order=(.*),");

	private static String getGroup1(String content, Pattern pattern) {
		var matcher = pattern.matcher(content);
		matcher.find();
		return matcher.group(1);
	}

	public class Dir {
		Dir(String url) throws IOException, NotFoundException {
			if (!url.endsWith("/")) {
				throw new IllegalArgumentException("URL needs to end with /" + url);
			}
			var p2index = getString(url + "p2.index");
			var metadata = getGroup1(p2index, p2metadata);
			var artifact = getGroup1(p2index, p2artifact);
			System.out.println("~~~~~~~~");
			System.out.println("METADATA");
			System.out.println("~~~~~~~~");
			System.out.println(getContent(url, metadata));

			System.out.println("");
			System.out.println("~~~~~~~~");
			System.out.println("ARTIFACT");
			System.out.println("~~~~~~~~");
			System.out.println(getContent(url, artifact));
		}

		private String getContent(String root, String target) throws IOException {
			if (!target.endsWith(".xml")) {
				throw new IllegalArgumentException("Expected to end with .xml, was " + target);
			}

			var xzUrl = root + target + ".xz";
			var jarUrl = root + target.substring(0, target.length() - ".xml".length()) + ".jar";

			try {
				var bytes = getBytes(xzUrl);
				try (var stream = new XZInputStream(new ByteArrayInputStream(bytes))) {
					return new String(stream.readAllBytes(), StandardCharsets.UTF_8);
				}
			} catch (NotFoundException e) {
				// no problem, just keep trying
			}

			try {
				var bytes = getBytes(jarUrl);
				try (var zipStream = new ZipInputStream(new ByteArrayInputStream(bytes))) {
					ZipEntry entry = zipStream.getNextEntry();
					if (entry.getName().equals(target)) {
						return new String(zipStream.readAllBytes(), StandardCharsets.UTF_8);
					} else {
						throw new IllegalArgumentException(
								"Expected to find " + target + " but was " + entry.getName());
					}
				}
			} catch (NotFoundException e) {
				// no problem, just tell what we tried
			}
			throw new IllegalArgumentException("Could not find content at\n" + xzUrl + "\n" + jarUrl);
		}
	}

	@Override
	public void close() throws IOException {
		cache.close();
	}

	private static Map<String, String> parseFromFile(
			File artifactsJar, Function<String, String> keyExtractor) throws IOException {
		//        Box.Nullable<Map<String, String>> value = Box.Nullable.ofNull();
		//        ZipMisc.read(artifactsJar, "artifacts.xml", input -> {
		//            value.set(Errors.rethrow().get(() -> parse(input, keyExtractor)));
		//        });
		//        return Objects.requireNonNull(value.get());
		throw new IllegalArgumentException();
	}

	static Map<String, String> parse(InputStream inputStream, Function<String, String> keyExtractor)
			throws ParserConfigurationException, SAXException, IOException {
		Map<String, String> map = new HashMap<>();
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document doc = db.parse(inputStream);
		Node artifacts = doc.getDocumentElement().getElementsByTagName("artifacts").item(0);
		for (int i = 0; i < artifacts.getChildNodes().getLength(); ++i) {
			Node artifact = artifacts.getChildNodes().item(i);
			if ("artifact".equals(artifact.getNodeName())) {
				String classifier = artifact.getAttributes().getNamedItem("classifier").getNodeValue();
				if ("osgi.bundle".equals(classifier)) {
					String bundleId = artifact.getAttributes().getNamedItem("id").getNodeValue();
					String bundleVersion = artifact.getAttributes().getNamedItem("version").getNodeValue();
					String key = keyExtractor.apply(bundleId);
					String version = calculateMavenCentralVersion(bundleId, bundleVersion);
					map.put(key, version);
				}
			}
		}
		return map;
	}

	static String calculateMavenCentralVersion(String bundleId, String bundleVersion) {
		Version parsed = Version.parseVersion(bundleVersion);
		if (ICU_BUNDLE_ID.equals(bundleId) && parsed.getMicro() == 0) {
			return parsed.getMajor() + "." + parsed.getMinor();
		} else {
			return parsed.getMajor() + "." + parsed.getMinor() + "." + parsed.getMicro();
		}
	}

	private static final String ICU_BUNDLE_ID = "com.ibm.icu";
}
