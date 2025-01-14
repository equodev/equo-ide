/*******************************************************************************
 * Copyright (c) 2022-2025 EquoTech, Inc. and others.
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

import static com.diffplug.selfie.Selfie.expectSelfie;

import org.junit.jupiter.api.Test;

public class ConsoleTableTest {
	private P2Query query() throws Exception {
		var session = new P2Session();
		try (var client = new P2Client()) {
			session.populateFrom(client, "https://download.eclipse.org/eclipse/updates/4.25/");
		}
		var query = session.query();
		query.addAllUnits();
		return query;
	}

	@Test
	public void detail() throws Exception {
		var query = query();
		expectSelfie(
						ConsoleTable.detail(
								query.getAllAvailableUnitsById(
										"org.eclipse.ecf.filetransfer.httpclient5.feature.feature.jar"),
								ConsoleTable.Format.ascii))
				.toMatchDisk();
	}
}
