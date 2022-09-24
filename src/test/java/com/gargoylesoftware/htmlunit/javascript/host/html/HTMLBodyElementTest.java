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
package com.gargoylesoftware.htmlunit.javascript.host.html;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.junit.BrowserRunner;
import com.gargoylesoftware.htmlunit.junit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.junit.BrowserRunner.HtmlUnitNYI;
import com.gargoylesoftware.htmlunit.util.MimeType;

/**
 * Unit tests for {@link HTMLBodyElement}.
 *
 * @author Daniel Gredler
 * @author Ahmed Ashour
 * @author Marc Guillemot
 * @author Ronald Brill
 * @author Frank Danek
 */
@RunWith(BrowserRunner.class)
public class HTMLBodyElementTest extends WebDriverTestCase {

    /**
     * Tests the default body padding and margins.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"0px,0px,0px,0px,0px", ",,,,", "8px,8px,8px,8px,8px", ",,,,"})
    @HtmlUnitNYI(CHROME = {"0px,0px,0px,0px,0px", ",,,,", "0px,8px,8px,8px,8px", ",,,,"},
            EDGE = {"0px,0px,0px,0px,0px", ",,,,", "0px,8px,8px,8px,8px", ",,,,"},
            FF = {"0px,0px,0px,0px,0px", ",,,,", "0px,8px,8px,8px,8px", ",,,,"})
    public void defaultPaddingAndMargins() throws Exception {
        final String html =
            "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + LOG_TITLE_FUNCTION
            + "      function test() {\n"
            + "        var b = document.getElementById('body');\n"
            + "        var s = b.currentStyle ? b.currentStyle : getComputedStyle(b, null);\n"
            + "        log(s.padding + ',' + s.paddingLeft + ',' + s.paddingRight + ',' + s.paddingTop + ',' + s.paddingBottom);\n"
            + "        log(b.style.padding + ',' + b.style.paddingLeft + ',' + b.style.paddingRight + ',' + b.style.paddingTop + ',' + b.style.paddingBottom);\n"
            + "        log(s.margin + ',' + s.marginLeft + ',' + s.marginRight + ',' + s.marginTop + ',' + s.marginBottom);\n"
            + "        log(b.style.margin + ',' + b.style.marginLeft + ',' + b.style.marginRight + ',' + b.style.marginTop + ',' + b.style.marginBottom);\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body id='body' onload='test()'>blah</body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("exception")
    public void attachEvent() throws Exception {
        final String html =
            "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + LOG_TITLE_FUNCTION
            + "      function handler() {\n"
            + "        log(event);\n"
            + "      }\n"
            + "      function test() {\n"
            + "        try {\n"
            + "          document.body.attachEvent('onclick', handler);\n"
            + "        } catch(e) { log('exception'); }\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='test()'>\n"
            + "    <input type='button' id='myInput' value='Test me'>\n"
            + "  </body>\n"
            + "</html>";

        final WebDriver driver = loadPageVerifyTitle2(html);

        driver.findElement(By.id("myInput")).click();
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("no")
    public void doScroll() throws Exception {
        final String html =
            "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + LOG_TITLE_FUNCTION
            + "      function test() {\n"
            + "        if(document.body.doScroll) {\n"
            + "          log('yes');\n"
            + "          document.body.doScroll();\n"
            + "          document.body.doScroll('down');\n"
            + "        } else {\n"
            + "          log('no');\n"
            + "        }\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='test()'>\n"
            + "  </body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"", "#0000aa", "x"},
            IE = {"", "#0000aa", "#0"})
    public void aLink() throws Exception {
        final String html =
            "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + LOG_TITLE_FUNCTION
            + "      function test() {\n"
            + "        var b = document.getElementById('body');\n"
            + "        log(b.aLink);\n"
            + "        b.aLink = '#0000aa';\n"
            + "        log(b.aLink);\n"
            + "        b.aLink = 'x';\n"
            + "        log(b.aLink);\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body id='body' onload='test()'>blah</body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"", "http://www.foo.com/blah.gif", "blah.gif"})
    public void background() throws Exception {
        final String html =
            "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + LOG_TITLE_FUNCTION
            + "      function test() {\n"
            + "        var b = document.getElementById('body');\n"
            + "        log(b.background);\n"
            + "        b.background = 'http://www.foo.com/blah.gif';\n"
            + "        log(b.background);\n"
            + "        b.background = 'blah.gif';\n"
            + "        log(b.background);\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body id='body' onload='test()'>blah</body>\n"
            + "</html>";
        getMockWebConnection().setDefaultResponse("Error: not found", 404, "Not Found", MimeType.TEXT_HTML);

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"", "#0000aa", "x"},
            IE = {"", "#0000aa", "#0"})
    public void bgColor() throws Exception {
        final String html =
            "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + LOG_TITLE_FUNCTION
            + "      function test() {\n"
            + "        var b = document.getElementById('body');\n"
            + "        log(b.bgColor);\n"
            + "        b.bgColor = '#0000aa';\n"
            + "        log(b.bgColor);\n"
            + "        b.bgColor = 'x';\n"
            + "        log(b.bgColor);\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body id='body' onload='test()'>blah</body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"", "#0000aa", "x"},
            IE = {"", "#0000aa", "#0"})
    public void link() throws Exception {
        final String html =
            "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + LOG_TITLE_FUNCTION
            + "      function test() {\n"
            + "        var b = document.getElementById('body');\n"
            + "        log(b.link);\n"
            + "        b.link = '#0000aa';\n"
            + "        log(b.link);\n"
            + "        b.link = 'x';\n"
            + "        log(b.link);\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body id='body' onload='test()'>blah</body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"", "#0000aa", "x"},
            IE = {"", "#0000aa", "#0"})
    public void text() throws Exception {
        final String html =
            "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + LOG_TITLE_FUNCTION
            + "      function test() {\n"
            + "        var b = document.getElementById('body');\n"
            + "        log(b.text);\n"
            + "        b.text = '#0000aa';\n"
            + "        log(b.text);\n"
            + "        b.text = 'x';\n"
            + "        log(b.text);\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body id='body' onload='test()'>blah</body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"", "#0000aa", "x"},
            IE = {"", "#0000aa", "#0"})
    public void vLink() throws Exception {
        final String html =
            "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + LOG_TITLE_FUNCTION
            + "      function test() {\n"
            + "        var b = document.getElementById('body');\n"
            + "        log(b.vLink);\n"
            + "        b.vLink = '#0000aa';\n"
            + "        log(b.vLink);\n"
            + "        b.vLink = 'x';\n"
            + "        log(b.vLink);\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body id='body' onload='test()'>blah</body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"function HTMLBodyElement() { [native code] }", ""
                    + "ELEMENT_NODE, ATTRIBUTE_NODE, TEXT_NODE, CDATA_SECTION_NODE, ENTITY_REFERENCE_NODE, "
                    + "ENTITY_NODE, PROCESSING_INSTRUCTION_NODE, COMMENT_NODE, DOCUMENT_NODE, DOCUMENT_TYPE_NODE, "
                    + "DOCUMENT_FRAGMENT_NODE, NOTATION_NODE, DOCUMENT_POSITION_DISCONNECTED, "
                    + "DOCUMENT_POSITION_PRECEDING, "
                    + "DOCUMENT_POSITION_FOLLOWING, DOCUMENT_POSITION_CONTAINS, DOCUMENT_POSITION_CONTAINED_BY, "
                    + "DOCUMENT_POSITION_IMPLEMENTATION_SPECIFIC, "},
            IE = {"[object HTMLBodyElement]", ""})
    public void enumeratedProperties() throws Exception {
        final String html
            = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var str = '';\n"
            + "    try {\n"
            + "      log(HTMLBodyElement);\n"
            + "      var str = '';\n"
            + "      for (var i in HTMLBodyElement)\n"
            + "        str += i + ', ';\n"
            + "      log(str);\n"
            + "    } catch (e) { log('exception')}\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"keydown (target)", "keydown (body)"})
    public void eventHandler() throws Exception {
        final String html =
            "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + "      function test() {\n"
            + "        var target = document.getElementById('target');\n"
            + "        target.onkeydown = function() {\n"
            + "          alert('keydown (target)');\n"
            + "        };\n"
            + "        document.body.onkeydown = function() {\n"
            + "          alert('keydown (body)');\n"
            + "        };\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='test()'>\n"
            + "    <input id='target' type='checkbox'>\n"
            + "  </body>\n"
            + "</html>";
        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("target")).sendKeys("a");
        verifyAlerts(DEFAULT_WAIT_TIME, driver, getExpectedAlerts());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"0", "8"})
    @HtmlUnitNYI(CHROME = {"0", "0"},
            EDGE = {"0", "0"},
            FF = {"0", "0"},
            FF_ESR = {"0", "0"},
            IE = {"0", "0"})
    public void top() throws Exception {
        final String html =
            "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + LOG_TITLE_FUNCTION
            + "      function test() {\n"
            + "        var b = document.getElementById('body');\n"
            + "        log(b.offsetTop);\n"
            + "        log(b.getBoundingClientRect().top);\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body id='body' onload='test()'>blah</body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

}
