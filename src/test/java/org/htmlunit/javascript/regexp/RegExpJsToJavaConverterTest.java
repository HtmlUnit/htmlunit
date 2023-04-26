/*
 * Copyright (c) 2002-2023 Gargoyle Software Inc.
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
package org.htmlunit.javascript.regexp;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Tests for {@link RegExpJsToJavaConverter}.
 *
 * @author Ronald Brill
 * @author Lai Quang Duong
 */
public class RegExpJsToJavaConverterTest {

    /**
     * Test empty input.
     */
    @Test
    public void empty() {
        final RegExpJsToJavaConverter regExpJsToJavaConverter = new RegExpJsToJavaConverter();

        final String in = "";
        final String out = regExpJsToJavaConverter.convert(in);

        assertEquals("", out);
    }

    /**
     * Test simple.
     */
    @Test
    public void stringOnly() {
        final RegExpJsToJavaConverter regExpJsToJavaConverter = new RegExpJsToJavaConverter();

        assertEquals("abc1234", regExpJsToJavaConverter.convert("abc1234"));
    }

    /**
     * Test simple escape sequence.
     */
    @Test
    public void simpleEscape() {
        final RegExpJsToJavaConverter regExpJsToJavaConverter = new RegExpJsToJavaConverter();

        assertEquals("abc\\t234", regExpJsToJavaConverter.convert("abc\\t234"));
    }

    /**
     * Test escape char at the end.
     */
    @Test
    public void escapeAtEnd() {
        final RegExpJsToJavaConverter regExpJsToJavaConverter = new RegExpJsToJavaConverter();

        assertEquals("abc\\", regExpJsToJavaConverter.convert("abc\\"));
    }

    /**
     * Test hex code.
     */
    @Test
    public void escapeHex() {
        final RegExpJsToJavaConverter regExpJsToJavaConverter = new RegExpJsToJavaConverter();

        assertEquals("\\x0A", regExpJsToJavaConverter.convert("\\x0A"));
    }

    /**
     * Test unicode.
     */
    @Test
    public void escapeUnicode() {
        final RegExpJsToJavaConverter regExpJsToJavaConverter = new RegExpJsToJavaConverter();

        assertEquals("\\u0074", regExpJsToJavaConverter.convert("\\u0074"));
        assertEquals("\\u0074 \\{", regExpJsToJavaConverter.convert("\\u0074 {"));
        assertEquals("\\u74 \\{", regExpJsToJavaConverter.convert("\\u74 {"));
    }

    /**
     * Test octal.
     */
    @Test
    public void escapeOctal() {
        final RegExpJsToJavaConverter regExpJsToJavaConverter = new RegExpJsToJavaConverter();

        assertEquals("\\09", regExpJsToJavaConverter.convert("\\9"));
        assertEquals("\\09abc", regExpJsToJavaConverter.convert("\\9abc"));
        assertEquals("\\091", regExpJsToJavaConverter.convert("\\91"));
        assertEquals("\\091abc", regExpJsToJavaConverter.convert("\\91abc"));
        assertEquals("\\0912", regExpJsToJavaConverter.convert("\\912"));
        assertEquals("\\0912abc", regExpJsToJavaConverter.convert("\\912abc"));
        assertEquals("(a) (b) \\02", regExpJsToJavaConverter.convert("(a) (b) \\02"));
    }

    /**
     * JS is special for \0.
     */
    @Test
    public void escapeNullChar() {
        final RegExpJsToJavaConverter regExpJsToJavaConverter = new RegExpJsToJavaConverter();

        assertEquals("[\\x00-\\x08]", regExpJsToJavaConverter.convert("[\\0-\\x08]"));
        assertEquals("[\\x00\\09]", regExpJsToJavaConverter.convert("[\\0\\9]"));
    }

    /**
     * Test not required escape.
     */
    @Test
    public void escapeNotNeeded() {
        final RegExpJsToJavaConverter regExpJsToJavaConverter = new RegExpJsToJavaConverter();

        assertEquals("abcA12y", regExpJsToJavaConverter.convert("abc\\A12\\y"));
    }

