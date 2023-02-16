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

import dev.equo.ide.EquoCatalog;
import dev.equo.ide.FeatureDsl;
import dev.equo.ide.IdeHook;
import dev.equo.solstice.p2.P2Model;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Parameter;

/** Performs typed p2 resolutions against known projects. */
public abstract class AbstractP2MojoWithFeatures extends AbstractP2Mojo {
	public final void addPlatform(Platform platform) throws MojoFailureException {
		addFeature(platform);
	}

	public static class Platform extends MavenFeatureDsl {
		public Platform() {
			super(EquoCatalog.PLATFORM);
		}
	}

	public final void addJdt(Jdt jdt) throws MojoFailureException {
		addFeature(jdt);
	}

	public static class Jdt extends MavenFeatureDsl {
		public Jdt() {
			super(EquoCatalog.JDT);
		}
	}

	public final void addGradleBuildship(GradleBuildship buildship) throws MojoFailureException {
		addFeature(buildship);
	}

	public static class GradleBuildship extends MavenFeatureDsl {
		public GradleBuildship() {
			super(EquoCatalog.GRADLE_BUILDSHIP);
		}
	}

	public static class MavenFeatureDsl extends FeatureDsl {
		protected MavenFeatureDsl(EquoCatalog feature) {
			super(feature);
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
		featuresInternal.putInto(model, ideHooks);
	}

	private void addFeature(MavenFeatureDsl dsl) {
		dsl.processVersionOverrides();
		featuresInternal.addFeature(dsl);
	}

	private final FeatureDsl.TransitiveAwareList<MavenFeatureDsl> featuresInternal =
			new FeatureDsl.TransitiveAwareList<>();
}
