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
 * Tests originally in '/js/src/tests/js1_2/regexp/interval.js'.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class IntervalTest extends WebDriverTestCase {

    /**
     * Tests 'aaabbbbcccddeeeefffff'.match(new RegExp('b{2}c')).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("bbc")
    public void test1() throws Exception {
        test("'aaabbbbcccddeeeefffff'.match(new RegExp('b{2}c'))");
    }

    /**
     * Tests 'aaabbbbcccddeeeefffff'.match(new RegExp('b{8}')).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("null")
    public void test2() throws Exception {
        test("'aaabbbbcccddeeeefffff'.match(new RegExp('b{8}'))");
    }

    /**
     * Tests 'aaabbbbcccddeeeefffff'.match(new RegExp('b{2,}c')).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("bbbbc")
    public void test3() throws Exception {
        test("'aaabbbbcccddeeeefffff'.match(new RegExp('b{2,}c'))");
    }

    /**
     * Tests 'aaabbbbcccddeeeefffff'.match(new RegExp('b{8,}c')).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("null")
    public void test4() throws Exception {
        test("'aaabbbbcccddeeeefffff'.match(new RegExp('b{8,}c'))");
    }

    /**
     * Tests 'aaabbbbcccddeeeefffff'.match(new RegExp('b{2,3}c')).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("bbbc")
    public void test5() throws Exception {
        test("'aaabbbbcccddeeeefffff'.match(new RegExp('b{2,3}c'))");
    }

    /**
     * Tests 'aaabbbbcccddeeeefffff'.match(new RegExp('b{42,93}c')).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("null")
    public void test6() throws Exception {
        test("'aaabbbbcccddeeeefffff'.match(new RegExp('b{42,93}c'))");
    }

    /**
     * Tests 'aaabbbbcccddeeeefffff'.match(new RegExp('b{0,93}c')).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("bbbbc")
    public void test7() throws Exception {
        test("'aaabbbbcccddeeeefffff'.match(new RegExp('b{0,93}c'))");
    }

    /**
     * Tests 'aaabbbbcccddeeeefffff'.match(new RegExp('bx{0,93}c')).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("bc")
    public void test8() throws Exception {
        test("'aaabbbbcccddeeeefffff'.match(new RegExp('bx{0,93}c'))");
    }

    /**
     * Tests 'weirwerdf'.match(new RegExp('.{0,93}')).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("weirwerdf")
    public void test9() throws Exception {
        test("'weirwerdf'.match(new RegExp('.{0,93}'))");
    }

    /**
     * Tests 'wqe456646dsff'.match(new RegExp('\\d{1,}')).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("456646")
    public void test10() throws Exception {
        test("'wqe456646dsff'.match(new RegExp('\\\\d{1,}'))");
    }

    /**
     * Tests '123123'.match(new RegExp('(123){1,}')).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("123123,123")
    public void test11() throws Exception {
        test("'123123'.match(new RegExp('(123){1,}'))");
    }

    /**
     * Tests '123123x123'.match(new RegExp('(123){1,}x\\1')).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("123123x123,123")
    public void test12() throws Exception {
        test("'123123x123'.match(new RegExp('(123){1,}x\\\\1'))");
    }

    /**
     * Tests '123123x123'.match(/(123){1,}x\1/).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("123123x123,123")
    public void test13() throws Exception {
        test("'123123x123'.match(/(123){1,}x\\1/)");
    }

    /**
     * Tests 'xxxxxxx'.match(new RegExp('x{1,2}x{1,}')).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("xxxxxxx")
    public void test14() throws Exception {
        test("'xxxxxxx'.match(new RegExp('x{1,2}x{1,}'))");
    }

    /**
     * Tests 'xxxxxxx'.match(/x{1,2}x{1,}/).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("xxxxxxx")
    public void test15() throws Exception {
        test("'xxxxxxx'.match(/x{1,2}x{1,}/)");
    }

    private void test(final String script) throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  alert(" + script + ");\n"
            + "</script></head><body>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }
}
