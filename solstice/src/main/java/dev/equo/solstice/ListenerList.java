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
import java.util.Iterator;
import java.util.Objects;
import java.util.function.Predicate;

/**
 * A special list implementation optimized for ServiceRegistry listeners. It is backed by an
 * append-only ArrayList. "Removed" elements are simply nulled-out. This allows elements to be added
 * and removed during iteration without conflict, at the expense of a larger-than-necessary listener
 * list.
 *
 * <p>For an IDE with JDT, PDE, and GradleBuildship, the list reaches a length of 201 entries, with
 * 11 of the entries being null = 5% wastage. Not bad.
 */
class ListenerList<T> implements Iterable<T> {
	private ArrayList<T> backing = new ArrayList<>();

	@Override
	public Iterator<T> iterator() {
		return new Iterator<T>() {
			int idx = 0;
			// snapshot the backing size so that we don't iterate over entries which get added after we
			// started
			final int size = backing.size();

			@Override
			public boolean hasNext() {
				// bump idx until the backing array has a non-null value
				while (idx < size && backing.get(idx) == null) {
					++idx;
				}
				return idx < size;
			}

			@Override
			public void remove() {
				backing.set(idx - 1, null);
			}

			@Override
			public T next() {
				return Objects.requireNonNull(backing.get(idx++));
			}
		};
	}

	public boolean add(T e) {
		backing.add(Objects.requireNonNull(e));
		return true;
	}

	public void removeIf(Predicate<T> removeIf) {
		for (int i = 0; i < backing.size(); ++i) {
			var e = backing.get(i);
			if (e != null && removeIf.test(e)) {
				backing.set(i, null);
			}
		}
	}
}
