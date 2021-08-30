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
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.html.HtmlHtml;

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
    @Alerts(DEFAULT = {"[object HTMLHtmlElement]", "function HTMLHtmlElement() {\n    [native code]\n}"},
            CHROME = {"[object HTMLHtmlElement]", "function HTMLHtmlElement() { [native code] }"},
            EDGE = {"[object HTMLHtmlElement]", "function HTMLHtmlElement() { [native code] }"},
            IE = {"[object HTMLHtmlElement]", "[object HTMLHtmlElement]"})
    public void HTMLHtmlElement_toString() throws Exception {
        final String html = "<html id='myId'><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    try {\n"
            + "      alert(document.getElementById('myId'));\n"
            + "      alert(HTMLHtmlElement);\n"
            + "    } catch (e) {\n"
            + "      alert('exception');\n"
            + "    }\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
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
            + "var nodes = document.documentElement.childNodes;\n"
            + "alert(nodes.length);\n"
            + "alert(nodes[0].nodeName);\n"
            + "alert(nodes[1].nodeName);\n"
            + "alert(nodes[0].previousSibling);\n"
            + "alert(nodes[1].nextSibling);\n"
            + "</script> \n </body> \n </html>";
        loadPageWithAlerts2(html);
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
            + "var nodes = document.documentElement.childNodes;\n"
            + "alert(nodes.length);\n"
            + "alert(nodes[0].nodeName);\n"
            + "</script> \n </head> \n </html>";
        loadPageWithAlerts2(html);
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
            + "function test() {\n"
            + "  var elt = document.body.parentNode;\n"
            + "  alert(elt.clientWidth > 0);\n"
            + "  alert(elt.clientWidth == window.innerWidth);\n"
            + "  alert(elt.clientHeight > 0);\n"
            + "  alert(elt.clientHeight == window.innerHeight);\n"
            + "}\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
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
}
