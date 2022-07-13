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
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.html.HtmlHtml;
import com.gargoylesoftware.htmlunit.junit.BrowserRunner;
import com.gargoylesoftware.htmlunit.junit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.junit.BrowserRunner.HtmlUnitNYI;

/**
 * Unit tests for {@link HTMLHtmlElement}.
 *
 * @author Daniel Gredler
 * @author Marc Guillemot
 * @author Frank Danek
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class HTMLHtmlElementTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("[object HTMLHtmlElement]")
    public void simpleScriptable() throws Exception {
        final String html = "<html id='myId'><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    alert(document.getElementById('myId'));\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        final WebDriver driver = loadPageWithAlerts2(html);
        if (driver instanceof HtmlUnitDriver) {
            final WebElement element = driver.findElement(By.id("myId"));
            assertTrue(toHtmlElement(element) instanceof HtmlHtml);
        }
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"[object HTMLHtmlElement]", "function HTMLHtmlElement() { [native code] }"},
            IE = {"[object HTMLHtmlElement]", "[object HTMLHtmlElement]"})
    public void HTMLHtmlElement_toString() throws Exception {
        final String html = "<html id='myId'><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    try {\n"
            + "      log(document.getElementById('myId'));\n"
            + "      log(HTMLHtmlElement);\n"
            + "    } catch (e) {\n"
            + "      log('exception');\n"
            + "    }\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"2", "HEAD", "BODY", "null", "null"})
    public void childNodes_1() throws Exception {
        // The whitespace in this HTML is very important, because we're verifying
        // that it doesn't get included in the childNodes collection.
        final String html = "<html> \n <body> \n <script>\n"
            + LOG_TITLE_FUNCTION
            + "var nodes = document.documentElement.childNodes;\n"
            + "log(nodes.length);\n"
            + "log(nodes[0].nodeName);\n"
            + "log(nodes[1].nodeName);\n"
            + "log(nodes[0].previousSibling);\n"
            + "log(nodes[1].nextSibling);\n"
            + "</script> \n </body> \n </html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"1", "HEAD"})
    public void childNodes_2() throws Exception {
        // The whitespace in this HTML is very important, because we're verifying
        // that it doesn't get included in the childNodes collection.
        final String html = "<html> \n <head> \n <script>\n"
            + LOG_TITLE_FUNCTION
            + "var nodes = document.documentElement.childNodes;\n"
            + "log(nodes.length);\n"
            + "log(nodes[0].nodeName);\n"
            + "</script> \n </head> \n </html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"true", "true", "true", "true"})
    public void clientWidth() throws Exception {
        final String html = "<!DOCTYPE HTML PUBLIC '-//W3C//DTD HTML 4.01 Transitional//EN'"
            + " 'http://www.w3.org/TR/html4/loose.dtd'>"
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  var elt = document.body.parentNode;\n"
            + "  log(elt.clientWidth > 0);\n"
            + "  log(elt.clientWidth == window.innerWidth);\n"
            + "  log(elt.clientHeight > 0);\n"
            + "  log(elt.clientHeight == window.innerHeight);\n"
            + "}\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Hello World")
    public void innerText() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    document.documentElement.innerText = 'Hello World';\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "</body></html>";

        final String js =
                "if (document.documentElement.childNodes.length == 0) { return '0'; }"
                + " return document.documentElement.childNodes.item(0).data;";

        final WebDriver driver = loadPage2(html);
        final String text = (String) ((JavascriptExecutor) driver).executeScript(js);
        assertEquals(getExpectedAlerts()[0], text);
    }

    /**
     * Test offsets (real values don't matter currently).
     * But we have to make sure this works without an exception
     * because parent is null.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"8", "16", "13", "0"},
            FF = {"8", "16", "0", "0"},
            FF_ESR = {"8", "16", "0", "0"},
            IE = {"687", "16", "0", "0"})
    @HtmlUnitNYI(CHROME = {"613", "1256", "13", "0"},
            EDGE = {"613", "1256", "13", "0"},
            FF = {"613", "1256", "13", "0"},
            FF_ESR = {"613", "1256", "13", "0"},
            IE = {"613", "1256", "13", "0"})
    public void offsetsHtmlAbsoluteLeft() throws Exception {
        offsetsHtml("position: absolute; left: 13px;");
    }

    /**
     * Test offsets (real values don't matter currently).
     * But we have to make sure this works without an exception
     * because parent is null.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"8", "16", "1229", "0"},
            FF = {"8", "16", "0", "0"},
            FF_ESR = {"8", "16", "0", "0"},
            IE = {"687", "16", "0", "0"})
    @HtmlUnitNYI(CHROME = {"613", "1256", "1243", "0"},
            EDGE = {"613", "1256", "1243", "0"},
            FF = {"613", "1256", "1243", "0"},
            FF_ESR = {"613", "1256", "1243", "0"},
            IE = {"613", "1256", "1243", "0"})
    public void offsetsHtmlAbsoluteRight() throws Exception {
        offsetsHtml("position: absolute; right: 13px;");
    }

    /**
     * Test offsets (real values don't matter currently).
     * But we have to make sure this works without an exception
     * because parent is null.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"8", "16", "0", "0"},
            IE = {"687", "16", "0", "0"})
    @HtmlUnitNYI(CHROME = {"613", "1256", "0", "0"},
            EDGE = {"613", "1256", "0", "0"},
            FF = {"613", "1256", "0", "0"},
            FF_ESR = {"613", "1256", "0", "0"},
            IE = {"613", "1256", "0", "0"})
    public void offsetsHtmlFixed() throws Exception {
        offsetsHtml("position: fixed;");
    }

    /**
     * Test offsets (real values don't matter currently).
     * But we have to make sure this works without an exception
     * because parent is null.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"8", "16", "1229", "0"},
            FF = {"8", "16", "0", "0"},
            FF_ESR = {"8", "16", "0", "0"},
            IE = {"687", "16", "0", "0"})
    @HtmlUnitNYI(CHROME = {"613", "1256", "1243", "0"},
            EDGE = {"613", "1256", "1243", "0"},
            FF = {"613", "1256", "1243", "0"},
            FF_ESR = {"613", "1256", "1243", "0"},
            IE = {"613", "1256", "1243", "0"})
    public void offsetsHtmlFixedRight() throws Exception {
        offsetsHtml("position: fixed; right: 13px;");
    }

    private void offsetsHtml(final String style) throws Exception {
        final String html = "<html id='my' style='" + style + "'>\n"
              + "<head></head>\n"
              + "<body>\n"
              + "</div></body>\n"
              + "<script>\n"
              + LOG_TITLE_FUNCTION
              + "function alertOffsets(elt) {\n"
              + "  log(elt.offsetHeight);\n"
              + "  log(elt.offsetWidth);\n"
              + "  log(elt.offsetLeft);\n"
              + "  log(elt.offsetTop);\n"
              + "}\n"

              + "alertOffsets(document.getElementById('my'));\n"
              + "</script></body></html>";
        loadPageVerifyTitle2(html);
    }
}
