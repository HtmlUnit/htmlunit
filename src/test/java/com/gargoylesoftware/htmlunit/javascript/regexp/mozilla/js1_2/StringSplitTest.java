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
import com.gargoylesoftware.htmlunit.BrowserRunner.Browser;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;

/**
 * Tests originally in '/js/src/tests/js1_2/regexp/string_split.js'.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class StringSplitTest extends WebDriverTestCase {

    /**
     * Tests 'a b c de f'.split(/\s/).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a,b,c,de,f")
    public void test1() throws Exception {
        test("'a b c de f'.split(/\\s/)");
    }

    /**
     * Tests 'a b c de f'.split(/\s/,3).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a,b,c")
    public void test2() throws Exception {
        test("'a b c de f'.split(/\\s/,3)");
    }

    /**
     * Tests 'a b c de f'.split(/X/).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a b c de f")
    public void test3() throws Exception {
        test("'a b c de f'.split(/X/)");
    }

    /**
     * Tests 'dfe23iu 34 =+65--'.split(/\d+/).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("dfe,iu , =+,--")
    public void test4() throws Exception {
        test("'dfe23iu 34 =+65--'.split(/\\d+/)");
    }

    /**
     * Tests 'dfe23iu 34 =+65--'.split(new RegExp('\\d+')).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("dfe,iu , =+,--")
    public void test5() throws Exception {
        test("'dfe23iu 34 =+65--'.split(new RegExp('\\\\d+'))");
    }

    /**
     * Tests 'abc'.split(/[a-z]/).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(FF = ",,,", IE = "")
    @NotYetImplemented(Browser.IE)
    public void test6() throws Exception {
        test("'abc'.split(/[a-z]/)");
    }

    /**
     * Tests 'abc'.split(/[a-z]/).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(FF = ",,,", IE = "")
    @NotYetImplemented(Browser.IE)
    public void test7() throws Exception {
        test("'abc'.split(/[a-z]/)");
    }

    /**
     * Tests 'abc'.split(new RegExp('[a-z]')).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(FF = ",,,", IE = "")
    @NotYetImplemented(Browser.IE)
    public void test8() throws Exception {
        test("'abc'.split(new RegExp('[a-z]'))");
    }

    /**
     * Tests 'abc'.split(new RegExp('[a-z]')).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(FF = ",,,", IE = "")
    @NotYetImplemented(Browser.IE)
    public void test9() throws Exception {
        test("'abc'.split(new RegExp('[a-z]'))");
    }

    private void test(final String script) throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  alert(" + script + ");\n"
            + "</script></head><body>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }
}
