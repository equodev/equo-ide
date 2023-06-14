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

import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.WorkbenchWindow;

@SuppressWarnings("serial")
public class IdeHookAssistAI implements IdeHook {
	@Override
	public IdeHookInstantiated instantiate() {
		return new Instantiated();
	}

	class Instantiated implements IdeHookInstantiated {
		@Override
		public void postStartup() throws Exception {
			var workbench = PlatformUI.getWorkbench();
			var application = ((org.eclipse.e4.ui.workbench.IWorkbench) workbench).getApplication();
			for (var descriptor : application.getDescriptors()) {
				if (descriptors.contains(descriptor.getElementId())) {
					var model = ((WorkbenchWindow) workbench.getActiveWorkbenchWindow()).getModel();
					var epartService = model.getContext().get(EPartService.class);
					var part =
							epartService.showPart(descriptor.getElementId(), EPartService.PartState.CREATE);
					epartService.showPart(part, EPartService.PartState.VISIBLE);
				}
			}
		}
	}

	static final java.util.List<String> descriptors =
			java.util.List.of(
					"assitai.partdescriptor.chatgptview", "assistai.partdescriptor.chatgptview");
}
