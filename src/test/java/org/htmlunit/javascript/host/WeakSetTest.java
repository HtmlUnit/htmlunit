/*
 * Copyright (c) 2002-2025 Gargoyle Software Inc.
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
package org.htmlunit.javascript.host;

import org.htmlunit.WebDriverTestCase;
import org.htmlunit.junit.annotation.Alerts;
import org.junit.jupiter.api.Test;

/**
 * Tests for WeakSet.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
public class WeakSetTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void constructorArray() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  if (window.WeakSet) {\n"
            + "    var obj = {};\n"
            + "    var foo = {};\n"
            + "    var mySet = new WeakSet([obj, foo]);\n"
            + "    log(mySet.has(foo));\n"
            + "  }\n"
            + "}\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "false", "true"})
    public void constructorSetParam() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  if (window.WeakSet) {\n"
            + "    var obj = {};\n"
            + "    var foo = {};\n"
            + "    var mySet = new WeakSet(new Set([foo]));\n"
            + "    log(mySet.has(obj));\n"
            + "    log(mySet.has(foo));\n"
            + "  }\n"
            + "}\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"false", "false", "false"})
    public void constructorMapParam() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  if (window.WeakSet) {\n"
            + "    var obj = {};\n"
            + "    var foo = {};\n"
            + "    var kvArray = [['key1', obj], ['key2', foo]];\n"
            + "    var myMap = new Map(kvArray);\n"
            + "    var mySet = new WeakSet(myMap);\n"
            + "    log(mySet.has('key1'));\n"
            + "    log(mySet.has(obj));\n"
            + "    log(mySet.has(kvArray[1]));\n"
            + "  }\n"
            + "}\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"true", "false"})
    public void constructorIteratorParam() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function logElement(value) {\n"
            + "  log(value);\n"
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
            + "    log(mySet.has(foo));\n"
            + "    log(mySet.has(obj));\n"
            + "  }\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"undefined", "true"})
    public void has() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    if (window.WeakSet) {\n"
            + "      var obj = {};\n"
            + "      var foo = {};\n"
            + "      var myArray = [obj, foo];\n"
            + "      var mySet = new WeakSet(myArray);\n"
            + "      mySet.add(window);\n"
            + "      log(mySet.size);\n"
            + "      log(mySet.has(window));\n"
            + "    }\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"Type error", "Type error", "true"})
    public void add() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    if (window.WeakSet) {\n"
            + "      var mySet = new WeakSet();\n"

            + "      try {\n"
            + "        mySet.add(7);\n"
            + "      } catch(e) { log('Type error'); }\n"

            + "      try {\n"
            + "        mySet.add('seven');\n"
            + "      } catch(e) { log('Type error'); }\n"

            + "      var foo = {};\n"
            + "      mySet.add(foo);\n"
            + "      log(mySet.has(foo));\n"
            + "    }\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }
}
