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
 * Tests for {@link HtmlSvg}.
 *
 * @author Ahmed Ashour
 * @author Frank Danek
 */
public class HtmlSvgTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("[object SVGSVGElement]")
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
            + "  <svg xmlns='http://www.w3.org/2000/svg' id='myId' version='1.1'>\n"
            + "  </svg>\n"
            + "</body></html>";

        final WebDriver driver = loadPageVerifyTitle2(html);
        if (driver instanceof HtmlUnitDriver) {
            final HtmlPage page = (HtmlPage) getEnclosedPage();
            assertTrue(HtmlSvg.class.isInstance(page.getElementById("myId")));
        }
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void style() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body>\n"
            + "  <svg xmlns='http://www.w3.org/2000/svg' id='myId' version='1.1'>\n"
            + "  </svg>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "    log(document.getElementById('myId').style == undefined);\n"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"function", "function"})
    public void functions() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body>\n"
            + "  <svg xmlns='http://www.w3.org/2000/svg' id='myId' version='1.1'>\n"
            + "  </svg>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  var svg =  document.getElementById('myId');\n"
            + "  log(typeof svg.getScreenCTM);\n"
            + "  log(typeof svg.createSVGMatrix);\n"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("[object SVGMatrix]")
    public void getScreenCTM() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body>\n"
            + "  <svg xmlns='http://www.w3.org/2000/svg' id='myId' version='1.1'>\n"
            + "  </svg>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  var svg =  document.getElementById('myId');\n"
            + "  try {\n"
            + "    log(svg.getScreenCTM());\n"
            + "  } catch(e) { logEx(e); }\n"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }
}
