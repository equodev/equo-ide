╔═ _01_minimal_allCategories ═╗
+----------------------------------------------+---------------------------------------------------+
| id                                           | name \n description                               |
+----------------------------------------------+---------------------------------------------------+
| I20221123-1800.Default                       | Uncategorized                                     |
|                                              |   Default category for otherwise uncategorized    |
|                                              |   features                                        |
| org.eclipse.equinox.target.categoryIU        | Equinox Target Components                         |
|                                              |   Features especially useful to install as PDE    |
|                                              |   runtime targets.                                |
| org.eclipse.platform.ide.categoryIU          | Eclipse Platform                                  |
|                                              |   Minimum version of Eclipse: no source or API    |
|                                              |   documentation, no PDE or JDT.                   |
| org.eclipse.platform.sdk.categoryIU          | Eclipse Platform SDK                              |
|                                              |   Minimum version of Eclipse with source and      |
|                                              |   documentation, no PDE or JDT.                   |
| org.eclipse.rcp.categoryIU                   | Eclipse RCP Target Components                     |
|                                              |   Features to use as PDE runtime target, while    |
|                                              |   developing RCP applications.                    |
| org.eclipse.releng.java.languages.categoryIU | Eclipse Java Development Tools                    |
|                                              |   Tools to allow development with Java.           |
| org.eclipse.releng.pde.categoryIU            | Eclipse Plugin Development Tools                  |
|                                              |   Tools to develop bundles, plugins and features. |
| org.eclipse.releng.testsIU                   | Eclipse Tests, Tools, Examples, and Extras        |
|                                              |   Collection of Misc. Features, such as unit      |
|                                              |   tests, SWT and e4 tools, examples, and          |
|                                              |   compatibility features not shipped as part of   |
|                                              |   main SDK, but which some people may desire in   |
|                                              |   creating products based on previous versions of |
|                                              |   Eclipse.                                        |
| org.eclipse.sdk.ide.categoryIU               | Eclipse SDK                                       |
|                                              |   The full version of Eclipse, with source and    |
|                                              |   documentation: Platform, JDT and PDE.           |
+----------------------------------------------+---------------------------------------------------+
╔═ _02_minimal_installed ═╗
0 unmet requirement(s), 0 ambigous requirement(s). For more info: `gradlew equoList --problems`
60 optional requirement(s) were not installed. For more info: `gradlew equoList --optional`
+---------------------------------------------------------------------------------+------------------------+
| maven coordinate / p2 id                                                        | repo                   |
+---------------------------------------------------------------------------------+------------------------+
| com.ibm.icu:icu4j:72.1                                                          | maven central          |
| commons-io:commons-io:2.11.0                                                    | maven central          |
| jakarta.servlet:jakarta.servlet-api:4.0.4                                       | maven central          |
| javax.servlet.jsp:javax.servlet.jsp-api:2.3.3                                   | maven central          |
| net.java.dev.jna:jna-platform:5.12.1                                            | maven central          |
| net.java.dev.jna:jna:5.12.1                                                     | maven central          |
| org.apache.felix:org.apache.felix.gogo.command:1.1.2                            | maven central          |
| org.apache.felix:org.apache.felix.gogo.runtime:1.1.6                            | maven central          |
| org.apache.felix:org.apache.felix.gogo.shell:1.1.4                              | maven central          |
| org.apache.felix:org.apache.felix.scr:2.2.4                                     | maven central          |
| org.bouncycastle:bcpg-jdk18on:1.72                                              | maven central          |
| org.bouncycastle:bcprov-jdk18on:1.72                                            | maven central          |
| org.eclipse.jetty:jetty-http:10.0.12                                            | maven central          |
| org.eclipse.jetty:jetty-io:10.0.12                                              | maven central          |
| org.eclipse.jetty:jetty-security:10.0.12                                        | maven central          |
| org.eclipse.jetty:jetty-server:10.0.12                                          | maven central          |
| org.eclipse.jetty:jetty-servlet:10.0.12                                         | maven central          |
| org.eclipse.jetty:jetty-util-ajax:10.0.12                                       | maven central          |
| org.eclipse.jetty:jetty-util:10.0.12                                            | maven central          |
| org.osgi:org.osgi.service.cm:1.6.1                                              | maven central          |
| org.osgi:org.osgi.service.component:1.5.0                                       | maven central          |
| org.osgi:org.osgi.service.device:1.1.1                                          | maven central          |
| org.osgi:org.osgi.service.event:1.4.1                                           | maven central          |
| org.osgi:org.osgi.service.metatype:1.4.1                                        | maven central          |
| org.osgi:org.osgi.service.prefs:1.1.2                                           | maven central          |
| org.osgi:org.osgi.service.provisioning:1.2.0                                    | maven central          |
| org.osgi:org.osgi.service.upnp:1.2.1                                            | maven central          |
| org.osgi:org.osgi.service.useradmin:1.1.1                                       | maven central          |
| org.osgi:org.osgi.service.wireadmin:1.0.2                                       | maven central          |
| org.osgi:org.osgi.util.function:1.2.0                                           | maven central          |
| org.osgi:org.osgi.util.measurement:1.0.2                                        | maven central          |
| org.osgi:org.osgi.util.position:1.0.1                                           | maven central          |
| org.osgi:org.osgi.util.promise:1.2.0                                            | maven central          |
| org.osgi:org.osgi.util.xml:1.0.2                                                | maven central          |
| org.ow2.sat4j:org.ow2.sat4j.core:2.3.6                                          | maven central          |
| org.ow2.sat4j:org.ow2.sat4j.pb:2.3.6                                            | maven central          |
| org.slf4j:slf4j-api:1.7.36                                                      | maven central          |
| org.slf4j:slf4j-nop:1.7.36                                                      | maven central          |
| org.tukaani:xz:1.9                                                              | maven central          |
| org.eclipse.ecf:org.eclipse.ecf.filetransfer:5.1.102                            | maven central?         |
| org.eclipse.ecf:org.eclipse.ecf.identity:3.9.402                                | maven central?         |
| org.eclipse.ecf:org.eclipse.ecf.provider.filetransfer.httpclient5:1.0.401       | maven central?         |
| org.eclipse.ecf:org.eclipse.ecf.provider.filetransfer.ssl:1.0.201               | maven central?         |
| org.eclipse.ecf:org.eclipse.ecf.provider.filetransfer:3.2.800                   | maven central?         |
| org.eclipse.ecf:org.eclipse.ecf.ssl:1.2.401                                     | maven central?         |
| org.eclipse.ecf:org.eclipse.ecf:3.10.0                                          | maven central?         |
| org.eclipse.emf:org.eclipse.emf.common:2.27.0                                   | maven central?         |
| org.eclipse.emf:org.eclipse.emf.ecore.change:2.14.0                             | maven central?         |
| org.eclipse.emf:org.eclipse.emf.ecore.xmi:2.17.0                                | maven central?         |
| org.eclipse.emf:org.eclipse.emf.ecore:2.29.0                                    | maven central?         |
| org.eclipse.jdt:org.eclipse.jdt.core:3.32.0                                     | maven central?         |
| org.eclipse.platform:org.eclipse.ant.core:3.6.500                               | maven central?         |
| org.eclipse.platform:org.eclipse.compare.core:3.7.100                           | maven central?         |
| org.eclipse.platform:org.eclipse.compare:3.8.500                                | maven central?         |
| org.eclipse.platform:org.eclipse.core.commands:3.10.300                         | maven central?         |
| org.eclipse.platform:org.eclipse.core.contenttype:3.8.200                       | maven central?         |
| org.eclipse.platform:org.eclipse.core.databinding.beans:1.9.0                   | maven central?         |
| org.eclipse.platform:org.eclipse.core.databinding.observable:1.12.100           | maven central?         |
| org.eclipse.platform:org.eclipse.core.databinding.property:1.9.100              | maven central?         |
| org.eclipse.platform:org.eclipse.core.databinding:1.11.200                      | maven central?         |
| org.eclipse.platform:org.eclipse.core.expressions:3.8.200                       | maven central?         |
| org.eclipse.platform:org.eclipse.core.externaltools:1.2.300                     | maven central?         |
| org.eclipse.platform:org.eclipse.core.filebuffers:3.7.200                       | maven central?         |
| org.eclipse.platform:org.eclipse.core.filesystem:1.9.500                        | maven central?         |
| org.eclipse.platform:org.eclipse.core.jobs:3.13.200                             | maven central?         |
| org.eclipse.platform:org.eclipse.core.net:1.4.0                                 | maven central?         |
| org.eclipse.platform:org.eclipse.core.resources:3.18.100                        | maven central?         |
| org.eclipse.platform:org.eclipse.core.runtime:3.26.100                          | maven central?         |
| org.eclipse.platform:org.eclipse.core.variables:3.5.100                         | maven central?         |
| org.eclipse.platform:org.eclipse.debug.core:3.20.0                              | maven central?         |
| org.eclipse.platform:org.eclipse.debug.ui.launchview:1.0.300                    | maven central?         |
| org.eclipse.platform:org.eclipse.debug.ui:3.17.100                              | maven central?         |
| org.eclipse.platform:org.eclipse.e4.core.commands:1.0.300                       | maven central?         |
| org.eclipse.platform:org.eclipse.e4.core.contexts:1.11.0                        | maven central?         |
| org.eclipse.platform:org.eclipse.e4.core.di.annotations:1.7.200                 | maven central?         |
| org.eclipse.platform:org.eclipse.e4.core.di.extensions.supplier:0.16.400        | maven central?         |
| org.eclipse.platform:org.eclipse.e4.core.di.extensions:0.17.200                 | maven central?         |
| org.eclipse.platform:org.eclipse.e4.core.di:1.8.300                             | maven central?         |
| org.eclipse.platform:org.eclipse.e4.core.services:2.3.400                       | maven central?         |
| org.eclipse.platform:org.eclipse.e4.emf.xpath:0.3.100                           | maven central?         |
| org.eclipse.platform:org.eclipse.e4.ui.bindings:0.13.200                        | maven central?         |
| org.eclipse.platform:org.eclipse.e4.ui.css.core:0.13.400                        | maven central?         |
| org.eclipse.platform:org.eclipse.e4.ui.css.swt.theme:0.13.200                   | maven central?         |
| org.eclipse.platform:org.eclipse.e4.ui.css.swt:0.14.700                         | maven central?         |
| org.eclipse.platform:org.eclipse.e4.ui.di:1.4.100                               | maven central?         |
| org.eclipse.platform:org.eclipse.e4.ui.dialogs:1.3.400                          | maven central?         |
| org.eclipse.platform:org.eclipse.e4.ui.ide:3.16.200                             | maven central?         |
| org.eclipse.platform:org.eclipse.e4.ui.model.workbench:2.2.300                  | maven central?         |
| org.eclipse.platform:org.eclipse.e4.ui.progress:0.3.600                         | maven central?         |
| org.eclipse.platform:org.eclipse.e4.ui.services:1.5.100                         | maven central?         |
| org.eclipse.platform:org.eclipse.e4.ui.widgets:1.3.100                          | maven central?         |
| org.eclipse.platform:org.eclipse.e4.ui.workbench.addons.swt:1.4.500             | maven central?         |
| org.eclipse.platform:org.eclipse.e4.ui.workbench.renderers.swt:0.15.700         | maven central?         |
| org.eclipse.platform:org.eclipse.e4.ui.workbench.swt:0.16.700                   | maven central?         |
| org.eclipse.platform:org.eclipse.e4.ui.workbench3:0.16.100                      | maven central?         |
| org.eclipse.platform:org.eclipse.e4.ui.workbench:1.14.0                         | maven central?         |
| org.eclipse.platform:org.eclipse.equinox.app:1.6.200                            | maven central?         |
| org.eclipse.platform:org.eclipse.equinox.bidi:1.4.200                           | maven central?         |
| org.eclipse.platform:org.eclipse.equinox.common:3.17.0                          | maven central?         |
| org.eclipse.platform:org.eclipse.equinox.concurrent:1.2.100                     | maven central?         |
| org.eclipse.platform:org.eclipse.equinox.console:1.4.500                        | maven central?         |
| org.eclipse.platform:org.eclipse.equinox.event:1.6.100                          | maven central?         |
| org.eclipse.platform:org.eclipse.equinox.frameworkadmin.equinox:1.2.200         | maven central?         |
| org.eclipse.platform:org.eclipse.equinox.frameworkadmin:2.2.100                 | maven central?         |
| org.eclipse.platform:org.eclipse.equinox.http.jetty:3.8.200                     | maven central?         |
| org.eclipse.platform:org.eclipse.equinox.http.registry:1.3.200                  | maven central?         |
| org.eclipse.platform:org.eclipse.equinox.http.servlet:1.7.400                   | maven central?         |
| org.eclipse.platform:org.eclipse.equinox.jsp.jasper.registry:1.2.100            | maven central?         |
| org.eclipse.platform:org.eclipse.equinox.jsp.jasper:1.1.700                     | maven central?         |
| org.eclipse.platform:org.eclipse.equinox.launcher:1.6.400                       | maven central?         |
| org.eclipse.platform:org.eclipse.equinox.p2.artifact.repository:1.4.600         | maven central?         |
| org.eclipse.platform:org.eclipse.equinox.p2.console:1.2.100                     | maven central?         |
| org.eclipse.platform:org.eclipse.equinox.p2.core:2.9.200                        | maven central?         |
| org.eclipse.platform:org.eclipse.equinox.p2.director.app:1.2.300                | maven central?         |
| org.eclipse.platform:org.eclipse.equinox.p2.director:2.5.400                    | maven central?         |
| org.eclipse.platform:org.eclipse.equinox.p2.directorywatcher:1.3.100            | maven central?         |
| org.eclipse.platform:org.eclipse.equinox.p2.engine:2.7.500                      | maven central?         |
| org.eclipse.platform:org.eclipse.equinox.p2.extensionlocation:1.4.100           | maven central?         |
| org.eclipse.platform:org.eclipse.equinox.p2.garbagecollector:1.2.100            | maven central?         |
| org.eclipse.platform:org.eclipse.equinox.p2.jarprocessor:1.2.300                | maven central?         |
| org.eclipse.platform:org.eclipse.equinox.p2.metadata.repository:1.4.100         | maven central?         |
| org.eclipse.platform:org.eclipse.equinox.p2.metadata:2.6.300                    | maven central?         |
| org.eclipse.platform:org.eclipse.equinox.p2.operations:2.6.100                  | maven central?         |
| org.eclipse.platform:org.eclipse.equinox.p2.publisher.eclipse:1.4.200           | maven central?         |
| org.eclipse.platform:org.eclipse.equinox.p2.publisher:1.7.200                   | maven central?         |
| org.eclipse.platform:org.eclipse.equinox.p2.reconciler.dropins:1.4.200          | maven central?         |
| org.eclipse.platform:org.eclipse.equinox.p2.repository.tools:2.3.200            | maven central?         |
| org.eclipse.platform:org.eclipse.equinox.p2.repository:2.6.300                  | maven central?         |
| org.eclipse.platform:org.eclipse.equinox.p2.touchpoint.eclipse:2.3.300          | maven central?         |
| org.eclipse.platform:org.eclipse.equinox.p2.touchpoint.natives:1.4.400          | maven central?         |
| org.eclipse.platform:org.eclipse.equinox.p2.transport.ecf:1.3.300               | maven central?         |
| org.eclipse.platform:org.eclipse.equinox.p2.ui.importexport:1.3.300             | maven central?         |
| org.eclipse.platform:org.eclipse.equinox.p2.ui.sdk.scheduler:1.5.400            | maven central?         |
| org.eclipse.platform:org.eclipse.equinox.p2.ui.sdk:1.2.100                      | maven central?         |
| org.eclipse.platform:org.eclipse.equinox.p2.ui:2.7.700                          | maven central?         |
| org.eclipse.platform:org.eclipse.equinox.p2.updatechecker:1.3.100               | maven central?         |
| org.eclipse.platform:org.eclipse.equinox.p2.updatesite:1.2.300                  | maven central?         |
| org.eclipse.platform:org.eclipse.equinox.preferences:3.10.100                   | maven central?         |
| org.eclipse.platform:org.eclipse.equinox.registry:3.11.200                      | maven central?         |
| org.eclipse.platform:org.eclipse.equinox.security.ui:1.3.400                    | maven central?         |
| org.eclipse.platform:org.eclipse.equinox.security:1.3.1000                      | maven central?         |
| org.eclipse.platform:org.eclipse.equinox.simpleconfigurator.manipulator:2.2.100 | maven central?         |
| org.eclipse.platform:org.eclipse.equinox.simpleconfigurator:1.4.200             | maven central?         |
| org.eclipse.platform:org.eclipse.equinox.supplement:1.10.600                    | maven central?         |
| org.eclipse.platform:org.eclipse.help.base:4.3.900                              | maven central?         |
| org.eclipse.platform:org.eclipse.help.ui:4.4.100                                | maven central?         |
| org.eclipse.platform:org.eclipse.help.webapp:3.10.900                           | maven central?         |
| org.eclipse.platform:org.eclipse.help:3.9.100                                   | maven central?         |
| org.eclipse.platform:org.eclipse.jface.databinding:1.14.0                       | maven central?         |
| org.eclipse.platform:org.eclipse.jface.notifications:0.5.100                    | maven central?         |
| org.eclipse.platform:org.eclipse.jface.text:3.22.0                              | maven central?         |
| org.eclipse.platform:org.eclipse.jface:3.28.0                                   | maven central?         |
| org.eclipse.platform:org.eclipse.jsch.core:1.4.0                                | maven central?         |
| org.eclipse.platform:org.eclipse.jsch.ui:1.4.200                                | maven central?         |
| org.eclipse.platform:org.eclipse.ltk.core.refactoring:3.13.0                    | maven central?         |
| org.eclipse.platform:org.eclipse.ltk.ui.refactoring:3.12.200                    | maven central?         |
| org.eclipse.platform:org.eclipse.osgi.compatibility.state:1.2.800               | maven central?         |
| org.eclipse.platform:org.eclipse.osgi.services:3.11.100                         | maven central?         |
| org.eclipse.platform:org.eclipse.osgi.util:3.7.100                              | maven central?         |
| org.eclipse.platform:org.eclipse.osgi:3.18.200                                  | maven central?         |
| org.eclipse.platform:org.eclipse.platform.doc.user:4.26.0                       | maven central?         |
| org.eclipse.platform:org.eclipse.platform:4.26.0                                | maven central?         |
| org.eclipse.platform:org.eclipse.rcp:4.26.0                                     | maven central?         |
| org.eclipse.platform:org.eclipse.search:3.14.300                                | maven central?         |
| org.eclipse.platform:org.eclipse.swt:3.122.0                                    | maven central?         |
| org.eclipse.platform:org.eclipse.team.core:3.9.600                              | maven central?         |
| org.eclipse.platform:org.eclipse.team.genericeditor.diff.extension:1.1.100      | maven central?         |
| org.eclipse.platform:org.eclipse.team.ui:3.9.500                                | maven central?         |
| org.eclipse.platform:org.eclipse.text.quicksearch:1.1.400                       | maven central?         |
| org.eclipse.platform:org.eclipse.text:3.12.300                                  | maven central?         |
| org.eclipse.platform:org.eclipse.ui.browser:3.7.300                             | maven central?         |
| org.eclipse.platform:org.eclipse.ui.cheatsheets:3.7.500                         | maven central?         |
| org.eclipse.platform:org.eclipse.ui.console:3.11.400                            | maven central?         |
| org.eclipse.platform:org.eclipse.ui.editors:3.14.400                            | maven central?         |
| org.eclipse.platform:org.eclipse.ui.externaltools:3.5.200                       | maven central?         |
| org.eclipse.platform:org.eclipse.ui.forms:3.11.500                              | maven central?         |
| org.eclipse.platform:org.eclipse.ui.genericeditor:1.2.300                       | maven central?         |
| org.eclipse.platform:org.eclipse.ui.ide.application:1.4.600                     | maven central?         |
| org.eclipse.platform:org.eclipse.ui.ide:3.20.0                                  | maven central?         |
| org.eclipse.platform:org.eclipse.ui.intro.quicklinks:1.1.200                    | maven central?         |
| org.eclipse.platform:org.eclipse.ui.intro.universal:3.4.300                     | maven central?         |
| org.eclipse.platform:org.eclipse.ui.intro:3.6.600                               | maven central?         |
| org.eclipse.platform:org.eclipse.ui.monitoring:1.2.300                          | maven central?         |
| org.eclipse.platform:org.eclipse.ui.navigator.resources:3.8.500                 | maven central?         |
| org.eclipse.platform:org.eclipse.ui.navigator:3.10.400                          | maven central?         |
| org.eclipse.platform:org.eclipse.ui.net:1.4.100                                 | maven central?         |
| org.eclipse.platform:org.eclipse.ui.themes:1.2.2100                             | maven central?         |
| org.eclipse.platform:org.eclipse.ui.views.log:1.3.400                           | maven central?         |
| org.eclipse.platform:org.eclipse.ui.views.properties.tabbed:3.9.300             | maven central?         |
| org.eclipse.platform:org.eclipse.ui.views:3.11.300                              | maven central?         |
| org.eclipse.platform:org.eclipse.ui.workbench.texteditor:3.16.600               | maven central?         |
| org.eclipse.platform:org.eclipse.ui.workbench:3.127.0                           | maven central?         |
| org.eclipse.platform:org.eclipse.ui:3.201.200                                   | maven central?         |
| org.eclipse.platform:org.eclipse.update.configurator:3.4.1000                   | maven central?         |
| org.eclipse.platform:org.eclipse.urischeme:1.2.200                              | maven central?         |
| com.jcraft.jsch:0.1.55.v20221112-0806                                           | p2 R-4.26-202211231800 |
| com.sun.el:2.2.0.v201303151357                                                  | p2 R-4.26-202211231800 |
| javax.annotation:1.3.5.v20221112-0806                                           | p2 R-4.26-202211231800 |
| javax.el:2.2.0.v201303151357                                                    | p2 R-4.26-202211231800 |
| javax.inject:1.0.0.v20220405-0441                                               | p2 R-4.26-202211231800 |
| org.apache.ant:1.10.12.v20211102-1452                                           | p2 R-4.26-202211231800 |
| org.apache.batik.constants:1.16.0.v20221027-0840                                | p2 R-4.26-202211231800 |
| org.apache.batik.css:1.16.0.v20221027-0840                                      | p2 R-4.26-202211231800 |
| org.apache.batik.i18n:1.16.0.v20221027-0840                                     | p2 R-4.26-202211231800 |
| org.apache.batik.util:1.16.0.v20221027-0840                                     | p2 R-4.26-202211231800 |
| org.apache.commons.codec:1.14.0.v20221112-0806                                  | p2 R-4.26-202211231800 |
| org.apache.commons.jxpath:1.3.0.v200911051830                                   | p2 R-4.26-202211231800 |
| org.apache.commons.logging:1.2.0.v20180409-1502                                 | p2 R-4.26-202211231800 |
| org.apache.httpcomponents.client5.httpclient5:5.1.3.v20221013-1742              | p2 R-4.26-202211231800 |
| org.apache.httpcomponents.core5.httpcore5-h2:5.1.4.v20221013-1742               | p2 R-4.26-202211231800 |
| org.apache.httpcomponents.core5.httpcore5:5.1.4.v20221013-1742                  | p2 R-4.26-202211231800 |
| org.apache.jasper.glassfish:2.2.2.v201501141630                                 | p2 R-4.26-202211231800 |
| org.apache.lucene.analyzers-common:8.4.1.v20221112-0806                         | p2 R-4.26-202211231800 |
| org.apache.lucene.analyzers-smartcn:8.4.1.v20221112-0806                        | p2 R-4.26-202211231800 |
| org.apache.lucene.core:8.4.1.v20221112-0806                                     | p2 R-4.26-202211231800 |
| org.apache.xmlgraphics:2.7.0.v20221018-0736                                     | p2 R-4.26-202211231800 |
| org.w3c.css.sac:1.3.1.v200903091627                                             | p2 R-4.26-202211231800 |
| org.w3c.dom.events:3.0.0.draft20060413_v201105210656                            | p2 R-4.26-202211231800 |
| org.w3c.dom.smil:1.0.1.v200903091627                                            | p2 R-4.26-202211231800 |
| org.w3c.dom.svg:1.1.0.v201011041433                                             | p2 R-4.26-202211231800 |
+---------------------------------------------------------------------------------+------------------------+
╔═ _02_minimal_installed_empty ═╗
No jars were specified.
╔═ _03_corrosion_allCategories ═╗
+-------------------------------------------------+---------------------------------------------------+
| id                                              | name \n description                               |
+-------------------------------------------------+---------------------------------------------------+
| 202206282034.Default                            | Uncategorized                                     |
|                                                 |   Default category for otherwise uncategorized    |
|                                                 |   features                                        |
| 202206282034.org.eclipse.corrosion.category     | Corrosion: Rust edition in Eclipse IDE            |
|                                                 |   Corrosion enables Rust application development  |
|                                                 |   in the Eclipse IDE.                             |
| 202206282034.org.eclipse.corrosion.dev.category | Developer Resources                               |
|                                                 |   Useful resources for Corrosion contributors     |
|                                                 |   and integrators.                                |
| I20221123-1800.Default                          | Uncategorized                                     |
|                                                 |   Default category for otherwise uncategorized    |
|                                                 |   features                                        |
| org.eclipse.equinox.target.categoryIU           | Equinox Target Components                         |
|                                                 |   Features especially useful to install as PDE    |
|                                                 |   runtime targets.                                |
| org.eclipse.platform.ide.categoryIU             | Eclipse Platform                                  |
|                                                 |   Minimum version of Eclipse: no source or API    |
|                                                 |   documentation, no PDE or JDT.                   |
| org.eclipse.platform.sdk.categoryIU             | Eclipse Platform SDK                              |
|                                                 |   Minimum version of Eclipse with source and      |
|                                                 |   documentation, no PDE or JDT.                   |
| org.eclipse.rcp.categoryIU                      | Eclipse RCP Target Components                     |
|                                                 |   Features to use as PDE runtime target, while    |
|                                                 |   developing RCP applications.                    |
| org.eclipse.releng.java.languages.categoryIU    | Eclipse Java Development Tools                    |
|                                                 |   Tools to allow development with Java.           |
| org.eclipse.releng.pde.categoryIU               | Eclipse Plugin Development Tools                  |
|                                                 |   Tools to develop bundles, plugins and features. |
| org.eclipse.releng.testsIU                      | Eclipse Tests, Tools, Examples, and Extras        |
|                                                 |   Collection of Misc. Features, such as unit      |
|                                                 |   tests, SWT and e4 tools, examples, and          |
|                                                 |   compatibility features not shipped as part of   |
|                                                 |   main SDK, but which some people may desire in   |
|                                                 |   creating products based on previous versions of |
|                                                 |   Eclipse.                                        |
| org.eclipse.sdk.ide.categoryIU                  | Eclipse SDK                                       |
|                                                 |   The full version of Eclipse, with source and    |
|                                                 |   documentation: Platform, JDT and PDE.           |
+-------------------------------------------------+---------------------------------------------------+
╔═ _04_corrosion_installed ═╗
WARNING!!! 13 unmet requirement(s), 674 ambigous requirement(s).
WARNING!!!  For more info: `gradlew equoList --problems`
62 optional requirement(s) were not installed. For more info: `gradlew equoList --optional`
+---------------------------------------------------------------------------------+------------------------+
| maven coordinate / p2 id                                                        | repo                   |
+---------------------------------------------------------------------------------+------------------------+
| com.google.code.gson:gson:2.9.1                                                 | maven central          |
| com.ibm.icu:icu4j:72.1                                                          | maven central          |
| commons-io:commons-io:2.11.0                                                    | maven central          |
| jakarta.servlet:jakarta.servlet-api:4.0.4                                       | maven central          |
| javax.servlet.jsp:javax.servlet.jsp-api:2.3.3                                   | maven central          |
| net.java.dev.jna:jna-platform:5.12.1                                            | maven central          |
| net.java.dev.jna:jna:5.12.1                                                     | maven central          |
| org.apache.felix:org.apache.felix.gogo.runtime:1.1.6                            | maven central          |
| org.apache.felix:org.apache.felix.scr:2.2.4                                     | maven central          |
| org.bouncycastle:bcpg-jdk18on:1.72                                              | maven central          |
| org.bouncycastle:bcprov-jdk18on:1.72                                            | maven central          |
| org.eclipse.jetty:jetty-http:10.0.12                                            | maven central          |
| org.eclipse.jetty:jetty-io:10.0.12                                              | maven central          |
| org.eclipse.jetty:jetty-security:10.0.12                                        | maven central          |
| org.eclipse.jetty:jetty-server:10.0.12                                          | maven central          |
| org.eclipse.jetty:jetty-servlet:10.0.12                                         | maven central          |
| org.eclipse.jetty:jetty-util-ajax:10.0.12                                       | maven central          |
| org.eclipse.jetty:jetty-util:10.0.12                                            | maven central          |
| org.osgi:org.osgi.service.cm:1.6.1                                              | maven central          |
| org.osgi:org.osgi.service.component:1.5.0                                       | maven central          |
| org.osgi:org.osgi.service.device:1.1.1                                          | maven central          |
| org.osgi:org.osgi.service.event:1.4.1                                           | maven central          |
| org.osgi:org.osgi.service.metatype:1.4.1                                        | maven central          |
| org.osgi:org.osgi.service.prefs:1.1.2                                           | maven central          |
| org.osgi:org.osgi.service.provisioning:1.2.0                                    | maven central          |
| org.osgi:org.osgi.service.upnp:1.2.1                                            | maven central          |
| org.osgi:org.osgi.service.useradmin:1.1.1                                       | maven central          |
| org.osgi:org.osgi.service.wireadmin:1.0.2                                       | maven central          |
| org.osgi:org.osgi.util.function:1.2.0                                           | maven central          |
| org.osgi:org.osgi.util.measurement:1.0.2                                        | maven central          |
| org.osgi:org.osgi.util.position:1.0.1                                           | maven central          |
| org.osgi:org.osgi.util.promise:1.2.0                                            | maven central          |
| org.osgi:org.osgi.util.xml:1.0.2                                                | maven central          |
| org.ow2.sat4j:org.ow2.sat4j.core:2.3.6                                          | maven central          |
| org.ow2.sat4j:org.ow2.sat4j.pb:2.3.6                                            | maven central          |
| org.eclipse.ecf:org.eclipse.ecf.filetransfer:5.1.102                            | maven central?         |
| org.eclipse.ecf:org.eclipse.ecf.identity:3.9.402                                | maven central?         |
| org.eclipse.ecf:org.eclipse.ecf.provider.filetransfer.httpclient5:1.0.401       | maven central?         |
| org.eclipse.ecf:org.eclipse.ecf.provider.filetransfer.ssl:1.0.201               | maven central?         |
| org.eclipse.ecf:org.eclipse.ecf.provider.filetransfer:3.2.800                   | maven central?         |
| org.eclipse.ecf:org.eclipse.ecf.ssl:1.2.401                                     | maven central?         |
| org.eclipse.ecf:org.eclipse.ecf:3.10.0                                          | maven central?         |
| org.eclipse.emf:org.eclipse.emf.common:2.27.0                                   | maven central?         |
| org.eclipse.emf:org.eclipse.emf.ecore.change:2.14.0                             | maven central?         |
| org.eclipse.emf:org.eclipse.emf.ecore.xmi:2.17.0                                | maven central?         |
| org.eclipse.emf:org.eclipse.emf.ecore:2.29.0                                    | maven central?         |
| org.eclipse.jdt:org.eclipse.jdt.core:3.32.0                                     | maven central?         |
| org.eclipse.platform:org.eclipse.ant.core:3.6.500                               | maven central?         |
| org.eclipse.platform:org.eclipse.compare.core:3.7.100                           | maven central?         |
| org.eclipse.platform:org.eclipse.compare:3.8.500                                | maven central?         |
| org.eclipse.platform:org.eclipse.core.commands:3.10.300                         | maven central?         |
| org.eclipse.platform:org.eclipse.core.contenttype:3.8.200                       | maven central?         |
| org.eclipse.platform:org.eclipse.core.databinding.beans:1.9.0                   | maven central?         |
| org.eclipse.platform:org.eclipse.core.databinding.observable:1.12.100           | maven central?         |
| org.eclipse.platform:org.eclipse.core.databinding.property:1.9.100              | maven central?         |
| org.eclipse.platform:org.eclipse.core.databinding:1.11.200                      | maven central?         |
| org.eclipse.platform:org.eclipse.core.expressions:3.8.200                       | maven central?         |
| org.eclipse.platform:org.eclipse.core.externaltools:1.2.300                     | maven central?         |
| org.eclipse.platform:org.eclipse.core.filebuffers:3.7.200                       | maven central?         |
| org.eclipse.platform:org.eclipse.core.filesystem:1.9.500                        | maven central?         |
| org.eclipse.platform:org.eclipse.core.jobs:3.13.200                             | maven central?         |
| org.eclipse.platform:org.eclipse.core.net:1.4.0                                 | maven central?         |
| org.eclipse.platform:org.eclipse.core.resources:3.18.100                        | maven central?         |
| org.eclipse.platform:org.eclipse.core.runtime:3.26.100                          | maven central?         |
| org.eclipse.platform:org.eclipse.core.variables:3.5.100                         | maven central?         |
| org.eclipse.platform:org.eclipse.debug.core:3.20.0                              | maven central?         |
| org.eclipse.platform:org.eclipse.debug.ui.launchview:1.0.300                    | maven central?         |
| org.eclipse.platform:org.eclipse.debug.ui:3.17.100                              | maven central?         |
| org.eclipse.platform:org.eclipse.e4.core.commands:1.0.300                       | maven central?         |
| org.eclipse.platform:org.eclipse.e4.core.contexts:1.11.0                        | maven central?         |
| org.eclipse.platform:org.eclipse.e4.core.di.annotations:1.7.200                 | maven central?         |
| org.eclipse.platform:org.eclipse.e4.core.di.extensions.supplier:0.16.400        | maven central?         |
| org.eclipse.platform:org.eclipse.e4.core.di.extensions:0.17.200                 | maven central?         |
| org.eclipse.platform:org.eclipse.e4.core.di:1.8.300                             | maven central?         |
| org.eclipse.platform:org.eclipse.e4.core.services:2.3.400                       | maven central?         |
| org.eclipse.platform:org.eclipse.e4.emf.xpath:0.3.100                           | maven central?         |
| org.eclipse.platform:org.eclipse.e4.ui.bindings:0.13.200                        | maven central?         |
| org.eclipse.platform:org.eclipse.e4.ui.css.core:0.13.400                        | maven central?         |
| org.eclipse.platform:org.eclipse.e4.ui.css.swt.theme:0.13.200                   | maven central?         |
| org.eclipse.platform:org.eclipse.e4.ui.css.swt:0.14.700                         | maven central?         |
| org.eclipse.platform:org.eclipse.e4.ui.di:1.4.100                               | maven central?         |
| org.eclipse.platform:org.eclipse.e4.ui.dialogs:1.3.400                          | maven central?         |
| org.eclipse.platform:org.eclipse.e4.ui.ide:3.16.200                             | maven central?         |
| org.eclipse.platform:org.eclipse.e4.ui.model.workbench:2.2.300                  | maven central?         |
| org.eclipse.platform:org.eclipse.e4.ui.progress:0.3.600                         | maven central?         |
| org.eclipse.platform:org.eclipse.e4.ui.services:1.5.100                         | maven central?         |
| org.eclipse.platform:org.eclipse.e4.ui.widgets:1.3.100                          | maven central?         |
| org.eclipse.platform:org.eclipse.e4.ui.workbench.addons.swt:1.4.500             | maven central?         |
| org.eclipse.platform:org.eclipse.e4.ui.workbench.renderers.swt:0.15.700         | maven central?         |
| org.eclipse.platform:org.eclipse.e4.ui.workbench.swt:0.16.700                   | maven central?         |
| org.eclipse.platform:org.eclipse.e4.ui.workbench3:0.16.100                      | maven central?         |
| org.eclipse.platform:org.eclipse.e4.ui.workbench:1.14.0                         | maven central?         |
| org.eclipse.platform:org.eclipse.equinox.app:1.6.200                            | maven central?         |
| org.eclipse.platform:org.eclipse.equinox.bidi:1.4.200                           | maven central?         |
| org.eclipse.platform:org.eclipse.equinox.common:3.17.0                          | maven central?         |
| org.eclipse.platform:org.eclipse.equinox.concurrent:1.2.100                     | maven central?         |
| org.eclipse.platform:org.eclipse.equinox.console:1.4.500                        | maven central?         |
| org.eclipse.platform:org.eclipse.equinox.event:1.6.100                          | maven central?         |
| org.eclipse.platform:org.eclipse.equinox.frameworkadmin.equinox:1.2.200         | maven central?         |
| org.eclipse.platform:org.eclipse.equinox.frameworkadmin:2.2.100                 | maven central?         |
| org.eclipse.platform:org.eclipse.equinox.http.jetty:3.8.200                     | maven central?         |
| org.eclipse.platform:org.eclipse.equinox.http.registry:1.3.200                  | maven central?         |
| org.eclipse.platform:org.eclipse.equinox.http.servlet:1.7.400                   | maven central?         |
| org.eclipse.platform:org.eclipse.equinox.jsp.jasper.registry:1.2.100            | maven central?         |
| org.eclipse.platform:org.eclipse.equinox.jsp.jasper:1.1.700                     | maven central?         |
| org.eclipse.platform:org.eclipse.equinox.launcher:1.6.400                       | maven central?         |
| org.eclipse.platform:org.eclipse.equinox.p2.artifact.repository:1.4.600         | maven central?         |
| org.eclipse.platform:org.eclipse.equinox.p2.console:1.2.100                     | maven central?         |
| org.eclipse.platform:org.eclipse.equinox.p2.core:2.9.200                        | maven central?         |
| org.eclipse.platform:org.eclipse.equinox.p2.director.app:1.2.300                | maven central?         |
| org.eclipse.platform:org.eclipse.equinox.p2.director:2.5.400                    | maven central?         |
| org.eclipse.platform:org.eclipse.equinox.p2.directorywatcher:1.3.100            | maven central?         |
| org.eclipse.platform:org.eclipse.equinox.p2.engine:2.7.500                      | maven central?         |
| org.eclipse.platform:org.eclipse.equinox.p2.extensionlocation:1.4.100           | maven central?         |
| org.eclipse.platform:org.eclipse.equinox.p2.garbagecollector:1.2.100            | maven central?         |
| org.eclipse.platform:org.eclipse.equinox.p2.jarprocessor:1.2.300                | maven central?         |
| org.eclipse.platform:org.eclipse.equinox.p2.metadata.repository:1.4.100         | maven central?         |
| org.eclipse.platform:org.eclipse.equinox.p2.metadata:2.6.300                    | maven central?         |
| org.eclipse.platform:org.eclipse.equinox.p2.operations:2.6.100                  | maven central?         |
| org.eclipse.platform:org.eclipse.equinox.p2.publisher.eclipse:1.4.200           | maven central?         |
| org.eclipse.platform:org.eclipse.equinox.p2.publisher:1.7.200                   | maven central?         |
| org.eclipse.platform:org.eclipse.equinox.p2.reconciler.dropins:1.4.200          | maven central?         |
| org.eclipse.platform:org.eclipse.equinox.p2.repository.tools:2.3.200            | maven central?         |
| org.eclipse.platform:org.eclipse.equinox.p2.repository:2.6.300                  | maven central?         |
| org.eclipse.platform:org.eclipse.equinox.p2.touchpoint.eclipse:2.3.300          | maven central?         |
| org.eclipse.platform:org.eclipse.equinox.p2.touchpoint.natives:1.4.400          | maven central?         |
| org.eclipse.platform:org.eclipse.equinox.p2.transport.ecf:1.3.300               | maven central?         |
| org.eclipse.platform:org.eclipse.equinox.p2.ui.importexport:1.3.300             | maven central?         |
| org.eclipse.platform:org.eclipse.equinox.p2.ui.sdk.scheduler:1.5.400            | maven central?         |
| org.eclipse.platform:org.eclipse.equinox.p2.ui.sdk:1.2.100                      | maven central?         |
| org.eclipse.platform:org.eclipse.equinox.p2.ui:2.7.700                          | maven central?         |
| org.eclipse.platform:org.eclipse.equinox.p2.updatechecker:1.3.100               | maven central?         |
| org.eclipse.platform:org.eclipse.equinox.p2.updatesite:1.2.300                  | maven central?         |
| org.eclipse.platform:org.eclipse.equinox.preferences:3.10.100                   | maven central?         |
| org.eclipse.platform:org.eclipse.equinox.registry:3.11.200                      | maven central?         |
| org.eclipse.platform:org.eclipse.equinox.security.ui:1.3.400                    | maven central?         |
| org.eclipse.platform:org.eclipse.equinox.security:1.3.1000                      | maven central?         |
| org.eclipse.platform:org.eclipse.equinox.simpleconfigurator.manipulator:2.2.100 | maven central?         |
| org.eclipse.platform:org.eclipse.equinox.simpleconfigurator:1.4.200             | maven central?         |
| org.eclipse.platform:org.eclipse.equinox.supplement:1.10.600                    | maven central?         |
| org.eclipse.platform:org.eclipse.help.base:4.3.900                              | maven central?         |
| org.eclipse.platform:org.eclipse.help.ui:4.4.100                                | maven central?         |
| org.eclipse.platform:org.eclipse.help.webapp:3.10.900                           | maven central?         |
| org.eclipse.platform:org.eclipse.help:3.9.100                                   | maven central?         |
| org.eclipse.platform:org.eclipse.jface.databinding:1.14.0                       | maven central?         |
| org.eclipse.platform:org.eclipse.jface.notifications:0.5.100                    | maven central?         |
| org.eclipse.platform:org.eclipse.jface.text:3.22.0                              | maven central?         |
| org.eclipse.platform:org.eclipse.jface:3.28.0                                   | maven central?         |
| org.eclipse.platform:org.eclipse.jsch.core:1.4.0                                | maven central?         |
| org.eclipse.platform:org.eclipse.jsch.ui:1.4.200                                | maven central?         |
| org.eclipse.platform:org.eclipse.ltk.core.refactoring:3.13.0                    | maven central?         |
| org.eclipse.platform:org.eclipse.ltk.ui.refactoring:3.12.200                    | maven central?         |
| org.eclipse.platform:org.eclipse.osgi.compatibility.state:1.2.800               | maven central?         |
| org.eclipse.platform:org.eclipse.osgi.services:3.10.200                         | maven central?         |
| org.eclipse.platform:org.eclipse.osgi.util:3.7.100                              | maven central?         |
| org.eclipse.platform:org.eclipse.osgi:3.18.200                                  | maven central?         |
| org.eclipse.platform:org.eclipse.platform.doc.user:4.26.0                       | maven central?         |
| org.eclipse.platform:org.eclipse.platform:4.26.0                                | maven central?         |
| org.eclipse.platform:org.eclipse.rcp:4.26.0                                     | maven central?         |
| org.eclipse.platform:org.eclipse.search:3.14.300                                | maven central?         |
| org.eclipse.platform:org.eclipse.swt:3.122.0                                    | maven central?         |
| org.eclipse.platform:org.eclipse.team.core:3.9.600                              | maven central?         |
| org.eclipse.platform:org.eclipse.team.genericeditor.diff.extension:1.1.100      | maven central?         |
| org.eclipse.platform:org.eclipse.team.ui:3.9.500                                | maven central?         |
| org.eclipse.platform:org.eclipse.text.quicksearch:1.1.400                       | maven central?         |
| org.eclipse.platform:org.eclipse.text:3.12.300                                  | maven central?         |
| org.eclipse.platform:org.eclipse.ui.browser:3.7.300                             | maven central?         |
| org.eclipse.platform:org.eclipse.ui.cheatsheets:3.7.500                         | maven central?         |
| org.eclipse.platform:org.eclipse.ui.console:3.11.400                            | maven central?         |
| org.eclipse.platform:org.eclipse.ui.editors:3.14.400                            | maven central?         |
| org.eclipse.platform:org.eclipse.ui.externaltools:3.5.200                       | maven central?         |
| org.eclipse.platform:org.eclipse.ui.forms:3.11.500                              | maven central?         |
| org.eclipse.platform:org.eclipse.ui.genericeditor:1.2.300                       | maven central?         |
| org.eclipse.platform:org.eclipse.ui.ide.application:1.4.600                     | maven central?         |
| org.eclipse.platform:org.eclipse.ui.ide:3.20.0                                  | maven central?         |
| org.eclipse.platform:org.eclipse.ui.intro.quicklinks:1.1.200                    | maven central?         |
| org.eclipse.platform:org.eclipse.ui.intro.universal:3.4.300                     | maven central?         |
| org.eclipse.platform:org.eclipse.ui.intro:3.6.600                               | maven central?         |
| org.eclipse.platform:org.eclipse.ui.monitoring:1.2.300                          | maven central?         |
| org.eclipse.platform:org.eclipse.ui.navigator.resources:3.8.500                 | maven central?         |
| org.eclipse.platform:org.eclipse.ui.navigator:3.10.400                          | maven central?         |
| org.eclipse.platform:org.eclipse.ui.net:1.4.100                                 | maven central?         |
| org.eclipse.platform:org.eclipse.ui.themes:1.2.2100                             | maven central?         |
| org.eclipse.platform:org.eclipse.ui.views.log:1.3.400                           | maven central?         |
| org.eclipse.platform:org.eclipse.ui.views.properties.tabbed:3.9.300             | maven central?         |
| org.eclipse.platform:org.eclipse.ui.views:3.11.300                              | maven central?         |
| org.eclipse.platform:org.eclipse.ui.workbench.texteditor:3.16.600               | maven central?         |
| org.eclipse.platform:org.eclipse.ui.workbench:3.127.0                           | maven central?         |
| org.eclipse.platform:org.eclipse.ui:3.201.200                                   | maven central?         |
| org.eclipse.platform:org.eclipse.update.configurator:3.4.1000                   | maven central?         |
| org.eclipse.platform:org.eclipse.urischeme:1.2.200                              | maven central?         |
| com.sun.el:2.2.0.v201303151357                                                  | p2 1.2.4               |
| javax.el:2.2.0.v201303151357                                                    | p2 1.2.4               |
| javax.inject:1.0.0.v20220405-0441                                               | p2 1.2.4               |
| javax.servlet.jsp:2.2.0.v201112011158                                           | p2 1.2.4               |
| org.apache.ant:1.10.12.v20211102-1452                                           | p2 1.2.4               |
| org.apache.commons.jxpath:1.3.0.v200911051830                                   | p2 1.2.4               |
| org.apache.commons.logging:1.2.0.v20180409-1502                                 | p2 1.2.4               |
| org.apache.felix.gogo.command:1.1.2.v20210111-1007                              | p2 1.2.4               |
| org.apache.felix.gogo.shell:1.1.4.v20210111-1007                                | p2 1.2.4               |
| org.apache.jasper.glassfish:2.2.2.v201501141630                                 | p2 1.2.4               |
| org.eclipse.cdt.core.native:6.2.200.202204200013                                | p2 1.2.4               |
| org.eclipse.cdt.native.serial:1.2.600.202206081808                              | p2 1.2.4               |
| org.eclipse.corrosion:1.2.4.202206282034                                        | p2 1.2.4               |
| org.eclipse.lsp4e:0.13.12.202206011407                                          | p2 1.2.4               |
| org.eclipse.lsp4j.jsonrpc:0.14.0.v20220526-1518                                 | p2 1.2.4               |
| org.eclipse.lsp4j:0.14.0.v20220526-1518                                         | p2 1.2.4               |
| org.eclipse.mylyn.wikitext.markdown:3.0.42.20220107230029                       | p2 1.2.4               |
| org.eclipse.mylyn.wikitext:3.0.42.20220107230029                                | p2 1.2.4               |
| org.eclipse.tm4e.core:0.4.4.202205101731                                        | p2 1.2.4               |
| org.eclipse.tm4e.languageconfiguration:0.4.1.202205101731                       | p2 1.2.4               |
| org.eclipse.tm4e.registry:0.5.1.202205101731                                    | p2 1.2.4               |
| org.eclipse.tm4e.ui:0.5.1.202205101731                                          | p2 1.2.4               |
| org.eclipse.unittest.ui:1.0.100.v20210429-0605                                  | p2 1.2.4               |
| org.eclipse.xtext.xbase.lib:2.27.0.v20220530-0353                               | p2 1.2.4               |
| org.jcodings:1.0.57                                                             | p2 1.2.4               |
| org.joni:2.1.43                                                                 | p2 1.2.4               |
| org.jsoup:1.14.3.v20211012-1727                                                 | p2 1.2.4               |
| org.slf4j.api:1.7.30.v20200204-2150                                             | p2 1.2.4               |
| org.tukaani.xz:1.9.0.v20210624-1259                                             | p2 1.2.4               |
| org.w3c.css.sac:1.3.1.v200903091627                                             | p2 1.2.4               |
| org.w3c.dom.events:3.0.0.draft20060413_v201105210656                            | p2 1.2.4               |
| org.w3c.dom.smil:1.0.1.v200903091627                                            | p2 1.2.4               |
| org.w3c.dom.svg:1.1.0.v201011041433                                             | p2 1.2.4               |
| com.jcraft.jsch:0.1.55.v20221112-0806                                           | p2 R-4.26-202211231800 |
| javax.annotation:1.3.5.v20221112-0806                                           | p2 R-4.26-202211231800 |
| org.apache.batik.constants:1.16.0.v20221027-0840                                | p2 R-4.26-202211231800 |
| org.apache.batik.css:1.16.0.v20221027-0840                                      | p2 R-4.26-202211231800 |
| org.apache.batik.i18n:1.16.0.v20221027-0840                                     | p2 R-4.26-202211231800 |
| org.apache.batik.util:1.16.0.v20221027-0840                                     | p2 R-4.26-202211231800 |
| org.apache.commons.codec:1.14.0.v20221112-0806                                  | p2 R-4.26-202211231800 |
| org.apache.httpcomponents.client5.httpclient5:5.1.3.v20221013-1742              | p2 R-4.26-202211231800 |
| org.apache.httpcomponents.core5.httpcore5-h2:5.1.4.v20221013-1742               | p2 R-4.26-202211231800 |
| org.apache.httpcomponents.core5.httpcore5:5.1.4.v20221013-1742                  | p2 R-4.26-202211231800 |
| org.apache.lucene.analyzers-common:8.4.1.v20221112-0806                         | p2 R-4.26-202211231800 |
| org.apache.lucene.analyzers-smartcn:8.4.1.v20221112-0806                        | p2 R-4.26-202211231800 |
| org.apache.lucene.core:8.4.1.v20221112-0806                                     | p2 R-4.26-202211231800 |
| org.apache.xmlgraphics:2.7.0.v20221018-0736                                     | p2 R-4.26-202211231800 |
+---------------------------------------------------------------------------------+------------------------+
╔═ _05/apt ═╗
1 unit available with id org.eclipse.jdt.compiler.apt
  1.4.300.v20221108-0856  [ ] not included by install
