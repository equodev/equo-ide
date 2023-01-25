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
	public ConsoleTable.Format format = ConsoleTable.Format.ascii;
	public boolean installed = false;
	public boolean problems = false;
	public boolean optional = false;
	public P2Multitool.All all;
	public String detail;
	public String raw;

	public boolean argsAreValid() {
		int numArgs = 0;
		if (installed) ++numArgs;
		if (problems) ++numArgs;
		if (optional) ++numArgs;
		if (all != null) ++numArgs;
		if (detail != null) ++numArgs;
		if (raw != null) ++numArgs;
		return numArgs == 1;
	}

	public void dump(P2Query query) throws TransformerException {
		if (all != null) {
			all(query, all);
		} else if (detail != null) {
			detail(query, detail);
		} else if (raw != null) {
			raw(query, raw);
		} else if (installed) {
			installed(query);
		} else if (problems) {
			problems(query);
		} else if (optional) {
			optional(query);
		} else {
			throw new UnsupportedOperationException("Programming error");
		}
	}

	private void detail(P2Query query, String detail) {
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

	private void optional(P2Query query) {
		System.out.println(ConsoleTable.optionalRequirementsNotInstalled(query, format));
	}

	private void problems(P2Query query) {
		System.out.println(ConsoleTable.unmetRequirements(query, format));
		System.out.println(ConsoleTable.ambiguousRequirements(query, format));
	}

	private void installed(P2Query query) {
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

	private void raw(P2Query query, String raw) throws TransformerException {
		var allAvailable = query.getAllAvailableUnitsById(raw);
		for (var unit : allAvailable) {
			System.out.println(unit.getRawXml());
		}
	}

	private void all(P2Query query, All all) {
		query.addAllUnits();
		List<P2Unit> unitsToList;
		switch (all) {
			case categories:
				unitsToList = query.getCategories();
				break;
			case features:
				unitsToList = query.getFeatures();
				break;
			case jars:
				unitsToList = query.getJars();
				break;
			default:
				throw new IllegalArgumentException("Unknown " + all);
		}
		System.out.println(ConsoleTable.nameAndDescription(unitsToList, format));
	}

	public enum All {
		categories,
		features,
		jars
	}
}
