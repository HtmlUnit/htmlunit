/*
 * Copyright (c) 2002-2014 Gargoyle Software Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.gargoylesoftware.htmlunit.javascript.host;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.base.testing.EventCatcher;
import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;
import com.gargoylesoftware.htmlunit.CollectingAlertHandler;
import com.gargoylesoftware.htmlunit.ConfirmHandler;
import com.gargoylesoftware.htmlunit.DialogWindow;
import com.gargoylesoftware.htmlunit.MockWebConnection;
import com.gargoylesoftware.htmlunit.OnbeforeunloadHandler;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.PromptHandler;
import com.gargoylesoftware.htmlunit.SimpleWebTestCase;
import com.gargoylesoftware.htmlunit.StatusHandler;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebWindow;
import com.gargoylesoftware.htmlunit.WebWindowEvent;
import com.gargoylesoftware.htmlunit.WebWindowNotFoundException;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlButton;
import com.gargoylesoftware.htmlunit.html.HtmlButtonInput;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlInlineFrame;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.util.NameValuePair;

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
 * @author Frank Danek
 */
@RunWith(BrowserRunner.class)
public class WindowTest extends SimpleWebTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void setLocation() throws Exception {
        final WebClient webClient = getWebClient();
        final MockWebConnection webConnection = new MockWebConnection();

        final String firstContent
            = "<html><head><title>First</title></head><body>\n"
            + "<form name='form1'>\n"
            + "    <a id='link' onClick='location=\"" + URL_SECOND + "\"; return false;'>Click me</a>\n"
            + "</form>\n"
            + "</body></html>";
        final String secondContent
            = "<html><head><title>Second</title></head><body></body></html>";

        webConnection.setResponse(URL_FIRST, firstContent);
        webConnection.setResponse(URL_SECOND, secondContent);
        webClient.setWebConnection(webConnection);

        final HtmlPage firstPage = webClient.getPage(URL_FIRST);
        assertEquals("First", firstPage.getTitleText());

