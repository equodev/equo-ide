# P2 Multitool

[//]: <> (UPDATE CHECKLIST)
[//]: <> (update version in plugin-gradle/example-gradle/build.gradle)
[//]: <> ($ rm *.tar)
[//]: <> ($ ./gradlew p2multitool)
[//]: <> ($ cp p2multi<tab> p2multitool-latest.tar)
[//]: <> (update link in paragraph immediately below)
[//]: <> (update version in paragraph quickstart)

The EquoIDE gradle plugin can help you browse and debug p2 repositories. The maven plugin will get these features someday ([#25](https://github.com/equodev/equo-ide/issues/25)), but in the meantime this quickstart assumes that you have zero knowledge of gradle and p2. If you'd like to follow along and modify the examples to suit your problem, just download [`p2multitool-0.5.0.tar`](https://github.com/equodev/equo-ide/raw/main/p2multitool-0.5.0.tar) and extract it to a directory of your choice - you don't need to install anything else (besides a JDK on your system path).

## Command reference

- [`equoList --all=categories`](#equolist-all)
  - `equoList --all=features`
  - `equoList --all=jars`
- [`equoList --installed`](#equolist-installed)
- [`equoList --problems`](#equolist-problems)
- [`equoList --detail=any.unit.id`](#equolist-detail)
- [`equoList --raw=any.unit.id`](#equolist-raw)
- (any command) `--format=csv` to output diff-friendly CSV instead of the default `ascii` table
- `equoIde --init-only`

## Quickstart

<a name="equolist-all"></a>
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

[//]: <> (P2MultitoolExamples._01)
```console
user@machine p2-multitool % ls
build           build.gradle    gradle          gradlew         gradlew.bat     P2_MULTITOOL.md
user@machine p2-multitool % ./gradlew equoList --all=categories
+----------------------------------------------+---------------------------------------------------+
| id                                           | name \n description                               |
+----------------------------------------------+---------------------------------------------------+
...
| org.eclipse.platform.ide.categoryIU          | Eclipse Platform                                  |
|                                              |   Minimum version of Eclipse: no source or API    |
|                                              |   documentation, no PDE or JDT.                   |
| org.eclipse.rcp.categoryIU                   | Eclipse RCP Target Components                     |
|                                              |   Features to use as PDE runtime target, while    |
|                                              |   developing RCP applications.                    |
| org.eclipse.releng.java.languages.categoryIU | Eclipse Java Development Tools                    |
|                                              |   Tools to allow development with Java.           |
| org.eclipse.releng.pde.categoryIU            | Eclipse Plugin Development Tools                  |
|                                              |   Tools to develop bundles, plugins and features. |
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
  addFilter 'exclude-noise', { // 'exclude-noise' is just a label for the end user
    excludeSuffix '.source'  // no source bundles
    excludePrefix 'tooling'  // ignore internal tooling
    exclude 'org.apache.sshd.osgi' // we don't want sshd
  }
}
```

The filters will apply to every command of the `equoList` debugging tool and also to the `equoIde` launch command.

<a name="equolist-installed"></a>
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

So let's try installing that one.

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
+----------------------------------------------------------+------------------------+
| maven coordinate / p2 id                                 | repo                   |
+----------------------------------------------------------+------------------------+
| com.ibm.icu:icu4j:72.1                                   | maven central          |
| commons-fileupload:commons-fileupload:1.4                | maven central          |
| commons-io:commons-io:2.11.0                             | maven central          |
...
| org.eclipse.jdt:org.eclipse.jdt.annotation:2.2.700       | maven central?         |
| org.eclipse.jdt:org.eclipse.jdt.apt.core:3.7.50          | maven central?         |
| org.eclipse.jdt:org.eclipse.jdt.apt.pluggable.core:1.3.0 | maven central?         |
...
| org.apache.ant:1.10.12.v20211102-1452                    | p2 R-4.26-202211231800 |
| org.apache.batik.constants:1.16.0.v20221027-0840         | p2 R-4.26-202211231800 |
| org.apache.batik.css:1.16.0.v20221027-0840               | p2 R-4.26-202211231800 |
+----------------------------------------------------------+------------------------+
```

The first thing to note is that some dependencies come from `mavenCentral`, which means that the p2 metadata declares *exactly* where on `mavenCentral` that jar comes from. Other dependencies come from `mavenCentral?`, which means that [our (possibly flawed) heuristic](https://github.com/equodev/equo-ide/blob/main/solstice/src/main/java/dev/equo/solstice/p2/MavenCentralMapping.java) has identified that the jar is available on `mavenCentral`. And the rest of the jars will come from `p2`.

The second thing to note is the warning at the top:

```
WARNING!!! 46 unmet requirement(s), 8 ambigous requirement(s). For more info:
WARNING!!! gradlew equoList --problems
```

So let's try that!

<a name="equolist-problems"></a>
### `equoList --problems`

[//]: <> (P2MultitoolExamples._04)
```console
user@machine p2-multitool % ./gradlew equoList --problems
+-------------------------------------------+---------------------------------------+
| unmet requirement                         | needed by                             |
+-------------------------------------------+---------------------------------------+
...
| java.package:COM.ibm.netrexx.process      | org.apache.ant:1.10.12.v20211102-1452 |
| java.package:com.jcraft.jzlib             | com.jcraft.jsch:0.1.55.v20221112-0806 |
| java.package:com.sun.tools.javah          | org.apache.ant:1.10.12.v20211102-1452 |
| java.package:org.eclipse.jetty.jmx        | org.eclipse.jetty.io:10.0.12          |
|                                           | org.eclipse.jetty.server:10.0.12      |
|                                           | org.eclipse.jetty.servlet:10.0.12     |
+-------------------------------------------+---------------------------------------+

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

<a name="equolist-detail"></a>
### `equoList --detail=any.unit.id`

Let's dig in to one of the ambiguous parts above - that the java package `org.eclipse.jdt.internal.compiler.apt.dispatch` (and others) is provided by both `org.eclipse.jdt.compiler.apt` and `org.eclipse.jdt.core.compiler.batch`. Which one do we actually want?

[//]: <> (P2MultitoolExamples._05)
```console
user@machine p2-multitool % ./gradlew equoList --detail=org.eclipse.jdt.compiler.apt
1 unit available with id org.eclipse.jdt.compiler.apt
  1.4.300.v20221108-0856  [x] included by install
+--------------------------------------------+-------------------------------------------------------------+
| key                                        | value                                                       |
+--------------------------------------------+-------------------------------------------------------------+
| id                                         | org.eclipse.jdt.compiler.apt                                |
| version                                    | 1.4.300.v20221108-0856                                      |
| maven coordinate                           | org.eclipse.jdt:org.eclipse.jdt.compiler.apt:1.4.300        |
| maven repo                                 | maven central?                                              |
| prop artifact-classifier                   | osgi.bundle                                                 |
| prop maven-artifactId                      | org.eclipse.jdt.compiler.apt                                |
| prop maven-groupId                         | org.eclipse.jdt                                             |
| prop maven-type                            | eclipse-plugin                                              |
| prop maven-version                         | 1.4.300-SNAPSHOT                                            |
| prop org.eclipse.equinox.p2.name           | Java Compiler Apt                                           |
| req org.eclipse.jdt.internal.compiler.tool | 2 available                                                 |
|                                            |   org.eclipse.jdt.compiler.tool:1.3.200.v20220802-0458      |
|                                            |   org.eclipse.jdt.core.compiler.batch:3.32.0.v20221108-1853 |
| req org.eclipse.jdt.core                   | org.eclipse.jdt.core:3.32.0.v20221108-1853                  |
+--------------------------------------------+-------------------------------------------------------------+

user@machine p2-multitool % ./gradlew equoList --detail=org.eclipse.jdt.core.compiler.batch
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
```

Looking at this, it doesn't seem like there's any problem with 


<a name="equolist-raw"></a>
### `equoList --raw=any.unit.id`

In the tables generated by `--detail`, you can see all of the data used by the [Solstice p2 implementation](https://github.com/equodev/equo-ide/tree/main/solstice). But there is a lot of data in the p2 files which are not shown here. For example, requirements (indicated by `req`) don't include version constraints. This is because p2 repositories rarely contain multiple versions of a single bundle, and when they do the answer is almost always to take the latest available version.

If you want to see the original p2 metadata for an artifact, you can do

[//]: <> (P2MultitoolExamples._06)
```console
user@machine p2-multitool % ./gradlew equoList --raw=org.eclipse.jdt.core.compiler.batch
<unit id="org.eclipse.jdt.core.compiler.batch" singleton="false" version="3.32.0.v20221108-1853">
  <update id="org.eclipse.jdt.core.compiler.batch" range="[0.0.0,3.32.0.v20221108-1853)" severity="0"/>
  <properties size="7">
    <property name="org.eclipse.equinox.p2.name" value="Eclipse Compiler for Java(TM)"/>
    <property name="org.eclipse.equinox.p2.provider" value="Eclipse.org"/>
    ...
  </properties>
  <provides size="25">
    ...
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
```

If you think there's something important that we're ignoring here, [let us know](https://github.com/equodev/equo-ide/discussions)!