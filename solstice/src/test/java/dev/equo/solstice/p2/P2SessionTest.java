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

import com.diffplug.common.swt.os.SwtPlatform;
import java.io.File;
import java.util.stream.Collectors;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class P2SessionTest {
	private P2Session populateSession() throws Exception {
		File cacheDir = new File("build/solstice-test/" + P2SessionTest.class.getSimpleName());
		var session = new P2Session();
		try (var client = new P2Client(cacheDir)) {
			session.populateFrom(client, "https://download.eclipse.org/eclipse/updates/4.25/");
		}
		return session;
	}

	@Test
	public void testQuery() throws Exception {
		var session = populateSession();
		var query = new P2Query();
		query.exclude("a.jre.javase");
		query.exclude("org.eclipse.platform_root");
		query.exclude("org.eclipse.rcp_root");
		query.excludePrefix("tooling");
		query.resolve(session.getUnitById("org.eclipse.platform.ide.categoryIU"));
		var jars = query.jarsOnMavenCentral().stream().collect(Collectors.joining(","));
		Assertions.assertThat(jars)
				.isEqualTo(
						"com.ibm.icu:icu4j:67.1,net.java.dev.jna:jna:5.8.0,net.java.dev.jna:jna-platform:5.8.0,jakarta.servlet:jakarta.servlet-api:4.0.4,javax.servlet.jsp:javax.servlet.jsp-api:2.3.3,org.junit.platform:junit-platform-commons:1.9.0,org.junit.platform:junit-platform-engine:1.9.0,org.junit.platform:junit-platform-launcher:1.9.0,commons-fileupload:commons-fileupload:1.4,commons-io:commons-io:2.11.0,org.apache.felix:org.apache.felix.gogo.command:1.1.2,org.apache.felix:org.apache.felix.gogo.runtime:1.1.6,org.apache.felix:org.apache.felix.gogo.shell:1.1.4,org.apache.felix:org.apache.felix.scr:2.2.2,org.apiguardian:apiguardian-api:1.1.2,org.eclipse.platform:org.eclipse.ant.core:3.6.500,org.eclipse.platform:org.eclipse.compare:3.8.500,org.eclipse.platform:org.eclipse.compare.core:3.7.100,org.eclipse.platform:org.eclipse.compare.win32:1.2.900,org.eclipse.platform:org.eclipse.core.commands:3.10.200,org.eclipse.platform:org.eclipse.core.contenttype:3.8.200,org.eclipse.platform:org.eclipse.core.databinding:1.11.100,org.eclipse.platform:org.eclipse.core.databinding.beans:1.8.0,org.eclipse.platform:org.eclipse.core.databinding.observable:1.12.0,org.eclipse.platform:org.eclipse.core.databinding.property:1.9.0,org.eclipse.platform:org.eclipse.core.expressions:3.8.200,org.eclipse.platform:org.eclipse.core.externaltools:1.2.300,org.eclipse.platform:org.eclipse.core.filebuffers:3.7.200,org.eclipse.platform:org.eclipse.core.filesystem:1.9.500,org.eclipse.platform:org.eclipse.core.filesystem.linux.aarch64:1.4.200,org.eclipse.platform:org.eclipse.core.filesystem.linux.ppc64le:1.4.200,org.eclipse.platform:org.eclipse.core.filesystem.linux.x86_64:1.2.400,org.eclipse.platform:org.eclipse.core.filesystem.macosx:1.3.400,org.eclipse.platform:org.eclipse.core.filesystem.win32.x86_64:1.4.300,org.eclipse.platform:org.eclipse.core.jobs:3.13.100,org.eclipse.platform:org.eclipse.core.net:1.4.0,org.eclipse.platform:org.eclipse.core.net.linux:1.0.200,org.eclipse.platform:org.eclipse.core.net.win32:1.0.100,org.eclipse.platform:org.eclipse.core.net.win32.x86_64:1.1.700,org.eclipse.platform:org.eclipse.core.resources:3.18.0,org.eclipse.platform:org.eclipse.core.resources.win32.x86_64:3.5.500,org.eclipse.platform:org.eclipse.core.runtime:3.26.0,org.eclipse.platform:org.eclipse.core.variables:3.5.100,org.eclipse.platform:org.eclipse.debug.core:3.20.0,org.eclipse.platform:org.eclipse.debug.ui:3.17.0,org.eclipse.platform:org.eclipse.debug.ui.launchview:1.0.300,org.eclipse.platform:org.eclipse.e4.core.commands:1.0.200,org.eclipse.platform:org.eclipse.e4.core.contexts:1.11.0,org.eclipse.platform:org.eclipse.e4.core.di:1.8.300,org.eclipse.platform:org.eclipse.e4.core.di.annotations:1.7.200,org.eclipse.platform:org.eclipse.e4.core.di.extensions:0.17.200,org.eclipse.platform:org.eclipse.e4.core.di.extensions.supplier:0.16.400,org.eclipse.platform:org.eclipse.e4.core.services:2.3.300,org.eclipse.platform:org.eclipse.e4.emf.xpath:0.3.0,org.eclipse.platform:org.eclipse.e4.ui.bindings:0.13.100,org.eclipse.platform:org.eclipse.e4.ui.css.core:0.13.300,org.eclipse.platform:org.eclipse.e4.ui.css.swt:0.14.600,org.eclipse.platform:org.eclipse.e4.ui.css.swt.theme:0.13.100,org.eclipse.platform:org.eclipse.e4.ui.di:1.4.0,org.eclipse.platform:org.eclipse.e4.ui.dialogs:1.3.300,org.eclipse.platform:org.eclipse.e4.ui.ide:3.16.100,org.eclipse.platform:org.eclipse.e4.ui.model.workbench:2.2.200,org.eclipse.platform:org.eclipse.e4.ui.progress:0.3.500,org.eclipse.platform:org.eclipse.e4.ui.services:1.5.0,org.eclipse.platform:org.eclipse.e4.ui.swt.gtk:1.1.100,org.eclipse.platform:org.eclipse.e4.ui.swt.win32:1.1.0,org.eclipse.platform:org.eclipse.e4.ui.widgets:1.3.0,org.eclipse.platform:org.eclipse.e4.ui.workbench:1.13.200,org.eclipse.platform:org.eclipse.e4.ui.workbench.addons.swt:1.4.400,org.eclipse.platform:org.eclipse.e4.ui.workbench.renderers.swt:0.15.600,org.eclipse.platform:org.eclipse.e4.ui.workbench.renderers.swt.cocoa:0.12.600,org.eclipse.platform:org.eclipse.e4.ui.workbench.swt:0.16.600,org.eclipse.platform:org.eclipse.e4.ui.workbench3:0.16.0,org.eclipse.ecf:org.eclipse.ecf:3.10.0,org.eclipse.ecf:org.eclipse.ecf.filetransfer:5.1.102,org.eclipse.ecf:org.eclipse.ecf.identity:3.9.402,org.eclipse.ecf:org.eclipse.ecf.provider.filetransfer:3.2.800,org.eclipse.ecf:org.eclipse.ecf.provider.filetransfer.httpclient5:1.0.300,org.eclipse.ecf:org.eclipse.ecf.provider.filetransfer.httpclient5.win32:1.0.400,org.eclipse.ecf:org.eclipse.ecf.provider.filetransfer.ssl:1.0.201,org.eclipse.ecf:org.eclipse.ecf.ssl:1.2.401,org.eclipse.emf:org.eclipse.emf.common:2.26.0,org.eclipse.emf:org.eclipse.emf.ecore:2.28.0,org.eclipse.emf:org.eclipse.emf.ecore.change:2.14.0,org.eclipse.emf:org.eclipse.emf.ecore.xmi:2.17.0,org.eclipse.platform:org.eclipse.equinox.app:1.6.200,org.eclipse.platform:org.eclipse.equinox.bidi:1.4.200,org.eclipse.platform:org.eclipse.equinox.common:3.16.200,org.eclipse.platform:org.eclipse.equinox.concurrent:1.2.100,org.eclipse.platform:org.eclipse.equinox.console:1.4.500,org.eclipse.platform:org.eclipse.equinox.event:1.6.100,org.eclipse.platform:org.eclipse.equinox.frameworkadmin:2.2.100,org.eclipse.platform:org.eclipse.equinox.frameworkadmin.equinox:1.2.200,org.eclipse.platform:org.eclipse.equinox.http.jetty:3.8.100,org.eclipse.platform:org.eclipse.equinox.http.registry:1.3.200,org.eclipse.platform:org.eclipse.equinox.http.servlet:1.7.300,org.eclipse.platform:org.eclipse.equinox.jsp.jasper:1.1.700,org.eclipse.platform:org.eclipse.equinox.jsp.jasper.registry:1.2.100,org.eclipse.platform:org.eclipse.equinox.launcher:1.6.400,org.eclipse.platform:org.eclipse.equinox.launcher.cocoa.macosx.aarch64:1.2.600,org.eclipse.platform:org.eclipse.equinox.launcher.cocoa.macosx.x86_64:1.2.600,org.eclipse.platform:org.eclipse.equinox.launcher.gtk.linux.aarch64:1.2.600,org.eclipse.platform:org.eclipse.equinox.launcher.gtk.linux.ppc64le:1.2.600,org.eclipse.platform:org.eclipse.equinox.launcher.gtk.linux.x86_64:1.2.600,org.eclipse.platform:org.eclipse.equinox.launcher.win32.win32.x86_64:1.2.600,org.eclipse.platform:org.eclipse.equinox.p2.artifact.repository:1.4.500,org.eclipse.platform:org.eclipse.equinox.p2.console:1.2.0,org.eclipse.platform:org.eclipse.equinox.p2.core:2.9.200,org.eclipse.platform:org.eclipse.equinox.p2.director:2.5.400,org.eclipse.platform:org.eclipse.equinox.p2.director.app:1.2.200,org.eclipse.platform:org.eclipse.equinox.p2.directorywatcher:1.3.0,org.eclipse.platform:org.eclipse.equinox.p2.engine:2.7.500,org.eclipse.platform:org.eclipse.equinox.p2.extensionlocation:1.4.100,org.eclipse.platform:org.eclipse.equinox.p2.garbagecollector:1.2.0,org.eclipse.platform:org.eclipse.equinox.p2.jarprocessor:1.2.300,org.eclipse.platform:org.eclipse.equinox.p2.metadata:2.6.300,org.eclipse.platform:org.eclipse.equinox.p2.metadata.repository:1.4.100,org.eclipse.platform:org.eclipse.equinox.p2.operations:2.6.100,org.eclipse.platform:org.eclipse.equinox.p2.publisher:1.7.200,org.eclipse.platform:org.eclipse.equinox.p2.publisher.eclipse:1.4.100,org.eclipse.platform:org.eclipse.equinox.p2.reconciler.dropins:1.4.100,org.eclipse.platform:org.eclipse.equinox.p2.repository:2.6.200,org.eclipse.platform:org.eclipse.equinox.p2.repository.tools:2.3.100,org.eclipse.platform:org.eclipse.equinox.p2.touchpoint.eclipse:2.3.300,org.eclipse.platform:org.eclipse.equinox.p2.touchpoint.natives:1.4.400,org.eclipse.platform:org.eclipse.equinox.p2.transport.ecf:1.3.300,org.eclipse.platform:org.eclipse.equinox.p2.ui:2.7.600,org.eclipse.platform:org.eclipse.equinox.p2.ui.importexport:1.3.300,org.eclipse.platform:org.eclipse.equinox.p2.ui.sdk:1.2.100,org.eclipse.platform:org.eclipse.equinox.p2.ui.sdk.scheduler:1.5.400,org.eclipse.platform:org.eclipse.equinox.p2.updatechecker:1.3.0,org.eclipse.platform:org.eclipse.equinox.p2.updatesite:1.2.300,org.eclipse.platform:org.eclipse.equinox.preferences:3.10.100,org.eclipse.platform:org.eclipse.equinox.registry:3.11.200,org.eclipse.platform:org.eclipse.equinox.security:1.3.1000,org.eclipse.platform:org.eclipse.equinox.security.linux:1.0.200,org.eclipse.platform:org.eclipse.equinox.security.macosx:1.101.400,org.eclipse.platform:org.eclipse.equinox.security.ui:1.3.300,org.eclipse.platform:org.eclipse.equinox.security.win32.x86_64:1.1.300,org.eclipse.platform:org.eclipse.equinox.simpleconfigurator:1.4.100,org.eclipse.platform:org.eclipse.equinox.simpleconfigurator.manipulator:2.2.0,org.eclipse.platform:org.eclipse.help:3.9.100,org.eclipse.platform:org.eclipse.help.base:4.3.800,org.eclipse.platform:org.eclipse.help.ui:4.4.100,org.eclipse.platform:org.eclipse.help.webapp:3.10.800,org.eclipse.jdt:org.eclipse.jdt.annotation:2.2.700,org.eclipse.jdt:org.eclipse.jdt.core:3.31.0,org.eclipse.jetty:jetty-http:10.0.11,org.eclipse.jetty:jetty-io:10.0.11,org.eclipse.jetty:jetty-security:10.0.11,org.eclipse.jetty:jetty-server:10.0.11,org.eclipse.jetty:jetty-servlet:10.0.11,org.eclipse.jetty:jetty-util:10.0.11,org.eclipse.jetty:jetty-util-ajax:10.0.11,org.eclipse.platform:org.eclipse.jface:3.27.0,org.eclipse.platform:org.eclipse.jface.databinding:1.13.0,org.eclipse.platform:org.eclipse.jface.notifications:0.5.0,org.eclipse.platform:org.eclipse.jface.text:3.21.0,org.eclipse.platform:org.eclipse.jsch.core:1.4.0,org.eclipse.platform:org.eclipse.jsch.ui:1.4.200,org.eclipse.platform:org.eclipse.ltk.core.refactoring:3.13.0,org.eclipse.platform:org.eclipse.ltk.ui.refactoring:3.12.200,org.eclipse.platform:org.eclipse.osgi:3.18.100,org.eclipse.platform:org.eclipse.osgi.compatibility.state:1.2.700,org.eclipse.platform:org.eclipse.osgi.services:3.11.0,org.eclipse.platform:org.eclipse.osgi.util:3.7.100,org.eclipse.platform:org.eclipse.platform:4.25.0,org.eclipse.platform:org.eclipse.platform.doc.user:4.25.0,org.eclipse.platform:org.eclipse.rcp:4.25.0,org.eclipse.platform:org.eclipse.search:3.14.200,org.eclipse.platform:org.eclipse.swt:3.121.0,org.eclipse.platform:org.eclipse.swt.cocoa.macosx.aarch64:3.121.0,org.eclipse.platform:org.eclipse.swt.cocoa.macosx.x86_64:3.121.0,org.eclipse.platform:org.eclipse.swt.gtk.linux.aarch64:3.121.0,org.eclipse.platform:org.eclipse.swt.gtk.linux.ppc64le:3.121.0,org.eclipse.platform:org.eclipse.swt.gtk.linux.x86_64:3.121.0,org.eclipse.platform:org.eclipse.swt.win32.win32.x86_64:3.121.0,org.eclipse.platform:org.eclipse.team.core:3.9.500,org.eclipse.platform:org.eclipse.team.genericeditor.diff.extension:1.1.100,org.eclipse.platform:org.eclipse.team.ui:3.9.400,org.eclipse.platform:org.eclipse.text:3.12.200,org.eclipse.platform:org.eclipse.text.quicksearch:1.1.300,org.eclipse.platform:org.eclipse.ui:3.201.100,org.eclipse.platform:org.eclipse.ui.browser:3.7.200,org.eclipse.platform:org.eclipse.ui.cheatsheets:3.7.400,org.eclipse.platform:org.eclipse.ui.cocoa:1.2.400,org.eclipse.platform:org.eclipse.ui.console:3.11.300,org.eclipse.platform:org.eclipse.ui.editors:3.14.400,org.eclipse.platform:org.eclipse.ui.externaltools:3.5.200,org.eclipse.platform:org.eclipse.ui.forms:3.11.400,org.eclipse.platform:org.eclipse.ui.genericeditor:1.2.200,org.eclipse.platform:org.eclipse.ui.ide:3.19.100,org.eclipse.platform:org.eclipse.ui.ide.application:1.4.500,org.eclipse.platform:org.eclipse.ui.intro:3.6.600,org.eclipse.platform:org.eclipse.ui.intro.quicklinks:1.1.200,org.eclipse.platform:org.eclipse.ui.intro.universal:3.4.300,org.eclipse.platform:org.eclipse.ui.monitoring:1.2.200,org.eclipse.platform:org.eclipse.ui.navigator:3.10.300,org.eclipse.platform:org.eclipse.ui.navigator.resources:3.8.400,org.eclipse.platform:org.eclipse.ui.net:1.4.100,org.eclipse.platform:org.eclipse.ui.themes:1.2.2000,org.eclipse.platform:org.eclipse.ui.views:3.11.200,org.eclipse.platform:org.eclipse.ui.views.log:1.3.300,org.eclipse.platform:org.eclipse.ui.views.properties.tabbed:3.9.200,org.eclipse.platform:org.eclipse.ui.win32:3.4.400,org.eclipse.platform:org.eclipse.ui.workbench:3.126.0,org.eclipse.platform:org.eclipse.ui.workbench.texteditor:3.16.600,org.eclipse.platform:org.eclipse.update.configurator:3.4.900,org.eclipse.platform:org.eclipse.urischeme:1.2.100,org.opentest4j:opentest4j:1.2.0,org.osgi:org.osgi.service.cm:1.6.1,org.osgi:org.osgi.service.component:1.5.0,org.osgi:org.osgi.service.component.annotations:1.5.0,org.osgi:org.osgi.service.device:1.1.1,org.osgi:org.osgi.service.event:1.4.1,org.osgi:org.osgi.service.metatype:1.4.1,org.osgi:org.osgi.service.prefs:1.1.2,org.osgi:org.osgi.service.provisioning:1.2.0,org.osgi:org.osgi.service.upnp:1.2.1,org.osgi:org.osgi.service.useradmin:1.1.1,org.osgi:org.osgi.service.wireadmin:1.0.2,org.osgi:org.osgi.util.function:1.2.0,org.osgi:org.osgi.util.measurement:1.0.2,org.osgi:org.osgi.util.position:1.0.1,org.osgi:org.osgi.util.promise:1.2.0,org.osgi:org.osgi.util.xml:1.0.2,org.ow2.sat4j:org.ow2.sat4j.core:2.3.6,org.ow2.sat4j:org.ow2.sat4j.pb:2.3.6,org.tukaani:xz:1.9");
		var notOnMavenCentral =
				query.jarsNotOnMavenCentral().stream()
						.map(unit -> unit.id)
						.collect(Collectors.joining(","));
		Assertions.assertThat(notOnMavenCentral)
				.isEqualTo(
						"com.jcraft.jsch,com.sun.el,javax.annotation,javax.el,javax.inject,org.apache.ant,org.apache.batik.constants,org.apache.batik.css,org.apache.batik.i18n,org.apache.batik.util,org.apache.commons.codec,org.apache.commons.jxpath,org.apache.commons.logging,org.apache.httpcomponents.client5.httpclient5,org.apache.httpcomponents.client5.httpclient5-win,org.apache.httpcomponents.core5.httpcore5,org.apache.httpcomponents.core5.httpcore5-h2,org.apache.jasper.glassfish,org.apache.lucene.analyzers-common,org.apache.lucene.analyzers-smartcn,org.apache.lucene.core,org.apache.xmlgraphics,org.bouncycastle.bcpg,org.bouncycastle.bcprov,org.hamcrest.core,org.junit,org.slf4j.api,org.w3c.css.sac,org.w3c.dom.events,org.w3c.dom.smil,org.w3c.dom.svg");
	}

	@Test
	public void testQueryPlatformSpecific() throws Exception {
		var session = populateSession();
		var query = new P2Query();
		query.resolve(session.getUnitById("org.eclipse.swt"));

		var notOnMavenCentral =
				query.jarsNotOnMavenCentral().stream()
						.map(unit -> unit.id)
						.collect(Collectors.joining(","));
		Assertions.assertThat(notOnMavenCentral).isEmpty();

		var jarsAllPlatforms = query.jarsOnMavenCentral().stream().collect(Collectors.joining(","));
		Assertions.assertThat(jarsAllPlatforms)
				.isEqualTo(
						"org.eclipse.platform:org.eclipse.swt:3.121.0,org.eclipse.platform:org.eclipse.swt.cocoa.macosx.aarch64:3.121.0,org.eclipse.platform:org.eclipse.swt.cocoa.macosx.x86_64:3.121.0,org.eclipse.platform:org.eclipse.swt.gtk.linux.aarch64:3.121.0,org.eclipse.platform:org.eclipse.swt.gtk.linux.ppc64le:3.121.0,org.eclipse.platform:org.eclipse.swt.gtk.linux.x86_64:3.121.0,org.eclipse.platform:org.eclipse.swt.win32.win32.x86_64:3.121.0");

		query.setPlatform(SwtPlatform.parseWsOsArch("cocoa.macosx.aarch64"));
		var jarsOnMac = query.jarsOnMavenCentral().stream().collect(Collectors.joining(","));
		Assertions.assertThat(jarsOnMac)
				.isEqualTo(
						"org.eclipse.platform:org.eclipse.swt:3.121.0,org.eclipse.platform:org.eclipse.swt.cocoa.macosx.aarch64:3.121.0");
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
