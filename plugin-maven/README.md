# <image align="left" src="../.github/equo_logo.svg"> Equo IDE for Maven

[![Maven Plugin](https://img.shields.io/maven-central/v/dev.equo.ide/equo-ide-maven-plugin?color=blue&label=maven%20plugin)](https://search.maven.org/artifact/dev.equo.ide/equo-ide-maven-plugin)
[![Changelog](https://img.shields.io/badge/changelog-here-blue)](CHANGELOG.md)
[![Javadoc](https://img.shields.io/badge/javadoc-here-blue)](https://javadoc.io/doc/dev.equo.ide/equo-ide-maven-plugin)

**[Join our mailing list](https://equo.dev/ide) for more updates.**

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
    <branding>
      <title>My IDE</title>
      <icon>${project.basedir}/my_icon.png</icon>
      <splash>${project.basedir}/my_splash.png</splash>
    </branding>
    <welcome>
      <openUrl>https://github.com/me/myproject/CONTRIBUTING.md</openUrl>
    </welcome>
    <!-- see https://github.com/equodev/equo-ide/blob/main/CATALOG.md for all available plugins -->
    <jdt/>
    <!-- or you can add specific p2 urls and targets
         https://github.com/equodev/equo-ide/blob/main/P2_MULTITOOL.md for more info -->
  </configuration>
</plugin>
```

## Coming soon

- Use m2e to import this maven project. ([#18](https://github.com/equodev/equo-ide/issues/18))

## Task listing

- `equo-ide:launch` to launch
  - `equoIde -Dclean` wipes all workspace settings and state before rebuilding and launching.
    - (also revalidates cached p2 data)
  - `equoIde -DshowConsole` pipes console output of launched IDE to the build console.
  - `equoIde -DinitOnly` initializes the runtime to check for errors then exits.
  - `equoIde -DdebugClasspath=[names|paths]` dumps the classpath (in order) without starting the application.
  - `equoIde -DdebugIde` blocks IDE startup and prints instructions to help you attach a remote debugger.
  - `equoIde -DuseAtomos=[true|false]` determines whether to use Atomos
- `equo-ide:list` to debug IDE dependencies ([p2 multitool](../P2_MULTITOOL.md))

## User plugins

You can use the [`dev.equo.p2deps` gradle plugin](../plugin-gradle/README.md#user-plugins) to compile against p2 dependencies. We would love help porting this feature into the maven plugin, see [#54](https://github.com/equodev/equo-ide/issues/54).

## Limitations

- Java 11+
- Maven 3.0+
- Eclipse JDT 2022-09 (4.25) or later, though it might work on earlier versions too.