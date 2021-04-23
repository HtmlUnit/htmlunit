/*
 * Copyright (c) 2002-2021 Gargoyle Software Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.gargoylesoftware.htmlunit.javascript.host.xml;

import java.io.IOException;
import java.io.Writer;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.Servlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;
import com.gargoylesoftware.htmlunit.CollectingAlertHandler;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.MockWebConnection;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.WebServerTestCase;
import com.gargoylesoftware.htmlunit.html.DomChangeEvent;
import com.gargoylesoftware.htmlunit.html.DomChangeListener;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.javascript.host.xml.XMLHttpRequestTest.StreamingServlet;
import com.gargoylesoftware.htmlunit.util.MimeType;

/**
 * Tests for {@link XMLHttpRequest}.
 *
 * @author Daniel Gredler
 * @author Marc Guillemot
 * @author Ahmed Ashour
 * @author Stuart Begg
 * @author Sudhan Moghe
 * @author Frank Danek
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class XMLHttpRequest3Test extends WebServerTestCase {

    private static final String MSG_NO_CONTENT = "no Content";
    private static final String MSG_PROCESSING_ERROR = "error processing";

    /**
     * Tests asynchronous use of XMLHttpRequest, where the XHR request fails due to IOException (Connection refused).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"0", "1", "4", MSG_NO_CONTENT, MSG_PROCESSING_ERROR},
            IE = {"0", "1", "1", "4", MSG_NO_CONTENT, MSG_PROCESSING_ERROR})
    public void asyncUseWithNetworkConnectionFailure() throws Exception {
        final String html =
              "<html>\n"
            + "<head>\n"
            + "<title>XMLHttpRequest Test</title>\n"
            + "<script>\n"
            + "var request;\n"
            + "function testAsync() {\n"
            + "  request = new XMLHttpRequest();\n"
            + "  request.onreadystatechange = onReadyStateChange;\n"
            + "  request.onerror = onError;\n"
            + "  alert(request.readyState);\n"
            + "  request.open('GET', '" + URL_SECOND + "', true);\n"
            + "  request.send('');\n"
            + "}\n"
            + "function onError() {\n"
            + "  alert('" + MSG_PROCESSING_ERROR + "');\n"
            + "}\n"
            + "function onReadyStateChange() {\n"
            + "  alert(request.readyState);\n"
            + "  if (request.readyState == 4) {\n"
            + "    if (request.responseText.length == 0)\n"
            + "      alert('" + MSG_NO_CONTENT + "');\n"
            + "    else\n"
            + "      throw 'Unexpected content, should be zero length but is: \"' + request.responseText + '\"';\n"
            + "  }\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='testAsync()'>\n"
            + "</body>\n"
            + "</html>";

        final WebClient client = getWebClient();
        final List<String> collectedAlerts = Collections.synchronizedList(new ArrayList<String>());
        client.setAlertHandler(new CollectingAlertHandler(collectedAlerts));
        final MockWebConnection conn = new DisconnectedMockWebConnection();
        conn.setResponse(URL_FIRST, html);
        client.setWebConnection(conn);
        client.getPage(URL_FIRST);

        assertEquals(0, client.waitForBackgroundJavaScriptStartingBefore(1000));
        assertEquals(getExpectedAlerts(), collectedAlerts);
    }

    /**
     * Connection refused WebConnection for URL_SECOND.
     */
    private static final class DisconnectedMockWebConnection extends MockWebConnection {

        DisconnectedMockWebConnection() {
        }

        /** {@inheritDoc} */
        @Override
        public WebResponse getResponse(final WebRequest request) throws IOException {
            if (URL_SECOND.equals(request.getUrl())) {
                throw new IOException("Connection refused");
            }
            return super.getResponse(request);
        }
    }

    /**
     * Asynchronous callback should be called in "main" js thread and not parallel to other js execution.
     * See http://sourceforge.net/p/htmlunit/bugs/360/.
     * @throws Exception if the test fails
     */
    @Test
    public void noParallelJSExecutionInPage() throws Exception {
        final String content = "<html><head><script>\n"
            + "var j = 0;\n"
            + "function test() {\n"
            + "  req = new XMLHttpRequest();\n"
            + "  req.onreadystatechange = handler;\n"
            + "  req.open('post', 'foo.xml', true);\n"
            + "  req.send('');\n"
            + "  alert('before long loop');\n"
            + "  for (var i = 0; i < 5000; i++) {\n"
            + "    j = j + 1;\n"
            + "  }\n"
            + "  alert('after long loop');\n"
            + "}\n"
            + "function handler() {\n"
            + "  if (req.readyState == 4) {\n"
            + "    alert('ready state handler, content loaded: j=' + j);\n"
            + "  }\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'></body></html>";

        final WebClient client = getWebClient();
        final List<String> collectedAlerts = Collections.synchronizedList(new ArrayList<String>());
        client.setAlertHandler(new CollectingAlertHandler(collectedAlerts));
        final MockWebConnection conn = new MockWebConnection() {
            @Override
            public WebResponse getResponse(final WebRequest webRequest) throws IOException {
                collectedAlerts.add(webRequest.getUrl().toExternalForm());
                return super.getResponse(webRequest);
            }
        };
        conn.setResponse(URL_FIRST, content);
        final URL urlPage2 = new URL(URL_FIRST, "foo.xml");
        conn.setResponse(urlPage2, "<foo/>\n", MimeType.TEXT_XML);
        client.setWebConnection(conn);
        client.getPage(URL_FIRST);

        assertEquals(0, client.waitForBackgroundJavaScriptStartingBefore(1000));

        final String[] alerts = {URL_FIRST.toExternalForm(), "before long loop", "after long loop",
            urlPage2.toExternalForm(), "ready state handler, content loaded: j=5000" };
        assertEquals(alerts, collectedAlerts);
    }

    /**
     * Tests that the different HTTP methods are supported.
     * @throws Exception if an error occurs
     */
    @Test
    public void methods() throws Exception {
        testMethod(HttpMethod.GET);
        testMethod(HttpMethod.HEAD);
        testMethod(HttpMethod.DELETE);
        testMethod(HttpMethod.POST);
        testMethod(HttpMethod.PUT);
        testMethod(HttpMethod.OPTIONS);
        testMethod(HttpMethod.TRACE);
        testMethod(HttpMethod.PATCH);
    }

    /**
     * @throws Exception if the test fails
     */
    private void testMethod(final HttpMethod method) throws Exception {
        final String content = "<html><head><script>\n"
            + "function test() {\n"
            + "  var req = new XMLHttpRequest();\n"
            + "  req.open('" + method.name().toLowerCase(Locale.ROOT) + "', 'foo.xml', false);\n"
            + "  req.send('');\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'></body></html>";

        final WebClient client = getWebClient();
        final MockWebConnection conn = new MockWebConnection();
        conn.setResponse(URL_FIRST, content);
        final URL urlPage2 = new URL(URL_FIRST, "foo.xml");
        conn.setResponse(urlPage2, "<foo/>\n", MimeType.TEXT_XML);
        client.setWebConnection(conn);
        client.getPage(URL_FIRST);

        final WebRequest request = conn.getLastWebRequest();
        assertEquals(urlPage2, request.getUrl());
        assertSame(method, request.getHttpMethod());
    }

    /**
     * Was causing a deadlock on 03.11.2007 (and probably with release 1.13 too).
     * @throws Exception if the test fails
     */
    @Test
    public void xmlHttpRequestWithDomChangeListenerDeadlock() throws Exception {
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
            + "  var request = new XMLHttpRequest();\n"
            + "  request.onreadystatechange = onReadyStateChange;\n"
            + "  request.open('GET', _src, _async);\n"
            + "  request.send('');\n"
            + "}\n"
            + "function onReadyStateChange() {\n"
            + "  parent.document.getElementById('p1').title = 'new title';\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body>\n"
            + "<p id='p1' title='myTitle'></p>\n"
            + "</body></html>";

        final MockWebConnection connection = new MockWebConnection() {
            private boolean gotFoo1_ = false;

            @Override
            public WebResponse getResponse(final WebRequest webRequest) throws IOException {
                final String url = webRequest.getUrl().toExternalForm();

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
                return super.getResponse(webRequest);
            }
        };
        connection.setDefaultResponse("");
        connection.setResponse(URL_FIRST, content);
        connection.setResponse(new URL(URL_FIRST, "page2.html"), content2);

        final WebClient webClient = getWebClient();
        webClient.setWebConnection(connection);

        final HtmlPage page = webClient.getPage(URL_FIRST);
        final DomChangeListener listener = new DomChangeListener() {
            @Override
            public void nodeAdded(final DomChangeEvent event) {
                // Empty.
            }
            @Override
            public void nodeDeleted(final DomChangeEvent event) {
                // Empty.
            }
        };
        page.addDomChangeListener(listener);
        page.getHtmlElementById("p1").click();
    }

    /**
     * Regression test for bug 1209686 (onreadystatechange not called with partial data when emulating FF).
     * @throws Exception if an error occurs
     */
    @Test
    @NotYetImplemented
    public void streaming() throws Exception {
        final Map<String, Class<? extends Servlet>> servlets = new HashMap<>();
        servlets.put("/test", StreamingServlet.class);

        final String resourceBase = "./src/test/resources/com/gargoylesoftware/htmlunit/javascript/host";
        startWebServer(resourceBase, null, servlets);
        final WebClient client = getWebClient();
        final HtmlPage page = client.getPage(URL_FIRST + "XMLHttpRequestTest_streaming.html");
        assertEquals(0, client.waitForBackgroundJavaScriptStartingBefore(1000));
        final HtmlElement body = page.getBody();
        assertEquals(10, body.asText().split("\n").length);
    }

    /**
     * Tests the value of "this" in handler.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("this == request")
    public void thisValueInHandler() throws Exception {
        final String html =
              "<html>\n"
            + "  <head>\n"
            + "    <title>XMLHttpRequest Test</title>\n"
            + "    <script>\n"
            + "      var request;\n"
            + "      function testAsync() {\n"
            + "        request = new XMLHttpRequest();\n"
            + "        request.onreadystatechange = onReadyStateChange;\n"
            + "        request.open('GET', 'foo.xml', true);\n"
            + "        request.send('');\n"
            + "      }\n"
            + "      function onReadyStateChange() {\n"
            + "        if (request.readyState == 4) {\n"
            + "          if (this == request)\n"
            + "            alert('this == request');\n"
            + "          else if (this == onReadyStateChange)\n"
            + "            alert('this == handler');\n"
            + "          else alert('not expected: ' + this)\n"
            + "        }\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='testAsync()'>\n"
            + "  </body>\n"
            + "</html>";

        final WebClient client = getWebClient();
        final List<String> collectedAlerts = Collections.synchronizedList(new ArrayList<String>());
        client.setAlertHandler(new CollectingAlertHandler(collectedAlerts));
        final MockWebConnection conn = new MockWebConnection();
        conn.setResponse(URL_FIRST, html);
        conn.setDefaultResponse("");
        client.setWebConnection(conn);
        client.getPage(URL_FIRST);

        assertEquals(0, client.waitForBackgroundJavaScriptStartingBefore(1000));
        assertEquals(getExpectedAlerts(), collectedAlerts);
    }

    /**
     * Test for a strange error we found: An ajax running
     * in parallel shares the additional headers with a form
     * submit.
     *
     * @throws Exception if an error occurs
     */
    @Test
    public void ajaxInfluencesSubmitHeaders() throws Exception {
        final Map<String, Class<? extends Servlet>> servlets = new HashMap<>();
        servlets.put("/content.html", ContentServlet.class);
        servlets.put("/ajax_headers.html", AjaxHeaderServlet.class);
        servlets.put("/form_headers.html", FormHeaderServlet.class);
        startWebServer("./", null, servlets);

        collectedHeaders_.clear();
        XMLHttpRequest3Test.STATE_ = 0;
        final WebClient client = getWebClient();

        final List<String> collectedAlerts = Collections.synchronizedList(new ArrayList<String>());
        client.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        final HtmlPage page = client.getPage(URL_FIRST + "content.html");
        final DomElement elem = page.getElementById("doIt");
        while (STATE_ < 1) {
            Thread.sleep(42);
        }
        ((HtmlSubmitInput) elem).click();

        client.waitForBackgroundJavaScript(DEFAULT_WAIT_TIME);
        assertEquals(collectedHeaders_.toString(), 2, collectedHeaders_.size());

        String headers = collectedHeaders_.get(0);
        if (!headers.startsWith("Form: ")) {
            headers = collectedHeaders_.get(1);
        }
        assertTrue(headers, headers.startsWith("Form: "));
        assertFalse(headers, headers.contains("Html-Unit=is great,;"));

        headers = collectedHeaders_.get(0);
        if (!headers.startsWith("Ajax: ")) {
            headers = collectedHeaders_.get(1);
        }
        assertTrue(headers, headers.startsWith("Ajax: "));
        assertTrue(headers, headers.contains("Html-Unit=is great,;"));
    }

    static final List<String> collectedHeaders_ = Collections.synchronizedList(new ArrayList<String>());
    static int STATE_ = 0;

    /**
     * First servlet for testNoContent().
     */
    public static class ContentServlet extends HttpServlet {
        /**
         * {@inheritDoc}
         */
        @Override
        protected void doGet(final HttpServletRequest req, final HttpServletResponse res) throws IOException {
            final String html = "<html><head><script>\n"
                    + "  function test() {\n"
                    + "    xhr = new XMLHttpRequest();\n"
                    + "    xhr.open('POST', 'ajax_headers.html', true);\n"
                    + "    xhr.setRequestHeader('Html-Unit', 'is great');\n"
                    + "    xhr.send('');\n"
                    + "  }\n"
                    + "</script></head>\n"
                    + "<body onload='test()'>\n"
                    + "  <form action='form_headers.html' name='myForm'>\n"
                    + "    <input name='myField' value='some value'>\n"
                    + "    <input type='submit' id='doIt' value='Do It'>\n"
                    + "  </form>\n"
                    + "</body></html>";

            res.setContentType(MimeType.TEXT_HTML);
            final Writer writer = res.getWriter();
            writer.write(html);
        }
    }

    /**
     * Servlet for setRequestHeader().
     */
    public static class AjaxHeaderServlet extends HttpServlet {
        /**
         * {@inheritDoc}
         */
        @Override
        protected void doPost(final HttpServletRequest req, final HttpServletResponse res) throws IOException {
            doGet(req, res);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        protected void doGet(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
            final String header = headers(request);
            STATE_ = 1;
            try {
                // do not return before the form request is also sent
                while (STATE_ < 2) {
                    Thread.sleep(42);
                }
            }
            catch (final InterruptedException e) {
                e.printStackTrace();
            }

            collectedHeaders_.add("Ajax: " + header);
            response.setContentType(MimeType.TEXT_PLAIN);
            final Writer writer = response.getWriter();
            writer.write(header);
            writer.flush();
        }
    }

    /**
     * Servlet for setRequestHeader().
     */
    public static class FormHeaderServlet extends HttpServlet {
        /**
         * {@inheritDoc}
         */
        @Override
        protected void doPost(final HttpServletRequest req, final HttpServletResponse res) throws IOException {
            doGet(req, res);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        protected void doGet(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
            final String header = headers(request);
            STATE_ = 2;

            final String html = "<html><head></head>\n"
                    + "<body>\n"
                    + "<p>Form: " + header + "</p<\n"
                    + "</body></html>";

            collectedHeaders_.add("Form: " + header);
            response.setContentType(MimeType.TEXT_HTML);
            final Writer writer = response.getWriter();
            writer.write(html);
            writer.flush();
        }
    }

    static String headers(final HttpServletRequest request) {
        final StringBuilder text = new StringBuilder();
        text.append("Headers: ");
        final Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            final String name = headerNames.nextElement();
            text.append(name);
            text.append('=');
            final Enumeration<String> headers = request.getHeaders(name);
            while (headers.hasMoreElements()) {
                final String header = headers.nextElement();
                text.append(header);
                text.append(',');
            }
            text.append(';');
        }
        return text.toString();
    }
}
