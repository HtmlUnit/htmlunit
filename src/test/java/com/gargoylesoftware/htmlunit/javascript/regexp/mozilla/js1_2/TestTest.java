/*
 * Copyright (c) 2002-2010 Gargoyle Software Inc.
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
 * Tests originally in '/js/src/tests/js1_2/regexp/test.js'.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class TestTest extends WebDriverTestCase {

    /**
     * Tests /[0-9]{3}/.test('23 2 34 678 9 09').
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void test1() throws Exception {
        test("/[0-9]{3}/.test('23 2 34 678 9 09')");
    }

    /**
     * Tests /[0-9]{3}/.test('23 2 34 78 9 09').
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void test2() throws Exception {
        test("/[0-9]{3}/.test('23 2 34 78 9 09')");
    }

    /**
     * Tests /\w+ \w+ \w+/.test("do a test").
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void test3() throws Exception {
        test("/\\w+ \\w+ \\w+/.test(\"do a test\")");
    }

    /**
     * Tests /\w+ \w+ \w+/.test("a test").
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void test4() throws Exception {
        test("/\\w+ \\w+ \\w+/.test(\"a test\")");
    }

    /**
     * Tests (new RegExp('[0-9]{3}')).test('23 2 34 678 9 09').
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void test5() throws Exception {
        test("(new RegExp('[0-9]{3}')).test('23 2 34 678 9 09')");
    }

    /**
     * Tests (new RegExp('[0-9]{3}')).test('23 2 34 78 9 09').
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void test6() throws Exception {
        test("(new RegExp('[0-9]{3}')).test('23 2 34 78 9 09')");
    }

    /**
     * Tests (new RegExp('\\w+ \\w+ \\w+')).test("do a test").
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void test7() throws Exception {
        test("(new RegExp('\\\\w+ \\\\w+ \\\\w+')).test(\"do a test\")");
    }

    /**
     * Tests (new RegExp('\\w+ \\w+ \\w+')).test("a test").
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void test8() throws Exception {
        test("(new RegExp('\\\\w+ \\\\w+ \\\\w+')).test(\"a test\")");
    }

    private void test(final String script) throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  alert(" + script + ");\n"
            + "</script></head><body>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }
}
