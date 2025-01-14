╔═ toStringTest/empty ═╗
{'useMavenCentral': true}
╔═ toStringTest/filter empty ═╗
{}
╔═ toStringTest/filter exclude ═╗
{exclude: ['exclude.me']}
╔═ toStringTest/filter prefix exclude ═╗
{exclude: ['exclude.me'],
excludePrefix: ['exclude.prefix']}
╔═ toStringTest/filter props multiple ═╗
{exclude: ['exclude.me'],
excludePrefix: ['exclude.prefix'],
excludeSuffix: ['exclude.suffix'],
props: { 'blue': '0,0,255',
          'green': '0,255,0',
          'red': '255,0,0' }}
╔═ toStringTest/filter props single ═╗
{exclude: ['exclude.me'],
excludePrefix: ['exclude.prefix'],
excludeSuffix: ['exclude.suffix'],
props: { 'red': '255,0,0' }}
╔═ toStringTest/filter suffix exclude ═╗
{exclude: ['exclude.me'],
excludePrefix: ['exclude.prefix'],
excludeSuffix: ['exclude.suffix']}
╔═ toStringTest/install multiple ═╗
{'useMavenCentral': true,
p2repo: ['https://download.eclipse.org/buildship/updates/e423/releases/3.x/3.1.6.v20220511-1359/', 'https://download.eclipse.org/eclipse/updates/4.26/'],
install: ['org.eclipse.buildship.feature.group', 'org.eclipse.platform.ide.categoryIU', 'org.eclipse.releng.java.languages.categoryIU']}
╔═ toStringTest/install single ═╗
{'useMavenCentral': true,
p2repo: ['https://download.eclipse.org/buildship/updates/e423/releases/3.x/3.1.6.v20220511-1359/', 'https://download.eclipse.org/eclipse/updates/4.26/'],
install: ['org.eclipse.platform.ide.categoryIU']}
╔═ toStringTest/p2 multiple ═╗
{'useMavenCentral': true,
p2repo: ['https://download.eclipse.org/buildship/updates/e423/releases/3.x/3.1.6.v20220511-1359/', 'https://download.eclipse.org/eclipse/updates/4.26/']}
╔═ toStringTest/p2 single ═╗
{'useMavenCentral': true,
p2repo: ['https://download.eclipse.org/eclipse/updates/4.26/']}
╔═ [end of file] ═╗
