package dev.equo.ide.gradle;

import java.io.IOException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class GradlePluginTest extends GradleHarness {
	@Test
	public void tasks() throws IOException {
		setFile("build.gradle").toLines("plugins { id 'dev.equo.ide' }", "equoIde {", "}");
		String output = gradleRunner().withArguments("tasks").build().getOutput().replace("\r", "");
		Assertions.assertThat(output)
				.contains("IDE tasks\n" + "---------\n" + "equoIde - Launches EquoIDE");
	}
}
