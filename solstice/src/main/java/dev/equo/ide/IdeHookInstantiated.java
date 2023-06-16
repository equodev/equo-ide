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

public interface IdeHookInstantiated {
	/**
	 * The very first method to be called, called as soon as the command line arguments have been
	 * parsed.
	 */
	default void isClean(boolean isClean) throws Exception {}

	/** Called after display is created, ~310ms after isClean. */
	default void afterDisplay(org.eclipse.swt.widgets.Display display) throws Exception {}

	/** Called after the OSGi container is created and populated, ~5850ms after isClean. */
	default void afterOsgi(org.osgi.framework.BundleContext context) throws Exception {}

	/**
	 * This method is called during workbench initialization prior to any windows being opened,
	 * ~6720ms after isClean.
	 *
	 * <p>{@link org.eclipse.ui.application.WorkbenchAdvisor#initialize}
	 */
	default void initialize() throws Exception {}

	/**
	 * Performs arbitrary actions just before the first workbench window is opened (or restored),
	 * ~6740ms after isClean.
	 *
	 * <p>This method is called after the workbench has been initialized and just before the first
	 * window is about to be opened.
	 *
	 * <p>{@link org.eclipse.ui.application.WorkbenchAdvisor#preStartup}
	 */
	default void preStartup() throws Exception {}

	/**
	 * Performs arbitrary actions after the workbench windows have been opened (or restored), but
	 * before the main event loop is run, ~8400ms after isClean.
	 *
	 * <p>{@link org.eclipse.ui.application.WorkbenchAdvisor#postStartup}
	 */
	default void postStartup() throws Exception {}

	/**
	 * Performs arbitrary finalization before the workbench is about to shut down.
	 *
	 * <p>{@link org.eclipse.ui.application.WorkbenchAdvisor#preShutdown}
	 *
	 * @return <code>true</code> to allow the workbench to proceed with shutdown, <code>false</code>
	 *     to veto a non-forced shutdown
	 */
	default boolean preShutdown() throws Exception {
		return true;
	}

	/**
	 * Performs arbitrary finalization after the workbench stops running, ~200ms after preShutdown.
	 *
	 * <p>{@link org.eclipse.ui.application.WorkbenchAdvisor#postShutdown}
	 */
	default void postShutdown() throws Exception {}
}
