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
import java.nio.charset.StandardCharsets;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import javax.xml.parsers.DocumentBuilderFactory;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.tukaani.xz.XZInputStream;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

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

	public DeepDir open(P2Session session, String url) throws Exception {
		return new DeepDir(session, url);
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
		List<Unit> units = new ArrayList<>();

		DeepDir(P2Session session, String rootUrl) throws Exception {
			List<String> contentXmlUrls = new ArrayList<>();
			var queue = new ArrayDeque<Dir>();
			queue.push(new Dir(rootUrl));
			while (!queue.isEmpty()) {
				var dir = queue.pop();
				if (!dir.isComposite()) {
					contentXmlUrls.add(dir.url + dir.metadataTarget);
				} else {
					var children = parseComposite(resolveXml(dir.url, dir.metadataTarget));
					for (var child : children) {
						try {
							queue.push(new Dir(dir.url + child + "/"));
						} catch (NotFoundException e) {
							contentXmlUrls.add(dir.url + child + "/content.xml");
						}
					}
				}
			}

			for (var content : contentXmlUrls) {
				if (!content.endsWith("/" + CONTENT_XML)) {
					throw new IllegalArgumentException(
							"Expected " + content + " to ends with /" + CONTENT_XML);
				}
				int splitPoint = content.length() - CONTENT_XML.length();
				var contentXml =
						resolveXml(content.substring(0, splitPoint), content.substring(splitPoint));
				units.addAll(parseContentXml(session, contentXml));
			}
			units.sort(Comparator.comparing(unit -> unit.id));
			for (var unit : units) {
				System.out.println(unit);
			}
		}
	}

	private static final String CONTENT_XML = "content.xml";

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
	}

	private String resolveXml(String url, String metadataTarget) throws IOException {
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

	@Override
	public void close() throws IOException {
		cache.close();
	}

	private static List<String> parseComposite(String content) throws Exception {
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

	private static List<Unit> parseContentXml(P2Session session, String content) throws Exception {
		return parseDocument(
				content,
				doc -> {
					var units = new ArrayList<Unit>();
					var unitNodes =
							doc.getDocumentElement().getElementsByTagName("units").item(0).getChildNodes();
					for (int i = 0; i < unitNodes.getLength(); ++i) {
						Node node = unitNodes.item(i);
						if ("unit".equals(node.getNodeName())) {
							units.add(new Unit(session, node));
						}
					}
					return units;
				});
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
