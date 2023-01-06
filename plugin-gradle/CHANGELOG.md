# EquoIDE for Gradle - Changelog

We adhere to the [keepachangelog](https://keepachangelog.com/en/1.0.0/) format.

## [Unreleased]
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