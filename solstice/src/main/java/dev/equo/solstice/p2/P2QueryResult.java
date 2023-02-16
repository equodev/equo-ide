package dev.equo.solstice.p2;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class P2QueryResult implements Serializable {

	private static final long serialVersionUID = 1L;

	private List<String> mavenCoordinates;
	private List<File> downloadedP2Jars;

	P2QueryResult(P2Query query, P2Client.Caching cachingPolicy) {
		this.mavenCoordinates = query.getJarsOnMavenCentral();
		this.downloadedP2Jars = new ArrayList<>();
		try (var client = new P2Client(cachingPolicy)) {
			for (P2Unit unit : query.getJarsNotOnMavenCentral()) {
				downloadedP2Jars.add(client.download(unit));
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public List<String> getJarsOnMavenCentral() {
		return new ArrayList<>(mavenCoordinates);
	}

	public List<File> getJarsNotOnMavenCentral() {
		return new ArrayList<>(downloadedP2Jars);
	}
}
