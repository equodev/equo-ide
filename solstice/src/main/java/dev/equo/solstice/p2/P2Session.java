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
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.eclipse.osgi.internal.framework.FilterImpl;
import org.jetbrains.annotations.NotNull;
import org.osgi.framework.InvalidSyntaxException;

public class P2Session {
	List<P2Unit> units = new ArrayList<>();

	public void populateFrom(P2Client client, String url) throws Exception {
		client.addUnits(this, url);
	}

	public List<P2Unit> getUnitsWithProperty(String key, String value) {
		List<P2Unit> matches = new ArrayList<>();
		for (var unit : units) {
			if (Objects.equals(value, unit.properties.get(key))) {
				matches.add(unit);
			}
		}
		return matches;
	}

	public P2Unit getUnitById(String id) {
		for (var unit : units) {
			if (id.equals(unit.id)) {
				return unit;
			}
		}
		throw new IllegalArgumentException("No such unit id " + id);
	}

	public String listAllCategories() {
		var builder = new StringBuilder();
		var units = getUnitsWithProperty(P2Unit.P2_TYPE_CATEGORY, "true");
		for (var unit : units) {
			var name = unit.properties.get(P2Unit.P2_NAME);
			var desc = unit.properties.get(P2Unit.P2_DESC);
			builder.append(unit.id);
			builder.append('\n');
			builder.append("  ");
			builder.append(name);
			builder.append(": ");
			builder.append(desc.replace("\n", "").replace("\r", ""));
			builder.append('\n');
		}
		return builder.toString();
	}

	public String listAllFeatures() {
		var builder = new StringBuilder();
		var units = getUnitsWithProperty(P2Unit.P2_TYPE_FEATURE, "true");
		for (var unit : units) {
			var name = unit.properties.get(P2Unit.P2_NAME);
			var desc = unit.properties.get(P2Unit.P2_DESC);
			if (name == null) {
				name = "(None)";
			}
			if (desc == null) {
				desc = "(None)";
			}
			builder.append(unit.id);
			builder.append('\n');
			builder.append("  ");
			builder.append(name);
			builder.append(": ");
			builder.append(desc.replace("\n", "").replace("\r", ""));
			builder.append('\n');
		}
		return builder.toString();
	}

	public void sort() {
		units.sort(Comparator.naturalOrder());
		for (var submap : providerRegistry.values()) {
			for (Providers value : submap.values()) {
				value.sort();
			}
		}
	}

	static class Providers implements Comparable<Providers> {
		final String name;
		private Object field;

		private Providers(String name) {
			this.name = name;
		}

		private void add(P2Unit unit) {
			field = add(field, unit);
		}

		public boolean hasOnlyOne() {
			return field instanceof P2Unit;
		}

		public P2Unit getOnlyOne() {
			return (P2Unit) field;
		}

		public List<P2Unit> get() {
			return get(field);
		}

		private void sort() {
			if (field instanceof ArrayList) {
				((ArrayList<P2Unit>) field).sort(Comparator.reverseOrder());
			}
		}

		/** FYI, profiling against Eclipse 4.25 shows that 95% of these don't need a list. */
		private static Object add(Object existing, P2Unit toAdd) {
			if (existing == null) {
				return toAdd;
			} else if (existing instanceof P2Unit) {
				var list = new ArrayList<>();
				list.add(existing);
				list.add(toAdd);
				return list;
			} else {
				((ArrayList<P2Unit>) existing).add(toAdd);
				return existing;
			}
		}

		private static List<P2Unit> get(Object existing) {
			if (existing == null) {
				return Collections.emptyList();
			} else if (existing instanceof P2Unit) {
				return Collections.singletonList((P2Unit) existing);
			} else {
				return (ArrayList<P2Unit>) existing;
			}
		}

		@Override
		public int compareTo(@NotNull Providers o) {
			return name.compareTo(o.name);
		}

		@Override
		public String toString() {
			return name;
		}
	}

	private final Map<String, Map<String, Providers>> providerRegistry = new HashMap<>();

	Providers requires(String namespace, String name) {
		var perName = providerRegistry.computeIfAbsent(namespace, unused -> new HashMap<>());
		return perName.computeIfAbsent(name, unused -> new Providers(name));
	}

	void provides(String namespace, String name, P2Unit unit) {
		requires(namespace, name).add(unit);
	}

	private final Map<String, FilterImpl> filterCache = new HashMap<>();

	FilterImpl parseFilter(String filter) {
		return filterCache.computeIfAbsent(
				filter,
				f -> {
					try {
						return FilterImpl.newInstance(f);
					} catch (InvalidSyntaxException e) {
						throw new RuntimeException(e);
					}
				});
	}
}
