/*
 * Copyright (c) 2002-2008 Gargoyle Software Inc. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 3. The end-user documentation included with the redistribution, if any, must
 *    include the following acknowledgment:
 *
 *       "This product includes software developed by Gargoyle Software Inc.
 *        (http://www.GargoyleSoftware.com/)."
 *
 *    Alternately, this acknowledgment may appear in the software itself, if
 *    and wherever such third-party acknowledgments normally appear.
 * 4. The name "Gargoyle Software" must not be used to endorse or promote
 *    products derived from this software without prior written permission.
 *    For written permission, please contact info@GargoyleSoftware.com.
 * 5. Products derived from this software may not be called "HtmlUnit", nor may
 *    "HtmlUnit" appear in their name, without prior written permission of
 *    Gargoyle Software Inc.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL GARGOYLE
 * SOFTWARE INC. OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
 * OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.gargoylesoftware.htmlunit.javascript.host;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.CollectingAlertHandler;
import com.gargoylesoftware.htmlunit.MockWebConnection;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.SubmitMethod;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequestSettings;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.WebTestCase;
import com.gargoylesoftware.htmlunit.WebWindow;
import com.gargoylesoftware.htmlunit.html.ClickableElement;
import com.gargoylesoftware.htmlunit.html.DomChangeEvent;
import com.gargoylesoftware.htmlunit.html.DomChangeListener;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * Tests for {@link XMLHttpRequest}.
 *
 * @version $Revision$
 * @author Daniel Gredler
 * @author Marc Guillemot
 * @author Ahmed Ashour
 */
public class XMLHttpRequestTest extends WebTestCase {

    private static final String UNINITIALIZED = String.valueOf(XMLHttpRequest.STATE_UNINITIALIZED);
    private static final String LOADING = String.valueOf(XMLHttpRequest.STATE_LOADING);
    private static final String LOADED = String.valueOf(XMLHttpRequest.STATE_LOADED);
    private static final String INTERACTIVE = String.valueOf(XMLHttpRequest.STATE_INTERACTIVE);
    private static final String COMPLETED = String.valueOf(XMLHttpRequest.STATE_COMPLETED);

    /**
     * Tests synchronous use of XMLHttpRequest.
     * @throws Exception If the test fails.
     */
    @Test
    public void testSyncUse() throws Exception {
        testSyncUse(BrowserVersion.FIREFOX_2);
        testSyncUse(BrowserVersion.INTERNET_EXPLORER_6_0);
    }

    /**
     * Tests Mozilla and IE style object creation.
     * @throws Exception If the test fails.
     */
    @Test
    public void testCreation() throws Exception {
        testCreation(BrowserVersion.FIREFOX_2, new String[] {"[object XMLHttpRequest]"});
        testCreation(BrowserVersion.INTERNET_EXPLORER_6_0, new String[] {"activeX created"});
    }

    /**
     * Tests Mozilla style object creation.
     * @throws Exception If the test fails.
     */
    void testCreation(final BrowserVersion browser, final String[] expected) throws Exception {
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

        createTestPageForRealBrowserIfNeeded(html, expected);

        final List<String> collectedAlerts = new ArrayList<String>();
        loadPage(browser, html, collectedAlerts);

        assertEquals(expected, collectedAlerts);
    }

    /**
     * Tests synchronous use of XMLHttpRequest.
     * @throws Exception If the test fails.
     */
    void testSyncUse(final BrowserVersion browserVersion) throws Exception {
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

        final WebClient client = new WebClient(browserVersion);
        final List<String> collectedAlerts = new ArrayList<String>();
        client.setAlertHandler(new CollectingAlertHandler(collectedAlerts));
        final MockWebConnection webConnection = new MockWebConnection(client);
        webConnection.setResponse(URL_FIRST, html);
        webConnection.setResponse(URL_SECOND, xml, "text/xml");
        client.setWebConnection(webConnection);
        client.getPage(URL_FIRST);

        final String[] alerts = {UNINITIALIZED, LOADING, COMPLETED, xml};
        assertEquals(alerts, collectedAlerts);
    }

