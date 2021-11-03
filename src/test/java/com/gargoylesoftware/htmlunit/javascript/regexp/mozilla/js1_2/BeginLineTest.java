/*
 * Copyright (c) 2002-2021 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript.regexp.mozilla.js1_2;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * Tests originally in '/js/src/tests/js1_2/regexp/beginLine.js'.
 *
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class BeginLineTest extends WebDriverTestCase {

    /**
     * Tests 'abcde'.match(new RegExp('^ab')).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("ab")
    public void test1() throws Exception {
        test("'abcde'.match(new RegExp('^ab'))");
    }

    /**
     * Tests 'ab\ncde'.match(new RegExp('^..^e')).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("null")
    public void test2() throws Exception {
        test("'ab\\ncde'.match(new RegExp('^..^e'))");
    }

    /**
     * Tests 'yyyyy'.match(new RegExp('^xxx')).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("null")
    public void test3() throws Exception {
        test("'yyyyy'.match(new RegExp('^xxx'))");
    }

    /**
     * Tests '^^^x'.match(new RegExp('^\\^+')).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("^^^")
    public void test4() throws Exception {
        test("'^^^x'.match(new RegExp('^\\\\^+'))");
    }

    /**
     * Tests '^^^x'.match(/^\^+/).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("^^^")
    public void test5() throws Exception {
        test("'^^^x'.match(/^\\^+/)");
    }

    /**
     * Tests 'abc\n123xyz'.match(new RegExp('^\\d+', 'm')).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("123")
    public void test6() throws Exception {
        test("'abc\\n123xyz'.match(new RegExp('^\\\\d+', 'm'))");
    }

    private void test(final String script) throws Exception {
        final String html = "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  log(" + script + ");\n"
            + "</script></head><body>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }
}
