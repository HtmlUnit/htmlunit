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
package com.gargoylesoftware.htmlunit.html;

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.CollectingAlertHandler;
import com.gargoylesoftware.htmlunit.MockWebConnection;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebTestCase;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;

/**
 * Unit tests for {@link HtmlInlineFrame}.
 *
 * @version $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author Ahmed Ashour
 * @author Marc Guillemot
 * @author Daniel Gredler
 */
@RunWith(BrowserRunner.class)
public class HtmlInlineFrameTest extends WebTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testSetSrcAttribute() throws Exception {
        final String firstContent
            = "<html><head><title>First</title></head><body>\n"
            + "<iframe id='iframe1' src='" + URL_SECOND + "'>\n"
            + "</body></html>";
        final String secondContent = "<html><head><title>Second</title></head><body></body></html>";
        final String thirdContent = "<html><head><title>Third</title></head><body></body></html>";
        final WebClient client = getWebClient();

        final MockWebConnection webConnection = new MockWebConnection();
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
     * @throws Exception if the test fails
     */
    @Test
    public void testSetSrcAttributeWithWhiteSpaces() throws Exception {
        final String firstContent
            = "<html><head><title>First</title></head><body>\n"
            + "<iframe id='iframe1' src='\n" + URL_SECOND + "\n'>\n"
            + "</body></html>";
        final String secondContent = "<html><head><title>Second</title></head><body></body></html>";
        final String thirdContent = "<html><head><title>Third</title></head><body></body></html>";
        final WebClient client = getWebClient();

        final MockWebConnection webConnection = new MockWebConnection();
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
     * Tests that a recursive src attribute (i.e. src="#xyz") doesn't result in an
     * infinite loop (bug 1699125).
     *
     * @throws Exception if an error occurs
     */
    @Test
    public void testRecursiveSrcAttribute() throws Exception {
        final String html = "<html><body><iframe id='a' src='#abc'></body></html>";
        final HtmlPage page = loadPage(html);
        final HtmlInlineFrame iframe = page.getHtmlElementById("a");
        assertNotNull(iframe.getEnclosedPage());
    }

    /**
     * Tests that a recursive src is prevented.
     * @throws Exception if an error occurs
     */
    @Test
    public void testRecursiveNestedFrames() throws Exception {
        final String firstContent
            = "<html><head><title>First</title></head><body>\n"
            + "<iframe id='iframe1' src='" + URL_SECOND + "'>\n"
            + "</body></html>";
        final String secondContent = "<html><head><title>Second</title></head>\n"
            + "<body><iframe id='iframe2_1' src='" + URL_FIRST + "'></iframe></body></html>";
        final WebClient client = getWebClient();

        final MockWebConnection webConnection = new MockWebConnection();
        webConnection.setResponse(URL_FIRST, firstContent);
        webConnection.setResponse(URL_SECOND, secondContent);

        client.setWebConnection(webConnection);

        final HtmlPage page = client.getPage(URL_FIRST);
        assertEquals("First", page.getTitleText());

        final HtmlInlineFrame iframe = page.getHtmlElementById("iframe1");
        assertEquals(URL_SECOND.toExternalForm(), iframe.getSrcAttribute());
        final HtmlPage iframePage = (HtmlPage) iframe.getEnclosedPage();
        assertEquals("Second", iframePage.getTitleText());

        // the nested frame should not have been loaded
        final HtmlInlineFrame iframeIn2 = iframePage.getHtmlElementById("iframe2_1");
        assertEquals(URL_FIRST.toExternalForm(), iframeIn2.getSrcAttribute());
        assertEquals("about:blank", iframeIn2.getEnclosedPage().getWebResponse().getRequestSettings().getUrl());
    }

    /**
     * Tests that an invalid src attribute (i.e. src="foo://bar") doesn't result
     * in a NPE (bug 1699119).
     *
     * @throws Exception if an error occurs
     */
    @Test
    public void testInvalidSrcAttribute() throws Exception {
        final String html = "<html><body><iframe id='a' src='foo://bar'></body></html>";
        final HtmlPage page = loadPage(html);
        final HtmlInlineFrame iframe = page.getHtmlElementById("a");
        assertNotNull(iframe.getEnclosedPage());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testSetSrcAttribute_ViaJavaScript() throws Exception {
        final String firstContent
            = "<html><head><title>First</title></head><body>\n"
            + "<iframe id='iframe1' src='" + URL_SECOND + "'></iframe>\n"
            + "<script type='text/javascript'>document.getElementById('iframe1').src = '" + URL_THIRD + "';\n"
            + "</script></body></html>";
        final String secondContent = "<html><head><title>Second</title></head><body></body></html>";
        final String thirdContent = "<html><head><title>Third</title></head><body></body></html>";
        final WebClient client = getWebClient();

        final MockWebConnection webConnection = new MockWebConnection();
        webConnection.setResponse(URL_FIRST, firstContent);
        webConnection.setResponse(URL_SECOND, secondContent);
        webConnection.setResponse(URL_THIRD, thirdContent);

        client.setWebConnection(webConnection);

        final HtmlPage page = client.getPage(URL_FIRST);
        assertEquals("First", page.getTitleText());

        final HtmlInlineFrame iframe = page.getHtmlElementById("iframe1");
        assertEquals(URL_THIRD.toExternalForm(), iframe.getSrcAttribute());
        assertEquals("Third", ((HtmlPage) iframe.getEnclosedPage()).getTitleText());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testScriptUnderIFrame() throws Exception {
        final String firstContent
            = "<html><body>\n"
            + "<iframe src='" + URL_SECOND + "'>\n"
            + "  <div><script>alert(1);</script></div>\n"
            + "  <script src='" + URL_THIRD + "'></script>\n"
            + "</iframe>\n"
            + "</body></html>";
        final String secondContent
            = "<html><body><script>alert(2);</script></body></html>";
        final String thirdContent
            = "alert('3');";
        final WebClient client = getWebClient();

        final MockWebConnection webConnection = new MockWebConnection();
        webConnection.setResponse(URL_FIRST, firstContent);
        webConnection.setResponse(URL_SECOND, secondContent);
        webConnection.setResponse(URL_THIRD, thirdContent, "text/javascript");

        client.setWebConnection(webConnection);

        final String[] expectedAlerts = {"2"};
        final List<String> collectedAlerts = new ArrayList<String>();
        client.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        client.getPage(URL_FIRST);
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(FF = "[object HTMLIFrameElement]", IE = "[object]")
    public void testSimpleScriptable() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    alert(document.getElementById('myId'));\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "  <iframe id='myId'>\n"
            + "</body></html>";

        final HtmlPage page = loadPageWithAlerts(html);
        assertTrue(HtmlInlineFrame.class.isInstance(page.getHtmlElementById("myId")));
    }

    /**
     * Verifies that cloned frames do no reload their content (bug 1954869).
     * @throws Exception if an error occurs
     */
    @Test
    public void testFrameCloneDoesNotReloadFrame() throws Exception {
        final String html1 = "<html><body><iframe src='" + URL_SECOND + "'></iframe></body></html>";
        final String html2 = "<html><body>abc</body></html>";

        final WebClient client = getWebClient();

        final MockWebConnection conn = new MockWebConnection();
        conn.setResponse(URL_FIRST, html1);
        conn.setResponse(URL_SECOND, html2);
        client.setWebConnection(conn);

        final HtmlPage page = client.getPage(URL_FIRST);
        assertEquals(2, conn.getRequestCount());

        page.cloneNode(true);
        assertEquals(2, conn.getRequestCount());
    }

    /**
     * Verifies that frames added via document.write() don't get their contents loaded twice (bug 1156009).
     * @throws Exception if an error occurs
     */
    @Test
    public void testFrameWriteDoesNotReloadFrame() throws Exception {
        final String html1 =
              "<html><body>\n"
            + "<script>document.write('<iframe id=\"f\" src=\"" + URL_SECOND + "\"></iframe>')</script>\n"
            + "</body></html>";
        final String html2 = "<html><body>abc</body></html>";

        final WebClient client = getWebClient();

        final MockWebConnection conn = new MockWebConnection();
        conn.setResponse(URL_FIRST, html1);
        conn.setResponse(URL_SECOND, html2);
        client.setWebConnection(conn);

        final HtmlPage page = client.getPage(URL_FIRST);
        assertEquals("iframe", page.getElementById("f").getTagName());

        assertEquals(2, conn.getRequestCount());
    }

}
