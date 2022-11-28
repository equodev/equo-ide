package dev.equo.solstice;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.osgi.framework.Bundle;
import org.slf4j.simple.SimpleLogger;

public class SolsticeSmokeTest {
	@Test
	public void bundleIds() {
		System.setProperty(SimpleLogger.DEFAULT_LOG_LEVEL_KEY, "WARN");
		var solstice = Solstice.initialize(new SolsticeConfiguration() {});
		Assertions.assertThat(solstice.systemBundle().getBundleId()).isEqualTo(0);
		int count = 0;
		for (Bundle bundle : solstice.getBundles()) {
			++count;
			Assertions.assertThat(bundle.getBundleId()).isEqualTo(count);
		}
		Assertions.assertThat(count).isGreaterThan(10);
	}
}
