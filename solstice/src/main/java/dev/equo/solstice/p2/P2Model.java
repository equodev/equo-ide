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
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public class P2Model {
	private final TreeSet<String> p2repo = new TreeSet<>();
	private final TreeSet<String> install = new TreeSet<>();
	private final TreeMap<String, Filter> filters = new TreeMap<>();

	public TreeSet<String> getP2repo() {
		return p2repo;
	}

	public TreeSet<String> getInstall() {
		return install;
	}

	public TreeMap<String, Filter> getFilters() {
		return filters;
	}

	/** Ensures there are no conflicts between the existing filters. */
	public void validateFilters() {
		var entryList = new ArrayList<>(filters.entrySet());
		for (int i = 0; i < entryList.size(); ++i) {
			var first = entryList.get(i);
			for (int j = i + 1; j < entryList.size(); ++j) {
				var second = entryList.get(j);
				String conflictMsg =
						first.getValue().conflictsWith(first.getKey(), second.getKey(), second.getValue());
				if (conflictMsg != null) {
					throw new IllegalArgumentException(conflictMsg);
				}
			}
		}
	}

	public void addFilterAndValidate(String filterName, Filter filter) {
		Filter existingForThisName = filters.get(filterName);
		if (existingForThisName != null) {
			if (existingForThisName.equals(filter)) {
				return;
			} else {
				throw new IllegalArgumentException(
						"We already have a filter named "
								+ filterName
								+ "\n"
								+ "   existing: "
								+ existingForThisName
								+ "\n"
								+ "  attempted: "
								+ filter
								+ "\n"
								+ "You can fix this by removing or renaming one of the filters.");
			}
		}
		for (var existing : filters.entrySet()) {
			String conflictMsg = filter.conflictsWith(filterName, existing.getKey(), existing.getValue());
			if (conflictMsg != null) {
				throw new IllegalArgumentException(conflictMsg);
			}
		}
		filters.put(filterName, filter);
	}

	@Override
	public String toString() {
		StringBuilder buf = new StringBuilder();
		buf.append('{');
		if (!p2repo.isEmpty()) {
			appendSet(buf, "p2repo", p2repo);
		}
		if (!install.isEmpty()) {
			appendSet(buf, "install", install);
		}
		if (!filters.isEmpty()) {
			String START___ = "filters: { '";
			String START = ",\n            '";
			boolean isFirst = true;
			for (var entry : filters.entrySet()) {
				String lead;
				if (isFirst) {
					lead = START___;
					isFirst = false;
				} else {
					lead = START;
				}
				buf.append(lead + entry.getKey() + "': " + entry.getValue());
			}
			buf.append(" },\n");
		}
		return closeJson(buf);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		P2Model model = (P2Model) o;
		return p2repo.equals(model.p2repo)
				&& install.equals(model.install)
				&& filters.equals(model.filters);
	}

	@Override
	public int hashCode() {
		return Objects.hash(p2repo, install, filters);
	}

	public static class Filter {
		private final TreeSet<String> exclude = new TreeSet<>();
		private final TreeSet<String> excludePrefix = new TreeSet<>();
		private final TreeSet<String> excludeSuffix = new TreeSet<>();
		private final TreeMap<String, String> props = new TreeMap<>();

		public TreeSet<String> getExclude() {
			return exclude;
		}

		public TreeSet<String> getExcludePrefix() {
			return excludePrefix;
		}

		public TreeSet<String> getExcludeSuffix() {
			return excludeSuffix;
		}

		public TreeMap<String, String> getProps() {
			return props;
		}

		public String conflictsWith(String nameThis, String nameOther, Filter other) {
			for (var prop : props.entrySet()) {
				var otherVal = other.getProps().get(prop.getKey());
				if (!Objects.equals(prop.getValue(), otherVal)) {
					return "conflict for prop "
							+ prop.getKey()
							+ "! "
							+ nameThis
							+ " has "
							+ prop.getValue()
							+ " but "
							+ nameOther
							+ " has "
							+ otherVal;
				}
			}
			return null;
		}

		@Override
		public String toString() {
			StringBuilder buf = new StringBuilder();
			buf.append('{');
			if (!exclude.isEmpty()) {
				appendSet(buf, "exclude", exclude);
			}
			if (!excludePrefix.isEmpty()) {
				appendSet(buf, "excludePrefix", excludePrefix);
			}
			if (!excludeSuffix.isEmpty()) {
				appendSet(buf, "excludeSuffix", excludeSuffix);
			}
			if (!props.isEmpty()) {
				String START___ = "props: { '";
				String START = ",\n          '";
				boolean isFirst = true;
				for (var entry : props.entrySet()) {
					String lead;
					if (isFirst) {
						lead = START___;
						isFirst = false;
					} else {
						lead = START;
					}
					buf.append(lead + entry.getKey() + "': '" + entry.getValue() + "'");
				}
				buf.append(" },\n");
			}
			return closeJson(buf);
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (o == null || getClass() != o.getClass()) return false;
			Filter filter = (Filter) o;
			return exclude.equals(filter.exclude)
					&& excludePrefix.equals(filter.excludePrefix)
					&& excludeSuffix.equals(filter.excludeSuffix)
					&& props.equals(filter.props);
		}

		@Override
		public int hashCode() {
			return Objects.hash(exclude, excludePrefix, excludeSuffix, props);
		}
	}

	private static void appendSet(StringBuilder buf, String name, Set<String> toAdd) {
		buf.append(name);
		buf.append(": ['");
		buf.append(String.join("', '", toAdd));
		buf.append("'],\n");
	}

	private static String closeJson(StringBuilder buf) {
		if (buf.charAt(buf.length() - 1) == '\n') {
			buf.setLength(buf.length() - 1);
			buf.setCharAt(buf.length() - 1, '}');
		} else {
			buf.append('}');
		}
		return buf.toString();
	}
}
