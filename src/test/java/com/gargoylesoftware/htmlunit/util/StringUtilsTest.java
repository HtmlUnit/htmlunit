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
package com.gargoylesoftware.htmlunit.util;

import java.awt.Color;

import org.junit.Test;

import com.gargoylesoftware.htmlunit.SimpleWebTestCase;

/**
 * Tests for {@link StringUtils}.
 *
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
    public void findColorRGB() throws Exception {
        assertNull(StringUtils.findColorRGB(null));
        assertNull(StringUtils.findColorRGB(""));
        assertNull(StringUtils.findColorRGB("    "));

        assertNull(StringUtils.findColorRGB("#a1"));
        assertNull(StringUtils.findColorRGB("rgb(1,12,256)"));
        assertNull(StringUtils.findColorRGB("rgb(1,256,7)"));
        assertNull(StringUtils.findColorRGB("rgb(256,13,7)"));

        assertEquals(new Color(1, 12, 13), StringUtils.findColorRGB("rgb(1,12,13)"));
        assertEquals(new Color(1, 12, 13), StringUtils.findColorRGB("rgb(  1, \t12, 13  )"));
        assertEquals(new Color(1, 12, 13), StringUtils.findColorRGB("beforergb(1,12,13)after"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void findColorRGBA() throws Exception {
        assertNull(StringUtils.findColorRGBA(null));
        assertNull(StringUtils.findColorRGBA(""));
        assertNull(StringUtils.findColorRGBA("    "));

        assertNull(StringUtils.findColorRGBA("#a1"));
        assertNull(StringUtils.findColorRGBA("rgba(1,12,256, .1)"));

        assertEquals(new Color(1, 12, 13), StringUtils.findColorRGBA("rgba(1,12,13, 1)"));
        assertEquals(new Color(1, 12, 17, 25), StringUtils.findColorRGBA("rgba(1, 12, 17, 0.1)"));
        assertEquals(new Color(1, 12, 17, 25), StringUtils.findColorRGBA("rgba(1, 12, 17, .1)"));
        assertEquals(new Color(1, 12, 13, 127), StringUtils.findColorRGBA("beforergba(1,12,13,0.5)after"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void findColorHSL() throws Exception {
        assertNull(StringUtils.findColorHSL(null));
        assertNull(StringUtils.findColorHSL(""));
        assertNull(StringUtils.findColorHSL("    "));

        assertNull(StringUtils.findColorHSL("#a1"));
        assertNull(StringUtils.findColorHSL("hsl(1,12,256, .1)"));

        assertEquals(new Color(255, 0, 0), StringUtils.findColorHSL("hsl(0, 100%, 50%)"));
        assertEquals(new Color(255, 85, 0), StringUtils.findColorHSL("hsl(20, 100%, 50%)"));
        assertEquals(new Color(204, 68, 0), StringUtils.findColorHSL("hsl(20, 100%, 40%)"));
        assertEquals(new Color(51, 153, 153), StringUtils.findColorHSL("hsl( 180 ,50%, 40% )"));
        assertEquals(new Color(51, 153, 153), StringUtils.findColorHSL("beforehsl(180,50%,40%)after"));

        assertEquals(new Color(37, 58, 59), StringUtils.findColorHSL("hsl(181 , 22%, 19% )"));
        assertEquals(new Color(38, 60, 60), StringUtils.findColorHSL("hsl(180.75 , 22.3%, 19.3333% )"));
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

    /**
     * Test for method {@link StringUtils#sanitizeForFileName(String)}.
     */
    @Test
    public void sanitizeForFileName() {
        assertEquals("HtmlUnit", StringUtils.sanitizeForFileName("HtmlUnit"));
        assertEquals("Html_Uni_", StringUtils.sanitizeForFileName("Html:Uni\t"));
        assertEquals("Html_Unit", StringUtils.sanitizeForFileName("Html\\Unit"));
    }
}
