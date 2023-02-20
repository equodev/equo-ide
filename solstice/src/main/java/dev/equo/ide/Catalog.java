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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;

public class Catalog implements Comparable<Catalog> {
	private static String V = "${VERSION}";
	public static final Catalog PLATFORM =
			new Catalog(
					"platform",
					"https://download.eclipse.org/eclipse/updates/" + V,
					"4.26",
					"org.eclipse.platform.ide.categoryIU");
	public static final Catalog JDT =
			new Catalog("jdt", PLATFORM, "org.eclipse.releng.java.languages.categoryIU", PLATFORM);
	public static final Catalog GRADLE_BUILDSHIP =
			new Catalog(
					"gradleBuildship",
					"https://download.eclipse.org/buildship/updates/e423/releases/3.x/" + V,
					"3.1.6.v20220511-1359",
					"org.eclipse.buildship.feature.group",
					JDT);

	private final String name;
	private final String p2urlTemplate;
	private final String latestVersion;
	private final String toInstall;
	private final List<Catalog> requires;

	Catalog(String name, Catalog copyFrom, String toInstall, Catalog... requires) {
		this(name, copyFrom.p2urlTemplate, copyFrom.latestVersion, toInstall, requires);
	}

	Catalog(
			String name,
			String p2urlTemplate,
			String latestVersion,
			String toInstall,
			Catalog... requires) {
		this.name = name;
		if (p2urlTemplate.endsWith("/")) {
			this.p2urlTemplate = p2urlTemplate;
		} else {
			this.p2urlTemplate = p2urlTemplate + "/";
		}
		this.latestVersion = latestVersion;
		this.toInstall = toInstall;
		this.requires = Arrays.asList(requires);
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
		return Collections.singletonList(toInstall);
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
