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

import dev.equo.solstice.p2.JdtSetup;
import dev.equo.solstice.p2.P2Client;
import dev.equo.solstice.p2.P2Model;
import dev.equo.solstice.p2.P2Query;
import org.gradle.api.Action;
import org.gradle.api.GradleException;

/** The DSL inside the equoIde block. */
public class EquoIdeExtension {
	public boolean useAtomos = true;
	private final P2Model model;

	public EquoIdeExtension() {
		useAtomos = true;
		model = new P2Model();
	}

	private EquoIdeExtension(EquoIdeExtension existing) {
		useAtomos = existing.useAtomos;
		model = existing.model.deepCopy();
	}

	private EquoIdeExtension deepCopy() {
		return new EquoIdeExtension(this);
	}

	private void setToDefault() {
		release(JdtSetup.DEFAULT_VERSION);
	}

	/** Sets which eclipse JDT release to use, such as "4.25", "4.26", or a future release. */
	public void release(String version) {
		if (version.indexOf('/') != -1) {
			throw new IllegalArgumentException("Version should not have any slashes");
		}
		p2repo("https://download.eclipse.org/eclipse/updates/" + version + "/");
		install("org.eclipse.releng.java.languages.categoryIU");
		install("org.eclipse.platform.ide.categoryIU");
		addFilter(
				"JDT " + version,
				query -> {
					query.excludePrefix("org.apache.felix.gogo");
					query.excludePrefix("org.eclipse.equinox.console");
					query.excludePrefix("org.eclipse.equinox.p2");
					query.excludeSuffix(".source");
				});
	}

	/** Adds the given p2 repo to the list of repositories to populate the session with. */
	public void p2repo(String p2) {
		if (!p2.endsWith("/")) {
			throw new GradleException(
					"Must end with /\n"
							+ "p2repo(\""
							+ p2
							+ "\")   <- WRONG\n"
							+ "p2repo(\""
							+ p2
							+ "/\")  <- CORRECT\n");
		}
		if (p2.endsWith(("//"))) {
			throw new GradleException(
					"Must end with a single /\n"
							+ "p2repo(\""
							+ p2
							+ "\")  <- WRONG\n"
							+ "p2repo(\""
							+ p2.substring(0, p2.length() - 1)
							+ "\")   <- CORRECT\n");
		}
		model.getP2repo().add(p2);
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

	P2Query performQuery(P2Client.Caching caching) throws Exception {
		var extension = this;
		if (model.isEmpty()) {
			extension = deepCopy();
			extension.setToDefault();
		}
		P2Model model = extension.model.deepCopy();
		if (!model.hasAnyPlatformFilter()) {
			model.addFilterAndValidate("platform running", new P2Model.Filter().platformRunning());
		}
		return model.query(caching);
	}
}
