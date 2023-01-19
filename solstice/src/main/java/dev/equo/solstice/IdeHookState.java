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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public interface IdeHookState extends Serializable {
	class List extends ArrayList<IdeHookState> {}

	class Instantiated extends ArrayList<IdeHook> {
		Instantiated(List list) {
			for (int i = 0; i < list.size(); ++i) {
				add(list.get(i).instantiate());
			}
		}

		void callEach(Consumer<IdeHook> method) {
			for (IdeHook hook : this) {
				method.accept(hook);
			}
		}

		<T> void callEach(BiConsumer<IdeHook, T> method, T arg) {
			for (IdeHook hook : this) {
				method.accept(hook, arg);
			}
		}
	}

	IdeHook instantiate();
}
