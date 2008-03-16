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

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.CollectingAlertHandler;
import com.gargoylesoftware.htmlunit.MockWebConnection;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebTestCase;

/**
 * Unit tests for {@link HTMLScriptElement}.
 *
 * @version $Revision$
 * @author Daniel Gredler
 * @author Ahmed Ashour
 */
public class HTMLScriptElementTest extends WebTestCase {

    /**
     * Verifies that the <tt>onreadystatechange</tt> handler is invoked correctly.
     * @throws Exception If an error occurs.
     */
    @Test
    public void onReadyStateChangeHandler() throws Exception {
        final String html = "<html>\n"
            + "  <head>\n"
            + "    <title>test</title>\n"
            + "    <script id='a'>\n"
            + "      var script = document.createElement('script');\n"
            + "      script.id = 'b';\n"
            + "      script.type = 'text/javascript';\n"
            + "      script.onreadystatechange = null;"
            + "      script.onreadystatechange = function() {\n"
            + "        alert(script.id + '=' + script.readyState);\n"
            + "      }\n"
            + "      alert('1');\n"
            + "      script.src = '" + URL_SECOND + "';\n"
            + "      alert('2');\n"
            + "      document.getElementsByTagName('head')[0].appendChild(script);\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body>abc</body>\n"
            + "</html>";

        final String js = "alert('3')";

        final WebClient client = new WebClient();
        final List<String> collectedAlerts = new ArrayList<String>();
        client.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        final MockWebConnection webConnection = new MockWebConnection(client);
        webConnection.setResponse(URL_FIRST, html);
        webConnection.setResponse(URL_SECOND, js, "text/javascript");
        client.setWebConnection(webConnection);

        client.getPage(URL_FIRST);
        final String[] expectedAlerts = {"1", "2", "b=complete", "3" };
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * Test for bug https://sourceforge.net/tracker/?func=detail&atid=448266&aid=1782719&group_id=47038.
     * @throws Exception if the test fails
     */
    @Test
    public void srcWithJavaScriptProtocol_Static() throws Exception {
        srcWithJavaScriptProtocol_Static(BrowserVersion.INTERNET_EXPLORER_6_0, "1");
        srcWithJavaScriptProtocol_Static(BrowserVersion.INTERNET_EXPLORER_7_0);
        srcWithJavaScriptProtocol_Static(BrowserVersion.FIREFOX_2, "1");
    }

    private void srcWithJavaScriptProtocol_Static(final BrowserVersion version, final String... expected) throws Exception {
        final String html = "<html><head><script src='javascript:\"alert(1)\"'></script></head><body></body></html>";
        final List<String> actual = new ArrayList<String>();
        loadPage(version, html, actual);
        assertEquals(expected, actual);
    }

    /**
     * Test for bug https://sourceforge.net/tracker/?func=detail&atid=448266&aid=1782719&group_id=47038.
     * @throws Exception if the test fails
     */
    @Test
    public void srcWithJavaScriptProtocol_Dynamic() throws Exception {
        srcWithJavaScriptProtocol_Dynamic(BrowserVersion.INTERNET_EXPLORER_6_0, "1");
        srcWithJavaScriptProtocol_Dynamic(BrowserVersion.INTERNET_EXPLORER_7_0);
        srcWithJavaScriptProtocol_Dynamic(BrowserVersion.FIREFOX_2, "1");
    }

    private void srcWithJavaScriptProtocol_Dynamic(final BrowserVersion version, final String... expected) throws Exception {
        final String content =
              "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var script=document.createElement('script');\n"
            + "    script.src=\"javascript: 'alert(1)'\";\n"
            + "    document.getElementsByTagName('head')[0].appendChild(script);\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        final List<String> actual = new ArrayList<String>();
        loadPage(version, content, actual);
        assertEquals(expected, actual);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void scriptForEvent() throws Exception {
        // IE accepts it with () or without
        scriptForEvent("onload");
        scriptForEvent("onload()");
    }

    private void scriptForEvent(final String eventName) throws Exception {
        final String content
            = "<html><head><title>foo</title>\n"
            + "<script FOR='window' EVENT='" + eventName + "' LANGUAGE='javascript'>\n"
            + " document.form1.txt.value='hello';\n"
            + " alert(document.form1.txt.value);\n"
            + "</script></head><body>\n"
            + "<form name='form1'><input type=text name='txt'></form></body></html>";
        final List<String> collectedAlerts = new ArrayList<String>();

        final String[] expectedAlerts = {"hello"};
        createTestPageForRealBrowserIfNeeded(content, expectedAlerts);

        loadPage(content, collectedAlerts);
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * Verifies the correct the ordering of script element execution, deferred script element
     * execution, script ready state changes, deferred script ready state changes, and onload
     * handlers.
     *
     * @throws Exception if an error occurs
     */
    @Test
    public void onReadyStateChange_Order() throws Exception {
        onReadyStateChange_Order(BrowserVersion.FIREFOX_2, "3", "4", "2", "5");
        //onReadyStateChange_Order(BrowserVersion.INTERNET_EXPLORER_6_0, "1", "2", "3", "4", "5", "6", "7");
        //onReadyStateChange_Order(BrowserVersion.INTERNET_EXPLORER_7_0, "1", "2", "3", "4", "5", "6", "7");
    }

    private void onReadyStateChange_Order(final BrowserVersion version, final String... expected)
        throws Exception {
        final String html =
              "<html>\n"
            + "  <head>\n"
            + "    <title>test</title>\n"
            + "    <script defer=''>alert('3');</script>\n"
            + "    <script defer='' onreadystatechange='if(this.readyState==\"complete\") alert(\"6\");'>alert('4');</script>\n"
            + "    <script src='//:' onreadystatechange='if(this.readyState==\"complete\") alert(\"1\");'></script>\n"
            + "    <script defer='' src='//:' onreadystatechange='if(this.readyState==\"complete\") alert(\"7\");'></script>\n"
            + "    <script>alert('2')</script>\n"
            + "  </head>\n"
            + "  <body onload='alert(5)'></body>\n"
            + "</html>";
        final List<String> actual = new ArrayList<String>();
        loadPage(version, html, actual);
        assertEquals(expected, actual);
    }

    /**
     * Verifies the correct the ordering of script element execution, deferred script element
     * execution, script ready state changes, deferred script ready state changes, and onload
     * handlers when the document doesn't have an explicit <tt>body</tt> element.
     *
     * @throws Exception if an error occurs
     */
    @Test
    public void onReadyStateChange_Order_NoBody() throws Exception {
        onReadyStateChange_Order_NoBody(BrowserVersion.FIREFOX_2, "3", "4", "2");
        onReadyStateChange_Order_NoBody(BrowserVersion.INTERNET_EXPLORER_6_0, "1", "2", "3", "4", "5", "6");
        onReadyStateChange_Order_NoBody(BrowserVersion.INTERNET_EXPLORER_7_0, "1", "2", "3", "4", "5", "6");
    }

    private void onReadyStateChange_Order_NoBody(final BrowserVersion version, final String... expected)
        throws Exception {
        final String html =
              "<html>\n"
            + "  <head>\n"
            + "    <title>test</title>\n"
            + "    <script defer=''>alert('3');</script>\n"
            + "    <script defer='' onreadystatechange='if(this.readyState==\"complete\") alert(\"5\");'>alert('4');</script>\n"
            + "    <script src='//:' onreadystatechange='if(this.readyState==\"complete\") alert(\"1\");'></script>\n"
            + "    <script defer='' src='//:' onreadystatechange='if(this.readyState==\"complete\") alert(\"6\");'></script>\n"
            + "    <script>alert('2')</script>\n"
            + "  </head>\n"
            + "</html>";
        final List<String> actual = new ArrayList<String>();
        loadPage(version, html, actual);
        assertEquals(expected, actual);
    }

}
