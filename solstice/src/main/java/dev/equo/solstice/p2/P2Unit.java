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

import java.io.StringWriter;
import java.util.Arrays;
import java.util.List;
import java.util.TreeMap;
import java.util.TreeSet;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.eclipse.osgi.internal.framework.FilterImpl;
import org.osgi.framework.Filter;
import org.osgi.framework.Version;
import org.w3c.dom.Node;

/** Usually represents a jar file in a p2 repository, but could also be a "feature" or "group". */
public class P2Unit implements Comparable<P2Unit> {
	final Node rootNode;
	final P2Client.Folder index;
	final String id;
	final Version version;
	Filter filter;
	final TreeMap<String, String> properties = new TreeMap<>();
	final TreeSet<P2Session.Requirement> requires = new TreeSet<>();

	P2Unit(P2Session session, P2Client.Folder index, Node rootNode) {
		this.rootNode = rootNode;
		this.index = index;
		id = rootNode.getAttributes().getNamedItem("id").getNodeValue();
		version = Version.parseVersion(rootNode.getAttributes().getNamedItem("version").getNodeValue());
		var nodeList = rootNode.getChildNodes();
		for (int i = 0; i < nodeList.getLength(); ++i) {
			var node = nodeList.item(i);
			if ("filter".equals(node.getNodeName())) {
				filter = session.parseFilter(node.getTextContent().trim());
			} else if ("properties".equals(node.getNodeName())) {
				parseProperties(node);
			} else if ("provides".equals(node.getNodeName())) {
				parseProvides(session, node);
			} else if ("requires".equals(node.getNodeName())) {
				parseRequires(session, node);
			} else if ("artifacts".equals(node.getNodeName())) {
				parseArtifact(node);
			}
		}
	}

	private void parseArtifact(Node node) {
		String artifactClassifier = null;
		var artifactNodes = node.getChildNodes();
		for (int i = 0; i < artifactNodes.getLength(); ++i) {
			var propNode = artifactNodes.item(i);
			if ("artifact".equals(propNode.getNodeName())) {
				var classifier = propNode.getAttributes().getNamedItem("classifier").getNodeValue();
				if (artifactClassifier != null && !artifactClassifier.equals(classifier)) {
					throw new IllegalArgumentException(
							id
									+ ":"
									+ version
									+ " has multiple artifacts, first was "
									+ artifactClassifier
									+ " second was "
									+ classifier);
				}
				artifactClassifier = classifier;
			}
		}
		if (artifactClassifier != null) {
			properties.put(ARTIFACT_CLASSIFIER, artifactClassifier);
		}
	}

	private void parseProperties(Node node) {
		var propertyNodes = node.getChildNodes();
		var needsReplacing = new TreeMap<String, String>();
		for (int i = 0; i < propertyNodes.getLength(); ++i) {
			var propNode = propertyNodes.item(i);
			if ("property".equals(propNode.getNodeName())) {
				var name = propNode.getAttributes().getNamedItem("name").getNodeValue();
				var idx = PROP_FILTER.indexOf(name);
				if (idx != -1) {
					String value = propNode.getAttributes().getNamedItem("value").getNodeValue();
					properties.put(PROP_FILTER.get(idx), value);
					if (value.startsWith("%")) {
						needsReplacing.put(value, name);
					}
				}
			}
		}
		if (!needsReplacing.isEmpty()) {
			for (int i = 0; i < propertyNodes.getLength(); ++i) {
				var propNode = propertyNodes.item(i);
				if ("property".equals(propNode.getNodeName())) {
					var name = propNode.getAttributes().getNamedItem("name").getNodeValue();
					if (name.startsWith(df_LT) && !needsReplacing.isEmpty()) {
						var replaceKey = "%" + name.substring(df_LT.length());
						var keyThatNeedsReplacement = needsReplacing.remove(replaceKey);
						if (keyThatNeedsReplacement != null) {
							String value = propNode.getAttributes().getNamedItem("value").getNodeValue();
							properties.put(keyThatNeedsReplacement, value);
							if (needsReplacing.isEmpty()) {
								break;
							}
						}
					}
				}
			}
		}
	}

	private static final String df_LT = "df_LT.";

	private void parseProvides(P2Session session, Node providesRoot) {
		var providesNodes = providesRoot.getChildNodes();
		for (int i = 0; i < providesNodes.getLength(); ++i) {
			var node = providesNodes.item(i);
			if ("provided".equals(node.getNodeName())) {
				var namespace = node.getAttributes().getNamedItem("namespace").getNodeValue();
				if (EXCLUDED_REQUIRE_PROVIDE_NAMESPACES.contains(namespace)) {
					continue;
				}
				var name = node.getAttributes().getNamedItem("name").getNodeValue();
				session.provides(namespace, name, this);
			}
		}
	}

