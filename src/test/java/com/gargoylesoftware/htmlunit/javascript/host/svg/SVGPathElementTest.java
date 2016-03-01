/*
 * Copyright (c) 2002-2016 Gargoyle Software Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
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
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.html.HtmlPageTest;

/**
 * Tests for {@link SVGPathElement}.
 *
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class SVGPathElementTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "[object SVGPathElement]",
            FF = "function SVGPathElement() {\n    [native code]\n}",
            CHROME = "function SVGPathElement() { [native code] }")
    public void simpleScriptable() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    alert(window.SVGPathElement);\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "[object SVGPathElement]", "true" })
    public void getTotalLengthAvailable() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    if (window.SVGPathElement) {\n"
            + "      var path = document.getElementById('myId');\n"
            + "      alert(path);\n"
            + "      alert(path.getTotalLength() > 0);\n"
            + "    }\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "<svg width='100%' height='100%' viewBox='0 0 400 400' xmlns='http://www.w3.org/2000/svg'>\n"
            + "  <path id='myId' d='M 100 100 L 300 100 L 200 300 z' stroke='black' stroke-width='3' />\n"
            + "</svg>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "[object SVGPathElement]", "647.213623046875" })
    @NotYetImplemented
    public void getTotalLength() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    if (window.SVGPathElement) {\n"
            + "      var path = document.getElementById('myId');\n"
            + "      alert(path);\n"
            + "      alert(path.getTotalLength());\n"
            + "    }\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "<svg width='100%' height='100%' viewBox='0 0 400 400' xmlns='http://www.w3.org/2000/svg'>\n"
            + "  <path id='myId' d='M 100 100 L 300 100 L 200 300 z' stroke='black' stroke-width='3' />\n"
            + "</svg>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }
}
