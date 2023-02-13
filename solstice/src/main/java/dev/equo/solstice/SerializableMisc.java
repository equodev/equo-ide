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
package dev.equo.solstice;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;

public class SerializableMisc {
	public static void toFile(Serializable obj, File file) {
		try {
			java.nio.file.Files.createDirectories(file.getParentFile().toPath());
			try (OutputStream fileStream = new FileOutputStream(file);
					ObjectOutputStream objectStream = new ObjectOutputStream(fileStream)) {
				objectStream.writeObject(obj);
			}
		} catch (IOException e) {
			throw Unchecked.wrap(e);
		}
	}

	@SuppressWarnings("unchecked")
	public static <T> T fromFile(Class<T> clazz, File file) {
		try (InputStream fileStream = new FileInputStream(file);
				ObjectInputStream objectStream = new ObjectInputStream(fileStream)) {
			Object obj = objectStream.readObject();
			if (clazz.isInstance(obj)) {
				return (T) obj;
			} else {
				throw new IllegalArgumentException("Expected " + clazz + " but was " + obj.getClass());
			}
		} catch (IOException | ClassNotFoundException e) {
			throw Unchecked.wrap(e);
		}
	}
}
