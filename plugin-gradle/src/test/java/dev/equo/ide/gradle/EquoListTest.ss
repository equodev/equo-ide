╔═ allCategories ═╗
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
╔═ allFeatures ═╗
+-----------------------------------------------------------------------+----------------------------------------------------+
| id                                                                    | name \n description                                |
+-----------------------------------------------------------------------+----------------------------------------------------+
| eclipse-junit-tests                                                   | eclipse-junit-tests                                |
| org.eclipse.core.runtime.feature.feature.group                        | Eclipse Core Runtime Infrastructure                |
|                                                                       |   Common OS-independent base of the Eclipse        |
|                                                                       |   platform. (Binary runtime and user               |
|                                                                       |   documentation.)                                  |
| org.eclipse.e4.core.tools.feature.feature.group                       | Eclipse e4 Tools                                   |
|                                                                       |   Eclipse e4 Model Tooling                         |
| org.eclipse.e4.core.tools.feature.source.feature.group                | Eclipse e4 Tools Developer Resources               |
|                                                                       |   Eclipse e4 Model Tooling                         |
| org.eclipse.e4.rcp.feature.group                                      | Eclipse 4 Rich Client Platform                     |
|                                                                       |   The bundles typical required by Eclipse RCP      |
|                                                                       |   applications as of version 4.0                   |
| org.eclipse.e4.rcp.source.feature.group                               | Eclipse 4 Rich Client Platform Developer Resources |
|                                                                       |   The bundles typical required by Eclipse RCP      |
|                                                                       |   applications as of version 4.0                   |
| org.eclipse.ecf.core.feature.feature.group                            | ECF Core Feature                                   |
|                                                                       |   This feature provides the ECF core               |
|                                                                       |   (org.eclipse.ecf) and ECF identity               |
|                                                                       |   (org.eclipse.ecf.identity) bundles.  These two   |
|                                                                       |   bundles are required for all other parts of ECF. |
| org.eclipse.ecf.core.feature.source.feature.group                     | ECF Core Feature Developer Resources               |
|                                                                       |   This feature provides the ECF core               |
|                                                                       |   (org.eclipse.ecf) and ECF identity               |
|                                                                       |   (org.eclipse.ecf.identity) bundles.  These two   |
|                                                                       |   bundles are required for all other parts of ECF. |
| org.eclipse.ecf.core.ssl.feature.feature.group                        | ECF Core SSL Feature                               |
|                                                                       |   This feature provides the ECF core SSL           |
|                                                                       |   fragment.  On Equinox-based frameworks, this     |
|                                                                       |   fragment exposes the Equinox TrustManager to ECF |
|                                                                       |   FileTransfer and other ECF-based communications. |
| org.eclipse.ecf.core.ssl.feature.source.feature.group                 | ECF Core SSL Feature Developer Resources           |
|                                                                       |   This feature provides the ECF core SSL           |
|                                                                       |   fragment.  On Equinox-based frameworks, this     |
|                                                                       |   fragment exposes the Equinox TrustManager to ECF |
|                                                                       |   FileTransfer and other ECF-based communications. |
| org.eclipse.ecf.filetransfer.feature.feature.group                    | ECF Filetransfer Feature                           |
|                                                                       |   This feature provides the ECF Filetransfer API   |
|                                                                       |   bundle.  This API is used by the Eclipse         |
|                                                                       |   platform to support P2 filetransfer and is       |
|                                                                       |   required for any of the ECF FileTransfer         |
|                                                                       |   providers.                                       |
| org.eclipse.ecf.filetransfer.feature.source.feature.group             | ECF Filetransfer Feature Developer Resources       |
|                                                                       |   This feature provides the ECF Filetransfer API   |
|                                                                       |   bundle.  This API is used by the Eclipse         |
|                                                                       |   platform to support P2 filetransfer and is       |
|                                                                       |   required for any of the ECF FileTransfer         |
|                                                                       |   providers.                                       |
| org.eclipse.ecf.filetransfer.httpclient5.feature.feature.group        | ECF Apache Httpclient 5 FileTransfer Provider      |
|                                                                       |   This feature provides the Apache                 |
|                                                                       |   HttpComponents/HttpClient 5 based FileTransfer   |
|                                                                       |   provider used by the Eclipse platform to support |
|                                                                       |   P2 filetransfer.                                 |
| org.eclipse.ecf.filetransfer.httpclient5.feature.source.feature.group | ECF Apache Httpclient 5 FileTransfer Provider      |
|                                                                       |   Developer Resources                              |
|                                                                       |   This feature provides the Apache                 |
|                                                                       |   HttpComponents/HttpClient 5 based FileTransfer   |
|                                                                       |   provider used by the Eclipse platform to support |
|                                                                       |   P2 filetransfer.                                 |
| org.eclipse.ecf.filetransfer.ssl.feature.feature.group                | ECF Filetransfer SSL Feature                       |
|                                                                       |   This feature provides the SSL support for the    |
|                                                                       |   ECF FileTransfer API used by the Eclipse         |
|                                                                       |   platform to support P2 filetransfer.             |
| org.eclipse.ecf.filetransfer.ssl.feature.source.feature.group         | ECF Filetransfer SSL Feature Developer Resources   |
|                                                                       |   This feature provides the SSL support for the    |
|                                                                       |   ECF FileTransfer API used by the Eclipse         |
|                                                                       |   platform to support P2 filetransfer.             |
| org.eclipse.emf.common.feature.group                                  | EMF Common                                         |
|                                                                       |   Common platform-independent utilities used       |
|                                                                       |   throughout EMF, including collection classes,    |
|                                                                       |   notifiers, adapters, and commands.               |
| org.eclipse.emf.common.source.feature.group                           | EMF Common Developer Resources                     |
|                                                                       |   Common platform-independent utilities used       |
|                                                                       |   throughout EMF, including collection classes,    |
|                                                                       |   notifiers, adapters, and commands.               |
| org.eclipse.emf.databinding.edit.feature.group                        | EMF Edit Data Binding                              |
|                                                                       |   Support for using EMF objects with JFace's       |
|                                                                       |   data binding framework and integrating with      |
|                                                                       |   EMF's editing framework.                         |
| org.eclipse.emf.databinding.edit.source.feature.group                 | EMF Edit Data Binding Developer Resources          |
|                                                                       |   Support for using EMF objects with JFace's       |
|                                                                       |   data binding framework and integrating with      |
|                                                                       |   EMF's editing framework.                         |
| org.eclipse.emf.databinding.feature.group                             | EMF Data Binding                                   |
|                                                                       |   Support for using EMF objects with JFace's       |
|                                                                       |   data binding framework.                          |
| org.eclipse.emf.databinding.source.feature.group                      | EMF Data Binding Developer Resources               |
|                                                                       |   Support for using EMF objects with JFace's       |
|                                                                       |   data binding framework.                          |
| org.eclipse.emf.ecore.feature.group                                   | EMF - Eclipse Modeling Framework Core Runtime      |
|                                                                       |   The core runtime for EMF, including EMF's        |
|                                                                       |   common utilities, Ecore, XML/XMI persistence,    |
|                                                                       |   and the change model.                            |
| org.eclipse.emf.ecore.source.feature.group                            | EMF - Eclipse Modeling Framework Core Runtime      |
|                                                                       |   Developer Resources                              |
|                                                                       |   The core runtime for EMF, including EMF's        |
|                                                                       |   common utilities, Ecore, XML/XMI persistence,    |
|                                                                       |   and the change model.                            |
| org.eclipse.emf.edit.feature.group                                    | EMF Edit                                           |
|                                                                       |   Platform-independent framework for viewing and   |
|                                                                       |   editing EMF objects.                             |
| org.eclipse.emf.edit.source.feature.group                             | EMF Edit Developer Resources                       |
|                                                                       |   Platform-independent framework for viewing and   |
|                                                                       |   editing EMF objects.                             |
| org.eclipse.equinox.compendium.sdk.feature.group                      | Equinox Compendium SDK                             |
|                                                                       |   A collection of Equinox bundles  and source      |
|                                                                       |   that implement optional parts of the current     |
|                                                                       |   OSGi specifications--the so called               |
|                                                                       |   &quot;Compendium Services&quot;.  This feature   |
|                                                                       |   includes the corresponding source and is         |
|                                                                       |   intended to be added to target platforms at      |
|                                                                       |   development time rather than deployed with       |
|                                                                       |   end-user systems.                                |
| org.eclipse.equinox.core.feature.feature.group                        | Equinox Core Function                              |
| org.eclipse.equinox.core.feature.source.feature.group                 | Equinox Core Function Developer Resources          |
| org.eclipse.equinox.core.sdk.feature.group                            | Equinox Core SDK                                   |
|                                                                       |   A collection of core Equinox bundles and         |
|                                                                       |   source including the Equinox framework           |
|                                                                       |   implementation itself.  This feature includes    |
|                                                                       |   the corresponding source and is intended to be   |
|                                                                       |   added to target platforms at development time    |
|                                                                       |   rather than deployed with end-user systems.      |
| org.eclipse.equinox.executable                                        | Eclipse Platform Launcher Executables for          |
|                                                                       |   Multi-Architecture Builds                        |
|                                                                       |   Platform specific launchers.                     |
| org.eclipse.equinox.executable.feature.group                          | Eclipse Platform Launcher Executables              |
|                                                                       |   Platform specific launchers.                     |
| org.eclipse.equinox.p2.core.feature.feature.group                     | Equinox p2, headless functionalities               |
|                                                                       |   Provides a minimal headless provisioning system. |
| org.eclipse.equinox.p2.core.feature.source.feature.group              | Equinox p2 Core Function Source                    |
|                                                                       |   Source code for the Equinox provisioning         |
|                                                                       |   platform                                         |
| org.eclipse.equinox.p2.discovery.feature.feature.group                | Equinox p2, Discovery UI support                   |
|                                                                       |   All of the bundles that comprise Equinox p2      |
|                                                                       |   discovery.  This feature is intended to be used  |
|                                                                       |   by integrators building on discovery.            |
| org.eclipse.equinox.p2.discovery.feature.source.feature.group         | Eclipse p2 Discovery Developer Resources           |
|                                                                       |   Source code for the Equinox p2 Discovery         |
| org.eclipse.equinox.p2.extras.feature.feature.group                   | Equinox p2, backward compatibility support         |
|                                                                       |   Provides some backward compatibility support     |
|                                                                       |   (e.g. drop-ins, legacy update site) and the      |
|                                                                       |   metadata generation facility.                    |
| org.eclipse.equinox.p2.extras.feature.source.feature.group            | Equinox p2 RCP Management Facilities               |
|                                                                       |   Source code for the Equinox provisioning         |
|                                                                       |   platform                                         |
| org.eclipse.equinox.p2.rcp.feature.feature.group                      | Equinox p2, minimal support for RCP applications   |
|                                                                       |   Provides the minimal set of p2 bundles to use    |
|                                                                       |   in RCP applications.                             |
| org.eclipse.equinox.p2.rcp.feature.source.feature.group               | Equinox p2 RCP Management Facilities Source        |
|                                                                       |   Source code for the Equinox provisioning         |
|                                                                       |   platform                                         |
| org.eclipse.equinox.p2.sdk.feature.group                              | Equinox p2, SDK                                    |
|                                                                       |   All of the bundles and source that comprise      |
|                                                                       |   the Equinox p2 provisioning platform.  This      |
|                                                                       |   feature includes the corresponding source and is |
|                                                                       |   intended to be added to target platforms at      |
|                                                                       |   development time rather than deployed with       |
|                                                                       |   end-user systems.                                |
| org.eclipse.equinox.p2.user.ui.feature.group                          | Equinox p2, Provisioning for IDEs.                 |
|                                                                       |   Eclipse p2 Provisioning Platform for use in      |
|                                                                       |   IDE related scenarios                            |
| org.eclipse.equinox.p2.user.ui.source.feature.group                   | Eclipse p2 Provisioning Developer Resources        |
|                                                                       |   Source code for the Equinox provisioning         |
|                                                                       |   platform                                         |
| org.eclipse.equinox.sdk.feature.group                                 | Equinox Target Components                          |
|                                                                       |   All of the bundles and source that are           |
|                                                                       |   produced by the Equinox project.  This includes  |
|                                                                       |   basic OSGi framework support, all implemented    |
|                                                                       |   compendium services, the p2 provisioning         |
|                                                                       |   platform and various server-side support         |
|                                                                       |   bundles. This feature includes the corresponding |
|                                                                       |   source and is intended to be added to target     |
|                                                                       |   platforms at development time rather than        |
|                                                                       |   deployed with end-user systems.                  |
| org.eclipse.equinox.server.core.feature.group                         | Core Server Feature                                |
| org.eclipse.equinox.server.core.source.feature.group                  | Core Server Feature Developer Resources            |
| org.eclipse.equinox.server.jetty.feature.group                        | Jetty Http Server Feature                          |
| org.eclipse.equinox.server.jetty.source.feature.group                 | Jetty Http Server Feature Developer Resources      |
| org.eclipse.equinox.server.p2.feature.group                           | p2 Server Feature                                  |
| org.eclipse.equinox.server.p2.source.feature.group                    | p2 Server Feature Developer Resources              |
| org.eclipse.help.feature.group                                        | Eclipse Help System                                |
|                                                                       |   Eclipse help system.                             |
| org.eclipse.help.source.feature.group                                 | Eclipse Help System Developer Resources            |
|                                                                       |   Eclipse help system.                             |
| org.eclipse.jdt.feature.group                                         | Eclipse Java Development Tools                     |
|                                                                       |   Eclipse Java development tools (binary runtime   |
|                                                                       |   and user documentation).                         |
| org.eclipse.jdt.source.feature.group                                  | Eclipse JDT Plug-in Developer Resources            |
|                                                                       |   API documentation and source code zips for       |
|                                                                       |   Eclipse Java development tools.                  |
| org.eclipse.jdt.ui.unittest.junit.feature.feature.group               | JUnit Test runner client for UnitTest View         |
|                                                                       |   JUnit test runner client for UnitTest View       |
| org.eclipse.jdt.ui.unittest.junit.feature.source.feature.group        | JUnit Test runner client for UnitTest View         |
|                                                                       |   Developer Resources                              |
|                                                                       |   JUnit test runner client for UnitTest View       |
| org.eclipse.pde.feature.group                                         | Eclipse Plug-in Development Environment            |
|                                                                       |   Eclipse plug-in development environment.         |
| org.eclipse.pde.source.feature.group                                  | Eclipse PDE Plug-in Developer Resources            |
|                                                                       |   Eclipse plug-in development environment,         |
|                                                                       |   including documentation and source code zips.    |
| org.eclipse.pde.unittest.junit.feature.group                          | Eclipse Plug-in Test runner client for UnitTest    |
|                                                                       |   View (Experimental)                              |
|                                                                       |   Eclipse plug-in test runner client for           |
|                                                                       |   UnitTest View.                                   |
| org.eclipse.pde.unittest.junit.source.feature.group                   | Eclipse PDE Plug-in Developer Unit Test support    |
|                                                                       |   Resources                                        |
|                                                                       |   Eclipse plug-in development environment Unit     |
|                                                                       |   Test support, including documentation and source |
|                                                                       |   code zips.                                       |
| org.eclipse.platform.feature.group                                    | Eclipse Platform                                   |
|                                                                       |   Common OS-independent base of the Eclipse        |
|                                                                       |   platform. (Binary runtime and user               |
|                                                                       |   documentation.)                                  |
| org.eclipse.platform.ide                                              | Eclipse Platform                                   |
|                                                                       |   4.26 Release of the Eclipse Platform.            |
| org.eclipse.platform.sdk                                              | Eclipse Platform SDK                               |
|                                                                       |   4.26 Release of the Platform SDK.                |
| org.eclipse.platform.source.feature.group                             | Eclipse Platform Developer Resources               |
|                                                                       |   Common OS-independent base of the Eclipse        |
|                                                                       |   platform. (Binary runtime and user               |
|                                                                       |   documentation.)                                  |
| org.eclipse.rcp.configuration.feature.group                           | Eclipse Product Configuration                      |
|                                                                       |   Configuration information for the Eclipse        |
|                                                                       |   product                                          |
| org.eclipse.rcp.feature.group                                         | Eclipse RCP                                        |
|                                                                       |   Rich Client Platform                             |
| org.eclipse.rcp.id                                                    | Eclipse RCP                                        |
| org.eclipse.rcp.sdk.id                                                | Eclipse RCP SDK                                    |
| org.eclipse.rcp.source.feature.group                                  | Eclipse RCP Developer Resources                    |
|                                                                       |   Rich Client Platform                             |
| org.eclipse.sdk.examples.feature.group                                | Eclipse SDK Examples                               |
|                                                                       |   Eclipse SDK examples. Used in conjunction with   |
|                                                                       |   Eclipse Project SDK.                             |
| org.eclipse.sdk.examples.source.feature.group                         | Eclipse SDK Examples Developer Resources           |
|                                                                       |   Eclipse SDK examples. Used in conjunction with   |
|                                                                       |   Eclipse Project SDK.                             |
| org.eclipse.sdk.feature.group                                         | Eclipse Project SDK                                |
|                                                                       |   SDK for Eclipse.                                 |
| org.eclipse.sdk.ide                                                   | Eclipse SDK                                        |
|                                                                       |   4.26 Release of the Eclipse SDK.                 |
| org.eclipse.sdk.tests.feature.group                                   | Eclipse SDK Tests                                  |
|                                                                       |   Eclipse SDK test plug-ins. Used in conjunction   |
|                                                                       |   with Eclipse Project SDK.                        |
| org.eclipse.swt.tools.feature.feature.group                           | SWT Tools                                          |
|                                                                       |   SWT Tools, including Sleak, SWT Spy Plugin,      |
|                                                                       |   and JniGen.                                      |
| org.eclipse.swt.tools.feature.source.feature.group                    | SWT Tools Developer Resources                      |
|                                                                       |   SWT Tools, including Sleak, SWT Spy Plugin,      |
|                                                                       |   and JniGen.                                      |
| org.eclipse.test.feature.group                                        | Eclipse Test Framework                             |
|                                                                       |   Eclipse Test Framework. Used in conjunction      |
|                                                                       |   with Eclipse JUnit tests.                        |
| org.eclipse.tips.feature.feature.group                                | Tip of the Day UI Feature                          |
|                                                                       |   Contains the Eclipse Tips framework.             |
| org.eclipse.tips.feature.source.feature.group                         | Tip of the Day UI Feature Developer Resources      |
|                                                                       |   Contains the Eclipse Tips framework.             |
+-----------------------------------------------------------------------+----------------------------------------------------+
╔═ allJars ═╗
id,name \n description
assertj-core,AssertJ fluent assertions
,  Rich and fluent assertions for testing for Java
bcpg,bcpg
bcprov,bcprov
com.google.gson,Gson
,  Gson JSON library
com.ibm.icu,ICU4J
,  International Components for Unicode for Java
com.jcraft.jsch,JSch
com.sun.el,Javax Expression Language Reference
,  Implementation Bundle
,  Javax El RI el-impl-2.2.4
com.sun.jna,jna
,  JNA Library
com.sun.jna.platform,jna-platform
,  JNA Platform Library
jakarta.servlet-api,Jakarta Servlet
,  Jakarta Servlet 4.0
javax.annotation,Jakarta Annotations API
,  Jakarta Annotations API
javax.el,Javax Expression Language Bundle
javax.inject,Jakarta Inject
javax.servlet.jsp-api,JavaServer Pages(TM) API
,  Java.net - The Source for Java Technology
,  Collaboration
junit-jupiter-api,JUnit Jupiter API
junit-jupiter-engine,JUnit Jupiter Engine
junit-jupiter-migrationsupport,JUnit Jupiter Migration Support
junit-jupiter-params,JUnit Jupiter Params
junit-platform-commons,JUnit Platform Commons
junit-platform-engine,JUnit Platform Engine API
junit-platform-launcher,JUnit Platform Launcher
junit-platform-runner,JUnit Platform Runner
junit-platform-suite-api,JUnit Platform Suite API
junit-platform-suite-commons,JUnit Platform Suite Commons
junit-platform-suite-engine,JUnit Platform Suite Engine
junit-vintage-engine,JUnit Vintage Engine
net.bytebuddy.byte-buddy,Byte Buddy (without dependencies)
,  Byte Buddy is a Java library for creating Java
,  classes at run time.        This artifact is a
,  build of Byte Buddy with all ASM dependencies
,  repackaged into its own name space.
net.bytebuddy.byte-buddy-agent,Byte Buddy agent
,  The Byte Buddy agent offers convenience for
,  attaching an agent to the local or a remote VM.
org.apache.ant,Apache Ant
org.apache.batik.constants,Batik constants library
org.apache.batik.css,Batik CSS engine
org.apache.batik.i18n,Batik i18n library
org.apache.batik.util,Batik utility library
org.apache.commons.codec,Apache Commons Codec
,  The Apache Commons Codec package contains
,  simple encoder and decoders for     various
,  formats such as Base64 and Hexadecimal.  In
,  addition to these     widely used encoders and
,"  decoders, the codec package also maintains a    "
,  collection of phonetic encoding utilities.
org.apache.commons.commons-fileupload,Apache Commons FileUpload
,  The Apache Commons FileUpload component
,  provides a simple yet flexible means of adding
,  support for multipart    file upload
,  functionality to servlets and web applications.
org.apache.commons.commons-io,Apache Commons IO
,  The Apache Commons IO library contains utility
,"  classes, stream implementations, file"
,"  filters,file comparators, endian transformation"
,"  classes, and much more."
org.apache.commons.jxpath,Apache Commons JXPath
org.apache.commons.logging,Apache Commons Logging
,  Apache Commons Logging is a thin adapter
,"  allowing configurable bridging to other,    well"
,  known logging systems.
org.apache.felix.gogo.command,Apache Felix Gogo Command
,  Provides basic shell commands for Gogo.
org.apache.felix.gogo.runtime,Apache Felix Gogo Runtime
,  Apache Felix Gogo Subproject
org.apache.felix.gogo.shell,Apache Felix Gogo Shell
,  Apache Felix Gogo Subproject
org.apache.felix.scr,Apache Felix Declarative Services
,  Implementation of the Declarative Services
,  specification 1.5
org.apache.httpcomponents.client5.httpclient5,Apache HttpClient
org.apache.httpcomponents.client5.httpclient5-win,Apache HttpClient5 Win32 Support
org.apache.httpcomponents.core5.httpcore5,Apache HttpComponents Core5 HTTP/1.1
org.apache.httpcomponents.core5.httpcore5-h2,Apache HttpComponents Core HTTP/2
org.apache.jasper.glassfish,JSP 2.2 implementation from Glassfish
,  JSP 2.2 reference implementation from Glassfish
org.apache.lucene.analyzers-common,Lucene Common Analyzers
org.apache.lucene.analyzers-smartcn,Lucene Smart Chinese Analyzer
org.apache.lucene.core,Lucene Core
org.apache.xmlgraphics,Apache XML Graphics Commons
org.apiguardian.api,apiguardian-api
,  @API Guardian
org.eclipse.ant.core,Ant Build Tool Core
org.eclipse.ant.launching,Ant Launching Support
org.eclipse.ant.optional.junit,Eclipse JUnit Fragment for Ant used in Eclipse
,  Test Framework
,  This fragment is required by the Eclipse Test
,  Framework. It allows communication between JUnit
,  and Ant.
org.eclipse.ant.tests.core,Ant Core Test Plugin
org.eclipse.ant.tests.ui,Ant UI Test Plugin
org.eclipse.ant.ui,Ant UI
org.eclipse.compare,Compare Support
org.eclipse.compare.core,Core Compare Support
org.eclipse.compare.examples,Compare Example
org.eclipse.compare.examples.xml,XML Compare Support
org.eclipse.compare.tests,Compare Tests
org.eclipse.core.commands,Commands
org.eclipse.core.contenttype,Eclipse Content Mechanism
org.eclipse.core.databinding,JFace Data Binding
org.eclipse.core.databinding.beans,JFace Data Binding for JavaBeans
org.eclipse.core.databinding.observable,JFace Data Binding Observables
org.eclipse.core.databinding.property,JFace Data Binding Properties
org.eclipse.core.expressions,Expression Language
org.eclipse.core.expressions.tests,Expression Language Tests
org.eclipse.core.externaltools,External Tools Headless Support
org.eclipse.core.filebuffers,File Buffers
org.eclipse.core.filebuffers.tests,File Buffers Test Plug-in
org.eclipse.core.filesystem,Core File Systems
org.eclipse.core.jobs,Eclipse Jobs Mechanism
org.eclipse.core.net,Internet Connection Management
org.eclipse.core.resources,Core Resource Management
org.eclipse.core.runtime,Core Runtime
org.eclipse.core.tests.harness,Eclipse Core Tests Harness
org.eclipse.core.tests.net,Net Tests Plug-in
org.eclipse.core.tests.resources,Eclipse Core Tests Resources
org.eclipse.core.tests.runtime,Eclipse Core Tests Runtime
org.eclipse.core.variables,Core Variables
org.eclipse.debug.core,Debug Core
org.eclipse.debug.examples.core,Example Debug Core Plug-in
org.eclipse.debug.examples.ui,Example Debug UI Plug-in
org.eclipse.debug.tests,Debug Test Plugin
org.eclipse.debug.ui,Debug UI
org.eclipse.debug.ui.launchview,Debug UI Launch Configuration View
org.eclipse.e4.core.commands,Eclipse e4 core commands
org.eclipse.e4.core.commands.tests,Eclipse e4 core commands tests
org.eclipse.e4.core.contexts,Eclipse Contexts
org.eclipse.e4.core.di,Eclipse Dependency Injection
org.eclipse.e4.core.di.annotations,Eclipse Dependency Injection Annotations
org.eclipse.e4.core.di.extensions,Eclipse Dependency Injection Extensions
org.eclipse.e4.core.di.extensions.supplier,Eclipse Dependency Injection Extensions Supplier
org.eclipse.e4.core.services,Eclipse Application Services
org.eclipse.e4.core.tests,E4 Core Tests
org.eclipse.e4.emf.xpath,Eclipse Model Xpath
org.eclipse.e4.tools,Tools
org.eclipse.e4.tools.compat,Compat
org.eclipse.e4.tools.emf.editor3x,Editor3x
org.eclipse.e4.tools.emf.ui,EMF ModelTooling UI
org.eclipse.e4.tools.jdt.templates,e4 JDT code templates
org.eclipse.e4.tools.services,Services
org.eclipse.e4.ui.bindings,Eclipse Bindings Support
org.eclipse.e4.ui.bindings.tests,UI Bindings Tests
org.eclipse.e4.ui.css.core,Eclipse CSS Core Support
org.eclipse.e4.ui.css.swt,Eclipse CSS SWT Support
org.eclipse.e4.ui.css.swt.theme,Eclipse CSS SWT Theme Support
org.eclipse.e4.ui.di,Eclipse UI Dependency Injection
org.eclipse.e4.ui.dialogs,Eclipse e4 dialogs
org.eclipse.e4.ui.ide,E4 IDE UI
org.eclipse.e4.ui.model.workbench,Eclipse Workbench Model
org.eclipse.e4.ui.progress,Eclipse e4 Progress View
org.eclipse.e4.ui.services,Eclipse UI Application Services
org.eclipse.e4.ui.tests,TestModel Model
org.eclipse.e4.ui.tests.css.core,e4 css core tests
org.eclipse.e4.ui.tests.css.swt,e4 css swt tests (Incubation)
org.eclipse.e4.ui.widgets,Eclipse UI Custom widgets
org.eclipse.e4.ui.workbench,Eclipse e4 Workbench
org.eclipse.e4.ui.workbench.addons.swt,Eclipse e4 Workbench Add-ons
org.eclipse.e4.ui.workbench.renderers.swt,Eclipse e4 Workbench SWT Renderer
org.eclipse.e4.ui.workbench.swt,Eclipse e4 Workbench SWT
org.eclipse.e4.ui.workbench3,Bundle for Workbench APIs available in e4
org.eclipse.ecf,ECF Core API
org.eclipse.ecf.filetransfer,ECF Filetransfer API
org.eclipse.ecf.identity,ECF Identity Core API
org.eclipse.ecf.provider.filetransfer,ECF Filetransfer Provider
org.eclipse.ecf.provider.filetransfer.httpclient5,ECF HttpComponents 5 Filetransfer Provider
org.eclipse.ecf.provider.filetransfer.ssl,ECF Filetransfer SSL Fragment
org.eclipse.ecf.ssl,ECF SSL Fragment
org.eclipse.emf.common,EMF Common
org.eclipse.emf.databinding,EMF Data Binding
org.eclipse.emf.databinding.edit,EMF Edit Data Binding
org.eclipse.emf.ecore,EMF Ecore
org.eclipse.emf.ecore.change,EMF Change Model
org.eclipse.emf.ecore.xmi,EMF XML/XMI Persistence
org.eclipse.emf.edit,EMF Edit
org.eclipse.equinox.app,Equinox Application Container
org.eclipse.equinox.bidi,Bidirectional Text Support
org.eclipse.equinox.bidi.tests,BiDi tests
org.eclipse.equinox.cm,Configuration Admin
org.eclipse.equinox.common,Common Eclipse Runtime
org.eclipse.equinox.common.tests,Common Eclipse Runtime Tests
org.eclipse.equinox.concurrent,Equinox Concurrent API
org.eclipse.equinox.console,Console plug-in
org.eclipse.equinox.console.jaas.fragment,SSHD Fragment
org.eclipse.equinox.console.ssh,Console ssh support plug-in
org.eclipse.equinox.coordinator,Coordinator
org.eclipse.equinox.device,Device Access Service
org.eclipse.equinox.ds.tests,Declarative Services Tests
org.eclipse.equinox.event,Event Admin
org.eclipse.equinox.frameworkadmin,Equinox Framework Admin
org.eclipse.equinox.frameworkadmin.equinox,Equinox Framework Admin for Equinox
org.eclipse.equinox.frameworkadmin.test,Test Plug-in for Framework Admin
org.eclipse.equinox.http.jetty,Jetty Http Service
org.eclipse.equinox.http.registry,Http Service Registry Extensions
org.eclipse.equinox.http.servlet,Http Services Servlet
org.eclipse.equinox.http.servlet.tests,org.eclipse.equinox.http.servlet.tests
org.eclipse.equinox.http.servletbridge,Servletbridge Http Service
org.eclipse.equinox.jsp.jasper,Jasper Jsp Support Bundle
org.eclipse.equinox.jsp.jasper.registry,Jasper Jsp Registry Support Plug-in
org.eclipse.equinox.launcher,Equinox Launcher
org.eclipse.equinox.log.stream,Log Stream Provider
org.eclipse.equinox.metatype,Meta Type
org.eclipse.equinox.p2.artifact.repository,Equinox Provisioning Artifact Repository Support
org.eclipse.equinox.p2.console,Equinox Provisioning Console
org.eclipse.equinox.p2.core,Equinox Provisioning Core
org.eclipse.equinox.p2.director,Equinox Provisioning Director
org.eclipse.equinox.p2.director.app,Equinox Provisioning Director Application
org.eclipse.equinox.p2.directorywatcher,Equinox Provisioning Directory Watcher
org.eclipse.equinox.p2.discovery,Equinox Provisioning Discovery
org.eclipse.equinox.p2.discovery.compatibility,Equinox Provisioning Discovery
org.eclipse.equinox.p2.engine,Equinox Provisioning Engine
org.eclipse.equinox.p2.extensionlocation,Equinox Provisioning Extension Location
,  Repository Support
org.eclipse.equinox.p2.garbagecollector,Equinox Provisioning Garbage Collector
org.eclipse.equinox.p2.installer,Equinox Provisioning Installer
org.eclipse.equinox.p2.jarprocessor,Equinox Provisioning JAR Processor
org.eclipse.equinox.p2.metadata,Equinox Provisioning Metadata
org.eclipse.equinox.p2.metadata.repository,Equinox Provisioning Metadata Repository
org.eclipse.equinox.p2.operations,Equinox Provisioning Operations API
org.eclipse.equinox.p2.publisher,Equinox Provisioning Publisher Infrastructure
org.eclipse.equinox.p2.publisher.eclipse,Equinox Provisioning Publisher for Eclipse
org.eclipse.equinox.p2.reconciler.dropins,Equinox Provisioning Drop-in
org.eclipse.equinox.p2.repository,Equinox Provisioning Repository
org.eclipse.equinox.p2.repository.tools,Equinox Provisioning Repository Tools
org.eclipse.equinox.p2.tests,Equinox Provisioning Tests (Incubation)
org.eclipse.equinox.p2.tests.discovery,Equinox Provisioning Discovery Tests
org.eclipse.equinox.p2.tests.ui,Equinox Provisioning Tests (Incubation)
org.eclipse.equinox.p2.tests.verifier,Test Install Verifier
org.eclipse.equinox.p2.touchpoint.eclipse,Equinox Provisioning Eclipse Touchpoint
org.eclipse.equinox.p2.touchpoint.natives,Equinox Provisioning Native Touchpoint
org.eclipse.equinox.p2.transport.ecf,Equinox Provisioning ECF based Transport
org.eclipse.equinox.p2.ui,Equinox Provisioning UI Support
org.eclipse.equinox.p2.ui.admin,p2 Admin UI
org.eclipse.equinox.p2.ui.admin.rcp,Provisioning Admin UI RCP (Incubation)
org.eclipse.equinox.p2.ui.discovery,Equinox Provisioning Discovery UI
org.eclipse.equinox.p2.ui.importexport,Equinox Provisioning Import and Export
org.eclipse.equinox.p2.ui.sdk,Equinox Provisioning Platform Update Support
org.eclipse.equinox.p2.ui.sdk.scheduler,Equinox Provisioning Platform Automatic Update
,  Support
org.eclipse.equinox.p2.updatechecker,Equinox Provisioning Update Checker
org.eclipse.equinox.p2.updatesite,Equinox Provisioning Legacy Update Site Support
org.eclipse.equinox.preferences,Eclipse Preferences Mechanism
org.eclipse.equinox.preferences.tests,Preferences Tests
org.eclipse.equinox.region,Region Digraph
org.eclipse.equinox.registry,Extension Registry Support
org.eclipse.equinox.security,Equinox Java Authentication and Authorization
,  Service (JAAS)
org.eclipse.equinox.security.tests,Equinox security tests
org.eclipse.equinox.security.ui,Equinox Security Default UI
org.eclipse.equinox.servletbridge,Servletbridge
org.eclipse.equinox.simpleconfigurator,Simple Configurator
org.eclipse.equinox.simpleconfigurator.manipulator,Simple Configurator Manipulator
org.eclipse.equinox.supplement,Supplemental Equinox Functionality
org.eclipse.equinox.transforms.hook,Transformer Hook Framework Extension
org.eclipse.equinox.transforms.xslt,XSLT Transform Provider
org.eclipse.equinox.useradmin,User Admin Service
org.eclipse.equinox.weaving.caching,Standard Caching Service for Equinox Aspects
org.eclipse.equinox.weaving.caching.j9,J9 CachingService Plug-in
org.eclipse.equinox.weaving.hook,Aspect Weaving Hooks Plug-in
org.eclipse.help,Help System Core
org.eclipse.help.base,Help System Base
org.eclipse.help.ui,Help System UI
org.eclipse.help.webapp,Help System Webapp
org.eclipse.jdt,Eclipse Java Development Tools
org.eclipse.jdt.annotation,JDT Annotations for Enhanced Null Analysis
org.eclipse.jdt.apt.core,Java Annotation Processing Core
org.eclipse.jdt.apt.pluggable.core,Java Compiler Apt IDE
org.eclipse.jdt.apt.pluggable.tests,Java Annotation Processing Tests
org.eclipse.jdt.apt.tests,Java Annotation Processing Tests
org.eclipse.jdt.apt.ui,Java Annotation Processing UI
org.eclipse.jdt.compiler.apt,Java Compiler Apt
org.eclipse.jdt.compiler.apt.tests,Java Annotation Processing Tests
org.eclipse.jdt.compiler.tool,Java Compiler Tool Support
org.eclipse.jdt.compiler.tool.tests,Java Compiler Tool Tests
org.eclipse.jdt.core,Java Development Tools Core
org.eclipse.jdt.core.compiler.batch,Eclipse Compiler for Java(TM)
org.eclipse.jdt.core.formatterapp,Java Development Tools Formatter Application
org.eclipse.jdt.core.manipulation,Java Code Manipulation Functionality
org.eclipse.jdt.core.tests.binaries,%pluginName
org.eclipse.jdt.core.tests.builder,Java Builder Tests
org.eclipse.jdt.core.tests.compiler,Java Compiler Tests
org.eclipse.jdt.core.tests.model,Java Model Tests
org.eclipse.jdt.core.tests.performance,JDT Core Performance Tests
org.eclipse.jdt.debug,JDI Debug Model
org.eclipse.jdt.debug.tests,Java Debug Test Plugin
org.eclipse.jdt.debug.ui,JDI Debug UI
org.eclipse.jdt.doc.isv,Eclipse JDT Plug-in Developer Guide
org.eclipse.jdt.doc.user,Eclipse Java Development User Guide
org.eclipse.jdt.junit,Java Development Tools JUnit support
org.eclipse.jdt.junit.core,Java Development Tools JUnit core support
org.eclipse.jdt.junit.runtime,Java Development Tools JUnit Runtime Support
org.eclipse.jdt.junit4.runtime,Java Development Tools JUnit4 Runtime Support
org.eclipse.jdt.junit5.runtime,Java Development Tools JUnit5 Runtime Support
org.eclipse.jdt.launching,Java Development Tools Launching Support
org.eclipse.jdt.text.tests,JDT Text Test Plug-in
org.eclipse.jdt.ui,Java Development Tools UI
org.eclipse.jdt.ui.tests,Java Test Plug-in
org.eclipse.jdt.ui.tests.refactoring,Refactoring Tests Plug-in
org.eclipse.jdt.ui.unittest.junit,Java Development Tools Unit Test support
org.eclipse.jetty.http,Jetty :: Http Utility
,  Jetty module for Jetty :: Http Utility
org.eclipse.jetty.io,Jetty :: IO Utility
,  Jetty module for Jetty :: IO Utility
org.eclipse.jetty.security,Jetty :: Security
,  Jetty module for Jetty :: Security
org.eclipse.jetty.server,Jetty :: Server Core
,  Jetty module for Jetty :: Server Core
org.eclipse.jetty.servlet,Jetty :: Servlet Handling
,  Jetty module for Jetty :: Servlet Handling
org.eclipse.jetty.util,Jetty :: Utilities
,  Jetty module for Jetty :: Utilities
org.eclipse.jetty.util.ajax,Jetty :: Utilities :: Ajax(JSON)
,  Jetty module for Jetty :: Utilities ::
,  Ajax(JSON)
org.eclipse.jface,JFace
org.eclipse.jface.databinding,JFace Data Binding for SWT and JFace
org.eclipse.jface.examples.databinding,JFace Data Binding Examples
org.eclipse.jface.notifications,Notification API
org.eclipse.jface.tests,Jface tests
org.eclipse.jface.tests.databinding,JFace Data Binding Tests
org.eclipse.jface.tests.databinding.conformance,JFace Data Binding Conformance Tests
org.eclipse.jface.text,JFace Text
org.eclipse.jface.text.tests,JFace Text Test Plug-in
org.eclipse.jsch.core,JSch Core
org.eclipse.jsch.tests,JSch Tests Plug-in
org.eclipse.jsch.ui,JSch UI
org.eclipse.ltk.core.refactoring,Refactoring Core
org.eclipse.ltk.core.refactoring.tests,Refactoring Core Test Plug-in
org.eclipse.ltk.ui.refactoring,Refactoring UI
org.eclipse.ltk.ui.refactoring.tests,Refactoring UI Test Plug-in
org.eclipse.osgi,%systemBundle
,  OSGi System Bundle
org.eclipse.osgi.compatibility.state,Equinox State and Resolver Compatibility Fragment
org.eclipse.osgi.services,OSGi Release 4.2.0 Services
,  OSGi Service Platform Release 4.2.0 Service
,  Interfaces and Classes
org.eclipse.osgi.tests,Core OSGi Tests
org.eclipse.osgi.util,OSGi Release 4.2.0 Utility Classes
,  OSGi Service Platform Release 4.2.0 Utility
,  Classes
org.eclipse.pde,PDE
org.eclipse.pde.api.tools,API Tools
org.eclipse.pde.api.tools.annotations,PDE API Tools Annotations
org.eclipse.pde.api.tools.tests,API Tools Tests
org.eclipse.pde.api.tools.ui,API Tools UI
org.eclipse.pde.build,Plug-in Development Environment Build Support
org.eclipse.pde.build.tests,Tests Plug-in
org.eclipse.pde.core,PDE Core
org.eclipse.pde.doc.user,PDE User Guide
org.eclipse.pde.ds.annotations,Declarative Services Annotations Support
org.eclipse.pde.ds.core,PDE DS Core
org.eclipse.pde.ds.tests,PDE DS Tests
org.eclipse.pde.ds.ui,PDE DS UI
org.eclipse.pde.genericeditor.extension,Target definition generic editor extension
org.eclipse.pde.genericeditor.extension.tests,Tests for Generic Target Platform Editor
org.eclipse.pde.junit.runtime,PDE JUnit Runtime Support
org.eclipse.pde.junit.runtime.tests,PDE JUnit Runtime Tests
org.eclipse.pde.launching,PDE Launching Support
org.eclipse.pde.runtime,PDE Runtime
org.eclipse.pde.spy.bundle,Bundle Spy
org.eclipse.pde.spy.context,Context Spy
org.eclipse.pde.spy.core,Spy Core plug-in
org.eclipse.pde.spy.css,CSS Spy
org.eclipse.pde.spy.event,Event Spy
org.eclipse.pde.spy.model,Model Spy
org.eclipse.pde.spy.preferences,Preference Spy
org.eclipse.pde.ua.core,PDE UA Core
org.eclipse.pde.ua.tests,PDE UA Tests (Incubation)
org.eclipse.pde.ua.ui,PDE UA UI
org.eclipse.pde.ui,PDE UI
org.eclipse.pde.ui.templates,PDE Templates
org.eclipse.pde.ui.templates.tests,Tests for PDE templates
org.eclipse.pde.ui.tests,PDE JUnit Tests
org.eclipse.pde.unittest.junit,Plug-in Development Unit Test support
org.eclipse.platform,Eclipse Platform
org.eclipse.platform.doc.isv,Eclipse Platform Plug-in Developer Guide
org.eclipse.platform.doc.user,Eclipse Platform User Guide
org.eclipse.rcp,Eclipse RCP
org.eclipse.releng.tests,Eclipse Releng Tests
org.eclipse.sdk,Eclipse Project SDK
org.eclipse.sdk.examples,Eclipse SDK Examples
org.eclipse.sdk.tests,Tests
org.eclipse.search,Search Support
org.eclipse.search.tests,Search Support Tests Plug-in
org.eclipse.swt,Standard Widget Toolkit
org.eclipse.swt.examples,SWT Standalone Examples Plugin
org.eclipse.swt.examples.browser.demos,SWT Browser Demos Plugin
org.eclipse.swt.examples.launcher,SWT Launcher Example Plugin
org.eclipse.swt.examples.ole.win32,SWT OLE Example Plugin (Win32)
org.eclipse.swt.examples.views,SWT Views Example Plugin
org.eclipse.swt.tests,Tests
org.eclipse.swt.tools,SWT Tools
org.eclipse.swt.tools.base,SWT Tools Base
org.eclipse.swt.tools.spies,SWT Tools Spy
org.eclipse.team.core,Team Support Core
org.eclipse.team.examples.filesystem,Eclipse Team File System Example
org.eclipse.team.genericeditor.diff.extension,Diff Extension for Generic Editor
org.eclipse.team.tests.core,Eclipse Team Core Tests
org.eclipse.team.ui,Team Support UI
org.eclipse.test,Eclipse Automated Testing
org.eclipse.test.performance,Performance Test Plug-in
org.eclipse.test.performance.win32,Performance Monitor for Windows
org.eclipse.tests.urischeme,Tests for Eclipse URI Scheme Handling
org.eclipse.text,Text
org.eclipse.text.quicksearch,Quick Search
org.eclipse.text.quicksearch.tests,Tests for org.eclipse.text.quicksearch Bundle
org.eclipse.text.tests,Text Test Plug-in
org.eclipse.tips.core,Tip of the Day core plugin
org.eclipse.tips.ide,IDE Enablement for Tip of the Day
org.eclipse.tips.json,Tip of the Day Json Provider
org.eclipse.tips.ui,Tip of the Day UI
org.eclipse.tools.layout.spy,Layout Spy
org.eclipse.ua.tests,User Assistance Test
org.eclipse.ua.tests.doc,Doc
org.eclipse.ui,Eclipse UI
org.eclipse.ui.browser,Browser Support
org.eclipse.ui.cheatsheets,Cheat Sheets
org.eclipse.ui.console,Console
org.eclipse.ui.editors,Default Text Editor
org.eclipse.ui.editors.tests,Editors Test Plug-in
org.eclipse.ui.examples.contributions,Command Contribution Examples
org.eclipse.ui.examples.fieldassist,Field Assist Example
org.eclipse.ui.examples.javaeditor,JFace Text Example
org.eclipse.ui.examples.multipageeditor,Multi-Page Editor Example
org.eclipse.ui.examples.propertysheet,User File Editor
org.eclipse.ui.examples.readmetool,Readme File Editing Tool
org.eclipse.ui.examples.undo,Undo Example
org.eclipse.ui.examples.uriSchemeHandler,Example for Eclipse URI Scheme Handling
org.eclipse.ui.examples.views.properties.tabbed.article,Tabbed Properties View Article Example
org.eclipse.ui.externaltools,External Tools
org.eclipse.ui.forms,Eclipse Forms
org.eclipse.ui.genericeditor,Generic and Extensible Text Editor
org.eclipse.ui.genericeditor.examples,Examples for Generic Editor
org.eclipse.ui.genericeditor.tests,Generic Editor Test Plug-in
org.eclipse.ui.ide,Eclipse IDE UI
org.eclipse.ui.ide.application,Eclipse IDE UI Application
org.eclipse.ui.intro,Welcome Framework
org.eclipse.ui.intro.quicklinks,Quicklinks for the Welcome Framework
org.eclipse.ui.intro.universal,Universal Welcome
org.eclipse.ui.monitoring,UI Freeze Monitoring
org.eclipse.ui.navigator,Common Navigator View
org.eclipse.ui.navigator.resources,Navigator Workbench Components
org.eclipse.ui.net,Internet Connection Management UI
org.eclipse.ui.tests,Eclipse UI Tests
org.eclipse.ui.tests.forms,Forms Test
org.eclipse.ui.tests.harness,Harness Plug-in
org.eclipse.ui.tests.navigator,Common Navigator Tests
org.eclipse.ui.tests.performance,Performance Plug-in
org.eclipse.ui.tests.pluginchecks,Pluginchecks
org.eclipse.ui.tests.rcp,Eclipse RCP Tests
org.eclipse.ui.tests.views.properties.tabbed,Tabbed Properties View Tests
org.eclipse.ui.themes,Eclipse SDK Themes
org.eclipse.ui.trace,Equinox Dynamic Tracing Enablement UI
org.eclipse.ui.views,Views
org.eclipse.ui.views.log,Log View
org.eclipse.ui.views.properties.tabbed,Tabbed Properties View
org.eclipse.ui.workbench,Eclipse Workbench
org.eclipse.ui.workbench.texteditor,Text Editor Framework
org.eclipse.ui.workbench.texteditor.tests,Workbench Text Editor Test Plug-in
org.eclipse.unittest.ui,Debug Unit Test support
org.eclipse.update.configurator,Install/Update Configurator
org.eclipse.urischeme,Eclipse URI Scheme Handling
org.hamcrest.core,Hamcrest Core Library of Matchers
org.junit,JUnit
org.mockito.mockito-core,Mockito Mock Library for Java. Core bundle
,  requires Byte Buddy and Objenesis.
org.objectweb.asm,org.objectweb.asm
org.objectweb.asm.tree,org.objectweb.asm.tree
org.objenesis,Objenesis
,  A library for instantiating Java objects
org.opentest4j,opentest4j
org.osgi.annotation.bundle,org.osgi:org.osgi.annotation.bundle
,  OSGi Companion Code for
,  org.osgi.annotation.bundle Version 2.0.0
org.osgi.annotation.versioning,org.osgi:org.osgi.annotation.versioning
,  OSGi Companion Code for
,  org.osgi.annotation.versioning Version 1.1.2
org.osgi.service.cm,org.osgi:org.osgi.service.cm
,  OSGi Companion Code for org.osgi.service.cm
,  Version 1.6.1
org.osgi.service.component,org.osgi:org.osgi.service.component
,  OSGi Companion Code for
,  org.osgi.service.component Version 1.5.0
org.osgi.service.component.annotations,org.osgi:org.osgi.service.component.annotations
,  OSGi Companion Code for
,  org.osgi.service.component.annotations Version
,  1.5.0
org.osgi.service.coordinator,org.osgi:org.osgi.service.coordinator
,  OSGi Companion Code for
,  org.osgi.service.coordinator Version 1.0.2.
org.osgi.service.device,org.osgi:org.osgi.service.device
,  OSGi Companion Code for
,  org.osgi.service.device Version 1.1.1
org.osgi.service.event,org.osgi:org.osgi.service.event
,  OSGi Companion Code for org.osgi.service.event
,  Version 1.4.1
org.osgi.service.log.stream,org.osgi:org.osgi.service.log.stream
,  OSGi Companion Code for
,  org.osgi.service.log.stream Version 1.0.0
org.osgi.service.metatype,org.osgi:org.osgi.service.metatype
,  OSGi Companion Code for
,  org.osgi.service.metatype Version 1.4.1
org.osgi.service.metatype.annotations,org.osgi:org.osgi.service.metatype.annotations
,  OSGi Companion Code for
,  org.osgi.service.metatype.annotations Version
,  1.4.1
org.osgi.service.prefs,org.osgi:org.osgi.service.prefs
,  OSGi Companion Code for org.osgi.service.prefs
,  Version 1.1.2
org.osgi.service.provisioning,org.osgi:org.osgi.service.provisioning
,  OSGi Companion Code for
,  org.osgi.service.provisioning Version 1.2.0.
org.osgi.service.upnp,org.osgi:org.osgi.service.upnp
,  OSGi Companion Code for org.osgi.service.upnp
,  Version 1.2.1
org.osgi.service.useradmin,org.osgi:org.osgi.service.useradmin
,  OSGi Companion Code for
,  org.osgi.service.useradmin Version 1.1.1
org.osgi.service.wireadmin,org.osgi:org.osgi.service.wireadmin
,  OSGi Companion Code for
,  org.osgi.service.wireadmin Version 1.0.2
org.osgi.util.function,org.osgi:org.osgi.util.function
,  OSGi Companion Code for org.osgi.util.function
,  Version 1.2.0
org.osgi.util.measurement,org.osgi:org.osgi.util.measurement
,  OSGi Companion Code for
,  org.osgi.util.measurement Version 1.0.2
org.osgi.util.position,org.osgi:org.osgi.util.position
,  OSGi Companion Code for org.osgi.util.position
,  Version 1.0.1.
org.osgi.util.promise,org.osgi:org.osgi.util.promise
,  OSGi Companion Code for org.osgi.util.promise
,  Version 1.2.0
org.osgi.util.pushstream,org.osgi:org.osgi.util.pushstream
,  OSGi Companion Code for
,  org.osgi.util.pushstream Version 1.0.2
org.osgi.util.xml,org.osgi:org.osgi.util.xml
,  OSGi Companion Code for org.osgi.util.xml
,  Version 1.0.2
org.sat4j.core,%bundleName
org.sat4j.pb,%bundleName
org.tukaani.xz,XZ data compression
org.w3c.css.sac,W3C CSS SAC
org.w3c.dom.events,W3C DOM Level 3 Events
org.w3c.dom.smil,W3C SMIL DOM
org.w3c.dom.svg,W3C SVG DOM
slf4j.api,slf4j-api
,  The slf4j API
slf4j.nop,slf4j-nop
,  SLF4J NOP Binding
╔═ defaultP2/installed ═╗
0 unmet requirement(s), 6 ambigous requirement(s). For more info: `gradlew equoList --problems`
50 optional requirement(s) were not installed. For more info: `gradlew equoList --optional`
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
| org.apiguardian:apiguardian-api:1.1.2                                           | maven central          |
| org.apiguardian:apiguardian-api:1.1.2                                           | maven central          |
| org.bouncycastle:bcpg-jdk18on:1.72                                              | maven central          |
| org.bouncycastle:bcprov-jdk18on:1.72                                            | maven central          |
| org.eclipse.jetty:jetty-http:10.0.12                                            | maven central          |
| org.eclipse.jetty:jetty-io:10.0.12                                              | maven central          |
| org.eclipse.jetty:jetty-security:10.0.12                                        | maven central          |
| org.eclipse.jetty:jetty-server:10.0.12                                          | maven central          |
| org.eclipse.jetty:jetty-servlet:10.0.12                                         | maven central          |
| org.eclipse.jetty:jetty-util-ajax:10.0.12                                       | maven central          |
| org.eclipse.jetty:jetty-util:10.0.12                                            | maven central          |
| org.junit.jupiter:junit-jupiter-api:5.9.1                                       | maven central          |
| org.junit.jupiter:junit-jupiter-api:5.9.1                                       | maven central          |
| org.junit.jupiter:junit-jupiter-engine:5.9.1                                    | maven central          |
| org.junit.jupiter:junit-jupiter-engine:5.9.1                                    | maven central          |
| org.junit.jupiter:junit-jupiter-migrationsupport:5.9.1                          | maven central          |
| org.junit.jupiter:junit-jupiter-migrationsupport:5.9.1                          | maven central          |
| org.junit.jupiter:junit-jupiter-params:5.9.1                                    | maven central          |
| org.junit.jupiter:junit-jupiter-params:5.9.1                                    | maven central          |
| org.junit.platform:junit-platform-commons:1.9.1                                 | maven central          |
| org.junit.platform:junit-platform-commons:1.9.1                                 | maven central          |
| org.junit.platform:junit-platform-engine:1.9.1                                  | maven central          |
| org.junit.platform:junit-platform-engine:1.9.1                                  | maven central          |
| org.junit.platform:junit-platform-launcher:1.9.1                                | maven central          |
| org.junit.platform:junit-platform-launcher:1.9.1                                | maven central          |
| org.junit.platform:junit-platform-runner:1.9.1                                  | maven central          |
| org.junit.platform:junit-platform-runner:1.9.1                                  | maven central          |
| org.junit.platform:junit-platform-suite-api:1.9.1                               | maven central          |
| org.junit.platform:junit-platform-suite-api:1.9.1                               | maven central          |
| org.junit.platform:junit-platform-suite-commons:1.9.1                           | maven central          |
| org.junit.platform:junit-platform-suite-commons:1.9.1                           | maven central          |
| org.junit.platform:junit-platform-suite-engine:1.9.1                            | maven central          |
| org.junit.platform:junit-platform-suite-engine:1.9.1                            | maven central          |
| org.junit.vintage:junit-vintage-engine:5.9.1                                    | maven central          |
| org.junit.vintage:junit-vintage-engine:5.9.1                                    | maven central          |
| org.opentest4j:opentest4j:1.2.0                                                 | maven central          |
| org.opentest4j:opentest4j:1.2.0                                                 | maven central          |
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
| org.eclipse.jdt:org.eclipse.jdt.annotation:2.2.700                              | maven central?         |
| org.eclipse.jdt:org.eclipse.jdt.apt.core:3.7.50                                 | maven central?         |
| org.eclipse.jdt:org.eclipse.jdt.apt.pluggable.core:1.3.0                        | maven central?         |
| org.eclipse.jdt:org.eclipse.jdt.apt.ui:3.7.0                                    | maven central?         |
| org.eclipse.jdt:org.eclipse.jdt.compiler.apt:1.4.300                            | maven central?         |
| org.eclipse.jdt:org.eclipse.jdt.compiler.tool:1.3.200                           | maven central?         |
| org.eclipse.jdt:org.eclipse.jdt.core.formatterapp:1.1.0                         | maven central?         |
| org.eclipse.jdt:org.eclipse.jdt.core.manipulation:1.17.0                        | maven central?         |
| org.eclipse.jdt:org.eclipse.jdt.core:3.32.0                                     | maven central?         |
| org.eclipse.jdt:org.eclipse.jdt.debug.ui:3.12.900                               | maven central?         |
| org.eclipse.jdt:org.eclipse.jdt.debug:3.20.0                                    | maven central?         |
| org.eclipse.jdt:org.eclipse.jdt.doc.isv:3.14.1800                               | maven central?         |
| org.eclipse.jdt:org.eclipse.jdt.doc.user:3.15.1600                              | maven central?         |
| org.eclipse.jdt:org.eclipse.jdt.junit.core:3.11.500                             | maven central?         |
| org.eclipse.jdt:org.eclipse.jdt.junit.runtime:3.7.0                             | maven central?         |
| org.eclipse.jdt:org.eclipse.jdt.junit4.runtime:1.3.0                            | maven central?         |
| org.eclipse.jdt:org.eclipse.jdt.junit5.runtime:1.1.100                          | maven central?         |
| org.eclipse.jdt:org.eclipse.jdt.junit:3.15.100                                  | maven central?         |
| org.eclipse.jdt:org.eclipse.jdt.launching:3.19.800                              | maven central?         |
| org.eclipse.jdt:org.eclipse.jdt.ui:3.27.100                                     | maven central?         |
| org.eclipse.jdt:org.eclipse.jdt:3.18.1400                                       | maven central?         |
| org.eclipse.platform:org.eclipse.ant.core:3.6.500                               | maven central?         |
| org.eclipse.platform:org.eclipse.ant.launching:1.3.400                          | maven central?         |
| org.eclipse.platform:org.eclipse.ant.ui:3.8.300                                 | maven central?         |
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
| org.eclipse.ant.launching.source:1.3.400.v20220718-1722                         | p2 R-4.26-202211231800 |
| org.eclipse.ant.ui.source:3.8.300.v20220718-1722                                | p2 R-4.26-202211231800 |
| org.eclipse.jdt.annotation.source:2.2.700.v20220826-1026                        | p2 R-4.26-202211231800 |
| org.eclipse.jdt.apt.core.source:3.7.50.v20210914-1429                           | p2 R-4.26-202211231800 |
| org.eclipse.jdt.apt.pluggable.core.source:1.3.0.v20210618-1653                  | p2 R-4.26-202211231800 |
| org.eclipse.jdt.apt.ui.source:3.7.0.v20210620-1751                              | p2 R-4.26-202211231800 |
| org.eclipse.jdt.compiler.apt.source:1.4.300.v20221108-0856                      | p2 R-4.26-202211231800 |
| org.eclipse.jdt.compiler.tool.source:1.3.200.v20220802-0458                     | p2 R-4.26-202211231800 |
| org.eclipse.jdt.core.formatterapp.source:1.1.0.v20210618-1653                   | p2 R-4.26-202211231800 |
| org.eclipse.jdt.core.manipulation.source:1.17.0.v20221026-1918                  | p2 R-4.26-202211231800 |
| org.eclipse.jdt.core.source:3.32.0.v20221108-1853                               | p2 R-4.26-202211231800 |
| org.eclipse.jdt.debug.source:3.20.0.v20220922-0905                              | p2 R-4.26-202211231800 |
| org.eclipse.jdt.debug.ui.source:3.12.900.v20221001-0715                         | p2 R-4.26-202211231800 |
| org.eclipse.jdt.junit.core.source:3.11.500.v20221031-1935                       | p2 R-4.26-202211231800 |
| org.eclipse.jdt.junit.runtime.source:3.7.0.v20220609-1843                       | p2 R-4.26-202211231800 |
| org.eclipse.jdt.junit.source:3.15.100.v20220909-2154                            | p2 R-4.26-202211231800 |
| org.eclipse.jdt.junit4.runtime.source:1.3.0.v20220609-1843                      | p2 R-4.26-202211231800 |
| org.eclipse.jdt.junit5.runtime.source:1.1.100.v20220907-0450                    | p2 R-4.26-202211231800 |
| org.eclipse.jdt.launching.source:3.19.800.v20221107-1851                        | p2 R-4.26-202211231800 |
| org.eclipse.jdt.ui.source:3.27.100.v20221122-0749                               | p2 R-4.26-202211231800 |
| org.hamcrest.core.source:1.3.0.v20180420-1519                                   | p2 R-4.26-202211231800 |
| org.hamcrest.core:1.3.0.v20180420-1519                                          | p2 R-4.26-202211231800 |
| org.junit.source:4.13.2.v20211018-1956                                          | p2 R-4.26-202211231800 |
| org.junit:4.13.2.v20211018-1956                                                 | p2 R-4.26-202211231800 |
| org.w3c.css.sac:1.3.1.v200903091627                                             | p2 R-4.26-202211231800 |
| org.w3c.dom.events:3.0.0.draft20060413_v201105210656                            | p2 R-4.26-202211231800 |
| org.w3c.dom.smil:1.0.1.v200903091627                                            | p2 R-4.26-202211231800 |
| org.w3c.dom.svg:1.1.0.v201011041433                                             | p2 R-4.26-202211231800 |
+---------------------------------------------------------------------------------+------------------------+
╔═ defaultP2/optional ═╗
+--------------------------------+------------------------------------+-----------------------------------------------------------+
| requirement (not installed)    | provided by                        | optionally needed by                                      |
+--------------------------------+------------------------------------+-----------------------------------------------------------+
| COM.ibm.netrexx.process        | -- none available --               | á.ant:1.10.12.v20211102-1452                              |
| com.jcraft.jzlib               | -- none available --               | com.jcraft.jsch:0.1.55.v20221112-0806                     |
| com.sun.media.jai.codec        | -- none available --               | á.ant:1.10.12.v20211102-1452                              |
| com.sun.net.ssl.internal.ssl   | -- none available --               | á.ant:1.10.12.v20211102-1452                              |
| com.sun.tools.javah            | -- none available --               | á.ant:1.10.12.v20211102-1452                              |
| com.sun.tools.javah.oldjavah   | -- none available --               | á.ant:1.10.12.v20211102-1452                              |
| gnu.classpath                  | -- none available --               | á.ant:1.10.12.v20211102-1452                              |
| gnu.gcj                        | -- none available --               | á.ant:1.10.12.v20211102-1452                              |
| javax.activation               | -- none available --               | á.ant:1.10.12.v20211102-1452                              |
| javax.mail                     | -- none available --               | á.ant:1.10.12.v20211102-1452                              |
| javax.mail.internet            | -- none available --               | á.ant:1.10.12.v20211102-1452                              |
| javax.media.jai                | -- none available --               | á.ant:1.10.12.v20211102-1452                              |
| jdepend.framework              | -- none available --               | á.ant:1.10.12.v20211102-1452                              |
| jdepend.textui                 | -- none available --               | á.ant:1.10.12.v20211102-1452                              |
| jdepend.xmlui                  | -- none available --               | á.ant:1.10.12.v20211102-1452                              |
| kaffe.util                     | -- none available --               | á.ant:1.10.12.v20211102-1452                              |
| kotlin                         | -- none available --               | junit-jupiter-api:5.9.1                                   |
|                                |                                    | junit-jupiter-params:5.9.1                                |
| kotlin.collections             | -- none available --               | junit-jupiter-api:5.9.1                                   |
| kotlin.jvm.functions           | -- none available --               | junit-jupiter-api:5.9.1                                   |
| kotlin.jvm.internal            | -- none available --               | junit-jupiter-api:5.9.1                                   |
|                                |                                    | junit-jupiter-params:5.9.1                                |
| netrexx.lang                   | -- none available --               | á.ant:1.10.12.v20211102-1452                              |
| á.avalon.framework.logger      | -- none available --               | á.commons.logging:1.2.0.v20180409-1502                    |
| á.bcel.classfile               | -- none available --               | á.ant:1.10.12.v20211102-1452                              |
| á.bsf                          | -- none available --               | á.ant:1.10.12.v20211102-1452                              |
| á.commons.fileupload           | á.commons.commons-fileupload:1.4.0 | ë.http.servlet:1.7.400.v20221006-1531                     |
| á.commons.fileupload.disk      | á.commons.commons-fileupload:1.4.0 | ë.http.servlet:1.7.400.v20221006-1531                     |
| á.commons.fileupload.servlet   | á.commons.commons-fileupload:1.4.0 | ë.http.servlet:1.7.400.v20221006-1531                     |
| á.commons.net.bsd              | -- none available --               | á.ant:1.10.12.v20211102-1452                              |
| á.commons.net.ftp              | -- none available --               | á.ant:1.10.12.v20211102-1452                              |
| á.commons.net.telnet           | -- none available --               | á.ant:1.10.12.v20211102-1452                              |
| á.env                          | -- none available --               | á.ant:1.10.12.v20211102-1452                              |
| á.harmony.luni.util            | -- none available --               | á.ant:1.10.12.v20211102-1452                              |
| á.log                          | -- none available --               | á.commons.logging:1.2.0.v20180409-1502                    |
| á.log4j                        | -- none available --               | á.ant:1.10.12.v20211102-1452                              |
|                                |                                    | á.commons.logging:1.2.0.v20180409-1502                    |
| á.oro.text.regex               | -- none available --               | á.ant:1.10.12.v20211102-1452                              |
| á.regexp                       | -- none available --               | á.ant:1.10.12.v20211102-1452                              |
| á.xalan.trace                  | -- none available --               | á.ant:1.10.12.v20211102-1452                              |
| á.xalan.transformer            | -- none available --               | á.ant:1.10.12.v20211102-1452                              |
| á.xml.resolver                 | -- none available --               | á.ant:1.10.12.v20211102-1452                              |
| á.xml.resolver.helpers         | -- none available --               | á.ant:1.10.12.v20211102-1452                              |
| á.xml.resolver.tools           | -- none available --               | á.ant:1.10.12.v20211102-1452                              |
| org.conscrypt                  | -- none available --               | á.httpcomponents.client5.httpclient5:5.1.3.v20221013-1742 |
|                                |                                    | á.httpcomponents.core5.httpcore5-h2:5.1.4.v20221013-1742  |
| org.eclipse.jetty.jmx          | -- none available --               | org.eclipse.jetty.io:10.0.12                              |
|                                |                                    | org.eclipse.jetty.server:10.0.12                          |
|                                |                                    | org.eclipse.jetty.servlet:10.0.12                         |
| org.eclipse.swt.accessibility2 | -- none available --               | org.eclipse.swt:3.122.0.v20221123-2302                    |
| org.junit.Suite                | -- none available --               | á.ant:1.10.12.v20211102-1452                              |
| sun.nio.ch                     | -- none available --               | á.commons.commons-io:2.11.0                               |
| sun.rmi.rmic                   | -- none available --               | á.ant:1.10.12.v20211102-1452                              |
| sun.tools.javac                | -- none available --               | á.ant:1.10.12.v20211102-1452                              |
| sun.tools.native2ascii         | -- none available --               | á.ant:1.10.12.v20211102-1452                              |
| weblogic                       | -- none available --               | á.ant:1.10.12.v20211102-1452                              |
+--------------------------------+------------------------------------+-----------------------------------------------------------+
á org.apache
ë org.eclipse.equinox
╔═ defaultP2/problems ═╗
0 unmet requirement(s).
6 ambiguous requirement(s).
+------------------------------------------+-------------------------------------------------+-----------+
| ambiguous requirement                    | candidate                                       | installed |
+------------------------------------------+-------------------------------------------------+-----------+
| pkg é.jdt.internal.compiler.apt.dispatch | é.jdt.compiler.apt:1.4.300.v20221108-0856       | [x]       |
|                                          | é.jdt.core.compiler.batch:3.32.0.v20221108-1853 | [ ]       |
| pkg é.jdt.internal.compiler.apt.model    | é.jdt.compiler.apt:1.4.300.v20221108-0856       | [x]       |
|                                          | é.jdt.core.compiler.batch:3.32.0.v20221108-1853 | [ ]       |
| pkg é.jdt.internal.compiler.apt.util     | é.jdt.compiler.apt:1.4.300.v20221108-0856       | [x]       |
|                                          | é.jdt.core.compiler.batch:3.32.0.v20221108-1853 | [ ]       |
| pkg é.jdt.internal.compiler.tool         | é.jdt.compiler.tool:1.3.200.v20220802-0458      | [x]       |
|                                          | é.jdt.core.compiler.batch:3.32.0.v20221108-1853 | [ ]       |
| iu é.jdt.annotation                      | é.jdt.annotation:2.2.700.v20220826-1026         | [x]       |
|                                          | é.jdt.annotation:1.2.100.v20220826-1026         | [ ]       |
| iu é.jdt.annotation.source               | é.jdt.annotation.source:2.2.700.v20220826-1026  | [x]       |
|                                          | é.jdt.annotation.source:1.2.100.v20220826-1026  | [ ]       |
+------------------------------------------+-------------------------------------------------+-----------+
é org.eclipse
╔═ detail ═╗
2 units available with id org.eclipse.jdt.annotation
  2.2.700.v20220826-1026  [x] included by install
  1.2.100.v20220826-1026  [ ] not included by install
