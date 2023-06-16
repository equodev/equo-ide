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
package dev.equo.ide;

import java.util.List;

public class CatalogAssistAI extends Catalog {
	CatalogAssistAI() {
		super(
				"assistAI",
				"https://eclipse-chatgpt-plugin.lm.r.appspot.com/",
				jre17(""),
				List.of("com.github.gradusnikov.eclipse.assistai"),
				PLATFORM,
				ORBIT,
				EGIT);
	}

	private static final String SETTINGS_FILE =
			"instance/.metadata/.plugins/org.eclipse.core.runtime/.settings/com.github.gradusnikov.eclipse.plugin.assistai.main.prefs";

	public void apiKey(WorkspaceInit workspace, String apiKey) {
		workspace.setProperty(SETTINGS_FILE, "OpenAIAPIKey", apiKey);
	}

	public void modelName(WorkspaceInit workspace, String modelName) {
		workspace.setProperty(SETTINGS_FILE, "OpenAIModelName", modelName);
	}
}
