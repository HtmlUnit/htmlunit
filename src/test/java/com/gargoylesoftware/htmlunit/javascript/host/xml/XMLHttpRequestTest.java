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
package com.gargoylesoftware.htmlunit.javascript.host.xml;

import static org.junit.Assert.assertSame;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.Servlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.CollectingAlertHandler;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.MockWebConnection;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.WebServerTestCase;
import com.gargoylesoftware.htmlunit.WebWindow;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.Browser;
import com.gargoylesoftware.htmlunit.BrowserRunner.Browsers;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;
import com.gargoylesoftware.htmlunit.BrowserRunner.Tries;
import com.gargoylesoftware.htmlunit.html.DomChangeEvent;
import com.gargoylesoftware.htmlunit.html.DomChangeListener;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
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
 */
@RunWith(BrowserRunner.class)
public class XMLHttpRequestTest extends WebServerTestCase {

    private static final String MSG_NO_CONTENT = "no Content";
    private static final String MSG_PROCESSING_ERROR = "error processing";

    private static final String UNINITIALIZED = String.valueOf(XMLHttpRequest.STATE_UNINITIALIZED);
    private static final String LOADING = String.valueOf(XMLHttpRequest.STATE_LOADING);
    private static final String LOADED = String.valueOf(XMLHttpRequest.STATE_LOADED);
    private static final String INTERACTIVE = String.valueOf(XMLHttpRequest.STATE_INTERACTIVE);
    private static final String COMPLETED = String.valueOf(XMLHttpRequest.STATE_COMPLETED);

    /**
     * Tests synchronous use of XMLHttpRequest.
     * @throws Exception if the test fails
     */
    @Test
    @Tries(3)
    public void testSyncUse() throws Exception {
        final String html =
              "<html>\n"
            + "  <head>\n"
            + "    <title>XMLHttpRequest Test</title>\n"
            + "    <script>\n"
            + "      function testSync() {\n"
            + "        var request;\n"
            + "        if (window.XMLHttpRequest)\n"
            + "          request = new XMLHttpRequest();\n"
            + "        else if (window.ActiveXObject)\n"
            + "          request = new ActiveXObject('Microsoft.XMLHTTP');\n"
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

        final WebClient client = getWebClient();
        final List<String> collectedAlerts = new ArrayList<String>();
        client.setAlertHandler(new CollectingAlertHandler(collectedAlerts));
        final MockWebConnection conn = new MockWebConnection();
        conn.setResponse(URL_FIRST, html);
        conn.setResponse(URL_SECOND, xml, "text/xml");
        client.setWebConnection(conn);
        client.getPage(URL_FIRST);

        final String[] alerts = {UNINITIALIZED, LOADING, COMPLETED, xml};
        assertEquals(alerts, collectedAlerts);
    }

    /**
     * Tests Mozilla and IE style object creation.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE = "[object]", IE6 = "activeX created", FF = "[object XMLHttpRequest]")
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
        loadPageWithAlerts(html);
    }

    /**
     * Tests asynchronous use of XMLHttpRequest, using Mozilla style object creation.
     * @throws Exception if the test fails
     */
    @Test
    public void testAsyncUse() throws Exception {
        final String html =
              "<html>\n"
            + "  <head>\n"
            + "    <title>XMLHttpRequest Test</title>\n"
            + "    <script>\n"
            + "      var request;\n"
            + "      function testAsync() {\n"
            + "        if (window.XMLHttpRequest)\n"
            + "          request = new XMLHttpRequest();\n"
            + "        else if (window.ActiveXObject)\n"
            + "          request = new ActiveXObject('Microsoft.XMLHTTP');\n"
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

        final WebClient client = getWebClient();
        final List<String> collectedAlerts = Collections.synchronizedList(new ArrayList<String>());
        client.setAlertHandler(new CollectingAlertHandler(collectedAlerts));
        final MockWebConnection conn = new MockWebConnection();
        conn.setResponse(URL_FIRST, html);
        conn.setResponse(URL_SECOND, xml, "text/xml");
        client.setWebConnection(conn);
        client.getPage(URL_FIRST);

        assertEquals(0, client.waitForBackgroundJavaScriptStartingBefore(1000));
        final String[] alerts = {UNINITIALIZED, LOADING, LOADING, LOADED, INTERACTIVE, COMPLETED, xml};
        assertEquals(alerts, collectedAlerts);
    }