	private void parseRequires(P2Session session, Node providesRoot) {
		var providesNodes = providesRoot.getChildNodes();
		for (int i = 0; i < providesNodes.getLength(); ++i) {
			var node = providesNodes.item(i);
			if ("required".equals(node.getNodeName())) {
				var namespaceNode = node.getAttributes().getNamedItem("namespace");
				if (namespaceNode == null) {
					// the eclipse corrosion p2 repository has requirements without a namespace, e.g.
					// <required match='providedCapabilities.exists(x | x.name == $0 &amp;&amp; x.namespace ==
					// $1)' matchParameters='[&apos;a.jre.javase&apos;,
					// &apos;org.eclipse.equinox.p2.iu&apos;]' min='0' max='0'>
					continue;
				}
				var namespace = namespaceNode.getNodeValue();
				if (EXCLUDED_REQUIRE_PROVIDE_NAMESPACES.contains(namespace)) {
					continue;
				}
				boolean isOptional = false;
				var optionalNode = node.getAttributes().getNamedItem("optional");
				if (optionalNode != null) {
					isOptional = "true".equals(optionalNode.getNodeValue().trim());
				}

				FilterImpl filter = null;
				var filterNodes = node.getChildNodes();
				for (int f = 0; f < filterNodes.getLength(); ++f) {
					var filterNode = filterNodes.item(f);
					if ("filter".equals(filterNode.getNodeName())) {
						if (filter != null) {
							throw new IllegalArgumentException("We don't support multiple filters: " + this);
						}
						filter = session.parseFilter(filterNode.getTextContent().trim());
					}
				}
				var name = node.getAttributes().getNamedItem("name").getNodeValue();
				requires.add(session.requires(namespace, name, isOptional, filter));
			}
		}
	}

	@Override
	public String toString() {
		return id + ":" + version;
	}

	public static final String MAVEN_GROUP_ID = "maven-groupId";
	public static final String MAVEN_ARTIFACT_ID = "maven-artifactId";
	public static final String MAVEN_VERSION = "maven-version";
	public static final String MAVEN_REPOSITORY = "maven-repository";
	public static final String MAVEN_TYPE = "maven-type";
	public static final String P2_NAME = "org.eclipse.equinox.p2.name";
	public static final String P2_DESC = "org.eclipse.equinox.p2.description";
	public static final String P2_TYPE_CATEGORY = "org.eclipse.equinox.p2.type.category";
	public static final String P2_TYPE_FEATURE = "org.eclipse.equinox.p2.type.group";

	/** This is a synthetic property that we create ourselves. */
	public static final String ARTIFACT_CLASSIFIER = "artifact-classifier";

	public static final String ARTIFACT_CLASSIFIER_BUNDLE = "osgi.bundle";
	public static final String ARTIFACT_CLASSIFIER_FEATURE = "org.eclipse.update.feature";
	public static final String ARTIFACT_CLASSIFIER_BINARY = "binary";

	private static final List<String> PROP_FILTER =
			Arrays.asList(
					MAVEN_GROUP_ID,
					MAVEN_ARTIFACT_ID,
					MAVEN_VERSION,
					MAVEN_REPOSITORY,
					MAVEN_TYPE,
					P2_NAME,
					P2_DESC,
					P2_TYPE_CATEGORY,
					P2_TYPE_FEATURE);

	private static final List<String> EXCLUDED_REQUIRE_PROVIDE_NAMESPACES =
			Arrays.asList("org.eclipse.equinox.p2.eclipse.type", "osgi.ee");

	/** Sorted alphabetically based on id, and then based on version with greater versions first. */
	@Override
	public int compareTo(P2Unit o) {
		if (id.equals(o.id)) {
			return -version.compareTo(o.version);
		} else {
			return id.compareTo(o.id);
		}
	}

	public String getId() {
		return id;
	}

	public Version getVersion() {
		return version;
	}

	public String getRawXml() throws TransformerException {
		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer transformer = tf.newTransformer();
		transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
		transformer.setOutputProperty(OutputKeys.METHOD, "xml");
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
		transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

		var writer = new StringWriter();
		transformer.transform(new DOMSource(rootNode), new StreamResult(writer));
		var raw = writer.toString();
		var unixEndings = raw.replace("\r", "");
		var lines = unixEndings.split("\n");
		var result = new StringBuilder(unixEndings.length());
		for (var line : lines) {
			if (!line.trim().isEmpty()) {
				result.append(line);
				result.append('\n');
			}
		}
		return result.toString();
	}

	public String getRepoUrl() {
		return index.url;
	}

	public String getRepoUrlLastSegment() {
		char lastChar = index.url.charAt(index.url.length() - 1);
		if (lastChar != '/') {
			throw new IllegalArgumentException("p2 repo must end with /");
		}
		int lastSlash = index.url.lastIndexOf('/', index.url.length() - 2);
		return index.url.substring(lastSlash + 1, index.url.length() - 1);
	}

	public String getJarUrl() {
		return index.url + "plugins/" + id + "_" + version + ".jar";
	}
}
