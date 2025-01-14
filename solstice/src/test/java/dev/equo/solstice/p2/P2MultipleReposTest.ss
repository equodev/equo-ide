╔═ features ═╗
+---------------------------------------------------------------------------------------------------+---------------------------------------------------+
| id                                                                                                | name \n description                               |
+---------------------------------------------------------------------------------------------------+---------------------------------------------------+
| I20221123-1800.Default                                                                            | Uncategorized                                     |
|                                                                                                   |   Default category for otherwise uncategorized    |
|                                                                                                   |   features                                        |
| file:/home/tcagent1/agent/work/46ffae0028d9fdb9/org.eclipse.buildship.site/category.xml.Buildship | Buildship: Eclipse Plug-ins for Gradle            |
| org.eclipse.equinox.target.categoryIU                                                             | Equinox Target Components                         |
|                                                                                                   |   Features especially useful to install as PDE    |
|                                                                                                   |   runtime targets.                                |
| org.eclipse.platform.ide.categoryIU                                                               | Eclipse Platform                                  |
|                                                                                                   |   Minimum version of Eclipse: no source or API    |
|                                                                                                   |   documentation, no PDE or JDT.                   |
| org.eclipse.platform.sdk.categoryIU                                                               | Eclipse Platform SDK                              |
|                                                                                                   |   Minimum version of Eclipse with source and      |
|                                                                                                   |   documentation, no PDE or JDT.                   |
| org.eclipse.rcp.categoryIU                                                                        | Eclipse RCP Target Components                     |
|                                                                                                   |   Features to use as PDE runtime target, while    |
|                                                                                                   |   developing RCP applications.                    |
| org.eclipse.releng.java.languages.categoryIU                                                      | Eclipse Java Development Tools                    |
|                                                                                                   |   Tools to allow development with Java.           |
| org.eclipse.releng.pde.categoryIU                                                                 | Eclipse Plugin Development Tools                  |
|                                                                                                   |   Tools to develop bundles, plugins and features. |
| org.eclipse.releng.testsIU                                                                        | Eclipse Tests, Tools, Examples, and Extras        |
|                                                                                                   |   Collection of Misc. Features, such as unit      |
|                                                                                                   |   tests, SWT and e4 tools, examples, and          |
|                                                                                                   |   compatibility features not shipped as part of   |
|                                                                                                   |   main SDK, but which some people may desire in   |
|                                                                                                   |   creating products based on previous versions of |
|                                                                                                   |   Eclipse.                                        |
| org.eclipse.sdk.ide.categoryIU                                                                    | Eclipse SDK                                       |
|                                                                                                   |   The full version of Eclipse, with source and    |
|                                                                                                   |   documentation: Platform, JDT and PDE.           |
+---------------------------------------------------------------------------------------------------+---------------------------------------------------+

╔═ query ═╗
12 ambiguous requirement(s).
+------------------------------------------+-------------------------------------------------+-----------+
| ambiguous requirement                    | candidate                                       | installed |
+------------------------------------------+-------------------------------------------------+-----------+
| pkg com.google.gson.annotations          | com.google.gson:2.9.1                           | [x]       |
|                                          | com.google.gson:2.8.9.v20220111-1409            | [ ]       |
| pkg é.jdt.internal.compiler.apt.dispatch | é.jdt.compiler.apt:1.4.300.v20221108-0856       | [x]       |
|                                          | é.jdt.core.compiler.batch:3.32.0.v20221108-1853 | [ ]       |
| pkg é.jdt.internal.compiler.apt.model    | é.jdt.compiler.apt:1.4.300.v20221108-0856       | [x]       |
|                                          | é.jdt.core.compiler.batch:3.32.0.v20221108-1853 | [ ]       |
| pkg é.jdt.internal.compiler.apt.util     | é.jdt.compiler.apt:1.4.300.v20221108-0856       | [x]       |
|                                          | é.jdt.core.compiler.batch:3.32.0.v20221108-1853 | [ ]       |
| pkg é.jdt.internal.compiler.tool         | é.jdt.compiler.tool:1.3.200.v20220802-0458      | [x]       |
|                                          | é.jdt.core.compiler.batch:3.32.0.v20221108-1853 | [ ]       |
| pkg org.slf4j                            | org.slf4j.api:1.7.30.v20200204-2150             | [x]       |
|                                          | slf4j.api:1.7.36                                | [ ]       |
| pkg org.slf4j.event                      | org.slf4j.api:1.7.30.v20200204-2150             | [x]       |
|                                          | slf4j.api:1.7.36                                | [ ]       |
| pkg org.slf4j.helpers                    | org.slf4j.api:1.7.30.v20200204-2150             | [x]       |
|                                          | slf4j.api:1.7.36                                | [ ]       |
| pkg org.slf4j.spi                        | org.slf4j.api:1.7.30.v20200204-2150             | [x]       |
|                                          | slf4j.api:1.7.36                                | [ ]       |
| iu é.jdt.annotation                      | é.jdt.annotation:2.2.700.v20220826-1026         | [x]       |
|                                          | é.jdt.annotation:1.2.100.v20220826-1026         | [ ]       |
| iu é.jdt.annotation.source               | é.jdt.annotation.source:2.2.700.v20220826-1026  | [x]       |
|                                          | é.jdt.annotation.source:1.2.100.v20220826-1026  | [ ]       |
| bundle com.google.gson                   | com.google.gson:2.9.1                           | [x]       |
|                                          | com.google.gson:2.8.9.v20220111-1409            | [ ]       |
+------------------------------------------+-------------------------------------------------+-----------+
é org.eclipse

