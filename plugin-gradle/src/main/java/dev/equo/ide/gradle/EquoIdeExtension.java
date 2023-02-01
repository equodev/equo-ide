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
import dev.equo.ide.IdeHookBranding;
import dev.equo.ide.IdeHookBuildship;
import dev.equo.ide.IdeHookWelcome;
import dev.equo.solstice.p2.P2Client;
import dev.equo.solstice.p2.P2Model;
import dev.equo.solstice.p2.P2Query;
import org.gradle.api.Project;

/** The DSL inside the equoIde block. */
public class EquoIdeExtension extends P2ModelDsl {
	private Project project;

	public EquoIdeExtension(Project project) {
		this.project = project;
	}

	public boolean useAtomos = true;
	private final IdeHook.List ideHooks = new IdeHook.List();
	public final IdeHookBranding branding = new IdeHookBranding();

	{
		ideHooks.add(branding);
	}

	public IdeHookBranding branding() {
		return branding;
	}

	private IdeHookWelcome welcome = null;

	public IdeHookWelcome welcome() {
		if (welcome == null) {
			welcome = new IdeHookWelcome();
			ideHooks.add(welcome);
		}
		return welcome;
	}

	private static void setToDefault(P2Model model) {
		model.addP2Repo("https://download.eclipse.org/eclipse/updates/4.26/");
		model.getInstall().add("org.eclipse.releng.java.languages.categoryIU");
		model.getInstall().add("org.eclipse.platform.ide.categoryIU");

		model.addP2Repo(
				"https://download.eclipse.org/buildship/updates/e423/releases/3.x/3.1.6.v20220511-1359/");
		model.getInstall().add("org.eclipse.buildship.feature.group");

		model.addFilterAndValidate(
				"no-slf4j-nop",
				filter -> {
					filter.exclude("slf4j.nop");
					filter.exclude("org.slf4j.api");
				});
		model.addFilterAndValidate(
				"no-source",
				filter -> {
					filter.excludeSuffix(".source");
				});
	}

	IdeHook.List getIdeHooks() {
		return ideHooks;
	}

	P2Query performQuery(P2Client.Caching caching) throws Exception {
		var modelToQuery = model;
		if (modelToQuery.isEmpty()) {
			modelToQuery = modelToQuery.deepCopy();
			setToDefault(modelToQuery);
			ideHooks.add(
					new IdeHookBuildship(
							project.getProjectDir(), project.getGradle().getStartParameter().isOffline()));
		}
		modelToQuery.applyNativeFilterIfNoPlatformFilter();
		return modelToQuery.query(caching);
	}
}
