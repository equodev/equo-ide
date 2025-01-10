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

import java.io.IOException;
import org.junit.jupiter.api.Test;

public class ListTest extends MavenHarness {
	@Test
	public void help() throws IOException, InterruptedException {
		setPom("");
		mvnw("help:describe -Dcmd=equo-ide:list -Ddetail")
				.snapshotBetween("Mojo: 'equo-ide:list'", "[INFO] BUILD SUCCESS")
				.toMatchDisk();
	}

	@Test
	public void argErrorCheck() throws IOException, InterruptedException {
		setPom("");
		mvnw("equo-ide:list")
				.snapshotBetween(" on project equo-maven-test-harness: ", "-> [Help 1]")
				.toMatchDisk("no args");
		mvnw("equo-ide:list -Dinstalled -Dproblems")
				.snapshotBetween(" on project equo-maven-test-harness: ", "-> [Help 1]")
				.toMatchDisk("multi args");
	}

	@Test
	public void defaultP2() throws IOException, InterruptedException {
		setPom(
				""
						+ "<p2repos>\n"
						+ "  <p2repo>https://download.eclipse.org/eclipse/updates/4.26/</p2repo>\n"
						+ "</p2repos>\n"
						+ "<installs>\n"
						+ "  <install>org.eclipse.platform.ide.categoryIU</install>\n"
						+ "</installs>\n"
						+ "<filters>\n"
						+ "  <filter><platformNone>true</platformNone></filter>\n"
						+ "</filters>\n"
						+ "");
		mvnw("equo-ide:list -Dinstalled")
				.snapshotBetween("(default-cli) @ equo-maven-test-harness ---", "[INFO] BUILD SUCCESS")
				.toMatchDisk("installed");
		mvnw("equo-ide:list -Dproblems")
				.snapshotBetween("(default-cli) @ equo-maven-test-harness ---", "[INFO] BUILD SUCCESS")
				.toMatchDisk("problems");
		mvnw("equo-ide:list -Doptional")
				.snapshotBetween("(default-cli) @ equo-maven-test-harness ---", "[INFO] BUILD SUCCESS")
				.toMatchDisk("optional");
	}
}
