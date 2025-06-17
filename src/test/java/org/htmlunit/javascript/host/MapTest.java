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

import java.util.Map;

import org.htmlunit.WebDriverTestCase;
import org.htmlunit.junit.annotation.Alerts;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link Map}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
public class MapTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"3", "value1"})
    public void get() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    if (window.Map) {\n"
            + "      var kvArray = [['key1', 'value1'], ['key2', 'value2']];\n"
            + "      var myMap = new Map(kvArray);\n"
            + "      myMap.set(1, 2);\n"
            + "      log(myMap.size);\n"
            + "      log(myMap.get('key1'));\n"
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
    @Alerts({"function entries() { [native code] }",
             "[object Map Iterator]", "0,foo", "1,bar", "[object Object],baz", "undefined"})
    public void iterator() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    if (window.Symbol) {\n"
            + "      var myMap = new Map();\n"
            + "      myMap.set('0', 'foo');\n"
            + "      myMap.set(1, 'bar');\n"
            + "      myMap.set({}, 'baz');\n"
            + "      log(myMap[Symbol.iterator]);\n"
            + "      var iter = myMap[Symbol.iterator]();\n"
            + "      log(iter);\n"
            + "      log(iter.next().value);\n"
            + "      log(iter.next().value);\n"
            + "      log(iter.next().value);\n"
            + "      log(iter.next().value);\n"
            + "    }\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"function entries() { [native code] }",
             "[object Map Iterator]", "0,foo", "1,bar", "[object Object],baz", "undefined"})
    public void entries() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    if (window.Symbol) {\n"
            + "      var myMap = new Map();\n"
            + "      myMap.set('0', 'foo');\n"
            + "      myMap.set(1, 'bar');\n"
            + "      myMap.set({}, 'baz');\n"
            + "      log(myMap.entries);\n"
            + "      var iter = myMap.entries();\n"
            + "      log(iter);\n"
            + "      log(iter.next().value);\n"
            + "      log(iter.next().value);\n"
            + "      log(iter.next().value);\n"
            + "      log(iter.next().value);\n"
            + "    }\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"function values() { [native code] }",
             "[object Map Iterator]", "foo", "bar", "baz", "undefined"})
    public void values() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    if (window.Symbol) {\n"
            + "      var myMap = new Map();\n"
            + "      myMap.set('0', 'foo');\n"
            + "      myMap.set(1, 'bar');\n"
            + "      myMap.set({}, 'baz');\n"
            + "      log(myMap.values);\n"
            + "      var iter = myMap.values();\n"
            + "      log(iter);\n"
            + "      log(iter.next().value);\n"
            + "      log(iter.next().value);\n"
            + "      log(iter.next().value);\n"
            + "      log(iter.next().value);\n"
            + "    }\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"function keys() { [native code] }",
             "[object Map Iterator]", "0", "1", "[object Object]", "undefined"})
    public void keys() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    if (window.Symbol) {\n"
            + "      var myMap = new Map();\n"
            + "      myMap.set('0', 'foo');\n"
            + "      myMap.set(1, 'bar');\n"
            + "      myMap.set({}, 'baz');\n"
            + "      log(myMap.keys);\n"
            + "      var iter = myMap.keys();\n"
            + "      log(iter);\n"
            + "      log(iter.next().value);\n"
            + "      log(iter.next().value);\n"
            + "      log(iter.next().value);\n"
            + "      log(iter.next().value);\n"
            + "    }\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("2")
    public void constructorArray() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  var myMap = new Map([[ 1, 'one' ],[ 2, 'two' ]]);\n"
            + "  log(myMap.size);\n"
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
    public void constructorInt32Array() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  var array = new Int32Array([2, 7]);\n"
            + "  try {\n"
            + "    var myMap = new Map(array);\n"
            + "    log(myMap.size);\n"
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
    @Alerts("TypeError")
    public void constructorStringParam() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  try {\n"
            + "    var myMap = new Map('test');\n"
            + "    log(myMap.size);\n"
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
    @Alerts("TypeError")
    public void constructorSetParam() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  try {\n"
            + "    var myMap = new Map(new Set('test'));\n"
            + "    log(myMap.size);\n"
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
    @Alerts("2")
    public void constructorMapParam() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  var kvArray = [['key1', 'value1'], ['key2', 'value2']];\n"
            + "  var testMap = new Map(kvArray);\n"
            + "  var myMap = new Map(testMap);\n"
            + "  log(myMap.size);\n"
            + "}\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"1", "77", "one"})
    public void constructorIteratorParam() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function logElement(value, key) {\n"
            + "  log(key);\n"
            + "  log(value);\n"
            + "}\n"
            + "function test() {\n"
            + "  try {\n"
            + "    var myIterable = {};\n"
            + "    myIterable[Symbol.iterator] = function() {\n"
            + "      return {\n"
            + "        next: function() {\n"
            + "          if (this._first) {;\n"
            + "            this._first = false;\n"
            + "            return { value: [ 77, 'one' ], done: false };\n"
            + "          }\n"
            + "          return { done: true };\n"
            + "        },\n"
            + "        _first: true\n"
            + "      };\n"
            + "    };\n"
            + "    var myMap = new Map(myIterable);\n"
            + "    log(myMap.size);\n"
            + "    myMap.forEach(logElement);\n"
            + "  }catch(e) { logEx(e); }"
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
    @Alerts({"value1", "key1", "[object Map]", "[object Window]",
             "[object Object]", "key2", "[object Map]", "[object Window]",
             "null", "key3", "[object Map]", "[object Window]",
             "undefined", "key4", "[object Map]", "[object Window]"})
    public void forEach() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function logElement(value, key, m) {\n"
            + "  log(value);\n"
            + "  log(key);\n"
            + "  log(m);\n"
            + "  log(this);\n"
            + "}\n"
            + "function test() {\n"
            + "try {"
            + "  var myMap = new Map([['key1', 'value1'], ['key2', {}], ['key3', null], ['key4', undefined]]);\n"
            + "  myMap.forEach(logElement);\n"
             + "}catch(e){log(e)}"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"value1", "key1", "[object Map]", "undefined",
             "[object Object]", "key2", "[object Map]", "undefined",
             "null", "key3", "[object Map]", "undefined",
             "undefined", "key4", "[object Map]", "undefined"})
    public void forEachStrict() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + "'use strict';\n"
            + LOG_TITLE_FUNCTION
            + "function logElement(value, key, m) {\n"
            + "  log(value);\n"
            + "  log(key);\n"
            + "  log(m);\n"
            + "  log(this);\n"
            + "}\n"
            + "function test() {\n"
            + "try {"
            + "  var myMap = new Map([['key1', 'value1'], ['key2', {}], ['key3', null], ['key4', undefined]]);\n"
            + "  myMap.forEach(logElement);\n"
             + "}catch(e){log(e)}"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"value1", "key1", "[object Map]", "hello",
             "[object Object]", "key2", "[object Map]", "hello",
             "null", "key3", "[object Map]", "hello",
             "undefined", "key4", "[object Map]", "hello"})
    public void forEachThis() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function logElement(value, key, m) {\n"
            + "  log(value);\n"
            + "  log(key);\n"
            + "  log(m);\n"
            + "  log(this);\n"
            + "}\n"
            + "function test() {\n"
            + "  var myMap = new Map([['key1', 'value1'], ['key2', {}], ['key3', null], ['key4', undefined]]);\n"
            + "  myMap.forEach(logElement, 'hello');\n"
            + "}\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Test case for Bug #1868.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("[object Map Iterator]")
    public void iteratorPrototype() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    if (window.Symbol) {\n"
            + "      var myMap = new Map();\n"
            + "      var iter = myMap[Symbol.iterator]();\n"
            + "      log(Object.getPrototypeOf(iter));\n"
            + "    }\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"value1", "undefined", "[object Map]", "[object Window]",
             "[object Object]", "key2", "[object Map]", "[object Window]"})
    public void forEach_withElision() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function logElement(value, key, m) {\n"
            + "  log(value);\n"
            + "  log(key);\n"
            + "  log(m);\n"
            + "  log(this);\n"
            + "}\n"
            + "function test() {\n"
            + "try {"
            + "  var myMap = new Map([[, 'value1'], ['key2', {}]]);\n"
            + "  myMap.forEach(logElement);\n"
             + "}catch(e){log(e)}"
            + "}\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"0", "0"})
    public void setSize() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  var map = new Map();\n"
            + "  try {\n"
            + "    log(map.size);\n"
            + "    map.size = 100;\n"
            + "    log(map.size);\n"
            + "  } catch(e) { log(e); }\n"
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
    @Alerts({"0", "Type error"})
    public void setSizeStrictMode() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  'use strict';\n"
            + "  var map = new Map();\n"
            + "  try {\n"
            + "    log(map.size);\n"
            + "    map.size = 100;\n"
            + "    log(map.size);\n"
            + "  } catch(e) { log('Type error'); }\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

}
