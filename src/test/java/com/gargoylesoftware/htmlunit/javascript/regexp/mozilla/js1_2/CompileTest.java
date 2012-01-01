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
package com.gargoylesoftware.htmlunit.javascript.regexp.mozilla.js1_2;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;

/**
 * Tests originally in '/js/src/tests/js1_2/regexp/compile.js'.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class CompileTest extends WebDriverTestCase {

    /**
     * Tests '234X456X7890'.match(regularExpression).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("456X7890")
    public void test1() throws Exception {
        final String initialScript = "var regularExpression = new RegExp();\n"
            + "regularExpression.compile('[0-9]{3}x[0-9]{4}', 'i');";
        test(initialScript, "'234X456X7890'.match(regularExpression)");
    }

    /**
     * Tests regularExpression.source.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("[0-9]{3}x[0-9]{4}")
    public void test2() throws Exception {
        final String initialScript = "var regularExpression = new RegExp();\n"
            + "regularExpression.compile('[0-9]{3}x[0-9]{4}', 'i');";
        test(initialScript, "regularExpression.source");
    }

    /**
     * Tests regularExpression.global.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void test3() throws Exception {
        final String initialScript = "var regularExpression = new RegExp();\n"
            + "regularExpression.compile('[0-9]{3}x[0-9]{4}', 'i');";
        test(initialScript, "regularExpression.global");
    }

    /**
     * Tests regularExpression.ignoreCase.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void test4() throws Exception {
        final String initialScript = "var regularExpression = new RegExp();\n"
            + "regularExpression.compile('[0-9]{3}x[0-9]{4}', 'i');";
        test(initialScript, "regularExpression.ignoreCase");
    }

    /**
     * Tests '234X456X7890'.match(regularExpression).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("234X456")
    public void test5() throws Exception {
        final String initialScript = "var regularExpression = new RegExp();\n"
            + "regularExpression.compile('[0-9]{3}X[0-9]{3}', 'g')";
        test(initialScript, "'234X456X7890'.match(regularExpression)");
    }

    /**
     * Tests regularExpression.source.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("[0-9]{3}X[0-9]{3}")
    public void test6() throws Exception {
        final String initialScript = "var regularExpression = new RegExp();\n"
            + "regularExpression.compile('[0-9]{3}X[0-9]{3}', 'g')";
        test(initialScript, "regularExpression.source");
    }

    /**
     * Tests regularExpression.global.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void test7() throws Exception {
        final String initialScript = "var regularExpression = new RegExp();\n"
            + "regularExpression.compile('[0-9]{3}X[0-9]{3}', 'g')";
        test(initialScript, "regularExpression.global");
    }

    /**
     * Tests regularExpression.ignoreCase.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void test8() throws Exception {
        final String initialScript = "var regularExpression = new RegExp();\n"
            + "regularExpression.compile('[0-9]{3}X[0-9]{3}', 'g')";
        test(initialScript, "regularExpression.ignoreCase");
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
