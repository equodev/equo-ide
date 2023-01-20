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

import java.util.Arrays;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class BrandingIdeHook implements IdeHook {
	@Override
	public IdeHookInstantiated instantiate() {
		return new Instantiated();
	}

	class Instantiated implements IdeHookInstantiated {
		private Shell splash;

		@Override
		public void afterDisplay(org.eclipse.swt.widgets.Display display) {
			var cursor = display.getCursorLocation();
			var monitors = display.getMonitors();
			var bestMonitor =
					Arrays.stream(monitors)
							.filter(monitor -> monitor.getBounds().contains(cursor))
							.findAny()
							.orElse(monitors[0])
							.getBounds();

			int imgX = 1000;
			int imgY = 1000;
			splash = new Shell(display, SWT.NO_TRIM);
			splash.setText("Branding");
			splash.setBounds(
					bestMonitor.x + (bestMonitor.width - imgX) / 2,
					bestMonitor.y + (bestMonitor.height - imgY) / 2,
					imgX,
					imgY);
			splash.setBackground(display.getSystemColor(SWT.COLOR_GREEN));
			splash.open();

			Display.setAppName("Branding");
			while (display.readAndDispatch())
				// pump the event loop enough to show the branding
				;
		}

		@Override
		public void postStartup() {
			splash.dispose();
			splash = null;

			Shell[] shells = Display.getCurrent().getShells();
			for (var shell : shells) {
				shell.setText("Branding");
			}
		}
	}
}
