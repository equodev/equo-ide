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
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
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
import org.w3c.dom.NodeList;
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

	public DeepDir open(String url) throws Exception {
		return new DeepDir(url);
	}

	private String getString(String url) throws IOException, NotFoundException {
		var request = new Request.Builder().url(url).build();
		try (var response = client.newCall(request).execute()) {
			if (response.code() == 404) {
				throw new NotFoundException(url);
			}
			return response.body().string();
		}
	}

	private byte[] getBytes(String url) throws IOException, NotFoundException {
		var request = new Request.Builder().url(url).build();
		try (var response = client.newCall(request).execute()) {
			if (response.code() == 404) {
				throw new NotFoundException(url);
			}
			return response.body().bytes();
		}
	}

	static class NotFoundException extends Exception {
		NotFoundException(String url) {
			super(url);
		}
	}

	private static final Pattern p2metadata =
			Pattern.compile("metadata\\.repository\\.factory\\.order=(.*),");

	private static String getGroup1(String content, Pattern pattern) {
		var matcher = pattern.matcher(content);
		matcher.find();
		return matcher.group(1);
	}

	class DeepDir {
		private List<String> contentXmlUrls = new ArrayList<>();

		DeepDir(String rootUrl) throws Exception {
			var queue = new ArrayDeque<Dir>();
			queue.push(new Dir(rootUrl));
			while (!queue.isEmpty()) {
				var dir = queue.pop();
				if (!dir.isComposite()) {
					contentXmlUrls.add(dir.url + dir.metadataTarget);
				} else {
					var children = dir.parseComposite(dir.resolveXml());
					for (var child : children) {
						try {
							queue.push(new Dir(dir.url + child + "/"));
						} catch (NotFoundException e) {
							contentXmlUrls.add(dir.url + child + "/content.xml");
						}
					}
				}
			}
		}
	}

	class Dir {
		String url;
		String metadataTarget;

		Dir(String url) throws IOException, NotFoundException {
			this.url = url;
			if (!url.endsWith("/")) {
				throw new IllegalArgumentException("URL needs to end with /" + url);
			}
			var p2index = getString(url + "p2.index");
			String metadataTarget = getGroup1(p2index, p2metadata).trim();
			if (metadataTarget.indexOf(',') == -1) {
				this.metadataTarget = metadataTarget;
			} else {
				this.metadataTarget =
						Arrays.stream(metadataTarget.split(","))
								.map(String::trim)
								.filter(s -> s.endsWith(".xml"))
								.findFirst()
								.get();
			}
		}

		private boolean isComposite() {
			return metadataTarget.startsWith("composite");
		}

		private String resolveXml() throws IOException {
			if (!metadataTarget.endsWith(".xml")) {
				throw new IllegalArgumentException("Expected to end with .xml, was " + metadataTarget);
			}

			var xzUrl = url + metadataTarget + ".xz";
			var jarUrl =
					url + metadataTarget.substring(0, metadataTarget.length() - ".xml".length()) + ".jar";

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
					if (entry.getName().equals(metadataTarget)) {
						return new String(zipStream.readAllBytes(), StandardCharsets.UTF_8);
					} else {
						throw new IllegalArgumentException(
								"Expected to find " + metadataTarget + " but was " + entry.getName());
					}
				}
			} catch (NotFoundException e) {
				// no problem, just tell what we tried
			}
			throw new IllegalArgumentException(
					"Could not find " + metadataTarget + " at\n" + xzUrl + "\n" + jarUrl);
		}

		private List<String> parseComposite(String content) throws Exception {
			return parseDocument(
					content,
					doc -> {
						var childLocations = new ArrayList<String>();
						Node childrenNode = doc.getDocumentElement().getElementsByTagName("children").item(0);
						NodeList children = childrenNode.getChildNodes();
						for (int i = 0; i < children.getLength(); ++i) {
							Node node = children.item(i);
							if ("child".equals(node.getNodeName())) {
								childLocations.add(node.getAttributes().getNamedItem("location").getNodeValue());
							}
						}
						return childLocations;
					});
		}
	}

	@Override
	public void close() throws IOException {
		cache.close();
	}

	private static Map<String, String> parse(
			InputStream inputStream, Function<String, String> keyExtractor)
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
		if ("com.ibm.icu".equals(bundleId) && parsed.getMicro() == 0) {
			return parsed.getMajor() + "." + parsed.getMinor();
		} else {
			return parsed.getMajor() + "." + parsed.getMinor() + "." + parsed.getMicro();
		}
	}

	private static <T> T parseDocument(String content, Function<Document, T> parser)
			throws Exception {
		var dbf = DocumentBuilderFactory.newInstance();
		var db = dbf.newDocumentBuilder();
		var bytes = content.getBytes(StandardCharsets.UTF_8);
		try (var stream = new ByteArrayInputStream(bytes)) {
			var doc = db.parse(stream);
			return parser.apply(doc);
		} catch (Exception e) {
			System.err.println("/ERROR WHILE PARSING BELOW");
			System.err.println(content);
			System.err.println("\\ERROR WHILE PARSING ABOVE");
			throw e;
		}
	}
}
