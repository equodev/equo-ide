/*******************************************************************************
 * Copyright (c) 2022-2023 EquoTech, Inc. and others.
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
import javax.annotation.Nullable;
import org.eclipse.osgi.internal.framework.FilterImpl;
import org.jetbrains.annotations.NotNull;
import org.osgi.framework.Filter;
import org.osgi.framework.InvalidSyntaxException;

/** In-memory store of all p2 metadata, especially provides/requires dependency information. */
public class P2Session {
	final List<P2Unit> units = new ArrayList<>();

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

		@Nullable
		Filter getFilter();
		// the properties below are for both requirements and providers
		String getNamespace();

		String getName();

		boolean hasOnlyOneProvider();

		P2Unit getOnlyProvider();

		List<P2Unit> getProviders();

		/** Returns a non-optional form of the requirement. */
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

	/**
	 * In terms of design {@link RequirementRoot} represents both a mandatory requirement on a
	 * capability and also the matching ability to provide that capability.
	 *
	 * <p>{@link RequirementModified} delegates everything about describing that capability to {@link
	 * RequirementRoot}, but layers on top the ability to add detail to the requirement (specifically
	 * that it is optional).
	 */
	@SuppressWarnings("unchecked")
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

		@Nullable
		@Override
		public Filter getFilter() {
			return null;
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
			if (namespace.equals("java.package")) {
				return "pkg " + name;
			} else if (namespace.equals("osgi.bundle")) {
				return "bundle " + name;
			} else if (namespace.equals("org.eclipse.equinox.p2.iu")) {
				return "iu " + name;
			} else {
				return namespace + " " + name;
			}
		}
	}

	private static class RequirementModified implements Requirement {
		final RequirementRoot root;
		final boolean isOptional;
		final @Nullable Filter filter;

		RequirementModified(RequirementRoot root, boolean isOptional, @Nullable Filter filter) {
			this.root = root;
			this.isOptional = isOptional;
			this.filter = filter;
		}

		@Override
		public boolean isOptional() {
			return isOptional;
		}

		@Nullable
		@Override
		public Filter getFilter() {
			return filter;
		}

		@Override
		public String toString() {
			if (isOptional) {
				if (filter != null) {
					return root + " (opt) " + filter;
				} else {
					return root + " (opt)";
				}
			} else {
				return root + " " + filter;
			}
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
	}

	private final Map<String, Map<String, RequirementRoot>> requirements = new HashMap<>();

	private RequirementRoot requires(String namespace, String name) {
		var perName = requirements.computeIfAbsent(namespace, unused -> new HashMap<>());
		return perName.computeIfAbsent(name, n -> new RequirementRoot(namespace, n));
	}

	Requirement requires(
			String namespace, String name, boolean optional, @Nullable FilterImpl filter) {
		var root = requires(namespace, name);
		if (!optional && filter == null) {
			return root;
		} else {
			return new RequirementModified(root, optional, filter);
		}
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
						throw Unchecked.wrap(e);
					}
				});
	}
}
