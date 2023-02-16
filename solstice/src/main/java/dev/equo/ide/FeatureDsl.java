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
package dev.equo.ide;

import dev.equo.solstice.p2.P2Model;
import java.util.List;
import java.util.Objects;
import java.util.TreeMap;
import javax.annotation.Nullable;

/**
 * Base class for DSL configuring an {@link EquoFeature} for inclusion in the Gradle or Maven
 * plugins.
 */
public class FeatureDsl {
	protected final EquoFeature feature;
	private final @Nullable FeatureDsl addedAsTransitiveOf;
	private @Nullable String urlOverride;

	protected FeatureDsl(EquoFeature feature) {
		this(feature, null);
	}

	private FeatureDsl(EquoFeature feature, @Nullable FeatureDsl addedAsTransitiveOf) {
		this.feature = feature;
		this.addedAsTransitiveOf = addedAsTransitiveOf;
	}

	/**
	 * Subclasses can override this method to include their IdeHooks in {@link
	 * TransitiveAwareList#putInto(P2Model, dev.equo.ide.IdeHook.List)}.
	 */
	protected List<IdeHook> ideHooks() {
		return List.of();
	}

	private String url() {
		return feature.getUrlForOverride(urlOverride);
	}

	private List<String> installs() {
		return feature.getTargetsFor(urlOverride);
	}

	/** Sets the URL and/or version override. */
	protected void setUrlOverride(String urlOverride) {
		this.urlOverride = urlOverride;
	}

	/**
	 * If this feature has the same URL template as one of its transitive dependencies, then it should
	 * also have the exact same urlOverride. If it doesn't, and one of them is null, then we can
	 * automatically override the null with the non-null to resolve the issue.
	 */
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

	public static class TransitiveAwareList<T extends FeatureDsl> {
		private TreeMap<EquoFeature, FeatureDsl> features = new TreeMap<>();

		public void addFeature(T dsl) {
			var existing = features.get(dsl.feature);
			if (existing != null) {
				if (existing.addedAsTransitiveOf != null) {
					throw new IllegalArgumentException(
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
					throw new IllegalArgumentException(
							"You can only add " + dsl.feature.getName() + " once.");
				}
			}
			features.put(dsl.feature, dsl);
			for (var required : dsl.feature.getRequires()) {
				var transitive = addFeatureAsTransitiveOf(required, dsl);
				transitive.syncUrlWith(dsl);
			}
		}

		private FeatureDsl addFeatureAsTransitiveOf(
				EquoFeature transitive, FeatureDsl originalRequest) {
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

		public void putInto(P2Model model, IdeHook.List hooks) {
			for (FeatureDsl dsl : features.values()) {
				model.addP2Repo(dsl.url());
				model.getInstall().addAll(dsl.installs());
				hooks.addAll(dsl.ideHooks());
			}
		}
	}
}
