/*
 * Copyright (c) 2002, 2003 Gargoyle Software Inc. All rights reserved.
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

import com.gargoylesoftware.htmlunit.SubmitMethod;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebWindow;
import com.gargoylesoftware.htmlunit.FakeWebConnection;
import com.gargoylesoftware.htmlunit.WebTestCase;
import java.net.URL;
import java.util.Collections;

/**
 *  Tests for HtmlFrameSet
 *
 * @version  $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 */
public class HtmlFrameSetTest extends WebTestCase {

    /**
     *  Create an instance
     *
     * @param  name Name of the test
     */
    public HtmlFrameSetTest( final String name ) {
        super( name );
    }


    public void testLoadingFrameSet()
        throws Exception {

        final String firstContent
             = "<html><head><title>First</title></head>"
             + "<frameset cols='130,*'>"
             + "  <frame scrolling='no' name='left' src='http://second' frameborder='1' />"
             + "  <frame scrolling='auto' name='right' src='http://third' frameborder='1' />"
             + "  <noframes>"
             + "    <body>Frames not supported</body>"
             + "  </noframes>"
             + "</frameset>"
             + "</html>";
        final String secondContent = "<html><head><title>Second</title></head><body></body></html>";
        final String thirdContent  = "<html><head><title>Third</title></head><body></body></html>";

        final WebClient webClient = new WebClient();

        final FakeWebConnection webConnection = new FakeWebConnection( webClient );
        webConnection.setResponse(
            new URL("http://first"), firstContent, 200, "OK", "text/html", Collections.EMPTY_LIST );
        webConnection.setResponse(
            new URL("http://second"), secondContent,200,"OK","text/html",Collections.EMPTY_LIST );
        webConnection.setResponse(
            new URL("http://third"), thirdContent,200,"OK","text/html",Collections.EMPTY_LIST );

        webClient.setWebConnection( webConnection );

        final HtmlPage firstPage = ( HtmlPage )webClient.getPage(
                new URL( "http://first" ), SubmitMethod.POST, Collections.EMPTY_LIST );
        assertEquals( "First", firstPage.getTitleText() );

        final WebWindow secondWebWindow = webClient.getWebWindowByName("left");
        assertInstanceOf( secondWebWindow, HtmlFrame.class );
        assertSame( firstPage, ((HtmlFrame)secondWebWindow).getPage() );
        assertEquals( "Second", ((HtmlPage)secondWebWindow.getEnclosedPage()).getTitleText() );

        final WebWindow thirdWebWindow = webClient.getWebWindowByName("right");
        assertInstanceOf( thirdWebWindow, HtmlFrame.class );
        assertSame( firstPage, ((HtmlFrame)thirdWebWindow).getPage() );
        assertEquals( "Third", ((HtmlPage)thirdWebWindow.getEnclosedPage()).getTitleText() );
    }


    public void testLoadingIFrames()
        throws Exception {

        final String firstContent
             = "<html><head><title>First</title></head>"
             + "<body>"
             + "  <iframe name='left' src='http://second' />"
             + "  some stuff"
             + "</html>";
        final String secondContent = "<html><head><title>Second</title></head><body></body></html>";

        final WebClient webClient = new WebClient();

        final FakeWebConnection webConnection = new FakeWebConnection( webClient );
        webConnection.setResponse(
            new URL("http://first"), firstContent, 200, "OK", "text/html", Collections.EMPTY_LIST );
        webConnection.setResponse(
            new URL("http://second"), secondContent,200,"OK","text/html",Collections.EMPTY_LIST );

        webClient.setWebConnection( webConnection );

        final HtmlPage firstPage = ( HtmlPage )webClient.getPage(
                new URL( "http://first" ), SubmitMethod.POST, Collections.EMPTY_LIST );
        assertEquals( "First", firstPage.getTitleText() );

        final WebWindow secondWebWindow = webClient.getWebWindowByName("left");
        assertInstanceOf( secondWebWindow, HtmlInlineFrame.class );
        assertSame( firstPage, ((HtmlInlineFrame)secondWebWindow).getPage() );
        assertEquals( "Second", ((HtmlPage)secondWebWindow.getEnclosedPage()).getTitleText() );
    }
}
