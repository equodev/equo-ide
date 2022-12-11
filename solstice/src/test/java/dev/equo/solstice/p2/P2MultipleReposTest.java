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

import au.com.origin.snapshots.Expect;
import au.com.origin.snapshots.junit5.SnapshotExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith({SnapshotExtension.class})
public class P2MultipleReposTest {
	private P2Session populateSession() throws Exception {
		var session = new P2Session();
		try (var client = new P2Client()) {
			session.populateFrom(client, "https://download.eclipse.org/eclipse/updates/4.26/");
			session.populateFrom(
					client, "https://download.eclipse.org/tools/cdt/releases/11.0/cdt-11.0.0/");
		}
		return session;
	}

	final ConsoleTable.Format format = ConsoleTable.Format.ASCII;

	@Test
	public void categories(Expect expect) throws Exception {
		var session = populateSession();
		var query = session.query();
		query.addAllUnits();
		expect.toMatchSnapshot(ConsoleTable.nameAndDescription(query.getCategories(), format));
	}

	@Test
	public void query(Expect expect) throws Exception {
		var session = populateSession();
		var query = session.query();
		query.resolve("202211300214.main");
		var buffer = new StringBuilder();
		buffer.append(ConsoleTable.ambiguousRequirements(query, format));
		buffer.append('\n');
		buffer.append(ConsoleTable.unmetRequirements(query, format));
		buffer.append('\n');
		buffer.append(ConsoleTable.mavenStatus(query.getJars(), format));
		buffer.append('\n');
	}
}