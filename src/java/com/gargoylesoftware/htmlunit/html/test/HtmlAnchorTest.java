/*
 *  Copyright (C) 2002, 2003 Gargoyle Software Inc. All rights reserved.
 *
 *  This file is part of HtmlUnit. For details on use and redistribution
 *  please refer to the license.html file included with these sources.
 */
package com.gargoylesoftware.htmlunit.html.test;

import com.gargoylesoftware.htmlunit.CollectingAlertHandler;
import com.gargoylesoftware.htmlunit.SubmitMethod;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.test.FakeWebConnection;
import com.gargoylesoftware.htmlunit.test.WebTestCase;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *  Tests for HtmlAnchor
 *
 * @version  $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 */
public class HtmlAnchorTest extends WebTestCase {

    /**
     *  Create an instance
     *
     * @param  name Name of the test
     */
    public HtmlAnchorTest( final String name ) {
        super( name );
    }


    public void testClick() throws Exception {

        final String htmlContent
                 = "<html><head><title>foo</title></head><body>"
                 + "<a href='http://www.foo1.com' id='a1'>link to foo1</a>"
                 + "<a href='http://www.foo2.com' id='a2'>link to foo2</a>"
                 + "<a href='http://www.foo3.com' id='a3'>link to foo3</a>"
                 + "</body></html>";
        final WebClient client = new WebClient();

        final FakeWebConnection webConnection = new FakeWebConnection( client );
        webConnection.setContent( htmlContent );
        client.setWebConnection( webConnection );

        final HtmlPage page = ( HtmlPage )client.getPage(
                new URL( "http://www.gargoylesoftware.com" ),
                SubmitMethod.POST, Collections.EMPTY_LIST );
        final HtmlAnchor anchor = ( HtmlAnchor )page.getHtmlElementById( "a2" );

        // Test that the correct value is being passed back up to the server
        final HtmlPage secondPage = ( HtmlPage )anchor.click();

        final List expectedParameters = Collections.EMPTY_LIST;

        assertEquals( "url", "http://www.foo2.com",
            secondPage.getWebResponse().getUrl().toExternalForm() );
        assertEquals( "method", SubmitMethod.GET, webConnection.getLastMethod() );
        assertEquals( "parameters", expectedParameters, webConnection.getLastParameters() );
        assertNotNull( secondPage );
    }


    public void testClick_onClickHandler()
        throws Exception {

        final String firstContent
                 = "<html><head><title>First</title></head><body>"
                 + "<a href='http://www.foo1.com' id='a1'>link to foo1</a>"
                 + "<a href='http://second' id='a2' "
                 + "onClick='alert(\"clicked\")'>link to foo2</a>"
                 + "<a href='http://www.foo3.com' id='a3'>link to foo3</a>"
                 + "</body></html>";
        final String secondContent
             = "<html><head><title>Second</title></head><body></body></html>";

        final WebClient client = new WebClient();
        final List collectedAlerts = new ArrayList();
        client.setAlertHandler( new CollectingAlertHandler(collectedAlerts) );

        final FakeWebConnection webConnection = new FakeWebConnection( client );
        webConnection.setResponse(
            new URL("http://first"), firstContent, 200, "OK", "text/html",
            Collections.EMPTY_LIST );
        webConnection.setResponse(
            new URL("http://second"), secondContent, 200, "OK", "text/html",
            Collections.EMPTY_LIST );
        client.setWebConnection( webConnection );

        final HtmlPage page = ( HtmlPage )client.getPage(
                new URL( "http://first" ),
                SubmitMethod.POST, Collections.EMPTY_LIST );
        final HtmlAnchor anchor = ( HtmlAnchor )page.getHtmlElementById( "a2" );

        assertEquals( Collections.EMPTY_LIST, collectedAlerts );

        final HtmlPage secondPage = ( HtmlPage )anchor.click();

        assertEquals( Collections.singletonList("clicked"), collectedAlerts );
        assertEquals( "Second", secondPage.getTitleText() );
    }


    public void testClick_onClickHandler_returnFalse()
        throws Exception {

        final String firstContent
                 = "<html><head><title>First</title></head><body>"
                 + "<a href='http://www.foo1.com' id='a1'>link to foo1</a>"
                 + "<a href='http://second' id='a2' "
                 + "onClick='alert(\"clicked\");return false;'>link to foo2</a>"
                 + "<a href='http://www.foo3.com' id='a3'>link to foo3</a>"
                 + "</body></html>";
        final String secondContent
             = "<html><head><title>Second</title></head><body></body></html>";

        final WebClient client = new WebClient();
        final List collectedAlerts = new ArrayList();
        client.setAlertHandler( new CollectingAlertHandler(collectedAlerts) );

        final FakeWebConnection webConnection = new FakeWebConnection( client );
        webConnection.setResponse(
            new URL("http://first"), firstContent, 200, "OK", "text/html",
            Collections.EMPTY_LIST );
        webConnection.setResponse(
            new URL("http://second"), secondContent, 200, "OK", "text/html",
            Collections.EMPTY_LIST );
        client.setWebConnection( webConnection );

        final HtmlPage page = ( HtmlPage )client.getPage(
                new URL( "http://first" ),
                SubmitMethod.POST, Collections.EMPTY_LIST );
        final HtmlAnchor anchor = ( HtmlAnchor )page.getHtmlElementById( "a2" );

        assertEquals( Collections.EMPTY_LIST, collectedAlerts );

        final HtmlPage secondPage = ( HtmlPage )anchor.click();

        assertEquals( Collections.singletonList("clicked"), collectedAlerts );
        assertSame( page, secondPage );
    }


