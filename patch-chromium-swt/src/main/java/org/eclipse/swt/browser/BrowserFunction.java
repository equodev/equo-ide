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
package org.eclipse.swt.browser;

import com.equo.chromium.swt.Browser;

public class BrowserFunction extends com.equo.chromium.swt.BrowserFunction {

	public Browser browser;

	public BrowserFunction(Browser browser, String name) {
		super(browser, name);
		this.browser = browser;
	}

	public BrowserFunction(Browser browser, String name, boolean top, String[] frameNames) {
		super(browser, name, top, frameNames);
		this.browser = browser;
	}
}
