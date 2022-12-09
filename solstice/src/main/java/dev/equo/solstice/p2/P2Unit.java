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

import java.util.Arrays;
import java.util.List;
import java.util.TreeMap;
import java.util.TreeSet;
import org.eclipse.osgi.internal.framework.FilterImpl;
import org.w3c.dom.Node;

/** Usually represents a jar file in a p2 repository, but could also be a "feature" or "group". */
public class P2Unit implements Comparable<P2Unit> {
	final String id, version;
	FilterImpl filter;
	final TreeMap<String, String> properties = new TreeMap<>();
	final TreeSet<P2Session.Providers> requires = new TreeSet<>();

	P2Unit(P2Session session, P2Client.Folder index, Node rootNode) {
		id = rootNode.getAttributes().getNamedItem("id").getNodeValue();
		version = rootNode.getAttributes().getNamedItem("version").getNodeValue();
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
			}
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
				var namespace = node.getAttributes().getNamedItem("namespace").getNodeValue();
				var name = node.getAttributes().getNamedItem("name").getNodeValue();
				requires.add(session.requires(namespace, name));
			}
		}
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(id);
		builder.append(" v=");
		builder.append(version);
		if (filter != null) {
			builder.append(" filter=");
			builder.append(filter);
		}
		builder.append('\n');
		properties.forEach(
				(key, value) -> {
					builder.append("  prop ");
					builder.append(key);
					builder.append('=');
					builder.append(value);
					builder.append('\n');
				});
		for (var r : requires) {
			builder.append("  req ");
			builder.append(r.name);
			builder.append('\n');
		}
		builder.setLength(builder.length() - 1);
		return builder.toString();
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

	@Override
	public int compareTo(P2Unit o) {
		if (id.equals(o.id)) {
			return version.compareTo(o.version);
		} else {
			return id.compareTo(o.id);
		}
	}

	public String getId() {
		return id;
	}
}
