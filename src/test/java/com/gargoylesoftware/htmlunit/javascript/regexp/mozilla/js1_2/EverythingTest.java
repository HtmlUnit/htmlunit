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
 * Tests originally in '/js/src/tests/js1_2/regexp/everything.js'.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class EverythingTest extends WebDriverTestCase {

    /**
     * Tests 'Sally and Fred are sure to come'.match(/^[a-z\s]*\/i).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Sally and Fred are sure to come")
    public void test1() throws Exception {
        test("'Sally and Fred are sure to come'.match(/^[a-z\\s]*/i)");
    }

    /**
     * Tests 'test123W+xyz'.match(new RegExp('^[a-z]*[0-9]+[A-Z]?.(123|xyz)$')).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("test123W+xyz,xyz")
    public void test2() throws Exception {
        test("'test123W+xyz'.match(new RegExp('^[a-z]*[0-9]+[A-Z]?.(123|xyz)$'))");
    }

    /**
     * Tests 'number one 12365 number two 9898'.match(/(\d+)\D+(\d+)/).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("12365 number two 9898,12365,9898")
    public void test3() throws Exception {
        test("'number one 12365 number two 9898'.match(/(\\d+)\\D+(\\d+)/)");
    }

    /**
     * Tests 'See Spot run.'.match(simpleSentence).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("See Spot run.,See Spot run.")
    public void test4() throws Exception {
        final String initialScript = "simpleSentence = /(\\s?[^\\!\\?\\.]+[\\!\\?\\.])+/;";
        test(initialScript, "'See Spot run.'.match(simpleSentence)");
    }

    /**
     * Tests 'I like it. What\'s up? I said NO!'.match(simpleSentence).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("I like it. What's up? I said NO!, I said NO!")
    public void test5() throws Exception {
        final String initialScript = "simpleSentence = /(\\s?[^\\!\\?\\.]+[\\!\\?\\.])+/;";
        test(initialScript, "'I like it. What\\'s up? I said NO!'.match(simpleSentence)");
    }

    /**
     * Tests 'the quick brown fox jumped over the lazy dogs'.match(/((\w+)\s*)+/).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("the quick brown fox jumped over the lazy dogs,dogs,dogs")
    public void test6() throws Exception {
        test("'the quick brown fox jumped over the lazy dogs'.match(/((\\w+)\\s*)+/)");
    }

    private void test(final String script) throws Exception {
        test(null, script);
    }

    private void test(final String initialScript, final String script) throws Exception {
        String html = "<html><head><title>foo</title><script>\n";
        if (initialScript != null) {
            html += initialScript + ";\n";
        }
        html += "  alert(" + script + ");\n"
            + "</script></head><body>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }
}
