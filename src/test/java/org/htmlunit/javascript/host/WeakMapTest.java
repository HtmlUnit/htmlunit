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
 * Tests for WeakMap.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
public class WeakMapTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"true", "one"})
    public void constructorArray() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  var obj = {};\n"
            + "  var foo = {};\n"
            + "  var myMap = new WeakMap([[ obj, 'one' ],[ foo, 'two' ]]);\n"
            + "  log(myMap.has(foo));\n"
            + "  log(myMap.get(obj));\n"
            + "}\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("TypeError")
    public void constructorSetParam() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  try {\n"
            + "    var myMap = new WeakMap(new Set('test'));\n"
            + "    log(myMap.has('test'));\n"
            + "  } catch(e) {\n"
            + "    logEx(e);\n"
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
    @Alerts("true")
    public void constructorMapParam() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  var obj = {};\n"
            + "  var foo = {};\n"
            + "  var testMap = new Map([[ obj, 'one' ],[ foo, 'two' ]]);\n"
            + "  try {\n"
            + "    var myMap = new WeakMap(testMap);\n"
            + "    log(myMap.has(foo));\n"
            + "  } catch(e) {\n"
            + "    logEx(e);\n"
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
    @Alerts("true")
    public void constructorIteratorParam() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
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
            + "      log(myMap.has(foo));\n"
            + "    } catch(e) {\n"
            + "      logEx(e);\n"
            + "    }\n"
            + "  }"
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
    @Alerts({"undefined", "value2"})
    public void get() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var kvArray = [[{}, 'value1'], [window, 'value2']];\n"
            + "    var myMap = new WeakMap(kvArray);\n"
            + "    log(myMap.size);\n"
            + "    log(myMap.get(window));\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("TypeError")
    public void setNonObject() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    if (window.WeakMap) {\n"
            + "      var kvArray = [[{}, 'value1'], [window, 'value2']];\n"
            + "      var myMap = new WeakMap(kvArray);\n"
            + "      try {\n"
            + "        myMap.set(1, 2);\n"
            + "      } catch(e) { logEx(e) }\n"
            + "    }\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }
}
