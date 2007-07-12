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
package com.gargoylesoftware.htmlunit.javascript.host;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.gargoylesoftware.base.testing.EventCatcher;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.CollectingAlertHandler;
import com.gargoylesoftware.htmlunit.ConfirmHandler;
import com.gargoylesoftware.htmlunit.MockWebConnection;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.PromptHandler;
import com.gargoylesoftware.htmlunit.StatusHandler;
import com.gargoylesoftware.htmlunit.ThreadManager;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebTestCase;
import com.gargoylesoftware.htmlunit.WebWindow;
import com.gargoylesoftware.htmlunit.WebWindowEvent;
import com.gargoylesoftware.htmlunit.WebWindowNotFoundException;
import com.gargoylesoftware.htmlunit.html.ClickableElement;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlButton;
import com.gargoylesoftware.htmlunit.html.HtmlInlineFrame;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;

/**
 * Tests for {@link Window}.
 *
 * @version $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author <a href="mailto:chen_jun@users.sourceforge.net">Chen Jun</a>
 * @author David K. Taylor
 * @author Darrell DeBoer
 * @author Marc Guillemot
 * @author Dierk Koenig
 * @author Chris Erskine
 * @author David D. Kilzer
 * @author Ahmed Ashour
 * @author Daniel Gredler
 */
public class WindowTest extends WebTestCase {

    /**
     * Creates an instance.
     * @param name The name of the test.
     */
    public WindowTest( final String name ) {
        super(name);
    }

    /**
     * @throws Exception If the test fails
     */
    public void testSetLocation() throws Exception {
        final WebClient webClient = new WebClient();
        final MockWebConnection webConnection = new MockWebConnection( webClient );

        final String firstContent
            = "<html><head><title>First</title></head><body>"
            + "<form name='form1'>"
            + "    <a id='link' onClick='location=\"http://second\"; return false;'>Click me</a>"
            + "</form>"
            + "</body></html>";
        final String secondContent
            = "<html><head><title>Second</title></head><body></body></html>";

        webConnection.setResponse(URL_FIRST, firstContent);
        webConnection.setResponse(URL_SECOND, secondContent);
        webClient.setWebConnection( webConnection );

        final HtmlPage firstPage = ( HtmlPage )webClient.getPage(URL_FIRST);
        assertEquals( "First", firstPage.getTitleText() );

        final HtmlAnchor anchor = (HtmlAnchor)firstPage.getHtmlElementById("link");
        final HtmlPage secondPage = (HtmlPage)anchor.click();
        assertNotNull("secondPage", secondPage);
        assertEquals( "Second", secondPage.getTitleText() );
        assertSame( webClient.getCurrentWindow(), secondPage.getEnclosingWindow() );
    }


