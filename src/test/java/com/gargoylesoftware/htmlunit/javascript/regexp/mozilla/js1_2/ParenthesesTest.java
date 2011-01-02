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
 * Tests originally in '/js/src/tests/js1_2/regexp/parentheses.js'.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class ParenthesesTest extends WebDriverTestCase {

    /**
     * Tests 'abc'.match(new RegExp('(abc)')).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("abc,abc")
    public void test1() throws Exception {
        test("'abc'.match(new RegExp('(abc)'))");
    }

    /**
     * Tests 'abcdefg'.match(new RegExp('a(bc)d(ef)g')).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("abcdefg,bc,ef")
    public void test2() throws Exception {
        test("'abcdefg'.match(new RegExp('a(bc)d(ef)g'))");
    }

    /**
     * Tests 'abcdefg'.match(new RegExp('(.{3})(.{4})')).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("abcdefg,abc,defg")
    public void test3() throws Exception {
        test("'abcdefg'.match(new RegExp('(.{3})(.{4})'))");
    }

    /**
     * Tests 'aabcdaabcd'.match(new RegExp('(aa)bcd\\1')).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("aabcdaa,aa")
    public void test4() throws Exception {
        test("'aabcdaabcd'.match(new RegExp('(aa)bcd\\\\1'))");
    }

    /**
     * Tests 'aabcdaabcd'.match(new RegExp('(aa).+\\1')).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("aabcdaa,aa")
    public void test5() throws Exception {
        test("'aabcdaabcd'.match(new RegExp('(aa).+\\\\1'))");
    }

    /**
     * Tests 'aabcdaabcd'.match(new RegExp('(.{2}).+\\1')).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("aabcdaa,aa")
    public void test6() throws Exception {
        test("'aabcdaabcd'.match(new RegExp('(.{2}).+\\\\1'))");
    }

    /**
     * Tests '123456123456'.match(new RegExp('(\\d{3})(\\d{3})\\1\\2')).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("123456123456,123,456")
    public void test7() throws Exception {
        test("'123456123456'.match(new RegExp('(\\\\d{3})(\\\\d{3})\\\\1\\\\2'))");
    }

    /**
     * Tests 'abcdefg'.match(new RegExp('a(..(..)..)')).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("abcdefg,bcdefg,de")
    public void test8() throws Exception {
        test("'abcdefg'.match(new RegExp('a(..(..)..)'))");
    }

    /**
     * Tests 'abcdefg'.match(/a(..(..)..)/).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("abcdefg,bcdefg,de")
    public void test9() throws Exception {
        test("'abcdefg'.match(/a(..(..)..)/)");
    }

    /**
     * Tests 'xabcdefg'.match(new RegExp('(a(b(c)))(d(e(f)))')).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("abcdef,abc,bc,c,def,ef,f")
    public void test10() throws Exception {
        test("'xabcdefg'.match(new RegExp('(a(b(c)))(d(e(f)))'))");
    }

    /**
     * Tests 'xabcdefbcefg'.match(new RegExp('(a(b(c)))(d(e(f)))\\2\\5')).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("abcdefbcef,abc,bc,c,def,ef,f")
    public void test11() throws Exception {
        test("'xabcdefbcefg'.match(new RegExp('(a(b(c)))(d(e(f)))\\\\2\\\\5'))");
    }

    /**
     * Tests 'abcd'.match(new RegExp('a(.?)b\\1c\\1d\\1')).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("abcd,")
    public void test12() throws Exception {
        test("'abcd'.match(new RegExp('a(.?)b\\\\1c\\\\1d\\\\1'))");
    }

    /**
     * Tests 'abcd'.match(/a(.?)b\1c\1d\1/).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("abcd,")
    public void test13() throws Exception {
        test("'abcd'.match(/a(.?)b\\1c\\1d\\1/)");
    }

    private void test(final String script) throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  alert(" + script + ");\n"
            + "</script></head><body>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }
}
