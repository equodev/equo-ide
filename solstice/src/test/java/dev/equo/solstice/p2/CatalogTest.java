/*******************************************************************************
 * Copyright (c) 2023-2025 EquoTech, Inc. and others.
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

import static com.diffplug.selfie.Selfie.expectSelfie;

import com.diffplug.common.swt.os.SwtPlatform;
import dev.equo.ide.Catalog;
import dev.equo.ide.CatalogDsl;
import dev.equo.ide.IdeHook;
import org.junit.jupiter.api.Test;

public class CatalogTest {
	private static final TestCatalogDsl JAVA_11_PLATFORM =
			new TestCatalogDsl(Catalog.PLATFORM, "4.27");

	@Test
	public void equoChatGptDefault() {
		expectSelfie(requestModel(JAVA_11_PLATFORM, new TestCatalogDsl(Catalog.CHATGPT, null)))
				.toMatchDisk();
	}

	@Test
	public void equoChatGptFixed() {
		expectSelfie(requestModel(JAVA_11_PLATFORM, new TestCatalogDsl(Catalog.CHATGPT, "9.9.9")))
				.toMatchDisk();
	}

	static class TestCatalogDsl extends CatalogDsl {
		public TestCatalogDsl(Catalog catalog, String urlOverride) {
			super(catalog);
			super.setUrlOverride(urlOverride);
		}
	}

	private String requestModel(TestCatalogDsl... catalogs) {
		var list = new CatalogDsl.TransitiveAwareList<TestCatalogDsl>();
		for (var catalog : catalogs) {
			list.add(catalog);
		}
		var model = new P2Model();
		var hooks = new IdeHook.List();
		list.putInto(model, hooks);

		String result = ConsoleTable.request(model, ConsoleTable.Format.csv);
		for (SwtPlatform platform : SwtPlatform.getAll()) {
			result = result.replace(platform.toString(), "platform-specific");
		}
		return result;
	}
}
