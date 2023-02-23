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
package dev.equo.ide;

import java.util.List;
import javax.annotation.Nullable;

public class Catalog implements Comparable<Catalog> {
	private static String V = "${VERSION}";
	public static final Catalog PLATFORM =
			new Catalog(
					"platform",
					"https://download.eclipse.org/eclipse/updates/" + V,
					"4.26",
					List.of("org.eclipse.platform.ide.categoryIU"));
	public static final Catalog JDT =
			new Catalog(
					"jdt", PLATFORM, List.of("org.eclipse.releng.java.languages.categoryIU"), PLATFORM);
	public static final Catalog GRADLE_BUILDSHIP =
			new Catalog(
					"gradleBuildship",
					"https://download.eclipse.org/buildship/updates/e423/releases/3.x/" + V,
					"3.1.6.v20220511-1359",
					List.of("org.eclipse.buildship.feature.group"),
					JDT);
	public static final Catalog PDE =
			new Catalog("pde", PLATFORM, List.of("org.eclipse.releng.pde.categoryIU"), JDT);

	public static final Catalog KOTLIN =
			new Catalog(
					"kotlin",
					"https://files.pkg.jetbrains.space/kotlin/p/kotlin-eclipse/main/" + V,
					"0.8.21", // 0.8.24 is broken in many ways
					List.of(
							"org.jetbrains.kotlin.feature.feature.group",
							"org.jetbrains.kotlin.gradle.feature.feature.group"),
					JDT);

	public static final Catalog TM_TERMINAL =
			new Catalog(
					"tmTerminal",
					"https://download.eclipse.org/tools/cdt/releases/11.0/cdt-" + V,
					"11.0.0",
					List.of(
							"org.eclipse.tm.terminal.feature.feature.group",
							"org.eclipse.tm.terminal.view.feature.feature.group"),
					PLATFORM);

	public static final Catalog CDT =
			new Catalog(
					"cdt",
					TM_TERMINAL,
					List.of(
							"org.eclipse.launchbar.feature.group",
							"org.eclipse.cdt.visualizer.feature.group",
							"org.eclipse.cdt.unittest.feature.feature.group",
							"org.eclipse.cdt.testsrunner.feature.feature.group",
							"org.eclipse.cdt.native.feature.group",
							"org.eclipse.cdt.msw.feature.group",
							"org.eclipse.cdt.managedbuilder.llvm.feature.group",
							"org.eclipse.cdt.llvm.dsf.lldb.feature.group",
							"org.eclipse.cdt.launch.serial.feature.feature.group",
							"org.eclipse.cdt.launch.remote.feature.group",
							"org.eclipse.cdt.gnu.multicorevisualizer.feature.group",
							"org.eclipse.cdt.feature.group",
							"org.eclipse.cdt.debug.ui.memory.feature.group",
							"org.eclipse.cdt.debug.standalone.feature.group",
							"org.eclipse.cdt.debug.gdbjtag.feature.group",
							"org.eclipse.cdt.core.autotools.feature.group",
							"org.eclipse.cdt.cmake.feature.group",
							"org.eclipse.cdt.build.crossgcc.feature.group",
							"org.eclipse.cdt.autotools.feature.group"),
					TM_TERMINAL);

	public static final Catalog RUST =
			new Catalog(
					"rust",
					"https://download.eclipse.org/corrosion/releases/" + V,
					"1.2.4",
					List.of("org.eclipse.corrosion.product", "org.eclipse.corrosion.feature.feature.group"),
					TM_TERMINAL);

	private final String name;
	private final String p2urlTemplate;
	private final String latestVersion;
	private final List<String> toInstall;
	private final List<Catalog> requires;

	Catalog(String name, Catalog copyFrom, List<String> toInstall, Catalog... requires) {
		this(name, copyFrom.p2urlTemplate, copyFrom.latestVersion, toInstall, requires);
	}

	Catalog(
			String name,
			String p2urlTemplate,
			String latestVersion,
			List<String> toInstall,
			Catalog... requires) {
		this.name = name;
		if (p2urlTemplate.endsWith("/")) {
			this.p2urlTemplate = p2urlTemplate;
		} else {
			this.p2urlTemplate = p2urlTemplate + "/";
		}
		this.latestVersion = latestVersion;
		this.toInstall = toInstall;
		this.requires = List.of(requires);
	}

	public String getP2UrlTemplate() {
		return p2urlTemplate;
	}

	public String getName() {
		return name;
	}

	public List<Catalog> getRequires() {
		return requires;
	}

	public String getUrlForOverride(@Nullable String override) {
		if (override == null) {
			return p2urlTemplate.replace(V, latestVersion);
		} else if (isUrl(override)) {
			return override;
		} else {
			return p2urlTemplate.replace(V, override);
		}
	}

	public List<String> getTargetsFor(@Nullable String override) {
		return toInstall;
	}

	public static boolean isUrl(String maybeUrl) {
		return maybeUrl.startsWith("https:") || maybeUrl.startsWith("http:");
	}

	@Override
	public int compareTo(Catalog o) {
		return name.compareTo(o.name);
	}

	@Override
	public int hashCode() {
		return name.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Catalog) {
			return ((Catalog) obj).name.equals(name);
		} else {
			return false;
		}
	}
}
