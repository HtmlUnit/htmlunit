/*
 * Copyright (c) 2002-2014 Gargoyle Software Inc.
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
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * Tests for {@link HtmlHeading1} to {@link HtmlHeading6}.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 * @author Ronald Brill
 * @author Frank Danek
 */
@RunWith(BrowserRunner.class)
public class HtmlHeading2Test extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "[object HTMLHeadingElement]",
            IE8 = "[object]")
    public void simpleScriptable() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    alert(document.getElementById('myId'));\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "  <h2 id='myId'>asdf</h2>\n"
            + "</body></html>";

        final WebDriver driver = loadPageWithAlerts2(html);
        if (driver instanceof HtmlUnitDriver) {
            final HtmlElement element = toHtmlElement(driver.findElement(By.id("myId")));
            assertTrue(element instanceof HtmlHeading2);
        }
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = { "left", "right", "center", "justify", "wrong", "" },
            IE = { "left", "right", "center", "justify", "", "" })
    public void getAlign() throws Exception {
        final String html
            = "<html><body>\n"
            + "<form>\n"
            + "  <h1 id='e1' align='left'>Header1</h1>\n"
            + "  <h2 id='e2' align='right'>Header2</h2>\n"
            + "  <h3 id='e3' align='center'>Header3</h3>\n"
            + "  <h4 id='e4' align='justify'>Header4</h4>\n"
            + "  <h5 id='e5' align='wrong'>Header5</h5>\n"
            + "  <h6 id='e6'>Header6</h6>\n"
            + "</form>\n"

            + "<script>\n"
            + "  alert(e1.align);\n"
            + "  alert(e2.align);\n"
            + "  alert(e3.align);\n"
            + "  alert(e4.align);\n"
            + "  alert(e5.align);\n"
            + "  alert(e6.align);\n"
            + "</script>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = { "CenTer", "8", "foo" },
            IE = { "center", "error", "center", "error", "center" })
    public void setAlign() throws Exception {
        final String html
            = "<html><body>\n"
            + "<form>\n"
            + "  <h1 id='e1' align='left'>Header1</h1>\n"
            + "</form>\n"

            + "<script>\n"
            + "  function setAlign(elem, value) {\n"
            + "    try {\n"
            + "      elem.align = value;\n"
            + "    } catch (e) {\n"
            + "      alert('error');\n"
            + "    }\n"
            + "  }\n"

            + "  var elem = document.getElementById('e1');\n"
            + "  setAlign(elem, 'CenTer');\n"
            + "  alert(elem.align);\n"
            + "  setAlign(e1, '8');\n"
            + "  alert(e1.align);\n"
            + "  setAlign(e1, 'foo');\n"
            + "  alert(e1.align);\n"
            + "</script>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }
}
