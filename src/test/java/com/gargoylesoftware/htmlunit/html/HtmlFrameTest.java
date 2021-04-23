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
package com.gargoylesoftware.htmlunit.html;

import static org.junit.Assert.fail;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.CollectingAlertHandler;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.FrameContentHandler;
import com.gargoylesoftware.htmlunit.MockWebConnection;
import com.gargoylesoftware.htmlunit.SimpleWebTestCase;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.util.MimeType;

/**
 * Tests for {@link HtmlFrame}.
 *
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class HtmlFrameTest extends SimpleWebTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void srcOfBlankAndEmpty() throws Exception {
        final String html
            = "<html><head><title>first</title></head>\n"
            + "<frameset cols='20%,80%'>\n"
            + "  <frame src='' id='frame1'>\n"
            + "  <frame src='about:blank' id='frame2'>\n"
            + "</frameset></html>";
        final HtmlPage page = loadPage(html);

        final HtmlFrame frame1 = page.getHtmlElementById("frame1");
        assertEquals("frame1", "", ((HtmlPage) frame1.getEnclosedPage()).getTitleText());

        final HtmlFrame frame2 = page.getHtmlElementById("frame2");
        assertEquals("frame2", "", ((HtmlPage) frame2.getEnclosedPage()).getTitleText());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void onLoadHandler() throws Exception {
        final WebClient webClient = getWebClientWithMockWebConnection();
        final MockWebConnection webConnection = getMockWebConnection();
        final List<String> collectedAlerts = new ArrayList<>();
        webClient.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        final String html
            = "<html><head><title>first</title></head>\n"
            + "<frameset cols='20%,80%'>\n"
            + "  <frame id='frame1'>\n"
            + "  <frame onload='alert(this.tagName)' id='frame2'>\n"
            + "</frameset></html>";
        final String[] expectedAlerts = {"FRAME"};

        webConnection.setResponse(URL_FIRST, html);

        final HtmlPage page = webClient.getPage(URL_FIRST);
        assertEquals("first", page.getTitleText());

        final HtmlFrame frame1 = page.getHtmlElementById("frame1");
        assertEquals("frame1", "", ((HtmlPage) frame1.getEnclosedPage()).getTitleText());

        final HtmlFrame frame2 = page.getHtmlElementById("frame2");
        assertEquals("frame2", "", ((HtmlPage) frame2.getEnclosedPage()).getTitleText());

        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void documentWrite() throws Exception {
        final String html
            = "<html><head><title>first</title></head>\n"
            + "<frameset cols='20%,80%'>\n"
            + "  <frame src='' name='frame1' id='frame1'>\n"
            + "  <frame onload=\"frame1.document.open();frame1.document.write("
            + "'<html><head><title>generated</title></head><body>generated</body></html>');"
            + "frame1.document.close()\"  id='frame2'>\n"
            + "</frameset></html>";
        final HtmlPage page = loadPage(html);

        assertEquals("first", page.getTitleText());

        final HtmlFrame frame1 = page.getHtmlElementById("frame1");
        assertEquals("frame1", "generated", ((HtmlPage) frame1.getEnclosedPage()).getTitleText());

        final HtmlFrame frame2 = page.getHtmlElementById("frame2");
        assertEquals("frame2", "", ((HtmlPage) frame2.getEnclosedPage()).getTitleText());
    }

    /**
     * Tests that frames are correctly deregistered even if not HTML.
     * @throws Exception if the test fails
     */
    @Test
    public void deregisterNonHtmlFrame() throws Exception {
        final WebClient webClient = getWebClientWithMockWebConnection();
        final MockWebConnection webConnection = getMockWebConnection();

        final String html
            = "<html><head><title>first</title></head>\n"
            + "<frameset cols='100%'>\n"
            + "  <frame src='foo.txt'>\n"
            + "</frameset></html>";
        webConnection.setDefaultResponse("foo", 200, "OK", MimeType.TEXT_PLAIN);
        webConnection.setResponse(URL_FIRST, html);

        final HtmlPage page = webClient.getPage(URL_FIRST);
        assertEquals("first", page.getTitleText());

        // loads something else to trigger frame de-registration
        webClient.getPage(URL_SECOND);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void failingHttpStatusCodeException() throws Exception {
        final String failingHtml = "<html><head><body>Not found</body></html>";

        final String firstHtml
            = "<html><head><title>First</title></head>\n"
            + "<frameset cols='130,*'>\n"
            + "  <frame scrolling='no' name='left' src='" + "failing_url" + "' frameborder='1' />\n"
            + "  <frame scrolling='auto' name='right' src='" + URL_THIRD + "' frameborder='1' />\n"
            + "  <noframes>\n"
            + "    <body>Frames not supported</body>\n"
            + "  </noframes>\n"
            + "</frameset>\n"
            + "</html>";

        final String secondHtml = "<html><head><title>Second</title></head><body></body></html>";
        final String thirdHtml  = "<html><head><title>Third</title></head><body></body></html>";

        final WebClient webClient = getWebClientWithMockWebConnection();

        final MockWebConnection webConnection = getMockWebConnection();
        webConnection.setDefaultResponse(failingHtml, 404, "Not Found", MimeType.TEXT_HTML);
        webConnection.setResponse(URL_FIRST, firstHtml);
        webConnection.setResponse(URL_SECOND, secondHtml);
        webConnection.setResponse(URL_THIRD, thirdHtml);

        try {
            webClient.getPage(URL_FIRST);
            fail("Expected FailingHttpStatusCodeException");
        }
        catch (final FailingHttpStatusCodeException e) {
            assertEquals(404, e.getStatusCode());
        }
    }

    /**
     * Regression test for bug #363.
     * @throws Exception if the test fails
     */
    @Test
    public void frameScriptReplaceOtherFrame() throws Exception {
        final String html =
            "<html><head><title>frames</title></head>\n"
            + "<frameset cols='180,*'>\n"
            + "<frame name='f1' src='1.html'/>\n"
            + "<frame name='f2' src='2.html'/>\n"
            + "</frameset>\n"
            + "</html>";

        final String frame1 = "<html><head><title>1</title></head>\n"
            + "<body>1"
            + "<script>\n"
            + "  parent.frames['f2'].location.href = '3.html';\n"
            + "</script>\n"
            + "</body></html>";

        final String frame3 = "<html><head><title>page 3</title></head><body></body></html>";

        final WebClient webClient = getWebClientWithMockWebConnection();
        final MockWebConnection conn = getMockWebConnection();
        conn.setDefaultResponse("<html><head><title>default</title></head><body></body></html>");
        conn.setResponse(URL_FIRST, html);
        conn.setResponse(new URL(URL_FIRST, "1.html"), frame1);
        conn.setResponse(new URL(URL_FIRST, "3.html"), frame3);

        final HtmlPage page = webClient.getPage(URL_FIRST);

        assertEquals("page 3", ((HtmlPage) page.getFrameByName("f2").getEnclosedPage()).getTitleText());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"default", ""})
    public void frameContentHandler() throws Exception {
        final WebClient webClient = getWebClientWithMockWebConnection();
        final MockWebConnection webConnection = getMockWebConnection();

        final String html =
                "<html><head><title>frames</title></head>\n"
                + "<frameset cols='180,*'>\n"
                + "<frame id='frame1' src='1.html'/>\n"
                + "</frameset>\n"
                + "</html>";

        webConnection.setDefaultResponse("<html><head><title>default</title></head><body></body></html>");
        webConnection.setResponse(URL_FIRST, html);

        HtmlPage page = webClient.getPage(URL_FIRST);
        assertEquals("frames", page.getTitleText());

        HtmlFrame frame1 = page.getHtmlElementById("frame1");
        assertEquals(getExpectedAlerts()[0], ((HtmlPage) frame1.getEnclosedPage()).getTitleText());

        webClient.setFrameContentHandler(new FrameContentHandler() {
            @Override
            public boolean loadFrameDocument(final BaseFrameElement baseFrameElement) {
                return false;
            }
        });

        page = webClient.getPage(URL_FIRST);
        assertEquals("frames", page.getTitleText());

        frame1 = page.getHtmlElementById("frame1");
        assertEquals(getExpectedAlerts()[1], ((HtmlPage) frame1.getEnclosedPage()).getTitleText());
    }
}
