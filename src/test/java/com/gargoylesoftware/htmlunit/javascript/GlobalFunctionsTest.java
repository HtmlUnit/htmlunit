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
package com.gargoylesoftware.htmlunit.javascript;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;

/**
 * Test for functions/properties of the global object.
 *
 * @version $Revision$
 * @author Marc Guillemot
 */
@RunWith(BrowserRunner.class)
public class GlobalFunctionsTest extends WebDriverTestCase {

    /**
     * Test for bug <a href="http://sourceforge.net/support/tracker.php?aid=2815674">2815674</a>
     * due to Rhino bug <a href="https://bugzilla.mozilla.org/show_bug.cgi?id=501972">501972</a>
     * and for bug <a href="http://sourceforge.net/support/tracker.php?aid=2903514">2903514</a>
     * due to Rhino bug <a href="https://bugzilla.mozilla.org/show_bug.cgi?id=531436">531436</a>.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "7.89", "7.89" })
    public void parseFloat() throws Exception {
        final String html
            = "<html><head><title>foo</title><script>\n"
            + "function doTest() {\n"
            + "    alert(parseFloat('\\n 7.89 '));\n"
            + "    alert(parseFloat('7.89em'));\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "</body></html>";

        loadPageWithAlerts(html);
    }

    /**
     * Test for the methods with the same expectations for all browsers.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "decodeURI: function", "decodeURIComponent: function", "encodeURI: function",
        "encodeURIComponent: function", "escape: function", "eval: function", "isFinite: function", "isNaN: function",
        "parseFloat: function", "parseInt: function", "unescape: function" })
    public void methods_common() throws Exception {
        final String[] methods = {"decodeURI", "decodeURIComponent", "encodeURI", "encodeURIComponent", "escape",
            "eval", "isFinite", "isNaN", "parseFloat", "parseInt", "unescape"};
        final String html = NativeDateTest.createHTMLTestMethods("this", methods);
        loadPageWithAlerts2(html);
    }

    /**
     * Test for the methods with the different expectations depending on the browsers.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(FF = { "isXMLName: function", "uneval: function" },
            DEFAULT = { "isXMLName: undefined", "uneval: undefined" })
    public void methods_different() throws Exception {
        final String[] methods = {"isXMLName", "uneval"};
        final String html = NativeDateTest.createHTMLTestMethods("this", methods);
        loadPageWithAlerts2(html);
    }
}
