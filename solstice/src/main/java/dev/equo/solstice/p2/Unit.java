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

import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;
import org.w3c.dom.Node;

public class Unit {
	private final String id, version;
	private String filter;
	private final TreeMap<String, String> properties = new TreeMap<>();
	private final Map<String, TreeSet<String>> requires = new TreeMap<>();
	private final Map<String, TreeSet<String>> provides = new TreeMap<>();

	public Unit(Node node) {
		id = node.getAttributes().getNamedItem("id").getNodeValue();
		version = node.getAttributes().getNamedItem("version").getNodeValue();
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
		requires.forEach(
				(key, list) -> {
					for (var value : list) {
						builder.append("  requires ");
						builder.append(key);
						builder.append('=');
						builder.append(value);
						builder.append('\n');
					}
				});
		provides.forEach(
				(key, list) -> {
					for (var value : list) {
						builder.append("  provides ");
						builder.append(key);
						builder.append('=');
						builder.append(value);
						builder.append('\n');
					}
				});
		builder.setLength(builder.length() - 1);
		return builder.toString();
	}
}
