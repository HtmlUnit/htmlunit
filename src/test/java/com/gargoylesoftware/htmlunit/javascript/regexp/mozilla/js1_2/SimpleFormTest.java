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
 * Tests originally in '/js/src/tests/js1_2/regexp/simple_form.js'.
 *
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class SimpleFormTest extends WebDriverTestCase {

    /**
     * Tests /[0-9]{3}/('23 2 34 678 9 09').
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void test1() throws Exception {
        test("/[0-9]{3}/('23 2 34 678 9 09')");
    }

    /**
     * Tests /3.{4}8/('23 2 34 678 9 09').
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void test2() throws Exception {
        test("/3.{4}8/('23 2 34 678 9 09')");
    }

    /**
     * Tests (/3.{4}8/('23 2 34 678 9 09')).length.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void test3() throws Exception {
        test("(/3.{4}8/('23 2 34 678 9 09')).length");
    }

    /**
     * Tests re('23 2 34 678 9 09').
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void test4() throws Exception {
        test("var re = /[0-9]{3}/", "re('23 2 34 678 9 09')");
    }

    /**
     * Tests re('23 2 34 678 9 09').
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void test5() throws Exception {
        test("var re = /3.{4}8/", "re('23 2 34 678 9 09')");
    }

    /**
     * Tests /3.{4}8/('23 2 34 678 9 09').
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void test6() throws Exception {
        test("/3.{4}8/('23 2 34 678 9 09')");
    }

    /**
     * Tests (re('23 2 34 678 9 09')).length.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void test7() throws Exception {
        test("var re =/3.{4}8/", "(re('23 2 34 678 9 09')).length");
    }

    /**
     * Tests (/3.{4}8/('23 2 34 678 9 09')).length.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void test8() throws Exception {
        test("(/3.{4}8/('23 2 34 678 9 09')).length");
    }

    private void test(final String script) throws Exception {
        test(null, script);
    }

    private void test(final String initialScript, final String script) throws Exception {
        String html = "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "try {\n";
        if (initialScript != null) {
            html += initialScript + ";\n";
        }
        html += "  log(" + script + ");\n"
            + "} catch (e) { log('exception'); }\n"
            + "</script></head><body>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }
}
