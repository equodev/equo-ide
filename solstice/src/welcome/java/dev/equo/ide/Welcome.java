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

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

class Welcome implements IdeHookInstantiated {
	IdeHookWelcome data;

	Welcome(IdeHookWelcome data) {
		this.data = data;
	}

	@Override
	public void postStartup() {
		// terrible hack to get workbench shell
		Shell[] shells = Display.getCurrent().getShells();
		if (shells == null || shells.length == 0) {
			return;
		}
		Shell workbenchShell = shells[0];
		if (data.openUrlOnStartup != null) {
			var welcomeShell = new Shell(workbenchShell, SWT.SHELL_TRIM);
			welcomeShell.setLayout(new FillLayout());
			var browser = new Browser(welcomeShell, SWT.NONE);
			browser.setUrl(data.openUrlOnStartup);
			welcomeShell.setBounds(workbenchShell.getBounds());
			welcomeShell.open();
		}
	}
}
