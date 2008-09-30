/*
 * Copyright (c) 2002-2008 Gargoyle Software Inc.
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.Servlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.HttpState;
import org.junit.After;
import org.junit.Test;
import org.mortbay.jetty.Server;

/**
 * Unit tests for {@link CookieManager}.
 *
 * @version $Revision$
 * @author Daniel Gredler
 * @author Ahmed Ashour
 */
public class CookieManagerTest extends WebTestCase {

    private Server server_;

    /**
     * Performs post-test deconstruction.
     * @throws Exception if an error occurs
     */
    @After
    public void tearDown() throws Exception {
        HttpWebConnectionTest.stopWebServer(server_);
        server_ = null;
    }

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
        final Cookie cookie = new Cookie("a", "b", "c");
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
        final Cookie cookie2 = new Cookie("x", "y", "z");
        final HttpState state2 = new HttpState();
        state2.addCookie(cookie2);
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
        server_ = HttpWebConnectionTest.startWebServer("./", null, servlets);

        final String[] expectedAlerts = {"my_key=\"Hello, big, big, world\"; another_key=Hi"};
        final WebClient client = new WebClient();
        final List<String> collectedAlerts = new ArrayList<String>();
        client.setAlertHandler(new CollectingAlertHandler(collectedAlerts));
        client.getPage("http://localhost:" + HttpWebConnectionTest.PORT + "/test");
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
    public void serverModifiesCookieValue() throws Exception {
        final Map<String, Class< ? extends Servlet>> servlets = new HashMap<String, Class< ? extends Servlet>>();
        servlets.put("/test", SimpleCookieServlet.class);
        server_ = HttpWebConnectionTest.startWebServer("./", null, servlets);

        final String[] expectedAlerts = {"nbCalls=1", "nbCalls=2"};
        final WebClient client = new WebClient();
        final List<String> collectedAlerts = new ArrayList<String>();
        client.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        // get the page a first time
        client.getPage("http://localhost:" + HttpWebConnectionTest.PORT + "/test");

        // and a second one
        client.getPage("http://localhost:" + HttpWebConnectionTest.PORT + "/test");
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

}
