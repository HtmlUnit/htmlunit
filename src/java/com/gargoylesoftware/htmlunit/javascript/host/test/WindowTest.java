/*
 *  Copyright (C) 2002 Gargoyle Software Inc. All rights reserved.
 *
 *  This file is part of HtmlUnit. For details on use and redistribution
 *  please refer to the license.html file included with these sources.
 */
package com.gargoylesoftware.htmlunit.javascript.host.test;

import com.gargoylesoftware.base.testing.EventCatcher;
import com.gargoylesoftware.htmlunit.CollectingAlertHandler;
import com.gargoylesoftware.htmlunit.ConfirmHandler;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.PromptHandler;
import com.gargoylesoftware.htmlunit.SubmitMethod;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebWindow;
import com.gargoylesoftware.htmlunit.WebWindowEvent;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.test.FakeWebConnection;
import com.gargoylesoftware.htmlunit.test.WebTestCase;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @version  $Revision$
 * @author  <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 */
public class WindowTest extends WebTestCase {
    public WindowTest( final String name ) {
        super(name);
    }


    public void testSetLocation() throws Exception {
        final WebClient webClient = new WebClient();
        final FakeWebConnection webConnection = new FakeWebConnection( webClient );

        final String firstContent
             = "<html><head><title>First</title></head><body>"
             + "<form name='form1'>"
             + "    <a id='link' onClick='location=\"http://second\"; return false;'>Click me</a>"
             + "</form>"
             + "</body></html>";
        final String secondContent
             = "<html><head><title>Second</title></head><body></body></html>";

        webConnection.setResponse(
            new URL("http://first"), firstContent, 200, "OK", "text/html",
            Collections.EMPTY_LIST );
        webConnection.setResponse(
            new URL("http://second"), secondContent, 200, "OK", "text/html",
            Collections.EMPTY_LIST );
        webClient.setWebConnection( webConnection );

        final HtmlPage firstPage = ( HtmlPage )webClient.getPage(
                new URL( "http://first" ), SubmitMethod.POST, Collections.EMPTY_LIST );
        assertEquals( "First", firstPage.getTitleText() );

        final HtmlAnchor anchor = (HtmlAnchor)firstPage.getHtmlElementById("link");
        final HtmlPage secondPage = (HtmlPage)anchor.click();
        assertNotNull("secondPage", secondPage);
        assertEquals( "Second", secondPage.getTitleText() );
        assertSame( webClient.getCurrentWindow(), secondPage.getEnclosingWindow() );
    }


    public void testOpenWindow() throws Exception {
        final WebClient webClient = new WebClient();
        final FakeWebConnection webConnection = new FakeWebConnection( webClient );

        final String firstContent
             = "<html><head><title>First</title></head><body>"
             + "<form name='form1'>"
             + "    <a id='link' onClick='open(\"http://second\", \"MyNewWindow\").focus(); "
             + "return false;'>Click me</a>"
             + "</form>"
             + "</body></html>";
        final String secondContent
             = "<html><head><title>Second</title></head><body></body></html>";

        final EventCatcher eventCatcher = new EventCatcher();
        eventCatcher.listenTo( webClient );

        webConnection.setResponse(
            new URL("http://first"), firstContent, 200, "OK", "text/html", Collections.EMPTY_LIST );
        webConnection.setResponse(
            new URL("http://second"), secondContent, 200, "OK", "text/html",
            Collections.EMPTY_LIST );
        webClient.setWebConnection( webConnection );

        final HtmlPage firstPage = ( HtmlPage )webClient.getPage(
                new URL( "http://first" ), SubmitMethod.POST, Collections.EMPTY_LIST );
        assertEquals( "First", firstPage.getTitleText() );

        final HtmlAnchor anchor = (HtmlAnchor)firstPage.getHtmlElementById("link");
        final HtmlPage secondPage = (HtmlPage)anchor.click();
        assertSame( firstPage, secondPage );

        // Expecting contentChanged, opened, contentChanged
        assertEquals( 3, eventCatcher.getEventCount() );

        final WebWindow firstWebWindow
            = (WebWindow)((WebWindowEvent)eventCatcher.getEventAt(0)).getSource();
        final WebWindow secondWebWindow
            = (WebWindow)((WebWindowEvent)eventCatcher.getEventAt(2)).getSource();
        assertSame( webClient.getCurrentWindow(), firstWebWindow);
        assertEquals( "MyNewWindow", secondWebWindow.getName() );

        assertEquals( "First", ((HtmlPage)firstWebWindow.getEnclosedPage()).getTitleText());
        assertEquals( "Second", ((HtmlPage)secondWebWindow.getEnclosedPage()).getTitleText());
    }


