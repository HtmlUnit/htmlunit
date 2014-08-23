/*
 * Copyright (c) 2002-2014 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript.host.xml;

import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.CHROME;
import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.FF;
import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.IE11;
import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.IE8;

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

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.Browsers;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;
import com.gargoylesoftware.htmlunit.BrowserRunner.Tries;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPageTest;
import com.gargoylesoftware.htmlunit.util.NameValuePair;

/**
 * Tests for {@link XMLHttpRequest}.
 *
 * @version $Revision$
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

    private static final String XHR_INSTANTIATION = "(window.XMLHttpRequest ? "
        + "new XMLHttpRequest() : new ActiveXObject('Microsoft.XMLHTTP'))";

    private static final String UNINITIALIZED = String.valueOf(XMLHttpRequest.STATE_UNSENT);
    private static final String LOADING = String.valueOf(XMLHttpRequest.STATE_OPENED);
    private static final String COMPLETED = String.valueOf(XMLHttpRequest.STATE_DONE);

    /**
     * Tests synchronous use of XMLHttpRequest.
     * @throws Exception if the test fails
     */
    @Test
    @Tries(3)
    // TODO [IE11]SINGLE-VS-BULK test runs when executed as single but breaks as bulk
    public void syncUse() throws Exception {
        final String html =
              "<html>\n"
            + "  <head>\n"
            + "    <title>XMLHttpRequest Test</title>\n"
            + "    <script>\n"
            + "      function testSync() {\n"
            + "        var request = " + XHR_INSTANTIATION + ";\n"
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
        getMockWebConnection().setDefaultResponse(xml, "text/xml");

        loadPageWithAlerts2(html);
    }

    /**
     * Tests Mozilla and IE style object creation.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "[object XMLHttpRequest]",
            IE8 = "[object]")
    public void creation() throws Exception {
        final String html =
            "<html>\n"
            + "  <head>\n"
            + "    <title>XMLHttpRequest Test</title>\n"
            + "    <script>\n"
            + "        if (window.XMLHttpRequest)\n"
            + "          alert(new XMLHttpRequest());\n"
            + "        else if (window.ActiveXObject)\n"
            + "        {\n"
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
    @Alerts(DEFAULT = { "1: 0-", "2: ", "3: 200-OK" },
            IE8 = {"1: ex: status-ex: statusText, 2: , 3: 200-OK" })
    public void statusSync() throws Exception {
        final String html =
            "<html>\n"
            + "  <head>\n"
            + "    <title>XMLHttpRequest Test</title>\n"
            + "<script>\n"
            + "  var xhr = " + XHR_INSTANTIATION + ";\n"

            + "  alertStatus('1: ');\n"
            + "  xhr.open('GET', '/foo.xml', false);\n"
            + "  alert('2: ');\n"

            + "  xhr.send();\n"
            + "  alertStatus('3: ');\n"

            + "  function alertStatus(prefix) {\n"
            + "    var msg = prefix;"
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

        getMockWebConnection().setDefaultResponse("<res></res>", "text/xml");
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "1: 0-", "2: 0-", "#1: 0-", "3: 0-", "#1: 0-", "4: 0-",
                            "#2: 200-OK", "#3: 200-OK", "#4: 200-OK" },
            FF = { "1: 0-", "2: 0-", "#1: 0-", "3: 0-", "4: 0-", "#2: 200-OK", "#3: 200-OK", "#4: 200-OK" },
            IE8 = { "1: ex: status-ex: statusText", "2: ex: status-ex: statusText", "#1: ex: status-ex: statusText",
                    "3: ex: status-ex: statusText", "#1: ex: status-ex: statusText", "4: ex: status-ex: statusText",
                    "#2: 200-OK", "#3: 200-OK", "#4: 200-OK" },
            CHROME = { "1: 0-", "2: 0-", "#1: 0-", "3: 0-", "4: 0-",
                    "#2: 200-OK", "#3: 200-OK", "#4: 200-OK" })
    public void statusAsync() throws Exception {
        final String html =
            "<html>\n"
            + "  <head>\n"
            + "    <title>XMLHttpRequest Test</title>\n"
            + "<script>\n"
            + "  var xhr = " + XHR_INSTANTIATION + ";\n"

            + "  function test() {\n"
            + "    try {\n"
            + "      alertStatus('1: ');\n"

            + "      xhr.onreadystatechange = onReadyStateChange;\n"
            + "      alertStatus('2: ');\n"

            + "      xhr.open('GET',  '/foo.xml', true);\n"
            + "      alertStatus('3: ');\n"

            + "      xhr.send();\n"
            + "      alertStatus('4: ');\n"
            + "    } catch(e) { alert(e) }\n"
            + "  }\n"

            + "  function onReadyStateChange() {\n"
            + "    alertStatus('#' + xhr.readyState + ': ');"
            + "  }\n"

            + "  function alertStatus(prefix) {\n"
            + "    var msg = prefix;"
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
            + "  <body onload='test()'></body>\n"
            + "</html>";

        getMockWebConnection().setDefaultResponse("<res></res>", "text/xml");
        loadPageWithAlerts2(html);
    }

    /**
     * Checks that not passing the async flag to <code>open()</code>
     * results in async execution.  If this gets interpreted as <code>false</code>
     * then you will see the alert order 1-2-4-3 instead of 1-2-3-4.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "#1", "#2", "#3", "#4" })
    public void asyncIsDefault() throws Exception {
        final String html = "<html><body><script>\n"
            + "var xhr = " + XHR_INSTANTIATION + ";\n"

            + "function onReadyStateChange() {\n"
            + "  if( xhr.readyState == 4 ) {\n"
            + "    alert('#4');\n"
            + "  }\n"
            + "}\n"

            + "try {\n"
            + "  alert('#1');\n"
            + "  xhr.onreadystatechange = onReadyStateChange;\n"
            + "  xhr.open('GET',  '/foo.xml');\n"
            + "  alert('#2');\n"
            + "  xhr.send();\n"
            + "  alert('#3');\n"
            + "} catch(e) { alert(e); }\n"
            + "</script></body></html>";

        getMockWebConnection().setDefaultResponse("<res></res>", "text/xml");
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "orsc1", "open-done", "orsc1", "send-done",
                "orsc2", "orsc3", "orsc4", "4", "<a>b</a>", "[object XMLHttpRequest]" },
            FF = { "orsc1", "open-done", "send-done",
                "orsc2", "orsc3", "orsc4", "4", "<a>b</a>", "[object XMLHttpRequest]" },
            IE8 = { "orsc1", "open-done", "orsc1", "send-done", "orsc2", "orsc3", "orsc4" },
            CHROME = {"orsc1", "open-done", "send-done",
                "orsc2", "orsc3", "orsc4", "4", "<a>b</a>", "[object XMLHttpRequest]" })
    public void onload() throws Exception {
        final String html =
              "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + "      function test() {\n"
            + "        var xhr = " + XHR_INSTANTIATION + ";\n"

            + "        xhr.onreadystatechange = function() { alert('orsc' + xhr.readyState); };\n"
            + "        xhr.onload = function() { alert(xhr.readyState); alert(xhr.responseText); alert(this); }\n"

            + "        xhr.open('GET', '/foo.xml', true);\n"
            + "        alert('open-done');\n"

            + "        xhr.send('');\n"
            + "        alert('send-done');\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='test()'></body>\n"
            + "</html>";

        final String xml = "<a>b</a>";

        getMockWebConnection().setDefaultResponse(xml, "text/xml");
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"null", "null" })
    public void responseHeaderBeforeSend() throws Exception {
        final String html =
            "<html>\n"
            + "  <head>\n"
            + "    <title>XMLHttpRequest Test</title>\n"
            + "    <script>\n"
            + "        var request = " + XHR_INSTANTIATION + ";\n"

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
        final String html =
              "<html>\n"
            + "  <head>\n"
            + "    <title>XMLHttpRequest Test</title>\n"
            + "    <script>\n"
            + "      function testSync() {\n"
            + "        var request = " + XHR_INSTANTIATION + ";\n"
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
        getMockWebConnection().setDefaultResponse(xml, "text/xml");
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("bla bla")
    // TODO [IE11]SINGLE-VS-BULK test runs when executed as single but breaks as bulk
    public void responseText_NotXml() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + "function test() {\n"
            + "  var request = " + XHR_INSTANTIATION + ";\n"
            + "  request.open('GET', 'foo.txt', false);\n"
            + "  request.send('');\n"
            + "  alert(request.responseText);\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'></body></html>";

        getMockWebConnection().setDefaultResponse("bla bla", "text/plain");
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("null")
    // TODO [IE11]SINGLE-VS-BULK test runs when executed as single but breaks as bulk
    public void responseXML_text_html() throws Exception {
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

        getMockWebConnection().setDefaultResponse("<html></html>", "text/html");
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "[object XMLDocument]",
            IE8 = "[object Object]")
    // TODO [IE11]SINGLE-VS-BULK test runs when executed as single but breaks as bulk
    public void responseXML_text_xml() throws Exception {
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

        getMockWebConnection().setDefaultResponse("<note/>", "text/xml");
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "[object XMLDocument]",
            IE8 = "[object Object]")
    // TODO [IE11]SINGLE-VS-BULK test runs when executed as single but breaks as bulk
    public void responseXML_application_xml() throws Exception {
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
    @Alerts(DEFAULT = "[object XMLDocument]",
            IE8 = "[object Object]")
    // TODO [IE11]SINGLE-VS-BULK test runs when executed as single but breaks as bulk
    public void responseXML_application_xhtmlXml() throws Exception {
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
    @Alerts(DEFAULT = "[object XMLDocument]",
            IE8 = "[object Object]")
    // TODO [IE11]SINGLE-VS-BULK test runs when executed as single but breaks as bulk
    public void responseXML_application_svgXml() throws Exception {
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

        getMockWebConnection().setDefaultResponse("<svg  xmlns=\"http://www.w3.org/2000/svg\"/>", "image/svg+xml");
        loadPageWithAlerts2(html);
    }

    /**
     * Regression test for IE specific properties attribute.text & attribute.xml.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "1", "someAttr", "someValue", "someAttr=\"someValue\"" },
            IE11 = { "1", "someAttr", "undefined", "undefined" })
    // TODO [IE11]SINGLE-VS-BULK test runs when executed as single but breaks as bulk
    @NotYetImplemented(FF)
    public void responseXML2() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + "function test() {\n"
            + "  var request = " + XHR_INSTANTIATION + ";\n"
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

        final URL urlPage2 = new URL(URL_FIRST + "foo.xml");
        getMockWebConnection().setResponse(urlPage2,
            "<bla someAttr='someValue'><foo><fi id='fi1'/><fi/></foo></bla>\n",
            "text/xml");
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
            + "  var request = " + XHR_INSTANTIATION + ";\n"
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
            + "  var request = " + XHR_INSTANTIATION + ";\n"
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
            + "  var request = " + XHR_INSTANTIATION + ";\n"
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
            + "  var request = " + XHR_INSTANTIATION + ";\n"
            + "  request.open('GET', 'foo.txt', false);\n"
            + "  request.send();\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'></body></html>";

        getMockWebConnection().setDefaultResponse("");
        final WebDriver webdriver = loadPageWithAlerts2(html);
        assertEquals(getDefaultUrl().toString(), webdriver.getCurrentUrl());
        assertEquals("foo", webdriver.getTitle());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "true", "false" },
            IE = { "true", "exception" })
    public void overrideMimeType() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + "function test() {\n"
            + "try {\n"
            + "  var request = new XMLHttpRequest();\n"
            + "  request.open('GET', 'foo.xml.txt', false);\n"
            + "  request.send('');\n"
            + "  alert(request.responseXML == null);\n"
            + "  request.overrideMimeType('text/xml');\n"
            + "  request.open('GET', 'foo.xml.txt', false);\n"
            + "  request.send('');\n"
            + "  alert(request.responseXML == null);\n"
            + "} catch (e) { alert('exception'); }\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'></body></html>";

        final URL urlPage2 = new URL(getDefaultUrl() + "foo.xml.txt");
        getMockWebConnection().setResponse(urlPage2,
            "<bla someAttr='someValue'><foo><fi id='fi1'/><fi/></foo></bla>\n",
            "text/plain");
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "27035", IE8 = "exception", IE11 = "111")
    @NotYetImplemented(IE11)
    public void overrideMimeType_charset() throws Exception {
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

        getMockWebConnection().setResponse(URL_SECOND, "\u9ec4", "text/plain", "UTF-8");
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "27035", IE8 = "exception", IE11 = "111")
    @NotYetImplemented(IE11)
    public void overrideMimeType_charset_upper_case() throws Exception {
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

        getMockWebConnection().setResponse(URL_SECOND, "\u9ec4", "text/plain", "UTF-8");
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "40644", IE8 = "exception", IE11 = "111")
    @NotYetImplemented(IE11)
    public void overrideMimeType_charset_empty() throws Exception {
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

        getMockWebConnection().setResponse(URL_SECOND, "\u9ec4", "text/plain", "UTF-8");
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "40644", CHROME = "233", IE8 = "exception", IE11 = "NaN")
    @NotYetImplemented({ IE11, CHROME })
    public void overrideMimeType_charset_wrong() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + "function test() {\n"
            + "try {\n"
            + "  var request = new XMLHttpRequest();\n"
            + "  request.open('GET', '" + URL_SECOND + "', false);\n"
            + "  request.overrideMimeType('text/plain; charset=abcdefg');\n"
            + "  request.send('');\n"
            + "  alert(request.responseText.charCodeAt(0));\n"
            + "} catch (e) { alert('exception'); }\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'></body></html>";

        getMockWebConnection().setResponse(URL_SECOND, "\u9ec4", "text/plain", "UTF-8");
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
    @Alerts({ "ibcdefg", "xxxxxfg" })
    // TODO [IE11]SINGLE-VS-BULK test runs when executed as single but breaks as bulk
    public void replaceOnTextData() throws Exception {
        final String html =
              "<html>\n"
            + "  <head>\n"
            + "    <title>XMLHttpRequest Test</title>\n"
            + "    <script>\n"
            + "      var request;\n"
            + "      function testReplace() {\n"
            + "        request = " + XHR_INSTANTIATION + ";\n"
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
            + "          alert(theResult)\n"
            + "          theResult = theData.replace('abcde', 'xxxxx');\n"
            + "          alert(theResult)\n"
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

        getMockWebConnection().setDefaultResponse(xml, "text/xml");
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
            + " req = " + XHR_INSTANTIATION + ";\n"
            + " req.open('post', 'foo.xml', false);\n"
            + " req.send('');\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'></body></html>";

        final URL urlPage2 = new URL(URL_FIRST + "foo.xml");
        getMockWebConnection().setResponse(urlPage2, "<foo/>\n", "text/xml");
        loadPage2(html);

        final WebRequest request = getMockWebConnection().getLastWebRequest();
        assertEquals(urlPage2, request.getUrl());
        assertEquals(URL_FIRST.toExternalForm(), request.getAdditionalHeaders().get("Referer"));
    }

    /**
     * Test for bug https://sourceforge.net/tracker/?func=detail&atid=448266&aid=1784330&group_id=47038.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "ActiveXObject not available",
            IE = "0")
    public void caseSensitivity() throws Exception {
        final String html = "<html><head><script>\n"
            + "function test() {\n"
            + "  if (!window.ActiveXObject) { alert('ActiveXObject not available'); return }\n"
            + "  var req = new ActiveXObject('MSXML2.XmlHttp');\n"
            + "  alert(req.readyState);\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'></body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(IE8)
    @Alerts("2")
    public void responseXML_selectNodesIE() throws Exception {
        final String html =
              "<html>\n"
            + "  <head>\n"
            + "    <title>XMLHttpRequest Test</title>\n"
            + "    <script>\n"
            + "      function test() {\n"
            + "        var request = " + XHR_INSTANTIATION + ";\n"
            + "        request.open('GET', '" + URL_SECOND + "', false);\n"
            + "        request.send('');\n"
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

        getMockWebConnection().setResponse(URL_SECOND, xml, "text/xml");
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "null", "myID", "blah", "span", "[object XMLDocument]" },
            CHROME = { "[object Element]", "myID", "blah", "span", "[object XMLDocument]" },
            IE8 = "responseXML.getElementById not available")
    public void responseXML_getElementById_FF() throws Exception {
        final String html =
              "<html>\n"
            + "  <head>\n"
            + "    <title>XMLHttpRequest Test</title>\n"
            + "    <script>\n"
            + "      function test() {\n"
            + "        var request = " + XHR_INSTANTIATION + ";\n"
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

        getMockWebConnection().setResponse(URL_SECOND, xml, "text/xml");
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "[object Element]", "[object Element]", "[object HTMLBodyElement]",
                "[object HTMLSpanElement]", "[object XMLDocument]", "undefined" },
            IE8 = { "[object]", "[object]", "[object]",
                "<body xmlns=\"http://www.w3.org/1999/xhtml\"><span id=\"out\">Hello Bob Dole!</span></body>" })
    // TODO [IE11]SINGLE-VS-BULK test runs when executed as single but breaks as bulk
    public void responseXML_getElementById() throws Exception {
        final String html =
              "<html>\n"
            + "  <head>\n"
            + "    <title>XMLHttpRequest Test</title>\n"
            + "    <script>\n"
            + "      function test() {\n"
            + "        var request = " + XHR_INSTANTIATION + ";\n"
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

        getMockWebConnection().setResponse(URL_SECOND, xml, "text/xml");
        loadPageWithAlerts2(html);
    }

    /**
     * Verifies that the default encoding for an XMLHttpRequest is UTF-8.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("ol\u00E9")
    public void defaultEncodingIsUTF8() throws Exception {
        final String html =
              "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + "      function test() {\n"
            + "        var request = " + XHR_INSTANTIATION + ";\n"
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
        final byte[] responseBytes = response.getBytes("UTF-8");

        getMockWebConnection().setResponse(URL_SECOND, responseBytes, 200, "OK", "text/html",
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
            resp.addHeader("Content-Type", "text/html");
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

        private void handleRequest(final HttpServletRequest req, final HttpServletResponse resp) throws IOException {
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
                resp.addHeader("Content-Type", "text/plain");
                resp.getOutputStream().print(authToken);
                resp.flushBuffer();
            }
        }
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "myID",
            IE8 = "exception")
    public void responseXML_html_select() throws Exception {
        final String html =
              "<html>\n"
            + "  <head>\n"
            + "    <title>XMLHttpRequest Test</title>\n"
            + "    <script>\n"
            + "      function test() {\n"
            + "        try {\n"
            + "        var request = " + XHR_INSTANTIATION + ";\n"
            + "        request.open('GET', '" + URL_SECOND + "', false);\n"
            + "        request.send('');\n"
            + "        alert(request.responseXML.getElementById('myID').id);\n"
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

        getMockWebConnection().setResponse(URL_SECOND, xml, "text/xml");
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "myInput",
            IE8 = "exception")
    public void responseXML_html_form() throws Exception {
        final String html =
              "<html>\n"
            + "  <head>\n"
            + "    <title>XMLHttpRequest Test</title>\n"
            + "    <script>\n"
            + "      function test() {\n"
            + "        try {\n"
            + "        var request = " + XHR_INSTANTIATION + ";\n"
            + "        request.open('GET', '" + URL_SECOND + "', false);\n"
            + "        request.send('');\n"
            + "        alert(request.responseXML.getElementById('myID').myInput.name);\n"
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

        getMockWebConnection().setResponse(URL_SECOND, xml, "text/xml");
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "ActiveXObject not available",
            IE = "0")
    public void caseSensitivity_activeX() throws Exception {
        final String html = "<html><head><script>\n"
            + "function test() {\n"
            + "  if (!window.ActiveXObject) { alert('ActiveXObject not available'); return }\n"
            + "  var req = new ActiveXObject('MSXML2.XmlHttp');\n"
            + "  alert(req.reAdYsTaTe);\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'></body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("undefined")
    public void caseSensitivity_XMLHttpRequest() throws Exception {
        final String html = "<html><head><script>\n"
            + "function test() {\n"
            + "try {\n"
            + "  var req = new XMLHttpRequest();\n"
            + "  alert(req.reAdYsTaTe);\n"
            + "} catch (e) { alert('exception'); }\n"
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
        assertTrue(XMLHttpRequest.isAuthorizedHeader("Content-Type"));

        final String[] headers = {"accept-charset", "accept-encoding",
            "connection", "content-length", "cookie", "cookie2", "content-transfer-encoding", "date",
            "expect", "host", "keep-alive", "referer", "te", "trailer", "transfer-encoding", "upgrade",
            "user-agent", "via" };
        for (final String header : headers) {
            assertFalse(XMLHttpRequest.isAuthorizedHeader(header));
            assertFalse(XMLHttpRequest.isAuthorizedHeader(header.toUpperCase(Locale.ENGLISH)));
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
    @Alerts(DEFAULT = { "39", "27035", "65533", "39" }, IE8 = "exception", IE11 = { "39", "27035", "63" })
    @NotYetImplemented({ CHROME, FF, IE11 })
    public void overrideMimeType_charset_all() throws Exception {
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

        getMockWebConnection().setResponse(URL_SECOND, "'\u9ec4'", "text/plain", "UTF-8");
        loadPageWithAlerts2(html);
    }

    /**
     * Helper method Bug #1623.
     *
     * @throws Exception if the test fails
     */
    @Test
    @NotYetImplemented
    public void java_encoding() throws Exception {
        // Chrome and FF return the last apostrophe, see overrideMimeType_charset_all()
        // but Java and other tools (e.g. Notpad++) return only 3 characters, not 4
        // this method is not a test case, but rather to show the behavior of java

        final String string = "'\u9ec4'";
        final ByteArrayInputStream bais = new ByteArrayInputStream(string.getBytes("UTF-8"));
        final BufferedReader reader = new BufferedReader(new InputStreamReader(bais, "GBK"));
        final String output = reader.readLine();
        reader.close();

        assertEquals(39, output.codePointAt(0));
        assertEquals(27035, output.codePointAt(1));
        assertEquals(65533, output.codePointAt(2));
        assertEquals(39, output.codePointAt(3));
    }
}
