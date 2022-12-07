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

import java.lang.reflect.InvocationTargetException;
import java.util.Hashtable;
import org.eclipse.equinox.log.ExtendedLogReaderService;
import org.eclipse.equinox.log.ExtendedLogService;
import org.eclipse.osgi.internal.framework.EquinoxContainer;
import org.eclipse.osgi.internal.log.ExtendedLogReaderServiceFactory;
import org.eclipse.osgi.internal.log.ExtendedLogServiceFactory;
import org.objenesis.ObjenesisStd;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.service.log.LogLevel;
import org.osgi.service.log.LogReaderService;
import org.osgi.service.log.LogService;
import org.osgi.service.log.LoggerFactory;
import org.osgi.service.log.admin.LoggerAdmin;

/** Stripped down version of org.eclipse.osgi.internal.log.LogServiceManager */
class ShimLogServiceManager {
	private static final String[] LOGSERVICE_CLASSES = {
		LogService.class.getName(), LoggerFactory.class.getName(), ExtendedLogService.class.getName()
	};
	private static final String[] LOGREADERSERVICE_CLASSES = {
		LogReaderService.class.getName(), ExtendedLogReaderService.class.getName()
	};

	private final ExtendedLogReaderServiceFactory logReaderServiceFactory;
	private final ExtendedLogServiceFactory logServiceFactory;

	public ShimLogServiceManager(
			int maxHistory, LogLevel defaultLevel, boolean captureLogEntryLocation) {
		logReaderServiceFactory = new ExtendedLogReaderServiceFactory(maxHistory, defaultLevel);
		logServiceFactory =
				new ExtendedLogServiceFactory(logReaderServiceFactory, captureLogEntryLocation);
	}

	public void start(BundleContext context) {
		logReaderServiceFactory.start(new ObjenesisStd().newInstance(EquinoxContainer.class));

		context.addBundleListener(logServiceFactory);
		context.registerService(LOGREADERSERVICE_CLASSES, logReaderServiceFactory, null);
		var logServiceRegistration =
				context.registerService(LOGSERVICE_CLASSES, logServiceFactory, null);
		Hashtable<String, Object> loggerAdminProps = new Hashtable<>();
		loggerAdminProps.put(
				"osgi.log.service.id",
				logServiceRegistration.getReference().getProperty(Constants.SERVICE_ID)); // $NON-NLS-1$

		try {
			var getLoggerAdmin = logServiceFactory.getClass().getDeclaredMethod("getLoggerAdmin");
			getLoggerAdmin.setAccessible(true);
			context.registerService(
					LoggerAdmin.class,
					(LoggerAdmin) getLoggerAdmin.invoke(logServiceFactory),
					loggerAdminProps);
		} catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
			throw Unchecked.wrap(e);
		}
	}
}