+--------------------------------------------------+-------------------------------------------------------------+
| key                                              | value                                                       |
+--------------------------------------------------+-------------------------------------------------------------+
| id                                               | org.eclipse.jdt.compiler.apt                                |
| version                                          | 1.4.300.v20221108-0856                                      |
| maven coordinate                                 | org.eclipse.jdt:org.eclipse.jdt.compiler.apt:1.4.300        |
| maven repo                                       | maven central?                                              |
| prop artifact-classifier                         | osgi.bundle                                                 |
| prop maven-artifactId                            | org.eclipse.jdt.compiler.apt                                |
| prop maven-groupId                               | org.eclipse.jdt                                             |
| prop maven-type                                  | eclipse-plugin                                              |
| prop maven-version                               | 1.4.300-SNAPSHOT                                            |
| prop org.eclipse.equinox.p2.name                 | Java Compiler Apt                                           |
| req org.eclipse.jdt.core                         | org.eclipse.jdt.core:3.32.0.v20221108-1853                  |
| req (opt) org.eclipse.jdt.internal.compiler.tool | 2 available                                                 |
|                                                  |   org.eclipse.jdt.compiler.tool:1.3.200.v20220802-0458      |
|                                                  |   org.eclipse.jdt.core.compiler.batch:3.32.0.v20221108-1853 |
+--------------------------------------------------+-------------------------------------------------------------+
╔═ _05/batch ═╗
1 unit available with id org.eclipse.jdt.core.compiler.batch
  3.32.0.v20221108-1853  [ ] not included by install
+----------------------------------+-------------------------------------+
| key                              | value                               |
+----------------------------------+-------------------------------------+
| id                               | org.eclipse.jdt.core.compiler.batch |
| version                          | 3.32.0.v20221108-1853               |
| maven coordinate                 | org.eclipse.jdt:ecj:3.32.0          |
| maven repo                       | maven central?                      |
| prop artifact-classifier         | osgi.bundle                         |
| prop maven-artifactId            | org.eclipse.jdt.core                |
| prop maven-groupId               | org.eclipse.jdt                     |
| prop maven-type                  | eclipse-plugin                      |
| prop maven-version               | 3.32.0-SNAPSHOT                     |
| prop org.eclipse.equinox.p2.name | Eclipse Compiler for Java(TM)       |
+----------------------------------+-------------------------------------+
╔═ _05_corrosion_problems ═╗
13 unmet requirement(s).
+-------------------------------+-------------------------------------------------+
| unmet requirement             | needed by                                       |
+-------------------------------+-------------------------------------------------+
| pkg com.google.common.base    | é.lsp4e:0.13.12.202206011407                    |
|                               | é.lsp4j:0.14.0.v20220526-1518                   |
|                               | é.mylyn.wikitext:3.0.42.20220107230029          |
|                               | é.mylyn.wikitext.markdown:3.0.42.20220107230029 |
| pkg com.google.common.collect | é.mylyn.wikitext:3.0.42.20220107230029          |
|                               | é.mylyn.wikitext.markdown:3.0.42.20220107230029 |
| pkg com.google.common.escape  | é.mylyn.wikitext:3.0.42.20220107230029          |
| pkg com.google.common.xml     | é.mylyn.wikitext:3.0.42.20220107230029          |
| bundle com.google.guava       | é.lsp4e:0.13.12.202206011407                    |
|                               | é.tm4e.core:0.4.4.202205101731                  |
|                               | é.tm4e.ui:0.5.1.202205101731                    |
|                               | é.xtext.xbase.lib:2.27.0.v20220530-0353         |
| bundle é.cdt.core             | é.corrosion:1.2.4.202206282034                  |
| bundle é.cdt.debug.core       | é.corrosion:1.2.4.202206282034                  |
| bundle é.cdt.debug.ui         | é.corrosion:1.2.4.202206282034                  |
| bundle é.cdt.dsf              | é.corrosion:1.2.4.202206282034                  |
| bundle é.cdt.dsf.gdb          | é.corrosion:1.2.4.202206282034                  |
| bundle é.cdt.dsf.gdb.ui       | é.corrosion:1.2.4.202206282034                  |
| bundle é.cdt.launch           | é.corrosion:1.2.4.202206282034                  |
| bundle org.yaml.snakeyaml     | é.tm4e.core:0.4.4.202205101731                  |
+-------------------------------+-------------------------------------------------+
é org.eclipse

