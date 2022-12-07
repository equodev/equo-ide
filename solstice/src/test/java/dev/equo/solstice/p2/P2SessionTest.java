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

import java.io.File;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class P2SessionTest {
	private P2Session populateSession() throws Exception {
		File cacheDir = new File("build/solstice-test/" + P2SessionTest.class.getSimpleName());
		var session = new P2Session();
		try (var client = new P2Client(cacheDir)) {
			session.populateFrom(client, "https://download.eclipse.org/eclipse/updates/4.25/");
		}
		session.sort();
		return session;
	}

	@Test
	public void testResolution() throws Exception {
		var session = populateSession();
		var resolution = new P2Resolution();
		resolution.resolve(session.getUnitById("org.eclipse.platform.ide.categoryIU"));
		for (var s : resolution.jarsOnMavenCentral()) {
			System.out.println("maven: " + s);
		}
		for (var u : resolution.jarsNotOnMavenCentral()) {
			System.out.println("p2only: " + u);
		}
	}

	@Test
	public void testCategories() throws Exception {
		var session = populateSession();
		Assertions.assertThat(session.listAllCategories())
				.isEqualTo(
						"I20220831-1800.Default\n"
								+ "  Uncategorized: Default category for otherwise uncategorized features\n"
								+ "org.eclipse.equinox.target.categoryIU\n"
								+ "  Equinox Target Components: Features especially useful to install as PDE runtime targets.\n"
								+ "org.eclipse.pde.api.tools.ee.categoryIU\n"
								+ "  API Tools Execution Environment Descriptions: API Tools Execution Environment Descriptions.\n"
								+ "org.eclipse.platform.ide.categoryIU\n"
								+ "  Eclipse Platform: Minimum version of Eclipse: no source or API documentation, no PDE or JDT.\n"
								+ "org.eclipse.platform.sdk.categoryIU\n"
								+ "  Eclipse Platform SDK: Minimum version of Eclipse with source and documentation, no PDE or JDT.\n"
								+ "org.eclipse.rcp.categoryIU\n"
								+ "  Eclipse RCP Target Components: Features to use as PDE runtime target, while developing RCP applications.\n"
								+ "org.eclipse.releng.categoryIU\n"
								+ "  Releng Tools: Tools handy for committers, such as to fix copyright headings and a POM version checker.\n"
								+ "org.eclipse.releng.cvs.categoryIU\n"
								+ "  Eclipse CVS Client: Tools to allow working with CVS repositories.\n"
								+ "org.eclipse.releng.java.languages.categoryIU\n"
								+ "  Eclipse Java Development Tools: Tools to allow development with Java.\n"
								+ "org.eclipse.releng.pde.categoryIU\n"
								+ "  Eclipse Plugin Development Tools: Tools to develop bundles, plugins and features.\n"
								+ "org.eclipse.releng.testsIU\n"
								+ "  Eclipse Tests, Tools, Examples, and Extras: Collection of Misc. Features, such as unit tests, SWT and e4 tools, examples, and compatibility features not shipped as part of main SDK, but which some people may desire in creating products based on previous versions of Eclipse.\n"
								+ "org.eclipse.sdk.ide.categoryIU\n"
								+ "  Eclipse SDK: The full version of Eclipse, with source and documentation: Platform, JDT and PDE.\n");
	}

	@Test
	public void testFeatures() throws Exception {
		var session = populateSession();
		Assertions.assertThat(session.listAllFeatures())
				.isEqualTo(
						"eclipse-junit-tests\n"
								+ "  eclipse-junit-tests: (None)\n"
								+ "org.eclipse.core.runtime.feature.feature.group\n"
								+ "  Eclipse Core Runtime Infrastructure: Common OS-independent base of the Eclipse platform. (Binary runtime and user documentation.)\n"
								+ "org.eclipse.e4.core.tools.feature.feature.group\n"
								+ "  Eclipse e4 Tools: Eclipse e4 Model Tooling\n"
								+ "org.eclipse.e4.core.tools.feature.source.feature.group\n"
								+ "  Eclipse e4 Tools Developer Resources: Eclipse e4 Model Tooling\n"
								+ "org.eclipse.e4.rcp.feature.group\n"
								+ "  Eclipse 4 Rich Client Platform: The bundles typical required by Eclipse RCP applications as of version 4.0\n"
								+ "org.eclipse.e4.rcp.source.feature.group\n"
								+ "  Eclipse 4 Rich Client Platform Developer Resources: The bundles typical required by Eclipse RCP applications as of version 4.0\n"
								+ "org.eclipse.ecf.core.feature.feature.group\n"
								+ "  ECF Core Feature: This feature provides the ECF core (org.eclipse.ecf) and ECF identity (org.eclipse.ecf.identity) bundles.  These two bundles are required for all other parts of ECF.\n"
								+ "org.eclipse.ecf.core.feature.source.feature.group\n"
								+ "  ECF Core Feature Developer Resources: This feature provides the ECF core (org.eclipse.ecf) and ECF identity (org.eclipse.ecf.identity) bundles.  These two bundles are required for all other parts of ECF.\n"
								+ "org.eclipse.ecf.core.ssl.feature.feature.group\n"
								+ "  ECF Core SSL Feature: This feature provides the ECF core SSL fragment.  On Equinox-based frameworks, this fragment exposes the Equinox TrustManager to ECF FileTransfer and other ECF-based communications.\n"
								+ "org.eclipse.ecf.core.ssl.feature.source.feature.group\n"
								+ "  ECF Core SSL Feature Developer Resources: This feature provides the ECF core SSL fragment.  On Equinox-based frameworks, this fragment exposes the Equinox TrustManager to ECF FileTransfer and other ECF-based communications.\n"
								+ "org.eclipse.ecf.filetransfer.feature.feature.group\n"
								+ "  ECF Filetransfer Feature: This feature provides the ECF Filetransfer API bundle.  This API is usedby the Eclipse platform to support P2 filetransfer and is required for any of the ECF FileTransfer providers.\n"
								+ "org.eclipse.ecf.filetransfer.feature.source.feature.group\n"
								+ "  ECF Filetransfer Feature Developer Resources: This feature provides the ECF Filetransfer API bundle.  This API is usedby the Eclipse platform to support P2 filetransfer and is required for any of the ECF FileTransfer providers.\n"
								+ "org.eclipse.ecf.filetransfer.httpclient5.feature.feature.group\n"
								+ "  ECF Apache Httpclient 5 FileTransfer Provider: This feature provides the Apache HttpComponents/HttpClient 5 based FileTransfer provider used by the Eclipse platform to support P2 filetransfer.\n"
								+ "org.eclipse.ecf.filetransfer.httpclient5.feature.source.feature.group\n"
								+ "  ECF Apache Httpclient 5 FileTransfer Provider Developer Resources: This feature provides the Apache HttpComponents/HttpClient 5 based FileTransfer provider used by the Eclipse platform to support P2 filetransfer.\n"
								+ "org.eclipse.ecf.filetransfer.ssl.feature.feature.group\n"
								+ "  ECF Filetransfer SSL Feature: This feature provides the SSL support for the ECF FileTransfer API used by the Eclipse platform to support P2 filetransfer.\n"
								+ "org.eclipse.ecf.filetransfer.ssl.feature.source.feature.group\n"
								+ "  ECF Filetransfer SSL Feature Developer Resources: This feature provides the SSL support for the ECF FileTransfer API used by the Eclipse platform to support P2 filetransfer.\n"
								+ "org.eclipse.emf.common.feature.group\n"
								+ "  EMF Common: Common platform-independent utilities used throughout EMF, including collection classes, notifiers, adapters, and commands.\n"
								+ "org.eclipse.emf.common.source.feature.group\n"
								+ "  EMF Common Developer Resources: Common platform-independent utilities used throughout EMF, including collection classes, notifiers, adapters, and commands.\n"
								+ "org.eclipse.emf.databinding.edit.feature.group\n"
								+ "  EMF Edit Data Binding: Support for using EMF objects with JFace's data binding framework and integrating with EMF's editing framework.\n"
								+ "org.eclipse.emf.databinding.edit.source.feature.group\n"
								+ "  EMF Edit Data Binding Developer Resources: Support for using EMF objects with JFace's data binding framework and integrating with EMF's editing framework.\n"
								+ "org.eclipse.emf.databinding.feature.group\n"
								+ "  EMF Data Binding: Support for using EMF objects with JFace's data binding framework.\n"
								+ "org.eclipse.emf.databinding.source.feature.group\n"
								+ "  EMF Data Binding Developer Resources: Support for using EMF objects with JFace's data binding framework.\n"
								+ "org.eclipse.emf.ecore.feature.group\n"
								+ "  EMF - Eclipse Modeling Framework Core Runtime: The core runtime for EMF, including EMF's common utilities, Ecore, XML/XMI persistence, and the change model.\n"
								+ "org.eclipse.emf.ecore.source.feature.group\n"
								+ "  EMF - Eclipse Modeling Framework Core Runtime Developer Resources: The core runtime for EMF, including EMF's common utilities, Ecore, XML/XMI persistence, and the change model.\n"
								+ "org.eclipse.emf.edit.feature.group\n"
								+ "  EMF Edit: Platform-independent framework for viewing and editing EMF objects.\n"
								+ "org.eclipse.emf.edit.source.feature.group\n"
								+ "  EMF Edit Developer Resources: Platform-independent framework for viewing and editing EMF objects.\n"
								+ "org.eclipse.equinox.compendium.sdk.feature.group\n"
								+ "  Equinox Compendium SDK: A collection of Equinox bundles  and source that implement optional parts of the current OSGi specifications--the so called &quot;Compendium Services&quot;. This feature includes the corresponding source and is intended to be added to target platforms at development time rather than deployed with end-user systems.\n"
								+ "org.eclipse.equinox.core.feature.feature.group\n"
								+ "  Equinox Core Function: (None)\n"
								+ "org.eclipse.equinox.core.feature.source.feature.group\n"
								+ "  Equinox Core Function Developer Resources: (None)\n"
								+ "org.eclipse.equinox.core.sdk.feature.group\n"
								+ "  Equinox Core SDK: A collection of core Equinox bundles and source including the Equinox framework implementation itself. This feature includes the corresponding source and is intended to be added to target platforms at development time rather than deployed with end-user systems.\n"
								+ "org.eclipse.equinox.executable\n"
								+ "  Eclipse Platform Launcher Executables for Multi-Architecture Builds: Platform specific launchers.\n"
								+ "org.eclipse.equinox.executable.feature.group\n"
								+ "  Eclipse Platform Launcher Executables: Platform specific launchers.\n"
								+ "org.eclipse.equinox.p2.core.feature.feature.group\n"
								+ "  Equinox p2, headless functionalities: Provides a minimal headless provisioning system.\n"
								+ "org.eclipse.equinox.p2.core.feature.source.feature.group\n"
								+ "  Equinox p2 Core Function Source: Source code for the Equinox provisioning platform\n"
								+ "org.eclipse.equinox.p2.discovery.feature.feature.group\n"
								+ "  Equinox p2, Discovery UI support: All of the bundles that comprise Equinox p2 discovery. This feature is intended to be used by integrators building on discovery.\n"
								+ "org.eclipse.equinox.p2.discovery.feature.source.feature.group\n"
								+ "  Eclipse p2 Discovery Developer Resources: Source code for the Equinox p2 Discovery\n"
								+ "org.eclipse.equinox.p2.extras.feature.feature.group\n"
								+ "  Equinox p2, backward compatibility support: Provides some backward compatibility support (e.g. drop-ins, legacy update site) and the metadata generation facility.\n"
								+ "org.eclipse.equinox.p2.extras.feature.source.feature.group\n"
								+ "  Equinox p2 RCP Management Facilities: Source code for the Equinox provisioning platform\n"
								+ "org.eclipse.equinox.p2.rcp.feature.feature.group\n"
								+ "  Equinox p2, minimal support for RCP applications: Provides the minimal set of p2 bundles to use in RCP applications.\n"
								+ "org.eclipse.equinox.p2.rcp.feature.source.feature.group\n"
								+ "  Equinox p2 RCP Management Facilities Source: Source code for the Equinox provisioning platform\n"
								+ "org.eclipse.equinox.p2.sdk.feature.group\n"
								+ "  Equinox p2, SDK: All of the bundles and source that comprise the Equinox p2 provisioning platform. This feature includes the corresponding source and is intended to be added to target platforms at development time rather than deployed with end-user systems.\n"
								+ "org.eclipse.equinox.p2.user.ui.feature.group\n"
								+ "  Equinox p2, Provisioning for IDEs.: Eclipse p2 Provisioning Platform for use in IDE related scenarios\n"
								+ "org.eclipse.equinox.p2.user.ui.source.feature.group\n"
								+ "  Eclipse p2 Provisioning Developer Resources: Source code for the Equinox provisioning platform\n"
								+ "org.eclipse.equinox.sdk.feature.group\n"
								+ "  Equinox Target Components: All of the bundles and source that are produced by the Equinox project.  This includes basic OSGi framework support, all implemented compendium services, the p2 provisioning platform and various server-side support bundles.This feature includes the corresponding source and is intended to be added to target platforms at development time rather than deployed with end-user systems.\n"
								+ "org.eclipse.equinox.server.core.feature.group\n"
								+ "  Core Server Feature: (None)\n"
								+ "org.eclipse.equinox.server.core.source.feature.group\n"
								+ "  Core Server Feature Developer Resources: (None)\n"
								+ "org.eclipse.equinox.server.jetty.feature.group\n"
								+ "  Jetty Http Server Feature: (None)\n"
								+ "org.eclipse.equinox.server.jetty.source.feature.group\n"
								+ "  Jetty Http Server Feature Developer Resources: (None)\n"
								+ "org.eclipse.equinox.server.p2.feature.group\n"
								+ "  p2 Server Feature: (None)\n"
								+ "org.eclipse.equinox.server.p2.source.feature.group\n"
								+ "  p2 Server Feature Developer Resources: (None)\n"
								+ "org.eclipse.help.feature.group\n"
								+ "  Eclipse Help System: Eclipse help system.\n"
								+ "org.eclipse.help.source.feature.group\n"
								+ "  Eclipse Help Developer Resources: Source code for the Eclipse help system.\n"
								+ "org.eclipse.jdt.feature.group\n"
								+ "  Eclipse Java Development Tools: Eclipse Java development tools (binary runtime and user documentation).\n"
								+ "org.eclipse.jdt.source.feature.group\n"
								+ "  Eclipse JDT Plug-in Developer Resources: API documentation and source code zips for Eclipse Java development tools.\n"
								+ "org.eclipse.jdt.ui.unittest.junit.feature.feature.group\n"
								+ "  JUnit Test runner client for UnitTest View: JUnit test runner client for UnitTest View\n"
								+ "org.eclipse.jdt.ui.unittest.junit.feature.source.feature.group\n"
								+ "  JUnit Test runner client for UnitTest View Developer Resources: JUnit test runner client for UnitTest View\n"
								+ "org.eclipse.pde.feature.group\n"
								+ "  Eclipse Plug-in Development Environment: Eclipse plug-in development environment.\n"
								+ "org.eclipse.pde.source.feature.group\n"
								+ "  Eclipse PDE Plug-in Developer Resources: Eclipse plug-in development environment, including documentation and source code zips.\n"
								+ "org.eclipse.pde.unittest.junit.feature.group\n"
								+ "  Eclipse Plug-in Test runner client for UnitTest View (Experimental): Eclipse plug-in test runner client for UnitTest View.\n"
								+ "org.eclipse.pde.unittest.junit.source.feature.group\n"
								+ "  Eclipse PDE Plug-in Developer Unit Test support Resources: Eclipse plug-in development environment Unit Test support, including documentation and source code zips.\n"
								+ "org.eclipse.platform.feature.group\n"
								+ "  Eclipse Platform: Common OS-independent base of the Eclipse platform. (Binary runtime and user documentation.)\n"
								+ "org.eclipse.platform.ide\n"
								+ "  Eclipse Platform: 4.25 Release of the Eclipse Platform.\n"
								+ "org.eclipse.platform.sdk\n"
								+ "  Eclipse Platform SDK: 4.25 Release of the Platform SDK.\n"
								+ "org.eclipse.platform.source.feature.group\n"
								+ "  Eclipse Platform Plug-in Developer Resources: Common OS-independent API documentation and source code zips for the Eclipse Platform.\n"
								+ "org.eclipse.rcp.configuration.feature.group\n"
								+ "  Eclipse Product Configuration: Configuration information for the Eclipse product\n"
								+ "org.eclipse.rcp.feature.group\n"
								+ "  Eclipse RCP: Rich Client Platform\n"
								+ "org.eclipse.rcp.id\n"
								+ "  Eclipse RCP: (None)\n"
								+ "org.eclipse.rcp.sdk.id\n"
								+ "  Eclipse RCP SDK: (None)\n"
								+ "org.eclipse.rcp.source.feature.group\n"
								+ "  Eclipse RCP Plug-in Developer Resources: Source code zips for the Eclipse RCP.\n"
								+ "org.eclipse.sdk.examples.feature.group\n"
								+ "  Eclipse SDK Examples: Eclipse SDK examples. Used in conjunction with Eclipse Project SDK.\n"
								+ "org.eclipse.sdk.examples.source.feature.group\n"
								+ "  Eclipse SDK Examples Source: Eclipse SDK examples. Used in conjunction with Eclipse Project SDK.\n"
								+ "org.eclipse.sdk.feature.group\n"
								+ "  Eclipse Project SDK: SDK for Eclipse.\n"
								+ "org.eclipse.sdk.ide\n"
								+ "  Eclipse SDK: 4.25 Release of the Eclipse SDK.\n"
								+ "org.eclipse.sdk.tests.feature.group\n"
								+ "  Eclipse SDK Tests: Eclipse SDK test plug-ins. Used in conjunction with Eclipse Project SDK.\n"
								+ "org.eclipse.swt.tools.feature.feature.group\n"
								+ "  SWT Tools: SWT Tools, including Sleak, SWT Spy Plugin, and JniGen.\n"
								+ "org.eclipse.swt.tools.feature.source.feature.group\n"
								+ "  SWT Tools Developer Resources: SWT Tools, including Sleak, SWT Spy Plugin, and JniGen.\n"
								+ "org.eclipse.test.feature.group\n"
								+ "  Eclipse Test Framework: Eclipse Test Framework. Used in conjunction with Eclipse JUnit tests.\n"
								+ "org.eclipse.tips.feature.feature.group\n"
								+ "  Tip of the Day UI Feature: Contains the Eclipse Tips framework.\n"
								+ "org.eclipse.tips.feature.source.feature.group\n"
								+ "  Tip of the Day UI Feature Developer Resources: Contains the Eclipse Tips framework.\n");
	}
}
