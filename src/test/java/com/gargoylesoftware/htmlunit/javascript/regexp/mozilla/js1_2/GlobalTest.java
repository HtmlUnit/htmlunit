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
 * Tests originally in '/js/src/tests/js1_2/regexp/global.js'.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class GlobalTest extends WebDriverTestCase {

    /**
     * Tests /xyz/g.global.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void test1() throws Exception {
        test("/xyz/g.global");
    }

    /**
     * Tests /xyz/.global.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void test2() throws Exception {
        test("/xyz/.global");
    }

    /**
     * Tests '123 456 789'.match(/\d+/g).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("123,456,789")
    public void test3() throws Exception {
        test("'123 456 789'.match(/\\d+/g)");
    }

    /**
     * Tests '123 456 789'.match(/(\d+)/g).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("123,456,789")
    public void test4() throws Exception {
        test("'123 456 789'.match(/(\\d+)/g)");
    }

    /**
     * Tests '123 456 789'.match(/\d+/).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("123")
    public void test5() throws Exception {
        test("'123 456 789'.match(/\\d+/)");
    }

    /**
     * Tests (new RegExp('[a-z]','g')).global.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void test6() throws Exception {
        test("(new RegExp('[a-z]','g')).global");
    }

    /**
     * Tests (new RegExp('[a-z]','i')).global.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void test7() throws Exception {
        test("(new RegExp('[a-z]','i')).global");
    }

    /**
     * Tests '123 456 789'.match(new RegExp('\\d+','g')).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("123,456,789")
    public void test8() throws Exception {
        test("'123 456 789'.match(new RegExp('\\\\d+','g'))");
    }

    /**
     * Tests '123 456 789'.match(new RegExp('(\\d+)','g')).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("123,456,789")
    public void test9() throws Exception {
        test("'123 456 789'.match(new RegExp('(\\\\d+)','g'))");
    }

    /**
     * Tests '123 456 789'.match(new RegExp('\\d+','i')).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("123")
    public void test10() throws Exception {
        test("'123 456 789'.match(new RegExp('\\\\d+','i'))");
    }

    private void test(final String script) throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  alert(" + script + ");\n"
            + "</script></head><body>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }
}
