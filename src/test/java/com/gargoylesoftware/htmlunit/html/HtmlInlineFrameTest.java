/*
 * Copyright (c) 2002-2007 Gargoyle Software Inc. All rights reserved.
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.gargoylesoftware.htmlunit.CollectingAlertHandler;
import com.gargoylesoftware.htmlunit.MockWebConnection;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebTestCase;

/**
 * Unit tests for {@link HtmlInlineFrame}.
 *
 * @version $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author Ahmed Ashour
 */
public class HtmlInlineFrameTest extends WebTestCase {

    /**
     * Creates an instance.
     *
     * @param name The name of the test
     */
    public HtmlInlineFrameTest(final String name) {
        super(name);
    }

    /**
     * @throws Exception if the test fails
     */
    public void testSetSrcAttribute() throws Exception {
        final String firstContent
            = "<html><head><title>First</title></head><body>"
            + "<iframe id='iframe1' src='http://second'>"
            + "</body></html>";
        final String secondContent = "<html><head><title>Second</title></head><body></body></html>";
        final String thirdContent = "<html><head><title>Third</title></head><body></body></html>";
        final WebClient client = new WebClient();

        final MockWebConnection webConnection = new MockWebConnection(client);
        webConnection.setResponse(
            URL_FIRST, firstContent, 200, "OK", "text/html", Collections.EMPTY_LIST);
        webConnection.setResponse(
            URL_SECOND,secondContent,200,"OK","text/html",Collections.EMPTY_LIST);
        webConnection.setResponse(
            URL_THIRD,thirdContent,200,"OK","text/html",Collections.EMPTY_LIST);

        client.setWebConnection(webConnection);

        final HtmlPage page = (HtmlPage )client.getPage(URL_FIRST);
        assertEquals("First", page.getTitleText());

        final HtmlInlineFrame iframe = (HtmlInlineFrame)page.getHtmlElementById("iframe1");
        assertEquals("http://second", iframe.getSrcAttribute());
        assertEquals("Second", ((HtmlPage)iframe.getEnclosedPage()).getTitleText());

        iframe.setSrcAttribute("http://third");
        assertEquals("http://third", iframe.getSrcAttribute());
        assertEquals("Third", ((HtmlPage)iframe.getEnclosedPage()).getTitleText());
    }

    /**
     * Tests that a recursive src attribute (i.e. src="#xyz") doesn't result in an
     * infinite loop (bug 1699125).
     *
     * @throws Exception if an error occurs
     */
    public void testRecursiveSrcAttribute() throws Exception {
        final String html = "<html><body><iframe id='a' src='#abc'></body></html>";
        final HtmlPage page = loadPage(html);
        final HtmlInlineFrame iframe = (HtmlInlineFrame) page.getHtmlElementById("a");
        assertNotNull(iframe.getEnclosedPage());
    }

    /**
     * Tests that an invalid src attribute (i.e. src="foo://bar") doesn't result
     * in a NPE (bug 1699119).
     *
     * @throws Exception if an error occurs
     */
    public void testInvalidSrcAttribute() throws Exception {
        final String html = "<html><body><iframe id='a' src='foo://bar'></body></html>";
        final HtmlPage page = loadPage(html);
        final HtmlInlineFrame iframe = (HtmlInlineFrame) page.getHtmlElementById("a");
        assertNotNull(iframe.getEnclosedPage());
    }

    /**
     * @throws Exception if the test fails
     */
    public void testSetSrcAttribute_ViaJavaScript() throws Exception {
        final String firstContent
            = "<html><head><title>First</title></head><body>"
            + "<iframe id='iframe1' src='http://second'></iframe>"
            + "<script type='text/javascript'>document.getElementById('iframe1').src = 'http://third';"
            + "</script></body></html>";
        final String secondContent = "<html><head><title>Second</title></head><body></body></html>";
        final String thirdContent = "<html><head><title>Third</title></head><body></body></html>";
        final WebClient client = new WebClient();

        final MockWebConnection webConnection = new MockWebConnection(client);
        webConnection.setResponse(URL_FIRST, firstContent);
        webConnection.setResponse(URL_SECOND, secondContent);
        webConnection.setResponse(URL_THIRD, thirdContent);

        client.setWebConnection(webConnection);

        final HtmlPage page = (HtmlPage )client.getPage(URL_FIRST);
        assertEquals("First", page.getTitleText());

        final HtmlInlineFrame iframe = (HtmlInlineFrame) page.getHtmlElementById("iframe1");
        assertEquals("http://third", iframe.getSrcAttribute());
        assertEquals("Third", ((HtmlPage)iframe.getEnclosedPage()).getTitleText());
    }

    /**
     *
     * @throws Exception if the test fails
     */
    public void testScriptUnderIFrame() throws Exception {
        final String firstContent
            = "<html><body>"
            + "<iframe src='http://second'>"
            + "  <div><script>alert(1);</script></div>"
            + "  <script src='http://third'></script>"
            + "</iframe>"
            + "</body></html>";
        final String secondContent
            = "<html><body><script>alert(2);</script></body></html>";
        final String thirdContent
            = "alert('3');";
        final WebClient client = new WebClient();

        final MockWebConnection webConnection = new MockWebConnection(client);
        webConnection.setResponse(URL_FIRST, firstContent);
        webConnection.setResponse(URL_SECOND, secondContent);
        webConnection.setResponse(URL_THIRD, thirdContent, "text/javascript");

        client.setWebConnection(webConnection);
        
        final String[] expectedAlerts = {"2"};
        final List collectedAlerts = new ArrayList();
        client.setAlertHandler(new CollectingAlertHandler(collectedAlerts));
        
        client.getPage(URL_FIRST);
        assertEquals(expectedAlerts, collectedAlerts);
    }

}
