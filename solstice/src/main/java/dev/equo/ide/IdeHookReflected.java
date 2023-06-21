/*******************************************************************************
 * Copyright (c) 2023 EquoTech, Inc. and others.
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

/**
 * Creates an IDE hook by calling the no-arg constructor of the given class. Useful for dogfood
 * testing.
 */
public class IdeHookReflected implements IdeHook {
	final String className;

	public IdeHookReflected(String className) {
		this.className = className;
	}

	@Override
	public IdeHookInstantiated instantiate() throws Exception {
		var clazz = Class.forName(className);
		var constructor = clazz.getDeclaredConstructor();
		constructor.setAccessible(true);
		IdeHook hook = (IdeHook) constructor.newInstance();
		return hook.instantiate();
	}
}
