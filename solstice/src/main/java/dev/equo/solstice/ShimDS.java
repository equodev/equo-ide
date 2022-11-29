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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.zip.ZipFile;
import org.osgi.framework.wiring.BundleWire;

class ShimDS {
	static final String SERVICE_COMPONENT = "Service-Component";
	private static final String STAR_DOT_XML = "OSGI-INF/*.xml";

	static String cleanHeader(String jarFile, String header) {
		if (!STAR_DOT_XML.equals(header)) {
			return header;
		} else {
			return String.join(",", starDotXML(jarFile));
		}
	}

	private static List<String> starDotXML(String jarFile) {
		String prefix = "jar:file:";
		if (!jarFile.startsWith(prefix)) {
			throw new IllegalArgumentException("jar does not start with expected prefix: " + jarFile);
		}
		if (!jarFile.endsWith("!")) {
			throw new IllegalArgumentException("jar does not end with expected suffix: !");
		}
		List<String> dotXml = new ArrayList<>();
		try (ZipFile file = new ZipFile(jarFile.substring(prefix.length(), jarFile.length() - 1))) {
			var entries = file.entries();
			while (entries.hasMoreElements()) {
				var entry = entries.nextElement().getName();
				if (entry.startsWith("OSGI-INF/") && entry.endsWith(".xml")) {
					dotXml.add(entry);
				}
			}
		} catch (IOException e) {
			throw Unchecked.wrap(e);
		}
		return dotXml;
	}

	static class BundleWiring extends Unimplemented.BundleWiring {
		@Override
		public List<BundleWire> getRequiredWires(String namespace) {
			return Collections.emptyList();
		}

		@Override
		public boolean isInUse() {
			return true;
		}

		@Override
		public List<BundleWire> getProvidedWires(String namespace) {
			return Collections.emptyList();
		}
	}
}
