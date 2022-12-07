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

public class P2Resolution {
	TreeSet<Unit> resolved = new TreeSet<>();
	List<UnmetRequirement> unmetRequirements = new ArrayList<>();
	List<ResolvedWithFirst> resolvedWithFirst = new ArrayList<>();

	public void resolve(Unit toResolve) {
		if (!resolved.add(toResolve)) {
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

	public List<String> jarsOnMavenCentral() {
		var mavenCoords = new ArrayList<String>();
		for (var unit : resolved) {
			var coord = unit.getMavenCentralCoord();
			if (coord != null) {
				mavenCoords.add(coord);
			}
		}
		return mavenCoords;
	}

	public List<Unit> jarsNotOnMavenCentral() {
		var notOnMaven = new ArrayList<Unit>();
		for (var unit : resolved) {
			if (unit.getMavenCentralCoord() != null) {
				continue;
			}
			if ("true".equals(unit.properties.get(Unit.P2_TYPE_FEATURE))) {
				continue;
			}
			if ("true".equals(unit.properties.get(Unit.P2_TYPE_CATEGORY))) {
				continue;
			}
			notOnMaven.add(unit);
		}
		return notOnMaven;
	}

	static class UnmetRequirement {
		final Unit target;
		final P2Session.Providers unmet;

		UnmetRequirement(Unit target, P2Session.Providers unmet) {
			this.target = target;
			this.unmet = unmet;
		}
	}

	static class ResolvedWithFirst {
		final Unit target;
		final P2Session.Providers withFirst;

		ResolvedWithFirst(Unit target, P2Session.Providers withFirst) {
			this.target = target;
			this.withFirst = withFirst;
		}
	}
}
