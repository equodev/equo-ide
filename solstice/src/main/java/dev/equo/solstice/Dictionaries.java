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

import java.util.Arrays;
import java.util.Collections;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;

class Dictionaries {
	static <T> Enumeration<T> enumeration(T... values) {
		return Collections.enumeration(Arrays.asList(values));
	}

	private static class EmptyDictionary<K, V> extends Dictionary<K, V> {
		private final Hashtable<K, V> backing = new Hashtable<>();

		@Override
		public int size() {
			return 0;
		}

		@Override
		public boolean isEmpty() {
			return true;
		}

		@Override
		public Enumeration<K> keys() {
			return backing.keys();
		}

		@Override
		public Enumeration<V> elements() {
			return backing.elements();
		}

		@Override
		public V get(Object key) {
			return null;
		}

		@Override
		public V put(K key, V value) {
			throw Unimplemented.onPurpose();
		}

		@Override
		public V remove(Object key) {
			throw Unimplemented.onPurpose();
		}

		@Override
		public int hashCode() {
			return backing.hashCode();
		}

		@Override
		public boolean equals(Object other) {
			return backing.equals(other);
		}

		private static final EmptyDictionary INSTANCE = new EmptyDictionary();
	}

	public static <K, V> Dictionary<K, V> empty() {
		return EmptyDictionary.INSTANCE;
	}

	public static <K, V> Dictionary<K, V> of(K k, V v) {
		var table = new Hashtable<K, V>();
		table.put(k, v);
		return table;
	}

	public static <K, V> Dictionary<K, V> of(K k1, V v1, K k2, V v2) {
		var table = new Hashtable<K, V>();
		table.put(k1, v1);
		table.put(k2, v2);
		return table;
	}

	public static <K, V> Dictionary<K, V> copy(Dictionary<K, V> in) {
		if (in.isEmpty()) {
			return empty();
		} else {
			var table = new Hashtable<K, V>();
			var keys = in.keys();
			while (keys.hasMoreElements()) {
				var key = keys.nextElement();
				table.put(key, in.get(key));
			}
			return table;
		}
	}
}
