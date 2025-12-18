/*
 * Copyright (c) 2002-2025 Gargoyle Software Inc.
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
package org.htmlunit.javascript.host.xml;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.zip.GZIPOutputStream;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.htmlunit.HttpHeader;
import org.htmlunit.WebDriverTestCase;
import org.htmlunit.WebRequest;
import org.htmlunit.junit.annotation.Alerts;
import org.htmlunit.junit.annotation.HtmlUnitNYI;
import org.htmlunit.util.MimeType;
import org.htmlunit.util.NameValuePair;
import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.RetryingTest;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.opentest4j.AssertionFailedError;

/**
 * Tests for {@link XMLHttpRequest}.
 *
 * @author Daniel Gredler
 * @author Marc Guillemot
 * @author Ahmed Ashour
 * @author Stuart Begg
 * @author Sudhan Moghe
 * @author Sebastian Cato
 * @author Ronald Brill
 * @author Frank Danek
 * @author Jake Cobb
 */
public class XMLHttpRequestTest extends WebDriverTestCase {

    private static final String UNINITIALIZED = String.valueOf(XMLHttpRequest.UNSENT);
    private static final String LOADING = String.valueOf(XMLHttpRequest.OPENED);
    private static final String COMPLETED = String.valueOf(XMLHttpRequest.DONE);

    /**
     * Tests synchronous use of XMLHttpRequest.
     * @throws Exception if the test fails
     */
    @RetryingTest(3)
    public void syncUse() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + LOG_TITLE_FUNCTION_NORMALIZE
            + "      function testSync() {\n"
            + "        var request = new XMLHttpRequest();\n"
            + "        log(request.readyState);\n"
            + "        log(request.responseType);\n"
            + "        request.open('GET', '" + URL_SECOND + "', false);\n"
            + "        log(request.readyState);\n"
            + "        request.send('');\n"
            + "        log(request.readyState);\n"
            + "        log(request.responseText);\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='testSync()'>\n"
            + "  </body>\n"
            + "</html>";

        final String xml =
              "<xml>\n"
            + "<content>blah</content>\n"
            + "<content>blah2</content>\n"
            + "</xml>";

        setExpectedAlerts(UNINITIALIZED, "", LOADING, COMPLETED, xml.replace("\n", "\\n"));
        getMockWebConnection().setDefaultResponse(xml, MimeType.TEXT_XML);

