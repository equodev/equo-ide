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

/** The DSL inside the equoIde block. */
public class EquoIdeExtension extends P2ModelDsl {
	public boolean useAtomos = true;

	public EquoIdeExtension() {
		super(new P2Model());
		useAtomos = true;
	}

	private EquoIdeExtension(EquoIdeExtension existing) {
		super(existing.model.deepCopy());
		useAtomos = existing.useAtomos;
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

	P2Query performQuery(P2Client.Caching caching) throws Exception {
		var extension = this;
		if (model.isEmpty()) {
			extension = deepCopy();
			extension.setToDefault();
		}
		P2Model model = extension.model.deepCopy();
		if (!model.hasAnyPlatformFilter()) {
			model.addFilterAndValidate(
					"platform-specific-for-running", new P2Model.Filter().platformRunning());
		}
		return model.query(caching);
	}
}
