package pkg;

import java.util.Collection;
import java.util.Collections;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Objects;

class Dictionaries {
	private static class EnumerationOf<T> implements Enumeration<T> {
		private final Iterator<T> iterator;

		EnumerationOf(Collection<T> collection) {
			this(collection.iterator());
		}

		EnumerationOf(Iterator<T> iterator) {
			this.iterator = iterator;
		}

		@Override
		public boolean hasMoreElements() {
			return iterator.hasNext();
		}

		@Override
		public T nextElement() {
			return iterator.next();
		}

		public Iterator<T> asIterator() {
			return iterator;
		}

		static final Enumeration<Object> EMPTY = new EnumerationOf<Object>(Collections.emptyIterator());

		static <T> Enumeration<T> empty() {
			return (Enumeration<T>) EMPTY;
		}
	}

	private static class SingleDictionary<K, V> extends Dictionary<K, V> {
		K k;
		V v;

		SingleDictionary(K k, V v) {
			this.k = k;
			this.v = v;
		}

		@Override
		public int size() {
			return 1;
		}

		@Override
		public boolean isEmpty() {
			return false;
		}

		@Override
		public Enumeration<K> keys() {
			return new EnumerationOf<>(Collections.singleton(k));
		}

		@Override
		public Enumeration<V> elements() {
			return new EnumerationOf<>(Collections.singleton(v));
		}

		@Override
		public V get(Object key) {
			if (Objects.equals(k, key)) {
				return v;
			} else {
				return null;
			}
		}

		@Override
		public V put(K key, V value) {
			throw new UnsupportedOperationException();
		}

		@Override
		public V remove(Object key) {
			throw new UnsupportedOperationException();
		}
	}

	private static class EmptyDictionary<K, V> extends Dictionary<K, V> {
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
			return EnumerationOf.empty();
		}

		@Override
		public Enumeration<V> elements() {
			return EnumerationOf.empty();
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
	}

	public static <K, V> Dictionary<K, V> empty() {
		return new EmptyDictionary<>();
	}

	public static <K, V> Dictionary<K, V> of(K k, V v) {
		return new SingleDictionary<>(k, v);
	}
}
