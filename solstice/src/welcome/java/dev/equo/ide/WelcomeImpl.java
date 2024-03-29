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
package dev.equo.ide;

import dev.equo.ide.ui.PartDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.ui.PlatformUI;

class WelcomeImpl implements IdeHookInstantiated {
	IdeHookWelcome data;

	WelcomeImpl(IdeHookWelcome data) {
		this.data = data;
	}

	@Override
	public void preStartup() throws Exception {
		if (data.perspective != null) {
			PlatformUI.getWorkbench().getPerspectiveRegistry().setDefaultPerspective(data.perspective);
		}
	}

	@Override
	public void postStartup() {
		if (data.openUrl == null) {
			return;
		}
		PartDescriptor.create(
						"Welcome",
						parentCmp -> {
							parentCmp.setLayout(new FillLayout());
							var browser = new Browser(parentCmp, SWT.NONE);
							browser.setUrl(data.openUrl);
						})
				.openOnActivePage();
	}
}
