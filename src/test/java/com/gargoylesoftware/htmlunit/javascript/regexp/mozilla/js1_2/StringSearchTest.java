/*
 * Copyright (c) 2002-2011 Gargoyle Software Inc.
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
 * Tests originally in '/js/src/tests/js1_2/regexp/string_search.js'.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class StringSearchTest extends WebDriverTestCase {

    /**
     * Tests 'abcdefg'.search(/d/).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("3")
    public void test1() throws Exception {
        test("'abcdefg'.search(/d/)");
    }

    /**
     * Tests 'abcdefg'.search(/x/).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("-1")
    public void test2() throws Exception {
        test("'abcdefg'.search(/x/)");
    }

    /**
     * Tests 'abcdefg123456hijklmn'.search(/\d+/).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("7")
    public void test3() throws Exception {
        test("'abcdefg123456hijklmn'.search(/\\d+/)");
    }

    /**
     * Tests 'abcdefg123456hijklmn'.search(new RegExp()).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0")
    public void test4() throws Exception {
        test("'abcdefg123456hijklmn'.search(new RegExp())");
    }

    /**
     * Tests 'abc'.search(new RegExp('$')).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("3")
    public void test5() throws Exception {
        test("'abc'.search(new RegExp('$'))");
    }

    /**
     * Tests 'abc'.search(new RegExp('^')).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0")
    public void test6() throws Exception {
        test("'abc'.search(new RegExp('^'))");
    }

    /**
     * Tests 'abc1'.search(/.\d/).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("2")
    public void test7() throws Exception {
        test("'abc1'.search(/.\\d/)");
    }

    /**
     * Tests 'abc1'.search(/\d{2}/).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("-1")
    public void test8() throws Exception {
        test("'abc1'.search(/\\d{2}/)");
    }

    private void test(final String script) throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  alert(" + script + ");\n"
            + "</script></head><body>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }
}