674 ambiguous requirement(s).
+--------------------------------------------------------+----------------------------------------------------------------+-----------+
| ambiguous requirement                                  | candidate                                                      | installed |
+--------------------------------------------------------+----------------------------------------------------------------+-----------+
| pkg com.google.gson                                    | com.google.gson:2.9.1                                          | [x]       |
|                                                        | com.google.gson:2.8.9.v20220111-1409                           | [ ]       |
| pkg com.google.gson.annotations                        | com.google.gson:2.9.1                                          | [x]       |
|                                                        | com.google.gson:2.8.9.v20220111-1409                           | [ ]       |
| pkg com.google.gson.reflect                            | com.google.gson:2.9.1                                          | [x]       |
|                                                        | com.google.gson:2.8.9.v20220111-1409                           | [ ]       |
| pkg com.google.gson.stream                             | com.google.gson:2.9.1                                          | [x]       |
|                                                        | com.google.gson:2.8.9.v20220111-1409                           | [ ]       |
| pkg com.ibm.icu.text                                   | com.ibm.icu:72.1.0                                             | [x]       |
|                                                        | com.ibm.icu:67.1.0.v20200706-1749                              | [ ]       |
| pkg com.ibm.icu.util                                   | com.ibm.icu:72.1.0                                             | [x]       |
|                                                        | com.ibm.icu:67.1.0.v20200706-1749                              | [ ]       |
| pkg javax.annotation                                   | javax.annotation:1.3.5.v20221112-0806                          | [x]       |
|                                                        | javax.annotation:1.3.5.v20200909-1856                          | [ ]       |
| pkg javax.annotation.security                          | javax.annotation:1.3.5.v20221112-0806                          | [x]       |
|                                                        | javax.annotation:1.3.5.v20200909-1856                          | [ ]       |
| pkg javax.annotation.sql                               | javax.annotation:1.3.5.v20221112-0806                          | [x]       |
|                                                        | javax.annotation:1.3.5.v20200909-1856                          | [ ]       |
| pkg javax.el                                           | javax.el:2.2.0.v201303151357                                   | [x]       |
|                                                        | javax.el:2.2.0.v201303151357                                   | [ ]       |
| pkg javax.inject                                       | javax.inject:1.0.0.v20220405-0441                              | [x]       |
|                                                        | javax.inject:1.0.0.v20220405-0441                              | [ ]       |
| pkg javax.servlet                                      | jakarta.servlet-api:4.0.0                                      | [x]       |
|                                                        | jakarta.servlet-api:4.0.0                                      | [ ]       |
| pkg javax.servlet.annotation                           | jakarta.servlet-api:4.0.0                                      | [x]       |
|                                                        | jakarta.servlet-api:4.0.0                                      | [ ]       |
| pkg javax.servlet.descriptor                           | jakarta.servlet-api:4.0.0                                      | [x]       |
|                                                        | jakarta.servlet-api:4.0.0                                      | [ ]       |
| pkg javax.servlet.http                                 | jakarta.servlet-api:4.0.0                                      | [x]       |
|                                                        | jakarta.servlet-api:4.0.0                                      | [ ]       |
| pkg mozilla                                            | á.httpcomponents.client5.httpclient5:5.1.3.v20221013-1742      | [x]       |
|                                                        | á.httpcomponents.client5.httpclient5:5.1.2.v20211217-1500      | [ ]       |
| pkg á.batik.i18n                                       | á.batik.i18n:1.16.0.v20221027-0840                             | [x]       |
|                                                        | á.batik.i18n:1.14.0.v20210324-0332                             | [ ]       |
| pkg á.commons.codec                                    | á.commons.codec:1.14.0.v20221112-0806                          | [x]       |
|                                                        | á.commons.codec:1.14.0.v20200818-1422                          | [ ]       |
| pkg á.commons.codec.binary                             | á.commons.codec:1.14.0.v20221112-0806                          | [x]       |
|                                                        | á.commons.codec:1.14.0.v20200818-1422                          | [ ]       |
| pkg á.commons.io                                       | á.commons.commons-io:2.11.0                                    | [x]       |
|                                                        | á.commons.commons-io:2.11.0                                    | [x]       |
|                                                        | á.commons.io:2.8.0.v20210415-0900                              | [ ]       |
| pkg á.commons.io.output                                | á.commons.commons-io:2.11.0                                    | [x]       |
|                                                        | á.commons.commons-io:2.11.0                                    | [x]       |
|                                                        | á.commons.io:2.8.0.v20210415-0900                              | [ ]       |
| pkg á.commons.logging                                  | á.commons.logging:1.2.0.v20180409-1502                         | [x]       |
|                                                        | á.commons.logging:1.2.0.v20180409-1502                         | [ ]       |
| pkg á.felix.gogo.runtime.threadio                      | á.felix.gogo.runtime:1.1.6                                     | [x]       |
|                                                        | á.felix.gogo.runtime:1.1.4.v20210111-1007                      | [ ]       |
| pkg á.felix.scr.component                              | á.felix.scr:2.2.4                                              | [x]       |
|                                                        | á.felix.scr:2.1.24.v20200924-1939                              | [ ]       |
| pkg á.felix.scr.info                                   | á.felix.scr:2.2.4                                              | [x]       |
|                                                        | á.felix.scr:2.1.24.v20200924-1939                              | [ ]       |
| pkg á.felix.service.command                            | á.felix.gogo.runtime:1.1.6                                     | [x]       |
|                                                        | á.felix.gogo.runtime:1.1.4.v20210111-1007                      | [ ]       |
| pkg á.felix.service.threadio                           | á.felix.gogo.runtime:1.1.6                                     | [x]       |
|                                                        | á.felix.gogo.runtime:1.1.4.v20210111-1007                      | [ ]       |
| pkg á.hc.client5                                       | á.httpcomponents.client5.httpclient5:5.1.3.v20221013-1742      | [x]       |
|                                                        | á.httpcomponents.client5.httpclient5:5.1.2.v20211217-1500      | [ ]       |
| pkg á.hc.client5.http                                  | á.httpcomponents.client5.httpclient5:5.1.3.v20221013-1742      | [x]       |
|                                                        | á.httpcomponents.client5.httpclient5:5.1.2.v20211217-1500      | [ ]       |
| pkg á.hc.client5.http.async                            | á.httpcomponents.client5.httpclient5:5.1.3.v20221013-1742      | [x]       |
|                                                        | á.httpcomponents.client5.httpclient5:5.1.2.v20211217-1500      | [ ]       |
| pkg á.hc.client5.http.async.methods                    | á.httpcomponents.client5.httpclient5:5.1.3.v20221013-1742      | [x]       |
|                                                        | á.httpcomponents.client5.httpclient5:5.1.2.v20211217-1500      | [ ]       |
| pkg á.hc.client5.http.auth                             | á.httpcomponents.client5.httpclient5:5.1.3.v20221013-1742      | [x]       |
|                                                        | á.httpcomponents.client5.httpclient5:5.1.2.v20211217-1500      | [ ]       |
| pkg á.hc.client5.http.classic                          | á.httpcomponents.client5.httpclient5:5.1.3.v20221013-1742      | [x]       |
|                                                        | á.httpcomponents.client5.httpclient5:5.1.2.v20211217-1500      | [ ]       |
| pkg á.hc.client5.http.classic.methods                  | á.httpcomponents.client5.httpclient5:5.1.3.v20221013-1742      | [x]       |
|                                                        | á.httpcomponents.client5.httpclient5:5.1.2.v20211217-1500      | [ ]       |
| pkg á.hc.client5.http.config                           | á.httpcomponents.client5.httpclient5:5.1.3.v20221013-1742      | [x]       |
|                                                        | á.httpcomponents.client5.httpclient5:5.1.2.v20211217-1500      | [ ]       |
| pkg á.hc.client5.http.cookie                           | á.httpcomponents.client5.httpclient5:5.1.3.v20221013-1742      | [x]       |
|                                                        | á.httpcomponents.client5.httpclient5:5.1.2.v20211217-1500      | [ ]       |
| pkg á.hc.client5.http.entity                           | á.httpcomponents.client5.httpclient5:5.1.3.v20221013-1742      | [x]       |
|                                                        | á.httpcomponents.client5.httpclient5:5.1.2.v20211217-1500      | [ ]       |
| pkg á.hc.client5.http.entity.mime                      | á.httpcomponents.client5.httpclient5:5.1.3.v20221013-1742      | [x]       |
|                                                        | á.httpcomponents.client5.httpclient5:5.1.2.v20211217-1500      | [ ]       |
| pkg á.hc.client5.http.impl                             | á.httpcomponents.client5.httpclient5:5.1.3.v20221013-1742      | [x]       |
|                                                        | á.httpcomponents.client5.httpclient5:5.1.2.v20211217-1500      | [ ]       |
| pkg á.hc.client5.http.impl.async                       | á.httpcomponents.client5.httpclient5:5.1.3.v20221013-1742      | [x]       |
|                                                        | á.httpcomponents.client5.httpclient5:5.1.2.v20211217-1500      | [ ]       |
| pkg á.hc.client5.http.impl.auth                        | á.httpcomponents.client5.httpclient5:5.1.3.v20221013-1742      | [x]       |
|                                                        | á.httpcomponents.client5.httpclient5:5.1.2.v20211217-1500      | [ ]       |
| pkg á.hc.client5.http.impl.classic                     | á.httpcomponents.client5.httpclient5:5.1.3.v20221013-1742      | [x]       |
|                                                        | á.httpcomponents.client5.httpclient5:5.1.2.v20211217-1500      | [ ]       |
| pkg á.hc.client5.http.impl.cookie                      | á.httpcomponents.client5.httpclient5:5.1.3.v20221013-1742      | [x]       |
|                                                        | á.httpcomponents.client5.httpclient5:5.1.2.v20211217-1500      | [ ]       |
| pkg á.hc.client5.http.impl.io                          | á.httpcomponents.client5.httpclient5:5.1.3.v20221013-1742      | [x]       |
|                                                        | á.httpcomponents.client5.httpclient5:5.1.2.v20211217-1500      | [ ]       |
| pkg á.hc.client5.http.impl.nio                         | á.httpcomponents.client5.httpclient5:5.1.3.v20221013-1742      | [x]       |
|                                                        | á.httpcomponents.client5.httpclient5:5.1.2.v20211217-1500      | [ ]       |
| pkg á.hc.client5.http.impl.routing                     | á.httpcomponents.client5.httpclient5:5.1.3.v20221013-1742      | [x]       |
|                                                        | á.httpcomponents.client5.httpclient5:5.1.2.v20211217-1500      | [ ]       |
| pkg á.hc.client5.http.io                               | á.httpcomponents.client5.httpclient5:5.1.3.v20221013-1742      | [x]       |
|                                                        | á.httpcomponents.client5.httpclient5:5.1.2.v20211217-1500      | [ ]       |
| pkg á.hc.client5.http.nio                              | á.httpcomponents.client5.httpclient5:5.1.3.v20221013-1742      | [x]       |
|                                                        | á.httpcomponents.client5.httpclient5:5.1.2.v20211217-1500      | [ ]       |
| pkg á.hc.client5.http.protocol                         | á.httpcomponents.client5.httpclient5:5.1.3.v20221013-1742      | [x]       |
|                                                        | á.httpcomponents.client5.httpclient5:5.1.2.v20211217-1500      | [ ]       |
| pkg á.hc.client5.http.psl                              | á.httpcomponents.client5.httpclient5:5.1.3.v20221013-1742      | [x]       |
|                                                        | á.httpcomponents.client5.httpclient5:5.1.2.v20211217-1500      | [ ]       |
| pkg á.hc.client5.http.routing                          | á.httpcomponents.client5.httpclient5:5.1.3.v20221013-1742      | [x]       |
|                                                        | á.httpcomponents.client5.httpclient5:5.1.2.v20211217-1500      | [ ]       |
| pkg á.hc.client5.http.socket                           | á.httpcomponents.client5.httpclient5:5.1.3.v20221013-1742      | [x]       |
|                                                        | á.httpcomponents.client5.httpclient5:5.1.2.v20211217-1500      | [ ]       |
| pkg á.hc.client5.http.ssl                              | á.httpcomponents.client5.httpclient5:5.1.3.v20221013-1742      | [x]       |
|                                                        | á.httpcomponents.client5.httpclient5:5.1.2.v20211217-1500      | [ ]       |
| pkg á.hc.client5.http.utils                            | á.httpcomponents.client5.httpclient5:5.1.3.v20221013-1742      | [x]       |
|                                                        | á.httpcomponents.client5.httpclient5:5.1.2.v20211217-1500      | [ ]       |
| pkg á.hc.core5.concurrent                              | á.httpcomponents.core5.httpcore5:5.1.4.v20221013-1742          | [x]       |
|                                                        | á.httpcomponents.core5.httpcore5:5.1.2.v20211217-1500          | [ ]       |
| pkg á.hc.core5.function                                | á.httpcomponents.core5.httpcore5:5.1.4.v20221013-1742          | [x]       |
|                                                        | á.httpcomponents.core5.httpcore5:5.1.2.v20211217-1500          | [ ]       |
| pkg á.hc.core5.http                                    | á.httpcomponents.core5.httpcore5:5.1.4.v20221013-1742          | [x]       |
|                                                        | á.httpcomponents.core5.httpcore5:5.1.2.v20211217-1500          | [ ]       |
| pkg á.hc.core5.http.config                             | á.httpcomponents.core5.httpcore5:5.1.4.v20221013-1742          | [x]       |
|                                                        | á.httpcomponents.core5.httpcore5:5.1.2.v20211217-1500          | [ ]       |
| pkg á.hc.core5.http.impl                               | á.httpcomponents.core5.httpcore5:5.1.4.v20221013-1742          | [x]       |
|                                                        | á.httpcomponents.core5.httpcore5:5.1.2.v20211217-1500          | [ ]       |
| pkg á.hc.core5.http.impl.bootstrap                     | á.httpcomponents.core5.httpcore5:5.1.4.v20221013-1742          | [x]       |
|                                                        | á.httpcomponents.core5.httpcore5:5.1.2.v20211217-1500          | [ ]       |
| pkg á.hc.core5.http.impl.io                            | á.httpcomponents.core5.httpcore5:5.1.4.v20221013-1742          | [x]       |
|                                                        | á.httpcomponents.core5.httpcore5:5.1.2.v20211217-1500          | [ ]       |
| pkg á.hc.core5.http.impl.nio                           | á.httpcomponents.core5.httpcore5:5.1.4.v20221013-1742          | [x]       |
|                                                        | á.httpcomponents.core5.httpcore5:5.1.2.v20211217-1500          | [ ]       |
| pkg á.hc.core5.http.io                                 | á.httpcomponents.core5.httpcore5:5.1.4.v20221013-1742          | [x]       |
|                                                        | á.httpcomponents.core5.httpcore5:5.1.2.v20211217-1500          | [ ]       |
| pkg á.hc.core5.http.io.entity                          | á.httpcomponents.core5.httpcore5:5.1.4.v20221013-1742          | [x]       |
|                                                        | á.httpcomponents.core5.httpcore5:5.1.2.v20211217-1500          | [ ]       |
| pkg á.hc.core5.http.io.support                         | á.httpcomponents.core5.httpcore5:5.1.4.v20221013-1742          | [x]       |
|                                                        | á.httpcomponents.core5.httpcore5:5.1.2.v20211217-1500          | [ ]       |
| pkg á.hc.core5.http.message                            | á.httpcomponents.core5.httpcore5:5.1.4.v20221013-1742          | [x]       |
|                                                        | á.httpcomponents.core5.httpcore5:5.1.2.v20211217-1500          | [ ]       |
| pkg á.hc.core5.http.nio                                | á.httpcomponents.core5.httpcore5:5.1.4.v20221013-1742          | [x]       |
|                                                        | á.httpcomponents.core5.httpcore5:5.1.2.v20211217-1500          | [ ]       |
| pkg á.hc.core5.http.nio.command                        | á.httpcomponents.core5.httpcore5:5.1.4.v20221013-1742          | [x]       |
|                                                        | á.httpcomponents.core5.httpcore5:5.1.2.v20211217-1500          | [ ]       |
| pkg á.hc.core5.http.nio.entity                         | á.httpcomponents.core5.httpcore5:5.1.4.v20221013-1742          | [x]       |
|                                                        | á.httpcomponents.core5.httpcore5:5.1.2.v20211217-1500          | [ ]       |
| pkg á.hc.core5.http.nio.ssl                            | á.httpcomponents.core5.httpcore5:5.1.4.v20221013-1742          | [x]       |
|                                                        | á.httpcomponents.core5.httpcore5:5.1.2.v20211217-1500          | [ ]       |
| pkg á.hc.core5.http.nio.support                        | á.httpcomponents.core5.httpcore5:5.1.4.v20221013-1742          | [x]       |
|                                                        | á.httpcomponents.core5.httpcore5:5.1.2.v20211217-1500          | [ ]       |
| pkg á.hc.core5.http.protocol                           | á.httpcomponents.core5.httpcore5:5.1.4.v20221013-1742          | [x]       |
|                                                        | á.httpcomponents.core5.httpcore5:5.1.2.v20211217-1500          | [ ]       |
| pkg á.hc.core5.http.ssl                                | á.httpcomponents.core5.httpcore5:5.1.4.v20221013-1742          | [x]       |
|                                                        | á.httpcomponents.core5.httpcore5:5.1.2.v20211217-1500          | [ ]       |
| pkg á.hc.core5.http.support                            | á.httpcomponents.core5.httpcore5:5.1.4.v20221013-1742          | [x]       |
|                                                        | á.httpcomponents.core5.httpcore5:5.1.2.v20211217-1500          | [ ]       |
| pkg á.hc.core5.http2                                   | á.httpcomponents.core5.httpcore5-h2:5.1.4.v20221013-1742       | [x]       |
|                                                        | á.httpcomponents.core5.httpcore5-h2:5.1.2.v20211217-1500       | [ ]       |
| pkg á.hc.core5.http2.config                            | á.httpcomponents.core5.httpcore5-h2:5.1.4.v20221013-1742       | [x]       |
|                                                        | á.httpcomponents.core5.httpcore5-h2:5.1.2.v20211217-1500       | [ ]       |
| pkg á.hc.core5.http2.frame                             | á.httpcomponents.core5.httpcore5-h2:5.1.4.v20221013-1742       | [x]       |
|                                                        | á.httpcomponents.core5.httpcore5-h2:5.1.2.v20211217-1500       | [ ]       |
| pkg á.hc.core5.http2.hpack                             | á.httpcomponents.core5.httpcore5-h2:5.1.4.v20221013-1742       | [x]       |
|                                                        | á.httpcomponents.core5.httpcore5-h2:5.1.2.v20211217-1500       | [ ]       |
| pkg á.hc.core5.http2.impl                              | á.httpcomponents.core5.httpcore5-h2:5.1.4.v20221013-1742       | [x]       |
|                                                        | á.httpcomponents.core5.httpcore5-h2:5.1.2.v20211217-1500       | [ ]       |
| pkg á.hc.core5.http2.impl.nio                          | á.httpcomponents.core5.httpcore5-h2:5.1.4.v20221013-1742       | [x]       |
|                                                        | á.httpcomponents.core5.httpcore5-h2:5.1.2.v20211217-1500       | [ ]       |
| pkg á.hc.core5.http2.nio                               | á.httpcomponents.core5.httpcore5-h2:5.1.4.v20221013-1742       | [x]       |
|                                                        | á.httpcomponents.core5.httpcore5-h2:5.1.2.v20211217-1500       | [ ]       |
| pkg á.hc.core5.http2.nio.command                       | á.httpcomponents.core5.httpcore5-h2:5.1.4.v20221013-1742       | [x]       |
|                                                        | á.httpcomponents.core5.httpcore5-h2:5.1.2.v20211217-1500       | [ ]       |
| pkg á.hc.core5.http2.nio.pool                          | á.httpcomponents.core5.httpcore5-h2:5.1.4.v20221013-1742       | [x]       |
|                                                        | á.httpcomponents.core5.httpcore5-h2:5.1.2.v20211217-1500       | [ ]       |
| pkg á.hc.core5.http2.nio.support                       | á.httpcomponents.core5.httpcore5-h2:5.1.4.v20221013-1742       | [x]       |
|                                                        | á.httpcomponents.core5.httpcore5-h2:5.1.2.v20211217-1500       | [ ]       |
| pkg á.hc.core5.http2.protocol                          | á.httpcomponents.core5.httpcore5-h2:5.1.4.v20221013-1742       | [x]       |
|                                                        | á.httpcomponents.core5.httpcore5-h2:5.1.2.v20211217-1500       | [ ]       |
| pkg á.hc.core5.http2.ssl                               | á.httpcomponents.core5.httpcore5-h2:5.1.4.v20221013-1742       | [x]       |
|                                                        | á.httpcomponents.core5.httpcore5-h2:5.1.2.v20211217-1500       | [ ]       |
| pkg á.hc.core5.io                                      | á.httpcomponents.core5.httpcore5:5.1.4.v20221013-1742          | [x]       |
|                                                        | á.httpcomponents.core5.httpcore5:5.1.2.v20211217-1500          | [ ]       |
| pkg á.hc.core5.net                                     | á.httpcomponents.core5.httpcore5:5.1.4.v20221013-1742          | [x]       |
|                                                        | á.httpcomponents.core5.httpcore5:5.1.2.v20211217-1500          | [ ]       |
| pkg á.hc.core5.pool                                    | á.httpcomponents.core5.httpcore5:5.1.4.v20221013-1742          | [x]       |
|                                                        | á.httpcomponents.core5.httpcore5:5.1.2.v20211217-1500          | [ ]       |
| pkg á.hc.core5.reactor                                 | á.httpcomponents.core5.httpcore5:5.1.4.v20221013-1742          | [x]       |
|                                                        | á.httpcomponents.core5.httpcore5:5.1.2.v20211217-1500          | [ ]       |
| pkg á.hc.core5.reactor.ssl                             | á.httpcomponents.core5.httpcore5:5.1.4.v20221013-1742          | [x]       |
|                                                        | á.httpcomponents.core5.httpcore5:5.1.2.v20211217-1500          | [ ]       |
| pkg á.hc.core5.ssl                                     | á.httpcomponents.core5.httpcore5:5.1.4.v20221013-1742          | [x]       |
|                                                        | á.httpcomponents.core5.httpcore5:5.1.2.v20211217-1500          | [ ]       |
| pkg á.hc.core5.util                                    | á.httpcomponents.core5.httpcore5:5.1.4.v20221013-1742          | [x]       |
|                                                        | á.httpcomponents.core5.httpcore5:5.1.2.v20211217-1500          | [ ]       |
| pkg á.jasper.servlet                                   | á.jasper.glassfish:2.2.2.v201501141630                         | [x]       |
|                                                        | á.jasper.glassfish:2.2.2.v201501141630                         | [ ]       |
| pkg á.xmlgraphics.java2d.color                         | á.xmlgraphics:2.7.0.v20221018-0736                             | [x]       |
|                                                        | á.xmlgraphics:2.6.0.v20210409-0748                             | [ ]       |
| pkg org.bouncycastle                                   | bcprov:1.72.0                                                  | [x]       |
|                                                        | org.bouncycastle.bcprov:1.70.0.v20220507-1208                  | [ ]       |
| pkg org.bouncycastle.asn1                              | bcprov:1.72.0                                                  | [x]       |
|                                                        | org.bouncycastle.bcprov:1.70.0.v20220507-1208                  | [ ]       |
| pkg org.bouncycastle.asn1.cryptlib                     | bcprov:1.72.0                                                  | [x]       |
|                                                        | org.bouncycastle.bcprov:1.70.0.v20220507-1208                  | [ ]       |
| pkg org.bouncycastle.asn1.edec                         | bcprov:1.72.0                                                  | [x]       |
|                                                        | org.bouncycastle.bcprov:1.70.0.v20220507-1208                  | [ ]       |
| pkg org.bouncycastle.asn1.gnu                          | bcprov:1.72.0                                                  | [x]       |
|                                                        | org.bouncycastle.bcprov:1.70.0.v20220507-1208                  | [ ]       |
| pkg org.bouncycastle.asn1.nist                         | bcprov:1.72.0                                                  | [x]       |
|                                                        | org.bouncycastle.bcprov:1.70.0.v20220507-1208                  | [ ]       |
| pkg org.bouncycastle.asn1.pkcs                         | bcprov:1.72.0                                                  | [x]       |
|                                                        | org.bouncycastle.bcprov:1.70.0.v20220507-1208                  | [ ]       |
| pkg org.bouncycastle.asn1.sec                          | bcprov:1.72.0                                                  | [x]       |
|                                                        | org.bouncycastle.bcprov:1.70.0.v20220507-1208                  | [ ]       |
| pkg org.bouncycastle.asn1.teletrust                    | bcprov:1.72.0                                                  | [x]       |
|                                                        | org.bouncycastle.bcprov:1.70.0.v20220507-1208                  | [ ]       |
| pkg org.bouncycastle.asn1.x509                         | bcprov:1.72.0                                                  | [x]       |
|                                                        | org.bouncycastle.bcprov:1.70.0.v20220507-1208                  | [ ]       |
| pkg org.bouncycastle.asn1.x9                           | bcprov:1.72.0                                                  | [x]       |
|                                                        | org.bouncycastle.bcprov:1.70.0.v20220507-1208                  | [ ]       |
| pkg org.bouncycastle.bcpg                              | bcpg:1.72.0                                                    | [x]       |
|                                                        | org.bouncycastle.bcpg:1.70.0.v20220507-1208                    | [ ]       |
| pkg org.bouncycastle.crypto                            | bcprov:1.72.0                                                  | [x]       |
|                                                        | org.bouncycastle.bcprov:1.70.0.v20220507-1208                  | [ ]       |
| pkg org.bouncycastle.crypto.agreement                  | bcprov:1.72.0                                                  | [x]       |
|                                                        | org.bouncycastle.bcprov:1.70.0.v20220507-1208                  | [ ]       |
| pkg org.bouncycastle.crypto.digests                    | bcprov:1.72.0                                                  | [x]       |
|                                                        | org.bouncycastle.bcprov:1.70.0.v20220507-1208                  | [ ]       |
| pkg org.bouncycastle.crypto.ec                         | bcprov:1.72.0                                                  | [x]       |
|                                                        | org.bouncycastle.bcprov:1.70.0.v20220507-1208                  | [ ]       |
| pkg org.bouncycastle.crypto.encodings                  | bcprov:1.72.0                                                  | [x]       |
|                                                        | org.bouncycastle.bcprov:1.70.0.v20220507-1208                  | [ ]       |
| pkg org.bouncycastle.crypto.engines                    | bcprov:1.72.0                                                  | [x]       |
|                                                        | org.bouncycastle.bcprov:1.70.0.v20220507-1208                  | [ ]       |
| pkg org.bouncycastle.crypto.generators                 | bcprov:1.72.0                                                  | [x]       |
|                                                        | org.bouncycastle.bcprov:1.70.0.v20220507-1208                  | [ ]       |
| pkg org.bouncycastle.crypto.io                         | bcprov:1.72.0                                                  | [x]       |
|                                                        | org.bouncycastle.bcprov:1.70.0.v20220507-1208                  | [ ]       |
| pkg org.bouncycastle.crypto.modes                      | bcprov:1.72.0                                                  | [x]       |
|                                                        | org.bouncycastle.bcprov:1.70.0.v20220507-1208                  | [ ]       |
| pkg org.bouncycastle.crypto.params                     | bcprov:1.72.0                                                  | [x]       |
|                                                        | org.bouncycastle.bcprov:1.70.0.v20220507-1208                  | [ ]       |
| pkg org.bouncycastle.crypto.signers                    | bcprov:1.72.0                                                  | [x]       |
|                                                        | org.bouncycastle.bcprov:1.70.0.v20220507-1208                  | [ ]       |
| pkg org.bouncycastle.crypto.util                       | bcprov:1.72.0                                                  | [x]       |
|                                                        | org.bouncycastle.bcprov:1.70.0.v20220507-1208                  | [ ]       |
| pkg org.bouncycastle.gpg.keybox                        | bcpg:1.72.0                                                    | [x]       |
|                                                        | org.bouncycastle.bcpg:1.70.0.v20220507-1208                    | [ ]       |
| pkg org.bouncycastle.gpg.keybox.jcajce                 | bcpg:1.72.0                                                    | [x]       |
|                                                        | org.bouncycastle.bcpg:1.70.0.v20220507-1208                    | [ ]       |
| pkg org.bouncycastle.jcajce.io                         | bcprov:1.72.0                                                  | [x]       |
|                                                        | org.bouncycastle.bcprov:1.70.0.v20220507-1208                  | [ ]       |
| pkg org.bouncycastle.jcajce.spec                       | bcprov:1.72.0                                                  | [x]       |
|                                                        | org.bouncycastle.bcprov:1.70.0.v20220507-1208                  | [ ]       |
| pkg org.bouncycastle.jcajce.util                       | bcprov:1.72.0                                                  | [x]       |
|                                                        | org.bouncycastle.bcprov:1.70.0.v20220507-1208                  | [ ]       |
| pkg org.bouncycastle.jce.provider                      | bcprov:1.72.0                                                  | [x]       |
|                                                        | org.bouncycastle.bcprov:1.70.0.v20220507-1208                  | [ ]       |
| pkg org.bouncycastle.math.ec                           | bcprov:1.72.0                                                  | [x]       |
|                                                        | org.bouncycastle.bcprov:1.70.0.v20220507-1208                  | [ ]       |
| pkg org.bouncycastle.math.ec.rfc8032                   | bcprov:1.72.0                                                  | [x]       |
|                                                        | org.bouncycastle.bcprov:1.70.0.v20220507-1208                  | [ ]       |
| pkg org.bouncycastle.openpgp                           | bcpg:1.72.0                                                    | [x]       |
|                                                        | org.bouncycastle.bcpg:1.70.0.v20220507-1208                    | [ ]       |
| pkg org.bouncycastle.openpgp.bc                        | bcpg:1.72.0                                                    | [x]       |
|                                                        | org.bouncycastle.bcpg:1.70.0.v20220507-1208                    | [ ]       |
| pkg org.bouncycastle.openpgp.jcajce                    | bcpg:1.72.0                                                    | [x]       |
|                                                        | org.bouncycastle.bcpg:1.70.0.v20220507-1208                    | [ ]       |
| pkg org.bouncycastle.openpgp.operator                  | bcpg:1.72.0                                                    | [x]       |
|                                                        | org.bouncycastle.bcpg:1.70.0.v20220507-1208                    | [ ]       |
| pkg org.bouncycastle.openpgp.operator.bc               | bcpg:1.72.0                                                    | [x]       |
|                                                        | org.bouncycastle.bcpg:1.70.0.v20220507-1208                    | [ ]       |
| pkg org.bouncycastle.openpgp.operator.jcajce           | bcpg:1.72.0                                                    | [x]       |
|                                                        | org.bouncycastle.bcpg:1.70.0.v20220507-1208                    | [ ]       |
| pkg org.bouncycastle.util                              | bcprov:1.72.0                                                  | [x]       |
|                                                        | org.bouncycastle.bcprov:1.70.0.v20220507-1208                  | [ ]       |
| pkg org.bouncycastle.util.encoders                     | bcprov:1.72.0                                                  | [x]       |
|                                                        | org.bouncycastle.bcprov:1.70.0.v20220507-1208                  | [ ]       |
| pkg org.bouncycastle.util.io                           | bcprov:1.72.0                                                  | [x]       |
|                                                        | org.bouncycastle.bcprov:1.70.0.v20220507-1208                  | [ ]       |
| pkg é.core.commands                                    | é.core.commands:3.10.300.v20221024-2137                        | [x]       |
|                                                        | é.core.commands:3.10.200.v20220512-0851                        | [ ]       |
| pkg é.core.commands.common                             | é.core.commands:3.10.300.v20221024-2137                        | [x]       |
|                                                        | é.core.commands:3.10.200.v20220512-0851                        | [ ]       |
| pkg é.core.expressions                                 | é.core.expressions:3.8.200.v20220613-1047                      | [x]       |
|                                                        | é.core.expressions:3.8.100.v20210910-0640                      | [ ]       |
| pkg é.core.filebuffers                                 | é.core.filebuffers:3.7.200.v20220202-1008                      | [x]       |
|                                                        | é.core.filebuffers:3.7.200.v20220202-1008                      | [ ]       |
| pkg é.core.internal.preferences                        | ë.preferences:3.10.100.v20220710-1223                          | [x]       |
|                                                        | ë.preferences:3.10.0.v20220503-1634                            | [ ]       |
| pkg é.core.internal.runtime                            | é.core.runtime:3.26.100.v20221021-0005                         | [x]       |
|                                                        | é.core.runtime:3.25.0.v20220506-1157                           | [ ]       |
|                                                        | ë.common:3.17.0.v20221108-1156                                 | [x]       |
|                                                        | ë.common:3.16.100.v20220315-2327                               | [ ]       |
| pkg é.core.runtime                                     | é.core.runtime:3.26.100.v20221021-0005                         | [x]       |
|                                                        | é.core.runtime:3.25.0.v20220506-1157                           | [ ]       |
|                                                        | ë.common:3.17.0.v20221108-1156                                 | [x]       |
|                                                        | ë.common:3.16.100.v20220315-2327                               | [ ]       |
|                                                        | ë.registry:3.11.200.v20220817-1601                             | [x]       |
|                                                        | ë.registry:3.11.100.v20211021-1418                             | [ ]       |
| pkg é.core.runtime.jobs                                | é.core.jobs:3.13.200.v20221102-1024                            | [x]       |
|                                                        | é.core.jobs:3.13.0.v20220512-1935                              | [ ]       |
| pkg é.core.runtime.preferences                         | ë.preferences:3.10.100.v20220710-1223                          | [x]       |
|                                                        | ë.preferences:3.10.0.v20220503-1634                            | [ ]       |
| pkg é.e4.core.commands                                 | é.e4.core.commands:1.0.300.v20221024-2137                      | [x]       |
|                                                        | é.e4.core.commands:1.0.100.v20211204-1536                      | [ ]       |
| pkg é.e4.core.commands.internal                        | é.e4.core.commands:1.0.300.v20221024-2137                      | [x]       |
|                                                        | é.e4.core.commands:1.0.100.v20211204-1536                      | [ ]       |
| pkg é.e4.core.contexts                                 | é.e4.core.contexts:1.11.0.v20220716-0839                       | [x]       |
|                                                        | é.e4.core.contexts:1.10.0.v20220430-0424                       | [ ]       |
| pkg é.e4.core.di                                       | é.e4.core.di:1.8.300.v20220817-1539                            | [x]       |
|                                                        | é.e4.core.di:1.8.200.v20220512-1957                            | [ ]       |
| pkg é.e4.core.di.annotations                           | é.e4.core.di.annotations:1.7.200.v20220613-1008                | [x]       |
|                                                        | é.e4.core.di.annotations:1.7.100.v20210910-0640                | [ ]       |
| pkg é.e4.core.di.extensions                            | é.e4.core.di.extensions:0.17.200.v20220613-1008                | [x]       |
|                                                        | é.e4.core.di.extensions:0.17.100.v20210910-0640                | [ ]       |
| pkg é.e4.core.di.internal.extensions                   | é.e4.core.di.extensions.supplier:0.16.400.v20220613-1047       | [x]       |
|                                                        | é.e4.core.di.extensions.supplier:0.16.300.v20220503-2248       | [ ]       |
| pkg é.e4.core.di.suppliers                             | é.e4.core.di:1.8.300.v20220817-1539                            | [x]       |
|                                                        | é.e4.core.di:1.8.200.v20220512-1957                            | [ ]       |
| pkg é.e4.core.services.log                             | é.e4.core.services:2.3.400.v20220915-1347                      | [x]       |
|                                                        | é.e4.core.services:2.3.200.v20220513-1235                      | [ ]       |
| pkg é.e4.ui.internal.workbench                         | é.e4.ui.workbench:1.14.0.v20221024-2137                        | [x]       |
|                                                        | é.e4.ui.workbench:1.13.100.v20211019-0756                      | [ ]       |
| pkg é.e4.ui.internal.workbench.addons                  | é.e4.ui.workbench:1.14.0.v20221024-2137                        | [x]       |
|                                                        | é.e4.ui.workbench:1.13.100.v20211019-0756                      | [ ]       |
| pkg é.e4.ui.internal.workbench.renderers.swt           | é.e4.ui.workbench.renderers.swt:0.15.700.v20221024-2137        | [x]       |
|                                                        | é.e4.ui.workbench.renderers.swt:0.15.500.v20220511-1638        | [ ]       |
| pkg é.e4.ui.internal.workbench.swt                     | é.e4.ui.workbench.swt:0.16.700.v20221024-2137                  | [x]       |
|                                                        | é.e4.ui.workbench.swt:0.16.500.v20220506-1520                  | [ ]       |
| pkg é.e4.ui.services                                   | é.e4.ui.services:1.5.100.v20221024-2137                        | [x]       |
|                                                        | é.e4.ui.services:1.5.0.v20210115-1333                          | [ ]       |
| pkg é.e4.ui.workbench                                  | é.e4.ui.workbench:1.14.0.v20221024-2137                        | [x]       |
|                                                        | é.e4.ui.workbench:1.13.100.v20211019-0756                      | [ ]       |
| pkg é.e4.ui.workbench.modeling                         | é.e4.ui.workbench:1.14.0.v20221024-2137                        | [x]       |
|                                                        | é.e4.ui.workbench:1.13.100.v20211019-0756                      | [ ]       |
| pkg é.e4.ui.workbench.renderers.swt                    | é.e4.ui.workbench.renderers.swt:0.15.700.v20221024-2137        | [x]       |
|                                                        | é.e4.ui.workbench.renderers.swt:0.15.500.v20220511-1638        | [ ]       |
| pkg é.ecf.filetransfer                                 | é.ecf.filetransfer:5.1.102.v20210409-2301                      | [x]       |
|                                                        | é.ecf.filetransfer:5.1.102.v20210409-2301                      | [ ]       |
| pkg é.ecf.filetransfer.events                          | é.ecf.filetransfer:5.1.102.v20210409-2301                      | [x]       |
|                                                        | é.ecf.filetransfer:5.1.102.v20210409-2301                      | [ ]       |
| pkg é.ecf.filetransfer.identity                        | é.ecf.filetransfer:5.1.102.v20210409-2301                      | [x]       |
|                                                        | é.ecf.filetransfer:5.1.102.v20210409-2301                      | [ ]       |
| pkg é.ecf.filetransfer.service                         | é.ecf.filetransfer:5.1.102.v20210409-2301                      | [x]       |
|                                                        | é.ecf.filetransfer:5.1.102.v20210409-2301                      | [ ]       |
| pkg é.ecf.internal.provider.filetransfer               | é.ecf.provider.filetransfer:3.2.800.v20220215-0126             | [x]       |
|                                                        | é.ecf.provider.filetransfer:3.2.800.v20220215-0126             | [ ]       |
| pkg é.ecf.provider.filetransfer.browse                 | é.ecf.provider.filetransfer:3.2.800.v20220215-0126             | [x]       |
|                                                        | é.ecf.provider.filetransfer:3.2.800.v20220215-0126             | [ ]       |
| pkg é.ecf.provider.filetransfer.events.socket          | é.ecf.provider.filetransfer:3.2.800.v20220215-0126             | [x]       |
|                                                        | é.ecf.provider.filetransfer:3.2.800.v20220215-0126             | [ ]       |
| pkg é.ecf.provider.filetransfer.identity               | é.ecf.provider.filetransfer:3.2.800.v20220215-0126             | [x]       |
|                                                        | é.ecf.provider.filetransfer:3.2.800.v20220215-0126             | [ ]       |
| pkg é.ecf.provider.filetransfer.retrieve               | é.ecf.provider.filetransfer:3.2.800.v20220215-0126             | [x]       |
|                                                        | é.ecf.provider.filetransfer:3.2.800.v20220215-0126             | [ ]       |
| pkg é.ecf.provider.filetransfer.util                   | é.ecf.provider.filetransfer:3.2.800.v20220215-0126             | [x]       |
|                                                        | é.ecf.provider.filetransfer:3.2.800.v20220215-0126             | [ ]       |
| pkg ë.app                                              | ë.app:1.6.200.v20220720-2012                                   | [x]       |
|                                                        | ë.app:1.6.100.v20211021-1418                                   | [ ]       |
| pkg ë.concurrent.future                                | ë.concurrent:1.2.100.v20211021-1418                            | [x]       |
|                                                        | ë.concurrent:1.2.100.v20211021-1418                            | [ ]       |
| pkg ë.frameworkadmin                                   | ë.frameworkadmin:2.2.100.v20220817-1208                        | [x]       |
|                                                        | ë.frameworkadmin:2.2.0.v20210315-2042                          | [ ]       |
| pkg ë.http.servlet                                     | ë.http.servlet:1.7.400.v20221006-1531                          | [x]       |
|                                                        | ë.http.servlet:1.7.200.v20211021-1418                          | [ ]       |
| pkg ë.internal.frameworkadmin.equinox                  | ë.frameworkadmin.equinox:1.2.200.v20220315-2155                | [x]       |
|                                                        | ë.frameworkadmin.equinox:1.2.200.v20220315-2155                | [ ]       |
| pkg ë.internal.frameworkadmin.utils                    | ë.frameworkadmin:2.2.100.v20220817-1208                        | [x]       |
|                                                        | ë.frameworkadmin:2.2.0.v20210315-2042                          | [ ]       |
| pkg ë.internal.p2.artifact.processors.checksum         | ë.p2.artifact.repository:1.4.600.v20221106-1146                | [x]       |
|                                                        | ë.p2.artifact.repository:1.4.500.v20220420-1427                | [ ]       |
| pkg ë.internal.p2.artifact.processors.pgp              | ë.p2.artifact.repository:1.4.600.v20221106-1146                | [x]       |
|                                                        | ë.p2.artifact.repository:1.4.500.v20220420-1427                | [ ]       |
| pkg ë.internal.p2.artifact.repository                  | ë.p2.artifact.repository:1.4.600.v20221106-1146                | [x]       |
|                                                        | ë.p2.artifact.repository:1.4.500.v20220420-1427                | [ ]       |
| pkg ë.internal.p2.artifact.repository.simple           | ë.p2.artifact.repository:1.4.600.v20221106-1146                | [x]       |
|                                                        | ë.p2.artifact.repository:1.4.500.v20220420-1427                | [ ]       |
| pkg ë.internal.p2.core                                 | ë.p2.core:2.9.200.v20220817-1208                               | [x]       |
|                                                        | ë.p2.core:2.9.100.v20220310-1733                               | [ ]       |
| pkg ë.internal.p2.core.helpers                         | ë.p2.core:2.9.200.v20220817-1208                               | [x]       |
|                                                        | ë.p2.core:2.9.100.v20220310-1733                               | [ ]       |
| pkg ë.internal.p2.director                             | ë.p2.director:2.5.400.v20220817-1208                           | [x]       |
|                                                        | ë.p2.director:2.5.300.v20220421-0708                           | [ ]       |
| pkg ë.internal.p2.engine                               | ë.p2.engine:2.7.500.v20220817-1208                             | [x]       |
|                                                        | ë.p2.engine:2.7.400.v20220329-1456                             | [ ]       |
| pkg ë.internal.p2.engine.phases                        | ë.p2.engine:2.7.500.v20220817-1208                             | [x]       |
|                                                        | ë.p2.engine:2.7.400.v20220329-1456                             | [ ]       |
| pkg ë.internal.p2.extensionlocation                    | ë.p2.extensionlocation:1.4.100.v20220213-0541                  | [x]       |
|                                                        | ë.p2.extensionlocation:1.4.100.v20220213-0541                  | [ ]       |
| pkg ë.internal.p2.garbagecollector                     | ë.p2.garbagecollector:1.2.100.v20221111-1340                   | [x]       |
|                                                        | ë.p2.garbagecollector:1.2.0.v20210316-1209                     | [ ]       |
| pkg ë.internal.p2.jarprocessor                         | ë.p2.jarprocessor:1.2.300.v20220420-1427                       | [x]       |
|                                                        | ë.p2.jarprocessor:1.2.300.v20220420-1427                       | [ ]       |
| pkg ë.internal.p2.metadata                             | ë.p2.metadata:2.6.300.v20220817-1208                           | [x]       |
|                                                        | ë.p2.metadata:2.6.200.v20220324-1313                           | [ ]       |
| pkg ë.internal.p2.metadata.expression                  | ë.p2.metadata:2.6.300.v20220817-1208                           | [x]       |
|                                                        | ë.p2.metadata:2.6.200.v20220324-1313                           | [ ]       |
| pkg ë.internal.p2.metadata.index                       | ë.p2.metadata:2.6.300.v20220817-1208                           | [x]       |
|                                                        | ë.p2.metadata:2.6.200.v20220324-1313                           | [ ]       |
| pkg ë.internal.p2.metadata.query                       | ë.p2.metadata:2.6.300.v20220817-1208                           | [x]       |
|                                                        | ë.p2.metadata:2.6.200.v20220324-1313                           | [ ]       |
| pkg ë.internal.p2.metadata.repository                  | ë.p2.metadata.repository:1.4.100.v20220329-1456                | [x]       |
|                                                        | ë.p2.metadata.repository:1.4.100.v20220329-1456                | [ ]       |
| pkg ë.internal.p2.metadata.repository.io               | ë.p2.metadata.repository:1.4.100.v20220329-1456                | [x]       |
|                                                        | ë.p2.metadata.repository:1.4.100.v20220329-1456                | [ ]       |
| pkg ë.internal.p2.operations                           | ë.p2.operations:2.6.100.v20220817-1208                         | [x]       |
|                                                        | ë.p2.operations:2.6.0.v20210315-2228                           | [ ]       |
| pkg ë.internal.p2.persistence                          | ë.p2.repository:2.6.300.v20221030-1923                         | [x]       |
|                                                        | ë.p2.repository:2.6.100.v20220422-1806                         | [ ]       |
| pkg ë.internal.p2.publisher                            | ë.p2.publisher:1.7.200.v20220805-0804                          | [x]       |
|                                                        | ë.p2.publisher:1.7.100.v20220420-1427                          | [ ]       |
| pkg ë.internal.p2.publisher.eclipse                    | ë.p2.publisher.eclipse:1.4.200.v20221007-0636                  | [x]       |
|                                                        | ë.p2.publisher.eclipse:1.4.100.v20220420-1427                  | [ ]       |
| pkg ë.internal.p2.repository                           | ë.p2.repository:2.6.300.v20221030-1923                         | [x]       |
|                                                        | ë.p2.repository:2.6.100.v20220422-1806                         | [ ]       |
| pkg ë.internal.p2.repository.helpers                   | ë.p2.repository:2.6.300.v20221030-1923                         | [x]       |
|                                                        | ë.p2.repository:2.6.100.v20220422-1806                         | [ ]       |
| pkg ë.internal.p2.touchpoint.eclipse                   | ë.p2.touchpoint.eclipse:2.3.300.v20220817-1208                 | [x]       |
|                                                        | ë.p2.touchpoint.eclipse:2.3.200.v20220503-2330                 | [ ]       |
| pkg ë.internal.p2.ui                                   | ë.p2.ui:2.7.700.v20221015-0933                                 | [x]       |
|                                                        | ë.p2.ui:2.7.500.v20220423-1604                                 | [ ]       |
| pkg ë.internal.p2.ui.actions                           | ë.p2.ui:2.7.700.v20221015-0933                                 | [x]       |
|                                                        | ë.p2.ui:2.7.500.v20220423-1604                                 | [ ]       |
| pkg ë.internal.p2.ui.dialogs                           | ë.p2.ui:2.7.700.v20221015-0933                                 | [x]       |
|                                                        | ë.p2.ui:2.7.500.v20220423-1604                                 | [ ]       |
| pkg ë.internal.p2.ui.model                             | ë.p2.ui:2.7.700.v20221015-0933                                 | [x]       |
|                                                        | ë.p2.ui:2.7.500.v20220423-1604                                 | [ ]       |
| pkg ë.internal.p2.ui.query                             | ë.p2.ui:2.7.700.v20221015-0933                                 | [x]       |
|                                                        | ë.p2.ui:2.7.500.v20220423-1604                                 | [ ]       |
| pkg ë.internal.p2.ui.viewers                           | ë.p2.ui:2.7.700.v20221015-0933                                 | [x]       |
|                                                        | ë.p2.ui:2.7.500.v20220423-1604                                 | [ ]       |
| pkg ë.internal.p2.update                               | ë.p2.touchpoint.eclipse:2.3.300.v20220817-1208                 | [x]       |
|                                                        | ë.p2.touchpoint.eclipse:2.3.200.v20220503-2330                 | [ ]       |
| pkg ë.internal.provisional.configurator                | ë.simpleconfigurator:1.4.200.v20221111-1340                    | [x]       |
|                                                        | ë.simpleconfigurator:1.4.0.v20210315-2228                      | [ ]       |
| pkg ë.internal.provisional.configuratormanipulator     | ë.frameworkadmin:2.2.100.v20220817-1208                        | [x]       |
|                                                        | ë.frameworkadmin:2.2.0.v20210315-2042                          | [ ]       |
| pkg ë.internal.provisional.frameworkadmin              | ë.frameworkadmin:2.2.100.v20220817-1208                        | [x]       |
|                                                        | ë.frameworkadmin:2.2.0.v20210315-2042                          | [ ]       |
| pkg ë.internal.provisional.p2.core.eventbus            | ë.p2.core:2.9.200.v20220817-1208                               | [x]       |
|                                                        | ë.p2.core:2.9.100.v20220310-1733                               | [ ]       |
| pkg ë.internal.provisional.p2.director                 | ë.p2.director:2.5.400.v20220817-1208                           | [x]       |
|                                                        | ë.p2.director:2.5.300.v20220421-0708                           | [ ]       |
| pkg ë.internal.provisional.p2.directorywatcher         | ë.p2.directorywatcher:1.3.100.v20221111-1340                   | [x]       |
|                                                        | ë.p2.directorywatcher:1.3.0.v20210316-1209                     | [ ]       |
| pkg ë.internal.provisional.p2.repository               | ë.p2.repository:2.6.300.v20221030-1923                         | [x]       |
|                                                        | ë.p2.repository:2.6.100.v20220422-1806                         | [ ]       |
| pkg ë.internal.provisional.p2.updatechecker            | ë.p2.updatechecker:1.3.100.v20221111-1340                      | [x]       |
|                                                        | ë.p2.updatechecker:1.3.0.v20210315-2228                        | [ ]       |
| pkg ë.internal.simpleconfigurator                      | ë.simpleconfigurator:1.4.200.v20221111-1340                    | [x]       |
|                                                        | ë.simpleconfigurator:1.4.0.v20210315-2228                      | [ ]       |
| pkg ë.internal.simpleconfigurator.manipulator          | ë.simpleconfigurator.manipulator:2.2.100.v20221117-1044        | [x]       |
|                                                        | ë.simpleconfigurator.manipulator:2.2.0.v20210315-2228          | [ ]       |
| pkg ë.internal.simpleconfigurator.utils                | ë.simpleconfigurator:1.4.200.v20221111-1340                    | [x]       |
|                                                        | ë.simpleconfigurator:1.4.0.v20210315-2228                      | [ ]       |
| pkg ë.jsp.jasper                                       | ë.jsp.jasper:1.1.700.v20220801-1124                            | [x]       |
|                                                        | ë.jsp.jasper:1.1.600.v20211021-1418                            | [ ]       |
| pkg ë.log                                              | ë.supplement:1.10.600.v20220726-1348                           | [x]       |
|                                                        | é.osgi:3.18.200.v20221116-1324                                 | [x]       |
|                                                        | é.osgi:3.18.0.v20220516-2155                                   | [ ]       |
| pkg ë.p2.core                                          | ë.p2.core:2.9.200.v20220817-1208                               | [x]       |
|                                                        | ë.p2.core:2.9.100.v20220310-1733                               | [ ]       |
| pkg ë.p2.core.spi                                      | ë.p2.core:2.9.200.v20220817-1208                               | [x]       |
|                                                        | ë.p2.core:2.9.100.v20220310-1733                               | [ ]       |
| pkg ë.p2.engine                                        | ë.p2.engine:2.7.500.v20220817-1208                             | [x]       |
|                                                        | ë.p2.engine:2.7.400.v20220329-1456                             | [ ]       |
| pkg ë.p2.engine.query                                  | ë.p2.engine:2.7.500.v20220817-1208                             | [x]       |
|                                                        | ë.p2.engine:2.7.400.v20220329-1456                             | [ ]       |
| pkg ë.p2.engine.spi                                    | ë.p2.engine:2.7.500.v20220817-1208                             | [x]       |
|                                                        | ë.p2.engine:2.7.400.v20220329-1456                             | [ ]       |
| pkg ë.p2.metadata                                      | ë.p2.metadata:2.6.300.v20220817-1208                           | [x]       |
|                                                        | ë.p2.metadata:2.6.200.v20220324-1313                           | [ ]       |
| pkg ë.p2.metadata.expression                           | ë.p2.metadata:2.6.300.v20220817-1208                           | [x]       |
|                                                        | ë.p2.metadata:2.6.200.v20220324-1313                           | [ ]       |
| pkg ë.p2.metadata.index                                | ë.p2.metadata:2.6.300.v20220817-1208                           | [x]       |
|                                                        | ë.p2.metadata:2.6.200.v20220324-1313                           | [ ]       |
| pkg ë.p2.operations                                    | ë.p2.operations:2.6.100.v20220817-1208                         | [x]       |
|                                                        | ë.p2.operations:2.6.0.v20210315-2228                           | [ ]       |
| pkg ë.p2.planner                                       | ë.p2.director:2.5.400.v20220817-1208                           | [x]       |
|                                                        | ë.p2.director:2.5.300.v20220421-0708                           | [ ]       |
| pkg ë.p2.publisher                                     | ë.p2.publisher:1.7.200.v20220805-0804                          | [x]       |
|                                                        | ë.p2.publisher:1.7.100.v20220420-1427                          | [ ]       |
| pkg ë.p2.publisher.actions                             | ë.p2.publisher:1.7.200.v20220805-0804                          | [x]       |
|                                                        | ë.p2.publisher:1.7.100.v20220420-1427                          | [ ]       |
| pkg ë.p2.publisher.eclipse                             | ë.p2.publisher.eclipse:1.4.200.v20221007-0636                  | [x]       |
|                                                        | ë.p2.publisher.eclipse:1.4.100.v20220420-1427                  | [ ]       |
| pkg ë.p2.query                                         | ë.p2.metadata:2.6.300.v20220817-1208                           | [x]       |
|                                                        | ë.p2.metadata:2.6.200.v20220324-1313                           | [ ]       |
| pkg ë.p2.repository                                    | ë.p2.repository:2.6.300.v20221030-1923                         | [x]       |
|                                                        | ë.p2.repository:2.6.100.v20220422-1806                         | [ ]       |
| pkg ë.p2.repository.artifact                           | ë.p2.repository:2.6.300.v20221030-1923                         | [x]       |
|                                                        | ë.p2.repository:2.6.100.v20220422-1806                         | [ ]       |
| pkg ë.p2.repository.artifact.spi                       | ë.p2.repository:2.6.300.v20221030-1923                         | [x]       |
|                                                        | ë.p2.repository:2.6.100.v20220422-1806                         | [ ]       |
| pkg ë.p2.repository.metadata                           | ë.p2.repository:2.6.300.v20221030-1923                         | [x]       |
|                                                        | ë.p2.repository:2.6.100.v20220422-1806                         | [ ]       |
| pkg ë.p2.repository.metadata.spi                       | ë.p2.repository:2.6.300.v20221030-1923                         | [x]       |
|                                                        | ë.p2.repository:2.6.100.v20220422-1806                         | [ ]       |
| pkg ë.p2.repository.spi                                | ë.p2.repository:2.6.300.v20221030-1923                         | [x]       |
|                                                        | ë.p2.repository:2.6.100.v20220422-1806                         | [ ]       |
| pkg ë.p2.ui                                            | ë.p2.ui:2.7.700.v20221015-0933                                 | [x]       |
|                                                        | ë.p2.ui:2.7.500.v20220423-1604                                 | [ ]       |
| pkg ë.security.storage                                 | ë.security:1.3.1000.v20220801-1135                             | [x]       |
|                                                        | ë.security:1.3.900.v20220108-1321                              | [ ]       |
| pkg ë.simpleconfigurator.manipulator                   | ë.simpleconfigurator.manipulator:2.2.100.v20221117-1044        | [x]       |
|                                                        | ë.simpleconfigurator.manipulator:2.2.0.v20210315-2228          | [ ]       |
| pkg ë.spi.p2.publisher                                 | ë.p2.publisher:1.7.200.v20220805-0804                          | [x]       |
|                                                        | ë.p2.publisher:1.7.100.v20220420-1427                          | [ ]       |
| pkg é.jetty.http                                       | é.jetty.http:10.0.12                                           | [x]       |
|                                                        | é.jetty.http:10.0.9                                            | [ ]       |
| pkg é.jetty.http.pathmap                               | é.jetty.http:10.0.12                                           | [x]       |
|                                                        | é.jetty.http:10.0.9                                            | [ ]       |
| pkg é.jetty.io                                         | é.jetty.io:10.0.12                                             | [x]       |
|                                                        | é.jetty.io:10.0.9                                              | [ ]       |
| pkg é.jetty.io.ssl                                     | é.jetty.io:10.0.12                                             | [x]       |
|                                                        | é.jetty.io:10.0.9                                              | [ ]       |
| pkg é.jetty.security                                   | é.jetty.security:10.0.12                                       | [x]       |
|                                                        | é.jetty.security:10.0.9                                        | [ ]       |
| pkg é.jetty.security.authentication                    | é.jetty.security:10.0.12                                       | [x]       |
|                                                        | é.jetty.security:10.0.9                                        | [ ]       |
| pkg é.jetty.server                                     | é.jetty.server:10.0.12                                         | [x]       |
|                                                        | é.jetty.server:10.0.9                                          | [ ]       |
| pkg é.jetty.server.handler                             | é.jetty.server:10.0.12                                         | [x]       |
|                                                        | é.jetty.server:10.0.9                                          | [ ]       |
| pkg é.jetty.server.handler.gzip                        | é.jetty.server:10.0.12                                         | [x]       |
|                                                        | é.jetty.server:10.0.9                                          | [ ]       |
| pkg é.jetty.server.resource                            | é.jetty.server:10.0.12                                         | [x]       |
|                                                        | é.jetty.server:10.0.9                                          | [ ]       |
| pkg é.jetty.server.session                             | é.jetty.server:10.0.12                                         | [x]       |
|                                                        | é.jetty.server:10.0.9                                          | [ ]       |
| pkg é.jetty.servlet                                    | é.jetty.servlet:10.0.12                                        | [x]       |
|                                                        | é.jetty.servlet:10.0.9                                         | [ ]       |
| pkg é.jetty.util                                       | é.jetty.util:10.0.12                                           | [x]       |
|                                                        | é.jetty.util:10.0.9                                            | [ ]       |
| pkg é.jetty.util.annotation                            | é.jetty.util:10.0.12                                           | [x]       |
|                                                        | é.jetty.util:10.0.9                                            | [ ]       |
| pkg é.jetty.util.component                             | é.jetty.util:10.0.12                                           | [x]       |
|                                                        | é.jetty.util:10.0.9                                            | [ ]       |
| pkg é.jetty.util.compression                           | é.jetty.util:10.0.12                                           | [x]       |
|                                                        | é.jetty.util:10.0.9                                            | [ ]       |
| pkg é.jetty.util.log                                   | é.jetty.util:10.0.12                                           | [x]       |
|                                                        | é.jetty.util:10.0.9                                            | [ ]       |
| pkg é.jetty.util.resource                              | é.jetty.util:10.0.12                                           | [x]       |
|                                                        | é.jetty.util:10.0.9                                            | [ ]       |
| pkg é.jetty.util.security                              | é.jetty.util:10.0.12                                           | [x]       |
|                                                        | é.jetty.util:10.0.9                                            | [ ]       |
| pkg é.jetty.util.ssl                                   | é.jetty.util:10.0.12                                           | [x]       |
|                                                        | é.jetty.util:10.0.9                                            | [ ]       |
| pkg é.jetty.util.statistic                             | é.jetty.util:10.0.12                                           | [x]       |
|                                                        | é.jetty.util:10.0.9                                            | [ ]       |
| pkg é.jetty.util.thread                                | é.jetty.util:10.0.12                                           | [x]       |
|                                                        | é.jetty.util:10.0.9                                            | [ ]       |
| pkg é.jetty.util.thread.strategy                       | é.jetty.util:10.0.12                                           | [x]       |
|                                                        | é.jetty.util:10.0.9                                            | [ ]       |
| pkg é.jface.bindings                                   | é.jface:3.28.0.v20221024-1426                                  | [x]       |
|                                                        | é.jface:3.26.0.v20220513-0449                                  | [ ]       |
| pkg é.jface.bindings.keys                              | é.jface:3.28.0.v20221024-1426                                  | [x]       |
|                                                        | é.jface:3.26.0.v20220513-0449                                  | [ ]       |
| pkg é.jface.bindings.keys.formatting                   | é.jface:3.28.0.v20221024-1426                                  | [x]       |
|                                                        | é.jface:3.26.0.v20220513-0449                                  | [ ]       |
| pkg é.jface.dialogs                                    | é.jface:3.28.0.v20221024-1426                                  | [x]       |
|                                                        | é.jface:3.26.0.v20220513-0449                                  | [ ]       |
| pkg é.jface.resource                                   | é.jface:3.28.0.v20221024-1426                                  | [x]       |
|                                                        | é.jface:3.26.0.v20220513-0449                                  | [ ]       |
| pkg é.jface.window                                     | é.jface:3.28.0.v20221024-1426                                  | [x]       |
|                                                        | é.jface:3.26.0.v20220513-0449                                  | [ ]       |
| pkg é.osgi.framework.console                           | ë.supplement:1.10.600.v20220726-1348                           | [x]       |
|                                                        | é.osgi:3.18.200.v20221116-1324                                 | [x]       |
|                                                        | é.osgi:3.18.0.v20220516-2155                                   | [ ]       |
| pkg é.osgi.framework.eventmgr                          | ë.supplement:1.10.600.v20220726-1348                           | [x]       |
|                                                        | é.osgi:3.18.200.v20221116-1324                                 | [x]       |
|                                                        | é.osgi:3.18.0.v20220516-2155                                   | [ ]       |
| pkg é.osgi.framework.log                               | ë.supplement:1.10.600.v20220726-1348                           | [x]       |
|                                                        | é.osgi:3.18.200.v20221116-1324                                 | [x]       |
|                                                        | é.osgi:3.18.0.v20220516-2155                                   | [ ]       |
| pkg é.osgi.framework.util                              | é.osgi:3.18.200.v20221116-1324                                 | [x]       |
|                                                        | é.osgi:3.18.0.v20220516-2155                                   | [ ]       |
| pkg é.osgi.internal.provisional.service.security       | é.osgi:3.18.200.v20221116-1324                                 | [x]       |
|                                                        | é.osgi:3.18.0.v20220516-2155                                   | [ ]       |
| pkg é.osgi.internal.service.security                   | é.osgi:3.18.200.v20221116-1324                                 | [x]       |
|                                                        | é.osgi:3.18.0.v20220516-2155                                   | [ ]       |
| pkg é.osgi.report.resolution                           | ë.supplement:1.10.600.v20220726-1348                           | [x]       |
|                                                        | é.osgi:3.18.200.v20221116-1324                                 | [x]       |
|                                                        | é.osgi:3.18.0.v20220516-2155                                   | [ ]       |
| pkg é.osgi.service.datalocation                        | ë.supplement:1.10.600.v20220726-1348                           | [x]       |
|                                                        | é.osgi:3.18.200.v20221116-1324                                 | [x]       |
|                                                        | é.osgi:3.18.0.v20220516-2155                                   | [ ]       |
| pkg é.osgi.service.debug                               | ë.supplement:1.10.600.v20220726-1348                           | [x]       |
|                                                        | é.osgi:3.18.200.v20221116-1324                                 | [x]       |
|                                                        | é.osgi:3.18.0.v20220516-2155                                   | [ ]       |
| pkg é.osgi.service.environment                         | ë.supplement:1.10.600.v20220726-1348                           | [x]       |
|                                                        | é.osgi:3.18.200.v20221116-1324                                 | [x]       |
|                                                        | é.osgi:3.18.0.v20220516-2155                                   | [ ]       |
| pkg é.osgi.service.localization                        | ë.supplement:1.10.600.v20220726-1348                           | [x]       |
|                                                        | é.osgi:3.18.200.v20221116-1324                                 | [x]       |
|                                                        | é.osgi:3.18.0.v20220516-2155                                   | [ ]       |
| pkg é.osgi.service.pluginconversion                    | é.osgi:3.18.200.v20221116-1324                                 | [x]       |
|                                                        | é.osgi:3.18.0.v20220516-2155                                   | [ ]       |
| pkg é.osgi.service.resolver                            | é.osgi:3.18.200.v20221116-1324                                 | [x]       |
|                                                        | é.osgi:3.18.0.v20220516-2155                                   | [ ]       |
| pkg é.osgi.service.runnable                            | ë.supplement:1.10.600.v20220726-1348                           | [x]       |
|                                                        | é.osgi:3.18.200.v20221116-1324                                 | [x]       |
|                                                        | é.osgi:3.18.0.v20220516-2155                                   | [ ]       |
| pkg é.osgi.service.security                            | é.osgi:3.18.200.v20221116-1324                                 | [x]       |
|                                                        | é.osgi:3.18.0.v20220516-2155                                   | [ ]       |
| pkg é.osgi.service.urlconversion                       | ë.supplement:1.10.600.v20220726-1348                           | [x]       |
|                                                        | é.osgi:3.18.200.v20221116-1324                                 | [x]       |
|                                                        | é.osgi:3.18.0.v20220516-2155                                   | [ ]       |
| pkg é.osgi.signedcontent                               | é.osgi:3.18.200.v20221116-1324                                 | [x]       |
|                                                        | é.osgi:3.18.0.v20220516-2155                                   | [ ]       |
| pkg é.osgi.storagemanager                              | ë.supplement:1.10.600.v20220726-1348                           | [x]       |
|                                                        | é.osgi:3.18.200.v20221116-1324                                 | [x]       |
|                                                        | é.osgi:3.18.0.v20220516-2155                                   | [ ]       |
| pkg é.osgi.util                                        | ë.supplement:1.10.600.v20220726-1348                           | [x]       |
|                                                        | é.osgi:3.18.200.v20221116-1324                                 | [x]       |
|                                                        | é.osgi:3.18.0.v20220516-2155                                   | [ ]       |
| pkg é.ui.forms.widgets                                 | é.ui.forms:3.11.500.v20221024-2137                             | [x]       |
|                                                        | é.ui.forms:3.11.300.v20211022-1451                             | [ ]       |
| pkg org.osgi.dto                                       | é.osgi:3.18.200.v20221116-1324                                 | [x]       |
|                                                        | é.osgi:3.18.0.v20220516-2155                                   | [ ]       |
| pkg org.osgi.framework                                 | é.osgi:3.18.200.v20221116-1324                                 | [x]       |
|                                                        | é.osgi:3.18.0.v20220516-2155                                   | [ ]       |
| pkg org.osgi.framework.dto                             | é.osgi:3.18.200.v20221116-1324                                 | [x]       |
|                                                        | é.osgi:3.18.0.v20220516-2155                                   | [ ]       |
| pkg org.osgi.framework.hooks.resolver                  | é.osgi:3.18.200.v20221116-1324                                 | [x]       |
|                                                        | é.osgi:3.18.0.v20220516-2155                                   | [ ]       |
| pkg org.osgi.framework.namespace                       | é.osgi:3.18.200.v20221116-1324                                 | [x]       |
|                                                        | é.osgi:3.18.0.v20220516-2155                                   | [ ]       |
| pkg org.osgi.framework.startlevel                      | é.osgi:3.18.200.v20221116-1324                                 | [x]       |
|                                                        | é.osgi:3.18.0.v20220516-2155                                   | [ ]       |
| pkg org.osgi.framework.wiring                          | é.osgi:3.18.200.v20221116-1324                                 | [x]       |
|                                                        | é.osgi:3.18.0.v20220516-2155                                   | [ ]       |
| pkg org.osgi.resource                                  | é.osgi:3.18.200.v20221116-1324                                 | [x]       |
|                                                        | é.osgi:3.18.0.v20220516-2155                                   | [ ]       |
| pkg org.osgi.service.application                       | ë.app:1.6.200.v20220720-2012                                   | [x]       |
|                                                        | ë.app:1.6.100.v20211021-1418                                   | [ ]       |
| pkg org.osgi.service.component.annotations             | é.osgi.services:3.11.100.v20221006-1531                        | [ ]       |
|                                                        | é.osgi.services:3.10.200.v20210723-0643                        | [x]       |
|                                                        | org.osgi.service.component.annotations:1.5.0.202109301733      | [ ]       |
| pkg org.osgi.service.condpermadmin                     | é.osgi:3.18.200.v20221116-1324                                 | [x]       |
|                                                        | é.osgi:3.18.0.v20220516-2155                                   | [ ]       |
| pkg org.osgi.service.http                              | é.osgi.services:3.11.100.v20221006-1531                        | [ ]       |
|                                                        | é.osgi.services:3.10.200.v20210723-0643                        | [x]       |
| pkg org.osgi.service.http.context                      | é.osgi.services:3.11.100.v20221006-1531                        | [ ]       |
|                                                        | é.osgi.services:3.10.200.v20210723-0643                        | [x]       |
| pkg org.osgi.service.http.runtime                      | é.osgi.services:3.11.100.v20221006-1531                        | [ ]       |
|                                                        | é.osgi.services:3.10.200.v20210723-0643                        | [x]       |
| pkg org.osgi.service.http.runtime.dto                  | é.osgi.services:3.11.100.v20221006-1531                        | [ ]       |
|                                                        | é.osgi.services:3.10.200.v20210723-0643                        | [x]       |
| pkg org.osgi.service.http.whiteboard                   | é.osgi.services:3.11.100.v20221006-1531                        | [ ]       |
|                                                        | é.osgi.services:3.10.200.v20210723-0643                        | [x]       |
| pkg org.osgi.service.log                               | é.osgi:3.18.200.v20221116-1324                                 | [x]       |
|                                                        | é.osgi:3.18.0.v20220516-2155                                   | [ ]       |
|                                                        | é.osgi.services:3.11.100.v20221006-1531                        | [ ]       |
|                                                        | é.osgi.services:3.10.200.v20210723-0643                        | [x]       |
| pkg org.osgi.service.packageadmin                      | é.osgi:3.18.200.v20221116-1324                                 | [x]       |
|                                                        | é.osgi:3.18.0.v20220516-2155                                   | [ ]       |
| pkg org.osgi.service.permissionadmin                   | é.osgi:3.18.200.v20221116-1324                                 | [x]       |
|                                                        | é.osgi:3.18.0.v20220516-2155                                   | [ ]       |
| pkg org.osgi.service.prefs                             | org.osgi.service.prefs:1.1.2.202109301733                      | [x]       |
|                                                        | org.osgi.service.prefs:1.1.2.202109301733                      | [ ]       |
| pkg org.osgi.service.resolver                          | é.osgi:3.18.200.v20221116-1324                                 | [x]       |
|                                                        | é.osgi:3.18.0.v20220516-2155                                   | [ ]       |
| pkg org.osgi.service.startlevel                        | é.osgi:3.18.200.v20221116-1324                                 | [x]       |
|                                                        | é.osgi:3.18.0.v20220516-2155                                   | [ ]       |
| pkg org.osgi.service.url                               | é.osgi:3.18.200.v20221116-1324                                 | [x]       |
|                                                        | é.osgi:3.18.0.v20220516-2155                                   | [ ]       |
| pkg org.osgi.util.function                             | org.osgi.util.function:1.2.0.202109301733                      | [x]       |
|                                                        | org.osgi.util.function:1.2.0.202109301733                      | [ ]       |
| pkg org.osgi.util.measurement                          | org.osgi.util.measurement:1.0.2.201802012109                   | [x]       |
|                                                        | org.osgi.util.measurement:1.0.2.201802012109                   | [ ]       |
| pkg org.osgi.util.promise                              | org.osgi.util.promise:1.2.0.202109301733                       | [x]       |
|                                                        | org.osgi.util.promise:1.2.0.202109301733                       | [ ]       |
| pkg org.osgi.util.tracker                              | é.osgi:3.18.200.v20221116-1324                                 | [x]       |
|                                                        | é.osgi:3.18.0.v20220516-2155                                   | [ ]       |
| pkg org.slf4j                                          | org.slf4j.api:1.7.30.v20200204-2150                            | [x]       |
|                                                        | slf4j.api:1.7.36                                               | [ ]       |
| pkg org.slf4j.event                                    | org.slf4j.api:1.7.30.v20200204-2150                            | [x]       |
|                                                        | slf4j.api:1.7.36                                               | [ ]       |
| pkg org.slf4j.helpers                                  | org.slf4j.api:1.7.30.v20200204-2150                            | [x]       |
|                                                        | slf4j.api:1.7.36                                               | [ ]       |
| pkg org.slf4j.spi                                      | org.slf4j.api:1.7.30.v20200204-2150                            | [x]       |
|                                                        | slf4j.api:1.7.36                                               | [ ]       |
| pkg org.w3c.css.sac                                    | org.w3c.css.sac:1.3.1.v200903091627                            | [x]       |
|                                                        | org.w3c.css.sac:1.3.1.v200903091627                            | [ ]       |
| pkg org.w3c.dom.svg                                    | org.w3c.dom.svg:1.1.0.v201011041433                            | [x]       |
|                                                        | org.w3c.dom.svg:1.1.0.v201011041433                            | [ ]       |
| iu com.ibm.icu                                         | com.ibm.icu:72.1.0                                             | [x]       |
|                                                        | com.ibm.icu:67.1.0.v20200706-1749                              | [ ]       |
| iu com.jcraft.jsch                                     | com.jcraft.jsch:0.1.55.v20221112-0806                          | [x]       |
|                                                        | com.jcraft.jsch:0.1.55.v20190404-1902                          | [ ]       |
| iu com.sun.el                                          | com.sun.el:2.2.0.v201303151357                                 | [x]       |
|                                                        | com.sun.el:2.2.0.v201303151357                                 | [ ]       |
| iu com.sun.jna                                         | com.sun.jna:5.12.1                                             | [x]       |
|                                                        | com.sun.jna:5.8.0.v20210503-0343                               | [ ]       |
| iu com.sun.jna.platform                                | com.sun.jna.platform:5.12.1                                    | [x]       |
|                                                        | com.sun.jna.platform:5.8.0.v20210406-1004                      | [ ]       |
| iu jakarta.servlet-api                                 | jakarta.servlet-api:4.0.0                                      | [x]       |
|                                                        | jakarta.servlet-api:4.0.0                                      | [ ]       |
| iu javax.annotation                                    | javax.annotation:1.3.5.v20221112-0806                          | [x]       |
|                                                        | javax.annotation:1.3.5.v20200909-1856                          | [ ]       |
| iu javax.el                                            | javax.el:2.2.0.v201303151357                                   | [x]       |
|                                                        | javax.el:2.2.0.v201303151357                                   | [ ]       |
| iu javax.inject                                        | javax.inject:1.0.0.v20220405-0441                              | [x]       |
|                                                        | javax.inject:1.0.0.v20220405-0441                              | [ ]       |
| iu á.ant                                               | á.ant:1.10.12.v20211102-1452                                   | [x]       |
|                                                        | á.ant:1.10.12.v20211102-1452                                   | [ ]       |
| iu á.batik.constants                                   | á.batik.constants:1.16.0.v20221027-0840                        | [x]       |
|                                                        | á.batik.constants:1.14.0.v20210324-0332                        | [ ]       |
| iu á.batik.css                                         | á.batik.css:1.16.0.v20221027-0840                              | [x]       |
|                                                        | á.batik.css:1.14.0.v20210324-0332                              | [ ]       |
| iu á.batik.i18n                                        | á.batik.i18n:1.16.0.v20221027-0840                             | [x]       |
|                                                        | á.batik.i18n:1.14.0.v20210324-0332                             | [ ]       |
| iu á.batik.util                                        | á.batik.util:1.16.0.v20221027-0840                             | [x]       |
|                                                        | á.batik.util:1.14.0.v20210324-0332                             | [ ]       |
| iu á.commons.codec                                     | á.commons.codec:1.14.0.v20221112-0806                          | [x]       |
|                                                        | á.commons.codec:1.14.0.v20200818-1422                          | [ ]       |
| iu á.commons.jxpath                                    | á.commons.jxpath:1.3.0.v200911051830                           | [x]       |
|                                                        | á.commons.jxpath:1.3.0.v200911051830                           | [ ]       |
| iu á.commons.logging                                   | á.commons.logging:1.2.0.v20180409-1502                         | [x]       |
|                                                        | á.commons.logging:1.2.0.v20180409-1502                         | [ ]       |
| iu á.felix.gogo.command                                | á.felix.gogo.command:1.1.2.v20210111-1007                      | [x]       |
|                                                        | á.felix.gogo.command:1.1.2                                     | [ ]       |
| iu á.felix.gogo.runtime                                | á.felix.gogo.runtime:1.1.6                                     | [x]       |
|                                                        | á.felix.gogo.runtime:1.1.4.v20210111-1007                      | [ ]       |
| iu á.felix.gogo.shell                                  | á.felix.gogo.shell:1.1.4.v20210111-1007                        | [x]       |
|                                                        | á.felix.gogo.shell:1.1.4                                       | [ ]       |
| iu á.felix.scr                                         | á.felix.scr:2.2.4                                              | [x]       |
|                                                        | á.felix.scr:2.1.24.v20200924-1939                              | [ ]       |
| iu á.httpcomponents.client5.httpclient5                | á.httpcomponents.client5.httpclient5:5.1.3.v20221013-1742      | [x]       |
|                                                        | á.httpcomponents.client5.httpclient5:5.1.2.v20211217-1500      | [ ]       |
| iu á.httpcomponents.core5.httpcore5                    | á.httpcomponents.core5.httpcore5:5.1.4.v20221013-1742          | [x]       |
|                                                        | á.httpcomponents.core5.httpcore5:5.1.2.v20211217-1500          | [ ]       |
| iu á.httpcomponents.core5.httpcore5-h2                 | á.httpcomponents.core5.httpcore5-h2:5.1.4.v20221013-1742       | [x]       |
|                                                        | á.httpcomponents.core5.httpcore5-h2:5.1.2.v20211217-1500       | [ ]       |
| iu á.jasper.glassfish                                  | á.jasper.glassfish:2.2.2.v201501141630                         | [x]       |
|                                                        | á.jasper.glassfish:2.2.2.v201501141630                         | [ ]       |
| iu á.lucene.analyzers-common                           | á.lucene.analyzers-common:8.4.1.v20221112-0806                 | [x]       |
|                                                        | á.lucene.analyzers-common:8.4.1.v20200122-1459                 | [ ]       |
| iu á.lucene.analyzers-smartcn                          | á.lucene.analyzers-smartcn:8.4.1.v20221112-0806                | [x]       |
|                                                        | á.lucene.analyzers-smartcn:8.4.1.v20200122-1459                | [ ]       |
| iu á.lucene.core                                       | á.lucene.core:8.4.1.v20221112-0806                             | [x]       |
|                                                        | á.lucene.core:8.4.1.v20200122-1459                             | [ ]       |
| iu á.xmlgraphics                                       | á.xmlgraphics:2.7.0.v20221018-0736                             | [x]       |
|                                                        | á.xmlgraphics:2.6.0.v20210409-0748                             | [ ]       |
| iu é.ant.core                                          | é.ant.core:3.6.500.v20220718-1722                              | [x]       |
|                                                        | é.ant.core:3.6.400.v20220317-1003                              | [ ]       |
| iu é.compare                                           | é.compare:3.8.500.v20220812-1406                               | [x]       |
|                                                        | é.compare:3.8.400.v20220420-1133                               | [ ]       |
| iu é.compare.core                                      | é.compare.core:3.7.100.v20220812-1406                          | [x]       |
|                                                        | é.compare.core:3.7.0.v20220513-0551                            | [ ]       |
| iu é.core.commands                                     | é.core.commands:3.10.300.v20221024-2137                        | [x]       |
|                                                        | é.core.commands:3.10.200.v20220512-0851                        | [ ]       |
| iu é.core.contenttype                                  | é.core.contenttype:3.8.200.v20220817-1539                      | [x]       |
|                                                        | é.core.contenttype:3.8.100.v20210910-0640                      | [ ]       |
| iu é.core.databinding                                  | é.core.databinding:1.11.200.v20221024-2137                     | [x]       |
|                                                        | é.core.databinding:1.11.0.v20220118-1028                       | [ ]       |
| iu é.core.databinding.beans                            | é.core.databinding.beans:1.9.0.v20221024-2137                  | [x]       |
|                                                        | é.core.databinding.beans:1.8.0.v20210619-1111                  | [ ]       |
| iu é.core.databinding.observable                       | é.core.databinding.observable:1.12.100.v20221024-2137          | [x]       |
|                                                        | é.core.databinding.observable:1.12.0.v20211231-1006            | [ ]       |
| iu é.core.databinding.property                         | é.core.databinding.property:1.9.100.v20221024-2137             | [x]       |
|                                                        | é.core.databinding.property:1.9.0.v20210619-1129               | [ ]       |
| iu é.core.expressions                                  | é.core.expressions:3.8.200.v20220613-1047                      | [x]       |
|                                                        | é.core.expressions:3.8.100.v20210910-0640                      | [ ]       |
| iu é.core.externaltools                                | é.core.externaltools:1.2.300.v20220618-1805                    | [x]       |
|                                                        | é.core.externaltools:1.2.200.v20220125-2302                    | [ ]       |
| iu é.core.filebuffers                                  | é.core.filebuffers:3.7.200.v20220202-1008                      | [x]       |
|                                                        | é.core.filebuffers:3.7.200.v20220202-1008                      | [ ]       |
| iu é.core.filesystem                                   | é.core.filesystem:1.9.500.v20220817-1539                       | [x]       |
|                                                        | é.core.filesystem:1.9.400.v20220419-0658                       | [ ]       |
| iu é.core.jobs                                         | é.core.jobs:3.13.200.v20221102-1024                            | [x]       |
|                                                        | é.core.jobs:3.13.0.v20220512-1935                              | [ ]       |
| iu é.core.net                                          | é.core.net:1.4.0.v20220813-1037                                | [x]       |
|                                                        | é.core.net:1.3.1200.v20220312-1450                             | [ ]       |
| iu é.core.resources                                    | é.core.resources:3.18.100.v20221025-2047                       | [x]       |
|                                                        | é.core.resources:3.17.0.v20220517-0751                         | [ ]       |
| iu é.core.runtime                                      | é.core.runtime:3.26.100.v20221021-0005                         | [x]       |
|                                                        | é.core.runtime:3.25.0.v20220506-1157                           | [ ]       |
| iu é.core.variables                                    | é.core.variables:3.5.100.v20210721-1355                        | [x]       |
|                                                        | é.core.variables:3.5.100.v20210721-1355                        | [ ]       |
| iu é.debug.core                                        | é.debug.core:3.20.0.v20220811-0741                             | [x]       |
|                                                        | é.debug.core:3.19.100.v20220324-0630                           | [ ]       |
| iu é.debug.ui                                          | é.debug.ui:3.17.100.v20220926-1344                             | [x]       |
|                                                        | é.debug.ui:3.16.100.v20220526-0826                             | [ ]       |
| iu é.debug.ui.launchview                               | é.debug.ui.launchview:1.0.300.v20220811-0741                   | [x]       |
|                                                        | é.debug.ui.launchview:1.0.200.v20220308-0315                   | [ ]       |
| iu é.e4.core.commands                                  | é.e4.core.commands:1.0.300.v20221024-2137                      | [x]       |
|                                                        | é.e4.core.commands:1.0.100.v20211204-1536                      | [ ]       |
| iu é.e4.core.contexts                                  | é.e4.core.contexts:1.11.0.v20220716-0839                       | [x]       |
|                                                        | é.e4.core.contexts:1.10.0.v20220430-0424                       | [ ]       |
| iu é.e4.core.di                                        | é.e4.core.di:1.8.300.v20220817-1539                            | [x]       |
|                                                        | é.e4.core.di:1.8.200.v20220512-1957                            | [ ]       |
| iu é.e4.core.di.annotations                            | é.e4.core.di.annotations:1.7.200.v20220613-1008                | [x]       |
|                                                        | é.e4.core.di.annotations:1.7.100.v20210910-0640                | [ ]       |
| iu é.e4.core.di.extensions                             | é.e4.core.di.extensions:0.17.200.v20220613-1008                | [x]       |
|                                                        | é.e4.core.di.extensions:0.17.100.v20210910-0640                | [ ]       |
| iu é.e4.core.di.extensions.supplier                    | é.e4.core.di.extensions.supplier:0.16.400.v20220613-1047       | [x]       |
|                                                        | é.e4.core.di.extensions.supplier:0.16.300.v20220503-2248       | [ ]       |
| iu é.e4.core.services                                  | é.e4.core.services:2.3.400.v20220915-1347                      | [x]       |
|                                                        | é.e4.core.services:2.3.200.v20220513-1235                      | [ ]       |
| iu é.e4.emf.xpath                                      | é.e4.emf.xpath:0.3.100.v20221024-2137                          | [x]       |
|                                                        | é.e4.emf.xpath:0.3.0.v20210722-1426                            | [ ]       |
| iu é.e4.rcp.feature.group                              | é.e4.rcp.feature.group:4.26.0.v20221123-2302                   | [x]       |
|                                                        | é.e4.rcp.feature.group:4.24.0.v20220530-1036                   | [ ]       |
| iu é.e4.ui.bindings                                    | é.e4.ui.bindings:0.13.200.v20221024-2137                       | [x]       |
|                                                        | é.e4.ui.bindings:0.13.100.v20210722-1426                       | [ ]       |
| iu é.e4.ui.css.core                                    | é.e4.ui.css.core:0.13.400.v20221024-2137                       | [x]       |
|                                                        | é.e4.ui.css.core:0.13.200.v20211022-1402                       | [ ]       |
| iu é.e4.ui.css.swt                                     | é.e4.ui.css.swt:0.14.700.v20221102-0748                        | [x]       |
|                                                        | é.e4.ui.css.swt:0.14.500.v20220511-1639                        | [ ]       |
| iu é.e4.ui.css.swt.theme                               | é.e4.ui.css.swt.theme:0.13.200.v20221024-2137                  | [x]       |
|                                                        | é.e4.ui.css.swt.theme:0.13.100.v20220310-1056                  | [ ]       |
| iu é.e4.ui.di                                          | é.e4.ui.di:1.4.100.v20221024-2137                              | [x]       |
|                                                        | é.e4.ui.di:1.4.0.v20210621-1133                                | [ ]       |
| iu é.e4.ui.dialogs                                     | é.e4.ui.dialogs:1.3.400.v20221024-2137                         | [x]       |
|                                                        | é.e4.ui.dialogs:1.3.200.v20211210-1500                         | [ ]       |
| iu é.e4.ui.ide                                         | é.e4.ui.ide:3.16.200.v20221024-2137                            | [x]       |
|                                                        | é.e4.ui.ide:3.16.100.v20220310-1350                            | [ ]       |
| iu é.e4.ui.model.workbench                             | é.e4.ui.model.workbench:2.2.300.v20221024-2137                 | [x]       |
|                                                        | é.e4.ui.model.workbench:2.2.100.v20220331-0744                 | [ ]       |
| iu é.e4.ui.services                                    | é.e4.ui.services:1.5.100.v20221024-2137                        | [x]       |
|                                                        | é.e4.ui.services:1.5.0.v20210115-1333                          | [ ]       |
| iu é.e4.ui.widgets                                     | é.e4.ui.widgets:1.3.100.v20221024-2137                         | [x]       |
|                                                        | é.e4.ui.widgets:1.3.0.v20210621-1136                           | [ ]       |
| iu é.e4.ui.workbench                                   | é.e4.ui.workbench:1.14.0.v20221024-2137                        | [x]       |
|                                                        | é.e4.ui.workbench:1.13.100.v20211019-0756                      | [ ]       |
| iu é.e4.ui.workbench.addons.swt                        | é.e4.ui.workbench.addons.swt:1.4.500.v20221028-0801            | [x]       |
|                                                        | é.e4.ui.workbench.addons.swt:1.4.400.v20211102-0453            | [ ]       |
| iu é.e4.ui.workbench.renderers.swt                     | é.e4.ui.workbench.renderers.swt:0.15.700.v20221024-2137        | [x]       |
|                                                        | é.e4.ui.workbench.renderers.swt:0.15.500.v20220511-1638        | [ ]       |
| iu é.e4.ui.workbench.swt                               | é.e4.ui.workbench.swt:0.16.700.v20221024-2137                  | [x]       |
|                                                        | é.e4.ui.workbench.swt:0.16.500.v20220506-1520                  | [ ]       |
| iu é.e4.ui.workbench3                                  | é.e4.ui.workbench3:0.16.100.v20221024-2137                     | [x]       |
|                                                        | é.e4.ui.workbench3:0.16.0.v20210619-0956                       | [ ]       |
| iu é.ecf                                               | é.ecf:3.10.0.v20210925-0032                                    | [x]       |
|                                                        | é.ecf:3.10.0.v20210925-0032                                    | [ ]       |
| iu é.ecf.core.§§                                       | é.ecf.core.§§:1.6.1.v20211005-1944                             | [x]       |
|                                                        | é.ecf.core.§§:1.6.1.v20211005-1944                             | [ ]       |
| iu é.ecf.core.ssl.§§                                   | é.ecf.core.ssl.§§:1.1.501.v20210409-2301                       | [x]       |
|                                                        | é.ecf.core.ssl.§§:1.1.501.v20210409-2301                       | [ ]       |
| iu é.ecf.filetransfer                                  | é.ecf.filetransfer:5.1.102.v20210409-2301                      | [x]       |
|                                                        | é.ecf.filetransfer:5.1.102.v20210409-2301                      | [ ]       |
| iu é.ecf.filetransfer.§§                               | é.ecf.filetransfer.§§:3.14.1800.v20220215-0126                 | [x]       |
|                                                        | é.ecf.filetransfer.§§:3.14.1800.v20220215-0126                 | [ ]       |
| iu é.ecf.filetransfer.httpclient5.§§                   | é.ecf.filetransfer.httpclient5.§§:1.1.701.v20221112-0806       | [x]       |
|                                                        | é.ecf.filetransfer.httpclient5.§§:1.1.600.v20220215-0126       | [ ]       |
| iu é.ecf.filetransfer.ssl.§§                           | é.ecf.filetransfer.ssl.§§:1.1.401.v20210409-2301               | [x]       |
|                                                        | é.ecf.filetransfer.ssl.§§:1.1.401.v20210409-2301               | [ ]       |
| iu é.ecf.identity                                      | é.ecf.identity:3.9.402.v20210409-2301                          | [x]       |
|                                                        | é.ecf.identity:3.9.402.v20210409-2301                          | [ ]       |
| iu é.ecf.provider.filetransfer                         | é.ecf.provider.filetransfer:3.2.800.v20220215-0126             | [x]       |
|                                                        | é.ecf.provider.filetransfer:3.2.800.v20220215-0126             | [ ]       |
| iu é.ecf.provider.filetransfer.httpclient5             | é.ecf.provider.filetransfer.httpclient5:1.0.401.v20221105-0315 | [x]       |
|                                                        | é.ecf.provider.filetransfer.httpclient5:1.0.300.v20220215-0126 | [ ]       |
| iu é.ecf.provider.filetransfer.ssl                     | é.ecf.provider.filetransfer.ssl:1.0.201.v20210409-2301         | [x]       |
|                                                        | é.ecf.provider.filetransfer.ssl:1.0.201.v20210409-2301         | [ ]       |
| iu é.ecf.ssl                                           | é.ecf.ssl:1.2.401.v20210409-2301                               | [x]       |
|                                                        | é.ecf.ssl:1.2.401.v20210409-2301                               | [ ]       |
| iu é.emf.common                                        | é.emf.common:2.27.0.v20221121-1400                             | [x]       |
|                                                        | é.emf.common:2.25.0.v20220325-0806                             | [ ]       |
| iu é.emf.common.feature.group                          | é.emf.common.feature.group:2.28.0.v20221121-1400               | [x]       |
|                                                        | é.emf.common.feature.group:2.26.0.v20220325-0806               | [ ]       |
| iu é.emf.ecore                                         | é.emf.ecore:2.29.0.v20221121-1400                              | [x]       |
|                                                        | é.emf.ecore:2.27.0.v20220426-0617                              | [ ]       |
| iu é.emf.ecore.change                                  | é.emf.ecore.change:2.14.0.v20190528-0725                       | [x]       |
|                                                        | é.emf.ecore.change:2.14.0.v20190528-0725                       | [ ]       |
| iu é.emf.ecore.feature.group                           | é.emf.ecore.feature.group:2.30.0.v20221121-1400                | [x]       |
|                                                        | é.emf.ecore.feature.group:2.28.0.v20220426-0617                | [ ]       |
| iu é.emf.ecore.xmi                                     | é.emf.ecore.xmi:2.17.0.v20220817-1334                          | [x]       |
|                                                        | é.emf.ecore.xmi:2.16.0.v20190528-0725                          | [ ]       |
| iu ë.app                                               | ë.app:1.6.200.v20220720-2012                                   | [x]       |
|                                                        | ë.app:1.6.100.v20211021-1418                                   | [ ]       |
| iu ë.bidi                                              | ë.bidi:1.4.200.v20220710-1223                                  | [x]       |
|                                                        | ë.bidi:1.4.100.v20211021-1418                                  | [ ]       |
| iu ë.common                                            | ë.common:3.17.0.v20221108-1156                                 | [x]       |
|                                                        | ë.common:3.16.100.v20220315-2327                               | [ ]       |
| iu ë.concurrent                                        | ë.concurrent:1.2.100.v20211021-1418                            | [x]       |
|                                                        | ë.concurrent:1.2.100.v20211021-1418                            | [ ]       |
| iu ë.console                                           | ë.console:1.4.500.v20211021-1418                               | [x]       |
|                                                        | ë.console:1.4.500.v20211021-1418                               | [ ]       |
| iu ë.event                                             | ë.event:1.6.100.v20211021-1418                                 | [x]       |
|                                                        | ë.event:1.6.100.v20211021-1418                                 | [ ]       |
| iu ë.frameworkadmin                                    | ë.frameworkadmin:2.2.100.v20220817-1208                        | [x]       |
|                                                        | ë.frameworkadmin:2.2.0.v20210315-2042                          | [ ]       |
| iu ë.frameworkadmin.equinox                            | ë.frameworkadmin.equinox:1.2.200.v20220315-2155                | [x]       |
|                                                        | ë.frameworkadmin.equinox:1.2.200.v20220315-2155                | [ ]       |
| iu ë.http.jetty                                        | ë.http.jetty:3.8.200.v20221109-0702                            | [x]       |
|                                                        | ë.http.jetty:3.8.100.v20211021-1418                            | [ ]       |
| iu ë.http.registry                                     | ë.http.registry:1.3.200.v20220720-2012                         | [x]       |
|                                                        | ë.http.registry:1.3.100.v20211021-1418                         | [ ]       |
| iu ë.http.servlet                                      | ë.http.servlet:1.7.400.v20221006-1531                          | [x]       |
|                                                        | ë.http.servlet:1.7.200.v20211021-1418                          | [ ]       |
| iu ë.jsp.jasper                                        | ë.jsp.jasper:1.1.700.v20220801-1124                            | [x]       |
|                                                        | ë.jsp.jasper:1.1.600.v20211021-1418                            | [ ]       |
| iu ë.jsp.jasper.registry                               | ë.jsp.jasper.registry:1.2.100.v20211021-1418                   | [x]       |
|                                                        | ë.jsp.jasper.registry:1.2.100.v20211021-1418                   | [ ]       |
| iu ë.launcher                                          | ë.launcher:1.6.400.v20210924-0641                              | [x]       |
|                                                        | ë.launcher:1.6.400.v20210924-0641                              | [ ]       |
| iu ë.p2.artifact.repository                            | ë.p2.artifact.repository:1.4.600.v20221106-1146                | [x]       |
|                                                        | ë.p2.artifact.repository:1.4.500.v20220420-1427                | [ ]       |
| iu ë.p2.console                                        | ë.p2.console:1.2.100.v20221111-1340                            | [x]       |
|                                                        | ë.p2.console:1.2.0.v20210315-2042                              | [ ]       |
| iu ë.p2.core                                           | ë.p2.core:2.9.200.v20220817-1208                               | [x]       |
|                                                        | ë.p2.core:2.9.100.v20220310-1733                               | [ ]       |
| iu ë.p2.core.§§                                        | ë.p2.core.§§:1.6.1600.v20221117-1044                           | [x]       |
|                                                        | ë.p2.core.§§:1.6.1400.v20220518-1326                           | [ ]       |
| iu ë.p2.director                                       | ë.p2.director:2.5.400.v20220817-1208                           | [x]       |
|                                                        | ë.p2.director:2.5.300.v20220421-0708                           | [ ]       |
| iu ë.p2.director.app                                   | ë.p2.director.app:1.2.300.v20220911-2007                       | [x]       |
|                                                        | ë.p2.director.app:1.2.100.v20211220-1825                       | [ ]       |
| iu ë.p2.directorywatcher                               | ë.p2.directorywatcher:1.3.100.v20221111-1340                   | [x]       |
|                                                        | ë.p2.directorywatcher:1.3.0.v20210316-1209                     | [ ]       |
| iu ë.p2.engine                                         | ë.p2.engine:2.7.500.v20220817-1208                             | [x]       |
|                                                        | ë.p2.engine:2.7.400.v20220329-1456                             | [ ]       |
| iu ë.p2.extensionlocation                              | ë.p2.extensionlocation:1.4.100.v20220213-0541                  | [x]       |
|                                                        | ë.p2.extensionlocation:1.4.100.v20220213-0541                  | [ ]       |
| iu ë.p2.extras.§§                                      | ë.p2.extras.§§:1.4.1800.v20221117-1044                         | [x]       |
|                                                        | ë.p2.extras.§§:1.4.1600.v20220518-1326                         | [ ]       |
| iu ë.p2.garbagecollector                               | ë.p2.garbagecollector:1.2.100.v20221111-1340                   | [x]       |
|                                                        | ë.p2.garbagecollector:1.2.0.v20210316-1209                     | [ ]       |
| iu ë.p2.jarprocessor                                   | ë.p2.jarprocessor:1.2.300.v20220420-1427                       | [x]       |
|                                                        | ë.p2.jarprocessor:1.2.300.v20220420-1427                       | [ ]       |
| iu ë.p2.metadata                                       | ë.p2.metadata:2.6.300.v20220817-1208                           | [x]       |
|                                                        | ë.p2.metadata:2.6.200.v20220324-1313                           | [ ]       |
| iu ë.p2.metadata.repository                            | ë.p2.metadata.repository:1.4.100.v20220329-1456                | [x]       |
|                                                        | ë.p2.metadata.repository:1.4.100.v20220329-1456                | [ ]       |
| iu ë.p2.operations                                     | ë.p2.operations:2.6.100.v20220817-1208                         | [x]       |
|                                                        | ë.p2.operations:2.6.0.v20210315-2228                           | [ ]       |
| iu ë.p2.publisher                                      | ë.p2.publisher:1.7.200.v20220805-0804                          | [x]       |
|                                                        | ë.p2.publisher:1.7.100.v20220420-1427                          | [ ]       |
| iu ë.p2.publisher.eclipse                              | ë.p2.publisher.eclipse:1.4.200.v20221007-0636                  | [x]       |
|                                                        | ë.p2.publisher.eclipse:1.4.100.v20220420-1427                  | [ ]       |
| iu ë.p2.rcp.§§                                         | ë.p2.rcp.§§:1.4.1800.v20221117-1044                            | [x]       |
|                                                        | ë.p2.rcp.§§:1.4.1600.v20220518-1326                            | [ ]       |
| iu ë.p2.reconciler.dropins                             | ë.p2.reconciler.dropins:1.4.200.v20220911-2007                 | [x]       |
|                                                        | ë.p2.reconciler.dropins:1.4.100.v20211217-1131                 | [ ]       |
| iu ë.p2.repository                                     | ë.p2.repository:2.6.300.v20221030-1923                         | [x]       |
|                                                        | ë.p2.repository:2.6.100.v20220422-1806                         | [ ]       |
| iu ë.p2.repository.tools                               | ë.p2.repository.tools:2.3.200.v20221108-1232                   | [x]       |
|                                                        | ë.p2.repository.tools:2.3.100.v20220504-1755                   | [ ]       |
| iu ë.p2.touchpoint.eclipse                             | ë.p2.touchpoint.eclipse:2.3.300.v20220817-1208                 | [x]       |
|                                                        | ë.p2.touchpoint.eclipse:2.3.200.v20220503-2330                 | [ ]       |
| iu ë.p2.touchpoint.natives                             | ë.p2.touchpoint.natives:1.4.400.v20220506-1821                 | [x]       |
|                                                        | ë.p2.touchpoint.natives:1.4.400.v20220506-1821                 | [ ]       |
| iu ë.p2.transport.ecf                                  | ë.p2.transport.ecf:1.3.300.v20220512-1321                      | [x]       |
|                                                        | ë.p2.transport.ecf:1.3.300.v20220512-1321                      | [ ]       |
| iu ë.p2.ui                                             | ë.p2.ui:2.7.700.v20221015-0933                                 | [x]       |
|                                                        | ë.p2.ui:2.7.500.v20220423-1604                                 | [ ]       |
| iu ë.p2.ui.importexport                                | ë.p2.ui.importexport:1.3.300.v20220329-1456                    | [x]       |
|                                                        | ë.p2.ui.importexport:1.3.300.v20220329-1456                    | [ ]       |
| iu ë.p2.ui.sdk                                         | ë.p2.ui.sdk:1.2.100.v20220814-1551                             | [x]       |
|                                                        | ë.p2.ui.sdk:1.2.4.v20220213-1624                               | [ ]       |
| iu ë.p2.ui.sdk.scheduler                               | ë.p2.ui.sdk.scheduler:1.5.400.v20220805-0804                   | [x]       |
|                                                        | ë.p2.ui.sdk.scheduler:1.5.300.v20211214-1242                   | [ ]       |
| iu ë.p2.updatechecker                                  | ë.p2.updatechecker:1.3.100.v20221111-1340                      | [x]       |
|                                                        | ë.p2.updatechecker:1.3.0.v20210315-2228                        | [ ]       |
| iu ë.p2.updatesite                                     | ë.p2.updatesite:1.2.300.v20220420-1427                         | [x]       |
|                                                        | ë.p2.updatesite:1.2.300.v20220420-1427                         | [ ]       |
| iu ë.p2.user.ui.feature.group                          | ë.p2.user.ui.feature.group:2.4.1800.v20221117-1044             | [x]       |
|                                                        | ë.p2.user.ui.feature.group:2.4.1600.v20220518-1326             | [ ]       |
| iu ë.preferences                                       | ë.preferences:3.10.100.v20220710-1223                          | [x]       |
|                                                        | ë.preferences:3.10.0.v20220503-1634                            | [ ]       |
| iu ë.registry                                          | ë.registry:3.11.200.v20220817-1601                             | [x]       |
|                                                        | ë.registry:3.11.100.v20211021-1418                             | [ ]       |
| iu ë.security                                          | ë.security:1.3.1000.v20220801-1135                             | [x]       |
|                                                        | ë.security:1.3.900.v20220108-1321                              | [ ]       |
| iu ë.security.ui                                       | ë.security.ui:1.3.400.v20221007-1815                           | [x]       |
|                                                        | ë.security.ui:1.3.200.v20220115-0654                           | [ ]       |
| iu ë.simpleconfigurator                                | ë.simpleconfigurator:1.4.200.v20221111-1340                    | [x]       |
|                                                        | ë.simpleconfigurator:1.4.0.v20210315-2228                      | [ ]       |
| iu ë.simpleconfigurator.manipulator                    | ë.simpleconfigurator.manipulator:2.2.100.v20221117-1044        | [x]       |
|                                                        | ë.simpleconfigurator.manipulator:2.2.0.v20210315-2228          | [ ]       |
| iu é.help                                              | é.help:3.9.100.v20210721-0601                                  | [x]       |
|                                                        | é.help:3.9.100.v20210721-0601                                  | [ ]       |
| iu é.help.base                                         | é.help.base:4.3.900.v20221123-1800                             | [x]       |
|                                                        | é.help.base:4.3.700.v20220607-0700                             | [ ]       |
| iu é.help.feature.group                                | é.help.feature.group:2.3.1200.v20221123-1800                   | [x]       |
|                                                        | é.help.feature.group:2.3.1000.v20220607-0700                   | [ ]       |
| iu é.help.ui                                           | é.help.ui:4.4.100.v20220619-1918                               | [x]       |
|                                                        | é.help.ui:4.4.0.v20220411-0938                                 | [ ]       |
| iu é.help.webapp                                       | é.help.webapp:3.10.900.v20221103-0933                          | [x]       |
|                                                        | é.help.webapp:3.10.700.v20220510-1941                          | [ ]       |
| iu é.jetty.http                                        | é.jetty.http:10.0.12                                           | [x]       |
|                                                        | é.jetty.http:10.0.9                                            | [ ]       |
| iu é.jetty.io                                          | é.jetty.io:10.0.12                                             | [x]       |
|                                                        | é.jetty.io:10.0.9                                              | [ ]       |
| iu é.jetty.security                                    | é.jetty.security:10.0.12                                       | [x]       |
|                                                        | é.jetty.security:10.0.9                                        | [ ]       |
| iu é.jetty.server                                      | é.jetty.server:10.0.12                                         | [x]       |
|                                                        | é.jetty.server:10.0.9                                          | [ ]       |
| iu é.jetty.servlet                                     | é.jetty.servlet:10.0.12                                        | [x]       |
|                                                        | é.jetty.servlet:10.0.9                                         | [ ]       |
| iu é.jetty.util                                        | é.jetty.util:10.0.12                                           | [x]       |
|                                                        | é.jetty.util:10.0.9                                            | [ ]       |
| iu é.jetty.util.ajax                                   | é.jetty.util.ajax:10.0.12                                      | [x]       |
|                                                        | é.jetty.util.ajax:10.0.9                                       | [ ]       |
| iu é.jface                                             | é.jface:3.28.0.v20221024-1426                                  | [x]       |
|                                                        | é.jface:3.26.0.v20220513-0449                                  | [ ]       |
| iu é.jface.databinding                                 | é.jface.databinding:1.14.0.v20221024-2137                      | [x]       |
|                                                        | é.jface.databinding:1.13.0.v20210619-1146                      | [ ]       |
| iu é.jface.notifications                               | é.jface.notifications:0.5.100.v20221024-2137                   | [x]       |
|                                                        | é.jface.notifications:0.5.0.v20220401-0716                     | [ ]       |
| iu é.jface.text                                        | é.jface.text:3.22.0.v20221119-1047                             | [x]       |
|                                                        | é.jface.text:3.20.100.v20220516-0819                           | [ ]       |
| iu é.jsch.core                                         | é.jsch.core:1.4.0.v20220813-1037                               | [x]       |
|                                                        | é.jsch.core:1.3.900.v20200422-1935                             | [ ]       |
| iu é.jsch.ui                                           | é.jsch.ui:1.4.200.v20220812-1406                               | [x]       |
|                                                        | é.jsch.ui:1.4.100.v20210917-1201                               | [ ]       |
| iu é.ltk.core.refactoring                              | é.ltk.core.refactoring:3.13.0.v20220822-0502                   | [x]       |
|                                                        | é.ltk.core.refactoring:3.12.200.v20220502-1514                 | [ ]       |
| iu é.ltk.ui.refactoring                                | é.ltk.ui.refactoring:3.12.200.v20220808-2221                   | [x]       |
|                                                        | é.ltk.ui.refactoring:3.12.100.v20220329-1353                   | [ ]       |
| iu é.osgi                                              | é.osgi:3.18.200.v20221116-1324                                 | [x]       |
|                                                        | é.osgi:3.18.0.v20220516-2155                                   | [ ]       |
| iu é.osgi.compatibility.state                          | é.osgi.compatibility.state:1.2.800.v20221116-1440              | [x]       |
|                                                        | é.osgi.compatibility.state:1.2.600.v20220207-1403              | [ ]       |
| iu é.osgi.services                                     | é.osgi.services:3.11.100.v20221006-1531                        | [ ]       |
|                                                        | é.osgi.services:3.10.200.v20210723-0643                        | [x]       |
| iu é.osgi.util                                         | é.osgi.util:3.7.100.v20220615-1601                             | [x]       |
|                                                        | é.osgi.util:3.7.0.v20220427-2144                               | [ ]       |
| iu é.platform                                          | é.platform:4.26.0.v20221123-1800                               | [x]       |
|                                                        | é.platform:4.24.0.v20220607-0700                               | [ ]       |
| iu é.platform.doc.user                                 | é.platform.doc.user:4.26.0.v20221121-0559                      | [x]       |
|                                                        | é.platform.doc.user:4.24.0.v20220530-1438                      | [ ]       |
| iu é.platform.feature.group                            | é.platform.feature.group:4.26.0.v20221123-2302                 | [x]       |
|                                                        | é.platform.feature.group:4.24.0.v20220607-0700                 | [ ]       |
| iu é.platform.ide                                      | é.platform.ide:4.26.0.I20221123-1800                           | [x]       |
|                                                        | é.platform.ide:4.24.0.I20220607-0700                           | [ ]       |
| iu é.platform_root                                     | é.platform_root:4.26.0.v20221123-2302                          | [x]       |
|                                                        | é.platform_root:4.24.0.v20220607-0700                          | [ ]       |
| iu é.rcp                                               | é.rcp:4.26.0.v20221123-1800                                    | [x]       |
|                                                        | é.rcp:4.24.0.v20220607-0700                                    | [ ]       |
| iu é.rcp.feature.group                                 | é.rcp.feature.group:4.26.0.v20221123-2302                      | [x]       |
|                                                        | é.rcp.feature.group:4.24.0.v20220607-0700                      | [ ]       |
| iu é.rcp_root                                          | é.rcp_root:4.26.0.v20221123-2302                               | [x]       |
|                                                        | é.rcp_root:4.24.0.v20220607-0700                               | [ ]       |
| iu é.search                                            | é.search:3.14.300.v20220905-1020                               | [x]       |
|                                                        | é.search:3.14.100.v20220120-1549                               | [ ]       |
| iu é.swt                                               | é.swt:3.122.0.v20221123-2302                                   | [x]       |
|                                                        | é.swt:3.120.0.v20220530-1036                                   | [ ]       |
| iu é.team.core                                         | é.team.core:3.9.600.v20220902-2219                             | [x]       |
|                                                        | é.team.core:3.9.400.v20220511-1440                             | [ ]       |
| iu é.team.genericeditor.diff.extension                 | é.team.genericeditor.diff.extension:1.1.100.v20220812-1406     | [x]       |
|                                                        | é.team.genericeditor.diff.extension:1.1.0.v20210426-0951       | [ ]       |
| iu é.team.ui                                           | é.team.ui:3.9.500.v20220920-2213                               | [x]       |
|                                                        | é.team.ui:3.9.300.v20220420-1133                               | [ ]       |
| iu é.text                                              | é.text:3.12.300.v20220921-1010                                 | [x]       |
|                                                        | é.text:3.12.100.v20220506-1404                                 | [ ]       |
| iu é.text.quicksearch                                  | é.text.quicksearch:1.1.400.v20221023-0941                      | [x]       |
|                                                        | é.text.quicksearch:1.1.300.v20211203-1705                      | [ ]       |
| iu é.ui                                                | é.ui:3.201.200.v20221024-2137                                  | [x]       |
|                                                        | é.ui:3.201.0.v20220124-1108                                    | [ ]       |
| iu é.ui.browser                                        | é.ui.browser:3.7.300.v20221024-2137                            | [x]       |
|                                                        | é.ui.browser:3.7.100.v20211105-1434                            | [ ]       |
| iu é.ui.cheatsheets                                    | é.ui.cheatsheets:3.7.500.v20220903-1020                        | [x]       |
|                                                        | é.ui.cheatsheets:3.7.300.v20210507-0822                        | [ ]       |
| iu é.ui.console                                        | é.ui.console:3.11.400.v20221012-0524                           | [x]       |
|                                                        | é.ui.console:3.11.200.v20220324-0630                           | [ ]       |
| iu é.ui.editors                                        | é.ui.editors:3.14.400.v20220730-1844                           | [x]       |
|                                                        | é.ui.editors:3.14.300.v20210913-0815                           | [ ]       |
| iu é.ui.externaltools                                  | é.ui.externaltools:3.5.200.v20220618-1805                      | [x]       |
|                                                        | é.ui.externaltools:3.5.100.v20210812-1118                      | [ ]       |
| iu é.ui.forms                                          | é.ui.forms:3.11.500.v20221024-2137                             | [x]       |
|                                                        | é.ui.forms:3.11.300.v20211022-1451                             | [ ]       |
| iu é.ui.genericeditor                                  | é.ui.genericeditor:1.2.300.v20220915-0924                      | [x]       |
|                                                        | é.ui.genericeditor:1.2.200.v20211217-1247                      | [ ]       |
| iu é.ui.ide                                            | é.ui.ide:3.20.0.v20221027-2208                                 | [x]       |
|                                                        | é.ui.ide:3.19.0.v20220511-1638                                 | [ ]       |
| iu é.ui.ide.application                                | é.ui.ide.application:1.4.600.v20221024-2137                    | [x]       |
|                                                        | é.ui.ide.application:1.4.400.v20220502-1523                    | [ ]       |
| iu é.ui.intro                                          | é.ui.intro:3.6.600.v20220619-1918                              | [x]       |
|                                                        | é.ui.intro:3.6.500.v20220317-1346                              | [ ]       |
| iu é.ui.intro.quicklinks                               | é.ui.intro.quicklinks:1.1.200.v20220619-1918                   | [x]       |
|                                                        | é.ui.intro.quicklinks:1.1.100.v20210315-0954                   | [ ]       |
| iu é.ui.intro.universal                                | é.ui.intro.universal:3.4.300.v20220619-1918                    | [x]       |
|                                                        | é.ui.intro.universal:3.4.200.v20210409-1747                    | [ ]       |
| iu é.ui.monitoring                                     | é.ui.monitoring:1.2.300.v20221024-2137                         | [x]       |
|                                                        | é.ui.monitoring:1.2.200.v20220429-1040                         | [ ]       |
| iu é.ui.navigator                                      | é.ui.navigator:3.10.400.v20221024-2137                         | [x]       |
|                                                        | é.ui.navigator:3.10.200.v20211009-1706                         | [ ]       |
| iu é.ui.navigator.resources                            | é.ui.navigator.resources:3.8.500.v20221024-2137                | [x]       |
|                                                        | é.ui.navigator.resources:3.8.400.v20220203-1803                | [ ]       |
| iu é.ui.net                                            | é.ui.net:1.4.100.v20220812-1406                                | [x]       |
|                                                        | é.ui.net:1.4.0.v20210426-0838                                  | [ ]       |
| iu é.ui.themes                                         | é.ui.themes:1.2.2100.v20221024-2137                            | [x]       |
|                                                        | é.ui.themes:1.2.1800.v20220316-1102                            | [ ]       |
| iu é.ui.views                                          | é.ui.views:3.11.300.v20221024-2137                             | [x]       |
|                                                        | é.ui.views:3.11.100.v20210816-0811                             | [ ]       |
| iu é.ui.views.log                                      | é.ui.views.log:1.3.400.v20220907-1244                          | [x]       |
|                                                        | é.ui.views.log:1.3.200.v20220310-1555                          | [ ]       |
| iu é.ui.views.properties.tabbed                        | é.ui.views.properties.tabbed:3.9.300.v20221024-2137            | [x]       |
|                                                        | é.ui.views.properties.tabbed:3.9.100.v20201223-1348            | [ ]       |
| iu é.ui.workbench                                      | é.ui.workbench:3.127.0.v20221024-2137                          | [x]       |
|                                                        | é.ui.workbench:3.125.100.v20220524-1304                        | [ ]       |
| iu é.ui.workbench.texteditor                           | é.ui.workbench.texteditor:3.16.600.v20220809-1658              | [x]       |
|                                                        | é.ui.workbench.texteditor:3.16.500.v20220331-0848              | [ ]       |
| iu é.update.configurator                               | é.update.configurator:3.4.1000.v20221114-1512                  | [x]       |
|                                                        | é.update.configurator:3.4.800.v20210415-1314                   | [ ]       |
| iu é.urischeme                                         | é.urischeme:1.2.200.v20221024-2137                             | [x]       |
|                                                        | é.urischeme:1.2.100.v20211001-1648                             | [ ]       |
| iu org.osgi.service.prefs                              | org.osgi.service.prefs:1.1.2.202109301733                      | [x]       |
|                                                        | org.osgi.service.prefs:1.1.2.202109301733                      | [ ]       |
| iu org.osgi.util.function                              | org.osgi.util.function:1.2.0.202109301733                      | [x]       |
|                                                        | org.osgi.util.function:1.2.0.202109301733                      | [ ]       |
| iu org.osgi.util.measurement                           | org.osgi.util.measurement:1.0.2.201802012109                   | [x]       |
|                                                        | org.osgi.util.measurement:1.0.2.201802012109                   | [ ]       |
| iu org.osgi.util.position                              | org.osgi.util.position:1.0.1.201505202026                      | [x]       |
|                                                        | org.osgi.util.position:1.0.1.201505202026                      | [ ]       |
| iu org.osgi.util.promise                               | org.osgi.util.promise:1.2.0.202109301733                       | [x]       |
|                                                        | org.osgi.util.promise:1.2.0.202109301733                       | [ ]       |
| iu org.osgi.util.xml                                   | org.osgi.util.xml:1.0.2.202109301733                           | [x]       |
|                                                        | org.osgi.util.xml:1.0.2.202109301733                           | [ ]       |
| iu org.tukaani.xz                                      | org.tukaani.xz:1.9.0.v20210624-1259                            | [x]       |
|                                                        | org.tukaani.xz:1.9.0                                           | [ ]       |
| iu org.w3c.css.sac                                     | org.w3c.css.sac:1.3.1.v200903091627                            | [x]       |
|                                                        | org.w3c.css.sac:1.3.1.v200903091627                            | [ ]       |
| iu org.w3c.dom.events                                  | org.w3c.dom.events:3.0.0.draft20060413_v201105210656           | [x]       |
|                                                        | org.w3c.dom.events:3.0.0.draft20060413_v201105210656           | [ ]       |
| iu org.w3c.dom.smil                                    | org.w3c.dom.smil:1.0.1.v200903091627                           | [x]       |
|                                                        | org.w3c.dom.smil:1.0.1.v200903091627                           | [ ]       |
| iu org.w3c.dom.svg                                     | org.w3c.dom.svg:1.1.0.v201011041433                            | [x]       |
|                                                        | org.w3c.dom.svg:1.1.0.v201011041433                            | [ ]       |
| iu tooling.osgi.bundle.default                         | tooling.osgi.bundle.default:1.0.0                              | [x]       |
|                                                        | tooling.osgi.bundle.default:1.0.0                              | [ ]       |
| iu tooling.source.default                              | tooling.source.default:1.0.0                                   | [x]       |
|                                                        | tooling.source.default:1.0.0                                   | [ ]       |
| iu toolingë.launcher                                   | toolingë.launcher:1.6.400.v20210924-0641                       | [x]       |
|                                                        | toolingë.launcher:1.6.400.v20210924-0641                       | [ ]       |
| iu toolingé.platform.configuration (!(osgi.os=macosx)) | toolingé.platform.configuration:1.0.0                          | [x]       |
|                                                        | toolingé.platform.configuration:1.0.0                          | [ ]       |
| iu toolingé.platform.ide.application                   | toolingé.platform.ide.application:4.26.0.I20221123-1800        | [x]       |
|                                                        | toolingé.platform.ide.application:4.24.0.I20220607-0700        | [ ]       |
| iu toolingé.platform.ide.configuration                 | toolingé.platform.ide.configuration:4.26.0.I20221123-1800      | [x]       |
|                                                        | toolingé.platform.ide.configuration:4.24.0.I20220607-0700      | [ ]       |
| bundle com.google.gson                                 | com.google.gson:2.9.1                                          | [x]       |
|                                                        | com.google.gson:2.8.9.v20220111-1409                           | [ ]       |
| bundle com.jcraft.jsch                                 | com.jcraft.jsch:0.1.55.v20221112-0806                          | [x]       |
|                                                        | com.jcraft.jsch:0.1.55.v20190404-1902                          | [ ]       |
| bundle com.sun.jna                                     | com.sun.jna:5.12.1                                             | [x]       |
|                                                        | com.sun.jna:5.8.0.v20210503-0343                               | [ ]       |
| bundle com.sun.jna.platform                            | com.sun.jna.platform:5.12.1                                    | [x]       |
|                                                        | com.sun.jna.platform:5.8.0.v20210406-1004                      | [ ]       |
| bundle javax.annotation                                | javax.annotation:1.3.5.v20221112-0806                          | [x]       |
|                                                        | javax.annotation:1.3.5.v20200909-1856                          | [ ]       |
| bundle javax.inject                                    | javax.inject:1.0.0.v20220405-0441                              | [x]       |
|                                                        | javax.inject:1.0.0.v20220405-0441                              | [ ]       |
| bundle á.ant                                           | á.ant:1.10.12.v20211102-1452                                   | [x]       |
|                                                        | á.ant:1.10.12.v20211102-1452                                   | [ ]       |
| bundle á.batik.constants                               | á.batik.constants:1.16.0.v20221027-0840                        | [x]       |
|                                                        | á.batik.constants:1.14.0.v20210324-0332                        | [ ]       |
| bundle á.batik.css                                     | á.batik.css:1.16.0.v20221027-0840                              | [x]       |
|                                                        | á.batik.css:1.14.0.v20210324-0332                              | [ ]       |
| bundle á.batik.util                                    | á.batik.util:1.16.0.v20221027-0840                             | [x]       |
|                                                        | á.batik.util:1.14.0.v20210324-0332                             | [ ]       |
| bundle á.commons.jxpath                                | á.commons.jxpath:1.3.0.v200911051830                           | [x]       |
|                                                        | á.commons.jxpath:1.3.0.v200911051830                           | [ ]       |
| bundle á.lucene.analyzers-common                       | á.lucene.analyzers-common:8.4.1.v20221112-0806                 | [x]       |
|                                                        | á.lucene.analyzers-common:8.4.1.v20200122-1459                 | [ ]       |
| bundle á.lucene.analyzers-smartcn                      | á.lucene.analyzers-smartcn:8.4.1.v20221112-0806                | [x]       |
|                                                        | á.lucene.analyzers-smartcn:8.4.1.v20200122-1459                | [ ]       |
| bundle á.lucene.core                                   | á.lucene.core:8.4.1.v20221112-0806                             | [x]       |
|                                                        | á.lucene.core:8.4.1.v20200122-1459                             | [ ]       |
| bundle é.compare                                       | é.compare:3.8.500.v20220812-1406                               | [x]       |
|                                                        | é.compare:3.8.400.v20220420-1133                               | [ ]       |
| bundle é.compare.core                                  | é.compare.core:3.7.100.v20220812-1406                          | [x]       |
|                                                        | é.compare.core:3.7.0.v20220513-0551                            | [ ]       |
| bundle é.core.commands                                 | é.core.commands:3.10.300.v20221024-2137                        | [x]       |
|                                                        | é.core.commands:3.10.200.v20220512-0851                        | [ ]       |
| bundle é.core.contenttype                              | é.core.contenttype:3.8.200.v20220817-1539                      | [x]       |
|                                                        | é.core.contenttype:3.8.100.v20210910-0640                      | [ ]       |
| bundle é.core.databinding                              | é.core.databinding:1.11.200.v20221024-2137                     | [x]       |
|                                                        | é.core.databinding:1.11.0.v20220118-1028                       | [ ]       |
| bundle é.core.databinding.observable                   | é.core.databinding.observable:1.12.100.v20221024-2137          | [x]       |
|                                                        | é.core.databinding.observable:1.12.0.v20211231-1006            | [ ]       |
| bundle é.core.databinding.property                     | é.core.databinding.property:1.9.100.v20221024-2137             | [x]       |
|                                                        | é.core.databinding.property:1.9.0.v20210619-1129               | [ ]       |
| bundle é.core.expressions                              | é.core.expressions:3.8.200.v20220613-1047                      | [x]       |
|                                                        | é.core.expressions:3.8.100.v20210910-0640                      | [ ]       |
| bundle é.core.externaltools                            | é.core.externaltools:1.2.300.v20220618-1805                    | [x]       |
|                                                        | é.core.externaltools:1.2.200.v20220125-2302                    | [ ]       |
| bundle é.core.filebuffers                              | é.core.filebuffers:3.7.200.v20220202-1008                      | [x]       |
|                                                        | é.core.filebuffers:3.7.200.v20220202-1008                      | [ ]       |
| bundle é.core.filesystem                               | é.core.filesystem:1.9.500.v20220817-1539                       | [x]       |
|                                                        | é.core.filesystem:1.9.400.v20220419-0658                       | [ ]       |
| bundle é.core.jobs                                     | é.core.jobs:3.13.200.v20221102-1024                            | [x]       |
|                                                        | é.core.jobs:3.13.0.v20220512-1935                              | [ ]       |
| bundle é.core.net                                      | é.core.net:1.4.0.v20220813-1037                                | [x]       |
|                                                        | é.core.net:1.3.1200.v20220312-1450                             | [ ]       |
| bundle é.core.resources                                | é.core.resources:3.18.100.v20221025-2047                       | [x]       |
|                                                        | é.core.resources:3.17.0.v20220517-0751                         | [ ]       |
| bundle é.core.runtime                                  | é.core.runtime:3.26.100.v20221021-0005                         | [x]       |
|                                                        | é.core.runtime:3.25.0.v20220506-1157                           | [ ]       |
| bundle é.core.variables                                | é.core.variables:3.5.100.v20210721-1355                        | [x]       |
|                                                        | é.core.variables:3.5.100.v20210721-1355                        | [ ]       |
| bundle é.debug.core                                    | é.debug.core:3.20.0.v20220811-0741                             | [x]       |
|                                                        | é.debug.core:3.19.100.v20220324-0630                           | [ ]       |
| bundle é.debug.ui                                      | é.debug.ui:3.17.100.v20220926-1344                             | [x]       |
|                                                        | é.debug.ui:3.16.100.v20220526-0826                             | [ ]       |
| bundle é.e4.core.commands                              | é.e4.core.commands:1.0.300.v20221024-2137                      | [x]       |
|                                                        | é.e4.core.commands:1.0.100.v20211204-1536                      | [ ]       |
| bundle é.e4.core.contexts                              | é.e4.core.contexts:1.11.0.v20220716-0839                       | [x]       |
|                                                        | é.e4.core.contexts:1.10.0.v20220430-0424                       | [ ]       |
| bundle é.e4.core.di                                    | é.e4.core.di:1.8.300.v20220817-1539                            | [x]       |
|                                                        | é.e4.core.di:1.8.200.v20220512-1957                            | [ ]       |
| bundle é.e4.core.di.annotations                        | é.e4.core.di.annotations:1.7.200.v20220613-1008                | [x]       |
|                                                        | é.e4.core.di.annotations:1.7.100.v20210910-0640                | [ ]       |
| bundle é.e4.core.di.extensions                         | é.e4.core.di.extensions:0.17.200.v20220613-1008                | [x]       |
|                                                        | é.e4.core.di.extensions:0.17.100.v20210910-0640                | [ ]       |
| bundle é.e4.core.services                              | é.e4.core.services:2.3.400.v20220915-1347                      | [x]       |
|                                                        | é.e4.core.services:2.3.200.v20220513-1235                      | [ ]       |
| bundle é.e4.emf.xpath                                  | é.e4.emf.xpath:0.3.100.v20221024-2137                          | [x]       |
|                                                        | é.e4.emf.xpath:0.3.0.v20210722-1426                            | [ ]       |
| bundle é.e4.ui.bindings                                | é.e4.ui.bindings:0.13.200.v20221024-2137                       | [x]       |
|                                                        | é.e4.ui.bindings:0.13.100.v20210722-1426                       | [ ]       |
| bundle é.e4.ui.css.core                                | é.e4.ui.css.core:0.13.400.v20221024-2137                       | [x]       |
|                                                        | é.e4.ui.css.core:0.13.200.v20211022-1402                       | [ ]       |
| bundle é.e4.ui.css.swt                                 | é.e4.ui.css.swt:0.14.700.v20221102-0748                        | [x]       |
|                                                        | é.e4.ui.css.swt:0.14.500.v20220511-1639                        | [ ]       |
| bundle é.e4.ui.css.swt.theme                           | é.e4.ui.css.swt.theme:0.13.200.v20221024-2137                  | [x]       |
|                                                        | é.e4.ui.css.swt.theme:0.13.100.v20220310-1056                  | [ ]       |
| bundle é.e4.ui.di                                      | é.e4.ui.di:1.4.100.v20221024-2137                              | [x]       |
|                                                        | é.e4.ui.di:1.4.0.v20210621-1133                                | [ ]       |
| bundle é.e4.ui.dialogs                                 | é.e4.ui.dialogs:1.3.400.v20221024-2137                         | [x]       |
|                                                        | é.e4.ui.dialogs:1.3.200.v20211210-1500                         | [ ]       |
| bundle é.e4.ui.ide                                     | é.e4.ui.ide:3.16.200.v20221024-2137                            | [x]       |
|                                                        | é.e4.ui.ide:3.16.100.v20220310-1350                            | [ ]       |
| bundle é.e4.ui.model.workbench                         | é.e4.ui.model.workbench:2.2.300.v20221024-2137                 | [x]       |
|                                                        | é.e4.ui.model.workbench:2.2.100.v20220331-0744                 | [ ]       |
| bundle é.e4.ui.services                                | é.e4.ui.services:1.5.100.v20221024-2137                        | [x]       |
|                                                        | é.e4.ui.services:1.5.0.v20210115-1333                          | [ ]       |
| bundle é.e4.ui.widgets                                 | é.e4.ui.widgets:1.3.100.v20221024-2137                         | [x]       |
|                                                        | é.e4.ui.widgets:1.3.0.v20210621-1136                           | [ ]       |
| bundle é.e4.ui.workbench                               | é.e4.ui.workbench:1.14.0.v20221024-2137                        | [x]       |
|                                                        | é.e4.ui.workbench:1.13.100.v20211019-0756                      | [ ]       |
| bundle é.e4.ui.workbench.addons.swt                    | é.e4.ui.workbench.addons.swt:1.4.500.v20221028-0801            | [x]       |
|                                                        | é.e4.ui.workbench.addons.swt:1.4.400.v20211102-0453            | [ ]       |
| bundle é.e4.ui.workbench.renderers.swt                 | é.e4.ui.workbench.renderers.swt:0.15.700.v20221024-2137        | [x]       |
|                                                        | é.e4.ui.workbench.renderers.swt:0.15.500.v20220511-1638        | [ ]       |
| bundle é.e4.ui.workbench.swt                           | é.e4.ui.workbench.swt:0.16.700.v20221024-2137                  | [x]       |
|                                                        | é.e4.ui.workbench.swt:0.16.500.v20220506-1520                  | [ ]       |
| bundle é.e4.ui.workbench3                              | é.e4.ui.workbench3:0.16.100.v20221024-2137                     | [x]       |
|                                                        | é.e4.ui.workbench3:0.16.0.v20210619-0956                       | [ ]       |
| bundle é.ecf                                           | é.ecf:3.10.0.v20210925-0032                                    | [x]       |
|                                                        | é.ecf:3.10.0.v20210925-0032                                    | [ ]       |
| bundle é.ecf.filetransfer                              | é.ecf.filetransfer:5.1.102.v20210409-2301                      | [x]       |
|                                                        | é.ecf.filetransfer:5.1.102.v20210409-2301                      | [ ]       |
| bundle é.ecf.identity                                  | é.ecf.identity:3.9.402.v20210409-2301                          | [x]       |
|                                                        | é.ecf.identity:3.9.402.v20210409-2301                          | [ ]       |
| bundle é.ecf.provider.filetransfer                     | é.ecf.provider.filetransfer:3.2.800.v20220215-0126             | [x]       |
|                                                        | é.ecf.provider.filetransfer:3.2.800.v20220215-0126             | [ ]       |
| bundle é.emf.common                                    | é.emf.common:2.27.0.v20221121-1400                             | [x]       |
|                                                        | é.emf.common:2.25.0.v20220325-0806                             | [ ]       |
| bundle é.emf.ecore                                     | é.emf.ecore:2.29.0.v20221121-1400                              | [x]       |
|                                                        | é.emf.ecore:2.27.0.v20220426-0617                              | [ ]       |
| bundle é.emf.ecore.change                              | é.emf.ecore.change:2.14.0.v20190528-0725                       | [x]       |
|                                                        | é.emf.ecore.change:2.14.0.v20190528-0725                       | [ ]       |
| bundle é.emf.ecore.xmi                                 | é.emf.ecore.xmi:2.17.0.v20220817-1334                          | [x]       |
|                                                        | é.emf.ecore.xmi:2.16.0.v20190528-0725                          | [ ]       |
| bundle ë.app                                           | ë.app:1.6.200.v20220720-2012                                   | [x]       |
|                                                        | ë.app:1.6.100.v20211021-1418                                   | [ ]       |
| bundle ë.bidi                                          | ë.bidi:1.4.200.v20220710-1223                                  | [x]       |
|                                                        | ë.bidi:1.4.100.v20211021-1418                                  | [ ]       |
| bundle ë.common                                        | ë.common:3.17.0.v20221108-1156                                 | [x]       |
|                                                        | ë.common:3.16.100.v20220315-2327                               | [ ]       |
| bundle ë.jsp.jasper.registry                           | ë.jsp.jasper.registry:1.2.100.v20211021-1418                   | [x]       |
|                                                        | ë.jsp.jasper.registry:1.2.100.v20211021-1418                   | [ ]       |
| bundle ë.launcher                                      | ë.launcher:1.6.400.v20210924-0641                              | [x]       |
|                                                        | ë.launcher:1.6.400.v20210924-0641                              | [ ]       |
| bundle ë.p2.artifact.repository                        | ë.p2.artifact.repository:1.4.600.v20221106-1146                | [x]       |
|                                                        | ë.p2.artifact.repository:1.4.500.v20220420-1427                | [ ]       |
| bundle ë.p2.core                                       | ë.p2.core:2.9.200.v20220817-1208                               | [x]       |
|                                                        | ë.p2.core:2.9.100.v20220310-1733                               | [ ]       |
| bundle ë.p2.engine                                     | ë.p2.engine:2.7.500.v20220817-1208                             | [x]       |
|                                                        | ë.p2.engine:2.7.400.v20220329-1456                             | [ ]       |
| bundle ë.p2.extensionlocation                          | ë.p2.extensionlocation:1.4.100.v20220213-0541                  | [x]       |
|                                                        | ë.p2.extensionlocation:1.4.100.v20220213-0541                  | [ ]       |
| bundle ë.p2.metadata                                   | ë.p2.metadata:2.6.300.v20220817-1208                           | [x]       |
|                                                        | ë.p2.metadata:2.6.200.v20220324-1313                           | [ ]       |
| bundle ë.p2.metadata.repository                        | ë.p2.metadata.repository:1.4.100.v20220329-1456                | [x]       |
|                                                        | ë.p2.metadata.repository:1.4.100.v20220329-1456                | [ ]       |
| bundle ë.p2.repository                                 | ë.p2.repository:2.6.300.v20221030-1923                         | [x]       |
|                                                        | ë.p2.repository:2.6.100.v20220422-1806                         | [ ]       |
| bundle ë.p2.touchpoint.eclipse                         | ë.p2.touchpoint.eclipse:2.3.300.v20220817-1208                 | [x]       |
|                                                        | ë.p2.touchpoint.eclipse:2.3.200.v20220503-2330                 | [ ]       |
| bundle ë.p2.ui                                         | ë.p2.ui:2.7.700.v20221015-0933                                 | [x]       |
|                                                        | ë.p2.ui:2.7.500.v20220423-1604                                 | [ ]       |
| bundle ë.p2.updatechecker                              | ë.p2.updatechecker:1.3.100.v20221111-1340                      | [x]       |
|                                                        | ë.p2.updatechecker:1.3.0.v20210315-2228                        | [ ]       |
| bundle ë.preferences                                   | ë.preferences:3.10.100.v20220710-1223                          | [x]       |
|                                                        | ë.preferences:3.10.0.v20220503-1634                            | [ ]       |
| bundle ë.registry                                      | ë.registry:3.11.200.v20220817-1601                             | [x]       |
|                                                        | ë.registry:3.11.100.v20211021-1418                             | [ ]       |
| bundle ë.security                                      | ë.security:1.3.1000.v20220801-1135                             | [x]       |
|                                                        | ë.security:1.3.900.v20220108-1321                              | [ ]       |
| bundle ë.security.ui                                   | ë.security.ui:1.3.400.v20221007-1815                           | [x]       |
|                                                        | ë.security.ui:1.3.200.v20220115-0654                           | [ ]       |
| bundle é.help                                          | é.help:3.9.100.v20210721-0601                                  | [x]       |
|                                                        | é.help:3.9.100.v20210721-0601                                  | [ ]       |
| bundle é.help.base                                     | é.help.base:4.3.900.v20221123-1800                             | [x]       |
|                                                        | é.help.base:4.3.700.v20220607-0700                             | [ ]       |
| bundle é.jface                                         | é.jface:3.28.0.v20221024-1426                                  | [x]       |
|                                                        | é.jface:3.26.0.v20220513-0449                                  | [ ]       |
| bundle é.jface.databinding                             | é.jface.databinding:1.14.0.v20221024-2137                      | [x]       |
|                                                        | é.jface.databinding:1.13.0.v20210619-1146                      | [ ]       |
| bundle é.jface.notifications                           | é.jface.notifications:0.5.100.v20221024-2137                   | [x]       |
|                                                        | é.jface.notifications:0.5.0.v20220401-0716                     | [ ]       |
| bundle é.jface.text                                    | é.jface.text:3.22.0.v20221119-1047                             | [x]       |
|                                                        | é.jface.text:3.20.100.v20220516-0819                           | [ ]       |
| bundle é.jsch.core                                     | é.jsch.core:1.4.0.v20220813-1037                               | [x]       |
|                                                        | é.jsch.core:1.3.900.v20200422-1935                             | [ ]       |
| bundle é.ltk.core.refactoring                          | é.ltk.core.refactoring:3.13.0.v20220822-0502                   | [x]       |
|                                                        | é.ltk.core.refactoring:3.12.200.v20220502-1514                 | [ ]       |
| bundle é.ltk.ui.refactoring                            | é.ltk.ui.refactoring:3.12.200.v20220808-2221                   | [x]       |
|                                                        | é.ltk.ui.refactoring:3.12.100.v20220329-1353                   | [ ]       |
| bundle é.osgi                                          | é.osgi:3.18.200.v20221116-1324                                 | [x]       |
|                                                        | é.osgi:3.18.0.v20220516-2155                                   | [ ]       |
| bundle é.search                                        | é.search:3.14.300.v20220905-1020                               | [x]       |
|                                                        | é.search:3.14.100.v20220120-1549                               | [ ]       |
| bundle é.swt                                           | é.swt:3.122.0.v20221123-2302                                   | [x]       |
|                                                        | é.swt:3.120.0.v20220530-1036                                   | [ ]       |
| bundle é.team.core                                     | é.team.core:3.9.600.v20220902-2219                             | [x]       |
|                                                        | é.team.core:3.9.400.v20220511-1440                             | [ ]       |
| bundle é.team.ui                                       | é.team.ui:3.9.500.v20220920-2213                               | [x]       |
|                                                        | é.team.ui:3.9.300.v20220420-1133                               | [ ]       |
| bundle é.text                                          | é.text:3.12.300.v20220921-1010                                 | [x]       |
|                                                        | é.text:3.12.100.v20220506-1404                                 | [ ]       |
| bundle é.ui                                            | é.ui:3.201.200.v20221024-2137                                  | [x]       |
|                                                        | é.ui:3.201.0.v20220124-1108                                    | [ ]       |
| bundle é.ui.browser                                    | é.ui.browser:3.7.300.v20221024-2137                            | [x]       |
|                                                        | é.ui.browser:3.7.100.v20211105-1434                            | [ ]       |
| bundle é.ui.console                                    | é.ui.console:3.11.400.v20221012-0524                           | [x]       |
|                                                        | é.ui.console:3.11.200.v20220324-0630                           | [ ]       |
| bundle é.ui.editors                                    | é.ui.editors:3.14.400.v20220730-1844                           | [x]       |
|                                                        | é.ui.editors:3.14.300.v20210913-0815                           | [ ]       |
| bundle é.ui.forms                                      | é.ui.forms:3.11.500.v20221024-2137                             | [x]       |
|                                                        | é.ui.forms:3.11.300.v20211022-1451                             | [ ]       |
| bundle é.ui.genericeditor                              | é.ui.genericeditor:1.2.300.v20220915-0924                      | [x]       |
|                                                        | é.ui.genericeditor:1.2.200.v20211217-1247                      | [ ]       |
| bundle é.ui.ide                                        | é.ui.ide:3.20.0.v20221027-2208                                 | [x]       |
|                                                        | é.ui.ide:3.19.0.v20220511-1638                                 | [ ]       |
| bundle é.ui.intro                                      | é.ui.intro:3.6.600.v20220619-1918                              | [x]       |
|                                                        | é.ui.intro:3.6.500.v20220317-1346                              | [ ]       |
| bundle é.ui.navigator                                  | é.ui.navigator:3.10.400.v20221024-2137                         | [x]       |
|                                                        | é.ui.navigator:3.10.200.v20211009-1706                         | [ ]       |
| bundle é.ui.navigator.resources                        | é.ui.navigator.resources:3.8.500.v20221024-2137                | [x]       |
|                                                        | é.ui.navigator.resources:3.8.400.v20220203-1803                | [ ]       |
| bundle é.ui.views                                      | é.ui.views:3.11.300.v20221024-2137                             | [x]       |
|                                                        | é.ui.views:3.11.100.v20210816-0811                             | [ ]       |
| bundle é.ui.views.properties.tabbed                    | é.ui.views.properties.tabbed:3.9.300.v20221024-2137            | [x]       |
|                                                        | é.ui.views.properties.tabbed:3.9.100.v20201223-1348            | [ ]       |
| bundle é.ui.workbench                                  | é.ui.workbench:3.127.0.v20221024-2137                          | [x]       |
|                                                        | é.ui.workbench:3.125.100.v20220524-1304                        | [ ]       |
| bundle é.ui.workbench.texteditor                       | é.ui.workbench.texteditor:3.16.600.v20220809-1658              | [x]       |
|                                                        | é.ui.workbench.texteditor:3.16.500.v20220331-0848              | [ ]       |
| bundle é.unittest.ui                                   | é.unittest.ui:1.0.100.v20210429-0605                           | [x]       |
|                                                        | é.unittest.ui:1.0.100.v20210429-0605                           | [ ]       |
| bundle é.urischeme                                     | é.urischeme:1.2.200.v20221024-2137                             | [x]       |
|                                                        | é.urischeme:1.2.100.v20211001-1648                             | [ ]       |
| bundle org.osgi.service.prefs                          | org.osgi.service.prefs:1.1.2.202109301733                      | [x]       |
|                                                        | org.osgi.service.prefs:1.1.2.202109301733                      | [ ]       |
| bundle org.osgi.util.function                          | org.osgi.util.function:1.2.0.202109301733                      | [x]       |
|                                                        | org.osgi.util.function:1.2.0.202109301733                      | [ ]       |
| bundle org.osgi.util.measurement                       | org.osgi.util.measurement:1.0.2.201802012109                   | [x]       |
|                                                        | org.osgi.util.measurement:1.0.2.201802012109                   | [ ]       |
| bundle org.osgi.util.position                          | org.osgi.util.position:1.0.1.201505202026                      | [x]       |
|                                                        | org.osgi.util.position:1.0.1.201505202026                      | [ ]       |
| bundle org.osgi.util.promise                           | org.osgi.util.promise:1.2.0.202109301733                       | [x]       |
|                                                        | org.osgi.util.promise:1.2.0.202109301733                       | [ ]       |
| bundle org.osgi.util.xml                               | org.osgi.util.xml:1.0.2.202109301733                           | [x]       |
|                                                        | org.osgi.util.xml:1.0.2.202109301733                           | [ ]       |
| bundle org.tukaani.xz                                  | org.tukaani.xz:1.9.0.v20210624-1259                            | [x]       |
|                                                        | org.tukaani.xz:1.9.0                                           | [ ]       |
| bundle org.w3c.css.sac                                 | org.w3c.css.sac:1.3.1.v200903091627                            | [x]       |
|                                                        | org.w3c.css.sac:1.3.1.v200903091627                            | [ ]       |
| bundle org.w3c.dom.smil                                | org.w3c.dom.smil:1.0.1.v200903091627                           | [x]       |
|                                                        | org.w3c.dom.smil:1.0.1.v200903091627                           | [ ]       |
+--------------------------------------------------------+----------------------------------------------------------------+-----------+
§§ feature.feature.group
á org.apache
é org.eclipse
ë org.eclipse.equinox
╔═ _06/batch ═╗
<unit id="org.eclipse.jdt.core.compiler.batch" singleton="false" version="3.32.0.v20221108-1853">
  <update id="org.eclipse.jdt.core.compiler.batch" range="[0.0.0,3.32.0.v20221108-1853)" severity="0"/>
  <properties size="7">
    <property name="org.eclipse.equinox.p2.name" value="Eclipse Compiler for Java(TM)"/>
    <property name="org.eclipse.equinox.p2.provider" value="Eclipse.org"/>
    <property name="maven-groupId" value="org.eclipse.jdt"/>
    <property name="maven-artifactId" value="org.eclipse.jdt.core"/>
    <property name="maven-version" value="3.32.0-SNAPSHOT"/>
    <property name="maven-classifier" value="batch-compiler"/>
    <property name="maven-type" value="eclipse-plugin"/>
  </properties>
  <provides size="25">
    <provided name="org.eclipse.jdt.core.compiler.batch" namespace="org.eclipse.equinox.p2.iu" version="3.32.0.v20221108-1853"/>
    <provided name="org.eclipse.jdt.core.compiler.batch" namespace="osgi.bundle" version="3.32.0.v20221108-1853"/>
    <provided name="org.eclipse.jdt.core" namespace="java.package" version="0.0.0"/>
    <provided name="org.eclipse.jdt.core.compiler" namespace="java.package" version="0.0.0"/>
    <provided name="org.eclipse.jdt.core.compiler.batch" namespace="java.package" version="0.0.0"/>
    <provided name="org.eclipse.jdt.internal.antadapter" namespace="java.package" version="0.0.0"/>
    <provided name="org.eclipse.jdt.internal.compiler" namespace="java.package" version="0.0.0"/>
    <provided name="org.eclipse.jdt.internal.compiler.apt.dispatch" namespace="java.package" version="0.0.0"/>
    <provided name="org.eclipse.jdt.internal.compiler.apt.model" namespace="java.package" version="0.0.0"/>
    <provided name="org.eclipse.jdt.internal.compiler.apt.util" namespace="java.package" version="0.0.0"/>
    <provided name="org.eclipse.jdt.internal.compiler.ast" namespace="java.package" version="0.0.0"/>
    <provided name="org.eclipse.jdt.internal.compiler.batch" namespace="java.package" version="0.0.0"/>
    <provided name="org.eclipse.jdt.internal.compiler.classfmt" namespace="java.package" version="0.0.0"/>
    <provided name="org.eclipse.jdt.internal.compiler.codegen" namespace="java.package" version="0.0.0"/>
    <provided name="org.eclipse.jdt.internal.compiler.env" namespace="java.package" version="0.0.0"/>
    <provided name="org.eclipse.jdt.internal.compiler.flow" namespace="java.package" version="0.0.0"/>
    <provided name="org.eclipse.jdt.internal.compiler.impl" namespace="java.package" version="0.0.0"/>
    <provided name="org.eclipse.jdt.internal.compiler.lookup" namespace="java.package" version="0.0.0"/>
    <provided name="org.eclipse.jdt.internal.compiler.parser" namespace="java.package" version="0.0.0"/>
    <provided name="org.eclipse.jdt.internal.compiler.parser.diagnose" namespace="java.package" version="0.0.0"/>
    <provided name="org.eclipse.jdt.internal.compiler.problem" namespace="java.package" version="0.0.0"/>
    <provided name="org.eclipse.jdt.internal.compiler.util" namespace="java.package" version="0.0.0"/>
    <provided name="org.eclipse.jdt.internal.compiler.tool" namespace="java.package" version="0.0.0"/>
    <provided name="org.eclipse.jdt.core.compiler.batch" namespace="osgi.identity" version="3.32.0.v20221108-1853">
      <properties size="1">
        <property name="type" value="osgi.bundle"/>
      </properties>
    </provided>
    <provided name="bundle" namespace="org.eclipse.equinox.p2.eclipse.type" version="1.0.0"/>
  </provides>
  <artifacts size="1">
    <artifact classifier="osgi.bundle" id="org.eclipse.jdt.core.compiler.batch" version="3.32.0.v20221108-1853"/>
  </artifacts>
  <touchpoint id="org.eclipse.equinox.p2.osgi" version="1.0.0"/>
  <touchpointData size="1">
    <instructions size="1">
      <instruction key="manifest">
            Bundle-SymbolicName: org.eclipse.jdt.core.compiler.batch
