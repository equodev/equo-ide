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

import com.diffplug.common.swt.os.SwtPlatform;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import org.eclipse.osgi.service.environment.EnvironmentInfo;

class ShimEnvironmentInfo implements EnvironmentInfo {
	@Override
	public String[] getCommandLineArgs() {
		return new String[0];
	}

	@Override
	public String[] getFrameworkArgs() {
		return new String[0];
	}

	@Override
	public String[] getNonFrameworkArgs() {
		return new String[0];
	}

	@Override
	public String getNL() {
		return Locale.getDefault().getLanguage();
	}

	@Override
	public String getOSArch() {
		return SwtPlatform.getRunning().getArch();
	}

	@Override
	public String getOS() {
		return SwtPlatform.getRunning().getOs();
	}

	@Override
	public String getWS() {
		return SwtPlatform.getRunning().getWs();
	}

	@Override
	public boolean inDebugMode() {
		return false;
	}

	@Override
	public boolean inDevelopmentMode() {
		return false;
	}

	private final Map<String, String> properties = new HashMap<>();

	@Override
	public synchronized String getProperty(String key) {
		return properties.get(key);
	}

	@Override
	public synchronized String setProperty(String key, String value) {
		return properties.put(key, value);
	}
}
