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

import com.diffplug.common.swt.os.SwtPlatform;
import dev.equo.solstice.p2.JdtSetup;
import dev.equo.solstice.p2.P2Client;
import dev.equo.solstice.p2.P2Query;
import dev.equo.solstice.p2.P2Session;
import java.util.LinkedHashSet;
import java.util.Set;
import org.gradle.api.Action;
import org.gradle.api.GradleException;

/** The DSL inside the equoIde block. */
public class EquoIdeExtension {
	private Set<String> repos = new LinkedHashSet<>();
	private Set<String> targets = new LinkedHashSet<>();
	private Action<P2Query> queryHandler = null;

	private EquoIdeExtension copy() {
		var copy = new EquoIdeExtension();
		copy.repos.addAll(repos);
		copy.targets.addAll(targets);
		copy.queryHandler = queryHandler;
		return copy;
	}

	private boolean isEmpty() {
		return repos.isEmpty() && targets.isEmpty() && queryHandler == null;
	}

	private void setToDefault() {
		release(JdtSetup.DEFAULT_VERSION);
	}

	/** Sets which eclipse release to use, such as "4.25", "4.26", or a future release. */
	public void release(String version) {
		if (version.indexOf('/') != -1) {
			throw new IllegalArgumentException("Version should not have any slashes");
		}
		p2repo("https://download.eclipse.org/eclipse/updates/" + version + "/");
		targets.add("org.eclipse.releng.java.languages.categoryIU");
		targets.add("org.eclipse.platform.ide.categoryIU");
		queryHandler =
				query -> {
					query.excludePrefix("org.apache.felix.gogo");
					query.excludePrefix("org.eclipse.equinox.console");
					query.excludePrefix("org.eclipse.equinox.p2");
				};
	}

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
		repos.add(p2);
	}

	public void install(String target) {
		targets.add(target);
	}

	public void filter(Action<P2Query> query) {
		this.queryHandler = query;
	}

	P2Query performQuery() throws Exception {
		var extension = this;
		if (isEmpty()) {
			extension = copy();
			extension.setToDefault();
		}
		return extension.performQueryInternal();
	}

	private P2Query performQueryInternal() throws Exception {
		var session = new P2Session();
		try (var client = new P2Client()) {
			for (var repo : repos) {
				session.populateFrom(client, repo);
			}
		}
		var query = session.query();
		query.setPlatform(SwtPlatform.getRunning());
		if (queryHandler != null) {
			queryHandler.execute(query);
		}
		for (var target : targets) {
			query.resolve(target);
		}
		return query;
	}
}
