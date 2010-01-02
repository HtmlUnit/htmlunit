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
package com.gargoylesoftware.htmlunit.javascript;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;

/**
 * Function is a native JavaScript object and therefore provided by Rhino but some tests are needed here
 * to be sure that we have the expected results (for instance "bind" is an EcmaScript 5 method that is not
 * available in FF2 or FF3).
 *
 * @version $Revision$
 * @author Marc Guillemot
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class NativeFunctionTest extends WebDriverTestCase {

    /**
     * Test for the methods with the same expectations for all browsers.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "apply: function", "arguments: object", "bind: undefined", "call: function", "constructor: function",
            "toString: function" })
    public void methods_common() throws Exception {
        final String[] methods = {"apply", "arguments", "bind", "call", "constructor", "toString"};
        final String html = NativeDateTest.createHTMLTestMethods("function() {}", methods);
        loadPageWithAlerts2(html);
    }

    /**
     * Test for the methods with the different expectations depending on the browsers.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(FF = "toSource: function", IE = "toSource: undefined")
    public void methods_different() throws Exception {
        final String html = NativeDateTest.createHTMLTestMethods("function() {}", "toSource");
        loadPageWithAlerts2(html);
    }
}
