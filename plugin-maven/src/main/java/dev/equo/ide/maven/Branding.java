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

import java.io.File;
import org.apache.maven.plugins.annotations.Parameter;
import org.sonatype.inject.Nullable;

public class Branding {
	@Parameter(required = false)
	@Nullable
	String title;

	@Parameter(required = false)
	@Nullable
	File icon;

	@Parameter(required = false)
	@Nullable
	File splash;
}
