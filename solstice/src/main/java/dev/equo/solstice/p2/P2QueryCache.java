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
package dev.equo.solstice.p2;

public enum P2QueryCache {
	NONE,
	ALLOW,
	FORCE_RECALCULATE;

	public boolean allowRead() {
		return this == ALLOW;
	}

	public boolean allowWrite() {
		return this == ALLOW || this == FORCE_RECALCULATE;
	}
}
