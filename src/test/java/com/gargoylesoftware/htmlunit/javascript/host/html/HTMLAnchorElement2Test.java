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
package com.gargoylesoftware.htmlunit.javascript.host.html;

import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.CHROME;
import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.FF;
import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.IE;
import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.IE10;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.Browsers;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * Unit tests for {@link HTMLLinkElement}.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 * @author Ronald Brill
 * @author Marc Guillemot
 * @author Frank Danek
 */
@RunWith(BrowserRunner.class)
public class HTMLAnchorElement2Test extends WebDriverTestCase {

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({ "", "", "",  "§§URL§§test.css", "stylesheet", "stylesheet1" })
    public void attributes() throws Exception {
        final String html =
              "<html>\n"
            + "    <body onload='test()'>\n"
            + "        <script>\n"
            + "            function test() {\n"
            + "                var a = document.createElement('a');\n"
            + "                alert(a.href);\n"
            + "                alert(a.rel);\n"
            + "                alert(a.rev);\n"
            + "                a.href = 'test.css';\n"
            + "                a.rel  = 'stylesheet';\n"
            + "                a.rev  = 'stylesheet1';\n"
            + "                alert(a.href);\n"
            + "                alert(a.rel);\n"
            + "                alert(a.rev);\n"
            + "            }\n"
            + "        </script>\n"
            + "    </body>\n"
            + "</html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(IE)
    @Alerts("onclick")
    public void javaScriptPreventDefaultIE() throws Exception {
        final String html
            = "<html><head><title>Test</title>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var a = document.getElementById('link');\n"
            + "    a.attachEvent('onclick', handler);\n"
            + "  }\n"
            + "  function handler() {\n"
            + "    event.returnValue = false;\n"
            + "    alert('onclick');\n"
            + "  }\n"
            + "</script>\n"
            + "<body onload='test()'>\n"
            + "<a id='link' href='javascript: alert(\"href\");'>link</a>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("link")).click();
        assertEquals(getExpectedAlerts(), getCollectedAlerts(driver));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers({ FF, CHROME, IE10 })
    @Alerts("onclick")
    public void javaScriptPreventDefault() throws Exception {
        final String html
            = "<html><head><title>Test</title>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var a = document.getElementById('link');\n"
            + "    a.addEventListener('click', handler);\n"
            + "  }\n"
            + "  function handler(event) {\n"
            + "    event.preventDefault();\n"
            + "    alert('onclick');\n"
            + "  }\n"
            + "</script>\n"
            + "<body onload='test()'>\n"
            + "<a id='link' href='javascript: alert(\"href\");'>link</a>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("link")).click();
        assertEquals(getExpectedAlerts(), getCollectedAlerts(driver));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "", "§§URL§§foo.html", "javascript:void(0)", "§§URL§§#", "mailto:" })
    public void defaultConversionToString() throws Exception {
        final String html
            = "<html><head><title>AnchorTest</title><script>\n"
            + "function test() {\n"
            + "  alert(document.getElementById('myAnchor'));\n"
            + "  for (var i=0; i<document.links.length; ++i)\n"
            + "  {\n"
            + "    alert(document.links[i]);\n"
            + "  }\n"
            + "}</script></head>\n"
            + "<body onload='test()'>\n"
            + "<a name='start' id='myAnchor'/>\n"
            + "<a href='foo.html'>foo</a>\n"
            + "<a href='javascript:void(0)'>void</a>\n"
            + "<a href='#'>#</a>\n"
            + "<a href='mailto:'>mail</a>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Second",
            FF3_6 = "First") // in fact not alerts here, but it makes config easier
    public void javaScriptAnchorClick() throws Exception {
        final String html
            = "<html><head><title>First</title><script>\n"
            + "function delegateClick() {\n"
            + "  try {"
            + "    document.getElementById(\"link1\").click();\n"
            + "  } catch(e) {}\n"
            + "}"
            + "</script></head><body>\n"
            + "<a id='link1' href='#' onclick='document.form1.submit()'>link 1</a>\n"
            + "<form name='form1' action='" + URL_SECOND + "' method='post'>\n"
            + "<input type=button id='button1' value='Test' onclick='delegateClick()'>\n"
            + "<input name='testText'>\n"
            + "</form>\n"
            + "</body></html>";

        final String secondHtml
            = "<html>\n"
            + "<head><title>Second</title></head>\n"
            + "</html>";

        getMockWebConnection().setResponse(URL_SECOND, secondHtml);

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("button1")).click();

        assertEquals(getExpectedAlerts()[0], driver.getTitle());
    }

}
