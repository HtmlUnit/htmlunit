/*
 * Copyright (c) 2002-2010 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript.host.html;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;

/**
 * Unit tests for {@link HTMLBodyElement}.
 *
 * @version $Revision$
 * @author Daniel Gredler
 * @author Ahmed Ashour
 * @author Marc Guillemot
 */
@RunWith(BrowserRunner.class)
public class HTMLBodyElementTest extends WebDriverTestCase {

    /**
     * Tests the default body padding and margins.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = {",0px,0px,0px,0px", ",,,,", ",8px,8px,8px,8px", ",,,," },
            IE = {"0px,0px,0px,0px,0px", ",,,,", "15px 10px,10px,10px,15px,15px", ",,,," })
    public void testDefaultPaddingAndMargins() throws Exception {
        final String html =
            "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + "      function test() {\n"
            + "        var b = document.getElementById('body');\n"
            + "        var s = b.currentStyle ? b.currentStyle : getComputedStyle(b, null);\n"
            + "        alert(s.padding + ',' + s.paddingLeft + ',' + s.paddingRight + ',' + s.paddingTop + ',' + s.paddingBottom);\n"
            + "        alert(b.style.padding + ',' + b.style.paddingLeft + ',' + b.style.paddingRight + ',' + b.style.paddingTop + ',' + b.style.paddingBottom);\n"
            + "        alert(s.margin + ',' + s.marginLeft + ',' + s.marginRight + ',' + s.marginTop + ',' + s.marginBottom);\n"
            + "        alert(b.style.margin + ',' + b.style.marginLeft + ',' + b.style.marginRight + ',' + b.style.marginTop + ',' + b.style.marginBottom);\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body id='body' onload='test()'>blah</body>\n"
            + "</html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = "exception", IE = "[object]")
    public void attachEvent() throws Exception {
        final String html =
            "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + "      function handler() {\n"
            + "        alert(event);\n"
            + "      }\n"
            + "      function test() {\n"
            + "        try {\n"
            + "          document.body.attachEvent('onclick', handler);\n"
            + "        } catch(e) { alert('exception'); }\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='test()'>\n"
            + "    <input type='button' id='myInput' value='Test me'>\n"
            + "  </body>\n"
            + "</html>";

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("myInput")).click();
        assertEquals(getExpectedAlerts(), getCollectedAlerts(driver));
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = "no",
            IE = "yes")
    public void doScroll() throws Exception {
        final String html =
            "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + "      function test() {\n"
            + "        if(document.body.doScroll) {\n"
            + "          alert('yes');\n"
            + "          document.body.doScroll();\n"
            + "          document.body.doScroll('down');\n"
            + "        } else {\n"
            + "          alert('no');\n"
            + "        }\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='test()'>\n"
            + "  </body>\n"
            + "</html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = {"#ee0000", "#0000aa", "#000000" },
            IE = {"", "#0000aa", "#000000" })
    public void aLink() throws Exception {
        final String html =
            "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + "      function test() {\n"
            + "        var b = document.getElementById('body');\n"
            + "        alert(b.aLink);\n"
            + "        b.aLink = '#0000aa';\n"
            + "        alert(b.aLink);\n"
            + "        b.aLink = 'x';\n"
            + "        alert(b.aLink);\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body id='body' onload='test()'>blah</body>\n"
            + "</html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = {"", "http://www.foo.com/blah.gif", "§§URL§§blah.gif" },
            IE = {"", "http://www.foo.com/blah.gif", "blah.gif" })
    public void background() throws Exception {
        final String html =
            "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + "      function test() {\n"
            + "        var b = document.getElementById('body');\n"
            + "        alert(b.background);\n"
            + "        b.background = 'http://www.foo.com/blah.gif';\n"
            + "        alert(b.background);\n"
            + "        b.background = 'blah.gif';\n"
            + "        alert(b.background);\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body id='body' onload='test()'>blah</body>\n"
            + "</html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = {"#ffffff", "#0000aa", "#000000" },
            IE = {"", "#0000aa", "#000000" })
    public void bgColor() throws Exception {
        final String html =
            "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + "      function test() {\n"
            + "        var b = document.getElementById('body');\n"
            + "        alert(b.bgColor);\n"
            + "        b.bgColor = '#0000aa';\n"
            + "        alert(b.bgColor);\n"
            + "        b.bgColor = 'x';\n"
            + "        alert(b.bgColor);\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body id='body' onload='test()'>blah</body>\n"
            + "</html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = {"#0000ee", "#0000aa", "#000000" },
            IE = {"", "#0000aa", "#000000" })
    public void link() throws Exception {
        final String html =
            "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + "      function test() {\n"
            + "        var b = document.getElementById('body');\n"
            + "        alert(b.link);\n"
            + "        b.link = '#0000aa';\n"
            + "        alert(b.link);\n"
            + "        b.link = 'x';\n"
            + "        alert(b.link);\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body id='body' onload='test()'>blah</body>\n"
            + "</html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = {"#000000", "#0000aa", "#000000" },
            IE = {"", "#0000aa", "#000000" })
    public void text() throws Exception {
        final String html =
            "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + "      function test() {\n"
            + "        var b = document.getElementById('body');\n"
            + "        alert(b.text);\n"
            + "        b.text = '#0000aa';\n"
            + "        alert(b.text);\n"
            + "        b.text = 'x';\n"
            + "        alert(b.text);\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body id='body' onload='test()'>blah</body>\n"
            + "</html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = {"#551a8b", "#0000aa", "#000000" },
            IE = {"", "#0000aa", "#000000" })
    public void vLink() throws Exception {
        final String html =
            "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + "      function test() {\n"
            + "        var b = document.getElementById('body');\n"
            + "        alert(b.vLink);\n"
            + "        b.vLink = '#0000aa';\n"
            + "        alert(b.vLink);\n"
            + "        b.vLink = 'x';\n"
            + "        alert(b.vLink);\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body id='body' onload='test()'>blah</body>\n"
            + "</html>";
        loadPageWithAlerts2(html);
    }
}
