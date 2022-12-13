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
import dev.equo.solstice.p2.P2Query;
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

public abstract class EquoIdeTask extends DefaultTask {
	@Internal
	public abstract Property<P2Query> getQuery();

	@Internal
	public abstract Property<FileCollection> getClassPath();

	@Internal
	public abstract Property<File> getWorkspaceDir();

	@Internal
	public abstract Property<Boolean> getIsTestOnly();

	@Inject
	public abstract ObjectFactory getObjectFactory();

	@TaskAction
	public void launch() throws IOException, InterruptedException {
		var cp = getClassPath().get();

		var workspaceDir = getWorkspaceDir().get();
		var nestedJarFolder = new File(workspaceDir, NestedBundles.DIR);
		var allNested =
				NestedBundles.inFiles(cp).extractAllNestedJars(nestedJarFolder).stream()
						.map(e -> e.getValue())
						.collect(Collectors.toList());
		var nestedFileCollection = getObjectFactory().fileCollection().from(allNested);

		var query = getQuery().get();

		var result =
				NestedBundles.javaExec(
						"dev.equo.solstice.IdeMain",
						cp.plus(nestedFileCollection),
						"-installDir",
						workspaceDir.getAbsolutePath(),
						"-equoTestOnly",
						Boolean.toString(getIsTestOnly().get()));
		System.out.println(result);
	}
}
