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

import java.util.function.Function;

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
class TableColumn {
	public final String header;
	public final Table.Align headerAlign;
	public final Table.Align dataAlign;

	/** A Column with a name. */
	public TableColumn(String headerName) {
		this(headerName, Table.Align.LEFT, Table.Align.LEFT);
	}

	/** A Column with a name and alignment. */
	public TableColumn(String header, Table.Align headerAlign, Table.Align dataAlign) {
		this.header = header;
		this.headerAlign = headerAlign;
		this.dataAlign = dataAlign;
	}

	/** An object which can extract data with which to populate its column. */
	public <T> Data<T> with(Function<T, String> getter) {
		return new Data<>(this, getter);
	}

	/** Represents a Data-driven column. */
	public static class Data<T> {
		public final TableColumn column;
		public final Function<T, String> getter;

		private Data(TableColumn column, Function<T, String> getter) {
			this.column = column;
			this.getter = getter;
		}
	}
}
