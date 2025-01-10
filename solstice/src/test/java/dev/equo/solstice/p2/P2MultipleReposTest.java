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

import java.util.Comparator;
import org.junit.jupiter.api.Test;

public class P2MultipleReposTest {
	private P2Session populateSession() throws Exception {
		var session = new P2Session();
		try (var client = new P2Client()) {
			session.populateFrom(client, "https://download.eclipse.org/eclipse/updates/4.26/");
			session.populateFrom(
					client,
					"https://download.eclipse.org/buildship/updates/e423/releases/3.x/3.1.6.v20220511-1359/");
		}
		return session;
	}

	private P2Query queryInstall() throws Exception {
		var session = populateSession();
		var query = session.query();
		query.platformNone();
		query.install("org.eclipse.platform.ide.categoryIU");
		query.install("org.eclipse.releng.java.languages.categoryIU");
		query.install("org.eclipse.buildship.feature.group");
		return query;
	}

	final ConsoleTable.Format format = ConsoleTable.Format.ascii;

	@Test
	public void features() throws Exception {
		var session = populateSession();
		var query = session.query();
		query.addAllUnits();
		expectSelfie(ConsoleTable.nameAndDescription(query.getCategories(), format)).toMatchDisk();
	}

	@Test
	public void query() throws Exception {
		var query = queryInstall();
		var buffer = new StringBuilder();
		buffer.append(ConsoleTable.ambiguousRequirements(query, format));
		buffer.append('\n');
		buffer.append(ConsoleTable.unmetRequirements(query, format));
		buffer.append('\n');
		buffer.append(ConsoleTable.mavenStatus(query.getJars(), format));
		expectSelfie(buffer.toString().trim()).toMatchDisk();
	}

	@Test
	public void download() throws Exception {
		var query = queryInstall();
		var jars = query.getJarsNotOnMavenCentral(true);
		jars.sort(Comparator.comparing(unit -> unit.getJarUrl()));
		for (var jar : jars) {
			System.out.println(jar.getJarUrl());
		}
	}
}
