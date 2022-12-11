# P2 Multitool

The EquoIDE gradle plugin can help you browse and debug p2 repositories. The maven plugin will get these features someday ([#25](https://github.com/equodev/equo-ide/issues/25)), but in the meantime this quickstart assumes that you have zero knowledge of gradle and p2. If you'd like to follow along and modify the examples to suit your problem, just download [`p2multitool.tar.gz`](TODO) and extract it to a directory of your choice - you don't need to install anything else (besides a JDK on your system path).

## Command reference

- `equoList --all=categories`
- `equoList --all=features`
- `equoList --all=jars`
- `equoList --installed`
- `equoList --detail=any.unit.id`
- `equoList --raw=any.unit.id`
- (any command) `--format=csv` to output CSV instead of the default `ascii` table

## Quickstart

### `equoList --all=categories` (or `features` or `jars`)

```gradle
// build.gradle
plugins { id 'dev.equo.ide' version '0.4.0' }
equoIde {
  p2repo 'https://download.eclipse.org/eclipse/updates/4.26/'
}
```

To start, `cd` into the multitool directory where you'll see the `build.gradle` and then run `./gradlew  equoList --all=categories`.

- (On windows, replace `ls` with `dir` and replace `./gradlew` with `gradlew`)

```console
user@machine p2-multitool % ls
build           build.gradle    gradle          gradlew         gradlew.bat     P2_MULTITOOL.md
user@machine p2-multitool % ./gradlew equoList --all=categories
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
```

In addition to `--all=categories`, you can also pass `--all=features` or `--all=jars`.

- A category is the largest p2 concept, representing an entire "product"
- A feature is a collection of jars and other features, representing a single "feature"
  - could be a high level feature like `org.eclipse.jdt` (Eclipse Java Development Tools)
  - or a fine-grained feature like `org.eclipse.jdt.ui.unittest.junit` (JUnit Test runner client for UnitTest View)
- A jar is a literal jar. Ultimately, our objective is to get the right jars.

If you run `./gradlew --all=jars` you'll get a *lot* of results. To make things easier, you can filter out results you don't care about.

### filter

```gradle
equoIde {
  p2repo 'https://download.eclipse.org/eclipse/updates/4.26/'
  filter {
    excludeSuffix '.source'  // no source bundles
    excludePrefix 'tooling'  // ignore internal tooling
    exclude 'org.apache.sshd.osgi' // we don't want sshd
  }
}
```

The filters will apply to every command of the `equoList` debugging tool and also to the `equoIde` launch command.

### `equoList --installed`

Ultimately, the point of `equoList` is to make sure we've got all of the jars we need for `equoIde` to run. That is a function of:

- the repositories we specify
- the units we filter out and exclude
- the units we install

```gradle
equoIde {
  p2repo 'https://download.eclipse.org/eclipse/updates/4.26/'
}
```

```console
user@machine p2-multitool % ./gradlew equoList --installed
No ambiguous requirements.
No unmet requirements.
No jars were specified.
```

We haven't specified anything that we want installed, so there are no jars. Looking back at the categories from the first command,

```console
user@machine p2-multitool % ./gradlew equoList --all=categories
...
| org.eclipse.releng.java.languages.categoryIU | Eclipse Java Development Tools                    |
|                                              |   Tools to allow development with Java.           |
...
```

So let's try that one.

```gradle
equoIde {
  p2repo 'https://download.eclipse.org/eclipse/updates/4.26/'
  install 'org.eclipse.releng.java.languages.categoryIU'
}
```

```console
user@machine p2-multitool % ./gradlew equoList --installed
+-------------------------------------------------------------+-----------------------------------------------------------+----------+
| ambiguous requirement                                       | candidate                                                 | resolved |
+-------------------------------------------------------------+-----------------------------------------------------------+----------+
| java.package:org.eclipse.jdt.internal.compiler.apt.dispatch | org.eclipse.jdt.compiler.apt:1.4.300.v20221108-0856       | [x]      |
|                                                             | org.eclipse.jdt.core.compiler.batch:3.32.0.v20221108-1853 | [ ]      |
| java.package:org.eclipse.jdt.internal.compiler.apt.model    | org.eclipse.jdt.compiler.apt:1.4.300.v20221108-0856       | [x]      |
|                                                             | org.eclipse.jdt.core.compiler.batch:3.32.0.v20221108-1853 | [ ]      |
| java.package:org.eclipse.jdt.internal.compiler.apt.util     | org.eclipse.jdt.compiler.apt:1.4.300.v20221108-0856       | [x]      |
|                                                             | org.eclipse.jdt.core.compiler.batch:3.32.0.v20221108-1853 | [ ]      |
| java.package:org.eclipse.jdt.internal.compiler.tool         | org.eclipse.jdt.compiler.tool:1.3.200.v20220802-0458      | [x]      |
|                                                             | org.eclipse.jdt.core.compiler.batch:3.32.0.v20221108-1853 | [ ]      |
| java.package:org.osgi.service.component.annotations         | org.eclipse.osgi.services:3.11.100.v20221006-1531         | [x]      |
|                                                             | org.osgi.service.component.annotations:1.5.0.202109301733 | [ ]      |
| org.eclipse.equinox.p2.iu:org.eclipse.jdt.annotation        | org.eclipse.jdt.annotation:2.2.700.v20220826-1026         | [x]      |
|                                                             | org.eclipse.jdt.annotation:1.2.100.v20220826-1026         | [ ]      |
| org.eclipse.equinox.p2.iu:org.eclipse.jdt.annotation.source | org.eclipse.jdt.annotation.source:2.2.700.v20220826-1026  | [x]      |
|                                                             | org.eclipse.jdt.annotation.source:1.2.100.v20220826-1026  | [ ]      |
| osgi.bundle:org.eclipse.jdt.annotation                      | org.eclipse.jdt.annotation:2.2.700.v20220826-1026         | [x]      |
|                                                             | org.eclipse.jdt.annotation:1.2.100.v20220826-1026         | [ ]      |
+-------------------------------------------------------------+-----------------------------------------------------------+----------+

+-------------------------------------------------+-------------------------------------------------+
| unmet requirement                               | needed by                                       |
+-------------------------------------------------+-------------------------------------------------+
| java.package:COM.ibm.netrexx.process            | org.apache.ant:1.10.12.v20211102-1452           |
| java.package:com.jcraft.jzlib                   | com.jcraft.jsch:0.1.55.v20221112-0806           |
| java.package:com.sun.media.jai.codec            | org.apache.ant:1.10.12.v20211102-1452           |
| java.package:com.sun.net.ssl.internal.ssl       | org.apache.ant:1.10.12.v20211102-1452           |
| java.package:com.sun.tools.javah                | org.apache.ant:1.10.12.v20211102-1452           |
| java.package:com.sun.tools.javah.oldjavah       | org.apache.ant:1.10.12.v20211102-1452           |
| java.package:gnu.classpath                      | org.apache.ant:1.10.12.v20211102-1452           |
| java.package:gnu.gcj                            | org.apache.ant:1.10.12.v20211102-1452           |
| java.package:javax.activation                   | org.apache.ant:1.10.12.v20211102-1452           |
| java.package:javax.mail                         | org.apache.ant:1.10.12.v20211102-1452           |
| java.package:javax.mail.internet                | org.apache.ant:1.10.12.v20211102-1452           |
| java.package:javax.media.jai                    | org.apache.ant:1.10.12.v20211102-1452           |
| java.package:jdepend.framework                  | org.apache.ant:1.10.12.v20211102-1452           |
| java.package:jdepend.textui                     | org.apache.ant:1.10.12.v20211102-1452           |
| java.package:jdepend.xmlui                      | org.apache.ant:1.10.12.v20211102-1452           |
| java.package:kaffe.util                         | org.apache.ant:1.10.12.v20211102-1452           |
| java.package:kotlin                             | junit-jupiter-api:5.9.1                         |
|                                                 | junit-jupiter-params:5.9.1                      |
| java.package:kotlin.collections                 | junit-jupiter-api:5.9.1                         |
| java.package:kotlin.jvm.functions               | junit-jupiter-api:5.9.1                         |
| java.package:kotlin.jvm.internal                | junit-jupiter-api:5.9.1                         |
|                                                 | junit-jupiter-params:5.9.1                      |
| java.package:netrexx.lang                       | org.apache.ant:1.10.12.v20211102-1452           |
| java.package:org.apache.avalon.framework.logger | org.apache.commons.logging:1.2.0.v20180409-1502 |
| java.package:org.apache.bcel.classfile          | org.apache.ant:1.10.12.v20211102-1452           |
| java.package:org.apache.bsf                     | org.apache.ant:1.10.12.v20211102-1452           |
| java.package:org.apache.commons.net.bsd         | org.apache.ant:1.10.12.v20211102-1452           |
| java.package:org.apache.commons.net.ftp         | org.apache.ant:1.10.12.v20211102-1452           |
| java.package:org.apache.commons.net.telnet      | org.apache.ant:1.10.12.v20211102-1452           |
| java.package:org.apache.env                     | org.apache.ant:1.10.12.v20211102-1452           |
| java.package:org.apache.harmony.luni.util       | org.apache.ant:1.10.12.v20211102-1452           |
| java.package:org.apache.log                     | org.apache.commons.logging:1.2.0.v20180409-1502 |
| java.package:org.apache.log4j                   | org.apache.ant:1.10.12.v20211102-1452           |
|                                                 | org.apache.commons.logging:1.2.0.v20180409-1502 |
| java.package:org.apache.oro.text.regex          | org.apache.ant:1.10.12.v20211102-1452           |
| java.package:org.apache.regexp                  | org.apache.ant:1.10.12.v20211102-1452           |
| java.package:org.apache.xalan.trace             | org.apache.ant:1.10.12.v20211102-1452           |
| java.package:org.apache.xalan.transformer       | org.apache.ant:1.10.12.v20211102-1452           |
| java.package:org.apache.xml.resolver            | org.apache.ant:1.10.12.v20211102-1452           |
| java.package:org.apache.xml.resolver.helpers    | org.apache.ant:1.10.12.v20211102-1452           |
| java.package:org.apache.xml.resolver.tools      | org.apache.ant:1.10.12.v20211102-1452           |
| java.package:org.eclipse.jetty.jmx              | org.eclipse.jetty.io:10.0.12                    |
|                                                 | org.eclipse.jetty.server:10.0.12                |
|                                                 | org.eclipse.jetty.servlet:10.0.12               |
| java.package:org.eclipse.swt.accessibility2     | org.eclipse.swt:3.122.0.v20221123-2302          |
| java.package:org.junit.Suite                    | org.apache.ant:1.10.12.v20211102-1452           |
| java.package:sun.nio.ch                         | org.apache.commons.commons-io:2.11.0            |
| java.package:sun.rmi.rmic                       | org.apache.ant:1.10.12.v20211102-1452           |
| java.package:sun.tools.javac                    | org.apache.ant:1.10.12.v20211102-1452           |
| java.package:sun.tools.native2ascii             | org.apache.ant:1.10.12.v20211102-1452           |
| java.package:weblogic                           | org.apache.ant:1.10.12.v20211102-1452           |
+-------------------------------------------------+-------------------------------------------------+

+---------------------------------------------------------------------------------+---------------+
| maven coordinate / p2 id                                                        | repo          |
+---------------------------------------------------------------------------------+---------------+
| com.ibm.icu:icu4j:72.1                                                          | mavenCentral  |
| commons-fileupload:commons-fileupload:1.4                                       | mavenCentral  |
| commons-io:commons-io:2.11.0                                                    | mavenCentral  |
| jakarta.servlet:jakarta.servlet-api:4.0.4                                       | mavenCentral  |
| net.java.dev.jna:jna-platform:5.12.1                                            | mavenCentral  |
| net.java.dev.jna:jna:5.12.1                                                     | mavenCentral  |
| org.apiguardian:apiguardian-api:1.1.2                                           | mavenCentral  |
| org.apiguardian:apiguardian-api:1.1.2                                           | mavenCentral  |
| org.bouncycastle:bcpg-jdk18on:1.72                                              | mavenCentral  |
| org.bouncycastle:bcprov-jdk18on:1.72                                            | mavenCentral  |
| org.eclipse.jetty:jetty-http:10.0.12                                            | mavenCentral  |
| org.eclipse.jetty:jetty-io:10.0.12                                              | mavenCentral  |
| org.eclipse.jetty:jetty-security:10.0.12                                        | mavenCentral  |
| org.eclipse.jetty:jetty-server:10.0.12                                          | mavenCentral  |
| org.eclipse.jetty:jetty-servlet:10.0.12                                         | mavenCentral  |
| org.eclipse.jetty:jetty-util-ajax:10.0.12                                       | mavenCentral  |
| org.eclipse.jetty:jetty-util:10.0.12                                            | mavenCentral  |
| org.junit.jupiter:junit-jupiter-api:5.9.1                                       | mavenCentral  |
| org.junit.jupiter:junit-jupiter-api:5.9.1                                       | mavenCentral  |
| org.junit.jupiter:junit-jupiter-engine:5.9.1                                    | mavenCentral  |
| org.junit.jupiter:junit-jupiter-engine:5.9.1                                    | mavenCentral  |
| org.junit.jupiter:junit-jupiter-migrationsupport:5.9.1                          | mavenCentral  |
| org.junit.jupiter:junit-jupiter-migrationsupport:5.9.1                          | mavenCentral  |
| org.junit.jupiter:junit-jupiter-params:5.9.1                                    | mavenCentral  |
| org.junit.jupiter:junit-jupiter-params:5.9.1                                    | mavenCentral  |
| org.junit.platform:junit-platform-commons:1.9.1                                 | mavenCentral  |
| org.junit.platform:junit-platform-commons:1.9.1                                 | mavenCentral  |
| org.junit.platform:junit-platform-engine:1.9.1                                  | mavenCentral  |
| org.junit.platform:junit-platform-engine:1.9.1                                  | mavenCentral  |
| org.junit.platform:junit-platform-launcher:1.9.1                                | mavenCentral  |
| org.junit.platform:junit-platform-launcher:1.9.1                                | mavenCentral  |
| org.junit.platform:junit-platform-runner:1.9.1                                  | mavenCentral  |
| org.junit.platform:junit-platform-runner:1.9.1                                  | mavenCentral  |
| org.junit.platform:junit-platform-suite-api:1.9.1                               | mavenCentral  |
| org.junit.platform:junit-platform-suite-api:1.9.1                               | mavenCentral  |
| org.junit.platform:junit-platform-suite-commons:1.9.1                           | mavenCentral  |
| org.junit.platform:junit-platform-suite-commons:1.9.1                           | mavenCentral  |
| org.junit.platform:junit-platform-suite-engine:1.9.1                            | mavenCentral  |
| org.junit.platform:junit-platform-suite-engine:1.9.1                            | mavenCentral  |
| org.junit.vintage:junit-vintage-engine:5.9.1                                    | mavenCentral  |
| org.junit.vintage:junit-vintage-engine:5.9.1                                    | mavenCentral  |
| org.opentest4j:opentest4j:1.2.0                                                 | mavenCentral  |
| org.opentest4j:opentest4j:1.2.0                                                 | mavenCentral  |
| org.osgi:org.osgi.service.cm:1.6.1                                              | mavenCentral  |
| org.osgi:org.osgi.service.component:1.5.0                                       | mavenCentral  |
| org.osgi:org.osgi.service.device:1.1.1                                          | mavenCentral  |
| org.osgi:org.osgi.service.event:1.4.1                                           | mavenCentral  |
| org.osgi:org.osgi.service.metatype:1.4.1                                        | mavenCentral  |
| org.osgi:org.osgi.service.prefs:1.1.2                                           | mavenCentral  |
| org.osgi:org.osgi.service.provisioning:1.2.0                                    | mavenCentral  |
| org.osgi:org.osgi.service.upnp:1.2.1                                            | mavenCentral  |
| org.osgi:org.osgi.service.useradmin:1.1.1                                       | mavenCentral  |
| org.osgi:org.osgi.service.wireadmin:1.0.2                                       | mavenCentral  |
| org.osgi:org.osgi.util.function:1.2.0                                           | mavenCentral  |
| org.osgi:org.osgi.util.promise:1.2.0                                            | mavenCentral  |
| org.slf4j:slf4j-api:1.7.36                                                      | mavenCentral  |
| org.slf4j:slf4j-nop:1.7.36                                                      | mavenCentral  |
| org.tukaani:xz:1.9                                                              | mavenCentral  |
| org.eclipse.emf:org.eclipse.emf.common:2.27.0                                   | mavenCentral? |
| org.eclipse.emf:org.eclipse.emf.ecore.change:2.14.0                             | mavenCentral? |
| org.eclipse.emf:org.eclipse.emf.ecore.xmi:2.17.0                                | mavenCentral? |
| org.eclipse.emf:org.eclipse.emf.ecore:2.29.0                                    | mavenCentral? |
| org.eclipse.jdt:org.eclipse.jdt.annotation:2.2.700                              | mavenCentral? |
| org.eclipse.jdt:org.eclipse.jdt.annotation:2.2.700                              | mavenCentral? |
| org.eclipse.jdt:org.eclipse.jdt.apt.core:3.7.50                                 | mavenCentral? |
| org.eclipse.jdt:org.eclipse.jdt.apt.core:3.7.50                                 | mavenCentral? |
| org.eclipse.jdt:org.eclipse.jdt.apt.pluggable.core:1.3.0                        | mavenCentral? |
| org.eclipse.jdt:org.eclipse.jdt.apt.pluggable.core:1.3.0                        | mavenCentral? |
| org.eclipse.jdt:org.eclipse.jdt.apt.ui:3.7.0                                    | mavenCentral? |
| org.eclipse.jdt:org.eclipse.jdt.apt.ui:3.7.0                                    | mavenCentral? |
| org.eclipse.jdt:org.eclipse.jdt.compiler.apt:1.4.300                            | mavenCentral? |
| org.eclipse.jdt:org.eclipse.jdt.compiler.apt:1.4.300                            | mavenCentral? |
| org.eclipse.jdt:org.eclipse.jdt.compiler.tool:1.3.200                           | mavenCentral? |
| org.eclipse.jdt:org.eclipse.jdt.compiler.tool:1.3.200                           | mavenCentral? |
| org.eclipse.jdt:org.eclipse.jdt.core.formatterapp:1.1.0                         | mavenCentral? |
| org.eclipse.jdt:org.eclipse.jdt.core.formatterapp:1.1.0                         | mavenCentral? |
| org.eclipse.jdt:org.eclipse.jdt.core.manipulation:1.17.0                        | mavenCentral? |
| org.eclipse.jdt:org.eclipse.jdt.core.manipulation:1.17.0                        | mavenCentral? |
| org.eclipse.jdt:org.eclipse.jdt.core:3.32.0                                     | mavenCentral? |
| org.eclipse.jdt:org.eclipse.jdt.core:3.32.0                                     | mavenCentral? |
| org.eclipse.jdt:org.eclipse.jdt.debug.ui:3.12.900                               | mavenCentral? |
| org.eclipse.jdt:org.eclipse.jdt.debug.ui:3.12.900                               | mavenCentral? |
| org.eclipse.jdt:org.eclipse.jdt.debug:3.20.0                                    | mavenCentral? |
| org.eclipse.jdt:org.eclipse.jdt.debug:3.20.0                                    | mavenCentral? |
| org.eclipse.jdt:org.eclipse.jdt.doc.isv:3.14.1800                               | mavenCentral? |
| org.eclipse.jdt:org.eclipse.jdt.doc.user:3.15.1600                              | mavenCentral? |
| org.eclipse.jdt:org.eclipse.jdt.junit.core:3.11.500                             | mavenCentral? |
| org.eclipse.jdt:org.eclipse.jdt.junit.core:3.11.500                             | mavenCentral? |
| org.eclipse.jdt:org.eclipse.jdt.junit.runtime:3.7.0                             | mavenCentral? |
| org.eclipse.jdt:org.eclipse.jdt.junit.runtime:3.7.0                             | mavenCentral? |
| org.eclipse.jdt:org.eclipse.jdt.junit4.runtime:1.3.0                            | mavenCentral? |
| org.eclipse.jdt:org.eclipse.jdt.junit4.runtime:1.3.0                            | mavenCentral? |
| org.eclipse.jdt:org.eclipse.jdt.junit5.runtime:1.1.100                          | mavenCentral? |
| org.eclipse.jdt:org.eclipse.jdt.junit5.runtime:1.1.100                          | mavenCentral? |
| org.eclipse.jdt:org.eclipse.jdt.junit:3.15.100                                  | mavenCentral? |
| org.eclipse.jdt:org.eclipse.jdt.junit:3.15.100                                  | mavenCentral? |
| org.eclipse.jdt:org.eclipse.jdt.launching.macosx:3.5.100                        | mavenCentral? |
| org.eclipse.jdt:org.eclipse.jdt.launching.macosx:3.5.100                        | mavenCentral? |
| org.eclipse.jdt:org.eclipse.jdt.launching.ui.macosx:1.3.100                     | mavenCentral? |
| org.eclipse.jdt:org.eclipse.jdt.launching.ui.macosx:1.3.100                     | mavenCentral? |
| org.eclipse.jdt:org.eclipse.jdt.launching:3.19.800                              | mavenCentral? |
| org.eclipse.jdt:org.eclipse.jdt.launching:3.19.800                              | mavenCentral? |
| org.eclipse.jdt:org.eclipse.jdt.ui:3.27.100                                     | mavenCentral? |
| org.eclipse.jdt:org.eclipse.jdt.ui:3.27.100                                     | mavenCentral? |
| org.eclipse.jdt:org.eclipse.jdt:3.18.1400                                       | mavenCentral? |
| org.eclipse.platform:org.eclipse.ant.core:3.6.500                               | mavenCentral? |
| org.eclipse.platform:org.eclipse.ant.launching:1.3.400                          | mavenCentral? |
| org.eclipse.platform:org.eclipse.ant.launching:1.3.400                          | mavenCentral? |
| org.eclipse.platform:org.eclipse.ant.ui:3.8.300                                 | mavenCentral? |
| org.eclipse.platform:org.eclipse.ant.ui:3.8.300                                 | mavenCentral? |
| org.eclipse.platform:org.eclipse.compare.core:3.7.100                           | mavenCentral? |
| org.eclipse.platform:org.eclipse.compare:3.8.500                                | mavenCentral? |
| org.eclipse.platform:org.eclipse.core.commands:3.10.300                         | mavenCentral? |
| org.eclipse.platform:org.eclipse.core.contenttype:3.8.200                       | mavenCentral? |
| org.eclipse.platform:org.eclipse.core.databinding.observable:1.12.100           | mavenCentral? |
| org.eclipse.platform:org.eclipse.core.databinding.property:1.9.100              | mavenCentral? |
| org.eclipse.platform:org.eclipse.core.databinding:1.11.200                      | mavenCentral? |
| org.eclipse.platform:org.eclipse.core.expressions:3.8.200                       | mavenCentral? |
| org.eclipse.platform:org.eclipse.core.externaltools:1.2.300                     | mavenCentral? |
| org.eclipse.platform:org.eclipse.core.filebuffers:3.7.200                       | mavenCentral? |
| org.eclipse.platform:org.eclipse.core.filesystem:1.9.500                        | mavenCentral? |
| org.eclipse.platform:org.eclipse.core.jobs:3.13.200                             | mavenCentral? |
| org.eclipse.platform:org.eclipse.core.net:1.4.0                                 | mavenCentral? |
| org.eclipse.platform:org.eclipse.core.resources:3.18.100                        | mavenCentral? |
| org.eclipse.platform:org.eclipse.core.runtime:3.26.100                          | mavenCentral? |
| org.eclipse.platform:org.eclipse.core.variables:3.5.100                         | mavenCentral? |
| org.eclipse.platform:org.eclipse.debug.core:3.20.0                              | mavenCentral? |
| org.eclipse.platform:org.eclipse.debug.ui:3.17.100                              | mavenCentral? |
| org.eclipse.platform:org.eclipse.e4.core.commands:1.0.300                       | mavenCentral? |
| org.eclipse.platform:org.eclipse.e4.core.contexts:1.11.0                        | mavenCentral? |
| org.eclipse.platform:org.eclipse.e4.core.di.annotations:1.7.200                 | mavenCentral? |
| org.eclipse.platform:org.eclipse.e4.core.di.extensions.supplier:0.16.400        | mavenCentral? |
| org.eclipse.platform:org.eclipse.e4.core.di.extensions:0.17.200                 | mavenCentral? |
| org.eclipse.platform:org.eclipse.e4.core.di:1.8.300                             | mavenCentral? |
| org.eclipse.platform:org.eclipse.e4.core.services:2.3.400                       | mavenCentral? |
| org.eclipse.platform:org.eclipse.e4.emf.xpath:0.3.100                           | mavenCentral? |
| org.eclipse.platform:org.eclipse.e4.ui.bindings:0.13.200                        | mavenCentral? |
| org.eclipse.platform:org.eclipse.e4.ui.css.core:0.13.400                        | mavenCentral? |
| org.eclipse.platform:org.eclipse.e4.ui.css.swt.theme:0.13.200                   | mavenCentral? |
| org.eclipse.platform:org.eclipse.e4.ui.css.swt:0.14.700                         | mavenCentral? |
| org.eclipse.platform:org.eclipse.e4.ui.di:1.4.100                               | mavenCentral? |
| org.eclipse.platform:org.eclipse.e4.ui.dialogs:1.3.400                          | mavenCentral? |
| org.eclipse.platform:org.eclipse.e4.ui.ide:3.16.200                             | mavenCentral? |
| org.eclipse.platform:org.eclipse.e4.ui.model.workbench:2.2.300                  | mavenCentral? |
| org.eclipse.platform:org.eclipse.e4.ui.services:1.5.100                         | mavenCentral? |
| org.eclipse.platform:org.eclipse.e4.ui.widgets:1.3.100                          | mavenCentral? |
| org.eclipse.platform:org.eclipse.e4.ui.workbench.addons.swt:1.4.500             | mavenCentral? |
| org.eclipse.platform:org.eclipse.e4.ui.workbench.renderers.swt:0.15.700         | mavenCentral? |
| org.eclipse.platform:org.eclipse.e4.ui.workbench.swt:0.16.700                   | mavenCentral? |
| org.eclipse.platform:org.eclipse.e4.ui.workbench3:0.16.100                      | mavenCentral? |
| org.eclipse.platform:org.eclipse.e4.ui.workbench:1.14.0                         | mavenCentral? |
| org.eclipse.platform:org.eclipse.equinox.app:1.6.200                            | mavenCentral? |
| org.eclipse.platform:org.eclipse.equinox.bidi:1.4.200                           | mavenCentral? |
| org.eclipse.platform:org.eclipse.equinox.common:3.17.0                          | mavenCentral? |
| org.eclipse.platform:org.eclipse.equinox.frameworkadmin.equinox:1.2.200         | mavenCentral? |
| org.eclipse.platform:org.eclipse.equinox.frameworkadmin:2.2.100                 | mavenCentral? |
| org.eclipse.platform:org.eclipse.equinox.http.jetty:3.8.200                     | mavenCentral? |
| org.eclipse.platform:org.eclipse.equinox.http.servlet:1.7.400                   | mavenCentral? |
| org.eclipse.platform:org.eclipse.equinox.p2.artifact.repository:1.4.600         | mavenCentral? |
| org.eclipse.platform:org.eclipse.equinox.p2.core:2.9.200                        | mavenCentral? |
| org.eclipse.platform:org.eclipse.equinox.p2.engine:2.7.500                      | mavenCentral? |
| org.eclipse.platform:org.eclipse.equinox.p2.jarprocessor:1.2.300                | mavenCentral? |
| org.eclipse.platform:org.eclipse.equinox.p2.metadata.repository:1.4.100         | mavenCentral? |
| org.eclipse.platform:org.eclipse.equinox.p2.metadata:2.6.300                    | mavenCentral? |
| org.eclipse.platform:org.eclipse.equinox.p2.repository:2.6.300                  | mavenCentral? |
| org.eclipse.platform:org.eclipse.equinox.preferences:3.10.100                   | mavenCentral? |
| org.eclipse.platform:org.eclipse.equinox.registry:3.11.200                      | mavenCentral? |
| org.eclipse.platform:org.eclipse.equinox.security:1.3.1000                      | mavenCentral? |
| org.eclipse.platform:org.eclipse.equinox.simpleconfigurator.manipulator:2.2.100 | mavenCentral? |
| org.eclipse.platform:org.eclipse.equinox.simpleconfigurator:1.4.200             | mavenCentral? |
| org.eclipse.platform:org.eclipse.equinox.supplement:1.10.600                    | mavenCentral? |
| org.eclipse.platform:org.eclipse.help.base:4.3.900                              | mavenCentral? |
| org.eclipse.platform:org.eclipse.help.ui:4.4.100                                | mavenCentral? |
| org.eclipse.platform:org.eclipse.help:3.9.100                                   | mavenCentral? |
| org.eclipse.platform:org.eclipse.jface.databinding:1.14.0                       | mavenCentral? |
| org.eclipse.platform:org.eclipse.jface.text:3.22.0                              | mavenCentral? |
| org.eclipse.platform:org.eclipse.jface:3.28.0                                   | mavenCentral? |
| org.eclipse.platform:org.eclipse.ltk.core.refactoring:3.13.0                    | mavenCentral? |
| org.eclipse.platform:org.eclipse.ltk.ui.refactoring:3.12.200                    | mavenCentral? |
| org.eclipse.platform:org.eclipse.osgi.services:3.11.100                         | mavenCentral? |
| org.eclipse.platform:org.eclipse.osgi:3.18.200                                  | mavenCentral? |
| org.eclipse.platform:org.eclipse.search:3.14.300                                | mavenCentral? |
| org.eclipse.platform:org.eclipse.swt.cocoa.macosx.aarch64:3.122.0               | mavenCentral? |
| org.eclipse.platform:org.eclipse.swt:3.122.0                                    | mavenCentral? |
| org.eclipse.platform:org.eclipse.team.core:3.9.600                              | mavenCentral? |
| org.eclipse.platform:org.eclipse.team.ui:3.9.500                                | mavenCentral? |
| org.eclipse.platform:org.eclipse.text:3.12.300                                  | mavenCentral? |
| org.eclipse.platform:org.eclipse.ui.cheatsheets:3.7.500                         | mavenCentral? |
| org.eclipse.platform:org.eclipse.ui.console:3.11.400                            | mavenCentral? |
| org.eclipse.platform:org.eclipse.ui.editors:3.14.400                            | mavenCentral? |
| org.eclipse.platform:org.eclipse.ui.externaltools:3.5.200                       | mavenCentral? |
| org.eclipse.platform:org.eclipse.ui.forms:3.11.500                              | mavenCentral? |
| org.eclipse.platform:org.eclipse.ui.ide:3.20.0                                  | mavenCentral? |
| org.eclipse.platform:org.eclipse.ui.intro:3.6.600                               | mavenCentral? |
| org.eclipse.platform:org.eclipse.ui.navigator.resources:3.8.500                 | mavenCentral? |
| org.eclipse.platform:org.eclipse.ui.navigator:3.10.400                          | mavenCentral? |
| org.eclipse.platform:org.eclipse.ui.views.properties.tabbed:3.9.300             | mavenCentral? |
| org.eclipse.platform:org.eclipse.ui.views:3.11.300                              | mavenCentral? |
| org.eclipse.platform:org.eclipse.ui.workbench.texteditor:3.16.600               | mavenCentral? |
| org.eclipse.platform:org.eclipse.ui.workbench:3.127.0                           | mavenCentral? |
| org.eclipse.platform:org.eclipse.ui:3.201.200                                   | mavenCentral? |
| org.eclipse.platform:org.eclipse.urischeme:1.2.200                              | mavenCentral? |
| com.jcraft.jsch:0.1.55.v20221112-0806                                           | p2            |
| javax.annotation:1.3.5.v20221112-0806                                           | p2            |
| javax.inject:1.0.0.v20220405-0441                                               | p2            |
| org.apache.ant:1.10.12.v20211102-1452                                           | p2            |
| org.apache.batik.constants:1.16.0.v20221027-0840                                | p2            |
| org.apache.batik.css:1.16.0.v20221027-0840                                      | p2            |
| org.apache.batik.i18n:1.16.0.v20221027-0840                                     | p2            |
| org.apache.batik.util:1.16.0.v20221027-0840                                     | p2            |
| org.apache.commons.jxpath:1.3.0.v200911051830                                   | p2            |
| org.apache.commons.logging:1.2.0.v20180409-1502                                 | p2            |
| org.apache.lucene.analyzers-common:8.4.1.v20221112-0806                         | p2            |
| org.apache.lucene.analyzers-smartcn:8.4.1.v20221112-0806                        | p2            |
| org.apache.lucene.core:8.4.1.v20221112-0806                                     | p2            |
| org.apache.xmlgraphics:2.7.0.v20221018-0736                                     | p2            |
| org.hamcrest.core.source:1.3.0.v20180420-1519                                   | p2            |
| org.hamcrest.core:1.3.0.v20180420-1519                                          | p2            |
| org.junit.source:4.13.2.v20211018-1956                                          | p2            |
| org.junit:4.13.2.v20211018-1956                                                 | p2            |
| org.w3c.css.sac:1.3.1.v200903091627                                             | p2            |
| org.w3c.dom.smil:1.0.1.v200903091627                                            | p2            |
| org.w3c.dom.svg:1.1.0.v201011041433                                             | p2            |
+---------------------------------------------------------------------------------+---------------+
```