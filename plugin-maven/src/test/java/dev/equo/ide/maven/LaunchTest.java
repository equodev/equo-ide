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

import dev.equo.ide.ResourceHarness;
import dev.equo.solstice.Launcher;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.jar.Attributes;
import java.util.jar.Manifest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

public class LaunchTest extends ResourceHarness {
	@Test
	public void integrationTestAtomos() throws IOException, InterruptedException {
		integrationTestUseAtomos(true);
	}

	@Test
	public void integrationTestSolstice() throws IOException, InterruptedException {
		integrationTestUseAtomos(false);
	}

	private void integrationTestUseAtomos(boolean useAtomos)
			throws IOException, InterruptedException {
		setFile("pom.xml").toResource("/dev/equo/ide/maven/pom.xml");
		var outputBytes =
				new ProcessRunner(rootFolder())
						.exec(
								"mvn",
								"dev.equo.ide:equo-ide-maven-plugin:" + pluginVersion() + ":launch",
								"-DinitOnly=true",
								"-DuseAtomos=" + useAtomos)
						.stdOut();
		var output = new String(outputBytes, StandardCharsets.UTF_8);
		Assertions.assertThat(output).matches("(?s)(.*)Loaded (\\d+) bundles(.*)");
		Assertions.assertThat(output).contains(useAtomos ? "using Atomos" : "not using Atomos");
	}

	@Disabled
	@Test
	public void integrationTestReal() throws IOException, InterruptedException {
		setFile("pom.xml").toResource("/dev/equo/ide/maven/pom.xml");
		Launcher.launchAndInheritIO(
				rootFolder(),
				Arrays.asList("mvn", "dev.equo.ide:equo-ide-maven-plugin:" + pluginVersion() + ":launch"));
	}

	private static String pluginVersion() throws IOException {
		var solsticeJar =
				LaunchMojo.class.getResource(LaunchMojo.class.getSimpleName() + ".class").toString();
		if (solsticeJar.startsWith("jar")) {
			var url = new URL(solsticeJar);
			var jarConnection = (JarURLConnection) url.openConnection();
			var manifest = jarConnection.getManifest();
			return manifest.getMainAttributes().getValue("Implementation-Version");
		} else {
			var file = new File("build/tmp/jar/MANIFEST.MF");
			try (var stream = new FileInputStream(file)) {
				var manifest = new Manifest(stream);
				return manifest
						.getMainAttributes()
						.get(new Attributes.Name("Implementation-Version"))
						.toString();
			}
		}
	}
}
