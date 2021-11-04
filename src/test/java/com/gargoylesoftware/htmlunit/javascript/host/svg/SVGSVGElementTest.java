/*
 * Copyright (c) 2002-2021 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript.host.svg;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.html.HtmlPageTest;

/**
 * Tests for {@link SVGSVGElement}.
 *
 * @author Ahmed Ashour
 * @author Frank Danek
 * @author Ronald Brill
 * @author Natasha Lazarova
 */
@RunWith(BrowserRunner.class)
public class SVGSVGElementTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("[object SVGRect]")
    public void createSVGRect() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    log(document.createElementNS('http://www.w3.org/2000/svg', 'svg').createSVGRect());\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("undefined")
    public void getInnerTextOfSvg() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
                + "<html><body>\n"
                + "  <svg xmlns='http://www.w3.org/2000/svg' id='myId' version='1.1'></svg>\n"
                + "  <script>\n"
                + LOG_TITLE_FUNCTION
                + "    var svg =  document.getElementById('myId');\n"
                + "    log(svg.innerText);\n"
                + "  </script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void getInnerTextOfElementContainingSvg() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
                + "<html><body>\n"
                + "  <div id='myDivId'><svg xmlns='http://www.w3.org/2000/svg' version='1.1'></svg></div>\n"
                + "  <script>\n"
                + LOG_TITLE_FUNCTION
                + "    var div = document.getElementById('myDivId');\n"
                + "    log(div.innerText);\n"
                + "  </script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }
}
