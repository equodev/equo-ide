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

import dev.equo.solstice.p2.P2Client;
import dev.equo.solstice.p2.P2Model;
import dev.equo.solstice.p2.QueryCache;
import org.gradle.api.Action;
import org.gradle.api.Project;

/** The DSL for defining a P2Model. */
public class P2ModelDsl {
	protected final P2Model model;

	public P2ModelDsl(P2Model model) {
		this.model = model;
	}

	public P2ModelDsl() {
		this(new P2Model());
	}

	/** Adds the given p2 repo to the list of repositories to populate the session with. */
	public void p2repo(String p2) {
		model.addP2Repo(p2);
	}

	/** Marks the given unit id for installation. */
	public void install(String target) {
		model.getInstall().add(target);
	}

	/** Adds a filter with the given name. Duplicate names are not allowed. */
	public void addFilter(String name, Action<P2Model.Filter> filterSetup) {
		P2Model.Filter filter = new P2Model.Filter();
		filterSetup.execute(filter);
		model.addFilterAndValidate(name, filter);
	}

	/** Removes the filter with the given name. Throws an error if no such filter exists. */
	public void removeFilter(String name) {
		model.removeFilter(name);
	}

	/** Removes all filters. */
	public void clearFilters() {
		model.getFilters().clear();
	}

	static P2Client.Caching clientCaching(Project project) {
		return P2Client.Caching.defaultIfOfflineIs(project.getGradle().getStartParameter().isOffline());
	}

	static QueryCache queryCaching(Project project) {
		boolean forceRecalculate =
				EquoIdeGradlePlugin.anyArgEquals(project, CLEAN_FLAG)
						|| EquoIdeGradlePlugin.anyArgEquals(project, REFRESH_DEPENDENCIES);
		return forceRecalculate ? QueryCache.FORCE_RECALCULATE : QueryCache.ALLOW;
	}

	private static final String CLEAN_FLAG = "--clean";
	private static final String REFRESH_DEPENDENCIES = "--refresh-dependencies";
}