    /**
     * Tests asynchronous use of XMLHttpRequest, using Mozilla style object creation.
     * @throws Exception If the test fails.
     */
    @Test
    public void testAsyncUse() throws Exception {
        testAsyncUse(BrowserVersion.FIREFOX_2);
        testAsyncUse(BrowserVersion.INTERNET_EXPLORER_6_0);
    }

    void testAsyncUse(final BrowserVersion browserVersion) throws Exception {
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

        final WebClient client = new WebClient(browserVersion);
        final List<String> collectedAlerts = Collections.synchronizedList(new ArrayList<String>());
        client.setAlertHandler(new CollectingAlertHandler(collectedAlerts));
        final MockWebConnection webConnection = new MockWebConnection(client);
        webConnection.setResponse(URL_FIRST, html);
        webConnection.setResponse(URL_SECOND, xml, "text/xml");
        client.setWebConnection(webConnection);
        final Page page = client.getPage(URL_FIRST);

        final String[] alerts = {UNINITIALIZED, LOADING, LOADING, LOADED, INTERACTIVE, COMPLETED, xml};

        assertTrue("thread failed to stop in 1 second", page.getEnclosingWindow().getThreadManager().joinAll(1000));
        assertEquals(alerts, collectedAlerts);
    }

    /**
     * Regression test for bug 1209692
     * http://sourceforge.net/tracker/index.php?func=detail&aid=1209692&group_id=47038&atid=448266
     * @throws Exception If the test fails.
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

        final WebClient client = new WebClient();
        final List<String> collectedAlerts = new ArrayList<String>();
        client.setAlertHandler(new CollectingAlertHandler(collectedAlerts));
        final MockWebConnection webConnection = new MockWebConnection(client);
        webConnection.setResponse(URL_FIRST, html);
        final URL urlPage2 = new URL(URL_FIRST + "/foo.xml");
        webConnection.setResponse(urlPage2, xml, "text/xml");
        client.setWebConnection(webConnection);
        client.getPage(URL_FIRST);

        final String[] alerts = {COMPLETED, xml};
        assertEquals(alerts, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails.
     */
    @Test
    public void testResponseText_NotXml() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + "function test()\n"
            + "{\n"
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

        final WebClient client = new WebClient();
        final List<String> collectedAlerts = new ArrayList<String>();
        client.setAlertHandler(new CollectingAlertHandler(collectedAlerts));
        final MockWebConnection webConnection = new MockWebConnection(client);
        webConnection.setResponse(URL_FIRST, html);
        final URL urlPage2 = new URL(URL_FIRST + "/foo.txt");
        webConnection.setResponse(urlPage2, "bla bla", "text/plain");
        client.setWebConnection(webConnection);
        client.getPage(URL_FIRST);