    /**
     * Tests asynchronous use of XMLHttpRequest, where the XHR request fails due to IOException (Connection refused).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE = { "0", "1", "1", "2", "4", MSG_NO_CONTENT },
            FF = { "0", "1", "1", "2", "4", MSG_NO_CONTENT, MSG_PROCESSING_ERROR })
    public void testAsyncUseWithNetworkConnectionFailure() throws Exception {
        final String html =
              "<html>\n"
            + "<head>\n"
            + "<title>XMLHttpRequest Test</title>\n"
            + "<script>\n"
            + "var request;\n"
            + "function testAsync() {\n"
            + "  if (window.XMLHttpRequest)\n"
            + "    request = new XMLHttpRequest();\n"
            + "  else if (window.ActiveXObject)\n"
            + "    request = new ActiveXObject('Microsoft.XMLHTTP');\n"
            + "  request.onreadystatechange = onReadyStateChange;\n"
            + "  request.onerror = onError;\n"
            + "  alert(request.readyState);\n"
            + "  request.open('GET', '" + URL_SECOND + "', true);\n"
            + "  request.send('');\n"
            + "}\n"
            + "function onError() {\n"
            + "  alert('" + MSG_PROCESSING_ERROR + "');\n"
            + "}\n"
            + "function onReadyStateChange() {\n"
            + "  alert(request.readyState);\n"
            + "  if (request.readyState == 4) {\n"
            + "    if (request.responseText.length == 0)\n"
            + "      alert('" + MSG_NO_CONTENT + "');"
            + "    else\n"
            + "      throw 'Unexpected content, should be zero length but is: \"' + request.responseText + '\"';\n"
            + "  }\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='testAsync()'>\n"
            + "</body>\n"
            + "</html>";

        final WebClient client = getWebClient();
        final List<String> collectedAlerts = Collections.synchronizedList(new ArrayList<String>());
        client.setAlertHandler(new CollectingAlertHandler(collectedAlerts));
        final MockWebConnection conn = new DisconnectedMockWebConnection();
        conn.setResponse(URL_FIRST, html);
        client.setWebConnection(conn);
        client.getPage(URL_FIRST);

        assertEquals(0, client.waitForBackgroundJavaScriptStartingBefore(1000));
        assertEquals(getExpectedAlerts(), collectedAlerts);
    }

    /**
     * Regression test for http://sourceforge.net/tracker/index.php?func=detail&aid=1209692&group_id=47038&atid=448266.
     * @throws Exception if the test fails
     */
    @Test
    public void testRelativeUrl() throws Exception {
        final String html =
              "<html>\n"
            + "  <head>\n"
            + "    <title>XMLHttpRequest Test</title>\n"
            + "    <script>\n"
            + "      function testSync() {\n"
            + "        var request;\n"
            + "        if (window.XMLHttpRequest)\n"
            + "          request = new XMLHttpRequest();\n"
            + "        else if (window.ActiveXObject)\n"
            + "          request = new ActiveXObject('Microsoft.XMLHTTP');\n"
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

        final WebClient client = getWebClient();
        final List<String> collectedAlerts = new ArrayList<String>();
        client.setAlertHandler(new CollectingAlertHandler(collectedAlerts));
        final MockWebConnection conn = new MockWebConnection();
        conn.setResponse(URL_FIRST, html);
        final URL urlPage2 = new URL(URL_FIRST + "foo.xml");
        conn.setResponse(urlPage2, xml, "text/xml");
        client.setWebConnection(conn);
        client.getPage(URL_FIRST);

        final String[] alerts = {COMPLETED, xml};
        assertEquals(alerts, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testResponseText_NotXml() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + "function test() {\n"
            + "  var request;\n"
            + "  if (window.XMLHttpRequest)\n"
            + "    request = new XMLHttpRequest();\n"
            + "  else if (window.ActiveXObject)\n"
            + "    request = new ActiveXObject('Microsoft.XMLHTTP');\n"
            + "  request.open('GET', 'foo.txt', false);\n"
            + "  request.send('');\n"
            + "  alert(request.responseText);\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'></body></html>";

        final WebClient client = getWebClient();
        final List<String> collectedAlerts = new ArrayList<String>();
        client.setAlertHandler(new CollectingAlertHandler(collectedAlerts));
        final MockWebConnection conn = new MockWebConnection();
        conn.setResponse(URL_FIRST, html);
        final URL urlPage2 = new URL(URL_FIRST + "foo.txt");
        conn.setResponse(urlPage2, "bla bla", "text/plain");
        client.setWebConnection(conn);
        client.getPage(URL_FIRST);

        final String[] alerts = {"bla bla"};
        assertEquals(alerts, collectedAlerts);
    }

    /**
     * Regression test for IE specific properties attribute.text & attribute.xml.
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(Browser.IE)
    public void testResponseXML2() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + "function test() {\n"
            + "  var request;\n"
            + "  if (window.XMLHttpRequest)\n"
            + "    request = new XMLHttpRequest();\n"
            + "  else if (window.ActiveXObject)\n"
            + "    request = new ActiveXObject('Microsoft.XMLHTTP');\n"
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

        final WebClient client = getWebClient();
        final List<String> collectedAlerts = new ArrayList<String>();
        client.setAlertHandler(new CollectingAlertHandler(collectedAlerts));
        final MockWebConnection conn = new MockWebConnection();
        conn.setResponse(URL_FIRST, html);
        final URL urlPage2 = new URL(URL_FIRST + "foo.xml");
        conn.setResponse(urlPage2, "<bla someAttr='someValue'><foo><fi id='fi1'/><fi/></foo></bla>\n",
                "text/xml");
        client.setWebConnection(conn);
        client.getPage(URL_FIRST);

        final String[] alerts = {"1", "someAttr", "someValue", "someAttr=\"someValue\""};
        assertEquals(alerts, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testSendNull() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + "function test() {\n"
            + "  var request;\n"
            + "  if (window.XMLHttpRequest)\n"
            + "    request = new XMLHttpRequest();\n"
            + "  else if (window.ActiveXObject)\n"
            + "    request = new ActiveXObject('Microsoft.XMLHTTP');\n"
            + "  request.open('GET', 'foo.txt', false);\n"
            + "  request.send(null);\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'></body></html>";

        final WebClient client = getWebClient();
        final MockWebConnection conn = new MockWebConnection();
        conn.setResponse(URL_FIRST, html);
        conn.setDefaultResponse("");
        client.setWebConnection(conn);
        client.getPage(URL_FIRST);
    }

    /**
     * Test calls to send('foo') for a GET. HtmlUnit 1.14 was incorrectly throwing an exception.
     * @throws Exception if the test fails
     */
    @Test
    public void testSendGETWithContent() throws Exception {
        testSend("'foo'");
    }

    /**
     * Test calls to send() without any arguments.
     * @throws Exception if the test fails
     */
    @Test
    public void testSendNoArg() throws Exception {
        testSend("");
    }

    /**
     * @throws Exception if the test fails
     */
    private void testSend(final String sendArg) throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + "function test() {\n"
            + "  var request;\n"
            + "  if (window.XMLHttpRequest)\n"
            + "    request = new XMLHttpRequest();\n"
            + "  else if (window.ActiveXObject)\n"
            + "    request = new ActiveXObject('Microsoft.XMLHTTP');\n"
            + "  request.open('GET', 'foo.txt', false);\n"
            + "  request.send(" + sendArg + ");\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'></body></html>";

        final WebClient client = getWebClient();
        final MockWebConnection conn = new MockWebConnection();
        conn.setResponse(URL_FIRST, html);
        conn.setDefaultResponse("");
        client.setWebConnection(conn);
        client.getPage(URL_FIRST);
    }

