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

import dev.equo.solstice.p2.P2Client;
import dev.equo.solstice.p2.P2Model;
import dev.equo.solstice.p2.P2Query;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Parameter;

public abstract class AbstractP2Mojo extends AbstractMojo {
	public static class Filter {
		@Parameter private List<String> excludes = Collections.emptyList();

		@Parameter private List<String> excludePrefixes = Collections.emptyList();

		@Parameter private List<String> excludeSuffixes = Collections.emptyList();

		@Parameter(defaultValue = "false")
		boolean platformNone;

		private P2Model.Filter toFilter() {
			var filter = new P2Model.Filter();
			excludes.forEach(filter::exclude);
			excludePrefixes.forEach(filter::excludePrefix);
			excludeSuffixes.forEach(filter::excludeSuffix);
			if (platformNone) {
				filter.platformNone();
			}
			return filter;
		}
	}

	@Parameter private List<Filter> filters = new ArrayList<>();

	@Parameter private List<String> p2repos = new ArrayList<>();

	@Parameter private List<String> installs = new ArrayList<>();

	protected P2Query query() throws MojoFailureException {
		var model = new P2Model();
		p2repos.forEach(model::addP2Repo);
		installs.forEach(model.getInstall()::add);
		for (Filter filterModel : filters) {
			var filter = filterModel.toFilter();
			model.addFilterAndValidate(Integer.toString(filter.hashCode()), filter);
		}
		if (model.isEmpty()) {
			setToDefault(model);
		}
		model.applyNativeFilterIfNoPlatformFilter();
		try {
			return model.query(P2Client.Caching.ALLOW_OFFLINE);
		} catch (Exception e) {
			throw new MojoFailureException(e.getMessage(), e);
		}
	}

	private void setToDefault(P2Model model) {
		model.addP2Repo("https://download.eclipse.org/eclipse/updates/4.26/");
		model.getInstall().add("org.eclipse.releng.java.languages.categoryIU");
		model.getInstall().add("org.eclipse.platform.ide.categoryIU");
	}
}
