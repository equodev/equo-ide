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
import dev.equo.ide.IdeHookWelcome;
import dev.equo.solstice.p2.P2Model;
import org.gradle.api.GradleException;
import org.gradle.api.Project;

/** The DSL inside the equoIde block. */
public class EquoIdeExtension extends P2ModelDslWithFeatures {
	public boolean useAtomos = false;
	private final IdeHook.List ideHooks = new IdeHook.List();
	public final IdeHookBranding branding = new IdeHookBranding();

	public EquoIdeExtension(Project project) {
		super(project);
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

	public IdeHook.List getIdeHooks() {
		return ideHooks;
	}

	P2Model prepareModel() throws Exception {
		if (model.isEmpty()) {
			throw new GradleException(
					"EquoIDE has nothing to install!\n\n"
							+ "We recommend starting with this:\n"
							+ "equoIde {\n"
							+ "  gradleBuildship()\n"
							+ "}");
		}
		if (hasBeenPrepared) {
			return model;
		}
		hasBeenPrepared = true;
		features.putInto(model, ideHooks);
		model.applyNativeFilterIfNoPlatformFilter();
		return model;
	}

	private boolean hasBeenPrepared = false;
}
