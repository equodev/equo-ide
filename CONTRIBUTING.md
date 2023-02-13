# EquoIDE and Solstice Contributing Guide

We use EquoIDE to develop EquoIDE and Solstice. Clone this repo, cd into its root directory, and run `gradlew equoIde`.

The first time you launch it, watch the status bar at the bottom as the Gradle project gets imported. Once it's done you're ready to develop!

A good place to start is running the Solstice tests by right-clicking the solstice project and doing `Run As -> JUnit Test`.

<img src="https://github.com/equodev/equo-ide/raw/main/.github/example-run-junit-test.png" max-height="558px">

## Troubleshooting

If you can't run the tests, the first thing to try is `Project -> Clean`. If that doesn't work, you can clear all IDE state by closing the IDE and relaunching with `gradlew equoIde --clean`.

## Developer's Guide

TODO