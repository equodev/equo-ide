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
package dev.equo.solstice.p2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Copyright (C) 2011 K Venkata Sudhakar <kvenkatasudhakar@gmail.com> Modified (C) 2014 and 2022 Ned
 * Twigg <ned.twigg@diffplug.com>
 *
 * <p>Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of the License at
 *
 * <p>http://www.apache.org/licenses/LICENSE-2.0
 *
 * <p>Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */
class Table {
	enum Align {
		LEFT,
		CENTER,
		RIGHT
	}

	public static <T> String getTable(
			ConsoleTable.Format kind, Collection<T> objects, TableColumn.Data<T>... columns) {
		if (kind == ConsoleTable.Format.CSV) {
			var rawColumns = Arrays.stream(columns).map(c -> c.column).toArray(TableColumn[]::new);
			var iter = objects.iterator();
			var rows = new String[objects.size()][];
			for (int r = 0; r < rows.length; ++r) {
				var row = new String[columns.length];
				rows[r] = row;
				var o = iter.next();
				for (int c = 0; c < row.length; ++c) {
					row[c] = columns[c].getter.apply(o);
				}
			}
			return getTable(rawColumns, rows);
		} else if (kind == ConsoleTable.Format.ASCII) {
			return getTable(objects, columns);
		} else {
			throw new IllegalArgumentException("Unknown kind " + kind);
		}
	}

	public static String getTable(
			ConsoleTable.Format format, TableColumn[] columns, String[][] data) {
		if (format == ConsoleTable.Format.CSV) {
			var builder = new StringBuilder();
			for (var column : columns) {
				builder.append(column.header);
				builder.append(',');
			}
			builder.setCharAt(builder.length() - 1, '\n');

			for (var row : data) {
				for (var cell : row) {
					if (cell.indexOf(',') != -1 || cell.indexOf('"') != -1) {
						cell = cell.replace("\"", "\"\"");
						cell = "\"" + cell + "\"";
					}
					builder.append(cell);
					builder.append(',');
				}
				builder.setCharAt(builder.length() - 1, '\n');
			}
			return builder.toString();
		} else if (format == ConsoleTable.Format.ASCII) {
			return getTable(columns, data);
		} else {
			throw new IllegalArgumentException("Unknown format " + format);
		}
	}

	private static <T> String getTable(Collection<T> objects, TableColumn.Data<T>... columns) {
		return getTable(objects, Arrays.asList(columns));
	}

	private static <T> String getTable(Collection<T> objects, List<TableColumn.Data<T>> columns) {
		String[][] data = new String[objects.size()][];

		Iterator<T> iter = objects.iterator();
		int i = 0;
		while (i < objects.size()) {
			T object = iter.next();
			data[i] = new String[columns.size()];
			for (int j = 0; j < columns.size(); ++j) {
				data[i][j] = columns.get(j).getter.apply(object);
			}
			++i;
		}

		TableColumn[] rawColumns =
				columns.stream()
						.map(c -> c.column)
						.collect(Collectors.toList())
						.toArray(new TableColumn[columns.size()]);
		return getTable(rawColumns, data);
	}

	/** Returns a formatted table string. */
	private static String getTable(TableColumn[] headerObjs, String[][] data) {
		if (data == null || data.length == 0) {
			throw new IllegalArgumentException("Please provide valid data : " + data);
		}

		/** Table String buffer */
		StringBuilder tableBuf = new StringBuilder();

		/** Get maximum number of columns across all rows */
		String[] header = getHeaders(headerObjs);
		int colCount = getMaxColumns(header, data);

		/** Get max length of data in each column */
		List<Integer> colMaxLenList = getMaxColLengths(colCount, header, data);

		/** Check for the existence of header */
		if (header != null && header.length > 0) {
			/** 1. Row line */
			tableBuf.append(getRowLineBuf(colCount, colMaxLenList, data));

			/** 2. Header line */
			tableBuf.append(getRowDataBuf(colCount, colMaxLenList, header, headerObjs, true));
		}

		/** 3. Data Row lines */
		tableBuf.append(getRowLineBuf(colCount, colMaxLenList, data));
		String[] rowData = null;

		// Build row data buffer by iterating through all rows
		for (int i = 0; i < data.length; i++) {

			// Build cell data in each row
			rowData = new String[colCount];
			for (int j = 0; j < colCount; j++) {

				if (j < data[i].length) {
					rowData[j] = data[i][j];
				} else {
					rowData[j] = "";
				}
			}

			tableBuf.append(getRowDataBuf(colCount, colMaxLenList, rowData, headerObjs, false));
		}

		/** 4. Row line */
		tableBuf.append(getRowLineBuf(colCount, colMaxLenList, data));
		return tableBuf.toString();
	}

