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
package dev.equo.solstice.p2;

import au.com.origin.snapshots.Expect;
import au.com.origin.snapshots.junit5.SnapshotExtension;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith({SnapshotExtension.class})
public class P2ModelTest {
	@Test
	public void toString(Expect expect) {
		var model = new P2Model();
		expect.scenario("empty").toMatchSnapshot(model.toString());

		model.getP2repo().add("https://download.eclipse.org/eclipse/updates/4.26/");
		expect.scenario("p2 single").toMatchSnapshot(model.toString());

		model
				.getP2repo()
				.add(
						"https://download.eclipse.org/buildship/updates/e423/releases/3.x/3.1.6.v20220511-1359/");
		expect.scenario("p2 multiple").toMatchSnapshot(model.toString());

		model.getInstall().add("org.eclipse.platform.ide.categoryIU");
		expect.scenario("install single").toMatchSnapshot(model.toString());

		model.getInstall().add("org.eclipse.releng.java.languages.categoryIU");
		model.getInstall().add("org.eclipse.buildship.feature.group");
		expect.scenario("install multiple").toMatchSnapshot(model.toString());

		var filter = new P2Model.Filter();
		expect.scenario("filter empty").toMatchSnapshot(filter.toString());

		filter.getExclude().add("exclude.me");
		expect.scenario("filter exclude").toMatchSnapshot(filter.toString());

		filter.getExcludePrefix().add("exclude.prefix");
		expect.scenario("filter prefix exclude").toMatchSnapshot(filter.toString());

		filter.getExcludeSuffix().add("exclude.suffix");
		expect.scenario("filter suffix exclude").toMatchSnapshot(filter.toString());

		filter.getProps().put("red", "255,0,0");
		expect.scenario("filter props single").toMatchSnapshot(filter.toString());

		filter.getProps().put("green", "0,255,0");
		filter.getProps().put("blue", "0,0,255");
		expect.scenario("filter props multiple").toMatchSnapshot(filter.toString());
	}

	@Test
	public void cache() {
		var model = new P2Model();
		model.addP2Repo("https://download.eclipse.org/eclipse/updates/4.26/");
		model.getInstall().add("org.eclipse.releng.java.languages.categoryIU");
		model.getInstall().add("org.eclipse.platform.ide.categoryIU");
		var result = model.query(P2ClientCache.PREFER_OFFLINE, P2QueryCache.FORCE_RECALCULATE);
		var mavenCentralCoordinates = result.getJarsOnMavenCentral();
		var downloadedJars = result.getJarsNotOnMavenCentral();
		Assertions.assertFalse(mavenCentralCoordinates.isEmpty());
		Assertions.assertFalse(downloadedJars.isEmpty());
		result = model.query(P2ClientCache.PREFER_OFFLINE, P2QueryCache.ALLOW);
		Assertions.assertEquals(result.getJarsOnMavenCentral(), mavenCentralCoordinates);
		Assertions.assertEquals(result.getJarsNotOnMavenCentral(), downloadedJars);
	}
}
