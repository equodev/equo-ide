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
package dev.equo.ide.maven;

import com.diffplug.selfie.StringSelfie;
import java.io.IOException;
import org.junit.jupiter.api.Test;

public class MavenCatalogTest extends MavenHarness {
	private StringSelfie test(String content) throws IOException, InterruptedException {
		setPom(
				content
						+ "\n"
						+ "<filters>\n"
						+ "  <filter><platformNone>true</platformNone></filter>\n"
						+ "</filters>\n");
		return mvnw("equo-ide:list -Drequest")
				.snapshotBetween("(default-cli) @ equo-maven-test-harness ---", "[INFO] BUILD SUCCESS");
	}

	@Test
	public void simple() throws Exception {
		test("<platform><version>4.27</version></platform><jdt/>").toMatchDisk("jdt");
		test("<platform><version>4.27</version></platform><gradleBuildship/>")
				.toMatchDisk("gradleBuildship");
	}

	@Test
	public void versionOverride() throws IOException, InterruptedException {
		test("<jdt><version>4.25</version></jdt>").toMatchDisk("jdt-spec");
		test("<platform/><jdt><version>4.25</version></jdt>").toMatchDisk("platform-neutral-jdt-spec");
		test("<platform><version>4.25</version></platform><jdt/>").toMatchDisk("platform-spec");
		test("<platform><version>4.25</version></platform><jdt><version>4.25</version></jdt>")
				.toMatchDisk("both-spec");
	}

	@Test
	public void urlOverride() throws IOException, InterruptedException {
		test("<jdt><url>http://test.url/</url></jdt>").toMatchDisk();
	}
}
