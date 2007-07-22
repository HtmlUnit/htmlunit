/*
 * Copyright (c) 2002-2007 Gargoyle Software Inc. All rights reserved.
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

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.CollectingAlertHandler;
import com.gargoylesoftware.htmlunit.MockWebConnection;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequestSettings;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.WebTestCase;
import com.gargoylesoftware.htmlunit.WebWindow;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * Tests for {@link XMLHttpRequest}.
 *
 * @version $Revision$
 * @author Daniel Gredler
 * @author Marc Guillemot
 */
public class XMLHttpRequestTest extends WebTestCase {

    private static final String UNINITIALIZED = String.valueOf( XMLHttpRequest.STATE_UNINITIALIZED );
    private static final String LOADING = String.valueOf( XMLHttpRequest.STATE_LOADING );
    private static final String LOADED = String.valueOf( XMLHttpRequest.STATE_LOADED );
    private static final String INTERACTIVE = String.valueOf( XMLHttpRequest.STATE_INTERACTIVE );
    private static final String COMPLETED = String.valueOf( XMLHttpRequest.STATE_COMPLETED );

    /**
     * Creates a new test instance.
     * @param name The name of the new test instance.
     */
    public XMLHttpRequestTest( final String name ) {
        super( name );
    }

    /**
     * Tests synchronous use of XMLHttpRequest.
     * @throws Exception If the test fails.
     */
    public void testSyncUse() throws Exception {
        testSyncUse(BrowserVersion.FIREFOX_2);
        testSyncUse(BrowserVersion.INTERNET_EXPLORER_6_0);
    }

    /**
     * Tests Mozilla and IE style object creation.
     * @throws Exception If the test fails.
     */
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
            + "          new ActiveXObject('Microsoft.XMLHTTP');"
            + "          alert('activeX created');"
            + "        }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body></body>\n"
            + "</html>";

        createTestPageForRealBrowserIfNeeded(html, expected);

        final List collectedAlerts = new ArrayList();
        loadPage(browser, html, collectedAlerts);

        assertEquals( expected, collectedAlerts );
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
            + "        request.open('GET', '" + URL_SECOND.toExternalForm() + "', false);\n"
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
        final List collectedAlerts = new ArrayList();
        client.setAlertHandler( new CollectingAlertHandler( collectedAlerts ) );
        final MockWebConnection webConnection = new MockWebConnection( client );
        webConnection.setResponse(URL_FIRST, html);
        webConnection.setResponse(URL_SECOND, xml, "text/xml");
        client.setWebConnection( webConnection );
        client.getPage( URL_FIRST );