    /**
     * Test char class.
     */
    @Test
    public void charClass() {
        final RegExpJsToJavaConverter regExpJsToJavaConverter = new RegExpJsToJavaConverter();

        assertEquals("[afg]", regExpJsToJavaConverter.convert("[afg]"));
        assertEquals("[a-g]", regExpJsToJavaConverter.convert("[a-g]"));
    }

    /**
     * Test char class with [ inside.
     */
    @Test
    public void charClassOpenInside() {
        final RegExpJsToJavaConverter regExpJsToJavaConverter = new RegExpJsToJavaConverter();

        assertEquals("[af\\[g]", regExpJsToJavaConverter.convert("[af[g]"));
        assertEquals("[a\\[b]c]", regExpJsToJavaConverter.convert("[a[b]c]"));
    }

    /**
     * Test start char class at end.
     */
    @Test
    public void charClassStartsAtEnd() {
        final RegExpJsToJavaConverter regExpJsToJavaConverter = new RegExpJsToJavaConverter();

        assertEquals("ab\\[", regExpJsToJavaConverter.convert("ab["));
    }

    /**
     * Test char class special.
     */
    @Test
    public void charClassSpecial() {
        final RegExpJsToJavaConverter regExpJsToJavaConverter = new RegExpJsToJavaConverter();

        assertEquals("[\\cH]", regExpJsToJavaConverter.convert("[\\b]"));
        assertEquals("[ab\\cH]", regExpJsToJavaConverter.convert("[ab\\b]"));
        assertEquals("[\\cHc]", regExpJsToJavaConverter.convert("[\\bc]"));
        assertEquals("[ab\\cHcd]", regExpJsToJavaConverter.convert("[ab\\bcd]"));
    }

    /**
     * Test char class special.
     */
    @Test
    public void charClassSpecial2() {
        final RegExpJsToJavaConverter regExpJsToJavaConverter = new RegExpJsToJavaConverter();

        assertEquals("(?!)[a][b]", regExpJsToJavaConverter.convert("[][a][b]"));
        assertEquals("[a](?s:.)[b]", regExpJsToJavaConverter.convert("[a][^][b]"));
    }

    /**
     * Test negated char class.
     */
    @Test
    public void charClassNegated() {
        final RegExpJsToJavaConverter regExpJsToJavaConverter = new RegExpJsToJavaConverter();

        assertEquals("ab[^c]", regExpJsToJavaConverter.convert("ab[^c]"));
    }

    /**
     * Test negated char class with back reference.
     */
    @Test
    public void charClassNegatedWithBackReference() {
        final RegExpJsToJavaConverter regExpJsToJavaConverter = new RegExpJsToJavaConverter();

        assertEquals("(a)b.", regExpJsToJavaConverter.convert("(a)b[^\\1]"));
    }

    /**
     * Test negated char class with escaped char.
     */
    @Test
    public void charClassNegatedEscape() {
        final RegExpJsToJavaConverter regExpJsToJavaConverter = new RegExpJsToJavaConverter();

        assertEquals("(a)b[^\\n]", regExpJsToJavaConverter.convert("(a)b[^\\n]"));
    }

    /**
     * Test negated char class.
     */
    @Test
    public void charClassNegatedStrange() {
        final RegExpJsToJavaConverter regExpJsToJavaConverter = new RegExpJsToJavaConverter();

        assertEquals("ab\\[^", regExpJsToJavaConverter.convert("ab[^"));
    }

    /**
     * Test single repetition.
     */
    @Test
    public void repetitionSingle() {
        final RegExpJsToJavaConverter regExpJsToJavaConverter = new RegExpJsToJavaConverter();

        assertEquals("ab{1}", regExpJsToJavaConverter.convert("ab{1}"));
    }

    /**
     * Test many repetition.
     */
    @Test
    public void repetitionMany() {
        final RegExpJsToJavaConverter regExpJsToJavaConverter = new RegExpJsToJavaConverter();

        assertEquals("ab{1,7}", regExpJsToJavaConverter.convert("ab{1,7}"));
    }

    /**
     * Test many repetition.
     */
    @Test
    public void repetitionManyOpen() {
        final RegExpJsToJavaConverter regExpJsToJavaConverter = new RegExpJsToJavaConverter();

        assertEquals("ab{1,}", regExpJsToJavaConverter.convert("ab{1,}"));
    }

