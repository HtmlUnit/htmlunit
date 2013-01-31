/*
 * Copyright (c) 2002-2013 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.html;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * Tests for {@link HtmlProgress}.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class HtmlProgressTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "[object HTMLProgressElement]", FF3_6 = "[object HTMLUnknownElement]",
            IE = "[object HTMLGenericElement]")
    public void simpleScriptable() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_ + "<html><head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    alert(document.getElementById('myId'));\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "  <progress id='myId'></progress>\n"
            + "</body></html>";

        final WebDriver driver = loadPageWithAlerts2(html);
        if (driver instanceof HtmlUnitDriver) {
            final HtmlPage page = (HtmlPage) getWebWindowOf((HtmlUnitDriver) driver).getEnclosedPage();
            if ("[object HTMLProgressElement]".equals(getExpectedAlerts()[0])) {
                assertTrue(HtmlProgress.class.isInstance(page.getHtmlElementById("myId")));
            }
            else {
                assertTrue(HtmlUnknownElement.class.isInstance(page.getHtmlElementById("myId")));
            }
        }
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = { "number70", "number100" },
            FF3_6 = { }, IE = { })
    public void properties() throws Exception {
        final String html = "<html><body>\n"
            + "<progress id='it' value='70' max='100'>70%</progress>\n"
            + "<script>\n"
            + "var elt = document.getElementById('it');\n"
            + "if (window.HTMLProgressElement) {\n"
            + "  alert(typeof(elt.value) + elt.value);\n"
            + "  alert(typeof(elt.max) + elt.max);\n"
            + "}\n"
            + "</script></body></html>";
        loadPageWithAlerts2(html);
    }
}
