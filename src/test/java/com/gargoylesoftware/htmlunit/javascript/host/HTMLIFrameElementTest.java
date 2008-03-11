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
package com.gargoylesoftware.htmlunit.javascript.host;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.CollectingAlertHandler;
import com.gargoylesoftware.htmlunit.MockWebConnection;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebTestCase;
import com.gargoylesoftware.htmlunit.html.HtmlInlineFrame;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * Tests for {@link HTMLIFrameElement}.
 *
 * @version $Revision$
 * @author Marc Guillemot
 * @author Ahmed Ashour
 */
public class HTMLIFrameElementTest extends WebTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testStyle() throws Exception {
        final String content
            = "<html><head><title>First</title><script>\n"
            + "function doTest() {\n"
            + "    alert(document.getElementById('myIFrame').style == undefined);\n"
            + "}\n</script></head>\n"
            + "<body onload='doTest()'>\n"
            + "<iframe id='myIFrame' src='about:blank'></iframe></body></html>";

        final List<String> collectedAlerts = new ArrayList<String>();
        loadPage(content, collectedAlerts);
        final String[] expectedAlerts = {"false"};
        createTestPageForRealBrowserIfNeeded(content, expectedAlerts);
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testReferenceFromJavaScript() throws Exception {
        final String content
            = "<html><head><title>First</title><script>\n"
            + "function doTest() {\n"
            + "    alert(window.frames.length);\n"
            + "    alert(window.frames['myIFrame'].name);\n"
            + "}\n</script></head>\n"
            + "<body onload='doTest()'>\n"
            + "<iframe name='myIFrame' src='about:blank'></iframe></body></html>";

        final List<String> collectedAlerts = new ArrayList<String>();
        loadPage(content, collectedAlerts);
        final String[] expectedAlerts = {"1", "myIFrame"};
        createTestPageForRealBrowserIfNeeded(content, expectedAlerts);
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * Regression test for bug 1562872
     * @throws Exception if the test fails
     */
    @Test
    public void testDirectAccessPerName() throws Exception {
        final String content
            = "<html><head><title>First</title><script>\n"
            + "function doTest() {\n"
            + "    alert(myIFrame.location);\n"
            + "    alert(Frame.location);\n"
            + "}\n</script></head>\n"
            + "<body onload='doTest()'>\n"
            + "<iframe name='myIFrame' src='about:blank'></iframe>\n"
            + "<iframe name='Frame' src='about:blank'></iframe>\n"
            + "</body></html>";

        final List<String> collectedAlerts = new ArrayList<String>();
        final String[] expectedAlerts = {"about:blank", "about:blank"};
        createTestPageForRealBrowserIfNeeded(content, expectedAlerts);
        loadPage(content, collectedAlerts);
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * Tests that the <iframe> node is visible from the contained page when it is loaded
     * @throws Exception if the test fails
     */
    @Test
    public void testOnLoadGetsIFrameElementByIdInParent() throws Exception {
        final String firstContent
            = "<html><head><title>First</title></head>\n"
            + "<body>\n"
            + "<iframe id='myIFrame' src='frame.html'></iframe></body></html>";

        final String frameContent
            = "<html><head><title>Frame</title><script>\n"
            + "function doTest() {\n"
            + "    alert(parent.document.getElementById('myIFrame').tagName);\n"
            + "}\n</script></head>\n"
            + "<body onload='doTest()'>\n"
            + "</body></html>";
              
        final WebClient webClient = new WebClient();
        final MockWebConnection webConnection =
            new MockWebConnection(webClient);

        webConnection.setDefaultResponse(frameContent);
        webConnection.setResponse(URL_FIRST, firstContent);
        webClient.setWebConnection(webConnection);

        final List<String> collectedAlerts = new ArrayList<String>();
        webClient.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        final String[] expectedAlerts = {"IFRAME"};
        webClient.getPage(URL_FIRST);
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testContentDocument() throws Exception {
        final String content
            = "<html><head><title>first</title>\n"
                + "<script>\n"
                + "function test()\n"
                + "{\n"
                + "  alert(document.getElementById('myFrame').contentDocument == frames.foo.document);\n"
                + "}\n"
                + "</script></head>\n"
                + "<body onload='test()'>\n"
                + "<iframe name='foo' id='myFrame' src='about:blank'></iframe>\n"
                + "</body></html>";
        final String[] expectedAlerts = {"true"};
        createTestPageForRealBrowserIfNeeded(content, expectedAlerts);

        final List<String> collectedAlerts = new ArrayList<String>();
        loadPage(BrowserVersion.FIREFOX_2, content, collectedAlerts);

        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testFrameElement() throws Exception {
        final String content
            = "<html><head><title>first</title>\n"
                + "<script>\n"
                + "function test()\n"
                + "{\n"
                + "  alert(document.getElementById('myFrame') == frames.foo.frameElement);\n"
                + "}\n"
                + "</script></head>\n"
                + "<body onload='test()'>\n"
                + "<iframe name='foo' id='myFrame' src='about:blank'></iframe>\n"
                + "</body></html>";
        final String[] expectedAlerts = {"true"};
        createTestPageForRealBrowserIfNeeded(content, expectedAlerts);

        final List<String> collectedAlerts = new ArrayList<String>();
        loadPage(content, collectedAlerts);

        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * Verifies that writing to an iframe keeps the same intrinsic variables around (window,
     * document, etc) and in a usable form. Bug detected via the jQuery 1.1.3.1 unit tests.
     *
     * @throws Exception if an error occurs
     */
    @Test
    public void testWriteToIFrame() throws Exception {
        final String html =
              "<html><body onload='test()'><script>\n"
            + "    function test() {\n"
            + "        \n"
            + "        var frame = document.createElement('iframe');\n"
            + "        document.body.appendChild(frame);\n"
            + "        var win = frame.contentWindow;\n"
            + "        var doc = frame.contentWindow.document;\n"
            + "        alert(win == window);\n"
            + "        alert(doc == document);\n"
            + "        \n"
            + "        doc.open();\n"
            + "        doc.write(\"<html><body><input type='text'/></body></html>\");\n"
            + "        doc.close();\n"
            + "        var win2 = frame.contentWindow;\n"
            + "        var doc2 = frame.contentWindow.document;\n"
            + "        alert(win == win2);\n"
            + "        alert(doc == doc2);\n"
            + "        \n"
            + "        var input = doc.getElementsByTagName('input')[0];\n"
            + "        var input2 = doc2.getElementsByTagName('input')[0];\n"
            + "        alert(input == input2);\n"
            + "        alert(typeof input);\n"
            + "        alert(typeof input2);\n"
            + "    }\n"
            + "</script></body></html>";
        final String[] expected = {"false", "false", "true", "true", "true", "object", "object"};
        final List<String> actual = new ArrayList<String>();
        loadPage(html, actual);
        assertEquals(expected, actual);
    }

    /**
     * Verifies that writing to an iframe keeps the same intrinsic variables around (window,
     * document, etc) and in a usable form. Bug detected via the jQuery 1.1.3.1 unit tests.
     *
     * @throws Exception if an error occurs
     */
    @Test
    public void testIFrameReinitialized() throws Exception {
        final String html =
              "<html><body><a href='2.html' target='theFrame'>page 2 in frame</a>\n"
            + "<iframe name='theFrame' src='1.html'></iframe>\n"
            + "</body></html>";
        
        final String frame1 = "<html><head><script>window.foo = 123; alert(window.foo);</script></head></html>";
        final String frame2 = "<html><head><script>alert(window.foo);</script></head></html>";
        final String[] expected = {"123", "undefined"};

        final WebClient webClient = new WebClient();
        final MockWebConnection webConnection = new MockWebConnection(webClient);

        webConnection.setResponse(URL_FIRST, html);
        webConnection.setResponse(new URL(URL_FIRST, "1.html"), frame1);
        webConnection.setResponse(new URL(URL_FIRST, "2.html"), frame2);
        webClient.setWebConnection(webConnection);

        final List<String> collectedAlerts = new ArrayList<String>();
        webClient.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        final HtmlPage page = (HtmlPage) webClient.getPage(URL_FIRST);
        page.getAnchors().get(0).click();
        assertEquals(expected, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testSetSrcAttribute() throws Exception {
        final String firstContent
            = "<html><head><title>First</title></head><script>\n"
            + "  function test() {\n"
            + "   document.getElementById('iframe1').setAttribute('src', '" + URL_SECOND + "');\n"
            + "  }"
            + "</script>\n"
            + "<body onload='test()'>\n"
            + "<iframe id='iframe1'>\n"
            + "</body></html>";

        final String secondContent = "<html><head><title>Second</title></head><body></body></html>";
        final String thirdContent = "<html><head><title>Third</title></head><body></body></html>";
        final WebClient client = new WebClient();

        final MockWebConnection webConnection = new MockWebConnection(client);
        webConnection.setResponse(URL_FIRST, firstContent);
        webConnection.setResponse(URL_SECOND, secondContent);
        webConnection.setResponse(URL_THIRD, thirdContent);

        client.setWebConnection(webConnection);

        final HtmlPage page = (HtmlPage) client.getPage(URL_FIRST);
        assertEquals("First", page.getTitleText());

        final HtmlInlineFrame iframe = (HtmlInlineFrame) page.getHtmlElementById("iframe1");
        assertEquals(URL_SECOND.toExternalForm(), iframe.getSrcAttribute());
        assertEquals("Second", ((HtmlPage) iframe.getEnclosedPage()).getTitleText());

        iframe.setSrcAttribute(URL_THIRD.toExternalForm());
        assertEquals(URL_THIRD.toExternalForm(), iframe.getSrcAttribute());
        assertEquals("Third", ((HtmlPage) iframe.getEnclosedPage()).getTitleText());
    }
}
