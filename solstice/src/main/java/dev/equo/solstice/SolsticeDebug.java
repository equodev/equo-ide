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

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.ServiceEvent;

class SolsticeDebug {
	static String typeToString(BundleEvent e) {
		switch (e.getType()) {
			case BundleEvent.INSTALLED:
				return "INSTALLED";
			case BundleEvent.RESOLVED:
				return "RESOLVED";
			case BundleEvent.STARTED:
				return "STARTED";
			case BundleEvent.STARTING:
				return "STARTING";
			case BundleEvent.STOPPED:
				return "STOPPED";
			case BundleEvent.STOPPING:
				return "STOPPING";
			case BundleEvent.UNINSTALLED:
				return "UNINSTALLED";
			case BundleEvent.UNRESOLVED:
				return "UNRESOLVED";
			case BundleEvent.UPDATED:
				return "UPDATED";
			default:
				return "UNKNOWN";
		}
	}

	static String typeToString(ServiceEvent e) {
		switch (e.getType()) {
			case ServiceEvent.REGISTERED:
				return "REGISTERED";
			case ServiceEvent.UNREGISTERING:
				return "UNREGISTERING";
			case ServiceEvent.MODIFIED:
				return "MODIFIED";
			case ServiceEvent.MODIFIED_ENDMATCH:
				return "MODIFIED_ENDMATCH";
			default:
				return "UNKNOWN";
		}
	}

	static String stateToString(Bundle e) {
		switch (e.getState()) {
			case Bundle.ACTIVE:
				return "ACTIVE";
			case Bundle.INSTALLED:
				return "INSTALLED";
			case Bundle.RESOLVED:
				return "RESOLVED";
			case Bundle.STARTING:
				return "STARTING";
			case Bundle.STOPPING:
				return "STOPPING";
			case Bundle.UNINSTALLED:
				return "UNINSTALLED";
			default:
				return "UNKNOWN";
		}
	}
}
