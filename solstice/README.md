# Solstice: OSGi with a single classloader (and p2)

[![Maven Plugin](https://img.shields.io/maven-central/v/dev.equo.ide/solstice?color=blue&label=dev.equo.ide%3Asolstice)](https://search.maven.org/artifact/dev.equo.ide/solstice)
[![Changelog](https://img.shields.io/badge/changelog-here-blue)](CHANGELOG.md)
[![Javadoc](https://img.shields.io/badge/javadoc-here-blue)](https://javadoc.io/doc/dev.equo.ide/solstice)

If the jars are there, then it should run. You shouldn't have to spend time on manifest files if the `.class` files are on the classpath. To use Solstice OSGi, all you need to do is put the jars on your classpath, then do this:

```java
var solstice = Solstice.findBundlesOnClasspath();
solstice.warnAndModifyManifestsToFix();

var props = new LinkedHashMap<String, String>(); // e.g. https://github.com/equodev/equo-ide/blob/2f47b51171c1b2fb2ebe9f93723393649d43172c/solstice/src/main/java/dev/equo/ide/BuildPluginIdeMain.java#L275-L283
solstice.openShim(props); // or solstice.openAtomos(props); 

solstice.start("org.apache.felix.scr"); // if you're using OSGi-DS, start that first
solstice.startAllWithLazy(false);       // start all the non-lazy bundles
solstice.start("org.eclipse.ui.ide.application"); // start any bundles you want
solstice.startAllWithLazy(true);        // and finally, once your UI has started, start the lazy bundles too
```

Some other examples

- https://github.com/equodev/equo-ide/blob/main/solstice/src/main/java/dev/equo/ide/BuildPluginIdeMain.java
- https://github.com/equodev/equo-ide/blob/main/solstice/src/main/java/dev/equo/ide/IdeMainUi.java
- https://github.com/diffplug/spotless/pull/1524

### static single-classloader OSGi

OSGi provides a lot of power:

- dynamic loading / unloading of plugins
- lazy initialization
- allowing multiple versions of the same classes to exist in the runtime at once, thus solving the [diamond dependency problem](https://jlbp.dev/what-is-a-diamond-dependency-conflict#:~:text=A%20diamond%20dependency%20conflict%20is,features%20that%20the%20consumers%20expect.)

However, this power brings a lot of complexity into the system. You can remove a lot of this complexity by imposing these constraints:

- no dynamic unloading or refreshing of plugins -> less code
- sorted plugins eagerly initialized -> no heisenbugs caused by initialization order
- only one version of a class is allowed to exist in the runtime -> `Class.forName` just works 

That's what Solstice does - removes the complexity by implementing fewer features. If you feel that Soltice implements too little, you can always opt-in to full Atomos Equinox by calling `openAtomos` instead of `openShim`, just make sure that [these Atomos jars](https://github.com/equodev/equo-ide/blob/2f47b51171c1b2fb2ebe9f93723393649d43172c/solstice/build.gradle#L39-L40) are on your classpath since Solstice doesn't bring them by default. But we've been able to get everything in the [`CATALOG.md`](../CATALOG.md) to run without any errors, so it's unlikely that you'll need anything besides Solstice.

### p2 but just for jars

Eclipse p2 is a "general purpose provisioning system". It can manage licenses, java runtimes, binary artifacts, and other things. It has a complex dependency model based on versions which can potentially identify and solve the diamond dependency problem, but it's difficult for users to reason about.

The implementation in this project is only suitable for jars. It traverses dependencies quickly and greedily, ignoring versions on the assumption that the creator of the update site chose a set of compatible versions when they created it. Once a set of jars have been resolved, you can get the maven central coordinates for all artifacts which are available there, and you can also download the p2 bundles for any artifacts which are not available on maven central.

The solstice p2 client can navigate p2 "categories" and "features", but it only cares about getting the jars at the end.

## Acknowledgments

- Huge thanks to [Thomas Watson](https://github.com/tjwatson) for helping us learn about and integrate the [Atomos OSGi Framework](https://github.com/apache/felix-atomos). 
- Solstice is heavily inspired by [spotless-eclipse-base](https://github.com/diffplug/spotless/tree/main/_ext/eclipse-base) by [Frank Vennemeyer](https://github.com/fvgh).