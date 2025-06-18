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
package org.htmlunit.svg;

import org.htmlunit.WebDriverTestCase;
import org.htmlunit.html.DomElement;
import org.htmlunit.html.HtmlPage;
import org.htmlunit.html.HtmlScript;
import org.htmlunit.junit.annotation.Alerts;
import org.htmlunit.junit.annotation.HtmlUnitNYI;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

/**
 * Tests for {@link SvgScript}.
 *
 * @author Ahmed Ashour
 * @author Frank Danek
 * @author Ronald Brill
 */
public class SvgScriptTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("[object SVGScriptElement]")
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
            + "  <svg xmlns='http://www.w3.org/2000/svg' version='1.1'>\n"
            + "    <script id='myId'></script>\n"
            + "  </svg>\n"
            + "</body></html>";

        final WebDriver driver = loadPageVerifyTitle2(html);
        if (driver instanceof HtmlUnitDriver) {
            final HtmlPage page = (HtmlPage) getEnclosedPage();
            assertType(getExpectedAlerts()[0], page.getElementById("myId"));
        }
    }

    private void assertType(final String expectedAlert, final DomElement element) {
        if ("[object SVGScriptElement]".equals(expectedAlert)) {
            assertTrue(SvgScript.class.isInstance(element));
        }
        else {
            assertTrue(HtmlScript.class.isInstance(element));
        }
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[object SVGScriptElement]", "[object HTMLScriptElement]"})
    @HtmlUnitNYI(CHROME = {"[object SVGScriptElement]", "[object SVGScriptElement]"},
            EDGE = {"[object SVGScriptElement]", "[object SVGScriptElement]"},
            FF = {"[object SVGScriptElement]", "[object SVGScriptElement]"},
            FF_ESR = {"[object SVGScriptElement]", "[object SVGScriptElement]"})
    public void htmlOrSvg() throws Exception {
        final String html = "<svg xmlns='http://www.w3.org/2000/svg' version='1.1'>\n"
            + "<script id='id1'>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    log(document.getElementById('id1'));\n"
            + "    log(document.getElementById('id2'));\n"
            + "  }\n"
            + "</script>\n"
            + "<body onload='test()'>\n"
            + "  <script id='id2'></script>\n"
            + "</body>\n"
            + "</svg>";

        final WebDriver driver = loadPageVerifyTitle2(html);
        if (driver instanceof HtmlUnitDriver) {
            final HtmlPage page = (HtmlPage) getEnclosedPage();
            assertType(getExpectedAlerts()[0], page.getElementById("id1"));
            assertType(getExpectedAlerts()[1], page.getElementById("id2"));
        }
    }
}
