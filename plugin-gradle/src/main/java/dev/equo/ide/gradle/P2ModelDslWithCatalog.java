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
import java.io.File;
import java.util.List;
import org.gradle.api.GradleException;
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

	public Platform platform(String urlOverride) {
		return add(new Platform(urlOverride));
	}

	public Platform platform() {
		return platform(null);
	}

	public static class Jdt extends GradleCatalogDsl {
		public Jdt(String urlOverride) {
			super(Catalog.JDT, urlOverride);
		}
	}

	public Jdt jdt(String urlOverride) {
		return add(new Jdt(urlOverride));
	}

	public Jdt jdt() {
		return jdt(null);
	}

	public static class GradleBuildship extends GradleCatalogDsl {
		private final Project project;
		private File dirToAutoImport;

		public GradleBuildship(String urlOverride, Project project) {
			super(Catalog.GRADLE_BUILDSHIP, urlOverride);
			this.project = project;
		}

		public GradleBuildship autoImport(Object path) {
			dirToAutoImport = project.file(path);
			var wrapper = new File(dirToAutoImport, "gradle/wrapper/gradle-wrapper.jar");
			if (!wrapper.exists()) {
				throw new GradleException(
						"autoImport of "
								+ dirToAutoImport
								+ " will fail because there is no gradle wrapper at "
								+ wrapper.getAbsolutePath());
			}
			return this;
		}

		@Override
		protected List<IdeHook> ideHooks() {
			if (dirToAutoImport == null) {
				return List.of();
			} else {
				return List.of(
						new IdeHookBuildship(
								dirToAutoImport, project.getGradle().getStartParameter().isOffline()));
			}
		}
	}

	public GradleBuildship gradleBuildship(String urlOverride) {
		return add(new GradleBuildship(urlOverride, project));
	}

	public GradleBuildship gradleBuildship() {
		return gradleBuildship(null);
	}

	public static class Pde extends GradleCatalogDsl {
		public Pde(String urlOverride, Project project) {
			super(Catalog.PDE, urlOverride);
		}

		/** Ignore / Error */
		public void missingApiBaseline(String ignoreOrError) {
			Catalog.PDE.missingApiBaseline(workspaceInit(), ignoreOrError);
		}
	}

	public Pde pde(String urlOverride) {
		return add(new Pde(urlOverride, project));
	}

	public Pde pde() {
		return pde(null);
	}

	public static class M2E extends GradleCatalogDsl {
		public M2E(String urlOverride, Project project) {
			super(Catalog.M2E, urlOverride);
		}
	}

	public M2E m2e(String urlOverride) {
		return add(new M2E(urlOverride, project));
	}

	public M2E m2e() {
		return m2e(null);
	}

	public static class Kotlin extends GradleCatalogDsl {
		public Kotlin(String urlOverride, Project project) {
			super(Catalog.KOTLIN, urlOverride);
		}
	}

	public Kotlin kotlin(String urlOverride) {
		return add(new Kotlin(urlOverride, project));
	}

	public Kotlin kotlin() {
		return kotlin(null);
	}

	public static class TmTerminal extends GradleCatalogDsl {
		public TmTerminal(String urlOverride, Project project) {
			super(Catalog.TM_TERMINAL, urlOverride);
		}
	}

	public TmTerminal tmTerminal(String urlOverride) {
		return add(new TmTerminal(urlOverride, project));
	}

	public TmTerminal tmTerminal() {
		return tmTerminal(null);
	}

	public static class Cdt extends GradleCatalogDsl {
		public Cdt(String urlOverride, Project project) {
			super(Catalog.CDT, urlOverride);
		}
	}

	public Cdt cdt(String urlOverride) {
		return add(new Cdt(urlOverride, project));
	}

	public Cdt cdt() {
		return cdt(null);
	}

	public static class Rust extends GradleCatalogDsl {
		public Rust(String urlOverride, Project project) {
			super(Catalog.RUST, urlOverride);
		}
	}

	public Rust rust(String urlOverride) {
		return add(new Rust(urlOverride, project));
	}

	public Rust rust() {
		return rust(null);
	}

	public static class Groovy extends GradleCatalogDsl {
		public Groovy(String urlOverride, Project project) {
			super(Catalog.GROOVY, urlOverride);
		}
	}

	public Groovy groovy(String urlOverride) {
		return add(new Groovy(urlOverride, project));
	}

	public Groovy groovy() {
		return groovy(null);
	}

	public static class GradleCatalogDsl extends CatalogDsl {
		protected GradleCatalogDsl(Catalog catalog, String urlOverride) {
			super(catalog);
			super.setUrlOverride(urlOverride);
		}
	}

	private <T extends GradleCatalogDsl> T add(T dsl) {
		catalog.add(dsl);
		return dsl;
	}

	final CatalogDsl.TransitiveAwareList<GradleCatalogDsl> catalog =
			new CatalogDsl.TransitiveAwareList<>();
}
