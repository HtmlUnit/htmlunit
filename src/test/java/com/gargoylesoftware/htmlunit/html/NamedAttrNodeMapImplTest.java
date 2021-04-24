/*
 * Copyright (c) 2002-2021 Gargoyle Software Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.gargoylesoftware.htmlunit.html;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.junit.Test;

/**
 * Tests for {@link NamedAttrNodeMapImpl}.
 *
 * @author Ronald Brill
 * @author Frank Danek
 */
public class NamedAttrNodeMapImplTest {

    /**
     * Test constructors.
     * @throws Exception if an error occurs
     */
    @Test
    public void construction() throws Exception {
        try {
            new NamedAttrNodeMapImpl(null, true);
            fail("IllegalArgumentException expected.");
        }
        catch (final IllegalArgumentException e) {
            // expected
        }

        try {
            new NamedAttrNodeMapImpl(null, false);
            fail("IllegalArgumentException expected.");
        }
        catch (final IllegalArgumentException e) {
            // expected
        }

        final DomElement dom = new HtmlBreak("", null, null);

        NamedAttrNodeMapImpl map = new NamedAttrNodeMapImpl(dom, true);
        assertTrue(map.isEmpty());

        final Map<String, DomAttr> attribs = new HashMap<>();
        map = new NamedAttrNodeMapImpl(dom, true, attribs);
        assertTrue(map.isEmpty());
    }

    /**
     * Test the case sensitive version.
     * @throws Exception if an error occurs
     */
    @Test
    public void caseSensitive() throws Exception {
        final DomElement dom = new HtmlBreak("", null, null);

        NamedAttrNodeMapImpl map = new NamedAttrNodeMapImpl(dom, true);
        assertTrue(map.isEmpty());
        map.put("Key", new DomAttr(null, "", "", null, false));
        assertTrue(map.containsKey("Key"));
        assertFalse(map.containsKey("key"));
        assertNotNull(map.get("Key"));
        assertNull(map.get("key"));

        final Map<String, DomAttr> attribs = new HashMap<>();
        attribs.put("Key", new DomAttr(null, "", "", null, false));

        map = new NamedAttrNodeMapImpl(dom, true, attribs);
        assertTrue(map.containsKey("Key"));
        assertFalse(map.containsKey("key"));
        assertNotNull(map.get("Key"));
        assertNull(map.get("key"));
    }

    /**
     * Test the case insensitive version.
     * @throws Exception if an error occurs
     */
    @Test
    public void caseInSensitive() throws Exception {
        final DomElement dom = new HtmlBreak("", null, null);

        NamedAttrNodeMapImpl map = new NamedAttrNodeMapImpl(dom, false);
        assertTrue(map.isEmpty());
        map.put("Key", new DomAttr(null, "", "", null, false));
        assertTrue(map.containsKey("Key"));
        assertTrue(map.containsKey("key"));
        assertNotNull(map.get("Key"));
        assertNotNull(map.get("key"));

        final Map<String, DomAttr> attribs = new HashMap<>();
        attribs.put("Key", new DomAttr(null, "", "", null, false));

        map = new NamedAttrNodeMapImpl(dom, false, attribs);
        assertTrue(map.containsKey("Key"));
        assertTrue(map.containsKey("key"));
        assertNotNull(map.get("Key"));
        assertNotNull(map.get("key"));
    }

    /**
     * Test the case insensitive version.
     * @throws Exception if an error occurs
     */
    @Test
    public void preseveOrderCaseInsensitive() throws Exception {
        final DomElement dom = new HtmlBreak("", null, null);

        final NamedAttrNodeMapImpl map = new NamedAttrNodeMapImpl(dom, false);
        assertTrue(map.isEmpty());
        map.put("Key1", new DomAttr(null, "", "attr1", null, false));
        map.put("Key2", new DomAttr(null, "", "attr2", null, false));

        assertTrue(map.containsKey("Key1"));
        assertTrue(map.containsKey("Key2"));

        assertEquals("attr1", map.item(0).getNodeName());
        assertEquals("attr2", map.item(1).getNodeName());

        final Iterator<String> keys = map.keySet().iterator();
        assertEquals("key1", keys.next());
        assertEquals("key2", keys.next());

        final Iterator<Map.Entry<String, DomAttr>> attrs = map.entrySet().iterator();

        Map.Entry<String, DomAttr> entry = attrs.next();
        assertEquals("key1", entry.getKey());
        assertEquals("attr1", entry.getValue().getNodeName());

        entry = attrs.next();
        assertEquals("key2", entry.getKey());
        assertEquals("attr2", entry.getValue().getNodeName());
    }

    /**
     * Test the case insensitive version.
     * @throws Exception if an error occurs
     */
    @Test
    public void preseveOrderCaseSensitive() throws Exception {
        final DomElement dom = new HtmlBreak("", null, null);

        final NamedAttrNodeMapImpl map = new NamedAttrNodeMapImpl(dom, true);
        assertTrue(map.isEmpty());
        map.put("Key1", new DomAttr(null, "", "attr1", null, false));
        map.put("Key2", new DomAttr(null, "", "attr2", null, false));

        assertTrue(map.containsKey("Key1"));
        assertTrue(map.containsKey("Key2"));

        assertEquals("attr1", map.item(0).getNodeName());
        assertEquals("attr2", map.item(1).getNodeName());

        final Iterator<String> keys = map.keySet().iterator();
        assertEquals("Key1", keys.next());
        assertEquals("Key2", keys.next());

        final Iterator<Map.Entry<String, DomAttr>> attrs = map.entrySet().iterator();

        Map.Entry<String, DomAttr> entry = attrs.next();
        assertEquals("Key1", entry.getKey());
        assertEquals("attr1", entry.getValue().getNodeName());

        entry = attrs.next();
        assertEquals("Key2", entry.getKey());
        assertEquals("attr2", entry.getValue().getNodeName());
    }
}