    /**
     * Test repetition strange cases.
     */
    @Test
    public void repetitionStrange() {
        final RegExpJsToJavaConverter regExpJsToJavaConverter = new RegExpJsToJavaConverter();

        assertEquals("ab\\{", regExpJsToJavaConverter.convert("ab{"));
        assertEquals("ab\\{\\} x", regExpJsToJavaConverter.convert("ab{} x"));
        assertEquals("ab\\{x\\} x", regExpJsToJavaConverter.convert("ab{x} x"));
    }

    /**
     * Test subExpression.
     */
    @Test
    public void subExpression() {
        final RegExpJsToJavaConverter regExpJsToJavaConverter = new RegExpJsToJavaConverter();

        assertEquals("(a)(b) \\1 \\2", regExpJsToJavaConverter.convert("(a)(b) \\1 \\2"));
        assertEquals("(a)(?b) \\1 \\2 \\03", regExpJsToJavaConverter.convert("(a)(?b) \\1 \\2 \\3"));
        assertEquals("(a)(?:b) \\1 \\02", regExpJsToJavaConverter.convert("(a)(?:b) \\1 \\2"));
        assertEquals("(a) \\02", regExpJsToJavaConverter.convert("(a\\1) \\2"));
        assertEquals("(a)(b\\1) \\03", regExpJsToJavaConverter.convert("(a)(b\\1\\2) \\3"));
        assertEquals("(a)(b\\1)(c\\1\\2) \\3 \\07",
                regExpJsToJavaConverter.convert("(a)(b\\1\\2)(c\\1\\2\\3) \\3 \\7"));

        assertEquals(
                "^(?:\\[((?:[@?$])?[\\w\\-]*)\\s*(?:([\\^$*~%!\\/]?=)\\s*"
                + "((?:['\\\"])?)((?:\\\\\\]|.)*?)\\3)?(?!\\\\)\\])",
                regExpJsToJavaConverter.convert(
                        "^(?:\\[((?:[@?$])?[\\w\\-]*)\\s*(?:([\\^$*~%!\\/]?=)\\s*"
                        + "(['\\\"])?((?:\\\\\\]|.)*?)\\3)?(?!\\\\)\\])"));
    }

    /**
     * Test subExpression start end.
     */
    @Test
    public void subExpressionStartAtEnd() {
        final RegExpJsToJavaConverter regExpJsToJavaConverter = new RegExpJsToJavaConverter();

        assertEquals("ab(", regExpJsToJavaConverter.convert("ab("));
        assertEquals("ab(?", regExpJsToJavaConverter.convert("ab(?"));
    }

    /**
     * Test subExpression start end.
     */
    @Test
    public void subExpressionOptional() {
        final RegExpJsToJavaConverter regExpJsToJavaConverter = new RegExpJsToJavaConverter();

        assertEquals("a((?:xy)?) b \\1", regExpJsToJavaConverter.convert("a(xy)? b \\1"));
        assertEquals("a(xy) b \\1", regExpJsToJavaConverter.convert("a(xy) b \\1"));
        assertEquals("a((?:xy)?) ((?:u)?) b \\1 \\2", regExpJsToJavaConverter.convert("a(xy)? (u)? b \\1 \\2"));
    }

    /**
     * Verifies that curly braces can be used non escaped in JS regexp.
     */
    @Test
    public void escapeCurlyBraces() {
        final RegExpJsToJavaConverter regExpJsToJavaConverter = new RegExpJsToJavaConverter();

        assertEquals("\\{", regExpJsToJavaConverter.convert("{"));
        assertEquals("\\{", regExpJsToJavaConverter.convert("\\{"));
        assertEquals("\\}", regExpJsToJavaConverter.convert("}"));
        assertEquals("\\}", regExpJsToJavaConverter.convert("\\}"));
        assertEquals("(^|\\{)#([^\\}]+)(\\}|$)", regExpJsToJavaConverter.convert("(^|{)#([^}]+)(}|$)"));

        assertEquals("a{5}", regExpJsToJavaConverter.convert("a{5}"));
        assertEquals("a{5,}", regExpJsToJavaConverter.convert("a{5,}"));
        assertEquals("a{5,10}", regExpJsToJavaConverter.convert("a{5,10}"));

        // issue 3434548
        assertEquals("\\{\\{abc\\}\\}", regExpJsToJavaConverter.convert("{{abc}}"));
        assertEquals("\\{{2}\\}{2}", regExpJsToJavaConverter.convert("{{2}}{2}"));
    }

