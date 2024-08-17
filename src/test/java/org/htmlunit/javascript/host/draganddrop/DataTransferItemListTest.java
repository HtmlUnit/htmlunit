/*
 * Copyright (c) 2002-2024 Gargoyle Software Inc.
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
import org.htmlunit.html.HtmlPageTest;
import org.htmlunit.junit.BrowserRunner;
import org.htmlunit.junit.BrowserRunner.Alerts;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Tests for {@link DataTransferItemList}.
 *
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class DataTransferItemListTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"0", "1"})
    public void length() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
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
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
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
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
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
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
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
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
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
}
