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
 * Tests for {@link HtmlHeading1} to {@link HtmlHeading6}.
 *
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
    @Alerts("[object HTMLHeadingElement]")
    public void simpleScriptable() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    log(document.getElementById('myId'));\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "  <h2 id='myId'>asdf</h2>\n"
            + "</body></html>";

        final WebDriver driver = loadPageVerifyTitle2(html);
        if (driver instanceof HtmlUnitDriver) {
            final HtmlElement element = toHtmlElement(driver.findElement(By.id("myId")));
            assertTrue(element instanceof HtmlHeading2);
        }
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"left", "right", "center", "justify", "wrong", ""},
            IE = {"left", "right", "center", "justify", "", ""})
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
            + LOG_TITLE_FUNCTION
            + "  log(e1.align);\n"
            + "  log(e2.align);\n"
            + "  log(e3.align);\n"
            + "  log(e4.align);\n"
            + "  log(e5.align);\n"
            + "  log(e6.align);\n"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"CenTer", "8", "foo"},
            IE = {"center", "error", "center", "error", "center"})
    public void setAlign() throws Exception {
        final String html
            = "<html><body>\n"
            + "<form>\n"
            + "  <h1 id='e1' align='left'>Header1</h1>\n"
            + "</form>\n"

            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function setAlign(elem, value) {\n"
            + "    try {\n"
            + "      elem.align = value;\n"
            + "    } catch (e) {\n"
            + "      log('error');\n"
            + "    }\n"
            + "  }\n"

            + "  var elem = document.getElementById('e1');\n"
            + "  setAlign(elem, 'CenTer');\n"
            + "  log(elem.align);\n"
            + "  setAlign(e1, '8');\n"
            + "  log(e1.align);\n"
            + "  setAlign(e1, 'foo');\n"
            + "  log(e1.align);\n"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"undefined", "undefined", "undefined", "undefined", "undefined", "undefined",
                       "undefined", "left", "none", "right", "all", "2", "abc", "8"},
            IE = {"", "left", "all", "right", "none", "", "", "!", "!", "!", "left", "none", "right", "all", "none",
                  "", ""})
    public void clear() throws Exception {
        final String html
            = "<html><body>\n"
            + "<h1 id='h1'>h1</h1>\n"
            + "<h2 id='h2' clear='left'>h2</h2>\n"
            + "<h3 id='h3' clear='all'>h3</h3>\n"
            + "<h4 id='h4' clear='right'>h4</h4>\n"
            + "<h5 id='h5' clear='none'>h5</h5>\n"
            + "<h6 id='h6' clear='2'>h6</h6>\n"
            + "<h1 id='h7' clear='foo'>h7</h1>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function set(h, value) {\n"
            + "  try {\n"
            + "    h.clear = value;\n"
            + "  } catch(e) {\n"
            + "    log('!');\n"
            + "  }\n"
            + "}\n"
            + "var h1 = document.getElementById('h1');\n"
            + "var h2 = document.getElementById('h2');\n"
            + "var h3 = document.getElementById('h3');\n"
            + "var h4 = document.getElementById('h4');\n"
            + "var h5 = document.getElementById('h5');\n"
            + "var h6 = document.getElementById('h6');\n"
            + "var h7 = document.getElementById('h7');\n"
            + "log(h1.clear);\n"
            + "log(h2.clear);\n"
            + "log(h3.clear);\n"
            + "log(h4.clear);\n"
            + "log(h5.clear);\n"
            + "log(h6.clear);\n"
            + "log(h7.clear);\n"
            + "set(h1, 'left');\n"
            + "set(h2, 'none');\n"
            + "set(h3, 'right');\n"
            + "set(h4, 'all');\n"
            + "set(h5, 2);\n"
            + "set(h6, 'abc');\n"
            + "set(h7, '8');\n"
            + "log(h1.clear);\n"
            + "log(h2.clear);\n"
            + "log(h3.clear);\n"
            + "log(h4.clear);\n"
            + "log(h5.clear);\n"
            + "log(h6.clear);\n"
            + "log(h7.clear);\n"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }
}
