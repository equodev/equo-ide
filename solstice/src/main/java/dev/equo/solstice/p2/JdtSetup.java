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

/** This is kludge placeholder, will not be in 1.0. */
public class JdtSetup {
	public static String DEFAULT_VERSION = "4.26";
	public static String URL_BASE = "https://download.eclipse.org/eclipse/updates/";

	public static void mavenCoordinate(P2Query query, P2Session session) {
		query.excludePrefix("org.apache.felix.gogo");
		query.excludePrefix("org.eclipse.equinox.console");
		query.excludePrefix("org.eclipse.equinox.p2");
		query.exclude("org.eclipse.equinox.supplement");
		query.install("org.eclipse.releng.java.languages.categoryIU");
		query.install("org.eclipse.platform.ide.categoryIU");
	}
}
