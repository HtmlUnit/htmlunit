/*
 * Copyright (c) 2002-2012 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript.regexp;

import junit.framework.Assert;

import org.junit.Test;

/**
 * Tests for {@link RegExpJsToJavaConverter}.
 *
 * @version $Revision$
 * @author Ronald Brill
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

        Assert.assertEquals("", out);
    }

    /**
     * Test simple.
     */
    @Test
    public void stringOnly() {
        final RegExpJsToJavaConverter regExpJsToJavaConverter = new RegExpJsToJavaConverter();

        final String in = "abc1234";
        final String out = regExpJsToJavaConverter.convert(in);

        Assert.assertEquals("abc1234", out);
    }

    /**
     * Test simple escape sequence.
     */
    @Test
    public void simpleEscape() {
        final RegExpJsToJavaConverter regExpJsToJavaConverter = new RegExpJsToJavaConverter();

        final String in = "abc\\t234";
        final String out = regExpJsToJavaConverter.convert(in);

        Assert.assertEquals("abc\\t234", out);
    }

    /**
     * Test escape char at the end.
     */
    @Test
    public void escapeAtEnd() {
        final RegExpJsToJavaConverter regExpJsToJavaConverter = new RegExpJsToJavaConverter();

        final String in = "abc\\";
        final String out = regExpJsToJavaConverter.convert(in);

        Assert.assertEquals("abc\\", out);
    }

    /**
     * Test hex code.
     */
    @Test
    public void escapeHex() {
        final RegExpJsToJavaConverter regExpJsToJavaConverter = new RegExpJsToJavaConverter();

        final String in = "\\x0A";
        final String out = regExpJsToJavaConverter.convert(in);

        Assert.assertEquals("\\x0A", out);
    }

    /**
     * Test unicode.
     */
    @Test
    public void escapeUnicode() {
        final RegExpJsToJavaConverter regExpJsToJavaConverter = new RegExpJsToJavaConverter();

        final String in = "\\u0074";
        final String out = regExpJsToJavaConverter.convert(in);

        Assert.assertEquals("\\u0074", out);
    }

    /**
     * Test octal.
     */
    @Test
    public void escapeOctal() {
        final RegExpJsToJavaConverter regExpJsToJavaConverter = new RegExpJsToJavaConverter();

        String in = "\\9";
        String out = regExpJsToJavaConverter.convert(in);
        Assert.assertEquals("\\09", out);

        in = "\\9abc";
        out = regExpJsToJavaConverter.convert(in);
        Assert.assertEquals("\\09abc", out);

        in = "\\91";
        out = regExpJsToJavaConverter.convert(in);
        Assert.assertEquals("\\091", out);

        in = "\\91abc";
        out = regExpJsToJavaConverter.convert(in);
        Assert.assertEquals("\\091abc", out);

        in = "\\912";
        out = regExpJsToJavaConverter.convert(in);
        Assert.assertEquals("\\0912", out);

        in = "\\912abc";
        out = regExpJsToJavaConverter.convert(in);
        Assert.assertEquals("\\0912abc", out);
    }

    /**
     * Test not required escape.
     */
    @Test
    public void escapeNotNeeded() {
        final RegExpJsToJavaConverter regExpJsToJavaConverter = new RegExpJsToJavaConverter();

        final String in = "abc\\A12\\y";
        final String out = regExpJsToJavaConverter.convert(in);

        Assert.assertEquals("abcA12y", out);
    }

    /**
     * Test char class.
     */
    @Test
    public void charClass() {
        final RegExpJsToJavaConverter regExpJsToJavaConverter = new RegExpJsToJavaConverter();

        String in = "[afg]";
        String out = regExpJsToJavaConverter.convert(in);
        Assert.assertEquals("[afg]", out);

        in = "[a-g]";
        out = regExpJsToJavaConverter.convert(in);
        Assert.assertEquals("[a-g]", out);
    }

    /**
     * Test char class.
     */
    @Test
    public void charClassOpenInside() {
        final RegExpJsToJavaConverter regExpJsToJavaConverter = new RegExpJsToJavaConverter();

        final String in = "[af[g]";
        final String out = regExpJsToJavaConverter.convert(in);
        Assert.assertEquals("[af\\[g]", out);
    }

    /**
     * Test start char class at end.
     */
    @Test
    public void charClassStartsAtEnd() {
        final RegExpJsToJavaConverter regExpJsToJavaConverter = new RegExpJsToJavaConverter();

        final String in = "ab[";
        final String out = regExpJsToJavaConverter.convert(in);

        Assert.assertEquals("ab\\[", out);
    }

    /**
     * Test char class special.
     */
    @Test
    public void charClassSpecial() {
        final RegExpJsToJavaConverter regExpJsToJavaConverter = new RegExpJsToJavaConverter();

        String in = "[\\b]";
        String out = regExpJsToJavaConverter.convert(in);
        Assert.assertEquals("[\\cH]", out);

        in = "[ab\\b]";
        out = regExpJsToJavaConverter.convert(in);
        Assert.assertEquals("[ab\\cH]", out);

        in = "[\\bc]";
        out = regExpJsToJavaConverter.convert(in);
        Assert.assertEquals("[\\cHc]", out);

        in = "[ab\\bcd]";
        out = regExpJsToJavaConverter.convert(in);
        Assert.assertEquals("[ab\\cHcd]", out);
    }

    /**
     * Test negated char class.
     */
    @Test
    public void charClassNegated() {
        final RegExpJsToJavaConverter regExpJsToJavaConverter = new RegExpJsToJavaConverter();

        final String in = "ab[^c]";
        final String out = regExpJsToJavaConverter.convert(in);

        Assert.assertEquals("ab[^c]", out);
    }

    /**
     * Test negated char class with back reference.
     */
    @Test
    public void charClassNegatedWithBackReference() {
        final RegExpJsToJavaConverter regExpJsToJavaConverter = new RegExpJsToJavaConverter();

        final String in = "(a)b[^\\1]";
        final String out = regExpJsToJavaConverter.convert(in);

        Assert.assertEquals("(a)b.", out);
    }

    /**
     * Test negated char class with escaped char.
     */
    @Test
    public void charClassNegatedEscape() {
        final RegExpJsToJavaConverter regExpJsToJavaConverter = new RegExpJsToJavaConverter();

        final String in = "(a)b[^\\n]";
        final String out = regExpJsToJavaConverter.convert(in);

        Assert.assertEquals("(a)b[^\\n]", out);
    }

    /**
     * Test negated char class.
     */
    @Test
    public void charClassNegatedStrange() {
        final RegExpJsToJavaConverter regExpJsToJavaConverter = new RegExpJsToJavaConverter();

        final String in = "ab[^";
        final String out = regExpJsToJavaConverter.convert(in);

        Assert.assertEquals("ab\\[^", out);
    }

    /**
     * Test single repetion.
     */
    @Test
    public void repetitionSingle() {
        final RegExpJsToJavaConverter regExpJsToJavaConverter = new RegExpJsToJavaConverter();

        final String in = "ab{1}";
        final String out = regExpJsToJavaConverter.convert(in);

        Assert.assertEquals("ab{1}", out);
    }

    /**
     * Test many repetion.
     */
    @Test
    public void repetitionMany() {
        final RegExpJsToJavaConverter regExpJsToJavaConverter = new RegExpJsToJavaConverter();

        final String in = "ab{1,7}";
        final String out = regExpJsToJavaConverter.convert(in);

        Assert.assertEquals("ab{1,7}", out);
    }

    /**
     * Test many repetion.
     */
    @Test
    public void repetitionManyOpen() {
        final RegExpJsToJavaConverter regExpJsToJavaConverter = new RegExpJsToJavaConverter();

        final String in = "ab{1,}";
        final String out = regExpJsToJavaConverter.convert(in);

        Assert.assertEquals("ab{1,}", out);
    }

    /**
     * Test repetion strange cases.
     */
    @Test
    public void repetitionStrange() {
        final RegExpJsToJavaConverter regExpJsToJavaConverter = new RegExpJsToJavaConverter();

        String in = "ab{";
        String out = regExpJsToJavaConverter.convert(in);
        Assert.assertEquals("ab\\{", out);

        in = "ab{} x";
        out = regExpJsToJavaConverter.convert(in);
        Assert.assertEquals("ab\\{\\} x", out);

        in = "ab{x} x";
        out = regExpJsToJavaConverter.convert(in);
        Assert.assertEquals("ab\\{x\\} x", out);
    }

    /**
     * Test subExpression.
     */
    @Test
    public void subExpression() {
        final RegExpJsToJavaConverter regExpJsToJavaConverter = new RegExpJsToJavaConverter();

        String in = "(a)(b) \\1 \\2";
        String out = regExpJsToJavaConverter.convert(in);
        Assert.assertEquals("(a)(b) \\1 \\2", out);

        in = "(a)(?b) \\1 \\2 \\3";
        out = regExpJsToJavaConverter.convert(in);
        Assert.assertEquals("(a)(?b) \\1 \\2 \\03", out);

        in = "(a)(?:b) \\1 \\2";
        out = regExpJsToJavaConverter.convert(in);
        Assert.assertEquals("(a)(?:b) \\1 \\02", out);
    }

    /**
     * Test subExpression start end.
     */
    @Test
    public void subExpressionStartAtEnd() {
        final RegExpJsToJavaConverter regExpJsToJavaConverter = new RegExpJsToJavaConverter();

        String in = "ab(";
        String out = regExpJsToJavaConverter.convert(in);
        Assert.assertEquals("ab(", out);

        in = "ab(?";
        out = regExpJsToJavaConverter.convert(in);
        Assert.assertEquals("ab(?", out);
    }

    /**
     * Verifies that curly braces can be used non escaped in JS regexp.
     */
    @Test
    public void testEscapeCurlyBraces() {
        final RegExpJsToJavaConverter regExpJsToJavaConverter = new RegExpJsToJavaConverter();

        Assert.assertEquals("\\{", regExpJsToJavaConverter.convert("{"));
        Assert.assertEquals("\\{", regExpJsToJavaConverter.convert("\\{"));
        Assert.assertEquals("\\}", regExpJsToJavaConverter.convert("}"));
        Assert.assertEquals("\\}", regExpJsToJavaConverter.convert("\\}"));
        Assert.assertEquals("(^|\\{)#([^\\}]+)(\\}|$)", regExpJsToJavaConverter.convert("(^|{)#([^}]+)(}|$)"));

        Assert.assertEquals("a{5}", regExpJsToJavaConverter.convert("a{5}"));
        Assert.assertEquals("a{5,}", regExpJsToJavaConverter.convert("a{5,}"));
        Assert.assertEquals("a{5,10}", regExpJsToJavaConverter.convert("a{5,10}"));

        // issue 3434548
        Assert.assertEquals("\\{\\{abc\\}\\}", regExpJsToJavaConverter.convert("{{abc}}"));
        Assert.assertEquals("\\{{2}\\}{2}", regExpJsToJavaConverter.convert("{{2}}{2}"));
    }

    /**
     * Verifies that square braces can be used non escaped in JS regexp.
     */
    @Test
    public void escapeOpeningSquareBracketInCharacterClass() {
        final RegExpJsToJavaConverter regExpJsToJavaConverter = new RegExpJsToJavaConverter();

        Assert.assertEquals("[ab\\[]", regExpJsToJavaConverter.convert("[ab[]"));
        Assert.assertEquals("[\\[]", regExpJsToJavaConverter.convert("[[]"));
        Assert.assertEquals("[a\\[b]", regExpJsToJavaConverter.convert("[a[b]"));
        Assert.assertEquals("[ab][a\\[b][ab]", regExpJsToJavaConverter.convert("[ab][a[b][ab]"));
        Assert.assertEquals("[!\\^()\\[\\]]", regExpJsToJavaConverter.convert("[!\\^()[\\]]"));

        // with already escaped [
        Assert.assertEquals("[ab\\[]", regExpJsToJavaConverter.convert("[ab\\[]"));
        Assert.assertEquals("[\\[]", regExpJsToJavaConverter.convert("[\\[]"));
        Assert.assertEquals("[a\\[b]", regExpJsToJavaConverter.convert("[a\\[b]"));
        Assert.assertEquals("[ab][a\\[b][ab]", regExpJsToJavaConverter.convert("[ab][a\\[b][ab]"));
        Assert.assertEquals("[!\\^()\\[\\]]", regExpJsToJavaConverter.convert("[!\\^()\\[\\]]"));
    }

}
