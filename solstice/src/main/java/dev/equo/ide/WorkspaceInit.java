/*******************************************************************************
 * Copyright (c) 2023 EquoTech, Inc. and others.
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
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.TreeMap;
import java.util.function.Function;
import org.slf4j.Logger;

/** Initializes the IDE workspace. */
public class WorkspaceInit {
	private static final Logger logger = org.slf4j.LoggerFactory.getLogger(WorkspaceInit.class);

	private final TreeMap<String, List<Function<byte[], byte[]>>> modifiers = new TreeMap<>();

	private void setFileBinary(String subpath, Function<byte[], byte[]> modifier) {
		modifiers.computeIfAbsent(subpath, k -> new ArrayList<>(1)).add(modifier);
	}

	private void setFile(String subpath, Function<String, String> modifier) {
		setFileBinary(
				subpath,
				bytes ->
						modifier
								.apply(new String(bytes, StandardCharsets.UTF_8))
								.getBytes(StandardCharsets.UTF_8));
	}

	/** Sets the given property at the given subpath. */
	public void setProperty(String subpath, String key, String value) {
		setFile(
				subpath,
				prev -> {
					var props = new Properties();
					try {
						props.load(new StringReader(prev));
						props.put("eclipse.preferences.version", "1");
						props.put(key, value);
						var writer = new StringWriter();
						props.store(writer, null);
						return writer.toString();
					} catch (IOException e) {
						throw new RuntimeException(e);
					}
				});
	}

	/** Copies all initializers from the given workspace into this one. */
	public void copyAllFrom(WorkspaceInit other) {
		for (var modifierList : other.modifiers.entrySet()) {
			for (var modifier : modifierList.getValue()) {
				setFileBinary(modifierList.getKey(), modifier);
			}
		}
	}

	/** Should be called on the root workspace dir, which has folders like instance/install/config. */
	public void applyTo(File workspaceDir) {
		modifiers.forEach(
				(subpath, modifiers) -> {
					File file = new File(workspaceDir, subpath);
					file.getParentFile().mkdirs();
					try {
						byte[] content;
						if (file.exists()) {
							content = Files.readAllBytes(file.toPath());
						} else {
							content = new byte[0];
						}
						for (var modifier : modifiers) {
							content = modifier.apply(content);
						}
						Files.write(file.toPath(), content);
					} catch (IOException e) {
						logger.warn("Failed to write workspace file " + subpath, e);
					}
				});
	}
}