        final String[] alerts = {UNINITIALIZED, LOADING, COMPLETED, xml};
        assertEquals( alerts, collectedAlerts );
    }

    /**
     * Tests asynchronous use of XMLHttpRequest, using Mozilla style object creation.
     * @throws Exception If the test fails.
     */
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
            + "        request.open('GET', '" + URL_SECOND.toExternalForm() + "', true);\n"
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
        final List collectedAlerts = Collections.synchronizedList( new ArrayList() );
        client.setAlertHandler( new CollectingAlertHandler( collectedAlerts ) );
        final MockWebConnection webConnection = new MockWebConnection( client );
        webConnection.setResponse(URL_FIRST, html);
        webConnection.setResponse(URL_SECOND, xml, "text/xml");
        client.setWebConnection( webConnection );
        final Page page = client.getPage( URL_FIRST );

        final String[] alerts = { UNINITIALIZED,
            LOADING, LOADING, LOADED, INTERACTIVE, COMPLETED, xml };

        assertTrue("thread failed to stop in 1 second", page.getEnclosingWindow().getThreadManager().joinAll(1000));
        assertEquals( alerts, collectedAlerts );
    }

    /**
     * Regression test for bug 1209692
     * http://sourceforge.net/tracker/index.php?func=detail&aid=1209692&group_id=47038&atid=448266
     * @throws Exception If the test fails.
     */
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
        final List collectedAlerts = new ArrayList();
        client.setAlertHandler( new CollectingAlertHandler( collectedAlerts ) );
        final MockWebConnection webConnection = new MockWebConnection( client );
        webConnection.setResponse(URL_FIRST, html);
        final URL urlPage2 = new URL(URL_FIRST.toExternalForm() + "/foo.xml");
        webConnection.setResponse(urlPage2, xml, "text/xml");
        client.setWebConnection( webConnection );
        client.getPage(URL_FIRST);

        final String[] alerts = {COMPLETED, xml};
        assertEquals( alerts, collectedAlerts );
    }

    /**
     * @throws Exception if the test fails.
     */
    public void testResponseText_NotXml() throws Exception {
        final String html = "<html><head>"
            + "<script>"
            + "function test()"
            + "{"
            + "  var request;"
            + "  if (window.XMLHttpRequest)"
            + "    request = new XMLHttpRequest();"
            + "  else if (window.ActiveXObject)"
            + "    request = new ActiveXObject('Microsoft.XMLHTTP');"
            + "  request.open('GET', 'foo.txt', false);"
            + "  request.send('');"
            + "  alert(request.responseText);"
            + "}"
            + "</script>"
            + "</head>"
            + "<body onload='test()'></body></html>";

        final WebClient client = new WebClient();
        final List collectedAlerts = new ArrayList();
        client.setAlertHandler( new CollectingAlertHandler( collectedAlerts ) );
        final MockWebConnection webConnection = new MockWebConnection( client );
        webConnection.setResponse(URL_FIRST, html);
        final URL urlPage2 = new URL(URL_FIRST.toExternalForm() + "/foo.txt");
        webConnection.setResponse(urlPage2, "bla bla", "text/plain");
        client.setWebConnection( webConnection );
        client.getPage(URL_FIRST);

        final String[] alerts = {"bla bla"};
        assertEquals( alerts, collectedAlerts );
    }

    /**
     * Test access to the XML DOM
     * @throws Exception if the test fails.
     */
    public void testResponseXML() throws Exception {

        final String html = "<html><head>"
            + "<script>"
            + "function test()"
            + "{"
            + "  var request;"
            + "  if (window.XMLHttpRequest)"
            + "    request = new XMLHttpRequest();"
            + "  else if (window.ActiveXObject)"
            + "    request = new ActiveXObject('Microsoft.XMLHTTP');"
            + "  request.open('GET', 'foo.xml', false);"
            + "  request.send('');"
            + "  var childNodes = request.responseXML.childNodes;"
            + "  alert(childNodes.length);"
            + "  var rootNode = childNodes[0];"
            + "  alert(rootNode.nodeName);"
            + "  alert(rootNode.attributes[0].nodeName);"
            + "  alert(rootNode.attributes[0].nodeValue);"
            + "  alert(rootNode.attributes['someAttr'] == rootNode.attributes[0]);"
            + "  alert(rootNode.firstChild.nodeName);"
            + "  alert(rootNode.firstChild.childNodes.length);"
            + "  alert(request.responseXML.getElementsByTagName('fi').item(0).attributes[0].nodeValue);"
            + "}"
            + "</script>"
            + "</head>"
            + "<body onload='test()'></body></html>";

        final WebClient client = new WebClient();
        final List collectedAlerts = new ArrayList();
        client.setAlertHandler( new CollectingAlertHandler( collectedAlerts ) );
        final MockWebConnection webConnection = new MockWebConnection( client );
        webConnection.setResponse(URL_FIRST, html);
        final URL urlPage2 = new URL(URL_FIRST.toExternalForm() + "/foo.xml");
        webConnection.setResponse(urlPage2, "<bla someAttr='someValue'><foo><fi id='fi1'/><fi/></foo></bla>",
                "text/xml");
        client.setWebConnection( webConnection );
        client.getPage(URL_FIRST);

        final String[] alerts = { "1", "bla", "someAttr", "someValue", "true", "foo", "2", "fi1" };
        assertEquals( alerts, collectedAlerts );
    }

    /**
     * @throws Exception if the test fails.
     */
    public void testSendNull() throws Exception {
        final String html = "<html><head>"
            + "<script>"
            + "function test()"
            + "{"
            + "  var request;"
            + "  if (window.XMLHttpRequest)"
            + "    request = new XMLHttpRequest();"
            + "  else if (window.ActiveXObject)"
            + "    request = new ActiveXObject('Microsoft.XMLHTTP');"
            + "  request.open('GET', 'foo.txt', false);"
            + "  request.send(null);"
            + "}"
            + "</script>"
            + "</head>"
            + "<body onload='test()'></body></html>";

        final WebClient client = new WebClient();
        final MockWebConnection webConnection = new MockWebConnection( client );
        webConnection.setResponse(URL_FIRST, html);
        webConnection.setDefaultResponse("");
        client.setWebConnection( webConnection );
        client.getPage(URL_FIRST);
    }

    /**
     * Test calls to send() without any argument
     * @throws Exception if the test fails.
     */
    public void testSendNoArg() throws Exception {
        testSendNoArg(BrowserVersion.INTERNET_EXPLORER_6_0);
        // Mozilla fails if no arg is provided.
        try {
            testSendNoArg(BrowserVersion.FIREFOX_2);
            fail("Should have thrown");
        }
        catch (final Exception e) {
            // nothing
            assertTrue(e.getMessage().indexOf("not enough arguments") != -1);
        }

    }

    /**
     * @throws Exception if the test fails.
     */
    private void testSendNoArg(final BrowserVersion browserVersion) throws Exception {
        final String html = "<html><head>"
            + "<script>"
            + "function test()"
            + "{"
            + "  var request;"
            + "  if (window.XMLHttpRequest)"
            + "    request = new XMLHttpRequest();"
            + "  else if (window.ActiveXObject)"
            + "    request = new ActiveXObject('Microsoft.XMLHTTP');"
            + "  request.open('GET', 'foo.txt', false);"
            + "  request.send();"
            + "}"
            + "</script>"
            + "</head>"
            + "<body onload='test()'></body></html>";

        final WebClient client = new WebClient(browserVersion);
        final MockWebConnection webConnection = new MockWebConnection( client );
        webConnection.setResponse(URL_FIRST, html);
        webConnection.setDefaultResponse("");
        client.setWebConnection( webConnection );
        client.getPage(URL_FIRST);
    }

    /**
     * Regression test for bug 1357412.
     * Response received by the XMLHttpRequest should not come in any window
     * @throws Exception if the test fails.
     */
    public void testResponseNotInWindow() throws Exception {
        final String html = "<html><head><title>foo</title>"
            + "<script>"
            + "function test()"
            + "{"
            + "  var request;"
            + "  if (window.XMLHttpRequest)"
            + "    request = new XMLHttpRequest();"
            + "  else if (window.ActiveXObject)"
            + "    request = new ActiveXObject('Microsoft.XMLHTTP');"
            + "  request.open('GET', 'foo.txt', false);"
            + "  request.send();"
            + "}"
            + "</script>"
            + "</head>"
            + "<body onload='test()'></body></html>";

        final WebClient client = new WebClient();
        final MockWebConnection webConnection = new MockWebConnection( client );
        webConnection.setResponse(URL_FIRST, html);
        webConnection.setDefaultResponse("");
        client.setWebConnection( webConnection );
        final HtmlPage page = (HtmlPage) client.getPage(URL_FIRST);
        assertEquals(URL_FIRST, page.getWebResponse().getUrl());
        assertEquals("foo", page.getTitleText());
    }

    /**
     * Test Mozilla's overrideMimeType method
     * @throws Exception if the test fails.
     */
    public void testOverrideMimeType() throws Exception {

        final String html = "<html><head>"
            + "<script>"
            + "function test()"
            + "{"
            + "  var request = new XMLHttpRequest();"
            + "  request.open('GET', 'foo.xml.txt', false);"
            + "  request.send('');"
            + "  alert(request.responseXML == null);"
            + "  request.overrideMimeType('text/xml');"
            + "  request.open('GET', 'foo.xml.txt', false);"
            + "  request.send('');"
            + "  alert(request.responseXML == null);"
            + "}"
            + "</script>"
            + "</head>"
            + "<body onload='test()'></body></html>";

        final WebClient client = new WebClient(BrowserVersion.FIREFOX_2);
        final List collectedAlerts = new ArrayList();
        client.setAlertHandler( new CollectingAlertHandler( collectedAlerts ) );
        final MockWebConnection webConnection = new MockWebConnection( client );
        webConnection.setResponse(URL_FIRST, html);
        final URL urlPage2 = new URL(URL_FIRST.toExternalForm() + "/foo.xml.txt");
        webConnection.setResponse(urlPage2, "<bla someAttr='someValue'><foo><fi id='fi1'/><fi/></foo></bla>",
                "text/plain");
        client.setWebConnection( webConnection );
        client.getPage(URL_FIRST);

        final String[] alerts = { "true", "false" };
        assertEquals( alerts, collectedAlerts );
    }

    /**
     * Regression test for bug 1611097.
     * https://sourceforge.net/tracker/index.php?func=detail&aid=1611097&group_id=47038&atid=448266
     * Caution: the problem appeared with jdk 1.4 but not with jdk 1.5 as String contains a
     * replace(CharSequence, CharSequence) method in this version
     * @throws Exception if the test fails
     */
    public void testReplaceOnTextData() throws Exception {
        testReplaceOnTextData(BrowserVersion.FIREFOX_2);
        testReplaceOnTextData(BrowserVersion.INTERNET_EXPLORER_6_0);
    }

    /**
     * @param browserVersion the browser version to simulate
     * @throws Exception if the test fails
     */
    void testReplaceOnTextData(final BrowserVersion browserVersion) throws Exception {
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
            + "        request.open('GET', '" + URL_SECOND.toExternalForm() + "', false);\n"
            + "        request.send('');\n"
            + "      }\n"
            + "      function onReadyStateChange() {\n"
            + "        if (request.readyState == 4){\n"
            + "          var theXML = request.responseXML;\n"
            + "          var theDoc = theXML.documentElement;\n"
            + "          var theElements = theDoc.getElementsByTagName(\"update\");\n"
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
        final List collectedAlerts = Collections.synchronizedList( new ArrayList() );
        client.setAlertHandler( new CollectingAlertHandler( collectedAlerts ) );
        final MockWebConnection webConnection = new MockWebConnection( client );
        webConnection.setResponse(URL_FIRST, html);
        webConnection.setResponse(URL_SECOND, xml, "text/xml");
        client.setWebConnection( webConnection );
        client.getPage( URL_FIRST );

        final String[] alerts = { "ibcdefg", "xxxxxfg" };

        assertEquals( alerts, collectedAlerts );
    }

    /**
     * @throws Exception If the test fails
     */
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
    public void testNoParallelJSExecutionInPage() throws Exception {

        final String content = "<html><head><script>"
            + "function getXMLHttpRequest() {"
            + " if (window.XMLHttpRequest)"
            + "        return new XMLHttpRequest();"
            + " else if (window.ActiveXObject)"
            + "        return new ActiveXObject('Microsoft.XMLHTTP');"
            + "}"
            + "var j = 0;"
            + "function test()"
            + "{"
            + " req = getXMLHttpRequest();"
            + " req.onreadystatechange = handler;"
            + " req.open('post', 'foo.xml', true);"
            + " req.send('');"
            + " alert('before long loop');"
            + " for (var i = 0; i < 5000; i++) {"
            + "     j = j + 1;"
            + " }"
            + " alert('after long loop');"
            + "}"
            + "function handler()"
            + "{"
            + " if (req.readyState == 4)"
            + " {"
            + "     alert('ready state handler, content loaded: j=' + j);"
            + " }"
            + "}"
            + "</script></head>"
            + "<body onload='test()'></body></html>";

        final WebClient client = new WebClient();
        final List collectedAlerts = Collections.synchronizedList(new ArrayList());
        client.setAlertHandler( new CollectingAlertHandler( collectedAlerts ) );
        final MockWebConnection webConnection = new MockWebConnection( client )
        {
            public WebResponse getResponse(final WebRequestSettings webRequestSettings) throws IOException {
                collectedAlerts.add(webRequestSettings.getURL().toExternalForm());
                return super.getResponse(webRequestSettings);
            }
        };
        webConnection.setResponse(URL_FIRST, content);
        final URL urlPage2 = new URL(URL_FIRST.toExternalForm() + "/foo.xml");
        webConnection.setResponse(urlPage2, "<foo/>", "text/xml");
        client.setWebConnection( webConnection );
        final Page page = client.getPage(URL_FIRST);

        assertTrue("thread failed to stop in 4 seconds", page.getEnclosingWindow().getThreadManager().joinAll(4000));

        final String[] alerts = { URL_FIRST.toExternalForm(), "before long loop", "after long loop",
                urlPage2.toExternalForm(), "ready state handler, content loaded: j=5000" };
        assertEquals( alerts, collectedAlerts );
    }
}
