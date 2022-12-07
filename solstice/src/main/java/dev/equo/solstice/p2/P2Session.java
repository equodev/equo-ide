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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.eclipse.osgi.internal.framework.FilterImpl;
import org.osgi.framework.InvalidSyntaxException;

class P2Session {
	public void dump() {
		int[] count = new int[4];
		map.forEach(
				(namespace, nameMap) -> {
					nameMap.forEach(
							(name, pair) -> {
								int idx = pair.get().size();
								if (idx >= count.length) {
									idx = count.length - 1;
								}
								++count[idx];
							});
				});
		for (int i = 0; i < count.length; ++i) {
			System.out.println(i + "," + count[i]);
		}
	}

	public static class Providers {
		private final String name;
		private Object field;

		private Providers(String name) {
			this.name = name;
		}

		private void add(Unit unit) {
			field = add(field, unit);
		}

		public List<Unit> get() {
			return get(field);
		}

		/** FYI, profiling against Eclipse 4.25 shows that 95% of these don't need a list. */
		private static Object add(Object existing, Unit toAdd) {
			if (existing == null) {
				return toAdd;
			} else if (existing instanceof Unit) {
				var list = new ArrayList<>();
				list.add(existing);
				list.add(toAdd);
				return list;
			} else {
				((ArrayList<Unit>) existing).add(toAdd);
				return existing;
			}
		}

		private static List<Unit> get(Object existing) {
			if (existing == null) {
				return Collections.emptyList();
			} else if (existing instanceof Unit) {
				return Collections.singletonList((Unit) existing);
			} else {
				return (ArrayList<Unit>) existing;
			}
		}
	}

	private final Map<String, Map<String, Providers>> map = new HashMap<>();

	private Providers forName(String namespace, String name) {
		var perName = map.computeIfAbsent(namespace, unused -> new HashMap<>());
		return perName.computeIfAbsent(name, unused -> new Providers(name));
	}

	public Providers requires(String namespace, String name) {
		return forName(namespace, name);
	}

	public void provides(String namespace, String name, Unit unit) {
		forName(namespace, name).add(unit);
	}

	private final Map<String, FilterImpl> filterCache = new HashMap<>();

	public FilterImpl parseFilter(String filter) {
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
