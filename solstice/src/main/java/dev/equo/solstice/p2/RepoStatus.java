/*******************************************************************************
 * Copyright (c) 2022-2023 EquoTech, Inc. and others.
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

/**
 * Determines where a P2Unit is available. The {@link #repo()} method returns one of the following
 * values:
 *
 * <ul>
 *   <li>{@code maven central} means the jar is on maven central. ({@link #MAVEN_CENTRAL})
 *   <li>{@code maven central?} means the jar on maven central according to <a
 *       href="https://github.com/equodev/equo-ide/blob/main/solstice/src/main/java/dev/equo/solstice/p2/MavenCentralMapping.java">this
 *       heuristic</a>. ({@link #MAVEN_CENTRAL_INFERRED})
 *   <li>{@code p2 4.25} to indicate that the jar is on p2. The part after the p2 is the last
 *       segment in the p2 repository URL to help disambiguate between multiple p2 repositories.
 *       ({@link #P2_})
 * </ul>
 */
public class RepoStatus implements Comparable<RepoStatus> {
	/** This unit is available on some maven server, determined after this token. */
	public static final String MAVEN_ = "maven ";

	/**
	 * This unit is available on Maven Central because of a property {@code maven-repository =
	 * eclipse.maven.central.mirror}
	 */
	public static final String MAVEN_CENTRAL = MAVEN_ + "central";

	/**
	 * This unit is probably available on Maven Central because of the logic in <a
	 * href="https://github.com/equodev/equo-ide/blob/main/solstice/src/main/java/dev/equo/solstice/p2/MavenCentralMapping.java">MavenCentralMapping.java</a>
	 */
	public static final String MAVEN_CENTRAL_INFERRED = MAVEN_ + "central?";

	/** This unit is only available in p2. */
	public static final String P2_ = "p2 ";

	private final String coordinate;
	private final String repo;

	private RepoStatus(String coordinate, String repo) {
		this.coordinate = coordinate;
		this.repo = repo;
	}

	// TODO: some users might have internal maven repositories. We should have a way to hook into
	// those
	//	public boolean isOnMaven() {
	//		return repo.startsWith(MAVEN_);
	//	}

	/**
	 * {@link #repo()} is equal to either {@link #MAVEN_CENTRAL} or {@link #MAVEN_CENTRAL_INFERRED}.
	 */
	public boolean isOnMavenCentral() {
		return repo.equals(MAVEN_CENTRAL) || repo.equals(MAVEN_CENTRAL_INFERRED);
	}

	/** group:artifact:version if {@link #isOnMavenCentral()} else id:version. */
	public String coordinate() {
		return coordinate;
	}

	/** UI string for showing where jars come from in a table, see class description. */
	public String repo() {
		return repo;
	}

	public static RepoStatus forUnit(P2Unit unit) {
		var group = unit.properties.get(P2Unit.MAVEN_GROUP_ID);
		var artifact = unit.properties.get(P2Unit.MAVEN_ARTIFACT_ID);
		var version = unit.properties.get(P2Unit.MAVEN_VERSION);
		RepoStatus repoStatus = null;
		if (group != null && artifact != null && version != null) {
			if (unit.getId().equals("org.eclipse.equinox.preferences")
					&& unit.getVersion().toString().equals("3.10.0.v20220503-1634")) {
				// See https://github.com/eclipse-equinox/equinox.bundles/issues/54
				repoStatus= new RepoStatus("org.eclipse.platform:" + artifact + ":3.10.1", MAVEN_CENTRAL);
			} else if (unit.getId().equals("org.eclipse.osgi.util")
					&& unit.getVersion().toString().equals("3.7.0.v20220427-2144")) {
				// See https://github.com/eclipse-equinox/equinox.framework/issues/70
				repoStatus= new RepoStatus("org.eclipse.platform:" + artifact + ":3.7.1", MAVEN_CENTRAL);
			}
			var repo = unit.properties.get(P2Unit.MAVEN_REPOSITORY);
			if (MavenCentralMapping.MIRROR.equals(repo)) {
				repoStatus= new RepoStatus(group + ":" + artifact + ":" + version, MAVEN_CENTRAL);
			} else {
				var coord = MavenCentralMapping.getMavenCentralCoord(unit);
				if (coord != null) {
					repoStatus= new RepoStatus(coord, MAVEN_CENTRAL_INFERRED);
				}
			}
		}
		if (null!=repoStatus) {
			return repoStatus;
		} else {
			return new RepoStatus(unit.id + ":" + unit.version, P2_ + unit.getRepoUrlLastSegment());
		}
	}

	/** Sorts on repo first, then based on coordinate. */
	@Override
	public int compareTo(RepoStatus o) {
		var repoCompare = repo.compareTo(o.repo);
		if (repoCompare == 0) {
			return coordinate.compareTo(o.coordinate);
		} else {
			return repoCompare;
		}
	}

}
