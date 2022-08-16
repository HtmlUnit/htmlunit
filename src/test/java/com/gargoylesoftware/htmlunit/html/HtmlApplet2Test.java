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
package com.gargoylesoftware.htmlunit.html;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.junit.BrowserRunner;
import com.gargoylesoftware.htmlunit.junit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.junit.BrowserRunner.BuggyWebDriver;

/**
 * Tests for {@link HtmlApplet}.
 *
 * @author Ahmed Ashour
 * @author Marc Guillemot
 * @author Ronald Brill
 * @author Frank Danek
 */
@RunWith(BrowserRunner.class)
public class HtmlApplet2Test extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"[object HTMLUnknownElement]", "[object HTMLCollection]", "0", "undefined"},
            IE = {"[object HTMLAppletElement]", "[object HTMLCollection]", "1", "[object HTMLAppletElement]"})
    @BuggyWebDriver(IE = {"The page you are viewing uses Java. More information on Java support "
                            + "is available from the Microsoft website.",
                          "[object HTMLAppletElement]", "[object HTMLCollection]", "1", "[object HTMLAppletElement]"})
    public void simpleScriptable() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    alert(document.getElementById('myId'));\n"
            + "    alert(document.applets);\n"
            + "    alert(document.applets.length);\n"
            + "    alert(document.applets[0]);\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "  <applet id='myId'></applet>\n"
            + "</body></html>";

        final WebDriver driver = loadPageWithAlerts2(html);
        if (driver instanceof HtmlUnitDriver) {
            if (getBrowserVersion().isChrome()
                    || getBrowserVersion().isFirefox()
                    || getBrowserVersion().isEdge()) {
                final HtmlPage page = (HtmlPage) getEnclosedPage();
                assertTrue(HtmlUnknownElement.class.isInstance(page.getHtmlElementById("myId")));
            }
            else {
                final HtmlPage page = (HtmlPage) getEnclosedPage();
                assertTrue(HtmlApplet.class.isInstance(page.getHtmlElementById("myId")));
            }
        }
    }

}
