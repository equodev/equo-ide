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
import org.eclipse.osgi.internal.framework.FilterImpl;
import org.jetbrains.annotations.NotNull;
import org.osgi.framework.InvalidSyntaxException;

/** In-memory store of all p2 metadata, especially provides/requires dependency information. */
public class P2Session {
	List<P2Unit> units = new ArrayList<>();

	/** Adds every {@link P2Unit} from the given url into this session. */
	public void populateFrom(P2Client client, String url) throws Exception {
		client.addUnits(this, url);
		sort();
	}

	private void sort() {
		units.sort(Comparator.naturalOrder());
		for (var namespace : requirements.values()) {
			for (RequirementRoot requirement : namespace.values()) {
				requirement.sortProviders();
			}
		}
	}

	/**
	 * Returns the unit with the given id. If there are multiple units with the same id, returns the
	 * one with the greatest version number. If there are none, throws an exception.
	 */
	public P2Unit getUnitById(String id) {
		for (var unit : units) {
			if (id.equals(unit.id)) {
				return unit;
			}
		}
		throw new IllegalArgumentException("No such unit id " + id);
	}

	/** Creates a new {@link P2Query} against this session. */
	public P2Query query() {
		return new P2Query(this);
	}

	/** Keeps track of every unit which provides the given capability. */
	public interface Requirement extends Comparable<P2Session.Requirement> {
		// these properties are per-requirement-specific
		boolean isOptional();
		// the properties below are for both requirements and providers
		String getNamespace();

		String getName();

		boolean hasOnlyOneProvider();

		P2Unit getOnlyProvider();

		List<P2Unit> getProviders();

		Requirement getRoot();

		@Override
		default int compareTo(@NotNull Requirement o) {
			int byNamespace = getNamespace().compareTo(o.getNamespace());
			if (byNamespace != 0) {
				return byNamespace;
			}
			int byName = getName().compareTo(o.getName());
			if (byName != 0) {
				return byName;
			}
			return toString().compareTo(o.toString());
		}
	}

	private static class RequirementOptional implements Requirement {
		final RequirementRoot root;

		RequirementOptional(RequirementRoot root) {
			this.root = root;
		}

		@Override
		public boolean isOptional() {
			return true;
		}

		// methods below this are all pure delegation
		@Override
		public String getNamespace() {
			return root.getNamespace();
		}

		@Override
		public String getName() {
			return root.getName();
		}

		@Override
		public boolean hasOnlyOneProvider() {
			return root.hasOnlyOneProvider();
		}

		@Override
		public P2Unit getOnlyProvider() {
			return root.getOnlyProvider();
		}

		@Override
		public List<P2Unit> getProviders() {
			return root.getProviders();
		}

		@Override
		public Requirement getRoot() {
			return root;
		}

		@Override
		public String toString() {
			return root.toString() + " (opt)";
		}
	}

	private static class RequirementRoot implements Requirement {
		private final String namespace;
		private final String name;
		private Object providers;

		private RequirementRoot(String namespace, String name) {
			this.namespace = namespace;
			this.name = name;
		}

		@Override
		public boolean isOptional() {
			return false;
		}

		@Override
		public String getNamespace() {
			return namespace;
		}

		@Override
		public String getName() {
			return name;
		}

		private void add(P2Unit unit) {
			providers = add(providers, unit);
		}

		@Override
		public boolean hasOnlyOneProvider() {
			return providers instanceof P2Unit;
		}

		@Override
		public P2Unit getOnlyProvider() {
			return (P2Unit) providers;
		}

		@Override
		public List<P2Unit> getProviders() {
			return get(providers);
		}

		@Override
		public Requirement getRoot() {
			return this;
		}

		private void sortProviders() {
			if (providers instanceof ArrayList) {
				((ArrayList<P2Unit>) providers).sort(Comparator.naturalOrder());
			}
		}

		/** FYI, profiling against Eclipse 4.25 shows that 95% of these don't need a list. */
		private static Object add(Object existing, P2Unit toAdd) {
			if (existing == null) {
				return toAdd;
			} else if (existing instanceof P2Unit) {
				var list = new ArrayList<P2Unit>();
				list.add((P2Unit) existing);
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
		public String toString() {
			return namespace + ":" + name;
		}
	}

	private final Map<String, Map<String, RequirementRoot>> requirements = new HashMap<>();

	private RequirementRoot requires(String namespace, String name) {
		var perName = requirements.computeIfAbsent(namespace, unused -> new HashMap<>());
		return perName.computeIfAbsent(name, n -> new RequirementRoot(namespace, n));
	}

	Requirement requires(String namespace, String name, boolean optional) {
		var root = requires(namespace, name);
		return optional ? new RequirementOptional(root) : root;
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