    /**
     * Regression test for bug 1357412.
     * Response received by the XMLHttpRequest should not come in any window
     * @throws Exception if the test fails
     */
    @Test
    public void testResponseNotInWindow() throws Exception {
        final String html = "<html><head><title>foo</title>\n"
            + "<script>\n"
            + "function test() {\n"
            + "  var request;\n"
            + "  if (window.XMLHttpRequest)\n"
            + "    request = new XMLHttpRequest();\n"
            + "  else if (window.ActiveXObject)\n"
            + "    request = new ActiveXObject('Microsoft.XMLHTTP');\n"
            + "  request.open('GET', 'foo.txt', false);\n"
            + "  request.send();\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'></body></html>";

        final WebClient client = getWebClient();
        final MockWebConnection conn = new MockWebConnection();
        conn.setResponse(URL_FIRST, html);
        conn.setDefaultResponse("");
        client.setWebConnection(conn);
        final HtmlPage page = client.getPage(URL_FIRST);
        assertEquals(URL_FIRST, page.getWebResponse().getWebRequest().getUrl());
        assertEquals("foo", page.getTitleText());
    }

    /**
     * Test Mozilla's overrideMimeType method.
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(Browser.FF)
    public void testOverrideMimeType() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + "function test() {\n"
            + "  var request = new XMLHttpRequest();\n"
            + "  request.open('GET', 'foo.xml.txt', false);\n"
            + "  request.send('');\n"
            + "  alert(request.responseXML == null);\n"
            + "  request.overrideMimeType('text/xml');\n"
            + "  request.open('GET', 'foo.xml.txt', false);\n"
            + "  request.send('');\n"
            + "  alert(request.responseXML == null);\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'></body></html>";

        final WebClient client = getWebClient();
        final List<String> collectedAlerts = new ArrayList<String>();
        client.setAlertHandler(new CollectingAlertHandler(collectedAlerts));
        final MockWebConnection conn = new MockWebConnection();
        conn.setResponse(URL_FIRST, html);
        final URL urlPage2 = new URL(URL_FIRST + "foo.xml.txt");
        conn.setResponse(urlPage2, "<bla someAttr='someValue'><foo><fi id='fi1'/><fi/></foo></bla>\n",
                "text/plain");
        client.setWebConnection(conn);
        client.getPage(URL_FIRST);

        final String[] alerts = {"true", "false"};
        assertEquals(alerts, collectedAlerts);
    }

    /**
     * Regression test for bug 1611097.
     * https://sourceforge.net/tracker/index.php?func=detail&aid=1611097&group_id=47038&atid=448266
     * Caution: the problem appeared with jdk 1.4 but not with jdk 1.5 as String contains a
     * replace(CharSequence, CharSequence) method in this version
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE = { "ibcdefg", "xxxxxfg" }, FF = { })
    public void testReplaceOnTextData() throws Exception {
        final String html =
              "<html>\n"
            + "  <head>\n"
            + "    <title>XMLHttpRequest Test</title>\n"
            + "    <script>\n"
            + "      var request;\n"
            + "      function testAsync() {\n"
            + "        if (window.XMLHttpRequest)\n"
            + "          request = new XMLHttpRequest();\n"
            + "        else if (window.ActiveXObject)\n"
            + "          request = new ActiveXObject('Microsoft.XMLHTTP');\n"
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
            + "  <body onload='testAsync()'>\n"
            + "  </body>\n"
            + "</html>";

        final String xml =
              "<updates>\n"
            + "<update>abcdefg</update>\n"
            + "<update>hijklmn</update>\n"
            + "</updates>";

        final WebClient client = getWebClient();
        final List<String> collectedAlerts = Collections.synchronizedList(new ArrayList<String>());
        client.setAlertHandler(new CollectingAlertHandler(collectedAlerts));
        final MockWebConnection conn = new MockWebConnection();
        conn.setResponse(URL_FIRST, html);
        conn.setResponse(URL_SECOND, xml, "text/xml");
        client.setWebConnection(conn);
        client.getPage(URL_FIRST);

        assertEquals(getExpectedAlerts(), collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testSetLocation() throws Exception {
        final String content =
              "<html>\n"
            + "  <head>\n"
            + "    <title>Page1</title>\n"
            + "    <script>\n"
            + "      var request;\n"
            + "      function testAsync() {\n"
            + "        if (window.XMLHttpRequest)\n"
            + "          request = new XMLHttpRequest();\n"
            + "        else if (window.ActiveXObject)\n"
            + "          request = new ActiveXObject('Microsoft.XMLHTTP');\n"
            + "        request.onreadystatechange = onReadyStateChange;\n"
            + "        request.open('GET', 'foo.xml', true);\n"
            + "        request.send('');\n"
            + "      }\n"
            + "      function onReadyStateChange() {\n"
            + "        if (request.readyState == 4) {\n"
            + "          window.location.href = 'about:blank';\n"
            + "        }\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='testAsync()'>\n"
            + "  </body>\n"
            + "</html>";

        getMockWebConnection().setDefaultResponse("");
        final WebWindow window = loadPage(getBrowserVersion(), content, null).getEnclosingWindow();
        assertEquals(0, window.getWebClient().waitForBackgroundJavaScriptStartingBefore(1000));
        assertEquals("about:blank", window.getEnclosedPage().getWebResponse().getWebRequest().getUrl());
    }

    /**
     * Asynchronous callback should be called in "main" js thread and not parallel to other js execution.
     * See https://sourceforge.net/tracker/index.php?func=detail&aid=1508377&group_id=47038&atid=448266.
     * @throws Exception if the test fails
     */
    @Test
    public void testNoParallelJSExecutionInPage() throws Exception {
        final String content = "<html><head><script>\n"
            + "function getXMLHttpRequest() {\n"
            + " if (window.XMLHttpRequest)\n"
            + "        return new XMLHttpRequest();\n"
            + " else if (window.ActiveXObject)\n"
            + "        return new ActiveXObject('Microsoft.XMLHTTP');\n"
            + "}\n"
            + "var j = 0;\n"
            + "function test() {\n"
            + " req = getXMLHttpRequest();\n"
            + " req.onreadystatechange = handler;\n"
            + " req.open('post', 'foo.xml', true);\n"
            + " req.send('');\n"
            + " alert('before long loop');\n"
            + " for (var i = 0; i < 5000; i++) {\n"
            + "     j = j + 1;\n"
            + " }\n"
            + " alert('after long loop');\n"
            + "}\n"
            + "function handler() {\n"
            + " if (req.readyState == 4)\n"
            + "     alert('ready state handler, content loaded: j=' + j);\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'></body></html>";

        final WebClient client = getWebClient();
        final List<String> collectedAlerts = Collections.synchronizedList(new ArrayList<String>());
        client.setAlertHandler(new CollectingAlertHandler(collectedAlerts));
        final MockWebConnection conn = new MockWebConnection() {
            @Override
            public WebResponse getResponse(final WebRequest webRequest) throws IOException {
                collectedAlerts.add(webRequest.getUrl().toExternalForm());
                return super.getResponse(webRequest);
            }
        };
        conn.setResponse(URL_FIRST, content);
        final URL urlPage2 = new URL(URL_FIRST + "foo.xml");
        conn.setResponse(urlPage2, "<foo/>\n", "text/xml");
        client.setWebConnection(conn);
        client.getPage(URL_FIRST);

        assertEquals(0, client.waitForBackgroundJavaScriptStartingBefore(1000));

        final String[] alerts = {URL_FIRST.toExternalForm(), "before long loop", "after long loop",
            urlPage2.toExternalForm(), "ready state handler, content loaded: j=5000" };
        assertEquals(alerts, collectedAlerts);
    }

