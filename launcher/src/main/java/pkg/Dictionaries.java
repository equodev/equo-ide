package pkg;

import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;

class Dictionaries {
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
			throw new UnsupportedOperationException();
		}

		@Override
		public V remove(Object key) {
			throw new UnsupportedOperationException();
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
}