    /**
     * @throws Exception If the test fails
     */
    public void testOpenWindow() throws Exception {
        final WebClient webClient = new WebClient();
        final MockWebConnection webConnection = new MockWebConnection( webClient );

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

        webConnection.setResponse(URL_FIRST, firstContent);
        webConnection.setResponse(URL_SECOND, secondContent);
        webClient.setWebConnection( webConnection );

        final HtmlPage firstPage = ( HtmlPage )webClient.getPage(URL_FIRST);
        assertEquals( "First", firstPage.getTitleText() );

        final HtmlAnchor anchor = (HtmlAnchor)firstPage.getHtmlElementById("link");
        final HtmlPage secondPage = (HtmlPage)anchor.click();
        assertNotSame( firstPage, secondPage );

        // Expecting contentChanged, opened, contentChanged
        assertEquals( 3, eventCatcher.getEventCount() );

        final WebWindow firstWebWindow
            = (WebWindow)((WebWindowEvent)eventCatcher.getEventAt(0)).getSource();
        final WebWindow secondWebWindow
            = (WebWindow)((WebWindowEvent)eventCatcher.getEventAt(2)).getSource();
        assertSame( webClient.getCurrentWindow(), secondWebWindow);
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
     * @throws Exception If the test fails
     */
    public void testOpenWindow_base() throws Exception {
        final WebClient webClient = new WebClient();
        final MockWebConnection webConnection = new MockWebConnection( webClient );

        final List collectedAlerts = new ArrayList();
        webClient.setAlertHandler( new CollectingAlertHandler(collectedAlerts) );

        final String firstContent
            = "<html><head><title>First</title><base target='MyNewWindow'></head><body>"
            + "<form name='form1'>"
            + "    <a id='link' href='http://second'>Click me</a>"
            + "</form>"
            + "</body></html>";
        final String secondContent
            = "<html><head><title>Second</title></head><body>"
            + "<script>alert(self.name)</script>"
            + "</body></html>";

        webConnection.setResponse(URL_FIRST, firstContent);
        webConnection.setResponse(URL_SECOND, secondContent);
        webClient.setWebConnection( webConnection );

        final HtmlPage firstPage = ( HtmlPage )webClient.getPage(URL_FIRST);
        assertEquals( "First", firstPage.getTitleText() );
        final WebWindow firstWebWindow = firstPage.getEnclosingWindow();
        assertEquals( firstWebWindow, firstWebWindow.getTopWindow() );

        final HtmlAnchor anchor = (HtmlAnchor)firstPage.getHtmlElementById("link");
        final HtmlPage secondPage = (HtmlPage)anchor.click();
        assertEquals( "Second", secondPage.getTitleText() );
        assertNotSame( firstPage, secondPage );

        final WebWindow secondWebWindow = secondPage.getEnclosingWindow();
        assertNotSame( firstWebWindow, secondWebWindow );
        assertEquals( "MyNewWindow", secondWebWindow.getName() );
        assertEquals( secondWebWindow, secondWebWindow.getTopWindow() );

        assertEquals(
            Collections.singletonList("MyNewWindow"),
            collectedAlerts);
    }


    /**
     * _blank is a magic name.  If we call open(url, '_blank') then a new
     * window must be loaded.
     * @throws Exception If the test fails
     */
    public void testOpenWindow_blank() throws Exception {
        final WebClient webClient = new WebClient();
        final MockWebConnection webConnection = new MockWebConnection( webClient );

        final List collectedAlerts = new ArrayList();
        webClient.setAlertHandler( new CollectingAlertHandler(collectedAlerts) );

        final String firstContent
            = "<html><head><title>First</title></head><body>"
            + "  <iframe name='secondFrame' id='secondFrame' src='http://second' />"
            + "</body></html>";
        final String secondContent
            = "<html><head><title>Second</title></head><body>"
            + "  <a id='link' "
            + "onClick='open(\"http://third\", \"_blank\").focus(); '>"
            + "Click me</a>"
            + "</body></html>";
        final String thirdContent
            = "<html><head><title>Third</title></head><body>"
            + "</body></html>";

        webConnection.setResponse(URL_FIRST, firstContent);
        webConnection.setResponse(URL_SECOND, secondContent);
        webConnection.setResponse(URL_THIRD, thirdContent);
        webClient.setWebConnection( webConnection );

        final HtmlPage firstPage = ( HtmlPage )webClient.getPage(URL_FIRST);
        assertEquals( "First", firstPage.getTitleText() );
        final WebWindow firstWindow = firstPage.getEnclosingWindow();

        final HtmlInlineFrame secondFrame =
            (HtmlInlineFrame)firstPage.getHtmlElementById("secondFrame");
        final HtmlPage secondPage = (HtmlPage)secondFrame.getEnclosedPage();
        assertEquals( "Second", secondPage.getTitleText() );
        try {
            assertEquals(secondFrame.getEnclosedWindow(),
                webClient.getWebWindowByName( "secondFrame" ));
            // Expected path
        }
        catch (final WebWindowNotFoundException exception) {
            fail( "Expected secondFrame would be found before click." );
        }
        final HtmlAnchor anchor = (HtmlAnchor)secondPage.getHtmlElementById("link");
        final HtmlPage thirdPage = (HtmlPage)anchor.click();
        assertEquals( "Third", thirdPage.getTitleText() );
        final WebWindow thirdWindow = thirdPage.getEnclosingWindow();
        assertNotSame( firstWindow, thirdWindow );

        assertEquals( "", thirdWindow.getName() );

        assertEquals( thirdWindow, thirdWindow.getTopWindow() );
        try {
            assertEquals(secondFrame.getEnclosedWindow(),
                webClient.getWebWindowByName( "secondFrame" ));
            // Expected path
        }
        catch (final WebWindowNotFoundException exception) {
            fail( "Expected secondFrame would be found after click." );
        }

        assertEquals(
            Collections.EMPTY_LIST,
            collectedAlerts);
    }


    /**
     * _self is a magic name.  If we call open(url, '_self') then the current window must be
     * reloaded.
     * @throws Exception If the test fails.
     */
    public void testOpenWindow_self() throws Exception {
        final WebClient webClient = new WebClient();
        final MockWebConnection webConnection = new MockWebConnection( webClient );

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

        webConnection.setResponse(URL_FIRST, firstContent);
        webConnection.setResponse(URL_SECOND, secondContent);
        webClient.setWebConnection( webConnection );

        final HtmlPage firstPage = ( HtmlPage )webClient.getPage(URL_FIRST);
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
     * _top is a magic name.  If we call open(url, '_top') then the top level
     * window must be reloaded.
     * @throws Exception If the test fails.
     */
    public void testOpenWindow_top() throws Exception {
        final WebClient webClient = new WebClient();
        final MockWebConnection webConnection = new MockWebConnection( webClient );

        final String firstContent
            = "<html><head><title>First</title></head><body>"
            + "  <iframe name='secondFrame' id='secondFrame' src='http://second' />"
            + "</body></html>";
        final String secondContent
            = "<html><head><title>Second</title></head><body>"
            + "  <iframe name='thirdFrame' id='thirdFrame' src='http://third' />"
            + "</body></html>";
        final String thirdContent
            = "<html><head><title>Third</title></head><body>"
            + "    <a id='link' onClick='open(\"http://fourth\", \"_top\"); "
            + "return false;'>Click me</a>"
            + "</body></html>";
        final String fourthContent
            = "<html><head><title>Fourth</title></head><body></body></html>";

        webConnection.setResponse(URL_FIRST, firstContent);
        webConnection.setResponse(URL_SECOND, secondContent);
        webConnection.setResponse(URL_THIRD, thirdContent);
        webConnection.setResponse(new URL("http://fourth"), fourthContent);
        webClient.setWebConnection( webConnection );

        final HtmlPage firstPage = ( HtmlPage )webClient.getPage(URL_FIRST);
        assertEquals( "First", firstPage.getTitleText() );

        final WebWindow firstWebWindow = firstPage.getEnclosingWindow();
        assertEquals( "First", firstPage.getTitleText() );
        final HtmlInlineFrame secondFrame =
            (HtmlInlineFrame)firstPage.getHtmlElementById("secondFrame");
        final HtmlPage secondPage = (HtmlPage)secondFrame.getEnclosedPage();
        assertEquals( "Second", secondPage.getTitleText() );
        final HtmlInlineFrame thirdFrame =
            (HtmlInlineFrame)secondPage.getHtmlElementById("thirdFrame");
        final HtmlPage thirdPage = (HtmlPage)thirdFrame.getEnclosedPage();
        assertEquals( "Third", thirdPage.getTitleText() );

        assertSame( webClient.getCurrentWindow(), firstWebWindow);
        assertNotSame( firstWebWindow, secondPage );

        final HtmlAnchor anchor =
            (HtmlAnchor)thirdPage.getHtmlElementById("link");
        final HtmlPage fourthPage = (HtmlPage)anchor.click();
        final WebWindow fourthWebWindow = fourthPage.getEnclosingWindow();
        assertSame( firstWebWindow, fourthWebWindow );
        assertSame( fourthWebWindow, fourthWebWindow.getTopWindow() );
        try {
            webClient.getWebWindowByName( "secondFrame" );
            fail( "Did not expect secondFrame to still exist after click." );
        }
        catch (final WebWindowNotFoundException exception) {
            // Expected path
        }
        try {
            webClient.getWebWindowByName( "thirdFrame" );
            fail( "Did not expect thirdFrame to still exist after click." );
        }
        catch (final WebWindowNotFoundException exception) {
            // Expected path
        }
    }


    /**
     * _parent is a magic name.  If we call open(url, '_parent') then the
     * parent window must be reloaded.
     * @throws Exception If the test fails.
     */
    public void testOpenWindow_parent() throws Exception {
        final WebClient webClient = new WebClient();
        final MockWebConnection webConnection = new MockWebConnection( webClient );

        final String firstContent
            = "<html><head><title>First</title></head><body>"
            + "  <iframe name='secondFrame' id='secondFrame' src='http://second' />"
            + "</body></html>";
        final String secondContent
            = "<html><head><title>Second</title></head><body>"
            + "  <iframe name='thirdFrame' id='thirdFrame' src='http://third' />"
            + "</body></html>";
        final String thirdContent
            = "<html><head><title>Third</title></head><body>"
            + "    <a id='link' onClick='open(\"http://fourth\", \"_parent\"); "
            + "return false;'>Click me</a>"
            + "</body></html>";
        final String fourthContent
            = "<html><head><title>Fourth</title></head><body></body></html>";

        webConnection.setResponse(URL_FIRST, firstContent);
        webConnection.setResponse(URL_SECOND, secondContent);
        webConnection.setResponse(URL_THIRD, thirdContent);
        webConnection.setResponse(new URL("http://fourth"), fourthContent);
        webClient.setWebConnection( webConnection );

        final HtmlPage firstPage = ( HtmlPage )webClient.getPage(URL_FIRST);
        assertEquals( "First", firstPage.getTitleText() );

        final WebWindow firstWebWindow = firstPage.getEnclosingWindow();
        assertEquals( "First", firstPage.getTitleText() );
        final HtmlInlineFrame secondFrame =
            (HtmlInlineFrame)firstPage.getHtmlElementById("secondFrame");
        final HtmlPage secondPage = (HtmlPage)secondFrame.getEnclosedPage();
        assertEquals( "Second", secondPage.getTitleText() );
        final HtmlInlineFrame thirdFrame =
            (HtmlInlineFrame)secondPage.getHtmlElementById("thirdFrame");
        final HtmlPage thirdPage = (HtmlPage)thirdFrame.getEnclosedPage();
        assertEquals( "Third", thirdPage.getTitleText() );


        assertSame( webClient.getCurrentWindow(), firstWebWindow);
        assertNotSame( firstWebWindow, secondFrame );

        final HtmlAnchor anchor =
            (HtmlAnchor)thirdPage.getHtmlElementById("link");
        final HtmlPage fourthPage = (HtmlPage)anchor.click();
        final WebWindow fourthWebWindow = fourthPage.getEnclosingWindow();
        assertSame( secondFrame.getEnclosedWindow(), fourthWebWindow );
        try {
            final WebWindow namedWindow = webClient.getWebWindowByName( "secondFrame" );
            assertSame( namedWindow.getEnclosedPage(), fourthPage);
            // Expected path
        }
        catch (final WebWindowNotFoundException exception) {
            fail( "Expected secondFrame would be found after click." );
        }
        try {
            webClient.getWebWindowByName( "thirdFrame" );
            fail( "Did not expect thirdFrame to still exist after click." );
        }
        catch (final WebWindowNotFoundException exception) {
            // Expected path
        }
    }

    /**
     * Regression test for 1592723: window.open('', 'someName') should
     * retrieve existing window named 'someName' rather than opening a new window
     * if such a window exists.
     * @throws Exception if the test fails
     */
    public void testOpenWindow_existingWindow() throws Exception {
        final String content
            = "<html><head><script>"
            + "function test()"
            + "{"
            + "  var w1 = window.open('about:blank', 'foo');"
            + "  alert(w1 != null);"
            + "  var w2 = window.open('', 'foo');"
            + "  alert(w1 == w2);"
            + "  var w3 = window.open('', 'myFrame');"
            + "  alert(w3 == window.frames.myFrame);"
            + "}"
            + "</script></head><body onload='test()'>"
            + "<iframe name='myFrame' id='myFrame'></iframe>"
            + "</body></html>";

        final List collectedAlerts = new ArrayList();
        final String[] expectedAlerts = {"true", "true", "true"};
        loadPage(content, collectedAlerts);
        createTestPageForRealBrowserIfNeeded(content, expectedAlerts);
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * Regression test to reproduce a known bug
     * @throws Exception if the test fails
     */
    public void testOpenWindow_emptyUrl() throws Exception {
        final String content
            = "<html><head><script>"
            + "var w = window.open('');"
            + "alert(w ? w.document.location : w);"
            + "</script></head>"
            + "<body></body></html>";

        final List collectedAlerts = new ArrayList();
        final String[] expectedAlertsMoz = {"null"};
        loadPage(BrowserVersion.MOZILLA_1_0, content, collectedAlerts);
        createTestPageForRealBrowserIfNeeded(content, expectedAlertsMoz);
        assertEquals(expectedAlertsMoz, collectedAlerts);

        collectedAlerts.clear();
        final String[] expectedAlertsIE = {"about:blank"};
        loadPage(BrowserVersion.INTERNET_EXPLORER_6_0, content, collectedAlerts);
        createTestPageForRealBrowserIfNeeded(content, expectedAlertsIE);
        assertEquals(expectedAlertsIE, collectedAlerts);
    }

    /**
     * Regression test to reproduce a known bug
     * @throws Exception if the test fails
     */
    public void testAlert_NoAlertHandler() throws Exception {
        final String firstContent
            = "<html><head><title>First</title><script>function doTest(){alert('foo')}</script></head>"
            + "<body onload='doTest()'></body></html>";

        getLog().warn("Warning for no alert handler expected next");
        final HtmlPage firstPage = loadPage(firstContent);
        assertEquals( "First", firstPage.getTitleText() );
    }


    /**
     * @throws Exception If the test fails
     */
    public void testParentAndTop() throws Exception {

        final String firstContent
            = "<html><head><title>First</title></head><body>"
            + "  <iframe name='left' src='http://second' />"
            + "</body></html>";
        final String secondContent
            = "<html><head><title>Second</title></head><body>"
            + "  <iframe name='innermost' src='http://third' />"
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

        final MockWebConnection webConnection = new MockWebConnection( webClient );
        webConnection.setResponse(URL_FIRST, firstContent);
        webConnection.setResponse(URL_SECOND, secondContent);
        webConnection.setResponse(URL_THIRD, thirdContent);

        webClient.setWebConnection( webConnection );

        final HtmlPage firstPage = (HtmlPage) webClient.getPage(URL_FIRST);
        assertEquals( "First", firstPage.getTitleText() );

        final WebWindow innermostWebWindow = webClient.getWebWindowByName("innermost");
        final HtmlPage innermostPage = (HtmlPage)innermostWebWindow.getEnclosedPage();
        ((HtmlAnchor)innermostPage.getHtmlElementById("clickme")).click();

        assertNotSame(innermostWebWindow.getParentWindow(), innermostWebWindow);
        assertNotSame(innermostWebWindow.getTopWindow(), innermostWebWindow);
        assertNotSame(innermostWebWindow.getParentWindow(),
            innermostWebWindow.getTopWindow());
        assertSame(innermostWebWindow.getParentWindow().getParentWindow(),
            innermostWebWindow.getTopWindow());

        assertEquals(
            new String[] {"true", "true", "true", "true", "true", "true"},
            collectedAlerts);
    }


    /**
     * @throws Exception If the test fails
     */
    public void testConfirm() throws Exception {
        final WebClient webClient = new WebClient();
        final MockWebConnection webConnection = new MockWebConnection( webClient );
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

        webConnection.setResponse(URL_FIRST, firstContent);
        webClient.setWebConnection( webConnection );

        final HtmlPage firstPage = ( HtmlPage )webClient.getPage(URL_FIRST);
        assertEquals( "First", firstPage.getTitleText() );

        assertEquals( Collections.singletonList("foo"), collectedConfirms );
        assertEquals( Collections.singletonList("true"), collectedAlerts );
    }


    /**
     * @throws Exception If the test fails
     */
    public void testConfirm_noConfirmHandler() throws Exception {
        final String html
            = "<html><head><title>First</title><script>function doTest(){alert(confirm('foo'))}</script>"
            + "</head><body onload='doTest()'></body></html>";

        getLog().warn("Warning for no confirm handler expected next");
        final List collectedAlerts = new ArrayList();
        loadPage(html, collectedAlerts);

        assertEquals( Collections.singletonList("true"), collectedAlerts );
    }


    /**
     * @throws Exception If the test fails
     */
    public void testPrompt() throws Exception {
        final WebClient webClient = new WebClient();
        final MockWebConnection webConnection = new MockWebConnection( webClient );
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

        webConnection.setResponse(URL_FIRST, firstContent);
        webClient.setWebConnection( webConnection );

        final HtmlPage firstPage = ( HtmlPage )webClient.getPage(URL_FIRST);
        assertEquals( "First", firstPage.getTitleText() );

        assertEquals( Collections.singletonList("foo"), collectedPrompts );
        assertEquals( Collections.singletonList("Flintstone"), collectedAlerts );
    }


    /**
     * @throws Exception If the test fails
     */
    public void testPrompt_noPromptHandler() throws Exception {
        final WebClient webClient = new WebClient();
        final MockWebConnection webConnection = new MockWebConnection( webClient );
        final List collectedAlerts = new ArrayList();
        final List collectedPrompts = new ArrayList();

        webClient.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        final String firstContent
            = "<html><head><title>First</title><script>function doTest(){alert(prompt('foo'))}</script>"
            + "</head><body onload='doTest()'></body></html>";

        webConnection.setResponse(URL_FIRST, firstContent);
        webClient.setWebConnection( webConnection );
        getLog().warn("Warning for no prompt handler expected next");

        final HtmlPage firstPage = ( HtmlPage )webClient.getPage(URL_FIRST);
        assertEquals( "First", firstPage.getTitleText() );

        assertEquals( Collections.EMPTY_LIST, collectedPrompts );
        assertEquals( Collections.singletonList("null"), collectedAlerts );
    }


    /**
     * @throws Exception If the test fails
     */
    public void testOpener() throws Exception {
        final WebClient webClient = new WebClient();
        final MockWebConnection webConnection = new MockWebConnection( webClient );
        final List collectedAlerts = new ArrayList();

        webClient.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        final String firstContent
            = "<html><head><title>First</title><script>"
            + "function runtest() {\n"
            + "    alert(window.opener)\n"
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
            + "<form name='form1' action='http://third' method='post'><input type='submit'></form>"
            + "</body></html>";
        final String thirdContent
            = "<html><head><title>Third</title><script>"
            + "function runtest() {\n"
            + "    opener.callAlert('three')"
            + "}\n"
            + "</script></head><body onload='runtest()'>"
            + "</body></html>";

        webConnection.setResponse(URL_FIRST, firstContent);
        webConnection.setResponse(URL_SECOND, secondContent);
        webConnection.setResponse(URL_THIRD, thirdContent);
        webClient.setWebConnection( webConnection );

        final HtmlPage firstPage = ( HtmlPage )webClient.getPage(URL_FIRST);
        assertEquals( "First", firstPage.getTitleText() );

        final String[] expectedAlerts = {"null", "one", "two", "three"};
        assertEquals( expectedAlerts, collectedAlerts );
    }


    /**
     * @throws Exception If the test fails
     */
    public void testSetTimeout() throws Exception {
        final String content
            = "<html><body><script language='JavaScript'>window.setTimeout('alert(\"Yo!\")',1);"
            + "</script></body></html>";

        final List collectedAlerts = Collections.synchronizedList(new ArrayList());
        final HtmlPage page = loadPage(content, collectedAlerts);
        assertTrue("thread failed to stop in 1 second", page.getEnclosingWindow().getThreadManager().joinAll(1000));
        assertEquals(Collections.singletonList("Yo!"), collectedAlerts);
    }

    /**
     * @throws Exception If the test fails
     */
    public void testSetTimeoutByReference() throws Exception {
        final String content = "<html><body><script language='JavaScript'>"
            + "function doTimeout() { alert('Yo!'); }"
            + "window.setTimeout(doTimeout,1);"
            + "</script></body></html>";

        final List collectedAlerts = Collections.synchronizedList(new ArrayList());
        final HtmlPage page = loadPage(content, collectedAlerts);
        assertTrue("thread failed to stop in 1 second", page.getEnclosingWindow().getThreadManager().joinAll(1000));
        assertEquals(Collections.singletonList("Yo!"), collectedAlerts);
    }

    /**
     * Just tests that setting and clearing an interval doesn't throw
     * @throws Exception If the test fails
     */
    public void testSetAndClearInterval() throws Exception {
        final String content
            = "<html><body>\n"
            + "<script>\n"
            + "window.setInterval('alert(\"Yo!\")', 500);"
            + "function foo() { alert('Yo2'); }\n"
            + "var i = window.setInterval(foo, 500);"
            + "window.clearInterval(i);"
            + "</script></body></html>";

        final List collectedAlerts = Collections.synchronizedList(new ArrayList());
        loadPage(content, collectedAlerts);
    }

    /**
     * @throws Exception If the test fails
     */
    public void testSetIntervalFunctionReference() throws Exception {
        final String content = "<html>\n"
            + "<head>\n"
            + "  <title>test</title>\n"
            + "  <script>\n"
            + "    var threadID;\n"
            + "    function test() {\n"
            + "      threadID = setInterval(doAlert, 100);\n"
            + "    }\n"
            + "    var iterationNumber=0;"
            + "    function doAlert() {\n"
            + "      alert('blah');\n"
            + "      if (++iterationNumber >= 3) {"
            + "        clearInterval(threadID);"
            + "      }"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "</body>\n"
            + "</html>";

        final List collectedAlerts = Collections.synchronizedList(new ArrayList());
        final HtmlPage page = loadPage(content, collectedAlerts);
        final ThreadManager threadManager = page.getEnclosingWindow().getThreadManager();
        threadManager.joinAll(1000);
        assertEquals(0, threadManager.activeCount());
        assertEquals(Collections.nCopies(3, "blah"), collectedAlerts);
    }

    /**
     * Test that a script started by a timer is stopped if the page that started it
     * is not loaded anymore.
     * @throws Exception If the test fails
     */
    public void testSetTimeoutStopped() throws Exception {
        final String firstContent
            = "<html><head>"
            + "<script language='JavaScript'>window.setTimeout('alert(\"Yo!\")', 1000);</script>"
            + "</head><body onload='document.location.replace(\"http://second\")'></body></html>";
        final String secondContent = "<html><head><title>Second</title></head><body></body></html>";

        final WebClient webClient = new WebClient();
        final MockWebConnection webConnection = new MockWebConnection( webClient );
        final List collectedAlerts = Collections.synchronizedList(new ArrayList());

        webClient.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        webConnection.setResponse(URL_FIRST, firstContent);
        webConnection.setResponse(URL_SECOND, secondContent);
        webClient.setWebConnection( webConnection );

        final HtmlPage page = (HtmlPage) webClient.getPage(URL_FIRST);
        assertEquals("Second", page.getTitleText());
        assertEquals("no thread should be running", 0, page.getEnclosingWindow().getThreadManager().activeCount());
        assertEquals(Collections.EMPTY_LIST, collectedAlerts);
    }

    /**
     * @throws Exception If the test fails
     */
    public void testClearTimeout() throws Exception {
        final String content =
              "<html>\n"
            + "<head>\n"
            + "  <title>test</title>\n"
            + "  <script>\n"
            + "    function test() {\n"
            + "      var id = setTimeout('doAlert()', 2000);\n"
            + "      clearTimeout(id);\n"
            + "    }\n"
            + "    function doAlert() {\n"
            + "      alert('blah');\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "</body>\n"
            + "</html>";

        final List collectedAlerts = Collections.synchronizedList(new ArrayList());
        final HtmlPage page = loadPage(content, collectedAlerts);
        page.getEnclosingWindow().getThreadManager().joinAll(2000);
        assertEquals(Collections.EMPTY_LIST, collectedAlerts);
    }

    /**
     * @throws Exception If the test fails
     */
    public void testAboutURL() throws Exception {
        final WebClient webClient = new WebClient();
        final MockWebConnection webConnection =
            new MockWebConnection(webClient);
        final String firstContent =
            "<html><body><script language='JavaScript'>"
            + "w2 = window.open('about:blank', 'AboutBlank');"
            + "w2.document.open();"
            + "w2.document.write('<html><head><title>hello</title></head><body></body></html>');"
            + "w2.document.close();"
            + "</script></body></html>";
        webConnection.setResponse(URL_FIRST, firstContent);
        webClient.setWebConnection(webConnection);

        webClient.getPage(URL_FIRST);
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
        final String firstContent =
            "<html><body><script language='JavaScript'>"
            + "if (typeof top.frames['anyXXXname'] == 'undefined') {"
            + "alert('one')};"
            + "</script></body></html>";

        final List collectedAlerts = new ArrayList();
        loadPage(firstContent, collectedAlerts);
        assertEquals(1, collectedAlerts.size());
    }

    /**
     *
     * @throws Exception If the test fails
     */
    public void testWindowFramesLive() throws Exception {

        final String content =
            "<html>"
            + "<script>"
            + "var oFrames = window.frames;"
            + "alert(oFrames.length);"
            + "function test()"
            + "{"
            + "    alert(oFrames.length);"
            + "    alert(window.frames.length);"
            + "    alert(oFrames == window.frames);"
            + "}"
            + "</script>"
            + "<frameset rows='50,*' onload='test()'>"
            + "<frame src='about:blank'/>"
            + "<frame src='about:blank'/>"
            + "</frameset>"
            + "</html>";

        final String[] expectedAlerts = {"0", "2", "2", "true"};

        final List collectedAlerts = new ArrayList();
        loadPage(content, collectedAlerts);

        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * Variables that are defined inside javascript should be accessible through the
     * window object (ie window.myVariable).  Test that this works.
     * @throws Exception If the test fails.
     */
    public void testJavascriptVariableFromWindow() throws Exception {
        final String firstContent =
            "<html><head><title>first</title></head><body><script>\n"
            + "myVariable = 'foo';\n"
            + "alert(window.myVariable);\n"
            + "</script></body></head>";

        final List collectedAlerts = new ArrayList();
        final HtmlPage page = loadPage(firstContent, collectedAlerts);
        assertEquals( Collections.singletonList("foo"), collectedAlerts );
        assertEquals( "first", page.getTitleText() );
    }

    /**
     * Variables that are defined inside javascript should be accessible through the
     * window object (ie window.myVariable).  Test that this works.
     * @throws Exception If the test fails.
     */
    public void testJavascriptVariableFromTopAndParentFrame() throws Exception {
        final WebClient webClient = new WebClient();
        final MockWebConnection webConnection = new MockWebConnection( webClient );
        final List collectedAlerts = new ArrayList();

        webClient.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        final String firstContent
            = "<html><head><title>First</title></head><body><script>"
            + "myVariable = 'first'"
            + "  </script><iframe name='left' src='http://second' />"
            + "</body></html>";
        webConnection.setResponse(URL_FIRST, firstContent);

        final String secondContent
            = "<html><head><title>Second</title></head><body><script>"
            + "myVariable = 'second'"
            + "  </script><iframe name='innermost' src='http://third' />"
            + "</body></html>";
        webConnection.setResponse(URL_SECOND, secondContent);

        final String thirdContent
            = "<html><head><title>Third</title><script>"
            + "myVariable = 'third';\n"
            + "function doTest() {\n"
            + "alert('parent.myVariable = ' + parent.myVariable);\n"
            + "alert('top.myVariable = ' + top.myVariable);\n"
            + "}\n"
            + "</script></head>"
            + "<body onload='doTest()'></body></html>";

        webConnection.setResponse(URL_THIRD, thirdContent);

        webClient.setWebConnection( webConnection );

        final HtmlPage page = (HtmlPage)webClient.getPage(URL_FIRST);
        assertEquals( "First", page.getTitleText() );

        final String[] expectedAlerts = {
            "parent.myVariable = second",
            "top.myVariable = first",
        };
        assertEquals( expectedAlerts, collectedAlerts );
    }
    /**
     * Variables that are defined inside javascript should be accessible through the
     * window object (ie window.myVariable).  Test that this works.
     * @throws Exception If the test fails.
     */
    public void testJavascriptVariableFromNamedFrame() throws Exception {
        final WebClient webClient = new WebClient();
        final MockWebConnection webConnection = new MockWebConnection(webClient);
        final List collectedAlerts = new ArrayList();

        webClient.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        final String firstContent
            = "<html><head><title>first</title></head>"
            + "<frameset cols='20%,80%'>"
            + "    <frameset rows='30%,70%'>"
            + "        <frame src='http://second' name='second'>"
            + "        <frame src='http://third' name='third'>"
            + "    </frameset>"
            + "    <frame src='http://fourth' name='fourth'>"
            + "</frameset></html>";
        webConnection.setResponse( URL_FIRST, firstContent);

        final String secondContent
            = "<html><head><title>second</title></head><body><script>"
            + "myVariable = 'second';\n"
            + "</script><p>second</p></body></html>";
        webConnection.setResponse( URL_SECOND, secondContent);

        final String thirdContent
            = "<html><head><title>third</title></head><body><script>"
            + "myVariable = 'third';\n"
            + "</script><p>third</p></body></html>";
        webConnection.setResponse( URL_THIRD, thirdContent);

        final String fourthContent
            = "<html><head><title>fourth</title></head><body onload='doTest()'><script>\n"
            + "myVariable = 'fourth';\n"
            + "function doTest() {\n"
            + "alert('parent.second.myVariable = ' + parent.second.myVariable);\n"
            + "alert('parent.third.myVariable = ' + parent.third.myVariable);\n"
            + "}\n"
            + "</script></body></html>";
        webConnection.setResponse( new URL("http://fourth"), fourthContent);

        webClient.setWebConnection(webConnection);

        final HtmlPage page = (HtmlPage)webClient.getPage(URL_FIRST);
        assertEquals( "first", page.getTitleText() );

        final String[] expectedAlerts = {
            "parent.second.myVariable = second",
            "parent.third.myVariable = third",
        };
        assertEquals( expectedAlerts, collectedAlerts );
    }

    /**
     * Variables that have not been defined should return null when accessed.
     * @throws Exception If the test fails.
     */
    public void testJavascriptVariableFromWindow_NotFound() throws Exception {
        final String firstContent =
            "<html><head><title>first</title></head><body><script>\n"
            + "myVariable = 'foo';\n"
            + "alert(window.myOtherVariable == null);\n"
            + "</script></body></head>";

        final List collectedAlerts = new ArrayList();
        final HtmlPage page = loadPage(firstContent, collectedAlerts);
        assertEquals( Collections.singletonList("true"), collectedAlerts );
        assertEquals( "first", page.getTitleText() );
    }

    /**
     *
     * @throws Exception If the test fails.
     */
    public void testGetFrameByName() throws Exception {
        final WebClient webClient = new WebClient();
        final MockWebConnection webConnection =
            new MockWebConnection(webClient);
        final List collectedAlerts = new ArrayList();

        webClient.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        final String firstContent
            = "<html><head><title>first</title></head>"
            + "<frameset cols='20%,80%'>"
            + "    <frameset rows='30%,70%'>"
            + "        <frame src='http://second' name='second'>"
            + "        <frame src='http://third' name='third'>"
            + "    </frameset>"
            + "    <frame src='http://fourth' name='fourth'>"
            + "</frameset></html>";
        webConnection.setResponse( URL_FIRST, firstContent);

        final String secondContent
            = "<html><head><title>second</title></head><body><p>second</p></body></html>";
        webConnection.setResponse( URL_SECOND, secondContent);

        final String thirdContent
            = "<html><head><title>third</title></head><body><p>third</p></body></html>";
        webConnection.setResponse( URL_THIRD, thirdContent);

        final String fourthContent
            = "<html><head><title>fourth</title></head><body onload='doTest()'><script>\n"
            + "function doTest() {\n"
            + "alert('fourth-second='+parent.second.document.location);\n"
            + "alert('fourth-third='+parent.third.document.location);\n"
            + "}\n"
            + "</script></body></html>";
        webConnection.setResponse( new URL("http://fourth"), fourthContent);

        webClient.setWebConnection(webConnection);

        final HtmlPage page = (HtmlPage)webClient.getPage(URL_FIRST);
        assertEquals( "first", page.getTitleText() );

        final String[] expectedAlerts = {
            "fourth-second=http://second",
            "fourth-third=http://third",
        };
        assertEquals( expectedAlerts, collectedAlerts );
    }

    /**
     * @throws Exception If the test fails
     */
    public void testSetOpenerLocationHrefRelative() throws Exception {
        final WebClient webClient = new WebClient();
        final MockWebConnection webConnection = new MockWebConnection( webClient );

        final String aContent
            = "<html><head><title>A</title></head><body>"
            + "<button id='clickme' onClick='window.open(\"b/b.html\");'>Click me</a>"
            + "</body></html>";
        final String bContent
            = "<html><head><title>B</title></head><body>"
            + "<button id='clickme' onClick='opener.location.href=\"../c.html\";'>Click me</a>"
            + "</body></html>";
        final String cContent
            = "<html><head><title>C</title></head><body></body></html>";
        final String failContent
            = "<html><head><title>FAILURE!!!</title></head><body></body></html>";

        webConnection.setResponse(new URL("http://opener/test/a.html"), aContent);
        webConnection.setResponse(new URL("http://opener/test/b/b.html"), bContent);
        webConnection.setResponse(new URL("http://opener/test/c.html"), cContent);
        webConnection.setResponse(new URL("http://opener/c.html"), failContent);

        webClient.setWebConnection( webConnection );

        final HtmlPage firstPage = ( HtmlPage )webClient.getPage(
                new URL("http://opener/test/a.html"));
        assertEquals( "A", firstPage.getTitleText() );

        final HtmlButton buttonA = (HtmlButton)firstPage.getHtmlElementById("clickme");
        final HtmlPage secondPage = (HtmlPage)buttonA.click();
        assertNotNull("B", secondPage);
        assertEquals( "B", secondPage.getTitleText() );

        final HtmlButton buttonB = (HtmlButton)secondPage.getHtmlElementById("clickme");
        final HtmlPage thirdPage = (HtmlPage)buttonB.click();
        assertNotNull("C", thirdPage);
        assertEquals( "C", thirdPage.getTitleText() );
    }

    /**
     * Test the window.closed property
     * @throws Exception if the test fails.
     */
    public void testClosed() throws Exception {
        final String content = "<html><head>"
            + "<script>"
            + "function test()"
            + "{"
            + "  alert(window.closed);"
            + "  var newWindow = window.open('about:blank', 'foo');"
            + "  alert(newWindow.closed);"
            + "  newWindow.close();"
            + "  alert(newWindow.closed);"
            + "}"
            + "</script>"
            + "</head>"
            + "<body onload='test()'>"
            + "</body></html>";

        final String[] expectedAlerts = {"false", "false", "true"};
        createTestPageForRealBrowserIfNeeded(content, expectedAlerts);

        final List collectedAlerts = new ArrayList();
        loadPage(content, collectedAlerts);

        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * Test closing using javascript
     * @throws Exception if the test fails.
     */
    public void testClose() throws Exception {

        final WebClient webClient = new WebClient();
        final MockWebConnection webConnection = new MockWebConnection(webClient);

        final List collectedAlerts = new ArrayList();
        webClient.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        final String firstContent
            = "<html><head><title>First</title></head><body>"
             + "<a href='" + URL_SECOND + "' id='link' target='_blank'>Link</a>"
             + "</body></html>";
        final String secondContent
            = "<html><head><title>Second</title></head><body>"
             + "<h1>Second</h1><form>"
             + "<input type='submit' name='action' value='Close' id='button' "
             + "onclick='window.close(); return false;'>"
             + "</form></body></html>";

        webConnection.setResponse(URL_FIRST, firstContent);
        webConnection.setResponse(URL_SECOND, secondContent);
        webClient.setWebConnection(webConnection);

        final HtmlPage firstPage = (HtmlPage) webClient.getPage(URL_FIRST);
        assertEquals("First", firstPage.getTitleText());
        assertEquals(1, webClient.getWebWindows().size());
        final WebWindow firstWindow = firstPage.getEnclosingWindow();

        final HtmlPage secondPage = (HtmlPage) ((HtmlAnchor) firstPage.getHtmlElementById("link")).click();
        assertEquals("Second", secondPage.getTitleText());
        assertEquals(2, webClient.getWebWindows().size());
        final WebWindow secondWindow = secondPage.getEnclosingWindow();

        assertNotSame(firstWindow, secondWindow);

        final EventCatcher eventCatcher = new EventCatcher();
        eventCatcher.listenTo(webClient);
        ((HtmlSubmitInput) secondPage.getHtmlElementById("button")).click();

        final List expectedEvents = Arrays.asList(new Object[]{
            new WebWindowEvent(secondWindow, WebWindowEvent.CLOSE, secondPage, null)
        });
        assertEquals(expectedEvents, eventCatcher.getEvents());

        assertEquals(1, webClient.getWebWindows().size());
        assertEquals(firstWindow, webClient.getCurrentWindow());

        assertEquals(Collections.EMPTY_LIST, collectedAlerts);
    }

    /**
     * Test that length of frames collection is retrieved
     * @throws Exception if the test fails
     */
    public void testFramesLengthZero() throws Exception {
        final String content
            = "<html><head><title>foo</title><script>"
            + "alert(window.frames.length)"
            + "</script></head><body>"
            + "</body></html>";
        final List collectedAlerts = new ArrayList();
        loadPage(content, collectedAlerts);

        final String[] expectedAlerts = {"0"};
        assertEquals( expectedAlerts, collectedAlerts );
    }

    /**
     * Test that length of frames collection is retrieved when there
     * are frames.
     * @throws Exception If the test fails
     */
    public void testFramesLengthAndFrameAccess() throws Exception {

        final String content =
            "<html>"
            + "<script>"
            + "function test()"
            + "{"
            + "    alert(window.frames.length);"
            + "    alert(window.frames[0].name);"
            + "    alert(window.frames.frame2.name);"
            + "}"
            + "</script>"
            + "<frameset rows='50,*' onload='test()'>"
            + "<frame name='frame1' src='about:blank'/>"
            + "<frame name='frame2' src='about:blank'/>"
            + "</frameset>"
            + "</html>";

        final List collectedAlerts = new ArrayList();
        loadPage(content, collectedAlerts);

        final String[] expectedAlerts = {"2", "frame1", "frame2"};
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * Test that Window.moveTo method gets correctly called and handled
     * by the scripting engine.
     * @throws Exception if the test fails
     */
    public void testMoveTo() throws Exception {
        final String content
            = "<html><head><title>foo</title><script>"
            + "window.moveTo(10, 20)"
            + "</script></head><body>"
            + "</body></html>";
        loadPage(content);
    }

    /**
     * Test that Window.moveBy method gets correctly called and handled
     * by the scripting engine.
     * @throws Exception if the test fails
     */
    public void testMoveBy() throws Exception {
        final String content
            = "<html><head><title>foo</title><script>"
            + "window.moveBy(10, 20)"
            + "</script></head><body>"
            + "</body></html>";
        loadPage(content);
    }

    /**
     * Tests that the Window.resizeTo method gets correctly called and
     * handled by the scripting engine.
     * @throws Exception if the test fails
     */
    public void testResizeTo() throws Exception {
        final String content = "<html><head><title>foo</title><script>\n"
            + "window.resizeTo(10, 20);\n"
            + "window.resizeTo(-10, 20);\n"
            + "</script></head><body></body></html>";
        loadPage(content);
    }

    /**
     * Test that Window.scroll method gets correctly called and handled
     * by the scripting engine.
     * @throws Exception if the test fails
     */
    public void testScroll() throws Exception {
        final String content
            = "<html><head><title>foo</title><script>"
            + "window.scroll(10, 20);"
            + "</script></head><body>"
            + "</body></html>";
        loadPage(content);
    }

    /**
     * Test that Window.scrollBy method gets correctly called and handled
     * by the scripting engine.
     * @throws Exception if the test fails
     */
    public void testScrollBy() throws Exception {
        final String content
            = "<html><head><title>foo</title><script>"
            + "window.scrollBy(10, 20);"
            + "</script></head><body>"
            + "</body></html>";
        loadPage(content);
    }

    /**
     * Test that Window.scrollByLines method gets correctly called and handled
     * by the scripting engine.
     * @throws Exception if the test fails
     */
    public void testScrollByLines() throws Exception {
        final String content
            = "<html><head><title>foo</title><script>"
            + "window.scrollByLines(2);"
            + "</script></head><body>"
            + "</body></html>";
        loadPage(content);
    }

    /**
     * Test that Window.scrollByPages method gets correctly called and handled
     * by the scripting engine.
     * @throws Exception if the test fails
     */
    public void testScrollByPages() throws Exception {
        final String content
            = "<html><head><title>foo</title><script>"
            + "window.scrollByPages(2);"
            + "</script></head><body>"
            + "</body></html>";
        loadPage(content);
    }

    /**
     * Test that Window.scrollTo method gets correctly called and handled
     * by the scripting engine.
     * @throws Exception if the test fails
     */
    public void testScrollTo() throws Exception {
        final String content
            = "<html><head><title>foo</title><script>"
            + "window.scrollTo(10, 20);"
            + "</script></head><body>"
            + "</body></html>";
        loadPage(content);
    }

     /**
     * All elements should be accessible via the window object by their name, both in qualified
     * format (<tt>window.elementName</tt>) and unqualified format (<tt>elementName</tt>), if we
     * are emulating Microsoft Internet Explorer. Both of these expressions are therefore equivalent
     * to <tt>document.getElementsByName(elementName)</tt> in IE, and should return a collection of
     * elements if there is more than one with the specified name.
     * @throws Exception If the test fails.
     */
    public void testElementByNameFromWindow() throws Exception {
        final String content = "<html>\n" +
            "<head><title>test</title>\n" +
            "<script>\n" +
            "  function test() {\n" +
            "    alert(window.form1.name);\n" +
            "    alert(form2.name);\n" +
            "    alert(window.input1.length);\n" +
            "    alert(input2[1].value);\n" +
            "  }\n" +
            "</script>\n" +
            "</head>\n" +
            "<body onload='test()'>\n" +
            "<form name='form1'></form>\n" +
            "<form name='form2'></form>\n" +
            "<input type='text' name='input1' value='1'/>\n" +
            "<input type='text' name='input1' value='2'/>\n" +
            "<input type='text' name='input2' value='3'/>\n" +
            "<input type='text' name='input2' value='4'/>\n" +
            "</body>\n" +
            "</html>\n";
        final List collectedAlerts = new ArrayList();
        final String[] expectedAlerts = {"form1", "form2", "2", "4"};
        createTestPageForRealBrowserIfNeeded(content, expectedAlerts);
        loadPage(content, collectedAlerts);
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * All elements should be accessible via the window object by their id, if we
     * are emulating Microsoft Internet Explorer. 
     * @throws Exception If the test fails.
     */
    public void testElementByIdFromWindow() throws Exception {
        final String content = "<html>\n" +
            "<head><title>test</title>\n" +
            "<script>\n" +
            "  function test() {\n" +
            "    alert(window.form1Id.name);\n" +
            "    alert(form2Id.name);\n" +
            "    alert(window.input1Id.value);\n" +
            "    alert(myDiv.tagName);\n" +
            "  }\n" +
            "</script>\n" +
            "</head>\n" +
            "<body onload='test()'>\n" +
            "<div id='myDiv'>\n" +
            "<form name='form1' id='form1Id'></form>\n" +
            "</div>\n" +
            "<form name='form2' id='form2Id'></form>\n" +
            "<input type='text' name='input1' id='input1Id' value='1'/>\n" +
            "</form>\n" +
            "</body>\n" +
            "</html>\n";
        final List collectedAlerts = new ArrayList();
        final String[] expectedAlerts = {"form1", "form2", "1", "DIV"};
        loadPage(content, collectedAlerts);
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * Test that Window.execScript method gets called correctly.
     * @throws Exception if the test fails
     */
    public void testExecScript() throws Exception {
        final String content = "<html>\n"
            + "<head><title>test</title>\n"
            + "<script>\n"
            + "  function test()\n"
            + "  {\n"
            + "    window.execScript('alert(\"JavaScript\")', 'JavaScript');\n"
            + "    window.execScript('alert(\"JScript\")',    'JScript');\n"
            + "    window.execScript('alert(\"VBScript\")',   'VBScript');\n"
            + "    try {\n"
            + "      window.execScript('alert(\"BadLanguage\")', 'BadLanguage');\n"
            + "    }\n"
            + "    catch(e) {\n"
            + "      alert(e.message.substr(0, 20)); // msg now contains info on error location\n"
            + "    }\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <div id='div1'>blah</div>\n"
            + "</body>\n"
            + "</html>\n";
        final String[] expectedAlerts = {"JavaScript", "JScript", "Invalid class string"};
        createTestPageForRealBrowserIfNeeded(content, expectedAlerts);
        final List collectedAlerts = new ArrayList();
        loadPage(content, collectedAlerts);
        assertEquals(expectedAlerts, collectedAlerts);
    }

   /**
     * @throws Exception If the test fails.
     */
    public void testOnLoadFunction() throws Exception {
        final String content = "<html>\n"
            + "<head><title>test</title>\n"
            + "<script>\n"
            + "  function test()\n"
            + "  {\n"
            + "    alert('test');\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "<script>\n"
            + "  var oldOnLoad = window.onload;\n"
            + "  window.onload = test2;\n"
            + "  function test2()\n"
            + "  {\n"
            + "    alert('test2');\n"
            + "    oldOnLoad();\n"
            + "  }\n"
            + "</script>\n"
            + "</body>\n"
            + "</html>\n";
        final String[] expectedAlerts = {"test2", "test"};
        createTestPageForRealBrowserIfNeeded(content, expectedAlerts);
        final List collectedAlerts = new ArrayList();
        loadPage(content, collectedAlerts);
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * Verifies that you can set window.onload to something else than a function. 
     * See bug 1708532 & 1201561.
     * @throws Exception If an error occurs.
     */
    public void testOnloadNotAFunction() throws Exception {

        final String html = "<html><body><script>"
            + "window.onload = new function() {alert('a')};"
            + "window.onload = undefined;"
            + "alert(window.onload);"
            + "</script></body></html>";

        final String[] expectedAlerts = {"a", "undefined"};
        createTestPageForRealBrowserIfNeeded(html, expectedAlerts);

        final List collectedAlerts = new ArrayList();
        loadPage(html, collectedAlerts);

        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception If the test fails.
     */
    public void testAddOnLoadEventListener() throws Exception {
        final String content = "<html>\n"
            + "<head><title>test</title>\n"
            + "<script>\n"
            + "  function test1() { alert('test1'); }\n"
            + "  function test2() { alert('test2'); }\n"
            + "  function test3() { alert('test3'); }\n"
            + "alert(window.addEventListener == null);"
            + "alert(window.attachEvent == null);"
            + "alert(window.removeEventListener == null);"
            + "alert(window.detachEvent == null);"
            + "window.addEventListener('load', test1, true);"
            + "window.addEventListener('load', test1, true);"
            + "window.addEventListener('load', test2, true);"
            + "window.addEventListener('load', test3, true);"
            + "window.removeEventListener('load', test3, true);"
            + "</script></head>\n"
            + "<body onload='alert(\"onload\")'></body></html>\n";
        final String[] expectedAlerts = {"false", "true", "false", "true", "test1", "test2", "onload"};
        createTestPageForRealBrowserIfNeeded(content, expectedAlerts);
        final List collectedAlerts = new ArrayList();
        loadPage(BrowserVersion.MOZILLA_1_0, content, collectedAlerts);
        assertEquals(expectedAlerts, collectedAlerts);
    }


    /**
     * @throws Exception If the test fails.
     */
    public void testAttachOnLoadEvent() throws Exception {
        final String content = "<html>\n"
            + "<head><title>test</title>\n"
            + "<script>\n"
            + "  function test1(_e) { alert('test1, param null: ' + (_e == null)); }\n"
            + "  function test2() { alert('test2'); }\n"
            + "  function test3() { alert('test3'); }\n"
            + "alert(window.addEventListener == null);"
            + "alert(window.attachEvent == null);"
            + "alert(window.removeEventListener == null);"
            + "alert(window.detachEvent == null);"
            + "window.attachEvent('onload', test1);"
            + "window.attachEvent('onload', test1);"
            + "window.attachEvent('onload', test2);"
            + "window.attachEvent('onload', test3);"
            + "window.detachEvent('onload', test3);"
            + "</script></head>\n"
            + "<body onload='alert(\"onload\")'></body></html>\n";
        final String[] expectedAlerts = {"true", "false", "true", "false", 
            "onload", "test1, param null: false", "test2"};
        createTestPageForRealBrowserIfNeeded(content, expectedAlerts);
        final List collectedAlerts = new ArrayList();
        loadPage(BrowserVersion.INTERNET_EXPLORER_6_0, content, collectedAlerts);
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * Regression test for 1596926
     * @throws Exception If the test fails.
     */
    public void testDetachEventInAttachEvent() throws Exception {
        final String content = "<html>\n"
            + "<head><title>test</title>\n"
            + "<script>\n"
            + "function test()" 
            + "{"
            + "  window.detachEvent('onload', test);"
            + "  alert('detached');"
            + "}"
            + "window.attachEvent('onload', test);"
            + "</script></head>\n"
            + "<body></body></html>\n";
        final String[] expectedAlerts = {"detached"};
        createTestPageForRealBrowserIfNeeded(content, expectedAlerts);
        final List collectedAlerts = new ArrayList();
        loadPage(BrowserVersion.INTERNET_EXPLORER_6_0, content, collectedAlerts);
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    public void testStatus() throws Exception {
        final WebClient webClient = new WebClient();
        final MockWebConnection webConnection = new MockWebConnection( webClient );

        final String firstContent
            = "<html><head><title>First</title><script>"
            + "function doTest() {\n"
            + "    alert(window.status);\n"
            + "    window.status = 'newStatus';\n"
            + "    alert(window.status);\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>"
            + "</body></html>";

        final URL url = URL_FIRST;
        webConnection.setResponse(url, firstContent);
        webClient.setWebConnection( webConnection );

        final List collectedAlerts = new ArrayList();
        webClient.setAlertHandler( new CollectingAlertHandler(collectedAlerts) );

        final List collectedStatus = new ArrayList();
        webClient.setStatusHandler( new StatusHandler() {
            public void statusMessageChanged( final Page page, final String message ) {
                collectedStatus.add(message);
            }
        });
        final HtmlPage firstPage = ( HtmlPage )webClient.getPage( URL_FIRST );
        assertEquals( "First", firstPage.getTitleText() );

        final String[] expectedAlerts = {"", "newStatus"};
        assertEquals( "alerts", expectedAlerts, collectedAlerts );

        final String[] expectedStatus = {"newStatus"};
        assertEquals( "status", expectedStatus, collectedStatus );
    }


    /**
     * Test <code>window.name</code>.
     *
     * @throws Exception If the test fails.
     */
    public void testWindowName() throws Exception {
        final String windowName = "main";
        final String content = "<html>\n"
            + "<head><title>window.name test</title></head>\n"
            + "<body>\n"
            + "<script>\n"
            + "alert('window.name before: ' + window.name);\n"
            + "window.name = '" + windowName + "';\n"
            + "alert('window.name after: ' + window.name);\n"
            + "</script>\n"
            + "</body>\n"
            + "</html>\n";
        final String[] expectedAlerts = {"window.name before: ", "window.name after: " + windowName};
        createTestPageForRealBrowserIfNeeded(content, expectedAlerts);
        final List collectedAlerts = new ArrayList();
        loadPage(content, collectedAlerts);
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * Test Mozilla viewport properties
     *
     * @throws Exception If the test fails.
     */
    public void testMozillaViewport() throws Exception {
        final String content = "<html>\n"
            + "<head></head>\n"
            + "<body>\n"
            + "<script>\n"
            + "alert(typeof window.innerWidth);\n"
            + "alert(typeof window.innerHeight);\n"
            + "alert(typeof window.outerWidth);\n"
            + "alert(typeof window.outerHeight);\n"
            + "</script>\n"
            + "</body>\n"
            + "</html>\n";
        final List expectedAlerts = Collections.nCopies(4, "number");
        createTestPageForRealBrowserIfNeeded(content, expectedAlerts);
        final List collectedAlerts = new ArrayList();
        loadPage(BrowserVersion.MOZILLA_1_0, content, collectedAlerts);
        assertEquals(expectedAlerts, collectedAlerts);
    }
    /**
     * @throws Exception If the test fails.
     */
    public void testPrint() throws Exception {
        final String content = "<html>\n"
            + "<head></head>\n"
            + "<body>\n"
            + "<script>\n"
            + "window.print();\n"
            + "</script>\n"
            + "</body>\n"
            + "</html>\n";

        loadPage(content);
    }
    /**
     * Open a window with only an image for content, then try to set focus to it.
     *
     * @throws Exception If the test fails.
     */
    public void testOpenWindow_image() throws Exception {
        final WebClient webClient = new WebClient();
        final MockWebConnection webConnection = new MockWebConnection(webClient);

        final String firstContent = "<html><head><title>First</title></head><body>"
            + "<form name='form1'>"
            + "    <a id='link' onClick='open(\"http://second\", \"_blank\").focus(); return false;'"
            + "return false;'>Click me</a>"
            + "</form>"
            + "</body></html>";
        final String secondContent = new String(new char[]{
            'G', 'I', 'F', '8', '9', 'a', 0x01, 0x00,
            0x01, 0x00, 0x80, 0x00, 0x00, 0xfe, 0xd4, 0xaf,
            0x00, 0x00, 0x00, 0x21, 0xf9, 0x04, 0x00, 0x00,
            0x00, 0x00, 0x00, 0x2c, 0x00, 0x00, 0x00, 0x00,
            0x01, 0x00, 0x01, 0x00, 0x00, 0x02, 0x02, 0x44,
            0x01, 0x00, 0x3b});

        final EventCatcher eventCatcher = new EventCatcher();

        webConnection.setResponse(URL_FIRST, firstContent, 200, "OK", "text/html", Collections.EMPTY_LIST);
        webConnection.setResponse(URL_SECOND, secondContent, 200, "OK", "image/gif", Collections.EMPTY_LIST);
        webClient.setWebConnection(webConnection);

        final HtmlPage firstPage = (HtmlPage) webClient.getPage(URL_FIRST);
        assertEquals("First", firstPage.getTitleText());

        eventCatcher.listenTo(webClient);

        final WebWindow firstWebWindow = firstPage.getEnclosingWindow();

        final HtmlAnchor anchor = (HtmlAnchor) firstPage.getHtmlElementById("link");
        final Page secondPage = anchor.click();
        assertEquals("First", firstPage.getTitleText());
        assertEquals("image/gif", secondPage.getWebResponse().getContentType());

        assertEquals(2, eventCatcher.getEventCount());

        final WebWindow secondWebWindow = (WebWindow) eventCatcher.getEventAt(0).getSource();

        assertSame(webClient.getCurrentWindow(), secondWebWindow);
        assertNotSame(firstWebWindow, secondWebWindow);
    }

    /**
     * Open a window with only text for content, then try to set focus to it.
     *
     * @throws Exception If the test fails.
     */
    public void testOpenWindow_text() throws Exception {
        final WebClient webClient = new WebClient();
        final MockWebConnection webConnection = new MockWebConnection(webClient);

        final String firstContent = "<html><head><title>First</title></head><body>"
            + "<form name='form1'>"
            + "    <a id='link' onClick='open(\"http://second\", \"_blank\").focus(); return false;'"
            + "return false;'>Click me</a>"
            + "</form>"
            + "</body></html>";
        final String secondContent = "Hello World";

        final EventCatcher eventCatcher = new EventCatcher();

        webConnection.setResponse(URL_FIRST, firstContent, 200, "OK", "text/html", Collections.EMPTY_LIST);
        webConnection.setResponse(URL_SECOND, secondContent, 200, "OK", "text/plain", Collections.EMPTY_LIST);
        webClient.setWebConnection(webConnection);

        final HtmlPage firstPage = (HtmlPage) webClient.getPage(URL_FIRST);
        assertEquals("First", firstPage.getTitleText());

        eventCatcher.listenTo(webClient);

        final WebWindow firstWebWindow = firstPage.getEnclosingWindow();

        final HtmlAnchor anchor = (HtmlAnchor) firstPage.getHtmlElementById("link");
        final Page secondPage = anchor.click();
        assertEquals("First", firstPage.getTitleText());
        assertEquals("text/plain", secondPage.getWebResponse().getContentType());

        assertEquals(2, eventCatcher.getEventCount());

        final WebWindow secondWebWindow = (WebWindow) eventCatcher.getEventAt(0).getSource();

        assertSame(webClient.getCurrentWindow(), secondWebWindow);
        assertNotSame(firstWebWindow, secondWebWindow);
    }

    /**
     * Open a window with only XML for content, then try to set focus to it.
     *
     * @throws Exception If the test fails.
     */
    public void testOpenWindow_xml() throws Exception {
        final WebClient webClient = new WebClient();
        final MockWebConnection webConnection = new MockWebConnection(webClient);

        final String firstContent = "<html><head><title>First</title></head><body>"
            + "<form name='form1'>"
            + "    <a id='link' onClick='open(\"http://second\", \"_blank\").focus(); return false;'"
            + "return false;'>Click me</a>"
            + "</form>"
            + "</body></html>";
        final String secondContent = "<junk></junk>";

        final EventCatcher eventCatcher = new EventCatcher();

        webConnection.setResponse(URL_FIRST, firstContent, 200, "OK", "text/html", Collections.EMPTY_LIST);
        webConnection.setResponse(URL_SECOND, secondContent, 200, "OK", "text/xml", Collections.EMPTY_LIST);
        webClient.setWebConnection(webConnection);

        final HtmlPage firstPage = (HtmlPage) webClient.getPage(URL_FIRST);
        assertEquals("First", firstPage.getTitleText());

        eventCatcher.listenTo(webClient);

        final WebWindow firstWebWindow = firstPage.getEnclosingWindow();

        final HtmlAnchor anchor = (HtmlAnchor) firstPage.getHtmlElementById("link");
        final Page secondPage = anchor.click();
        assertEquals("First", firstPage.getTitleText());
        assertEquals("text/xml", secondPage.getWebResponse().getContentType());

        assertEquals(2, eventCatcher.getEventCount());

        final WebWindow secondWebWindow = (WebWindow) eventCatcher.getEventAt(0).getSource();

        assertSame(webClient.getCurrentWindow(), secondWebWindow);
        assertNotSame(firstWebWindow, secondWebWindow);
    }

    /**
     * Open a window with only javascript for content, then try to set focus to it.
     *
     * @throws Exception If the test fails.
     */
    public void testOpenWindow_javascript() throws Exception {
        final WebClient webClient = new WebClient();
        final MockWebConnection webConnection = new MockWebConnection(webClient);

        final String firstContent = "<html><head><title>First</title></head><body>"
            + "<form name='form1'>"
            + "    <a id='link' onClick='open(\"http://second\", \"_blank\").focus(); return false;'"
            + "return false;'>Click me</a>"
            + "</form>"
            + "</body></html>";
        final String secondContent = "var x=1;";

        final EventCatcher eventCatcher = new EventCatcher();

        webConnection.setResponse(URL_FIRST, firstContent, 200, "OK", "text/html", Collections.EMPTY_LIST);
        webConnection.setResponse(URL_SECOND, secondContent, 200, "OK", "text/javascript", Collections.EMPTY_LIST);
        webClient.setWebConnection(webConnection);

        final HtmlPage firstPage = (HtmlPage) webClient.getPage(URL_FIRST);
        assertEquals("First", firstPage.getTitleText());

        eventCatcher.listenTo(webClient);

        final WebWindow firstWebWindow = firstPage.getEnclosingWindow();

        final HtmlAnchor anchor = (HtmlAnchor) firstPage.getHtmlElementById("link");
        final Page secondPage = anchor.click();
        assertEquals("First", firstPage.getTitleText());
        assertEquals("text/javascript", secondPage.getWebResponse().getContentType());

        assertEquals(2, eventCatcher.getEventCount());

        final WebWindow secondWebWindow = (WebWindow) eventCatcher.getEventAt(0).getSource();

        assertSame(webClient.getCurrentWindow(), secondWebWindow);
        assertNotSame(firstWebWindow, secondWebWindow);
    }

    /**
     * Open a window with only text for content, then try to set focus to it.
     *
     * @throws Exception If the test fails.
     */
    public void testOpenWindow_html() throws Exception {
        final WebClient webClient = new WebClient();
        final MockWebConnection webConnection = new MockWebConnection(webClient);

        final String firstContent = "<html><head><title>First</title></head><body>"
            + "<form name='form1'>"
            + "    <a id='link' onClick='open(\"http://second\", \"_blank\").focus(); return false;'"
            + "return false;'>Click me</a>"
            + "</form>"
            + "</body></html>";
        final String secondContent = new String("<html><head><title>Second</title></head><body>"
            + "<p>Hello World</p>"
            + "</body></html>");

        final EventCatcher eventCatcher = new EventCatcher();

        webConnection.setResponse(URL_FIRST, firstContent);
        webConnection.setResponse(URL_SECOND, secondContent);
        webClient.setWebConnection(webConnection);

        final HtmlPage firstPage = (HtmlPage) webClient.getPage(URL_FIRST);
        assertEquals("First", firstPage.getTitleText());

        eventCatcher.listenTo(webClient);

        final WebWindow firstWebWindow = firstPage.getEnclosingWindow();

        final HtmlAnchor anchor = (HtmlAnchor) firstPage.getHtmlElementById("link");
        final Page secondPage = anchor.click();
        assertEquals("First", firstPage.getTitleText());
        assertEquals("text/html", secondPage.getWebResponse().getContentType());

        assertEquals(2, eventCatcher.getEventCount());

        final WebWindow secondWebWindow = (WebWindow) eventCatcher.getEventAt(0).getSource();

        assertSame(webClient.getCurrentWindow(), secondWebWindow);
        assertNotSame(firstWebWindow, secondWebWindow);
    }

    /**
     * Test the 'Referer' HTTP header by window.open
     * @throws Exception if the test fails.
     */
    public void testOpenWindow_refererHeader() throws Exception {
        final String headerIE = null;
        testOpenWindow_refererHeader(BrowserVersion.INTERNET_EXPLORER_6_0, headerIE);

        final String headerFF = URL_FIRST.toString();
        testOpenWindow_refererHeader(BrowserVersion.MOZILLA_1_0, headerFF);

        final String headerNS = URL_FIRST.toString();
        testOpenWindow_refererHeader(BrowserVersion.NETSCAPE_4_7_9, headerNS);
    }

    /**
     * @throws Exception If the test fails
     */
    private void testOpenWindow_refererHeader(final BrowserVersion browser,
            final String expectedRefererHeader) throws Exception {
        final String firstContent = "<html><head></head>"
            + "<body>"
            + "<button id='clickme' onClick='window.open(\"http://second\");'>Click me</a>"
            + "</body></html>";

        final String secondContent
            = "<html><head><title>Second</title></head><body></body></html>";

        final WebClient client = new WebClient(browser);
        final MockWebConnection conn = new MockWebConnection(client);
        client.setWebConnection(conn);

        conn.setResponse(URL_FIRST, firstContent);
        conn.setResponse(URL_SECOND, secondContent);
        
        final HtmlPage firstPage = (HtmlPage) client.getPage(URL_FIRST);
        final HtmlButton buttonA = (HtmlButton)firstPage.getHtmlElementById("clickme");
        
        buttonA.click();
        final Map lastAdditionalHeaders = conn.getLastAdditionalHeaders();
        assertEquals(expectedRefererHeader, lastAdditionalHeaders.get("Referer"));
    }

    /**
     * Tests that nested setTimeouts that are deeper than Thread.MAX_PRIORITY
     * do not cause an exception.
     * @throws Exception If the test fails
     */
    public void testNestedSetTimeoutAboveMaxPriority() throws Exception {
        final int max = Thread.MAX_PRIORITY + 1;
        final String content = "<html><body><script language='JavaScript'>"
            + "var depth = 0;\n"
            + "var maxdepth = " + max + ";\n"
            + "function addAnother() {\n"
            + "  if (depth < maxdepth) {\n"
            + "    window.alert('ping');\n"
            + "    depth++;\n"
            + "    window.setTimeout('addAnother();', 1);\n"
            + "  }\n"
            + "}\n"
            + "addAnother();\n"
            + "</script></body></html>";

        final List collectedAlerts = Collections.synchronizedList(new ArrayList());
        final HtmlPage page = loadPage(content, collectedAlerts);
        assertTrue("threads did not stop in time", page.getEnclosingWindow()
                .getThreadManager().joinAll((max + 1) * 1000));
        assertEquals(Collections.nCopies(max, "ping"), collectedAlerts);
    }

    /**
     * @throws Exception If the test fails
     */
    public void testEvalScopeOtherWindow() throws Exception {
        final String content = "<html><body>"
            + "<iframe src='iframe.html'></iframe>"
            + "</body></html>";
        final String iframe = "<html><body>"
            + "<script>"
            + "window.parent.eval('var foo = 1');"
            + "alert(window.parent.foo)"
            + "</script>"
            + "</body></html>";

        final WebClient webClient = new WebClient();
        final List collectedAlerts = new ArrayList();
        webClient.setAlertHandler( new CollectingAlertHandler(collectedAlerts) );

        final MockWebConnection webConnection = new MockWebConnection( webClient );
        webConnection.setResponse(URL_FIRST, content);
        webConnection.setDefaultResponse(iframe);
        webClient.setWebConnection( webConnection );

        webClient.getPage(URL_FIRST);
        final String[] expectedAlersts = {"1"};
        assertEquals(expectedAlersts, collectedAlerts);
    }

    /**
     * Regression test for [ 1608555 ] JavaScript: window.eval does evaluate local scope
     * https://sourceforge.net/tracker/index.php?func=detail&aid=1608555&group_id=47038&atid=448266
     * @throws Exception If the test fails
     */
    public void testEvalScopeLocal() throws Exception {
        final String content = "<html><body><form id='formtest'><input id='element' value='elementValue'/></form>"
            + "<script> \n"
            + "var docPatate = 'patate';"
            + "function test() {\n"
            + " var f = document.forms['formtest']; \n"
            + " alert(eval(\"document.forms['formtest'].element.value\")); \n"
            + " alert(f.element.value); \n"
            + " alert(eval('f.element.value')); \n"
            + "}\n"
            + "test(); \n"
            + "</script>"
            + "</body></html>";

        final String[] expectedAlerts = {"elementValue", "elementValue", "elementValue"};
        createTestPageForRealBrowserIfNeeded(content, expectedAlerts);
        final List collectedAlerts = new ArrayList();
        loadPage(content, collectedAlerts);
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception If the test fails
     */
    public void testFunctionEquality() throws Exception {
        final String content = "<html><body>"
            + "<script>"
            + "alert(window.focus == window.focus)"
            + "</script>"
            + "</body></html>";

        final List collectedAlerts = new ArrayList();
        final String[] expectedAlerts = {"true"};
        loadPage(content, collectedAlerts);
        createTestPageForRealBrowserIfNeeded(content, expectedAlerts);
        assertEquals(expectedAlerts, collectedAlerts);
    }
    
    /**
     * Test for 1225021.
     * @throws Exception If the test fails
     */
    public void testCaptureEvents() throws Exception {
    	final String content = "<html><head><title>foo</title>"
	    	+ "<script>"
	    	+ "function t()"
	    	+ "{"
	    	+ "	alert('captured');"
	    	+ "}"
	    	+ "window.captureEvents(Event.CLICK);"
	    	+ "window.onclick = t;"
	    	+ "</script></head><body>"
	    	+ "<div id='theDiv' onclick='alert(123)'>foo</div>"
	    	+ "</body></html>";
    	
        final List collectedAlerts = new ArrayList();
    	final HtmlPage page = loadPage(BrowserVersion.MOZILLA_1_0, content, collectedAlerts);
    	((ClickableElement) page.getHtmlElementById("theDiv")).click();

        final String[] expectedAlerts = {"123", "captured"};
        assertEquals(expectedAlerts, collectedAlerts);
    }
}
