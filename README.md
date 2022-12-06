# <image align="left" src=".github/equo_logo.svg"> Equo IDE: IDE as a build artifact

[![Gradle Plugin](https://img.shields.io/gradle-plugin-portal/v/dev.equo.ide?color=blue&label=gradle%20plugin)](https://plugins.gradle.org/plugin/dev.equo.ide)
[![Maven Plugin](https://img.shields.io/maven-central/v/dev.equo.ide/equo-ide-maven-plugin?color=blue&label=maven%20plugin)](https://central.sonatype.dev/artifact/dev.equo.ide/equo-ide-maven-plugin/0.1.1/versions)
[![Solstice OSGi](https://img.shields.io/maven-central/v/dev.equo.ide/solstice?color=blue&label=solstice%20OSGi)](https://github.com/equodev/equo-ide/tree/main/solstice)

**PUBLIC BETA! Try it out, but it's not production ready yet. [Join our mailing list](https://equo.dev/ide) to be notified when it's ready.**

- a build plugin for Gradle and Maven
- downloads, configures, and launches an instance of the Eclipse IDE
- ensures that all of your devs have a zero-effort and perfectly repeatable IDE setup process

Use it like this in Gradle with `gradlew equoIde`:

```gradle
plugins {
  id 'dev.equo.ide'
}
equoIde { // launch with gradlew equoIde
  release '4.26.0'
}
```

or like this in Maven with `mvn equo-ide:launch`:

```xml
<plugin><!-- add this to pom.xml/<project><build><plugins> -->
    <groupId>dev.equo.ide</groupId>
    <artifactId>equo-ide-maven-plugin</artifactId>
    <version>${equo.ide.version}</version>
    <configuration>
        <release>4.26.0</release>
    </configuration>
</plugin>

<!-- or use equo on all your maven projects by putting this into ~/.m2/settings.xml -->
<settings> 
  <pluginGroups>
     <pluginGroup>dev.equo.ide</pluginGroup>
  </pluginGroups>
  ...
```

## How it works

Much of the complexity of downloading, running, and modifying the Eclipse IDE is caused OSGi and p2. Equo IDE cuts out this complexity by replacing p2 with plain-old maven, and replacing OSGi with a simple shim called [solstice](https://github.com/equodev/equo-ide/tree/main/solstice).

**Currently only the Eclipse Java Development Tools are supported, check out [this issue](https://github.com/equodev/equo-ide/issues/1) to vote on which plugins we should add support for next.**