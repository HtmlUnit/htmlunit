/*
 *  Copyright (C) 2002, 2003 Gargoyle Software Inc. All rights reserved.
 *
 *  This file is part of HtmlUnit. For details on use and redistribution
 *  please refer to the license.html file included with these sources.
 */
package com.gargoylesoftware.htmlunit.html.test;

import com.gargoylesoftware.htmlunit.SubmitMethod;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebWindow;
import com.gargoylesoftware.htmlunit.html.HtmlFrame;
import com.gargoylesoftware.htmlunit.html.HtmlInlineFrame;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.test.FakeWebConnection;
import com.gargoylesoftware.htmlunit.test.WebTestCase;
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