        loadPageVerifyTitle2(html);
    }

    /**
     * Tests style object creation.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("[object XMLHttpRequest]")
    public void creation() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + LOG_TITLE_FUNCTION
            + "        log(new XMLHttpRequest());\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body></body>\n"
            + "</html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"1: 0-", "2: ", "3: 200-OK"})
    public void statusSync() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "  <head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  var xhr = new XMLHttpRequest();\n"

            + "  alertStatus('1: ');\n"
            + "  xhr.open('GET', '/foo.xml', false);\n"
            + "  log('2: ');\n"

            + "  xhr.send();\n"
            + "  alertStatus('3: ');\n"

            + "  function alertStatus(prefix) {\n"
            + "    var msg = prefix;\n"
            + "    try {\n"
            + "      msg = msg + xhr.status + '-';\n"
            + "    } catch(e) { msg = msg + 'ex: status' + '-' }\n"
            + "    try {\n"
            + "      msg = msg + xhr.statusText;\n"
            + "    } catch(e) { msg = msg + 'ex: statusText' }\n"
            + "    log(msg);\n"
            + "  }\n"
            + "</script>\n"
            + "  </head>\n"
            + "  <body></body>\n"
            + "</html>";

        getMockWebConnection().setDefaultResponse("<res></res>", MimeType.TEXT_XML);
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"1: 0-", "2: 0-", "#1: 0-", "3: 0-", "4: 0-", "#2: 200-OK", "#3: 200-OK", "#4: 200-OK"})
    public void statusAsync() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "  <head>\n"
            + "    <title>XMLHttpRequest Test</title>\n"
            + "<script>\n"
            + "  var xhr = new XMLHttpRequest();\n"

            + "  function test() {\n"
            + "    try {\n"
            + "      logStatus('1: ');\n"

            + "      xhr.onreadystatechange = onReadyStateChange;\n"
            + "      logStatus('2: ');\n"

            + "      xhr.open('GET', '/foo.xml', true);\n"
            + "      logStatus('3: ');\n"

            + "      xhr.send();\n"
            + "      logStatus('4: ');\n"
            + "    } catch(e) {\n"
            + "      document.getElementById('log').value += e + '\\n';\n"
            + "    }\n"
            + "  }\n"

            + "  function onReadyStateChange() {\n"
            + "    logStatus('#' + xhr.readyState + ': ');\n"
            + "  }\n"

            + "  function logStatus(prefix) {\n"
            + "    var msg = prefix;\n"
            + "    try {\n"
            + "      msg = msg + xhr.status + '-';\n"
            + "    } catch(e) { msg = msg + 'ex: status' + '-' }\n"
            + "    try {\n"
            + "      msg = msg + xhr.statusText;\n"
            + "    } catch(e) { msg = msg + 'ex: statusText' }\n"
            + "    document.getElementById('log').value += msg + '\\n';\n"
            + "  }\n"
            + "</script>\n"
            + "  </head>\n"
            + "  <body onload='test()'>\n"
            + "    <textarea id='log' cols='80' rows='40'></textarea>\n"
            + "  </body>\n"
            + "</html>";

        getMockWebConnection().setDefaultResponse("<res></res>", MimeType.TEXT_XML);
        final WebDriver driver = loadPage2(html);

        final String expected = String.join("\n", getExpectedAlerts());
        assertLog(driver, expected);
    }

    private static void assertLog(final WebDriver driver, final String expected) throws InterruptedException {
        final long maxWait = System.currentTimeMillis() + DEFAULT_WAIT_TIME.toMillis();
        while (true) {
            try {
                final String text = driver
                                        .findElement(By.id("log"))
                                        .getDomProperty("value")
                                        .trim()
                                        .replaceAll("\r", "");
                assertEquals(expected, text);
                return;
            }
            catch (final AssertionFailedError e) {
                if (System.currentTimeMillis() > maxWait) {
                    throw e;
                }
                Thread.sleep(10);
            }
        }
    }

    /**
     * Checks that not passing the async flag to <code>open()</code>
     * results in async execution.  If this gets interpreted as {@code false}
     * then you will see the alert order 1-2-4-3 instead of 1-2-3-4.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"#1", "#2", "#3", "#4"})
    public void asyncIsDefault() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<body>\n"
            + "  <textarea id='log' cols='80' rows='40'></textarea>\n"

            + "<script>\n"
            + "    function log(x) {\n"
            + "      document.getElementById('log').value += x + '\\n';\n"
            + "    }\n"

            + "var xhr = new XMLHttpRequest();\n"

            + "function onReadyStateChange() {\n"
            + "  if( xhr.readyState == 4 ) {\n"
            + "    log('#4');\n"
            + "  }\n"
            + "}\n"

            + "try {\n"
            + "  log('#1');\n"
            + "  xhr.onreadystatechange = onReadyStateChange;\n"
            + "  xhr.open('GET', '/foo.xml');\n"
            + "  log('#2');\n"
            + "  xhr.send();\n"
            + "  log('#3');\n"
            + "} catch(e) { log(e); }\n"
            + "</script>\n"
            + "</body></html>";

        getMockWebConnection().setDefaultResponse("<res></res>", MimeType.TEXT_XML);
        final WebDriver driver = loadPage2(html);

        final String expected = String.join("\n", getExpectedAlerts());
        assertLog(driver, expected);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"orsc1", "open-done", "send-done",
             "orsc2", "orsc3", "orsc4", "4", "<a>b</a>", "[object XMLHttpRequest]"})
    public void onload() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + "      function log(x) {\n"
            + "        document.getElementById('log').value += x + '\\n';\n"
            + "      }\n"

            + "      function test() {\n"
            + "        var xhr = new XMLHttpRequest();\n"

            + "        xhr.onreadystatechange = function() { log('orsc' + xhr.readyState); };\n"
            + "        xhr.onload = function() { log(xhr.readyState); log(xhr.responseText); log(this); }\n"

            + "        xhr.open('GET', '/foo.xml', true);\n"
            + "        log('open-done');\n"

            + "        xhr.send('');\n"
            + "        log('send-done');\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='test()'>\n"
            + "    <textarea id='log' cols='80' rows='40'></textarea>\n"
            + "  </body>\n"
            + "</html>";

        final String xml = "<a>b</a>";

        getMockWebConnection().setDefaultResponse(xml, MimeType.TEXT_XML);
        final WebDriver driver = loadPage2(html);

        final String expected = String.join("\n", getExpectedAlerts());
        assertLog(driver, expected);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"null", "null"})
    public void responseHeaderBeforeSend() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + LOG_TITLE_FUNCTION
            + "        var request = new XMLHttpRequest();\n"

            + "        log(request.getResponseHeader('content-length'));\n"
            + "        request.open('GET', '/foo.xml', false);\n"
            + "        log(request.getResponseHeader('content-length'));\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body></body>\n"
            + "</html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * Regression test for http://sourceforge.net/p/htmlunit/bugs/269/.
     * @throws Exception if the test fails
     */
    @Test
    public void relativeUrl() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + LOG_TITLE_FUNCTION_NORMALIZE
            + "      function testSync() {\n"
            + "        var request = new XMLHttpRequest();\n"
            + "        request.open('GET', '/foo.xml', false);\n"
            + "        request.send('');\n"
            + "        log(request.readyState);\n"
            + "        log(request.responseText);\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='testSync()'>\n"
            + "  </body>\n"
            + "</html>";

        final String xml =
              "<xml>\n"
            + "<content>blah</content>\n"
            + "<content>blah2</content>\n"
            + "</xml>";

        setExpectedAlerts(COMPLETED, xml.replace("\n", "\\n"));
        getMockWebConnection().setDefaultResponse(xml, MimeType.TEXT_XML);
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("bla bla")
    public void responseText_NotXml() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  var request = new XMLHttpRequest();\n"
            + "  request.open('GET', 'foo.txt', false);\n"
            + "  request.send('');\n"
            + "  log(request.responseText);\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'></body></html>";

        getMockWebConnection().setDefaultResponse("bla bla", MimeType.TEXT_PLAIN);
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("null")
    public void responseXML_text_html() throws Exception {
        responseXML("<html></html>", MimeType.TEXT_HTML, false, false);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("null")
    public void responseXMLAsync_text_html() throws Exception {
        responseXML("<html></html>", MimeType.TEXT_HTML, true, false);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[object HTMLDocument]", "[object HTMLHtmlElement]", "HTML", "§§URL§§foo"})
    public void responseXMLAsyncDocument_text_html() throws Exception {
        responseXML("<html></html>", MimeType.TEXT_HTML, true, true);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("null")
    public void responseXML_text_html_empty() throws Exception {
        responseXML("", MimeType.TEXT_HTML, false, false);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("null")
    public void responseXMLAsync_text_html_empty() throws Exception {
        responseXML("", MimeType.TEXT_HTML, true, false);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[object HTMLDocument]", "[object HTMLHtmlElement]", "HTML", "§§URL§§foo"})
    public void responseXMLAsyncDocument_text_html_empty() throws Exception {
        responseXML("", MimeType.TEXT_HTML, true, true);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("null")
    public void responseXML_text_html_blank() throws Exception {
        responseXML("    ", MimeType.TEXT_HTML, false, false);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("null")
    public void responseXMLAsync_text_html_blank() throws Exception {
        responseXML("    ", MimeType.TEXT_HTML, true, false);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[object HTMLDocument]", "[object HTMLHtmlElement]", "HTML", "§§URL§§foo"})
    public void responseXMLAsyncDocument_text_html_blank() throws Exception {
        responseXML("    ", MimeType.TEXT_HTML, true, true);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[object XMLDocument]", "[object Element]", "note", "§§URL§§foo"})
    public void responseXML_text_xml() throws Exception {
        responseXML("<note/>", MimeType.TEXT_XML, false, false);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[object XMLDocument]", "[object Element]", "note", "§§URL§§foo"})
    public void responseXMLAsync_text_xml() throws Exception {
        responseXML("<note/>", MimeType.TEXT_XML, true, false);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[object XMLDocument]", "[object Element]", "note", "§§URL§§foo"})
    public void responseXMLAsyncDocument_text_xml() throws Exception {
        responseXML("<note/>", MimeType.TEXT_XML, true, true);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("null")
    public void responseXML_text_xml_empty() throws Exception {
        responseXML("", MimeType.TEXT_XML, false, false);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("null")
    public void responseXMLAsync_text_xml_empty() throws Exception {
        responseXML("", MimeType.TEXT_XML, true, false);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("null")
    public void responseXMLAsyncDocument_text_xml_empty() throws Exception {
        responseXML("", MimeType.TEXT_XML, true, true);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("null")
    public void responseXML_text_xml_blank() throws Exception {
        responseXML("    ", MimeType.TEXT_XML, false, false);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("null")
    public void responseXMLAsync_text_xml_blank() throws Exception {
        responseXML("    ", MimeType.TEXT_XML, true, false);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("null")
    public void responseXMLAsyncDocument_text_xml_blank() throws Exception {
        responseXML("    ", MimeType.TEXT_XML, true, true);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[object XMLDocument]", "[object Element]", "note", "§§URL§§foo"})
    public void responseXML_application_xml() throws Exception {
        responseXML("<note/>", MimeType.APPLICATION_XML, false, false);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[object XMLDocument]", "[object Element]", "note", "§§URL§§foo"})
    public void responseXMLAsync_application_xml() throws Exception {
        responseXML("<note/>", MimeType.APPLICATION_XML, true, false);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[object XMLDocument]", "[object Element]", "note", "§§URL§§foo"})
    public void responseXMLAsyncDocument_application_xml() throws Exception {
        responseXML("<note/>", MimeType.APPLICATION_XML, true, true);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[object XMLDocument]", "[object Element]", "html", "§§URL§§foo"})
    public void responseXML_application_xhtmlXml() throws Exception {
        responseXML("<html/>", MimeType.APPLICATION_XHTML, false, false);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[object XMLDocument]", "[object Element]", "html", "§§URL§§foo"})
    public void responseXMLAsync_application_xhtmlXml() throws Exception {
        responseXML("<html/>", MimeType.APPLICATION_XHTML, true, false);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[object XMLDocument]", "[object Element]", "html", "§§URL§§foo"})
    public void responseXMLAsyncDocument_application_xhtmlXml() throws Exception {
        responseXML("<html/>", MimeType.APPLICATION_XHTML, true, true);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[object XMLDocument]", "[object SVGSVGElement]", "svg", "§§URL§§foo"})
    @HtmlUnitNYI(CHROME = {"[object XMLDocument]", "[object SVGElement]", "svg", "§§URL§§foo"},
            EDGE = {"[object XMLDocument]", "[object SVGElement]", "svg", "§§URL§§foo"},
            FF = {"[object XMLDocument]", "[object SVGElement]", "svg", "§§URL§§foo"},
            FF_ESR = {"[object XMLDocument]", "[object SVGElement]", "svg", "§§URL§§foo"})
    public void responseXML_application_svgXml() throws Exception {
        responseXML("<svg xmlns=\"http://www.w3.org/2000/svg\"/>", "image/svg+xml", false, false);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[object XMLDocument]", "[object SVGSVGElement]", "svg", "§§URL§§foo"})
    @HtmlUnitNYI(CHROME = {"[object XMLDocument]", "[object SVGElement]", "svg", "§§URL§§foo"},
            EDGE = {"[object XMLDocument]", "[object SVGElement]", "svg", "§§URL§§foo"},
            FF = {"[object XMLDocument]", "[object SVGElement]", "svg", "§§URL§§foo"},
            FF_ESR = {"[object XMLDocument]", "[object SVGElement]", "svg", "§§URL§§foo"})
    public void responseXMLAsync_application_svgXml() throws Exception {
        responseXML("<svg xmlns=\"http://www.w3.org/2000/svg\"/>", "image/svg+xml", true, false);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[object XMLDocument]", "[object SVGSVGElement]", "svg", "§§URL§§foo"})
    @HtmlUnitNYI(CHROME = {"[object XMLDocument]", "[object SVGElement]", "svg", "§§URL§§foo"},
            EDGE = {"[object XMLDocument]", "[object SVGElement]", "svg", "§§URL§§foo"},
            FF = {"[object XMLDocument]", "[object SVGElement]", "svg", "§§URL§§foo"},
            FF_ESR = {"[object XMLDocument]", "[object SVGElement]", "svg", "§§URL§§foo"})
    public void responseXMLAsyncDocument_application_svgXml() throws Exception {
        responseXML("<svg xmlns=\"http://www.w3.org/2000/svg\"/>", "image/svg+xml", true, true);
    }

    private void responseXML(final String resp, final String mimeType,
                    final boolean async, final boolean document) throws Exception {
        String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var xhr = new XMLHttpRequest();\n";

        if (document) {
            html +=  "    xhr.responseType = 'document';";
        }
        if (async) {
            html += "    xhr.onreadystatechange = () => {\n"
                    + "      if (xhr.readyState === 4) {\n"
                    + "        let respXml = xhr.responseXML;\n"
                    + "        log(respXml);\n"
                    + "        if (respXml !== null) {\n"
                    + "          log(respXml.documentElement);\n"
                    + "          log(respXml.documentElement.tagName);\n"
                    + "          log(respXml.documentElement.baseURI);\n"
                    + "        }\n"
                    + "      }\n"
                    + "    }\n"
                    + "    xhr.open('GET', 'foo', true);\n"
                    + "    xhr.send('');\n";
        }
        else {
            html += "    xhr.open('GET', 'foo', false);\n"
                    + "    xhr.send('');\n"
                    + "    let respXml = xhr.responseXML;\n"
                    + "    log(respXml);\n"
                    + "    if (respXml !== null) {\n"
                    + "      log(respXml.documentElement);\n"
                    + "      log(respXml.documentElement.tagName);\n"
                    + "      log(respXml.documentElement.baseURI);\n"
                    + "    }\n";
        }

        html += "  }\n"
                + "</script></head><body onload='test()'>\n"
                + "</body></html>";

        getMockWebConnection().setDefaultResponse(resp, mimeType);
        expandExpectedAlertsVariables(URL_FIRST);

        final WebDriver driver = loadPage2(html);
        verifyTitle2(DEFAULT_WAIT_TIME.multipliedBy(2), driver, getExpectedAlerts());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[object HTMLDocument]", "[object HTMLHtmlElement]", "HTML", "§§URL§§foo"})
    public void response_text_html() throws Exception {
        response("<html></html>", MimeType.TEXT_HTML);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[object HTMLDocument]", "[object HTMLHtmlElement]", "HTML", "§§URL§§foo"})
    public void response_text_html_empty() throws Exception {
        response("", MimeType.TEXT_HTML);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[object HTMLDocument]", "[object HTMLHtmlElement]", "HTML", "§§URL§§foo"})
    public void response_text_html_blank() throws Exception {
        response("    ", MimeType.TEXT_HTML);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[object XMLDocument]", "[object Element]", "note", "§§URL§§foo"})
    public void response_text_xml() throws Exception {
        response("<note/>", MimeType.TEXT_XML);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("null")
    public void response_text_xml_empty() throws Exception {
        response("", MimeType.TEXT_XML);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("null")
    public void response_text_xml_blank() throws Exception {
        response("    ", MimeType.TEXT_XML);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[object XMLDocument]", "[object Element]", "note", "§§URL§§foo"})
    public void response_application_xml() throws Exception {
        response("<note/>", MimeType.APPLICATION_XML);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[object XMLDocument]", "[object Element]", "html", "§§URL§§foo"})
    public void responseAsyncDocument_application_xhtmlXml() throws Exception {
        response("<html/>", MimeType.APPLICATION_XHTML);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[object XMLDocument]", "[object SVGSVGElement]", "svg", "§§URL§§foo"})
    @HtmlUnitNYI(CHROME = {"[object XMLDocument]", "[object SVGElement]", "svg", "§§URL§§foo"},
            EDGE = {"[object XMLDocument]", "[object SVGElement]", "svg", "§§URL§§foo"},
            FF = {"[object XMLDocument]", "[object SVGElement]", "svg", "§§URL§§foo"},
            FF_ESR = {"[object XMLDocument]", "[object SVGElement]", "svg", "§§URL§§foo"})
    public void responseAsyncDocument_application_svgXml() throws Exception {
        response("<svg xmlns=\"http://www.w3.org/2000/svg\"/>", "image/svg+xml");
    }

    private void response(final String resp, final String mimeType) throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var xhr = new XMLHttpRequest();\n"
            + "    xhr.responseType = 'document';"
            + "    xhr.onreadystatechange = () => {\n"
            + "      if (xhr.readyState === 4) {\n"
            + "        let respXml = xhr.response;\n"
            + "        log(respXml);\n"
            + "        if (respXml !== null) {\n"
            + "          log(respXml.documentElement);\n"
            + "          log(respXml.documentElement.tagName);\n"
            + "          log(respXml.documentElement.baseURI);\n"
            + "        }\n"
            + "      }\n"
            + "    }\n"
            + "    xhr.open('GET', 'foo', true);\n"
            + "    xhr.send('');\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        getMockWebConnection().setDefaultResponse(resp, mimeType);
        expandExpectedAlertsVariables(URL_FIRST);

        final WebDriver driver = loadPage2(html);
        verifyTitle2(DEFAULT_WAIT_TIME.multipliedBy(2), driver, getExpectedAlerts());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void responseXMLCalledTwoTimes() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  var xhr = new XMLHttpRequest();\n"
            + "  xhr.open('GET', 'foo', false);\n"
            + "  xhr.send('');\n"
            + "  let xml1 = xhr.responseXML;\n"
            + "  let xml2 = xhr.responseXML;\n"
            + "  log(xml1 === xml2);\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'></body></html>";

        getMockWebConnection().setDefaultResponse("<note/>", MimeType.TEXT_XML);

        final WebDriver driver = loadPage2(html);
        verifyTitle2(DEFAULT_WAIT_TIME.multipliedBy(2), driver, getExpectedAlerts());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void responseXMLAsyncCalledTwoTimes() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  var xhr = new XMLHttpRequest();\n"
            + "  xhr.onreadystatechange = () => {\n"
            + "    if (xhr.readyState === 4) {\n"
            + "      let xml1 = xhr.responseXML;\n"
            + "      let xml2 = xhr.responseXML;\n"
            + "      log(xml1 === xml2);\n"
            + "    }\n"
            + "  }\n"
            + "  xhr.open('GET', 'foo', true);\n"
            + "  xhr.send('');\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'></body></html>";

        getMockWebConnection().setDefaultResponse("<note/>", MimeType.TEXT_XML);

        final WebDriver driver = loadPage2(html);
        verifyTitle2(DEFAULT_WAIT_TIME.multipliedBy(2), driver, getExpectedAlerts());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void responseXMLDocumentXMLAsync() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  var xhr = new XMLHttpRequest();\n"
            + "  xhr.responseType = 'document';"
            + "  xhr.onreadystatechange = () => {\n"
            + "    if (xhr.readyState === 4) {\n"
            + "      let xml1 = xhr.responseXML;\n"
            + "      let xml2 = xhr.response;\n"
            + "      log(xml1 === xml2);\n"
            + "    }\n"
            + "  }\n"
            + "  xhr.open('GET', 'foo', true);\n"
            + "  xhr.send('');\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'></body></html>";

        getMockWebConnection().setDefaultResponse("<note/>", MimeType.TEXT_XML);

        final WebDriver driver = loadPage2(html);
        verifyTitle2(DEFAULT_WAIT_TIME.multipliedBy(2), driver, getExpectedAlerts());
    }

    /**
     * Regression test for IE specific properties attribute.text &amp; attribute.xml.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"1", "someAttr", "undefined", "undefined"})
    public void responseXML2() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  var request = new XMLHttpRequest();\n"
            + "  request.open('GET', 'foo.xml', false);\n"
            + "  request.send('');\n"
            + "  var childNodes = request.responseXML.childNodes;\n"
            + "  log(childNodes.length);\n"
            + "  var rootNode = childNodes[0];\n"
            + "  log(rootNode.attributes[0].nodeName);\n"
            + "  log(rootNode.attributes[0].text);\n"
            + "  log(rootNode.attributes[0].xml);\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'></body></html>";

        final URL urlPage2 = new URL(URL_FIRST, "foo.xml");
        getMockWebConnection().setResponse(urlPage2,
            "<bla someAttr='someValue'><foo><fi id='fi1'/><fi/></foo></bla>\n",
            MimeType.TEXT_XML);
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("received: null")
    public void responseXML_siteNotExisting() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  var request = new XMLHttpRequest();\n"
            + "try {\n"
            + "  request.open('GET', 'http://this.doesnt.exist/foo.xml', false);\n"
            + "  request.send('');\n"
            + "} catch(e) {\n"
            + "  log('received: ' + request.responseXML);\n"
            + "}\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void sendNull() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  var request = new XMLHttpRequest();\n"
            + "  request.open('GET', 'foo.txt', false);\n"
            + "  request.send(null);\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'></body></html>";

        getMockWebConnection().setDefaultResponse("");
        loadPageVerifyTitle2(html);
    }

    /**
     * Test calls to send('foo') for a GET. HtmlUnit 1.14 was incorrectly throwing an exception.
     * @throws Exception if the test fails
     */
    @Test
    public void sendGETWithContent() throws Exception {
        send("'foo'");
    }

    /**
     * Test calls to send() without any arguments.
     * @throws Exception if the test fails
     */
    @Test
    public void sendNoArg() throws Exception {
        send("");
    }

    /**
     * @throws Exception if the test fails
     */
    private void send(final String sendArg) throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  var request = new XMLHttpRequest();\n"
            + "  request.open('GET', 'foo.txt', false);\n"
            + "  request.send(" + sendArg + ");\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'></body></html>";

        getMockWebConnection().setDefaultResponse("");
        loadPageVerifyTitle2(html);
    }

    /**
     * Regression test for bug 1357412.
     * Response received by the XMLHttpRequest should not come in any window
     * @throws Exception if the test fails
     */
    @Test
    public void responseNotInWindow() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  var request = new XMLHttpRequest();\n"
            + "  request.open('GET', 'foo.txt', false);\n"
            + "  request.send();\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'></body></html>";

        getMockWebConnection().setDefaultResponse("");
        final WebDriver driver = loadPageVerifyTitle2(html);
        assertEquals(URL_FIRST.toString(), driver.getCurrentUrl());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"true", "false"})
    public void overrideMimeType() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "try {\n"
            + "  var request = new XMLHttpRequest();\n"
            + "  request.open('GET', 'foo.xml.txt', false);\n"
            + "  request.send('');\n"
            + "  log(request.responseXML == null);\n"
            + "  request.open('GET', 'foo.xml.txt', false);\n"
            + "  request.overrideMimeType('text/xml');\n"
            + "  request.send('');\n"
            + "  log(request.responseXML == null);\n"
            + "} catch(e) { logEx(e); }\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'></body></html>";

        final URL urlPage2 = new URL(URL_FIRST, "foo.xml.txt");
        getMockWebConnection().setResponse(urlPage2,
            "<bla someAttr='someValue'><foo><fi id='fi1'/><fi/></foo></bla>\n",
            MimeType.TEXT_PLAIN);
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"true", "InvalidStateError/DOMException"})
    public void overrideMimeTypeAfterSend() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  var request = new XMLHttpRequest();\n"
            + "  request.open('GET', 'foo.xml.txt', false);\n"
            + "  request.send('');\n"
            + "  log(request.responseXML == null);\n"
            + "  try {\n"
            + "    request.overrideMimeType('text/xml');\n"
            + "    log('overwritten');\n"
            + "  } catch(e) { logEx(e); }\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'></body></html>";

        final URL urlPage2 = new URL(URL_FIRST, "foo.xml.txt");
        getMockWebConnection().setResponse(urlPage2,
            "<bla someAttr='someValue'><foo><fi id='fi1'/><fi/></foo></bla>\n",
            MimeType.TEXT_PLAIN);
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("27035")
    public void overrideMimeType_charset() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "try {\n"
            + "  var request = new XMLHttpRequest();\n"
            + "  request.open('GET', '" + URL_SECOND + "', false);\n"
            + "  request.overrideMimeType('text/plain; charset=GBK');\n"
            + "  request.send('');\n"
            + "  log(request.responseText.charCodeAt(0));\n"
            + "} catch(e) { logEx(e); }\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'></body></html>";

        getMockWebConnection().setResponse(URL_SECOND, "\u9EC4", MimeType.TEXT_PLAIN, UTF_8);
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("27035")
    public void overrideMimeType_charset_upper_case() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "try {\n"
            + "  var request = new XMLHttpRequest();\n"
            + "  request.open('GET', '" + URL_SECOND + "', false);\n"
            + "  request.overrideMimeType('text/plain; chaRSet=GBK');\n"
            + "  request.send('');\n"
            + "  log(request.responseText.charCodeAt(0));\n"
            + "} catch(e) { logEx(e); }\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'></body></html>";

        getMockWebConnection().setResponse(URL_SECOND, "\u9EC4", MimeType.TEXT_PLAIN, UTF_8);
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("40644")
    public void overrideMimeType_charset_empty() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "try {\n"
            + "  var request = new XMLHttpRequest();\n"
            + "  request.open('GET', '" + URL_SECOND + "', false);\n"
            + "  request.overrideMimeType('text/plain; charset=');\n"
            + "  request.send('');\n"
            + "  log(request.responseText.charCodeAt(0));\n"
            + "} catch(e) { logEx(e); }\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'></body></html>";

        getMockWebConnection().setResponse(URL_SECOND, "\u9EC4", MimeType.TEXT_PLAIN, UTF_8);
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("40644")
    public void overrideMimeType_charset_wrong() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  try {\n"
            + "    var request = new XMLHttpRequest();\n"
            + "    request.open('GET', '" + URL_SECOND + "', false);\n"
            + "    request.overrideMimeType('text/plain; charset=abcdefg');\n"
            + "    request.send('');\n"
            + "    var text = request.responseText;\n"
            + "    for (var i = 0; i < text.length; i++) {\n"
            + "      log(text.charCodeAt(i));\n"
            + "    }\n"
            + "  } catch(e) { logEx(e); }\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'></body></html>";

        getMockWebConnection().setResponse(URL_SECOND, "\u9EC4", MimeType.TEXT_PLAIN, UTF_8);
        loadPageVerifyTitle2(html);
    }

    /**
     * Regression test for bug 410.
     * http://sourceforge.net/p/htmlunit/bugs/410/
     * Caution: the problem appeared with JDK 1.4 but not with JDK 1.5 as String contains a
     * replace(CharSequence, CharSequence) method in this version
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"ibcdefg", "xxxxxfg"})
    public void replaceOnTextData() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + LOG_TITLE_FUNCTION
            + "      var request;\n"
            + "      function testReplace() {\n"
            + "        request = new XMLHttpRequest();\n"
            + "        request.onreadystatechange = onReadyStateChange;\n"
            + "        request.open('GET', '" + URL_SECOND + "', false);\n"
            + "        request.send('');\n"
            + "      }\n"
            + "      function onReadyStateChange() {\n"
            + "        if (request.readyState == 4){\n"
            + "          var theXML = request.responseXML;\n"
            + "          var theDoc = theXML.documentElement;\n"
            + "          var theElements = theDoc.getElementsByTagName('update');\n"
            + "          var theUpdate = theElements[0];\n"
            + "          var theData = theUpdate.firstChild.data;\n"
            + "          theResult = theData.replace('a','i');\n"
            + "          log(theResult);\n"
            + "          theResult = theData.replace('abcde', 'xxxxx');\n"
            + "          log(theResult);\n"
            + "        }\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='testReplace()'>\n"
            + "  </body>\n"
            + "</html>";

        final String xml =
              "<updates>\n"
            + "<update>abcdefg</update>\n"
            + "<update>hijklmn</update>\n"
            + "</updates>";

        getMockWebConnection().setDefaultResponse(xml, MimeType.TEXT_XML);
        loadPageVerifyTitle2(html);
    }

    /**
     * Tests that the <tt>Referer</tt> header is set correctly.
     * @throws Exception if the test fails
     */
    @Test
    public void refererHeader() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + "function test() {\n"
            + "  req = new XMLHttpRequest();\n"
            + "  req.open('post', 'foo.xml', false);\n"
            + "  req.send('');\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'></body></html>";

        final URL urlPage2 = new URL(URL_FIRST, "foo.xml");
        getMockWebConnection().setResponse(urlPage2, "<foo/>\n", MimeType.TEXT_XML);
        loadPage2(html);

        final WebRequest request = getMockWebConnection().getLastWebRequest();
        assertEquals(urlPage2, request.getUrl());
        assertEquals(URL_FIRST.toExternalForm(), request.getAdditionalHeaders().get(HttpHeader.REFERER));
    }

    /**
     * Tests that the <tt>origin</tt> header is set correctly.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("null")
    public void originHeaderGet() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + "function test() {\n"
            + "  req = new XMLHttpRequest();\n"
            + "  req.open('get', 'foo.xml', false);\n"
            + "  req.send('');\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'></body></html>";

        final URL urlPage2 = new URL(URL_FIRST, "foo.xml");
        getMockWebConnection().setResponse(urlPage2, "<foo/>\n", MimeType.TEXT_XML);
        expandExpectedAlertsVariables(urlPage2.getProtocol() + "://" + urlPage2.getHost() + ":" + urlPage2.getPort());
        loadPage2(html);

        final WebRequest request = getMockWebConnection().getLastWebRequest();
        assertEquals(urlPage2, request.getUrl());
        assertEquals(getExpectedAlerts()[0], "" + request.getAdditionalHeaders().get(HttpHeader.ORIGIN));
        assertEquals(null, request.getAdditionalHeaders().get(HttpHeader.ACCESS_CONTROL_ALLOW_ORIGIN));
    }

    /**
     * Tests that the <tt>origin</tt> header is set correctly.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("§§URL§§")
    public void originHeaderPost() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + "function test() {\n"
            + "  req = new XMLHttpRequest();\n"
            + "  req.open('post', 'foo.xml', false);\n"
            + "  req.send('');\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'></body></html>";

        final URL urlPage2 = new URL(URL_FIRST, "foo.xml");
        getMockWebConnection().setResponse(urlPage2, "<foo/>\n", MimeType.TEXT_XML);
        expandExpectedAlertsVariables(urlPage2.getProtocol() + "://" + urlPage2.getHost() + ":" + urlPage2.getPort());
        loadPage2(html);

        final WebRequest request = getMockWebConnection().getLastWebRequest();
        assertEquals(urlPage2, request.getUrl());
        assertEquals(getExpectedAlerts()[0], "" + request.getAdditionalHeaders().get(HttpHeader.ORIGIN));
        assertEquals(null, request.getAdditionalHeaders().get(HttpHeader.ACCESS_CONTROL_ALLOW_ORIGIN));
    }

    /**
     * Tests that the <tt>origin</tt> header is set correctly.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("§§URL§§")
    public void originHeaderPut() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + "function test() {\n"
            + "  req = new XMLHttpRequest();\n"
            + "  req.open('put', 'foo.xml', false);\n"
            + "  req.send('');\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'></body></html>";

        final URL urlPage2 = new URL(URL_FIRST, "foo.xml");
        getMockWebConnection().setResponse(urlPage2, "<foo/>\n", MimeType.TEXT_XML);
        expandExpectedAlertsVariables(urlPage2.getProtocol() + "://" + urlPage2.getHost() + ":" + urlPage2.getPort());
        loadPage2(html);

        final WebRequest request = getMockWebConnection().getLastWebRequest();
        assertEquals(urlPage2, request.getUrl());
        assertEquals(getExpectedAlerts()[0], "" + request.getAdditionalHeaders().get(HttpHeader.ORIGIN));
        assertEquals(null, request.getAdditionalHeaders().get(HttpHeader.ACCESS_CONTROL_ALLOW_ORIGIN));
    }

    /**
     * Tests that the <tt>origin</tt> header is set correctly.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("§§URL§§")
    public void originHeaderDelete() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + "function test() {\n"
            + "  req = new XMLHttpRequest();\n"
            + "  req.open('delete', 'foo.xml', false);\n"
            + "  req.send('');\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'></body></html>";

        final URL urlPage2 = new URL(URL_FIRST, "foo.xml");
        getMockWebConnection().setResponse(urlPage2, "<foo/>\n", MimeType.TEXT_XML);
        expandExpectedAlertsVariables(urlPage2.getProtocol() + "://" + urlPage2.getHost() + ":" + urlPage2.getPort());
        loadPage2(html);

        final WebRequest request = getMockWebConnection().getLastWebRequest();
        assertEquals(urlPage2, request.getUrl());
        assertEquals(getExpectedAlerts()[0], "" + request.getAdditionalHeaders().get(HttpHeader.ORIGIN));
        assertEquals(null, request.getAdditionalHeaders().get(HttpHeader.ACCESS_CONTROL_ALLOW_ORIGIN));
    }

    /**
     * Tests that the <tt>origin</tt> header is set correctly.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"§§URL§§/", "null", "null"})
    @HtmlUnitNYI(CHROME = {"§§URL§§/foo.xml", "null", "null"},
            EDGE = {"§§URL§§/foo.xml", "null", "null"},
            FF = {"§§URL§§/foo.xml", "null", "null"},
            FF_ESR = {"§§URL§§/foo.xml", "null", "null"})
    public void originHeaderPatch() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + "function test() {\n"
            + "  req = new XMLHttpRequest();\n"
            + "  req.open('patch', 'foo.xml', false);\n"
            + "  req.send('');\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'></body></html>";

        final URL urlPage2 = new URL(URL_FIRST, "foo.xml");
        getMockWebConnection().setResponse(urlPage2, "<foo/>\n", MimeType.TEXT_XML);
        expandExpectedAlertsVariables(urlPage2.getProtocol() + "://" + urlPage2.getHost() + ":" + urlPage2.getPort());
        loadPage2(html);

        expandExpectedAlertsVariables(URL_FIRST);

        final WebRequest request = getMockWebConnection().getLastWebRequest();
        assertEquals(getExpectedAlerts()[0], request.getUrl());
        assertEquals(getExpectedAlerts()[1],
                        "" + request.getAdditionalHeaders().get(HttpHeader.ORIGIN));
        assertEquals(getExpectedAlerts()[2],
                        "" + request.getAdditionalHeaders().get(HttpHeader.ACCESS_CONTROL_ALLOW_ORIGIN));
    }

    /**
     * Tests that the <tt>origin</tt> header is set correctly.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("null")
    public void originHeaderTrace() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + "function test() {\n"
            + "  req = new XMLHttpRequest();\n"
            + "  req.open('trace', 'foo.xml', false);\n"
            + "  req.send('');\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'></body></html>";

        final URL urlPage2 = new URL(URL_FIRST, "foo.xml");
        getMockWebConnection().setResponse(urlPage2, "<foo/>\n", MimeType.TEXT_XML);
        expandExpectedAlertsVariables(urlPage2.getProtocol() + "://" + urlPage2.getHost() + ":" + urlPage2.getPort());
        loadPage2(html);

        final WebRequest request = getMockWebConnection().getLastWebRequest();
        assertEquals(URL_FIRST, request.getUrl());
        assertEquals(getExpectedAlerts()[0], "" + request.getAdditionalHeaders().get(HttpHeader.ORIGIN));
        assertEquals(null, request.getAdditionalHeaders().get(HttpHeader.ACCESS_CONTROL_ALLOW_ORIGIN));
    }

    /**
     * Tests that the <tt>origin</tt> header is set correctly.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("null")
    public void originHeaderHead() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + "function test() {\n"
            + "  req = new XMLHttpRequest();\n"
            + "  req.open('head', 'foo.xml', false);\n"
            + "  req.send('');\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'></body></html>";

        final URL urlPage2 = new URL(URL_FIRST, "foo.xml");
        getMockWebConnection().setResponse(urlPage2, "<foo/>\n", MimeType.TEXT_XML);
        expandExpectedAlertsVariables(urlPage2.getProtocol() + "://" + urlPage2.getHost() + ":" + urlPage2.getPort());
        loadPage2(html);

        final WebRequest request = getMockWebConnection().getLastWebRequest();
        assertEquals(urlPage2, request.getUrl());
        assertEquals(getExpectedAlerts()[0], "" + request.getAdditionalHeaders().get(HttpHeader.ORIGIN));
        assertEquals(null, request.getAdditionalHeaders().get(HttpHeader.ACCESS_CONTROL_ALLOW_ORIGIN));
    }

    /**
     * Tests that the <tt>origin</tt> header is set correctly.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("§§URL§§")
    public void originHeaderOptions() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + "function test() {\n"
            + "  req = new XMLHttpRequest();\n"
            + "  req.open('options', 'foo.xml', false);\n"
            + "  req.send('');\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'></body></html>";

        final URL urlPage2 = new URL(URL_FIRST, "foo.xml");
        getMockWebConnection().setResponse(urlPage2, "<foo/>\n", MimeType.TEXT_XML);
        expandExpectedAlertsVariables(urlPage2.getProtocol() + "://" + urlPage2.getHost() + ":" + urlPage2.getPort());
        loadPage2(html);

        final WebRequest request = getMockWebConnection().getLastWebRequest();
        assertEquals(urlPage2, request.getUrl());
        assertEquals(getExpectedAlerts()[0], "" + request.getAdditionalHeaders().get(HttpHeader.ORIGIN));
        assertEquals(null, request.getAdditionalHeaders().get(HttpHeader.ACCESS_CONTROL_ALLOW_ORIGIN));
    }

    /**
     * Test for bug
     * <a href="https://sourceforge.net/tracker/?func=detail&atid=448266&aid=1784330&group_id=47038">issue 515</a>.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("ActiveXObject not available")
    public void caseInsensitivityActiveXConstructor() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  try {\n"
            + "    var req = new ActiveXObject('MSXML2.XmlHttp');\n"
            + "    log(req.readyState);\n"

            + "    var req = new ActiveXObject('msxml2.xMLhTTp');\n"
            + "    log(req.readyState);\n"
            + "  } catch(e) { log('ActiveXObject not available'); }\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("selectNodes not available")
    public void responseXML_selectNodesIE() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + LOG_TITLE_FUNCTION
            + "      function test() {\n"
            + "        var request = new XMLHttpRequest();\n"
            + "        request.open('GET', '" + URL_SECOND + "', false);\n"
            + "        request.send('');\n"
            + "        if (!request.responseXML.selectNodes) { log('selectNodes not available'); return }\n"
            + "        log(request.responseXML.selectNodes('//content').length);\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='test()'>\n"
            + "  </body>\n"
            + "</html>";

        final String xml =
              "<xml>\n"
            + "<content>blah</content>\n"
            + "<content>blah2</content>\n"
            + "</xml>";

        getMockWebConnection().setResponse(URL_SECOND, xml, MimeType.TEXT_XML);
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[object Element]", "myID", "blah", "span", "[object XMLDocument]", "[object XMLDocument]"})
    public void responseXML_getElementById2() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + LOG_TITLE_FUNCTION
            + "      function test() {\n"
            + "        var request = new XMLHttpRequest();\n"
            + "        request.open('GET', '" + URL_SECOND + "', false);\n"
            + "        request.send('');\n"
            + "        if (request.responseXML.getElementById) {\n"
            + "          log(request.responseXML.getElementById('id1'));\n"
            + "          log(request.responseXML.getElementById('myID').id);\n"
            + "          log(request.responseXML.getElementById('myID').innerHTML);\n"
            + "          log(request.responseXML.getElementById('myID').tagName);\n"
            + "          log(request.responseXML.getElementById('myID').ownerDocument);\n"
            + "          if (request.responseXML.getElementById('myID').getRootNode) {\n"
            + "            log(request.responseXML.getElementById('myID').getRootNode());\n"
            + "          } else log('-');\n"
            + "        } else  {\n"
            + "          log('responseXML.getElementById not available');\n"
            + "        }\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='test()'>\n"
            + "  </body>\n"
            + "</html>";

        final String xml =
              "<xml>\n"
            + "<content id='id1'>blah</content>\n"
            + "<content>blah2</content>\n"
            + "<html xmlns='http://www.w3.org/1999/xhtml'>\n"
            + "<span id='myID'>blah</span>\n"
            + "<script src='foo.js'></script>\n"
            + "</html>\n"
            + "</xml>";

        getMockWebConnection().setResponse(URL_SECOND, xml, MimeType.TEXT_XML);
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[object Element]", "[object Element]", "[object HTMLBodyElement]",
             "[object HTMLSpanElement]", "[object XMLDocument]", "[object XMLDocument]", "undefined"})
    public void responseXML_getElementById() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + LOG_TITLE_FUNCTION
            + "      function test() {\n"
            + "        var request = new XMLHttpRequest();\n"
            + "        request.open('GET', '" + URL_SECOND + "', false);\n"
            + "        request.send('');\n"
            + "        var doc = request.responseXML;\n"
            + "        log(doc.documentElement);\n"
            + "        log(doc.documentElement.childNodes[0]);\n"
            + "        log(doc.documentElement.childNodes[1]);\n"
            + "        if (doc.getElementById) {\n"
            + "          log(doc.getElementById('out'));\n"
            + "          log(doc.getElementById('out').ownerDocument);\n"
            + "          if (doc.getElementById('out').getRootNode) {\n"
            + "            log(doc.getElementById('out').getRootNode());\n"
            + "          } else log('-');\n"
            + "        }\n"
            + "        log(doc.documentElement.childNodes[1].xml);\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='test()'>\n"
            + "  </body>\n"
            + "</html>";

        final String xml = DOCTYPE_HTML
            + "<html>"
            + "<head>"
            + "</head>"
            + "<body xmlns='http://www.w3.org/1999/xhtml'>"
            + "<span id='out'>Hello Bob Dole!</span>"
            + "</body>"
            + "</html>";

        getMockWebConnection().setResponse(URL_SECOND, xml, MimeType.TEXT_XML);
        loadPageVerifyTitle2(html);
    }

    /**
     * Verifies that the default encoding for an XMLHttpRequest is UTF-8.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("ol\u00E9")
    public void defaultEncodingIsUTF8() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + LOG_TITLE_FUNCTION
            + "      function test() {\n"
            + "        var request = new XMLHttpRequest();\n"
            + "        request.open('GET', '" + URL_SECOND + "', false);\n"
            + "        request.send('');\n"
            + "        log(request.responseText);\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='test()'>\n"
            + "  </body>\n"
            + "</html>";

        final String response = "ol\u00E9";
        final byte[] responseBytes = response.getBytes(UTF_8);

        getMockWebConnection().setResponse(URL_SECOND, responseBytes, 200, "OK", MimeType.TEXT_HTML,
            new ArrayList<>());
        loadPageVerifyTitle2(html);
    }

    /**
     * Custom servlet which streams content to the client little by little.
     */
    public static final class StreamingServlet extends HttpServlet {
        /** {@inheritDoc} */
        @Override
        protected void doGet(final HttpServletRequest req, final HttpServletResponse resp) throws IOException {
            resp.setStatus(200);
            resp.addHeader(HttpHeader.CONTENT_TYPE, MimeType.TEXT_HTML);
            try {
                for (int i = 0; i < 10; i++) {
                    resp.getOutputStream().print(String.valueOf(i));
                    resp.flushBuffer();
                    Thread.sleep(150);
                }
            }
            catch (final InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Servlet for testing XMLHttpRequest basic authentication.
     */
    public static final class BasicAuthenticationServlet extends HttpServlet {

        /**
         * {@inheritDoc}
         */
        @Override
        protected void doPost(final HttpServletRequest req, final HttpServletResponse resp) throws IOException {
            handleRequest(req, resp);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        protected void doGet(final HttpServletRequest req, final HttpServletResponse resp) throws IOException {
            handleRequest(req, resp);
        }

        private static void handleRequest(final HttpServletRequest req, final HttpServletResponse resp)
                    throws IOException {
            final String authHdr = req.getHeader("Authorization");
            if (null == authHdr) {
                resp.setStatus(401);
                resp.setHeader("WWW-Authenticate", "Basic realm=\"someRealm\"");
            }
            else {
                final String[] authHdrTokens = authHdr.split("\\s+");
                String authToken = "";
                if (authHdrTokens.length == 2) {
                    authToken += authHdrTokens[0] + ':' + authHdrTokens[1];
                }

                resp.setStatus(200);
                resp.addHeader(HttpHeader.CONTENT_TYPE, MimeType.TEXT_PLAIN);
                resp.getOutputStream().print(authToken);
                resp.flushBuffer();
            }
        }
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("myID")
    public void responseXML_html_select() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + LOG_TITLE_FUNCTION
            + "      function test() {\n"
            + "        try {\n"
            + "          var request = new XMLHttpRequest();\n"
            + "          request.open('GET', '" + URL_SECOND + "', false);\n"
            + "          request.send('');\n"
            + "          log(request.responseXML.getElementById('myID').id);\n"
            + "        } catch(e) { logEx(e); }\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='test()'>\n"
            + "  </body>\n"
            + "</html>";

        final String xml =
              "<xml>\n"
            + "<content id='id1'>blah</content>\n"
            + "<content>blah2</content>\n"
            + "<html xmlns='http://www.w3.org/1999/xhtml'>\n"
            + "<select id='myID'><option>One</option></select>\n"
            + "</html>\n"
            + "</xml>";

        getMockWebConnection().setResponse(URL_SECOND, xml, MimeType.TEXT_XML);
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("myInput")
    public void responseXML_html_form() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + LOG_TITLE_FUNCTION
            + "      function test() {\n"
            + "        try {\n"
            + "          var request = new XMLHttpRequest();\n"
            + "          request.open('GET', '" + URL_SECOND + "', false);\n"
            + "          request.send('');\n"
            + "          log(request.responseXML.getElementById('myID').myInput.name);\n"
            + "        } catch(e) { logEx(e); }\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='test()'>\n"
            + "  </body>\n"
            + "</html>";

        final String xml =
              "<xml>\n"
            + "<content id='id1'>blah</content>\n"
            + "<content>blah2</content>\n"
            + "<html xmlns='http://www.w3.org/1999/xhtml'>\n"
            + "<form id='myID'><input name='myInput'/></form>\n"
            + "</html>\n"
            + "</xml>";

        getMockWebConnection().setResponse(URL_SECOND, xml, MimeType.TEXT_XML);
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("ActiveXObject not available")
    public void caseSensitivity_activeX() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  try {\n"
            + "    var req = new ActiveXObject('MSXML2.XmlHttp');\n"
            + "    log(req.readyState);\n"
            + "    log(req.reAdYsTaTe);\n"
            + "  } catch(e) { log('ActiveXObject not available'); }\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'></body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"0", "undefined"})
    public void caseSensitivity_XMLHttpRequest() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  try {\n"
            + "    var req = new XMLHttpRequest();\n"
            + "    log(req.readyState);\n"
            + "    log(req.reAdYsTaTe);\n"
            + "  } catch(e) { logEx(e); }\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'></body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void isAuthorizedHeader() throws Exception {
        assertTrue(XMLHttpRequest.isAuthorizedHeader("Foo"));
        assertTrue(XMLHttpRequest.isAuthorizedHeader(HttpHeader.CONTENT_TYPE));

        final String[] headers = {"accept-charset", HttpHeader.ACCEPT_ENCODING_LC,
            HttpHeader.CONNECTION_LC, HttpHeader.CONTENT_LENGTH_LC, HttpHeader.COOKIE_LC, "cookie2",
            "content-transfer-encoding", "date",
            "expect", HttpHeader.HOST_LC, "keep-alive", HttpHeader.REFERER_LC,
            "te", "trailer", "transfer-encoding", "upgrade",
            HttpHeader.USER_AGENT_LC, "via" };
        for (final String header : headers) {
            assertFalse(XMLHttpRequest.isAuthorizedHeader(header));
            assertFalse(XMLHttpRequest.isAuthorizedHeader(header.toUpperCase(Locale.ROOT)));
        }
        assertFalse(XMLHttpRequest.isAuthorizedHeader("Proxy-"));
        assertFalse(XMLHttpRequest.isAuthorizedHeader("Proxy-Control"));
        assertFalse(XMLHttpRequest.isAuthorizedHeader("Proxy-Hack"));
        assertFalse(XMLHttpRequest.isAuthorizedHeader("Sec-"));
        assertFalse(XMLHttpRequest.isAuthorizedHeader("Sec-Hack"));
    }

    /**
     * Test case for Bug #1623.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"39", "27035", "65533", "39"})
    public void overrideMimeType_charset_all() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "try {\n"
            + "  var request = new XMLHttpRequest();\n"
            + "  request.open('GET', '" + URL_SECOND + "', false);\n"
            + "  request.overrideMimeType('text/plain; charset=GBK');\n"
            + "  request.send('');\n"
            + "  for (var i = 0; i < request.responseText.length; i++) {\n"
            + "    log(request.responseText.charCodeAt(i));\n"
            + "  }\n"
            + "} catch(e) { logEx(e); }\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'></body></html>";

        getMockWebConnection().setResponse(URL_SECOND, "'\u9EC4'", MimeType.TEXT_PLAIN, UTF_8);
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void java_encoding() throws Exception {
        // Chrome and FF return the last apostrophe, see overrideMimeType_charset_all()
        // but Java and other tools (e.g. Notpad++) return only 3 characters, not 4
        // this method is not a test case, but rather to show the behavior of java

        final String string = "'\u9EC4'";
        final ByteArrayInputStream bais = new ByteArrayInputStream(string.getBytes(UTF_8));
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(bais, "GBK"))) {
            final String output = reader.readLine();
            assertNotNull(output);
            assertEquals(39, output.codePointAt(0));
            assertEquals(27035, output.codePointAt(1));
            assertEquals(65533, output.codePointAt(2));
            assertEquals(39, output.codePointAt(3));
        }
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("[object ProgressEvent]")
    public void loadParameter() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + LOG_TITLE_FUNCTION
            + "      function someLoad(e) {\n"
            + "        log(e);\n"
            + "      }\n"
            + "      function test() {\n"
            + "        try {\n"
            + "          var request = new XMLHttpRequest();\n"
            + "          request.onload = someLoad;\n"
            + "          request.open('GET', '" + URL_SECOND + "', false);\n"
            + "          request.send('');\n"
            + "        } catch(e) { logEx(e); }\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='test()'>\n"
            + "  </body>\n"
            + "</html>";

        final String xml = "<abc></abc>";

        getMockWebConnection().setResponse(URL_SECOND, xml, MimeType.TEXT_XML);
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"someLoad [object ProgressEvent]", "load", "false", "11", "0"})
    public void addEventListener() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + LOG_TITLE_FUNCTION
            + "      function someLoad(event) {\n"
            + "        log('someLoad ' + event);\n"
            + "        log(event.type);\n"
            + "        log(event.lengthComputable);\n"
            + "        log(event.loaded);\n"
            + "        log(event.total);\n"
            + "      }\n"
            + "      function test() {\n"
            + "        try {\n"
            + "          var request = new XMLHttpRequest();\n"
            + "          request.addEventListener('load', someLoad, false);\n"
            + "          request.open('GET', '" + URL_SECOND + "', false);\n"
            + "          request.send('');\n"
            + "        } catch(e) { logEx(e); }\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='test()'>\n"
            + "  </body>\n"
            + "</html>";

        final String xml = "<abc></abc>";

        getMockWebConnection().setResponse(URL_SECOND, xml, MimeType.TEXT_XML);
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"someLoad [object ProgressEvent]", "load", "false", "11", "0"})
    public void addEventListenerDetails() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + LOG_TITLE_FUNCTION
            + "      function someLoad(event) {\n"
            + "        log('someLoad ' + event);\n"
            + "        log(event.type);\n"
            + "        log(event.lengthComputable);\n"
            + "        log(event.loaded);\n"
            + "        log(event.total);\n"
            + "      }\n"
            + "      function test() {\n"
            + "        try {\n"
            + "          var request = new XMLHttpRequest();\n"
            + "          request.addEventListener('load', someLoad, false);\n"
            + "          request.open('GET', '" + URL_SECOND + "', false);\n"
            + "          request.send('');\n"
            + "        } catch(e) { logEx(e); }\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='test()'>\n"
            + "  </body>\n"
            + "</html>";

        final String xml = "<abc></abc>";

        getMockWebConnection().setResponse(URL_SECOND, xml, MimeType.TEXT_XML);
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("function")
    @HtmlUnitNYI(CHROME = "null",
            EDGE = "null",
            FF = "null",
            FF_ESR = "null")
    public void addEventListenerCaller() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + LOG_TITLE_FUNCTION
            + "      function someLoad(event) {\n"
            + "        var caller = arguments.callee.caller;\n"
            + "        log(caller);\n"
            + "        log(typeof caller == 'function' ? 'function' : caller);\n"
            + "      }\n"
            + "      function test() {\n"
            + "        try {\n"
            + "          var request = new XMLHttpRequest();\n"
            + "          request.addEventListener('load', someLoad, false);\n"
            + "          request.open('GET', '" + URL_SECOND + "', false);\n"
            + "          request.send('');\n"
            + "        } catch(e) { logEx(e); }\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='test()'>\n"
            + "  </body>\n"
            + "</html>";

        final String xml = "<abc></abc>";

        getMockWebConnection().setResponse(URL_SECOND, xml, MimeType.TEXT_XML);
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("[object XMLHttpRequestUpload]")
    public void upload() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + LOG_TITLE_FUNCTION
            + "      function test() {\n"
            + "        try {\n"
            + "          var request = new XMLHttpRequest();\n"
            + "          log(request.upload);\n"
            + "        } catch(e) { logEx(e); }\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='test()'>\n"
            + "  </body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Tests asynchronous use of XMLHttpRequest, using Mozilla style object creation.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"0", "1", "2", "3", "4"})
    public void asyncUse() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + LOG_TITLE_FUNCTION_NORMALIZE
            + "      var request;\n"
            + "      function testAsync() {\n"
            + "        request = new XMLHttpRequest();\n"
            + "        request.onreadystatechange = onReadyStateChange;\n"
            + "        log(request.readyState);\n"
            + "        request.open('GET', '" + URL_SECOND + "', true);\n"
            + "        request.send('');\n"
            + "      }\n"
            + "      function onReadyStateChange() {\n"
            + "        log(request.readyState);\n"
            + "        if (request.readyState == 4)\n"
            + "          log(request.responseText);\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='testAsync()'>\n"
            + "  </body>\n"
            + "</html>";

        final String xml =
              "<xml2>\n"
            + "<content2>sdgxsdgx</content2>\n"
            + "<content2>sdgxsdgx2</content2>\n"
            + "</xml2>";

        getMockWebConnection().setResponse(URL_SECOND, xml, MimeType.TEXT_XML);
        setExpectedAlerts(ArrayUtils.add(getExpectedAlerts(), xml.replace("\n", "\\n")));
        loadPage2(html);
        verifyTitle2(DEFAULT_WAIT_TIME, getWebDriver(), getExpectedAlerts());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"[object Object]", "undefined", "undefined",
                       "function() { return !0 }",
                       "function set onreadystatechange() { [native code] }",
                       "true", "true"},
            FF = {"[object Object]", "undefined", "undefined",
                  "function() { return !0 }",
                  "function onreadystatechange() { [native code] }",
                  "true", "true"},
            FF_ESR = {"[object Object]", "undefined", "undefined",
                      "function() { return !0 }",
                      "function onreadystatechange() { [native code] }",
                      "true", "true"})
    @HtmlUnitNYI(CHROME = {"[object Object]", "undefined", "undefined",
                           "function() { return !0 }",
                           "function onreadystatechange() { [native code] }",
                           "true", "true"},
            EDGE = {"[object Object]", "undefined", "undefined",
                    "function() { return !0 }",
                    "function onreadystatechange() { [native code] }",
                    "true", "true"})
    public void defineProperty() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + LOG_TITLE_FUNCTION
            + "      var request;\n"
            + "      function test() {\n"
            + "        Object.defineProperty(XMLHttpRequest.prototype, 'onreadystatechange', {\n"
            + "                                 enumerable: !0,\n"
            + "                                 configurable: !0,\n"
            + "                                 get: function() { return !0 }\n"
            + "                             });\n"
            + "        var desc = Object.getOwnPropertyDescriptor(XMLHttpRequest.prototype, 'onreadystatechange');\n"
            + "        log(desc);\n"
            + "        log(desc.value);\n"
            + "        log(desc.writable);\n"
            + "        log(desc.get);\n"
            + "        log(desc.set);\n"
            + "        log(desc.configurable);\n"
            + "        log(desc.enumerable);\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='test()'>\n"
            + "  </body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Test case for https://stackoverflow.com/questions/44349339/htmlunit-ecmaerror-typeerror.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("[object XMLHttpRequest]")
    public void defineProperty2() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + LOG_TITLE_FUNCTION
            + "      var request;\n"
            + "      function test() {\n"
            + "        var t = Object.getOwnPropertyDescriptor(XMLHttpRequest.prototype, 'onreadystatechange');\n"
            + "        var res = Object.defineProperty(XMLHttpRequest.prototype, 'onreadystatechange', t);\n"
            + "        log(res);\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='test()'>\n"
            + "  </body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("application/json")
    public void enctypeBlob() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function doTest() {\n"
            + "  try {\n"
            + "    var debug = {hello: 'world'};\n"
            + "    var blob = new Blob([JSON.stringify(debug, null, 2)], {type : 'application/json'});\n"

            + "    var xhr = new XMLHttpRequest();\n"
            + "    xhr.open('post', '/test2', false);\n"
            + "    xhr.send(blob);\n"
            + "    log('done');\n"
            + "  } catch(e) { logEx(e); }\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='doTest()'>\n"
            + "</body>\n"
            + "</html>";

        getMockWebConnection().setDefaultResponse("<html><title>Response</title></html>");

        final WebDriver driver = loadPage2(html);
        verifyTitle2(driver, new String[] {"done"});

        String headerValue = getMockWebConnection().getLastWebRequest().getAdditionalHeaders()
            .get(HttpHeader.CONTENT_TYPE);
        // Can't test equality for multipart/form-data as it will have the form:
        // multipart/form-data; boundary=---------------------------42937861433140731107235900
        headerValue = StringUtils.substringBefore(headerValue, ";");
        assertEquals(getExpectedAlerts()[0], "" + headerValue);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("null")
    @HtmlUnitNYI(CHROME = "text/plain",
            EDGE = "text/plain",
            FF = "text/plain",
            FF_ESR = "text/plain")
    public void enctypeBufferSource() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function doTest() {\n"
            + "  try {\n"
            + "    var typedArray = new Int8Array(8);\n"
            + "    var xhr = new XMLHttpRequest();\n"
            + "    xhr.open('post', '/test2', false);\n"
            + "    xhr.send(typedArray);\n"
            + "    log('done');\n"
            + "  } catch(e) { logEx(e); }\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='doTest()'>\n"
            + "</body>\n"
            + "</html>";

        getMockWebConnection().setDefaultResponse("<html><title>Response</title></html>");

        final WebDriver driver = loadPage2(html);
        verifyTitle2(driver, new String[] {"done"});

        String headerValue = getMockWebConnection().getLastWebRequest().getAdditionalHeaders()
            .get(HttpHeader.CONTENT_TYPE);
        // Can't test equality for multipart/form-data as it will have the form:
        // multipart/form-data; boundary=---------------------------42937861433140731107235900
        headerValue = StringUtils.substringBefore(headerValue, ";");
        assertEquals(getExpectedAlerts()[0], "" + headerValue);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"q=HtmlUnit&u=%D0%BB%C6%89", "done", "application/x-www-form-urlencoded;charset=UTF-8",
             "q=HtmlUnit", "u=\u043B\u0189"})
    public void enctypeURLSearchParams() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function doTest() {\n"
            + "  var searchParams = '1234';\n"
            + "  try {\n"
            + "    searchParams = new URLSearchParams();\n"
            + "    searchParams.append('q', 'HtmlUnit');\n"
            + "    searchParams.append('u', '\u043B\u0189');\n"
            + "    log(searchParams);\n"
            + "  } catch(e) { logEx(e); }\n"

            + "  try {\n"
            + "    var xhr = new XMLHttpRequest();\n"
            + "    xhr.open('post', '/test2', false);\n"
            + "    xhr.send(searchParams);\n"
            + "    log('done');\n"
            + "  } catch(e) { logEx(e); }\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='doTest()'>\n"
            + "</body>\n"
            + "</html>";

        getMockWebConnection().setDefaultResponse("<html><title>Response</title></html>");

        final WebDriver driver = loadPage2(html, URL_FIRST, "text/html;charset=UTF-8", UTF_8, null);
        verifyTitle2(driver, new String[] {getExpectedAlerts()[0], getExpectedAlerts()[1]});

        String headerContentType = getMockWebConnection().getLastWebRequest().getAdditionalHeaders()
            .get(HttpHeader.CONTENT_TYPE);
        headerContentType = headerContentType.replace("; ", ";"); // normalize
        assertEquals(getExpectedAlerts()[2], headerContentType);
        if (getExpectedAlerts().length > 3) {
            assertEquals(getExpectedAlerts()[3], getMockWebConnection().getLastWebRequest()
                                .getRequestParameters().get(0).toString());
            assertEquals(getExpectedAlerts()[4], getMockWebConnection().getLastWebRequest()
                    .getRequestParameters().get(1).toString());
            assertEquals(null, getMockWebConnection().getLastWebRequest().getRequestBody());
        }
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("multipart/form-data")
    public void enctypeFormData() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function doTest() {\n"
            + "  try {\n"
            + "    var formData = new FormData(document.testForm);\n"
            + "    var xhr = new XMLHttpRequest();\n"
            + "    xhr.open('post', '/test2', false);\n"
            + "    xhr.send(formData);\n"
            + "    log('done');\n"
            + "  } catch(e) { logEx(e); }\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='doTest()'>\n"
            + "  <form name='testForm'>\n"
            + "    <input type='text' id='myText' name='myText' value='textxy'>\n"
            + "  </form>\n"
            + "</body>\n"
            + "</html>";

        getMockWebConnection().setDefaultResponse("<html><title>Response</title></html>");

        final WebDriver driver = loadPage2(html);
        verifyTitle2(driver, new String[] {"done"});

        String headerValue = getMockWebConnection().getLastWebRequest().getAdditionalHeaders()
            .get(HttpHeader.CONTENT_TYPE);
        // Can't test equality for multipart/form-data as it will have the form:
        // multipart/form-data; boundary=---------------------------42937861433140731107235900
        headerValue = StringUtils.substringBefore(headerValue, ";");
        assertEquals(getExpectedAlerts()[0], headerValue);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"text/plain;charset=UTF-8", "HtmlUnit \u043B\u0189"})
    public void enctypeString() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function doTest() {\n"
            + "  try {\n"
            + "    var xhr = new XMLHttpRequest();\n"
            + "    xhr.open('post', '/test2', false);\n"
            + "    xhr.send('HtmlUnit \u043B\u0189');\n"
            + "    log('done');\n"
            + "  } catch(e) { logEx(e); }\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='doTest()'>\n"
            + "</body>\n"
            + "</html>";

        getMockWebConnection().setDefaultResponse("<html><title>Response</title></html>");

        // use utf8 here to be able to send all chars
        final WebDriver driver = loadPage2(html, URL_FIRST, "text/html;charset=UTF-8", UTF_8, null);
        verifyTitle2(driver, new String[] {"done"});

        String headerContentType = getMockWebConnection().getLastWebRequest().getAdditionalHeaders()
            .get(HttpHeader.CONTENT_TYPE);
        headerContentType = headerContentType.replace("; ", ";"); // normalize
        assertEquals(getExpectedAlerts()[0], headerContentType);
        assertEquals(getExpectedAlerts()[1], getMockWebConnection().getLastWebRequest().getRequestBody());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"text/jpeg", "HtmlUnit \u00D0\u00BB\u00C6\u0089"})
    public void enctypeUserDefined() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function doTest() {\n"
            + "  try {\n"
            + "    var xhr = new XMLHttpRequest();\n"
            + "    xhr.open('post', '/test2', false);\n"
            + "    xhr.setRequestHeader('Content-Type', 'text/jpeg');\n"
            + "    xhr.send('HtmlUnit \u043B\u0189');\n"
            + "    log('done');\n"
            + "  } catch(e) { logEx(e); }\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='doTest()'>\n"
            + "</body>\n"
            + "</html>";

        getMockWebConnection().setDefaultResponse("<html><title>Response</title></html>");

        final WebDriver driver = loadPage2(html, URL_FIRST, "text/html;charset=UTF-8", UTF_8, null);
        verifyTitle2(driver, new String[] {"done"});

        String headerContentType = getMockWebConnection().getLastWebRequest().getAdditionalHeaders()
                .get(HttpHeader.CONTENT_TYPE);
        headerContentType = headerContentType.replace("; ", ";"); // normalize
        assertEquals(getExpectedAlerts()[0], headerContentType);
        assertEquals(getExpectedAlerts()[1], getMockWebConnection().getLastWebRequest().getRequestBody());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("InvalidStateError/DOMException")
    public void setRequestHeaderNotOpend() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function doTest() {\n"
            + "  try {\n"
            + "    var xhr = new XMLHttpRequest();\n"
            + "    xhr.setRequestHeader('Content-Type', 'text/jpeg');\n"
            + "    log('done');\n"
            + "  } catch(e) { logEx(e); }\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='doTest()'>\n"
            + "</body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails.
     */
    @Test
    @Alerts("undefined")
    public void getOwnPropertyDescriptor_onabort() throws Exception {
        getOwnPropertyDescriptor("onabort");
    }

    /**
     * @throws Exception if the test fails.
     */
    @Test
    @Alerts("undefined")
    public void getOwnPropertyDescriptor_onerror() throws Exception {
        getOwnPropertyDescriptor("onerror");
    }

    /**
     * @throws Exception if the test fails.
     */
    @Test
    @Alerts("undefined")
    public void getOwnPropertyDescriptor_onload() throws Exception {
        getOwnPropertyDescriptor("onload");
    }

    /**
     * @throws Exception if the test fails.
     */
    @Test
    @Alerts("undefined")
    public void getOwnPropertyDescriptor_onloadstart() throws Exception {
        getOwnPropertyDescriptor("onloadstart");
    }

    /**
     * @throws Exception if the test fails.
     */
    @Test
    @Alerts("undefined")
    public void getOwnPropertyDescriptor_onloadend() throws Exception {
        getOwnPropertyDescriptor("onloadend");
    }

    /**
     * @throws Exception if the test fails.
     */
    @Test
    @Alerts("undefined")
    public void getOwnPropertyDescriptor_onprogress() throws Exception {
        getOwnPropertyDescriptor("onprogress");
    }

    /**
     * @throws Exception if the test fails.
     */
    @Test
    @Alerts(DEFAULT = {"[object Object]", "undefined", "undefined",
                       "function get onreadystatechange() { [native code] }",
                       "function set onreadystatechange() { [native code] }",
                       "true", "true"},
            FF = {"[object Object]", "undefined", "undefined",
                  "function onreadystatechange() { [native code] }",
                  "function onreadystatechange() { [native code] }",
                  "true", "true"},
            FF_ESR = {"[object Object]", "undefined", "undefined",
                      "function onreadystatechange() { [native code] }",
                      "function onreadystatechange() { [native code] }",
                      "true", "true"})
    @HtmlUnitNYI(CHROME = {"[object Object]", "undefined", "undefined",
                           "function onreadystatechange() { [native code] }",
                           "function onreadystatechange() { [native code] }",
                           "true", "true"},
            EDGE = {"[object Object]", "undefined", "undefined",
                    "function onreadystatechange() { [native code] }",
                    "function onreadystatechange() { [native code] }",
                    "true", "true"})
    public void getOwnPropertyDescriptor_onreadystatechange() throws Exception {
        getOwnPropertyDescriptor("onreadystatechange");
    }

    /**
     * @throws Exception if the test fails.
     */
    @Test
    @Alerts("undefined")
    public void getOwnPropertyDescriptor_ontimeout() throws Exception {
        getOwnPropertyDescriptor("ontimeout");
    }

    private void getOwnPropertyDescriptor(final String event) throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + LOG_TITLE_FUNCTION
            + "      var request;\n"
            + "      function test() {\n"
            + "        var desc = Object.getOwnPropertyDescriptor("
                                + "XMLHttpRequest.prototype, '" + event + "');\n"
            + "        log(desc);\n"
            + "        if(!desc) { return; }\n"

            + "        log(desc.value);\n"
            + "        log(desc.writable);\n"
            + "        log(desc.get);\n"
            + "        log(desc.set);\n"
            + "        log(desc.configurable);\n"
            + "        log(desc.enumerable);\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='test()'>\n"
            + "  </body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"", "arraybuffer", "blob", "json", "text", "text", "text", "text", "text", ""})
    public void responseTypeSetBeforeOpen() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + LOG_TITLE_FUNCTION
            + "      function testSync() {\n"
            + "        var request = new XMLHttpRequest();\n"
            + "        log(request.responseType);\n"
            + "      try {\n"
            + "        request.responseType = 'arraybuffer';\n"
            + "        log(request.responseType);\n"
            + "        request.responseType = 'blob';\n"
            + "        log(request.responseType);\n"
            + "        request.responseType = 'json';\n"
            + "        log(request.responseType);\n"
            + "        request.responseType = 'text';\n"
            + "        log(request.responseType);\n"
            + "      } catch(e) { logEx(e); }\n"

            + "      try {\n"
            + "        request.responseType = 'JsON';\n"
            + "        log(request.responseType);\n"

            + "        request.responseType = 'unknown';\n"
            + "        log(request.responseType);\n"
            + "      } catch(e) { logEx(e); }\n"

            + "      try {\n"
            + "        request.responseType = null;\n"
            + "        log(request.responseType);\n"
            + "      } catch(e) { logEx(e); }\n"

            + "      try {\n"
            + "        request.responseType = undefined;\n"
            + "        log(request.responseType);\n"
            + "      } catch(e) { logEx(e); }\n"

            + "      try {\n"
            + "        request.responseType = '';\n"
            + "        log(request.responseType);\n"
            + "      } catch(e) { logEx(e); }\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='testSync()'>\n"
            + "  </body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"", "InvalidAccessError/DOMException", "InvalidAccessError/DOMException",
             "InvalidAccessError/DOMException", "InvalidAccessError/DOMException",
             "InvalidAccessError/DOMException", "", "", "", "", "InvalidAccessError/DOMException"})
    public void responseTypeSetAfterOpenSync() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + LOG_TITLE_FUNCTION
            + "      function testSync() {\n"
            + "        var request = new XMLHttpRequest();\n"
            + "        request.open('GET', '" + URL_SECOND + "', false);\n"

            + "        log(request.responseType);\n"

            + "      try {\n"
            + "        request.responseType = 'arraybuffer';\n"
            + "        log(request.responseType);\n"
            + "      } catch(e) { logEx(e); }\n"

            + "      try {\n"
            + "        request.responseType = 'blob';\n"
            + "        log(request.responseType);\n"
            + "      } catch(e) { logEx(e); }\n"

            + "      try {\n"
            + "        request.responseType = 'json';\n"
            + "        log(request.responseType);\n"
            + "      } catch(e) { logEx(e); }\n"

            + "      try {\n"
            + "        request.responseType = 'text';\n"
            + "        log(request.responseType);\n"
            + "      } catch(e) { logEx(e); }\n"

            + "      try {\n"
            + "        request.responseType = 'document';\n"
            + "        log(request.responseType);\n"
            + "      } catch(e) { logEx(e); }\n"

            + "      try {\n"
            + "        request.responseType = 'JsON';\n"
            + "        log(request.responseType);\n"
            + "      } catch(e) { logEx(e); }\n"

            + "      try {\n"
            + "        request.responseType = 'unknown';\n"
            + "        log(request.responseType);\n"
            + "      } catch(e) { logEx(e); }\n"

            + "      try {\n"
            + "        request.responseType = null;\n"
            + "        log(request.responseType);\n"
            + "      } catch(e) { logEx(e); }\n"

            + "      try {\n"
            + "        request.responseType = undefined;\n"
            + "        log(request.responseType);\n"
            + "      } catch(e) { logEx(e); }\n"

            + "      try {\n"
            + "        request.responseType = '';\n"
            + "        log(request.responseType);\n"
            + "      } catch(e) { logEx(e); }\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='testSync()'>\n"
            + "  </body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"", "arraybuffer", "blob", "json", "text", "document",
             "document", "document", "document", "document", ""})
    public void responseTypeSetAfterOpenAsync() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + LOG_TITLE_FUNCTION
            + "      function testSync() {\n"
            + "        var request = new XMLHttpRequest();\n"
            + "        request.open('GET', '" + URL_SECOND + "', true);\n"

            + "        log(request.responseType);\n"
            + "      try {\n"
            + "        request.responseType = 'arraybuffer';\n"
            + "        log(request.responseType);\n"
            + "      } catch(e) { logEx(e); }\n"

            + "      try {\n"
            + "        request.responseType = 'blob';\n"
            + "        log(request.responseType);\n"
            + "      } catch(e) { logEx(e); }\n"

            + "      try {\n"
            + "        request.responseType = 'json';\n"
            + "        log(request.responseType);\n"
            + "      } catch(e) { logEx(e); }\n"

            + "      try {\n"
            + "        request.responseType = 'text';\n"
            + "        log(request.responseType);\n"
            + "      } catch(e) { logEx(e); }\n"

            + "      try {\n"
            + "        request.responseType = 'document';\n"
            + "        log(request.responseType);\n"
            + "      } catch(e) { logEx(e); }\n"

            + "      try {\n"
            + "        request.responseType = 'JsON';\n"
            + "        log(request.responseType);\n"
            + "      } catch(e) { logEx(e); }\n"

            + "      try {\n"
            + "        request.responseType = 'unknown';\n"
            + "        log(request.responseType);\n"
            + "      } catch(e) { logEx(e); }\n"

            + "      try {\n"
            + "        request.responseType = null;\n"
            + "        log(request.responseType);\n"
            + "      } catch(e) { logEx(e); }\n"

            + "      try {\n"
            + "        request.responseType = undefined;\n"
            + "        log(request.responseType);\n"
            + "      } catch(e) { logEx(e); }\n"

            + "      try {\n"
            + "        request.responseType = '';\n"
            + "        log(request.responseType);\n"
            + "      } catch(e) { logEx(e); }\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='testSync()'>\n"
            + "  </body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"", "", "arraybuffer", "InvalidStateError/DOMException",
                       "send done", "InvalidStateError/DOMException"},
            FF = {"", "", "arraybuffer", "", "send done", "InvalidStateError/DOMException"},
            FF_ESR = {"", "", "arraybuffer", "", "send done", "InvalidStateError/DOMException"})
    public void responseTextInvalidResponseType() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + LOG_TITLE_FUNCTION
            + "      var xhr;\n"
            + "      function test() {\n"
            + "        xhr = new XMLHttpRequest();\n"
            + "        log(xhr.responseText);\n"

            + "        xhr.open('GET', '" + URL_SECOND + "', true);\n"
            + "        log(xhr.responseText);\n"

            + "        try {\n"
            + "          xhr.responseType = 'arraybuffer';\n"
            + "        } catch(e) { logEx(e); }\n"
            + "        log(xhr.responseType);\n"

            + "        try {\n"
            + "          log(xhr.responseText);\n"
            + "        } catch(e) { logEx(e); }\n"

            + "        try {\n"
            + "          xhr.onreadystatechange = onStateChange;\n"
            + "        } catch(e) { logEx(e); }\n"

            + "        try {\n"
            + "          xhr.send('');\n"
            + "          log('send done');\n"
            + "        } catch(e) { logEx(e); }\n"
            + "      }\n"

            + "      function onStateChange(e) {\n"
            + "        if (xhr.readyState == 4) {\n"
            + "          try {\n"
            + "            log(xhr.responseText);\n"
            + "          } catch(e) { logEx(e); }\n"
            + "        }\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='test()'>\n"
            + "  </body>\n"
            + "</html>";

        final String xml =
              "<xml>\n"
            + "<content>blah</content>\n"
            + "</xml>";

        getMockWebConnection().setResponse(URL_SECOND, xml, MimeType.TEXT_XML);
        loadPage2(html);
        verifyTitle2(DEFAULT_WAIT_TIME, getWebDriver(), getExpectedAlerts());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"", "", "<xml>\\n<content>blah</content>\\n</xml>"})
    public void responseResponseTypeDefault() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + LOG_TITLE_FUNCTION_NORMALIZE
            + "      var xhr;\n"
            + "      function test() {\n"
            + "        xhr = new XMLHttpRequest();\n"
            + "        log(xhr.responseText);\n"

            + "        xhr.open('GET', '" + URL_SECOND + "', true);\n"
            + "        log(xhr.responseType);\n"

            + "        xhr.onreadystatechange = onStateChange;\n"
            + "        xhr.send('');\n"
            + "      }\n"

            + "      function onStateChange(e) {\n"
            + "        if (xhr.readyState == 4) {\n"
            + "          try {\n"
            + "            log(xhr.response);\n"
            + "          } catch(ex) { logEx(e); }\n"
            + "        }\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='test()'>\n"
            + "  </body>\n"
            + "</html>";

        final String xml =
              "<xml>\n"
            + "<content>blah</content>\n"
            + "</xml>";

        getMockWebConnection().setResponse(URL_SECOND, xml, MimeType.TEXT_XML);
        loadPage2(html);
        verifyTitle2(DEFAULT_WAIT_TIME, getWebDriver(), getExpectedAlerts());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"", "text", "<xml>\\n<content>blah</content>\\n</xml>"})
    public void responseResponseTypeText() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + LOG_TITLE_FUNCTION_NORMALIZE
            + "      var xhr;\n"
            + "      function test() {\n"
            + "        xhr = new XMLHttpRequest();\n"
            + "        log(xhr.responseText);\n"

            + "        xhr.open('GET', '" + URL_SECOND + "', true);\n"
            + "        xhr.responseType = 'text';\n"
            + "        log(xhr.responseType);\n"

            + "        xhr.onreadystatechange = onStateChange;\n"
            + "        xhr.send('');\n"
            + "      }\n"

            + "      function onStateChange(e) {\n"
            + "        if (xhr.readyState == 4) {\n"
            + "          try {\n"
            + "            log(xhr.response);\n"
            + "          } catch(ex) { logEx(e); }\n"
            + "        }\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='test()'>\n"
            + "  </body>\n"
            + "</html>";

        final String xml =
              "<xml>\n"
            + "<content>blah</content>\n"
            + "</xml>";

        getMockWebConnection().setResponse(URL_SECOND, xml, MimeType.TEXT_XML);
        loadPage2(html);
        verifyTitle2(DEFAULT_WAIT_TIME, getWebDriver(), getExpectedAlerts());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"", "arraybuffer", "[object ArrayBuffer]", "36"})
    public void responseResponseTypeArrayBuffer() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + LOG_TITLE_FUNCTION
            + "      var xhr;\n"
            + "      function test() {\n"
            + "        xhr = new XMLHttpRequest();\n"
            + "        log(xhr.responseText);\n"

            + "        xhr.open('GET', '" + URL_SECOND + "', true);\n"
            + "        xhr.responseType = 'arraybuffer';\n"
            + "        log(xhr.responseType);\n"

            + "        xhr.onreadystatechange = onStateChange;\n"
            + "        xhr.send('');\n"
            + "      }\n"

            + "      function onStateChange(e) {\n"
            + "        if (xhr.readyState == 4) {\n"
            + "          try {\n"
            + "            log(xhr.response);\n"
            + "            log(xhr.response.byteLength);\n"
            + "          } catch(ex) { logEx(e); }\n"
            + "        }\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='test()'>\n"
            + "  </body>\n"
            + "</html>";

        final String xml =
              "<xml>\n"
            + "<content>blah</content>\n"
            + "</xml>";

        getMockWebConnection().setResponse(URL_SECOND, xml, MimeType.TEXT_XML);
        loadPage2(html);
        verifyTitle2(DEFAULT_WAIT_TIME, getWebDriver(), getExpectedAlerts());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"", "arraybuffer", "[object ArrayBuffer]", "36"})
    public void responseResponseTypeArrayBufferGzipIncrease() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + LOG_TITLE_FUNCTION
            + "      var xhr;\n"
            + "      function test() {\n"
            + "        xhr = new XMLHttpRequest();\n"
            + "        log(xhr.responseText);\n"

            + "        xhr.open('GET', '" + URL_SECOND + "', true);\n"
            + "        xhr.responseType = 'arraybuffer';\n"
            + "        log(xhr.responseType);\n"

            + "        xhr.onreadystatechange = onStateChange;\n"
            + "        xhr.send('');\n"
            + "      }\n"

            + "      function onStateChange(e) {\n"
            + "        if (xhr.readyState == 4) {\n"
            + "          try {\n"
            + "            log(xhr.response);\n"
            + "            log(xhr.response.byteLength);\n"
            + "          } catch(ex) { logEx(e); }\n"
            + "        }\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='test()'>\n"
            + "  </body>\n"
            + "</html>";

        final String xml =
              "<xml>\n"
            + "<content>blah</content>\n"
            + "</xml>";

        final byte[] bytes = xml.getBytes(UTF_8);
        final ByteArrayOutputStream bos = new ByteArrayOutputStream();
        final GZIPOutputStream gout = new GZIPOutputStream(bos);
        gout.write(bytes);
        gout.finish();

        final byte[] encoded = bos.toByteArray();
        assertTrue(encoded.length > xml.length());

        final List<NameValuePair> headers = new LinkedList<>();
        headers.add(new NameValuePair("Content-Encoding", "gzip"));
        getMockWebConnection().setResponse(URL_SECOND, encoded, 200, "OK", MimeType.TEXT_XML, headers);
        loadPage2(html);
        verifyTitle2(DEFAULT_WAIT_TIME, getWebDriver(), getExpectedAlerts());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"", "arraybuffer", "[object ArrayBuffer]", "72"})
    public void responseResponseTypeArrayBufferGzipDecrease() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + LOG_TITLE_FUNCTION
            + "      var xhr;\n"
            + "      function test() {\n"
            + "        xhr = new XMLHttpRequest();\n"
            + "        log(xhr.responseText);\n"

            + "        xhr.open('GET', '" + URL_SECOND + "', true);\n"
            + "        xhr.responseType = 'arraybuffer';\n"
            + "        log(xhr.responseType);\n"

            + "        xhr.onreadystatechange = onStateChange;\n"
            + "        xhr.send('');\n"
            + "      }\n"

            + "      function onStateChange(e) {\n"
            + "        if (xhr.readyState == 4) {\n"
            + "          try {\n"
            + "            log(xhr.response);\n"
            + "            log(xhr.response.byteLength);\n"
            + "          } catch(ex) { logEx(e); }\n"
            + "        }\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='test()'>\n"
            + "  </body>\n"
            + "</html>";

        final String xml =
              "<xml>\n"
            + "<content>blahblahblahblahblahblahblahblahblahblah</content>\n"
            + "</xml>";

        final byte[] bytes = xml.getBytes(UTF_8);
        final ByteArrayOutputStream bos = new ByteArrayOutputStream();
        final GZIPOutputStream gout = new GZIPOutputStream(bos);
        gout.write(bytes);
        gout.finish();

        final byte[] encoded = bos.toByteArray();
        assertTrue(encoded.length < xml.length());

        final List<NameValuePair> headers = new LinkedList<>();
        headers.add(new NameValuePair("Content-Encoding", "gzip"));
        getMockWebConnection().setResponse(URL_SECOND, encoded, 200, "OK", MimeType.TEXT_XML, headers);
        loadPage2(html);
        verifyTitle2(DEFAULT_WAIT_TIME, getWebDriver(), getExpectedAlerts());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"", "arraybuffer", "[object ArrayBuffer]", "0"})
    public void responseResponseTypeArrayBufferEmpty() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + LOG_TITLE_FUNCTION
            + "      var xhr;\n"
            + "      function test() {\n"
            + "        xhr = new XMLHttpRequest();\n"
            + "        log(xhr.responseText);\n"

            + "        xhr.open('GET', '" + URL_SECOND + "', true);\n"
            + "        xhr.responseType = 'arraybuffer';\n"
            + "        log(xhr.responseType);\n"

            + "        xhr.onreadystatechange = onStateChange;\n"
            + "        xhr.send('');\n"
            + "      }\n"

            + "      function onStateChange(e) {\n"
            + "        if (xhr.readyState == 4) {\n"
            + "          try {\n"
            + "            log(xhr.response);\n"
            + "            log(xhr.response.byteLength);\n"
            + "          } catch(ex) { logEx(e); }\n"
            + "        }\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='test()'>\n"
            + "  </body>\n"
            + "</html>";

        final String xml = "";

        getMockWebConnection().setResponse(URL_SECOND, xml, MimeType.TEXT_XML);
        loadPage2(html);
        verifyTitle2(DEFAULT_WAIT_TIME, getWebDriver(), getExpectedAlerts());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"", "blob", "[object Blob]", "36", "text/xml"})
    public void responseResponseTypeBlob() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + LOG_TITLE_FUNCTION
            + "      var xhr;\n"
            + "      function test() {\n"
            + "        xhr = new XMLHttpRequest();\n"
            + "        log(xhr.responseText);\n"

            + "        xhr.open('GET', '" + URL_SECOND + "', true);\n"
            + "        xhr.responseType = 'blob';\n"
            + "        log(xhr.responseType);\n"

            + "        xhr.onreadystatechange = onStateChange;\n"
            + "        xhr.send('');\n"
            + "      }\n"

            + "      function onStateChange(e) {\n"
            + "        if (xhr.readyState == 4) {\n"
            + "          try {\n"
            + "            log(xhr.response);\n"
            + "            log(xhr.response.size);\n"
            + "            log(xhr.response.type);\n"
            + "          } catch(ex) { logEx(e); }\n"
            + "        }\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='test()'>\n"
            + "  </body>\n"
            + "</html>";

        final String xml =
              "<xml>\n"
            + "<content>blah</content>\n"
            + "</xml>";

        getMockWebConnection().setResponse(URL_SECOND, xml, MimeType.TEXT_XML);
        loadPage2(html);
        verifyTitle2(DEFAULT_WAIT_TIME, getWebDriver(), getExpectedAlerts());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"", "blob", "[object Blob]", "0"})
    public void responseResponseTypeBlobEmpty() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + LOG_TITLE_FUNCTION
            + "      var xhr;\n"
            + "      function test() {\n"
            + "        xhr = new XMLHttpRequest();\n"
            + "        log(xhr.responseText);\n"

            + "        xhr.open('GET', '" + URL_SECOND + "', true);\n"
            + "        xhr.responseType = 'blob';\n"
            + "        log(xhr.responseType);\n"

            + "        xhr.onreadystatechange = onStateChange;\n"
            + "        xhr.send('');\n"
            + "      }\n"

            + "      function onStateChange(e) {\n"
            + "        if (xhr.readyState == 4) {\n"
            + "          try {\n"
            + "            log(xhr.response);\n"
            + "            log(xhr.response.size);\n"
            + "          } catch(ex) { logEx(e); }\n"
            + "        }\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='test()'>\n"
            + "  </body>\n"
            + "</html>";

        final String xml = "";

        getMockWebConnection().setResponse(URL_SECOND, xml, MimeType.TEXT_XML);
        loadPage2(html);
        verifyTitle2(DEFAULT_WAIT_TIME, getWebDriver(), getExpectedAlerts());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"", "json", "[object Object]", "Unit", "{\"Html\":\"Unit\"}"})
    public void responseResponseTypeJson() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + LOG_TITLE_FUNCTION
            + "      var xhr;\n"
            + "      function test() {\n"
            + "        xhr = new XMLHttpRequest();\n"
            + "        log(xhr.responseText);\n"

            + "        xhr.open('GET', '" + URL_SECOND + "', true);\n"
            + "        xhr.responseType = 'json';\n"
            + "        log(xhr.responseType);\n"

            + "        xhr.onreadystatechange = onStateChange;\n"
            + "        xhr.send('');\n"
            + "      }\n"

            + "      function onStateChange(e) {\n"
            + "        if (xhr.readyState == 4) {\n"
            + "          try {\n"
            + "            log(xhr.response);\n"
            + "            log(xhr.response.Html);\n"
            + "            log(JSON.stringify(xhr.response));\n"
            + "          } catch(e) { logEx(e); }\n"
            + "        }\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='test()'>\n"
            + "  </body>\n"
            + "</html>";

        final String json = "{ \"Html\": \"Unit\" }";

        getMockWebConnection().setResponse(URL_SECOND, json, MimeType.APPLICATION_JSON);
        loadPage2(html);
        verifyTitle2(DEFAULT_WAIT_TIME, getWebDriver(), getExpectedAlerts());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"", "json", "null"})
    public void responseResponseTypeJsonEmpty() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + LOG_TITLE_FUNCTION
            + "      var xhr;\n"
            + "      function test() {\n"
            + "        xhr = new XMLHttpRequest();\n"
            + "        log(xhr.responseText);\n"

            + "        xhr.open('GET', '" + URL_SECOND + "', true);\n"
            + "        xhr.responseType = 'json';\n"
            + "        log(xhr.responseType);\n"

            + "        xhr.onreadystatechange = onStateChange;\n"
            + "        xhr.send('');\n"
            + "      }\n"

            + "      function onStateChange(e) {\n"
            + "        if (xhr.readyState == 4) {\n"
            + "          try {\n"
            + "            log(xhr.response);\n"
            + "          } catch(e) { logEx(e); }\n"
            + "        }\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='test()'>\n"
            + "  </body>\n"
            + "</html>";

        final String json = "";

        getMockWebConnection().setResponse(URL_SECOND, json, MimeType.APPLICATION_JSON);
        loadPage2(html);
        verifyTitle2(DEFAULT_WAIT_TIME, getWebDriver(), getExpectedAlerts());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"", "document", "[object XMLDocument]"})
    public void responseResponseTypeDocumentXml() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + LOG_TITLE_FUNCTION
            + "      var xhr;\n"
            + "      function test() {\n"
            + "        xhr = new XMLHttpRequest();\n"
            + "        log(xhr.responseText);\n"

            + "        xhr.open('GET', '" + URL_SECOND + "', true);\n"
            + "        xhr.responseType = 'document';\n"
            + "        log(xhr.responseType);\n"

            + "        xhr.onreadystatechange = onStateChange;\n"
            + "        xhr.send('');\n"
            + "      }\n"

            + "      function onStateChange(e) {\n"
            + "        if (xhr.readyState == 4) {\n"
            + "          try {\n"
            + "            log(xhr.response);\n"
            + "          } catch(e) { logEx(e); }\n"
            + "        }\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='test()'>\n"
            + "  </body>\n"
            + "</html>";

        final String xml =
                "<xml>\n"
              + "<content>blah</content>\n"
              + "</xml>";

        getMockWebConnection().setResponse(URL_SECOND, xml, MimeType.TEXT_XML);
        loadPage2(html);
        verifyTitle2(DEFAULT_WAIT_TIME, getWebDriver(), getExpectedAlerts());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"", "document", "[object HTMLDocument]"})
    public void responseResponseTypeDocumentHtml() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + LOG_TITLE_FUNCTION
            + "      var xhr;\n"
            + "      function test() {\n"
            + "        xhr = new XMLHttpRequest();\n"
            + "        log(xhr.responseText);\n"

            + "        xhr.open('GET', '" + URL_SECOND + "', true);\n"
            + "        xhr.responseType = 'document';\n"
            + "        log(xhr.responseType);\n"

            + "        xhr.onreadystatechange = onStateChange;\n"
            + "        xhr.send('');\n"
            + "      }\n"

            + "      function onStateChange(e) {\n"
            + "        if (xhr.readyState == 4) {\n"
            + "          try {\n"
            + "            log(xhr.response);\n"
            + "          } catch(e) { logEx(e); }\n"
            + "        }\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='test()'>\n"
            + "  </body>\n"
            + "</html>";

        final String xml =
                "<html>\n"
              + "<body>Test</body>\n"
              + "<html>";

        getMockWebConnection().setResponse(URL_SECOND, xml, MimeType.TEXT_HTML);
        loadPage2(html);
        verifyTitle2(DEFAULT_WAIT_TIME, getWebDriver(), getExpectedAlerts());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"", "document", "null"})
    public void responseResponseTypeDocumentJs() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + LOG_TITLE_FUNCTION
            + "      var xhr;\n"
            + "      function test() {\n"
            + "        xhr = new XMLHttpRequest();\n"
            + "        log(xhr.responseText);\n"

            + "        xhr.open('GET', '" + URL_SECOND + "', true);\n"
            + "        xhr.responseType = 'document';\n"
            + "        log(xhr.responseType);\n"

            + "        xhr.onreadystatechange = onStateChange;\n"
            + "        xhr.send('');\n"
            + "      }\n"

            + "      function onStateChange(e) {\n"
            + "        if (xhr.readyState == 4) {\n"
            + "          try {\n"
            + "            log(xhr.response);\n"
            + "          } catch(e) { logEx(e); }\n"
            + "        }\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='test()'>\n"
            + "  </body>\n"
            + "</html>";

        final String xml =
                "<html>\n"
              + "<body>Test</body>\n"
              + "<html>";

        getMockWebConnection().setResponse(URL_SECOND, xml, MimeType.TEXT_JAVASCRIPT);
        loadPage2(html);
        verifyTitle2(DEFAULT_WAIT_TIME, getWebDriver(), getExpectedAlerts());
    }
}
