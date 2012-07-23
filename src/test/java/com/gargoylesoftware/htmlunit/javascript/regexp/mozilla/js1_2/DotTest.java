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
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * Tests originally in '/js/src/tests/js1_2/regexp/dot.js'.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class DotTest extends WebDriverTestCase {

    /**
     * Tests 'abcde'.match(new RegExp('ab.de')).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("abcde")
    public void test1() throws Exception {
        test("'abcde'.match(new RegExp('ab.de'))");
    }

    /**
     * Tests 'line 1\nline 2'.match(new RegExp('.+')).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("line 1")
    public void test2() throws Exception {
        test("'line 1\\nline 2'.match(new RegExp('.+'))");
    }

    /**
     * Tests 'this is a test'.match(new RegExp('.*a.*')).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("this is a test")
    public void test3() throws Exception {
        test("'this is a test'.match(new RegExp('.*a.*'))");
    }

    /**
     * Tests 'this is a *&^%$# test'.match(new RegExp('.+')).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("this is a *&^%$# test")
    public void test4() throws Exception {
        test("'this is a *&^%$# test'.match(new RegExp('.+'))");
    }

    /**
     * Tests '....'.match(new RegExp('.+')).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("....")
    public void test5() throws Exception {
        test("'....'.match(new RegExp('.+'))");
    }

    /**
     * Tests 'abcdefghijklmnopqrstuvwxyz'.match(new RegExp('.+')).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("abcdefghijklmnopqrstuvwxyz")
    public void test6() throws Exception {
        test("'abcdefghijklmnopqrstuvwxyz'.match(new RegExp('.+'))");
    }

    /**
     * Tests 'ABCDEFGHIJKLMNOPQRSTUVWXYZ'.match(new RegExp('.+')).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("ABCDEFGHIJKLMNOPQRSTUVWXYZ")
    public void test7() throws Exception {
        test("'ABCDEFGHIJKLMNOPQRSTUVWXYZ'.match(new RegExp('.+'))");
    }

    /**
     * Tests '`1234567890-=~!@#$%^&*()_+'.match(new RegExp('.+')).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("`1234567890-=~!@#$%^&*()_+")
    public void test8() throws Exception {
        test("'`1234567890-=~!@#$%^&*()_+'.match(new RegExp('.+'))");
    }

    /**
     * Tests '|\\[{]};:\"\',<>.?/'.match(new RegExp('.+')).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("|\\[{]};:\"\',<>.?/")
    public void test9() throws Exception {
        test("'|\\\\[{]};:\\\"\\',<>.?/'.match(new RegExp('.+'))");
    }

    /**
     * Tests '|\\[{]};:\"\',<>.?/'.match(/.+/).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("|\\[{]};:\"\',<>.?/")
    public void test10() throws Exception {
        test("'|\\\\[{]};:\\\"\\',<>.?/'.match(/.+/)");
    }

    private void test(final String script) throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  alert(" + script + ");\n"
            + "</script></head><body>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }
}
