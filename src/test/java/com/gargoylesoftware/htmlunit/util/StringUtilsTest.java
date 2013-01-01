/*
 * Copyright (c) 2002-2013 Gargoyle Software Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.gargoylesoftware.htmlunit.util;

import java.awt.Color;

import org.junit.Test;

import com.gargoylesoftware.htmlunit.SimpleWebTestCase;

/**
 * Tests for {@link StringUtils}.
 *
 * @version $Revision$
 * @author Ronald Brill
 */
public class StringUtilsTest extends SimpleWebTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void asColorHexadecimal() throws Exception {
        assertNull(StringUtils.asColorHexadecimal(null));
        assertNull(StringUtils.asColorHexadecimal(""));
        assertNull(StringUtils.asColorHexadecimal("    "));

        assertNull(StringUtils.asColorHexadecimal("#a1"));
        assertEquals(new Color(0, 17, 170), StringUtils.asColorHexadecimal("#0011aa"));
        assertEquals(new Color(0, 17, 170), StringUtils.asColorHexadecimal("#01A"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void isColorRGB() throws Exception {
        assertFalse(StringUtils.isColorRGB(null));
        assertFalse(StringUtils.isColorRGB(""));
        assertFalse(StringUtils.isColorRGB("    "));

        assertFalse(StringUtils.isColorRGB("#a1"));
        assertTrue(StringUtils.isColorRGB("rgb(1,12,13)"));
        assertTrue(StringUtils.isColorRGB("  rgb(1,12,13)   "));
        assertTrue(StringUtils.isColorRGB("rgb  ( 1,  12,  13        )"));
        assertFalse(StringUtils.isColorRGB("rgb(a1,12,13)"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void asColorRGB() throws Exception {
        assertNull(StringUtils.asColorRGB(null));
        assertNull(StringUtils.asColorRGB(""));
        assertNull(StringUtils.asColorRGB("    "));

        assertNull(StringUtils.asColorRGB("#a1"));
        assertEquals(new Color(1, 12, 13), StringUtils.asColorRGB("rgb(1,12,13)"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void findColorRGB() throws Exception {
        assertNull(StringUtils.findColorRGB(null));
        assertNull(StringUtils.findColorRGB(""));
        assertNull(StringUtils.findColorRGB("    "));

        assertNull(StringUtils.findColorRGB("#a1"));
        assertEquals(new Color(1, 12, 13), StringUtils.findColorRGB("rgb(1,12,13)"));
        assertEquals(new Color(1, 12, 13), StringUtils.findColorRGB("beforergb(1,12,13)after"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void formatColor() throws Exception {
        assertEquals("rgb(1, 12, 13)", StringUtils.formatColor(new Color(1, 12, 13)));
    }

    /**
     * Test for method {@link StringUtils#sanitizeForAppendReplacement(String)}.
     */
    @Test
    public void sanitizeForAppendReplacement() {
        assertNull(StringUtils.sanitizeForAppendReplacement(null));
        assertEquals("", StringUtils.sanitizeForAppendReplacement(""));
        assertEquals("aBc", StringUtils.sanitizeForAppendReplacement("aBc"));

        assertEquals("\\$1", StringUtils.sanitizeForAppendReplacement("$1"));
        assertEquals("\\$1\\$2 \\$3", StringUtils.sanitizeForAppendReplacement("$1$2 $3"));
        assertEquals("\\\\1", StringUtils.sanitizeForAppendReplacement("\\1"));
        assertEquals("\\\\1\\$2 \\\\3", StringUtils.sanitizeForAppendReplacement("\\1$2 \\3"));
    }
}
