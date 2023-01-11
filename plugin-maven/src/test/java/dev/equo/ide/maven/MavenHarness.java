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
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

public class MavenHarness extends ResourceHarness {
	protected void setPom(String configuration) throws IOException {
		setFile("pom.xml")
				.toResourceProcessed(
						"/dev/equo/ide/maven/pom.xml",
						raw -> {
							return raw.replace("{{version}}", pluginVersion())
									.replace("{{configuration}}", configuration);
						});
	}

	protected String mvnw(String... args) throws IOException, InterruptedException {
		String[] argsFinal = new String[args.length + 2];
		argsFinal[0] = "mvn";
		argsFinal[1] = "-q";
		System.arraycopy(args, 0, argsFinal, 2, args.length);
		var outputBytes =
				new ProcessRunner(rootFolder())
						.exec(argsFinal)
						.stdOut();
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
