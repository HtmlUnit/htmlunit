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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.htmlunit.html.impl.Color;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

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
    public void isEmptyOrNull() throws Exception {
        assertTrue(StringUtils.isEmptyOrNull(null));
        assertTrue(StringUtils.isEmptyOrNull(""));
        assertFalse(StringUtils.isEmptyOrNull(" "));
        assertFalse(StringUtils.isEmptyOrNull("\t"));
        assertFalse(StringUtils.isEmptyOrNull("\r"));
        assertFalse(StringUtils.isEmptyOrNull("\n"));
        assertFalse(StringUtils.isEmptyOrNull("string"));
    }


    /**
     * @throws Exception if the test fails
     */
    @Test
    public void defaultIfEmptyOrNull() throws Exception {
        assertEquals("X", StringUtils.defaultIfEmptyOrNull(null, "X"));
        assertEquals("X", StringUtils.defaultIfEmptyOrNull("", "X"));

        assertEquals(" ", StringUtils.defaultIfEmptyOrNull(" ", "X"));
        assertEquals("a", StringUtils.defaultIfEmptyOrNull("a", "X"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void isBlank() throws Exception {
        assertTrue(StringUtils.isBlank(null));
        assertTrue(StringUtils.isBlank(""));
        assertTrue(StringUtils.isBlank(" "));
        assertTrue(StringUtils.isBlank("\t"));
        assertTrue(StringUtils.isBlank("\r"));
        assertTrue(StringUtils.isBlank("\n"));

        assertFalse(StringUtils.isBlank("x"));
        assertFalse(StringUtils.isBlank("string"));
        assertFalse(StringUtils.isBlank("    x"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void isNotBlank() throws Exception {
        assertFalse(StringUtils.isNotBlank(null));
        assertFalse(StringUtils.isNotBlank(""));
        assertFalse(StringUtils.isNotBlank(" "));
        assertFalse(StringUtils.isNotBlank("\t"));
        assertFalse(StringUtils.isNotBlank("\r"));
        assertFalse(StringUtils.isNotBlank("\n"));

        assertTrue(StringUtils.isNotBlank("x"));
        assertTrue(StringUtils.isNotBlank("string"));
        assertTrue(StringUtils.isNotBlank("    x"));
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
        assertTrue(StringUtils.startsWithIgnoreCase("abcd", "a"));
        assertTrue(StringUtils.startsWithIgnoreCase("abcd", "ab"));
        assertTrue(StringUtils.startsWithIgnoreCase("abcd", "abcd"));
        assertTrue(StringUtils.startsWithIgnoreCase("abcd", "A"));
        assertTrue(StringUtils.startsWithIgnoreCase("abcd", "AB"));
        assertTrue(StringUtils.startsWithIgnoreCase("Abcd", "abCd"));

        assertFalse(StringUtils.startsWithIgnoreCase("AB", "x"));
        assertFalse(StringUtils.startsWithIgnoreCase("AB", "xxzOO"));
        assertFalse(StringUtils.startsWithIgnoreCase("", "x"));
        assertFalse(StringUtils.startsWithIgnoreCase("abcd", "bc"));

        assertFalse(StringUtils.startsWithIgnoreCase(null, "x"));

        Assertions.assertThrows(IllegalArgumentException.class, () -> StringUtils.startsWithIgnoreCase("AB", null));
        Assertions.assertThrows(IllegalArgumentException.class, () -> StringUtils.startsWithIgnoreCase("AB", ""));
        Assertions.assertThrows(IllegalArgumentException.class, () -> StringUtils.startsWithIgnoreCase("", ""));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void endsWithIgnoreCase() throws Exception {
        assertTrue(StringUtils.endsWithIgnoreCase("abcd", "d"));
        assertTrue(StringUtils.endsWithIgnoreCase("abcd", "cd"));
        assertTrue(StringUtils.endsWithIgnoreCase("abcd", "abcd"));
        assertTrue(StringUtils.endsWithIgnoreCase("abcd", "D"));
        assertTrue(StringUtils.endsWithIgnoreCase("abcd", "CD"));
        assertTrue(StringUtils.endsWithIgnoreCase("Abcd", "abCd"));

        assertFalse(StringUtils.endsWithIgnoreCase("AB", "x"));
        assertFalse(StringUtils.endsWithIgnoreCase("AB", "xxzOO"));
        assertFalse(StringUtils.endsWithIgnoreCase("", "x"));
        assertFalse(StringUtils.endsWithIgnoreCase("abcd", "bc"));

        assertFalse(StringUtils.endsWithIgnoreCase(null, "x"));

        Assertions.assertThrows(IllegalArgumentException.class, () -> StringUtils.endsWithIgnoreCase("AB", null));
        Assertions.assertThrows(IllegalArgumentException.class, () -> StringUtils.endsWithIgnoreCase("AB", ""));
        Assertions.assertThrows(IllegalArgumentException.class, () -> StringUtils.endsWithIgnoreCase("", ""));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void containsIgnoreCase() throws Exception {
        assertTrue(StringUtils.containsIgnoreCase("abcd", "a"));
        assertTrue(StringUtils.containsIgnoreCase("abcd", "b"));
        assertTrue(StringUtils.containsIgnoreCase("abcd", "c"));
        assertTrue(StringUtils.containsIgnoreCase("abcd", "d"));
        assertTrue(StringUtils.containsIgnoreCase("abcd", "A"));
        assertTrue(StringUtils.containsIgnoreCase("abcd", "B"));
        assertTrue(StringUtils.containsIgnoreCase("abcd", "C"));
        assertTrue(StringUtils.containsIgnoreCase("abcd", "D"));
        assertTrue(StringUtils.containsIgnoreCase("abcd", "cd"));
        assertTrue(StringUtils.containsIgnoreCase("abcd", "Cd"));
        assertTrue(StringUtils.containsIgnoreCase("abcd", "abcd"));
        assertTrue(StringUtils.containsIgnoreCase("abcd", "D"));
        assertTrue(StringUtils.containsIgnoreCase("abcd", "CD"));
        assertTrue(StringUtils.containsIgnoreCase("Abcd", "abCd"));

        assertFalse(StringUtils.containsIgnoreCase("AB", "x"));
        assertFalse(StringUtils.containsIgnoreCase("AB", "xxzOO"));
        assertFalse(StringUtils.containsIgnoreCase("", "x"));
        assertFalse(StringUtils.containsIgnoreCase("abcd", "bd"));

        assertFalse(StringUtils.containsIgnoreCase(null, "x"));

        Assertions.assertThrows(IllegalArgumentException.class, () -> StringUtils.containsIgnoreCase("AB", null));
        Assertions.assertThrows(IllegalArgumentException.class, () -> StringUtils.containsIgnoreCase("AB", ""));
        Assertions.assertThrows(IllegalArgumentException.class, () -> StringUtils.containsIgnoreCase("", ""));
    }


    /**
     * @throws Exception if the test fails
     */
    @Test
    public void containsOnly() {
        assertFalse(StringUtils.containsOnly(null, "x".toCharArray()));
        assertFalse(StringUtils.containsOnly("", "x".toCharArray()));
        assertFalse(StringUtils.containsOnly("a", "x".toCharArray()));
        assertFalse(StringUtils.containsOnly("ax", "x".toCharArray()));
        assertFalse(StringUtils.containsOnly("xa", "x".toCharArray()));

        assertTrue(StringUtils.containsOnly("x", "ax".toCharArray()));
        assertTrue(StringUtils.containsOnly("aa", "ax".toCharArray()));
        assertTrue(StringUtils.containsOnly("ax", "ax".toCharArray()));
        assertTrue(StringUtils.containsOnly("axaaa", "ax".toCharArray()));
        assertTrue(StringUtils.containsOnly("xaaxxxaa", "xa".toCharArray()));

        Assertions.assertThrows(IllegalArgumentException.class, () -> StringUtils.containsOnly("AB", null));
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> StringUtils.containsOnly("AB", ArrayUtils.EMPTY_CHAR_ARRAY));
    }


    /**
     * @throws Exception if the test fails
     */
    @Test
    public void toRootLowerCase() throws Exception {
        assertEquals("abcd", StringUtils.toRootLowerCase("abcd"));
        assertEquals("abcd", StringUtils.toRootLowerCase("ABcD"));

        assertEquals("", StringUtils.toRootLowerCase(""));
        assertNull(StringUtils.toRootLowerCase(null));
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
     * Test for method {@link StringUtils#replaceChars(String, String, String)}.
     */
    @Test
    public void replaceChars() {
        assertEquals(null, StringUtils.replaceChars(null, "", ""));
        assertEquals("", StringUtils.replaceChars("", "", null));
        assertEquals("abc", StringUtils.replaceChars("abc", null, null));
        assertEquals("abc", StringUtils.replaceChars("abc", "", ""));
        assertEquals("ac", StringUtils.replaceChars("abc", "b", null));
        assertEquals("ac", StringUtils.replaceChars("abc", "b", ""));
        assertEquals("ayzya", StringUtils.replaceChars("abcba", "bc", "yz"));
        assertEquals("ayya", StringUtils.replaceChars("abcba", "bc", "y"));
        assertEquals("ayzya", StringUtils.replaceChars("abcba", "bc", "yzx"));

        assertEquals("abc", StringUtils.replaceChars("abc", "d", "e"));

        assertEquals("ebc", StringUtils.replaceChars("abc", "a", "e"));
        assertEquals("bc", StringUtils.replaceChars("abc", "a", ""));

        assertEquals("abe", StringUtils.replaceChars("abc", "c", "e"));
        assertEquals("ab", StringUtils.replaceChars("abc", "c", null));
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

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void substringBefore() throws Exception {
        assertThrows(IllegalArgumentException.class, () -> StringUtils.substringBefore(null, null));
        assertThrows(IllegalArgumentException.class, () -> StringUtils.substringBefore(null, ""));

        assertEquals("", StringUtils.substringBefore("", "a"));
        assertEquals("xyz", StringUtils.substringBefore("xyz", "a"));
        assertEquals("", StringUtils.substringBefore("a", "a"));
        assertEquals("x", StringUtils.substringBefore("xaba", "a"));
        assertEquals(" ", StringUtils.substringBefore(" a", "a"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void toInt() throws Exception {
        assertEquals(17, StringUtils.toInt(null, 17));
        assertEquals(17, StringUtils.toInt("", 17));
        assertEquals(17, StringUtils.toInt(" ", 17));
        assertEquals(4, StringUtils.toInt("\t", 4));
        assertEquals(4, StringUtils.toInt("two", 4));

        assertEquals(21, StringUtils.toInt("21", 4));
        assertEquals(-21, StringUtils.toInt("-21", 4));
        assertEquals(0, StringUtils.toInt(" 21 ", 0));
        assertEquals(0, StringUtils.toInt(" -  21  \t", 0));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void toFloat() throws Exception {
        assertEquals(17.2f, StringUtils.toFloat(null, 17.2f));
        assertEquals(17.2f, StringUtils.toFloat("", 17.2f));
        assertEquals(17.2f, StringUtils.toFloat(" ", 17.2f));
        assertEquals(4f, StringUtils.toFloat("\t", 4f));
        assertEquals(4f, StringUtils.toFloat("two", 4));

        assertEquals(21f, StringUtils.toFloat("21", 4.1f));
        assertEquals(-21f, StringUtils.toFloat("-21", 4.1f));
        assertEquals(21f, StringUtils.toFloat(" 21 ", 0));
        assertEquals(0, StringUtils.toFloat(" -  21  \t", 0f));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void substringAfter() throws Exception {
        assertNull(StringUtils.substringAfter(null, null));
        assertNull(StringUtils.substringAfter(null, ""));
        assertNull(StringUtils.substringAfter(null, "abc"));

        assertEquals(StringUtils.EMPTY_STRING, StringUtils.substringAfter("", null));
        assertEquals(StringUtils.EMPTY_STRING, StringUtils.substringAfter("", ""));
        assertEquals(StringUtils.EMPTY_STRING, StringUtils.substringAfter("", "abc"));

        assertEquals(StringUtils.EMPTY_STRING, StringUtils.substringAfter(StringUtils.EMPTY_STRING, null));
        assertEquals(StringUtils.EMPTY_STRING, StringUtils.substringAfter(StringUtils.EMPTY_STRING, ""));
        assertEquals(StringUtils.EMPTY_STRING, StringUtils.substringAfter(StringUtils.EMPTY_STRING, "abc"));

        assertEquals(StringUtils.EMPTY_STRING, StringUtils.substringAfter("abc", null));
        assertEquals("abc", StringUtils.substringAfter("abc", ""));

        assertEquals("bc", StringUtils.substringAfter("abc", "a"));
        assertEquals("cba", StringUtils.substringAfter("abcba", "b"));
        assertEquals("", StringUtils.substringAfter("abc", "c"));
        assertEquals("", StringUtils.substringAfter("abc", "d"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void trimRight() throws Exception {
        assertNull(StringUtils.trimRight(null));
        assertEquals(StringUtils.EMPTY_STRING, StringUtils.trimRight(""));
        assertEquals("", StringUtils.trimRight(StringUtils.EMPTY_STRING));

        assertEquals("abc", StringUtils.trimRight("abc"));
        assertEquals("  abc", StringUtils.trimRight("  abc"));
        assertEquals("abc", StringUtils.trimRight("abc  "));
        assertEquals(" a b c", StringUtils.trimRight(" a b c "));
    }
}
