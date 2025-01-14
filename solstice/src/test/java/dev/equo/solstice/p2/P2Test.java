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

import com.diffplug.common.swt.os.SwtPlatform;
import com.diffplug.selfie.StringSelfie;
import org.junit.jupiter.api.Test;

public class P2Test {
	private P2Session populateSession(P2ClientCache caching) throws Exception {
		var session = new P2Session();
		try (var client = new P2Client(caching)) {
			session.populateFrom(client, "https://download.eclipse.org/eclipse/updates/4.25/");
		}
		return session;
	}

	@Test
	public void queryOffline() throws Exception {
		queryImpl(P2ClientCache.ALLOW_OFFLINE).toMatchDisk("ALLOW_OFFLINE");
		queryImpl(P2ClientCache.OFFLINE).toMatchDisk("OFFLINE");
	}

	private StringSelfie queryImpl(P2ClientCache caching) throws Exception {
		var session = populateSession(caching);
		var query = session.query();
		query.exclude("a.jre.javase");
		query.exclude("org.eclipse.platform_root");
		query.exclude("org.eclipse.rcp_root");
		query.excludePrefix("tooling");
		query.install("org.eclipse.platform.ide.categoryIU");
		return expectSelfie(ConsoleTable.mavenStatus(query.getJars(), ConsoleTable.Format.ascii));
	}

	@Test
	public void queryPlatformSpecific() throws Exception {
		var session = populateSession(P2ClientCache.PREFER_OFFLINE);
		var query = session.query();
		query.install("org.eclipse.swt");
		expectSelfie(ConsoleTable.mavenStatus(query.getJars(), ConsoleTable.Format.ascii))
				.toMatchDisk("all-platforms");

		var macQuery = session.query();
		macQuery.platform(SwtPlatform.parseWsOsArch("cocoa.macosx.aarch64"));
		macQuery.install("org.eclipse.swt");
		expectSelfie(ConsoleTable.mavenStatus(macQuery.getJars(), ConsoleTable.Format.ascii))
				.toMatchDisk("mac-only");
	}

	@Test
	public void weirdUpdateSites() throws Exception {
		listCategories("https://bndtools.jfrog.io/bndtools/update-latest/").toMatchDisk("bnd");
		listCategories(
						"https://groovy.jfrog.io/artifactory/plugins-release/org/codehaus/groovy/groovy-eclipse-integration/4.8.0/e4.26/")
				.toMatchDisk("groovy");
		listCategories("https://download.eclipse.org/eclipse/updates/4.28/").toMatchDisk("4.28");
	}

	private StringSelfie listCategories(String url) throws Exception {
		var session = new P2Session();
		try (var client = new P2Client(P2ClientCache.PREFER_OFFLINE)) {
			session.populateFrom(client, url);
		}
		var query = session.query();
		query.addAllUnits();
		return expectSelfie(
				ConsoleTable.nameAndDescription(query.getCategories(), ConsoleTable.Format.csv));
	}
}
