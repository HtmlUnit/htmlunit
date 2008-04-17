/*
 * Copyright (c) 2002-2008 Gargoyle Software Inc. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 3. The end-user documentation included with the redistribution, if any, must
 *    include the following acknowledgment:
 *
 *       "This product includes software developed by Gargoyle Software Inc.
 *        (http://www.GargoyleSoftware.com/)."
 *
 *    Alternately, this acknowledgment may appear in the software itself, if
 *    and wherever such third-party acknowledgments normally appear.
 * 4. The name "Gargoyle Software" must not be used to endorse or promote
 *    products derived from this software without prior written permission.
 *    For written permission, please contact info@GargoyleSoftware.com.
 * 5. Products derived from this software may not be called "HtmlUnit", nor may
 *    "HtmlUnit" appear in their name, without prior written permission of
 *    Gargoyle Software Inc.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL GARGOYLE
 * SOFTWARE INC. OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
 * OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.gargoylesoftware.htmlunit.html;

import static org.junit.Assert.fail;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.gargoylesoftware.htmlunit.CollectingAlertHandler;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.MockWebConnection;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebTestCase;

/**
 * Tests for {@link HtmlFrame}.
 *
 * @version $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author Ahmed Ashour
 */
public class HtmlFrameTest extends WebTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testSrcOfBlankAndEmpty() throws Exception {
        final String firstContent
            = "<html><head><title>first</title></head>\n"
            + "<frameset cols='20%,80%'>\n"
            + "    <frame src='' id='frame1'>\n"
            + "    <frame src='about:blank'  id='frame2'>\n"
            + "</frameset></html>";
        final HtmlPage page = loadPage(firstContent);

        final HtmlFrame frame1 = page.getHtmlElementById("frame1");
        Assert.assertEquals("frame1", "", ((HtmlPage) frame1.getEnclosedPage()).getTitleText());

