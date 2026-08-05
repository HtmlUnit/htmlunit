/*
 * Copyright (c) 2002-2026 Gargoyle Software Inc.
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
package org.htmlunit.html.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

/**
 * @author Ronald Brill
 */
public class HtmlNumberParserTest {

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void validIntegers() {
        valid(
            "0",
            "1",
            "-1",
            "01",
            "999999999999999999999999");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void validFractions() {
        valid(
            ".5",
            "0.5",
            "1.0",
            "123.456",
            "-.5");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void validExponent() {
        valid(
            "1e0",
            "1e1",
            "1e+1",
            "1e-1",
            ".5e2",
            "1.e2",
            "-1.5E-123");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void invalidEmpty() {
        invalid(
            "",
            "+",
            "-",
            ".");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void invalidIntegers() {
        invalid(
            "+1");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void invalidFractions() {
        invalid(
            "+.5",
            "1.",
            "-123.");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void invalidExponent() {
        invalid(
            "e",
            "e1",
            "1e",
            "1e+",
            "1e-",
            ".e1",
            "+1E999");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void invalidSyntax() {
        invalid(
            "NaN",
            "Infinity",
            "-Infinity",
            "0x10",
            "0b10",
            "1f",
            "1d",
            "1_000",
            "1..0",
            "1.2.3",
            "--1",
            "++1",
            "+-1");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void invalidWhitespace() {
        invalid(
            " 1",
            "1 ",
            "\t1",
            "1\n");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void invalidUnicodeDigits() {
        invalid(
            "١",
            "１２３");
    }

    private static void valid(final String... values) {
        for (final String value : values) {
            assertTrue(HtmlNumberParser.isValid(value), value);
            assertNotNull(HtmlNumberParser.parse(value), value);
        }
    }

    private static void invalid(final String... values) {
        for (final String value : values) {
            assertFalse(HtmlNumberParser.isValid(value), value);
            assertNull(HtmlNumberParser.parse(value), value);
        }
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void validPlainIntegers() {
        assertTrue(HtmlNumberParser.isValid("0"));
        assertTrue(HtmlNumberParser.isValid("1234"));
        assertTrue(HtmlNumberParser.isValid("-1234"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void leadingPlusIsInvalid() {
        assertFalse(HtmlNumberParser.isValid("+12.34"));
        assertFalse(HtmlNumberParser.isValid("+1234"));
        assertFalse(HtmlNumberParser.isValid("+"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void exponentSignBothAccepted() {
        assertTrue(HtmlNumberParser.isValid("1e+5"));
        assertTrue(HtmlNumberParser.isValid("1e-5"));
        assertTrue(HtmlNumberParser.isValid("1E5"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void signOnlyIsInvalid() {
        assertFalse(HtmlNumberParser.isValid("-"));
        assertFalse(HtmlNumberParser.isValid("+"));
        assertFalse(HtmlNumberParser.isValid("."));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void trailingWhitespaceIsInvalid() {
        assertFalse(HtmlNumberParser.isValid("7 "));
        assertFalse(HtmlNumberParser.isValid(" 7"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void negativeZeroPreservedByCallerNotByParser() {
        // parse() succeeding is all that matters here -- the CALLER must
        // keep the original raw string ("-0"), not regenerate it from the
        // returned BigDecimal, which normalizes to "0"
        assertTrue(HtmlNumberParser.isValid("-0"));
        final BigDecimal parsed = HtmlNumberParser.parse("-0");
        // documents the trap: this is "0", NOT "-0" -- callers must not
        // use parsed.toString() as the sanitized value
        assertEquals("0", parsed.toString());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void lonePeriodIsInvalid() {
        assertFalse(HtmlNumberParser.isValid("."));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void leadingDecimalPointNoIntegerPart() {
        assertTrue(HtmlNumberParser.isValid(".5"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void trailingDecimalPointNoFractionalPart() {
        // OPEN QUESTION, tied to the existing JS_INPUT_NUMBER_DOT_AT_END_IS_DOUBLE
        // feature flag in HtmlNumberInput -- real browsers are known to
        // disagree on this exact case; confirm which behavior(s) this
        // parser needs to support before locking in an assertion here

        assertFalse(HtmlNumberParser.isValid("1."));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void emptyAndNullAreInvalid() {
        assertFalse(HtmlNumberParser.isValid(""));
        assertFalse(HtmlNumberParser.isValid(null));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void leadingZerosAccepted() {
        // untested against real browsers so far -- worth a WebDriver-level
        // confirmation too, not just this unit-level check
        assertTrue(HtmlNumberParser.isValid("007"));
    }
}
