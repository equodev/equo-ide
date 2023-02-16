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
import dev.equo.solstice.p2.P2Client;
import dev.equo.solstice.p2.P2Model;
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

	public void into(String configuration, Action<P2ModelDslWithFeatures> p2) {
		into(project.getConfigurations().maybeCreate(configuration), p2);
	}

	public void into(Configuration configuration, Action<P2ModelDslWithFeatures> p2) {
		P2ModelDslWithFeatures dsl = new P2ModelDslWithFeatures(project);
		p2.execute(dsl);
		dsl.features.putInto(dsl.model, new IdeHook.List());
		dsl.model.applyNativeFilterIfNoPlatformFilter();
		dsl.model.validateFilters();
		configurations.put(configuration.getName(), dsl.model);
	}

	private final Map<String, P2Model> configurations = new HashMap<>();

	void configure() throws Exception {
		var caching = P2ModelDsl.caching(project);
		for (Map.Entry<String, P2Model> entry : configurations.entrySet()) {
			String config = entry.getKey();
			var query = entry.getValue().query(caching);
			for (String mavenCoord : query.getJarsOnMavenCentral()) {
				ModuleDependency dep = (ModuleDependency) project.getDependencies().add(config, mavenCoord);
				dep.setTransitive(false);
			}
			var localFiles = new ArrayList<>();
			try (var client = new P2Client(caching)) {
				for (var unit : query.getJarsNotOnMavenCentral()) {
					localFiles.add(client.download(unit));
				}
			}
			project.getDependencies().add(config, project.files(localFiles));
		}
	}
}
