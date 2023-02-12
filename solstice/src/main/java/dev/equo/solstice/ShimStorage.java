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

import java.io.File;
import java.util.Map;
import javax.annotation.Nullable;
import org.eclipse.osgi.service.datalocation.Location;
import org.slf4j.Logger;

class ShimStorage {
	private final File configDir;

	ShimStorage(Map<String, String> props, Logger logger) {
		String configArea = props.get(Location.CONFIGURATION_AREA_TYPE);
		if (configArea == null) {
			logger.warn(
					"Recommend setting "
							+ Location.CONFIGURATION_AREA_TYPE
							+ " to a directory, getDataFile will return null");
			this.configDir = null;
		} else {
			this.configDir = new File(configArea);
		}
	}

	File getDataFileBundle(ShimBundle bundle, @Nullable String filename) {
		if (configDir == null) {
			return null;
		}
		File dir = new File(configDir, bundle.getSymbolicName() + "/" + bundle.getBundleId() + "/data");
		dir.mkdirs();
		if (filename == null) {
			return dir;
		} else {
			return new File(dir, filename);
		}
	}
}
