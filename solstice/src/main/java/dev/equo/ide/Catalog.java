/*******************************************************************************
 * Copyright (c) 2023-2025 EquoTech, Inc. and others.
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
	protected static final String V = "${VERSION}";

	static String defaultPerspectiveFor(Catalog catalog) {
		if (catalog == Catalog.JDT || catalog == Catalog.GROOVY) {
			return "org.eclipse.jdt.ui.JavaPerspective";
		} else if (catalog == Catalog.PDE) {
			return "org.eclipse.pde.ui.PDEPerspective";
		} else if (catalog == Catalog.KOTLIN) {
			return "org.jetbrains.kotlin.perspective";
		} else if (catalog == Catalog.CDT || catalog == Catalog.RUST) {
			return "org.eclipse.cdt.ui.CPerspective";
		} else {
			return null;
		}
	}

	public static final CatalogPlatform PLATFORM = new CatalogPlatform();

	public static final CatalogJdt JDT = new CatalogJdt();

	public static final CatalogPde PDE = new CatalogPde();

	public static final Catalog EGIT =
			new Catalog(
					"egit",
					"https://download.eclipse.org/egit/updates-" + V + "/",
					jre11("6.6"),
					List.of("org.eclipse.egit.feature.group"));

	/** Pure transitive of m2e and AssistAI. */
	static final Catalog ORBIT =
			new Catalog(
					"orbit",
					"https://download.eclipse.org/tools/orbit/downloads/drops/" + V + "/repository/",
					jre11("R20230531010532"),
					List.of());

	public static final CatalogAssistAI ASSIST_AI = new CatalogAssistAI();

	public static final Catalog TABNINE =
			new Catalog(
					"tabnine",
					"https://eclipse-update-site.tabnine.com/",
					jre11(""),
					List.of("Tabnine Eclipse Plugin"));

	public static final Catalog GRADLE_BUILDSHIP =
			new Catalog(
					"gradleBuildship",
					"https://download.eclipse.org/buildship/updates/e427/releases/3.x/" + V,
					jre11("3.1.7.v20230428-1350"),
					List.of("org.eclipse.buildship.feature.group"),
					JDT);

	/** Pure transitive of m2e and others */
	private static final Catalog WST =
			new Catalog(
					"wst",
					"https://download.eclipse.org/webtools/downloads/drops/" + V + "/repository/",
					jre11("R3.30.0/R-3.30.0-20230603084739"),
					List.of());

	public static final Catalog M2E =
			new Catalog(
					"m2e",
					"https://download.eclipse.org/technology/m2e/releases/" + V,
					jre11("1.20.1").jre(17, "2.6.2"),
					List.of("org.eclipse.m2e.feature.feature.group"),
					JDT,
					WST,
					ORBIT) {
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
					jre11("11.2/cdt-11.2.0"),
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
					jre11("4.9.0/e4.27"),
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

	public static final EquoChromium EQUO_CHROMIUM = new EquoChromium();

	public static final Catalog CHATGPT =
			new Catalog.PureMaven(
					"chatGPT",
					jre11("1.0.1"),
					List.of("dev.equo.ide:equo-ide-chatgpt:" + V),
					PLATFORM,
					EQUO_CHROMIUM);

	private final String name;
	private final String p2urlTemplate;
	protected final VmVersion versions;
	private final List<String> toInstall;
	private final List<Catalog> requires;

	Catalog(String name, Catalog copyFrom, List<String> toInstall, Catalog... requires) {
		this(name, copyFrom.p2urlTemplate, copyFrom.versions, toInstall, requires);
	}

	protected Catalog(
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

	protected static VmVersion jre11(String ver) {
		return new VmVersion().jre(11, ver);
	}

	protected static VmVersion jre17(String ver) {
		return new VmVersion().jre(17, ver);
	}

	static class VmVersion {
		private TreeMap<Integer, String> versions = new TreeMap<>();

		public VmVersion jre(int vm, String catalog) {
			versions.put(vm, catalog);
			return this;
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
										+ " which is the latest version for JRE "
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
	}

	public boolean isPureMaven() {
		return this instanceof PureMaven;
	}

	public static class PureMaven extends Catalog {
		protected PureMaven(
				String name, VmVersion versions, List<String> toInstall, Catalog... requires) {
			super(name, "PureMaven", versions, toInstall, requires);
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
