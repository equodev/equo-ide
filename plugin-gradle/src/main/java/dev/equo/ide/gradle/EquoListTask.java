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
package dev.equo.ide.gradle;

import dev.equo.solstice.p2.ConsoleTable;
import dev.equo.solstice.p2.P2Client;
import dev.equo.solstice.p2.P2Query;
import dev.equo.solstice.p2.P2Unit;
import java.util.Collection;
import java.util.EnumSet;
import java.util.List;
import javax.xml.transform.TransformerException;
import org.gradle.api.DefaultTask;
import org.gradle.api.provider.Property;
import org.gradle.api.tasks.Internal;
import org.gradle.api.tasks.TaskAction;
import org.gradle.api.tasks.options.Option;
import org.gradle.api.tasks.options.OptionValues;

public abstract class EquoListTask extends DefaultTask {
	@Internal
	public abstract Property<P2Client.Caching> getCaching();

	@Internal
	public abstract Property<EquoIdeExtension> getExtension();

	private ConsoleTable.Format format = ConsoleTable.Format.ASCII;

	@Option(
			option = "format",
			description = "Determines output format (can be combined with all other commands)")
	void setFormat(ConsoleTable.Format format) {
		this.format = format;
	}

	@OptionValues("format")
	Collection<ConsoleTable.Format> getSupportedFormats() {
		return EnumSet.allOf(ConsoleTable.Format.class);
	}

	private boolean installed = false;

	@Option(option = "installed", description = "Lists the jars which were installed")
	void setInstalled(boolean installed) {
		this.installed = installed;
	}

	private boolean problems = false;

	@Option(option = "problems", description = "Lists any problems with the installed jars")
	void setProblems(boolean problems) {
		this.problems = problems;
	}

	private boolean optional = false;

	@Option(
			option = "optional",
			description = "Lists any optional requirements which were not installed")
	void setOptional(boolean optional) {
		this.optional = optional;
	}

	private All all;

	public enum All {
		CATEGORIES,
		FEATURES,
		JARS
	}

	@Option(
			option = "all",
			description =
					"Lists the id and name of all [categories|features|jars] which meet the filter criteria")
	void setAll(All all) {
		this.all = all;
	}

	@OptionValues("all")
	Collection<All> getSupportedAll() {
		return EnumSet.allOf(All.class);
	}

	private String detail;

	@Option(
			option = "detail",
			description =
					"Lists properties and requirements for all available versions of the given unit id")
	void setDetail(String detail) {
		this.detail = detail;
	}

	private String raw;

	@Option(
			option = "raw",
			description = "Lists the raw xml for all available versions of the given unit id")
	void setRaw(String raw) {
		this.raw = raw;
	}

	@TaskAction
	public void list() throws Exception {
		int numArgs = 0;
		if (installed) ++numArgs;
		if (problems) ++numArgs;
		if (optional) ++numArgs;
		if (all != null) ++numArgs;
		if (detail != null) ++numArgs;
		if (raw != null) ++numArgs;
		if (numArgs != 1) {
			throw new IllegalArgumentException(
					"Exactly one of --installed, --problems, --optional, --all, --detail, or --raw must be set.\n"
							+ "`gradlew help --task equoList` for more info or visit https://github.com/equodev/equo-ide/blob/main/P2_MULTITOOL.md");
		}
		var extension = getExtension().get();
		var query = extension.performQuery(getCaching().get());
		if (all != null) {
			all(query, all, format);
		} else if (detail != null) {
			detail(query, detail, format);
		} else if (raw != null) {
			raw(query, raw);
		} else if (installed) {
			installed(query, format);
		} else if (problems) {
			problems(query, format);
		} else if (optional) {
			optional(query, format);
		} else {
			throw new UnsupportedOperationException("Programming error");
		}
	}

	private static void all(P2Query query, All all, ConsoleTable.Format format) {
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

	private static void detail(P2Query query, String detail, ConsoleTable.Format format) {
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

	private static void raw(P2Query query, String raw) throws TransformerException {
		var allAvailable = query.getAllAvailableUnitsById(raw);
		for (var unit : allAvailable) {
			System.out.println(unit.getRawXml());
		}
	}

	private static void installed(P2Query query, ConsoleTable.Format format) {
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

	private static void problems(P2Query query, ConsoleTable.Format format) {
		System.out.println(ConsoleTable.unmetRequirements(query, format));
		System.out.println(ConsoleTable.ambiguousRequirements(query, format));
	}

	private static void optional(P2Query query, ConsoleTable.Format format) {
		System.out.println(ConsoleTable.optionalRequirementsNotInstalled(query, format));
	}
}
