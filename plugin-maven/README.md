# <image align="left" src="../.github/equo_logo.svg"> Equo IDE for Maven

[![Maven Plugin](https://img.shields.io/maven-central/v/dev.equo.ide/equo-ide-maven-plugin?color=blue&label=maven%20plugin)](https://search.maven.org/artifact/dev.equo.ide/equo-ide-maven-plugin)
[![Changelog](https://img.shields.io/badge/changelog-here-blue)](CHANGELOG.md)
[![Javadoc](https://img.shields.io/badge/javadoc-here-blue)](https://javadoc.io/doc/dev.equo.ide/equo-ide-maven-plugin)

**PUBLIC BETA! Try it out, but it's not production ready yet. [Join our mailing list](https://equo.dev/ide) to be notified when it's ready.**

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
     <release>4.26</release>
  </configuration>
</plugin>

<!-- or use equo on all your maven projects by putting this into ~/.m2/settings.xml -->
<settings> 
  <pluginGroups>
    <pluginGroup>dev.equo.ide</pluginGroup>
  </pluginGroups>
  ...
```

## Coming soon

- A p2 multitool for Eclipse ecosystem developers ([#25](https://github.com/equodev/equo-ide/issues/25))
- Use m2e to import this maven project ([#18](https://github.com/equodev/equo-ide/issues/18))

## Task listing

- `equo-ide:launch` to launch
    - `equoIde -DinitOnly=true` initializes the runtime to check for errors then exits.
    - `equoIde -DshowConsole=true` pipes console output of launched IDE to the build console.
    - `equoIde -DdebugClasspath=[names|paths]` dumps the classpath (in order) without starting the application.

## Limitations

- Java 11+
- Maven 3.0+
- Eclipse JDT 2022-09 (4.25) or later, though it might work on earlier versions too.