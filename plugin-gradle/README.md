# <image align="left" src="../.github/equo_logo.svg"> Equo IDE for Gradle

[![Gradle Plugin Portal](https://img.shields.io/gradle-plugin-portal/v/dev.equo.ide?color=blue&label=gradle%20plugin%20portal)](https://plugins.gradle.org/plugin/dev.equo.ide)
[![Changelog](https://img.shields.io/badge/changelog-here-blue)](CHANGELOG.md)
[![Javadoc](https://img.shields.io/badge/javadoc-here-blue)](https://javadoc.io/doc/dev.equo.ide/equo-ide-gradle-plugin)

- a build plugin for Gradle and Maven
- downloads, configures, and launches an instance of the Eclipse IDE
- ensures that all of your devs have a zero-effort and perfectly repeatable IDE setup process

Use it like this with `gradlew equoIde`

```gradle
plugins {
  id 'dev.equo.ide' version '{{ latest version at top of page }}'
}
equoIde { // launch with gradlew equoIde
  // see https://github.com/equodev/equo-ide/blob/main/CATALOG.md for all available plugins
  jdt()
  gradleBuildship().autoImport('.') // automatically imports "this" project, could also be '../' or something like that
  // or you can add p2 urls and targets
  // https://github.com/equodev/equo-ide/blob/main/P2_MULTITOOL.md for more info
  
  // you can also customize the IDE branding
  branding().title('My IDE')
  branding().icon(file('my_icon.png'))
  branding().splash(file('my_splash.png'))
  // and make your own plugins, without learning OSGi or p2
  welcome().openUrl('https://github.com/me/myproject/CONTRIBUTING.md')
  
  // for catalog entries you can set workspace properties using DSL
  platform().showLineNumbers(true).showWhitespace(true)
  // or you can set arbitrary properties
  workspaceInit 'instance/.metadata/.plugins/org.eclipse.core.runtime/.settings/org.eclipse.ui.editors.prefs', {
    prop 'lineNumberRuler', 'true'
    prop 'showWhitespaceCharacters', 'true'
  }
}
```

## Task listing

- `equoIde` to launch the IDE
  - `--clean` wipes all workspace settings and state before rebuilding and launching.
    - (also revalidates cached p2 data)
  - `--show-console` pipes console output of the launched IDE to the build console.
  - `--init-only` initializes the runtime to check for errors then exits.
  - `--debug-classpath=[names|paths]` dumps the classpath (in order) without starting the application.
  - `--debug-ide` blocks IDE startup and prints instructions to help you attach a remote debugger.
  - `--useAtomos=[true|false]` determines whether to use Atomos
- `equoList` to debug IDE dependencies ([p2 multitool](../P2_MULTITOOL.md))

## User plugins

To compile classes against dependencies from p2, you can use `dev.equo.p2deps`.

```gradle
apply plugin: 'dev.equo.p2deps'
p2deps {
  into 'compileOnly', {
    p2repo 'https://download.eclipse.org/eclipse/updates/4.26/'
    install 'org.eclipse.platform.ide.categoryIU'
  }
  into 'buildshipCompileOnly', {
    gradleBuildship()
  }
}
```

Note that you can use the full p2 metadata (`p2repo` and `install`) or the [catalog notation](../CATALOG.md) (as in `gradleBuildship`). When you install a p2 artifact, you also get all of its transitive dependencies according to its p2 metadata. You can debug this process using the [p2 multitool](../P2_MULTITOOL.md). (See [#100](https://github.com/equodev/equo-ide/issues/100) for a bit more info).

You can write plugins for the Eclipse IDE without creating any OSGi or plugin.xml metadata using our [`IdeHook` mechanism](../CONTRIBUTING.md#idehook). It's what we use to provide features like the `welcome` hook and automatically importing the current project into Gradle, for example.

## How it works

Much of the complexity of downloading, running, and modifying the Eclipse IDE is caused OSGi and p2. Equo IDE replaces p2 and OSGi with a simple shim called [Solstice](https://github.com/equodev/equo-ide/tree/main/solstice). This makes it easier and faster to build, debug, and run Eclipse-based applications.

## Limitations

- Java 11+
- Gradle 6.0+
- Eclipse JDT 2022-09 (4.25) or later, though it might work on earlier versions too.