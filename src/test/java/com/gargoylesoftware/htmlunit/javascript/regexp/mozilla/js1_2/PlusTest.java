/*
 * Copyright (c) 2002-2014 Gargoyle Software Inc.
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
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * Tests originally in '/js/src/tests/js1_2/regexp/plus.js'.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class PlusTest extends WebDriverTestCase {

    /**
     * Tests 'abcdddddefg'.match(new RegExp('d+')).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("ddddd")
    public void test1() throws Exception {
        test("'abcdddddefg'.match(new RegExp('d+'))");
    }

    /**
     * Tests 'abcdefg'.match(new RegExp('o+')).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("null")
    public void test2() throws Exception {
        test("'abcdefg'.match(new RegExp('o+'))");
    }

    /**
     * Tests 'abcdefg'.match(new RegExp('d+')).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("d")
    public void test3() throws Exception {
        test("'abcdefg'.match(new RegExp('d+'))");
    }

    /**
     * Tests 'abbbbbbbc'.match(new RegExp('(b+)(b+)(b+)')).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("bbbbbbb,bbbbb,b,b")
    public void test4() throws Exception {
        test("'abbbbbbbc'.match(new RegExp('(b+)(b+)(b+)'))");
    }

    /**
     * Tests 'abbbbbbbc'.match(new RegExp('(b+)(b*)')).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("bbbbbbb,bbbbbbb,")
    public void test5() throws Exception {
        test("'abbbbbbbc'.match(new RegExp('(b+)(b*)'))");
    }

    /**
     * Tests 'abbbbbbbc'.match(new RegExp('b*b+')).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("bbbbbbb")
    public void test6() throws Exception {
        test("'abbbbbbbc'.match(new RegExp('b*b+'))");
    }

    /**
     * Tests 'abbbbbbbc'.match(/(b+)(b*)/).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("bbbbbbb,bbbbbbb,")
    public void test7() throws Exception {
        test("'abbbbbbbc'.match(/(b+)(b*)/)");
    }

    /**
     * Tests 'abbbbbbbc'.match(/b*b+/).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("bbbbbbb")
    public void test8() throws Exception {
        test("'abbbbbbbc'.match(/b*b+/)");
    }

    private void test(final String script) throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  alert(" + script + ");\n"
            + "</script></head><body>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }
}
