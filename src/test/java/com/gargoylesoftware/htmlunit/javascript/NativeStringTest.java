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
 * String is a native JavaScript object and therefore provided by Rhino but some tests are needed here.
 *
 * @version $Revision$
 * @author Marc Guillemot
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class NativeStringTest extends WebDriverTestCase {

    /**
     * Test for bug <a href="http://sourceforge.net/support/tracker.php?aid=2783950">2783950</a>.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("key\\:b_M")
    public void replace() throws Exception {
        final String html
            = "<html><head><title>foo</title><script>\n"
            + "function doTest() {\n"
            + "    alert('key:b_M'.replace(':', '\\\\:'));\n"
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
    @Alerts(IE = { "anchor: function", "big: function", "blink: function", "bold: function", "charAt: function",
            "charCodeAt: function", "concat: function", "constructor: function", "equals: undefined",
            "equalsIgnoreCase: undefined", "fixed: function", "fontcolor: function", "fontsize: function",
            "fromCharCode: undefined", "indexOf: function", "italics: function", "lastIndexOf: function",
            "link: function", "localeCompare: function", "match: function", "replace: function", "search: function",
            "slice: function", "small: function", "split: function", "strike: function", "sub: function",
            "substr: function", "substring: function", "sup: function", "toLocaleLowerCase: function",
            "toLocaleUpperCase: function", "toLowerCase: function", "toString: function", "toUpperCase: function",
            "trim: undefined", "valueOf: function" },
        FF3 = { "anchor: function", "big: function", "blink: function", "bold: function", "charAt: function",
            "charCodeAt: function", "concat: function", "constructor: function", "equals: undefined",
            "equalsIgnoreCase: undefined", "fixed: function", "fontcolor: function", "fontsize: function",
            "fromCharCode: undefined", "indexOf: function", "italics: function", "lastIndexOf: function",
            "link: function", "localeCompare: function", "match: function", "replace: function", "search: function",
            "slice: function", "small: function", "split: function", "strike: function", "sub: function",
            "substr: function", "substring: function", "sup: function", "toLocaleLowerCase: function",
            "toLocaleUpperCase: function", "toLowerCase: function", "toString: function", "toUpperCase: function",
            "trim: undefined", "valueOf: function" },
        FF3_6 = { "anchor: function", "big: function", "blink: function", "bold: function", "charAt: function",
            "charCodeAt: function", "concat: function", "constructor: function", "equals: undefined",
            "equalsIgnoreCase: undefined", "fixed: function", "fontcolor: function", "fontsize: function",
            "fromCharCode: undefined", "indexOf: function", "italics: function", "lastIndexOf: function",
            "link: function", "localeCompare: function", "match: function", "replace: function", "search: function",
            "slice: function", "small: function", "split: function", "strike: function", "sub: function",
            "substr: function", "substring: function", "sup: function", "toLocaleLowerCase: function",
            "toLocaleUpperCase: function", "toLowerCase: function", "toString: function", "toUpperCase: function",
            "trim: function", "valueOf: function" })
    public void methods_common() throws Exception {
        final String[] methods = {"anchor", "big", "blink", "bold", "charAt", "charCodeAt", "concat", "constructor",
            "equals", "equalsIgnoreCase", "fixed", "fontcolor", "fontsize", "fromCharCode", "indexOf", "italics",
            "lastIndexOf", "link", "localeCompare", "match", "replace", "search", "slice", "small", "split",
            "strike", "sub", "substr", "substring", "sup", "toLocaleLowerCase", "toLocaleUpperCase", "toLowerCase",
            "toString", "toUpperCase", "trim", "valueOf"};
        final String html = NativeDateTest.createHTMLTestMethods("'hello'", methods);
        loadPageWithAlerts2(html);
    }

    /**
     * Test for the methods with different expectations depending on the browsers.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(FF = "toSource: function", IE = "toSource: undefined")
    public void methods_differences() throws Exception {
        final String[] methods = {"toSource"};
        final String html = NativeDateTest.createHTMLTestMethods("'hello'", methods);
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE = "", FF3 = "", FF3_6 = { "2", "3", "4" })
    public void trim() throws Exception {
        final String html
            = "<html><head><title>foo</title><script>\n"
            + "function doTest() {\n"
            + "    var string = ' hi  ';\n"
            + "    if (''.trim) {\n"
            + "      alert(string.trim().length);\n"
            + "      alert(string.trimRight().length);\n"
            + "      alert(string.trimLeft().length);\n"
            + "    }\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "</body></html>";

        loadPageWithAlerts(html);
    }

}