    /**
     * Verifies that square braces can be used non escaped in JS regexp.
     */
    @Test
    public void escapeOpeningSquareBracketInCharacterClass() {
        final RegExpJsToJavaConverter regExpJsToJavaConverter = new RegExpJsToJavaConverter();

        assertEquals("[ab\\[]", regExpJsToJavaConverter.convert("[ab[]"));
        assertEquals("[\\[]", regExpJsToJavaConverter.convert("[[]"));
        assertEquals("[a\\[b]", regExpJsToJavaConverter.convert("[a[b]"));
        assertEquals("[ab][a\\[b][ab]", regExpJsToJavaConverter.convert("[ab][a[b][ab]"));
        assertEquals("[!\\^()\\[\\]]", regExpJsToJavaConverter.convert("[!\\^()[\\]]"));

        // with already escaped [
        assertEquals("[ab\\[]", regExpJsToJavaConverter.convert("[ab\\[]"));
        assertEquals("[\\[]", regExpJsToJavaConverter.convert("[\\[]"));
        assertEquals("[a\\[b]", regExpJsToJavaConverter.convert("[a\\[b]"));
        assertEquals("[ab][a\\[b][ab]", regExpJsToJavaConverter.convert("[ab][a\\[b][ab]"));
        assertEquals("[!\\^()\\[\\]]", regExpJsToJavaConverter.convert("[!\\^()\\[\\]]"));
    }

    /**
     * Verifies that square braces can be used non escaped in JS regexp.
     */
    @Test
    public void squareBracket() {
        final RegExpJsToJavaConverter regExpJsToJavaConverter = new RegExpJsToJavaConverter();

        assertEquals("(?!)", regExpJsToJavaConverter.convert("[]"));
        assertEquals("x(?!)y", regExpJsToJavaConverter.convert("x[]y"));

        assertEquals("(?s:.)", regExpJsToJavaConverter.convert("[^]"));
        assertEquals("x(?s:.)y", regExpJsToJavaConverter.convert("x[^]y"));
    }

    /**
     * Verifies that square braces can be used non escaped in JS regexp.
     */
    @Test
    public void unicode() {
        final RegExpJsToJavaConverter regExpJsToJavaConverter = new RegExpJsToJavaConverter();

        assertEquals("[\\x{F0000}-\\x{FFFFD}]*", regExpJsToJavaConverter.convert("[\\u{F0000}-\\u{FFFFD}]*"));
        assertEquals("\\x{F0000}-\\x{FFFFD}", regExpJsToJavaConverter.convert("\\u{F0000}-\\u{FFFFD}"));
        assertEquals("\\x{000000000061}", regExpJsToJavaConverter.convert("\\u{000000000061}"));

        assertEquals("\\u{FFFFD", regExpJsToJavaConverter.convert("\\u{FFFFD"));
        assertEquals("\\x{FFFFD}\\}", regExpJsToJavaConverter.convert("\\u{FFFFD}}"));
    }

    /**
     * Unicode property escapes.
     */
    @Test
    public void unicodePropertyEscapes() {
        final RegExpJsToJavaConverter regExpJsToJavaConverter = new RegExpJsToJavaConverter();

        assertEquals("\\p{L}0-9", regExpJsToJavaConverter.convert("\\p{L}0-9"));
        assertEquals("\\p{Letter}0-9", regExpJsToJavaConverter.convert("\\p{Letter}0-9"));

        assertEquals("\\p{Lu}0-9", regExpJsToJavaConverter.convert("\\p{Lu}0-9"));
        assertEquals("\\p{Ll}0-9", regExpJsToJavaConverter.convert("\\p{Ll}0-9"));

        assertEquals("p\\{html\\}0-9", regExpJsToJavaConverter.convert("\\p{html}0-9"));
    }
}
