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

import org.eclipse.swt.widgets.Composite;

public class Browser extends com.equo.chromium.swt.Browser {
	public Browser(Composite parent, int style) {
		super(parent, style);
	}

	public void addOpenWindowListener(org.eclipse.swt.browser.OpenWindowListener listener) {
		super.addOpenWindowListener(listener);
	}
}
