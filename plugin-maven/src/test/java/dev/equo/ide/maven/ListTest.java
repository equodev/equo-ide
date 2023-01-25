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
package dev.equo.ide.maven;

import au.com.origin.snapshots.Expect;
import au.com.origin.snapshots.junit5.SnapshotExtension;
import java.io.IOException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith({SnapshotExtension.class})
public class ListTest extends MavenHarness {
	@Test
	public void help(Expect expect) throws IOException, InterruptedException {
		setPom("");
		mvnw("help:describe -Dcmd=equo-ide:list -Ddetail")
				.snapshotBetween("Mojo: 'equo-ide:list'", "[INFO] BUILD SUCCESS", expect);
	}

	@Test
	public void argErrorCheck(Expect expect) throws IOException, InterruptedException {
		setPom("");
		mvnw("equo-ide:list")
				.snapshotBetween(
						" on project equo-maven-test-harness: ", "-> [Help 1]", expect.scenario("no args"));
		mvnw("equo-ide:list -Dinstalled -Dproblems")
				.snapshotBetween(
						" on project equo-maven-test-harness: ", "-> [Help 1]", expect.scenario("multi args"));
	}

	@Test
	public void defaultP2(Expect expect) throws IOException, InterruptedException {
		setPom("");
		mvnw("equo-ide:list -Dinstalled")
				.snapshotBetween(
						"(default-cli) @ equo-maven-test-harness ---",
						"[INFO] BUILD SUCCESS",
						expect.scenario("installed"));
		mvnw("equo-ide:list -Dproblems")
				.snapshotBetween(
						"(default-cli) @ equo-maven-test-harness ---",
						"[INFO] BUILD SUCCESS",
						expect.scenario("problems"));
		mvnw("equo-ide:list -Doptional")
				.snapshotBetween(
						"(default-cli) @ equo-maven-test-harness ---",
						"[INFO] BUILD SUCCESS",
						expect.scenario("optional"));
	}
}
