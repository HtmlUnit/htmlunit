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
package org.htmlunit.javascript.regexp.generated;

import org.htmlunit.junit.annotation.Alerts;
import org.junit.jupiter.api.Test;

/**
 * Additional tests for RegExp character class escapes and
 * character set edge cases.
 *
 * @author Ronald Brill
 */
public class RegExp3Test extends AbstractRegExpTest {

    // =================================================================
    // Character class escape edge cases
    // =================================================================

    @Test
    @Alerts("true")
    public void characterClassNullChar() throws Exception {
        final String script = "log(/\\0/.test('\\0'));";
        testEvaluate(script);
    }

    @Test
    @Alerts("true")
    public void controlEscapeA() throws Exception {
        final String script = "log(/\\cA/.test('\\x01'));";
        testEvaluate(script);
    }

    @Test
    @Alerts("true")
    public void controlEscapeZ() throws Exception {
        final String script = "log(/\\cZ/.test('\\x1A'));";
        testEvaluate(script);
    }

    @Test
    @Alerts("true")
    public void controlEscapeM() throws Exception {
        final String script = "log(/\\cM/.test('\\r'));";
        testEvaluate(script);
    }

    @Test
    @Alerts("true")
    public void controlEscapeJ() throws Exception {
        final String script = "log(/\\cJ/.test('\\n'));";
        testEvaluate(script);
    }

    @Test
    @Alerts("true")
    public void controlEscapeI() throws Exception {
        final String script = "log(/\\cI/.test('\\t'));";
        testEvaluate(script);
    }

    @Test
    @Alerts("true")
    public void hexEscapeUpperCase() throws Exception {
        final String script = "log(/\\x41/.test('A'));";
        testEvaluate(script);
    }

    @Test
    @Alerts("true")
    public void hexEscapeLowerCase() throws Exception {
        final String script = "log(/\\x61/.test('a'));";
        testEvaluate(script);
    }

    @Test
    @Alerts("true")
    public void hexEscapeNull() throws Exception {
        final String script = "log(/\\x00/.test('\\0'));";
        testEvaluate(script);
    }

    @Test
    @Alerts("true")
    public void unicodeEscape4Digit() throws Exception {
        final String script = "log(/\\u0041/.test('A'));";
        testEvaluate(script);
    }

    @Test
    @Alerts("true")
    public void unicodeBraceEscape() throws Exception {
        final String script = "log(/\\u{41}/u.test('A'));";
        testEvaluate(script);
    }

    @Test
    @Alerts("true")
    public void unicodeBraceEscapeAstral() throws Exception {
        final String script = "log(/\\u{1F600}/u.test('\\uD83D\\uDE00'));";
        testEvaluate(script);
    }

    @Test
    @Alerts({"true", "false"})
    public void tabEscape() throws Exception {
        final String script = "log(/\\t/.test('\\t'));\n"
                            + "log(/\\t/.test(' '));";
        testEvaluate(script);
    }

    @Test
    @Alerts({"true", "true", "true"})
    public void newlineReturnVerticalTab() throws Exception {
        final String script = "log(/\\n/.test('\\n'));\n"
                            + "log(/\\r/.test('\\r'));\n"
                            + "log(/\\v/.test('\\v'));";
        testEvaluate(script);
    }

    @Test
    @Alerts("true")
    public void formFeed() throws Exception {
        final String script = "log(/\\f/.test('\\f'));";
        testEvaluate(script);
    }

    @Test
    @Alerts({"true", "true", "true"})
    public void whitespaceClassSpecialChars() throws Exception {
        final String script = "log(/\\s/.test('\\u00A0'));\n"
                            + "log(/\\s/.test('\\uFEFF'));\n"
                            + "log(/\\s/.test('\\u2003'));";
        testEvaluate(script);
    }

    @Test
    @Alerts({"true", "true"})
    public void digitBoundary() throws Exception {
        final String script = "log(/\\d/.test('0'));\n"
                            + "log(/\\d/.test('9'));";
        testEvaluate(script);
    }

    @Test
    @Alerts("false")
    public void digitNonAsciiNumber() throws Exception {
        // Arabic-Indic digit 4 is not matched by \d (non-unicode mode)
        final String script = "log(/\\d/.test('\\u0664'));";
        testEvaluate(script);
    }

    @Test
    @Alerts({"true", "true", "true"})
    public void wordClassBoundary() throws Exception {
        final String script = "log(/\\w/.test('a'));\n"
                            + "log(/\\w/.test('Z'));\n"
                            + "log(/\\w/.test('0'));";
        testEvaluate(script);
    }

    // =================================================================
    // Character set edge cases
    // =================================================================

    @Test
    @Alerts({"true", "true"})
    public void setClosingBracketEscaped() throws Exception {
        final String script = "log(/[\\]]/.test(']'));\n"
                            + "log(/[a\\]b]/.test(']'));";
        testEvaluate(script);
    }

