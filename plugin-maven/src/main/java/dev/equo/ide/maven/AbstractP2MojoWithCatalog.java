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
package dev.equo.ide.maven;

import dev.equo.ide.Catalog;
import dev.equo.ide.CatalogDsl;
import dev.equo.ide.IdeHook;
import dev.equo.ide.IdeHookM2E;
import dev.equo.ide.WorkspaceInit;
import dev.equo.solstice.p2.P2Model;
import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;
import org.apache.maven.plugins.annotations.Parameter;

/** Performs typed p2 resolutions against known projects. */
public abstract class AbstractP2MojoWithCatalog extends AbstractP2Mojo {
	@Parameter private Platform platform;

	public static class Platform extends MavenCatalogDsl {
		public Platform() {
			super(Catalog.PLATFORM);
		}

		@Parameter(required = false)
		private Boolean showLineNumbers;

		@Parameter(required = false)
		private Boolean showWhitespace;

		@Parameter(required = false)
		private Boolean showLineEndings;

		@Override
		protected void processVersionOverrides() {
			super.processVersionOverrides();
			if (showLineNumbers != null) {
				Catalog.PLATFORM.showLineNumbers(workspaceInit(), showLineNumbers);
			}
			if (showWhitespace != null) {
				Catalog.PLATFORM.showWhitespace(workspaceInit(), showWhitespace);
			}
			if (showLineEndings != null) {
				Catalog.PLATFORM.showLineEndings(workspaceInit(), showLineEndings);
			}
		}
	}

	@Parameter private Jdt jdt;

	public static class Jdt extends MavenCatalogDsl {
		public Jdt() {
			super(Catalog.JDT);
		}

		@Parameter(required = false)
		private Map<String, String> classpathVariable;

		@Override
		protected void processVersionOverrides() {
			super.processVersionOverrides();
			if (classpathVariable != null) {
				classpathVariable.forEach(
						(name, value) -> Catalog.JDT.classpathVariable(workspaceInit(), name, value));
			}
		}
	}

	@Parameter private GradleBuildship gradleBuildship;

	public static class GradleBuildship extends MavenCatalogDsl {
		public GradleBuildship() {
			super(Catalog.GRADLE_BUILDSHIP);
		}
	}

	@Parameter private Pde pde;

	public static class Pde extends MavenCatalogDsl {
		public Pde() {
			super(Catalog.PDE);
		}

		/** Ignore / Error */
		@Parameter(required = false)
		private String missingApiBaseline;

		@Override
		protected void processVersionOverrides() {
			super.processVersionOverrides();
			if (missingApiBaseline != null) {
				Catalog.PDE.missingApiBaseline(workspaceInit(), missingApiBaseline);
			}
		}
	}

	@Parameter private EGit egit;

	public static class EGit extends MavenCatalogDsl {
		public EGit() {
			super(Catalog.EGIT);
		}
	}

	@Parameter private AssistAI assistAI;

	public static class AssistAI extends MavenCatalogDsl {
		public AssistAI() {
			super(Catalog.ASSIST_AI);
		}

		@Parameter(required = false)
		private String apiKey;

		@Parameter(required = false)
		private String modelName;

		@Override
		protected void processVersionOverrides() {
			super.processVersionOverrides();
			if (apiKey != null) {
				Catalog.ASSIST_AI.apiKey(workspaceInit(), apiKey);
			}
			if (modelName != null) {
				Catalog.ASSIST_AI.modelName(workspaceInit(), modelName);
			}
		}
	}

	@Parameter private M2E m2e;

	public static class M2E extends MavenCatalogDsl {
		public M2E() {
			super(Catalog.M2E);
		}

		@Parameter private File autoImport;

		@Override
		protected List<IdeHook> ideHooks() {
			if (autoImport == null) {
				return List.of();
			} else {
				return List.of(new IdeHookM2E(autoImport));
			}
		}
	}

	@Parameter private Kotlin kotlin;

	public static class Kotlin extends MavenCatalogDsl {
		public Kotlin() {
			super(Catalog.KOTLIN);
		}
	}

	@Parameter private TmTerminal tmTerminal;

	public static class TmTerminal extends MavenCatalogDsl {
		public TmTerminal() {
			super(Catalog.TM_TERMINAL);
		}
	}

	@Parameter private Cdt cdt;

	public static class Cdt extends MavenCatalogDsl {
		public Cdt() {
			super(Catalog.CDT);
		}
	}

	@Parameter private Rust rust;

	public static class Rust extends MavenCatalogDsl {
		public Rust() {
			super(Catalog.RUST);
		}
	}

	@Parameter private Groovy groovy;

	public static class Groovy extends MavenCatalogDsl {
		public Groovy() {
			super(Catalog.GROOVY);
		}
	}

	public static class MavenCatalogDsl extends CatalogDsl {
		protected MavenCatalogDsl(Catalog catalog) {
			super(catalog);
		}

		/** Override EquoIDE's default version for this feature. */
		@Parameter private String version;

		/** Override EquoIDE's default p2 URL for this feature. */
		@Parameter private String url;

		protected void processVersionOverrides() {
			if (version != null && url != null) {
				throw new IllegalArgumentException(
						"You must set only one of <version> or <url>, not both.");
			} else if (version != null) {
				setUrlOverride(version);
			} else if (url != null) {
				setUrlOverride(url);
			}
		}
	}

	@Override
	protected void modifyModel(P2Model model, IdeHook.List ideHooks, WorkspaceInit workspace) {
		CatalogDsl.TransitiveAwareList<MavenCatalogDsl> catalog =
				new CatalogDsl.TransitiveAwareList<>();
		// NB: each entry must be after all of its transitive dependencies
		// e.g. jdt must be after platform
		Stream.of(platform, jdt, gradleBuildship, pde, m2e, kotlin, tmTerminal, cdt, rust, groovy)
				.filter(Objects::nonNull)
				.forEach(
						dsl -> {
							dsl.processVersionOverrides();
							catalog.add(dsl);
						});
		catalog.putInto(model, ideHooks, workspace);
	}
}
