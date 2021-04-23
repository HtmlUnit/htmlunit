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

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * Tests for WeakSet.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class WeakSetTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = {})
    public void constructorArray() throws Exception {
        final String html
            = "<html><head><title>foo</title><script>\n"
            + "function test() {\n"
            + "  if (window.WeakSet) {\n"
            + "    var obj = {};\n"
            + "    var foo = {};\n"
            + "    var mySet = new WeakSet([obj, foo]);\n"
            + "    alert(mySet.has(foo));\n"
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
    @Alerts(DEFAULT = { "false", "true"},
            IE = {})
    public void constructorSetParam() throws Exception {
        final String html
            = "<html><head><title>foo</title><script>\n"
            + "function test() {\n"
            + "  if (window.WeakSet) {\n"
            + "    var obj = {};\n"
            + "    var foo = {};\n"
            + "    var mySet = new WeakSet(new Set([foo]));\n"
            + "    alert(mySet.has(obj));\n"
            + "    alert(mySet.has(foo));\n"
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
    @Alerts(DEFAULT = {"false", "false", "false"},
            IE = {})
    public void constructorMapParam() throws Exception {
        final String html
            = "<html><head><title>foo</title><script>\n"
            + "function test() {\n"
            + "  if (window.WeakSet) {\n"
            + "    var obj = {};\n"
            + "    var foo = {};\n"
            + "    var kvArray = [['key1', obj], ['key2', foo]];\n"
            + "    var myMap = new Map(kvArray);\n"
            + "    var mySet = new WeakSet(myMap);\n"
            + "    alert(mySet.has('key1'));\n"
            + "    alert(mySet.has(obj));\n"
            + "    alert(mySet.has(kvArray[1]));\n"
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
    @Alerts(DEFAULT = {"true", "false"},
            IE = {})
    public void constructorIteratorParam() throws Exception {
        final String html
            = "<html><head><title>foo</title><script>\n"
            + "function logElement(value) {\n"
            + "  alert(value);\n"
            + "}\n"
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
            + "            return { value: foo, done: false };\n"
            + "          }\n"
            + "          return { done: true };\n"
            + "        },\n"
            + "        _first: true\n"
            + "      };\n"
            + "    };\n"
            + "    var mySet = new WeakSet(myIterable);\n"
            + "    alert(mySet.has(foo));\n"
            + "    alert(mySet.has(obj));\n"
            + "  }\n"
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
    @Alerts(DEFAULT = {"undefined", "true"},
            IE = {})
    public void has() throws Exception {
        final String html = "<html><head><script>\n"
            + "  function test() {\n"
            + "    if (window.WeakSet) {\n"
            + "      var obj = {};\n"
            + "      var foo = {};\n"
            + "      var myArray = [obj, foo];\n"
            + "      var mySet = new WeakSet(myArray);\n"
            + "      mySet.add(window);\n"
            + "      alert(mySet.size);\n"
            + "      alert(mySet.has(window));\n"
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
    @Alerts(DEFAULT = {"Type error", "Type error", "true"},
            IE = {})
    public void add() throws Exception {
        final String html = "<html><head><script>\n"
            + "  function test() {\n"
            + "    if (window.WeakSet) {\n"
            + "      var mySet = new WeakSet();\n"

            + "      try {\n"
            + "        mySet.add(7);\n"
            + "      } catch(e) { alert('Type error'); }\n"

            + "      try {\n"
            + "        mySet.add('seven');\n"
            + "      } catch(e) { alert('Type error'); }\n"

            + "      var foo = {};\n"
            + "      mySet.add(foo);\n"
            + "      alert(mySet.has(foo));\n"
            + "    }\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }
}
