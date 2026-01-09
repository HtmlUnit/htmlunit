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
package org.htmlunit.javascript.host.svg;

import org.htmlunit.WebDriverTestCase;
import org.htmlunit.junit.annotation.Alerts;
import org.htmlunit.junit.annotation.HtmlUnitNYI;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link SVGTextPathElement}.
 *
 * @author Ronald Brill
 */
public class SVGTextPathElementTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("function SVGTextPathElement() { [native code] }")
    public void simpleScriptable() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    log(window.SVGTextPathElement);\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[object SVGTextPathElement]", "true"})
    public void getComputedTextLengthAvailable() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    if (window.SVGPathElement) {\n"
            + "      var text = document.getElementById('myId');\n"
            + "      log(text);\n"
            + "      log(text.getComputedTextLength() > 0);\n"
            + "    }\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "<svg width='100%' height='100%' viewBox='0 0 400 400' xmlns='http://www.w3.org/2000/svg'>\n"
            + "  <defs>\n"
            + "    <path id='myTextPath' d='M 50,100 Q 150,50 250,100' />\n"
            + "  </defs>\n"
            + "  <text fill='steelblue' font-size='20'>\n"
            + "    <textPath id='myId' xlink:href='#myTextPath'>Heho</textPath>\n"
            + "  </text>\n"
            + "</svg>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[object SVGTextPathElement]", "43.3"})
    @HtmlUnitNYI(CHROME = {"[object SVGTextPathElement]", "40.0"},
            EDGE = {"[object SVGTextPathElement]", "40.0"},
            FF = {"[object SVGTextPathElement]", "40.0"},
            FF_ESR = {"[object SVGTextPathElement]", "40.0"})
    public void getComputedTextLength() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    if (window.SVGTextElement) {\n"
            + "      var text = document.getElementById('myId');\n"
            + "      log(text);\n"
            + "      var length = text.getComputedTextLength();\n"
            + "      log(length.toFixed(1));\n"
            + "    }\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "<svg width='100%' height='100%' viewBox='0 0 400 400' xmlns='http://www.w3.org/2000/svg'>\n"
            + "  <defs>\n"
            + "    <path id='myTextPath' d='M 50,100 Q 150,50 250,100' />\n"
            + "  </defs>\n"
            + "  <text fill='steelblue' font-size='20'>\n"
            + "    <textPath id='myId' xlink:href='#myTextPath'>Heho</textPath>\n"
            + "  </text>\n"
            + "</svg>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }
}
