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
package org.htmlunit.javascript.host.html;

import org.htmlunit.WebDriverTestCase;
import org.htmlunit.junit.annotation.Alerts;
import org.htmlunit.util.MimeType;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link HTMLObjectElement}.
 *
 * @author Daniel Gredler
 * @author Ahmed Ashour
 * @author Ronald Brill
 * @author Frank Danek
 */
public class HTMLObjectElement2Test extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[object HTMLFormElement]", "null"})
    public void form() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<body>\n"
            + "  <form>\n"
            + "    <object id='o1'></object>\n"
            + "</form>\n"
            + "<object id='o2'></object>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  log(document.getElementById('o1').form);\n"
            + "  log(document.getElementById('o2').form);\n"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * There was an error that this code throws an illegal state ex.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("[object XMLDocument]")
    public void responseXML_htmlObject() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var xhr = new XMLHttpRequest();\n"
            + "    xhr.open('GET', 'foo.xml', false);\n"
            + "    xhr.send('');\n"
            + "    try {\n"
            + "      log(xhr.responseXML);\n"
            + "    } catch(e) { logEx(e); }\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        final String xml = "<html xmlns='http://www.w3.org/1999/xhtml'>\n"
                    + "<object classid='CLSID:test'/>\n"
                    + "</html>";

        getMockWebConnection().setDefaultResponse(xml, MimeType.TEXT_XML);
        loadPage2(html);
        verifyTitle2(DEFAULT_WAIT_TIME, getWebDriver(), getExpectedAlerts());
    }
}
