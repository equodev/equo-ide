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
package org.eclipse.swt.browser;

public interface OpenWindowListener extends com.equo.chromium.swt.OpenWindowListener {

	public void open(org.eclipse.swt.browser.WindowEvent event);

	default void open(com.equo.chromium.swt.WindowEvent event) {
		org.eclipse.swt.browser.WindowEvent event2 =
				new org.eclipse.swt.browser.WindowEvent(event.widget);

		event2.addressBar = event.addressBar;
		event2.browser = (Browser) event.browser;
		event2.data = event.data;
		event2.display = event.display;
		event2.location = event.location;
		event2.menuBar = event.menuBar;
		event2.required = event.required;
		event2.size = event.size;
		event2.statusBar = event.statusBar;
		event2.time = event.time;
		event2.toolBar = event.toolBar;

		open(event2);
	}
}