        final String[] alerts = {"bla bla"};
        assertEquals(alerts, collectedAlerts);
    }

    /**
     * Test access to the XML DOM
     * @throws Exception if the test fails.
     */
    @Test
    public void testResponseXML() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + "function test()\n"
            + "{\n"
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
            + "  alert(rootNode.nodeName);\n"
            + "  alert(rootNode.attributes[0].nodeName);\n"
            + "  alert(rootNode.attributes[0].nodeValue);\n"
            + "  alert(rootNode.attributes['someAttr'] == rootNode.attributes[0]);\n"
            + "  alert(rootNode.firstChild.nodeName);\n"
            + "  alert(rootNode.firstChild.childNodes.length);\n"
            + "  alert(request.responseXML.getElementsByTagName('fi').item(0).attributes[0].nodeValue);\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'></body></html>";

        final WebClient client = new WebClient();
        final List<String> collectedAlerts = new ArrayList<String>();
        client.setAlertHandler(new CollectingAlertHandler(collectedAlerts));
        final MockWebConnection webConnection = new MockWebConnection(client);
        webConnection.setResponse(URL_FIRST, html);
        final URL urlPage2 = new URL(URL_FIRST + "/foo.xml");
        webConnection.setResponse(urlPage2, "<bla someAttr='someValue'><foo><fi id='fi1'/><fi/></foo></bla>\n",
                "text/xml");
        client.setWebConnection(webConnection);
        client.getPage(URL_FIRST);

        final String[] alerts = {"1", "bla", "someAttr", "someValue", "true", "foo", "2", "fi1" };
        assertEquals(alerts, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails.
     */
    @Test
    public void testSendNull() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + "function test()\n"
            + "{\n"
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

        final WebClient client = new WebClient();
        final MockWebConnection webConnection = new MockWebConnection(client);
        webConnection.setResponse(URL_FIRST, html);
        webConnection.setDefaultResponse("");
        client.setWebConnection(webConnection);
        client.getPage(URL_FIRST);
    }

    /**
     * Test calls to send('foo') for a GET. HtmlUnit 1.14 was incorrectly throwing an exception.
     * @throws Exception if the test fails.
     */
    @Test
    public void testSendGETWithContent() throws Exception {
        testSend(BrowserVersion.FIREFOX_2, "'foo'");
        testSend(BrowserVersion.INTERNET_EXPLORER_6_0, "'foo'");
    }
    
    /**
     * Test calls to send() without any argument
     * @throws Exception if the test fails.
     */
    @Test
    public void testSendNoArg() throws Exception {
        testSendNoArg(BrowserVersion.INTERNET_EXPLORER_6_0);
        testSendNoArg(BrowserVersion.FIREFOX_2);
    }

    /**
     * @throws Exception if the test fails.
     */
    private void testSendNoArg(final BrowserVersion browserVersion) throws Exception {
        testSend(browserVersion, "");
    }

    /**
     * @throws Exception if the test fails.
     */
    private void testSend(final BrowserVersion browserVersion, final String sendArg) throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + "function test()\n"
            + "{\n"
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

        final WebClient client = new WebClient(browserVersion);
        final MockWebConnection webConnection = new MockWebConnection(client);
        webConnection.setResponse(URL_FIRST, html);
        webConnection.setDefaultResponse("");
        client.setWebConnection(webConnection);
        client.getPage(URL_FIRST);
    }

    /**
     * Regression test for bug 1357412.
     * Response received by the XMLHttpRequest should not come in any window
     * @throws Exception if the test fails.
     */
    @Test
    public void testResponseNotInWindow() throws Exception {
        final String html = "<html><head><title>foo</title>\n"
            + "<script>\n"
            + "function test()\n"
            + "{\n"
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

        final WebClient client = new WebClient();
        final MockWebConnection webConnection = new MockWebConnection(client);
        webConnection.setResponse(URL_FIRST, html);
        webConnection.setDefaultResponse("");
        client.setWebConnection(webConnection);
        final HtmlPage page = (HtmlPage) client.getPage(URL_FIRST);
        assertEquals(URL_FIRST, page.getWebResponse().getUrl());
        assertEquals("foo", page.getTitleText());
    }

    /**
     * Test Mozilla's overrideMimeType method
     * @throws Exception if the test fails.
     */
    @Test
    public void testOverrideMimeType() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + "function test()\n"
            + "{\n"
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

        final WebClient client = new WebClient(BrowserVersion.FIREFOX_2);
        final List<String> collectedAlerts = new ArrayList<String>();
        client.setAlertHandler(new CollectingAlertHandler(collectedAlerts));
        final MockWebConnection webConnection = new MockWebConnection(client);
        webConnection.setResponse(URL_FIRST, html);
        final URL urlPage2 = new URL(URL_FIRST + "/foo.xml.txt");
        webConnection.setResponse(urlPage2, "<bla someAttr='someValue'><foo><fi id='fi1'/><fi/></foo></bla>\n",
                "text/plain");
        client.setWebConnection(webConnection);
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
    public void testReplaceOnTextData() throws Exception {
        final String[] expectedAlertsFF = {};
        testReplaceOnTextData(BrowserVersion.FIREFOX_2, expectedAlertsFF);
        final String[] expectedAlertsIE = {"ibcdefg", "xxxxxfg"};
        testReplaceOnTextData(BrowserVersion.INTERNET_EXPLORER_6_0, expectedAlertsIE);
    }

    /**
     * @param browserVersion the browser version to simulate
     * @throws Exception if the test fails
     */
    private void testReplaceOnTextData(final BrowserVersion browserVersion, final String[] expectedAlerts)
        throws Exception {
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

        final WebClient client = new WebClient(browserVersion);
        final List<String> collectedAlerts = Collections.synchronizedList(new ArrayList<String>());
        client.setAlertHandler(new CollectingAlertHandler(collectedAlerts));
        final MockWebConnection webConnection = new MockWebConnection(client);
        webConnection.setResponse(URL_FIRST, html);
        webConnection.setResponse(URL_SECOND, xml, "text/xml");
        client.setWebConnection(webConnection);
        client.getPage(URL_FIRST);

        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception If the test fails
     */
    @Test
    public void testSetLocation() throws Exception {
        testSetLocation(BrowserVersion.FIREFOX_2);
        testSetLocation(BrowserVersion.INTERNET_EXPLORER_6_0);
    }

    void testSetLocation(final BrowserVersion browserVersion) throws Exception {
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
            + "        request.open('GET', 'about:blank', true);\n"
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

        final WebWindow window = loadPage(content).getEnclosingWindow();
        assertTrue("thread failed to stop in 4 seconds", window.getThreadManager().joinAll(4000));
        assertEquals("about:blank", window.getEnclosedPage().getWebResponse().getUrl());
    }

    /**
     * Asynchron callback should be called in "main" js thread and not parallel to other js execution
     * https://sourceforge.net/tracker/index.php?func=detail&aid=1508377&group_id=47038&atid=448266
     * @throws Exception if the test fails.
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
            + "function test()\n"
            + "{\n"
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
            + "function handler()\n"
            + "{\n"
            + " if (req.readyState == 4)\n"
            + " {\n"
            + "     alert('ready state handler, content loaded: j=' + j);\n"
            + " }\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'></body></html>";

        final WebClient client = new WebClient();
        final List<String> collectedAlerts = Collections.synchronizedList(new ArrayList<String>());
        client.setAlertHandler(new CollectingAlertHandler(collectedAlerts));
        final MockWebConnection webConnection = new MockWebConnection(client) {
            @Override
            public WebResponse getResponse(final WebRequestSettings webRequestSettings) throws IOException {
                collectedAlerts.add(webRequestSettings.getURL().toExternalForm());
                return super.getResponse(webRequestSettings);
            }
        };
        webConnection.setResponse(URL_FIRST, content);
        final URL urlPage2 = new URL(URL_FIRST + "/foo.xml");
        webConnection.setResponse(urlPage2, "<foo/>\n", "text/xml");
        client.setWebConnection(webConnection);
        final Page page = client.getPage(URL_FIRST);

        assertTrue("thread failed to stop in 4 seconds", page.getEnclosingWindow().getThreadManager().joinAll(4000));

        final String[] alerts = {URL_FIRST.toExternalForm(), "before long loop", "after long loop",
                urlPage2.toExternalForm(), "ready state handler, content loaded: j=5000" };
        assertEquals(alerts, collectedAlerts);
    }

    /**
     * Test that the Referer header is set correctly
     * @throws Exception if the test fails.
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
            + "function test()\n"
            + "{\n"
            + " req = getXMLHttpRequest();\n"
            + " req.open('post', 'foo.xml', false);\n"
            + " req.send('');\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'></body></html>";

        final WebClient client = new WebClient();
        final MockWebConnection webConnection = new MockWebConnection(client);
        webConnection.setResponse(URL_FIRST, content);
        final URL urlPage2 = new URL(URL_FIRST + "/foo.xml");
        webConnection.setResponse(urlPage2, "<foo/>\n", "text/xml");
        client.setWebConnection(webConnection);
        client.getPage(URL_FIRST);

        final WebRequestSettings settings = webConnection.getLastWebRequestSettings();
        assertEquals(urlPage2, settings.getURL());
        assertEquals(URL_FIRST.toExternalForm(), settings.getAdditionalHeaders().get("Referer"));
    }

    /**
     * Test for bug
     * https://sourceforge.net/tracker/?func=detail&atid=448266&aid=1784330&group_id=47038
     *
     * @throws Exception if the test fails.
     */
    @Test
    public void testCaseSensitivity() throws Exception {
        final String content = "<html><head><script>\n"
            + "function test() {\n"
            + "  var req = new ActiveXObject('MSXML2.XmlHttp');\n"
            + "  alert(req.readyState);\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'></body></html>";

        final String[] expectedAlerts = {"0"};
        final List<String> collectedAlerts = new ArrayList<String>();
        loadPage(BrowserVersion.INTERNET_EXPLORER_7_0, content, collectedAlerts);
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * Test that the different http methods are supported
     * @throws Exception if the test fails.
     */
    @Test
    public void testMethods() throws Exception {
        testMethod(SubmitMethod.GET);
        testMethod(SubmitMethod.HEAD);
        testMethod(SubmitMethod.DELETE);
        testMethod(SubmitMethod.POST);
        testMethod(SubmitMethod.PUT);
        testMethod(SubmitMethod.OPTIONS);
        testMethod(SubmitMethod.TRACE);
    }

    /**
     * @throws Exception if the test fails.
     */
    private void testMethod(final SubmitMethod method) throws Exception {
        final String content = "<html><head><script>\n"
            + "function getXMLHttpRequest() {\n"
            + " if (window.XMLHttpRequest)\n"
            + "        return new XMLHttpRequest();\n"
            + " else if (window.ActiveXObject)\n"
            + "        return new ActiveXObject('Microsoft.XMLHTTP');\n"
            + "}\n"
            + "function test()\n"
            + "{\n"
            + " req = getXMLHttpRequest();\n"
            + " req.open('" + method.getName() + "', 'foo.xml', false);\n"
            + " req.send('');\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'></body></html>";

        final WebClient client = new WebClient();
        final MockWebConnection webConnection = new MockWebConnection(client);
        webConnection.setResponse(URL_FIRST, content);
        final URL urlPage2 = new URL(URL_FIRST + "/foo.xml");
        webConnection.setResponse(urlPage2, "<foo/>\n", "text/xml");
        client.setWebConnection(webConnection);
        client.getPage(URL_FIRST);

        final WebRequestSettings settings = webConnection.getLastWebRequestSettings();
        assertEquals(urlPage2, settings.getURL());
        assertEquals(method, settings.getSubmitMethod());
    }

    /**
     * @throws Exception If the test fails.
     */
    @Test
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

        final WebClient client = new WebClient(BrowserVersion.INTERNET_EXPLORER_7_0);
        final List<String> collectedAlerts = new ArrayList<String>();
        client.setAlertHandler(new CollectingAlertHandler(collectedAlerts));
        final MockWebConnection webConnection = new MockWebConnection(client);
        webConnection.setResponse(URL_FIRST, html);
        webConnection.setResponse(URL_SECOND, xml, "text/xml");
        client.setWebConnection(webConnection);
        client.getPage(URL_FIRST);

        final String[] alerts = {"2"};
        assertEquals(alerts, collectedAlerts);
    }

    /**
     * Was causing a deadlock on 03.11.2007 (and probably with release 1.13 too)
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
            + "function onReadyStateChange()\n"
            + "{\n"
            + "  parent.document.getElementById('p1').title = 'new title';\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body>\n"
            + "<p id='p1' title='myTitle'></p>\n"
            + "</body></html>";

        final WebClient webClient = new WebClient();
        final MockWebConnection connection = new MockWebConnection(webClient) {
            private boolean gotFoo1_ = false;

            @Override
            public WebResponse getResponse(final WebRequestSettings webRequestSettings) throws IOException {
                final String url = webRequestSettings.getURL().toExternalForm();
                
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
                return super.getResponse(webRequestSettings);
            }
        };
        connection.setDefaultResponse("");
        connection.setResponse(URL_FIRST, content);
        connection.setResponse(new URL(URL_FIRST, "page2.html"), content2);
        webClient.setWebConnection(connection);

        final HtmlPage page = (HtmlPage) webClient.getPage(URL_FIRST);
        final DomChangeListener listener = new DomChangeListener() {
            public void nodeAdded(final DomChangeEvent event) {
                // nothing
            }
            public void nodeDeleted(final DomChangeEvent event) {
                // nothing
            }
        };
        page.addDomChangeListener(listener);
        ((ClickableElement) page.getHtmlElementById("p1")).click();
    }

    /**
     * @throws Exception If the test fails.
     */
    @Test
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
            + "        alert(request.responseXML.getElementById('myID'));\n"
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

        final WebClient client = new WebClient(BrowserVersion.FIREFOX_2);
        final List<String> collectedAlerts = new ArrayList<String>();
        client.setAlertHandler(new CollectingAlertHandler(collectedAlerts));
        final MockWebConnection webConnection = new MockWebConnection(client);
        webConnection.setResponse(URL_FIRST, html);
        webConnection.setResponse(URL_SECOND, xml, "text/xml");
        client.setWebConnection(webConnection);
        client.getPage(URL_FIRST);

        final String[] expectedAlerts = {"null"};
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * Firefox does not call onreadystatechange handler if sync.
     * @throws Exception If the test fails.
     */
    @Test
    public void testOnreadystatechange_sync() throws Exception {
        final String[] expectedAlertsFF = {};
        testOnreadystatechange_sync(BrowserVersion.FIREFOX_2, expectedAlertsFF);
        final String[] expectedAlertsIE = {"1", "2", "3", "4"};
        testOnreadystatechange_sync(BrowserVersion.INTERNET_EXPLORER_7_0, expectedAlertsIE);
    }

    private void testOnreadystatechange_sync(final BrowserVersion browserVersion, final String[] expectedAlerts)
        throws Exception {
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

        final WebClient client = new WebClient(browserVersion);
        final List<String> collectedAlerts = new ArrayList<String>();
        client.setAlertHandler(new CollectingAlertHandler(collectedAlerts));
        final MockWebConnection webConnection = new MockWebConnection(client);
        webConnection.setResponse(URL_FIRST, html);
        webConnection.setResponse(URL_SECOND, xml, "text/xml");
        client.setWebConnection(webConnection);
        client.getPage(URL_FIRST);

        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception If the test fails.
     */
    @Test
    public void testResponseXML_getElementById() throws Exception {
        if (notYetImplemented()) {
            return;
        }
        final String[] expectedAlertsIE = {"[object]", "[object]", "[object]"};
        testResponseXML_getElementById(BrowserVersion.INTERNET_EXPLORER_7_0, expectedAlertsIE);
        final String[] expectedAlertsFF =
        {"[object Element]", "[object Element]", "[object HTMLBodyElement]",
            "[object HTMLSpanElement]", "[object XMLDocument]"};
        testResponseXML_getElementById(BrowserVersion.FIREFOX_2, expectedAlertsFF);
    }

    private void testResponseXML_getElementById(final BrowserVersion browserVersion, final String[] expectedAlerts)
        throws Exception {
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

        final WebClient client = new WebClient(browserVersion);
        final List<String> collectedAlerts = new ArrayList<String>();
        client.setAlertHandler(new CollectingAlertHandler(collectedAlerts));
        final MockWebConnection webConnection = new MockWebConnection(client);
        webConnection.setResponse(URL_FIRST, html);
        webConnection.setResponse(URL_SECOND, xml, "text/xml");
        client.setWebConnection(webConnection);
        client.getPage(URL_FIRST);

        assertEquals(expectedAlerts, collectedAlerts);
    }
}