+----------------------------------+----------------------------------------------------+
| key                              | value                                              |
+----------------------------------+----------------------------------------------------+
| id                               | org.eclipse.jdt.annotation                         |
| version                          | 2.2.700.v20220826-1026                             |
| maven coordinate                 | org.eclipse.jdt:org.eclipse.jdt.annotation:2.2.700 |
| maven repo                       | maven central?                                     |
| prop artifact-classifier         | osgi.bundle                                        |
| prop maven-artifactId            | org.eclipse.jdt.annotation                         |
| prop maven-groupId               | org.eclipse.jdt                                    |
| prop maven-type                  | eclipse-plugin                                     |
| prop maven-version               | 2.2.700-SNAPSHOT                                   |
| prop org.eclipse.equinox.p2.name | JDT Annotations for Enhanced Null Analysis         |
| ---                              | ---                                                |
| id                               | org.eclipse.jdt.annotation                         |
| version                          | 1.2.100.v20220826-1026                             |
| maven coordinate                 | org.eclipse.jdt:org.eclipse.jdt.annotation:1.2.100 |
| maven repo                       | maven central?                                     |
| prop artifact-classifier         | osgi.bundle                                        |
| prop maven-artifactId            | org.eclipse.jdt.annotation                         |
| prop maven-groupId               | org.eclipse.jdt                                    |
| prop maven-type                  | eclipse-plugin                                     |
| prop maven-version               | 1.2.100-SNAPSHOT                                   |
| prop org.eclipse.equinox.p2.name | JDT Annotations for Enhanced Null Analysis         |
+----------------------------------+----------------------------------------------------+
╔═ help ═╗
Detailed task information for equoList

