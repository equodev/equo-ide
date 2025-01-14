╔═ help ═╗
Detailed task information for equoIde

Path
     :equoIde

Type
     EquoIdeTask (dev.equo.ide.gradle.EquoIdeTask)

Options
     --clean     Wipes all IDE settings and state before rebuilding and launching.

     --debug-classpath     Dumps the classpath (in order) without starting the application.
                           Available values are:
                                disabled
                                names
                                paths

     --debug-ide     The IDE will suspend until you attach a debugger.

     --init-only     Initializes the runtime to check for errors then exits.

     --show-console     Adds a visible console to the launched application.

     --use-atomos     Determines whether to use Atomos+Equinox or only Solstice's built-in OSGi shim

Description
     Launches an Eclipse-based IDE for this project

Group
     IDE
╔═ p2repoArgCheck/double-slash ═╗
.
> Must end with a single /
    https://somerepo//  <- WRONG
    https://somerepo/   <- CORRECT
╔═ p2repoArgCheck/no-slash ═╗
.
> Must end with /
    https://somerepo   <- WRONG
    https://somerepo/  <- CORRECT
╔═ tasks ═╗
---------
equoIde - Launches an Eclipse-based IDE for this project
equoList - Lists the p2 dependencies of an Eclipse application
╔═ [end of file] ═╗
