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
import java.util.Collection;
import java.util.Comparator;

/** Formats P2Unit for display at the console. */
public class ConsoleTable {
	public enum Format {
		ASCII,
		CSV
	}

	/** Returns a table with the {@link MavenStatus} of all its units. */
	public static String mavenStatus(Iterable<P2Unit> units, Format kind) {
		var mavenStates = new ArrayList<MavenStatus>();
		for (var unit : units) {
			mavenStates.add(MavenStatus.forUnit(unit));
		}
		mavenStates.sort(Comparator.naturalOrder());

		var coordinate = new TableColumn("maven coordinate / p2 id").with(MavenStatus::coordinate);
		var repo = new TableColumn("repo").with(MavenStatus::repo);
		return Table.getTable(kind, mavenStates, coordinate, repo);
	}

	/** Returns a table with the id and name of all its units. */
	public static String nameAndDescription(Collection<P2Unit> units, Format kind) {
		var id = new TableColumn("id").with(P2Unit::getId);
		var name =
				new TableColumn("name")
						.<P2Unit>with(
								u -> {
									var n = u.properties.get(P2Unit.P2_NAME);
									return n != null ? n : "(null)";
								});
		return Table.getTable(kind, units, id, name);
	}
}
