# Equo IDE

**UNDER CONSTRUCTION: NOT READY FOR END USERS!!**

- a build plugin for Gradle and Maven
- downloads, configures, and launches an instance of the Eclipse IDE
- ensures that all of your devs have a zero-effort and perfectly repeatable IDE setup process

Use it like this in Gradle with `gradlew equoIde`:

```gradle
plugins {
  id 'dev.equo.ide'
}
equoIde { // launch with gradlew equoIde
  release '4.26.0'
}
```

or like this in Maven:

```xml
<settings> <!-- put this in ~/.m2/settings.xml and launch with mvn equo-ide:equoIde -->
  <pluginGroups>
     <pluginGroup>dev.equo.ide</pluginGroup>
  </pluginGroups>
  ...
    
    
<plugin><!-- or add this to pom.xml/<project><build><pluginManagement><plugins> to add non-default settings -->
  <groupId>dev.equo.ide</groupId>
  <artifactId>equo-ide-maven-plugin</artifactId>
  <version>${equo.ide.version}</version>
  <configuration>
    <release>4.26.0</release>
  </configuration>
</plugin>
```

## How it works

Much of the complexity of downloading, running, and modifying the Eclipse IDE is caused OSGi and p2. Equo IDE cuts out this complexity by replacing p2 with plain-old maven, and replacing OSGi with a simple shim called [solstice](https://github.com/equodev/equo-ide/tree/main/solstice).

**Currently only the Eclipse Java Development Tools are supported, check out [this issue](https://github.com/equodev/equo-ide/issues/1) to vote on which plugins we should add support for next.**

## Equo Chromium

By default, Eclipse uses the native browser for webviews. Equo maintains a stable [Chromium embed](https://www.equoplatform.com/chromium) which is more reliable than the native browser configuration. You can enable the Equo Chromium component in your IDE like so:

```gradle
equoIde { // gradle
  browser 'EQUO'
  ...
```

```xml
<configuration> <!-- maven -->
  <browser>EQUO</browser>
  ...
```

If you wish to ship your own desktop Java appication (SWT or Java Swing) using this Chromium component, you can [purchase a redistribution license here](https://www.equoplatform.com/chromium).
