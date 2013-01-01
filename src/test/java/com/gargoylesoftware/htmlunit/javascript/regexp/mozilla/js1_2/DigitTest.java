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
package com.gargoylesoftware.htmlunit.javascript.regexp.mozilla.js1_2;

import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.IE;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * Tests originally in '/js/src/tests/js1_2/regexp/digit.js'.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class DigitTest extends WebDriverTestCase {

    private static final String non_digits = "_abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"
        + "\\f\\n\\r\\t\\v~`!@#$%^&*()-+={[}]|\\\\:;\\'<,>./? \"";

    private static final String non_digits_expected = "_abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"
        + "\f\n\r\t\u000B~`!@#$%^&*()-+={[}]|\\:;\'<,>./? \"";

    private static final String non_digits_expected_ie = "_abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"
        + "\f\n\r\tv~`!@#$%^&*()-+={[}]|\\:;\'<,>./? \"";

    private static final String digits = "1234567890";

    /**
     * Tests digits.match(new RegExp('\\d+')).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(digits)
    public void test1() throws Exception {
        final String initialScript = "var digits = '" + digits + "'";
        test(initialScript, "digits.match(new RegExp('\\\\d+'))");
    }

    /**
     * Tests non_digits.match(new RegExp('\\D+')).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(FF = non_digits_expected, IE = non_digits_expected_ie)
    @NotYetImplemented(IE)
    public void test2() throws Exception {
        final String initialScript = "var non_digits = '" + non_digits + "'";
        test(initialScript, "non_digits.match(new RegExp('\\\\D+'))");
    }

    /**
     * Tests non_digits.match(new RegExp('\\d')).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("null")
    public void test3() throws Exception {
        final String initialScript = "var non_digits = '" + non_digits + "'";
        test(initialScript, "non_digits.match(new RegExp('\\\\d'))");
    }

    /**
     * Tests digits.match(new RegExp('\\D')).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("null")
    public void test4() throws Exception {
        final String initialScript = "var digits = '" + digits + "'";
        test(initialScript, "digits.match(new RegExp('\\\\D'))");
    }

    /**
     * Tests s.match(new RegExp('\\d+')).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(digits)
    public void test5() throws Exception {
        final String initialScript = "var s = '" + non_digits + digits + "'";
        test(initialScript, "s.match(new RegExp('\\\\d+'))");
    }

    /**
     * Tests s.match(new RegExp('\\D+')).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(FF = non_digits_expected, IE = non_digits_expected_ie)
    @NotYetImplemented(IE)
    public void test6() throws Exception {
        final String initialScript = "var s = '" + digits + non_digits + "'";
        test(initialScript, "s.match(new RegExp('\\\\D+'))");
    }

    /**
     * Tests s.match(new RegExp('\\d')).
     * @throws Exception if the test fails
     */
    @Test
    public void test7() throws Exception {
        for (int i = 0; i < digits.length(); i++) {
            final String initialScript = "var s = 'ab" + digits.charAt(i) + "cd'";
            setExpectedAlerts(String.valueOf(digits.charAt(i)));
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
        for (int i = 0; i < non_digits.length() - 1; i++) {
            final char ch = non_digits.charAt(i);
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
                    expected = "\n";
                    input = "\\" + ch;
                    break;

                case 'r':
                    expected = "\r";
                    input = "\\" + ch;
                    break;

                case 't':
                    expected = "\t";
                    input = "\\" + ch;
                    break;

                case 'v':
                    if (!getBrowserVersion().isIE()) {
                        expected = "\u000B";
                        input = "\\" + ch;
                    }
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
        String html = "<html><head><title>foo</title><script>\n";
        if (initialScript != null) {
            html += initialScript + ";\n";
        }
        html += "  alert(" + script + ");\n"
            + "</script></head><body>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }
}
