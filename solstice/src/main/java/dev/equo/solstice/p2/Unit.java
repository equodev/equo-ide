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

class Unit {
	final String id, version;
	FilterImpl filter;
	final TreeMap<String, String> properties = new TreeMap<>();
	final TreeSet<P2Session.Providers> requires = new TreeSet<>();

	public Unit(P2Session session, Node rootNode) {
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
		for (int i = 0; i < propertyNodes.getLength(); ++i) {
			var propNode = propertyNodes.item(i);
			if ("property".equals(propNode.getNodeName())) {
				var name = propNode.getAttributes().getNamedItem("name").getNodeValue();
				var idx = PROP_FILTER.indexOf(name);
				if (idx != -1) {
					properties.put(
							PROP_FILTER.get(idx), propNode.getAttributes().getNamedItem("value").getNodeValue());
				}
			}
		}
	}

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
		builder.setLength(builder.length() - 1);
		return builder.toString();
	}

	private static final List<String> PROP_FILTER =
			Arrays.asList(
					"maven-groupId", "maven-artifactId", "maven-version", "maven-repository", "maven-type");
}
