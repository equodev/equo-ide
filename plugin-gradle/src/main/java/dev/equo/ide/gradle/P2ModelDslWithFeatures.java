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

import dev.equo.ide.EquoCatalog;
import dev.equo.ide.FeatureDsl;
import dev.equo.ide.IdeHook;
import dev.equo.ide.IdeHookBuildship;
import java.util.List;
import org.gradle.api.Action;
import org.gradle.api.Project;

public class P2ModelDslWithFeatures extends P2ModelDsl {
	final Project project;

	public P2ModelDslWithFeatures(Project project) {
		this.project = project;
	}

	public static class Platform extends GradleFeatureDsl {
		protected Platform(String urlOverride) {
			super(EquoCatalog.PLATFORM, urlOverride);
		}
	}

	public void platform(String urlOverride) {
		addFeature(new Platform(urlOverride));
	}

	public void platform() {
		platform(null);
	}

	public static class Jdt extends GradleFeatureDsl {
		protected Jdt(String urlOverride) {
			super(EquoCatalog.JDT, urlOverride);
		}
	}

	public void jdt(String urlOverride) {
		addFeature(new Jdt(urlOverride));
	}

	public void jdt() {
		jdt(null);
	}

	public static class GradleBuildship extends GradleFeatureDsl {
		private IdeHookBuildship ideHook;

		protected GradleBuildship(String urlOverride, Project project) {
			super(EquoCatalog.GRADLE_BUILDSHIP, urlOverride);
			ideHook =
					new IdeHookBuildship(
							project.getProjectDir(), project.getGradle().getStartParameter().isOffline());
		}

		@Override
		protected List<IdeHook> ideHooks() {
			return List.of(ideHook);
		}
	}

	public void gradleBuildship(String urlOverride) {
		addFeature(new GradleBuildship(urlOverride, project));
	}

	public void gradleBuildship() {
		gradleBuildship(null);
	}

	public static class GradleFeatureDsl extends FeatureDsl {
		protected GradleFeatureDsl(EquoCatalog feature, String urlOverride) {
			super(feature);
			super.setUrlOverride(urlOverride);
		}
	}

	private <T extends GradleFeatureDsl> void addFeature(T dsl) {
		addFeature(dsl, unused -> {});
	}

	private <T extends GradleFeatureDsl> void addFeature(T dsl, Action<? super T> action) {
		action.execute(dsl);
		features.addFeature(dsl);
	}

	final FeatureDsl.TransitiveAwareList<GradleFeatureDsl> features =
			new FeatureDsl.TransitiveAwareList<>();
}
