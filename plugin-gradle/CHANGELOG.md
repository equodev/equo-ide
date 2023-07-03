# EquoIDE for Gradle - Changelog

We adhere to the [keepachangelog](https://keepachangelog.com/en/1.0.0/) format.

## [Unreleased]
### Added
- `equoList --request` now shows pure-maven dependencies. ([#151](https://github.com/equodev/equo-ide/pull/151))
### Fixed
- setting an explicit version for `chatGPT` would erroneously force that same version onto `equoChromium`, now fixed. ([#151](https://github.com/equodev/equo-ide/pull/151))

## [1.6.1] - 2023-06-27
### Fixed
- `equoChromium` browser now works correctly with Linux wayland. ([#150](https://github.com/equodev/equo-ide/pull/150))

## [1.6.0] - 2023-06-25
### Added
- `chatGPT()`, our [first Eclipse plugin](https://github.com/equodev/equo-ide-chatgpt) built and distributed without OSGi or p2, was added to the plugin catalog. ([#144](https://github.com/equodev/equo-ide/pull/144))
- `dev.equo.p2deps` plugin now allows putting deps into multiple configurations at once. ([#109](https://github.com/equodev/equo-ide/issues/109))
### Changed
- `useChromium` deprecated in favor of `equoChromium` which is now a standard catalog entry. ([#145](https://github.com/equodev/equo-ide/pull/145))

## [1.5.0] - 2023-06-22
### Added
- `equoIde { dogfood() }` which adds this jar (and its transitive dependencies) into the launched IDE. ([#143](https://github.com/equodev/equo-ide/pull/143))
  - You can also do `dogfood().ideHook('com.acme.MyIdeHook').ideHook('AnotherIdeHook')` which adds the given IDE hooks into the launched IDE startup sequence. The hooks must have a no-arg constructor. ([#143](https://github.com/equodev/equo-ide/pull/143))
### Fixed
- Fix issues in Chromium browser. ([#141](https://github.com/equodev/equo-ide/pull/141))
  - Now can calls multiple times setUrl.
  - Welcome view and javadocs they look correct.
- Large startup performance improvement. ([#142](https://github.com/equodev/equo-ide/pull/142))
- Use the latest buildship compiled for Eclipse `4.27` instead of the old `4.23`. ([#142](https://github.com/equodev/equo-ide/pull/142))

## [1.4.0] - 2023-06-16
### Added
- `tabnine()` (Copilot-style AI completion) to the plugin catalog. ([#136](https://github.com/equodev/equo-ide/pull/136))
- `assistAI()` (ChatGPT) is now available in the EquoIDE plugin catalog. ([#134](https://github.com/equodev/equo-ide/pull/134))
- `egit()` is now available in the EquoIDE plugin catalog. ([#133](https://github.com/equodev/equo-ide/pull/133))
- Bump all defaults to Eclipse 4.28 (2023-06). ([#137](https://github.com/equodev/equo-ide/pull/137))
### Fixed
- `branding()` no longer loads SWT classes at definition time, which required build plugins to have SWT on the classpath. ([#135](https://github.com/equodev/equo-ide/pull/135))

## [1.3.0] - 2023-05-17
### Added
- You can now do `equoIde { useChromium() }` to replace the standard SWT system browser with Equo Chromium. ([#123](https://github.com/equodev/equo-ide/pull/123))

## [1.2.0] - 2023-05-09
### Added
- The ability to set Eclipse preference files. ([#127](https://github.com/equodev/equo-ide/pull/127))
  - Each catalog entry has its own DSL, e.g. for Eclipse PDE
    ```gradle
    equoIde { 
      pde().missingApiBaseline('Ignore')
    ```
  - Or you can set properties manually on their own
    ```gradle
    equoIde {
      workspaceInit 'instance/.metadata/.plugins/org.eclipse.core.runtime/.settings/org.eclipse.pde.api.tools.prefs', {
        prop 'missing_default_api_profile', 'Ignore'
        prop 'missing_plugin_in_baseline', 'Ignore'
      }
    ```
- Control preferences for whitespace and line numbers under `platform`, add classpath variables under `jdt`. ([#128](https://github.com/equodev/equo-ide/pull/128))
### Fixed
- Eclipse PDE now runs well under Atomos. ([#126](https://github.com/equodev/equo-ide/pull/126)

## [1.1.0] - 2023-04-29
### Added
- We now set the initial perspective of the IDE. ([#125](https://github.com/equodev/equo-ide/pull/125))
  - It gets set automatically from catalog entries (e.g. `jdt()`), or you can set it manually
  - ```xml
    welcome().perspective('org.eclipse.jdt.ui.JavaPerspective')
    ```
### Changed
- Update version catalog to latest `2022-03` versions of everything. ([#110](https://github.com/equodev/equo-ide/pull/110))
### Fixed
- Fix branding for title bar and icon. ([#117](https://github.com/equodev/equo-ide/pull/117) fixes [#87](https://github.com/equodev/equo-ide/issues/87))

## [1.0.1] - 2023-03-13
### Fixed
- IDE launched wasn't handling nested jars correctly on Windows. ([#114](https://github.com/equodev/equo-ide/pull/114))

## [1.0.0] - 2023-03-13
### Fixed
- `BuildPluginIdeMain` wasn't adding nested jars onto the classpath of the launched application, oops. ([#113](https://github.com/equodev/equo-ide/pull/113))
- The gogo shell (gosh) finally stopped yelling about "message of the day". ([#113](https://github.com/equodev/equo-ide/pull/113))
- Fix `isClean` detection for IDE hooks. ([#113](https://github.com/equodev/equo-ide/pull/113))

## [0.18.0] - 2023-03-13
### Added
- `gradleBuildship()` no longer performs an auto-import by default, you have to do `gradleBuildship().autoImport('.')` which means you can also do `../` or whatever you would like. ([#112](https://github.com/equodev/equo-ide/pull/112))
### Fixed
- IDE would fail to launch due to a bug in nested jar extraction. ([#111](https://github.com/equodev/equo-ide/pull/111))

## [0.17.2] - 2023-03-13
### Fixed
- Locations registered in `ShimIdeBootstrapServices` have `url` property to keep Eclipse 4.25+ happy. ([#107](https://github.com/equodev/equo-ide/pull/107))
- Workaround some errant Eclipse artifacts in `4.23`. ([#106](https://github.com/equodev/equo-ide/issues/106))

## [0.17.1] - 2023-03-12
### Fixed
- `dev.equo.p2deps` now puts the nested jars inside of p2 dependencies onto the classpath. ([#104](https://github.com/equodev/equo-ide/pull/104))
- ClassPath order when launching with nested jars now respects the order of `Bundle-ClassPath` entries. ([#104](https://github.com/equodev/equo-ide/pull/104))

## [0.17.0] - 2023-03-10
### Added
- Gradle DSL now supports `groovy`. ([#103](https://github.com/equodev/equo-ide/pull/103))

## [0.16.0] - 2023-02-26
### Changed
- P2 operations now use cached values whenever they are available, unless `--clean` or `--refresh-dependencies` is specified.
- `equoIde` now downloads its dependencies only if it is called directly. This means that CI builds don't need to download IDE dependencies. ([#89]https://github.com/equodev/equo-ide/pull/89))
  - Also, `equoIde` no longer adds `mavenCentral()` automatically.
### Added
- Gradle DSL now supports `pde`. ([#90](https://github.com/equodev/equo-ide/pull/90))
- Gradle DSL now includes `kotlin`. ([#91](https://github.com/equodev/equo-ide/pull/91))
- Gradle DSL now includes `tmTerminal` and `cdt`. ([#92](https://github.com/equodev/equo-ide/pull/92))
- Gradle DSL now includes `rust`. ([#94](https://github.com/equodev/equo-ide/pull/94))
- Gradle DSL now includes `m2e`. ([#95](https://github.com/equodev/equo-ide/pull/95))

## [0.15.0] - 2023-02-19
### Added
- Users can now put `jdt()` or `gradleBuildship()` into the `equoIde` dsl, manually listing out p2 urls no longer necessary. ([#86](https://github.com/equodev/equo-ide/pull/86)) 

## [0.14.2] - 2023-02-17
### Fixed
- Massive performance gains for the `ide` and `p2deps` plugins thanks to improved caching. ([#83](https://github.com/equodev/equo-ide/pull/83))
- Atomos dependencies are only added when they are needed. ([#83](https://github.com/equodev/equo-ide/pull/83))

## [0.14.1] - 2023-02-13
### Fixed
- JUnit launch now works correctly. ([#81](https://github.com/equodev/equo-ide/pull/81))

## [0.14.0] - 2023-02-12
### Added
- Buildship import works! ([#76](https://github.com/equodev/equo-ide/pull/76))
  - We now run without Atomos by default.
  - Specifying `-Dclean` no longer breaks the IDE-already-running detection.
### Fixed
- No more errors on filesystems which don't support atomic move ([#73](https://github.com/equodev/equo-ide/pull/73))
### Changed
- Replaced the flag `--dont-use-atomos` with `--use-atomos=[true|false]` to override whatever is set in `equoIde { useAtomos = blah`. ([#75](https://github.com/equodev/equo-ide/pull/75))

## [0.13.0] - 2023-02-05
### Added
- `equoIde { welcome().openUrl('...` for opening a browser on startup. ([#65](https://github.com/equodev/equo-ide/pull/65))
- `--debug-ide` blocks IDE startup and prints instructions to help you attach a remote debugger. ([#69](https://github.com/equodev/equo-ide/pull/69))
- Default setup now installs Eclipse Buildship, but project import is still broken. ([#66](https://github.com/equodev/equo-ide/pull/66))

## [0.12.1] - 2023-01-25
### Fixed
- Convert all CLI enums to lowercase because maven arg parsing is case-sensitive. ([#64](https://github.com/equodev/equo-ide/pull/64))
  - Also switched from `/bin/sh` to `/bin/bash` so that `disown` works correctly when spawning the IDE on linux.

## [0.12.0] - 2023-01-25
### Added
- If the classpath changes, `equoIde` now suggests adding the `--clean` flag. ([#62](https://github.com/equodev/equo-ide/pull/62))

## [0.11.0] - 2023-01-23
### Added
- A lock file mechanism to prevent launching multiple IDEs operating on a single workspace. ([#56](https://github.com/equodev/equo-ide/pull/56) fixes [#44](https://github.com/equodev/equo-ide/issues/44))
- Support for setting title, icon, and splash screen. ([#58](https://github.com/equodev/equo-ide/pull/58) implements [#49](https://github.com/equodev/equo-ide/issues/49))

## [0.10.0] - 2023-01-11
### Added
- `dev.equo.p2deps` plugin allows you to compile against p2 dependencies. ([#53](https://github.com/equodev/equo-ide/pull/53))
- `equoIde --clean` now launches a totally fresh IDE. ([#50](https://github.com/equodev/equo-ide/pull/50))
### Removed
- Removed the `release` parameter in favor of just the p2 stuff. ([#51](https://github.com/equodev/equo-ide/pull/51))
  - We also now run CI against windows (previously unix-only).

## [0.9.0] - 2023-01-09
### Added
- We now parse the `filter` property of p2 requirements and use it in queries. ([#45](https://github.com/equodev/equo-ide/pull/45))
- If an ASCII table is too wide, we now add a legend of shortened replacements at the bottom so the rows can be narrower. ([#46](https://github.com/equodev/equo-ide/pull/46))
  - `á org.apache`
  - `é org.eclipse`
  - `ë org.eclipse.equinox`
  - `§ feature.group`
  - `§§ feature.feature.group`
### Fixed
- Partial improvements to process launching ([#47](https://github.com/equodev/equo-ide/pull/47) partial fix for [#44](https://github.com/equodev/equo-ide/issues/44)) 

## [0.8.0] - 2023-01-06
### Added
- `gradlew equoIde --debug-classpath=[names|paths]` to help debug classpath issues. ([#43](https://github.com/equodev/equo-ide/pull/43))
  - Also fixed a problem where we were loading multiple versions of some jars as part of our attempted fix in `0.7.1`.

## [0.7.1] - 2023-01-06
### Fixed
- Transitive dependencies of Solstice were not being added, now fixed. ([#42](https://github.com/equodev/equo-ide/pull/42))

## [0.7.0] - 2023-01-06
### Added
- Added a parameter `useAtomos` to the Solstice buildscript, default value is `true`. ([#36](https://github.com/equodev/equo-ide/pull/36))
  - You can override the `useAtomos` buildscript parameter at the command line with `--dont-use-atomos`.
- `equoIde` now launches the IDE in an independent process so that the build task can complete. ([#39](https://github.com/equodev/equo-ide/pull/39))
  - Use the command line flag `--show-console` to launch the IDE as a child process and redirect its console output to the build console.
    https://github.com/equodev/equo-ide/issues/35
- Respect optionality of p2 requirements. ([#41](https://github.com/equodev/equo-ide/pull/41) fixes [#35](https://github.com/equodev/equo-ide/issues/35))
  - Optional requirements are by default not followed.
  - New command `gradlew equoList --optional` to explore uninstalled optional requirements to help discover helpful features.

## [0.6.0] - 2022-12-16
### Added
- P2Client now falls back to offline cache if/when the network fails. Never attempts to connect at all when gradle is in offline mode. (closes [#24](https://github.com/equodev/equo-ide/issues/24))

## [0.5.0] - 2022-12-12
### Added
- Separated `equoList --installed` into `--installed` and `--problems`
- Renamed `filter` to `addFilter` to allow handling multiple filters.

## [0.4.0] - 2022-12-11
### Added
- Centralized cache locations for p2 metadata, p2 bundles, and also EquoIDE installations/workspaces.
- New `equoList` task for browsing and debugging p2 dependencies.

## [0.3.1] - 2022-12-07
### Fixed
- Javadoc on all public classes.

## [0.3.0] - 2022-12-07
### Added
- You can now change which release of Eclipse you want to use.

## [0.2.0] - 2022-12-07
### Added
- Support for win/mac/linux.

## [0.1.1] - 2022-12-06
- Most of the Eclipse JDT UI now lights up, but we're still having problems with the JDT project nature.

## [0.1.0] - 2022-12-05
- First release to test publishing infrastructure, not meant for end users.