Path
     :equoList

Type
     EquoListTask (dev.equo.ide.gradle.EquoListTask)

Options
     --all     Lists the id and name of all [categories|features|jars] which meet the filter criteria
               Available values are:
                    categories
                    features
                    jars

     --detail     Lists properties and requirements for all available versions of the given unit id

     --format     Determines output format (can be combined with all other commands)
                  Available values are:
                       ascii
                       csv

     --installed     Lists the jars which were installed

     --optional     Lists any optional requirements which were not installed

     --problems     Lists any problems with the installed jars

     --raw     Lists the raw xml for all available versions of the given unit id

     --request     Lists the full p2 request we are making (helpful for debugging catalog)

Description
     Lists the p2 dependencies of an Eclipse application

Group
     IDE
╔═ installedEmpty/installed ═╗
No jars were specified.
╔═ installedEmpty/problems ═╗
0 unmet requirement(s).
0 ambiguous requirement(s).
╔═ installedSwt/installed ═╗
0 unmet requirement(s), 0 ambigous requirement(s). For more info: `gradlew equoList --problems`
1 optional requirement(s) were not installed. For more info: `gradlew equoList --optional`
+----------------------------------------------+----------------+
| maven coordinate / p2 id                     | repo           |
+----------------------------------------------+----------------+
| org.eclipse.platform:org.eclipse.swt:3.122.0 | maven central? |
+----------------------------------------------+----------------+
╔═ installedSwt/problems ═╗
0 unmet requirement(s).
0 ambiguous requirement(s).
╔═ installedSwtCsv/installed ═╗
0 unmet requirement(s), 0 ambigous requirement(s). For more info: `gradlew equoList --problems`
1 optional requirement(s) were not installed. For more info: `gradlew equoList --optional`
maven coordinate / p2 id,repo
org.eclipse.platform:org.eclipse.swt:3.122.0,maven central?
╔═ installedSwtCsv/problems ═╗
0 unmet requirement(s).
0 ambiguous requirement(s).
╔═ raw ═╗
<unit generation="2" id="org.eclipse.jdt.annotation" singleton="false" version="2.2.700.v20220826-1026">
  <update id="org.eclipse.jdt.annotation" range="[0.0.0,2.2.700.v20220826-1026)" severity="0"/>
  <properties size="9">
    <property name="df_LT.bundleName" value="JDT Annotations for Enhanced Null Analysis"/>
    <property name="df_LT.providerName" value="Eclipse.org"/>
    <property name="org.eclipse.equinox.p2.name" value="%bundleName"/>
    <property name="org.eclipse.equinox.p2.provider" value="%providerName"/>
    <property name="org.eclipse.equinox.p2.bundle.localization" value="bundle"/>
    <property name="maven-groupId" value="org.eclipse.jdt"/>
    <property name="maven-artifactId" value="org.eclipse.jdt.annotation"/>
    <property name="maven-version" value="2.2.700-SNAPSHOT"/>
    <property name="maven-type" value="eclipse-plugin"/>
  </properties>
  <provides size="6">
    <provided name="org.eclipse.jdt.annotation" namespace="org.eclipse.equinox.p2.iu" version="2.2.700.v20220826-1026"/>
    <provided name="org.eclipse.jdt.annotation" namespace="osgi.bundle" version="2.2.700.v20220826-1026"/>
    <provided name="org.eclipse.jdt.annotation" namespace="java.package" version="0.0.0"/>
    <provided name="org.eclipse.jdt.annotation" namespace="osgi.identity" version="2.2.700.v20220826-1026">
      <properties size="1">
        <property name="type" value="osgi.bundle"/>
      </properties>
    </provided>
    <provided name="bundle" namespace="org.eclipse.equinox.p2.eclipse.type" version="1.0.0"/>
    <provided name="df_LT" namespace="org.eclipse.equinox.p2.localization" version="1.0.0"/>
  </provides>
  <requires size="1">
    <requiredProperties match="(&amp;(osgi.ee=JavaSE)(version=1.8))" namespace="osgi.ee">
      <description>
            org.eclipse.jdt.annotation
          </description>
    </requiredProperties>
  </requires>
  <artifacts size="1">
    <artifact classifier="osgi.bundle" id="org.eclipse.jdt.annotation" version="2.2.700.v20220826-1026"/>
  </artifacts>
  <touchpoint id="org.eclipse.equinox.p2.osgi" version="1.0.0"/>
  <touchpointData size="1">
    <instructions size="1">
      <instruction key="manifest">
            Bundle-SymbolicName: org.eclipse.jdt.annotation
