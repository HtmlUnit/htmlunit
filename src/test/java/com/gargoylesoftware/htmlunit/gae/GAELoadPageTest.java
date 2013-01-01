/*
 * Copyright (c) 2002-2013 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.gae;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.CollectingAlertHandler;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.MockWebConnection;
import com.gargoylesoftware.htmlunit.UrlFetchWebConnection;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.javascript.host.xml.XMLHttpRequest;

/**
 * Tests to see if pages can be loaded using <a href="http://code.google.com/appengine/">Google App Engine</a>
 * support.
 *
 * @version $Revision$
 * @author Amit Manjhi
 * @author Marc Guillemot
 * @author Ronald Brill
 */
@RunWith(GAETestRunner.class)
public class GAELoadPageTest {

    // can't use constants from {@link WebtestCase} as it forces to load forbidden classes
    private static final URL FIRST_URL;
    private static final URL FIRST_SECOND;

    static {
        try {
            FIRST_URL = new URL("http://localhost:8080/");
            FIRST_SECOND = new URL("http://localhost:8080/foo.html");
        }
        catch (final MalformedURLException e) {
            throw new Error("Can't create test urls", e);
        }
    }

    /**
     * Tests asynchronous use of XMLHttpRequest, using Mozilla style object creation.
     * @throws Exception if the test fails
     */
    @Test
    public void testAsyncUse() throws Exception {
        final URL secondUrl = new URL(FIRST_URL, "/second/");

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
            + "        request.open('GET', '/second/', true);\n"
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

        final WebClient client = new WebClient();
        final List<String> collectedAlerts = new ArrayList<String>();
        client.setAlertHandler(new CollectingAlertHandler(collectedAlerts));
        final MockWebConnection conn = new MockWebConnection();
        conn.setResponse(FIRST_URL, html);
        conn.setResponse(secondUrl, xml, "text/xml");
        client.setWebConnection(conn);

        client.getPage(FIRST_URL);

        final int executedJobs = client.getJavaScriptEngine().pumpEventLoop(1000);
        final String[] alerts = {String.valueOf(XMLHttpRequest.STATE_UNINITIALIZED),
            String.valueOf(XMLHttpRequest.STATE_LOADING),
            String.valueOf(XMLHttpRequest.STATE_LOADING),
            String.valueOf(XMLHttpRequest.STATE_LOADED),
            String.valueOf(XMLHttpRequest.STATE_INTERACTIVE),
            String.valueOf(XMLHttpRequest.STATE_COMPLETED), xml};
        assertEquals(Arrays.asList(alerts).toString(), collectedAlerts.toString());
        assertEquals(1, executedJobs);
    }

    /**
     * Test that a JS job using setInterval is processed on GAE.
     * @throws IOException if fails to get page.
     * @throws FailingHttpStatusCodeException if fails to get page.
     * @throws InterruptedException if wait fails
     */
    @Test
    public void setInterval() throws FailingHttpStatusCodeException, IOException, InterruptedException {
        final long timeout = 100L;
        final String html = "<html>\n"
            + "  <body>\n"
            + "    <script>\n"
            + "      setInterval(\"alert('hello')\", " + timeout + ");"
            + "    </script>\n"
            + "  </body>\n"
            + "</html>";
        final WebClient client = new WebClient();
        final List<String> collectedAlerts = new ArrayList<String>();
        client.setAlertHandler(new CollectingAlertHandler(collectedAlerts));
        final MockWebConnection conn = new MockWebConnection();
        conn.setDefaultResponse(html);
        client.setWebConnection(conn);

        client.getPage(FIRST_URL);
        final long startTime = System.currentTimeMillis();

        assertEquals(0, collectedAlerts.size());

        // pump but not long enough
        int executedJobs = client.getJavaScriptEngine().pumpEventLoop(timeout / 2);
        assertEquals(0, collectedAlerts.size());

        // pump a bit more
        executedJobs = client.getJavaScriptEngine().pumpEventLoop(timeout + 1);
        long count = (System.currentTimeMillis() - startTime) / timeout;
        count = Math.max(1, count);
        assertEquals(count, collectedAlerts.size());
        assertEquals(count, executedJobs);

        // pump even more
        executedJobs += client.getJavaScriptEngine().pumpEventLoop(timeout + 1);
        count = (System.currentTimeMillis() - startTime) / timeout;
        count = Math.max(2, count);
        assertEquals(count , collectedAlerts.size());
        assertEquals(count, executedJobs);
    }

    /**
     * Test that a JS job using setTimeout is processed on GAE.
     * @throws IOException if fails to get page.
     * @throws FailingHttpStatusCodeException if fails to get page.
     */
    @Test
    public void setTimeout() throws FailingHttpStatusCodeException, IOException {
        final long timeout = 400L;
        final String html = "<html>\n"
            + "  <body>\n"
            + "    <script>\n"
            + "      setTimeout(\"alert('hello')\", 0);"
            + "      setTimeout(\"alert('hello again')\", " + timeout + ");"
            + "    </script>\n"
            + "  </body>\n"
            + "</html>";
        final WebClient client = new WebClient();
        final List<String> collectedAlerts = new ArrayList<String>();
        client.setAlertHandler(new CollectingAlertHandler(collectedAlerts));
        final MockWebConnection conn = new MockWebConnection();
        conn.setDefaultResponse(html);
        client.setWebConnection(conn);
        client.getPage(FIRST_URL);

        int executedJobs = client.getJavaScriptEngine().pumpEventLoop(20);
        assertEquals(Arrays.asList("hello"), collectedAlerts);
        assertEquals(1, executedJobs);

        executedJobs = client.getJavaScriptEngine().pumpEventLoop(20);
        assertEquals(Arrays.asList("hello"), collectedAlerts);
        assertEquals(0, executedJobs);

        while (executedJobs < 1) {
            assertEquals(Arrays.asList("hello"), collectedAlerts);
            executedJobs = client.getJavaScriptEngine().pumpEventLoop(timeout / 10);
        }

        assertEquals(Arrays.asList("hello", "hello again"), collectedAlerts);
        assertEquals(1, executedJobs);
    }

    /**
     * Test that frames are loaded (issue #3544647).
     * @throws Exception if the test fails.
     */
    @Test
    public void frameShouldBeLoaded() throws Exception {
        final String html = "<html><body>\n"
            + "<iframe src='" + FIRST_SECOND + "'></iframe>\n"
            + "</body></html>";

        final String frame = "<html><body><script>alert('in frame');</script></body></html>";

        final WebClient client = new WebClient();
        final List<String> collectedAlerts = new ArrayList<String>();
        client.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        final MockWebConnection conn = new GAEMockWebConnection(client);
        conn.setResponse(FIRST_URL, html);
        conn.setResponse(FIRST_SECOND, frame);
        client.setWebConnection(conn);
        client.getPage(FIRST_URL);

        assertEquals(Arrays.asList("in frame"), collectedAlerts);
    }

    /**
     * Use special MockWebConnection here as the UrlFetchWebConnection handles
     * special URLs that are normally not even requested to the WebConnection (like "about:blank").
     * @author Marc Guillemot
     * @version $Revision$
     */
    static class GAEMockWebConnection extends MockWebConnection {
        private final UrlFetchWebConnection urlFetchWebconnection_;

        GAEMockWebConnection(final WebClient webClient) {
            urlFetchWebconnection_ = new UrlFetchWebConnection(webClient);
        }

        @Override
        public WebResponse getResponse(final WebRequest request) throws IOException {
            if (hasResponse(request.getUrl())) {
                return super.getResponse(request);
            }
            return urlFetchWebconnection_.getResponse(request);
        }
    }
}
