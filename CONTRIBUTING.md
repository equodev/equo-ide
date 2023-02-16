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
- [Add a new feature into the catalog (e.g. `jdt`, `gradleBuildship`, `m2e`, etc.)](#equo-catalog)

## Equo Catalog

- Add entry in `dev.equo.ide.EquoCatalog`
- Use that entry in `dev.equo.ide.maven.AbstractP2MojoWithFeatures`
- Use that entry in `dev.equo.ide.gradle.P2ModelDslWithFeatures`

TODO: add links and examples
