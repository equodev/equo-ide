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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.function.BiConsumer;
import javax.annotation.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * If you add a {@code Bundle-IdeHook} header to the manifest of a jar that points to the fully
 * qualified name of a class that implements this interface, then that class will be instantiated
 * and called at various points in the lifecycle of the IDE.
 */
public interface IdeHook extends Serializable {
	@SuppressWarnings("serial")
	class List extends ArrayList<IdeHook> {
		public List copy() {
			List copy = new List();
			copy.addAll(this);
			return copy;
		}

		InstantiatedList instantiate() {
			var list = new InstantiatedList();
			for (int i = 0; i < size(); ++i) {
				var hookGenerator = get(i);
				try {
					list.list.add(hookGenerator.instantiate());
				} catch (Exception e) {
					list.logError(hookGenerator, e);
				}
			}
			return list;
		}
	}

	class InstantiatedList {
		interface ThrowingConsumer {
			void accept(IdeHookInstantiated hook) throws Exception;
		}

		interface ThrowingBiConsumer<T> {
			void accept(IdeHookInstantiated hook, T arg) throws Exception;
		}

		private final ArrayList<IdeHookInstantiated> list = new ArrayList<>();
		private final Logger logger = LoggerFactory.getLogger(IdeHook.class);
		private @Nullable ArrayList<Map.Entry<Object, Exception>> errorsToReport = new ArrayList<>();
		private @Nullable BiConsumer<Object, Exception> errorLogger;

		private InstantiatedList() {}

		@SuppressWarnings("unchecked")
		@Nullable
		<T extends IdeHookInstantiated> T find(Class<T> clazz) {
			for (var e : list) {
				if (clazz.isInstance(e)) {
					return (T) e;
				}
			}
			return null;
		}

		void logError(Object hook, Exception e) {
			logger.error("IdeHook failure " + hook, e);
			if (errorLogger == null) {
				errorsToReport.add(Map.entry(hook, e));
			} else {
				errorLogger.accept(hook, e);
			}
		}

		/**
		 * Whenever we call IdeHook methods using the `forEach` methods, we catch the exceptions. If an
		 * exception gets thrown, we always log it to the slf4j logger. If an error logger has been set,
		 * then we pass the exception there too. If an error logger has *not* been set, then we store
		 * the exception. When a logger is eventually set, we pass all the exceptions to it.
		 */
		void setErrorLogger(BiConsumer<Object, Exception> errorLogger) {
			if (this.errorLogger != null) {
				try {
					throw new IllegalArgumentException("setErrorLogger was called twice!");
				} catch (IllegalArgumentException e) {
					logger.warn(e.getMessage(), e);
				}
				return;
			}
			this.errorLogger = errorLogger;
			for (var entry : errorsToReport) {
				errorLogger.accept(entry.getKey(), entry.getValue());
			}
			errorsToReport = null;
		}

		void forEach(ThrowingConsumer method) {
			for (IdeHookInstantiated hook : list) {
				try {
					method.accept(hook);
				} catch (Exception e) {
					logError(hook, e);
				}
			}
		}

		<T> void forEach(ThrowingBiConsumer<T> method, T arg) {
			for (IdeHookInstantiated hook : list) {
				try {
					method.accept(hook, arg);
				} catch (Exception e) {
					logError(hook, e);
				}
			}
		}

		Iterator<IdeHookInstantiated> iterator() {
			return list.iterator();
		}
	}

	IdeHookInstantiated instantiate() throws Exception;

	static IdeHookInstantiated usingReflection(String implementationClassName, IdeHook constructorArg)
			throws Exception {
		var clazz = Class.forName(implementationClassName);
		var constructor = clazz.getDeclaredConstructor(constructorArg.getClass());
		return (IdeHookInstantiated) constructor.newInstance(constructorArg);
	}
}