0 unmet requirement(s).
+---------------------------------------------------------------------------------+-------------------------+
| maven coordinate / p2 id                                                        | repo                    |
+---------------------------------------------------------------------------------+-------------------------+
| com.google.code.gson:gson:2.9.1                                                 | maven central           |
| com.ibm.icu:icu4j:72.1                                                          | maven central           |
| commons-io:commons-io:2.11.0                                                    | maven central           |
| jakarta.servlet:jakarta.servlet-api:4.0.4                                       | maven central           |
| javax.servlet.jsp:javax.servlet.jsp-api:2.3.3                                   | maven central           |
| net.java.dev.jna:jna-platform:5.12.1                                            | maven central           |
| net.java.dev.jna:jna:5.12.1                                                     | maven central           |
| org.apache.felix:org.apache.felix.gogo.command:1.1.2                            | maven central           |
| org.apache.felix:org.apache.felix.gogo.runtime:1.1.6                            | maven central           |
| org.apache.felix:org.apache.felix.gogo.shell:1.1.4                              | maven central           |
| org.apache.felix:org.apache.felix.scr:2.2.4                                     | maven central           |
| org.apiguardian:apiguardian-api:1.1.2                                           | maven central           |
| org.apiguardian:apiguardian-api:1.1.2                                           | maven central           |
| org.bouncycastle:bcpg-jdk18on:1.72                                              | maven central           |
| org.bouncycastle:bcprov-jdk18on:1.72                                            | maven central           |
| org.eclipse.jetty:jetty-http:10.0.12                                            | maven central           |
| org.eclipse.jetty:jetty-io:10.0.12                                              | maven central           |
| org.eclipse.jetty:jetty-security:10.0.12                                        | maven central           |
| org.eclipse.jetty:jetty-server:10.0.12                                          | maven central           |
| org.eclipse.jetty:jetty-servlet:10.0.12                                         | maven central           |
| org.eclipse.jetty:jetty-util-ajax:10.0.12                                       | maven central           |
| org.eclipse.jetty:jetty-util:10.0.12                                            | maven central           |
| org.junit.jupiter:junit-jupiter-api:5.9.1                                       | maven central           |
| org.junit.jupiter:junit-jupiter-api:5.9.1                                       | maven central           |
| org.junit.jupiter:junit-jupiter-engine:5.9.1                                    | maven central           |
| org.junit.jupiter:junit-jupiter-engine:5.9.1                                    | maven central           |
| org.junit.jupiter:junit-jupiter-migrationsupport:5.9.1                          | maven central           |
| org.junit.jupiter:junit-jupiter-migrationsupport:5.9.1                          | maven central           |
| org.junit.jupiter:junit-jupiter-params:5.9.1                                    | maven central           |
| org.junit.jupiter:junit-jupiter-params:5.9.1                                    | maven central           |
| org.junit.platform:junit-platform-commons:1.9.1                                 | maven central           |
| org.junit.platform:junit-platform-commons:1.9.1                                 | maven central           |
| org.junit.platform:junit-platform-engine:1.9.1                                  | maven central           |
| org.junit.platform:junit-platform-engine:1.9.1                                  | maven central           |
| org.junit.platform:junit-platform-launcher:1.9.1                                | maven central           |
| org.junit.platform:junit-platform-launcher:1.9.1                                | maven central           |
| org.junit.platform:junit-platform-runner:1.9.1                                  | maven central           |
| org.junit.platform:junit-platform-runner:1.9.1                                  | maven central           |
| org.junit.platform:junit-platform-suite-api:1.9.1                               | maven central           |
| org.junit.platform:junit-platform-suite-api:1.9.1                               | maven central           |
| org.junit.platform:junit-platform-suite-commons:1.9.1                           | maven central           |
| org.junit.platform:junit-platform-suite-commons:1.9.1                           | maven central           |
| org.junit.platform:junit-platform-suite-engine:1.9.1                            | maven central           |
| org.junit.platform:junit-platform-suite-engine:1.9.1                            | maven central           |
| org.junit.vintage:junit-vintage-engine:5.9.1                                    | maven central           |
| org.junit.vintage:junit-vintage-engine:5.9.1                                    | maven central           |
| org.opentest4j:opentest4j:1.2.0                                                 | maven central           |
| org.opentest4j:opentest4j:1.2.0                                                 | maven central           |
| org.osgi:org.osgi.service.cm:1.6.1                                              | maven central           |
| org.osgi:org.osgi.service.component:1.5.0                                       | maven central           |
| org.osgi:org.osgi.service.device:1.1.1                                          | maven central           |
| org.osgi:org.osgi.service.event:1.4.1                                           | maven central           |
| org.osgi:org.osgi.service.metatype:1.4.1                                        | maven central           |
| org.osgi:org.osgi.service.prefs:1.1.2                                           | maven central           |
| org.osgi:org.osgi.service.provisioning:1.2.0                                    | maven central           |
| org.osgi:org.osgi.service.upnp:1.2.1                                            | maven central           |
| org.osgi:org.osgi.service.useradmin:1.1.1                                       | maven central           |
| org.osgi:org.osgi.service.wireadmin:1.0.2                                       | maven central           |
| org.osgi:org.osgi.util.function:1.2.0                                           | maven central           |
| org.osgi:org.osgi.util.measurement:1.0.2                                        | maven central           |
| org.osgi:org.osgi.util.position:1.0.1                                           | maven central           |
| org.osgi:org.osgi.util.promise:1.2.0                                            | maven central           |
| org.osgi:org.osgi.util.xml:1.0.2                                                | maven central           |
| org.ow2.sat4j:org.ow2.sat4j.core:2.3.6                                          | maven central           |
| org.ow2.sat4j:org.ow2.sat4j.pb:2.3.6                                            | maven central           |
| org.tukaani:xz:1.9                                                              | maven central           |
| org.eclipse.ecf:org.eclipse.ecf.filetransfer:5.1.102                            | maven central?          |
| org.eclipse.ecf:org.eclipse.ecf.identity:3.9.402                                | maven central?          |
| org.eclipse.ecf:org.eclipse.ecf.provider.filetransfer.httpclient5:1.0.401       | maven central?          |
| org.eclipse.ecf:org.eclipse.ecf.provider.filetransfer.ssl:1.0.201               | maven central?          |
| org.eclipse.ecf:org.eclipse.ecf.provider.filetransfer:3.2.800                   | maven central?          |
| org.eclipse.ecf:org.eclipse.ecf.ssl:1.2.401                                     | maven central?          |
| org.eclipse.ecf:org.eclipse.ecf:3.10.0                                          | maven central?          |
| org.eclipse.emf:org.eclipse.emf.common:2.27.0                                   | maven central?          |
| org.eclipse.emf:org.eclipse.emf.ecore.change:2.14.0                             | maven central?          |
| org.eclipse.emf:org.eclipse.emf.ecore.xmi:2.17.0                                | maven central?          |
| org.eclipse.emf:org.eclipse.emf.ecore:2.29.0                                    | maven central?          |
| org.eclipse.jdt:org.eclipse.jdt.annotation:2.2.700                              | maven central?          |
| org.eclipse.jdt:org.eclipse.jdt.apt.core:3.7.50                                 | maven central?          |
| org.eclipse.jdt:org.eclipse.jdt.apt.pluggable.core:1.3.0                        | maven central?          |
| org.eclipse.jdt:org.eclipse.jdt.apt.ui:3.7.0                                    | maven central?          |
| org.eclipse.jdt:org.eclipse.jdt.compiler.apt:1.4.300                            | maven central?          |
| org.eclipse.jdt:org.eclipse.jdt.compiler.tool:1.3.200                           | maven central?          |
| org.eclipse.jdt:org.eclipse.jdt.core.formatterapp:1.1.0                         | maven central?          |
| org.eclipse.jdt:org.eclipse.jdt.core.manipulation:1.17.0                        | maven central?          |
| org.eclipse.jdt:org.eclipse.jdt.core:3.32.0                                     | maven central?          |
| org.eclipse.jdt:org.eclipse.jdt.debug.ui:3.12.900                               | maven central?          |
| org.eclipse.jdt:org.eclipse.jdt.debug:3.20.0                                    | maven central?          |
| org.eclipse.jdt:org.eclipse.jdt.doc.isv:3.14.1800                               | maven central?          |
| org.eclipse.jdt:org.eclipse.jdt.doc.user:3.15.1600                              | maven central?          |
| org.eclipse.jdt:org.eclipse.jdt.junit.core:3.11.500                             | maven central?          |
| org.eclipse.jdt:org.eclipse.jdt.junit.runtime:3.7.0                             | maven central?          |
| org.eclipse.jdt:org.eclipse.jdt.junit4.runtime:1.3.0                            | maven central?          |
| org.eclipse.jdt:org.eclipse.jdt.junit5.runtime:1.1.100                          | maven central?          |
| org.eclipse.jdt:org.eclipse.jdt.junit:3.15.100                                  | maven central?          |
| org.eclipse.jdt:org.eclipse.jdt.launching:3.19.800                              | maven central?          |
| org.eclipse.jdt:org.eclipse.jdt.ui:3.27.100                                     | maven central?          |
| org.eclipse.jdt:org.eclipse.jdt:3.18.1400                                       | maven central?          |
| org.eclipse.platform:org.eclipse.ant.core:3.6.500                               | maven central?          |
| org.eclipse.platform:org.eclipse.ant.launching:1.3.400                          | maven central?          |
| org.eclipse.platform:org.eclipse.ant.ui:3.8.300                                 | maven central?          |
| org.eclipse.platform:org.eclipse.compare.core:3.7.100                           | maven central?          |
| org.eclipse.platform:org.eclipse.compare:3.8.500                                | maven central?          |
| org.eclipse.platform:org.eclipse.core.commands:3.10.300                         | maven central?          |
| org.eclipse.platform:org.eclipse.core.contenttype:3.8.200                       | maven central?          |
| org.eclipse.platform:org.eclipse.core.databinding.beans:1.9.0                   | maven central?          |
| org.eclipse.platform:org.eclipse.core.databinding.observable:1.12.100           | maven central?          |
| org.eclipse.platform:org.eclipse.core.databinding.property:1.9.100              | maven central?          |
| org.eclipse.platform:org.eclipse.core.databinding:1.11.200                      | maven central?          |
| org.eclipse.platform:org.eclipse.core.expressions:3.8.200                       | maven central?          |
| org.eclipse.platform:org.eclipse.core.externaltools:1.2.300                     | maven central?          |
| org.eclipse.platform:org.eclipse.core.filebuffers:3.7.200                       | maven central?          |
| org.eclipse.platform:org.eclipse.core.filesystem:1.9.500                        | maven central?          |
| org.eclipse.platform:org.eclipse.core.jobs:3.13.200                             | maven central?          |
| org.eclipse.platform:org.eclipse.core.net:1.4.0                                 | maven central?          |
| org.eclipse.platform:org.eclipse.core.resources:3.18.100                        | maven central?          |
| org.eclipse.platform:org.eclipse.core.runtime:3.26.100                          | maven central?          |
| org.eclipse.platform:org.eclipse.core.variables:3.5.100                         | maven central?          |
| org.eclipse.platform:org.eclipse.debug.core:3.20.0                              | maven central?          |
| org.eclipse.platform:org.eclipse.debug.ui.launchview:1.0.300                    | maven central?          |
| org.eclipse.platform:org.eclipse.debug.ui:3.17.100                              | maven central?          |
| org.eclipse.platform:org.eclipse.e4.core.commands:1.0.300                       | maven central?          |
| org.eclipse.platform:org.eclipse.e4.core.contexts:1.11.0                        | maven central?          |
| org.eclipse.platform:org.eclipse.e4.core.di.annotations:1.7.200                 | maven central?          |
| org.eclipse.platform:org.eclipse.e4.core.di.extensions.supplier:0.16.400        | maven central?          |
| org.eclipse.platform:org.eclipse.e4.core.di.extensions:0.17.200                 | maven central?          |
| org.eclipse.platform:org.eclipse.e4.core.di:1.8.300                             | maven central?          |
| org.eclipse.platform:org.eclipse.e4.core.services:2.3.400                       | maven central?          |
| org.eclipse.platform:org.eclipse.e4.emf.xpath:0.3.100                           | maven central?          |
| org.eclipse.platform:org.eclipse.e4.ui.bindings:0.13.200                        | maven central?          |
| org.eclipse.platform:org.eclipse.e4.ui.css.core:0.13.400                        | maven central?          |
| org.eclipse.platform:org.eclipse.e4.ui.css.swt.theme:0.13.200                   | maven central?          |
| org.eclipse.platform:org.eclipse.e4.ui.css.swt:0.14.700                         | maven central?          |
| org.eclipse.platform:org.eclipse.e4.ui.di:1.4.100                               | maven central?          |
| org.eclipse.platform:org.eclipse.e4.ui.dialogs:1.3.400                          | maven central?          |
| org.eclipse.platform:org.eclipse.e4.ui.ide:3.16.200                             | maven central?          |
| org.eclipse.platform:org.eclipse.e4.ui.model.workbench:2.2.300                  | maven central?          |
| org.eclipse.platform:org.eclipse.e4.ui.progress:0.3.600                         | maven central?          |
| org.eclipse.platform:org.eclipse.e4.ui.services:1.5.100                         | maven central?          |
| org.eclipse.platform:org.eclipse.e4.ui.widgets:1.3.100                          | maven central?          |
| org.eclipse.platform:org.eclipse.e4.ui.workbench.addons.swt:1.4.500             | maven central?          |
| org.eclipse.platform:org.eclipse.e4.ui.workbench.renderers.swt:0.15.700         | maven central?          |
| org.eclipse.platform:org.eclipse.e4.ui.workbench.swt:0.16.700                   | maven central?          |
| org.eclipse.platform:org.eclipse.e4.ui.workbench3:0.16.100                      | maven central?          |
| org.eclipse.platform:org.eclipse.e4.ui.workbench:1.14.0                         | maven central?          |
| org.eclipse.platform:org.eclipse.equinox.app:1.6.200                            | maven central?          |
| org.eclipse.platform:org.eclipse.equinox.bidi:1.4.200                           | maven central?          |
| org.eclipse.platform:org.eclipse.equinox.common:3.17.0                          | maven central?          |
| org.eclipse.platform:org.eclipse.equinox.concurrent:1.2.100                     | maven central?          |
| org.eclipse.platform:org.eclipse.equinox.console:1.4.500                        | maven central?          |
| org.eclipse.platform:org.eclipse.equinox.event:1.6.100                          | maven central?          |
| org.eclipse.platform:org.eclipse.equinox.frameworkadmin.equinox:1.2.200         | maven central?          |
| org.eclipse.platform:org.eclipse.equinox.frameworkadmin:2.2.100                 | maven central?          |
| org.eclipse.platform:org.eclipse.equinox.http.jetty:3.8.200                     | maven central?          |
| org.eclipse.platform:org.eclipse.equinox.http.registry:1.3.200                  | maven central?          |
| org.eclipse.platform:org.eclipse.equinox.http.servlet:1.7.400                   | maven central?          |
| org.eclipse.platform:org.eclipse.equinox.jsp.jasper.registry:1.2.100            | maven central?          |
| org.eclipse.platform:org.eclipse.equinox.jsp.jasper:1.1.700                     | maven central?          |
| org.eclipse.platform:org.eclipse.equinox.launcher:1.6.400                       | maven central?          |
| org.eclipse.platform:org.eclipse.equinox.p2.artifact.repository:1.4.600         | maven central?          |
| org.eclipse.platform:org.eclipse.equinox.p2.console:1.2.100                     | maven central?          |
| org.eclipse.platform:org.eclipse.equinox.p2.core:2.9.200                        | maven central?          |
| org.eclipse.platform:org.eclipse.equinox.p2.director.app:1.2.300                | maven central?          |
| org.eclipse.platform:org.eclipse.equinox.p2.director:2.5.400                    | maven central?          |
| org.eclipse.platform:org.eclipse.equinox.p2.directorywatcher:1.3.100            | maven central?          |
| org.eclipse.platform:org.eclipse.equinox.p2.engine:2.7.500                      | maven central?          |
| org.eclipse.platform:org.eclipse.equinox.p2.extensionlocation:1.4.100           | maven central?          |
| org.eclipse.platform:org.eclipse.equinox.p2.garbagecollector:1.2.100            | maven central?          |
| org.eclipse.platform:org.eclipse.equinox.p2.jarprocessor:1.2.300                | maven central?          |
| org.eclipse.platform:org.eclipse.equinox.p2.metadata.repository:1.4.100         | maven central?          |
| org.eclipse.platform:org.eclipse.equinox.p2.metadata:2.6.300                    | maven central?          |
| org.eclipse.platform:org.eclipse.equinox.p2.operations:2.6.100                  | maven central?          |
| org.eclipse.platform:org.eclipse.equinox.p2.publisher.eclipse:1.4.200           | maven central?          |
| org.eclipse.platform:org.eclipse.equinox.p2.publisher:1.7.200                   | maven central?          |
| org.eclipse.platform:org.eclipse.equinox.p2.reconciler.dropins:1.4.200          | maven central?          |
| org.eclipse.platform:org.eclipse.equinox.p2.repository.tools:2.3.200            | maven central?          |
| org.eclipse.platform:org.eclipse.equinox.p2.repository:2.6.300                  | maven central?          |
| org.eclipse.platform:org.eclipse.equinox.p2.touchpoint.eclipse:2.3.300          | maven central?          |
| org.eclipse.platform:org.eclipse.equinox.p2.touchpoint.natives:1.4.400          | maven central?          |
| org.eclipse.platform:org.eclipse.equinox.p2.transport.ecf:1.3.300               | maven central?          |
| org.eclipse.platform:org.eclipse.equinox.p2.ui.importexport:1.3.300             | maven central?          |
| org.eclipse.platform:org.eclipse.equinox.p2.ui.sdk.scheduler:1.5.400            | maven central?          |
| org.eclipse.platform:org.eclipse.equinox.p2.ui.sdk:1.2.100                      | maven central?          |
| org.eclipse.platform:org.eclipse.equinox.p2.ui:2.7.700                          | maven central?          |
| org.eclipse.platform:org.eclipse.equinox.p2.updatechecker:1.3.100               | maven central?          |
| org.eclipse.platform:org.eclipse.equinox.p2.updatesite:1.2.300                  | maven central?          |
| org.eclipse.platform:org.eclipse.equinox.preferences:3.10.100                   | maven central?          |
| org.eclipse.platform:org.eclipse.equinox.registry:3.11.200                      | maven central?          |
| org.eclipse.platform:org.eclipse.equinox.security.ui:1.3.400                    | maven central?          |
| org.eclipse.platform:org.eclipse.equinox.security:1.3.1000                      | maven central?          |
| org.eclipse.platform:org.eclipse.equinox.simpleconfigurator.manipulator:2.2.100 | maven central?          |
| org.eclipse.platform:org.eclipse.equinox.simpleconfigurator:1.4.200             | maven central?          |
| org.eclipse.platform:org.eclipse.equinox.supplement:1.10.600                    | maven central?          |
| org.eclipse.platform:org.eclipse.help.base:4.3.900                              | maven central?          |
| org.eclipse.platform:org.eclipse.help.ui:4.4.100                                | maven central?          |
| org.eclipse.platform:org.eclipse.help.webapp:3.10.900                           | maven central?          |
| org.eclipse.platform:org.eclipse.help:3.9.100                                   | maven central?          |
| org.eclipse.platform:org.eclipse.jface.databinding:1.14.0                       | maven central?          |
| org.eclipse.platform:org.eclipse.jface.notifications:0.5.100                    | maven central?          |
| org.eclipse.platform:org.eclipse.jface.text:3.22.0                              | maven central?          |
| org.eclipse.platform:org.eclipse.jface:3.28.0                                   | maven central?          |
| org.eclipse.platform:org.eclipse.jsch.core:1.4.0                                | maven central?          |
| org.eclipse.platform:org.eclipse.jsch.ui:1.4.200                                | maven central?          |
| org.eclipse.platform:org.eclipse.ltk.core.refactoring:3.13.0                    | maven central?          |
| org.eclipse.platform:org.eclipse.ltk.ui.refactoring:3.12.200                    | maven central?          |
| org.eclipse.platform:org.eclipse.osgi.compatibility.state:1.2.800               | maven central?          |
| org.eclipse.platform:org.eclipse.osgi.services:3.11.100                         | maven central?          |
| org.eclipse.platform:org.eclipse.osgi.util:3.7.100                              | maven central?          |
| org.eclipse.platform:org.eclipse.osgi:3.18.200                                  | maven central?          |
| org.eclipse.platform:org.eclipse.platform.doc.user:4.26.0                       | maven central?          |
| org.eclipse.platform:org.eclipse.platform:4.26.0                                | maven central?          |
| org.eclipse.platform:org.eclipse.rcp:4.26.0                                     | maven central?          |
| org.eclipse.platform:org.eclipse.search:3.14.300                                | maven central?          |
| org.eclipse.platform:org.eclipse.swt:3.122.0                                    | maven central?          |
| org.eclipse.platform:org.eclipse.team.core:3.9.600                              | maven central?          |
| org.eclipse.platform:org.eclipse.team.genericeditor.diff.extension:1.1.100      | maven central?          |
| org.eclipse.platform:org.eclipse.team.ui:3.9.500                                | maven central?          |
| org.eclipse.platform:org.eclipse.text.quicksearch:1.1.400                       | maven central?          |
| org.eclipse.platform:org.eclipse.text:3.12.300                                  | maven central?          |
| org.eclipse.platform:org.eclipse.ui.browser:3.7.300                             | maven central?          |
| org.eclipse.platform:org.eclipse.ui.cheatsheets:3.7.500                         | maven central?          |
| org.eclipse.platform:org.eclipse.ui.console:3.11.400                            | maven central?          |
| org.eclipse.platform:org.eclipse.ui.editors:3.14.400                            | maven central?          |
| org.eclipse.platform:org.eclipse.ui.externaltools:3.5.200                       | maven central?          |
| org.eclipse.platform:org.eclipse.ui.forms:3.11.500                              | maven central?          |
| org.eclipse.platform:org.eclipse.ui.genericeditor:1.2.300                       | maven central?          |
| org.eclipse.platform:org.eclipse.ui.ide.application:1.4.600                     | maven central?          |
| org.eclipse.platform:org.eclipse.ui.ide:3.20.0                                  | maven central?          |
| org.eclipse.platform:org.eclipse.ui.intro.quicklinks:1.1.200                    | maven central?          |
| org.eclipse.platform:org.eclipse.ui.intro.universal:3.4.300                     | maven central?          |
| org.eclipse.platform:org.eclipse.ui.intro:3.6.600                               | maven central?          |
| org.eclipse.platform:org.eclipse.ui.monitoring:1.2.300                          | maven central?          |
| org.eclipse.platform:org.eclipse.ui.navigator.resources:3.8.500                 | maven central?          |
| org.eclipse.platform:org.eclipse.ui.navigator:3.10.400                          | maven central?          |
| org.eclipse.platform:org.eclipse.ui.net:1.4.100                                 | maven central?          |
| org.eclipse.platform:org.eclipse.ui.themes:1.2.2100                             | maven central?          |
| org.eclipse.platform:org.eclipse.ui.views.log:1.3.400                           | maven central?          |
| org.eclipse.platform:org.eclipse.ui.views.properties.tabbed:3.9.300             | maven central?          |
| org.eclipse.platform:org.eclipse.ui.views:3.11.300                              | maven central?          |
| org.eclipse.platform:org.eclipse.ui.workbench.texteditor:3.16.600               | maven central?          |
| org.eclipse.platform:org.eclipse.ui.workbench:3.127.0                           | maven central?          |
| org.eclipse.platform:org.eclipse.ui:3.201.200                                   | maven central?          |
| org.eclipse.platform:org.eclipse.update.configurator:3.4.1000                   | maven central?          |
| org.eclipse.platform:org.eclipse.urischeme:1.2.200                              | maven central?          |
| com.google.guava:30.1.0.v20210127-2300                                          | p2 3.1.6.v20220511-1359 |
| org.eclipse.buildship.branding:3.1.6.v20220511-1359                             | p2 3.1.6.v20220511-1359 |
| org.eclipse.buildship.compat:3.1.6.v20220511-1359                               | p2 3.1.6.v20220511-1359 |
| org.eclipse.buildship.core:3.1.6.v20220511-1359                                 | p2 3.1.6.v20220511-1359 |
| org.eclipse.buildship.ui:3.1.6.v20220511-1359                                   | p2 3.1.6.v20220511-1359 |
| org.gradle.toolingapi:7.4.2.v20220510-1312-s                                    | p2 3.1.6.v20220511-1359 |
| org.slf4j.api:1.7.30.v20200204-2150                                             | p2 3.1.6.v20220511-1359 |
| com.jcraft.jsch:0.1.55.v20221112-0806                                           | p2 R-4.26-202211231800  |
| com.sun.el:2.2.0.v201303151357                                                  | p2 R-4.26-202211231800  |
| javax.annotation:1.3.5.v20221112-0806                                           | p2 R-4.26-202211231800  |
| javax.el:2.2.0.v201303151357                                                    | p2 R-4.26-202211231800  |
| javax.inject:1.0.0.v20220405-0441                                               | p2 R-4.26-202211231800  |
| org.apache.ant:1.10.12.v20211102-1452                                           | p2 R-4.26-202211231800  |
| org.apache.batik.constants:1.16.0.v20221027-0840                                | p2 R-4.26-202211231800  |
| org.apache.batik.css:1.16.0.v20221027-0840                                      | p2 R-4.26-202211231800  |
| org.apache.batik.i18n:1.16.0.v20221027-0840                                     | p2 R-4.26-202211231800  |
| org.apache.batik.util:1.16.0.v20221027-0840                                     | p2 R-4.26-202211231800  |
| org.apache.commons.codec:1.14.0.v20221112-0806                                  | p2 R-4.26-202211231800  |
| org.apache.commons.jxpath:1.3.0.v200911051830                                   | p2 R-4.26-202211231800  |
| org.apache.commons.logging:1.2.0.v20180409-1502                                 | p2 R-4.26-202211231800  |
| org.apache.httpcomponents.client5.httpclient5:5.1.3.v20221013-1742              | p2 R-4.26-202211231800  |
| org.apache.httpcomponents.core5.httpcore5-h2:5.1.4.v20221013-1742               | p2 R-4.26-202211231800  |
| org.apache.httpcomponents.core5.httpcore5:5.1.4.v20221013-1742                  | p2 R-4.26-202211231800  |
| org.apache.jasper.glassfish:2.2.2.v201501141630                                 | p2 R-4.26-202211231800  |
| org.apache.lucene.analyzers-common:8.4.1.v20221112-0806                         | p2 R-4.26-202211231800  |
| org.apache.lucene.analyzers-smartcn:8.4.1.v20221112-0806                        | p2 R-4.26-202211231800  |
| org.apache.lucene.core:8.4.1.v20221112-0806                                     | p2 R-4.26-202211231800  |
| org.apache.xmlgraphics:2.7.0.v20221018-0736                                     | p2 R-4.26-202211231800  |
| org.eclipse.ant.launching.source:1.3.400.v20220718-1722                         | p2 R-4.26-202211231800  |
| org.eclipse.ant.ui.source:3.8.300.v20220718-1722                                | p2 R-4.26-202211231800  |
| org.eclipse.jdt.annotation.source:2.2.700.v20220826-1026                        | p2 R-4.26-202211231800  |
| org.eclipse.jdt.apt.core.source:3.7.50.v20210914-1429                           | p2 R-4.26-202211231800  |
| org.eclipse.jdt.apt.pluggable.core.source:1.3.0.v20210618-1653                  | p2 R-4.26-202211231800  |
| org.eclipse.jdt.apt.ui.source:3.7.0.v20210620-1751                              | p2 R-4.26-202211231800  |
| org.eclipse.jdt.compiler.apt.source:1.4.300.v20221108-0856                      | p2 R-4.26-202211231800  |
| org.eclipse.jdt.compiler.tool.source:1.3.200.v20220802-0458                     | p2 R-4.26-202211231800  |
| org.eclipse.jdt.core.formatterapp.source:1.1.0.v20210618-1653                   | p2 R-4.26-202211231800  |
| org.eclipse.jdt.core.manipulation.source:1.17.0.v20221026-1918                  | p2 R-4.26-202211231800  |
| org.eclipse.jdt.core.source:3.32.0.v20221108-1853                               | p2 R-4.26-202211231800  |
| org.eclipse.jdt.debug.source:3.20.0.v20220922-0905                              | p2 R-4.26-202211231800  |
| org.eclipse.jdt.debug.ui.source:3.12.900.v20221001-0715                         | p2 R-4.26-202211231800  |
| org.eclipse.jdt.junit.core.source:3.11.500.v20221031-1935                       | p2 R-4.26-202211231800  |
| org.eclipse.jdt.junit.runtime.source:3.7.0.v20220609-1843                       | p2 R-4.26-202211231800  |
| org.eclipse.jdt.junit.source:3.15.100.v20220909-2154                            | p2 R-4.26-202211231800  |
| org.eclipse.jdt.junit4.runtime.source:1.3.0.v20220609-1843                      | p2 R-4.26-202211231800  |
| org.eclipse.jdt.junit5.runtime.source:1.1.100.v20220907-0450                    | p2 R-4.26-202211231800  |
| org.eclipse.jdt.launching.source:3.19.800.v20221107-1851                        | p2 R-4.26-202211231800  |
| org.eclipse.jdt.ui.source:3.27.100.v20221122-0749                               | p2 R-4.26-202211231800  |
| org.hamcrest.core.source:1.3.0.v20180420-1519                                   | p2 R-4.26-202211231800  |
| org.hamcrest.core:1.3.0.v20180420-1519                                          | p2 R-4.26-202211231800  |
| org.junit.source:4.13.2.v20211018-1956                                          | p2 R-4.26-202211231800  |
| org.junit:4.13.2.v20211018-1956                                                 | p2 R-4.26-202211231800  |
| org.w3c.css.sac:1.3.1.v200903091627                                             | p2 R-4.26-202211231800  |
| org.w3c.dom.events:3.0.0.draft20060413_v201105210656                            | p2 R-4.26-202211231800  |
| org.w3c.dom.smil:1.0.1.v200903091627                                            | p2 R-4.26-202211231800  |
| org.w3c.dom.svg:1.1.0.v201011041433                                             | p2 R-4.26-202211231800  |
+---------------------------------------------------------------------------------+-------------------------+
╔═ [end of file] ═╗
