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

import java.util.Set;

import org.htmlunit.WebDriverTestCase;
import org.htmlunit.junit.annotation.Alerts;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link Set}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
public class SetTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"3", "true"})
    public void has() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var myArray = ['value1', 'value2', 'value3'];\n"
            + "    var mySet = new Set(myArray);\n"
            + "    mySet.add('value1');\n"
            + "    log(mySet.size);\n"
            + "    log(mySet.has('value2'));\n"
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
             "[object Set Iterator]", "0", "1", "[object Object]"})
    public void iterator() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    if (window.Symbol) {\n"
            + "      var set = new Set();\n"
            + "      set.add('0');\n"
            + "      set.add(1);\n"
            + "      set.add({});\n"
            + "      log(set[Symbol.iterator]);\n"
            + "      var iter = set[Symbol.iterator]();\n"
            + "      log(iter);\n"
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
             "[object Set Iterator]", "0", "1", "[object Object]"})
    public void values() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    if (window.Symbol) {\n"
            + "      var set = new Set();\n"
            + "      set.add('0');\n"
            + "      set.add(1);\n"
            + "      set.add({});\n"
            + "      log(set.values);\n"
            + "      var iter = set.values();\n"
            + "      log(iter);\n"
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
            + "  var mySet = new Set([2, 7]);\n"
            + "  log(mySet.size);\n"
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
    public void constructorInt32Array() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  var array = new Int32Array([2, 7]);\n"
            + "  var mySet = new Set(array);\n"
            + "  log(mySet.size);\n"
            + "}\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"3", "true", "false"})
    public void constructorStringParam() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  var mySet = new Set('test');\n"
            + "  log(mySet.size);\n"
            + "  log(mySet.has('t'));\n"
            + "  log(mySet.has('x'));\n"
            + "}\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"7", "true", "false"})
    public void constructorStringIteratorParam() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  if (window.Symbol) {\n"
            + "    var strIter = 'HtmlUnit'[Symbol.iterator]();"
            + "    var mySet = new Set(strIter);\n"
            + "    log(mySet.size);\n"
            + "    log(mySet.has('t'));\n"
            + "    log(mySet.has('x'));\n"
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
    @Alerts({"3", "true", "false"})
    public void constructorSetParam() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  var mySet = new Set(new Set('test'));\n"
            + "  log(mySet.size);\n"
            + "  log(mySet.has('t'));\n"
            + "  log(mySet.has('x'));\n"
            + "}\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"2", "false", "false"})
    public void constructorMapParam() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  var kvArray = [['key1', 'value1'], ['key2', 'value2']];\n"
            + "  var myMap = new Map(kvArray);\n"
            + "  var mySet = new Set(myMap);\n"
            + "  log(mySet.size);\n"
            + "  log(mySet.has('key1'));\n"
            + "  log(mySet.has('value2'));\n"
            + "}\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"1", "#77"})
    public void constructorIteratorParam() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function logElement(value) {\n"
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
            + "            return { value: '#77', done: false };\n"
            + "          }\n"
            + "          return { done: true };\n"
            + "        },\n"
            + "        _first: true\n"
            + "      };\n"
            + "    };\n"
            + "    var mySet = new Set(myIterable);\n"
            + "    log(mySet.size);\n"
            + "    mySet.forEach(logElement);\n"
            + "  } catch(e) { logEx(e); }"
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
    @Alerts({"ab", "ab", "[object Set]", "[object Window]",
             "undefined", "undefined", "[object Set]", "[object Window]",
             "null", "null", "[object Set]", "[object Window]"})
    public void forEach() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function logElement(value1, value2, s) {\n"
            + "  log(value1);\n"
            + "  log(value2);\n"
            + "  log(s);\n"
            + "  log(this);\n"
            + "}\n"
            + "function test() {\n"
            + "  var mySet = new Set(['ab', undefined, null]);\n"
            + "  mySet.forEach(logElement);\n"
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
    @Alerts({"ab", "ab", "[object Set]", "undefined",
             "undefined", "undefined", "[object Set]", "undefined",
             "null", "null", "[object Set]", "undefined"})
    public void forEachStrict() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + "'use strict';\n"
            + LOG_TITLE_FUNCTION
            + "function logElement(value1, value2, s) {\n"
            + "  log(value1);\n"
            + "  log(value2);\n"
            + "  log(s);\n"
            + "  log(this);\n"
            + "}\n"
            + "function test() {\n"
            + "  var mySet = new Set(['ab', undefined, null]);\n"
            + "  mySet.forEach(logElement);\n"
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
    @Alerts({"ab", "ab", "[object Set]", "hello", "undefined", "undefined", "[object Set]", "hello",
             "null", "null", "[object Set]", "hello"})
    public void forEachThis() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function logElement(value1, value2, s) {\n"
            + "  log(value1);\n"
            + "  log(value2);\n"
            + "  log(s);\n"
            + "  log(this);\n"
            + "}\n"
            + "function test() {\n"
            + "  var mySet = new Set(['ab', undefined, null]);\n"
            + "  mySet.forEach(logElement, 'hello');\n"
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
    @Alerts("[object Set Iterator]")
    public void iteratorPrototype() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    if (window.Symbol) {\n"
            + "      var mySet = new Set();\n"
            + "      var iter = mySet[Symbol.iterator]();\n"
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
     * Test case for Bug #1886.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"undefined", "0"})
    public void forEach_withElision() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "function logElement(value) {\n"
                + "  log(value);\n"
                + "}\n"
                + "function test() {\n"
                + "  var mySet = new Set([, 0]);\n"
                + "  mySet.forEach(logElement);\n"
                + "}\n"
                + "</script></head><body onload='test()'>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }
}
