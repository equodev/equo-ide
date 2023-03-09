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
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

/** Formats P2Unit for display at the console. */
public class ConsoleTable {
	/** Determines which format to print the table in. */
	public enum Format {
		ascii,
		csv
	}

	/** Returns a table with the full content of the given P2Model. */
	public static String request(P2Model model, Format format) {
		var table = new NColumnTable("kind", "value");
		for (var p2 : model.getP2repo()) {
			table.addRow("p2repo", p2);
		}
		for (var install : model.getInstall()) {
			table.addRow("install", install);
		}
		for (var filter : model.getFilters().entrySet()) {
			table.addRow("filter", filter.getKey());
			for (var exclude : filter.getValue().getExclude()) {
				table.addRow("  exclude", exclude);
			}
			for (var excludePrefix : filter.getValue().getExcludePrefix()) {
				table.addRow("  excludePrefix", excludePrefix);
			}
			for (var excludeSuffix : filter.getValue().getExcludeSuffix()) {
				table.addRow("  excludeSuffix", excludeSuffix);
			}
			filter.getValue().getProps().forEach((key, value) -> table.addRow("  " + key, value));
		}
		return table.toString(format);
	}

	/** Returns a table with the {@link RepoStatus} of all its units. */
	public static String mavenStatus(Iterable<P2Unit> units, Format format) {
		var mavenStates = new ArrayList<RepoStatus>();
		for (var unit : units) {
			mavenStates.add(RepoStatus.forUnit(unit));
		}
		mavenStates.sort(Comparator.naturalOrder());
		if (mavenStates.isEmpty()) {
			return "No jars were specified.";
		}
		var coordinate = new TableColumn("maven coordinate / p2 id").with(RepoStatus::coordinate);
		var repo = new TableColumn("repo").with(RepoStatus::repo);
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
		String firstLine = query.getAmbiguousRequirements().size() + " ambiguous requirement(s).";
		if (query.getAmbiguousRequirements().isEmpty()) {
			return firstLine;
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
		return firstLine + "\n" + table.toString(format);
	}

	/** Returns a table describing all unmet requirements and who they affect. */
	public static String unmetRequirements(P2Query query, Format format) {
		String firstLine = query.getUnmetRequirements().size() + " unmet requirement(s).";
		if (query.getUnmetRequirements().isEmpty()) {
			return firstLine;
		}
		var table = new NColumnTable("unmet requirement", "needed by");
		for (var unmet : query.getUnmetRequirements().entrySet()) {
			var neededByFirst = unmet.getValue().iterator().next();
			for (var neededBy : unmet.getValue()) {
				var firstCell = neededBy == neededByFirst ? unmet.getKey().toString() : "";
				table.addRow(firstCell, neededBy.toString());
			}
		}
		return firstLine + "\n" + table.toString(format);
	}

	/** Returns a table describing all optional requirements which were not installed. */
	public static String optionalRequirementsNotInstalled(P2Query query, Format format) {
		if (query.getOptionalRequirementsNotInstalled().isEmpty()) {
			return "Every optional requirement was installed.";
		}
		var table =
				new NColumnTable("requirement (not installed)", "provided by", "optionally needed by");
		for (var unmet : query.getOptionalRequirementsNotInstalled().entrySet()) {
			var notInstalled = unmet.getKey();
			var providedBy = new ArrayList<>(notInstalled.getProviders());
			var neededBy = new ArrayList<>(unmet.getValue());
			for (int i = 0; i < Math.max(neededBy.size(), providedBy.size()); ++i) {
				var cell0 = i == 0 ? notInstalled.getName() : "";
				var cell1 = i < providedBy.size() ? providedBy.get(i).toString() : "";
				var cell2 = i < neededBy.size() ? neededBy.get(i).toString() : "";
				if (i == 0 && cell1.equals("")) {
					cell1 = "-- none available --";
				}
				table.addRow(cell0, cell1, cell2);
			}
		}
		return table.toString(format);
	}

	static class NColumnTable {
		/** The maximum width (we hope) a table will have. */
		private static final int MAX_WIDTH = 80;
		/** We have to save at least this much for a shortening to be worth it. */
		private static final int MIN_SAVINGS = 1;

		private static final List<String> LEGEND = Arrays.asList("§§", "§", "ë", "é", "á");
		private static final List<String> DICTIONARY =
				Arrays.asList(
						"feature.feature.group",
						"feature.group",
						"org.eclipse.equinox",
						"org.eclipse",
						"org.apache");
		private final List<String> key = new ArrayList<>();

		private final int ncol;
		private final TableColumn[] headers;
		private final List<String[]> rows = new ArrayList<>();

		public NColumnTable(String... headers) {
			ncol = headers.length;
			this.headers = new TableColumn[ncol];
			for (int i = 0; i < ncol; ++i) {
				this.headers[i] = new TableColumn(headers[i]);
			}
		}

		public void addRow(String... data) {
			if (data.length != ncol) {
				throw new IllegalArgumentException(
						"Expected " + headers.length + " items but got " + data.length);
			}
			updateWidest(rows.size(), data);
			rows.add(data);
		}

		int widestWidth = -1;
		int widestRow = -1;

		private void updateWidest(int row, String[] data) {
			int width = 1; // 2 + 3 + ... + 3 + 2 = 1 + (3 * n)
			for (String d : data) {
				width += d.length() + 3;
			}
			if (row != -1 && width > widestWidth) {
				widestWidth = width;
				widestRow = row;
			}
		}

		private void addKeyToLegend(String dict) {
			int idx = DICTIONARY.indexOf(dict);
			String legend = LEGEND.get(idx);
			key.add(dict);
			for (int r = 0; r < rows.size(); ++r) {
				String[] row = rows.get(r);
				for (int c = 0; c < ncol; ++c) {
					String e = row[c];
					if (e.contains(dict)) {
						row[c] = e.replace(dict, legend);
					}
				}
			}
		}

		private void compress() {
			do {
				String[] row = rows.get(widestRow);
				String bestDict = null;
				int bestSavings = MIN_SAVINGS;
				for (String dict : DICTIONARY) {
					int numOccurrences = 0;
					for (int c = 0; c < ncol; ++c) {
						if (row[c].contains(dict)) {
							++numOccurrences;
						}
					}
					int savings = numOccurrences * (dict.length() - 1);
					if (savings > bestSavings) {
						bestDict = dict;
						bestSavings = savings;
					}
				}
				if (bestDict != null) {
					addKeyToLegend(bestDict);
				} else {
					// couldn't find anything worthwhile
					return;
				}
			} while (key.size() < LEGEND.size() && recalculateWidestWidth() > MAX_WIDTH);
		}

		private int recalculateWidestWidth() {
			widestWidth = -1;
			widestRow = -1;
			for (int r = 0; r < rows.size(); ++r) {
				updateWidest(r, rows.get(r));
			}
			return widestWidth;
		}

		public String toString(Format format) {
			if (format == Format.ascii && widestWidth > MAX_WIDTH) {
				compress();
			}
			StringBuilder result =
					new StringBuilder(Table.getTable(format, headers, rows.toArray(new String[0][])));
			key.sort(Comparator.naturalOrder());
			for (String k : key) {
				result.append(LEGEND.get(DICTIONARY.indexOf(k))).append(" ").append(k).append("\n");
			}
			return result.toString();
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
			var repoStatus = RepoStatus.forUnit(unit);
			table.add("maven coordinate", repoStatus.coordinate());
			table.add("maven repo", repoStatus.repo());
			if (unit.filter != null) {
				table.add("filter", unit.filter.toString());
			}
			for (var prop : unit.properties.entrySet()) {
				table.add("prop " + prop.getKey(), prop.getValue());
			}
			var sortedOptionalsLast = new ArrayList<>(unit.requires);
			sortedOptionalsLast.sort(
					Comparator.<P2Session.Requirement>comparingInt(u -> u.isOptional() ? 1 : 0)
							.thenComparing(Comparator.naturalOrder()));
			for (var req : sortedOptionalsLast) {
				String reqName = req.isOptional() ? "req (opt) " : "req ";
				if (req.hasOnlyOneProvider()) {
					table.add(reqName + req.getName(), req.getOnlyProvider().toString());
				} else {
					var available = req.getProviders();
					table.add(reqName + req.getName(), available.size() + " available");
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

		final List<Pair> pairs = new ArrayList<>();
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
		final String key;
		final String value;

		Pair(String key, String value) {
			this.key = key;
			this.value = value;
		}
	}
}
