/*
 * Copyright (c) 2002-2026 Gargoyle Software Inc.
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
package org.htmlunit.javascript;

import org.htmlunit.WebDriverTestCase;
import org.htmlunit.junit.annotation.Alerts;
import org.junit.jupiter.api.Test;

/**
 * Tests for Iterator.
 *
 * @author Ronald Brill
 */
public class IteratorTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"function", "function"})
    public void windowIterator() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    log(typeof window.Iterator);"
            + "    log(typeof Iterator);"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"function", "[object String Iterator]", "object",
             "H", "false", "U", "false", "undefined", "true"})
    public void stringIterator() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    let str = 'HU';\n"

            + "    log(typeof str[Symbol.iterator]);\n"
            + "    let iter = str[Symbol.iterator]();\n"
            + "    log(iter);\n"
            + "    log(typeof iter);\n"

            + "    let result = iter.next();\n"
            + "    log(result.value);\n"
            + "    log(result.done);\n"

            + "    result = iter.next();\n"
            + "    log(result.value);\n"
            + "    log(result.done);\n"

            + "    result = iter.next();\n"
            + "    log(result.value);\n"
            + "    log(result.done);\n"
            + "  }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "</body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"function", "[object Map Iterator]", "true",
             "function", "[object Map Iterator]", "object",
             "key", "value", "false", "undefined", "true"})
    public void mapEntriesIterator() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    let map = new Map();\n"
            + "    map.set('key', 'value');\n"

            + "    log(typeof map[Symbol.iterator]);\n"
            + "    log(map[Symbol.iterator]());\n"
            + "    log(map[Symbol.iterator] === map.entries);\n"

            + "    log(typeof map.entries);\n"
            + "    let iter = map.entries();\n"
            + "    log(iter);\n"
            + "    log(typeof iter);\n"

            + "    let result = iter.next();\n"
            + "    log(result.value[0]);\n"
            + "    log(result.value[1]);\n"
            + "    log(result.done);\n"

            + "    result = iter.next();\n"
            + "    log(result.value);\n"
            + "    log(result.done);\n"
            + "  }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "</body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"false", "function", "[object Map Iterator]", "object",
             "key", "false", "undefined", "true"})
    public void mapKeysIterator() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    let map = new Map();\n"
            + "    map.set('key', 'value');\n"

            + "    log(map[Symbol.iterator] === map.keys);\n"

            + "    log(typeof map.keys);\n"
            + "    let iter = map.keys();\n"
            + "    log(iter);\n"
            + "    log(typeof iter);\n"

            + "    let result = iter.next();\n"
            + "    log(result.value);\n"
            + "    log(result.done);\n"

            + "    result = iter.next();\n"
            + "    log(result.value);\n"
            + "    log(result.done);\n"
            + "  }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "</body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"false", "function", "[object Map Iterator]", "object",
             "value", "false", "undefined", "true"})
    public void mapValuesIterator() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    let map = new Map();\n"
            + "    map.set('key', 'value');\n"

            + "    log(map[Symbol.iterator] === map.values);\n"

            + "    log(typeof map.values);\n"
            + "    let iter = map.values();\n"
            + "    log(iter);\n"
            + "    log(typeof iter);\n"

            + "    let result = iter.next();\n"
            + "    log(result.value);\n"
            + "    log(result.done);\n"

            + "    result = iter.next();\n"
            + "    log(result.value);\n"
            + "    log(result.done);\n"
            + "  }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "</body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"function", "[object Set Iterator]", "false",
             "function", "[object Set Iterator]", "object",
             "entry", "entry", "false", "undefined", "true"})
    public void setEntriesIterator() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    let set = new Set(['entry']);\n"

            + "    log(typeof set[Symbol.iterator]);\n"
            + "    log(set[Symbol.iterator]());\n"
            + "    log(set[Symbol.iterator] === set.entries);\n"

            + "    log(typeof set.entries);\n"
            + "    let iter = set.entries();\n"
            + "    log(iter);\n"
            + "    log(typeof iter);\n"

            + "    let result = iter.next();\n"
            + "    log(result.value[0]);\n"
            + "    log(result.value[1]);\n"
            + "    log(result.done);\n"

            + "    result = iter.next();\n"
            + "    log(result.value);\n"
            + "    log(result.done);\n"
            + "  }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "</body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"function", "[object Set Iterator]", "true",
             "function", "[object Set Iterator]", "object",
             "entry", "false", "undefined", "true"})
    public void setKeysIterator() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    let set = new Set(['entry']);\n"

            + "    log(typeof set[Symbol.iterator]);\n"
            + "    log(set[Symbol.iterator]());\n"
            + "    log(set[Symbol.iterator] === set.keys);\n"

            + "    log(typeof set.keys);\n"
            + "    let iter = set.keys();\n"
            + "    log(iter);\n"
            + "    log(typeof iter);\n"

            + "    let result = iter.next();\n"
            + "    log(result.value);\n"
            + "    log(result.done);\n"

            + "    result = iter.next();\n"
            + "    log(result.value);\n"
            + "    log(result.done);\n"
            + "  }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "</body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"function", "[object Set Iterator]", "true",
             "function", "[object Set Iterator]", "object",
             "entry", "false", "undefined", "true"})
    public void setValuesIterator() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    let set = new Set(['entry']);\n"

            + "    log(typeof set[Symbol.iterator]);\n"
            + "    log(set[Symbol.iterator]());\n"
            + "    log(set[Symbol.iterator] === set.values);\n"

            + "    log(typeof set.values);\n"
            + "    let iter = set.values();\n"
            + "    log(iter);\n"
            + "    log(typeof iter);\n"

            + "    let result = iter.next();\n"
            + "    log(result.value);\n"
            + "    log(result.done);\n"

            + "    result = iter.next();\n"
            + "    log(result.value);\n"
            + "    log(result.done);\n"
            + "  }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "</body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }
}
