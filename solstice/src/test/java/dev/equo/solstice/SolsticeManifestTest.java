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

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.osgi.framework.BundleException;

public class SolsticeManifestTest {
	@Test
	public void testManifestStrip() throws BundleException {
		var orig =
				("org.eclipse.equinox.internal.p2.persistence; x-friends:=\n"
								+ " \"org.eclipse.equinox.p2.artifact.repository,  org.eclipse.equinox.p2.en\n"
								+ " gine,  org.eclipse.equinox.p2.metadata.repository,  org.eclipse.equinox\n"
								+ " .p2.ui.importexport,  org.eclipse.equinox.p2.repository.tools\",org.ecli\n"
								+ " pse.equinox.internal.p2.repository; x-friends:=\"org.eclipse.equinox.p2.\n"
								+ " artifact.repository,  org.eclipse.equinox.p2.metadata.repository,  org.\n"
								+ " eclipse.equinox.p2.updatesite,  org.eclipse.equinox.p2.repository.tools\n"
								+ " ,  org.eclipse.equinox.p2.transport.ecf,  org.eclipse.equinox.p2.engine\n"
								+ " ,  org.eclipse.equinox.p2.discovery.compatibility,  org.eclipse.equinox\n"
								+ " .p2.publisher\",org.eclipse.equinox.internal.p2.repository.helpers; x-fr\n"
								+ " iends:=\"org.eclipse.equinox.p2.artifact.repository,  org.eclipse.equino\n"
								+ " x.p2.exemplarysetup,  org.eclipse.equinox.p2.metadata.repository,  org.\n"
								+ " eclipse.equinox.p2.operations,  org.eclipse.equinox.p2.publisher,  org.\n"
								+ " eclipse.equinox.p2.repository.tools,  org.eclipse.equinox.p2.ui,  org.e\n"
								+ " clipse.equinox.p2.updatesite\",org.eclipse.equinox.internal.provisional.\n"
								+ " p2.repository,org.eclipse.equinox.p2.repository;version=\"2.0.0\",org.ecl\n"
								+ " ipse.equinox.p2.repository.artifact;version=\"2.3.0\",org.eclipse.equinox\n"
								+ " .p2.repository.artifact.spi;version=\"2.0.0\",org.eclipse.equinox.p2.repo\n"
								+ " sitory.metadata;version=\"2.0.0\",org.eclipse.equinox.p2.repository.metad\n"
								+ " ata.spi;version=\"2.0.0\",org.eclipse.equinox.p2.repository.spi;version=\"\n"
								+ " 2.0.0\"")
						.replace("\n ", "");
		var headers = SolsticeManifest.parseAndStripManifestHeader(orig);
		Assertions.assertThat(headers)
				.containsExactly(
						"org.eclipse.equinox.internal.p2.persistence",
						"org.eclipse.equinox.internal.p2.repository",
						"org.eclipse.equinox.internal.p2.repository.helpers",
						"org.eclipse.equinox.internal.provisional.p2.repository",
						"org.eclipse.equinox.p2.repository",
						"org.eclipse.equinox.p2.repository.artifact",
						"org.eclipse.equinox.p2.repository.artifact.spi",
						"org.eclipse.equinox.p2.repository.metadata",
						"org.eclipse.equinox.p2.repository.metadata.spi",
						"org.eclipse.equinox.p2.repository.spi");
	}
}
