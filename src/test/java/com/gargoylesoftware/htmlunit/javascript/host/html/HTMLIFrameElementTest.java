/*
 * Copyright (c) 2002-2014 Gargoyle Software Inc.
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

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.MockWebConnection;
import com.gargoylesoftware.htmlunit.SimpleWebTestCase;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.FrameWindow;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlInlineFrame;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * Tests for {@link HTMLIFrameElement}.
 *
 * @version $Revision$
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
        final String firstContent
            = "<html><head><title>First</title><script>\n"
            + "  function test() {\n"
            + "   document.getElementById('iframe1').setAttribute('src', '" + URL_SECOND + "');\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "<iframe id='iframe1'>\n"
            + "</body></html>";

        final String secondContent = "<html><head><title>Second</title></head><body></body></html>";
        final String thirdContent = "<html><head><title>Third</title></head><body></body></html>";
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
        final String html = "<html><body><iframe src='javascript:false;'></iframe></body></html>";

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
    public void testRemoveFrameWindow() throws Exception {
        final String index = "<html><head></head><body>"
                + "<div id='content'>"
                + "  <iframe name='content' src='second/'></iframe>"
                + "</div>"
                + "<button id='clickId' "
                +     "onClick=\"document.getElementById('content').innerHTML = 'new content';\">Item</button>\n"
                + "</body></html>";

        final String frame1 = "<html><head></head><body>"
                + "<p>frame content</p>"
                + "</body></html>";

        final WebClient webClient = getWebClient();
        final MockWebConnection webConnection = getMockWebConnection();

        webConnection.setResponse(URL_SECOND, frame1);
        webClient.setWebConnection(webConnection);

        final HtmlPage page = loadPage(index);

        assertEquals("", page.getElementById("content").asText());
        // check frame on page
        List<FrameWindow> frames = page.getFrames();
        assertEquals(1, frames.size());
        assertEquals("frame content", ((HtmlPage) page.getFrameByName("content").getEnclosedPage()).asText());

        // replace frame tag with javascript
        ((HtmlElement) page.getElementById("clickId")).click();

        assertEquals("new content", page.getElementById("content").asText());

        // frame has to be gone
        frames = page.getFrames();
        assertEquals(0, frames.size());
    }
}
