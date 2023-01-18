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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import org.eclipse.swt.widgets.Display;
import org.osgi.framework.BundleContext;

public interface IdeHook extends Serializable {
	class List extends ArrayList<IdeHook> {
		void callEach(Consumer<IdeHook> method) {
			for (IdeHook hook : this) {
				method.accept(hook);
			}
		}

		<T> void callEach(BiConsumer<IdeHook, T> method, T arg) {
			for (IdeHook hook : this) {
				method.accept(hook, arg);
			}
		}
	}

	default void beforeOsgi() {}

	default void afterOsgi(BundleContext context) {}

	default void beforeDisplay() {}

	default void afterDisplay(Display display) {}

	/**
	 * This method is called during workbench initialization prior to any windows being opened.
	 *
	 * <p>{@link org.eclipse.ui.application.WorkbenchAdvisor#initialize}
	 */
	default void initialize() {}

	/**
	 * Performs arbitrary actions just before the first workbench window is opened (or restored).
	 *
	 * <p>This method is called after the workbench has been initialized and just before the first
	 * window is about to be opened.
	 *
	 * <p>{@link org.eclipse.ui.application.WorkbenchAdvisor#preStartup}
	 */
	default void preStartup() {}

	/**
	 * Performs arbitrary actions after the workbench windows have been opened (or restored), but
	 * before the main event loop is run.
	 *
	 * <p>{@link org.eclipse.ui.application.WorkbenchAdvisor#postStartup}
	 */
	default void postStartup() {}

	/**
	 * Performs arbitrary finalization before the workbench is about to shut down.
	 *
	 * <p>{@link org.eclipse.ui.application.WorkbenchAdvisor#preShutdown}
	 *
	 * @return <code>true</code> to allow the workbench to proceed with shutdown, <code>false</code>
	 *     to veto a non-forced shutdown
	 */
	default boolean preShutdown() {
		return true;
	}

	/**
	 * Performs arbitrary finalization after the workbench stops running.
	 *
	 * <p>{@link org.eclipse.ui.application.WorkbenchAdvisor#postShutdown}
	 */
	default void postShutdown() {}
}
