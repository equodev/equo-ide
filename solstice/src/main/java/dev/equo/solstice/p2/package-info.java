/**
 * A simple implementation of p2 with the goal of making p2 an optional component of a project. It
 * accomplishes this by making it possible to incrementally switch some artifacts off of p2 and onto
 * maven central.
 *
 * <pre>
 * var session = new P2Session(); // stores all p2 metadata in memory
 * try (var client = new P2Client(cacheDir)) { // loads p2 metadata from a URL (and a disk cache) into the session
 *   session.populateFrom(client, "https://download.eclipse.org/eclipse/updates/4.26")
 * }
 * var query = new P2Query(); // stores the results of a query against the p2 metadata, including requires/provides
 * query.platform(SwtPlatform.getRunning()); // filters artifacts based on the platform
 * query.resolve(session.getUnitById("org.eclipse.platform.ide.categoryIU")); // resolves dependencies of the given IU
 *
 * for (String mavenCoord : query.jarsOnMavenCentral()) {
 *   System.out.println("maven coordinate: " + mavenCoord);
 * }
 * for (P2Unit iu : query.jarsNotOnMavenCentral()) {
 *   System.out.println("p2 only: " + ui.getId());
 * }
 * </pre>
 */
package dev.equo.solstice.p2;
