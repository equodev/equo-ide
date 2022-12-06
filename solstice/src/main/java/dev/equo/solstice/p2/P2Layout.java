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
package dev.equo.solstice.p2;

import java.io.File;
import java.io.IOException;

public class P2Layout {
	public static final String root = "https://download.eclipse.org/eclipse/updates/";

	public static void main(String[] args) throws IOException, P2Client.NotFoundException {
		var cacheDir = new File("build/cache");
		try (var client = new P2Client(cacheDir)) {
			client.open(root + "4.25/");
		}
	}
}
