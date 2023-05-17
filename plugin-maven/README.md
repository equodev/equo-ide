# <image align="left" src="../.github/equo_logo.svg"> Equo IDE for Maven

[![Maven Plugin](https://img.shields.io/maven-central/v/dev.equo.ide/equo-ide-maven-plugin?color=blue&label=maven%20plugin)](https://search.maven.org/artifact/dev.equo.ide/equo-ide-maven-plugin)
[![Changelog](https://img.shields.io/badge/changelog-here-blue)](CHANGELOG.md)
[![Javadoc](https://img.shields.io/badge/javadoc-here-blue)](https://javadoc.io/doc/dev.equo.ide/equo-ide-maven-plugin)

- a build plugin for Gradle and Maven
- downloads, configures, and launches an instance of the Eclipse IDE
- ensures that all of your devs have a zero-effort and perfectly repeatable IDE setup process

Use it like this with `mvn equo-ide:launch`

```xml
<plugin><!-- add this to pom.xml/<project><build><plugins> -->
  <groupId>dev.equo.ide</groupId>
  <artifactId>equo-ide-maven-plugin</artifactId>
  <version>{{ latest version at top of page }}</version>
  <configuration>
    <!-- see https://github.com/equodev/equo-ide/blob/main/CATALOG.md for all available plugins -->
    <jdt/>
    <m2e><autoImport>${project.basedir}</autoImport></m2e>
    <!-- or you can add specific p2 urls and targets
         https://github.com/equodev/equo-ide/blob/main/P2_MULTITOOL.md for more info -->

    <!-- you can also customize the IDE branding -->
    <branding>
      <title>My IDE</title>
      <icon>${project.basedir}/my_icon.png</icon>
      <splash>${project.basedir}/my_splash.png</splash>
    </branding>
    <!-- and make your own plugins, without learning OSGi or p2 -->
    <welcome>
      <openUrl>https://github.com/me/myproject/CONTRIBUTING.md</openUrl>
    </welcome>
    
    <!-- for catalog entries you can set workspace properties using DSL -->
    <platform>
      <showLineNumbers>true</showLineNumbers>
      <showWhitespace>true</showWhitespace>
    </platform>
    <!-- or you can set arbitrary properties -->
    <workspaceProps>
      <workspaceProp>
        <path>instance/.metadata/.plugins/org.eclipse.core.runtime/.settings/org.eclipse.ui.editors.prefs</path>
        <key>lineNumberRuler</key>
        <value>true</value>
      </workspaceProp>
      <workspaceProp>
        <path>instance/.metadata/.plugins/org.eclipse.core.runtime/.settings/org.eclipse.ui.editors.prefs</path>
        <key>showWhitespaceCharacters</key>
        <value>true</value>
      </workspaceProp>
    </workspaceProps>
  </configuration>
</plugin>
```

## Task listing

- `equo-ide:launch` to launch
  - `-Dclean` wipes all workspace settings and state before rebuilding and launching.
    - (also revalidates cached p2 data)
  - `-DshowConsole` pipes console output of launched IDE to the build console.
  - `-DinitOnly` initializes the runtime to check for errors then exits.
  - `-DdebugClasspath=[names|paths]` dumps the classpath (in order) without starting the application.
  - `-DdebugIde` blocks IDE startup and prints instructions to help you attach a remote debugger.
  - `-DuseAtomos=[true|false]` determines whether to use Atomos
- `equo-ide:list` to debug IDE dependencies ([p2 multitool](../P2_MULTITOOL.md))

## Web browser

By default, SWT uses the system browser (Internet Explorer on Windows, Safari on mac, etc). This means that SWT browser features are different depending on which operating system is being used. To get a consistent modern browser experience, you can replace the SWT system browser with the Equo Chromium browser.

```xml
<configuration>
  <useChromium>true</useChromium>
```

Using Equo Chromium will add `https://dl.equo.dev/chromium-swt-ee/equo-gpl/mvn` to your list of maven repositories, which is used only for the Chromium dependency.

## User plugins

You can use the [`dev.equo.p2deps` gradle plugin](../plugin-gradle/README.md#user-plugins) to compile against p2 dependencies. We would love help porting this feature into the maven plugin, see [#54](https://github.com/equodev/equo-ide/issues/54).

## How it works

Much of the complexity of downloading, running, and modifying the Eclipse IDE is caused OSGi and p2. Equo IDE replaces p2 and OSGi with a simple shim called [Solstice](https://github.com/equodev/equo-ide/tree/main/solstice). This makes it easier and faster to build, debug, and run Eclipse-based applications.

## Limitations

- Java 11+
- Maven 3.0+
- Eclipse JDT 2022-09 (4.25) or later, though it might work on earlier versions too.