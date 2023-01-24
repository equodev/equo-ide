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
import dev.equo.solstice.p2.P2Multitool;
import java.util.Collection;
import java.util.EnumSet;
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

	P2Multitool tool = new P2Multitool();

	@Option(
			option = "format",
			description = "Determines output format (can be combined with all other commands)")
	void setFormat(ConsoleTable.Format format) {
		tool.format = format;
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

	private P2Multitool.All all;

	@Option(
			option = "all",
			description =
					"Lists the id and name of all [categories|features|jars] which meet the filter criteria")
	void setAll(P2Multitool.All all) {
		this.all = all;
	}

	@OptionValues("all")
	Collection<P2Multitool.All> getSupportedAll() {
		return EnumSet.allOf(P2Multitool.All.class);
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
			tool.all(query, all);
		} else if (detail != null) {
			tool.detail(query, detail);
		} else if (raw != null) {
			tool.raw(query, raw);
		} else if (installed) {
			tool.installed(query);
		} else if (problems) {
			tool.problems(query);
		} else if (optional) {
			tool.optional(query);
		} else {
			throw new UnsupportedOperationException("Programming error");
		}
	}
}
