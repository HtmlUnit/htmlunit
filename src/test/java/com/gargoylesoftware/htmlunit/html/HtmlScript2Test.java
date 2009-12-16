/*
 * Copyright (c) 2002-2009 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.html;

import static org.apache.commons.httpclient.HttpStatus.SC_NO_CONTENT;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.CollectingAlertHandler;
import com.gargoylesoftware.htmlunit.MockWebConnection;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebTestCase;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.util.NameValuePair;

/**
 * Tests for {@link HtmlScript}, but with BrowserRunner.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class HtmlScript2Test extends WebTestCase {

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts("myValue")
    public void insertBefore() throws Exception {
        final String html
            = "<html><head><title>Page A</title>"
            + "<script>\n"
            + "  function test() {\n"
            + "    var script = document.createElement('script');\n"
            + "    script.text = \"foo = 'myValue';\";\n"
            + "    document.body.insertBefore(script, document.body.firstChild);\n"
            + "    alert(foo);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'></body>\n"
            + "</html>";

        loadPageWithAlerts(html);
    }

    /**
     * Tests the 'Referer' HTTP header.
     * @throws Exception on test failure
     */
    @Test
    public void refererHeader() throws Exception {
        final String firstContent
            = "<html><head><title>Page A</title></head>\n"
            + "<body><script src='" + URL_SECOND + "' id='link'/></body>\n"
            + "</html>";

        final String secondContent = "alert('test')";

        final WebClient client = getWebClient();
        final MockWebConnection conn = new MockWebConnection();
        conn.setResponse(URL_FIRST, firstContent);
        conn.setResponse(URL_SECOND, secondContent);
        client.setWebConnection(conn);
        client.getPage(URL_FIRST);

        final Map<String, String> lastAdditionalHeaders = conn.getLastAdditionalHeaders();
        assertEquals(URL_FIRST.toString(), lastAdditionalHeaders.get("Referer"));
    }

    /**
     * Verifies that cloned script nodes do not reload or re-execute their content (bug 1954869).
     * @throws Exception if an error occurs
     */
    @Test
    public void testScriptCloneDoesNotReloadScript() throws Exception {
        final String html = "<html><body><script src='" + URL_SECOND + "'></script></body></html>";
        final String js = "alert('loaded')";

        final WebClient client = getWebClient();

        final MockWebConnection conn = new MockWebConnection();
        conn.setResponse(URL_FIRST, html);
        conn.setResponse(URL_SECOND, js, "text/javascript");
        client.setWebConnection(conn);

        final List<String> actual = new ArrayList<String>();
        client.setAlertHandler(new CollectingAlertHandler(actual));

        final HtmlPage page = client.getPage(URL_FIRST);
        assertEquals(2, conn.getRequestCount());

        page.cloneNode(true);
        assertEquals(2, conn.getRequestCount());

        final String[] expected = {"loaded"};
        assertEquals(expected, actual);
    }

    /**
     * Verifies that we're lenient about whitespace before and after URLs in the "src" attribute.
     * @throws Exception if an error occurs
     */
    @Test
    public void testWhitespaceInSrc() throws Exception {
        final String html = "<html><head><script src=' " + URL_SECOND + " '></script></head><body>abc</body></html>";
        final String js = "alert('ok')";

        final WebClient client = getWebClient();

        final MockWebConnection webConnection = new MockWebConnection();
        webConnection.setResponse(URL_FIRST, html);
        webConnection.setResponse(URL_SECOND, js);
        client.setWebConnection(webConnection);

        final List<String> collectedAlerts = new ArrayList<String>();
        client.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        client.getPage(URL_FIRST);
        final String[] expectedAlerts = {"ok"};
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * Verifies that we're lenient about empty "src" attributes.
     * @throws Exception if an error occurs
     */
    @Test
    public void testEmptySrc() throws Exception {
        final String html1 = "<html><head><script src=''></script></head><body>abc</body></html>";
        final String html2 = "<html><head><script src='  '></script></head><body>abc</body></html>";

        final WebClient client = getWebClient();
        final MockWebConnection webConnection = new MockWebConnection();
        webConnection.setResponse(URL_FIRST, html1);
        webConnection.setResponse(URL_SECOND, html2);
        client.setWebConnection(webConnection);

        client.getPage(URL_FIRST);
        assertEquals(1, webConnection.getRequestCount());

        client.getPage(URL_SECOND);
        assertEquals(2, webConnection.getRequestCount());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void testAsXml() throws Exception {
        final String html
            = "<html><head><title>foo</title></head><body>\n"
            + "<script id='script1'>\n"
            + "    alert('hello');\n"
            + "</script></body></html>";

        final String[] expectedAlerts = {"hello"};
        final List<String> collectedAlerts = new ArrayList<String>();
        final HtmlPage page = loadPage(getBrowserVersion(), html, collectedAlerts);
        assertEquals(expectedAlerts, collectedAlerts);

        // asXml() should be reusable
        final String xml = page.asXml();
        collectedAlerts.clear();
        loadPage(xml, collectedAlerts);
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * Verifies that setting a script's <tt>src</tt> attribute behaves correctly.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = { "1", "2", "3" }, IE = { "1", "2", "3", "4", "5" })
    public void testSettingSrcAttribute() throws Exception {
        final String html =
            "<html>\n"
            + "  <head>\n"
            + "    <title>Test</title>\n"
            + "    <script id='a'></script>\n"
            + "    <script id='b'>alert('1');</script>\n"
            + "    <script id='c' src='script2.js'></script>\n"
            + "    <script>\n"
            + "      function test() {\n"
            + "        document.getElementById('a').src = 'script3.js';\n"
            + "        document.getElementById('b').src = 'script4.js';\n"
            + "        document.getElementById('c').src = 'script5.js';\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='test()'>\n"
            + "      test\n"
            + "  </body>\n"
            + "</html>";

        final List<String> actual = new ArrayList<String>();

        final WebClient client = getWebClient();
        client.setAlertHandler(new CollectingAlertHandler(actual));

        final MockWebConnection webConnection = new MockWebConnection();
        webConnection.setDefaultResponse(html);
        webConnection.setResponse(new URL("http://abc/script2.js"), "alert(2);");
        webConnection.setResponse(new URL("http://abc/script3.js"), "alert(3);");
        webConnection.setResponse(new URL("http://abc/script4.js"), "alert(4);");
        webConnection.setResponse(new URL("http://abc/script5.js"), "alert(5);");
        client.setWebConnection(webConnection);

        client.getPage("http://abc/");
        assertEquals(getExpectedAlerts(), actual);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE = "[object]", FF = "[object HTMLScriptElement]")
    public void testSimpleScriptable() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    alert(document.getElementById('myId'));\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "  <script id='myId'></script>\n"
            + "</body></html>";

        final HtmlPage page = loadPageWithAlerts(html);
        assertTrue(HtmlScript.class.isInstance(page.getHtmlElementById("myId")));
    }

    /**
     * Verifies that 204 (No Content) responses for script resources are handled gracefully.
     * @throws Exception on test failure
     * @see <a href="https://sourceforge.net/tracker/?func=detail&atid=448266&aid=2815903&group_id=47038">2815903</a>
     */
    @Test
    public void noContent() throws Exception {
        final String html = "<html><body><script src='" + URL_SECOND + "'/></body></html>";
        final WebClient client = getWebClient();
        final MockWebConnection conn = new MockWebConnection();
        conn.setResponse(URL_FIRST, html);
        final ArrayList<NameValuePair> headers = new ArrayList<NameValuePair>();
        conn.setResponse(URL_SECOND, (String) null, SC_NO_CONTENT, "No Content", "text/javascript", headers);
        client.setWebConnection(conn);
        client.getPage(URL_FIRST);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts(FF = "z")
    public void addEventListener_load() throws Exception {
        final String html
            = "<html><head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var s1 = document.createElement('script');\n"
            + "    s1.text = 'var foo';\n"
            + "    if(s1.addEventListener) s1.addEventListener('load', function(){alert('x')}, false);\n"
            + "    document.body.insertBefore(s1, document.body.firstChild);\n"
            + "    \n"
            + "    var s2 = document.createElement('script');\n"
            + "    s2.src = '//:';\n"
            + "    if(s2.addEventListener) s2.addEventListener('load', function(){alert('y')}, false);\n"
            + "    document.body.insertBefore(s2, document.body.firstChild);\n"
            + "    \n"
            + "    var s3 = document.createElement('script');\n"
            + "    s3.src = '" + URL_SECOND + "';\n"
            + "    if(s3.addEventListener) s3.addEventListener('load', function(){alert('z')}, false);\n"
            + "    document.body.insertBefore(s3, document.body.firstChild);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'></body>\n"
            + "</html>";
        final WebClient client = getWebClient();
        final MockWebConnection conn = new MockWebConnection();
        conn.setResponse(URL_FIRST, html);
        conn.setResponse(URL_SECOND, "", "text/javascript");
        client.setWebConnection(conn);
        final List<String> actual = new ArrayList<String>();
        client.setAlertHandler(new CollectingAlertHandler(actual));
        client.getPage(URL_FIRST);
        assertEquals(getExpectedAlerts(), actual);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts(FF = "f")
    public void addEventListener_error_clientThrows() throws Exception {
        addEventListener_error(true);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts(FF = "f")
    public void addEventListener_error_clientDoesNotThrow() throws Exception {
        addEventListener_error(false);
    }

    private void addEventListener_error(final boolean throwOnFailingStatusCode) throws Exception {
        final URL fourOhFour = new URL(URL_FIRST, "/404");
        final String html
            = "<html><head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var s1 = document.createElement('script');\n"
            + "    s1.text = 'var foo';\n"
            + "    if(s1.addEventListener) s1.addEventListener('error', function(){alert('a')}, false);\n"
            + "    document.body.insertBefore(s1, document.body.firstChild);\n"
            + "    \n"
            + "    var s2 = document.createElement('script');\n"
            + "    s2.text = 'varrrr foo';\n"
            + "    if(s2.addEventListener) s2.addEventListener('error', function(){alert('b')}, false);\n"
            + "    document.body.insertBefore(s2, document.body.firstChild);\n"
            + "    \n"
            + "    var s3 = document.createElement('script');\n"
            + "    s3.src = '//:';\n"
            + "    if(s3.addEventListener) s3.addEventListener('error', function(){alert('c')}, false);\n"
            + "    document.body.insertBefore(s3, document.body.firstChild);\n"
            + "    \n"
            + "    var s4 = document.createElement('script');\n"
            + "    s4.src = '" + URL_SECOND + "';\n"
            + "    if(s4.addEventListener) s4.addEventListener('error', function(){alert('d')}, false);\n"
            + "    document.body.insertBefore(s4, document.body.firstChild);\n"
            + "    \n"
            + "    var s5 = document.createElement('script');\n"
            + "    s5.src = '" + URL_THIRD + "';\n"
            + "    if(s5.addEventListener) s5.addEventListener('error', function(){alert('e')}, false);\n"
            + "    document.body.insertBefore(s5, document.body.firstChild);\n"
            + "    \n"
            + "    var s6 = document.createElement('script');\n"
            + "    s6.src = '" + fourOhFour + "';\n"
            + "    if(s6.addEventListener) s6.addEventListener('error', function(){alert('f')}, false);\n"
            + "    document.body.insertBefore(s6, document.body.firstChild);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'></body>\n"
            + "</html>";
        final WebClient client = getWebClient();
        client.setThrowExceptionOnFailingStatusCode(throwOnFailingStatusCode);
        final MockWebConnection conn = new MockWebConnection();
        conn.setResponse(URL_FIRST, html);
        conn.setResponse(URL_SECOND, "var foo;", "text/javascript");
        conn.setResponse(URL_THIRD, "varrrr foo;", "text/javascript");
        conn.setResponse(fourOhFour, "", 404, "Missing", "text/javascript", new ArrayList< NameValuePair >());
        client.setWebConnection(conn);
        final List<String> actual = new ArrayList<String>();
        client.setAlertHandler(new CollectingAlertHandler(actual));
        client.setThrowExceptionOnScriptError(false);
        client.getPage(URL_FIRST);
        assertEquals(getExpectedAlerts(), actual);
    }

    /**
     * @exception Exception If the test fails
     */
    @Test
    @Alerts("Hello")
    public void type_case_sensitivity() throws Exception {
        final String html = "<html>\n"
            + "<body>\n"
            + "  <script type='text/JavaScript'>\n"
            + "    alert('Hello');\n"
            + "  </script>\n"
            + "</body></html>";

        loadPageWithAlerts(html);
    }

    /**
     * @exception Exception If the test fails
     */
    @Test
    @Alerts({ "1", "5", "7" })
    public void type_language() throws Exception {
        final String html = "<html>\n"
            + "<body>\n"
            + "  <script>\n"
            + "    alert('1');\n"
            + "  </script>\n"
            + "  <script language='anything'>\n"
            + "    alert('2');\n"
            + "  </script>\n"
            + "  <script type='anything'>\n"
            + "    alert('3');\n"
            + "  </script>\n"
            + "  <script language='anything' type='anything'>\n"
            + "    alert('4');\n"
            + "  </script>\n"
            + "  <script language='anything' type='text/javascript'>\n"
            + "    alert('5');\n"
            + "  </script>\n"
            + "  <script language='javascript' type='anything'>\n"
            + "    alert('6');\n"
            + "  </script>\n"
            + "  <script language='javascript'>\n"
            + "    alert('7');\n"
            + "  </script>\n"
            + "</body></html>";

        loadPageWithAlerts(html);
    }
}
