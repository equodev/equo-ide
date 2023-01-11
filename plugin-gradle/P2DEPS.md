To compiled classes against dependencies from p2, you can do something like the following.

```gradle
apply plugin: 'dev.equo.p2deps'
p2deps {
  into 'compileOnly', {
    p2repo 'https://download.eclipse.org/eclipse/updates/4.26/'
    install 'org.eclipse.platform.ide.categoryIU'
  }
  into 'buildshipIntegration', {
    p2repo 'https://download.eclipse.org/eclipse/updates/4.26/'
    p2repo 'https://download.eclipse.org/buildship/updates/e423/releases/3.x/3.1.6.v20220511-1359/'
    install 'org.eclipse.buildship.feature.group'
  }
}
```

This is currently being dogfooded to implement Gradle Buildship integration, we'll release formal docs soon.