/*******************************************************************************
 * Copyright (c) 2022-2023 EquoTech, Inc. and others.
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
	private P2Session populateSession(P2ClientCache caching) throws Exception {
		var session = new P2Session();
		try (var client = new P2Client(caching)) {
			session.populateFrom(client, "https://download.eclipse.org/eclipse/updates/4.25/");
		}
		return session;
	}

	@Test
	public void queryOffline(Expect expect) throws Exception {
		queryImpl(expect, P2ClientCache.ALLOW_OFFLINE);
		queryImpl(expect, P2ClientCache.OFFLINE);
	}

	private void queryImpl(Expect expect, P2ClientCache caching) throws Exception {
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
		var session = populateSession(P2ClientCache.PREFER_OFFLINE);
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

	@Test
	public void weirdUpdateSites(Expect expect) throws Exception {
		listCategories("https://bndtools.jfrog.io/bndtools/update-latest/", expect.scenario("bnd"));
		listCategories(
				"https://groovy.jfrog.io/artifactory/plugins-release/org/codehaus/groovy/groovy-eclipse-integration/4.8.0/e4.26/",
				expect.scenario("groovy"));
		listCategories("https://download.eclipse.org/eclipse/updates/4.28/", expect.scenario("4.28"));
	}

	private void listCategories(String url, Expect expect) throws Exception {
		var session = new P2Session();
		try (var client = new P2Client(P2ClientCache.PREFER_OFFLINE)) {
			session.populateFrom(client, url);
		}
		var query = session.query();
		query.addAllUnits();
		expect.toMatchSnapshot(
				ConsoleTable.nameAndDescription(query.getCategories(), ConsoleTable.Format.csv));
	}
}
