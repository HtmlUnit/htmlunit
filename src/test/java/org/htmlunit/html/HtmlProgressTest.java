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
package org.htmlunit.html;

import org.htmlunit.WebDriverTestCase;
import org.htmlunit.junit.annotation.Alerts;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

/**
 * Tests for {@link HtmlProgress}.
 *
 * @author Ahmed Ashour
 * @author Frank Danek
 */
public class HtmlProgressTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("[object HTMLProgressElement]")
    public void simpleScriptable() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    log(document.getElementById('myId'));\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "  <progress id='myId'></progress>\n"
            + "</body></html>";

        final WebDriver driver = loadPageVerifyTitle2(html);
        if (driver instanceof HtmlUnitDriver) {
            final HtmlPage page = (HtmlPage) getEnclosedPage();
            if ("[object HTMLProgressElement]".equals(getExpectedAlerts()[0])) {
                assertTrue(page.getHtmlElementById("myId") instanceof HtmlProgress);
            }
            else {
                assertTrue(page.getHtmlElementById("myId") instanceof HtmlUnknownElement);
            }
        }
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"number70", "number100"})
    public void properties() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body>\n"
            + "<progress id='it' value='70' max='100'>70%</progress>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "var elt = document.getElementById('it');\n"
            + "if (window.HTMLProgressElement) {\n"
            + "  log(typeof(elt.value) + elt.value);\n"
            + "  log(typeof(elt.max) + elt.max);\n"
            + "}\n"
            + "</script></body></html>";
        loadPageVerifyTitle2(html);
    }
}
