/*
 * Copyright (c) 2002, 2005 Gargoyle Software Inc. All rights reserved.
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

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.CollectingAlertHandler;
import com.gargoylesoftware.htmlunit.MockWebConnection;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebTestCase;

/**
 * Tests for {@link XMLHttpRequest}.
 * 
 * @author Daniel Gredler
 * @author Marc Guillemot
 * @version $Revision$
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
        testSyncUse(BrowserVersion.MOZILLA_1_0);
        testSyncUse(BrowserVersion.INTERNET_EXPLORER_6_0);
    }

    /**
     * Tests Mozilla and IE style object creation.
     * @throws Exception If the test fails.
     */
    public void testCreation() throws Exception {
        testCreation(BrowserVersion.MOZILLA_1_0, Arrays.asList(new String[] {"[object XMLHttpRequest]"}));
        testCreation(BrowserVersion.INTERNET_EXPLORER_6_0, Arrays.asList( new String[] {"activeX created"}));
    }

    /**
     * Tests Mozilla style object creation.
     * @throws Exception If the test fails.
     */
    void testCreation(final BrowserVersion browser, final List expected) throws Exception {
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
        webConnection.setResponse( URL_FIRST, html );
        webConnection.setResponse( URL_SECOND, xml, 200, "OK", "text/xml", Collections.EMPTY_LIST );
        client.setWebConnection( webConnection );
        client.getPage( URL_FIRST );

        final List alerts = Arrays.asList( new String[] { UNINITIALIZED, LOADING, COMPLETED, xml } );
        assertEquals( alerts, collectedAlerts );
    }

    /**
     * Tests asynchronous use of XMLHttpRequest, using Mozilla style object creation.
     * @throws Exception If the test fails.
     */
    public void testAsyncUse() throws Exception {
        testAsyncUse(BrowserVersion.MOZILLA_1_0);
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
        webConnection.setResponse( URL_FIRST, html );
        webConnection.setResponse( URL_SECOND, xml, 200, "OK", "text/xml", Collections.EMPTY_LIST );
        client.setWebConnection( webConnection );
        client.getPage( URL_FIRST );

        final String[] s = new String[] { UNINITIALIZED,
            LOADING, LOADING, LOADED, INTERACTIVE, COMPLETED, xml };
        final List alerts = Collections.synchronizedList( Arrays.asList( s ) );

        final int waitTime = 50;
        final int maxTime = 1000;
        for( int time = 0; time < maxTime; time += waitTime ) {
            if( alerts.size() == collectedAlerts.size() ) {
                assertEquals( alerts, collectedAlerts );
                return;
            }
            Thread.sleep( waitTime );
        }
        fail( "Unable to collect expected alerts within " + maxTime + "ms; collected alerts: " + collectedAlerts );
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

        final List alerts = Arrays.asList( new String[] { COMPLETED, xml } );
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

        final List alerts = Arrays.asList( new String[] { "bla bla" } );
        assertEquals( alerts, collectedAlerts );
    }
}
