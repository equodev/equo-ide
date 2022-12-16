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

	private boolean initOnly = false;

	@Option(option = "initOnly", description = "Lists the jars which were installed")
	void setInitOnly(boolean initOnly) {
		this.initOnly = initOnly;
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

		var result =
				NestedJars.javaExec(
						"dev.equo.solstice.IdeMain",
						p2AndMavenDeps.plus(nestedDefs),
						"-installDir",
						workspaceDir.getAbsolutePath(),
						"-initOnly",
						Boolean.toString(initOnly),
						"-Dorg.slf4j.simpleLogger.defaultLogLevel=INFO");
		System.out.println(result);
	}
}
