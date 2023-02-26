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

import dev.equo.ide.Catalog;
import dev.equo.ide.CatalogDsl;
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
		public Platform(String urlOverride) {
			super(Catalog.PLATFORM, urlOverride);
		}
	}

	public void platform(String urlOverride) {
		add(new Platform(urlOverride));
	}

	public void platform() {
		platform(null);
	}

	public static class Jdt extends GradleCatalogDsl {
		public Jdt(String urlOverride) {
			super(Catalog.JDT, urlOverride);
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

		public GradleBuildship(String urlOverride, Project project) {
			super(Catalog.GRADLE_BUILDSHIP, urlOverride);
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

	public static class Pde extends GradleCatalogDsl {
		public Pde(String urlOverride, Project project) {
			super(Catalog.PDE, urlOverride);
		}
	}

	public void pde(String urlOverride) {
		add(new Pde(urlOverride, project));
	}

	public void pde() {
		pde(null);
	}

	public static class M2E extends GradleCatalogDsl {
		public M2E(String urlOverride, Project project) {
			super(Catalog.M2E, urlOverride);
		}
	}

	public void m2e(String urlOverride) {
		add(new M2E(urlOverride, project));
	}

	public void m2e() {
		m2e(null);
	}

	public static class Kotlin extends GradleCatalogDsl {
		public Kotlin(String urlOverride, Project project) {
			super(Catalog.KOTLIN, urlOverride);
		}
	}

	public void kotlin(String urlOverride) {
		add(new Kotlin(urlOverride, project));
	}

	public void kotlin() {
		kotlin(null);
	}

	public static class TmTerminal extends GradleCatalogDsl {
		public TmTerminal(String urlOverride, Project project) {
			super(Catalog.TM_TERMINAL, urlOverride);
		}
	}

	public void tmTerminal(String urlOverride) {
		add(new TmTerminal(urlOverride, project));
	}

	public void tmTerminal() {
		tmTerminal(null);
	}

	public static class Cdt extends GradleCatalogDsl {
		public Cdt(String urlOverride, Project project) {
			super(Catalog.CDT, urlOverride);
		}
	}

	public void cdt(String urlOverride) {
		add(new Cdt(urlOverride, project));
	}

	public void cdt() {
		cdt(null);
	}

	public static class Rust extends GradleCatalogDsl {
		public Rust(String urlOverride, Project project) {
			super(Catalog.RUST, urlOverride);
		}
	}

	public void rust(String urlOverride) {
		add(new Rust(urlOverride, project));
	}

	public void rust() {
		rust(null);
	}

	public static class GradleCatalogDsl extends CatalogDsl {
		protected GradleCatalogDsl(Catalog catalog, String urlOverride) {
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
