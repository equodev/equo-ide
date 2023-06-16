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
package dev.equo.solstice.p2;

class Unchecked {
	interface ThrowingConsumer<T> {
		void accept(T input) throws Exception;
	}

	static RuntimeException wrap(Exception e) {
		if (e instanceof RuntimeException) {
			return (RuntimeException) e;
		} else {
			return new RuntimeException(e);
		}
	}
}
