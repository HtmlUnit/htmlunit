/*
 *  Copyright (C) 2002, 2003 Gargoyle Software Inc. All rights reserved.
 *
 *  This file is part of HtmlUnit. For details on use and redistribution
 *  please refer to the license.html file included with these sources.
 */
package com.gargoylesoftware.htmlunit.html.test;

import com.gargoylesoftware.htmlunit.SubmitMethod;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlInlineFrame;
import com.gargoylesoftware.htmlunit.test.FakeWebConnection;
import com.gargoylesoftware.htmlunit.test.WebTestCase;
import java.net.URL;
import java.util.Collections;

/**
 *  Tests for HtmlInlineFrame
 *
 * @version  $Revision$
 * @author  <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 */
public class HtmlInlineFrameTest extends WebTestCase {
    /**
     *  Create an instance
     *
     * @param  name The name of the test
     */
    public HtmlInlineFrameTest( final String name ) {
        super( name );
    }


    public void testSetSrcAttribute() throws Exception {
        final String firstContent
                 = "<html><head><title>First</title></head><body>"
                 + "<iframe id='iframe1' src='http://second'>"
                 + "</body></html>";
        final String secondContent = "<html><head><title>Second</title></head><body></body></html>";
        final String thirdContent = "<html><head><title>Third</title></head><body></body></html>";
        final WebClient client = new WebClient();

        final FakeWebConnection webConnection = new FakeWebConnection( client );
        webConnection.setResponse(
            new URL("http://first"), firstContent, 200, "OK", "text/html", Collections.EMPTY_LIST );
        webConnection.setResponse(
            new URL("http://second"),secondContent,200,"OK","text/html",Collections.EMPTY_LIST );
        webConnection.setResponse(
            new URL("http://third"),thirdContent,200,"OK","text/html",Collections.EMPTY_LIST );

        client.setWebConnection( webConnection );

        final HtmlPage page = ( HtmlPage )client.getPage(
                new URL( "http://first" ),
                SubmitMethod.POST, Collections.EMPTY_LIST );
        assertEquals( "First", page.getTitleText() );

        final HtmlInlineFrame iframe = (HtmlInlineFrame)page.getHtmlElementById("iframe1");
        assertEquals( "http://second", iframe.getSrcAttribute() );
        assertEquals( "Second", ((HtmlPage)iframe.getEnclosedPage()).getTitleText() );

        iframe.setSrcAttribute("http://third");
        assertEquals( "http://third", iframe.getSrcAttribute() );
        assertEquals( "Third", ((HtmlPage)iframe.getEnclosedPage()).getTitleText() );
    }


    public void testSetSrcAttribute_ViaJavaScript() throws Exception {
        if( false ) {
            notImplemented();
            return;
        }
        final String firstContent
                 = "<html><head><title>First</title></head><body>"
                 + "<iframe id='iframe1' src='http://second'>"
                 + "<script type='text/javascript'>document.getElementById ('iframe1').src = 'http://third';"
                 + "</script></body></html>";
        final String secondContent = "<html><head><title>Second</title></head><body></body></html>";
        final String thirdContent = "<html><head><title>Third</title></head><body></body></html>";
        final WebClient client = new WebClient();

        final FakeWebConnection webConnection = new FakeWebConnection( client );
        webConnection.setResponse(
            new URL("http://first"), firstContent, 200, "OK", "text/html", Collections.EMPTY_LIST );
        webConnection.setResponse(
            new URL("http://second"),secondContent,200,"OK","text/html",Collections.EMPTY_LIST );
        webConnection.setResponse(
            new URL("http://third"),thirdContent,200,"OK","text/html",Collections.EMPTY_LIST );

        client.setWebConnection( webConnection );

        final HtmlPage page = ( HtmlPage )client.getPage(
                new URL( "http://first" ),
                SubmitMethod.POST, Collections.EMPTY_LIST );
        assertEquals( "First", page.getTitleText() );

        final HtmlInlineFrame iframe = (HtmlInlineFrame)page.getHtmlElementById("iframe1");
        assertEquals( "http://third", iframe.getSrcAttribute() );
        assertEquals( "Third", ((HtmlPage)iframe.getEnclosedPage()).getTitleText() );
    }
}
