╔═ simple/gradleBuildship ═╗
+-------------+-----------------------------------------------------------------------------------------+
| kind        | value                                                                                   |
+-------------+-----------------------------------------------------------------------------------------+
| p2repo      | https://download.eclipse.org/buildship/updates/e431/releases/3.x/3.1.10.v20240802-1211/ |
| p2repo      | https://download.eclipse.org/eclipse/updates/4.27/                                      |
| install     | org.eclipse.buildship.feature.group                                                     |
| install     | org.eclipse.platform.ide.categoryIU                                                     |
| install     | org.eclipse.releng.java.languages.categoryIU                                            |
| filter      | platform-neutral                                                                        |
|   osgi.arch | dont-include-platform-specific-artifacts                                                |
|   osgi.os   | dont-include-platform-specific-artifacts                                                |
|   osgi.ws   | dont-include-platform-specific-artifacts                                                |
+-------------+-----------------------------------------------------------------------------------------+
╔═ simple/jdt ═╗
+-------------+----------------------------------------------------+
| kind        | value                                              |
+-------------+----------------------------------------------------+
| p2repo      | https://download.eclipse.org/eclipse/updates/4.27/ |
| install     | org.eclipse.platform.ide.categoryIU                |
| install     | org.eclipse.releng.java.languages.categoryIU       |
| filter      | platform-neutral                                   |
|   osgi.arch | dont-include-platform-specific-artifacts           |
|   osgi.os   | dont-include-platform-specific-artifacts           |
|   osgi.ws   | dont-include-platform-specific-artifacts           |
+-------------+----------------------------------------------------+
╔═ versionOverride/both-spec ═╗
+-------------+----------------------------------------------------+
| kind        | value                                              |
+-------------+----------------------------------------------------+
| p2repo      | https://download.eclipse.org/eclipse/updates/4.25/ |
| install     | org.eclipse.platform.ide.categoryIU                |
| install     | org.eclipse.releng.java.languages.categoryIU       |
| filter      | platform-neutral                                   |
|   osgi.arch | dont-include-platform-specific-artifacts           |
|   osgi.os   | dont-include-platform-specific-artifacts           |
|   osgi.ws   | dont-include-platform-specific-artifacts           |
+-------------+----------------------------------------------------+
╔═ versionOverride/jdt-spec ═╗
+-------------+----------------------------------------------------+
| kind        | value                                              |
+-------------+----------------------------------------------------+
| p2repo      | https://download.eclipse.org/eclipse/updates/4.25/ |
| install     | org.eclipse.platform.ide.categoryIU                |
| install     | org.eclipse.releng.java.languages.categoryIU       |
| filter      | platform-neutral                                   |
|   osgi.arch | dont-include-platform-specific-artifacts           |
|   osgi.os   | dont-include-platform-specific-artifacts           |
|   osgi.ws   | dont-include-platform-specific-artifacts           |
+-------------+----------------------------------------------------+
╔═ versionOverride/platform-neutral-jdt-spec ═╗
+-------------+----------------------------------------------------+
| kind        | value                                              |
+-------------+----------------------------------------------------+
| p2repo      | https://download.eclipse.org/eclipse/updates/4.25/ |
| install     | org.eclipse.platform.ide.categoryIU                |
| install     | org.eclipse.releng.java.languages.categoryIU       |
| filter      | platform-neutral                                   |
|   osgi.arch | dont-include-platform-specific-artifacts           |
|   osgi.os   | dont-include-platform-specific-artifacts           |
|   osgi.ws   | dont-include-platform-specific-artifacts           |
+-------------+----------------------------------------------------+
╔═ versionOverride/platform-spec ═╗
+-------------+----------------------------------------------------+
| kind        | value                                              |
+-------------+----------------------------------------------------+
| p2repo      | https://download.eclipse.org/eclipse/updates/4.25/ |
| install     | org.eclipse.platform.ide.categoryIU                |
| install     | org.eclipse.releng.java.languages.categoryIU       |
| filter      | platform-neutral                                   |
|   osgi.arch | dont-include-platform-specific-artifacts           |
|   osgi.os   | dont-include-platform-specific-artifacts           |
|   osgi.ws   | dont-include-platform-specific-artifacts           |
+-------------+----------------------------------------------------+
╔═ wrongOrder ═╗
> platform was already added as a transitive dependency of jdt.
  You can fix this by moving the <platform> block above the <jdt> block.
╔═ [end of file] ═╗
