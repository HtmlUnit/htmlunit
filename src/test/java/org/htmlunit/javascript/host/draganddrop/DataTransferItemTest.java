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
import org.htmlunit.html.HtmlPageTest;
import org.htmlunit.junit.BrowserRunner;
import org.htmlunit.junit.annotation.Alerts;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Tests for {@link DataTransferItem}.
 *
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class DataTransferItemTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"string", "null", "string", "text/html", "string", "undefined",
             "string", "something really special", "string", "text/plain",
             "string", "text/xml",
             "HtmlUnit", "HtmlUnit", "HtmlUnit", "HtmlUnit", "1234", "[object Object]"})
    public void string() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    let dt = new DataTransfer();\n"
            + "    let i1 = dt.items.add('HtmlUnit', null);\n"
            + "    log(i1.kind);\n"
            + "    log(i1.type);\n"
            + "    i1.getAsString((s) => log(s));\n"

            + "    let i2 = dt.items.add('HtmlUnit', 'text/html');\n"
            + "    log(i2.kind);\n"
            + "    log(i2.type);\n"
            + "    i2.getAsString((s) => log(s));\n"

            + "    let i3 = dt.items.add('HtmlUnit', undefined);\n"
            + "    log(i3.kind);\n"
            + "    log(i3.type);\n"
            + "    i3.getAsString((s) => log(s));\n"

            + "    let i4 = dt.items.add('HtmlUnit', 'something really special');\n"
            + "    log(i4.kind);\n"
            + "    log(i4.type);\n"
            + "    i4.getAsString((s) => log(s));\n"

            + "    let i5 = dt.items.add(1234, 'text/plain');\n"
            + "    log(i5.kind);\n"
            + "    log(i5.type);\n"
            + "    i5.getAsString((s) => log(s));\n"

            + "    let i6 = dt.items.add({ab: 17}, 'text/xml');\n"
            + "    log(i6.kind);\n"
            + "    log(i6.type);\n"
            + "    i6.getAsString((s) => log(s));\n"
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
    @Alerts({"file", "", "file", "text/html", "exception"})
    public void file() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    let dt = new DataTransfer();\n"
            + "    let file = new File(['Html', 'Unit'], 'htMluniT.txt');\n"
            + "    let i1 = dt.items.add(file);\n"
            + "    log(i1.kind);\n"
            + "    log(i1.type);\n"

            + "    file = new File(['Html', 'Unit'], 'htMluniT.txt', { type: 'text/html' });\n"
            + "    let i2 = dt.items.add(file);\n"
            + "    log(i2.kind);\n"
            + "    log(i2.type);\n"

            + "    try {"
            + "      dt.items.add(undefined);\n"
            + "    } catch(e) { log('exception'); }\n"
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
    @Alerts({"[object File]", "true", "null"})
    public void getAsFile() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    let dt = new DataTransfer();\n"
            + "    let file = new File(['Html', 'Unit'], 'htMluniT.txt');\n"
            + "    let i1 = dt.items.add(file);\n"
            + "    let f1 = i1.getAsFile();\n"
            + "    log(f1);\n"
            + "    log(f1 === file);\n"

            + "    let i2 = dt.items.add('HtmlUnit', 'text/html');\n"
            + "    let f2 = i2.getAsFile();\n"
            + "    log(f2);\n"

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
    @Alerts({"done", "HtmlUnit"})
    public void getAsString() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    let dt = new DataTransfer();\n"
            + "    let i1 = dt.items.add('HtmlUnit', 'text/html');\n"
            + "    i1.getAsString((s) => log(s));\n"

            + "    let file = new File(['Html', 'Unit'], 'htMluniT.txt');\n"
            + "    let i2 = dt.items.add(file);\n"
            + "    i2.getAsString((s) => log(s));\n"
            + "    log('done');\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }
}
