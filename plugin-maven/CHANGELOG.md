# EquoIDE for Maven - Changelog

We adhere to the [keepachangelog](https://keepachangelog.com/en/1.0.0/) format.

## [Unreleased]

## [1.5.6] - 2024-08-08
### Changed
- Bump `equoChromium` default version `106.0.22` -> `116.0.15`.
### Fixed
- Resolve error in ChatGPT integration.

## [1.5.5] - 2023-12-20
### Changed
- Bump `equoChromium` default version `106.0.13` -> `106.0.22`.
### Fixed
- Equo-IDE crashes when hovering to view javadocs with Equo Chromium.

## [1.5.4] - 2023-12-08
### Fixed
- Change Equo Chromium user agent so that OpenAI login works. ([#169](https://github.com/equodev/equo-ide/pull/169))

## [1.5.3] - 2023-08-29
### Fixed
- Fix download with username and password in URL. ([#162](https://github.com/equodev/equo-ide/pull/162))

## [1.5.2] - 2023-08-01
### Changed
- Bump `equoChromium` default version `106.0.10` -> `106.0.13`.
### Fixed
- Eclipse Welcome page is blank after restart with Equo Chromium. ([#157](https://github.com/equodev/equo-ide/pull/157))
- Third party login in Equo Chromium Browser. ([#160](https://github.com/equodev/equo-ide/pull/160))
- Manifest urls and osgi properties with spaces. ([#160](https://github.com/equodev/equo-ide/pull/160))
- Failed to download p2repo when URL includes name and password. ([#160](https://github.com/equodev/equo-ide/pull/160))

## [1.5.1] - 2023-07-07
### Changed
- Bump `equoChromium` default version `106.0.9` -> `106.0.10`. (fixes [#5](https://github.com/equodev/equo-ide-chatgpt/issues/5))
- Bump `chatGPT` default version `1.0.0` -> `1.0.1`.

## [1.5.0] - 2023-07-03
### Added
- `equo-ide:list -Drequest` now shows pure-maven dependencies. ([#151](https://github.com/equodev/equo-ide/pull/151))
### Fixed
- setting an explicit version for `chatGPT` would erroneously force that same version onto `equoChromium`, now fixed. ([#151](https://github.com/equodev/equo-ide/pull/151))

## [1.4.1] - 2023-06-27
### Fixed
- `equoChromium` browser now works correctly with Linux wayland. ([#150](https://github.com/equodev/equo-ide/pull/150))

## [1.4.0] - 2023-06-25
### Added
- `<chatGPT/>`, our [first Eclipse plugin](https://github.com/equodev/equo-ide-chatgpt) built and distributed without OSGi or p2, was added to the plugin catalog. ([#144](https://github.com/equodev/equo-ide/pull/144))
### Changed
- `<useChromium>true</useChromium>` deprecated in favor of `<equoChromium/>` which is now a standard catalog entry. ([#145](https://github.com/equodev/equo-ide/pull/145))
### Fixed
- `egit`, `assistAI`, and `tabnine` were not hooked into the plugin catalog correctly, but they are now. ([#145](https://github.com/equodev/equo-ide/pull/145))

## [1.3.1] - 2023-06-22
### Fixed
- Fix issues in Chromium browser. ([#141](https://github.com/equodev/equo-ide/pull/141))
  - Now can calls multiple times setUrl.
  - Welcome view and javadocs they look correct.
- Large startup performance improvement. ([#142](https://github.com/equodev/equo-ide/pull/142))
- Use the latest buildship compiled for Eclipse `4.27` instead of the old `4.23`. ([#142](https://github.com/equodev/equo-ide/pull/142))

## [1.3.0] - 2023-06-16
### Added
- `<tabnine/>` (Copilot-style AI completion) to the plugin catalog. ([#136](https://github.com/equodev/equo-ide/pull/136))
- `<assistAI/>` (a ChatGPT plugin) is now available in the EquoIDE plugin catalog. ([#134](https://github.com/equodev/equo-ide/pull/134))
- `<egit/>` is now available in the EquoIDE plugin catalog. ([#133](https://github.com/equodev/equo-ide/pull/133))
- Bump all defaults to Eclipse 4.28 (2023-06). ([#137](https://github.com/equodev/equo-ide/pull/137))
### Fixed
- `<branding>` no longer loads SWT classes at definition time, which required build plugins to have SWT on the classpath. ([#135](https://github.com/equodev/equo-ide/pull/135))

## [1.2.0] - 2023-05-17
### Added
- You can now do `<useChromium/>` to replace the standard SWT system browser with Equo Chromium. ([#123](https://github.com/equodev/equo-ide/pull/123))

## [1.1.0] - 2023-05-09
### Added
- The ability to set Eclipse preference files. ([#127](https://github.com/equodev/equo-ide/pull/127))
  - Each catalog entry has its own DSL, e.g. for Eclipse PDE
    ```xml
    <configuration>
      <pde>
        <missingApiBaseline>Ignore</missingApiBaseline>
      </pde>
      ...
    ```
  - Or you can set properties manually on their own
    ```xml
    <configuration>
      <workspaceProps>
        <workspaceProp>
            <path>instance/.metadata/.plugins/org.eclipse.core.runtime/.settings/org.eclipse.pde.api.tools.prefs</path>
            <key>missing_default_api_profile</key>
            <value>Ignore</value>
        </workspaceProp>
        ...
    ```
- Control preferences for whitespace and line numbers under `platform`, add classpath variables under `jdt`. ([#128](https://github.com/equodev/equo-ide/pull/128))
### Fixed
- Eclipse PDE now runs well under Atomos. ([#126](https://github.com/equodev/equo-ide/pull/126))

## [1.0.0] - 2023-04-29
### Added
- `<m2e><autoImport>${project.basedir}</autoImport></m2e>` to automatically import the given repo on startup. ([#115](https://github.com/equodev/equo-ide/pull/115) implements [#18](https://github.com/equodev/equo-ide/issues/18))
- We now set the initial perspective of the IDE. ([#125](https://github.com/equodev/equo-ide/pull/125))
  - It gets set automatically from catalog entries (e.g. `<jdt/>`), or you can set it manually
  - ```xml
    <welcome>
      <perspective>org.eclipse.jdt.ui.JavaPerspective</perspective>
    </welcome>
    ```
### Changed
- Update version catalog to latest `2022-03` versions of everything. ([#110](https://github.com/equodev/equo-ide/pull/110))
### Fixed
- Fix branding for title bar and icon. ([#117](https://github.com/equodev/equo-ide/pull/117) fixes [#87](https://github.com/equodev/equo-ide/issues/87))

## [0.16.5] - 2023-03-13
### Fixed
- IDE launched wasn't handling nested jars correctly on Windows. ([#114](https://github.com/equodev/equo-ide/pull/114))

## [0.16.4] - 2023-03-13
### Fixed
- `BuildPluginIdeMain` wasn't adding nested jars onto the classpath of the launched application, oops. ([#113](https://github.com/equodev/equo-ide/pull/113))
- The gogo shell (gosh) finally stopped yelling about "message of the day". ([#113](https://github.com/equodev/equo-ide/pull/113))
- Fix `isClean` detection for IDE hooks. ([#113](https://github.com/equodev/equo-ide/pull/113))

## [0.16.3] - 2023-03-13
### Fixed
- IDE would fail to launch due to a bug in nested jar extraction. ([#111](https://github.com/equodev/equo-ide/pull/111))

## [0.16.2] - 2023-03-13
### Fixed
- Locations registered in `ShimIdeBootstrapServices` have `url` property to keep Eclipse 4.25+ happy. ([#107](https://github.com/equodev/equo-ide/pull/107))
- Workaround some errant Eclipse artifacts in `4.23`. ([#106](https://github.com/equodev/equo-ide/issues/106))

## [0.16.1] - 2023-03-12
### Fixed
- ClassPath order when launching with nested jars now respects the order of `Bundle-ClassPath` entries. ([#104](https://github.com/equodev/equo-ide/pull/104))

## [0.16.0] - 2023-03-10
### Added
- Maven DSL now supports `groovy`. ([#103](https://github.com/equodev/equo-ide/pull/103))

## [0.15.0] - 2023-02-26
### Changed
- P2 operations now use cached values whenever they are available, unless `-Dclean` is specified.
### Added
- Maven DSL now supports `pde`. ([#90](https://github.com/equodev/equo-ide/pull/90))
- Maven DSL now includes `kotlin`. ([#91](https://github.com/equodev/equo-ide/pull/91))
- Maven DSL now includes `tmTerminal` and `cdt`. ([#92](https://github.com/equodev/equo-ide/pull/92))
- Maven DSL now includes `rust`. ([#94](https://github.com/equodev/equo-ide/pull/94))
- Maven DSL now includes `m2e`. ([#95](https://github.com/equodev/equo-ide/pull/95))
  - Partially functional, watch [#18](https://github.com/equodev/equo-ide/issues/18) for progress on automatic import.
### Fixed
- Further improved Windows launch experience. ([#44](https://github.com/equodev/equo-ide/issues/44))

## [0.14.0] - 2023-02-19
### Added
- Users can now put `<jdt/>` into the EquoIDE dsl, manually listing out p2 urls no longer necessary. ([#86](https://github.com/equodev/equo-ide/pull/86)) 
### Fixed
- Improved Windows launch experience. ([#44](https://github.com/equodev/equo-ide/issues/44))

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
