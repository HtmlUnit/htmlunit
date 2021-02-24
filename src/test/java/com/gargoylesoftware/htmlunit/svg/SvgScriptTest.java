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
package com.gargoylesoftware.htmlunit.svg;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPageTest;
import com.gargoylesoftware.htmlunit.html.HtmlScript;

/**
 * Tests for {@link SvgScript}.
 *
 * @author Ahmed Ashour
 * @author Frank Danek
 */
@RunWith(BrowserRunner.class)
public class SvgScriptTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("[object SVGScriptElement]")
    public void simpleScriptable() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    alert(document.getElementById('myId'));\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "  <svg xmlns='http://www.w3.org/2000/svg' version='1.1'>\n"
            + "    <script id='myId'></script>\n"
            + "  </svg>\n"
            + "</body></html>";

        final WebDriver driver = loadPageWithAlerts2(html);
        if (driver instanceof HtmlUnitDriver) {
            final HtmlPage page = (HtmlPage) getWebWindowOf((HtmlUnitDriver) driver).getEnclosedPage();
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
    @NotYetImplemented
    public void htmlOrSvg() throws Exception {
        final String html = "<svg xmlns='http://www.w3.org/2000/svg' version='1.1'>\n"
            + "<script id='id1'>\n"
            + "  function test() {\n"
            + "    alert(document.getElementById('id1'));\n"
            + "    alert(document.getElementById('id2'));\n"
            + "  }\n"
            + "</script>\n"
            + "<body onload='test()'>\n"
            + "  <script id='id2'></script>\n"
            + "</body>\n"
            + "</svg>";

        final WebDriver driver = loadPageWithAlerts2(html);
        if (driver instanceof HtmlUnitDriver) {
            final HtmlPage page = (HtmlPage) getWebWindowOf((HtmlUnitDriver) driver).getEnclosedPage();
            assertType(getExpectedAlerts()[0], page.getElementById("id1"));
            assertType(getExpectedAlerts()[1], page.getElementById("id2"));
        }
    }
}
