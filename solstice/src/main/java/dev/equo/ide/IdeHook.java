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
package dev.equo.ide;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public interface IdeHook extends Serializable {
	class List extends ArrayList<IdeHook> {
		public <T> T get(Class<T> clazz) {
			for (int i = 0; i < size(); ++i) {
				if (clazz.isInstance(get(i))) {
					return (T) get(i);
				}
			}
			throw new IllegalArgumentException("Does not contain " + clazz);
		}

		InstantiatedList instantiate() {
			var copy = new ArrayList<IdeHookInstantiated>();
			for (int i = 0; i < size(); ++i) {
				copy.add(get(i).instantiate());
			}
			return new InstantiatedList(copy);
		}
	}

	class InstantiatedList {
		private final ArrayList<IdeHookInstantiated> list;

		private InstantiatedList(ArrayList<IdeHookInstantiated> list) {
			this.list = list;
		}

		void forEach(Consumer<IdeHookInstantiated> method) {
			for (IdeHookInstantiated hook : list) {
				method.accept(hook);
			}
		}

		<T> void forEach(BiConsumer<IdeHookInstantiated, T> method, T arg) {
			for (IdeHookInstantiated hook : list) {
				method.accept(hook, arg);
			}
		}

		Iterator<IdeHookInstantiated> iterator() {
			return list.iterator();
		}
	}

	IdeHookInstantiated instantiate();
}
