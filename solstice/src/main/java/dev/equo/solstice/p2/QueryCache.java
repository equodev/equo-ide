package dev.equo.solstice.p2;

import java.io.File;
import java.util.Optional;

import javax.annotation.Nullable;

import dev.equo.solstice.SerializableMisc;

public class QueryCache {
	final File rootDir;
	final String key;

	QueryCache(File rootDir, P2Model model) {
		this.rootDir = rootDir;
		this.key = String.valueOf(model.hashCode());
		if (!FileMisc.readToken(rootDir, VERSION).equals(Optional.of(VERSION_VALUE))) {
			if (rootDir.exists()) {
				FileMisc.delete(rootDir);
			}
			FileMisc.mkdirs(rootDir);
			FileMisc.writeToken(rootDir, VERSION, VERSION_VALUE);
		}
	}

	private static final String VERSION = "version";
	private static final String VERSION_VALUE = "1";

	private static final String CONTENT = "content";

	public @Nullable P2QueryResult get() {
		var dir = new File(rootDir, OfflineCache.filenameSafe(key));
		if (dir.isDirectory()) {
			return SerializableMisc.fromFile(P2QueryResult.class, new File(dir, CONTENT));
		} else {
			return null;
		}
	}

	public void put(P2QueryResult query) {
		var dir = new File(rootDir, OfflineCache.filenameSafe(key));
		FileMisc.mkdirs(dir);
		SerializableMisc.toFile(query, new File(dir, CONTENT));
	}

}
