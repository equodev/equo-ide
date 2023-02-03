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
package dev.equo.solstice;

import java.util.Optional;
import org.osgi.framework.Bundle;
import org.osgi.framework.connect.FrameworkUtilHelper;

/** Equinox will sometimes use this to determine what bundle a class comes from. */
public class SolsticeFrameworkUtilHelper implements FrameworkUtilHelper {
	private static Solstice owner;

	public static void initialize(Solstice owner) {
		SolsticeFrameworkUtilHelper.owner = owner;
	}

	@Override
	public Optional<Bundle> getBundle(Class<?> classFromBundle) {
		if (owner == null) {
			// this class is not needed when running under Atomos, otherwise this ought to be a hard error
			return Optional.empty();
		}
		var domain = classFromBundle.getProtectionDomain();
		var source = domain.getCodeSource();
		if (source == null) {
			return Optional.of(owner.systemBundle);
		}
		var location = source.getLocation();
		return Optional.of(owner.bundleForUrl(location));
	}
}
