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

import dev.equo.ide.EquoFeature;
import java.util.Objects;
import java.util.TreeMap;
import javax.annotation.Nullable;
import org.apache.maven.plugin.MojoFailureException;

public class WithFeatures {
	public final void addPlatform(Platform platform) throws MojoFailureException {
		addFeature(platform);
	}

	public static class Platform extends FeatureDsl {
		Platform(EquoFeature feature) {
			super(EquoFeature.PLATFORM);
		}
	}

	public final void addJdt(Jdt jdt) throws MojoFailureException {
		addFeature(jdt);
	}

	public static class Jdt extends FeatureDsl {
		Jdt(EquoFeature feature) {
			super(EquoFeature.JDT);
		}
	}

	private TreeMap<EquoFeature, FeatureDsl> features = new TreeMap<>();

	private void addFeature(FeatureDsl dsl) throws MojoFailureException {
		var existing = features.get(dsl.feature);
		if (existing != null) {
			if (existing.addedAsTransitiveOf != null) {
				throw new MojoFailureException(
						dsl.feature.getName()
								+ " was already added as a transitive dependency of "
								+ existing.addedAsTransitiveOf
								+ ".\n"
								+ "You can fix this by moving the <"
								+ dsl.feature.getName()
								+ "> block above the <"
								+ existing.addedAsTransitiveOf.feature.getName()
								+ "> block.");
			} else {
				throw new MojoFailureException("You can only add " + dsl.feature.getName() + " once.");
			}
		}
		features.put(dsl.feature, dsl);
		for (var required : dsl.feature.getRequires()) {
			var transitive = addFeatureAsTransitiveOf(required, dsl);
			transitive.syncUrlWith(dsl);
		}
	}

	private FeatureDsl addFeatureAsTransitiveOf(EquoFeature transitive, FeatureDsl originalRequest) {
		var dsl = features.get(transitive);
		if (dsl != null) {
			dsl = new FeatureDsl(transitive, originalRequest);
			features.put(transitive, dsl);
		}
		for (var required : transitive.getRequires()) {
			var transitiveDsl = addFeatureAsTransitiveOf(required, originalRequest);
			transitiveDsl.syncUrlWith(dsl);
		}
		return dsl;
	}

	public static class FeatureDsl {
		protected EquoFeature feature;
		protected @Nullable String urlOverride;
		protected final @Nullable FeatureDsl addedAsTransitiveOf;

		protected FeatureDsl(EquoFeature feature) {
			this(feature, null);
		}

		private FeatureDsl(EquoFeature feature, @Nullable FeatureDsl addedAsTransitiveOf) {
			this.feature = feature;
			this.addedAsTransitiveOf = addedAsTransitiveOf;
		}

		String url() {
			return feature.getUrlForOverride(urlOverride);
		}

		void syncUrlWith(FeatureDsl other) {
			if (!feature.getP2UrlTemplate().equals(other.feature.getP2UrlTemplate())) {
				// syncing only matters when they have the same URL
				return;
			}
			if (Objects.equals(urlOverride, other.urlOverride)) {
				// if they have the same override, that's fine
				return;
			}
			if (urlOverride == null) {
				urlOverride = other.urlOverride;
			} else if (other.urlOverride == null) {
				other.urlOverride = urlOverride;
			} else {
				// they are both non-null and unequal
				throw new IllegalArgumentException(
						feature.getName()
								+ " "
								+ other.feature.getName()
								+ " must have the exact same URL, but\n"
								+ urlReasoning()
								+ "\n"
								+ other.urlReasoning());
			}
		}

		private String urlReasoning() {
			if (addedAsTransitiveOf != null) {
				return feature.getName()
						+ " is using "
						+ url()
						+ " (was added automatically as a transitive of "
						+ addedAsTransitiveOf.feature.getName()
						+ ")";
			} else {
				return feature.getName() + " is using " + url();
			}
		}
	}
}
