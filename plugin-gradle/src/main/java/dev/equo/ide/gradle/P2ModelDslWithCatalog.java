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

import dev.equo.ide.CatalogDsl;
import dev.equo.ide.EquoCatalog;
import dev.equo.ide.IdeHook;
import dev.equo.ide.IdeHookBuildship;
import java.util.List;
import org.gradle.api.Action;
import org.gradle.api.Project;

public class P2ModelDslWithCatalog extends P2ModelDsl {
	final Project project;

	public P2ModelDslWithCatalog(Project project) {
		this.project = project;
	}

	public static class Platform extends GradleCatalogDsl {
		protected Platform(String urlOverride) {
			super(EquoCatalog.PLATFORM, urlOverride);
		}
	}

	public void platform(String urlOverride) {
		add(new Platform(urlOverride));
	}

	public void platform() {
		platform(null);
	}

	public static class Jdt extends GradleCatalogDsl {
		protected Jdt(String urlOverride) {
			super(EquoCatalog.JDT, urlOverride);
		}
	}

	public void jdt(String urlOverride) {
		add(new Jdt(urlOverride));
	}

	public void jdt() {
		jdt(null);
	}

	public static class GradleBuildship extends GradleCatalogDsl {
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
		add(new GradleBuildship(urlOverride, project));
	}

	public void gradleBuildship() {
		gradleBuildship(null);
	}

	public static class GradleCatalogDsl extends CatalogDsl {
		protected GradleCatalogDsl(EquoCatalog catalog, String urlOverride) {
			super(catalog);
			super.setUrlOverride(urlOverride);
		}
	}

	private <T extends GradleCatalogDsl> void add(T dsl) {
		add(dsl, unused -> {});
	}

	private <T extends GradleCatalogDsl> void add(T dsl, Action<? super T> action) {
		action.execute(dsl);
		catalog.add(dsl);
	}

	final CatalogDsl.TransitiveAwareList<GradleCatalogDsl> catalog =
			new CatalogDsl.TransitiveAwareList<>();
}