    @Test
    @Alerts("true")
    public void setBackslashEscaped() throws Exception {
        final String script = "log(/[\\\\]/.test('\\\\'));";
        testEvaluate(script);
    }

    @Test
    @Alerts({"true", "false"})
    public void setDotLiteral() throws Exception {
        final String script = "log(/[.]/.test('.'));\n"
                            + "log(/[.]/.test('x'));";
        testEvaluate(script);
    }

    @Test
    @Alerts({"true", "false"})
    public void setStarLiteral() throws Exception {
        final String script = "log(/[*]/.test('*'));\n"
                            + "log(/[*]/.test('x'));";
        testEvaluate(script);
    }

    @Test
    @Alerts({"true", "false"})
    public void setPlusLiteral() throws Exception {
        final String script = "log(/[+]/.test('+'));\n"
                            + "log(/[+]/.test('x'));";
        testEvaluate(script);
    }

    @Test
    @Alerts({"true", "false"})
    public void setQuestionLiteral() throws Exception {
        final String script = "log(/[?]/.test('?'));\n"
                            + "log(/[?]/.test('x'));";
        testEvaluate(script);
    }

    @Test
    @Alerts({"true", "false"})
    public void setOpenParenLiteral() throws Exception {
        final String script = "log(/[(]/.test('('));\n"
                            + "log(/[(]/.test('x'));";
        testEvaluate(script);
    }

    @Test
    @Alerts({"true", "false"})
    public void setPipeLiteral() throws Exception {
        final String script = "log(/[|]/.test('|'));\n"
                            + "log(/[|]/.test('x'));";
        testEvaluate(script);
    }

    @Test
    @Alerts({"true", "false"})
    public void setDollarLiteral() throws Exception {
        final String script = "log(/[$]/.test('$'));\n"
                            + "log(/[$]/.test('x'));";
        testEvaluate(script);
    }

    @Test
    @Alerts({"true", "false"})
    public void setCaretMiddleLiteral() throws Exception {
        // ^ at non-first position is literal
        final String script = "log(/[a^]/.test('^'));\n"
                            + "log(/[a^]/.test('b'));";
        testEvaluate(script);
    }

    @Test
    @Alerts({"true", "true", "true"})
    public void setMultipleRanges() throws Exception {
        final String script = "log(/[a-z0-9_]/.test('m'));\n"
                            + "log(/[a-z0-9_]/.test('5'));\n"
                            + "log(/[a-z0-9_]/.test('_'));";
        testEvaluate(script);
    }

    @Test
    @Alerts({"true", "false"})
    public void setNegatedRange() throws Exception {
        final String script = "log(/[^0-9]/.test('a'));\n"
                            + "log(/[^0-9]/.test('5'));";
        testEvaluate(script);
    }

    @Test
    @Alerts({"true", "true"})
    public void setUnicodeRange() throws Exception {
        final String script = "log(/[\\u0041-\\u005A]/.test('M'));\n"
                            + "log(/[\\u0041-\\u005A]/.test('Z'));";
        testEvaluate(script);
    }

    @Test
    @Alerts({"true", "true"})
    public void setHexRange() throws Exception {
        final String script = "log(/[\\x41-\\x5A]/.test('A'));\n"
                            + "log(/[\\x41-\\x5A]/.test('Z'));";
        testEvaluate(script);
    }

    @Test
    @Alerts({"true", "true", "false"})
    public void setShorthandInsideSet() throws Exception {
        final String script = "log(/[\\d]/.test('5'));\n"
                            + "log(/[\\w]/.test('a'));\n"
                            + "log(/[\\d]/.test('x'));";
        testEvaluate(script);
    }

    @Test
    @Alerts("true")
    public void setSingleChar() throws Exception {
        final String script = "log(/[a]/.test('a'));";
        testEvaluate(script);
    }

    @Test
    @Alerts({"true", "true"})
    public void setNegatedShorthand() throws Exception {
        final String script = "log(/[\\D]/.test('a'));\n"
                            + "log(/[\\W]/.test(' '));";
        testEvaluate(script);
    }

    @Test
    @Alerts({"true", "false"})
    public void setNegatedWithShorthand() throws Exception {
        final String script = "log(/[^\\d]/.test('a'));\n"
                            + "log(/[^\\d]/.test('5'));";
        testEvaluate(script);
    }

    @Test
    @Alerts("true")
    public void setEmptyNegatedMatchesAnything() throws Exception {
        // [^] matches any character including newlines
        final String script = "log(/[^]/.test('\\n'));";
        testEvaluate(script);
    }

    @Test
    @Alerts({"true", "false"})
    public void setHyphenAtStart() throws Exception {
        final String script = "log(/[-a]/.test('-'));\n"
                            + "log(/[-a]/.test('b'));";
        testEvaluate(script);
    }

    @Test
    @Alerts({"true", "false"})
    public void setHyphenAtEnd() throws Exception {
        final String script = "log(/[a-]/.test('-'));\n"
                            + "log(/[a-]/.test('b'));";
        testEvaluate(script);
    }
}