/*******************************************************************************
 * Copyright (c) 2023 EquoTech, Inc. and others.
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.stream.Collectors;
import javax.annotation.Nullable;

/**
 * Base class for DSL configuring an {@link Catalog} for inclusion in the Gradle or Maven plugins.
 */
public class CatalogDsl {
	protected final Catalog catalog;
	private final @Nullable CatalogDsl addedAsTransitiveOf;
	private @Nullable String p2AndMavenOverride;
	private final WorkspaceInit workspaceInit = new WorkspaceInit();

	protected CatalogDsl(Catalog catalog) {
		this(catalog, null);
	}

	private CatalogDsl(Catalog catalog, @Nullable CatalogDsl addedAsTransitiveOf) {
		this.catalog = catalog;
		this.addedAsTransitiveOf = addedAsTransitiveOf;
	}

	protected WorkspaceInit workspaceInit() {
		return workspaceInit;
	}

	/**
	 * Subclasses can override this method to include their IdeHooks in {@link
	 * TransitiveAwareList#putInto(P2Model, dev.equo.ide.IdeHook.List, WorkspaceInit)}.
	 */
	protected List<IdeHook> ideHooks() {
		return List.of();
	}

	private String url() {
		return catalog.getUrlForOverride(p2AndMavenOverride);
	}

	private List<String> installs() {
		if (!catalog.isPureMaven()) {
			return catalog.getTargetsFor(p2AndMavenOverride);
		} else {
			// for pure maven, the versions apply to anything like `group:artifact:${VERSION}`
			if (p2AndMavenOverride == null) {
				String version = catalog.versions.getAndWarn(catalog);
				return replaceV(catalog.getTargetsFor(null), version);
			} else if (p2AndMavenOverride.contains(":")) {
				return Arrays.asList(p2AndMavenOverride.split(","));
			} else {
				return replaceV(catalog.getTargetsFor(p2AndMavenOverride), p2AndMavenOverride);
			}
		}
	}

	private static List<String> replaceV(List<String> list, String replaceWith) {
		return list.stream()
				.map(str -> str.replace(Catalog.V, replaceWith))
				.collect(Collectors.toList());
	}

	private Map<String, P2Model.Filter> filters() {
		return catalog.getFiltersFor(p2AndMavenOverride);
	}

	/**
	 * Overrides the version, url, or artifacts.
	 *
	 * <ul>
	 *   <li>For a P2 Catalog entry (doesn't extend {@link Catalog.PureMaven})
	 *       <ul>
	 *         <li>if p2AndMavenOverride is just a version (doesn't contain ':') then it replaces
	 *             ${VERSION} in the p2 url
	 *         <li>if p2AndMavenOverride is a full URL, then it replaces the p2 url entirely
	 *       </ul>
	 *   <li>For pure Maven catalog entry (extends {@link Catalog.PureMaven})
	 *       <ul>
	 *         <li>if p2AndMavenOverride is just a version (doesn't contain ':') then it replaces
	 *             ${VERSION} in the installs artifact list
	 *         <li>if p2AndMavenOverride is a full maven coordinate (or comma-delimited list of
	 *             coordinates) then it replaces the entire artifact list
	 *       </ul>
	 * </ul>
	 */
	protected void setUrlOverride(String p2AndMavenOverride) {
		this.p2AndMavenOverride = p2AndMavenOverride;
	}

	/**
	 * If this catalog has the same URL template as one of its transitive dependencies, then it should
	 * also have the exact same p2AndMavenOverride. If it doesn't, and one of them is null, then we
	 * can automatically override the null with the non-null to resolve the issue.
	 */
	void syncUrlWith(CatalogDsl other) {
		if (!catalog.getP2UrlTemplate().equals(other.catalog.getP2UrlTemplate())) {
			// syncing only matters when they have the same URL
			return;
		}
		if (Objects.equals(p2AndMavenOverride, other.p2AndMavenOverride)) {
			// if they have the same override, that's fine
			return;
		}
		if (p2AndMavenOverride == null) {
			p2AndMavenOverride = other.p2AndMavenOverride;
		} else if (other.p2AndMavenOverride == null) {
			other.p2AndMavenOverride = p2AndMavenOverride;
		} else {
			// they are both non-null and unequal
			throw new IllegalArgumentException(
					catalog.getName()
							+ " "
							+ other.catalog.getName()
							+ " must have the exact same URL, but\n"
							+ urlReasoning()
							+ "\n"
							+ other.urlReasoning());
		}
	}

