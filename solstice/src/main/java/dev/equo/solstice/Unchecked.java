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
package dev.equo.solstice;

import java.util.function.Predicate;

class Unchecked {
	static boolean anyMatches(Throwable t, Predicate<Throwable> toMatch) {
		if (toMatch.test(t)) {
			return true;
		}
		for (var s : t.getSuppressed()) {
			if (anyMatches(s, toMatch)) {
				return true;
			}
		}
		if (t.getCause() != null) {
			return anyMatches(t.getCause(), toMatch);
		} else {
			return false;
		}
	}

	static Class<?> classForName(String className) {
		try {
			return Class.forName(className);
		} catch (ClassNotFoundException e) {
			throw wrap(e);
		}
	}

	static RuntimeException wrap(Exception e) {
		if (e instanceof RuntimeException) {
			return (RuntimeException) e;
		} else {
			return new RuntimeException(e);
		}
	}

	static <T> T get(ThrowingSupplier<T> supplier) {
		try {
			return supplier.get();
		} catch (Exception e) {
			throw wrap(e);
		}
	}

	interface ThrowingSupplier<T> {
		T get() throws Exception;
	}
}
