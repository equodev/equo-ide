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
package dev.equo.ide.gradle;

import dev.equo.solstice.JavaLaunch;
import dev.equo.solstice.NestedJars;
import dev.equo.solstice.p2.P2Client;
import dev.equo.solstice.p2.P2Query;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.stream.Collectors;
import javax.inject.Inject;
import org.gradle.api.DefaultTask;
import org.gradle.api.file.FileCollection;
import org.gradle.api.model.ObjectFactory;
import org.gradle.api.provider.Property;
import org.gradle.api.tasks.Internal;
import org.gradle.api.tasks.TaskAction;
import org.gradle.api.tasks.options.Option;

public abstract class EquoIdeTask extends DefaultTask {
	@Internal
	public abstract Property<P2Client.Caching> getCaching();

	@Internal
	public abstract Property<P2Query> getQuery();

	@Internal
	public abstract Property<FileCollection> getMavenDeps();

	@Internal
	public abstract Property<File> getWorkspaceDir();

	@Internal
	public abstract Property<Boolean> getUseAtomos();

	private boolean initOnly = false;

	@Option(
			option = "init-only",
			description = "Initializes the runtime to check for errors then exits.")
	void setInitOnly(boolean initOnly) {
		this.initOnly = initOnly;
	}

	private boolean dontUseAtomosOverride = false;

	@Option(
			option = "dont-use-atomos",
			description = "Initializes the runtime to check for errors then exits.")
	void dontUseAtomos(boolean dontUseAtomos) {
		this.dontUseAtomosOverride = dontUseAtomos;
	}

	@Inject
	public abstract ObjectFactory getObjectFactory();

	@TaskAction
	public void launch() throws IOException, InterruptedException {
		var mavenDeps = getMavenDeps().get();

		var p2files = new ArrayList<File>();
		var query = getQuery().get();
		try (var client = new P2Client(getCaching().get())) {
			for (var unit : query.getJarsNotOnMavenCentral()) {
				p2files.add(client.download(unit));
			}
		}

		var p2deps = getObjectFactory().fileCollection().from(p2files);
		var p2AndMavenDeps = p2deps.plus(mavenDeps);

		var workspaceDir = getWorkspaceDir().get();
		var nestedJarFolder = new File(workspaceDir, NestedJars.DIR);
		var nestedJars =
				NestedJars.inFiles(p2AndMavenDeps).extractAllNestedJars(nestedJarFolder).stream()
						.map(e -> e.getValue())
						.collect(Collectors.toList());
		var nestedDefs = getObjectFactory().fileCollection().from(nestedJars);

		boolean sameJVM = initOnly;
		boolean useAtomos = dontUseAtomosOverride ? false : getUseAtomos().get();
		var exitCode =
				JavaLaunch.launch(
						sameJVM,
						"dev.equo.solstice.IdeMain",
						p2AndMavenDeps.plus(nestedDefs),
						"-installDir",
						workspaceDir.getAbsolutePath(),
						"-useAtomos",
						Boolean.toString(useAtomos),
						"-initOnly",
						Boolean.toString(initOnly),
						"-Dorg.slf4j.simpleLogger.defaultLogLevel=INFO");
		if (sameJVM) {
			System.out.println("exit code: " + exitCode);
		}
	}
}
