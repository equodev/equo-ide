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
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
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

/** Performs network requests and parsing against a P2 repository, aided by caching. */
public class P2Client implements AutoCloseable {
	private final Cache metadataResponseCache;
	private final OkHttpClient metadataClient;

	/** The various caching modes that {@link P2Client} supports. */
	public enum Caching {
		OFFLINE,
		PREFER_OFFLINE,
		ALLOW_OFFLINE,
		NO_METADATA_CACHE;

		boolean tryOfflineFirst() {
			return this == OFFLINE || this == PREFER_OFFLINE;
		}

		boolean networkAllowed() {
			return this != OFFLINE;
		}

		boolean cacheAllowed() {
			return this != NO_METADATA_CACHE;
		}

		public static Caching defaultIfOfflineIs(boolean isOffline) {
			return isOffline ? Caching.OFFLINE : Caching.ALLOW_OFFLINE;
		}
	}

	private final Caching cachingPolicy;
	private final OfflineCache offlineMetadataCache;
	private final JarCache jarCache;
	private final LockFile lock;

	public P2Client() throws IOException {
		this(Caching.ALLOW_OFFLINE);
	}

	public P2Client(Caching cachingPolicy) throws IOException {
		this.cachingPolicy = cachingPolicy;
		this.jarCache = new JarCache(cachingPolicy);
		long maxSize = 50L * 1024L * 1024L; // 50 MiB
		File p2metadata = CacheLocations.p2metadata();
		if (cachingPolicy.cacheAllowed()) {
			metadataResponseCache = new Cache(new File(p2metadata, "connection"), maxSize);
			metadataClient = new OkHttpClient.Builder().cache(metadataResponseCache).build();
		} else {
			metadataResponseCache = null;
			metadataClient = new OkHttpClient.Builder().build();
		}
		offlineMetadataCache = new OfflineCache(new File(p2metadata, "offline"));
		lock = new LockFile(p2metadata);
	}

	public File download(P2Unit unit) throws IOException {
		return jarCache.download(unit);
	}

	@Override
	public void close() throws IOException {
		if (metadataResponseCache != null) {
			metadataResponseCache.close();
		}
		lock.close();
	}

	private static final String CONTENT_XML = "content.xml";

	void addUnits(P2Session session, String url) throws Exception {
		Unchecked.ThrowingConsumer<Folder> addUnits =
				(Folder root) -> {
					if (!root.metadataName.equals(CONTENT_XML)) {
						throw new IllegalArgumentException(
								"Expected endsWith /" + CONTENT_XML + " but was " + root.url + root.metadataName);
					}
					var contentXml = resolveXml(root.url, root.metadataName);
					parseContentXml(session, root, contentXml);
				};
		var queue = new ArrayDeque<Folder>();
		queue.push(new Folder(url));
		while (!queue.isEmpty()) {
			var dir = queue.pop();
			if (!dir.isComposite()) {
				addUnits.accept(new Folder(dir.url, dir.metadataName));
			} else {
				var children = parseComposite(resolveXml(dir.url, dir.metadataName));
				for (var child : children) {
					try {
						queue.push(new Folder(dir.url + child + "/"));
					} catch (NotFoundException e) {
						addUnits.accept(new Folder(dir.url + child + "/", CONTENT_XML));
					}
				}
			}
		}
	}

	private String getString(String url) throws IOException, NotFoundException {
		return new String(getBytes(url), StandardCharsets.UTF_8);
	}

	private byte[] getBytes(String url) throws IOException, NotFoundException {
		if (cachingPolicy.tryOfflineFirst()) {
			var cached = offlineMetadataCache.get(url);
			if (cached != null) {
				if (OfflineCache.is404(cached)) {
					throw new NotFoundException(url);
				}
				return cached;
			}
		}
		if (cachingPolicy.networkAllowed()) {
			var request = new Request.Builder().url(url).build();
			try (var response = metadataClient.newCall(request).execute()) {
				if (response.code() == 404) {
					if (cachingPolicy.cacheAllowed()) {
						offlineMetadataCache.put404(url);
					}
					throw new NotFoundException(url);
				} else {
					var bytes = response.body().bytes();
					if (cachingPolicy.cacheAllowed()) {
						offlineMetadataCache.put(url, bytes);
					}
					return bytes;
				}
			} catch (UnknownHostException e) {
				if (cachingPolicy.cacheAllowed()) {
					var cached = offlineMetadataCache.get(url);
					if (cached != null) {
						if (cachingPolicy.cacheAllowed()) {
							offlineMetadataCache.put404(url);
						}
						return cached;
					}
				}
				throw e;
			}
		}
		throw new IllegalStateException("P2Client is in offline mode but has no cache for " + url);
	}

	static class NotFoundException extends Exception {
		NotFoundException(String url) {
			super(url);
		}
	}

	private static final Pattern p2metadata =
			Pattern.compile("metadata\\.repository\\.factory\\.order\\s*=(.*),");

	private static String getGroup1(String content, Pattern pattern) {
		var matcher = pattern.matcher(content);
		matcher.find();
		return matcher.group(1);
	}

	class Folder {
		final String url;
		final String metadataName;

		Folder(String url) throws IOException, NotFoundException {
			if (!url.endsWith("/")) {
				throw new IllegalArgumentException("URL needs to end with /" + url);
			}
			this.url = url;
			var p2index = getString(url + "p2.index");
			String metadataTarget = getGroup1(p2index, p2metadata).trim();
			if (metadataTarget.indexOf(',') == -1) {
				this.metadataName = metadataTarget;
			} else {
				this.metadataName =
						Arrays.stream(metadataTarget.split(","))
								.map(String::trim)
								.filter(s -> s.endsWith(".xml"))
								.findFirst()
								.get();
			}
		}

		Folder(String url, String metadataTarget) {
			if (!url.endsWith("/")) {
				throw new IllegalArgumentException("URL needs to end with /" + url);
			}
			this.url = url;
			this.metadataName = metadataTarget;
		}

		private boolean isComposite() {
			return metadataName.startsWith("composite");
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

	private static void parseContentXml(P2Session session, Folder folder, String content)
			throws Exception {
		parseDocument(
				content,
				doc -> {
					var unitNodes =
							doc.getDocumentElement().getElementsByTagName("units").item(0).getChildNodes();
					for (int i = 0; i < unitNodes.getLength(); ++i) {
						Node node = unitNodes.item(i);
						if ("unit".equals(node.getNodeName())) {
							session.units.add(new P2Unit(session, folder, node));
						}
					}
					return null;
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
