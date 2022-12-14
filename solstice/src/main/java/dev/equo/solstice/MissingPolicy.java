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

import java.util.TreeMap;
import java.util.TreeSet;
import org.osgi.framework.Bundle;
import org.slf4j.LoggerFactory;

public abstract class MissingPolicy {
	protected final String kind;

	protected MissingPolicy(String kind) {
		this.kind = kind;
	}

	public abstract void isMissing(String pkg, Bundle neededBy);

	public abstract void initFinished();

	public static class Permissive extends MissingPolicy {
		public Permissive(String kind) {
			super(kind);
		}

		TreeMap<String, TreeSet> missing = new TreeMap<>();

		@Override
		public void isMissing(String pkg, Bundle neededBy) {
			missing.computeIfAbsent(pkg, unused -> new TreeSet<>()).add(neededBy);
		}

		@Override
		public void initFinished() {
			var logger = LoggerFactory.getLogger(MissingPolicy.class);
			if (missing.isEmpty()) {
				logger.info("No missing " + kind + "s.");
			} else {
				for (var entry : missing.entrySet()) {
					logger.warn("Missing " + kind + " " + entry.getKey() + " needed by " + entry.getValue());
				}
			}
		}
	}
}
