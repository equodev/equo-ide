# <image align="left" src="../.github/equo_logo.svg"> Equo IDE for Gradle

[![Gradle Plugin Portal](https://img.shields.io/gradle-plugin-portal/v/dev.equo.ide?color=blue&label=gradle%20plugin%20portal)](https://plugins.gradle.org/plugin/dev.equo.ide)
[![Changelog](https://img.shields.io/badge/changelog-here-blue)](CHANGELOG.md)
[![Javadoc](https://img.shields.io/badge/javadoc-here-blue)](https://javadoc.io/doc/dev.equo.ide/equo-ide-gradle-plugin)

- a build plugin for Gradle and Maven
- downloads, configures, and launches an instance of the Eclipse IDE
- ensures that all of your devs have a zero-effort and perfectly repeatable IDE setup process
- makes it easy to develop, dogfood, and distribute new IDE plugins

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

## Web browser

By default, SWT uses the system browser (Internet Explorer on Windows, Safari on mac, etc). This means that SWT browser features are different depending on which operating system is being used. To get a consistent modern browser experience, you can replace the SWT system browser with the Equo Chromium browser.

```gradle
equoIde {
  equoChromium()
```

Using Equo Chromium will add `https://dl.equo.dev/chromium-swt-ee/equo-gpl/mvn` to your list of maven repositories, which is used only for the Chromium dependency.

## User plugins

To compile classes against dependencies from p2, you can use `dev.equo.p2deps`. You can install the plugin you are building into a launched IDE using the `dogfood()` command. See [the code for our ChatGPT plugin](https://github.com/equodev/equo-ide-chatgpt) for an example of how you can build, dogfood, and distribute an Eclipse plugin without OSGi or p2.

You can use `plugin.xml` and `.e4xmi` and all of that, but if you prefer to just make method calls without any metadata, you can also use our [`IdeHook` mechanism](../CONTRIBUTING.md#idehook). It's what we use to provide features like the `welcome` hook and automatically importing the current project into Gradle, for example.

```gradle
apply plugin: 'dev.equo.p2deps'
p2deps {
  into (['compileOnly', 'testImplementation'], {
    p2repo 'https://someurl.com/abc'
    install 'whateverYouWant'
    // https://github.com/equodev/equo-ide/blob/main/P2_MULTITOOL.md for more info
 	})
  into 'implementation', {
    // you can also use the catalog to specify p2 repos and install units for you
    // https://github.com/equodev/equo-ide/blob/main/CATALOG.md for more info
    cdt()
    m2e()
  }
}
```

## How it works

Much of the complexity of downloading, running, and modifying the Eclipse IDE is caused OSGi and p2. Equo IDE replaces p2 and OSGi with a simple shim called [Solstice](https://github.com/equodev/equo-ide/tree/main/solstice). This makes it easier and faster to build, debug, and run Eclipse-based applications.

## Limitations

- Java 11+
- Gradle 6.0+
- Eclipse JDT 2022-09 (4.25) or later, though it might work on earlier versions too.