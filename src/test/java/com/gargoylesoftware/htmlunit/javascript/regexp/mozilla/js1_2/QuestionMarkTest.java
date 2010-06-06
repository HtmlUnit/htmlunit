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
 * Tests originally in '/js/src/tests/js1_2/regexp/question_mark.js'.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class QuestionMarkTest extends WebDriverTestCase {

    /**
     * Tests 'abcdef'.match(new RegExp('cd?e')).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("cde")
    public void test1() throws Exception {
        test("'abcdef'.match(new RegExp('cd?e'))");
    }

    /**
     * Tests 'abcdef'.match(new RegExp('cdx?e')).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("cde")
    public void test2() throws Exception {
        test("'abcdef'.match(new RegExp('cdx?e'))");
    }

    /**
     * Tests 'pqrstuvw'.match(new RegExp('o?pqrst')).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("pqrst")
    public void test3() throws Exception {
        test("'pqrstuvw'.match(new RegExp('o?pqrst'))");
    }

    /**
     * Tests 'abcd'.match(new RegExp('x?y?z?')).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void test4() throws Exception {
        test("'abcd'.match(new RegExp('x?y?z?'))");
    }

    /**
     * Tests 'abcd'.match(new RegExp('x?ay?bz?c')).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("abc")
    public void test5() throws Exception {
        test("'abcd'.match(new RegExp('x?ay?bz?c'))");
    }

    /**
     * Tests 'abcd'.match(/x?ay?bz?c/).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("abc")
    public void test6() throws Exception {
        test("'abcd'.match(/x?ay?bz?c/)");
    }

    /**
     * Tests 'abbbbc'.match(new RegExp('b?b?b?b')).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("bbbb")
    public void test7() throws Exception {
        test("'abbbbc'.match(new RegExp('b?b?b?b'))");
    }

    /**
     * Tests '123az789'.match(new RegExp('ab?c?d?x?y?z')).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("az")
    public void test8() throws Exception {
        test("'123az789'.match(new RegExp('ab?c?d?x?y?z'))");
    }

    /**
     * Tests '123az789'.match(/ab?c?d?x?y?z/).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("az")
    public void test9() throws Exception {
        test("'123az789'.match(/ab?c?d?x?y?z/)");
    }

    /**
     * Tests '?????'.match(new RegExp('\\??\\??\\??\\??\\??')).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("?????")
    public void test10() throws Exception {
        test("'?????'.match(new RegExp('\\\\??\\\\??\\\\??\\\\??\\\\??'))");
    }

    /**
     * Tests 'test'.match(new RegExp('.?.?.?.?.?.?.?')).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("test")
    public void test11() throws Exception {
        test("'test'.match(new RegExp('.?.?.?.?.?.?.?'))");
    }

    private void test(final String script) throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  alert(" + script + ");\n"
            + "</script></head><body>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }
}
