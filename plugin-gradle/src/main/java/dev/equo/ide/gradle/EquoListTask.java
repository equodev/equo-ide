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
import dev.equo.solstice.p2.P2Unit;
import java.util.Collection;
import java.util.EnumSet;
import java.util.List;
import org.gradle.api.DefaultTask;
import org.gradle.api.provider.Property;
import org.gradle.api.tasks.Internal;
import org.gradle.api.tasks.TaskAction;
import org.gradle.api.tasks.options.Option;
import org.gradle.api.tasks.options.OptionValues;

public abstract class EquoListTask extends DefaultTask {
	@Internal
	public abstract Property<EquoIdeExtension> getExtension();

	private ConsoleTable.Format format = ConsoleTable.Format.ASCII;

	@Option(option = "format", description = "Output format")
	void setFormat(ConsoleTable.Format format) {
		this.format = format;
	}

	@OptionValues("format")
	Collection<ConsoleTable.Format> getSupportedFormats() {
		return EnumSet.allOf(ConsoleTable.Format.class);
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
					"lists the id and name of all [categories|features|jars] which meet the filter criteria")
	void setAll(All all) {
		this.all = all;
	}

	@OptionValues("all")
	Collection<All> getSupportedAll() {
		return EnumSet.allOf(All.class);
	}

	@TaskAction
	public void list() throws Exception {
		var extension = getExtension().get();
		var query = extension.performQuery();
		if (all == null) {
			System.out.println(ConsoleTable.mavenStatus(query.getJars(), format));
		} else {
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
	}
}
