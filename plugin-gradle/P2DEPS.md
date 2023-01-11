To compile classes against dependencies from p2, you can use `dev.equo.p2deps`.

```gradle
apply plugin: 'dev.equo.p2deps'
p2deps {
  into 'compileOnly', {
    p2repo 'https://download.eclipse.org/eclipse/updates/4.26/'
    install 'org.eclipse.platform.ide.categoryIU'
  }
  into 'buildshipCompileOnly', {
    p2repo 'https://download.eclipse.org/eclipse/updates/4.26/'
    p2repo 'https://download.eclipse.org/buildship/updates/e423/releases/3.x/3.1.6.v20220511-1359/'
    install 'org.eclipse.buildship.feature.group'
  }
}
```

Dependencies which are available on maven central will be added as standard Gradle dependencies.

Dependencies which are only available on p2 will be added as raw files which doesn't interact well with maven poms. If you are publishing these jars, we recommend only using the `compileOnly` configuration to avoid a confusing POM.