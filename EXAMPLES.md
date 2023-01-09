## Table of Contents

- [Active projects](#active-projects)
  - [Eclipse Java Development Tools (JDT) and Equinox](#eclipse-jdt)
  - [Eclipse C/C++ Development Tooling (CDT)](#eclipse-cdt)
  - [Eclipse Corrosion (Rust)](#eclipse-rust)
  - [Eclipse Groovy Development Tools](#eclipse-groovy)
  - [Eclipse Web Tools Platform](#eclipse-wtp)
  - [Eclipse m2e (Maven integration)](#eclipse-m2e)
  - [Eclipse Buildship (Gradle integration)](#eclipse-buildship)
  - (PR's welcome!)
- [Working examples](#working-examples)
  - [JDT](#working-jdt)
  - [JDT and Buildship](#working-jdt-and-buildship)

## Active projects

A listing of:

- projects which are publishing their artifacts via p2
- their latest p2 repository
- their source code and issue tracker

<a name="eclipse-jdt"></a>
### Eclipse Java Development Tools (JDT) and Equinox

- Latest release https://download.eclipse.org/eclipse/updates/4.26/
- Code & issues
  - https://github.com/eclipse-jdt (Java tooling)
  - https://github.com/eclipse-platform (IDE platform)
  - https://github.com/eclipse-equinox (OSGi runtime)

<a name="eclipse-cdt"></a>
### Eclipse C/C++ Development Tooling (CDT)

- Latest release https://download.eclipse.org/tools/cdt/releases/11.0/cdt-11.0.0/
- Code & issues https://github.com/eclipse-cdt

<a name="eclipse-rust"></a>
### Eclipse Corrosion (Rust)

- Latest release https://download.eclipse.org/corrosion/releases/1.2.4/
- Code & issues https://github.com/eclipse/corrosion

<a name="eclipse-groovy"></a>
### Eclipse Groovy Development Tools

- Latest release https://dist.springsource.org/release/GRECLIPSE/4.8.0/e4.26
  - Find other URLs via https://github.com/groovy/groovy-eclipse/wiki/4.8.0-Release-Notes#update-sites
- Code & issues https://github.com/groovy/groovy-eclipse

### Eclipse Web Tools Platform
- Latest release https://download.eclipse.org/webtools/repository/2022-12/
- Code & issues ?

  <a name="eclipse-m2e"></a>
### Eclipse m2e (Maven integration)

- Latest release https://download.eclipse.org/technology/m2e/releases/2.1.2/
- Code & issues https://github.com/eclipse-m2e/m2e-core

<a name="eclipse-buildship"></a>
### Eclipse Buildship (Gradle integration)

- Latest release https://download.eclipse.org/buildship/updates/e423/releases/3.x/3.1.6.v20220511-1359/ 
- Code & issues https://github.com/eclipse/buildship

## Working examples

The examples below will work just as well in Gradle or Maven, but the Gradle format is shown for brevity. The very first example is shown in both formats to remove any ambiguity.

<a name="working-jdt"></a>
### JDT

```gradle
apply plugin: 'dev.equo.ide'
equoIde {
  p2repo 'https://download.eclipse.org/eclipse/updates/4.26/'
  install 'org.eclipse.platform.ide.categoryIU'
  install 'org.eclipse.releng.java.languages.categoryIU'
}
```

```xml
TODO: MAVEN PLUGIN DOESNT SUPPORT ARBITRARY P2 YET: https://github.com/equodev/equo-ide/issues/25
<plugin>
  <groupId>dev.equo.ide</groupId>
  <artifactId>equo-ide-maven-plugin</artifactId>
  <version>{{ latest version at top of https://github.com/equodev/equo-ide }}</version>
  <configuration>
    <p2repo>https://download.eclipse.org/eclipse/updates/4.26/</p2repo>
    <install>org.eclipse.platform.ide.categoryIU</install>
    <install>org.eclipse.releng.java.languages.categoryIU</install>
  </configuration>
</plugin>
```

<a name="working-jdt-and-buildship"></a>
### JDT and Buildship

```gradle
apply plugin: 'dev.equo.ide'
equoIde {
  p2repo 'https://download.eclipse.org/eclipse/updates/4.26/'
  install 'org.eclipse.platform.ide.categoryIU'
  install 'org.eclipse.releng.java.languages.categoryIU'
  p2repo 'https://download.eclipse.org/buildship/updates/e423/releases/3.x/3.1.6.v20220511-1359/'
  install 'org.eclipse.buildship.feature.group'
}
```