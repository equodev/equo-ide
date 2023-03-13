# Solstice - Changelog

We adhere to the [keepachangelog](https://keepachangelog.com/en/1.0.0/) format.

## [Unreleased]

## [0.19.2] - 2023-03-13
### Fixed
- `Solstice.start(String)` now throws an exception if there was no bundle with that name. ([#107](https://github.com/equodev/equo-ide/pull/107))
- Locations registered in `ShimIdeBootstrapServices` have `url` property to keep Eclipse 4.25+ happy. ([#107](https://github.com/equodev/equo-ide/pull/107))
- Workaround some errant Eclipse artifacts in `4.23`. ([#107](https://github.com/equodev/equo-ide/pull/107) fixes [#106](https://github.com/equodev/equo-ide/issues/106))

## [0.19.1] - 2023-03-12
### Fixed
- Make `ShimIdeBootstrapServices` more resilient to older versions of Equinox. ([#105](https://github.com/equodev/equo-ide/pull/105))

## [0.19.0] - 2023-03-12
### Added
- `NestedJars.extractAllNestedJars()` which extracts into `~/.equo/nested-jars`. ([#104](https://github.com/equodev/equo-ide/pull/104))
### Fixed
- Handle `system.bundle` in `Require-Bundle`. ([#104](https://github.com/equodev/equo-ide/pull/104))
- `ShimIdeBootstrapServices` is now compatible with older Eclipse versions. ([#104](https://github.com/equodev/equo-ide/pull/104))
- ClassPath order when launching with nested jars now respects the order of `Bundle-ClassPath` entries. ([#104](https://github.com/equodev/equo-ide/pull/104))
- `Solstice.warnAndModifyManifestsToFix` no longer warns about nested jars which export the same packages as their containing bundle. ([#104](https://github.com/equodev/equo-ide/pull/104))

## [0.18.0] - 2023-03-10
### Added
- Catalog now includes `groovy`. ([#103](https://github.com/equodev/equo-ide/pull/103))
- `Solstice.startWithoutTransitives()`. ([#102](https://github.com/equodev/equo-ide/pull/102))
  - `QueryCache` renamed to `P2QueryCache`
  - `P2Client.Caching` renamed to `P2ClientCache`
### Fixed
- Correct the shim implementation of `PackageAdmin.getBundles` and `.getHosts` to return null rather than an empty array. ([#102](https://github.com/equodev/equo-ide/pull/102))
- Redirect fragments of `system.bundle` to `org.eclipse.osgi`. ([#102](https://github.com/equodev/equo-ide/pull/102))

## [0.17.0] - 2023-02-26
### Added
- Catalog now includes `pde`. ([#90](https://github.com/equodev/equo-ide/pull/90))
- Catalog now includes `kotlin`. ([#91](https://github.com/equodev/equo-ide/pull/91))
- Catalog now includes `tmTerminal` and `cdt`. ([#92](https://github.com/equodev/equo-ide/pull/92))
- Catalog now includes `rust`. ([#94](https://github.com/equodev/equo-ide/pull/94))
- Catalog now includes `m2e`. ([#95](https://github.com/equodev/equo-ide/pull/95))
### Fixed
- Bundles without activators are now treated as `lazy=true` since there's no need to eagerly activate them. ([#90](https://github.com/equodev/equo-ide/pull/90))
- Concurrent modification errors in service registry have been fixed by adding a custom datastructure. ([#90](https://github.com/equodev/equo-ide/pull/90))
- We can now handle p2 update sites without a `p2.index` file. ([#91](https://github.com/equodev/equo-ide/pull/91))
- Solstice Shim's URL handler now handles missing resources correctly. ([#91](https://github.com/equodev/equo-ide/pull/91) and [#95](https://github.com/equodev/equo-ide/pull/95))
- The Eclipse home location system property is now a proper `file:/` URL. ([#92](https://github.com/equodev/equo-ide/pull/92))
- `IdeMainUi` now has a mechanism to allow certain lazy bundles to activate earlier than normal, which was needed for `tmTerminal`. ([#92](https://github.com/equodev/equo-ide/pull/92))
- Added support for the legacy header `Eclipse-LazyStart`. ([#92](https://github.com/equodev/equo-ide/pull/92))
- Solstice Shim's localization service now searches in the default location before giving up. ([#94](https://github.com/equodev/equo-ide/pull/94))
- Compatibility improvements to the P2 client. ([#97](https://github.com/equodev/equo-ide/pull/97))

## [0.16.0] - 2023-02-19
### Added
- `Catalog` feature for specifying p2 repos in a user-friendly way. ([#86](https://github.com/equodev/equo-ide/pull/86))

## [0.15.0] - 2023-02-17
### Added
- `P2QueryResult` which strips a `P2Query` down to pnly the maven coordinates and jars. ([#85](https://github.com/equodev/equo-ide/pull/85))
	- Easy to cache, which allows us to grab massive speedup gains.
	- Also fixed a bug which had been disabling our offline P2 metadata cache. 
### Fixed
- Atomos was changed from an `implementation` dependency to `compileOnly`. ([#83](https://github.com/equodev/equo-ide/pull/83))

## [0.14.1] - 2023-02-13
### Fixed
- Missing OSGi capabilities are now handled the same as missing bundles and packages. ([#80](https://github.com/equodev/equo-ide/pull/80) fixes [#74](https://github.com/equodev/equo-ide/issues/74))
- JUnit launch now works correctly. ([#81](https://github.com/equodev/equo-ide/pull/81))
  - `Bundle.getEntry("/")` now returns `file://blah.jar` URL, instead of the `jar:file://blah.jar!/subpath` that it normally returns.
  - We now remove the `Bundle-ClassPath` header from Atomos and Shim modes, because `NestedJars` ensures that it is not necessary.

## [0.14.0] - 2023-02-12
### Added
- Introduced `Capability` which takes OSGi `Provide-Capability`/`Require-Capability` into account. ([#71](https://github.com/equodev/equo-ide/pull/71))
  - In particular, this means that we don't need to do any manual startup ordering anymore.
  - Also reverted from SLF4J 2.x to 1.x because 2.x uses fancy parts of the capability system that nothing else in Eclipse seems to use.
### Fixed
- No more errors on filesystems which don't support atomic move. ([#73](https://github.com/equodev/equo-ide/pull/73))
- Pure solstice can now run the full Eclipse IDE correctly, including `UIEventTopic`. ([#75](https://github.com/equodev/equo-ide/pull/75) fixes [#33](https://github.com/equodev/equo-ide/issues/33))
- Better error handling for IdeHooks. ([#76](https://github.com/equodev/equo-ide/pull/76))

## [0.13.0] - 2023-02-05
### Added
- `IdeHookWelcome` for opening a browser on welcome, and `PartDescriptor` for creating IDE parts without any `plugin.xml` metadata. ([#65](https://github.com/equodev/equo-ide/pull/65))
- `BuildPluginIdeMain` now has a field `debugIde` which launches the IDE with vmargs setup for remote debugging. ([#69](https://github.com/equodev/equo-ide/pull/69))
### Changed
- Complete revamp of bundle activation for Atomos and Solstice to emulate lazy activation. ([#68](https://github.com/equodev/equo-ide/pull/68))

## [0.12.1] - 2023-01-25
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
