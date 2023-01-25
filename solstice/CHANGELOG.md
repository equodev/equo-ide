# Solstice - Changelog

We adhere to the [keepachangelog](https://keepachangelog.com/en/1.0.0/) format.

## [Unreleased]
### Fixed
- Convert all CLI enums to lowercase because maven arg parsing is case-sensitive. ([#64](https://github.com/equodev/equo-ide/pull/64))
  - Also switched from `/bin/sh` to `/bin/bash` so that `disown` works correctly when spawning the IDE on linux.

## [0.12.0] - 2023-01-25
### Added
- P2Multitool now lives inside Solstice so it can be used from any build plugin. ([#61](https://github.com/equodev/equo-ide/pull/61))
- `IdeLockFile` can now store and recall the classpath, to help prompt the user to clean when necessary. ([#62](https://github.com/equodev/equo-ide/pull/62))
  - Improvements to the launch experience by calling `forceActive()` on the splash and initial IDE windows.
  - Delayed PID lockfile instantiation until the workbench has opened, should be more reliable to improve ([#44](https://github.com/equodev/equo-ide/issues/44))
### Fixed
- Minor bug in `IdeLockFile`. ([`394ff8`](https://github.com/equodev/equo-ide/commit/394ff81f4b2c876416fc07ff12d4a33b5ae41164))
- Offline caching of p2 dependencies and metadata now works again. ([`7e3f03`](https://github.com/equodev/equo-ide/pull/61/commits/7e3f036714d08635f03853bf27588f0ebd187319))

## [0.11.0] - 2023-01-23
### Added
- `IdeHook` system for defining extremely simple plugins. ([#56](https://github.com/equodev/equo-ide/pull/56))
  - Moved IDE-related classes into `dev.equo.ide` package.
  - `DepsResolve` now works for `-SNAPSHOT` versions published to maven local, aiding integration testing.
- Support for setting title, icon, and splash screen. ([#58](https://github.com/equodev/equo-ide/pull/58) implements [#49](https://github.com/equodev/equo-ide/issues/49))
### Changed
- We are now dogfooding `dev.equo.p2deps` to get the dependencies we need for Solstice. ([#55](https://github.com/equodev/equo-ide/pull/55))
  - Way simpler than our previous elaborate build, no more `NestedJars` in our test classpath, but they aren't needed to launch IDE without atomos. 

## [0.10.0] - 2023-01-11
### Added
- `WorkspaceRegistry` now supports cleaning. ([#50](https://github.com/equodev/equo-ide/pull/50))
- Introduce `P2Model` so that Gradle and Maven DSLs can drive guts through a common model type. ([#51](https://github.com/equodev/equo-ide/pull/51))
  - We also now run CI against unix and windows.

## [0.9.0] - 2023-01-09
### Added
- We now parse the `filter` property of p2 requirements and use it in queries. ([#45](https://github.com/equodev/equo-ide/pull/45))
- If an ASCII table is too wide, we now add a legend like `§§ feature.feature.group` at the bottom so the rows can be narrower. ([#46](https://github.com/equodev/equo-ide/pull/46))
### Fixed
- Partial improvements to process launching ([#47](https://github.com/equodev/equo-ide/pull/47) partial fix for [#44](https://github.com/equodev/equo-ide/issues/44))

## [0.8.0] - 2023-01-06
### Added
- Add tools to help debug classpath issues. ([#43](https://github.com/equodev/equo-ide/pull/43))
  - Rename `IdeMain` to `BUildPluginIdeMain` because it has a weird interface that only makes sense for our build plugins.
  - Add `enum DebugClasspath` for dumping all classpath entires (names or full paths)
  - Stop sorting classpath entries implicitly, make it explicit via `Launcher.copyAndSortClasspath`, which is now used by all build plugins.

## [0.7.0] - 2023-01-06
### Added
- `IdeMain` has gained a `-useAtomos true` flag which, when enabled, uses [Atomos](https://github.com/apache/felix-atomos) as a single-classloader OSGi connector to Equinox. This is used instead of the OSGi runtime which is built-in to Solstice. ([#36](https://github.com/equodev/equo-ide/pull/36))
  - It is recommended to set `-useAtomos true` for the foreseeable future, because it has better compatibility with existing applications.
  - For simple applications, you can experiment with `-useAtomos false`.
- Improve process launching. ([#39](https://github.com/equodev/equo-ide/pull/39))
- Respect optionality of p2 requirements. ([#41](https://github.com/equodev/equo-ide/pull/41) fixes [#35](https://github.com/equodev/equo-ide/issues/35))

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