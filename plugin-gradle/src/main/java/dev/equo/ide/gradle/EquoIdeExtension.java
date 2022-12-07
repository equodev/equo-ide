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

import dev.equo.solstice.p2.JdtSetup;

/** The DSL inside the equoIde block. */
public class EquoIdeExtension {
	String jdtVersion = JdtSetup.DEFAULT_VERSION;

	/** Sets which eclipse release to use, such as "4.25", "4.26", or a future release. */
	public void release(String version) {
		if (version.indexOf('/') != -1) {
			throw new IllegalArgumentException("Version should not have any slashes");
		}
		jdtVersion = version;
	}
}
