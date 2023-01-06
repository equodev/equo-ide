# EquoIDE for Maven - Changelog

We adhere to the [keepachangelog](https://keepachangelog.com/en/1.0.0/) format.

## [Unreleased]

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