        final HtmlFrame frame2 = page.getHtmlElementById("frame2");
        Assert.assertEquals("frame2", "", ((HtmlPage) frame2.getEnclosedPage()).getTitleText());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testOnLoadHandler() throws Exception {
        final WebClient webClient = new WebClient();
        final MockWebConnection webConnection =
            new MockWebConnection(webClient);
        final List<String> collectedAlerts = new ArrayList<String>();

        webClient.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        final String firstContent
            = "<html><head><title>first</title></head>\n"
            + "<frameset cols='20%,80%'>\n"
            + "    <frame id='frame1'>\n"
            + "    <frame onload='alert(this.tagName)' id='frame2'>\n"
            + "</frameset></html>";
        final String[] expectedAlerts = {"FRAME"};

        webConnection.setResponse(URL_FIRST, firstContent);
        webClient.setWebConnection(webConnection);

        final HtmlPage page = (HtmlPage) webClient.getPage(URL_FIRST);
        assertEquals("first", page.getTitleText());

        final HtmlFrame frame1 = page.getHtmlElementById("frame1");
        Assert.assertEquals("frame1", "", ((HtmlPage) frame1.getEnclosedPage()).getTitleText());

        final HtmlFrame frame2 = page.getHtmlElementById("frame2");
        Assert.assertEquals("frame2", "", ((HtmlPage) frame2.getEnclosedPage()).getTitleText());

        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testDocumentWrite() throws Exception {
        final String firstContent
            = "<html><head><title>first</title></head>\n"
            + "<frameset cols='20%,80%'>\n"
            + "    <frame src='' name='frame1' id='frame1'>\n"
            + "    <frame onload=\"frame1.document.open();frame1.document.write("
            + "'<html><head><title>generated</title></head><body>generated</body></html>');"
            + "frame1.document.close()\"  id='frame2'>\n"
            + "</frameset></html>";
        final HtmlPage page = loadPage(firstContent);
        
        assertEquals("first", page.getTitleText());

        final HtmlFrame frame1 = page.getHtmlElementById("frame1");
        Assert.assertEquals("frame1", "generated", ((HtmlPage) frame1.getEnclosedPage()).getTitleText());

        final HtmlFrame frame2 = page.getHtmlElementById("frame2");
        Assert.assertEquals("frame2", "", ((HtmlPage) frame2.getEnclosedPage()).getTitleText());
    }

    /**
     * Tests that frames are correctly deregistered even if not HTML.
     * @throws Exception if the test fails
     */
    @Test
    public void testDeregisterNonHtmlFrame() throws Exception {
        final WebClient webClient = new WebClient();
        final MockWebConnection webConnection =
            new MockWebConnection(webClient);

        final String firstContent
            = "<html><head><title>first</title></head>\n"
            + "<frameset cols='100%'>\n"
            + "    <frame src='foo.txt'>\n"
            + "</frameset></html>";
        webConnection.setDefaultResponse("foo", 200, "OK", "text/plain");
        webConnection.setResponse(URL_FIRST, firstContent);
        webClient.setWebConnection(webConnection);

        final HtmlPage page = (HtmlPage) webClient.getPage(URL_FIRST);
        assertEquals("first", page.getTitleText());
        
        // loads something else to trigger frame de-registration
        webClient.getPage(URL_SECOND);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testFailingHttpStatusCodeException() throws Exception {
        final String failingContent
            = "<html><head><body>Not found</body></html>";
        
        final String firstContent
            = "<html><head><title>First</title></head>\n"
            + "<frameset cols='130,*'>\n"
            + "  <frame scrolling='no' name='left' src='" + "failing_url" + "' frameborder='1' />\n"
            + "  <frame scrolling='auto' name='right' src='" + URL_THIRD + "' frameborder='1' />\n"
            + "  <noframes>\n"
            + "    <body>Frames not supported</body>\n"
            + "  </noframes>\n"
            + "</frameset>\n"
            + "</html>";

        final String secondContent = "<html><head><title>Second</title></head><body></body></html>";
        final String thirdContent  = "<html><head><title>Third</title></head><body></body></html>";

        final WebClient webClient = new WebClient();

        final MockWebConnection webConnection = new MockWebConnection(webClient);
        webConnection.setDefaultResponse(
                failingContent, 404, "No Found", "text/html");
        webConnection.setResponse(URL_FIRST, firstContent);
        webConnection.setResponse(URL_SECOND, secondContent);
        webConnection.setResponse(URL_THIRD, thirdContent);

        webClient.setWebConnection(webConnection);

        try {
            webClient.getPage(URL_FIRST);
            fail("Expected FailingHttpStatusCodeException");
        }
        catch (final FailingHttpStatusCodeException e) {
            assertEquals(404, e.getStatusCode());
        }
    }

    /**
     * Regression test for bug 1518195.
     * See http://sourceforge.net/tracker/index.php?func=detail&aid=1518195&group_id=47038&atid=448266.
     * @throws Exception if the test fails
     */
    @Test
    public void testFrameScriptReplaceOtherFrame() throws Exception {
        final String mainContent =
            "<html><head><title>frames</title></head>\n"
            + "<frameset cols='180,*'>\n"
            + "<frame name='f1' src='1.html'/>\n"
            + "<frame name='f2' src='2.html'/>\n"
            + "</frameset>\n"
            + "</html>";
        
        final String frame1 = "<html><head><title>1</title></head>\n"
            + "<body>1"
            + "<script>\n"
            + "   parent.frames['f2'].location.href = '3.html';\n"
            + "</script>\n"
            + "</body></html>";

        final String frame3 = "<html><head><title>page 3</title></head><body></body></html>";

        final WebClient webClient = new WebClient();
        final MockWebConnection conn = new MockWebConnection(webClient);
        webClient.setWebConnection(conn);
        
        conn.setDefaultResponse("<html><head><title>default</title></head><body></body></html>");
        conn.setResponse(URL_FIRST, mainContent);
        conn.setResponse(new URL(URL_FIRST, "1.html"), frame1);
        conn.setResponse(new URL(URL_FIRST, "3.html"), frame3);

        final HtmlPage page = (HtmlPage) webClient.getPage(URL_FIRST);

        assertEquals("page 3", ((HtmlPage) page.getFrameByName("f2").getEnclosedPage()).getTitleText());
    }
}
