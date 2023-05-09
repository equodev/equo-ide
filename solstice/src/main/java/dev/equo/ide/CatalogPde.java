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

import java.util.List;

public class CatalogPde extends Catalog {
	CatalogPde() {
		super("pde", PLATFORM, List.of("org.eclipse.releng.pde.categoryIU"), JDT);
	}

	public void missingApiBaseline(WorkspaceInit workspace, String value) {
		String filename =
				"instance/.metadata/.plugins/org.eclipse.core.runtime/.settings/org.eclipse.pde.api.tools.prefs";
		workspace.setProperty(filename, "missing_default_api_profile", value);
		workspace.setProperty(filename, "missing_plugin_in_baseline", value);
	}
}
