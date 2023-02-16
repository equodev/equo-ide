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

public class EquoFeature {
	private static String V = "${VERSION}";
	public static final EquoFeature PLATFORM =
			new EquoFeature(
					"platform",
					"https://download.eclipse.org/eclipse/updates/" + V,
					"4.26",
					"org.eclipse.platform.ide.categoryIU");
	public static final EquoFeature JDT =
			new EquoFeature(
					"jdt",
					"https://download.eclipse.org/eclipse/updates/" + V,
					"4.26",
					"org.eclipse.releng.java.languages.categoryIU",
					PLATFORM);
	public static final EquoFeature GRADLE_BUILDSHIP =
			new EquoFeature(
					"gradleBuildship",
					"https://download.eclipse.org/buildship/updates/e423/releases/3.x/" + V,
					"3.1.6.v20220511-1359",
					"org.eclipse.buildship.feature.group",
					JDT);

	private final String name;
	private final String p2urlTemplate;
	private final String latestVersion;
	private final String toInstall;
	private final List<EquoFeature> requires;

	EquoFeature(
			String name,
			String p2urlTemplate,
			String latestVersion,
			String toInstall,
			EquoFeature... requires) {
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

	public List<EquoFeature> getRequires() {
		return requires;
	}

	public String getUrlForOverride(@Nullable String override) {
		if (override == null) {
			return p2urlTemplate.replace(V, latestVersion);
		} else if (override.startsWith("https:") || override.startsWith("http:")) {
			return override;
		} else {
			return p2urlTemplate.replace(V, override);
		}
	}

	public List<String> getTargetsFor(@Nullable String override) {
		return Collections.singletonList(toInstall);
	}
}