    public void testClick_onClickHandler_javascriptDisabled() throws Exception {

        final String htmlContent
                 = "<html><head><title>foo</title></head><body>"
                 + "<a href='http://www.foo1.com' id='a1'>link to foo1</a>"
                 + "<a href='http://www.foo2.com' id='a2' "
                 + "onClick='alert(\"clicked\")'>link to foo2</a>"
                 + "<a href='http://www.foo3.com' id='a3'>link to foo3</a>"
                 + "</body></html>";
        final WebClient client = new WebClient();
        client.setJavaScriptEnabled(false);

        final List collectedAlerts = new ArrayList();
        client.setAlertHandler( new CollectingAlertHandler(collectedAlerts) );

        final FakeWebConnection webConnection = new FakeWebConnection( client );
        webConnection.setContent( htmlContent );
        client.setWebConnection( webConnection );

        final HtmlPage page = ( HtmlPage )client.getPage(
                new URL( "http://www.gargoylesoftware.com" ),
                SubmitMethod.POST, Collections.EMPTY_LIST );
        final HtmlAnchor anchor = ( HtmlAnchor )page.getHtmlElementById( "a2" );

        assertEquals( Collections.EMPTY_LIST, collectedAlerts );

        final HtmlPage secondPage = ( HtmlPage )anchor.click();

        assertEquals( Collections.EMPTY_LIST, collectedAlerts );
        final List expectedParameters = Collections.EMPTY_LIST;

        assertEquals( "url", "http://www.foo2.com",
            secondPage.getWebResponse().getUrl().toExternalForm() );
        assertEquals( "method", SubmitMethod.GET, webConnection.getLastMethod() );
        assertEquals( "parameters", expectedParameters, webConnection.getLastParameters() );
        assertNotNull( secondPage );
    }


    public void testClick_javascriptUrl() throws Exception {

        final String htmlContent
                 = "<html><head><title>foo</title></head><body>"
                 + "<a href='http://www.foo1.com' id='a1'>link to foo1</a>"
                 + "<a href='javascript:alert(\"clicked\")' id='a2'>link to foo2</a>"
                 + "<a href='http://www.foo3.com' id='a3'>link to foo3</a>"
                 + "</body></html>";
        final WebClient client = new WebClient();
        final List collectedAlerts = new ArrayList();
        client.setAlertHandler( new CollectingAlertHandler(collectedAlerts) );

        final FakeWebConnection webConnection = new FakeWebConnection( client );
        webConnection.setContent( htmlContent );
        client.setWebConnection( webConnection );

        final HtmlPage page = ( HtmlPage )client.getPage(
                new URL( "http://www.gargoylesoftware.com" ),
                SubmitMethod.POST, Collections.EMPTY_LIST );
        final HtmlAnchor anchor = ( HtmlAnchor )page.getHtmlElementById( "a2" );

        assertEquals( Collections.EMPTY_LIST, collectedAlerts );

        final HtmlPage secondPage = ( HtmlPage )anchor.click();

        assertEquals( Collections.singletonList("clicked"), collectedAlerts );
        assertSame( page, secondPage );
    }


    public void testClick_javascriptUrl_javascriptDisabled() throws Exception {

        final String htmlContent
                 = "<html><head><title>foo</title></head><body>"
                 + "<a href='http://www.foo1.com' id='a1'>link to foo1</a>"
                 + "<a href='javascript:alert(\"clicked\")' id='a2'>link to foo2</a>"
                 + "<a href='http://www.foo3.com' id='a3'>link to foo3</a>"
                 + "</body></html>";
        final WebClient client = new WebClient();
        client.setJavaScriptEnabled(false);

        final List collectedAlerts = new ArrayList();
        client.setAlertHandler( new CollectingAlertHandler(collectedAlerts) );

        final FakeWebConnection webConnection = new FakeWebConnection( client );
        webConnection.setContent( htmlContent );
        client.setWebConnection( webConnection );

        final HtmlPage page = ( HtmlPage )client.getPage(
                new URL( "http://www.gargoylesoftware.com" ),
                SubmitMethod.POST, Collections.EMPTY_LIST );
        final HtmlAnchor anchor = ( HtmlAnchor )page.getHtmlElementById( "a2" );

        assertEquals( Collections.EMPTY_LIST, collectedAlerts );

        final HtmlPage secondPage = ( HtmlPage )anchor.click();

        assertEquals( Collections.EMPTY_LIST, collectedAlerts );
        assertSame( page, secondPage );
    }


    public void testClick_javascriptUrl_InvalidReturn_RegressionTest() throws Exception {

        final String htmlContent
                 = "<html><head><SCRIPT lang=\"JavaScript\">"
                 + "function doSubmit(formName){"
                 + "    return false;"
                 + "}"
                 + "</SCRIPT></head><body><form name=\"formName\" method=\"POST\"action=\"../foo\">"
                 + "<a href=\".\" id=\"testJavascript\" name=\"testJavascript\""
                 + "onclick=\"return false;\">Test Link </a>"
                 + "<input type=\"submit\" value=\"Login\" "
                 + "name=\"loginButton\"></form></body></html>";
        final WebClient client = new WebClient();
        final List collectedAlerts = new ArrayList();
        client.setAlertHandler( new CollectingAlertHandler(collectedAlerts) );

        final FakeWebConnection webConnection = new FakeWebConnection( client );
        webConnection.setContent( htmlContent );
        client.setWebConnection( webConnection );

        final HtmlPage page = ( HtmlPage )client.getPage(
                new URL( "http://www.gargoylesoftware.com" ),
                SubmitMethod.POST, Collections.EMPTY_LIST );
        final HtmlAnchor testAnchor = (HtmlAnchor)page.getAnchorByName("testJavascript");
        testAnchor.click();  // blows up here
    }
}

