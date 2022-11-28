package dev.equo.ide.gradle;

import org.gradle.api.Plugin;
import org.gradle.api.Project;

public class EquoIdeGradlePlugin implements Plugin<Project> {
	static final String MINIMUM_GRADLE = "5.0";

	@Override
	public void apply(Project project) {
		EquoIdeExtension extension = project.getExtensions().create("equoIde", EquoIdeExtension.class);
		project
				.getTasks()
				.register(
						"equoIde",
						EquoIdeTask.class,
						task -> {
							task.setGroup("IDE");
							task.setDescription("Launches EquoIDE");
							task.extension = extension;
						});
	}
}
