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
package dev.equo.solstice.p2;

import com.diffplug.common.swt.os.SwtPlatform;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Collectors;
import javax.annotation.Nullable;

/**
 * Follows the dependency information of a set of {@link dev.equo.solstice.p2.P2Unit} so that they
 * can be resolved from maven or directly from p2 if necessary.
 */
public class P2Query {
	private P2Session session;

	P2Query(P2Session session) {
		this.session = session;
	}

	private TreeSet<String> exclude = new TreeSet<>();
	private List<String> excludePrefix = new ArrayList<>();

	private Map<String, String> filterProps = new HashMap<String, String>();

	private TreeMap<String, P2Unit> resolved = new TreeMap<>();
	private TreeMap<P2Session.Requirement, TreeSet<P2Unit>> unmetRequirements = new TreeMap<>();
	private TreeSet<P2Session.Requirement> ambiguousRequirements = new TreeSet<>();

	private void assertNotUsed() {
		if (!resolved.isEmpty()) {
			throw new IllegalStateException(
					"You must not change any filter properties after you have already called `resolve` or `addAllUnits`.");
		}
	}

	public void exclude(String toExclude) {
		assertNotUsed();
		exclude.add(toExclude);
	}

	public void excludePrefix(String prefix) {
		assertNotUsed();
		excludePrefix.add(prefix);
	}

	public void setPlatform(@Nullable SwtPlatform platform) {
		assertNotUsed();
		if (platform == null) {
			filterProps.clear();
		} else {
			filterProps.put("osgi.os", platform.getOs());
			filterProps.put("osgi.ws", platform.getWs());
			filterProps.put("osgi.arch", platform.getArch());
		}
	}

	/** Resolves the given P2Unit by eagerly traversing all its dependencies. */
	public void resolve(String idToResolve) {
		resolve(session.getUnitById(idToResolve));
	}

	/** Returns the unit, if any, which has been resolved at the given id. */
	public P2Unit findResolvedUnitById(String id) {
		return resolved.get(id);
	}

	/**
	 * Returns every unit available in the parent session with the given id, possibly multiple
	 * versions of the same id.
	 */
	public List<P2Unit> findAllAvailableUnitsById(String id) {
		return session.units.stream().filter(u -> u.id.equals(id)).collect(Collectors.toList());
	}

	private boolean addUnlessExcludedOrAlreadyPresent(P2Unit unit) {
		for (var prefix : excludePrefix) {
			if (unit.id.startsWith(prefix)) {
				return false;
			}
		}
		if (exclude.contains(unit.id)) {
			return false;
		}
		if (!filterProps.isEmpty() && unit.filter != null && !unit.filter.matches(filterProps)) {
			return false;
		}
		return resolved.putIfAbsent(unit.id, unit) == null;
	}

	private void resolve(P2Unit toResolve) {
		if (!addUnlessExcludedOrAlreadyPresent(toResolve)) {
			return;
		}
		for (var requirement : toResolve.requires) {
			if (requirement.hasOnlyOneProvider()) {
				resolve(requirement.getOnlyProvider());
			} else {
				var units = requirement.getProviders();
				if (units.isEmpty()) {
					addUnmetRequirement(requirement, toResolve);
				} else {
					resolve(units.get(0));
					ambiguousRequirements.add(requirement);
				}
			}
		}
	}

	private void addUnmetRequirement(P2Session.Requirement providers, P2Unit needsIt) {
		var whoNeedsIt = unmetRequirements.computeIfAbsent(providers, unused -> new TreeSet<>());
		whoNeedsIt.add(needsIt);
	}

	public List<P2Unit> getJars() {
		var jars = new ArrayList<P2Unit>();
		for (var unit : resolved.values()) {
			if (unit.id.endsWith("feature.jar")
					|| "true".equals(unit.properties.get(P2Unit.P2_TYPE_FEATURE))
					|| "true".equals(unit.properties.get(P2Unit.P2_TYPE_CATEGORY))) {
				continue;
			}
			jars.add(unit);
		}
		return jars;
	}

	public List<P2Unit> getFeatures() {
		return getUnitsWithProperty(P2Unit.P2_TYPE_FEATURE, "true");
	}

	public List<P2Unit> getCategories() {
		return getUnitsWithProperty(P2Unit.P2_TYPE_CATEGORY, "true");
	}

	public List<P2Unit> getUnitsWithProperty(String key, String value) {
		List<P2Unit> matches = new ArrayList<>();
		for (var unit : resolved.values()) {
			if (Objects.equals(value, unit.properties.get(key))) {
				matches.add(unit);
			}
		}
		return matches;
	}

	public List<String> getJarsOnMavenCentral() {
		var mavenCoords = new ArrayList<String>();
		for (var unit : getJars()) {
			var mavenState = MavenStatus.forUnit(unit);
			if (mavenState.isOnMavenCentral()) {
				mavenCoords.add(mavenState.coordinate());
			}
		}
		return mavenCoords;
	}

	public List<P2Unit> getjarsNotOnMavenCentral() {
		var notOnMaven = new ArrayList<P2Unit>();
		for (var unit : getJars()) {
			var mavenState = MavenStatus.forUnit(unit);
			if (!mavenState.isOnMavenCentral()) {
				notOnMaven.add(unit);
			}
		}
		return notOnMaven;
	}

	/** Adds every unit in the session, subject to the query filters. */
	public void addAllUnits() {
		session.units.forEach(this::addUnlessExcludedOrAlreadyPresent);
	}

	public Set<P2Session.Requirement> getAmbiguousRequirements() {
		return ambiguousRequirements;
	}

	public boolean isResolved(P2Unit unit) {
		return resolved.get(unit.getId()) == unit;
	}

	public void resolutionMessages() {
		StringBuilder builder = new StringBuilder();
		if (ambiguousRequirements.isEmpty()) {
			builder.append("No ambiguous versions.");
		} else {
			builder.append("The following dependencies had ambiguous versions available:");
		}
	}
}
