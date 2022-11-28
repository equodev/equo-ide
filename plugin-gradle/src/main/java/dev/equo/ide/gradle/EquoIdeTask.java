package dev.equo.ide.gradle;

import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;

public class EquoIdeTask extends DefaultTask {
	EquoIdeExtension extension;

	@TaskAction
	public void launch() {
		System.out.println("DO THE LAUNCH");
	}
}
