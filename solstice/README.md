# Solstice: OSGi with a single classloader (and p2)

[![Maven Plugin](https://img.shields.io/maven-central/v/dev.equo.ide/solstice?color=blue&label=dev.equo.ide%3Asolstice)](https://search.maven.org/artifact/dev.equo.ide/solstice)
[![Changelog](https://img.shields.io/badge/changelog-here-blue)](CHANGELOG.md)
[![Javadoc](https://img.shields.io/badge/javadoc-here-blue)](https://javadoc.io/doc/dev.equo.ide/solstice)

### static single-classloader OSGi

OSGi provides a lot of power:

- dynamic loading / unloading of plugins
- lazy initialization
- allowing multiple versions of the same classes to exist in the runtime at once, thus solving the [diamond dependency problem](https://jlbp.dev/what-is-a-diamond-dependency-conflict#:~:text=A%20diamond%20dependency%20conflict%20is,features%20that%20the%20consumers%20expect.)

However, this power brings a lot of complexity into the system. You can remove a lot of this complexity by imposing these constraints:

- no dynamic unloading or refreshing of plugins -> less code
- all plugins sorted and loaded on startup -> no heisenbugs caused by initialization order
- only one version of a class is allowed to exist in the runtime -> `Class.forName` just works 

### p2 but just for jars

Eclipse p2 is a "general purpose provisioning system". It can manage licenses, java runtimes, binary artifacts, and other things. It has a complex dependency model based on versions which can potentially identify and solve the diamond dependency problem, but it's difficult for users to reason about.

The implementation in this project is only suitable for jars. It traverses dependencies quickly and greedily, ignoring versions on the assumption that the creator of the update site chose a set of compatible versions when they created it. Once a set of jars have been resolved, you can get the maven central coordinates for all artifacts which are available there, and you can also download the p2 bundles for any artifacts which are not available on maven central.

The solstice p2 client can navigate p2 "categories" and "features", but it only cares about getting the jars at the end.

## Acknowledgments
Solstice is heavily inspired by [spotless-eclipse-base](https://github.com/diffplug/spotless/tree/main/_ext/eclipse-base) by [Frank Vennemeyer](https://github.com/fvgh).