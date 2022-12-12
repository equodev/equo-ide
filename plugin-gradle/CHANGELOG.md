# EquoIDE for Gradle - Changelog

We adhere to the [keepachangelog](https://keepachangelog.com/en/1.0.0/) format.

## [Unreleased]

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