    /**
     * Tests that the <tt>Referer</tt> header is set correctly.
     * @throws Exception if the test fails
     */
    @Test
    public void testRefererHeader() throws Exception {
        final String content = "<html><head><script>\n"
            + "function getXMLHttpRequest() {\n"
            + " if (window.XMLHttpRequest)\n"
            + "        return new XMLHttpRequest();\n"
            + " else if (window.ActiveXObject)\n"
            + "        return new ActiveXObject('Microsoft.XMLHTTP');\n"
            + "}\n"
            + "function test() {\n"
            + " req = getXMLHttpRequest();\n"
            + " req.open('post', 'foo.xml', false);\n"
            + " req.send('');\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'></body></html>";

        final WebClient client = getWebClient();
        final MockWebConnection conn = new MockWebConnection();
        conn.setResponse(URL_FIRST, content);
        final URL urlPage2 = new URL(URL_FIRST + "foo.xml");
        conn.setResponse(urlPage2, "<foo/>\n", "text/xml");
        client.setWebConnection(conn);
        client.getPage(URL_FIRST);

        final WebRequest request = conn.getLastWebRequest();
        assertEquals(urlPage2, request.getUrl());
        assertEquals(URL_FIRST.toExternalForm(), request.getAdditionalHeaders().get("Referer"));
    }

