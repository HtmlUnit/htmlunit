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
 * Tests originally in '/js/src/tests/js1_2/regexp/string_replace.js'.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class StringReplaceTest extends WebDriverTestCase {

    /**
     * Tests 'adddb'.replace(/ddd/,'XX').
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("aXXb")
    public void test1() throws Exception {
        test("'adddb'.replace(/ddd/,'XX')");
    }

    /**
     * Tests 'adddb'.replace(/eee/,'XX').
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("adddb")
    public void test2() throws Exception {
        test("'adddb'.replace(/eee/,'XX')");
    }

    /**
     * Tests '34 56 78b 12'.replace(new RegExp('[0-9]+b'),'**').
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("34 56 ** 12")
    public void test3() throws Exception {
        test("'34 56 78b 12'.replace(new RegExp('[0-9]+b'),'**')");
    }

    /**
     * Tests '34 56 78b 12'.replace(new RegExp('[0-9]+c'),'XX').
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("34 56 78b 12")
    public void test4() throws Exception {
        test("'34 56 78b 12'.replace(new RegExp('[0-9]+c'),'XX')");
    }

    /**
     * Tests 'original'.replace(new RegExp(),'XX').
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("XXoriginal")
    public void test5() throws Exception {
        test("'original'.replace(new RegExp(),'XX')");
    }

    /**
     * Tests 'qwe ert x\t\n 345654AB'.replace(new RegExp('x\\s*\\d+(..)$'),'****').
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("qwe ert ****")
    public void test6() throws Exception {
        test("'qwe ert x\\t\\n 345654AB'.replace(new RegExp('x\\\\s*\\\\d+(..)$'),'****')");
    }

    private void test(final String script) throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  alert(" + script + ");\n"
            + "</script></head><body>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }
}
