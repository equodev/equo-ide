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

import dev.equo.ide.CatalogDsl;
import dev.equo.ide.EquoCatalog;
import dev.equo.ide.IdeHook;
import dev.equo.solstice.p2.P2Model;
import java.util.Objects;
import java.util.stream.Stream;
import org.apache.maven.plugins.annotations.Parameter;

/** Performs typed p2 resolutions against known projects. */
public abstract class AbstractP2MojoWithCatalog extends AbstractP2Mojo {
	@Parameter private Platform platform;

	public static class Platform extends MavenCatalogDsl {
		public Platform() {
			super(EquoCatalog.PLATFORM);
		}
	}

	@Parameter private Jdt jdt;

	public static class Jdt extends MavenCatalogDsl {
		public Jdt() {
			super(EquoCatalog.JDT);
		}
	}

	@Parameter private GradleBuildship gradleBuildship;

	public static class GradleBuildship extends MavenCatalogDsl {
		public GradleBuildship() {
			super(EquoCatalog.GRADLE_BUILDSHIP);
		}
	}

	public static class MavenCatalogDsl extends CatalogDsl {
		protected MavenCatalogDsl(EquoCatalog catalog) {
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
	protected void modifyModel(P2Model model, IdeHook.List ideHooks) {
		CatalogDsl.TransitiveAwareList<MavenCatalogDsl> catalog =
				new CatalogDsl.TransitiveAwareList<>();
		Stream.of(platform, jdt, gradleBuildship).filter(Objects::nonNull).forEach(catalog::add);
		catalog.putInto(model, ideHooks);
	}
}
