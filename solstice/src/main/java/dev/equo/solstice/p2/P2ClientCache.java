/*******************************************************************************
 * Copyright (c) 2023 EquoTech, Inc. and others.
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

/** The various caching modes that {@link P2Client} supports. */
public enum P2ClientCache {
	NONE,
	ALLOW_OFFLINE,
	PREFER_OFFLINE,
	OFFLINE;

	boolean tryOfflineFirst() {
		return this == OFFLINE || this == PREFER_OFFLINE;
	}

	boolean networkAllowed() {
		return this != OFFLINE;
	}

	boolean cacheAllowed() {
		return this != NONE;
	}

	public static P2ClientCache defaultIfOfflineIsAndForceRecalculateIs(
			boolean isOffline, boolean forceRecalculate) {
		return isOffline
				? P2ClientCache.OFFLINE
				: (forceRecalculate ? P2ClientCache.ALLOW_OFFLINE : P2ClientCache.PREFER_OFFLINE);
	}
}
