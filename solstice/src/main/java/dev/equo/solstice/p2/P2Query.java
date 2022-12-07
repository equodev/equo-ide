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

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public class P2Query {
	TreeSet<String> exclude = new TreeSet<>();
	List<String> excludePrefix = new ArrayList<>();
	List<String> excludeSuffix = new ArrayList<>();
	TreeSet<P2Unit> resolved = new TreeSet<>();
	List<UnmetRequirement> unmetRequirements = new ArrayList<>();
	List<ResolvedWithFirst> resolvedWithFirst = new ArrayList<>();

	public void exclude(String toExclude) {
		exclude.add(toExclude);
	}

	public void excludePrefix(String prefix) {
		excludePrefix.add(prefix);
	}

	public void resolve(P2Unit toResolve) {
		for (var prefix : excludePrefix) {
			if (toResolve.id.startsWith(prefix)) {
				return;
			}
		}
		if (exclude.contains(toResolve.id) || !resolved.add(toResolve)) {
			return;
		}
		for (var requirement : toResolve.requires) {
			if (requirement.hasOnlyOne()) {
				resolve(requirement.getOnlyOne());
			} else {
				var units = requirement.get();
				if (units.isEmpty()) {
					unmetRequirements.add(new UnmetRequirement(toResolve, requirement));
				} else {
					resolve(units.get(0));
					resolvedWithFirst.add(new ResolvedWithFirst(toResolve, requirement));
				}
			}
		}
	}

	private Iterable<P2Unit> jars() {
		var jars = new ArrayList<P2Unit>();
		for (var unit : resolved) {
			if (unit.id.endsWith("feature.jar")
					|| "true".equals(unit.properties.get(P2Unit.P2_TYPE_FEATURE))
					|| "true".equals(unit.properties.get(P2Unit.P2_TYPE_CATEGORY))) {
				continue;
			}
			if (unit.filter != null) {}

			jars.add(unit);
		}
		return jars;
	}

	public List<String> jarsOnMavenCentral() {
		var mavenCoords = new ArrayList<String>();
		for (var unit : jars()) {
			var coord = unit.getMavenCentralCoord();
			if (coord != null) {
				mavenCoords.add(coord);
			}
		}
		return mavenCoords;
	}

	public List<P2Unit> jarsNotOnMavenCentral() {
		var notOnMaven = new ArrayList<P2Unit>();
		for (var unit : jars()) {
			if (unit.getMavenCentralCoord() != null) {
				continue;
			}
			notOnMaven.add(unit);
		}
		return notOnMaven;
	}

	static class UnmetRequirement {
		final P2Unit target;
		final P2Session.Providers unmet;

		UnmetRequirement(P2Unit target, P2Session.Providers unmet) {
			this.target = target;
			this.unmet = unmet;
		}
	}

	static class ResolvedWithFirst {
		final P2Unit target;
		final P2Session.Providers withFirst;

		ResolvedWithFirst(P2Unit target, P2Session.Providers withFirst) {
			this.target = target;
			this.withFirst = withFirst;
		}
	}
}
