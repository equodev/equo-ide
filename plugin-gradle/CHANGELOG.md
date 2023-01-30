# EquoIDE for Gradle - Changelog

We adhere to the [keepachangelog](https://keepachangelog.com/en/1.0.0/) format.

## [Unreleased]
### Added
- `equoIde { welcome().openUrlOnStartup('...` for opening a browser on startup. ([#65](https://github.com/equodev/equo-ide/pull/65))

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