        final HtmlAnchor anchor = firstPage.getHtmlElementById("link");
        final HtmlPage secondPage = anchor.click();
        assertNotNull("secondPage", secondPage);
        assertEquals("Second", secondPage.getTitleText());
        assertSame(webClient.getCurrentWindow(), secondPage.getEnclosingWindow());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void openWindow() throws Exception {
        final WebClient webClient = getWebClient();
        final MockWebConnection webConnection = new MockWebConnection();

        final List<String> collectedAlerts = new ArrayList<String>();
        webClient.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        final String firstContent
            = "<html><head><title>First</title></head><body>\n"
            + "<form name='form1'>\n"
            + "    <a id='link' onClick='window.open(\"" + URL_SECOND + "\", \"MyNewWindow\").focus(); "
            + "return false;'>Click me</a>\n"
            + "</form>\n"
            + "</body></html>";
        final String secondContent
            = "<html><head><title>Second</title></head><body>\n"
            + "<script>alert(self.name)</script>\n"
            + "</body></html>";

        final EventCatcher eventCatcher = new EventCatcher();
        eventCatcher.listenTo(webClient);

        webConnection.setResponse(URL_FIRST, firstContent);
        webConnection.setResponse(URL_SECOND, secondContent);
        webClient.setWebConnection(webConnection);

        final HtmlPage firstPage = webClient.getPage(URL_FIRST);
        assertEquals("First", firstPage.getTitleText());

        final HtmlAnchor anchor = firstPage.getHtmlElementById("link");
        final HtmlPage secondPage = anchor.click();
        assertNotSame(firstPage, secondPage);

        // Expecting contentChanged, opened, contentChanged
        assertEquals(3, eventCatcher.getEventCount());

        final WebWindow firstWebWindow = (WebWindow) ((WebWindowEvent) eventCatcher.getEventAt(0)).getSource();
        final WebWindow secondWebWindow = (WebWindow) ((WebWindowEvent) eventCatcher.getEventAt(2)).getSource();
        assertSame(webClient.getCurrentWindow(), secondWebWindow);
        assertEquals("MyNewWindow", secondWebWindow.getName());

        assertEquals("First", ((HtmlPage) firstWebWindow.getEnclosedPage()).getTitleText());
        assertEquals("Second", ((HtmlPage) secondWebWindow.getEnclosedPage()).getTitleText());

        final WebWindowEvent changedEvent = (WebWindowEvent) eventCatcher.getEventAt(2);
        assertNull(changedEvent.getOldPage());
        assertEquals("Second", ((HtmlPage) changedEvent.getNewPage()).getTitleText());

        assertEquals(new String[] {"MyNewWindow"}, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void openWindow_base() throws Exception {
        final WebClient webClient = getWebClient();
        final MockWebConnection webConnection = new MockWebConnection();

        final List<String> collectedAlerts = new ArrayList<String>();
        webClient.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        final String firstContent
            = "<html><head><title>First</title><base target='MyNewWindow'></head><body>\n"
            + "<form name='form1'>\n"
            + "    <a id='link' href='" + URL_SECOND + "'>Click me</a>\n"
            + "</form>\n"
            + "</body></html>";
        final String secondContent
            = "<html><head><title>Second</title></head><body>\n"
            + "<script>alert(self.name)</script>\n"
            + "</body></html>";

        webConnection.setResponse(URL_FIRST, firstContent);
        webConnection.setResponse(URL_SECOND, secondContent);
        webClient.setWebConnection(webConnection);

        final HtmlPage firstPage = webClient.getPage(URL_FIRST);
        assertEquals("First", firstPage.getTitleText());
        final WebWindow firstWebWindow = firstPage.getEnclosingWindow();
        assertEquals(firstWebWindow, firstWebWindow.getTopWindow());

        final HtmlAnchor anchor = firstPage.getHtmlElementById("link");
        final HtmlPage secondPage = anchor.click();
        assertEquals("Second", secondPage.getTitleText());
        assertNotSame(firstPage, secondPage);

        final WebWindow secondWebWindow = secondPage.getEnclosingWindow();
        assertNotSame(firstWebWindow, secondWebWindow);
        assertEquals("MyNewWindow", secondWebWindow.getName());
        assertEquals(secondWebWindow, secondWebWindow.getTopWindow());

        assertEquals(new String[] {"MyNewWindow"}, collectedAlerts);
    }

    /**
     * _blank is a magic name. If we call open(url, '_blank') then a new
     * window must be loaded.
     * @throws Exception if the test fails
     */
    @Test
    public void openWindow_blank() throws Exception {
        final WebClient webClient = getWebClient();
        final MockWebConnection webConnection = new MockWebConnection();

        final List<String> collectedAlerts = new ArrayList<String>();
        webClient.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        final String firstContent
            = "<html><head><title>First</title></head><body>\n"
            + "  <iframe name='secondFrame' id='secondFrame' src='" + URL_SECOND + "'></iframe>\n"
            + "</body></html>";
        final String secondContent
            = "<html><head><title>Second</title></head><body>\n"
            + "  <a id='link' "
            + "onClick='window.open(\"" + URL_THIRD + "\", \"_blank\").focus(); '>\n"
            + "Click me</a>\n"
            + "</body></html>";
        final String thirdContent
            = "<html><head><title>Third</title></head><body>\n"
            + "</body></html>";

        webConnection.setResponse(URL_FIRST, firstContent);
        webConnection.setResponse(URL_SECOND, secondContent);
        webConnection.setResponse(URL_THIRD, thirdContent);
        webClient.setWebConnection(webConnection);

        final HtmlPage firstPage = webClient.getPage(URL_FIRST);
        assertEquals("First", firstPage.getTitleText());
        final WebWindow firstWindow = firstPage.getEnclosingWindow();

        final HtmlInlineFrame secondFrame = firstPage.getHtmlElementById("secondFrame");
        final HtmlPage secondPage = (HtmlPage) secondFrame.getEnclosedPage();
        assertEquals("Second", secondPage.getTitleText());
        try {
            assertEquals(secondFrame.getEnclosedWindow(), webClient.getWebWindowByName("secondFrame"));
            // Expected path
        }
        catch (final WebWindowNotFoundException exception) {
            fail("Expected secondFrame would be found before click.");
        }
        final HtmlAnchor anchor = secondPage.getHtmlElementById("link");
        final HtmlPage thirdPage = anchor.click();
        assertEquals("Third", thirdPage.getTitleText());
        final WebWindow thirdWindow = thirdPage.getEnclosingWindow();
        assertNotSame(firstWindow, thirdWindow);

        assertEquals("", thirdWindow.getName());

        assertEquals(thirdWindow, thirdWindow.getTopWindow());
        try {
            assertEquals(secondFrame.getEnclosedWindow(), webClient.getWebWindowByName("secondFrame"));
            // Expected path
        }
        catch (final WebWindowNotFoundException exception) {
            fail("Expected secondFrame would be found after click.");
        }

        assertEquals(Collections.EMPTY_LIST, collectedAlerts);
    }

    /**
     * _self is a magic name. If we call open(url, '_self') then the current window must be
     * reloaded.
     * @throws Exception if the test fails
     */
    @Test
    public void openWindow_self() throws Exception {
        final WebClient webClient = getWebClient();
        final MockWebConnection webConnection = new MockWebConnection();

        final String firstContent
            = "<html><head><title>First</title></head><body>\n"
            + "<form name='form1'>\n"
            + "    <a id='link' onClick='window.open(\"" + URL_SECOND + "\", \"_self\"); "
            + "return false;'>Click me</a>\n"
            + "</form>\n"
            + "</body></html>";
        final String secondContent = "<html><head><title>Second</title></head><body></body></html>";

        final EventCatcher eventCatcher = new EventCatcher();

        webConnection.setResponse(URL_FIRST, firstContent);
        webConnection.setResponse(URL_SECOND, secondContent);
        webClient.setWebConnection(webConnection);

        final HtmlPage firstPage = webClient.getPage(URL_FIRST);
        assertEquals("First", firstPage.getTitleText());

        eventCatcher.listenTo(webClient);

        final WebWindow firstWebWindow = firstPage.getEnclosingWindow();

        final HtmlAnchor anchor = firstPage.getHtmlElementById("link");
        final HtmlPage secondPage = anchor.click();
        assertEquals("First", firstPage.getTitleText());
        assertEquals("Second", secondPage.getTitleText());

        assertEquals(1, eventCatcher.getEventCount());

        final WebWindow secondWebWindow = (WebWindow) ((WebWindowEvent) eventCatcher.getEventAt(0)).getSource();
        assertSame(webClient.getCurrentWindow(), firstWebWindow);
        assertSame(firstWebWindow, secondWebWindow);
    }

    /**
     * _top is a magic name. If we call open(url, '_top') then the top level
     * window must be reloaded.
     * @throws Exception if the test fails
     */
    @Test
    public void openWindow_top() throws Exception {
        final WebClient webClient = getWebClient();
        final MockWebConnection webConnection = new MockWebConnection();

        final String firstContent
            = "<html><head><title>First</title></head><body>\n"
            + "  <iframe name='secondFrame' id='secondFrame' src='" + URL_SECOND + "'></iframe>\n"
            + "</body></html>";
        final String secondContent
            = "<html><head><title>Second</title></head><body>\n"
            + "  <iframe name='thirdFrame' id='thirdFrame' src='" + URL_THIRD + "'></iframe>\n"
            + "</body></html>";
        final String thirdContent
            = "<html><head><title>Third</title></head><body>\n"
            + "    <a id='link' onClick='window.open(\"http://fourth\", \"_top\"); "
            + "return false;'>Click me</a>\n"
            + "</body></html>";
        final String fourthContent = "<html><head><title>Fourth</title></head><body></body></html>";

        webConnection.setResponse(URL_FIRST, firstContent);
        webConnection.setResponse(URL_SECOND, secondContent);
        webConnection.setResponse(URL_THIRD, thirdContent);
        webConnection.setResponse(new URL("http://fourth/"), fourthContent);
        webClient.setWebConnection(webConnection);

        final HtmlPage firstPage = webClient.getPage(URL_FIRST);
        assertEquals("First", firstPage.getTitleText());

        final WebWindow firstWebWindow = firstPage.getEnclosingWindow();
        assertEquals("First", firstPage.getTitleText());
        final HtmlInlineFrame secondFrame = firstPage.getHtmlElementById("secondFrame");
        final HtmlPage secondPage = (HtmlPage) secondFrame.getEnclosedPage();
        assertEquals("Second", secondPage.getTitleText());
        final HtmlInlineFrame thirdFrame = secondPage.getHtmlElementById("thirdFrame");
        final HtmlPage thirdPage = (HtmlPage) thirdFrame.getEnclosedPage();
        assertEquals("Third", thirdPage.getTitleText());

        assertSame(webClient.getCurrentWindow(), firstWebWindow);
        assertNotSame(firstWebWindow, secondPage);

        final HtmlAnchor anchor = thirdPage.getHtmlElementById("link");
        final HtmlPage fourthPage = anchor.click();
        final WebWindow fourthWebWindow = fourthPage.getEnclosingWindow();
        assertSame(firstWebWindow, fourthWebWindow);
        assertSame(fourthWebWindow, fourthWebWindow.getTopWindow());
        try {
            webClient.getWebWindowByName("secondFrame");
            fail("Did not expect secondFrame to still exist after click.");
        }
        catch (final WebWindowNotFoundException exception) {
            // Expected path
        }
        try {
            webClient.getWebWindowByName("thirdFrame");
            fail("Did not expect thirdFrame to still exist after click.");
        }
        catch (final WebWindowNotFoundException exception) {
            // Expected path
        }
    }

    /**
     * _parent is a magic name. If we call open(url, '_parent') then the
     * parent window must be reloaded.
     * @throws Exception if the test fails
     */
    @Test
    public void openWindow_parent() throws Exception {
        final WebClient webClient = getWebClient();
        final MockWebConnection webConnection = new MockWebConnection();

        final String firstContent
            = "<html><head><title>First</title></head><body>\n"
            + "  <iframe name='secondFrame' id='secondFrame' src='" + URL_SECOND + "'></iframe>\n"
            + "</body></html>";
        final String secondContent
            = "<html><head><title>Second</title></head><body>\n"
            + "  <iframe name='thirdFrame' id='thirdFrame' src='" + URL_THIRD + "'></iframe>\n"
            + "</body></html>";
        final String thirdContent
            = "<html><head><title>Third</title></head><body>\n"
            + "    <a id='link' onClick='window.open(\"http://fourth\", \"_parent\"); "
            + "return false;'>Click me</a>\n"
            + "</body></html>";
        final String fourthContent = "<html><head><title>Fourth</title></head><body></body></html>";

        webConnection.setResponse(URL_FIRST, firstContent);
        webConnection.setResponse(URL_SECOND, secondContent);
        webConnection.setResponse(URL_THIRD, thirdContent);
        webConnection.setResponse(new URL("http://fourth/"), fourthContent);
        webClient.setWebConnection(webConnection);

        final HtmlPage firstPage = webClient.getPage(URL_FIRST);
        assertEquals("First", firstPage.getTitleText());

        final WebWindow firstWebWindow = firstPage.getEnclosingWindow();
        assertEquals("First", firstPage.getTitleText());
        final HtmlInlineFrame secondFrame = firstPage.getHtmlElementById("secondFrame");
        final HtmlPage secondPage = (HtmlPage) secondFrame.getEnclosedPage();
        assertEquals("Second", secondPage.getTitleText());
        final HtmlInlineFrame thirdFrame = secondPage.getHtmlElementById("thirdFrame");
        final HtmlPage thirdPage = (HtmlPage) thirdFrame.getEnclosedPage();
        assertEquals("Third", thirdPage.getTitleText());

        assertSame(webClient.getCurrentWindow(), firstWebWindow);
        assertNotSame(firstWebWindow, secondFrame);

        final HtmlAnchor anchor = thirdPage.getHtmlElementById("link");
        final HtmlPage fourthPage = anchor.click();
        final WebWindow fourthWebWindow = fourthPage.getEnclosingWindow();
        assertSame(secondFrame.getEnclosedWindow(), fourthWebWindow);
        try {
            final WebWindow namedWindow = webClient.getWebWindowByName("secondFrame");
            assertSame(namedWindow.getEnclosedPage(), fourthPage);
            // Expected path
        }
        catch (final WebWindowNotFoundException exception) {
            fail("Expected secondFrame would be found after click.");
        }
        try {
            webClient.getWebWindowByName("thirdFrame");
            fail("Did not expect thirdFrame to still exist after click.");
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
    @Test
    @Alerts({ "true", "true", "true" })
    public void openWindow_existingWindow() throws Exception {
        final String html
            = "<html><head><script>\n"
            + "function test() {\n"
            + "  var w1 = window.open('about:blank', 'foo');\n"
            + "  alert(w1 != null);\n"
            + "  var w2 = window.open('', 'foo');\n"
            + "  alert(w1 == w2);\n"
            + "  var w3 = window.open('', 'myFrame');\n"
            + "  alert(w3 == window.frames.myFrame);\n"
            + "}\n"
            + "</script></head><body onload='test()'>\n"
            + "<iframe name='myFrame' id='myFrame'></iframe>\n"
            + "</body></html>";

        loadPageWithAlerts(html);
    }

    /**
     * Verifies that <tt>window.open</tt> behaves correctly when popups are blocked.
     * @throws Exception if an error occurs
     */
    @Test
    public void openWindow_blocked() throws Exception {
        final String html =
            "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + "  var w;\n"
            + "  function test() {\n"
            + "    w = window.open('', 'foo');\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "<div id='d' onclick='alert(w)'>test</div>\n"
            + "</body></html>";

        final List<String> actual = new ArrayList<String>();
        final WebClient client = getWebClient();
        client.getOptions().setPopupBlockerEnabled(true);
        client.setAlertHandler(new CollectingAlertHandler(actual));

        final MockWebConnection webConnection = new MockWebConnection();
        webConnection.setDefaultResponse(html);
        client.setWebConnection(webConnection);

        final HtmlPage page = client.getPage("http://foo");
        ((HtmlDivision) page.getHtmlElementById("d")).click();
        final String[] expected = {"null"};
        assertEquals(expected, actual);
    }

    /**
     * Regression test to reproduce a known bug.
     * @throws Exception if the test fails
     */
    @Test
    public void alert_NoAlertHandler() throws Exception {
        final String firstContent
            = "<html><head><title>First</title><script>function doTest(){alert('foo')}</script></head>\n"
            + "<body onload='doTest()'></body></html>";

        final HtmlPage firstPage = loadPage(firstContent);
        assertEquals("First", firstPage.getTitleText());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void parentAndTop() throws Exception {
        final String firstContent
            = "<html><head><title>First</title></head><body>\n"
            + "  <iframe name='left' src='" + URL_SECOND + "'></iframe>\n"
            + "</body></html>";
        final String secondContent
            = "<html><head><title>Second</title></head><body>\n"
            + "  <iframe name='innermost' src='" + URL_THIRD + "'></iframe>\n"
            + "</body></html>";
        final String thirdContent
            = "<html><head><title>Third</title><script>\n"
            + "function doAlert() {\n"
            + "    alert(parent != this);\n"
            + "    alert(top != this);\n"
            + "    alert(parent != top);\n"
            + "    alert(parent.parent == top);\n"
            + "    alert(parent.frames[0] == this);\n"
            + "    alert(top.frames[0] == parent);\n"
            + "}\n"
            + "</script></head>\n"
            + "<body><a id='clickme' onClick='doAlert()'>foo</a></body></html>";

        final WebClient webClient = getWebClient();
        final List<String> collectedAlerts = new ArrayList<String>();
        webClient.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        final MockWebConnection webConnection = new MockWebConnection();
        webConnection.setResponse(URL_FIRST, firstContent);
        webConnection.setResponse(URL_SECOND, secondContent);
        webConnection.setResponse(URL_THIRD, thirdContent);

        webClient.setWebConnection(webConnection);

        final HtmlPage firstPage = webClient.getPage(URL_FIRST);
        assertEquals("First", firstPage.getTitleText());

        final WebWindow innermostWebWindow = webClient.getWebWindowByName("innermost");
        final HtmlPage innermostPage = (HtmlPage) innermostWebWindow.getEnclosedPage();
        innermostPage.getHtmlElementById("clickme").click();

        assertNotSame(innermostWebWindow.getParentWindow(), innermostWebWindow);
        assertNotSame(innermostWebWindow.getTopWindow(), innermostWebWindow);
        assertNotSame(innermostWebWindow.getParentWindow(), innermostWebWindow.getTopWindow());
        assertSame(innermostWebWindow.getParentWindow().getParentWindow(), innermostWebWindow.getTopWindow());

        assertEquals(new String[] {"true", "true", "true", "true", "true", "true"}, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void confirm() throws Exception {
        final WebClient webClient = getWebClient();
        final MockWebConnection webConnection = new MockWebConnection();
        final List<String> collectedAlerts = new ArrayList<String>();
        final List<String> collectedConfirms = new ArrayList<String>();

        webClient.setAlertHandler(new CollectingAlertHandler(collectedAlerts));
        webClient.setConfirmHandler(new ConfirmHandler() {
            public boolean handleConfirm(final Page page, final String message) {
                collectedConfirms.add(message);
                return true;
            }
        });

        final String firstContent
            = "<html><head><title>First</title><script>function doTest(){alert(confirm('foo'))}</script>\n"
            + "</head><body onload='doTest()'></body></html>";

        webConnection.setResponse(URL_FIRST, firstContent);
        webClient.setWebConnection(webConnection);

        final HtmlPage firstPage = webClient.getPage(URL_FIRST);
        assertEquals("First", firstPage.getTitleText());

        assertEquals(new String[] {"foo"}, collectedConfirms);
        assertEquals(new String[] {"true"}, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void confirm_noConfirmHandler() throws Exception {
        final String html
            = "<html><head><title>First</title><script>function doTest(){alert(confirm('foo'))}</script>\n"
            + "</head><body onload='doTest()'></body></html>";

        final List<String> collectedAlerts = new ArrayList<String>();
        loadPage(html, collectedAlerts);

        assertEquals(new String[] {"true"}, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void prompt() throws Exception {
        final WebClient webClient = getWebClient();
        final MockWebConnection webConnection = new MockWebConnection();
        final List<String> collectedAlerts = new ArrayList<String>();
        final List<String> collectedPrompts = new ArrayList<String>();

        webClient.setAlertHandler(new CollectingAlertHandler(collectedAlerts));
        webClient.setPromptHandler(new PromptHandler() {
            public String handlePrompt(final Page page, final String message) {
                collectedPrompts.add(message);
                return "Flintstone";
            }
        });

        final String firstContent
            = "<html><head><title>First</title><script>function doTest(){alert(prompt('foo'))}</script>\n"
            + "</head><body onload='doTest()'></body></html>";

        webConnection.setResponse(URL_FIRST, firstContent);
        webClient.setWebConnection(webConnection);

        final HtmlPage firstPage = webClient.getPage(URL_FIRST);
        assertEquals("First", firstPage.getTitleText());

        assertEquals(new String[] {"foo"}, collectedPrompts);
        assertEquals(new String[] {"Flintstone"}, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void prompt_noPromptHandler() throws Exception {
        final WebClient webClient = getWebClient();
        final MockWebConnection webConnection = new MockWebConnection();
        final List<String> collectedAlerts = new ArrayList<String>();
        final List<String> collectedPrompts = new ArrayList<String>();

        webClient.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        final String firstContent
            = "<html><head><title>First</title><script>function doTest(){alert(prompt('foo'))}</script>\n"
            + "</head><body onload='doTest()'></body></html>";

        webConnection.setResponse(URL_FIRST, firstContent);
        webClient.setWebConnection(webConnection);

        final HtmlPage firstPage = webClient.getPage(URL_FIRST);
        assertEquals("First", firstPage.getTitleText());

        assertEquals(Collections.EMPTY_LIST, collectedPrompts);
        assertEquals(new String[] {"null"}, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void setOpenerLocationHrefRelative() throws Exception {
        final WebClient webClient = getWebClient();
        final MockWebConnection webConnection = new MockWebConnection();

        final String aContent
            = "<html><head><title>A</title></head><body>\n"
            + "<button id='clickme' onClick='window.open(\"b/b.html\");'>Click me</a>\n"
            + "</body></html>";
        final String bContent
            = "<html><head><title>B</title></head><body>\n"
            + "<button id='clickme' onClick='opener.location.href=\"../c.html\";'>Click me</a>\n"
            + "</body></html>";
        final String cContent
            = "<html><head><title>C</title></head><body></body></html>";
        final String failContent
            = "<html><head><title>FAILURE!!!</title></head><body></body></html>";

        webConnection.setResponse(new URL("http://opener/test/a.html"), aContent);
        webConnection.setResponse(new URL("http://opener/test/b/b.html"), bContent);
        webConnection.setResponse(new URL("http://opener/test/c.html"), cContent);
        webConnection.setResponse(new URL("http://opener/c.html"), failContent);

        webClient.setWebConnection(webConnection);

        final HtmlPage firstPage = webClient.getPage("http://opener/test/a.html");
        assertEquals("A", firstPage.getTitleText());

        final HtmlButton buttonA = firstPage.getHtmlElementById("clickme");
        final HtmlPage pageB = buttonA.click();
        assertNotNull("B", pageB);
        assertEquals("B", pageB.getTitleText());

        final HtmlButton buttonB = pageB.getHtmlElementById("clickme");
        final HtmlPage thirdPage = buttonB.click();
        assertSame("Page B has lost focus", pageB, thirdPage);
        assertEquals("C", ((HtmlPage) firstPage.getEnclosingWindow().getEnclosedPage()).getTitleText());
    }

    /**
     * Test closing using JavaScript.
     * @throws Exception if the test fails
     */
    @Test
    public void close() throws Exception {
        final WebClient webClient = getWebClient();
        final MockWebConnection webConnection = new MockWebConnection();

        final List<String> collectedAlerts = new ArrayList<String>();
        webClient.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        final String firstContent
            = "<html><head><title>First</title></head><body>\n"
             + "<a href='" + URL_SECOND + "' id='link' target='_blank'>Link</a>\n"
             + "</body></html>";
        final String secondContent
            = "<html><head><title>Second</title></head><body>\n"
             + "<h1>Second</h1><form>\n"
             + "<input type='submit' name='action' value='Close' id='button' "
             + "onclick='window.close(); return false;'>\n"
             + "</form></body></html>";

        webConnection.setResponse(URL_FIRST, firstContent);
        webConnection.setResponse(URL_SECOND, secondContent);
        webClient.setWebConnection(webConnection);

        final HtmlPage firstPage = webClient.getPage(URL_FIRST);
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

        final List<WebWindowEvent> expectedEvents = Arrays.asList(new WebWindowEvent[]{
            new WebWindowEvent(secondWindow, WebWindowEvent.CLOSE, secondPage, null)
        });
        assertEquals(expectedEvents, eventCatcher.getEvents());

        assertEquals(1, webClient.getWebWindows().size());
        assertEquals(firstWindow, webClient.getCurrentWindow());

        assertEquals(Collections.EMPTY_LIST, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void status() throws Exception {
        final WebClient webClient = getWebClient();
        final MockWebConnection webConnection = new MockWebConnection();

        final String firstContent
            = "<html><head><title>First</title><script>\n"
            + "function doTest() {\n"
            + "    alert(window.status);\n"
            + "    window.status = 'newStatus';\n"
            + "    alert(window.status);\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "</body></html>";

        final URL url = URL_FIRST;
        webConnection.setResponse(url, firstContent);
        webClient.setWebConnection(webConnection);

        final List<String> collectedAlerts = new ArrayList<String>();
        webClient.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        final List<String> collectedStatus = new ArrayList<String>();
        webClient.setStatusHandler(new StatusHandler() {
            public void statusMessageChanged(final Page page, final String message) {
                collectedStatus.add(message);
            }
        });
        final HtmlPage firstPage = webClient.getPage(URL_FIRST);
        assertEquals("First", firstPage.getTitleText());

        final String[] expectedAlerts = {"", "newStatus"};
        assertEquals("alerts", expectedAlerts, collectedAlerts);

        final String[] expectedStatus = {"newStatus"};
        assertEquals("status", expectedStatus, collectedStatus);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void print() throws Exception {
        final String html = "<html>\n"
            + "<head></head>\n"
            + "<body>\n"
            + "<script>\n"
            + "window.print();\n"
            + "</script>\n"
            + "</body>\n"
            + "</html>";

        loadPageWithAlerts(html);
    }

    /**
     * Open a window with only an image for content, then try to set focus to it.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void openWindow_image() throws Exception {
        final WebClient webClient = getWebClient();
        final MockWebConnection webConnection = new MockWebConnection();

        final String firstContent = "<html><head><title>First</title></head><body>\n"
            + "<form name='form1'>\n"
            + "    <a id='link' onClick='window.open(\"" + URL_SECOND + "\", \"_blank\").focus(); return false;'"
            + "return false;'>Click me</a>\n"
            + "</form>\n"
            + "</body></html>";
        final String secondContent = new String(new char[]{
            'G', 'I', 'F', '8', '9', 'a', 0x01, 0x00,
            0x01, 0x00, 0x80, 0x00, 0x00, 0xfe, 0xd4, 0xaf,
            0x00, 0x00, 0x00, 0x21, 0xf9, 0x04, 0x00, 0x00,
            0x00, 0x00, 0x00, 0x2c, 0x00, 0x00, 0x00, 0x00,
            0x01, 0x00, 0x01, 0x00, 0x00, 0x02, 0x02, 0x44,
            0x01, 0x00, 0x3b});

        final EventCatcher eventCatcher = new EventCatcher();

        final List<NameValuePair> emptyList = Collections.emptyList();
        webConnection.setResponse(URL_FIRST, firstContent, 200, "OK", "text/html", emptyList);
        webConnection.setResponse(URL_SECOND, secondContent, 200, "OK", "image/gif", emptyList);
        webClient.setWebConnection(webConnection);

        final HtmlPage firstPage = webClient.getPage(URL_FIRST);
        assertEquals("First", firstPage.getTitleText());

        eventCatcher.listenTo(webClient);

        final WebWindow firstWebWindow = firstPage.getEnclosingWindow();

        final HtmlAnchor anchor = firstPage.getHtmlElementById("link");
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
     * @throws Exception if the test fails
     */
    @Test
    public void openWindow_text() throws Exception {
        final WebClient webClient = getWebClient();
        final MockWebConnection webConnection = new MockWebConnection();

        final String firstContent = "<html><head><title>First</title></head><body>\n"
            + "<form name='form1'>\n"
            + "    <a id='link' onClick='window.open(\"" + URL_SECOND + "\", \"_blank\").focus(); return false;'"
            + "return false;'>Click me</a>\n"
            + "</form>\n"
            + "</body></html>";
        final String secondContent = "Hello World";

        final EventCatcher eventCatcher = new EventCatcher();

        final List<NameValuePair> emptyList = Collections.emptyList();
        webConnection.setResponse(URL_FIRST, firstContent, 200, "OK", "text/html", emptyList);
        webConnection.setResponse(URL_SECOND, secondContent, 200, "OK", "text/plain", emptyList);
        webClient.setWebConnection(webConnection);

        final HtmlPage firstPage = webClient.getPage(URL_FIRST);
        assertEquals("First", firstPage.getTitleText());

        eventCatcher.listenTo(webClient);

        final WebWindow firstWebWindow = firstPage.getEnclosingWindow();

        final HtmlAnchor anchor = firstPage.getHtmlElementById("link");
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
     * @throws Exception if the test fails
     */
    @Test
    public void openWindow_xml() throws Exception {
        final WebClient webClient = getWebClient();
        final MockWebConnection webConnection = new MockWebConnection();

        final String firstContent = "<html><head><title>First</title></head><body>\n"
            + "<form name='form1'>\n"
            + "    <a id='link' onClick='window.open(\"" + URL_SECOND + "\", \"_blank\").focus(); return false;'"
            + "return false;'>Click me</a>\n"
            + "</form>\n"
            + "</body></html>";
        final String secondContent = "<junk></junk>\n";

        final EventCatcher eventCatcher = new EventCatcher();

        final List<NameValuePair> emptyList = Collections.emptyList();
        webConnection.setResponse(URL_FIRST, firstContent, 200, "OK", "text/html", emptyList);
        webConnection.setResponse(URL_SECOND, secondContent, 200, "OK", "text/xml", emptyList);
        webClient.setWebConnection(webConnection);

        final HtmlPage firstPage = webClient.getPage(URL_FIRST);
        assertEquals("First", firstPage.getTitleText());

        eventCatcher.listenTo(webClient);

        final WebWindow firstWebWindow = firstPage.getEnclosingWindow();

        final HtmlAnchor anchor = firstPage.getHtmlElementById("link");
        final Page secondPage = anchor.click();
        assertEquals("First", firstPage.getTitleText());
        assertEquals("text/xml", secondPage.getWebResponse().getContentType());

        assertEquals(2, eventCatcher.getEventCount());

        final WebWindow secondWebWindow = (WebWindow) eventCatcher.getEventAt(0).getSource();

        assertSame(webClient.getCurrentWindow(), secondWebWindow);
        assertNotSame(firstWebWindow, secondWebWindow);
    }

    /**
     * Open a window with only JavaScript for content, then try to set focus to it.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void openWindow_javascript() throws Exception {
        final WebClient webClient = getWebClient();
        final MockWebConnection webConnection = new MockWebConnection();

        final String firstContent = "<html><head><title>First</title></head><body>\n"
            + "<form name='form1'>\n"
            + "    <a id='link' onClick='window.open(\"" + URL_SECOND + "\", \"_blank\").focus(); return false;'"
            + "return false;'>Click me</a>\n"
            + "</form>\n"
            + "</body></html>";
        final String secondContent = "var x=1;\n";

        final EventCatcher eventCatcher = new EventCatcher();

        final List<NameValuePair> emptyList = Collections.emptyList();
        webConnection.setResponse(URL_FIRST, firstContent, 200, "OK", "text/html", emptyList);
        webConnection.setResponse(URL_SECOND, secondContent, 200, "OK", "text/javascript", emptyList);
        webClient.setWebConnection(webConnection);

        final HtmlPage firstPage = webClient.getPage(URL_FIRST);
        assertEquals("First", firstPage.getTitleText());

        eventCatcher.listenTo(webClient);

        final WebWindow firstWebWindow = firstPage.getEnclosingWindow();

        final HtmlAnchor anchor = firstPage.getHtmlElementById("link");
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
     * @throws Exception if the test fails
     */
    @Test
    public void openWindow_html() throws Exception {
        final WebClient webClient = getWebClient();
        final MockWebConnection webConnection = new MockWebConnection();

        final String firstContent = "<html><head><title>First</title></head><body>\n"
            + "<form name='form1'>\n"
            + "    <a id='link' onClick='window.open(\"" + URL_SECOND + "\", \"_blank\").focus(); return false;'"
            + "return false;'>Click me</a>\n"
            + "</form>\n"
            + "</body></html>";
        final String secondContent = "<html><head><title>Second</title></head><body>\n"
            + "<p>Hello World</p>\n"
            + "</body></html>";

        final EventCatcher eventCatcher = new EventCatcher();

        webConnection.setResponse(URL_FIRST, firstContent);
        webConnection.setResponse(URL_SECOND, secondContent);
        webClient.setWebConnection(webConnection);

        final HtmlPage firstPage = webClient.getPage(URL_FIRST);
        assertEquals("First", firstPage.getTitleText());

        eventCatcher.listenTo(webClient);

        final WebWindow firstWebWindow = firstPage.getEnclosingWindow();

        final HtmlAnchor anchor = firstPage.getHtmlElementById("link");
        final Page secondPage = anchor.click();
        assertEquals("First", firstPage.getTitleText());
        assertEquals("text/html", secondPage.getWebResponse().getContentType());

        assertEquals(2, eventCatcher.getEventCount());

        final WebWindow secondWebWindow = (WebWindow) eventCatcher.getEventAt(0).getSource();

        assertSame(webClient.getCurrentWindow(), secondWebWindow);
        assertNotSame(firstWebWindow, secondWebWindow);
    }

    /**
     * Basic test for the <tt>showModalDialog</tt> method. See bug 2124916.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({ "undefined", "Jane", "Smith", "sdg", "finished" })
    public void showModalDialog() throws Exception {
        final String html1
            = "<html><head><script>\n"
            + "  function test() {\n"
            + "    alert(window.returnValue);\n"
            + "    var o = new Object();\n"
            + "    o.firstName = 'Jane';\n"
            + "    o.lastName = 'Smith';\n"
            + "    var ret = showModalDialog('myDialog.html', o, 'dialogHeight:300px; dialogLeft:200px;');\n"
            + "    alert(ret);\n"
            + "    alert('finished');\n"
            + "  }\n"
            + "</script></head><body>\n"
            + "  <button onclick='test()' id='b'>Test</button>\n"
            + "</body></html>";

        final String html2
            = "<html><head><script>\n"
            + "  var o = window.dialogArguments;\n"
            + "  alert(o.firstName);\n"
            + "  alert(o.lastName);\n"
            + "  window.returnValue = 'sdg';\n"
            + "</script></head>\n"
            + "<body>foo</body></html>";

        final WebClient client = getWebClient();
        final List<String> actual = new ArrayList<String>();
        client.setAlertHandler(new CollectingAlertHandler(actual));

        final MockWebConnection conn = new MockWebConnection();
        conn.setResponse(URL_FIRST, html1);
        conn.setResponse(new URL(URL_FIRST.toExternalForm() + "myDialog.html"), html2);
        client.setWebConnection(conn);

        final HtmlPage page = client.getPage(URL_FIRST);
        final HtmlElement button = page.getHtmlElementById("b");
        final HtmlPage dialogPage = button.click();

        final DialogWindow dialog = (DialogWindow) dialogPage.getEnclosingWindow();
        dialog.close();

        assertEquals(getExpectedAlerts(), actual);
    }

    /**
     * Test for the <tt>showModalDialog</tt> method. This tests blocking
     * until the window gets closed.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({ "undefined", "sdg", "finished" })
    @NotYetImplemented
    public void showModalDialogWithButton() throws Exception {
        final String html1
            = "<html><head>\n"
            + "  <script>\n"
            + "    function test() {\n"
            + "      alert(window.returnValue);\n"
            + "      var res = showModalDialog('myDialog.html', null, 'dialogHeight:300px; dialogLeft:200px;');\n"
            + "      alert(res);\n"
            + "      alert('finished');\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body>\n"
            + "  <button onclick='test()' id='openDlg'>Test</button>\n"
            + "</body></html>";

        final String html2
            = "<html><head>\n"
            + "</head>\n"
            + "<body>\n"
            + "  <button id='closeDlg' onclick='window.returnValue = \"result\"; window.close();'></button>\n"
            + "</body>\n"
            + "</html>";

        final WebClient client = getWebClient();
        final List<String> actual = new ArrayList<String>();
        client.setAlertHandler(new CollectingAlertHandler(actual));

        final MockWebConnection conn = new MockWebConnection();
        conn.setResponse(URL_FIRST, html1);
        conn.setResponse(new URL(URL_FIRST.toExternalForm() + "myDialog.html"), html2);
        client.setWebConnection(conn);

        final HtmlPage page = getWebClient().getPage(URL_FIRST);
        final HtmlElement button = page.getHtmlElementById("openDlg");
        button.click();

        assertEquals(getExpectedAlerts(), actual);
    }

    /**
     * Basic test for the <tt>showModelessDialog</tt> method. See bug 2124916.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "", IE8 = { "[object]", "a" },
            IE11 = { "[object Window]", "a" })
    public void showModelessDialog() throws Exception {
        final String html1
            = "<html><head><script>\n"
            + "  var userName = '';\n"
            + "  function test() {\n"
            + "    if (window.showModelessDialog) {\n"
            + "      var newWindow = showModelessDialog('myDialog.html', window, 'status:false');\n"
            + "      alert(newWindow);\n"
            + "    }\n"
            + "  }\n"
            + "  function update() { alert(userName); }\n"
            + "</script></head><body>\n"
            + "  <input type='button' id='b' value='Test' onclick='test()'>\n"
            + "</body></html>";

        final String html2
            = "<html><head><script>\n"
            + "function update() {\n"
            + "  var w = dialogArguments;\n"
            + "  w.userName = document.getElementById('name').value;\n"
            + "  w.update();\n"
            + "}\n"
            + "</script></head><body>\n"
            + "  Name: <input id='name'><input value='OK' id='b' type='button' onclick='update()'>\n"
            + "</body></html>";

        final WebClient client = getWebClient();
        final List<String> actual = new ArrayList<String>();
        client.setAlertHandler(new CollectingAlertHandler(actual));

        final MockWebConnection conn = new MockWebConnection();
        conn.setResponse(URL_FIRST, html1);
        conn.setResponse(new URL(URL_FIRST.toExternalForm() + "myDialog.html"), html2);
        client.setWebConnection(conn);

        final HtmlPage page = client.getPage(URL_FIRST);
        final HtmlElement button = page.getHtmlElementById("b");
        final HtmlPage dialogPage = button.click();

        if (!dialogPage.getUrl().equals(URL_FIRST)) {
            final HtmlInput input = dialogPage.getHtmlElementById("name");
            input.setValueAttribute("a");

            final HtmlButtonInput button2 = (HtmlButtonInput) dialogPage.getHtmlElementById("b");
            button2.click();

            assertEquals(getExpectedAlerts(), actual);
        }
    }

    /**
     * Regression test for http://sourceforge.net/p/htmlunit/bugs/234/
     * and https://bugzilla.mozilla.org/show_bug.cgi?id=443491.
     * @throws Exception if the test fails
     */
    @Test
    public void overwriteFunctions_alert() throws Exception {
        final String html = "<html><head><script language='JavaScript'>\n"
            + "function alert(x) {\n"
            + "  document.title = x;\n"
            + "}\n"
            + "alert('hello');\n"
            + "</script></head><body></body></html>";

        final HtmlPage page = loadPageWithAlerts(html);
        assertEquals("hello", page.getTitleText());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({ "true", "123", "123" })
    public void overwriteProperty_top() throws Exception {
        final String html
            = "<html><body><script>\n"
            + "  alert(window.top == this);\n"
            + "  var top = 123;\n"
            + "  alert(top);\n"
            + "  alert(window.top);\n"
            + "</script></body></html>";
        // this can't be tested using WebDriver currently (i.e. using loadPageWithAlerts2)
        // because the hack currently used to capture alerts needs reference to property "top".
        loadPageWithAlerts(html);
    }

    /**
     * Download of next page is done first after onbeforeunload is done.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("x")
    public void onbeforeunload_calledBeforeDownload() throws Exception {
        final String html
            = "<html><body><script>\n"
            + "  window.onbeforeunload = function() { alert('x'); return 'hello'; };\n"
            + "  window.location = 'foo.html';\n"
            + "</script></body></html>";

        final WebClient webClient = getWebClientWithMockWebConnection();
        getMockWebConnection().setDefaultResponse("");

        final OnbeforeunloadHandler handler = new OnbeforeunloadHandler() {
            public boolean handleEvent(final Page page, final String returnValue) {
                final String[] expectedRequests = {""};
                assertEquals(expectedRequests, getMockWebConnection().getRequestedUrls(getDefaultUrl()));
                return true;
            }
        };
        webClient.setOnbeforeunloadHandler(handler);
        loadPageWithAlerts(html);

        final String[] expectedRequests = {"", "foo.html"};
        assertEquals(expectedRequests, getMockWebConnection().getRequestedUrls(getDefaultUrl()));
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void serialization() throws Exception {
        final String html = "<html><head></head><body><iframe></iframe><script>window.frames</script></body></html>";
        final HtmlPage page = loadPageWithAlerts(html);
        clone(page.getEnclosingWindow());
    }

    /**
     * Regression test for bug 2808901.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("x")
    public void onbeforeunload_setToFunction() throws Exception {
        final String html
            = "<html><body><script>\n"
            + "  window.onbeforeunload = function() { alert('x'); return 'x'; };\n"
            + "  window.location = 'about:blank';\n"
            + "</script></body></html>";
        loadPageWithAlerts(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = { "10", "20", "30", "40" },
            IE8 = { "undefined", "undefined", "undefined", "undefined" })
    public void mozillaViewportSetters() throws Exception {
        final String html = "<html>\n"
                + "<head></head>\n"
                + "<body>\n"
                + "<script>\n"
                + "alert(window.innerWidth);\n"
                + "alert(window.innerHeight);\n"
                + "alert(window.outerWidth);\n"
                + "alert(window.outerHeight);\n"
                + "</script>\n"
                + "</body>\n"
                + "</html>";

        final List<String> collectedAlerts = new ArrayList<String>();
        final WebClient client = getWebClient();
        client.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        final MockWebConnection webConnection = new MockWebConnection();
        webConnection.setDefaultResponse(html);
        client.setWebConnection(webConnection);

        final WebWindow topLevelWindow = client.getTopLevelWindows().get(0);
        topLevelWindow.setInnerWidth(10);
        topLevelWindow.setInnerHeight(20);
        topLevelWindow.setOuterWidth(30);
        topLevelWindow.setOuterHeight(40);
        client.getPage(URL_FIRST);
        assertEquals(getExpectedAlerts(), collectedAlerts);
    }

}
