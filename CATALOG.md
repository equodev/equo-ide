### Table of Contents

- [Available in DSL](#available-projects)
  - [`platform`](#platform), [`jdt`](#jdt), [`pde`](#pde)
  - [`gradleBuildship`](#gradle-buildship)
  - [`kotlin`](#kotlin)
  - [`cdt`](#cdt) and [`tmTerminal`](#tmterminal)
  - [`rust`](#rust)
  - [`m2e`](#m2e)
- [Wishlist](#wishlist)
  - [Eclipse Groovy Development Tools](#eclipse-groovy)
  - [Eclipse Web Tools Platform](#eclipse-wtp)
  - (PR's welcome!)

## Available in DSL

The following projects are available in the Gradle and Maven plugin catalog DSL. For example, to use `jdt` in Gradle, you do

```gradle
equoIde {
  jdt()
}
```

and in Maven, you do

```xml
<configuration>
  <jdt/>
</configuration>
```

There might be other aspects you can configure, see the [Gradle DSL](https://github.com/equodev/equo-ide/blob/main/plugin-gradle/src/main/java/dev/equo/ide/gradle/P2ModelDslWithCatalog.java) or [Maven DSL](https://github.com/equodev/equo-ide/blob/main/plugin-maven/src/main/java/dev/equo/ide/maven/AbstractP2MojoWithCatalog.java) for more details. PRs to add plugins to the catalog DSL or even just to the wishlist below are very welcome! See [`CONTRIBUTING.md#catalog-dsl`](https://github.com/equodev/equo-ide/blob/main/CONTRIBUTING.md#catalog.dsl) for details.

This catalog is a useful reference for the p2 urls, code repositories, and issue trackers for each project.

### `platform`

- Latest release https://download.eclipse.org/eclipse/updates/4.26/
- Code & issues
  - https://github.com/eclipse-platform (IDE platform)
  - https://github.com/eclipse-equinox (OSGi runtime)

### `jdt`

- Latest release https://download.eclipse.org/eclipse/updates/4.26/
- Code & issues https://github.com/eclipse-jdt

### `pde`

- Latest release https://download.eclipse.org/eclipse/updates/4.26/
- Code & issues https://github.com/eclipse-pde

<a name="gradle-buildship"></a>
### `gradleBuildship`

- Latest release https://download.eclipse.org/buildship/updates/e423/releases/3.x/3.1.6.v20220511-1359/ 
- Code & issues https://github.com/eclipse/buildship

### `kotlin`

- Latest release https://files.pkg.jetbrains.space/kotlin/p/kotlin-eclipse/main/0.8.21/
  - Note: there is a newer release, `0.8.24`, but it has more bugs than `0.8.21`. 
- Code & issues https://github.com/Kotlin/kotlin-eclipse

### `cdt`

- Latest release https://download.eclipse.org/tools/cdt/releases/11.0/cdt-11.0.0/
- Code & issues https://github.com/eclipse-cdt

### `tmTerminal`

- Latest release https://download.eclipse.org/tools/cdt/releases/11.0/cdt-11.0.0/
- Code & issues https://github.com/eclipse-cdt

### `rust`

- Latest release https://download.eclipse.org/corrosion/releases/1.2.4/
- Code & issues https://github.com/eclipse/corrosion

### `m2e`

- Latest release https://download.eclipse.org/technology/m2e/releases/2.1.2/
- Code & issues https://github.com/eclipse-m2e/m2e-core
- Also a transitive dependency on some jars from
  - https://download.eclipse.org/webtools/downloads/drops/R3.28.0/R-3.28.0-20221120050827/repository/
  - https://download.eclipse.org/lsp4j/updates/releases/0.20.0

## Wishlist

The projects below are not in the catalog yet, but somebody wants them! Feel free to add the plugins you want onto this wishlist. You can also move one of these out of the wishlist and into the Catalog DSL [like so](CONTRIBUTING.md#catalog-dsl).

<a name="eclipse-groovy"></a>
### Eclipse Groovy Development Tools

- Latest release https://dist.springsource.org/release/GRECLIPSE/4.8.0/e4.26
  - Find other URLs via https://github.com/groovy/groovy-eclipse/wiki/4.8.0-Release-Notes#update-sites
- Code & issues https://github.com/groovy/groovy-eclipse

### Eclipse Web Tools Platform
- Latest release https://download.eclipse.org/webtools/repository/2022-12/
- Code & issues ?

<a name="eclipse-m2e"></a>
