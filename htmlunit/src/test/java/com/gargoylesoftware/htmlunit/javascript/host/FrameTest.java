/*
 * Copyright (c) 2002, 2005 Gargoyle Software Inc. All rights reserved.
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
import java.util.Arrays;
import java.util.List;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.CollectingAlertHandler;
import com.gargoylesoftware.htmlunit.MockWebConnection;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebTestCase;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * Tests for Window
 *
 * @version  $Revision$
 * @author Chris Erskine
 * @author Marc Guillemot
 * @author Thomas Robbs
 */
public class FrameTest extends WebTestCase {
    /**
     * Create an instance
     * @param name The name of the test
     */
    public FrameTest( final String name ) {
        super(name);
    }

    /**
     * @throws Exception if the test fails
     */
    public void testFrameName() throws Exception {
        final WebClient webClient = new WebClient();
        final MockWebConnection webConnection =
            new MockWebConnection(webClient);
        final List collectedAlerts = new ArrayList();

        webClient.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        final String firstContent
            = "<html><head><title>first</title></head>"
            + "<frameset cols='20%,80%'>"
            + "    <frame id='frame1'>"
            + "    <frame name='Frame2' onload='alert(this.name)' id='frame2'>"
            + "</frameset></html>";
        final List expectedAlerts = Arrays.asList( new String[]{"Frame2"} );

        webConnection.setResponse(URL_FIRST, firstContent);
        webClient.setWebConnection(webConnection);

        final HtmlPage page = (HtmlPage) webClient.getPage(URL_FIRST);
        assertEquals("first", page.getTitleText());

        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * Regression test for
     * http://sourceforge.net/tracker/index.php?func=detail&aid=1101525&group_id=47038&atid=448266
     * @throws Exception if the test fails
     */
    public void testLocation() throws Exception {
        final WebClient webClient = new WebClient();
        final MockWebConnection webConnection =
            new MockWebConnection(webClient);
        final List collectedAlerts = new ArrayList();

        webClient.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        final String firstContent
            = "<html><head><title>first</title></head>"
            + "<frameset cols='*' onload='Frame1.location = \"frame.html\"'>"
            + "    <frame name='Frame1' src='subdir/frame.html'>"
            + "</frameset></html>";
        final String defaultContent
            = "<html><head><script>alert(location)</script></head></html>";

        webConnection.setResponse(URL_FIRST, firstContent);
        webConnection.setDefaultResponse(defaultContent);

        final List expectedAlerts = Arrays.asList( new String[] {URL_FIRST.toExternalForm() + "/subdir/frame.html",
                URL_FIRST.toExternalForm() + "/frame.html"} );

        webClient.setWebConnection(webConnection);

        final HtmlPage page = (HtmlPage) webClient.getPage(URL_FIRST);
        assertEquals("first", page.getTitleText());

        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    public void testContentDocument() throws Exception {
        final String content
            = "<html><head><title>first</title>"
                + "<script>"
                + "function test()\n"
                + "{\n"
                + "  alert(document.getElementById('myFrame').contentDocument == frames.foo.document);\n"
                + "}\n"
                + "</script></head>"
                + "<frameset rows='*' onload='test()'>"
                + "<frame name='foo' id='myFrame' src='about:blank'/>"
                + "</frameset>"
                + "</html>";
        final List expectedAlerts = Arrays.asList( new String[]{"true"} );

        final List collectedAlerts = new ArrayList();
        loadPage(BrowserVersion.MOZILLA_1_0, content, collectedAlerts);

        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * Regression test for bug 1236048
     * http://sourceforge.net/tracker/index.php?func=detail&aid=1236048&group_id=47038&atid=448266
     * @throws Exception if the test fails
     */
    public void testWriteFrameset() throws Exception {
        if (notYetImplemented()) {
            return;
        }

        final String content1 = "<html><head>"
            + "<script>"
            + "    document.write('<frameset>');"
            + "    document.write('<frame src=\"page2.html\" name=\"leftFrame\">');"
            + "    document.write('</frameset>');"
            + "</script>"
            + "</head></html>";
        final String content2 = "<html><head><script>alert(2)</script></head></html>";

        final WebClient webClient = new WebClient();
        final MockWebConnection webConnection = new MockWebConnection(webClient);
        webClient.setWebConnection(webConnection);

        webConnection.setDefaultResponse(content2);
        webConnection.setResponse(URL_FIRST, content1);

        final List collectedAlerts = new ArrayList();
        webClient.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        final List expectedAlerts = Arrays.asList( new String[]{"2"} );

        webClient.getPage(URL_FIRST);

        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * Regression test fo Bug #1289060
     * http://sourceforge.net/tracker/index.php?func=detail&aid=1289060&group_id=47038&atid=448266
     *
     * @throws Exception if the test fails
     */
    public void testFrameLoadedAfterParent() throws Exception {
        if (notYetImplemented()) {
            return;
        }

        final WebClient webClient = new WebClient();
        final MockWebConnection webConnection = new MockWebConnection( webClient );

        final String mainContent
            = "<html><head><title>first</title></head><body>"
            + "<iframe name='testFrame' src='testFrame.html'></iframe>"
            + "<div id='aButton'>test text</div>"
            + "</body></html>";
        final String frameContent
            = "<html><head></head><body>"
            + "<script>"
            + "alert(top.document.getElementById('aButton').tagName);"
            + "</script>"
            + "</body></html>";

        webConnection.setResponse(URL_GARGOYLE, mainContent);
        webConnection.setResponse(new URL(URL_GARGOYLE.toString() + "testFrame.html" ), frameContent);

        webClient.setWebConnection(webConnection);
        final List collectedAlerts = new ArrayList();
        webClient.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        final List expectedAlerts = Arrays.asList( new String[]{"DIV"} );

        webClient.getPage( URL_GARGOYLE );
        assertEquals(expectedAlerts, collectedAlerts);
    }
}
