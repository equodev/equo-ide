# <image align="left" src=".github/equo_logo.svg"> Equo IDE: IDE as a build artifact

[![Gradle Plugin](https://img.shields.io/gradle-plugin-portal/v/dev.equo.ide?color=blue&label=gradle%20plugin)](plugin-gradle)
[![Maven Plugin](https://img.shields.io/maven-central/v/dev.equo.ide/equo-ide-maven-plugin?color=blue&label=maven%20plugin)](plugin-maven)
[![Solstice OSGi](https://img.shields.io/maven-central/v/dev.equo.ide/solstice?color=blue&label=solstice%20OSGi)](solstice)

EquoIDE is:

- a build plugin for [Gradle](plugin-gradle) and [Maven](plugin-maven)
- downloads, configures, and launches an instance of the Eclipse IDE
- ensures that all of your devs have a zero-effort and perfectly repeatable IDE setup process
- makes it easy to develop, dogfood, and distribute new IDE plugins

There are [slides and a recorded presentation available here](https://github.com/equodev/equo-ide/issues/60).

## Demo

The best way to get acquainted with this project is to clone [our ChatGPT plugin](https://github.com/equodev/equo-ide-chatgpt) and launch the dogfooding IDE like so:

```console
user@machine % git clone https://github.com/equodev/equo-ide-chatgpt
user@machine % cd equo-chat-gpt
user@machine equo-ide % ./gradlew equoIde --clean
```

This will download and launch an IDE that shows the code for our ChatGPT plugin. Make changes to the code, restart the IDE with `gradlew equoIde`, and you'll see your change instantly.

## Quickstart

Use it like this in Gradle with `gradlew equoIde` ([more info](plugin-gradle))

```gradle
plugins {
  id 'dev.equo.ide' version '{{ latest version at top of page }}'
}
equoIde { // launch with gradlew equoIde
  jdt()
  gradleBuildship().autoImport('.')
  tabnine() // same idea as GitHub Copilot, but with free and self-hosted versions available
  chatGPT() // use GPT3 & 4 (no waitlist!) to refactor, write tests, and generate documentation 
}
```

or like this in Maven with `mvn equo-ide:launch` ([more info](plugin-maven))

```xml
<plugin><!-- add this to pom.xml/<project><build><plugins> -->
  <groupId>dev.equo.ide</groupId>
  <artifactId>equo-ide-maven-plugin</artifactId>
  <version>{{ latest version at top of page }}</version>
  <configuration>
    <jdt/>
    <m2e><autoImport>${project.basedir}</autoImport></m2e>
    <tabnine/> <!-- same idea as GitHub Copilot, but with free and self-hosted versions available -->
    <chatGPT/> <!-- use GPT3 & 4 (no waitlist!) to refactor, write tests, and generate documentation -->
  </configuration>
</plugin>
```

You can see all the plugins we support in [`CATALOG.md`](CATALOG.md), and we also support any arbitrary maven-based or p2-based plugins. See [P2_MULTITOOL.md](P2_MULTITOOL.md) for info on browsing and working with p2 repositories.

## How it works

Much of the complexity of downloading, running, and modifying the Eclipse IDE is caused by OSGi and p2. Equo IDE replaces p2 and OSGi with a simple shim called [Solstice](https://github.com/equodev/equo-ide/tree/main/solstice). This makes it easier and faster to build, debug, and run Eclipse-based applications.