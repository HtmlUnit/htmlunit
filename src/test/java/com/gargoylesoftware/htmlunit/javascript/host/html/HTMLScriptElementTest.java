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
package com.gargoylesoftware.htmlunit.javascript.host.html;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.CollectingAlertHandler;
import com.gargoylesoftware.htmlunit.MockWebConnection;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebTestCase;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTextArea;

/**
 * Unit tests for {@link HTMLScriptElement}.
 * TODO: check event order with defer in real browser WITHOUT using alert(...) as it impacts ordering.
 * Some expectations seems to be incorrect.
 * @version $Revision$
 * @author Daniel Gredler
 * @author Ahmed Ashour
 * @author Marc Guillemot
 */
public class HTMLScriptElementTest extends WebTestCase {

    /**
     * Verifies that the <tt>onreadystatechange</tt> handler is invoked correctly.
     * @throws Exception if an error occurs
     */
    @Test
    public void onReadyStateChangeHandler() throws Exception {
        //onReadyStateChangeHandler(BrowserVersion.INTERNET_EXPLORER_6_0, "1 2 4 3 b=complete ");
        //IE7 gives "1 2 3 b=complete " if 'Check new version of stored pages' is set to 'Never'
        onReadyStateChangeHandler(BrowserVersion.INTERNET_EXPLORER_7, "1 2 3 b=loading 4 b=loaded ");
        onReadyStateChangeHandler(BrowserVersion.FIREFOX_2, "1 2 3 4 onload ");
        onReadyStateChangeHandler(BrowserVersion.FIREFOX_3, "1 2 3 4 onload ");
    }

    private void onReadyStateChangeHandler(final BrowserVersion browserVersion, final String expectedValue)
        throws Exception {
        final String html = "<html>\n"
            + "  <head>\n"
            + "    <title>test</title>\n"
            + "    <script>\n"
            + "      function test() {\n"
            + "        var script = document.createElement('script');\n"
            + "        script.id = 'b';\n"
            + "        script.type = 'text/javascript';\n"
            + "        script.onreadystatechange = null;\n"
            + "        script.onreadystatechange = function() {\n"
            + "          document.getElementById('myTextarea').value += script.id + '=' + script.readyState + ' ';\n"
            + "        }\n"
            + "        script.onload = function () {\n"
            + "          document.getElementById('myTextarea').value += 'onload ';\n"
            + "        }\n"
            + "        document.getElementById('myTextarea').value += '1 ';\n"
            + "        script.src = '" + URL_SECOND + "';\n"
            + "        document.getElementById('myTextarea').value += '2 ';\n"
            + "        document.getElementsByTagName('head')[0].appendChild(script);\n"
            + "        document.getElementById('myTextarea').value += '3 ';\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='test()'>\n"
            + "    <textarea id='myTextarea' cols='40'></textarea>\n"
            + "  </body></html>";

        final String js = "document.getElementById('myTextarea').value += '4 ';";

        final WebClient client = new WebClient(browserVersion);
        final List<String> collectedAlerts = new ArrayList<String>();
        client.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        final MockWebConnection webConnection = new MockWebConnection();
        webConnection.setResponse(URL_FIRST, html);
        webConnection.setResponse(URL_SECOND, js, "text/javascript");
        client.setWebConnection(webConnection);

        final HtmlPage page = client.getPage(URL_FIRST);
        final HtmlTextArea textArea = page.getHtmlElementById("myTextarea");
        assertEquals(expectedValue, textArea.getText());
    }

    /**
     * Test for bug https://sourceforge.net/tracker/?func=detail&atid=448266&aid=1782719&group_id=47038.
     * @throws Exception if the test fails
     */
    @Test
    public void srcWithJavaScriptProtocol_Static() throws Exception {
        srcWithJavaScriptProtocol_Static(BrowserVersion.INTERNET_EXPLORER_6, "1");
        srcWithJavaScriptProtocol_Static(BrowserVersion.INTERNET_EXPLORER_7);
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
        srcWithJavaScriptProtocol_Dynamic(BrowserVersion.INTERNET_EXPLORER_6, "1");
        srcWithJavaScriptProtocol_Dynamic(BrowserVersion.INTERNET_EXPLORER_7);
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
     * @throws Exception if an error occurs
     */
    @Test
    public void onReadyStateChange_EventAvailable() throws Exception {
        final String html =
              "<html><body><script>\n"
            + "var s = document.createElement('script');\n"
            + "s.src = '//:';\n"
            + "s.onreadystatechange = function() {alert(window.event);};\n"
            + "document.body.appendChild(s);\n"
            + "</script></body></html>";
        final List<String> actual = new ArrayList<String>();
        loadPage(BrowserVersion.INTERNET_EXPLORER_7, html, actual);
        final String[] expected = new String[] {"[object]"};
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
        onReadyStateChange_Order_NoBody(BrowserVersion.INTERNET_EXPLORER_6, "1", "2", "3", "4", "5", "6");
        onReadyStateChange_Order_NoBody(BrowserVersion.INTERNET_EXPLORER_7, "1", "2", "3", "4", "5", "6");
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

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void text() throws Exception {
        text(BrowserVersion.INTERNET_EXPLORER_6);
        text(BrowserVersion.INTERNET_EXPLORER_7);
        text(BrowserVersion.FIREFOX_2);
    }

    private void text(final BrowserVersion browserVersion) throws Exception {
        final String html =
            "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + "      function test() {\n"
            + "        execMe('alert(1)');\n"
            + "      }\n"
            + "      function execMe(text) {\n"
            + "        document.head = document.getElementsByTagName('head')[0];\n"
            + "        var script = document.createElement('script');\n"
            + "        script.text = text;\n"
            + "        document.head.appendChild(script);\n"
            + "        document.head.removeChild(script);\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='test()'>\n"
            + "  </body>\n"
            + "</html>";
        final String[] expectedAlerts = {"1"};
        final List<String> collectedAlerts = new ArrayList<String>();
        loadPage(browserVersion, html, collectedAlerts);
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void onload_after_deferReadStateComplete() throws Exception {
        final String html =
              "<html>\n"
            + "  <head>\n"
            + "    <title>test</title>\n"
            + "    <script onreadystatechange='if(this.readyState==\"complete\") alert(\"defer\");' defer></script>\n"
            + "  </head>\n"
            + "  <body onload='alert(\"onload\")'>\n"
            + "  </body>\n"
            + "</html>";
        final List<String> actual = new ArrayList<String>();
        final String[] expected = {"defer", "onload"};
        loadPage(BrowserVersion.INTERNET_EXPLORER_6, html, actual);
        assertEquals(expected, actual);
    }
}
