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
package dev.equo.solstice;

import com.diffplug.common.swt.os.SwtPlatform;
import dev.equo.solstice.p2.CacheLocations;
import dev.equo.solstice.p2.JdtSetup;
import dev.equo.solstice.p2.P2Client;
import dev.equo.solstice.p2.P2Session;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class MavenJarsNeededForTest {
	/**
	 * This creates the File `nestedJarsNeededForTest` which is a list of extracted jars which are
	 * needed for the test task to run. This needs to be run if the jars in testSetupImplementation
	 * change.
	 */
	public static void main(String[] args) throws IOException {
		var session = new P2Session();
		try (var client = new P2Client()) {
			session.populateFrom(client, JdtSetup.URL_BASE + "4.25/");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		var query = session.query();
		// this could be SwtPlatfrom.getRunning(), but for CI it's important for the list to be
		// cross-platform
		query.platform(SwtPlatform.parseWsOsArch("x.x.x"));
		query.excludePrefix("org.apache.felix.gogo");
		query.excludePrefix("org.eclipse.equinox.console");
		query.excludePrefix("org.eclipse.equinox.p2");
		query.excludeSuffix(".source");
		query.install("org.eclipse.releng.java.languages.categoryIU");
		query.install("org.eclipse.platform.ide.categoryIU");
		query.install("org.eclipse.equinox.event");

		var content = new StringBuilder();
		var cacheRoot = CacheLocations.p2bundlePool().getAbsolutePath() + "/";
		try (var client = new P2Client()) {
			for (var p2 : query.getJarsNotOnMavenCentral()) {
				var path = client.download(p2).getAbsolutePath();
				if (!path.startsWith(cacheRoot)) {
					throw new IllegalArgumentException(path + " does not start with " + cacheRoot);
				}
				content.append("file ");
				content.append(path.substring(cacheRoot.length()));
				content.append('\n');
			}
		}
		for (var coordinate : query.getJarsOnMavenCentral()) {
			content.append("maven ");
			content.append(coordinate);
			content.append('\n');
		}
		Files.write(
				Paths.get("mavenJarsNeededForTest"), content.toString().getBytes(StandardCharsets.UTF_8));
	}
}
