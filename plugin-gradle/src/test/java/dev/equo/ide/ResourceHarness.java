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
package dev.equo.ide;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Arrays;
import org.junit.jupiter.api.io.TempDir;

public class ResourceHarness {
	/**
	 * On OS X, the temp folder is a symlink, and some of gradle's stuff breaks symlinks. By only
	 * accessing it through the {@link #rootFolder()} and {@link #newFile(String)} apis, we can
	 * guarantee there will be no symlink problems.
	 */
	@TempDir File folderDontUseDirectly;

	/** Returns the root folder (canonicalized to fix OS X issue) */
	protected final File rootFolder() throws IOException {
		return folderDontUseDirectly.getCanonicalFile();
	}

	/** Returns a new child of the root folder. */
	protected final File newFile(String subpath) throws IOException {
		return new File(rootFolder(), subpath);
	}

	protected final WriteAsserter setFile(String path) throws IOException {
		return new WriteAsserter(newFile(path));
	}

	protected static class WriteAsserter {
		private File file;

		private WriteAsserter(File file) {
			file.getParentFile().mkdirs();
			this.file = file;
		}

		public File toContent(byte[] content) throws IOException {
			Files.write(file.toPath(), content);
			return file;
		}

		public File toContent(String content) throws IOException {
			return toContent(content.getBytes(StandardCharsets.UTF_8));
		}

		public File toLines(String... lines) throws IOException {
			return toContent(String.join("\n", Arrays.asList(lines)));
		}

		public File toResource(String path) throws IOException {
			try (var input = ResourceHarness.class.getResource(path).openConnection().getInputStream()) {
				return toContent(input.readAllBytes());
			}
		}
	}
}
