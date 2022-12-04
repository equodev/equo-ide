/*******************************************************************************
 * Copyright (c) 2022 EquoTech, Inc. and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     EquoTech, Inc. - initial API and implementation
 *******************************************************************************/
package dev.equo.ide.gradle;

import dev.equo.solstice.NestedBundles;
import java.io.File;
import java.io.IOException;
import java.util.stream.Collectors;
import javax.inject.Inject;
import org.gradle.api.DefaultTask;
import org.gradle.api.file.FileCollection;
import org.gradle.api.model.ObjectFactory;
import org.gradle.api.provider.Property;
import org.gradle.api.tasks.Internal;
import org.gradle.api.tasks.TaskAction;
import org.gradle.process.ExecOperations;

public abstract class EquoIdeTask extends DefaultTask {
	@Internal
	public abstract Property<EquoIdeExtension> getExtension();

	@Internal
	public abstract Property<FileCollection> getClassPath();

	@Internal
	public abstract Property<File> getInstallDir();

	@Internal
	public abstract Property<Boolean> getIsTestOnly();

	@Inject
	public abstract ExecOperations getExecOperations();

	@Inject
	public abstract ObjectFactory getObjectFactory();

	@TaskAction
	public void launch() throws IOException, InterruptedException {
		var cp = getClassPath().get();

		var installDir = getInstallDir().get();
		var nestedJarFolder = new File(installDir, NestedBundles.DIR);
		var allNested =
				NestedBundles.inFiles(cp).extractAllNestedJars(nestedJarFolder).stream()
						.map(e -> e.getValue())
						.collect(Collectors.toList());
		var nestedFileCollection = getObjectFactory().fileCollection().from(allNested);

		var result =
				NestedBundles.javaExec(
						"dev.equo.solstice.SolsticeIDE",
						cp.plus(nestedFileCollection),
						"-installDir",
						installDir.getAbsolutePath(),
						"-equoTestOnly",
						Boolean.toString(getIsTestOnly().get()));
		System.out.println(result);
	}
}
