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
package dev.equo.ide.ui;

import java.util.function.Consumer;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

/** Creates Eclipse parts using only code, no metadata. */
public class PartDescriptor {
	private final Consumer<Composite> coat;
	private ImageDescriptor tabIcon;
	private final String tabName;
	private String toolTipText;

	public static PartDescriptor create(String tabName, Consumer<Composite> coat) {
		return new PartDescriptor(tabName, coat);
	}

	private PartDescriptor(String tabName, Consumer<Composite> coat) {
		this.tabName = tabName;
		this.toolTipText = tabName;
		this.coat = coat;
	}

	public PartDescriptor tabIcon(ImageDescriptor tabIcon) {
		this.tabIcon = tabIcon;
		return this;
	}

	public PartDescriptor toolTipText(String toolTipText) {
		this.toolTipText = toolTipText;
		return this;
	}

	public void openOn(IWorkbenchPage page) {
		try {
			page.openEditor(new Input(), PartDescriptorHelper.EDITOR_ID);
		} catch (PartInitException e) {
			throw new RuntimeException(e);
		}
	}

	public void openOnActivePage() {
		var workbench = PlatformUI.getWorkbench();
		var window = workbench.getActiveWorkbenchWindow();
		openOn(window.getActivePage());
	}

	class Input implements IEditorInput {
		@Override
		public boolean exists() {
			return false;
		}

		@Override
		public ImageDescriptor getImageDescriptor() {
			return tabIcon;
		}

		@Override
		public String getName() {
			return tabName;
		}

		@Override
		public String getToolTipText() {
			return toolTipText;
		}

		@Override
		public IPersistableElement getPersistable() {
			return null;
		}

		@Override
		public <T> T getAdapter(Class<T> adapter) {
			return null;
		}

		void createPartControl(Composite parentCmp) {
			coat.accept(parentCmp);
		}
	}
}
