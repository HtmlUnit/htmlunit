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
 * Tests originally in '/js/src/tests/js1_2/regexp/ignoreCase.js'.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class IgnoreCaseTest extends WebDriverTestCase {

    /**
     * Tests /xyz/i.ignoreCase.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void test1() throws Exception {
        test("/xyz/i.ignoreCase");
    }

    /**
     * Tests /xyz/.ignoreCase.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void test2() throws Exception {
        test("/xyz/.ignoreCase");
    }

    /**
     * Tests 'ABC def ghi'.match(/[a-z]+/ig).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("ABC,def,ghi")
    public void test3() throws Exception {
        test("'ABC def ghi'.match(/[a-z]+/ig)");
    }

    /**
     * Tests 'ABC def ghi'.match(/[a-z]+/i).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("ABC")
    public void test4() throws Exception {
        test("'ABC def ghi'.match(/[a-z]+/i)");
    }

    /**
     * Tests 'ABC def ghi'.match(/([a-z]+)/ig).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("ABC,def,ghi")
    public void test5() throws Exception {
        test("'ABC def ghi'.match(/([a-z]+)/ig)");
    }

    /**
     * Tests 'ABC def ghi'.match(/([a-z]+)/i).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("ABC,ABC")
    public void test6() throws Exception {
        test("'ABC def ghi'.match(/([a-z]+)/i)");
    }

    /**
     * Tests 'ABC def ghi'.match(/[a-z]+/).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("def")
    public void test7() throws Exception {
        test("'ABC def ghi'.match(/[a-z]+/)");
    }

    /**
     * Tests (new RegExp('xyz','i')).ignoreCase.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void test8() throws Exception {
        test("(new RegExp('xyz','i')).ignoreCase");
    }

    /**
     * Tests (new RegExp('xyz')).ignoreCase.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void test9() throws Exception {
        test("(new RegExp('xyz')).ignoreCase");
    }

    /**
     * Tests 'ABC def ghi'.match(new RegExp('[a-z]+','ig')).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("ABC,def,ghi")
    public void test10() throws Exception {
        test("'ABC def ghi'.match(new RegExp('[a-z]+','ig'))");
    }

    /**
     * Tests 'ABC def ghi'.match(new RegExp('[a-z]+','i')).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("ABC")
    public void test11() throws Exception {
        test("'ABC def ghi'.match(new RegExp('[a-z]+','i'))");
    }

    /**
     * Tests 'ABC def ghi'.match(new RegExp('([a-z]+)','ig')).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("ABC,def,ghi")
    public void test12() throws Exception {
        test("'ABC def ghi'.match(new RegExp('([a-z]+)','ig'))");
    }

    /**
     * Tests 'ABC def ghi'.match(new RegExp('([a-z]+)','i')).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("ABC,ABC")
    public void test13() throws Exception {
        test("'ABC def ghi'.match(new RegExp('([a-z]+)','i'))");
    }

    /**
     * Tests 'ABC def ghi'.match(new RegExp('[a-z]+')).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("def")
    public void test14() throws Exception {
        test("'ABC def ghi'.match(new RegExp('[a-z]+'))");
    }

    private void test(final String script) throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  alert(" + script + ");\n"
            + "</script></head><body>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }
}
