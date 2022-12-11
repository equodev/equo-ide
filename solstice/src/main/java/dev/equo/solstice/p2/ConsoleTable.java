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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

/** Formats P2Unit for display at the console. */
public class ConsoleTable {
	public enum Format {
		ASCII,
		CSV
	}

	/** Returns a table with the {@link MavenStatus} of all its units. */
	public static String mavenStatus(Iterable<P2Unit> units, Format format) {
		var mavenStates = new ArrayList<MavenStatus>();
		for (var unit : units) {
			mavenStates.add(MavenStatus.forUnit(unit));
		}
		mavenStates.sort(Comparator.naturalOrder());

		var coordinate = new TableColumn("maven coordinate / p2 id").with(MavenStatus::coordinate);
		var repo = new TableColumn("repo").with(MavenStatus::repo);
		return Table.getTable(format, mavenStates, coordinate, repo);
	}

	/** Returns a table with the id and name of all its units. */
	public static String nameAndDescription(Collection<P2Unit> units, Format format) {
		var id = new TableColumn("id").with(P2Unit::getId);
		var name =
				new TableColumn("name")
						.<P2Unit>with(
								u -> {
									var n = u.properties.get(P2Unit.P2_NAME);
									return n != null ? n : "(null)";
								});
		return Table.getTable(format, units, id, name);
	}

	public static String detail(Collection<P2Unit> units, Format format) {
		var table = new WordWrapTable();
		for (var unit : units) {
			table.add("id", unit.id);
			table.add("version", unit.version);
			var mavenStatus = MavenStatus.forUnit(unit);
			if (mavenStatus != null) {
				table.add("maven coordinate", mavenStatus.coordinate());
				table.add("maven repo", mavenStatus.repo());
			}
			if (unit.filter != null) {
				table.add("filter", unit.filter.toString());
			}
			for (var prop : unit.properties.entrySet()) {
				table.add("prop " + prop.getKey(), prop.getValue());
			}
			for (var req : unit.requires) {
				if (req.hasOnlyOne()) {
					table.add("req " + req.name, req.getOnlyOne().id);
				} else {
					var available = req.get();
					table.add("req " + req.name, available.size() + " available");
					for (var a : available) {
						table.add("", a.getId() + ":" + a.getVersion());
					}
				}
			}
		}
		return table.toString("key", "value", format);
	}

	private static class WordWrapTable {
		private static final int MAX_VALUE_LEN = 50;

		final List<Pair> pairs = new ArrayList<Pair>();

		public void add(String key, String value) {
			add(key, value, MAX_VALUE_LEN);
		}

		private void add(String key, String value, int maxLen) {
			if (value.length() <= maxLen) {
				pairs.add(new Pair(key, value));
			} else {
				var lines = new ArrayList<String>();
				int lineStart = 0;
				int i = 0;
				while (i + maxLen < value.length() && (i = value.lastIndexOf(' ', i + maxLen)) != -1) {
					lines.add(value.substring(lineStart, i));
					lineStart = i + 1;
				}
				lines.add(value.substring(lineStart));
				pairs.add(new Pair(key, lines.get(0)));
				for (i = 1; i < lines.size(); ++i) {
					pairs.add(new Pair("", lines.get(i)));
				}
			}
		}

		public String toString(String headerKey, String headerValue, Format format) {
			// reflow the table to the longest unwrappable value
			int maxLen = pairs.stream().mapToInt(p -> p.value.length()).max().getAsInt();
			if (maxLen > MAX_VALUE_LEN) {
				var copy = new ArrayList<>(pairs);
				pairs.clear();
				int i = 0;
				while (i < copy.size()) {
					var pair = copy.get(i);
					if (pair.key.isEmpty()) {
						// reflow this element to the new max
						pairs.remove(pairs.size() - 1);
						var parent = copy.get(i - 1);
						var builder = new StringBuilder(parent.value);
						while (i < copy.size() && copy.get(i).key.isEmpty()) {
							builder.append(' ');
							builder.append(copy.get(i).value);
							++i;
						}
						add(parent.key, builder.toString(), maxLen);
						continue;
					} else {
						pairs.add(pair);
					}
					++i;
				}
			}
			// render the table out
			var key = new TableColumn(headerKey).with((Pair pair) -> pair.key);
			var value = new TableColumn(headerValue).with((Pair pair) -> pair.value);
			return Table.getTable(format, pairs, key, value);
		}
	}

	private static class Pair {
		String key;
		String value;

		Pair(String key, String value) {
			this.key = key;
			this.value = value;
		}
	}
}