    /**
     * _self is a magic name.  If we call open(url, '_self') then the current window must be
     * reloaded.
     */
    public void testOpenWindow_self() throws Exception {
        final WebClient webClient = new WebClient();
        final FakeWebConnection webConnection = new FakeWebConnection( webClient );

        final String firstContent
             = "<html><head><title>First</title></head><body>"
             + "<form name='form1'>"
             + "    <a id='link' onClick='open(\"http://second\", \"_self\"); "
             + "return false;'>Click me</a>"
             + "</form>"
             + "</body></html>";
        final String secondContent
             = "<html><head><title>Second</title></head><body></body></html>";

        final EventCatcher eventCatcher = new EventCatcher();

        webConnection.setResponse(
            new URL("http://first"), firstContent, 200, "OK", "text/html", Collections.EMPTY_LIST );
        webConnection.setResponse(
            new URL("http://second"), secondContent, 200, "OK", "text/html",
            Collections.EMPTY_LIST );
        webClient.setWebConnection( webConnection );

        final HtmlPage firstPage = ( HtmlPage )webClient.getPage(
                new URL( "http://first" ), SubmitMethod.POST, Collections.EMPTY_LIST );
        assertEquals( "First", firstPage.getTitleText() );

        eventCatcher.listenTo( webClient );

        final WebWindow firstWebWindow = firstPage.getEnclosingWindow();

        final HtmlAnchor anchor = (HtmlAnchor)firstPage.getHtmlElementById("link");
        final HtmlPage secondPage = (HtmlPage)anchor.click();
        assertEquals( "First", firstPage.getTitleText() );
        assertEquals( "Second", secondPage.getTitleText() );

        assertEquals( 1, eventCatcher.getEventCount() );

        final WebWindow secondWebWindow
            = (WebWindow)((WebWindowEvent)eventCatcher.getEventAt(0)).getSource();
        assertSame( webClient.getCurrentWindow(), firstWebWindow);
        assertSame( firstWebWindow, secondWebWindow );
    }


    /**
     * Regression test to reproduce a known bug
     */
    public void testAlert_NoAlertHandler() throws Exception {
        final WebClient webClient = new WebClient();
        final FakeWebConnection webConnection = new FakeWebConnection( webClient );

        final String firstContent
             = "<html><head><title>First</title><script>alert('foo')</script></head><body>"
             + "</body></html>";

        webConnection.setResponse(
            new URL("http://first"), firstContent, 200, "OK", "text/html", Collections.EMPTY_LIST );
        webClient.setWebConnection( webConnection );

        final HtmlPage firstPage = ( HtmlPage )webClient.getPage(
                new URL( "http://first" ), SubmitMethod.POST, Collections.EMPTY_LIST );
        assertEquals( "First", firstPage.getTitleText() );
    }


    public void testParentAndTop()
        throws Exception {

        final String firstContent
             = "<html><head><title>First</title></head><body>"
             + "  <iframe name='left' src='http://second' />"
             + "</body></html>";
        final String secondContent
             = "<html><head><title>Second</title></head><body>"
             + "  <iframe name='innermost' src='http://third/' />"
             + "</body></html>";
        final String thirdContent
             = "<html><head><title>Third</title><script>"
             + "function doAlert() {\n"
             + "    alert(parent != this);\n"
             + "    alert(top != this);\n"
             + "    alert(parent != top);\n"
             + "    alert(parent.parent == top);\n"
             + "    alert(parent.frames[0] == this);\n"
             + "    alert(top.frames[0] == parent);\n"
             + "}\n"
             + "</script></head>"
             + "<body><a id='clickme' onClick='doAlert()'>foo</a></body></html>";

        final WebClient webClient = new WebClient();
        final List collectedAlerts = new ArrayList();
        webClient.setAlertHandler( new CollectingAlertHandler(collectedAlerts) );

        final FakeWebConnection webConnection = new FakeWebConnection( webClient );
        webConnection.setResponse(
            new URL("http://first"), firstContent, 200, "OK", "text/html", Collections.EMPTY_LIST );
        webConnection.setResponse(
            new URL("http://second"), secondContent,200,"OK","text/html",Collections.EMPTY_LIST );
        webConnection.setResponse(
            new URL("http://third/"), thirdContent,200,"OK","text/html",Collections.EMPTY_LIST );

        webClient.setWebConnection( webConnection );

        final HtmlPage firstPage = ( HtmlPage )webClient.getPage(
                new URL( "http://first" ), SubmitMethod.POST, Collections.EMPTY_LIST );
        assertEquals( "First", firstPage.getTitleText() );

        final WebWindow innermostWebWindow = webClient.getWebWindowByName("innermost");
        final HtmlPage innermostPage = (HtmlPage)innermostWebWindow.getEnclosedPage();
        ((HtmlAnchor)innermostPage.getHtmlElementById("clickme")).click();

        assertEquals(
            Arrays.asList( new String[] {"true", "true", "true", "true", "true", "true"} ),
            collectedAlerts);
    }


