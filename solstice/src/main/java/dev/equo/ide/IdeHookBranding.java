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

import java.io.File;
import javax.annotation.Nullable;

@SuppressWarnings("serial")
public class IdeHookBranding implements IdeHook {
	private static final String DEFAULT_TITLE = "Equo IDE";
	String title = DEFAULT_TITLE;
	@Nullable File icon;
	@Nullable File splash;

	public IdeHookBranding title(String title) {
		this.title = title == null ? DEFAULT_TITLE : title;
		return this;
	}

	public IdeHookBranding icon(File iconImg) {
		this.icon = iconImg;
		return this;
	}

	public IdeHookBranding splash(File splashImg) {
		this.splash = splashImg;
		return this;
	}

	@Override
	public IdeHookInstantiated instantiate() throws Exception {
		return IdeHook.usingReflection("dev.equo.ide.BrandingImpl", this);
	}
}