	private String urlReasoning() {
		if (addedAsTransitiveOf != null) {
			return catalog.getName()
					+ " is using "
					+ url()
					+ " (was added automatically as a transitive of "
					+ addedAsTransitiveOf.catalog.getName()
					+ ")";
		} else {
			return catalog.getName() + " is using " + url();
		}
	}

	public static class TransitiveAwareList<T extends CatalogDsl> {
		private List<CatalogDsl> insertionOrder = new ArrayList<>();
		private final TreeMap<Catalog, CatalogDsl> catalogEntries = new TreeMap<>();

		public void add(T dsl) {
			insertionOrder.add(dsl);
			var existing = catalogEntries.get(dsl.catalog);
			if (existing != null) {
				if (existing.addedAsTransitiveOf != null) {
					throw new IllegalArgumentException(
							dsl.catalog.getName()
									+ " was already added as a transitive dependency of "
									+ existing.addedAsTransitiveOf.catalog.getName()
									+ ".\n"
									+ "You can fix this by moving the <"
									+ dsl.catalog.getName()
									+ "> block above the <"
									+ existing.addedAsTransitiveOf.catalog.getName()
									+ "> block.");
				} else {
					throw new IllegalArgumentException(
							"You can only add " + dsl.catalog.getName() + " once.");
				}
			}
			catalogEntries.put(dsl.catalog, dsl);
			for (var required : dsl.catalog.getRequires()) {
				var transitive = addAsTransitiveOf(required, dsl);
				transitive.syncUrlWith(dsl);
			}
		}

		private CatalogDsl addAsTransitiveOf(Catalog transitive, CatalogDsl originalRequest) {
			var dsl = catalogEntries.get(transitive);
			if (dsl == null) {
				dsl = new CatalogDsl(transitive, originalRequest);
				catalogEntries.put(transitive, dsl);
				insertionOrder.add(dsl);
			}
			for (var required : transitive.getRequires()) {
				var transitiveDsl = addAsTransitiveOf(required, originalRequest);
				transitiveDsl.syncUrlWith(dsl);
			}
			return dsl;
		}

		@Deprecated
		public void putInto(P2Model model, IdeHook.List hooks) {
			putInto(model, hooks, new WorkspaceInit());
		}

		public void putInto(P2Model model, IdeHook.List hooks, WorkspaceInit workspace) {
			// setup the IDE hooks
			for (CatalogDsl dsl : catalogEntries.values()) {
				if (dsl.catalog.isPureMaven()) {
					model.getPureMaven().addAll(dsl.installs());
				} else {
					model.addP2Repo(dsl.url());
					model.getInstall().addAll(dsl.installs());
					dsl.filters().forEach(model::addFilterAndValidate);
				}
				hooks.addAll(dsl.ideHooks());
				workspace.copyAllFrom(dsl.workspaceInit());
			}
			// find the initial perspective
			String perspective =
					insertionOrder.stream()
							.map(dsl -> Catalog.defaultPerspectiveFor(dsl.catalog))
							.filter(Objects::nonNull)
							.findFirst()
							.orElse(null);
			if (perspective != null) {
				IdeHookWelcome welcomeHook =
						(IdeHookWelcome)
								hooks.stream()
										.filter(hook -> hook instanceof IdeHookWelcome)
										.findFirst()
										.orElse(null);
				if (welcomeHook == null) {
					welcomeHook = new IdeHookWelcome();
					hooks.add(welcomeHook);
				}
				if (welcomeHook.perspective() == null) {
					welcomeHook.perspective(perspective);
				}
			}
		}
	}
}
