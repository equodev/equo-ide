package dev.equo.solstice.platform;

import dev.equo.solstice.OsgiShim;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;

public class Handler extends URLStreamHandler {
	private static final String PKG = "dev.equo.solstice";
	private static final String CONTENT_PATH_PROP = "java.protocol.handler.pkgs";

	public static void install(OsgiShim solstice) {
		Handler.solstice = solstice;
		String handlerPkgs = System.getProperty(CONTENT_PATH_PROP, "");
		if (!handlerPkgs.contains(PKG)) {
			if (handlerPkgs.isEmpty()) {
				handlerPkgs = PKG;
			} else {
				handlerPkgs += "|" + PKG;
			}
			System.setProperty(CONTENT_PATH_PROP, handlerPkgs);
		}
	}

	private static OsgiShim solstice;

	private static final String SLASH_PLUGIN = "/plugin/";

	@Override
	protected URLConnection openConnection(URL u) throws IOException {
		String path = u.getPath();
		if (path.startsWith(SLASH_PLUGIN)) {
			String after = path.substring(SLASH_PLUGIN.length());
			int nextSlash = after.indexOf('/');
			String plugin = after.substring(0, nextSlash);
			String resource = after.substring(nextSlash + 1);
			return solstice.bundleByName(plugin).getEntry(resource).openConnection();
		} else {
			throw new IllegalArgumentException("Expected " + SLASH_PLUGIN + " got " + u.getPath());
		}
	}
}
