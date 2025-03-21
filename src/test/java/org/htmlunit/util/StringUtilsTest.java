/*
 * Copyright (c) 2002-2025 Gargoyle Software Inc.
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
package org.htmlunit.util;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.htmlunit.html.impl.Color;
import org.junit.Test;

/**
 * Tests for {@link StringUtils}.
 *
 * @author Ronald Brill
 */
public class StringUtilsTest {

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void isEmptyString() throws Exception {
        assertFalse(StringUtils.isEmptyString(null));
        assertTrue(StringUtils.isEmptyString(""));
        assertFalse(StringUtils.isEmptyString(" "));
        assertFalse(StringUtils.isEmptyString("\t"));
        assertFalse(StringUtils.isEmptyString("\r"));
        assertFalse(StringUtils.isEmptyString("\n"));
        assertFalse(StringUtils.isEmptyString("string"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void equalsChar() throws Exception {
        assertFalse(StringUtils.equalsChar('#', null));
        assertFalse(StringUtils.equalsChar('#', ""));
        assertFalse(StringUtils.equalsChar('#', " "));
        assertTrue(StringUtils.equalsChar('#', "#"));
        assertFalse(StringUtils.equalsChar('#', "##"));
        assertFalse(StringUtils.equalsChar('#', " #"));
        assertFalse(StringUtils.equalsChar('#', "# "));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void startsWithIgnoreCase() throws Exception {
        assertTrue(StringUtils.startsWithIgnoreCase("abcd", "ab"));
        assertTrue(StringUtils.startsWithIgnoreCase("abcd", "abcd"));
        assertTrue(StringUtils.startsWithIgnoreCase("abcd", "AB"));
        assertTrue(StringUtils.startsWithIgnoreCase("Abcd", "abCd"));

        assertFalse(StringUtils.startsWithIgnoreCase("AB", "x"));
        assertFalse(StringUtils.startsWithIgnoreCase("AB", "xxzOO"));
        assertFalse(StringUtils.startsWithIgnoreCase("", "x"));
        assertFalse(StringUtils.startsWithIgnoreCase("abcd", "bc"));

        assertFalse(StringUtils.startsWithIgnoreCase(null, "x"));

        assertThrows(IllegalArgumentException.class, () -> StringUtils.startsWithIgnoreCase("AB", null));
        assertThrows(IllegalArgumentException.class, () -> StringUtils.startsWithIgnoreCase("AB", ""));
        assertThrows(IllegalArgumentException.class, () -> StringUtils.startsWithIgnoreCase("", ""));
    }

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

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void stringToByteArray() throws Exception {
        byte[] result = StringUtils.toByteArray(null, UTF_8);
        assertEquals(0, result.length);

        result = StringUtils.toByteArray("", UTF_8);
        assertEquals(0, result.length);

        result = StringUtils.toByteArray("htmlunit", UTF_8);
        assertEquals(8, result.length);
        assertEquals(104, result[0]);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void escapeXmlAttributeValue() throws Exception {
        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 1024; i++) {
            sb.append((char) i);
        }

        final StringBuilder expected = new StringBuilder("\t\n\r");
        for (int i = 32; i < 1024; i++) {
            if (i == '&') {
                expected.append("&amp;");
            }
            else if (i == '<') {
                expected.append("&lt;");
            }
            else if (i == '"') {
                expected.append("&quot;");
            }
            else {
                expected.append((char) i);
            }
        }

        assertEquals(expected.toString(), StringUtils.escapeXmlAttributeValue(sb.toString()));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void escapeXmlAttributeValueBorderCases() throws Exception {
        assertEquals(null, StringUtils.escapeXmlAttributeValue(null));
        assertEquals("", StringUtils.escapeXmlAttributeValue(""));

        // [#x20-#xD7FF]
        assertEquals("", StringUtils.escapeXmlAttributeValue("\u001f"));
        assertEquals("\u0020", StringUtils.escapeXmlAttributeValue("\u0020"));
        assertEquals("\u0021", StringUtils.escapeXmlAttributeValue("\u0021"));

        assertEquals("\uD7FE", StringUtils.escapeXmlAttributeValue("\uD7FE"));
        assertEquals("\uD7FF", StringUtils.escapeXmlAttributeValue("\uD7FF"));
        assertEquals("", StringUtils.escapeXmlAttributeValue("\uD800"));

        // [#xE000-#xFFFD]
        assertEquals("", StringUtils.escapeXmlAttributeValue("\uDFFF"));
        assertEquals("\uE000", StringUtils.escapeXmlAttributeValue("\uE000"));
        assertEquals("\uE001", StringUtils.escapeXmlAttributeValue("\uE001"));

        assertEquals("\uFFFC", StringUtils.escapeXmlAttributeValue("\uFFFC"));
        assertEquals("\uFFFD", StringUtils.escapeXmlAttributeValue("\uFFFD"));
        assertEquals("", StringUtils.escapeXmlAttributeValue("\uFFFE"));

        // [#x10000-#x10FFFF]
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void escapeXml() throws Exception {
        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 1024; i++) {
            sb.append((char) i);
        }

        final StringBuilder expected = new StringBuilder("\t\n\r");
        for (int i = 32; i < 1024; i++) {
            if (i == '&') {
                expected.append("&amp;");
            }
            else if (i == '<') {
                expected.append("&lt;");
            }
            else if (i == '>') {
                expected.append("&gt;");
            }
            else if (i == '\'') {
                expected.append("&apos;");
            }
            else if (i == '"') {
                expected.append("&quot;");
            }
            else {
                expected.append((char) i);
            }
        }

        assertEquals(expected.toString(), StringUtils.escapeXml(sb.toString()));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void escapeXmlBorderCases() throws Exception {
        assertEquals(null, StringUtils.escapeXml(null));
        assertEquals("", StringUtils.escapeXml(""));

        // [#x20-#xD7FF]
        assertEquals("", StringUtils.escapeXml("\u001f"));
        assertEquals("\u0020", StringUtils.escapeXml("\u0020"));
        assertEquals("\u0021", StringUtils.escapeXml("\u0021"));

        assertEquals("\uD7FE", StringUtils.escapeXml("\uD7FE"));
        assertEquals("\uD7FF", StringUtils.escapeXml("\uD7FF"));
        assertEquals("", StringUtils.escapeXml("\uD800"));

        // [#xE000-#xFFFD]
        assertEquals("", StringUtils.escapeXml("\uDFFF"));
        assertEquals("\uE000", StringUtils.escapeXml("\uE000"));
        assertEquals("\uE001", StringUtils.escapeXml("\uE001"));

        assertEquals("\uFFFC", StringUtils.escapeXml("\uFFFC"));
        assertEquals("\uFFFD", StringUtils.escapeXml("\uFFFD"));
        assertEquals("", StringUtils.escapeXml("\uFFFE"));

        // [#x10000-#x10FFFF]
    }
}
