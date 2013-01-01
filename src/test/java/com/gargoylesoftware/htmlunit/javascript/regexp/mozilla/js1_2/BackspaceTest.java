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
 * Tests originally in '/js/src/tests/js1_2/regexp/backspace.js'.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class BackspaceTest extends WebDriverTestCase {

    /**
     * Tests 'abc\bdef'.match(new RegExp('.[\\b].')).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("c\bd")
    public void test1() throws Exception {
        test("'abc\\bdef'.match(new RegExp('.[\\\\b].'))");
    }

    /**
     * Tests 'abc\\bdef'.match(new RegExp('.[\\b].')).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("null")
    public void test2() throws Exception {
        test("'abc\\\\bdef'.match(new RegExp('.[\\\\b].'))");
    }

    /**
     * Tests 'abc\b\b\bdef'.match(new RegExp('c[\\b]{3}d')).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("c\b\b\bd")
    public void test3() throws Exception {
        test("'abc\\b\\b\\bdef'.match(new RegExp('c[\\\\b]{3}d'))");
    }

    /**
     * Tests 'abc\bdef'.match(new RegExp('[^\\[\\b\\]]+')).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("abc")
    public void test4() throws Exception {
        test("'abc\\bdef'.match(new RegExp('[^\\\\[\\\\b\\\\]]+'))");
    }

    /**
     * Tests 'abcdef'.match(new RegExp('[^\\[\\b\\]]+')).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("abcdef")
    public void test5() throws Exception {
        test("'abcdef'.match(new RegExp('[^\\\\[\\\\b\\\\]]+'))");
    }

    /**
     * Tests 'abcdef'.match(/[^\[\b\]]+/).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("abcdef")
    public void test6() throws Exception {
        test("'abcdef'.match(/[^\\[\\b\\]]+/)");
    }

    private void test(final String script) throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  alert(" + script + ");\n"
            + "</script></head><body>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }
}
