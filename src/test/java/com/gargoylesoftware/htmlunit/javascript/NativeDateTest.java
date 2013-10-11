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
package com.gargoylesoftware.htmlunit.javascript;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * Date is a native JavaScript object and therefore provided by Rhino but behavior should be
 * different depending on the simulated browser.
 *
 * @version $Revision$
 * @author Marc Guillemot
 * @author Ahmed Ashour
 * @author Frank Danek
 */
@RunWith(BrowserRunner.class)
public class NativeDateTest extends WebDriverTestCase {

    /**
     * Test for bug <a href="http://sourceforge.net/support/tracker.php?aid=2615817">2615817</a>.
     * @see <a href="http://www.w3schools.com/jsref/jsref_getYear.asp">this doc</a>
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "-13", "84", "109" },
            IE6 = { "1887", "84", "2009" },
            IE8 = { "1887", "84", "2009" })
    public void getYear() throws Exception {
        final String html
            = "<html><head><title>foo</title><script>\n"
            + "function doTest() {\n"
            + "    alert(new Date(1887, 2, 1).getYear());\n"
            + "    alert(new Date(1984, 2, 1).getYear());\n"
            + "    alert(new Date(2009, 2, 1).getYear());\n"
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
    @Alerts({ "constructor: function", "getDate: function", "getDay: function", "getFullYear: function",
            "getHours: function", "getMilliseconds: function", "getMinutes: function", "getMonth: function",
            "getSeconds: function", "getTime: function", "getTimezoneOffset: function", "getUTCDate: function",
            "getUTCDay: function", "getUTCFullYear: function", "getUTCHours: function", "getUTCMilliseconds: function",
            "getUTCMinutes: function", "getUTCMonth: function", "getUTCSeconds: function", "getYear: function",
            "now: undefined", "parse: undefined", "setDate: function", "setFullYear: function", "setHours: function",
            "setMilliseconds: function", "setMinutes: function", "setMonth: function", "setSeconds: function",
            "setTime: function", "setUTCDate: function", "setUTCFullYear: function", "setUTCHours: function",
            "setUTCMilliseconds: function", "setUTCMinutes: function", "setUTCMonth: function",
            "setUTCSeconds: function", "setYear: function", "toDateString: function",
            "toLocaleDateString: function", "toLocaleString: function",
            "toLocaleTimeString: function", "toString: function", "toTimeString: function",
            "toUTCString: function", "valueOf: function", "UTC: undefined" })
    public void methods_common() throws Exception {
        final String[] methods = {"constructor", "getDate", "getDay", "getFullYear", "getHours", "getMilliseconds",
            "getMinutes", "getMonth", "getSeconds", "getTime", "getTimezoneOffset", "getUTCDate", "getUTCDay",
            "getUTCFullYear", "getUTCHours", "getUTCMilliseconds", "getUTCMinutes", "getUTCMonth", "getUTCSeconds",
            "getYear", "now", "parse", "setDate", "setFullYear", "setHours", "setMilliseconds", "setMinutes",
            "setMonth", "setSeconds", "setTime", "setUTCDate", "setUTCFullYear", "setUTCHours",
            "setUTCMilliseconds", "setUTCMinutes", "setUTCMonth", "setUTCSeconds", "setYear", "toDateString",
            "toLocaleDateString", "toLocaleString", "toLocaleTimeString", "toString",
            "toTimeString", "toUTCString", "valueOf", "UTC"};
        final String html = createHTMLTestMethods("new Date()", methods);
        loadPageWithAlerts2(html);
    }

    /**
     * Test for the methods with different expectations depending on the browsers.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "toSource: undefined",
            FF = "toSource: function")
    public void methods_toSource() throws Exception {
        final String[] methods = {"toSource"};
        final String html = createHTMLTestMethods("new Date()", methods);
        loadPageWithAlerts2(html);
    }

    /**
     * Test for the methods with different expectations depending on the browsers.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"toISOString: function", "toJSON: function" },
            IE6 = {"toISOString: undefined", "toJSON: undefined" },
            IE8 = {"toISOString: undefined", "toJSON: undefined" })
    public void methods_differences() throws Exception {
        final String[] methods = {"toISOString", "toJSON"};
        final String html = createHTMLTestMethods("new Date()", methods);
        loadPageWithAlerts2(html);
    }

    static String createHTMLTestMethods(final String object, final String... methodNames) throws Exception {
        final StringBuilder methodList = new StringBuilder();
        for (final String methodName : methodNames) {
            methodList.append(", '").append(methodName).append("'");
        }
        final String html = "<html><head><title>foo</title><script>\n"
            + "function doTest() {\n"
            + "  var o = " + object + ";\n"
            + "  var props = [" + methodList.substring(2) + "];\n"
            + "  for (var i=0; i<props.length; ++i) {\n"
            + "    var p = props[i];\n"
            + "    alert(p + ': ' + typeof(o[p]));\n"
            + "  }\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "</body></html>";

        return html;
    }

    /**
     * Test for bug <a href="https://sourceforge.net/p/htmlunit/bugs/1467/">1467</a>.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Saturday, January 01, 2000")
    public void toLocaleDateString() throws Exception {
        final String html
            = "<html><head><title>foo</title><script>\n"
            + "function test() {\n"
            + "    alert(new Date(2000, 0, 1).toLocaleDateString());\n"
            + "}\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "2005-12-03T07:14:15.000Z", "2005-07-12T11:04:15.000Z",
                        "2005-07-03T15:14:05.000Z" },
            IE6 = "",
            IE8 = "")
    public void toISOString() throws Exception {
        final String html
            = "<html><head><title>foo</title><script>\n"
            + "function test() {\n"
            + "  if (new Date().toISOString) {\n"
            + "    alert(new Date(2005, 11, 3, 8, 14, 15).toISOString());\n"
            + "    alert(new Date(2005, 6, 12, 13, 4, 15).toISOString());\n"
            + "    alert(new Date(2005, 6, 3, 17, 14, 5).toISOString());\n"
            + "  }\n"
            + "}\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "Sat, 03 Dec 2005 07:14:15 GMT", "Tue, 12 Jul 2005 11:04:15 GMT",
                        "Sun, 03 Jul 2005 15:14:05 GMT" },
            IE = { "Sat, 3 Dec 2005 07:14:15 UTC", "Tue, 12 Jul 2005 11:04:15 UTC",
                    "Sun, 3 Jul 2005 15:14:05 UTC" })
    public void toUTCString() throws Exception {
        final String html
            = "<html><head><title>foo</title><script>\n"
            + "function test() {\n"
            + "    alert(new Date(2005, 11, 3, 8, 14, 15).toUTCString());\n"
            + "    alert(new Date(2005, 6, 12, 13, 4, 15).toUTCString());\n"
            + "    alert(new Date(2005, 6, 3, 17, 14, 5).toUTCString());\n"
            + "}\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "Sat, 03 Dec 2005 07:14:15 GMT", "Tue, 12 Jul 2005 11:04:15 GMT",
                        "Sun, 03 Jul 2005 15:14:05 GMT" },
            IE = { "Sat, 3 Dec 2005 07:14:15 UTC", "Tue, 12 Jul 2005 11:04:15 UTC",
                    "Sun, 3 Jul 2005 15:14:05 UTC" })
    public void toGMTString() throws Exception {
        final String html
            = "<html><head><title>foo</title><script>\n"
            + "function test() {\n"
            + "    alert(new Date(2005, 11, 3, 8, 14, 15).toGMTString());\n"
            + "    alert(new Date(2005, 6, 12, 13, 4, 15).toGMTString());\n"
            + "    alert(new Date(2005, 6, 3, 17, 14, 5).toGMTString());\n"
            + "}\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void enumerable() throws Exception {
        final String html
            = "<html><head><title>foo</title><script>\n"
            + "function test() {\n"
            + "    var date = new Date(2000, 0, 1);\n"
            + "    for (var x in date) {\n"
            + "      alert(x);\n"
            + "    }\n"
            + "}\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "00:00:00", "07:08:09" },
            FF3_6 = { "12:00:00 AM", "07:08:09 AM" },
            FF10 = { "12:00:00 AM", "07:08:09 AM" },
            IE6 = { "12:00:00 AM", "07:08:09 AM" })
    public void toLocaleTimeString() throws Exception {
        final String html
            = "<html><head><title>foo</title><script>\n"
            + "function test() {\n"
            + "    alert(new Date(2000, 0, 1).toLocaleTimeString());\n"
            + "    var date = new Date(2013, 0, 1);\n"
            + "    date.setHours(7);\n"
            + "    date.setMinutes(8);\n"
            + "    date.setSeconds(9);\n"
            + "    alert(date.toLocaleTimeString());\n"
            + "}\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

}
