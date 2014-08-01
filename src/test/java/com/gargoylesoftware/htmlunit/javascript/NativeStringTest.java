/*
 * Copyright (c) 2002-2014 Gargoyle Software Inc.
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
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * String is a native JavaScript object and therefore provided by Rhino but some tests are needed here.
 *
 * @version $Revision$
 * @author Marc Guillemot
 * @author Ahmed Ashour
 * @author Frank Danek
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

        loadPageWithAlerts2(html);
    }

    /**
     * Test for the methods with the same expectations for all browsers.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "anchor: function", "big: function", "blink: function", "bold: function", "charAt: function",
            "charCodeAt: function", "concat: function", "constructor: function", "equals: undefined",
            "equalsIgnoreCase: undefined", "fixed: function", "fontcolor: function", "fontsize: function",
            "fromCharCode: undefined", "indexOf: function", "italics: function", "lastIndexOf: function",
            "link: function", "localeCompare: function", "match: function", "replace: function", "search: function",
            "slice: function", "small: function", "split: function", "strike: function", "sub: function",
            "substr: function", "substring: function", "sup: function", "toLocaleLowerCase: function",
            "toLocaleUpperCase: function", "toLowerCase: function", "toString: function", "toUpperCase: function",
            "valueOf: function" })
    public void methods_common() throws Exception {
        final String[] methods = {"anchor", "big", "blink", "bold", "charAt", "charCodeAt", "concat", "constructor",
            "equals", "equalsIgnoreCase", "fixed", "fontcolor", "fontsize", "fromCharCode", "indexOf", "italics",
            "lastIndexOf", "link", "localeCompare", "match", "replace", "search", "slice", "small", "split",
            "strike", "sub", "substr", "substring", "sup", "toLocaleLowerCase", "toLocaleUpperCase", "toLowerCase",
            "toString", "toUpperCase", "valueOf"};
        final String html = NativeDateTest.createHTMLTestMethods("'hello'", methods);
        loadPageWithAlerts2(html);
    }

    /**
     * Test for the methods with different expectations depending on the browsers.
     * Function contains is introduced in FF18.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "contains: undefined", "toSource: undefined", "trim: function" },
            FF = { "contains: function", "toSource: function", "trim: function" },
            IE8 = { "contains: undefined", "toSource: undefined", "trim: undefined" })
    public void methods_differences() throws Exception {
        final String[] methods = {"contains", "toSource", "trim" };
        final String html = NativeDateTest.createHTMLTestMethods("'hello'", methods);
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "2",
            IE8 = "")
    public void trim() throws Exception {
        final String html
            = "<!DOCTYPE html>\n"
            + "<html><head><title>foo</title><script>\n"
            + "function doTest() {\n"
            + "    var string = ' hi  ';\n"
            + "    if (''.trim) {\n"
            + "      alert(string.trim().length);\n"
            + "    }\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "3",
            IE = "")
    public void trimRight() throws Exception {
        final String html
            = "<!DOCTYPE html>\n"
            + "<html><head><title>foo</title><script>\n"
            + "function doTest() {\n"
            + "    var string = ' hi  ';\n"
            + "    if (''.trimRight) {\n"
            + "      alert(string.trimRight().length);\n"
            + "    }\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "4",
            IE = "")
    public void trimLeft() throws Exception {
        final String html
            = "<!DOCTYPE html>\n"
            + "<html><head><title>foo</title><script>\n"
            + "function doTest() {\n"
            + "    var string = ' hi  ';\n"
            + "    if (''.trimLeft) {\n"
            + "      alert(string.trimLeft().length);\n"
            + "    }\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "contains not supported",
            FF = { "true", "false", "true", "true", "true", "false", "true", "true", "true", "false",
                        "true", "true", "false", "false" })
    public void contains() throws Exception {
        final String html
            = "<!DOCTYPE html>\n"
            + "<html><head><title>foo</title><script>\n"
            + "function doTest() {\n"
            + "  if ('contains' in String.prototype) {"
            + "    var str = 'To be, or not to be, that is the question.';\n"
            + "    alert(str.contains('To be'));\n"
            + "    alert(str.contains('TO'));\n"
            + "    alert(str.contains(''));\n"
            + "    alert(str.contains(' '));\n"
            + "    alert(str.contains('To be', 0));\n"
            + "    alert(str.contains('TO', 0));\n"
            + "    alert(str.contains(' ', 0));\n"
            + "    alert(str.contains('', 0));\n"
            + "    alert(str.contains('or', 7));\n"
            + "    alert(str.contains('or', 8));\n"

            + "    alert(str.contains('or', -3));\n"
            + "    alert(str.contains('or', 7.9));\n"
            + "    alert(str.contains('or', 8.1));\n"
            + "    alert(str.contains());\n"
            + "  } else {\n"
            + "    alert('contains not supported');\n"
            + "  }\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }
}
