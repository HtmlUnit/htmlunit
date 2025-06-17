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
package org.htmlunit.javascript.regexp.mozilla.js1_2;

import org.htmlunit.WebDriverTestCase;
import org.htmlunit.junit.annotation.Alerts;
import org.junit.jupiter.api.Test;

/**
 * Tests originally in '/js/src/tests/js1_2/regexp/digit.js'.
 *
 * @author Ahmed Ashour
 * @author Frank Danek
 * @author Ronald Brill
 */
public class DigitTest extends WebDriverTestCase {

    private static final String NON_DIGITS = "_abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"
        + "\\f\\n\\r\\t\\v~`!@#$%^&*()-+={[}]|\\\\:;\\'<,>./? \"";

    private static final String NON_DIGITS_EXPECTED = "_abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"
        + "\f\\n\\r\t\u000B~`!@#$%^&*()-+={[}]|\\:;\'<,>./? \"";

    private static final String DIGITS = "1234567890";

    /**
     * Tests digits.match(new RegExp('\\d+')).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DIGITS)
    public void test1() throws Exception {
        final String initialScript = "var digits = '" + DIGITS + "'";
        test(initialScript, "digits.match(new RegExp('\\\\d+'))");
    }

    /**
     * Tests non_digits.match(new RegExp('\\D+')).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(NON_DIGITS_EXPECTED)
    public void test2() throws Exception {
        final String initialScript = "var non_digits = '" + NON_DIGITS + "'";
        test(initialScript, "non_digits.match(new RegExp('\\\\D+'))");
    }

    /**
     * Tests non_digits.match(new RegExp('\\d')).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("null")
    public void test3() throws Exception {
        final String initialScript = "var non_digits = '" + NON_DIGITS + "'";
        test(initialScript, "non_digits.match(new RegExp('\\\\d'))");
    }

    /**
     * Tests digits.match(new RegExp('\\D')).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("null")
    public void test4() throws Exception {
        final String initialScript = "var digits = '" + DIGITS + "'";
        test(initialScript, "digits.match(new RegExp('\\\\D'))");
    }

    /**
     * Tests s.match(new RegExp('\\d+')).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DIGITS)
    public void test5() throws Exception {
        final String initialScript = "var s = '" + NON_DIGITS + DIGITS + "'";
        test(initialScript, "s.match(new RegExp('\\\\d+'))");
    }

    /**
     * Tests s.match(new RegExp('\\D+')).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(NON_DIGITS_EXPECTED)
    public void test6() throws Exception {
        final String initialScript = "var s = '" + DIGITS + NON_DIGITS + "'";
        test(initialScript, "s.match(new RegExp('\\\\D+'))");
    }

    /**
     * Tests s.match(new RegExp('\\d')).
     * @throws Exception if the test fails
     */
    @Test
    public void test7() throws Exception {
        for (int i = 0; i < DIGITS.length(); i++) {
            final String initialScript = "var s = 'ab" + DIGITS.charAt(i) + "cd'";
            setExpectedAlerts(String.valueOf(DIGITS.charAt(i)));
            test(initialScript, "s.match(new RegExp('\\\\d'))");
            test(initialScript, "s.match(/\\d/)");
        }
    }

    /**
     * Tests s.match(new RegExp('\\D')).
     * @throws Exception if the test fails
     */
    @Test
    public void test8() throws Exception {
        for (int i = 0; i < NON_DIGITS.length() - 1; i++) {
            final char ch = NON_DIGITS.charAt(i);
            String expected = String.valueOf(ch);
            String input = expected;
            switch (ch) {
                case '\\':
                    input = "\\" + ch;
                    break;

                case '\'':
                    input = "\\" + ch;
                    break;

                case 'f':
                    expected = "\f";
                    input = "\\" + ch;
                    break;

                case 'n':
                    expected = "\\n";
                    input = "\\" + ch;
                    break;

                case 'r':
                    expected = "\\r";
                    input = "\\" + ch;
                    break;

                case 't':
                    expected = "\t";
                    input = "\\" + ch;
                    break;

                case 'v':
                    expected = "\u000B";
                    input = "\\" + ch;
                    break;

                default:
            }

            setExpectedAlerts(expected);

            final String initialScript = "var s = '12" + input + "34'";
            test(initialScript, "s.match(new RegExp('\\\\D'))");
            test(initialScript, "s.match(/\\D/)");
        }
    }

    private void test(final String initialScript, final String script) throws Exception {
        String html = "<html><head>\n"
                + "</head><body>"
                + LOG_TEXTAREA
                + "<script>\n"
                + LOG_TEXTAREA_FUNCTION;
        if (initialScript != null) {
            html += initialScript + ";\n";
        }
        html += "  var txt = '' + " + script + ";\n"
            + "  txt = txt.replace('\\r', '\\\\r');\n"
            + "  txt = txt.replace('\\n', '\\\\n');\n"
            + "  log(txt);\n"
            + "</script>\n"
            + "</body></html>";
        loadPageVerifyTextArea2(html);
    }
}
