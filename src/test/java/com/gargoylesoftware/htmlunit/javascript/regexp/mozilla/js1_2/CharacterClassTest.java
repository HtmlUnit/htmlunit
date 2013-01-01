/*
 * Copyright (c) 2002-2013 Gargoyle Software Inc.
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
 * Tests originally in '/js/src/tests/js1_2/regexp/character_class.js'.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class CharacterClassTest extends WebDriverTestCase {

    /**
     * Tests 'abcde'.match(new RegExp('ab[ercst]de')).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("abcde")
    public void test1() throws Exception {
        test("'abcde'.match(new RegExp('ab[ercst]de'))");
    }

    /**
     * Tests 'abcde'.match(new RegExp('ab[erst]de')).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("null")
    public void test2() throws Exception {
        test("'abcde'.match(new RegExp('ab[erst]de'))");
    }

    /**
     * Tests 'abcdefghijkl'.match(new RegExp('[d-h]+')).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("defgh")
    public void test3() throws Exception {
        test("'abcdefghijkl'.match(new RegExp('[d-h]+'))");
    }

    /**
     * Tests 'abc6defghijkl'.match(new RegExp('[1234567].{2}')).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("6de")
    public void test4() throws Exception {
        test("'abc6defghijkl'.match(new RegExp('[1234567].{2}'))");
    }

    /**
     * Tests '\n\n\abc324234\n'.match(new RegExp('[a-c\\d]+')).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("abc324234")
    public void test5() throws Exception {
        test("'\\n\\n\\abc324234\\n'.match(new RegExp('[a-c\\\\d]+'))");
    }

    /**
     * Tests 'abc'.match(new RegExp('ab[.]?c')).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("abc")
    public void test6() throws Exception {
        test("'abc'.match(new RegExp('ab[.]?c'))");
    }

    /**
     * Tests 'abc'.match(new RegExp('a[b]c')).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("abc")
    public void test7() throws Exception {
        test("'abc'.match(new RegExp('a[b]c'))");
    }

    /**
     * Tests 'a1b  b2c  c3d  def  f4g'.match(new RegExp('[a-z][^1-9][a-z]')).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("def")
    public void test8() throws Exception {
        test("'a1b  b2c  c3d  def  f4g'.match(new RegExp('[a-z][^1-9][a-z]'))");
    }

    /**
     * Tests '123*&$abc'.match(new RegExp('[*&$]{3}')).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("*&$")
    public void test9() throws Exception {
        test("'123*&$abc'.match(new RegExp('[*&$]{3}'))");
    }

    /**
     * Tests 'abc'.match(new RegExp('a[^1-9]c')).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("abc")
    public void test10() throws Exception {
        test("'abc'.match(new RegExp('a[^1-9]c'))");
    }

    /**
     * Tests 'abc'.match(new RegExp('a[^b]c')).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("null")
    public void test11() throws Exception {
        test("'abc'.match(new RegExp('a[^b]c'))");
    }

    /**
     * Tests 'abc#$%def%&*@ghi)(*&'.match(new RegExp('[^a-z]{4}')).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("%&*@")
    public void test12() throws Exception {
        test("'abc#$%def%&*@ghi)(*&'.match(new RegExp('[^a-z]{4}'))");
    }

    /**
     * Tests 'abc#$%def%&*@ghi)(*&'.match(/[^a-z]{4}/).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("%&*@")
    public void test13() throws Exception {
        test("'abc#$%def%&*@ghi)(*&'.match(/[^a-z]{4}/)");
    }

    private void test(final String script) throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  alert(" + script + ");\n"
            + "</script></head><body>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }
}
