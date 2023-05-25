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

public class CatalogAssistAI extends Catalog {
	CatalogAssistAI() {
		super(
				"assistAI",
				"https://eclipse-chatgpt-plugin.lm.r.appspot.com/",
				jre11(""),
				List.of("com.github.gradusnikov.eclipse.assistai"),
				PLATFORM,
				EGIT);
	}
}
