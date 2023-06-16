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
package dev.equo.solstice;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.osgi.framework.Constants;

public class SolsticeManifestTest {
	@Test
	public void junitPlatformCommons() {
		var orig =
				("org.junit.platform.commons;version=\"1.9.1\";status=STABLE\n"
								+ " ;uses:=\"org.apiguardian.api,org.junit.platform.commons.util\",org.junit.\n"
								+ " platform.commons.annotation;version=\"1.9.1\";status=STABLE;uses:=\"org.ap\n"
								+ " iguardian.api\",org.junit.platform.commons.function;version=\"1.9.1\";stat\n"
								+ " us=MAINTAINED;uses:=\"org.apiguardian.api\",org.junit.platform.commons.lo\n"
								+ " gging;version=\"1.9.1\";status=INTERNAL;mandatory:=status;uses:=\"org.apig\n"
								+ " uardian.api\",org.junit.platform.commons.support;version=\"1.9.1\";status=\n"
								+ " MAINTAINED;uses:=\"org.apiguardian.api,org.junit.platform.commons,org.ju\n"
								+ " nit.platform.commons.function\",org.junit.platform.commons.util;version=\n"
								+ " \"1.9.1\";status=DEPRECATED;uses:=\"org.apiguardian.api,org.junit.platform\n"
								+ " .commons,org.junit.platform.commons.function\"")
						.replace("\n ", "");
		var headers = SolsticeManifest.parseAndStripManifestHeader(Constants.EXPORT_PACKAGE, orig);
		Assertions.assertThat(headers)
				.containsExactly(
						"org.junit.platform.commons",
						"org.junit.platform.commons.annotation",
						"org.junit.platform.commons.function",
						"org.junit.platform.commons.logging",
						"org.junit.platform.commons.support",
						"org.junit.platform.commons.util");
	}
}
