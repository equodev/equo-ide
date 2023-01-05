# Solstice - Changelog

We adhere to the [keepachangelog](https://keepachangelog.com/en/1.0.0/) format.

## [Unreleased]
### Added
- `IdeMain` has gained a `-useAtomos true` flag which, when enabled, uses [Atomos](https://github.com/apache/felix-atomos) as a single-classloader OSGi connector to Equinox. This is used instead of the OSGi runtime which is built-in to Solstice. ([#36](https://github.com/equodev/equo-ide/pull/36))
  - It is recommended to set `-useAtomos true` for the foreseeable future, because it has better compatibility with existing applications.
  - For simple applications, you can experiment with `-useAtomos false`.
- Improve process launching. ([#39](https://github.com/equodev/equo-ide/pull/39))

## [0.6.0] - 2022-12-16
### Added
- P2Client now supports various offline modes. (closes [#24](https://github.com/equodev/equo-ide/issues/24))

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
- Changes to allow end users to set the release manually.

## [0.2.0] - 2022-12-07
### Added
- The `dev.equo.solstice.p2` package which has a simple implementation of p2.

## [0.1.1] - 2022-12-06
- Most of the Eclipse JDT UI now lights up, but we're still having problems with the JDT project nature.

## [0.1.0] - 2022-12-05
- First release to test publishing infrastructure, not meant for end users.