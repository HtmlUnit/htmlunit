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
 * Number is a native JavaScript object and therefore provided by Rhino but some tests are needed here
 * to be sure that we have the expected results (for instance EcmaScript 5 adds methods that are not
 * available in FF2 or FF3).
 *
 * @version $Revision$
 * @author Marc Guillemot
 */
@RunWith(BrowserRunner.class)
public class NativeNumberTest extends WebDriverTestCase {

    /**
     * Test for the methods with the same expectations for all browsers.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "toFixed: function", "toExponential: function", "toLocaleString: function", "toPrecision: function",
        "toString: function", "valueOf: function" })
    public void methods_common() throws Exception {
        final String[] methods = {"toFixed", "toExponential", "toLocaleString", "toPrecision", "toString", "valueOf"};
        final String html = NativeDateTest.createHTMLTestMethods("new Number()", methods);
        loadPageWithAlerts2(html);
    }

    /**
     * Test for the methods with the different expectations depending on the browsers.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(FF = "toSource: function", IE = "toSource: undefined")
    public void methods_different() throws Exception {
        final String html = NativeDateTest.createHTMLTestMethods("new Number()", "toSource");
        loadPageWithAlerts2(html);
    }

    /**
     * Test for Rhino bug 538172.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("2.274341322658976e-309")
    public void toStringRhinoBug538172() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "alert(2.274341322658976E-309);\n"
            + "</script></head><body>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }
}
