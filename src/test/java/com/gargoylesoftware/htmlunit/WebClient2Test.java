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
package com.gargoylesoftware.htmlunit;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.Servlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.SerializationUtils;
import org.apache.http.client.CircularRedirectException;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * Tests for {@link WebClient} that run with BrowserRunner.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class WebClient2Test extends WebServerTestCase {

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(IE = "http://first/?param=\u00A3", FF = "http://first/?param=%A3")
    public void encodeURL() throws Exception {
        final String html = "<body onload='alert(window.location.href)'></body>";
        final WebClient webClient = getWebClient();
        final List<String> collectedAlerts = new ArrayList<String>();
        webClient.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        final MockWebConnection webConnection = new MockWebConnection();
        webConnection.setDefaultResponse(html);

        webClient.setWebConnection(webConnection);
        webClient.getPage("http://first/?param=\u00A3");
        assertEquals(getExpectedAlerts(), collectedAlerts);
    }

    /**
     * Test that the path and query string are encoded to be valid.
     * @throws Exception if something goes wrong
     */
    @Test
    public void loadPage_EncodeRequest() throws Exception {
        final String htmlContent
            = "<html><head><title>foo</title></head><body>\n"
            + "</body></html>";

        final WebClient client = getWebClient();

        final MockWebConnection webConnection = new MockWebConnection();
        webConnection.setDefaultResponse(htmlContent);
        client.setWebConnection(webConnection);

        // with query string not encoded
        HtmlPage page = client.getPage("http://first?a=b c&d=\u00E9\u00E8");
        String expected;
        final boolean ie = getBrowserVersion().isIE();
        if (ie) {
            expected = "?a=b%20c&d=\u00E9\u00E8";
        }
        else {
            expected = "?a=b%20c&d=%E9%E8";
        }
        assertEquals("http://first/" + expected, page.getWebResponse().getWebRequest().getUrl());

        // with query string already encoded
        page = client.getPage("http://first?a=b%20c&d=%C3%A9%C3%A8");
        assertEquals("http://first/?a=b%20c&d=%C3%A9%C3%A8", page.getWebResponse().getWebRequest().getUrl());

        // with query string partially encoded
        page = client.getPage("http://first?a=b%20c&d=e f");
        assertEquals("http://first/?a=b%20c&d=e%20f", page.getWebResponse().getWebRequest().getUrl());

        // with anchor
        page = client.getPage("http://first?a=b c#myAnchor");
        assertEquals("http://first/?a=b%20c#myAnchor", page.getWebResponse().getWebRequest().getUrl());

        // with query string containing encoded "&", "=", "+", ",", and "$"
        page = client.getPage("http://first?a=%26%3D%20%2C%24");
        assertEquals("http://first/?a=%26%3D%20%2C%24", page.getWebResponse().getWebRequest().getUrl());

        // with character to encode in path
        page = client.getPage("http://first/page 1.html");
        assertEquals("http://first/page%201.html", page.getWebResponse().getWebRequest().getUrl());

        // with character to encode in path
        page = client.getPage("http://first/page 1.html");
        assertEquals("http://first/page%201.html", page.getWebResponse().getWebRequest().getUrl());
    }

    /**
     * Verifies that a WebClient can be serialized and deserialized before it has been used.
     * @throws Exception if an error occurs
     */
    @Test
    public void serialization_beforeUse() throws Exception {
        final WebClient client = getWebClient();
        final WebClient copy = clone(client);
        assertNotNull(copy);
    }

    /**
     * Verifies that a WebClient can be serialized and deserialized after it has been used.
     * @throws Exception if an error occurs
     */
    @Test
    public void serialization_afterUse() throws Exception {
        startWebServer("./");

        final WebClient client = getWebClient();
        TextPage textPage = client.getPage("http://localhost:" + PORT + "/LICENSE.txt");
        assertTrue(textPage.getContent().contains("Gargoyle Software"));

        final WebClient copy = clone(client);
        assertNotNull(copy);

        final WebWindow window = copy.getCurrentWindow();
        assertNotNull(window);

        final WebWindow topWindow = window.getTopWindow();
        assertNotNull(topWindow);

        final Page page = topWindow.getEnclosedPage();
        assertNotNull(page);

        final WebResponse response = page.getWebResponse();
        assertNotNull(response);

        final String content = response.getContentAsString();
        assertNotNull(content);
        assertTrue(content.contains("Gargoyle Software"));

        textPage = copy.getPage("http://localhost:" + PORT + "/LICENSE.txt");
        assertTrue(textPage.getContent().contains("Gargoyle Software"));

        copy.closeAllWindows();
    }

    /**
     * Regression test for bug 2833433.
     * @throws Exception if an error occurs
     */
    @Test
    public void serialization_pageLoad() throws Exception {
        final String page1Content = "<html><body>hello 1</body></html>";
        final WebClient client = getWebClient();
        final HtmlPage page1 = loadPage(client, page1Content, null, URL_FIRST);
        assertEquals("hello 1", page1.asText());

        final String page2Content = "<html><body>hello 2</body></html>";
        final WebClient copy = clone(client);
        final HtmlPage page2 = loadPage(copy, page2Content, null, URL_SECOND);
        assertEquals("hello 2", page2.asText());
        copy.closeAllWindows();
    }

    /**
     * Regression test for bug 2836355.
     * @throws Exception if an error occurs
     */
    @Test
    public void serialization_withClickAfterwards() throws Exception {
        final String html =
              "<html><head>\n"
            + "<script>\n"
            + "  function foo() {\n"
            + "    document.getElementById('mybox').innerHTML='hello world';\n"
            + "    return false;\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body><div id='mybox'></div>\n"
            + "<a href='#' onclick='foo()' id='clicklink'>say hello world</a>\n"
            + "</body></html>";
        final HtmlPage page = loadPageWithAlerts(html);
        assertEquals("", page.getElementById("mybox").getTextContent());

        final WebClient clientCopy = clone(page.getWebClient());
        final HtmlPage pageCopy = (HtmlPage) clientCopy.getCurrentWindow().getTopWindow().getEnclosedPage();
        pageCopy.getElementById("clicklink").click();
        assertEquals("hello world", pageCopy.getElementById("mybox").getTextContent());
    }

    /**
     * Background tasks that have been registered before the serialization should
     * wake up and run normally after the deserialization.
     * Until now (2.7-SNAPSHOT 17.09.09) HtmlUnit has probably never supported it.
     * This is currently not requested and this test is just to document the current status.
     * @throws Exception if an error occurs
     */
    @Test
    @NotYetImplemented
    public void serialization_withJSBackgroundTasks() throws Exception {
        final String html =
              "<html><head>\n"
            + "<script>\n"
            + "  function foo() {\n"
            + "    if (window.name == 'hello') {\n"
            + "      alert('exiting');\n"
            + "      clearInterval(intervalId);\n"
            + "    }\n"
            + "  }\n"
            + "  var intervalId = setInterval(foo, 10);\n"
            + "</script></head>\n"
            + "<body></body></html>";
        final HtmlPage page = loadPageWithAlerts(html);
        // verify that 1 background job exists
        assertEquals(1, page.getEnclosingWindow().getJobManager().getJobCount());

        final byte[] bytes = SerializationUtils.serialize(page);
        page.getWebClient().closeAllWindows();

        // deserialize page and verify that 1 background job exists
        final HtmlPage clonedPage = (HtmlPage) SerializationUtils.deserialize(bytes);
        assertEquals(1, clonedPage.getEnclosingWindow().getJobManager().getJobCount());

        // configure a new CollectingAlertHandler (in fact it has surely already one and we could get and cast it)
        final List<String> collectedAlerts = Collections.synchronizedList(new ArrayList<String>());
        final AlertHandler alertHandler = new CollectingAlertHandler(collectedAlerts);
        clonedPage.getWebClient().setAlertHandler(alertHandler);

        // make some change in the page on which background script reacts
        clonedPage.getEnclosingWindow().setName("hello");

        clonedPage.getWebClient().waitForBackgroundJavaScriptStartingBefore(100);
        assertEquals(0, clonedPage.getEnclosingWindow().getJobManager().getJobCount());
        final String[] expectedAlerts = {"exiting"};
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * Regression test for bug 2812769.
     * @throws Exception if an error occurs
     */
    @Test
    public void acceptLanguage() throws Exception {
        final String html = "<html><body></body></html>";
        final HtmlPage p = loadPageWithAlerts(html);
        assertEquals("en-us", p.getWebResponse().getWebRequest().getAdditionalHeaders().get("Accept-Language"));

        final WebClient client = p.getWebClient();
        final String lang = client.getBrowserVersion().getBrowserLanguage();
        try {
            client.getBrowserVersion().setBrowserLanguage("fr");
            final HtmlPage p2 = client.getPage(getDefaultUrl());
            assertEquals("fr", p2.getWebResponse().getWebRequest().getAdditionalHeaders().get("Accept-Language"));
        }
        finally {
            // Restore original language.
            client.getBrowserVersion().setBrowserLanguage(lang);
        }
    }

    /**
     * As of HtmlUnit-2.7-SNAPSHOT from 24.09.09, loading about:blank in a page didn't
     * reinitialized the window host object.
     * @throws Exception if an error occurs
     */
    @Test
    public void newWindowScopeForAboutBlank() throws Exception {
        final HtmlPage p = loadPage("<html><body></body></html>");
        p.executeJavaScript("top.foo = 'hello';");
        final ScriptResult result = p.executeJavaScript("top.foo");
        assertEquals("hello", result.getJavaScriptResult());

        p.getWebClient().getPage("about:blank");
        final ScriptResult result2 = p.executeJavaScript("String(top.foo)");
        assertEquals("undefined", result2.getJavaScriptResult());
    }

    /**
     * Verifies that a redirect limit kicks in even if the redirects aren't for the same URL
     * and don't use the same redirect HTTP status code (see bug 2915453).
     * @throws Exception if an error occurs
     */
    @Test
    public void redirectInfinite303And307() throws Exception {
        final Map<String, Class<? extends Servlet>> servlets = new HashMap<String, Class<? extends Servlet>>();
        servlets.put(RedirectServlet307.URL, RedirectServlet307.class);
        servlets.put(RedirectServlet303.URL, RedirectServlet303.class);
        startWebServer("./", new String[0], servlets);

        final WebClient client = getWebClient();

        try {
            client.getPage("http://localhost:" + PORT + RedirectServlet307.URL);
        }
        catch (final Exception e) {
            assertTrue(e.getCause().toString(), e.getCause() instanceof CircularRedirectException);
        }
    }

    /**
     * Helper class for {@link #redirectInfinite303And307}.
     */
    public static class RedirectServlet extends HttpServlet {
        private static final long serialVersionUID = -140725579463205715L;
        private int count_;
        private int status_;
        private String location_;
        /**
         * Creates a new instance.
         * @param status the HTTP status to return
         * @param location the location to redirect to
         */
        public RedirectServlet(final int status, final String location) {
            count_ = 0;
            status_ = status;
            location_ = location;
        }
        /** {@inheritDoc} */
        @Override
        protected void doGet(final HttpServletRequest req, final HttpServletResponse resp) throws IOException {
            count_++;
            resp.setStatus(status_);
            resp.setHeader("Location", location_);
            resp.getWriter().write(status_ + " " + req.getContextPath() + " " + count_);
        }
    }

    /**
     * Helper class for {@link #redirectInfinite303And307}.
     */
    public static class RedirectServlet303 extends RedirectServlet {
        private static final long serialVersionUID = 3744682108187160138L;
        static final String URL = "/test";
        /** Creates a new instance. */
        public RedirectServlet303() {
            super(303, RedirectServlet307.URL);
        }
    }

    /**
     * Helper class for {@link #redirectInfinite303And307}.
     */
    public static class RedirectServlet307 extends RedirectServlet {
        private static final long serialVersionUID = 3670046341988874716L;
        static final String URL = "/test2";
        /** Creates a new instance. */
        public RedirectServlet307() {
            super(307, RedirectServlet303.URL);
        }
    }

}
