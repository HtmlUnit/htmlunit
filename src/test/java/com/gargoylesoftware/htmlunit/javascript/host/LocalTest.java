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
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 *
 * @version  $Revision$
 * @author  <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 */
public class LocalTest extends WebTestCase {
    /**
     * Create an instance
     * @param name The name of the test
     */
    public LocalTest( final String name ) {
        super(name);
    }
    /**
     * Test the ReadyState.  This should only work in IE.
     * Currently locked out since the browser type of code is not working.
     * 
     * @throws Exception if the test fails
     */
    public void xtestReadyStateNonIE() throws Exception {
        if (true) {
            notImplemented();
            return;
        }
        final WebClient client = new WebClient(BrowserVersion.NETSCAPE_4_7_9);
        final MockWebConnection webConnection = new MockWebConnection(client);
        final String content 
            = "<html><head>\n" 
            + "<script>\n"
            + "function testIt() {\n" 
            + "  alert(document.readyState);\n"
            + "}\n" + "alert(document.readyState);\n" 
            + "</script>\n"
            + "</head>\n" + "<body onLoad='testIt()'></body></html>\n";
        webConnection.setResponse(URL_FIRST, content);
        client.setWebConnection(webConnection);

        final List collectedAlerts = new ArrayList();
        client.setAlertHandler(new CollectingAlertHandler(collectedAlerts));
        client.getPage(URL_FIRST);

        final List expectedAlerts = Arrays.asList(new String[] { "undefined",
            "undefined" });

        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * Test redirecting with javascript during page load.
     * @throws Exception If something goes wrong.
     */
    public void xtestRedirectViaJavaScriptDuringInitialPageLoad() throws Exception {
        final String firstContent = "<html><head><title>First</title><script>"
            + "location.href='http://second'"
            + "</script></head><body></body></html>";
        final String secondContent = "<html><head><title>Second</title></head><body></body></html>";

        final WebClient webClient = new WebClient();

        final MockWebConnection webConnection = new MockWebConnection( webClient );
        webConnection.setResponse(
            URL_FIRST, firstContent, 200, "OK", "text/html", Collections.EMPTY_LIST );
        webConnection.setResponse(
            URL_SECOND, secondContent, 200, "OK", "text/html", Collections.EMPTY_LIST );

        webClient.setWebConnection( webConnection );

        final URL url = URL_FIRST;

        final HtmlPage page = (HtmlPage)webClient.getPage(url);
        assertEquals("Second", page.getTitleText());
    }

    /**
     * 
     * @throws Exception if the test fails
     */
    public void xtestClickHashAnchor() throws Exception {
        final String content 
            = "<html><head><title>HashAnchor</title>"
            + "  <script language='javascript'>"
            + "    function test() {alert('test hash');}"
            + "  </script>"
            + "</head>"
            + "<body>"
            + "  <a onClick='javascript:test();' href='#' name='hash'>Click</a>" 
            + "</body>"
            + "</html>";
        final List expectedAlerts = Arrays.asList(new String[]{"test hash"});

        // first use direct load
        final List loadCollectedAlerts = new ArrayList();
        final HtmlPage loadPage = loadPage(content, loadCollectedAlerts);

        final HtmlAnchor loadHashAnchor = loadPage.getAnchorByName("hash");
        loadHashAnchor.click();
        
        assertEquals(expectedAlerts, loadCollectedAlerts);
    }

    /**
     * Test when <form> inside <table> and before <tr>
     * @throws Exception failure
     */
    public void testBadlyFormedHTML() throws Exception {
        final String content
            = "<html><head><title>first</title>"
            + "<script>"
            + "function test()"
            + "{"
            + "  alert(document.getElementById('myInput').form.id);\n"
            + "}"
            + "</script>"
            + "</head>"
            + "<body onload='test()'>"
            + "<table>"
            + "<form name='myForm' action='foo' id='myForm'>"
            + "<tr><td>"
            + "<input type='text' name='myInput' id='myInput'/>"
            + "</td></tr>"
            + "</form>"
            + "</table>"
            + "</body></html>";

        final List collectedAlerts = new ArrayList();
        final List expectedAlerts = Arrays.asList(new String[]{"myForm"});
        createTestPageForRealBrowserIfNeeded(content, expectedAlerts);

        loadPage(content, collectedAlerts);

        assertEquals( expectedAlerts, collectedAlerts );
    }

}
