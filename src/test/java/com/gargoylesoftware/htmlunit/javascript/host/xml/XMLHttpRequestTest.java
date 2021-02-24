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
package com.gargoylesoftware.htmlunit.javascript.host.xml;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Locale;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.ComparisonFailure;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.HtmlUnitNYI;
import com.gargoylesoftware.htmlunit.BrowserRunner.Tries;
import com.gargoylesoftware.htmlunit.HttpHeader;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPageTest;
import com.gargoylesoftware.htmlunit.util.MimeType;
import com.gargoylesoftware.htmlunit.util.NameValuePair;

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
@RunWith(BrowserRunner.class)
public class XMLHttpRequestTest extends WebDriverTestCase {

    private static final String UNINITIALIZED = String.valueOf(XMLHttpRequest.UNSENT);
    private static final String LOADING = String.valueOf(XMLHttpRequest.OPENED);
    private static final String COMPLETED = String.valueOf(XMLHttpRequest.DONE);

    /**
     * Closes the real IE; otherwise tests are failing because of cached responses.
     */
    @After
    public void shutDownRealBrowsersAfter() {
        shutDownRealIE();
    }

    /**
     * Tests synchronous use of XMLHttpRequest.
     * @throws Exception if the test fails
     */
    @Test
    @Tries(3)
    public void syncUse() throws Exception {
        // TODO [IE]SINGLE-VS-BULK test runs when executed as single but breaks as bulk
        shutDownRealIE();

        final String html =
              "<html>\n"
            + "  <head>\n"
            + "    <title>XMLHttpRequest Test</title>\n"
            + "    <script>\n"
            + "      function testSync() {\n"
            + "        var request = new XMLHttpRequest();\n"
            + "        alert(request.readyState);\n"
            + "        request.open('GET', '" + URL_SECOND + "', false);\n"
            + "        alert(request.readyState);\n"
            + "        request.send('');\n"
            + "        alert(request.readyState);\n"
            + "        alert(request.responseText);\n"
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

        setExpectedAlerts(UNINITIALIZED, LOADING, COMPLETED, xml);
        getMockWebConnection().setDefaultResponse(xml, MimeType.TEXT_XML);

        loadPageWithAlerts2(html);
    }

