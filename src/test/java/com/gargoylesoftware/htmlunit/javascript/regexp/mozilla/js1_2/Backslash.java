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
 * Tests originally in '/js/src/tests/js1_2/regexp/backslash.js'.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class Backslash extends WebDriverTestCase {

    /**
     * Tests 'abcde'.match(new RegExp('\e')).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("e")
    public void test1() throws Exception {
        test("'abcde'.match(new RegExp('\\e'))");
    }

    /**
     * Tests 'ab\\cde'.match(new RegExp('\\\\')).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("\\")
    public void test2() throws Exception {
        test("'ab\\\\cde'.match(new RegExp('\\\\\\\\'))");
    }

    /**
     * Tests 'ab\\cde'.match(/\\/).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("\\")
    public void test3() throws Exception {
        test("'ab\\\\cde'.match(/\\\\/)");
    }

    /**
     * Tests 'before ^$*+?.()|{}[] after'.match(new RegExp('\\^\\$\\*\\+\\?\\.\\(\\)\\|\\{\\}\\[\\]')).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("^$*+?.()|{}[]")
    public void test4() throws Exception {
        test("'before ^$*+?.()|{}[] after'.match("
                + "new RegExp('\\\\^\\\\$\\\\*\\\\+\\\\?\\\\.\\\\(\\\\)\\\\|\\\\{\\\\}\\\\[\\\\]'))");
    }

    /**
     * Tests 'before ^$*+?.()|{}[] after'.match(/\^\$\*\+\?\.\(\)\|\{\}\[\]/).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("^$*+?.()|{}[]")
    public void test5() throws Exception {
        test("'before ^$*+?.()|{}[] after'.match(/\\^\\$\\*\\+\\?\\.\\(\\)\\|\\{\\}\\[\\]/)");
    }

    private void test(final String script) throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  alert(" + script + ");\n"
            + "</script></head><body>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }
}
