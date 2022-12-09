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

/** Determines whether and where a P2Unit is available in a Maven repo. */
public class MavenStatus implements Comparable<MavenStatus> {
	public static String MAVEN_CENTRAL = "mavenCentral";
	public static String MAVEN_CENTRAL_INFERRED = "mavenCentral?";
	public static String P2 = "p2";

	private final String coordinate;
	private final String repo;

	private MavenStatus(String coordinate, String repo) {
		this.coordinate = coordinate;
		this.repo = repo;
	}

	public boolean isOnMavenCentral() {
		return repo.equals(MAVEN_CENTRAL) || repo.equals(MAVEN_CENTRAL_INFERRED);
	}

	public String coordinate() {
		return coordinate;
	}

	public String repo() {
		return repo;
	}

	public static MavenStatus forUnit(P2Unit unit) {
		var group = unit.properties.get(P2Unit.MAVEN_GROUP_ID);
		var artifact = unit.properties.get(P2Unit.MAVEN_ARTIFACT_ID);
		var version = unit.properties.get(P2Unit.MAVEN_VERSION);
		if (group != null && artifact != null && version != null) {
			var repo = unit.properties.get(P2Unit.MAVEN_REPOSITORY);
			if (MavenCentralMapping.MIRROR.equals(repo)) {
				return new MavenStatus(group + ":" + artifact + ":" + version, MAVEN_CENTRAL);
			} else {
				var coord = MavenCentralMapping.getMavenCentralCoord(unit);
				if (coord != null) {
					return new MavenStatus(coord, MAVEN_CENTRAL_INFERRED);
				}
			}
		}
		return new MavenStatus(unit.id + ":" + unit.version, P2);
	}

	@Override
	public int compareTo(MavenStatus o) {
		var repoCompare = repo.compareTo(o.repo);
		if (repoCompare == 0) {
			return coordinate.compareTo(o.coordinate);
		} else {
			return repoCompare;
		}
	}
}
