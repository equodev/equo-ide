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
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.util.Calendar;
import java.util.Date;
import org.eclipse.osgi.framework.log.FrameworkLog;
import org.eclipse.osgi.framework.log.FrameworkLogEntry;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleException;
import org.osgi.framework.FrameworkEvent;

// MOSTLY COPIED FROM org.eclipse.osgi.internal.log.EquinoxLogWriter
/*******************************************************************************
 * Copyright (c) 2004, 2019 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
public class ShimFrameworkLog implements FrameworkLog {
	private File file;
	private Writer writer;
	private boolean consoleLog = false;

	@Override
	public void setFile(File newFile, boolean append) throws IOException {
		close();
		this.file = newFile;
		this.writer = new FileWriter(file, append);
	}

	@Override
	public File getFile() {
		return file;
	}

	@Override
	public void setWriter(Writer newWriter, boolean append) {
		close();
		this.writer = newWriter;
	}

	@Override
	public void setConsoleLog(boolean consoleLog) {
		this.consoleLog = consoleLog;
	}

	@Override
	public void close() {
		if (writer != null) {
			try {
				writer.close();
				writer = null;
			} catch (IOException e) {
				throw Unchecked.wrap(e);
			}
		}
	}

	@Override
	public void log(FrameworkEvent frameworkEvent) {
		Bundle b = frameworkEvent.getBundle();
		Throwable t = frameworkEvent.getThrowable();
		String entry = b.getSymbolicName() == null ? b.getLocation() : b.getSymbolicName();
		int severity;
		switch (frameworkEvent.getType()) {
			case FrameworkEvent.INFO:
				severity = FrameworkLogEntry.INFO;
				break;
			case FrameworkEvent.ERROR:
				severity = FrameworkLogEntry.ERROR;
				break;
			case FrameworkEvent.WARNING:
				severity = FrameworkLogEntry.WARNING;
				break;
			default:
				severity = FrameworkLogEntry.OK;
		}
		FrameworkLogEntry logEntry =
				new FrameworkLogEntry(entry, severity, 0, "", 0, t, null); // $NON-NLS-1$
		log(logEntry);
	}

	@Override
	public void log(FrameworkLogEntry logEntry) {
		try {
			writeLog(0, logEntry);
		} catch (IOException e) {
			throw Unchecked.wrap(e);
		}
	}

	private void writeLog(int depth, FrameworkLogEntry entry) throws IOException {
		writeEntry(depth, entry);
		writeMessage(entry);
		writeStack(entry);

		FrameworkLogEntry[] children = entry.getChildren();
		if (children != null) {
			for (FrameworkLogEntry child : children) {
				writeLog(depth + 1, child);
			}
		}
	}

	private static final String ENTRY = "!ENTRY"; // $NON-NLS-1$
	private static final String SUBENTRY = "!SUBENTRY"; // $NON-NLS-1$
	private static final String MESSAGE = "!MESSAGE"; // $NON-NLS-1$
	private static final String STACK = "!STACK"; // $NON-NLS-1$

	private void writeMessage(FrameworkLogEntry entry) throws IOException {
		write(MESSAGE);
		writeSpace();
		writeln(entry.getMessage());
	}

	private void writeStack(FrameworkLogEntry entry) throws IOException {
		Throwable t = entry.getThrowable();
		if (t != null) {
			String stack = getStackTrace(t);
			write(STACK);
			writeSpace();
			write(Integer.toString(entry.getStackCode()));
			writeln();
			write(stack);
		}
	}

	private String getStackTrace(Throwable t) {
		if (t == null) return null;

		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);

		t.printStackTrace(pw);
		// ensure the root exception is fully logged
		Throwable root = getRoot(t);
		if (root != null) {
			pw.println("Root exception:"); // $NON-NLS-1$
			root.printStackTrace(pw);
		}
		return sw.toString();
	}

	private Throwable getRoot(Throwable t) {
		Throwable root = null;
		if (t instanceof BundleException) root = ((BundleException) t).getNestedException();
		if (t instanceof InvocationTargetException)
			root = ((InvocationTargetException) t).getTargetException();
		// skip inner InvocationTargetExceptions and BundleExceptions
		if (root instanceof InvocationTargetException || root instanceof BundleException) {
			Throwable deeplyNested = getRoot(root);
			if (deeplyNested != null)
				// if we have something more specific, use it, otherwise keep what we have
				root = deeplyNested;
		}
		return root;
	}

	private void writeEntry(int depth, FrameworkLogEntry entry) throws IOException {
		if (depth == 0) {
			writeln(); // write a blank line before all !ENTRY tags bug #64406
			write(ENTRY);
		} else {
			write(SUBENTRY);
			writeSpace();
			write(Integer.toString(depth));
		}
		writeSpace();
		write(entry.getEntry());
		writeSpace();
		write(Integer.toString(entry.getSeverity()));
		writeSpace();
		write(Integer.toString(entry.getBundleCode()));
		writeSpace();
		write(getDate(new Date()));
		writeln();
	}

	private String getDate(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		StringBuilder sb = new StringBuilder();
		appendPaddedInt(c.get(Calendar.YEAR), 4, sb).append('-');
		appendPaddedInt(c.get(Calendar.MONTH) + 1, 2, sb).append('-');
		appendPaddedInt(c.get(Calendar.DAY_OF_MONTH), 2, sb).append(' ');
		appendPaddedInt(c.get(Calendar.HOUR_OF_DAY), 2, sb).append(':');
		appendPaddedInt(c.get(Calendar.MINUTE), 2, sb).append(':');
		appendPaddedInt(c.get(Calendar.SECOND), 2, sb).append('.');
		appendPaddedInt(c.get(Calendar.MILLISECOND), 3, sb);
		return sb.toString();
	}

	private StringBuilder appendPaddedInt(int value, int pad, StringBuilder buffer) {
		pad = pad - 1;
		if (pad == 0) return buffer.append(Integer.toString(value));
		int padding = (int) Math.pow(10, pad);
		if (value >= padding) return buffer.append(Integer.toString(value));
		while (padding > value && padding > 1) {
			buffer.append('0');
			padding = padding / 10;
		}
		buffer.append(value);
		return buffer;
	}

	private void write(String message) throws IOException {
		if (message != null) {
			writer.write(message);
			if (consoleLog) System.out.print(message);
		}
	}

	private void writeln(String s) throws IOException {
		write(s);
		writeln();
	}

	private void writeln() throws IOException {
		write(System.lineSeparator());
		writer.flush();
	}

	private void writeSpace() throws IOException {
		write(" "); // $NON-NLS-1$
	}
}