Bundle-Version: 2.2.700.v20220826-1026
          </instruction>
    </instructions>
  </touchpointData>
</unit>

<unit generation="2" id="org.eclipse.jdt.annotation" singleton="false" version="1.2.100.v20220826-1026">
  <update id="org.eclipse.jdt.annotation" range="[0.0.0,1.2.100.v20220826-1026)" severity="0"/>
  <properties size="9">
    <property name="df_LT.bundleName" value="JDT Annotations for Enhanced Null Analysis"/>
    <property name="df_LT.providerName" value="Eclipse.org"/>
    <property name="org.eclipse.equinox.p2.name" value="%bundleName"/>
    <property name="org.eclipse.equinox.p2.provider" value="%providerName"/>
    <property name="org.eclipse.equinox.p2.bundle.localization" value="bundle"/>
    <property name="maven-groupId" value="org.eclipse.jdt"/>
    <property name="maven-artifactId" value="org.eclipse.jdt.annotation"/>
    <property name="maven-version" value="1.2.100-SNAPSHOT"/>
    <property name="maven-type" value="eclipse-plugin"/>
  </properties>
  <provides size="6">
    <provided name="org.eclipse.jdt.annotation" namespace="org.eclipse.equinox.p2.iu" version="1.2.100.v20220826-1026"/>
    <provided name="org.eclipse.jdt.annotation" namespace="osgi.bundle" version="1.2.100.v20220826-1026"/>
    <provided name="org.eclipse.jdt.annotation" namespace="java.package" version="0.0.0"/>
    <provided name="org.eclipse.jdt.annotation" namespace="osgi.identity" version="1.2.100.v20220826-1026">
      <properties size="1">
        <property name="type" value="osgi.bundle"/>
      </properties>
    </provided>
    <provided name="bundle" namespace="org.eclipse.equinox.p2.eclipse.type" version="1.0.0"/>
    <provided name="df_LT" namespace="org.eclipse.equinox.p2.localization" version="1.0.0"/>
  </provides>
  <requires size="1">
    <requiredProperties match="(&amp;(osgi.ee=JavaSE)(version=1.8))" namespace="osgi.ee">
      <description>
            org.eclipse.jdt.annotation
          </description>
    </requiredProperties>
  </requires>
  <artifacts size="1">
    <artifact classifier="osgi.bundle" id="org.eclipse.jdt.annotation" version="1.2.100.v20220826-1026"/>
  </artifacts>
  <touchpoint id="org.eclipse.equinox.p2.osgi" version="1.0.0"/>
  <touchpointData size="1">
    <instructions size="1">
      <instruction key="manifest">
            Bundle-SymbolicName: org.eclipse.jdt.annotation
Bundle-Version: 1.2.100.v20220826-1026
          </instruction>
    </instructions>
  </touchpointData>
</unit>
╔═ [end of file] ═╗
