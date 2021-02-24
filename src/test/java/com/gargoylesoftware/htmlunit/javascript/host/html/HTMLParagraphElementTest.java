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
package com.gargoylesoftware.htmlunit.javascript.host.html;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlParagraph;

/**
 * Tests for {@link HTMLParagraphElement}.
 * @author Daniel Gredler
 * @author Ahmed Ashour
 * @author Frank Danek
 */
@RunWith(BrowserRunner.class)
public class HTMLParagraphElementTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("[object HTMLParagraphElement]")
    public void simpleScriptable() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    alert(document.getElementById('myId'));\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "  <p id='myId'/>\n"
            + "</body></html>";

        final WebDriver driver = loadPageWithAlerts2(html);
        if (driver instanceof HtmlUnitDriver) {
            final HtmlPage page = (HtmlPage) getWebWindowOf((HtmlUnitDriver) driver).getEnclosedPage();
            assertTrue(HtmlParagraph.class.isInstance(page.getHtmlElementById("myId")));
        }
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"", "hello", "left", "hi", "right"},
            IE = {"", "error", "", "left", "error", "left", "right"})
    public void align() throws Exception {
        final String html =
            "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + "      function test() {\n"
            + "        var p = document.getElementById('p');\n"
            + "        alert(p.align);\n"
            + "        set(p, 'hello');\n"
            + "        alert(p.align);\n"
            + "        set(p, 'left');\n"
            + "        alert(p.align);\n"
            + "        set(p, 'hi');\n"
            + "        alert(p.align);\n"
            + "        set(p, 'right');\n"
            + "        alert(p.align);\n"
            + "      }\n"
            + "      function set(e, value) {\n"
            + "        try {\n"
            + "          e.align = value;\n"
            + "        } catch (e) {\n"
            + "          alert('error');\n"
            + "        }\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='test()'><p id='p'>foo</p></body>\n"
            + "</html>";
        loadPageWithAlerts2(html);
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
            + "<p id='p1'>p1</p>\n"
            + "<p id='p2' clear='left'>p2</p>\n"
            + "<p id='p3' clear='all'>p3</p>\n"
            + "<p id='p4' clear='right'>p4</p>\n"
            + "<p id='p5' clear='none'>p5</p>\n"
            + "<p id='p6' clear='2'>p6</p>\n"
            + "<p id='p7' clear='foo'>p7</p>\n"
            + "<script>\n"
            + "function set(p, value) {\n"
            + "  try {\n"
            + "    p.clear = value;\n"
            + "  } catch(e) {\n"
            + "    alert('!');\n"
            + "  }\n"
            + "}\n"
            + "var p1 = document.getElementById('p1');\n"
            + "var p2 = document.getElementById('p2');\n"
            + "var p3 = document.getElementById('p3');\n"
            + "var p4 = document.getElementById('p4');\n"
            + "var p5 = document.getElementById('p5');\n"
            + "var p6 = document.getElementById('p6');\n"
            + "var p7 = document.getElementById('p7');\n"
            + "alert(p1.clear);\n"
            + "alert(p2.clear);\n"
            + "alert(p3.clear);\n"
            + "alert(p4.clear);\n"
            + "alert(p5.clear);\n"
            + "alert(p6.clear);\n"
            + "alert(p7.clear);\n"
            + "set(p1, 'left');\n"
            + "set(p2, 'none');\n"
            + "set(p3, 'right');\n"
            + "set(p4, 'all');\n"
            + "set(p5, 2);\n"
            + "set(p6, 'abc');\n"
            + "set(p7, '8');\n"
            + "alert(p1.clear);\n"
            + "alert(p2.clear);\n"
            + "alert(p3.clear);\n"
            + "alert(p4.clear);\n"
            + "alert(p5.clear);\n"
            + "alert(p6.clear);\n"
            + "alert(p7.clear);\n"
            + "</script>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }
}
