# EquoIDE for Maven - Changelog

We adhere to the [keepachangelog](https://keepachangelog.com/en/1.0.0/) format.

## [Unreleased]

## [0.13.3] - 2023-02-17
### Fixed
- Massive performance gains for the `ide` and `p2deps` plugins thanks to improved caching. ([#83](https://github.com/equodev/equo-ide/pull/83))
- Launcher fixes on Windows and Linux. ([#84](https://github.com/equodev/equo-ide/pull/84) fixes [#44](https://github.com/equodev/equo-ide/issues/44))
- Atomos dependencies are only added when they are needed. ([#83](https://github.com/equodev/equo-ide/pull/83))

## [0.13.2] - 2023-02-13
### Fixed
- More attempts to improve maven IDE launch experience. ([#79](https://github.com/equodev/equo-ide/pull/79) fixes [#44](https://github.com/equodev/equo-ide/issues/44))
  - mac already works great
  - linux now has a `sleep 5`
  - windows now has a visible cmd prompt console
- JUnit launch now works correctly. ([#81](https://github.com/equodev/equo-ide/pull/81))

## [0.13.1] - 2023-02-12
### Fixed
- No more errors on filesystems which don't support atomic move. ([#73](https://github.com/equodev/equo-ide/pull/73))
### Changes
- Switched Atomos off by default ([#76](https://github.com/equodev/equo-ide/pull/76))
  - Also specifying `-Dclean` no longer breaks the IDE-already-running detection.

## [0.13.0] - 2023-02-05
### Added
- `<welcome><openUrl>...` for opening a browser on startup. ([#65](https://github.com/equodev/equo-ide/pull/65))
- `-DdebugIde` blocks IDE startup and prints instructions to help you attach a remote debugger. ([#69](https://github.com/equodev/equo-ide/pull/69))

## [0.12.1] - 2023-01-25
### Fixed
- Convert all CLI enums to lowercase because maven arg parsing is case-sensitive. ([#64](https://github.com/equodev/equo-ide/pull/64))
  - Also switched from `/bin/sh` to `/bin/bash` so that `disown` works correctly when spawning the IDE on linux.

## [0.12.0] - 2023-01-25
### Added
- `mvn equo-ide:list` now has the p2 multitool from [`P2_MULTITOOL.md`](../P2_MULTITOOL.md). ([#61](https://github.com/equodev/equo-ide/pull/61))
- If the classpath changes, `equo-ide:launch` now suggests adding the `-Dclean` flag. ([#62](https://github.com/equodev/equo-ide/pull/62))

## [0.11.0] - 2023-01-23
### Added
- A lock file mechanism to prevent launching multiple IDEs operating on a single workspace. ([#56](https://github.com/equodev/equo-ide/pull/56) fixes [#44](https://github.com/equodev/equo-ide/issues/44))
- Support for setting title, icon, and splash screen. ([#58](https://github.com/equodev/equo-ide/pull/58) implements [#49](https://github.com/equodev/equo-ide/issues/49))

## [0.10.0] - 2023-01-11
### Added
- `equo-ide:launch -Dclean=true` now launches a totally fresh IDE. ([#50](https://github.com/equodev/equo-ide/pull/50))
- Full generic p2 model, removed the `release` parameter. ([#51](https://github.com/equodev/equo-ide/pull/51))
  - We also now run CI against windows (previously unix-only).

## [0.9.0] - 2023-01-09
### Added
- We now parse the `filter` property of p2 requirements and use it in queries. ([#45](https://github.com/equodev/equo-ide/pull/45))
### Fixed
- Partial improvements to process launching ([#47](https://github.com/equodev/equo-ide/pull/47) partial fix for [#44](https://github.com/equodev/equo-ide/issues/44))

## [0.8.0] - 2023-01-06
### Added
- `equo-ide:launch -DdebugClasspath=[names|paths]` to help debug classpath issues. ([#43](https://github.com/equodev/equo-ide/pull/43)) 

## [0.7.0] - 2023-01-06
### Added
- Added a parameter `useAtomos`, default value is `true`. ([#36](https://github.com/equodev/equo-ide/pull/36))
- `equo-ide:launch` now launches the IDE in an independent process so that the build task can complete. ([#39](https://github.com/equodev/equo-ide/pull/39))
    - Use the command line flag `-DshowConsole=true` to launch the IDE as a child process and redirect its console output to the build console.
- Respect optionality of p2 requirements. ([#41](https://github.com/equodev/equo-ide/pull/41) fixes [#35](https://github.com/equodev/equo-ide/issues/35))

### Fixed
- Dependency resolution no longer walks through unnecessary transitive dependency versions. (fixes [#37](https://github.com/equodev/equo-ide/issues/37)) 

## [0.6.0] - 2022-12-16
### Added
- P2Client now falls back to offline cache if/when the network fails. (closes [#24](https://github.com/equodev/equo-ide/issues/24))

## [0.5.0] - 2022-12-12
### Added
- More P2 client improvements.

## [0.4.0] - 2022-12-11
### Added
- Centralized cache locations for p2 metadata, p2 bundles, and also EquoIDE installations/workspaces.
- Tons of improvements to the P2 client.

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
- Fixes some missing fields in the plugin metadata.

## [0.1.0] - 2022-12-05
- First release to test publishing infrastructure, not meant for end users.