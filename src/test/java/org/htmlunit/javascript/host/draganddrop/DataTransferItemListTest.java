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
package org.htmlunit.javascript.host.draganddrop;

import org.htmlunit.WebDriverTestCase;
import org.htmlunit.junit.annotation.Alerts;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link DataTransferItemList}.
 *
 * @author Ronald Brill
 */
public class DataTransferItemListTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"0", "1"})
    public void length() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    let dt = new DataTransfer();\n"
            + "    let l1 = dt.items;\n"
            + "    log(l1.length);\n"

            + "    dt.items.add('HtmlUnit', 'text/html');\n"
            + "    log(l1.length);\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"0", "0", "1", "0"})
    public void clear() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    let dt = new DataTransfer();\n"
            + "    let l1 = dt.items;\n"
            + "    log(l1.length);\n"
            + "    l1.clear();\n"
            + "    log(l1.length);\n"

            + "    dt.items.add('HtmlUnit', 'text/html');\n"
            + "    log(l1.length);\n"
            + "    l1.clear();\n"
            + "    log(l1.length);\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"0", "1", "2"})
    public void add() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    let dt = new DataTransfer();\n"
            + "    let l1 = dt.items;\n"
            + "    log(l1.length);\n"

            + "    l1.add('HtmlUnit', 'text/html');\n"
            + "    log(l1.length);\n"

            + "    let file = new File(['Html', 'Unit'], 'htMluniT.txt', { type: 'text/html' });\n"
            + "    l1.add(file);\n"
            + "    log(l1.length);\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"0", "1", "0"})
    public void remove() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    let dt = new DataTransfer();\n"
            + "    let l1 = dt.items;\n"
            + "    log(l1.length);\n"

            + "    l1.add('HtmlUnit', 'text/html');\n"
            + "    log(l1.length);\n"

            + "    l1.remove(0);\n"
            + "    log(l1.length);\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"0", "1", "1", "1"})
    public void removeInvalid() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    let dt = new DataTransfer();\n"
            + "    let l1 = dt.items;\n"
            + "    log(l1.length);\n"

            + "    l1.add('HtmlUnit', 'text/html');\n"
            + "    log(l1.length);\n"

            + "    l1.remove(10);\n"
            + "    log(l1.length);\n"

            + "    l1.remove(-1);\n"
            + "    log(l1.length);\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts({"2", "[object DataTransferItem]", "[object DataTransferItem]"})
    public void indexed() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    let dt = new DataTransfer();\n"
            + "    let items = dt.items;\n"
            + "    items.add('HtmlUnit', 'text/html');\n"
            + "    items.add('1234', 'text/plain');\n"
            + "    log(items.length);\n"
            + "    log(items[0]);\n"
            + "    log(items[1]);\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts({"2", "undefined", "undefined"})
    public void indexedWrong() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    let dt = new DataTransfer();\n"
            + "    let items = dt.items;\n"
            + "    items.add('HtmlUnit', 'text/html');\n"
            + "    items.add('1234', 'text/plain');\n"
            + "    log(items.length);\n"
            + "    log(items[-1]);\n"
            + "    log(items[2]);\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts({"2", "[object DataTransferItem]", "[object DataTransferItem]"})
    public void iterator() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    let dt = new DataTransfer();\n"
            + "    let items = dt.items;\n"
            + "    items.add('HtmlUnit', 'text/html');\n"
            + "    items.add('1234', 'text/plain');\n"
            + "    log(items.length);\n"

            + "    for (var i of items) {\n"
            + "      log(i);\n"
            + "    }\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }
}
