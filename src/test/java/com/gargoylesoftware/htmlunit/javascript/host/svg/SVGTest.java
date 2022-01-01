/*
 * Copyright (c) 2002-2022 Gargoyle Software Inc.
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

import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.junit.BrowserRunner;
import com.gargoylesoftware.htmlunit.junit.BrowserRunner.Alerts;

/**
 * Very simple test for SVG "support".
 *
 * @author Marc Guillemot
 * @author Frank Danek
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class SVGTest extends WebDriverTestCase {

    /**
     * Test for issue 3313921.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("svgElem")
    public void getAttribute() throws Exception {
        final String html = "<html><body>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "try {\n"
            + "  var svg = document.createElementNS('http://www.w3.org/2000/svg', 'svg');\n"
            + "  svg.setAttribute('id', 'svgElem');\n"
            + "  document.body.appendChild(svg);\n"
            + "  log(document.getElementById('svgElem').getAttribute('id'));\n"
            + "} catch (e) { log('exception'); }\n"
            + "</script></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Test for issue 3313921.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("clicked")
    public void triggerEvent() throws Exception {
        final String html =
                "<html>\n"
                + "<head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function init() {\n"
                + "    try {\n"
                + "      var rect = document.getElementById('rect');\n"
                + "      rect.addEventListener('click', function() { log('clicked') });\n"

                + "      var e = document.createEvent('MouseEvents');\n"
                + "      e.initEvent('click', true, false);\n"
                + "      document.getElementById('rect').dispatchEvent(e);\n"
                + "    } catch (e) { log('exception'); }\n"
                + "  }\n"
                + "</script>\n"
                + "</head>\n"
                + "<body onload='init()'>\n"
                + "<svg xmlns='http://www.w3.org/2000/svg' version='1.1'>\n"
                + "  <rect id='rect'  width='300' height='100' />\n"
                + "</svg>\n"
                + "</body>\n"
                + "</html>";

        loadPageVerifyTitle2(html);
    }
}
