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
package com.gargoylesoftware.htmlunit;

import java.io.IOException;
import java.io.Writer;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.Servlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.httpclient.HttpState;
import org.junit.Test;

import com.gargoylesoftware.htmlunit.html.HtmlButtonInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.Cookie;

/**
 * Unit tests for {@link CookieManager}.
 *
 * @version $Revision$
 * @author Daniel Gredler
 * @author Ahmed Ashour
 */
public class CookieManagerTest extends WebServerTestCase {

    /**
     * Verifies the basic cookie manager behavior.
     * @throws Exception if an error occurs
     */
    @Test
    public void basicBehavior() throws Exception {
        // Create a new cookie manager.
        final CookieManager mgr = new CookieManager();
        assertTrue(mgr.isCookiesEnabled());
        assertTrue(mgr.getCookies().isEmpty());

        // Add a cookie to the manager.
        final Cookie cookie = new Cookie("a", "b");
        mgr.addCookie(cookie);
        assertFalse(mgr.getCookies().isEmpty());

        // Update an HTTP state.
        final HttpState state = new HttpState();
        mgr.updateState(state);
        assertEquals(1, state.getCookies().length);

        // Remove the cookie from the manager.
        mgr.removeCookie(cookie);
        assertTrue(mgr.getCookies().isEmpty());

        // Update an HTTP state after removing the cookie.
        mgr.updateState(state);
        assertEquals(0, state.getCookies().length);

        // Add the cookie back to the manager.
        mgr.addCookie(cookie);
        assertFalse(mgr.getCookies().isEmpty());

        // Update an HTTP state after adding the cookie back to the manager.
        mgr.updateState(state);
        assertEquals(1, state.getCookies().length);

        // Clear all cookies from the manager.
        mgr.clearCookies();
        assertTrue(mgr.getCookies().isEmpty());

        // Update an HTTP state after clearing all cookies from the manager.
        mgr.updateState(state);
        assertEquals(0, state.getCookies().length);

        // Disable cookies.
        mgr.setCookiesEnabled(false);
        assertFalse(mgr.isCookiesEnabled());

        // Add a cookie after disabling cookies.
        mgr.addCookie(cookie);
        assertFalse(mgr.getCookies().isEmpty());

        // Update an HTTP state after adding a cookie while cookies are disabled.
        mgr.updateState(state);
        assertEquals(0, state.getCookies().length);

        // Enable cookies again.
        mgr.setCookiesEnabled(true);
        assertTrue(mgr.isCookiesEnabled());

        // Update an HTTP state after enabling cookies again.
        mgr.updateState(state);
        assertEquals(1, state.getCookies().length);

        // Update the manager with a new state.
        final Cookie cookie2 = new Cookie("x", "y");
        final HttpState state2 = new HttpState();
        state2.addCookie(cookie2.toHttpClient());
        mgr.updateFromState(state2);
        assertEquals(1, mgr.getCookies().size());
        assertEquals(cookie2, mgr.getCookies().iterator().next());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void comma() throws Exception {
        final Map<String, Class< ? extends Servlet>> servlets = new HashMap<String, Class< ? extends Servlet>>();
        servlets.put("/test", CommaCookieServlet.class);
        startWebServer("./", null, servlets);

        final String[] expectedAlerts = {"my_key=\"Hello, big, big, world\"; another_key=Hi"};
        final WebClient client = new WebClient();
        final List<String> collectedAlerts = new ArrayList<String>();
        client.setAlertHandler(new CollectingAlertHandler(collectedAlerts));
        client.getPage("http://localhost:" + PORT + "/test");
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * Servlet for {@link #comma()}.
     */
    public static class CommaCookieServlet extends HttpServlet {

        private static final long serialVersionUID = 4094440276952531020L;

        /**
         * {@inheritDoc}
         */
        @Override
        protected void doGet(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
            response.setContentType("text/html");
            response.addCookie(new javax.servlet.http.Cookie("my_key", "Hello, big, big, world"));
            response.addCookie(new javax.servlet.http.Cookie("another_key", "Hi"));
            final Writer writer = response.getWriter();
            writer.write("<html><head><script>function test() {alert(document.cookie)}</script>"
                + "<body onload='test()'></body></html>");
            writer.close();
        }
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void emptyCookie() throws Exception {
        final Map<String, Class< ? extends Servlet>> servlets = new HashMap<String, Class< ? extends Servlet>>();
        servlets.put("/test", EmptyCookieServlet.class);
        startWebServer("./", null, servlets);

        final String[] expectedAlerts = {"key1=; key2="};
        final WebClient client = new WebClient();
        final List<String> collectedAlerts = new ArrayList<String>();
        client.setAlertHandler(new CollectingAlertHandler(collectedAlerts));
        client.getPage("http://localhost:" + PORT + "/test");
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * Servlet for {@link #emptyCookie()}.
     */
    public static class EmptyCookieServlet extends HttpServlet {

        private static final long serialVersionUID = 8871931124128513797L;

        /**
         * {@inheritDoc}
         */
        @Override
        protected void doGet(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
            response.setContentType("text/html");
            response.addCookie(new javax.servlet.http.Cookie("key1", ""));
            response.addCookie(new javax.servlet.http.Cookie("key2", null));
            final Writer writer = response.getWriter();
            writer.write("<html><head><script>function test() {alert(document.cookie)}</script>"
                + "<body onload='test()'></body></html>");
            writer.close();
        }
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void serverModifiesCookieValue() throws Exception {
        final Map<String, Class< ? extends Servlet>> servlets = new HashMap<String, Class< ? extends Servlet>>();
        servlets.put("/test", SimpleCookieServlet.class);
        startWebServer("./", null, servlets);

        final String[] expectedAlerts = {"nbCalls=1", "nbCalls=2"};
        final WebClient client = new WebClient();
        final List<String> collectedAlerts = new ArrayList<String>();
        client.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        // get the page a first time
        client.getPage("http://localhost:" + PORT + "/test");

        // and a second one
        client.getPage("http://localhost:" + PORT + "/test");
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * Servlet for {@link #serverModifiesCookieValue()}.
     */
    public static class SimpleCookieServlet extends HttpServlet {
        private static final long serialVersionUID = -2581456646146725479L;
        private int nbCalls_ = 0;

        /**
         * {@inheritDoc}
         */
        @Override
        protected void doGet(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
            response.setContentType("text/html");
            response.addCookie(new javax.servlet.http.Cookie("nbCalls", String.valueOf(++nbCalls_)));
            final Writer writer = response.getWriter();
            writer.write("<html><head><script>function test() {alert(document.cookie)}</script>"
                + "<body onload='test()'></body></html>");
            writer.close();
        }
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void resettingCookie() throws Exception {
        final String html
            = "<html><head><title>foo</title>"
            + "<script>\n"
            + "  function createCookie(name, value, days, path) {\n"
            + "    if (days) {\n"
            + "      var date = new Date();\n"
            + "      date.setTime(date.getTime() + (days * 24 * 60 * 60 * 1000));\n"
            + "      var expires = '; expires=' + date.toGMTString();\n"
            + "    }\n"
            + "    else\n"
            + "      var expires = '';\n"
            + "    document.cookie = name + '=' + value + expires + '; path=' + path;\n"
            + "  }\n"
            + "\n"
            + "  function readCookie(name) {\n"
            + "    var nameEQ = name + '=';\n"
            + "    var ca = document.cookie.split(';');\n"
            + "    for(var i = 0; i < ca.length; i++) {\n"
            + "      var c = ca[i];\n"
            + "      while (c.charAt(0) == ' ')\n"
            + "        c = c.substring(1, c.length);\n"
            + "      if (c.indexOf(nameEQ) == 0)\n"
            + "        return c.substring(nameEQ.length, c.length);\n"
            + "    }\n"
            + "    return null;\n"
            + "  }\n"
            + "</script></head><body>\n"
            + "  <input id='button1' type='button' "
            + "onclick=\"createCookie('button','button1',1,'/'); alert('cookie:' + readCookie('button'));\" "
            + "value='Button 1'>\n"
            + "  <input id='button2' type='button' "
            + "onclick=\"createCookie('button','button2',1,'/'); alert('cookie:' + readCookie('button'));\" "
            + "value='Button 2'>\n"
            + "</form></body></html>";

        final String[] expectedAlerts = {"cookie:button1", "cookie:button2"};
        final List<String> collectedAlerts = new ArrayList<String>();
        final HtmlPage page = loadPage(html, collectedAlerts);
        page.<HtmlButtonInput>getHtmlElementById("button1").click();
        page.<HtmlButtonInput>getHtmlElementById("button2").click();
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void cookie2() throws Exception {
        final Map<String, Class< ? extends Servlet>> servlets = new HashMap<String, Class< ? extends Servlet>>();
        servlets.put("/test", Cookie2Servlet.class);
        startWebServer("./", null, servlets);

        final String[] expectedAlerts = {"first=1", "first=1; second=2"};
        final WebClient client = new WebClient();
        final List<String> collectedAlerts = new ArrayList<String>();
        client.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        // get the page a first time
        client.getPage("http://localhost:" + PORT + "/test");

        // and a second one
        client.getPage("http://localhost:" + PORT + "/test");
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * Servlet for {@link #cookie2()}.
     */
    public static class Cookie2Servlet extends HttpServlet {
        private static final long serialVersionUID = -5384006255925285388L;
        private int nbCalls_ = 0;

        /**
         * {@inheritDoc}
         */
        @Override
        protected void doGet(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
            response.setContentType("text/html");
            if (nbCalls_++ == 0) {
                response.addCookie(new javax.servlet.http.Cookie("first", "1"));
            }
            else {
                response.addCookie(new javax.servlet.http.Cookie("second", "2"));
            }
            final Writer writer = response.getWriter();
            writer.write("<html><head><script>function test() {alert(document.cookie)}</script>"
                + "<body onload='test()'></body></html>");
            writer.close();
        }
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void cookie_nullValue() throws Exception {
        final WebClient webClient = new WebClient();
        final MockWebConnection webConnection = new MockWebConnection();

        final String html
            = "<html><head><title>First</title><script>\n"
            + "function doTest() {\n"
            + "    alert(document.cookie);\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "</body></html>";

        final URL url = URL_FIRST;
        webConnection.setResponse(url, html);
        webClient.setWebConnection(webConnection);

        final CookieManager mgr = webClient.getCookieManager();
        mgr.addCookie(new Cookie(URL_FIRST.getHost(), "my_key", null, "/", null, false));

        final List<String> collectedAlerts = new ArrayList<String>();
        webClient.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        webClient.getPage(URL_FIRST);

        final String[] expectedAlerts = {"my_key="};
        assertEquals(expectedAlerts, collectedAlerts);
    }
}
