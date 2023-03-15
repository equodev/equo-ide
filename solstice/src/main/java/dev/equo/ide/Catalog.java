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

import dev.equo.solstice.p2.P2Model;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.Nullable;
import org.slf4j.LoggerFactory;

public class Catalog implements Comparable<Catalog> {
	private static final String V = "${VERSION}";
	public static final Catalog PLATFORM =
			new Catalog(
					"platform",
					"https://download.eclipse.org/eclipse/updates/" + V,
					jre11("4.27"),
					List.of("org.eclipse.platform.ide.categoryIU"));
	public static final Catalog JDT =
			new Catalog(
					"jdt", PLATFORM, List.of("org.eclipse.releng.java.languages.categoryIU"), PLATFORM);
	public static final Catalog GRADLE_BUILDSHIP =
			new Catalog(
					"gradleBuildship",
					"https://download.eclipse.org/buildship/updates/e423/releases/3.x/" + V,
					jre11("3.1.6.v20220511-1359"),
					List.of("org.eclipse.buildship.feature.group"),
					JDT);

	/** Pure transitive of m2e and others */
	private static final Catalog LSP4J =
			new Catalog(
					"lsp4j",
					"https://download.eclipse.org/lsp4j/updates/releases/" + V,
					jre11("0.20.0"), // TODO: waiting on WST 3.29
					List.of());

	/** Pure transitive of m2e and others */
	private static final Catalog WST =
			new Catalog(
					"wst",
					"https://download.eclipse.org/webtools/downloads/drops/" + V + "/repository/",
					jre11("R3.28.0/R-3.28.0-20221120050827"), // TODO: 3.29 is still in RC
					List.of());

	public static final Catalog M2E =
			new Catalog(
					"m2e",
					"https://download.eclipse.org/technology/m2e/releases/" + V,
					jre11("1.20.1").jre(17, "2.1.2"),
					List.of("org.eclipse.m2e.feature.feature.group"),
					JDT,
					WST,
					LSP4J) { // TODO: waiting on WST 3.29
				@Override
				public Map<String, P2Model.Filter> getFiltersFor(@Nullable String override) {
					return Map.of(
							"m2e-nested-jar-has-lucene",
							P2Model.Filter.create(
									filter -> {
										filter.excludePrefix("org.apache.lucene.");
									}));
				}
			};
	public static final Catalog PDE =
			new Catalog("pde", PLATFORM, List.of("org.eclipse.releng.pde.categoryIU"), JDT);

	public static final Catalog KOTLIN =
			new Catalog(
					"kotlin",
					"https://files.pkg.jetbrains.space/kotlin/p/kotlin-eclipse/main/" + V,
					jre11("0.8.21"), // 0.8.24 is broken in many
					// ways
					List.of(
							"org.jetbrains.kotlin.feature.feature.group",
							"org.jetbrains.kotlin.gradle.feature.feature.group"),
					JDT);

	public static final Catalog TM_TERMINAL =
			new Catalog(
					"tmTerminal",
					"https://download.eclipse.org/tools/cdt/releases/" + V,
					jre11("11.1/cdt-11.1.0"),
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
					jre11("1.2.4"),
					List.of("org.eclipse.corrosion.product", "org.eclipse.corrosion.feature.feature.group"),
					TM_TERMINAL);

	public static final Catalog GROOVY =
			new Catalog(
					"groovy",
					"https://groovy.jfrog.io/artifactory/plugins-release/org/codehaus/groovy/groovy-eclipse-integration/"
							+ V,
					jre11("4.8.0/e4.26"), // TODO: waiting on 4.9.0
					List.of(
							"org.codehaus.groovy.compilerless.feature.feature.group",
							"org.codehaus.groovy40.feature.feature.group"
							// there's also groovy30 and groovy25 which have the same name but different versions
							// and
							// our p2 can't handle multiple versions. as it stands we always get the latest one,
							// groovy40
							//
							// someday we also might want to add
							// org.codehaus.groovy.m2eclipse.feature.feature.group
							),
					PLATFORM);

	private final String name;
	private final String p2urlTemplate;
	protected final VmVersion versions;
	private final List<String> toInstall;
	private final List<Catalog> requires;

	Catalog(String name, Catalog copyFrom, List<String> toInstall, Catalog... requires) {
		this(name, copyFrom.p2urlTemplate, copyFrom.versions, toInstall, requires);
	}

	Catalog(
			String name,
			String p2urlTemplate,
			VmVersion versions,
			List<String> toInstall,
			Catalog... requires) {
		this.name = name;
		if (p2urlTemplate.endsWith("/")) {
			this.p2urlTemplate = p2urlTemplate;
		} else {
			this.p2urlTemplate = p2urlTemplate + "/";
		}
		this.versions = versions;
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
			return p2urlTemplate.replace(V, versions.getAndWarn(this));
		} else if (isUrl(override)) {
			return override;
		} else {
			return p2urlTemplate.replace(V, override);
		}
	}

	public List<String> getTargetsFor(@Nullable String override) {
		return toInstall;
	}

	public Map<String, P2Model.Filter> getFiltersFor(@Nullable String override) {
		return Map.of();
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

	private static VmVersion jre11(String ver) {
		return new VmVersion().jre(11, ver);
	}

	static class VmVersion {
		private TreeMap<Integer, String> versions = new TreeMap<>();

		public VmVersion jre(int vm, String catalog) {
			versions.put(vm, catalog);
			return this;
		}

		private String get(Catalog catalog) {
			return versions.floorEntry(JVM_VER).getValue();
		}

		public String getAndWarn(Catalog catalog) {
			var entry = versions.floorEntry(JVM_VER);
			if (entry == null) {
				var oldest = versions.firstEntry();
				throw new IllegalArgumentException(
						"You are running on JRE "
								+ JVM_VER
								+ ", but the minimum supported version of "
								+ catalog.name
								+ " is "
								+ oldest.getValue()
								+ " which requires JRE "
								+ oldest.getKey()
								+ "+.");
			}
			var latest = versions.lastEntry();
			if (!entry.equals(latest)) {
				LoggerFactory.getLogger(Catalog.class)
						.warn(
								"You are using "
										+ catalog.name
										+ " "
										+ entry.getValue()
										+ " which requires JRE "
										+ entry.getKey()
										+ ". There is a newer version available, "
										+ catalog.name
										+ " "
										+ latest.getValue()
										+ ", but it requires JRE "
										+ latest.getKey()
										+ " and you are running JRE "
										+ JVM_VER
										+ ".");
			}
			return entry.getValue();
		}

		private String latest() {
			return versions.lastEntry().getValue();
		}
	}

	private static final int JVM_VER;

	static {
		String ver = System.getProperty("java.version");
		if (ver.startsWith("1.8")) {
			JVM_VER = 8;
		} else {
			Matcher matcher = Pattern.compile("(\\d+)").matcher(ver);
			matcher.find();
			JVM_VER = Integer.parseInt(matcher.group(1));
		}
	}
}
