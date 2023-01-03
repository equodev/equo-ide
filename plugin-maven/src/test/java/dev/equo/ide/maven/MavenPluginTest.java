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

import dev.equo.solstice.JavaLaunch;
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

public class MavenPluginTest {
	@Test
	public void integrationTest() throws IOException, InterruptedException {
		setFile("pom.xml").toResource("/dev/equo/ide/maven/pom.xml");
		var result =
				new ProcessRunner(rootFolder())
						.exec(
								"mvn",
								"dev.equo.ide:equo-ide-maven-plugin:" + pluginVersion() + ":launch",
								"-DinitOnly=true");
		System.out.println(new String(result.stdOut(), StandardCharsets.UTF_8));
		Assertions.assertThat(result.exitCode()).isEqualTo(0);
		Assertions.assertThat(result.stdOut())
				.asString(StandardCharsets.UTF_8)
				.matches("(?s)(.*)Loaded (\\d+) bundles(.*)");
	}

	@Disabled
	@Test
	public void integrationTestReal() throws IOException, InterruptedException {
		setFile("pom.xml").toResource("/dev/equo/ide/maven/pom.xml");
		JavaLaunch.launchAndInheritIO(
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

	/**
	 * On OS X, the temp folder is a symlink, and some of gradle's stuff breaks symlinks. By only
	 * accessing it through the {@link #rootFolder()} and {@link #newFile(String)} apis, we can
	 * guarantee there will be no symlink problems.
	 */
	@TempDir File folderDontUseDirectly;

	/** Returns the root folder (canonicalized to fix OS X issue) */
	protected File rootFolder() throws IOException {
		return folderDontUseDirectly.getCanonicalFile();
	}

	protected WriteAsserter setFile(String path) throws IOException {
		return new WriteAsserter(newFile(path));
	}

	/** Returns a new child of the root folder. */
	protected File newFile(String subpath) throws IOException {
		return new File(rootFolder(), subpath);
	}

	protected static class WriteAsserter {
		private File file;

		private WriteAsserter(File file) {
			file.getParentFile().mkdirs();
			this.file = file;
		}

		public File toContent(byte[] content) throws IOException {
			Files.write(file.toPath(), content);
			return file;
		}

		public File toResource(String path) throws IOException {
			try (var input = MavenPluginTest.class.getResource(path).openConnection().getInputStream()) {
				return toContent(input.readAllBytes());
			}
		}
	}
}
