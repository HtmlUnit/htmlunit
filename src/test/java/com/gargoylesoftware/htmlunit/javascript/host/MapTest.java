/*
 * Copyright (c) 2002-2016 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript.host;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.html.HtmlPageTest;

/**
 * Tests for {@link Map}.
 *
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class MapTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "3", "value1" },
            IE = { "1", "undefined" })
    public void get() throws Exception {
        final String html = "<html><head><script>\n"
            + "  function test() {\n"
            + "    if (window.Map) {\n"
            + "      var kvArray = [['key1', 'value1'], ['key2', 'value2']];\n"
            + "      var myMap = new Map(kvArray);\n"
            + "      myMap.set(1, 2);\n"
            + "      alert(myMap.size);\n"
            + "      alert(myMap.get('key1'));\n"
            + "    }\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "function entries() { [native code] }",
                "[object Map Iterator]", "0,foo", "1,bar", "[object Object],baz", "undefined" },
            FF = { "function entries() {\n    [native code]\n}",
                "[object Map Iterator]", "0,foo", "1,bar", "[object Object],baz", "undefined" },
            IE = { })
    public void iterator() throws Exception {
        final String html =
            HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    if (window.Symbol) {\n"
            + "      var myMap = new Map();\n"
            + "      myMap.set('0', 'foo');\n"
            + "      myMap.set(1, 'bar');\n"
            + "      myMap.set({}, 'baz');\n"
            + "      alert(myMap[Symbol.iterator]);\n"
            + "      var iter = myMap[Symbol.iterator]();\n"
            + "      alert(iter);\n"
            + "      alert(iter.next().value);\n"
            + "      alert(iter.next().value);\n"
            + "      alert(iter.next().value);\n"
            + "      alert(iter.next().value);\n"
            + "    }\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "function entries() { [native code] }",
                "[object Map Iterator]", "0,foo", "1,bar", "[object Object],baz", "undefined" },
            FF = { "function entries() {\n    [native code]\n}",
                "[object Map Iterator]", "0,foo", "1,bar", "[object Object],baz", "undefined" },
            IE = { })
    public void entries() throws Exception {
        final String html =
            HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    if (window.Symbol) {\n"
            + "      var myMap = new Map();\n"
            + "      myMap.set('0', 'foo');\n"
            + "      myMap.set(1, 'bar');\n"
            + "      myMap.set({}, 'baz');\n"
            + "      alert(myMap.entries);\n"
            + "      var iter = myMap.entries();\n"
            + "      alert(iter);\n"
            + "      alert(iter.next().value);\n"
            + "      alert(iter.next().value);\n"
            + "      alert(iter.next().value);\n"
            + "      alert(iter.next().value);\n"
            + "    }\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "function values() { [native code] }",
                "[object Map Iterator]", "foo", "bar", "baz", "undefined" },
            FF = { "function values() {\n    [native code]\n}",
                "[object Map Iterator]", "foo", "bar", "baz", "undefined" },
            IE = { })
    public void values() throws Exception {
        final String html =
            HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    if (window.Symbol) {\n"
            + "      var myMap = new Map();\n"
            + "      myMap.set('0', 'foo');\n"
            + "      myMap.set(1, 'bar');\n"
            + "      myMap.set({}, 'baz');\n"
            + "      alert(myMap.values);\n"
            + "      var iter = myMap.values();\n"
            + "      alert(iter);\n"
            + "      alert(iter.next().value);\n"
            + "      alert(iter.next().value);\n"
            + "      alert(iter.next().value);\n"
            + "      alert(iter.next().value);\n"
            + "    }\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "function keys() { [native code] }",
                "[object Map Iterator]", "0", "1", "[object Object]", "undefined" },
            FF = { "function keys() {\n    [native code]\n}",
                "[object Map Iterator]", "0", "1", "[object Object]", "undefined" },
            IE = { })
    public void keys() throws Exception {
        final String html =
            HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    if (window.Symbol) {\n"
            + "      var myMap = new Map();\n"
            + "      myMap.set('0', 'foo');\n"
            + "      myMap.set(1, 'bar');\n"
            + "      myMap.set({}, 'baz');\n"
            + "      alert(myMap.keys);\n"
            + "      var iter = myMap.keys();\n"
            + "      alert(iter);\n"
            + "      alert(iter.next().value);\n"
            + "      alert(iter.next().value);\n"
            + "      alert(iter.next().value);\n"
            + "      alert(iter.next().value);\n"
            + "    }\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }
}
