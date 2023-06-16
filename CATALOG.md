### Table of Contents

- [Available in DSL](#available-projects)
  - Eclipse core
    - [`platform`](#platform), [`jdt`](#jdt), [`pde`](#pde), [`egit`](#egit)
  - Build systems
    - Gradle: [`gradleBuildship`](#gradle-buildship)
    - Maven: [`m2e`](#m2e)
  - AI assistants
    - [`assistAI`](#assistai) (ChatGPT)
    - [`tabnine`]
  - Languages besides Java
    - [`kotlin`](#kotlin)
    - [`cdt`](#cdt) and [`tmTerminal`](#tmterminal)
    - [`rust`](#rust)
    - [`groovy`](#groovy)
- [Wishlist](#wishlist)
  - [`wtp`](#wtp)
  - [`tm4e`](#tm4e) 
  - (PR's welcome!)

## Available in DSL

The following projects are available in the [Gradle](https://github.com/equodev/equo-ide/tree/main/plugin-gradle) and [Maven](https://github.com/equodev/equo-ide/tree/main/plugin-maven) plugin catalog DSL. For example, to use `jdt` in Gradle, you do

```gradle
equoIde {
  jdt().classpathVariable('JUNIT_HOME', '/home/user/junit')
}
```

and in Maven, you do

```xml
<configuration>
  <jdt>
    <classpathVariable>
      <JUNIT_HOME>/home/user/junit</JUNIT_HOME>
    </classpathVariable>
  </jdt>
</configuration>
```

There might be other aspects you can configure, see the [Gradle DSL](https://github.com/equodev/equo-ide/blob/main/plugin-gradle/src/main/java/dev/equo/ide/gradle/P2ModelDslWithCatalog.java) or [Maven DSL](https://github.com/equodev/equo-ide/blob/main/plugin-maven/src/main/java/dev/equo/ide/maven/AbstractP2MojoWithCatalog.java) for more details. PRs to add plugins to the catalog DSL or even just to the wishlist below are very welcome! See [`CONTRIBUTING.md#catalog-dsl`](https://github.com/equodev/equo-ide/blob/main/CONTRIBUTING.md#catalog-dsl) for details.

This catalog is a useful reference for the p2 urls, code repositories, and issue trackers for each project.

## Eclipse core

### `platform`

- [`CatalogPlatform.java`](https://github.com/equodev/equo-ide/blob/main/solstice/src/main/java/dev/equo/ide/CatalogPlatform.java)
- Code & issues
  - https://github.com/eclipse-platform (IDE platform)
  - https://github.com/eclipse-equinox (OSGi runtime)

### `jdt`

- [`CatalogJdt.java`](https://github.com/equodev/equo-ide/blob/main/solstice/src/main/java/dev/equo/ide/CatalogJdt.java)
- Code & issues https://github.com/eclipse-jdt

### `pde`

- [`CatalogPde.java`](https://github.com/equodev/equo-ide/blob/main/solstice/src/main/java/dev/equo/ide/CatalogPde.java)
- Code & issues https://github.com/eclipse-pde

### `egit`
- Latest release https://download.eclipse.org/egit/updates-6.6/
- Code https://git.eclipse.org/c/egit/egit.git/
- Issues at [bugs.eclipse.org](https://bugs.eclipse.org/bugs/buglist.cgi?bug_file_loc_type=allwordssubstr&bug_status=NEW&bug_status=ASSIGNED&bug_status=REOPENED&bugidtype=include&chfieldto=Now&classification=Technology&cmdtype=doit&emailtype1=exact&emailtype2=substring&field0-0-0=noop&keywords_type=allwords&long_desc_type=allwordssubstr&order=Reuse%20same%20sort%20as%20last%20time&product=EGit&query_format=advanced&short_desc_type=allwordssubstr&status_whiteboard_type=allwordssubstr&type0-0-0=noop)

## Build systems

<a name="gradle-buildship"></a>
### `gradleBuildship`

- Latest release https://download.eclipse.org/buildship/updates/e423/releases/3.x/3.1.6.v20220511-1359/ 
- Code & issues https://github.com/eclipse/buildship

### `m2e`

- Latest release https://download.eclipse.org/technology/m2e/releases/2.3.0/
- Code & issues https://github.com/eclipse-m2e/m2e-core
- Also a transitive dependency on some jars from
  - https://download.eclipse.org/webtools/downloads/drops/R3.30.0/R-3.30.0-20230603084739
  - https://download.eclipse.org/tools/orbit/downloads/drops/R20230531010532/repository/

## AI assistants

### `assistai`

- Latest release https://eclipse-chatgpt-plugin.lm.r.appspot.com/
- Code & issues https://github.com/gradusnikov/eclipse-chatgpt-plugin
- Parameters
  - `modelName` we recommend `gpt-3.5-turbo`, you can see other available at far right under "Model" [here](https://platform.openai.com/playground?mode=chat).
  - `apiKey` you can get your value [here](https://platform.openai.com/account/api-keys).

### `tabnine`

- Latest release https://eclipse-update-site.tabnine.com/

## Languages besides Java

### `kotlin`

- Latest release https://files.pkg.jetbrains.space/kotlin/p/kotlin-eclipse/main/0.8.21/
  - Note: there is a newer release, `0.8.24`, but it has more bugs than `0.8.21`. 
- Code & issues https://github.com/Kotlin/kotlin-eclipse

### `cdt`

- Latest release https://download.eclipse.org/tools/cdt/releases/11.2/cdt-11.2.0/
- Code & issues https://github.com/eclipse-cdt

### `tmTerminal`

- Latest release https://download.eclipse.org/tools/cdt/releases/11.2/cdt-11.2.0/
- Code & issues https://github.com/eclipse-cdt

### `rust`

- Latest release https://download.eclipse.org/corrosion/releases/1.2.4/
- Code & issues https://github.com/eclipse/corrosion

### `groovy`

- Latest release https://groovy.jfrog.io/artifactory/plugins-release/org/codehaus/groovy/groovy-eclipse-integration/4.9.0/e4.27/
  - Find other URLs via https://github.com/groovy/groovy-eclipse/wiki/4.9.0-Release-Notes#update-sites
- Code & issues https://github.com/groovy/groovy-eclipse

## Wishlist

The projects below are not in the catalog yet, but somebody wants them! Feel free to add the plugins you want onto this wishlist. You can also move one of these out of the wishlist and into the Catalog DSL [like so](CONTRIBUTING.md#catalog-dsl).

### `wtp`
- Latest release https://download.eclipse.org/webtools/repository/2023-03/
- Code & issues https://www.eclipse.org/webtools/

### `tm4e`

- Latest release https://download.eclipse.org/tm4e/releases/0.6.3/
- Code & issues https://github.com/eclipse/tm4e