    /**
     * Tests style object creation.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("[object XMLHttpRequest]")
    public void creation() throws Exception {
        final String html =
            "<html>\n"
            + "  <head>\n"
            + "    <title>XMLHttpRequest Test</title>\n"
            + "    <script>\n"
            + "        if (window.XMLHttpRequest) {\n"
            + "          alert(new XMLHttpRequest());\n"
            + "        }\n"
            + "        else if (window.ActiveXObject) {\n"
            + "          new ActiveXObject('Microsoft.XMLHTTP');\n"
            + "          alert('activeX created');\n"
            + "        }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body></body>\n"
            + "</html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"1: 0-", "2: ", "3: 200-OK"})
    public void statusSync() throws Exception {
        final String html =
            "<html>\n"
            + "  <head>\n"
            + "    <title>XMLHttpRequest Test</title>\n"
            + "<script>\n"
            + "  var xhr = new XMLHttpRequest();\n"

            + "  alertStatus('1: ');\n"
            + "  xhr.open('GET', '/foo.xml', false);\n"
            + "  alert('2: ');\n"

            + "  xhr.send();\n"
            + "  alertStatus('3: ');\n"

            + "  function alertStatus(prefix) {\n"
            + "    var msg = prefix;\n"
            + "    try {\n"
            + "      msg = msg + xhr.status + '-';\n"
            + "    } catch(e) { msg = msg + 'ex: status' + '-' }\n"
            + "    try {\n"
            + "      msg = msg + xhr.statusText;;\n"
            + "    } catch(e) { msg = msg + 'ex: statusText' }\n"
            + "    alert(msg);\n"
            + "  }\n"
            + "</script>\n"
            + "  </head>\n"
            + "  <body></body>\n"
            + "</html>";

        getMockWebConnection().setDefaultResponse("<res></res>", MimeType.TEXT_XML);
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"1: 0-", "2: 0-", "#1: 0-", "3: 0-", "4: 0-", "#2: 200-OK", "#3: 200-OK", "#4: 200-OK"},
            IE = {"1: 0-", "2: 0-", "#1: 0-", "3: 0-", "#1: 0-", "4: 0-", "#2: 200-OK", "#3: 200-OK", "#4: 200-OK"})
    public void statusAsync() throws Exception {
        final String html =
            "<html>\n"
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
            + "      msg = msg + xhr.statusText;;\n"
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
        final long maxWait = System.currentTimeMillis() + DEFAULT_WAIT_TIME;
        while (true) {
            try {
                final String text = driver.findElement(By.id("log")).getAttribute("value").trim().replaceAll("\r", "");
                assertEquals(expected, text);
                return;
            }
            catch (final ComparisonFailure e) {
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
        final String html = "<html>\n"
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
    @Alerts(DEFAULT = {"orsc1", "open-done", "send-done",
                "orsc2", "orsc3", "orsc4", "4", "<a>b</a>", "[object XMLHttpRequest]"},
            IE = {"orsc1", "open-done", "orsc1", "send-done",
                "orsc2", "orsc3", "orsc4", "4", "<a>b</a>", "[object XMLHttpRequest]"})
    public void onload() throws Exception {
        // TODO [IE]SINGLE-VS-BULK test runs when executed as single but breaks as bulk
        shutDownRealIE();

        final String html =
              "<html>\n"
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
        final String html =
            "<html>\n"
            + "  <head>\n"
            + "    <title>XMLHttpRequest Test</title>\n"
            + "    <script>\n"
            + "        var request = new XMLHttpRequest();\n"

            + "        alert(request.getResponseHeader('content-length'));\n"
            + "        request.open('GET', '/foo.xml', false);\n"
            + "        alert(request.getResponseHeader('content-length'));\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body></body>\n"
            + "</html>";
        loadPageWithAlerts2(html);
    }

    /**
     * Regression test for http://sourceforge.net/p/htmlunit/bugs/269/.
     * @throws Exception if the test fails
     */
    @Test
    public void relativeUrl() throws Exception {
        // TODO [IE]SINGLE-VS-BULK test runs when executed as single but breaks as bulk
        shutDownRealIE();

        final String html =
              "<html>\n"
            + "  <head>\n"
            + "    <title>XMLHttpRequest Test</title>\n"
            + "    <script>\n"
            + "      function testSync() {\n"
            + "        var request = new XMLHttpRequest();\n"
            + "        request.open('GET', '/foo.xml', false);\n"
            + "        request.send('');\n"
            + "        alert(request.readyState);\n"
            + "        alert(request.responseText);\n"
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

        setExpectedAlerts(COMPLETED, xml);
        getMockWebConnection().setDefaultResponse(xml, MimeType.TEXT_XML);
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("bla bla")
    public void responseText_NotXml() throws Exception {
        // TODO [IE]SINGLE-VS-BULK test runs when executed as single but breaks as bulk
        shutDownRealIE();

        final String html = "<html><head>\n"
            + "<script>\n"
            + "function test() {\n"
            + "  var request = new XMLHttpRequest();\n"
            + "  request.open('GET', 'foo.txt', false);\n"
            + "  request.send('');\n"
            + "  alert(request.responseText);\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'></body></html>";

        getMockWebConnection().setDefaultResponse("bla bla", MimeType.TEXT_PLAIN);
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("null")
    public void responseXML_text_html() throws Exception {
        // TODO [IE]SINGLE-VS-BULK test runs when executed as single but breaks as bulk
        shutDownRealIE();

        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var xhr = new XMLHttpRequest();\n"
            + "    xhr.open('GET', 'foo.xml', false);\n"
            + "    xhr.send('');\n"
            + "    try {\n"
            + "      alert(xhr.responseXML);\n"
            + "    } catch(e) { alert('exception'); }\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        getMockWebConnection().setDefaultResponse("<html></html>", MimeType.TEXT_HTML);
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("[object XMLDocument]")
    public void responseXML_text_xml() throws Exception {
        // TODO [IE]SINGLE-VS-BULK test runs when executed as single but breaks as bulk
        shutDownRealIE();

        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var xhr = new XMLHttpRequest();\n"
            + "    xhr.open('GET', 'foo.xml', false);\n"
            + "    xhr.send('');\n"
            + "    try {\n"
            + "      alert(xhr.responseXML);\n"
            + "    } catch(e) { alert('exception'); }\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        getMockWebConnection().setDefaultResponse("<note/>", MimeType.TEXT_XML);
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("[object XMLDocument]")
    public void responseXML_application_xml() throws Exception {
        // TODO [IE]SINGLE-VS-BULK test runs when executed as single but breaks as bulk
        shutDownRealIE();

        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var xhr = new XMLHttpRequest();\n"
            + "    xhr.open('GET', 'foo.xml', false);\n"
            + "    xhr.send('');\n"
            + "    try {\n"
            + "      alert(xhr.responseXML);\n"
            + "    } catch(e) { alert('exception'); }\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        getMockWebConnection().setDefaultResponse("<note/>", "application/xml");
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("[object XMLDocument]")
    public void responseXML_application_xhtmlXml() throws Exception {
        // TODO [IE]SINGLE-VS-BULK test runs when executed as single but breaks as bulk
        shutDownRealIE();

        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var xhr = new XMLHttpRequest();\n"
            + "    xhr.open('GET', 'foo.xml', false);\n"
            + "    xhr.send('');\n"
            + "    try {\n"
            + "      alert(xhr.responseXML);\n"
            + "    } catch(e) { alert('exception'); }\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        getMockWebConnection().setDefaultResponse("<html/>", "application/xhtml+xml");
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("[object XMLDocument]")
    public void responseXML_application_svgXml() throws Exception {
        // TODO [IE]SINGLE-VS-BULK test runs when executed as single but breaks as bulk
        shutDownRealIE();

        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var xhr = new XMLHttpRequest();\n"
            + "    xhr.open('GET', 'foo.xml', false);\n"
            + "    xhr.send('');\n"
            + "    try {\n"
            + "      alert(xhr.responseXML);\n"
            + "    } catch(e) { alert('exception'); }\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        getMockWebConnection().setDefaultResponse("<svg xmlns=\"http://www.w3.org/2000/svg\"/>", "image/svg+xml");
        loadPageWithAlerts2(html);
    }

    /**
     * Regression test for IE specific properties attribute.text &amp; attribute.xml.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"1", "someAttr", "undefined", "undefined"})
    public void responseXML2() throws Exception {
        // TODO [IE]SINGLE-VS-BULK test runs when executed as single but breaks as bulk
        shutDownRealIE();

        final String html = "<html><head>\n"
            + "<script>\n"
            + "function test() {\n"
            + "  var request = new XMLHttpRequest();\n"
            + "  request.open('GET', 'foo.xml', false);\n"
            + "  request.send('');\n"
            + "  var childNodes = request.responseXML.childNodes;\n"
            + "  alert(childNodes.length);\n"
            + "  var rootNode = childNodes[0];\n"
            + "  alert(rootNode.attributes[0].nodeName);\n"
            + "  alert(rootNode.attributes[0].text);\n"
            + "  alert(rootNode.attributes[0].xml);\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'></body></html>";

        final URL urlPage2 = new URL(URL_FIRST, "foo.xml");
        getMockWebConnection().setResponse(urlPage2,
            "<bla someAttr='someValue'><foo><fi id='fi1'/><fi/></foo></bla>\n",
            MimeType.TEXT_XML);
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("received: null")
    public void responseXML_siteNotExisting() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + "function test() {\n"
            + "  var request = new XMLHttpRequest();\n"
            + "try {\n"
            + "  request.open('GET', 'http://this.doesnt.exist/foo.xml', false);\n"
            + "  request.send('');\n"
            + "} catch(e) {\n"
            + "  alert('received: ' + request.responseXML);\n"
            + "}\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'></body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void sendNull() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + "function test() {\n"
            + "  var request = new XMLHttpRequest();\n"
            + "  request.open('GET', 'foo.txt', false);\n"
            + "  request.send(null);\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'></body></html>";

        getMockWebConnection().setDefaultResponse("");
        loadPageWithAlerts2(html);
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
        final String html = "<html><head>\n"
            + "<script>\n"
            + "function test() {\n"
            + "  var request = new XMLHttpRequest();\n"
            + "  request.open('GET', 'foo.txt', false);\n"
            + "  request.send(" + sendArg + ");\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'></body></html>";

        getMockWebConnection().setDefaultResponse("");
        loadPageWithAlerts2(html);
    }

    /**
     * Regression test for bug 1357412.
     * Response received by the XMLHttpRequest should not come in any window
     * @throws Exception if the test fails
     */
    @Test
    public void responseNotInWindow() throws Exception {
        final String html = "<html><head><title>foo</title>\n"
            + "<script>\n"
            + "function test() {\n"
            + "  var request = new XMLHttpRequest();\n"
            + "  request.open('GET', 'foo.txt', false);\n"
            + "  request.send();\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'></body></html>";

        getMockWebConnection().setDefaultResponse("");
        final WebDriver driver = loadPageWithAlerts2(html);
        assertEquals(URL_FIRST.toString(), driver.getCurrentUrl());
        assertTitle(driver, "foo");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"true", "false"})
    public void overrideMimeType() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + "function test() {\n"
            + "try {\n"
            + "  var request = new XMLHttpRequest();\n"
            + "  request.open('GET', 'foo.xml.txt', false);\n"
            + "  request.send('');\n"
            + "  alert(request.responseXML == null);\n"
            + "  request.open('GET', 'foo.xml.txt', false);\n"
            + "  request.overrideMimeType('text/xml');\n"
            + "  request.send('');\n"
            + "  alert(request.responseXML == null);\n"
            + "} catch (e) { alert('exception'); }\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'></body></html>";

        final URL urlPage2 = new URL(URL_FIRST, "foo.xml.txt");
        getMockWebConnection().setResponse(urlPage2,
            "<bla someAttr='someValue'><foo><fi id='fi1'/><fi/></foo></bla>\n",
            MimeType.TEXT_PLAIN);
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"true", "exception"})
    public void overrideMimeTypeAfterSend() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + "function test() {\n"
            + "  var request = new XMLHttpRequest();\n"
            + "  request.open('GET', 'foo.xml.txt', false);\n"
            + "  request.send('');\n"
            + "  alert(request.responseXML == null);\n"
            + "  try {\n"
            + "    request.overrideMimeType('text/xml');\n"
            + "    alert('overwritten');\n"
            + "  } catch (e) { alert('exception'); }\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'></body></html>";

        final URL urlPage2 = new URL(URL_FIRST, "foo.xml.txt");
        getMockWebConnection().setResponse(urlPage2,
            "<bla someAttr='someValue'><foo><fi id='fi1'/><fi/></foo></bla>\n",
            MimeType.TEXT_PLAIN);
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("27035")
    public void overrideMimeType_charset() throws Exception {
        // TODO [IE]SINGLE-VS-BULK test runs when executed as single but breaks as bulk
        shutDownRealIE();

        final String html = "<html><head>\n"
            + "<script>\n"
            + "function test() {\n"
            + "try {\n"
            + "  var request = new XMLHttpRequest();\n"
            + "  request.open('GET', '" + URL_SECOND + "', false);\n"
            + "  request.overrideMimeType('text/plain; charset=GBK');\n"
            + "  request.send('');\n"
            + "  alert(request.responseText.charCodeAt(0));\n"
            + "} catch (e) { alert('exception'); }\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'></body></html>";

        getMockWebConnection().setResponse(URL_SECOND, "\u9EC4", MimeType.TEXT_PLAIN, UTF_8);
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("27035")
    public void overrideMimeType_charset_upper_case() throws Exception {
        // TODO [IE]SINGLE-VS-BULK test runs when executed as single but breaks as bulk
        shutDownRealIE();

        final String html = "<html><head>\n"
            + "<script>\n"
            + "function test() {\n"
            + "try {\n"
            + "  var request = new XMLHttpRequest();\n"
            + "  request.open('GET', '" + URL_SECOND + "', false);\n"
            + "  request.overrideMimeType('text/plain; chaRSet=GBK');\n"
            + "  request.send('');\n"
            + "  alert(request.responseText.charCodeAt(0));\n"
            + "} catch (e) { alert('exception'); }\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'></body></html>";

        getMockWebConnection().setResponse(URL_SECOND, "\u9EC4", MimeType.TEXT_PLAIN, UTF_8);
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("40644")
    public void overrideMimeType_charset_empty() throws Exception {
        // TODO [IE]SINGLE-VS-BULK test runs when executed as single but breaks as bulk
        shutDownRealIE();

        final String html = "<html><head>\n"
            + "<script>\n"
            + "function test() {\n"
            + "try {\n"
            + "  var request = new XMLHttpRequest();\n"
            + "  request.open('GET', '" + URL_SECOND + "', false);\n"
            + "  request.overrideMimeType('text/plain; charset=');\n"
            + "  request.send('');\n"
            + "  alert(request.responseText.charCodeAt(0));\n"
            + "} catch (e) { alert('exception'); }\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'></body></html>";

        getMockWebConnection().setResponse(URL_SECOND, "\u9EC4", MimeType.TEXT_PLAIN, UTF_8);
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "40644",
            IE = {})
    public void overrideMimeType_charset_wrong() throws Exception {
        // TODO [IE]SINGLE-VS-BULK test runs when executed as single but breaks as bulk
        shutDownRealIE();

        final String html = "<html><head>\n"
            + "<script>\n"
            + "function test() {\n"
            + "  try {\n"
            + "    var request = new XMLHttpRequest();\n"
            + "    request.open('GET', '" + URL_SECOND + "', false);\n"
            + "    request.overrideMimeType('text/plain; charset=abcdefg');\n"
            + "    request.send('');\n"
            + "    var text = request.responseText;\n"
            + "    for (var i = 0; i < text.length; i++) {\n"
            + "      alert(text.charCodeAt(i));\n"
            + "    }\n"
            + "  } catch (e) { alert('exception'); }\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'></body></html>";

        getMockWebConnection().setResponse(URL_SECOND, "\u9EC4", MimeType.TEXT_PLAIN, UTF_8);
        loadPageWithAlerts2(html);
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
        // TODO [IE]SINGLE-VS-BULK test runs when executed as single but breaks as bulk
        shutDownRealIE();

        final String html =
              "<html>\n"
            + "  <head>\n"
            + "    <title>XMLHttpRequest Test</title>\n"
            + "    <script>\n"
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
            + "          alert(theResult);\n"
            + "          theResult = theData.replace('abcde', 'xxxxx');\n"
            + "          alert(theResult);\n"
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
        loadPageWithAlerts2(html);
    }

    /**
     * Tests that the <tt>Referer</tt> header is set correctly.
     * @throws Exception if the test fails
     */
    @Test
    public void refererHeader() throws Exception {
        final String html = "<html><head><script>\n"
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
     * Test for bug
     * <a href="https://sourceforge.net/tracker/?func=detail&atid=448266&aid=1784330&group_id=47038">issue 515</a>.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "ActiveXObject not available",
            IE = {"0", "0"})
    public void caseInsensitivityActiveXConstructor() throws Exception {
        final String html = "<html><head><script>\n"
            + "function test() {\n"
            + "  try {\n"
            + "    var req = new ActiveXObject('MSXML2.XmlHttp');\n"
            + "    alert(req.readyState);\n"

            + "    var req = new ActiveXObject('msxml2.xMLhTTp');\n"
            + "    alert(req.readyState);\n"
            + "  } catch (e) { alert('ActiveXObject not available'); }\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'></body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("selectNodes not available")
    public void responseXML_selectNodesIE() throws Exception {
        final String html =
              "<html>\n"
            + "  <head>\n"
            + "    <title>XMLHttpRequest Test</title>\n"
            + "    <script>\n"
            + "      function test() {\n"
            + "        var request = new XMLHttpRequest();\n"
            + "        request.open('GET', '" + URL_SECOND + "', false);\n"
            + "        request.send('');\n"
            + "        if (!request.responseXML.selectNodes) { alert('selectNodes not available'); return }\n"
            + "        alert(request.responseXML.selectNodes('//content').length);\n"
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
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"[object Element]", "myID", "blah", "span", "[object XMLDocument]"},
            IE = {"null", "myID", "blah", "span", "[object XMLDocument]"})
    public void responseXML_getElementById2() throws Exception {
        // TODO [IE]SINGLE-VS-BULK test runs when executed as single but breaks as bulk
        shutDownRealIE();

        final String html =
              "<html>\n"
            + "  <head>\n"
            + "    <title>XMLHttpRequest Test</title>\n"
            + "    <script>\n"
            + "      function test() {\n"
            + "        var request = new XMLHttpRequest();\n"
            + "        request.open('GET', '" + URL_SECOND + "', false);\n"
            + "        request.send('');\n"
            + "        if (request.responseXML.getElementById) {\n"
            + "          alert(request.responseXML.getElementById('id1'));\n"
            + "          alert(request.responseXML.getElementById('myID').id);\n"
            + "          alert(request.responseXML.getElementById('myID').innerHTML);\n"
            + "          alert(request.responseXML.getElementById('myID').tagName);\n"
            + "          alert(request.responseXML.getElementById('myID').ownerDocument);\n"
            + "        } else  {\n"
            + "          alert('responseXML.getElementById not available');\n"
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
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[object Element]", "[object Element]", "[object HTMLBodyElement]",
                "[object HTMLSpanElement]", "[object XMLDocument]", "undefined"})
    public void responseXML_getElementById() throws Exception {
        // TODO [IE]SINGLE-VS-BULK test runs when executed as single but breaks as bulk
        shutDownRealIE();

        final String html =
              "<html>\n"
            + "  <head>\n"
            + "    <title>XMLHttpRequest Test</title>\n"
            + "    <script>\n"
            + "      function test() {\n"
            + "        var request = new XMLHttpRequest();\n"
            + "        request.open('GET', '" + URL_SECOND + "', false);\n"
            + "        request.send('');\n"
            + "        var doc = request.responseXML;\n"
            + "        alert(doc.documentElement);\n"
            + "        alert(doc.documentElement.childNodes[0]);\n"
            + "        alert(doc.documentElement.childNodes[1]);\n"
            + "        if (doc.getElementById) {\n"
            + "          alert(doc.getElementById('out'));\n"
            + "          alert(doc.getElementById('out').ownerDocument);\n"
            + "        }\n"
            + "        alert(doc.documentElement.childNodes[1].xml);\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='test()'>\n"
            + "  </body>\n"
            + "</html>";

        final String xml =
              "<html>"
            + "<head>"
            + "</head>"
            + "<body xmlns='http://www.w3.org/1999/xhtml'>"
            + "<span id='out'>Hello Bob Dole!</span>"
            + "</body>"
            + "</html>";

        getMockWebConnection().setResponse(URL_SECOND, xml, MimeType.TEXT_XML);
        loadPageWithAlerts2(html);
    }

    /**
     * Verifies that the default encoding for an XMLHttpRequest is UTF-8.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("ol\u00E9")
    public void defaultEncodingIsUTF8() throws Exception {
        // TODO [IE]SINGLE-VS-BULK test runs when executed as single but breaks as bulk
        shutDownRealIE();

        final String html =
              "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + "      function test() {\n"
            + "        var request = new XMLHttpRequest();\n"
            + "        request.open('GET', '" + URL_SECOND + "', false);\n"
            + "        request.send('');\n"
            + "        alert(request.responseText);\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='test()'>\n"
            + "  </body>\n"
            + "</html>";

        final String response = "ol\u00E9";
        final byte[] responseBytes = response.getBytes(UTF_8);

        getMockWebConnection().setResponse(URL_SECOND, responseBytes, 200, "OK", MimeType.TEXT_HTML,
            new ArrayList<NameValuePair>());
        loadPageWithAlerts2(html);
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
        final String html =
              "<html>\n"
            + "  <head>\n"
            + "    <title>XMLHttpRequest Test</title>\n"
            + "    <script>\n"
            + "      function test() {\n"
            + "        try {\n"
            + "          var request = new XMLHttpRequest();\n"
            + "          request.open('GET', '" + URL_SECOND + "', false);\n"
            + "          request.send('');\n"
            + "          alert(request.responseXML.getElementById('myID').id);\n"
            + "        } catch (e) { alert('exception'); }\n"
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
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("myInput")
    public void responseXML_html_form() throws Exception {
        // TODO [IE]SINGLE-VS-BULK test runs when executed as single but breaks as bulk
        shutDownRealIE();

        final String html =
              "<html>\n"
            + "  <head>\n"
            + "    <title>XMLHttpRequest Test</title>\n"
            + "    <script>\n"
            + "      function test() {\n"
            + "        try {\n"
            + "          var request = new XMLHttpRequest();\n"
            + "          request.open('GET', '" + URL_SECOND + "', false);\n"
            + "          request.send('');\n"
            + "          alert(request.responseXML.getElementById('myID').myInput.name);\n"
            + "        } catch (e) { alert('exception'); }\n"
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
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "ActiveXObject not available",
            IE = {"0", "0"})
    public void caseSensitivity_activeX() throws Exception {
        final String html = "<html><head><script>\n"
            + "function test() {\n"
            + "  try {\n"
            + "    var req = new ActiveXObject('MSXML2.XmlHttp');\n"
            + "    alert(req.readyState);\n"
            + "    alert(req.reAdYsTaTe);\n"
            + "  } catch (e) { alert('ActiveXObject not available'); }\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'></body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"0", "undefined"})
    public void caseSensitivity_XMLHttpRequest() throws Exception {
        final String html = "<html><head><script>\n"
            + "function test() {\n"
            + "  try {\n"
            + "    var req = new XMLHttpRequest();\n"
            + "    alert(req.readyState);\n"
            + "    alert(req.reAdYsTaTe);\n"
            + "  } catch (e) { alert('exception'); }\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'></body></html>";
        loadPageWithAlerts2(html);
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
    @Alerts(DEFAULT = {"39", "27035", "65533", "39"},
            IE = {"39", "27035", "63"})
    @HtmlUnitNYI(IE = {"39", "27035", "65533", "39"})
    public void overrideMimeType_charset_all() throws Exception {
        // TODO [IE]SINGLE-VS-BULK test runs when executed as single but breaks as bulk
        shutDownRealIE();

        final String html = "<html><head>\n"
            + "<script>\n"
            + "function test() {\n"
            + "try {\n"
            + "  var request = new XMLHttpRequest();\n"
            + "  request.open('GET', '" + URL_SECOND + "', false);\n"
            + "  request.overrideMimeType('text/plain; charset=GBK');\n"
            + "  request.send('');\n"
            + "  for (var i = 0; i < request.responseText.length; i++) {\n"
            + "    alert(request.responseText.charCodeAt(i));\n"
            + "  }\n"
            + "} catch (e) { alert('exception'); }\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'></body></html>";

        getMockWebConnection().setResponse(URL_SECOND, "'\u9EC4'", MimeType.TEXT_PLAIN, UTF_8);
        loadPageWithAlerts2(html);
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
        final String html =
              "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + "      function someLoad(e) {\n"
            + "        alert(e);\n"
            + "      }\n"
            + "      function test() {\n"
            + "        try {\n"
            + "          var request = new XMLHttpRequest();\n"
            + "          request.onload = someLoad;\n"
            + "          request.open('GET', '" + URL_SECOND + "', false);\n"
            + "          request.send('');\n"
            + "        } catch (e) { alert('exception'); }\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='test()'>\n"
            + "  </body>\n"
            + "</html>";

        final String xml = "<abc></abc>";

        getMockWebConnection().setResponse(URL_SECOND, xml, MimeType.TEXT_XML);
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"someLoad [object ProgressEvent]", "load", "false", "11", "0"},
            IE = {"someLoad [object ProgressEvent]", "load", "true", "11", "11"})
    public void addEventListener() throws Exception {
        final String html =
              "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + "      function someLoad(event) {\n"
            + "        alert('someLoad ' + event);\n"
            + "        alert(event.type);\n"
            + "        alert(event.lengthComputable);\n"
            + "        alert(event.loaded);\n"
            + "        alert(event.total);\n"
            + "      }\n"
            + "      function test() {\n"
            + "        try {\n"
            + "          var request = new XMLHttpRequest();\n"
            + "          request.addEventListener('load', someLoad, false);\n"
            + "          request.open('GET', '" + URL_SECOND + "', false);\n"
            + "          request.send('');\n"
            + "        } catch (e) { alert('exception'); }\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='test()'>\n"
            + "  </body>\n"
            + "</html>";

        final String xml = "<abc></abc>";

        getMockWebConnection().setResponse(URL_SECOND, xml, MimeType.TEXT_XML);
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"someLoad [object ProgressEvent]", "load", "false", "11", "0"},
            IE = {"someLoad [object ProgressEvent]", "load", "true", "11", "11"})
    public void addEventListenerDetails() throws Exception {
        final String html =
              "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + "      function someLoad(event) {\n"
            + "        alert('someLoad ' + event);\n"
            + "        alert(event.type);\n"
            + "        alert(event.lengthComputable);\n"
            + "        alert(event.loaded);\n"
            + "        alert(event.total);\n"
            + "      }\n"
            + "      function test() {\n"
            + "        try {\n"
            + "          var request = new XMLHttpRequest();\n"
            + "          request.addEventListener('load', someLoad, false);\n"
            + "          request.open('GET', '" + URL_SECOND + "', false);\n"
            + "          request.send('');\n"
            + "        } catch (e) { alert('exception'); }\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='test()'>\n"
            + "  </body>\n"
            + "</html>";

        final String xml = "<abc></abc>";

        getMockWebConnection().setResponse(URL_SECOND, xml, MimeType.TEXT_XML);
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function",
            IE = "null")
    @HtmlUnitNYI(CHROME = "undefined",
            EDGE = "undefined",
            FF = "undefined",
            FF78 = "undefined",
            IE = "undefined")
    public void addEventListenerCaller() throws Exception {
        final String html =
              "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + "      function someLoad(event) {\n"
            + "        var caller = arguments.callee.caller;\n"
            + "        alert(typeof caller == 'function' ? 'function' : caller);\n"
            + "      }\n"
            + "      function test() {\n"
            + "        try {\n"
            + "          var request = new XMLHttpRequest();\n"
            + "          request.addEventListener('load', someLoad, false);\n"
            + "          request.open('GET', '" + URL_SECOND + "', false);\n"
            + "          request.send('');\n"
            + "        } catch (e) { alert('exception'); }\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='test()'>\n"
            + "  </body>\n"
            + "</html>";

        final String xml = "<abc></abc>";

        getMockWebConnection().setResponse(URL_SECOND, xml, MimeType.TEXT_XML);
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "[object XMLHttpRequestUpload]",
            IE = "[object XMLHttpRequestEventTarget]")
    public void upload() throws Exception {
        final String html =
              "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + "      function test() {\n"
            + "        try {\n"
            + "          var request = new XMLHttpRequest();\n"
            + "          alert(request.upload);\n"
            + "        } catch (e) { alert('exception'); }\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='test()'>\n"
            + "  </body>\n"
            + "</html>";

        loadPageWithAlerts2(html);
    }

    /**
     * Tests asynchronous use of XMLHttpRequest, using Mozilla style object creation.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"0", "1", "2", "3", "4"},
            IE = {"0", "1", "1", "2", "3", "4"})
    public void asyncUse() throws Exception {
        final String html =
              "<html>\n"
            + "  <head>\n"
            + "    <title>XMLHttpRequest Test</title>\n"
            + "    <script>\n"
            + "      var request;\n"
            + "      function testAsync() {\n"
            + "        request = new XMLHttpRequest();\n"
            + "        request.onreadystatechange = onReadyStateChange;\n"
            + "        alert(request.readyState);\n"
            + "        request.open('GET', '" + URL_SECOND + "', true);\n"
            + "        request.send('');\n"
            + "      }\n"
            + "      function onReadyStateChange() {\n"
            + "        alert(request.readyState);\n"
            + "        if (request.readyState == 4)\n"
            + "          alert(request.responseText);\n"
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
        setExpectedAlerts(ArrayUtils.add(getExpectedAlerts(), xml));
        loadPageWithAlerts2(html);
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
                        "function onreadystatechange() {\n    [native code]\n}",
                        "true", "true"},
            FF78 = {"[object Object]", "undefined", "undefined",
                        "function() { return !0 }",
                        "function onreadystatechange() {\n    [native code]\n}",
                        "true", "true"},
            IE = {"[object Object]", "undefined", "undefined",
                        "function() { return !0 }",
                        "\nfunction onreadystatechange() {\n    [native code]\n}\n",
                        "true", "true"})
    @HtmlUnitNYI(CHROME = {"[object Object]", "undefined", "undefined",
                        "function () {\n    return !0;\n}",
                        "function onreadystatechange() { [native code] }",
                        "true", "true"},
            EDGE = {"[object Object]", "undefined", "undefined",
                        "function () {\n    return !0;\n}",
                        "function onreadystatechange() { [native code] }",
                        "true", "true"},
            FF = {"[object Object]", "undefined", "undefined",
                        "function () {\n    return !0;\n}",
                        "function onreadystatechange() {\n    [native code]\n}",
                        "true", "true"},
            FF78 = {"[object Object]", "undefined", "undefined",
                        "function () {\n    return !0;\n}",
                        "function onreadystatechange() {\n    [native code]\n}",
                        "true", "true"},
            IE = {"[object Object]", "undefined", "undefined",
                        "function () {\n    return !0;\n}",
                        "\nfunction onreadystatechange() {\n    [native code]\n}\n",
                        "true", "true"})
    public void defineProperty() throws Exception {
        final String html =
              "<html>\n"
            + "  <head>\n"
            + "    <title>XMLHttpRequest Test</title>\n"
            + "    <script>\n"
            + "      var request;\n"
            + "      function test() {\n"
            + "        Object.defineProperty(XMLHttpRequest.prototype, 'onreadystatechange', {\n"
            + "                                 enumerable: !0,\n"
            + "                                 configurable: !0,\n"
            + "                                 get: function() { return !0 }\n"
            + "                             });\n"
            + "        var desc = Object.getOwnPropertyDescriptor(XMLHttpRequest.prototype, 'onreadystatechange');\n"
            + "        alert(desc);\n"
            + "        alert(desc.value);\n"
            + "        alert(desc.writable);\n"
            + "        alert(desc.get);\n"
            + "        alert(desc.set);\n"
            + "        alert(desc.configurable);\n"
            + "        alert(desc.enumerable);\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='test()'>\n"
            + "  </body>\n"
            + "</html>";

        loadPageWithAlerts2(html);
    }

    /**
     * Test case for https://stackoverflow.com/questions/44349339/htmlunit-ecmaerror-typeerror.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "[object XMLHttpRequest]",
            IE = "[object XMLHttpRequestPrototype]")
    @HtmlUnitNYI(IE = "[object XMLHttpRequest]")
    public void defineProperty2() throws Exception {
        final String html =
              "<html>\n"
            + "  <head>\n"
            + "    <title>XMLHttpRequest Test</title>\n"
            + "    <script>\n"
            + "      var request;\n"
            + "      function test() {\n"
            + "        var t = Object.getOwnPropertyDescriptor(XMLHttpRequest.prototype, 'onreadystatechange');\n"
            + "        var res = Object.defineProperty(XMLHttpRequest.prototype, 'onreadystatechange', t);\n"
            + "        alert(res);\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='test()'>\n"
            + "  </body>\n"
            + "</html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "application/json",
            IE = "null")
    @HtmlUnitNYI(IE = "application/x-www-form-urlencoded")
    public void enctypeBlob() throws Exception {
        final String html
            = "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + "function doTest() {\n"
            + "  try {\n"
            + "    var debug = {hello: 'world'};\n"
            + "    var blob = new Blob([JSON.stringify(debug, null, 2)], {type : 'application/json'});\n"

            + "    var xhr = new XMLHttpRequest();\n"
            + "    xhr.open('post', '/test2', false);\n"
            + "    xhr.send(blob);\n"
            + "    alert('done');\n"
            + "  } catch (e) {\n"
            + "    alert('error: ' + e.message);\n"
            + "  }\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='doTest()'>\n"
            + "</body>\n"
            + "</html>";

        getMockWebConnection().setDefaultResponse("<html><title>Response</title></html>");

        final WebDriver driver = loadPage2(html);
        verifyAlerts(DEFAULT_WAIT_TIME, driver, new String[] {"done"});

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
            FF78 = "text/plain",
            IE = "text/plain")
    public void enctypeBufferSource() throws Exception {
        final String html
            = "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + "function doTest() {\n"
            + "  try {\n"
            + "    var typedArray = new Int8Array(8);\n"
            + "    var xhr = new XMLHttpRequest();\n"
            + "    xhr.open('post', '/test2', false);\n"
            + "    xhr.send(typedArray);\n"
            + "    alert('done');\n"
            + "  } catch (e) {\n"
            + "    alert('error: ' + e.message);\n"
            + "  }\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='doTest()'>\n"
            + "</body>\n"
            + "</html>";

        getMockWebConnection().setDefaultResponse("<html><title>Response</title></html>");

        final WebDriver driver = loadPage2(html);
        verifyAlerts(DEFAULT_WAIT_TIME, driver, new String[] {"done"});

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
    @Alerts(DEFAULT = {"q=HtmlUnit&u=%D0%BB%C6%89", "done", "application/x-www-form-urlencoded;charset=UTF-8",
                        "q=HtmlUnit", "u=\u043B\u0189"},
            IE = {"error: URLSearchParams", "done", "text/plain;charset=UTF-8"})
    public void enctypeURLSearchParams() throws Exception {
        final String html
            = "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + "function doTest() {\n"
            + "  var searchParams = '1234';\n"
            + "  try {\n"
            + "    searchParams = new URLSearchParams();\n"
            + "    searchParams.append('q', 'HtmlUnit');\n"
            + "    searchParams.append('u', '\u043B\u0189');\n"
            + "    alert(searchParams);\n"
            + "  } catch (e) {\n"
            + "    alert('error: URLSearchParams');\n"
            + "  }\n"
            + "  try {\n"
            + "    var xhr = new XMLHttpRequest();\n"
            + "    xhr.open('post', '/test2', false);\n"
            + "    xhr.send(searchParams);\n"
            + "    alert('done');\n"
            + "  } catch (e) {\n"
            + "    alert('error: ' + e.message);\n"
            + "  }\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='doTest()'>\n"
            + "</body>\n"
            + "</html>";

        getMockWebConnection().setDefaultResponse("<html><title>Response</title></html>");

        final WebDriver driver = loadPage2(html, URL_FIRST, "text/html;charset=UTF-8", UTF_8, null);
        verifyAlerts(DEFAULT_WAIT_TIME, driver, new String[] {getExpectedAlerts()[0], getExpectedAlerts()[1]});

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
        final String html
            = "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + "function doTest() {\n"
            + "  try {\n"
            + "    var formData = new FormData(document.testForm);\n"
            + "    var xhr = new XMLHttpRequest();\n"
            + "    xhr.open('post', '/test2', false);\n"
            + "    xhr.send(formData);\n"
            + "    alert('done');\n"
            + "  } catch (e) {\n"
            + "    alert('error: ' + e.message);\n"
            + "  }\n"
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
        verifyAlerts(DEFAULT_WAIT_TIME, driver, new String[] {"done"});

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
    @Alerts({"text/plain;charset=UTF-8", "HtmlUnit \u00D0\u00BB\u00C6\u0089"})
    public void enctypeString() throws Exception {
        final String html
            = "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + "function doTest() {\n"
            + "  try {\n"
            + "    var xhr = new XMLHttpRequest();\n"
            + "    xhr.open('post', '/test2', false);\n"
            + "    xhr.send('HtmlUnit \u043B\u0189');\n"
            + "    alert('done');\n"
            + "  } catch (e) {\n"
            + "    alert('error: ' + e.message);\n"
            + "  }\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='doTest()'>\n"
            + "</body>\n"
            + "</html>";

        getMockWebConnection().setDefaultResponse("<html><title>Response</title></html>");

        // use utf8 here to be able to send all chars
        final WebDriver driver = loadPage2(html, URL_FIRST, "text/html;charset=UTF-8", UTF_8, null);
        verifyAlerts(DEFAULT_WAIT_TIME, driver, new String[] {"done"});

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
        final String html
            = "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + "function doTest() {\n"
            + "  try {\n"
            + "    var xhr = new XMLHttpRequest();\n"
            + "    xhr.open('post', '/test2', false);\n"
            + "    xhr.setRequestHeader('Content-Type', 'text/jpeg');\n"
            + "    xhr.send('HtmlUnit \u043B\u0189');\n"
            + "    alert('done');\n"
            + "  } catch (e) {\n"
            + "    alert('error: ' + e.message);\n"
            + "  }\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='doTest()'>\n"
            + "</body>\n"
            + "</html>";

        getMockWebConnection().setDefaultResponse("<html><title>Response</title></html>");

        final WebDriver driver = loadPage2(html, URL_FIRST, "text/html;charset=UTF-8", UTF_8, null);
        verifyAlerts(DEFAULT_WAIT_TIME, driver, new String[] {"done"});

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
    @Alerts("error")
    public void setRequestHeaderNotOpend() throws Exception {
        final String html
            = "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + "function doTest() {\n"
            + "  try {\n"
            + "    var xhr = new XMLHttpRequest();\n"
            + "    xhr.setRequestHeader('Content-Type', 'text/jpeg');\n"
            + "    alert('done');\n"
            + "  } catch (e) {\n"
            + "    alert('error');\n"
            + "  }\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='doTest()'>\n"
            + "</body>\n"
            + "</html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails.
     */
    @Test
    @Alerts(DEFAULT = "undefined",
            IE = {"[object Object]", "undefined", "undefined",
                        "\nfunction onabort() {\n    [native code]\n}\n",
                        "\nfunction onabort() {\n    [native code]\n}\n",
                        "true", "true"})
    public void getOwnPropertyDescriptor_onabort() throws Exception {
        getOwnPropertyDescriptor("onabort");
    }

    /**
     * @throws Exception if the test fails.
     */
    @Test
    @Alerts(DEFAULT = "undefined",
            IE = {"[object Object]", "undefined", "undefined",
                        "\nfunction onerror() {\n    [native code]\n}\n",
                        "\nfunction onerror() {\n    [native code]\n}\n",
                        "true", "true"})
    public void getOwnPropertyDescriptor_onerror() throws Exception {
        getOwnPropertyDescriptor("onerror");
    }

    /**
     * @throws Exception if the test fails.
     */
    @Test
    @Alerts(DEFAULT = "undefined",
            IE = {"[object Object]", "undefined", "undefined",
                        "\nfunction onload() {\n    [native code]\n}\n",
                        "\nfunction onload() {\n    [native code]\n}\n",
                        "true", "true"})
    public void getOwnPropertyDescriptor_onload() throws Exception {
        getOwnPropertyDescriptor("onload");
    }

    /**
     * @throws Exception if the test fails.
     */
    @Test
    @Alerts(DEFAULT = "undefined",
            IE = {"[object Object]", "undefined", "undefined",
                        "\nfunction onloadstart() {\n    [native code]\n}\n",
                        "\nfunction onloadstart() {\n    [native code]\n}\n",
                        "true", "true"})
    public void getOwnPropertyDescriptor_onloadstart() throws Exception {
        getOwnPropertyDescriptor("onloadstart");
    }

    /**
     * @throws Exception if the test fails.
     */
    @Test
    @Alerts(DEFAULT = "undefined",
            IE = {"[object Object]", "undefined", "undefined",
                        "\nfunction onloadend() {\n    [native code]\n}\n",
                        "\nfunction onloadend() {\n    [native code]\n}\n",
                        "true", "true"})
    public void getOwnPropertyDescriptor_onloadend() throws Exception {
        getOwnPropertyDescriptor("onloadend");
    }

    /**
     * @throws Exception if the test fails.
     */
    @Test
    @Alerts(DEFAULT = "undefined",
            IE = {"[object Object]", "undefined", "undefined",
                        "\nfunction onprogress() {\n    [native code]\n}\n",
                        "\nfunction onprogress() {\n    [native code]\n}\n",
                        "true", "true"})
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
                        "function onreadystatechange() {\n    [native code]\n}",
                        "function onreadystatechange() {\n    [native code]\n}",
                        "true", "true"},
            FF78 = {"[object Object]", "undefined", "undefined",
                        "function onreadystatechange() {\n    [native code]\n}",
                        "function onreadystatechange() {\n    [native code]\n}",
                        "true", "true"},
            IE = {"[object Object]", "undefined", "undefined",
                        "\nfunction onreadystatechange() {\n    [native code]\n}\n",
                        "\nfunction onreadystatechange() {\n    [native code]\n}\n",
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
    @Alerts(DEFAULT = "undefined",
            IE = {"[object Object]", "undefined", "undefined",
                    "\nfunction ontimeout() {\n    [native code]\n}\n",
                    "\nfunction ontimeout() {\n    [native code]\n}\n",
                    "true", "true"})
    public void getOwnPropertyDescriptor_ontimeout() throws Exception {
        getOwnPropertyDescriptor("ontimeout");
    }

    private void getOwnPropertyDescriptor(final String event) throws Exception {
        final String html =
              "<html>\n"
            + "  <head>\n"
            + "    <title>XMLHttpRequest Test</title>\n"
            + "    <script>\n"
            + "      var request;\n"
            + "      function test() {\n"
            + "        var desc = Object.getOwnPropertyDescriptor("
                                + "XMLHttpRequest.prototype, '" + event + "');\n"
            + "        alert(desc);\n"
            + "        if(!desc) { return; }\n"

            + "        alert(desc.value);\n"
            + "        alert(desc.writable);\n"
            + "        alert(desc.get);\n"
            + "        alert(desc.set);\n"
            + "        alert(desc.configurable);\n"
            + "        alert(desc.enumerable);\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='test()'>\n"
            + "  </body>\n"
            + "</html>";

        loadPageWithAlerts2(html);
    }
}
