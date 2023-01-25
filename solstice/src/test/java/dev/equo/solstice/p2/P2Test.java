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
	private P2Session populateSession(P2Client.Caching caching) throws Exception {
		var session = new P2Session();
		try (var client = new P2Client(caching)) {
			session.populateFrom(client, "https://download.eclipse.org/eclipse/updates/4.25/");
		}
		return session;
	}

	@Test
	public void queryOffline(Expect expect) throws Exception {
		queryImpl(expect, P2Client.Caching.ALLOW_OFFLINE);
		queryImpl(expect, P2Client.Caching.OFFLINE);
	}

	private void queryImpl(Expect expect, P2Client.Caching caching) throws Exception {
		var session = populateSession(caching);
		var query = session.query();
		query.exclude("a.jre.javase");
		query.exclude("org.eclipse.platform_root");
		query.exclude("org.eclipse.rcp_root");
		query.excludePrefix("tooling");
		query.install("org.eclipse.platform.ide.categoryIU");
		expect.toMatchSnapshot(ConsoleTable.mavenStatus(query.getJars(), ConsoleTable.Format.ascii));
	}

	@Test
	public void queryPlatformSpecific(Expect expect) throws Exception {
		var session = populateSession(P2Client.Caching.ALLOW_OFFLINE);
		var query = session.query();
		query.install("org.eclipse.swt");
		expect
				.scenario("all-platforms")
				.toMatchSnapshot(ConsoleTable.mavenStatus(query.getJars(), ConsoleTable.Format.ascii));

		var macQuery = session.query();
		macQuery.platform(SwtPlatform.parseWsOsArch("cocoa.macosx.aarch64"));
		macQuery.install("org.eclipse.swt");
		expect
				.scenario("mac-only")
				.toMatchSnapshot(ConsoleTable.mavenStatus(macQuery.getJars(), ConsoleTable.Format.ascii));
	}
}
