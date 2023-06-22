/*******************************************************************************
 * Copyright (c) 2022-2023 EquoTech, Inc. and others.
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
import dev.equo.ide.IdeHookAssistAI;
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

		public Platform showLineNumbers(boolean showLineNumbers) {
			Catalog.PLATFORM.showLineNumbers(workspaceInit(), showLineNumbers);
			return this;
		}

		public Platform showWhitespace(boolean showWhitespace) {
			Catalog.PLATFORM.showWhitespace(workspaceInit(), showWhitespace);
			return this;
		}

		public Platform showLineEndings(boolean showLineEndings) {
			Catalog.PLATFORM.showLineEndings(workspaceInit(), showLineEndings);
			return this;
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

		public Jdt classpathVariable(String name, String value) {
			Catalog.JDT.classpathVariable(workspaceInit(), name, value);
			return this;
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

	public static class EGit extends GradleCatalogDsl {
		public EGit(String urlOverride, Project project) {
			super(Catalog.EGIT, urlOverride);
		}
	}

	public EGit egit(String urlOverride) {
		return add(new EGit(urlOverride, project));
	}

	public EGit egit() {
		return egit(null);
	}

	public static class AssistAI extends GradleCatalogDsl {
		public AssistAI(String urlOverride, Project project) {
			super(Catalog.ASSIST_AI, urlOverride);
		}

		public AssistAI apiKey(String key) {
			Catalog.ASSIST_AI.apiKey(workspaceInit(), key);
			return this;
		}

		public AssistAI modelName(String modelName) {
			Catalog.ASSIST_AI.modelName(workspaceInit(), modelName);
			return this;
		}

		@Override
		protected List<IdeHook> ideHooks() {
			return List.of(new IdeHookAssistAI());
		}
	}

	public AssistAI assistAI(String urlOverride) {
		return add(new AssistAI(urlOverride, project));
	}

	public AssistAI assistAI() {
		return assistAI(null);
	}

	public static class Tabnine extends GradleCatalogDsl {
		public Tabnine(String urlOverride, Project project) {
			super(Catalog.TABNINE, urlOverride);
		}
	}

	public Tabnine tabnine(String urlOverride) {
		return add(new Tabnine(urlOverride, project));
	}

	public Tabnine tabnine() {
		return tabnine(null);
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

	protected <T extends GradleCatalogDsl> T add(T dsl) {
		catalog.add(dsl);
		return dsl;
	}

	final CatalogDsl.TransitiveAwareList<GradleCatalogDsl> catalog =
			new CatalogDsl.TransitiveAwareList<>();
}