	private static String getRowDataBuf(
			int colCount,
			List<Integer> colMaxLenList,
			String[] row,
			TableColumn[] headerObjs,
			boolean isHeader) {

		StringBuilder rowBuilder = new StringBuilder();
		String formattedData = null;
		Align align;

		for (int i = 0; i < colCount; i++) {
			align = Table.Align.LEFT;
			if (headerObjs != null && i < headerObjs.length) {
				if (isHeader) {
					align = headerObjs[i].headerAlign;
				} else {
					align = headerObjs[i].dataAlign;
				}
			}

			formattedData = i < row.length ? row[i] : "";

			// format = "| %" + colFormat.get(i) + "s ";
			formattedData = "| " + getFormattedData(colMaxLenList.get(i), formattedData, align) + " ";

			if (i + 1 == colCount) {
				formattedData += "|";
			}

			rowBuilder.append(formattedData);
		}

		return rowBuilder.append("\n").toString();
	}

	private static String getFormattedData(int maxLength, String data, Align align) {
		if (data.length() > maxLength) {
			return data;
		}

		boolean toggle = true;

		while (data.length() < maxLength) {
			if (align == Table.Align.LEFT) {
				data = data + " ";
			} else if (align == Table.Align.RIGHT) {
				data = " " + data;
			} else if (align == Table.Align.CENTER) {
				if (toggle) {
					data = " " + data;
					toggle = false;
				} else {
					data = data + " ";
					toggle = true;
				}
			}
		}

		return data;
	}

	/**
	 * Each string item rendering requires the border and a space on both sides.
	 *
	 * <p>12 3 12 3 12 34 +----- +-------- +------+ abc venkat last
	 *
	 * @param colCount
	 * @param colMaxLenList
	 * @param data
	 * @return
	 */
	private static String getRowLineBuf(int colCount, List<Integer> colMaxLenList, String[][] data) {

		StringBuilder rowBuilder = new StringBuilder();
		int colWidth = 0;

		for (int i = 0; i < colCount; i++) {

			colWidth = colMaxLenList.get(i) + 3;

			for (int j = 0; j < colWidth; j++) {
				if (j == 0) {
					rowBuilder.append("+");
				} else if ((i + 1 == colCount && j + 1 == colWidth)) { // for last column close the border
					rowBuilder.append("-+");
				} else {
					rowBuilder.append("-");
				}
			}
		}

		return rowBuilder.append("\n").toString();
	}

	private static int getMaxItemLength(List<String> colData) {
		int maxLength = 0;
		for (int i = 0; i < colData.size(); i++) {
			maxLength = Math.max(colData.get(i).length(), maxLength);
		}
		return maxLength;
	}

	private static int getMaxColumns(String[] header, String[][] data) {
		int maxColumns = 0;
		for (int i = 0; i < data.length; i++) {
			maxColumns = Math.max(data[i].length, maxColumns);
		}
		maxColumns = Math.max(header.length, maxColumns);
		return maxColumns;
	}

	private static List<Integer> getMaxColLengths(int colCount, String[] header, String[][] data) {
		List<Integer> colMaxLenList = new ArrayList<Integer>(colCount);
		List<String> colData = null;
		int maxLength;

		for (int i = 0; i < colCount; i++) {
			colData = new ArrayList<String>();

			if (header != null && i < header.length) {
				colData.add(header[i]);
			}

			for (int j = 0; j < data.length; j++) {
				if (i < data[j].length) {
					colData.add(data[j][i]);
				} else {
					colData.add("");
				}
			}

			maxLength = getMaxItemLength(colData);
			colMaxLenList.add(maxLength);
		}

		return colMaxLenList;
	}

	private static String[] getHeaders(TableColumn[] headerObjs) {
		String[] header = new String[0];
		if (headerObjs != null && headerObjs.length > 0) {
			header = new String[headerObjs.length];
			for (int i = 0; i < headerObjs.length; i++) {
				header[i] = headerObjs[i].header;
			}
		}

		return header;
	}
}
