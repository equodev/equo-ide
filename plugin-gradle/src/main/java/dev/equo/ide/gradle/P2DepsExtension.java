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

import dev.equo.ide.IdeHook;
import dev.equo.ide.Launcher;
import dev.equo.solstice.NestedJars;
import dev.equo.solstice.StrippedJars;
import dev.equo.solstice.p2.P2Model;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.gradle.api.Action;
import org.gradle.api.Project;
import org.gradle.api.artifacts.Configuration;
import org.gradle.api.artifacts.ModuleDependency;

public class P2DepsExtension {
	private final Project project;

	public P2DepsExtension(Project project) {
		this.project = project;
	}

	public void into(String configuration, Action<P2ModelDslWithCatalog> p2) {
		into(project.getConfigurations().maybeCreate(configuration), p2);
	}

	public void into(Configuration configuration, Action<P2ModelDslWithCatalog> p2) {
		P2ModelDslWithCatalog dsl = new P2ModelDslWithCatalog(project);
		p2.execute(dsl);
		dsl.catalog.putInto(dsl.model, new IdeHook.List());
		dsl.model.applyNativeFilterIfNoPlatformFilter();
		dsl.model.validateFilters();
		configurations.put(configuration.getName(), dsl.model);
	}

	private final Map<String, P2Model> configurations = new HashMap<>();

	void configure() throws Exception {
		var clientCaching = P2ModelDsl.clientCaching(project);
		var queryCaching = P2ModelDsl.queryCaching(project);
		for (Map.Entry<String, P2Model> entry : configurations.entrySet()) {
			String config = entry.getKey();
			var query = entry.getValue().query(clientCaching, queryCaching);
			// add the maven deps
			for (String mavenCoord : query.getJarsOnMavenCentral()) {
				ModuleDependency dep = (ModuleDependency) project.getDependencies().add(config, mavenCoord);
				dep.setTransitive(false);
			}
			// and the p2 ones
			var nonMavenClasspath = new ArrayList<File>();
			nonMavenClasspath.addAll(query.getJarsNotOnMavenCentral());
			var classpathSorted = Launcher.copyAndSortClasspath(nonMavenClasspath);
			for (var nested : NestedJars.inFiles(classpathSorted).extractAllNestedJars()) {
				classpathSorted.add(nested.getValue());
			}
			StrippedJars.strip(classpathSorted);
			project.getDependencies().add(config, project.files(classpathSorted));
		}
	}
}