    public void testConfirm() throws Exception {
        final WebClient webClient = new WebClient();
        final FakeWebConnection webConnection = new FakeWebConnection( webClient );
        final List collectedAlerts = new ArrayList();
        final List collectedConfirms = new ArrayList();

        webClient.setAlertHandler(new CollectingAlertHandler(collectedAlerts));
        webClient.setConfirmHandler( new ConfirmHandler() {
            public boolean handleConfirm( final Page page, final String message ) {
                collectedConfirms.add(message);
                return true;
            }
        } );

        final String firstContent
             = "<html><head><title>First</title><script>alert(confirm('foo'))</script></head><body>"
             + "</body></html>";

        webConnection.setResponse(
            new URL("http://first"), firstContent, 200, "OK", "text/html", Collections.EMPTY_LIST );
        webClient.setWebConnection( webConnection );

        final HtmlPage firstPage = ( HtmlPage )webClient.getPage(
                new URL( "http://first" ), SubmitMethod.POST, Collections.EMPTY_LIST );
        assertEquals( "First", firstPage.getTitleText() );

        assertEquals( Collections.singletonList("foo"), collectedConfirms );
        assertEquals( Collections.singletonList("true"), collectedAlerts );
    }


    public void testConfirm_noConfirmHandler() throws Exception {
        final WebClient webClient = new WebClient();
        final FakeWebConnection webConnection = new FakeWebConnection( webClient );
        final List collectedAlerts = new ArrayList();
        final List collectedConfirms = new ArrayList();

        webClient.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        final String firstContent
             = "<html><head><title>First</title><script>alert(confirm('foo'))</script></head><body>"
             + "</body></html>";

        webConnection.setResponse(
            new URL("http://first"), firstContent, 200, "OK", "text/html", Collections.EMPTY_LIST );
        webClient.setWebConnection( webConnection );

        final HtmlPage firstPage = ( HtmlPage )webClient.getPage(
                new URL( "http://first" ), SubmitMethod.POST, Collections.EMPTY_LIST );
        assertEquals( "First", firstPage.getTitleText() );

        assertEquals( Collections.EMPTY_LIST, collectedConfirms );
        assertEquals( Collections.singletonList("false"), collectedAlerts );
    }


    public void testPrompt() throws Exception {
        final WebClient webClient = new WebClient();
        final FakeWebConnection webConnection = new FakeWebConnection( webClient );
        final List collectedAlerts = new ArrayList();
        final List collectedPrompts = new ArrayList();

        webClient.setAlertHandler(new CollectingAlertHandler(collectedAlerts));
        webClient.setPromptHandler( new PromptHandler() {
            public String handlePrompt( final Page page, final String message ) {
                collectedPrompts.add(message);
                return "Flintstone";
            }
        } );

        final String firstContent
             = "<html><head><title>First</title><script>alert(prompt('foo'))</script></head><body>"
             + "</body></html>";

        webConnection.setResponse(
            new URL("http://first"), firstContent, 200, "OK", "text/html", Collections.EMPTY_LIST );
        webClient.setWebConnection( webConnection );

        final HtmlPage firstPage = ( HtmlPage )webClient.getPage(
                new URL( "http://first" ), SubmitMethod.POST, Collections.EMPTY_LIST );
        assertEquals( "First", firstPage.getTitleText() );

        assertEquals( Collections.singletonList("foo"), collectedPrompts );
        assertEquals( Collections.singletonList("Flintstone"), collectedAlerts );
    }


    public void testPrompt_noPromptHandler() throws Exception {
        final WebClient webClient = new WebClient();
        final FakeWebConnection webConnection = new FakeWebConnection( webClient );
        final List collectedAlerts = new ArrayList();
        final List collectedPrompts = new ArrayList();

        webClient.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        final String firstContent
             = "<html><head><title>First</title><script>alert(prompt('foo'))</script></head><body>"
             + "</body></html>";

        webConnection.setResponse(
            new URL("http://first"), firstContent, 200, "OK", "text/html", Collections.EMPTY_LIST );
        webClient.setWebConnection( webConnection );

        final HtmlPage firstPage = ( HtmlPage )webClient.getPage(
                new URL( "http://first" ), SubmitMethod.POST, Collections.EMPTY_LIST );
        assertEquals( "First", firstPage.getTitleText() );

        assertEquals( Collections.EMPTY_LIST, collectedPrompts );
        assertEquals( Collections.singletonList("null"), collectedAlerts );
    }
}
