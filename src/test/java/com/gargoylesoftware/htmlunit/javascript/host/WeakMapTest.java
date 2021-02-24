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
package com.gargoylesoftware.htmlunit.javascript.host;

import static com.gargoylesoftware.htmlunit.BrowserRunner.TestedBrowser.IE;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * Tests for WeakMap.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class WeakMapTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"true", "one"},
            IE = {"false", "undefined"})
    @NotYetImplemented(IE)
    public void constructorArray() throws Exception {
        final String html
            = "<html><head><title>foo</title><script>\n"
            + "function test() {\n"
            + "  var obj = {};\n"
            + "  var foo = {};\n"
            + "  var myMap = new WeakMap([[ obj, 'one' ],[ foo, 'two' ]]);\n"
            + "  alert(myMap.has(foo));\n"
            + "  alert(myMap.get(obj));\n"
            + "}\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            IE = "false")
    @NotYetImplemented(IE)
    public void constructorSetParam() throws Exception {
        final String html
            = "<html><head><title>foo</title><script>\n"
            + "function test() {\n"
            + "  try {\n"
            + "    var myMap = new WeakMap(new Set('test'));\n"
            + "    alert(myMap.has('test'));\n"
            + "  } catch(e) {\n"
            + "    alert('exception');\n"
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
    @Alerts(DEFAULT = "true",
            IE = "false")
    @NotYetImplemented(IE)
    public void constructorMapParam() throws Exception {
        final String html
            = "<html><head><title>foo</title><script>\n"
            + "function test() {\n"
            + "  var obj = {};\n"
            + "  var foo = {};\n"
            + "  var testMap = new Map([[ obj, 'one' ],[ foo, 'two' ]]);\n"
            + "  try {\n"
            + "    var myMap = new WeakMap(testMap);\n"
            + "    alert(myMap.has(foo));\n"
            + "  } catch(e) {\n"
            + "    alert('exception');\n"
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
    @Alerts(DEFAULT = "true",
            IE = {})
    public void constructorIteratorParam() throws Exception {
        final String html
            = "<html><head><title>foo</title><script>\n"
            + "function test() {\n"
            + "  if (window.WeakSet) {\n"
            + "    var obj = {};\n"
            + "    var foo = {};\n"
            + "    var myIterable = {};\n"
            + "    myIterable[Symbol.iterator] = function() {\n"
            + "      return {\n"
            + "        next: function() {\n"
            + "          if (this._first) {;\n"
            + "            this._first = false;\n"
            + "            return { value: [ foo, 'one' ], done: false };\n"
            + "          }\n"
            + "          return { done: true };\n"
            + "        },\n"
            + "        _first: true\n"
            + "      };\n"
            + "    };\n"
            + "    try {\n"
            + "      var myMap = new WeakMap(myIterable);\n"
            + "      alert(myMap.has(foo));\n"
            + "    } catch(e) {\n"
            + "      alert('exception');\n"
            + "    }\n"
            + "  }"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"undefined", "value2"},
            IE = {"undefined", "undefined"})
    @NotYetImplemented(IE)
    public void get() throws Exception {
        final String html = "<html><head><script>\n"
            + "  function test() {\n"
            + "    var kvArray = [[{}, 'value1'], [window, 'value2']];\n"
            + "    var myMap = new WeakMap(kvArray);\n"
            + "    alert(myMap.size);\n"
            + "    alert(myMap.get(window));\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void setNonObject() throws Exception {
        final String html = "<html><head><script>\n"
            + "  function test() {\n"
            + "    if (window.WeakMap) {\n"
            + "      var kvArray = [[{}, 'value1'], [window, 'value2']];\n"
            + "      var myMap = new WeakMap(kvArray);\n"
            + "      try {\n"
            + "        myMap.set(1, 2);\n"
            + "      } catch(e) {alert('exception')}\n"
            + "    }\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }
}
