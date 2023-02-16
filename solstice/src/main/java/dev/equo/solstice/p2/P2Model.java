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

import com.diffplug.common.swt.os.SwtPlatform;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.function.Consumer;

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

	public boolean isEmpty() {
		return p2repo.isEmpty() && install.isEmpty() && filters.isEmpty();
	}

	public void addP2Repo(String p2url) {
		if (!p2url.endsWith("/")) {
			throw new IllegalArgumentException(
					"Must end with /\n" + "  " + p2url + "   <- WRONG\n" + "  " + p2url + "/  <- CORRECT\n");
		}
		if (p2url.endsWith(("//"))) {
			throw new IllegalArgumentException(
					"Must end with a single /\n"
							+ "  "
							+ p2url
							+ "  <- WRONG\n"
							+ "  "
							+ p2url.substring(0, p2url.length() - 1)
							+ "   <- CORRECT\n");
		}
		p2repo.add(p2url);
	}

	/**
	 * Applies a filter named `platform-specific-for-running` which selects artifacts for the running
	 * platform iff there are no other platform-related filters so far.
	 */
	public void applyNativeFilterIfNoPlatformFilter() {
		boolean hasAnyPlatformFilter =
				filters.values().stream()
						.filter(
								filter ->
										filter.props.containsKey(OSGI_OS)
												|| filter.props.containsKey(OSGI_WS)
												|| filter.props.containsKey(OSGI_ARCH))
						.findAny()
						.isPresent();
		if (!hasAnyPlatformFilter) {
			addFilterAndValidate("platform-specific-for-running", new P2Model.Filter().platformRunning());
		}
	}

	public P2Model deepCopy() {
		var deepCopy = new P2Model();
		deepCopy.p2repo.addAll(p2repo);
		deepCopy.install.addAll(install);
		for (var filterEntry : filters.entrySet()) {
			deepCopy.filters.put(filterEntry.getKey(), filterEntry.getValue().deepCopy());
		}
		return deepCopy;
	}

	public P2Query query(P2Client.Caching cachingPolicy) throws Exception {
		validateFilters();
		var session = new P2Session();
		try (var client = new P2Client(cachingPolicy)) {
			for (var repo : p2repo) {
				session.populateFrom(client, repo);
			}
		}
		var query = session.query();
		for (var filter : filters.values()) {
			filter.exclude.forEach(query::exclude);
			filter.excludePrefix.forEach(query::excludePrefix);
			filter.excludeSuffix.forEach(query::excludeSuffix);
			for (var prop : filter.getProps().entrySet()) {
				if (!P2Model.WILDCARD.equals(prop.getValue())) {
					query.filterProp(prop.getKey(), prop.getValue());
				}
			}
		}
		for (var target : install) {
			query.install(target);
		}
		return query;
	}

	public P2QueryResult queryUsingCache(
			P2Client.Caching clientCachingPolicy, QueryCache queryCachingPolicy) {
		QueryCacheOnDisk queryCache = new QueryCacheOnDisk(CacheLocations.p2Queries(), this);
		if (!QueryCache.FORCE_RECALCULATE.equals(queryCachingPolicy)) {
			var queryResult = queryCache.get();
			if (queryResult != null) {
				return queryResult;
			}
		}
		try {
			var query = query(clientCachingPolicy);
			var queryResult = new P2QueryResult(query, clientCachingPolicy);
			queryCache.put(queryResult);
			return queryResult;
		} catch (Exception e) {
			throw Unchecked.wrap(e);
		}
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

	public void addFilterAndValidate(String filterName, Consumer<Filter> filterSetup) {
		var filter = new Filter();
		filterSetup.accept(filter);
		addFilterAndValidate(filterName, filter);
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

	public void removeFilter(String name) {
		var existing = filters.remove(name);
		if (existing == null) {
			throw new IllegalArgumentException(
					"You tried to remove a filter with name '" + name + "' but no such filter exists");
		}
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

	public static final String WILDCARD = "*";
	private static final String OSGI_OS = "osgi.os";
	private static final String OSGI_WS = "osgi.ws";
	private static final String OSGI_ARCH = "osgi.arch";

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

		public Filter deepCopy() {
			var deepCopy = new Filter();
			deepCopy.exclude.addAll(exclude);
			deepCopy.excludePrefix.addAll(excludePrefix);
			deepCopy.excludeSuffix.addAll(excludeSuffix);
			deepCopy.props.putAll(props);
			return deepCopy;
		}

		public TreeSet<String> getExclude() {
			return exclude;
		}

		public Filter exclude(String unit) {
			exclude.add(unit);
			return this;
		}

		public TreeSet<String> getExcludePrefix() {
			return excludePrefix;
		}

		public Filter excludePrefix(String prefix) {
			excludePrefix.add(prefix);
			return this;
		}

		public TreeSet<String> getExcludeSuffix() {
			return excludeSuffix;
		}

		public Filter excludeSuffix(String suffix) {
			excludeSuffix.add(suffix);
			return this;
		}

		public TreeMap<String, String> getProps() {
			return props;
		}

		public Filter prop(String key, String value) {
			props.put(key, value);
			return this;
		}

		public Filter platformAll() {
			props.put(OSGI_OS, WILDCARD);
			props.put(OSGI_WS, WILDCARD);
			props.put(OSGI_ARCH, WILDCARD);
			return this;
		}

		public Filter platformNone() {
			props.put(OSGI_OS, "dont-include-platform-specific-artifacts");
			props.put(OSGI_WS, "dont-include-platform-specific-artifacts");
			props.put(OSGI_ARCH, "dont-include-platform-specific-artifacts");
			return this;
		}

		public Filter platform(SwtPlatform platform) {
			props.put(OSGI_OS, platform.getOs());
			props.put(OSGI_WS, platform.getWs());
			props.put(OSGI_ARCH, platform.getArch());
			return this;
		}

		public Filter platformNative() {
			return platform(SwtPlatform.getNative());
		}

		public Filter platformRunning() {
			return platform(SwtPlatform.getRunning());
		}

		public String conflictsWith(String nameThis, String nameOther, Filter other) {
			for (var prop : props.entrySet()) {
				var otherVal = other.getProps().get(prop.getKey());
				if (otherVal != null || other.getProps().containsKey(prop.getKey())) {
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
