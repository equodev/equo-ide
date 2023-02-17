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

import dev.equo.ide.IdeHook;
import dev.equo.solstice.p2.P2Model;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Parameter;

/** Performs arbitrary p2 resolutions. */
public abstract class AbstractP2Mojo extends AbstractMojo {
	public static class Filter {
		@Parameter private List<String> excludes = Collections.emptyList();

		@Parameter private List<String> excludePrefixes = Collections.emptyList();

		@Parameter private List<String> excludeSuffixes = Collections.emptyList();

		@Parameter private boolean platformNone = false;

		private P2Model.Filter toFilter() {
			var filter = new P2Model.Filter();
			if (platformNone) {
				filter.platformNone();
			}
			excludes.forEach(filter::exclude);
			excludePrefixes.forEach(filter::excludePrefix);
			excludeSuffixes.forEach(filter::excludeSuffix);
			return filter;
		}
	}

	@Parameter private List<Filter> filters = new ArrayList<>();

	@Parameter private List<String> p2repos = new ArrayList<>();

	@Parameter private List<String> installs = new ArrayList<>();

	protected void modifyModel(P2Model model, IdeHook.List ideHooks) {}

	protected P2Model prepareModel(IdeHook.List ideHooks) throws MojoFailureException {
		var model = new P2Model();
		modifyModel(model, ideHooks);
		p2repos.forEach(model::addP2Repo);
		installs.forEach(model.getInstall()::add);
		for (Filter filterModel : filters) {
			var filter = filterModel.toFilter();
			model.addFilterAndValidate(Integer.toString(filter.hashCode()), filter);
		}
		if (model.isEmpty()) {
			throw new MojoFailureException(
					"EquoIDE has nothing to install!\n\n"
							+ "We recommend starting with this:\n"
							+ "<configuration>\n"
							+ "  <jdt/>\n"
							+ "</configuration>\n");
		}
		model.applyNativeFilterIfNoPlatformFilter();
		return model;
	}
}
