/*
 * Copyright (c) 2002-2025 Gargoyle Software Inc.
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
package org.htmlunit.javascript.host.html;

import java.util.List;

import javax.net.ssl.SSLHandshakeException;

import org.htmlunit.MockWebConnection;
import org.htmlunit.SimpleWebTestCase;
import org.htmlunit.WebClient;
import org.htmlunit.html.FrameWindow;
import org.htmlunit.html.HtmlInlineFrame;
import org.htmlunit.html.HtmlPage;
import org.htmlunit.junit.BrowserRunner;
import org.htmlunit.junit.annotation.Alerts;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Tests for {@link HTMLIFrameElement}.
 *
 * @author Marc Guillemot
 * @author Ahmed Ashour
 * @author Daniel Gredler
 * @author Ronald Brill
 * @author Frank Danek
 */
@RunWith(BrowserRunner.class)
public class HTMLIFrameElementTest extends SimpleWebTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void setSrcAttribute() throws Exception {
        final String firstContent = DOCTYPE_HTML
            + "<html><head><title>First</title><script>\n"
            + "  function test() {\n"
            + "    document.getElementById('iframe1').setAttribute('src', '" + URL_SECOND + "');\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "<iframe id='iframe1'>\n"
            + "</body></html>";

        final String secondContent = DOCTYPE_HTML + "<html><head><title>Second</title></head><body></body></html>";
        final String thirdContent = DOCTYPE_HTML + "<html><head><title>Third</title></head><body></body></html>";
        final WebClient client = getWebClient();

        final MockWebConnection webConnection = getMockWebConnection();
        webConnection.setResponse(URL_FIRST, firstContent);
        webConnection.setResponse(URL_SECOND, secondContent);
        webConnection.setResponse(URL_THIRD, thirdContent);

        client.setWebConnection(webConnection);

        final HtmlPage page = client.getPage(URL_FIRST);
        assertEquals("First", page.getTitleText());

        final HtmlInlineFrame iframe = page.getHtmlElementById("iframe1");
        assertEquals(URL_SECOND.toExternalForm(), iframe.getSrcAttribute());
        assertEquals("Second", ((HtmlPage) iframe.getEnclosedPage()).getTitleText());

        iframe.setSrcAttribute(URL_THIRD.toExternalForm());
        assertEquals(URL_THIRD.toExternalForm(), iframe.getSrcAttribute());
        assertEquals("Third", ((HtmlPage) iframe.getEnclosedPage()).getTitleText());
    }

    /**
     * Verify that an iframe.src with a "javascript:..." URL loaded by a client with JavaScript
     * disabled does not cause a NullPointerException (reported on the mailing list).
     * @throws Exception if an error occurs
     */
    @Test
    public void srcJavaScriptUrl_JavaScriptDisabled() throws Exception {
        final String html = DOCTYPE_HTML + "<html><body><iframe src='javascript:false;'></iframe></body></html>";

        final WebClient client = getWebClient();
        client.getOptions().setJavaScriptEnabled(false);

        final MockWebConnection conn = getMockWebConnection();
        conn.setDefaultResponse(html);
        client.setWebConnection(conn);

        client.getPage(URL_FIRST);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void removeFrameWindow() throws Exception {
        final String index = DOCTYPE_HTML
                + "<html><head></head><body>\n"
                + "<div id='content'>\n"
                + "  <iframe name='content' src='second/'></iframe>\n"
                + "</div>\n"
                + "<button id='clickId' "
                +     "onClick=\"document.getElementById('content').innerHTML = 'new content';\">Item</button>\n"
                + "</body></html>";

        final String frame1 = DOCTYPE_HTML
                + "<html><head></head><body>\n"
                + "<p>frame content</p>\n"
                + "</body></html>";

        final WebClient webClient = getWebClient();
        final MockWebConnection webConnection = getMockWebConnection();

        webConnection.setResponse(URL_SECOND, frame1);
        webClient.setWebConnection(webConnection);

        final HtmlPage page = loadPage(index);

        assertEquals("frame content", page.getElementById("content").asNormalizedText());
        // check frame on page
        List<FrameWindow> frames = page.getFrames();
        assertEquals(1, frames.size());
        assertEquals("frame content", ((HtmlPage) page.getFrameByName("content").getEnclosedPage()).asNormalizedText());

        // replace frame tag with javascript
        page.getElementById("clickId").click();

        assertEquals("new content", page.getElementById("content").asNormalizedText());

        // frame has to be gone
        frames = page.getFrames();
        assertTrue(frames.isEmpty());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"", "2"})
    public void iframeUrlInvalid() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + "    function log(msg) { window.document.title += msg + '\\u00a7'; }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body>\n"
            + "  <iframe id='frame1' src='" + URL_SECOND + "' "
                        + "onLoad='log(\"loaded\")' onError='log(\"error\")'></iframe>\n"
            + "</body>\n"
            + "</html>";

        getMockWebConnection().setDefaultResponse(html);
        getMockWebConnection().setThrowable(URL_SECOND, new SSLHandshakeException("Test"));

        final String[] expectedAlerts = getExpectedAlerts();
        final HtmlPage page = loadPage(html);
        assertEquals(expectedAlerts[0], page.getTitleText());

        assertEquals(Integer.parseInt(expectedAlerts[1]), getMockWebConnection().getRequestCount());
    }
}
