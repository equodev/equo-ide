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
package dev.equo.ide.maven;

import dev.equo.solstice.p2.ConsoleTable;
import dev.equo.solstice.p2.P2Multitool;
import javax.xml.transform.TransformerException;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

/** Lists the p2 dependencies of an Eclipse application. */
@Mojo(name = "list")
public class ListMojo extends AbstractP2Mojo {
	/** Determines output format (can be combined with all other commands). */
	@Parameter(property = "format", defaultValue = "ascii")
	private ConsoleTable.Format format;

	/** Lists the jars which were installed. */
	@Parameter(property = "installed", defaultValue = "false")
	private boolean installed;

	/** Lists any problems with the installed jars. */
	@Parameter(property = "problems", defaultValue = "false")
	private boolean problems;

	/** Lists any optional requirements which were not installed. */
	@Parameter(property = "optional", defaultValue = "false")
	private boolean optional;

	/** Lists the id and name of all [categories|features|jars] which meet the filter criteria. */
	@Parameter(property = "all")
	private P2Multitool.All all;

	/** Lists properties and requirements for all available versions of the given unit id. */
	@Parameter(property = "detail")
	private String detail;

	/** Lists the raw xml for all available versions of the given unit id. */
	@Parameter(property = "raw", required = false)
	private String raw;

	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
		var tool = new P2Multitool();
		tool.format = format;
		tool.installed = installed;
		tool.problems = problems;
		tool.optional = optional;
		tool.all = all;
		tool.detail = detail;
		tool.raw = raw;
		if (!tool.argsAreValid()) {
			throw new MojoExecutionException(
					"Exactly one of -Dinstalled, -Dproblems, -Doptional, -Dall=[categories|features|jars], -Ddetail=id, or -Draw=id must be set.\n"
							+ "`mvn help:describe -Dcmd=equo-ide:list -Ddetail` for more info or visit https://github.com/equodev/equo-ide/blob/main/P2_MULTITOOL.md");
		}
		try {
			tool.dump(query());
		} catch (TransformerException e) {
			throw new RuntimeException(e);
		}
	}
}
