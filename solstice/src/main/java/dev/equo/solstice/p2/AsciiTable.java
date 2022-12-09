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
import java.util.Comparator;

public class AsciiTable {
	/** Returns an ascii table with the {@link MavenStatus} of all its units. */
	public static String mavenTable(Iterable<P2Unit> units) {
		var mavenStates = new ArrayList<MavenStatus>();
		for (var unit : units) {
			mavenStates.add(MavenStatus.forUnit(unit));
		}
		mavenStates.sort(Comparator.naturalOrder());

		var coordinate = new TableColumn("maven coordinate / p2 id").with(MavenStatus::coordinate);
		var repo = new TableColumn("repo").with(MavenStatus::repo);
		return Table.getTable(mavenStates, coordinate, repo);
	}
}