    /**
     * Test for bug https://sourceforge.net/tracker/?func=detail&atid=448266&aid=1784330&group_id=47038.
     * @throws Exception if an error occurs
     */
    @Test
    @Browsers(Browser.IE)
    @Alerts("0")
    public void testCaseSensitivity() throws Exception {
        final String html = "<html><head><script>\n"
            + "function test() {\n"
            + "  var req = new ActiveXObject('MSXML2.XmlHttp');\n"
            + "  alert(req.readyState);\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'></body></html>";

        loadPageWithAlerts(html);
    }

    /**
     * Tests that the different HTTP methods are supported.
     * @throws Exception if an error occurs
     */
    @Test
    public void testMethods() throws Exception {
        testMethod(HttpMethod.GET);
        testMethod(HttpMethod.HEAD);
        testMethod(HttpMethod.DELETE);
        testMethod(HttpMethod.POST);
        testMethod(HttpMethod.PUT);
        testMethod(HttpMethod.OPTIONS);
        testMethod(HttpMethod.TRACE);
    }

    /**
     * @throws Exception if the test fails
     */
    private void testMethod(final HttpMethod method) throws Exception {
        final String content = "<html><head><script>\n"
            + "function getXMLHttpRequest() {\n"
            + " if (window.XMLHttpRequest)\n"
            + "        return new XMLHttpRequest();\n"
            + " else if (window.ActiveXObject)\n"
            + "        return new ActiveXObject('Microsoft.XMLHTTP');\n"
            + "}\n"
            + "function test() {\n"
            + " req = getXMLHttpRequest();\n"
            + " req.open('" + method.name().toLowerCase() + "', 'foo.xml', false);\n"
            + " req.send('');\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'></body></html>";

        final WebClient client = getWebClient();
        final MockWebConnection conn = new MockWebConnection();
        conn.setResponse(URL_FIRST, content);
        final URL urlPage2 = new URL(URL_FIRST + "foo.xml");
        conn.setResponse(urlPage2, "<foo/>\n", "text/xml");
        client.setWebConnection(conn);
        client.getPage(URL_FIRST);

        final WebRequest request = conn.getLastWebRequest();
        assertEquals(urlPage2, request.getUrl());
        assertSame(method, request.getHttpMethod());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(Browser.IE)
    @Alerts("2")
    public void testResponseXML_selectNodesIE() throws Exception {
        final String html =
              "<html>\n"
            + "  <head>\n"
            + "    <title>XMLHttpRequest Test</title>\n"
            + "    <script>\n"
            + "      function test() {\n"
            + "        var request;\n"
            + "        if (window.XMLHttpRequest)\n"
            + "          request = new XMLHttpRequest();\n"
            + "        else if (window.ActiveXObject)\n"
            + "          request = new ActiveXObject('Microsoft.XMLHTTP');\n"
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

        final WebClient client = getWebClient();
        final List<String> collectedAlerts = new ArrayList<String>();
        client.setAlertHandler(new CollectingAlertHandler(collectedAlerts));
        final MockWebConnection conn = new MockWebConnection();
        conn.setResponse(URL_FIRST, html);
        conn.setResponse(URL_SECOND, xml, "text/xml");
        client.setWebConnection(conn);
        client.getPage(URL_FIRST);

        assertEquals(getExpectedAlerts(), collectedAlerts);
    }

    /**
     * Was causing a deadlock on 03.11.2007 (and probably with release 1.13 too).
     * @throws Exception if the test fails
     */
    @Test
    public void testXMLHttpRequestWithDomChangeListenerDeadlock() throws Exception {
        final String content
            = "<html><head><title>foo</title>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    frames[0].test('foo1.txt', true);\n"
            + "    frames[0].test('foo2.txt', false);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body>\n"
            + "<p id='p1' title='myTitle' onclick='test()'></p>\n"
            + "<iframe src='page2.html'></iframe>\n"
            + "</body></html>";

        final String content2
            = "<html><head><title>foo</title>\n"
            + "<script>\n"
            + "function test(_src, _async)\n"
            + "{\n"
            + "  var request;\n"
            + "  if (window.XMLHttpRequest)\n"
            + "    request = new XMLHttpRequest();\n"
            + "  else if (window.ActiveXObject)\n"
            + "    request = new ActiveXObject('Microsoft.XMLHTTP');\n"
            + "  request.onreadystatechange = onReadyStateChange;\n"
            + "  request.open('GET', _src, _async);\n"
            + "  request.send('');\n"
            + "}\n"
            + "function onReadyStateChange() {\n"
            + "  parent.document.getElementById('p1').title = 'new title';\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body>\n"
            + "<p id='p1' title='myTitle'></p>\n"
            + "</body></html>";

        final MockWebConnection connection = new MockWebConnection() {
            private boolean gotFoo1_ = false;

            @Override
            public WebResponse getResponse(final WebRequest webRequest) throws IOException {
                final String url = webRequest.getUrl().toExternalForm();

                synchronized (this) {
                    while (!gotFoo1_ && url.endsWith("foo2.txt")) {
                        try {
                            wait(100);
                        }
                        catch (final InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
                if (url.endsWith("foo1.txt")) {
                    gotFoo1_ = true;
                }
                return super.getResponse(webRequest);
            }
        };
        connection.setDefaultResponse("");
        connection.setResponse(URL_FIRST, content);
        connection.setResponse(new URL(URL_FIRST, "page2.html"), content2);

        final WebClient webClient = getWebClient();
        webClient.setWebConnection(connection);

        final HtmlPage page = webClient.getPage(URL_FIRST);
        final DomChangeListener listener = new DomChangeListener() {
            private static final long serialVersionUID = 1978591653173770574L;
            public void nodeAdded(final DomChangeEvent event) {
                // Empty.
            }
            public void nodeDeleted(final DomChangeEvent event) {
                // Empty.
            }
        };
        page.addDomChangeListener(listener);
        page.<HtmlElement>getHtmlElementById("p1").click();
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(Browser.FF)
    @Alerts({ "null", "myID", "blah", "span", "[object XMLDocument]" })
    public void testResponseXML_getElementById_FF() throws Exception {
        final String html =
              "<html>\n"
            + "  <head>\n"
            + "    <title>XMLHttpRequest Test</title>\n"
            + "    <script>\n"
            + "      function test() {\n"
            + "        var request;\n"
            + "        if (window.XMLHttpRequest)\n"
            + "          request = new XMLHttpRequest();\n"
            + "        else if (window.ActiveXObject)\n"
            + "          request = new ActiveXObject('Microsoft.XMLHTTP');\n"
            + "        request.open('GET', '" + URL_SECOND + "', false);\n"
            + "        request.send('');\n"
            + "        alert(request.responseXML.getElementById('id1'));\n"
            + "        alert(request.responseXML.getElementById('myID').id);\n"
            + "        alert(request.responseXML.getElementById('myID').innerHTML);\n"
            + "        alert(request.responseXML.getElementById('myID').tagName);\n"
            + "        alert(request.responseXML.getElementById('myID').ownerDocument);\n"
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

        final WebClient client = getWebClient();
        final List<String> collectedAlerts = new ArrayList<String>();
        client.setAlertHandler(new CollectingAlertHandler(collectedAlerts));
        final MockWebConnection conn = new MockWebConnection();
        conn.setResponse(URL_FIRST, html);
        conn.setResponse(URL_SECOND, xml, "text/xml");
        client.setWebConnection(conn);
        client.getPage(URL_FIRST);

        assertEquals(getExpectedAlerts(), collectedAlerts);
    }

    /**
     * Firefox does not call onreadystatechange handler if sync.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE = { "1", "2", "3", "4" })
    public void testOnreadystatechange_sync() throws Exception {
        final String html =
              "<html>\n"
            + "  <head>\n"
            + "    <title>XMLHttpRequest Test</title>\n"
            + "    <script>\n"
            + "      var request;\n"
            + "      function test() {\n"
            + "        if (window.XMLHttpRequest)\n"
            + "          request = new XMLHttpRequest();\n"
            + "        else if (window.ActiveXObject)\n"
            + "          request = new ActiveXObject('Microsoft.XMLHTTP');\n"
            + "        request.open('GET', '" + URL_SECOND + "', false);\n"
            + "        request.onreadystatechange = onStateChange;\n"
            + "        request.send('');\n"
            + "      }\n"
            + "      function onStateChange() {\n"
            + "        alert(request.readyState);\n"
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

        final WebClient client = getWebClient();
        final List<String> collectedAlerts = new ArrayList<String>();
        client.setAlertHandler(new CollectingAlertHandler(collectedAlerts));
        final MockWebConnection conn = new MockWebConnection();
        conn.setResponse(URL_FIRST, html);
        conn.setResponse(URL_SECOND, xml, "text/xml");
        client.setWebConnection(conn);
        client.getPage(URL_FIRST);

        assertEquals(getExpectedAlerts(), collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(FF = { "[object Element]", "[object Element]", "[object HTMLBodyElement]",
            "[object HTMLSpanElement]", "[object XMLDocument]", "undefined" },
            IE = { "[object]", "[object]", "[object]",
            "<body xmlns=\"http://www.w3.org/1999/xhtml\"><span id=\"out\">Hello Bob Dole!</span></body>" })
    public void testResponseXML_getElementById() throws Exception {
        final String html =
              "<html>\n"
            + "  <head>\n"
            + "    <title>XMLHttpRequest Test</title>\n"
            + "    <script>\n"
            + "      function test() {\n"
            + "        var request;\n"
            + "        if (window.XMLHttpRequest)\n"
            + "          request = new XMLHttpRequest();\n"
            + "        else if (window.ActiveXObject)\n"
            + "          request = new ActiveXObject('Microsoft.XMLHTTP');\n"
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

        final WebClient client = getWebClient();
        final List<String> collectedAlerts = new ArrayList<String>();
        client.setAlertHandler(new CollectingAlertHandler(collectedAlerts));
        final MockWebConnection conn = new MockWebConnection();
        conn.setResponse(URL_FIRST, html);
        conn.setResponse(URL_SECOND, xml, "text/xml");
        client.setWebConnection(conn);
        client.getPage(URL_FIRST);

        assertEquals(getExpectedAlerts(), collectedAlerts);
    }

    /**
     * Verifies that the default encoding for an XMLHttpRequest is UTF-8.
     * @throws Exception if an error occurs
     */
    @Test
    public void testDefaultEncodingIsUTF8() throws Exception {
        final String html =
              "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + "      function test() {\n"
            + "        var request;\n"
            + "        if (window.XMLHttpRequest)\n"
            + "          request = new XMLHttpRequest();\n"
            + "        else if (window.ActiveXObject)\n"
            + "          request = new ActiveXObject('Microsoft.XMLHTTP');\n"
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

        final WebClient client = getWebClient();
        final List<String> collectedAlerts = new ArrayList<String>();
        client.setAlertHandler(new CollectingAlertHandler(collectedAlerts));
        final MockWebConnection conn = new MockWebConnection();
        conn.setResponse(URL_FIRST, html);
        conn.setResponse(URL_SECOND, responseBytes, 200, "OK", "text/html", new ArrayList<NameValuePair>());
        client.setWebConnection(conn);
        client.getPage(URL_FIRST);

        final String[] alerts = {response};
        assertEquals(alerts, collectedAlerts);
    }

    /**
     * Regression test for bug 1209686 (onreadystatechange not called with partial data when emulating FF).
     * @throws Exception if an error occurs
     */
    @Browsers(Browser.FF)
    @Test
    @NotYetImplemented
    public void testStreaming() throws Exception {
        final Map<String, Class< ? extends Servlet>> servlets = new HashMap<String, Class< ? extends Servlet>>();
        servlets.put("/test", StreamingServlet.class);

        final String resourceBase = "./src/test/resources/com/gargoylesoftware/htmlunit/javascript/host";
        startWebServer(resourceBase, null, servlets);
        final WebClient client = getWebClient();
        final HtmlPage page = client.getPage("http://localhost:" + PORT + "/XMLHttpRequestTest_streaming.html");
        assertEquals(0, client.waitForBackgroundJavaScriptStartingBefore(1000));
        final HtmlElement body = page.getBody();
        assertEquals(10, body.asText().split("\n").length);
    }

    /**
     * Connection refused WebConnection for URL_SECOND.
     */
    private static final class DisconnectedMockWebConnection extends MockWebConnection {
        /** {@inheritDoc} */
        @Override
        public WebResponse getResponse(final WebRequest request) throws IOException {
            if (request.getUrl().equals(URL_SECOND)) {
                throw new IOException("Connection refused");
            }
            return super.getResponse(request);
        }
    }

    /**
     * Custom servlet which streams content to the client little by little.
     */
    public static final class StreamingServlet extends HttpServlet {
        private static final long serialVersionUID = -5892710154241871545L;
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
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(Browser.FF)
    public void responseXML_html_select() throws Exception {
        final String html =
              "<html>\n"
            + "  <head>\n"
            + "    <title>XMLHttpRequest Test</title>\n"
            + "    <script>\n"
            + "      function test() {\n"
            + "        var request;\n"
            + "        if (window.XMLHttpRequest)\n"
            + "          request = new XMLHttpRequest();\n"
            + "        else if (window.ActiveXObject)\n"
            + "          request = new ActiveXObject('Microsoft.XMLHTTP');\n"
            + "        request.open('GET', '" + URL_SECOND + "', false);\n"
            + "        request.send('');\n"
            + "        alert(request.responseXML.getElementById('myID').id);\n"
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
        final WebClient client = getWebClient();
        final List<String> collectedAlerts = new ArrayList<String>();
        client.setAlertHandler(new CollectingAlertHandler(collectedAlerts));
        final MockWebConnection conn = new MockWebConnection();
        conn.setResponse(URL_FIRST, html);
        conn.setResponse(URL_SECOND, xml, "text/xml");
        client.setWebConnection(conn);
        client.getPage(URL_FIRST);

        final String[] expectedAlerts = {"myID"};
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(Browser.FF)
    public void responseXML_html_form() throws Exception {
        final String html =
              "<html>\n"
            + "  <head>\n"
            + "    <title>XMLHttpRequest Test</title>\n"
            + "    <script>\n"
            + "      function test() {\n"
            + "        var request;\n"
            + "        if (window.XMLHttpRequest)\n"
            + "          request = new XMLHttpRequest();\n"
            + "        else if (window.ActiveXObject)\n"
            + "          request = new ActiveXObject('Microsoft.XMLHTTP');\n"
            + "        request.open('GET', '" + URL_SECOND + "', false);\n"
            + "        request.send('');\n"
            + "        alert(request.responseXML.getElementById('myID').myInput.name);\n"
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
        final WebClient client = getWebClient();
        final List<String> collectedAlerts = new ArrayList<String>();
        client.setAlertHandler(new CollectingAlertHandler(collectedAlerts));
        final MockWebConnection conn = new MockWebConnection();
        conn.setResponse(URL_FIRST, html);
        conn.setResponse(URL_SECOND, xml, "text/xml");
        client.setWebConnection(conn);
        client.getPage(URL_FIRST);

        final String[] expectedAlerts = {"myInput"};
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Browsers(Browser.IE)
    @Alerts("0")
    public void caseSensitivity_activeX() throws Exception {
        final String html = "<html><head><script>\n"
            + "function test() {\n"
            + "  var req = new ActiveXObject('MSXML2.XmlHttp');\n"
            + "  alert(req.reAdYsTaTe);\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'></body></html>";
        loadPageWithAlerts(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Browsers({ Browser.IE7, Browser.IE8, Browser.FF })
    @Alerts("undefined")
    public void caseSensitivity_XMLHttpRequest() throws Exception {
        final String html = "<html><head><script>\n"
            + "function test() {\n"
            + "  var req = new XMLHttpRequest();\n"
            + "  alert(req.reAdYsTaTe);\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'></body></html>";
        loadPageWithAlerts(html);
    }

    /**
     * Tests the value of "this" in handler.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(FF2 = "this == handler", FF3 = "this == request", IE = "this == request")
    public void thisValueInHandler() throws Exception {
        final String html =
              "<html>\n"
            + "  <head>\n"
            + "    <title>XMLHttpRequest Test</title>\n"
            + "    <script>\n"
            + "      var request;\n"
            + "      function testAsync() {\n"
            + "        if (window.XMLHttpRequest)\n"
            + "          request = new XMLHttpRequest();\n"
            + "        else if (window.ActiveXObject)\n"
            + "          request = new ActiveXObject('Microsoft.XMLHTTP');\n"
            + "        request.onreadystatechange = onReadyStateChange;\n"
            + "        request.open('GET', 'foo.xml', true);\n"
            + "        request.send('');\n"
            + "      }\n"
            + "      function onReadyStateChange() {\n"
            + "        if (request.readyState == 4) {\n"
            + "          if (this == request)\n"
            + "            alert('this == request');\n"
            + "          else if (this == onReadyStateChange)\n"
            + "            alert('this == handler');\n"
            + "          else alert('not expected: ' + this)\n"
            + "        }\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='testAsync()'>\n"
            + "  </body>\n"
            + "</html>";

        final WebClient client = getWebClient();
        final List<String> collectedAlerts = Collections.synchronizedList(new ArrayList<String>());
        client.setAlertHandler(new CollectingAlertHandler(collectedAlerts));
        final MockWebConnection conn = new MockWebConnection();
        conn.setResponse(URL_FIRST, html);
        conn.setDefaultResponse("");
        client.setWebConnection(conn);
        client.getPage(URL_FIRST);

        assertEquals(0, client.waitForBackgroundJavaScriptStartingBefore(1000));
        assertEquals(getExpectedAlerts(), collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(Browser.FF)
    @Alerts({ "orsc1", "orsc1", "orsc2", "orsc3", "orsc4", "4", "<a>b</a>", "[object XMLHttpRequest]" })
    public void onload() throws Exception {
        final String html =
              "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + "      function test() {\n"
            + "        var xhr;\n"
            + "        if (window.XMLHttpRequest) xhr = new XMLHttpRequest();\n"
            + "        else xhr = new ActiveXObject('Microsoft.XMLHTTP');\n"
            + "        xhr.onreadystatechange = function() { alert('orsc' + xhr.readyState); };\n"
            + "        xhr.onload = function() { alert(xhr.readyState); alert(xhr.responseText); alert(this); }\n"
            + "        xhr.open('GET', '" + URL_SECOND + "', true);\n"
            + "        xhr.send('');\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='test()'></body>\n"
            + "</html>";

        final String xml = "<a>b</a>";

        final WebClient client = getWebClient();
        client.setAjaxController(new NicelyResynchronizingAjaxController());
        final List<String> collectedAlerts = new ArrayList<String>();
        client.setAlertHandler(new CollectingAlertHandler(collectedAlerts));
        final MockWebConnection conn = new MockWebConnection();
        conn.setResponse(URL_FIRST, html);
        conn.setResponse(URL_SECOND, xml, "text/xml");
        client.setWebConnection(conn);
        client.getPage(URL_FIRST);

        assertEquals(getExpectedAlerts(), collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(Browser.NONE)
    public void isAuthorizedHeader() throws Exception {
        assertTrue(XMLHttpRequest.isAuthorizedHeader("Foo"));
        assertTrue(XMLHttpRequest.isAuthorizedHeader("Content-Type"));

        final String[] headers = {"accept-charset", "accept-encoding",
            "connection", "content-length", "cookie", "cookie2", "content-transfer-encoding", "date",
            "expect", "host", "keep-alive", "referer", "te", "trailer", "transfer-encoding", "upgrade",
            "user-agent", "via" };
        for (final String header : headers) {
            assertFalse(XMLHttpRequest.isAuthorizedHeader(header));
            assertFalse(XMLHttpRequest.isAuthorizedHeader(header.toUpperCase()));
        }
        assertFalse(XMLHttpRequest.isAuthorizedHeader("Proxy-"));
        assertFalse(XMLHttpRequest.isAuthorizedHeader("Proxy-Control"));
        assertFalse(XMLHttpRequest.isAuthorizedHeader("Proxy-Hack"));
        assertFalse(XMLHttpRequest.isAuthorizedHeader("Sec-"));
        assertFalse(XMLHttpRequest.isAuthorizedHeader("Sec-Hack"));
    }
}
