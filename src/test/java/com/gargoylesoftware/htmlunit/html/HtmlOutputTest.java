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
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.junit.BrowserRunner;
import com.gargoylesoftware.htmlunit.junit.BrowserRunner.Alerts;

/**
 * Tests for {@link HtmlOutput}.
 *
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class HtmlOutputTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"[object HTMLOutputElement]", "[object HTMLFormElement]"},
            IE = {"[object HTMLUnknownElement]", "undefined"})
    public void simpleScriptable() throws Exception {
        final String html
            = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var o = document.getElementById('o');\n"
            + "    log(o);\n"
            + "    log(o.form);\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "  <form>\n"
            + "    <output id='o'>\n"
            + "  </form>\n"
            + "</body></html>";
        final WebDriver driver = loadPageVerifyTitle2(html);
        if (driver instanceof HtmlUnitDriver) {
            final HtmlElement element = toHtmlElement(driver.findElement(By.id("o")));
            assertTrue(element instanceof HtmlOutput || element instanceof HtmlUnknownElement);
        }
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"undefined", "undefined", "undefined", "center", "8", "foo"})
    public void align() throws Exception {
        final String html
            = "<html><body>\n"
            + "<form>\n"
            + "  <output id='o1' align='left'>\n"
            + "  <output id='o2' align='right'>\n"
            + "  <output id='o3' align='3'>\n"
            + "</form>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function set(fs, value) {\n"
            + "    try {\n"
            + "      fs.align = value;\n"
            + "    } catch (e) {\n"
            + "      log('error');\n"
            + "    }\n"
            + "  }\n"
            + "  var o1 = document.getElementById('o1');\n"
            + "  var o2 = document.getElementById('o2');\n"
            + "  var o3 = document.getElementById('o3');\n"
            + "  log(o1.align);\n"
            + "  log(o2.align);\n"
            + "  log(o3.align);\n"
            + "  set(o1, 'center');\n"
            + "  set(o2, '8');\n"
            + "  set(o3, 'foo');\n"
            + "  log(o1.align);\n"
            + "  log(o2.align);\n"
            + "  log(o3.align);\n"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }
}
