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
		int[] count = new int[1000];
		map.forEach(
				(namespace, nameMap) -> {
					nameMap.forEach(
							(name, pair) -> {
								++count[pair.getRequires().size()];
								++count[pair.getProvides().size()];
							});
				});
		for (int i = 0; i < count.length; ++i) {
			System.out.println(i + "," + count[i]);
		}
	}

	static class Pair {
		private Object provides;
		private Object requires;

		void addProvides(Unit unit) {
			provides = add(provides, unit);
		}

		void addRequires(Unit unit) {
			requires = add(requires, unit);
		}

		/** FYI, profiling against Eclips 4.25 p2 shows that 57% of all pair fields don't need a List. */
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

		public List<Unit> getProvides() {
			return get(provides);
		}

		public List<Unit> getRequires() {
			return get(requires);
		}
	}

	private final Map<String, Map<String, Pair>> map = new HashMap<>();

	private Pair forName(String namespace, String name) {
		var perName = map.computeIfAbsent(namespace, unused -> new HashMap<>());
		return perName.computeIfAbsent(name, unused -> new Pair());
	}

	public void requires(String namespace, String name, Unit unit) {
		forName(namespace, name).addRequires(unit);
	}

	public void provides(String namespace, String name, Unit unit) {
		forName(namespace, name).addProvides(unit);
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
