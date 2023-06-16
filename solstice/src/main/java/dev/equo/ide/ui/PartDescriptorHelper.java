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

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.part.EditorPart;

/** NOT FOR END-USER USAGE, use {@link PartDescriptor}. */
public class PartDescriptorHelper extends EditorPart {
	static final String EDITOR_ID = "dev.equo.ide.ui.PartDescriptorHelper";

	@Override
	public void createPartControl(Composite parentCmp) {
		var input = (PartDescriptor.Input) getEditorInput();
		input.createPartControl(parentCmp);
	}

	@Override
	public void init(IEditorSite site, IEditorInput input) {
		setSite(site);
		setInput(input);

		var inputCast = (PartDescriptor.Input) getEditorInput();
		setPartName(inputCast.getName());
		var image = inputCast.getImageDescriptor();
		if (image == null) {
			setTitleImage(null);
		} else {
			setTitleImage(image.createImage());
		}
		setTitleToolTip(inputCast.getToolTipText());
	}

	@Override
	public final boolean isSaveAsAllowed() {
		return false;
	}

	@Override
	public boolean isDirty() {
		return false;
	}

	@Override
	public void doSave(IProgressMonitor monitor) {}

	@Override
	public void doSaveAs() {}

	@Override
	public void setFocus() {}
}
