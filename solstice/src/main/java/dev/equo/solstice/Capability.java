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
package dev.equo.solstice;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents an OSGi capability as a namespace plus N key-value pairs. Order is significant for
 * {@link #equals(Object)} and {@link #compareTo(Capability)}, but not for {@link
 * #isSubsetOf(Capability)} and {@link #isSupersetOf(Capability)}. If you are putting them in a map
 * or set, you probably want to use {@link SupersetMap} or {@link SupersetSet}.
 */
public class Capability implements Comparable<Capability> {
	static final String LIST_STR = ":List<String>";
	static final Set<String> IGNORED_NAMESPACES = Set.of("osgi.ee");
	static final Set<String> IGNORED_ATTRIBUTES = Set.of("version:Version");

	final String namespace;
	final ArrayList<String> keyValue = new ArrayList<>(2);

	public Capability(String namespace, String key, String value) {
		this(namespace);
		add(key, value);
	}

	public Capability(String namespace) {
		this.namespace = namespace;
	}

	public void add(String key, String value) {
		keyValue.add(key);
		keyValue.add(value);
	}

	public int size() {
		return keyValue.size() / 2;
	}

	@Override
	public int compareTo(@NotNull Capability o) {
		int result = namespace.compareTo(o.namespace);
		if (result != 0) {
			return result;
		}
		for (int i = 0; i < Math.min(keyValue.size(), o.keyValue.size()); ++i) {
			result = keyValue.get(i).compareTo(o.keyValue.get(i));
			if (result != 0) {
				return result;
			}
		}
		return keyValue.size() - o.keyValue.size();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		return o instanceof Capability ? compareTo((Capability) o) == 0 : false;
	}

	@Override
	public int hashCode() {
		return Objects.hash(namespace, keyValue);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(namespace);
		builder.append(':');
		for (int i = 0; i < keyValue.size() / 2; ++i) {
			builder.append(keyValue.get(2 * i));
			builder.append('=');
			builder.append(keyValue.get(2 * i + 1));
			builder.append(',');
		}
		builder.setLength(builder.length() - 1); // remove the trailing comma
		return builder.toString();
	}

	private Capability swap(int... indices) {
		Capability copy = new Capability(namespace);
		for (int i = 0; i < indices.length; ++i) {
			copy.add(keyValue.get(2 * indices[i]), keyValue.get(2 * indices[i] + 1));
		}
		return copy;
	}

	/** Order-insignificant matching to determine if this Capability is a subset of its argument. */
	public boolean isSubsetOf(Capability other) {
		return other.isSupersetOf(this);
	}

	/** Order-insignificant matching to determine if this Capability is a superset of its argument. */
	public boolean isSupersetOf(Capability other) {
		if (size() >= other.size()) {
			return __isSubsetMatch(other, this);
		} else {
			return false;
		}
	}

	private static boolean __isSubsetMatch(Capability shorter, Capability longer) {
		for (int i = 0; i < shorter.keyValue.size() / 2; ++i) {
			var key = shorter.keyValue.get(2 * i);
			var value = shorter.keyValue.get(2 * i + 1);

			var longerKeyIdx = longer.keyValue.indexOf(key);
			if (longerKeyIdx == -1) {
				// missing key
				return false;
			}
			if (longerKeyIdx % 2 == 1) {
				throw Unimplemented.onPurpose(
						"Key has the same content as a value, unlikely to ever happen, straight-forward to fix if it does, please file an issue at https://github.com/equodev/equo-ide");
			}
			var longerValue = longer.keyValue.get(longerKeyIdx + 1);
			if (!value.equals(longerValue)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * A map whose keys are {@link Capability}, and you can quickly retrieve entries which match
	 * superset of the {@link Capability} you request.
	 */
	public static class SupersetMap<T> {
		private @Nullable Capability lookingForSupersetOf;
		private final TreeMap<Capability, T> map =
				new TreeMap<>(
						(a, b) -> {
							if (lookingForSupersetOf == null) {
								return a.compareTo(b);
							} else {
								if (lookingForSupersetOf == a && b.isSupersetOf(a)) {
									return 0;
								} else if (lookingForSupersetOf == b && a.isSupersetOf(b)) {
									return 0;
								} else {
									return a.compareTo(b);
								}
							}
						});
		private final TreeMap<Capability, T> toStringMap = new TreeMap<>();

		/** Adds the given entry. */
		public void put(Capability cap, T value) {
			if (lookingForSupersetOf != null) {
				throw new IllegalStateException();
			}
			toStringMap.put(cap, value);
			/*
			 * <pre>
			 * var set = new TreeSet<Capability>();
			 * set.add(Capability.parse("namespace:name=foo,class=bar"))
			 * set.contains(Capability.parse("Capability.parse("namespace:name=foo)))   // subset matching says this should be true, and it is, huzzah!
			 * set.contains(Capability.parse("Capability.parse("namespace:class=bar)))  // subset matching says this should be true, but key ordering says it is not (class=bar is less than name=foo)
			 * </pre>
			 *
			 * The example above works perfect for capabilities with only one property. For capabilities with two
			 * properties, we can work around the problem by doing additions in both possible orders
			 *
			 * <pre>
			 * set.add(Capability.parse("namespace:name=foo,class=bar"))
			 * set.add(Capability.parse("namespace:class=bar,name=foo"))
			 * </pre>
			 *
			 * But this workaround is combinatoric. For a capability with three properties, we have to add it in
			 * 3! = 6 different orders. We implemented this combinatoric by hardcoding the cases for 1, 2, 3 and
			 * we error out on 4 or higher.
			 */
			switch (cap.size()) {
				case 0:
					throw new IllegalArgumentException("Must have at least one key/value");
				case 1:
					map.put(cap, value);
					break;
				case 2:
					map.put(cap, value);
					// total.add(cap.swap(0, 1)); // same as just cap
					map.put(cap.swap(1, 0), value);
					break;
				case 3:
					map.put(cap, value);
					// map.put(cap.swap(0, 1, 2), value); // same as just cap
					map.put(cap.swap(0, 2, 1), value);
					map.put(cap.swap(1, 0, 2), value);
					map.put(cap.swap(1, 2, 0), value);
					map.put(cap.swap(2, 0, 1), value);
					map.put(cap.swap(2, 1, 0), value);
					break;
				default:
					throw Unimplemented.onPurpose(
							"Solstice only supports Capabilities with at most 3 properties, see Capabilities javadoc for how to remove this limitation");
			}
		}

		/** Returns the first entry whose capability is a superset of the given argument. */
		public T getAnySupersetOf(Capability cap) {
			if (lookingForSupersetOf != null) {
				throw new IllegalStateException();
			}
			lookingForSupersetOf = cap;
			T value = map.get(cap);
			lookingForSupersetOf = null;
			return value;
		}

		@Override
		public String toString() {
			return toStringMap.toString();
		}
	}

	/**
	 * A set of {@link Capability} where you can quickly retrieve entries which match superset of the
	 * {@link Capability} you request. Backed by {@link SupersetMap}.
	 */
	public static class SupersetSet {
		private final SupersetMap<Capability> map = new SupersetMap<>();

		public void add(Capability cap) {
			map.put(cap, cap);
		}

		public void addAll(Iterable<Capability> capProvides) {
			capProvides.forEach(this::add);
		}

		public Capability getAnySupersetOf(Capability cap) {
			return map.getAnySupersetOf(cap);
		}

		public boolean containsAnySupersetOf(Capability cap) {
			return getAnySupersetOf(cap) != null;
		}

		@Override
		public String toString() {
			return map.toStringMap.keySet().toString();
		}
	}
}
