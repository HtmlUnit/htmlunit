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

import static com.gargoylesoftware.htmlunit.BrowserRunner.TestedBrowser.FF;
import static com.gargoylesoftware.htmlunit.BrowserRunner.TestedBrowser.FF78;
import static com.gargoylesoftware.htmlunit.BrowserRunner.TestedBrowser.IE;
import static java.nio.charset.StandardCharsets.ISO_8859_1;

import java.net.URL;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;
import com.gargoylesoftware.htmlunit.HttpHeader;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.util.MimeType;

/**
 * Tests for {@link HtmlPage}.
 *
 * @author Ahmed Ashour
 * @author Marc Guillemot
 * @author Ronald Brill
 * @author Frank Danek
 * @author Joerg Werner
 */
@RunWith(BrowserRunner.class)
public class HtmlPage3Test extends WebDriverTestCase {

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void emptyJavaScript() throws Exception {
        final String html = "<body>\n"
            + "<a id='myAnchor' href='javascript:'>Hello</a>\n"
            + "</body>";

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("myAnchor")).click();
    }

    /**
     * Test for Bug #1291.
     * @throws Exception if an error occurs
     */
    @Test
    public void formElementCreatedFromJavascript() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "<script type='text/javascript'>\n"
            + "  function modifyForm() {\n"
            + "    var myForm = document.forms['test_form'];\n"
            + "    var el = document.createElement('input');\n"
            + "    el.setAttribute('addedBy','js');\n"
            + "    el.name = 'myHiddenField';\n"
            + "    el.value = 'myValue';\n"
            + "    el.type = 'hidden';\n"
            + "    myForm.appendChild(el);\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onLoad='modifyForm()'>\n"
            + "  <form id='test_form' action='http://www.sourceforge.com/' method='post'>\n"
            + "    <input type='submit' value='click'/>\n"
            + "  </form>\n"
            + "</body>\n"
            + "</html>";

        final WebDriver driver = loadPage2(html);
        final List<WebElement> elements = driver.findElements(By.xpath("//*"));
        assertEquals(7, elements.size());

        assertEquals("html", elements.get(0).getTagName());
        assertEquals("head", elements.get(1).getTagName());
        assertEquals("script", elements.get(2).getTagName());
        assertEquals("body", elements.get(3).getTagName());
        assertEquals("form", elements.get(4).getTagName());
        assertEquals("input", elements.get(5).getTagName());

        final WebElement input = elements.get(6);
        assertEquals("input", input.getTagName());
        assertEquals("myHiddenField", input.getAttribute("name"));
        assertEquals("js", input.getAttribute("addedBy"));
        assertEquals("js", input.getAttribute("addedby"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"windows-1252", "windows-1252", "windows-1252", "undefined"},
            IE = {"ISO-8859-1", "iso-8859-1", "iso-8859-1", "windows-1252"})
    public void getPageEncoding() throws Exception {
        final String htmlContent = "<html><head>\n"
            + "  <title>foo</title>\n"
            + "  <meta http-equiv='Content-Type' content='text/html; charset=Shift_JIS'>\n"
            + "  <script>\n"
            + "    function test() {\n"
            + "      alert(document.inputEncoding);\n"
            + "      alert(document.characterSet);\n"
            + "      alert(document.charset);\n"
            + "      alert(document.defaultCharset);\n"
            + "    }\n"
            + "  </script>\n"
            + "</head><body onload='test()'>\n"
            + "<table><tr><td>\n"
            + "<meta name=vs_targetSchema content=\"http://schemas.microsoft.com/intellisense/ie5\">\n"
            + "<form name='form1'>\n"
            + "  <input type='text' name='textfield1' id='textfield1' value='foo' />\n"
            + "  <input type='text' name='textfield2' id='textfield2'/>\n"
            + "</form>\n"
            + "</td></tr></table>\n"
            + "</body></html>";
        loadPageWithAlerts2(htmlContent);
    }

    /**
     * Regression test for {@code window.onload} property.
     * @throws Exception if the test fails
     */
    @Test
    public void onLoadHandler_ScriptNameRead() throws Exception {
        final String html = "<html><head><title>foo</title>\n"
            + "<script type='text/javascript'>\n"
            + "  load = function() {};\n"
            + "  onload = load;\n"
            + "  alert(onload);\n"
            + "</script></head><body></body></html>";

        final WebDriver driver = loadPage2(html);
        final List<String> alerts = getCollectedAlerts(driver, 1);
        assertEquals(1, alerts.size());
        assertTrue(alerts.get(0).startsWith("function"));
    }

    /**
     * @exception Exception If the test fails
     */
    @Test
    public void constructor() throws Exception {
        final String html = "<html>\n"
            + "<head><title>foo</title></head>\n"
            + "<body>\n"
            + "<p>hello world</p>\n"
            + "<form id='form1' action='/formSubmit' method='post'>\n"
            + "  <input type='text' NAME='textInput1' value='textInput1'/>\n"
            + "  <input type='text' name='textInput2' value='textInput2'/>\n"
            + "  <input type='hidden' name='hidden1' value='hidden1'/>\n"
            + "  <input type='submit' name='submitInput1' value='push me'/>\n"
            + "</form>\n"
            + "</body></html>";

        final WebDriver driver = loadPageWithAlerts2(html);
        assertTitle(driver, "foo");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void getInputByName() throws Exception {
        final String html = "<html>\n"
            + "<head><title>foo</title></head>\n"
            + "<body>\n"
            + "<p>hello world</p>\n"
            + "<form id='form1' action='/formSubmit' method='post'>\n"
            + "  <input type='text' NAME='textInput1' value='textInput1'/>\n"
            + "  <input type='text' name='textInput2' value='textInput2'/>\n"
            + "  <input type='hidden' name='hidden1' value='hidden1'/>\n"
            + "  <input type='submit' name='submitInput1' value='push me'/>\n"
            + "</form>\n"
            + "</body></html>";

        final WebDriver driver = loadPageWithAlerts2(html);

        final WebElement form = driver.findElement(By.id("form1"));
        final WebElement input = form.findElement(By.name("textInput1"));
        assertEquals("name", "textInput1", input.getAttribute("name"));

        assertEquals("value", "textInput1", input.getAttribute("value"));
        assertEquals("type", "text", input.getAttribute("type"));
    }

    /**
     * @exception Exception if the test fails
     */
    @Test
    @Alerts({"[object HTMLInputElement]", "1"})
    public void write_getElementById_afterParsing() throws Exception {
        final String html = "<html>\n"
            + "<head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    document.write(\"<input id='sendemail'>\");\n"
            + "    alert(document.getElementById('sendemail'));\n"
            + "    document.write(\"<input name='sendemail2'>\");\n"
            + "    alert(document.getElementsByName('sendemail2').length);\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "</body></html>";

        // [IE] real IE waits for the page to load until infinity
        if (useRealBrowser() && getBrowserVersion().isIE()) {
            Assert.fail("Blocks real IE");
        }

        loadPageWithAlerts2(html);
    }

    /**
     * @exception Exception if the test fails
     */
    @Test
    @Alerts({"[object HTMLInputElement]", "1"})
    public void write_getElementById_duringParsing() throws Exception {
        final String html = "<html>\n"
            + "<head><title>foo</title></head>\n"
            + "<body><script>\n"
            + "  document.write(\"<input id='sendemail'>\");\n"
            + "  alert(document.getElementById('sendemail'));\n"
            + "  document.write(\"<input name='sendemail2'>\");\n"
            + "  alert(document.getElementsByName('sendemail2').length);\n"
            + "</script></body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Hello")
    public void application_javascript_type() throws Exception {
        final String html = "<html>\n"
            + "<body>\n"
            + "  <script type='application/javascript'>\n"
            + "    alert('Hello');\n"
            + "  </script>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Hello")
    public void application_x_javascript_type() throws Exception {
        final String html = "<html>\n"
            + "<body>\n"
            + "  <script type='application/x-javascript'>\n"
            + "    alert('Hello');\n"
            + "  </script>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void basePath()  throws Exception {
        basePath("base_path", URL_SECOND + "path");
    }

    private void basePath(final String baseUrl, final String expected) throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "  <base href='" + baseUrl + "'>\n"
            + "</head>\n"
            + "<body>\n"
            + "  <a id='testLink' href='path'>click me</a>\n"
            + "</body></html>";
        getMockWebConnection().setDefaultResponse("Error: not found", 404, "Not Found", MimeType.TEXT_HTML);
        final WebDriver webDriver = loadPage2(html, URL_SECOND);
        webDriver.findElement(By.id("testLink")).click();
        assertEquals(expected, webDriver.getCurrentUrl());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void basePathAndSlash()  throws Exception {
        basePath("base_path/", URL_SECOND + "base_path/path");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void basePathAfterSlash()  throws Exception {
        basePath("/base_path", "http://localhost:" + PORT + "/path");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void basePathSlashes()  throws Exception {
        basePath("/base_path/", URL_FIRST + "base_path/path");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void basePathFullyQualified()  throws Exception {
        basePath("http://localhost:" + PORT + "/base_path", "http://localhost:" + PORT + "/path");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void basePathFullyQualifiedSlash()  throws Exception {
        basePath("http://localhost:" + PORT + "/base_path/", "http://localhost:" + PORT + "/base_path/path");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    //TODO: fails with ChromeDriver if run with other tests
    public void basePathNoProtocol()  throws Exception {
        basePath("//localhost:" + PORT + "/base_path", "http://localhost:" + PORT + "/path");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void basePathNoProtocolSlash()  throws Exception {
        basePath("//localhost:" + PORT + "/base_path/", "http://localhost:" + PORT + "/base_path/path");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void basePathInvalid()  throws Exception {
        basePath("---****://==", URL_SECOND + "---****://path");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void basePathLeadingAndTrailingWhitespace()  throws Exception {
        basePath(" \t\n" + "http://localhost:" + PORT + "/base_path/" + "\n\t ",
                "http://localhost:" + PORT + "/base_path/path");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void basePathEmpty()  throws Exception {
        basePath("", "http://localhost:" + PORT + "/second/path");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void basePathWhitespaceOnly()  throws Exception {
        basePath(" \t\n ", "http://localhost:" + PORT + "/second/path");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[object SVGSVGElement]", "http://www.w3.org/2000/svg",
            "[object SVGRectElement]", "http://www.w3.org/2000/svg"})
    public void htmlPageEmbeddedSvgWithoutNamespace() throws Exception {
        final String content
            = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"
            + "<head>\n"
            + "<title>foo</title>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    alert(document.getElementById('mySvg'));\n"
            + "    alert(document.getElementById('mySvg').namespaceURI);\n"
            + "    alert(document.getElementById('myRect'));\n"
            + "    alert(document.getElementById('myRect').namespaceURI);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <svg id='mySvg'>\n"
            + "    <rect id='myRect' />\n"
            + "  </svg>\n"
            + "</body>\n"
            + "</html>";

        loadPageWithAlerts2(content);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("HTML")
    public void htmlPage() throws Exception {
        final String content
            = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"
            + "<svg xmlns=\"http://www.w3.org/2000/svg\">\n"
            + "  <rect id='rect' width='50' height='50' fill='green' />\n"
            + "<head>\n"
            + "<title>foo</title>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    alert(document.documentElement.tagName);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "</body>\n"
            + "</svg>";

        loadPageWithAlerts2(content);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("[object HTMLHtmlElement]")
    public void htmlSvgPage() throws Exception {
        final String content
            = "<html xmlns=\"http://www.w3.org/2000/svg\">\n"
            + "  <rect id='rect' width='50' height='50' fill='green' />\n"
            + "<body>\n"
            + "<script>\n"
            + "  alert(document.documentElement);\n"
            + "</script>\n"
            + "</body>\n"
            + "</html>";

        loadPageWithAlerts2(content);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "error",
            CHROME = "Something",
            EDGE = "Something")
    @NotYetImplemented({IE, FF, FF78})
    public void shouldBeAbleToFindElementByXPathInXmlDocument() throws Exception {
        final String html = "<?xml version='1.0' encoding='UTF-8'?>\n"
            + "<html xmlns='http://www.w3.org/1999/xhtml'\n"
            + "      xmlns:svg='http://www.w3.org/2000/svg'\n"
            + "      xmlns:xlink='http://www.w3.org/1999/xlink'>\n"
            + "<body>\n"
            + "  <svg:svg id='chart_container' height='220' width='400'>\n"
            + "    <svg:text y='16' x='200' text-anchor='middle'>Something</svg:text>\n"
            + "  </svg:svg>\n"
            + "</body>\n"
            + "</html>\n";

        final WebDriver driver = loadPage2(html, URL_FIRST, "application/xhtml+xml", ISO_8859_1, null);
        String actual;
        try {
            final WebElement element = driver.findElement(By.xpath("//svg:svg//svg:text"));
            actual = element.getText();
        }
        catch (final NoSuchElementException e) {
            actual = "error";
        }
        assertEquals(getExpectedAlerts()[0], actual);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("interactive")
    public void readyStateInDOMContentLoaded() throws Exception {
        final String html =
                "<html>\n"
                + "  <head>\n"
                + "    <script>\n"
                + "      document.addEventListener('DOMContentLoaded', function () {\n"
                + "        alert(document.readyState);\n"
                + "      });\n"
                + "    </script>\n"
                + "  </head>\n"
                + "  <body>test</body>\n"
                + "</html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("25")
    public void loadExternalJavaScript() throws Exception {
        final String html =
            "<html><head>\n"
            + "<script>\n"
            + "function makeIframe() {\n"
            + "  var iframesrc = '<html><head>';\n"
            + "  iframesrc += '<script src=\"" + "js.js" + "\"></' + 'script>';\n"
            + "  iframesrc += '<script>';\n"
            + "  iframesrc += 'function doSquared() {';\n"
            + "  iframesrc += '    try {';\n"
            + "  iframesrc += '      var y = squared(5);';\n"
            + "  iframesrc += '      alert(y);';\n"
            + "  iframesrc += '    } catch (e) {';\n"
            + "  iframesrc += '      alert(\"error\");';\n"
            + "  iframesrc += '    }';\n"
            + "  iframesrc += '}';\n"
            + "  iframesrc += '</' + 'script>';\n"
            + "  iframesrc += '</head>';\n"
            + "  iframesrc += '<body onLoad=\"doSquared()\" >';\n"
            + "  iframesrc += '</body>';\n"
            + "  iframesrc += '</html>';\n"
            + "  var iframe = document.createElement('IFRAME');\n"
            + "  iframe.id = 'iMessage';\n"
            + "  iframe.name = 'iMessage';\n"
            + "  iframe.src = \"javascript:'\" + iframesrc + \"'\";\n"
            + "  document.body.appendChild(iframe);\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='makeIframe()'>\n"
            + "</body></html>";

        final String js = "function squared(n) {return n * n}";

        getMockWebConnection().setResponse(URL_FIRST, html);
        getMockWebConnection().setResponse(new URL(URL_FIRST, "js.js"), js);

        loadPageWithAlerts2(URL_FIRST);

        assertEquals(2, getMockWebConnection().getRequestCount());

        final Map<String, String> lastAdditionalHeaders = getMockWebConnection().getLastAdditionalHeaders();
        assertNull(lastAdditionalHeaders.get(HttpHeader.REFERER));
    }

    /**
     * Differs from {@link #loadExternalJavaScript()} by the absolute reference of the javascript source.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("25")
    public void loadExternalJavaScript_absolute() throws Exception {
        final String html =
            "<html><head>\n"
            + "<script>\n"
            + "function makeIframe() {\n"
            + "  var iframesrc = '<html><head>';\n"
            + "  iframesrc += '<script src=\"" + URL_SECOND + "\"></' + 'script>';\n"
            + "  iframesrc += '<script>';\n"
            + "  iframesrc += 'function doSquared() {';\n"
            + "  iframesrc += '    try {';\n"
            + "  iframesrc += '      var y = squared(5);';\n"
            + "  iframesrc += '      alert(y);';\n"
            + "  iframesrc += '    } catch (e) {';\n"
            + "  iframesrc += '      alert(\"error\");';\n"
            + "  iframesrc += '    }';\n"
            + "  iframesrc += '}';\n"
            + "  iframesrc += '</' + 'script>';\n"
            + "  iframesrc += '</head>';\n"
            + "  iframesrc += '<body onLoad=\"doSquared()\" >';\n"
            + "  iframesrc += '</body>';\n"
            + "  iframesrc += '</html>';\n"
            + "  var iframe = document.createElement('IFRAME');\n"
            + "  iframe.id = 'iMessage';\n"
            + "  iframe.name = 'iMessage';\n"
            + "  iframe.src = \"javascript:'\" + iframesrc + \"'\";\n"
            + "  document.body.appendChild(iframe);\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='makeIframe()'>\n"
            + "</body></html>";

        final String js = "function squared(n) {return n * n}";

        getMockWebConnection().setResponse(URL_FIRST, html);
        getMockWebConnection().setResponse(URL_SECOND, js);

        loadPageWithAlerts2(URL_FIRST);

        assertEquals(2, getMockWebConnection().getRequestCount());

        final Map<String, String> lastAdditionalHeaders = getMockWebConnection().getLastAdditionalHeaders();
        assertNull(lastAdditionalHeaders.get(HttpHeader.REFERER));
    }
}
