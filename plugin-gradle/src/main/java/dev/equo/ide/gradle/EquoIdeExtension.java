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

import dev.equo.ide.EquoFeature;
import dev.equo.ide.FeatureDsl;
import dev.equo.ide.IdeHook;
import dev.equo.ide.IdeHookBranding;
import dev.equo.ide.IdeHookBuildship;
import dev.equo.ide.IdeHookWelcome;
import dev.equo.solstice.p2.P2Client;
import dev.equo.solstice.p2.P2Query;
import java.util.List;
import org.gradle.api.Action;
import org.gradle.api.GradleException;
import org.gradle.api.Project;

/** The DSL inside the equoIde block. */
public class EquoIdeExtension extends P2ModelDsl {
	private Project project;

	public EquoIdeExtension(Project project) {
		this.project = project;
	}

	public boolean useAtomos = false;
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

	public void platform() {
		platform(null);
	}

	public void platform(String urlOverride) {
		addFeature(new Platform(urlOverride));
	}

	public static class Platform extends GradleFeatureDsl {
		protected Platform(String urlOverride) {
			super(EquoFeature.PLATFORM, urlOverride);
		}
	}

	public void jdt() {
		jdt(null);
	}

	public void jdt(String urlOverride) {
		addFeature(new Jdt(urlOverride));
	}

	public static class Jdt extends GradleFeatureDsl {
		protected Jdt(String urlOverride) {
			super(EquoFeature.JDT, urlOverride);
		}
	}

	public void gradleBuildship() {
		gradleBuildship(null);
	}

	public void gradleBuildship(String urlOverride) {
		addFeature(new GradleBuildship(urlOverride, project));
	}

	public static class GradleBuildship extends GradleFeatureDsl {
		private IdeHookBuildship ideHook;

		protected GradleBuildship(String urlOverride, Project project) {
			super(EquoFeature.GRADLE_BUILDSHIP, urlOverride);
			ideHook =
					new IdeHookBuildship(
							project.getProjectDir(), project.getGradle().getStartParameter().isOffline());
		}

		@Override
		protected List<IdeHook> ideHooks() {
			return List.of(ideHook);
		}
	}

	private <T extends GradleFeatureDsl> void addFeature(T dsl) {
		addFeature(dsl, unused -> {});
	}

	private <T extends GradleFeatureDsl> void addFeature(T dsl, Action<? super T> action) {
		action.execute(dsl);
		features.addFeature(dsl);
	}

	private final FeatureDsl.TransitiveAwareList<GradleFeatureDsl> features =
			new FeatureDsl.TransitiveAwareList<>();

	public static class GradleFeatureDsl extends FeatureDsl {
		protected GradleFeatureDsl(EquoFeature feature, String urlOverride) {
			super(feature);
			setUrlOverride(urlOverride);
		}
	}

	public IdeHook.List getIdeHooks() {
		return ideHooks;
	}

	P2Query performQuery(P2Client.Caching caching) throws Exception {
		if (model.isEmpty()) {
			throw new GradleException(
					"EquoIDE has nothing to install!\n\n"
							+ "We recommend starting with this:\n"
							+ "equoIde {\n"
							+ "  gradleBuildship()\n"
							+ "}");
		}
		features.putInto(model, ideHooks);
		model.applyNativeFilterIfNoPlatformFilter();
		return model.query(caching);
	}
}
