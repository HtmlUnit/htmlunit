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
 * Tests for {@link HtmlFieldSet}.
 *
 * @author Ahmed Ashour
 * @author Daniel Gredler
 * @author Frank Danek
 */
@RunWith(BrowserRunner.class)
public class HtmlFieldSetTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[object HTMLFieldSetElement]", "[object HTMLFormElement]"})
    public void simpleScriptable() throws Exception {
        final String html
            = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var fs = document.getElementById('fs');\n"
            + "    log(fs);\n"
            + "    log(fs.form);\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "  <form>\n"
            + "    <fieldset id='fs'>\n"
            + "      <legend>Legend</legend>\n"
            + "    </fieldset>\n"
            + "  </form>\n"
            + "</body></html>";
        final WebDriver driver = loadPageVerifyTitle2(html);
        if (driver instanceof HtmlUnitDriver) {
            final HtmlElement element = toHtmlElement(driver.findElement(By.id("fs")));
            assertTrue(element instanceof HtmlFieldSet);
        }
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"undefined", "undefined", "undefined", "center", "8", "foo"},
            IE = {"left", "right", "", "error", "error", "center", "right", ""})
    public void align() throws Exception {
        final String html
            = "<html><body>\n"
            + "<form>\n"
            + "  <fieldset id='fs1' align='left'>\n"
            + "    <legend>Legend</legend>\n"
            + "  </fieldset>\n"
            + "  <fieldset id='fs2' align='right'>\n"
            + "    <legend>Legend</legend>\n"
            + "  </fieldset>\n"
            + "  <fieldset id='fs3' align='3'>\n"
            + "    <legend>Legend</legend>\n"
            + "  </fieldset>\n"
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
            + "  var fs1 = document.getElementById('fs1');\n"
            + "  var fs2 = document.getElementById('fs2');\n"
            + "  var fs3 = document.getElementById('fs3');\n"
            + "  log(fs1.align);\n"
            + "  log(fs2.align);\n"
            + "  log(fs3.align);\n"
            + "  set(fs1, 'center');\n"
            + "  set(fs2, '8');\n"
            + "  set(fs3, 'foo');\n"
            + "  log(fs1.align);\n"
            + "  log(fs2.align);\n"
            + "  log(fs3.align);\n"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

}
