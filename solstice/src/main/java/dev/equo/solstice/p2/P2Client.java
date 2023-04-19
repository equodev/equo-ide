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

	private final P2ClientCache cachingPolicy;
	private final OfflineCache offlineMetadataCache;
	private final JarCache jarCache;
	private final LockFile lock;

	public P2Client() throws IOException {
		this(P2ClientCache.PREFER_OFFLINE);
	}

	public P2Client(P2ClientCache cachingPolicy) throws IOException {
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
	private static final String COMPOSITE_XML = "compositeContent.xml";

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
					if (child.startsWith("https://") || child.startsWith("http://")) {
						queue.push(new Folder(child + "/"));
					} else {
						if (child.startsWith("file:")) {
							child = child.substring("file:".length());
						}
						queue.push(new Folder(dir.url + child + "/"));
					}
				}
			}
		}
	}

	private String getString(String url) throws IOException, NotFoundException {
		return new String(getBytes(url), StandardCharsets.UTF_8);
	}

	private static final byte[] DOCTYPE_HTML = "<!doctype html>".getBytes(StandardCharsets.UTF_8);

	private static boolean contentIsHtml(byte[] content) {
		if (content.length <= DOCTYPE_HTML.length) {
			return false;
		}
		for (int i = 0; i < DOCTYPE_HTML.length; ++i) {
			if (content[i] != DOCTYPE_HTML[i]) {
				return false;
			}
		}
		return true;
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
				if (response.code() != 404 && response.code() != 403) {
					var bytes = response.body().bytes();
					if (contentIsHtml(bytes)) {
						if (cachingPolicy.cacheAllowed()) {
							offlineMetadataCache.put404(url);
						}
						throw new NotFoundException(url);
					}
					if (cachingPolicy.cacheAllowed()) {
						offlineMetadataCache.put(url, bytes);
					}
					return bytes;
				} else {
					if (cachingPolicy.cacheAllowed()) {
						offlineMetadataCache.put404(url);
					}
					throw new NotFoundException(url);
				}
			} catch (UnknownHostException e) {
				if (cachingPolicy.cacheAllowed()) {
					var cached = offlineMetadataCache.get(url);
					if (cached != null) {
						if (OfflineCache.is404(cached)) {
							throw new NotFoundException(url);
						} else {
							return cached;
						}
					}
				}
				throw e;
			}
		}
		throw new IllegalStateException("P2Client is in offline mode but has no cache for " + url);
	}

	@SuppressWarnings("serial")
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
			String metadataTarget;
			String p2IndexContent = null;
			try {
				p2IndexContent = getString(url + "p2.index");
				metadataTarget = getGroup1(p2IndexContent, p2metadata).trim();
			} catch (IllegalStateException e) {
				throw new UnsupportedOperationException(
						"We could not parse the content at " + url + "p2.index:\n\n" + p2IndexContent, e);
			} catch (NotFoundException e) {
				metadataTarget = null;
			}
			if (metadataTarget == null) {
				String guessedXml = null;
				List<String> triedUrls = new ArrayList<>();
				triedUrls.add(url + "p2.index");
				try {
					resolveXml(url, CONTENT_XML);
					guessedXml = CONTENT_XML;
				} catch (CouldNotFindException e) {
					triedUrls.addAll(e.triedUrls);
					try {
						resolveXml(url, COMPOSITE_XML);
						guessedXml = COMPOSITE_XML;
					} catch (CouldNotFindException e2) {
						triedUrls.addAll(e2.triedUrls);
					}
				}
				if (guessedXml == null) {
					throw new CouldNotFindException(triedUrls.toArray(new String[0]));
				} else {
					this.metadataName = guessedXml;
				}
			} else if (metadataTarget.indexOf(',') == -1) {
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
		var rawUrl = url + metadataTarget;

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
			var unwantedEntries = new ArrayList<String>();
			try (var zipStream = new ZipInputStream(new ByteArrayInputStream(bytes))) {
				ZipEntry entry;
				while ((entry = zipStream.getNextEntry()) != null) {
					if (entry.getName().equals(metadataTarget)) {
						return new String(zipStream.readAllBytes(), StandardCharsets.UTF_8);
					} else {
						unwantedEntries.add(entry.getName());
					}
				}
				throw new IllegalArgumentException(
						"Expected to find " + metadataTarget + " but was " + unwantedEntries);
			}
		} catch (NotFoundException e) {
			// no problem, just keep trying
		}
		try {
			return new String(getBytes(rawUrl), StandardCharsets.UTF_8);
		} catch (NotFoundException e) {
			// no problem, just tell what we tried
		}
		throw new CouldNotFindException(xzUrl, jarUrl, rawUrl);
	}

	private static class CouldNotFindException extends IllegalArgumentException {
		final List<String> triedUrls;

		CouldNotFindException(String... triedUrls) {
			super("Attempted to find resource at\n" + String.join("\n", triedUrls));
			this.triedUrls = Arrays.asList(triedUrls);
		}
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
							var location = node.getAttributes().getNamedItem("location").getNodeValue();
							childLocations.add(location);
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
