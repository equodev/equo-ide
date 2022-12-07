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

import javax.annotation.Nullable;

class MavenCentralMapping {
	static final String MIRROR = "eclipse.maven.central.mirror";
	private static final String DASH_SNAPSHOT = "-SNAPSHOT";

	static @Nullable String getMavenCentralCoord(P2Unit unit) {
		var groupId = unit.properties.get(P2Unit.MAVEN_GROUP_ID);
		var artifactId = unit.properties.get(P2Unit.MAVEN_ARTIFACT_ID);
		var version = unit.properties.get(P2Unit.MAVEN_VERSION);
		if (groupId != null && artifactId != null && version != null) {
			var groupArtifact = groupIdArtifactId(unit.properties.get(P2Unit.MAVEN_ARTIFACT_ID));
			if (groupArtifact != null) {
				if (version.endsWith(DASH_SNAPSHOT)) {
					version = version.substring(0, version.length() - DASH_SNAPSHOT.length());
				}
				return groupArtifact + ":" + version;
			}
		}
		return null;
	}

	private static final String PLATFORM = "org.eclipse.platform";
	private static final String JDT = "org.eclipse.jdt";
	private static final String PDE = "org.eclipse.pde";
	private static final String EMF = "org.eclipse.emf";
	private static final String ECF = "org.eclipse.ecf";
	private static final String OSGI = "org.osgi";

	private static String groupIdArtifactId(String bundleId) {
		if ("org.eclipse.jdt.core.compiler.batch".equals(bundleId)) {
			return JDT + ":ecj";
		} else if (bundleId.startsWith(JDT)) {
			return JDT + ":" + bundleId;
		} else if (bundleId.startsWith(PDE)) {
			return PDE + ":" + bundleId;
		} else if (bundleId.startsWith(EMF)) {
			return EMF + ":" + bundleId;
		} else if (bundleId.startsWith(ECF)) {
			return ECF + ":" + bundleId;
		} else if (bundleId.startsWith(OSGI)) {
			return OSGI + ":" + bundleId;
		} else if (bundleId.endsWith(".feature")) {
			return null;
		} else if (bundleId.startsWith("org.eclipse.ant")
				|| bundleId.startsWith("org.eclipse.core")
				|| bundleId.startsWith("org.eclipse.compare")
				|| bundleId.startsWith("org.eclipse.debug")
				|| bundleId.startsWith("org.eclipse.e4")
				|| bundleId.startsWith("org.eclipse.equinox")
				|| bundleId.startsWith("org.eclipse.help")
				|| bundleId.startsWith("org.eclipse.jface")
				|| bundleId.startsWith("org.eclipse.jsch")
				|| bundleId.startsWith("org.eclipse.ltk")
				|| bundleId.startsWith("org.eclipse.osgi")
				|| bundleId.startsWith("org.eclipse.platform")
				|| bundleId.startsWith("org.eclipse.rcp")
				|| bundleId.startsWith("org.eclipse.search")
				|| bundleId.startsWith("org.eclipse.swt")
				|| bundleId.startsWith("org.eclipse.team")
				|| bundleId.startsWith("org.eclipse.text")
				|| bundleId.startsWith("org.eclipse.update")
				|| bundleId.startsWith("org.eclipse.urischeme")
				|| bundleId.startsWith("org.eclipse.ui")) {
			return PLATFORM + ":" + bundleId;
		} else {
			return null;
		}
	}
}
