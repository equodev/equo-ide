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

import java.util.List;
import javax.xml.transform.TransformerException;

public class P2Multitool {
	public ConsoleTable.Format format = ConsoleTable.Format.ASCII;

	public void detail(P2Query query, String detail) {
		var resolved = query.getInstalledUnitById(detail);
		var allAvailable = query.getAllAvailableUnitsById(detail);
		if (allAvailable.size() == 1) {
			System.out.println("1 unit available with id " + detail);
		} else {
			System.out.println(allAvailable.size() + " units available with id " + detail);
		}
		for (var v : allAvailable) {
			System.out.print("  " + v.getVersion());
			if (v == resolved) {
				System.out.println("  [x] included by install");
			} else {
				System.out.println("  [ ] not included by install");
			}
		}
		System.out.println(ConsoleTable.detail(allAvailable, format));
	}

	public void optional(P2Query query) {
		System.out.println(ConsoleTable.optionalRequirementsNotInstalled(query, format));
	}

	public void problems(P2Query query) {
		System.out.println(ConsoleTable.unmetRequirements(query, format));
		System.out.println(ConsoleTable.ambiguousRequirements(query, format));
	}

	public void installed(P2Query query) {
		if (query.getJars().isEmpty()) {
			System.out.println(ConsoleTable.mavenStatus(query.getJars(), format));
			return;
		}
		int numUnmet = query.getUnmetRequirements().size();
		int numAmbiguous = query.getAmbiguousRequirements().size();
		if (numUnmet > 0) {
			System.out.println(
					"WARNING!!! "
							+ numUnmet
							+ " unmet requirement(s), "
							+ numAmbiguous
							+ " ambigous requirement(s).");
			System.out.println("WARNING!!!  For more info: `gradlew equoList --problems`");
		} else {
			System.out.println(
					numUnmet
							+ " unmet requirement(s), "
							+ numAmbiguous
							+ " ambigous requirement(s). For more info: `gradlew equoList --problems`");
		}
		int numOptional = query.getOptionalRequirementsNotInstalled().size();
		if (numOptional > 0) {
			System.out.println(
					numOptional
							+ " optional requirement(s) were not installed. For more info: `gradlew equoList --optional`");
		} else {
			System.out.println(
					"Every optional requirement was installed. For more info: `gradlew equoList --optional`");
		}
		System.out.println(ConsoleTable.mavenStatus(query.getJars(), format));
	}

	public void raw(P2Query query, String raw) throws TransformerException {
		var allAvailable = query.getAllAvailableUnitsById(raw);
		for (var unit : allAvailable) {
			System.out.println(unit.getRawXml());
		}
	}

	public void all(P2Query query, All all) {
		query.addAllUnits();
		List<P2Unit> unitsToList;
		switch (all) {
			case CATEGORIES:
				unitsToList = query.getCategories();
				break;
			case FEATURES:
				unitsToList = query.getFeatures();
				break;
			case JARS:
				unitsToList = query.getJars();
				break;
			default:
				throw new IllegalArgumentException("Unknown " + all);
		}
		System.out.println(ConsoleTable.nameAndDescription(unitsToList, format));
	}

	public enum All {
		CATEGORIES,
		FEATURES,
		JARS
	}
}
