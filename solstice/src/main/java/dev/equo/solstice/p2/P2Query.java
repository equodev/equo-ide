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
import java.util.TreeSet;
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
	private TreeSet<P2Unit> resolved = new TreeSet<>();
	private List<UnmetRequirement> unmetRequirements = new ArrayList<>();
	private List<ResolvedWithFirst> resolvedWithFirst = new ArrayList<>();
	private @Nullable SwtPlatform platform;

	public P2Session getSession() {
		return session;
	}

	public void exclude(String toExclude) {
		exclude.add(toExclude);
	}

	public void excludePrefix(String prefix) {
		excludePrefix.add(prefix);
	}

	public void setPlatform(@Nullable SwtPlatform platform) {
		this.platform = platform;
	}

	public void resolve(String idToResolve) {
		resolve(session.getUnitById(idToResolve));
	}

	private void resolve(P2Unit toResolve) {
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
		var props = new HashMap<String, String>();
		if (platform != null) {
			props.put("osgi.os", platform.getOs());
			props.put("osgi.ws", platform.getWs());
			props.put("osgi.arch", platform.getArch());
		}
		var jars = new ArrayList<P2Unit>();
		for (var unit : resolved) {
			if (unit.id.endsWith("feature.jar")
					|| "true".equals(unit.properties.get(P2Unit.P2_TYPE_FEATURE))
					|| "true".equals(unit.properties.get(P2Unit.P2_TYPE_CATEGORY))) {
				continue;
			}
			if (platform != null && unit.filter != null && !unit.filter.matches(props)) {
				continue;
			}
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
