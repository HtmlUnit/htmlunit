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
package com.gargoylesoftware.htmlunit.javascript.host;

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
import com.gargoylesoftware.htmlunit.FakeWebConnection;
import com.gargoylesoftware.htmlunit.WebTestCase;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Tests for Window
 *
 * @version  $Revision$
 * @author  <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author  <a href="mailto:chen_jun@users.sourceforge.net">Chen Jun</a>
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

        final List collectedAlerts = new ArrayList();
        webClient.setAlertHandler( new CollectingAlertHandler(collectedAlerts) );
        
        final String firstContent
             = "<html><head><title>First</title></head><body>"
             + "<form name='form1'>"
             + "    <a id='link' onClick='open(\"http://second\", \"MyNewWindow\").focus(); "
             + "return false;'>Click me</a>"
             + "</form>"
             + "</body></html>";
        final String secondContent
             = "<html><head><title>Second</title></head><body>"
             + "<script>alert(self.name)</script>"
             + "</body></html>";

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

        final WebWindowEvent changedEvent = (WebWindowEvent)eventCatcher.getEventAt(2);
        assertNull( changedEvent.getOldPage() );
        assertEquals( "Second", ((HtmlPage)changedEvent.getNewPage()).getTitleText() );
        
        assertEquals(
            Collections.singletonList("MyNewWindow"),
            collectedAlerts);
    }


    /**
     * _self is a magic name.  If we call open(url, '_self') then the current window must be
     * reloaded.
     * @throws Exception If the test fails.
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
     * @throws Exception if the test fails
     */
    public void testAlert_NoAlertHandler() throws Exception {
        final WebClient webClient = new WebClient();
        final FakeWebConnection webConnection = new FakeWebConnection( webClient );

        final String firstContent
            = "<html><head><title>First</title><script>function doTest(){alert('foo')}</script></head>"
            + "<body onload='doTest()'></body></html>";

        webConnection.setResponse(
            new URL("http://first"), firstContent, 200, "OK", "text/html", Collections.EMPTY_LIST );
        webClient.setWebConnection( webConnection );

        final HtmlPage firstPage = ( HtmlPage )webClient.getPage(
                new URL( "http://first" ), SubmitMethod.POST, Collections.EMPTY_LIST );
        assertEquals( "First", firstPage.getTitleText() );
    }


    public void testParentAndTop() throws Exception {

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
            = "<html><head><title>First</title><script>function doTest(){alert(confirm('foo'))}</script>"
            + "</head><body onload='doTest()'></body></html>";

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
            = "<html><head><title>First</title><script>function doTest(){alert(confirm('foo'))}</script>"
            + "</head><body onload='doTest()'></body></html>";

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
            = "<html><head><title>First</title><script>function doTest(){alert(prompt('foo'))}</script>"
            + "</head><body onload='doTest()'></body></html>";

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
            = "<html><head><title>First</title><script>function doTest(){alert(prompt('foo'))}</script>"
            + "</head><body onload='doTest()'></body></html>";

        webConnection.setResponse(
            new URL("http://first"), firstContent, 200, "OK", "text/html", Collections.EMPTY_LIST );
        webClient.setWebConnection( webConnection );

        final HtmlPage firstPage = ( HtmlPage )webClient.getPage(
                new URL( "http://first" ), SubmitMethod.POST, Collections.EMPTY_LIST );
        assertEquals( "First", firstPage.getTitleText() );

        assertEquals( Collections.EMPTY_LIST, collectedPrompts );
        assertEquals( Collections.singletonList("null"), collectedAlerts );
    }


    public void testOpener() throws Exception {
        final WebClient webClient = new WebClient();
        final FakeWebConnection webConnection = new FakeWebConnection( webClient );
        final List collectedAlerts = new ArrayList();

        webClient.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        final String firstContent
             = "<html><head><title>First</title><script>"
             + "function runtest() {\n"
             + "    alert('one')\n"
             + "    open('http://second', 'foo')"
             + "}\n"
             + "function callAlert( text ) {\n"
             + "    alert(text)"
             + "}\n"
             + "</script></head><body onload='runtest()'>"
             + "</body></html>";
        final String secondContent
             = "<html><head><title>Second</title><script>"
             + "function runtest() {\n"
             + "    opener.callAlert('two')\n"
             + "    document.form1.submit()\n"
             + "}\n"
             + "</script></head><body onload='runtest()'>"
             + "<form name='form1' action='http://third'><input type='submit'></form>"
             + "</body></html>";
        final String thirdContent
             = "<html><head><title>Third</title><script>"
             + "function runtest() {\n"
             + "    opener.callAlert('three')"
             + "}\n"
             + "</script></head><body onload='runtest()'>"
             + "</body></html>";

        webConnection.setResponse(
            new URL("http://first"), firstContent, 200, "OK", "text/html", Collections.EMPTY_LIST );
        webConnection.setResponse(
            new URL("http://second"), secondContent, 200, "OK", "text/html", Collections.EMPTY_LIST );
        webConnection.setResponse(
            new URL("http://third"), thirdContent, 200, "OK", "text/html", Collections.EMPTY_LIST );
        webClient.setWebConnection( webConnection );

        final HtmlPage firstPage = ( HtmlPage )webClient.getPage(
                new URL( "http://first" ), SubmitMethod.POST, Collections.EMPTY_LIST );
        assertEquals( "First", firstPage.getTitleText() );

        final List expectedAlerts = Arrays.asList( new String[]{ "one", "two", "three" } );
        assertEquals( expectedAlerts, collectedAlerts );
    }


    public void testSetTimeout() throws Exception {
        final WebClient webClient = new WebClient();
        final FakeWebConnection webConnection = new FakeWebConnection( webClient );
        final List collectedAlerts = Collections.synchronizedList(new ArrayList());

        webClient.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        final String firstContent
            = "<html><body><script language='JavaScript'>window.setTimeout('alert(\"Yo!\")',1);"
            + "</script></body></html>";

        webConnection.setResponse(
            new URL("http://first"), firstContent, 200, "OK", "text/html", Collections.EMPTY_LIST );
        webClient.setWebConnection( webConnection );

        webClient.getPage(
            new URL( "http://first" ), SubmitMethod.POST, Collections.EMPTY_LIST );

        final int waitTime = 50;
        final int maxTime = 1000;
        for( int time = 0; time < maxTime; time+=waitTime ) {
            if( collectedAlerts.isEmpty() == false ) {
                assertEquals( Collections.singletonList("Yo!"), collectedAlerts );
                return;
            }
            Thread.sleep(waitTime);
        }
        fail("No alerts written within "+maxTime+"ms");
    }
    
    
    public void testAboutURL() throws Exception {
        final WebClient webClient = new WebClient();
        final FakeWebConnection webConnection =
            new FakeWebConnection(webClient);
        final String firstContent =
            "<html><body><script language='JavaScript'>"
                + "w2=window.open(\"about:blank\", \"AboutBlank\");"
                + "w2.document.open();"
                + "w2.document.write(\"<html><head><title>hello</title></head><body></body></html>\");"
                + "w2.document.close();"
                + "</script></body></html>";
        webConnection.setResponse(
            new URL("http://first"),
            firstContent,
            200,
            "OK",
            "text/html",
            Collections.EMPTY_LIST);
        webClient.setWebConnection(webConnection);

        webClient.getPage(
            new URL("http://first"),
            SubmitMethod.POST,
            Collections.EMPTY_LIST);
        final WebWindow webWindow = webClient.getWebWindowByName("AboutBlank");
        assertNotNull(webWindow);

        //  final HtmlPage page = (HtmlPage) webWindow.getEnclosedPage();
        //  assertEquals("<html><head><title>hello</title></head><body></body></html>",page.getDocument().toString());
    }

    /**
     * 
     * @throws Exception If the test fails
     */
    public void testWindowFrames() throws Exception {
        final WebClient webClient = new WebClient();
        final FakeWebConnection webConnection =
            new FakeWebConnection(webClient);
        final List collectedAlerts = new ArrayList();

        webClient.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        final String firstContent =
            "<html><body><script language='JavaScript'>"
                + "if (typeof top.frames[\"anyXXXname\"] == \"undefined\") {"
                + "alert('one')};"
                + "</script></body></html>";
        webConnection.setResponse(
            new URL("http://first"),
            firstContent,
            200,
            "OK",
            "text/html",
            Collections.EMPTY_LIST);
        webClient.setWebConnection(webConnection);

        webClient.getPage(
            new URL("http://first"),
            SubmitMethod.POST,
            Collections.EMPTY_LIST);
        assertEquals(1, collectedAlerts.size());
    }
}
