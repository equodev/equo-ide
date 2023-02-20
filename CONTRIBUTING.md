# EquoIDE and Solstice Developer's Guide

We use EquoIDE to develop EquoIDE and Solstice. Clone this repo, cd into its root directory, and run `gradlew equoIde`.

The first time you launch it, watch the status bar at the bottom as the Gradle project gets imported. Once it's done you're ready to develop!

A good place to start is running the Solstice tests by right-clicking the solstice project and doing `Run As -> JUnit Test`.

<img src="https://github.com/equodev/equo-ide/raw/main/.github/example-run-junit-test.png" width="652" height="558">

## Troubleshooting

If you can't run the tests, the first thing to try is `Project -> Clean`. If that doesn't work, you can clear all IDE state by closing the IDE and relaunching with `gradlew equoIde --clean`.

### Table of Contents

- Use EquoIDE in my own project
  - for Gradle, see [the Gradle README.md](plugin-gradle/README.md).
  - for Maven, see [the Maven README.md](plugin-maven/README.md).
- [Add a new feature to the catalog DSL (e.g. `jdt`, `gradleBuildship`, `m2e`, etc.)](#catalog-dsl)
- [Create an Eclipse plugin without learning any OSGi (`IdeHook`)](#idehook)

## Catalog DSL

- Add entry in `dev.equo.ide.Catalog` ([e.g.](https://github.com/equodev/equo-ide/pull/90/commits/8a734e1a98200d36774b2530ef58dfe395c06fc1))
- Use that entry in `dev.equo.ide.gradle.P2ModelDslWithFeatures` ([e.g.](https://github.com/equodev/equo-ide/pull/90/commits/0356ccd83d16f245dbf291d40cf21dc6903a8abe))
- Use that entry in `dev.equo.ide.maven.AbstractP2MojoWithFeatures` ([e.g.](https://github.com/equodev/equo-ide/pull/90/commits/7d79ce1d12d1d5a608ecebba1eb4192735318b36))
  - Run `./gradlew :plugin-maven:test` to update test snapshots ([e.g.](https://github.com/equodev/equo-ide/pull/90/commits/0eaedbab7cfd64b81bad0bd713f1c029e26bb623))
  - If the snapshot doesn't change, do `rm -rf .gradle` and run it again.
- Update `CATALOG.md` so people can find it. ([e.g.](https://github.com/equodev/equo-ide/pull/90/commits/04500c649f42271d4d70e19924a5c486c8fb2fdf))
- Open up a PR against this repository to share your contribution!

## `IdeHook`

The `IdeHook` mechanism lets you write a plugin for the Eclipse IDE without knowing anything about OSGi. It works like this:

- setup a build where you can compile against the Eclipse jars ([`dev.equo.p2deps`](https://github.com/equodev/equo-ide/blob/main/plugin-gradle/P2DEPS.md) for Gradle, issue [#54](https://github.com/equodev/equo-ide/issues/54) for Maven)
  - example: https://github.com/equodev/equo-ide/blob/07b8f0a78fdc194aa0d2b122a595cb4092dc8ad1/solstice/build.gradle#L102-L108
- define a subclass of `IdeHook` which stores anything you want to pass from the Gradle or Maven launcher into the IDE
  - https://github.com/equodev/equo-ide/blob/main/solstice/src/main/java/dev/equo/ide/IdeHookBranding.java
  - https://github.com/equodev/equo-ide/blob/main/solstice/src/main/java/dev/equo/ide/IdeHookWelcome.java
  - https://github.com/equodev/equo-ide/blob/main/solstice/src/main/java/dev/equo/ide/IdeHookBuildship.java
- define a subclass of `IdeHookInstantiated` which has methods that get called throughout the application lifecycle
  - https://github.com/equodev/equo-ide/blob/main/solstice/src/buildship/java/dev/equo/ide/BuildshipImpl.java
  - https://github.com/equodev/equo-ide/tree/main/solstice/src/welcome/java/dev/equo/ide
- you can use any methods you want to implement your plugin, but there are helpers in the `dev.equo.ide.ui` package
  - https://github.com/equodev/equo-ide/tree/main/solstice/src/main/java/dev/equo/ide/ui
