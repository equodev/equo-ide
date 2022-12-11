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
import com.diffplug.common.swt.os.SwtPlatform;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith({SnapshotExtension.class})
public class P2Test {
	private P2Session populateSession() throws Exception {
		var session = new P2Session();
		try (var client = new P2Client()) {
			session.populateFrom(client, "https://download.eclipse.org/eclipse/updates/4.25/");
		}
		return session;
	}

	@Test
	public void query(Expect expect) throws Exception {
		var session = populateSession();
		var query = session.query();
		query.exclude("a.jre.javase");
		query.exclude("org.eclipse.platform_root");
		query.exclude("org.eclipse.rcp_root");
		query.excludePrefix("tooling");
		query.resolve("org.eclipse.platform.ide.categoryIU");
		expect.toMatchSnapshot(ConsoleTable.mavenStatus(query.getJars(), ConsoleTable.Format.ASCII));
	}

	@Test
	public void queryPlatformSpecific(Expect expect) throws Exception {
		var session = populateSession();
		var query = session.query();
		query.resolve("org.eclipse.swt");
		expect
				.scenario("all-platforms")
				.toMatchSnapshot(ConsoleTable.mavenStatus(query.getJars(), ConsoleTable.Format.ASCII));

		var macQuery = session.query();
		macQuery.setPlatform(SwtPlatform.parseWsOsArch("cocoa.macosx.aarch64"));
		macQuery.resolve("org.eclipse.swt");
		expect
				.scenario("mac-only")
				.toMatchSnapshot(ConsoleTable.mavenStatus(macQuery.getJars(), ConsoleTable.Format.ASCII));
	}
}
