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

public class CatalogPlatform extends Catalog {
	CatalogPlatform() {
		super(
				"platform",
				"https://download.eclipse.org/eclipse/updates/" + V,
				jre11("4.28"),
				List.of("org.eclipse.platform.ide.categoryIU"));
	}

	private static final String UI_EDITORS =
			"instance/.metadata/.plugins/org.eclipse.core.runtime/.settings/org.eclipse.ui.editors.prefs";

	/** Determines whether or not to show line numbers. */
	public void showLineNumbers(WorkspaceInit workspace, boolean showLineNumbers) {
		workspace.setProperty(UI_EDITORS, "lineNumberRuler", Boolean.toString(showLineNumbers));
	}

	/** Determines whether or not to show white space not including line endings. */
	public void showWhitespace(WorkspaceInit workspace, boolean showWhiteSpace) {
		workspace.setProperty(UI_EDITORS, "showWhitespaceCharacters", Boolean.toString(showWhiteSpace));
	}

	/** Determines whether or not to show line ending characters (carriage return/line feeds). */
	public void showLineEndings(WorkspaceInit workspace, boolean showLineEndings) {
		workspace.setProperty(UI_EDITORS, "showLineFeed", Boolean.toString(showLineEndings));
		workspace.setProperty(UI_EDITORS, "showCarriageReturn", Boolean.toString(showLineEndings));
	}
}
