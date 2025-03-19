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
import org.htmlunit.junit.BrowserRunner;
import org.htmlunit.junit.annotation.Alerts;
import org.htmlunit.junit.annotation.HtmlUnitNYI;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Tests for {@link DataTransfer}.
 *
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class DataTransferTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("[object DataTransfer]")
    public void ctor() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var dt = new DataTransfer();\n"
            + "    log(dt);\n"
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
    @Alerts({"[object FileList]", "0"})
    public void filesEmpty() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var dt = new DataTransfer();\n"
            + "    log(dt.files);\n"
            + "    log(dt.files.length);\n"
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
    @Alerts({"0", "0", "0", "true"})
    public void filesStringAdded() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    let dt = new DataTransfer();\n"
            + "    let f1 = dt.files;\n"
            + "    log(f1.length);\n"

            + "    let i1 = dt.items.add('HtmlUnit', 'text/html');\n"
            + "    log(dt.files.length);\n"
            + "    log(f1.length);\n"

            + "    log(f1 === dt.files);\n"
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
    @Alerts({"0", "1", "1", "true", "true"})
    public void filesFileAdded() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    let dt = new DataTransfer();\n"
            + "    let f1 = dt.files;\n"
            + "    log(f1.length);\n"

            + "    let file = new File(['Html', 'Unit'], 'htMluniT.txt');\n"
            + "    let i1 = dt.items.add(file);\n"
            + "    log(dt.files.length);\n"
            + "    log(f1.length);\n"

            + "    log(f1 === dt.files);\n"
            + "    log(file === dt.files[0]);\n"
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
    @Alerts("true")
    public void filesRequestTwoTimes() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var dt = new DataTransfer();\n"
            + "    let f1 = dt.files;\n"
            + "    log(f1 === dt.files);\n"
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
    @Alerts({"[object DataTransferItemList]", "0"})
    public void itemsEmpty() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var dt = new DataTransfer();\n"
            + "    log(dt.items);\n"
            + "    log(dt.items.length);\n"
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
    @Alerts(DEFAULT = "false",
            FF = "true",
            FF_ESR = "true")
    @HtmlUnitNYI(CHROME = "true", EDGE = "true")
    public void itemsRequestTwoTimes() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var dt = new DataTransfer();\n"
            + "    let i1 = dt.items;\n"
            + "    log(i1 === dt.items);\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }
}
