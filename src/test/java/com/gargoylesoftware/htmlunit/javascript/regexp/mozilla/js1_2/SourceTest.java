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
 * Tests originally in '/js/src/tests/js1_2/regexp/source.js'.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class SourceTest extends WebDriverTestCase {

    /**
     * Tests /xyz/g.source.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("xyz")
    public void test1() throws Exception {
        test("/xyz/g.source");
    }

    /**
     * Tests /xyz/.source.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("xyz")
    public void test2() throws Exception {
        test("/xyz/.source");
    }

    /**
     * Tests /abc\\def/.source.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("abc\\\\def")
    public void test3() throws Exception {
        test("/abc\\\\def/.source");
    }

    /**
     * Tests /abc[\b]def/.source.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("abc[\\b]def")
    public void test4() throws Exception {
        test("/abc[\\b]def/.source");
    }

    /**
     * Tests (new RegExp('xyz')).source.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("xyz")
    public void test5() throws Exception {
        test("(new RegExp('xyz')).source");
    }

    /**
     * Tests (new RegExp('xyz','g')).source.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("xyz")
    public void test6() throws Exception {
        test("(new RegExp('xyz','g')).source");
    }

    /**
     * Tests (new RegExp('abc\\\\def')).source.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("abc\\\\def")
    public void test7() throws Exception {
        test("(new RegExp('abc\\\\\\\\def')).source");
    }

    /**
     * Tests (new RegExp('abc[\\b]def')).source.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("abc[\\b]def")
    public void test8() throws Exception {
        test("(new RegExp('abc[\\\\b]def')).source");
    }

    private void test(final String script) throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  alert(" + script + ");\n"
            + "</script></head><body>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }
}
