/*******************************************************************************
 * Copyright (c) 2023-2025 EquoTech, Inc. and others.
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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class P2ModelTest {
	@Test
	public void toStringTest() {
		var model = new P2Model();
		expectSelfie(model.toString()).toMatchDisk("empty");

		model.getP2repo().add("https://download.eclipse.org/eclipse/updates/4.26/");
		expectSelfie(model.toString()).toMatchDisk("p2 single");

		model
				.getP2repo()
				.add(
						"https://download.eclipse.org/buildship/updates/e423/releases/3.x/3.1.6.v20220511-1359/");
		expectSelfie(model.toString()).toMatchDisk("p2 multiple");

		model.getInstall().add("org.eclipse.platform.ide.categoryIU");
		expectSelfie(model.toString()).toMatchDisk("install single");

		model.getInstall().add("org.eclipse.releng.java.languages.categoryIU");
		model.getInstall().add("org.eclipse.buildship.feature.group");
		expectSelfie(model.toString()).toMatchDisk("install multiple");

		var filter = new P2Model.Filter();
		expectSelfie(filter.toString()).toMatchDisk("filter empty");

		filter.getExclude().add("exclude.me");
		expectSelfie(filter.toString()).toMatchDisk("filter exclude");

		filter.getExcludePrefix().add("exclude.prefix");
		expectSelfie(filter.toString()).toMatchDisk("filter prefix exclude");

		filter.getExcludeSuffix().add("exclude.suffix");
		expectSelfie(filter.toString()).toMatchDisk("filter suffix exclude");

		filter.getProps().put("red", "255,0,0");
		expectSelfie(filter.toString()).toMatchDisk("filter props single");

		filter.getProps().put("green", "0,255,0");
		filter.getProps().put("blue", "0,0,255");
		expectSelfie(filter.toString()).toMatchDisk("filter props multiple");
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