Bundle-Version: 3.32.0.v20221108-1853
          </instruction>
    </instructions>
  </touchpointData>
</unit>
╔═ _06_corrosion_cdt_installed ═╗
WARNING!!! 5 unmet requirement(s), 690 ambigous requirement(s).
WARNING!!!  For more info: `gradlew equoList --problems`
62 optional requirement(s) were not installed. For more info: `gradlew equoList --optional`
+---------------------------------------------------------------------------------+------------------------+
| maven coordinate / p2 id                                                        | repo                   |
+---------------------------------------------------------------------------------+------------------------+
| com.ibm.icu:icu4j:72.1                                                          | maven central          |
| commons-io:commons-io:2.11.0                                                    | maven central          |
| jakarta.servlet:jakarta.servlet-api:4.0.4                                       | maven central          |
| javax.servlet.jsp:javax.servlet.jsp-api:2.3.3                                   | maven central          |
| net.java.dev.jna:jna-platform:5.12.1                                            | maven central          |
| net.java.dev.jna:jna:5.12.1                                                     | maven central          |
| org.apache.felix:org.apache.felix.gogo.runtime:1.1.6                            | maven central          |
| org.apache.felix:org.apache.felix.scr:2.2.4                                     | maven central          |
| org.bouncycastle:bcpg-jdk18on:1.72                                              | maven central          |
| org.bouncycastle:bcprov-jdk18on:1.72                                            | maven central          |
| org.eclipse.jetty:jetty-http:10.0.12                                            | maven central          |
| org.eclipse.jetty:jetty-io:10.0.12                                              | maven central          |
| org.eclipse.jetty:jetty-security:10.0.12                                        | maven central          |
| org.eclipse.jetty:jetty-server:10.0.12                                          | maven central          |
| org.eclipse.jetty:jetty-servlet:10.0.12                                         | maven central          |
| org.eclipse.jetty:jetty-util-ajax:10.0.12                                       | maven central          |
| org.eclipse.jetty:jetty-util:10.0.12                                            | maven central          |
| org.osgi:org.osgi.service.cm:1.6.1                                              | maven central          |
| org.osgi:org.osgi.service.component:1.5.0                                       | maven central          |
| org.osgi:org.osgi.service.device:1.1.1                                          | maven central          |
| org.osgi:org.osgi.service.event:1.4.1                                           | maven central          |
| org.osgi:org.osgi.service.metatype:1.4.1                                        | maven central          |
| org.osgi:org.osgi.service.prefs:1.1.2                                           | maven central          |
| org.osgi:org.osgi.service.provisioning:1.2.0                                    | maven central          |
| org.osgi:org.osgi.service.upnp:1.2.1                                            | maven central          |
| org.osgi:org.osgi.service.useradmin:1.1.1                                       | maven central          |
| org.osgi:org.osgi.service.wireadmin:1.0.2                                       | maven central          |
| org.osgi:org.osgi.util.function:1.2.0                                           | maven central          |
| org.osgi:org.osgi.util.measurement:1.0.2                                        | maven central          |
| org.osgi:org.osgi.util.position:1.0.1                                           | maven central          |
| org.osgi:org.osgi.util.promise:1.2.0                                            | maven central          |
| org.osgi:org.osgi.util.xml:1.0.2                                                | maven central          |
| org.ow2.sat4j:org.ow2.sat4j.core:2.3.6                                          | maven central          |
| org.ow2.sat4j:org.ow2.sat4j.pb:2.3.6                                            | maven central          |
| org.eclipse.ecf:org.eclipse.ecf.filetransfer:5.1.102                            | maven central?         |
| org.eclipse.ecf:org.eclipse.ecf.identity:3.9.402                                | maven central?         |
| org.eclipse.ecf:org.eclipse.ecf.provider.filetransfer.httpclient5:1.0.401       | maven central?         |
| org.eclipse.ecf:org.eclipse.ecf.provider.filetransfer.ssl:1.0.201               | maven central?         |
| org.eclipse.ecf:org.eclipse.ecf.provider.filetransfer:3.2.800                   | maven central?         |
| org.eclipse.ecf:org.eclipse.ecf.ssl:1.2.401                                     | maven central?         |
| org.eclipse.ecf:org.eclipse.ecf:3.10.0                                          | maven central?         |
| org.eclipse.emf:org.eclipse.emf.common:2.27.0                                   | maven central?         |
| org.eclipse.emf:org.eclipse.emf.ecore.change:2.14.0                             | maven central?         |
| org.eclipse.emf:org.eclipse.emf.ecore.xmi:2.17.0                                | maven central?         |
| org.eclipse.emf:org.eclipse.emf.ecore:2.29.0                                    | maven central?         |
| org.eclipse.jdt:org.eclipse.jdt.core:3.32.0                                     | maven central?         |
| org.eclipse.platform:org.eclipse.ant.core:3.6.500                               | maven central?         |
| org.eclipse.platform:org.eclipse.compare.core:3.7.100                           | maven central?         |
| org.eclipse.platform:org.eclipse.compare:3.8.500                                | maven central?         |
| org.eclipse.platform:org.eclipse.core.commands:3.10.300                         | maven central?         |
| org.eclipse.platform:org.eclipse.core.contenttype:3.8.200                       | maven central?         |
| org.eclipse.platform:org.eclipse.core.databinding.beans:1.9.0                   | maven central?         |
| org.eclipse.platform:org.eclipse.core.databinding.observable:1.12.100           | maven central?         |
| org.eclipse.platform:org.eclipse.core.databinding.property:1.9.100              | maven central?         |
| org.eclipse.platform:org.eclipse.core.databinding:1.11.200                      | maven central?         |
| org.eclipse.platform:org.eclipse.core.expressions:3.8.200                       | maven central?         |
| org.eclipse.platform:org.eclipse.core.externaltools:1.2.300                     | maven central?         |
| org.eclipse.platform:org.eclipse.core.filebuffers:3.7.200                       | maven central?         |
| org.eclipse.platform:org.eclipse.core.filesystem:1.9.500                        | maven central?         |
| org.eclipse.platform:org.eclipse.core.jobs:3.13.200                             | maven central?         |
| org.eclipse.platform:org.eclipse.core.net:1.4.0                                 | maven central?         |
| org.eclipse.platform:org.eclipse.core.resources:3.18.100                        | maven central?         |
| org.eclipse.platform:org.eclipse.core.runtime:3.26.100                          | maven central?         |
| org.eclipse.platform:org.eclipse.core.variables:3.5.100                         | maven central?         |
| org.eclipse.platform:org.eclipse.debug.core:3.20.0                              | maven central?         |
| org.eclipse.platform:org.eclipse.debug.ui.launchview:1.0.300                    | maven central?         |
| org.eclipse.platform:org.eclipse.debug.ui:3.17.100                              | maven central?         |
| org.eclipse.platform:org.eclipse.e4.core.commands:1.0.300                       | maven central?         |
| org.eclipse.platform:org.eclipse.e4.core.contexts:1.11.0                        | maven central?         |
| org.eclipse.platform:org.eclipse.e4.core.di.annotations:1.7.200                 | maven central?         |
| org.eclipse.platform:org.eclipse.e4.core.di.extensions.supplier:0.16.400        | maven central?         |
| org.eclipse.platform:org.eclipse.e4.core.di.extensions:0.17.200                 | maven central?         |
| org.eclipse.platform:org.eclipse.e4.core.di:1.8.300                             | maven central?         |
| org.eclipse.platform:org.eclipse.e4.core.services:2.3.400                       | maven central?         |
| org.eclipse.platform:org.eclipse.e4.emf.xpath:0.3.100                           | maven central?         |
| org.eclipse.platform:org.eclipse.e4.ui.bindings:0.13.200                        | maven central?         |
| org.eclipse.platform:org.eclipse.e4.ui.css.core:0.13.400                        | maven central?         |
| org.eclipse.platform:org.eclipse.e4.ui.css.swt.theme:0.13.200                   | maven central?         |
| org.eclipse.platform:org.eclipse.e4.ui.css.swt:0.14.700                         | maven central?         |
| org.eclipse.platform:org.eclipse.e4.ui.di:1.4.100                               | maven central?         |
| org.eclipse.platform:org.eclipse.e4.ui.dialogs:1.3.400                          | maven central?         |
| org.eclipse.platform:org.eclipse.e4.ui.ide:3.16.200                             | maven central?         |
| org.eclipse.platform:org.eclipse.e4.ui.model.workbench:2.2.300                  | maven central?         |
| org.eclipse.platform:org.eclipse.e4.ui.progress:0.3.600                         | maven central?         |
| org.eclipse.platform:org.eclipse.e4.ui.services:1.5.100                         | maven central?         |
| org.eclipse.platform:org.eclipse.e4.ui.widgets:1.3.100                          | maven central?         |
| org.eclipse.platform:org.eclipse.e4.ui.workbench.addons.swt:1.4.500             | maven central?         |
| org.eclipse.platform:org.eclipse.e4.ui.workbench.renderers.swt:0.15.700         | maven central?         |
| org.eclipse.platform:org.eclipse.e4.ui.workbench.swt:0.16.700                   | maven central?         |
| org.eclipse.platform:org.eclipse.e4.ui.workbench3:0.16.100                      | maven central?         |
| org.eclipse.platform:org.eclipse.e4.ui.workbench:1.14.0                         | maven central?         |
| org.eclipse.platform:org.eclipse.equinox.app:1.6.200                            | maven central?         |
| org.eclipse.platform:org.eclipse.equinox.bidi:1.4.200                           | maven central?         |
| org.eclipse.platform:org.eclipse.equinox.common:3.17.0                          | maven central?         |
| org.eclipse.platform:org.eclipse.equinox.concurrent:1.2.100                     | maven central?         |
| org.eclipse.platform:org.eclipse.equinox.console:1.4.500                        | maven central?         |
| org.eclipse.platform:org.eclipse.equinox.event:1.6.100                          | maven central?         |
| org.eclipse.platform:org.eclipse.equinox.frameworkadmin.equinox:1.2.200         | maven central?         |
| org.eclipse.platform:org.eclipse.equinox.frameworkadmin:2.2.100                 | maven central?         |
| org.eclipse.platform:org.eclipse.equinox.http.jetty:3.8.200                     | maven central?         |
| org.eclipse.platform:org.eclipse.equinox.http.registry:1.3.200                  | maven central?         |
| org.eclipse.platform:org.eclipse.equinox.http.servlet:1.7.400                   | maven central?         |
| org.eclipse.platform:org.eclipse.equinox.jsp.jasper.registry:1.2.100            | maven central?         |
| org.eclipse.platform:org.eclipse.equinox.jsp.jasper:1.1.700                     | maven central?         |
| org.eclipse.platform:org.eclipse.equinox.launcher:1.6.400                       | maven central?         |
| org.eclipse.platform:org.eclipse.equinox.p2.artifact.repository:1.4.600         | maven central?         |
| org.eclipse.platform:org.eclipse.equinox.p2.console:1.2.100                     | maven central?         |
| org.eclipse.platform:org.eclipse.equinox.p2.core:2.9.200                        | maven central?         |
| org.eclipse.platform:org.eclipse.equinox.p2.director.app:1.2.300                | maven central?         |
| org.eclipse.platform:org.eclipse.equinox.p2.director:2.5.400                    | maven central?         |
| org.eclipse.platform:org.eclipse.equinox.p2.directorywatcher:1.3.100            | maven central?         |
| org.eclipse.platform:org.eclipse.equinox.p2.engine:2.7.500                      | maven central?         |
| org.eclipse.platform:org.eclipse.equinox.p2.extensionlocation:1.4.100           | maven central?         |
| org.eclipse.platform:org.eclipse.equinox.p2.garbagecollector:1.2.100            | maven central?         |
| org.eclipse.platform:org.eclipse.equinox.p2.jarprocessor:1.2.300                | maven central?         |
| org.eclipse.platform:org.eclipse.equinox.p2.metadata.repository:1.4.100         | maven central?         |
| org.eclipse.platform:org.eclipse.equinox.p2.metadata:2.6.300                    | maven central?         |
| org.eclipse.platform:org.eclipse.equinox.p2.operations:2.6.100                  | maven central?         |
| org.eclipse.platform:org.eclipse.equinox.p2.publisher.eclipse:1.4.200           | maven central?         |
| org.eclipse.platform:org.eclipse.equinox.p2.publisher:1.7.200                   | maven central?         |
| org.eclipse.platform:org.eclipse.equinox.p2.reconciler.dropins:1.4.200          | maven central?         |
| org.eclipse.platform:org.eclipse.equinox.p2.repository.tools:2.3.200            | maven central?         |
| org.eclipse.platform:org.eclipse.equinox.p2.repository:2.6.300                  | maven central?         |
| org.eclipse.platform:org.eclipse.equinox.p2.touchpoint.eclipse:2.3.300          | maven central?         |
| org.eclipse.platform:org.eclipse.equinox.p2.touchpoint.natives:1.4.400          | maven central?         |
| org.eclipse.platform:org.eclipse.equinox.p2.transport.ecf:1.3.300               | maven central?         |
| org.eclipse.platform:org.eclipse.equinox.p2.ui.importexport:1.3.300             | maven central?         |
| org.eclipse.platform:org.eclipse.equinox.p2.ui.sdk.scheduler:1.5.400            | maven central?         |
| org.eclipse.platform:org.eclipse.equinox.p2.ui.sdk:1.2.100                      | maven central?         |
| org.eclipse.platform:org.eclipse.equinox.p2.ui:2.7.700                          | maven central?         |
| org.eclipse.platform:org.eclipse.equinox.p2.updatechecker:1.3.100               | maven central?         |
| org.eclipse.platform:org.eclipse.equinox.p2.updatesite:1.2.300                  | maven central?         |
| org.eclipse.platform:org.eclipse.equinox.preferences:3.10.100                   | maven central?         |
| org.eclipse.platform:org.eclipse.equinox.registry:3.11.200                      | maven central?         |
| org.eclipse.platform:org.eclipse.equinox.security.ui:1.3.400                    | maven central?         |
| org.eclipse.platform:org.eclipse.equinox.security:1.3.1000                      | maven central?         |
| org.eclipse.platform:org.eclipse.equinox.simpleconfigurator.manipulator:2.2.100 | maven central?         |
| org.eclipse.platform:org.eclipse.equinox.simpleconfigurator:1.4.200             | maven central?         |
| org.eclipse.platform:org.eclipse.equinox.supplement:1.10.600                    | maven central?         |
| org.eclipse.platform:org.eclipse.help.base:4.3.900                              | maven central?         |
| org.eclipse.platform:org.eclipse.help.ui:4.4.100                                | maven central?         |
| org.eclipse.platform:org.eclipse.help.webapp:3.10.900                           | maven central?         |
| org.eclipse.platform:org.eclipse.help:3.9.100                                   | maven central?         |
| org.eclipse.platform:org.eclipse.jface.databinding:1.14.0                       | maven central?         |
| org.eclipse.platform:org.eclipse.jface.notifications:0.5.100                    | maven central?         |
| org.eclipse.platform:org.eclipse.jface.text:3.22.0                              | maven central?         |
| org.eclipse.platform:org.eclipse.jface:3.28.0                                   | maven central?         |
| org.eclipse.platform:org.eclipse.jsch.core:1.4.0                                | maven central?         |
| org.eclipse.platform:org.eclipse.jsch.ui:1.4.200                                | maven central?         |
| org.eclipse.platform:org.eclipse.ltk.core.refactoring:3.13.0                    | maven central?         |
| org.eclipse.platform:org.eclipse.ltk.ui.refactoring:3.12.200                    | maven central?         |
| org.eclipse.platform:org.eclipse.osgi.compatibility.state:1.2.800               | maven central?         |
| org.eclipse.platform:org.eclipse.osgi.services:3.11.100                         | maven central?         |
| org.eclipse.platform:org.eclipse.osgi.util:3.7.100                              | maven central?         |
| org.eclipse.platform:org.eclipse.osgi:3.18.200                                  | maven central?         |
| org.eclipse.platform:org.eclipse.platform.doc.user:4.26.0                       | maven central?         |
| org.eclipse.platform:org.eclipse.platform:4.26.0                                | maven central?         |
| org.eclipse.platform:org.eclipse.rcp:4.26.0                                     | maven central?         |
| org.eclipse.platform:org.eclipse.search:3.14.300                                | maven central?         |
| org.eclipse.platform:org.eclipse.swt:3.122.0                                    | maven central?         |
| org.eclipse.platform:org.eclipse.team.core:3.9.600                              | maven central?         |
| org.eclipse.platform:org.eclipse.team.genericeditor.diff.extension:1.1.100      | maven central?         |
| org.eclipse.platform:org.eclipse.team.ui:3.9.500                                | maven central?         |
| org.eclipse.platform:org.eclipse.text.quicksearch:1.1.400                       | maven central?         |
| org.eclipse.platform:org.eclipse.text:3.12.300                                  | maven central?         |
| org.eclipse.platform:org.eclipse.ui.browser:3.7.300                             | maven central?         |
| org.eclipse.platform:org.eclipse.ui.cheatsheets:3.7.500                         | maven central?         |
| org.eclipse.platform:org.eclipse.ui.console:3.11.400                            | maven central?         |
| org.eclipse.platform:org.eclipse.ui.editors:3.14.400                            | maven central?         |
| org.eclipse.platform:org.eclipse.ui.externaltools:3.5.200                       | maven central?         |
| org.eclipse.platform:org.eclipse.ui.forms:3.11.500                              | maven central?         |
| org.eclipse.platform:org.eclipse.ui.genericeditor:1.2.300                       | maven central?         |
| org.eclipse.platform:org.eclipse.ui.ide.application:1.4.600                     | maven central?         |
| org.eclipse.platform:org.eclipse.ui.ide:3.20.0                                  | maven central?         |
| org.eclipse.platform:org.eclipse.ui.intro.quicklinks:1.1.200                    | maven central?         |
| org.eclipse.platform:org.eclipse.ui.intro.universal:3.4.300                     | maven central?         |
| org.eclipse.platform:org.eclipse.ui.intro:3.6.600                               | maven central?         |
| org.eclipse.platform:org.eclipse.ui.monitoring:1.2.300                          | maven central?         |
| org.eclipse.platform:org.eclipse.ui.navigator.resources:3.8.500                 | maven central?         |
| org.eclipse.platform:org.eclipse.ui.navigator:3.10.400                          | maven central?         |
| org.eclipse.platform:org.eclipse.ui.net:1.4.100                                 | maven central?         |
| org.eclipse.platform:org.eclipse.ui.themes:1.2.2100                             | maven central?         |
| org.eclipse.platform:org.eclipse.ui.views.log:1.3.400                           | maven central?         |
| org.eclipse.platform:org.eclipse.ui.views.properties.tabbed:3.9.300             | maven central?         |
| org.eclipse.platform:org.eclipse.ui.views:3.11.300                              | maven central?         |
| org.eclipse.platform:org.eclipse.ui.workbench.texteditor:3.16.600               | maven central?         |
| org.eclipse.platform:org.eclipse.ui.workbench:3.127.0                           | maven central?         |
| org.eclipse.platform:org.eclipse.ui:3.201.200                                   | maven central?         |
| org.eclipse.platform:org.eclipse.update.configurator:3.4.1000                   | maven central?         |
| org.eclipse.platform:org.eclipse.urischeme:1.2.200                              | maven central?         |
| com.sun.el:2.2.0.v201303151357                                                  | p2 1.2.4               |
| javax.el:2.2.0.v201303151357                                                    | p2 1.2.4               |
| javax.inject:1.0.0.v20220405-0441                                               | p2 1.2.4               |
| javax.servlet.jsp:2.2.0.v201112011158                                           | p2 1.2.4               |
| org.apache.ant:1.10.12.v20211102-1452                                           | p2 1.2.4               |
| org.apache.commons.jxpath:1.3.0.v200911051830                                   | p2 1.2.4               |
| org.apache.commons.logging:1.2.0.v20180409-1502                                 | p2 1.2.4               |
| org.apache.felix.gogo.command:1.1.2.v20210111-1007                              | p2 1.2.4               |
| org.apache.felix.gogo.shell:1.1.4.v20210111-1007                                | p2 1.2.4               |
| org.apache.jasper.glassfish:2.2.2.v201501141630                                 | p2 1.2.4               |
| org.eclipse.corrosion:1.2.4.202206282034                                        | p2 1.2.4               |
| org.eclipse.lsp4e:0.13.12.202206011407                                          | p2 1.2.4               |
| org.eclipse.lsp4j.jsonrpc:0.14.0.v20220526-1518                                 | p2 1.2.4               |
| org.eclipse.lsp4j:0.14.0.v20220526-1518                                         | p2 1.2.4               |
| org.eclipse.mylyn.wikitext.markdown:3.0.42.20220107230029                       | p2 1.2.4               |
| org.eclipse.mylyn.wikitext:3.0.42.20220107230029                                | p2 1.2.4               |
| org.eclipse.tm4e.core:0.4.4.202205101731                                        | p2 1.2.4               |
| org.eclipse.tm4e.languageconfiguration:0.4.1.202205101731                       | p2 1.2.4               |
| org.eclipse.tm4e.registry:0.5.1.202205101731                                    | p2 1.2.4               |
| org.eclipse.tm4e.ui:0.5.1.202205101731                                          | p2 1.2.4               |
| org.eclipse.unittest.ui:1.0.100.v20210429-0605                                  | p2 1.2.4               |
| org.eclipse.xtext.xbase.lib:2.27.0.v20220530-0353                               | p2 1.2.4               |
| org.jcodings:1.0.57                                                             | p2 1.2.4               |
| org.joni:2.1.43                                                                 | p2 1.2.4               |
| org.jsoup:1.14.3.v20211012-1727                                                 | p2 1.2.4               |
| org.slf4j.api:1.7.30.v20200204-2150                                             | p2 1.2.4               |
| org.tukaani.xz:1.9.0.v20210624-1259                                             | p2 1.2.4               |
| org.w3c.css.sac:1.3.1.v200903091627                                             | p2 1.2.4               |
| org.w3c.dom.events:3.0.0.draft20060413_v201105210656                            | p2 1.2.4               |
| org.w3c.dom.smil:1.0.1.v200903091627                                            | p2 1.2.4               |
| org.w3c.dom.svg:1.1.0.v201011041433                                             | p2 1.2.4               |
| com.jcraft.jsch:0.1.55.v20221112-0806                                           | p2 R-4.26-202211231800 |
| javax.annotation:1.3.5.v20221112-0806                                           | p2 R-4.26-202211231800 |
| org.apache.batik.constants:1.16.0.v20221027-0840                                | p2 R-4.26-202211231800 |
| org.apache.batik.css:1.16.0.v20221027-0840                                      | p2 R-4.26-202211231800 |
| org.apache.batik.i18n:1.16.0.v20221027-0840                                     | p2 R-4.26-202211231800 |
| org.apache.batik.util:1.16.0.v20221027-0840                                     | p2 R-4.26-202211231800 |
| org.apache.commons.codec:1.14.0.v20221112-0806                                  | p2 R-4.26-202211231800 |
| org.apache.httpcomponents.client5.httpclient5:5.1.3.v20221013-1742              | p2 R-4.26-202211231800 |
| org.apache.httpcomponents.core5.httpcore5-h2:5.1.4.v20221013-1742               | p2 R-4.26-202211231800 |
| org.apache.httpcomponents.core5.httpcore5:5.1.4.v20221013-1742                  | p2 R-4.26-202211231800 |
| org.apache.lucene.analyzers-common:8.4.1.v20221112-0806                         | p2 R-4.26-202211231800 |
| org.apache.lucene.analyzers-smartcn:8.4.1.v20221112-0806                        | p2 R-4.26-202211231800 |
| org.apache.lucene.core:8.4.1.v20221112-0806                                     | p2 R-4.26-202211231800 |
| org.apache.xmlgraphics:2.7.0.v20221018-0736                                     | p2 R-4.26-202211231800 |
| com.google.gson:2.9.1.v20220915-1632                                            | p2 cdt-11.0.0          |
| org.eclipse.cdt.core.native:6.3.0.202211062329                                  | p2 cdt-11.0.0          |
| org.eclipse.cdt.core:8.0.0.202211292120                                         | p2 cdt-11.0.0          |
| org.eclipse.cdt.debug.core:8.8.0.202211072232                                   | p2 cdt-11.0.0          |
| org.eclipse.cdt.debug.ui:8.5.0.202211100047                                     | p2 cdt-11.0.0          |
| org.eclipse.cdt.dsf.gdb.ui:2.8.0.202211062329                                   | p2 cdt-11.0.0          |
| org.eclipse.cdt.dsf.gdb:7.0.0.202211221643                                      | p2 cdt-11.0.0          |
| org.eclipse.cdt.dsf.ui:2.7.0.202211062329                                       | p2 cdt-11.0.0          |
| org.eclipse.cdt.dsf:2.12.0.202211062329                                         | p2 cdt-11.0.0          |
| org.eclipse.cdt.gdb:7.2.0.202211062329                                          | p2 cdt-11.0.0          |
| org.eclipse.cdt.launch:10.4.0.202211062329                                      | p2 cdt-11.0.0          |
| org.eclipse.cdt.native.serial:1.3.0.202211300214                                | p2 cdt-11.0.0          |
| org.eclipse.cdt.ui:8.0.0.202211292120                                           | p2 cdt-11.0.0          |
| org.eclipse.launchbar.core:2.5.0.202211062329                                   | p2 cdt-11.0.0          |
| org.eclipse.launchbar.ui:2.5.0.202211062329                                     | p2 cdt-11.0.0          |
| org.eclipse.tm.terminal.control:5.4.0.202211062329                              | p2 cdt-11.0.0          |
| org.eclipse.tools.templates.core:1.3.0.202211062329                             | p2 cdt-11.0.0          |
| org.eclipse.tools.templates.ui:1.4.0.202211062329                               | p2 cdt-11.0.0          |
| org.yaml.snakeyaml:1.27.0.v20221112-0806                                        | p2 cdt-11.0.0          |
+---------------------------------------------------------------------------------+------------------------+
╔═ [end of file] ═╗
