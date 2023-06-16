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
package dev.equo.solstice;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CapabilityTest {
	@Test
	public void individualSubset() {
		var nameFoo = new Capability("ns", "name", "foo");
		var classBar = new Capability("ns", "class", "bar");
		Assertions.assertFalse(nameFoo.isSubsetOf(classBar));
		Assertions.assertFalse(classBar.isSubsetOf(nameFoo));
		Assertions.assertFalse(nameFoo.isSupersetOf(classBar));
		Assertions.assertFalse(classBar.isSupersetOf(nameFoo));

		var longer = new Capability("ns");
		longer.add("name", "foo");
		longer.add("class", "bar");
		Assertions.assertTrue(nameFoo.isSubsetOf(longer));
		Assertions.assertTrue(classBar.isSubsetOf(longer));
		Assertions.assertTrue(longer.isSupersetOf(classBar));
		Assertions.assertTrue(longer.isSupersetOf(nameFoo));
		Assertions.assertFalse(nameFoo.isSupersetOf(longer));
		Assertions.assertFalse(classBar.isSupersetOf(longer));
		Assertions.assertFalse(longer.isSubsetOf(classBar));
		Assertions.assertFalse(longer.isSubsetOf(nameFoo));
	}

	@Test
	public void supersetSet() {
		var nameFoo = new Capability("ns", "name", "foo");
		var longer = new Capability("ns");
		longer.add("name", "foo");
		longer.add("class", "bar");

		var set = new Capability.SupersetSet();
		Assertions.assertFalse(set.containsAnySupersetOf(nameFoo));
		Assertions.assertFalse(set.containsAnySupersetOf(longer));
		set.add(longer);
		Assertions.assertTrue(set.containsAnySupersetOf(longer));
		Assertions.assertEquals("[ns:name=foo,class=bar]", set.toString());

		// name=foo is a subset of name=foo,class=bar
		Assertions.assertEquals(longer, set.getAnySupersetOf(nameFoo));

		// when we add name=foo, it gets added, and becomes its own superset
		set.add(nameFoo);
		Assertions.assertEquals("[ns:name=foo, ns:name=foo,class=bar]", set.toString());
		Assertions.assertEquals(nameFoo, set.getAnySupersetOf(nameFoo));

		// when we add class=bar, it gets added, but long is still the superset
		var classBar = new Capability("ns", "class", "bar");
		Assertions.assertEquals(longer, set.getAnySupersetOf(classBar));
		set.add(classBar);
		Assertions.assertEquals("[ns:class=bar, ns:name=foo, ns:name=foo,class=bar]", set.toString());
		Assertions.assertEquals(longer, set.getAnySupersetOf(classBar));

		// we don't make any particular guarantees about the closest superset, we just say any
		// we'll see if the OSGi requirements actually demand more of us at some point
	}
}
