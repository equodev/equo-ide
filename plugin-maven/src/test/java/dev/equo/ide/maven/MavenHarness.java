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

import com.diffplug.common.swt.os.OS;
import dev.equo.ide.ResourceHarness;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.Attributes;
import java.util.jar.Manifest;
import org.junit.jupiter.api.BeforeEach;

public class MavenHarness extends ResourceHarness {
	@BeforeEach
	void copyPluginUnderTestMetadata() throws IOException {
		setFile(".mvn/wrapper/maven-wrapper.jar").toResource("/maven-wrapper.jar");
		setFile(".mvn/wrapper/maven-wrapper.properties").toResource("/maven-wrapper.properties");
		setFile("mvnw").toResource("/mvnw").setExecutable(true);
		setFile("mvnw.cmd").toResource("/mvnw.cmd");
	}

	protected void setPom(String configuration) throws IOException {
		setFile("pom.xml")
				.toResourceProcessed(
						"/dev/equo/ide/maven/pom.xml",
						raw -> {
							return raw.replace("{{version}}", pluginVersion())
									.replace("{{configuration}}", configuration);
						});
	}

	protected String mvnw(String argsAfterMvnw) throws IOException, InterruptedException {
		List<String> args = new ArrayList<>();
		if (OS.getRunning().isWindows()) {
			args.add("cmd");
			args.add("/S");
			args.add("/C");
			args.add("mvnw.cmd " + argsAfterMvnw);
		} else {
			args.add("/bin/sh");
			args.add("-c");
			args.add("./mvnw " + argsAfterMvnw);
		}
		var outputBytes = new ProcessRunner(rootFolder()).exec(args).stdOut();
		return new String(outputBytes, StandardCharsets.UTF_8);
	}

	private static String pluginVersion() {
		try {
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
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	}
}