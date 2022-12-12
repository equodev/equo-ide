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
	/** Determines which format to print the table in. */
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
		if (mavenStates.isEmpty()) {
			return "No jars were specified.";
		}
		var coordinate = new TableColumn("maven coordinate / p2 id").with(MavenStatus::coordinate);
		var repo = new TableColumn("repo").with(MavenStatus::repo);
		return Table.getTable(format, mavenStates, coordinate, repo);
	}

	/** Returns a table with the id, name, and description of all its units. */
	public static String nameAndDescription(Collection<P2Unit> units, Format format) {
		var table = new WordWrapTable();
		for (var unit : units) {
			var n = unit.properties.get(P2Unit.P2_NAME);
			var name = n != null ? n : "(no " + P2Unit.P2_NAME + ")";
			table.add(unit.id, name);
			var desc = unit.properties.get(P2Unit.P2_DESC);
			if (desc != null) {
				table.add("", "  " + desc);
			}
		}
		return table.toString("id", "name \\n description", format);
	}

	/** Returns a table describing all ambiguous requirements and how they ended up. */
	public static String ambiguousRequirements(P2Query query, Format format) {
		if (query.getAmbiguousRequirements().isEmpty()) {
			return "No ambiguous requirements.";
		}
		var table = new NColumnTable("ambiguous requirement", "candidate", "installed");
		for (var requirement : query.getAmbiguousRequirements()) {
			var candidates = requirement.getProviders();
			for (int i = 0; i < candidates.size(); ++i) {
				var candidate = candidates.get(i);
				var firstCell = i == 0 ? requirement.toString() : "";
				table.addRow(firstCell, candidate.toString(), query.isInstalled(candidate) ? "[x]" : "[ ]");
			}
		}
		return table.toString(format);
	}

	/** Returns a table describing all unmet requirements and who they affect. */
	public static String unmetRequirements(P2Query query, Format format) {
		if (query.getUnmetRequirements().isEmpty()) {
			return "No unmet requirements.";
		}
		var table = new NColumnTable("unmet requirement", "needed by");
		for (var unmet : query.getUnmetRequirements().entrySet()) {
			var neededByFirst = unmet.getValue().iterator().next();
			for (var neededBy : unmet.getValue()) {
				var firstCell = neededBy == neededByFirst ? unmet.getKey().toString() : "";
				table.addRow(firstCell, neededBy.toString());
			}
		}
		return table.toString(format);
	}

	static class NColumnTable {
		private final TableColumn[] headers;
		private final List<String[]> rows = new ArrayList<>();

		public NColumnTable(String... headers) {
			this.headers = new TableColumn[headers.length];
			for (int i = 0; i < headers.length; ++i) {
				this.headers[i] = new TableColumn(headers[i]);
			}
		}

		public void addRow(String... data) {
			if (data.length != headers.length) {
				throw new IllegalArgumentException(
						"Expected " + headers.length + " items but got " + data.length);
			}
			rows.add(data);
		}

		public String toString(Format format) {
			return Table.getTable(format, headers, rows.toArray(new String[0][]));
		}
	}

	/** Returns a table with the parsed details of the given units. */
	public static String detail(Collection<P2Unit> units, Format format) {
		if (units.isEmpty()) {
			return "(none)";
		}
		var table = new WordWrapTable();
		var first = units.iterator().next();
		for (var unit : units) {
			if (unit != first) {
				table.add("---", "---");
			}
			table.add("id", unit.id);
			table.add("version", unit.version.toString());
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
				if (req.hasOnlyOneProvider()) {
					table.add("req " + req.name, req.getOnlyProvider().toString());
				} else {
					var available = req.getProviders();
					table.add("req " + req.name, available.size() + " available");
					for (var a : available) {
						table.add("", a.toString());
					}
				}
			}
		}
		return table.toString("key", "value", format);
	}

	static class WordWrapTable {
		private static final int MAX_VALUE_LEN = 50;

		final List<Pair> pairs = new ArrayList<Pair>();
		final String wrapIndent = "  ";

		public void add(String key, String value) {
			add(key, value, MAX_VALUE_LEN);
		}

		private void add(String key, String value, int maxLenFirstLine) {
			value = value.replace("\n", " ").replace("\r", "");
			if (value.length() <= maxLenFirstLine) {
				pairs.add(new Pair(key, value));
			} else {
				int maxLen = maxLenFirstLine - wrapIndent.length();
				var lines = new ArrayList<String>();
				int lineStart = 0;
				int i = 0;
				while (i + maxLen < value.length() && (i = value.lastIndexOf(' ', i + maxLen)) != -1) {
					if (i < lineStart) {
						break;
					}
					lines.add(value.substring(lineStart, i));
					lineStart = ++i;
				}
				lines.add(value.substring(lineStart));
				pairs.add(new Pair(key, lines.get(0)));
				for (i = 1; i < lines.size(); ++i) {
					pairs.add(new Pair("", wrapIndent + lines.get(i)));
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
