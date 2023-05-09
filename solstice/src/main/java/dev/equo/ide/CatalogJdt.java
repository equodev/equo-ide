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

public class CatalogJdt extends Catalog {
	CatalogJdt() {
		super("jdt", PLATFORM, List.of("org.eclipse.releng.java.languages.categoryIU"), PLATFORM);
	}

	/** Adds a compiler class path variable. */
	public void classpathVariable(WorkspaceInit workspace, String name, String value) {
		String JDT_CORE_PREFS =
				"instance/.metadata/.plugins/org.eclipse.core.runtime/.settings/org.eclipse.jdt.core.prefs";
		workspace.setProperty(JDT_CORE_PREFS, "org.eclipse.jdt.core.classpathVariable." + name, value);
	}
}
