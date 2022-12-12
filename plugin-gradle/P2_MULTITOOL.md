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

[//]: <> (P2MultitoolExamples._01)

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

- A category is the highest-level p2 concept, representing an entire "product"
- A feature is a collection of jars and other features, representing a single "feature"
  - could be a high level feature like `org.eclipse.jdt` (Eclipse Java Development Tools)
  - or a fine-grained feature like `org.eclipse.jdt.ui.unittest.junit` (JUnit Test runner client for UnitTest View)
- A jar is a literal jar. Ultimately, our objective is to get the right jars.

If you run `./gradlew --all=jars` you'll get a *lot* of results. To make things easier, you can filter out results you don't care about.

### filter

```gradle
// build.gradle
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
// build.gradle
equoIde {
  p2repo 'https://download.eclipse.org/eclipse/updates/4.26/'
}
```

[//]: <> (P2MultitoolExamples._02)
```console
user@machine p2-multitool % ./gradlew equoList --installed
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
// build.gradle
equoIde {
  p2repo 'https://download.eclipse.org/eclipse/updates/4.26/'
  install 'org.eclipse.releng.java.languages.categoryIU'
}
```

[//]: <> (P2MultitoolExamples._03)
```console
user@machine p2-multitool % ./gradlew equoList --installed
WARNING!!! 46 unmet requirement(s), 8 ambigous requirement(s). For more info:
WARNING!!! gradlew equoList --problems
+---------------------------------------------------------------------------------+---------------+
| maven coordinate / p2 id                                                        | repo          |
+---------------------------------------------------------------------------------+---------------+
| com.ibm.icu:icu4j:72.1                                                          | mavenCentral  |
| commons-fileupload:commons-fileupload:1.4                                       | mavenCentral  |
| commons-io:commons-io:2.11.0                                                    | mavenCentral  |
...
| org.eclipse.emf:org.eclipse.emf.common:2.27.0                                   | mavenCentral? |
| org.eclipse.emf:org.eclipse.emf.ecore.change:2.14.0                             | mavenCentral? |
| org.eclipse.emf:org.eclipse.emf.ecore.xmi:2.17.0                                | mavenCentral? |
...
| com.jcraft.jsch:0.1.55.v20221112-0806                                           | p2            |
| javax.annotation:1.3.5.v20221112-0806                                           | p2            |
| javax.inject:1.0.0.v20220405-0441                                               | p2            |
+---------------------------------------------------------------------------------+---------------+
```

The first thing to note is that some dependencies come from `mavenCentral`, which means that the p2 metadata declares *exactly* where on `mavenCentral` that jar comes from. Other dependencies come from `mavenCentral?`, which means that [our (possibly flawed) heuristic](https://github.com/equodev/equo-ide/blob/main/solstice/src/main/java/dev/equo/solstice/p2/MavenCentralMapping.java) has identified that the jar is available on `mavenCentral`. And the rest of the jars will come from `p2`.

The second thing to note is the warning at the top:

```
WARNING!!! 46 unmet requirement(s), 8 ambigous requirement(s). For more info:
WARNING!!! gradlew equoList --problems
```

So let's try that!
[//]: <> (P2MultitoolExamples._04)
```console
user@machine p2-multitool % ./gradlew equoList --problems
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

+-------------------------------------------------------------+-----------------------------------------------------------+-----------+
| ambiguous requirement                                       | candidate                                                 | installed |
+-------------------------------------------------------------+-----------------------------------------------------------+-----------+
| java.package:org.eclipse.jdt.internal.compiler.apt.dispatch | org.eclipse.jdt.compiler.apt:1.4.300.v20221108-0856       | [x]       |
|                                                             | org.eclipse.jdt.core.compiler.batch:3.32.0.v20221108-1853 | [ ]       |
| java.package:org.eclipse.jdt.internal.compiler.apt.model    | org.eclipse.jdt.compiler.apt:1.4.300.v20221108-0856       | [x]       |
|                                                             | org.eclipse.jdt.core.compiler.batch:3.32.0.v20221108-1853 | [ ]       |
| java.package:org.eclipse.jdt.internal.compiler.apt.util     | org.eclipse.jdt.compiler.apt:1.4.300.v20221108-0856       | [x]       |
|                                                             | org.eclipse.jdt.core.compiler.batch:3.32.0.v20221108-1853 | [ ]       |
| java.package:org.eclipse.jdt.internal.compiler.tool         | org.eclipse.jdt.compiler.tool:1.3.200.v20220802-0458      | [x]       |
|                                                             | org.eclipse.jdt.core.compiler.batch:3.32.0.v20221108-1853 | [ ]       |
| java.package:org.osgi.service.component.annotations         | org.eclipse.osgi.services:3.11.100.v20221006-1531         | [x]       |
|                                                             | org.osgi.service.component.annotations:1.5.0.202109301733 | [ ]       |
| org.eclipse.equinox.p2.iu:org.eclipse.jdt.annotation        | org.eclipse.jdt.annotation:2.2.700.v20220826-1026         | [x]       |
|                                                             | org.eclipse.jdt.annotation:1.2.100.v20220826-1026         | [ ]       |
| org.eclipse.equinox.p2.iu:org.eclipse.jdt.annotation.source | org.eclipse.jdt.annotation.source:2.2.700.v20220826-1026  | [x]       |
|                                                             | org.eclipse.jdt.annotation.source:1.2.100.v20220826-1026  | [ ]       |
| osgi.bundle:org.eclipse.jdt.annotation                      | org.eclipse.jdt.annotation:2.2.700.v20220826-1026         | [x]       |
|                                                             | org.eclipse.jdt.annotation:1.2.100.v20220826-1026         | [ ]       |
+-------------------------------------------------------------+-----------------------------------------------------------+-----------+
```
