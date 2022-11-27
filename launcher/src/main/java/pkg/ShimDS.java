package pkg;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.zip.ZipFile;
import org.osgi.framework.Bundle;
import org.osgi.framework.wiring.BundleCapability;
import org.osgi.framework.wiring.BundleRequirement;
import org.osgi.framework.wiring.BundleRevision;
import org.osgi.framework.wiring.BundleWire;
import org.osgi.framework.wiring.BundleWiring;
import org.osgi.resource.Capability;
import org.osgi.resource.Requirement;
import org.osgi.resource.Wire;

class ShimDS {
	static final String SERVICE_COMPONENT = "Service-Component";
	private static final String STAR_DOT_XML = "OSGI-INF/*.xml";

	static String cleanHeader(String jarFile, String header) {
		if (!STAR_DOT_XML.equals(header)) {
			return header;
		} else {
			try {
				return starDotXML(jarFile).stream().collect(Collectors.joining(","));
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}

	private static List<String> starDotXML(String jarFile) throws IOException {
		String prefix = "jar:file:";
		if (!jarFile.startsWith(prefix)) {
			throw new IllegalArgumentException("jar does not start with expected prefix: " + jarFile);
		}
		if (!jarFile.endsWith("!")) {
			throw new IllegalArgumentException("jar does not end with expected suffix: !");
		}
		List<String> dotXml = new ArrayList<>();
		try (ZipFile file = new ZipFile(jarFile.substring(prefix.length(), jarFile.length() - 1))) {
			var entries = file.entries();
			while (entries.hasMoreElements()) {
				var entry = entries.nextElement().getName();
				if (entry.startsWith("OSGI-INF/") && entry.endsWith(".xml")) {
					dotXml.add(entry);
				}
			}
		}
		return dotXml;
	}

	static class BundleWiringImpl implements BundleWiring {
		@Override
		public List<BundleWire> getRequiredWires(String namespace) {
			return Collections.emptyList();
		}

		@Override
		public boolean isInUse() {
			return true;
		}

		@Override
		public List<BundleWire> getProvidedWires(String namespace) {
			return Collections.emptyList();
		}

		@Override
		public boolean isCurrent() {
			throw new UnsupportedOperationException();
		}

		@Override
		public List<BundleCapability> getCapabilities(String namespace) {
			throw new UnsupportedOperationException();
		}

		@Override
		public List<BundleRequirement> getRequirements(String namespace) {
			throw new UnsupportedOperationException();
		}

		@Override
		public BundleRevision getRevision() {
			throw new UnsupportedOperationException();
		}

		@Override
		public ClassLoader getClassLoader() {
			throw new UnsupportedOperationException();
		}

		@Override
		public List<URL> findEntries(String path, String filePattern, int options) {
			throw new UnsupportedOperationException();
		}

		@Override
		public Collection<String> listResources(String path, String filePattern, int options) {
			throw new UnsupportedOperationException();
		}

		@Override
		public List<Capability> getResourceCapabilities(String namespace) {
			throw new UnsupportedOperationException();
		}

		@Override
		public List<Requirement> getResourceRequirements(String namespace) {
			throw new UnsupportedOperationException();
		}

		@Override
		public List<Wire> getProvidedResourceWires(String namespace) {
			throw new UnsupportedOperationException();
		}

		@Override
		public List<Wire> getRequiredResourceWires(String namespace) {
			throw new UnsupportedOperationException();
		}

		@Override
		public BundleRevision getResource() {
			throw new UnsupportedOperationException();
		}

		@Override
		public Bundle getBundle() {
			throw new UnsupportedOperationException();
		}
	}
}
