/*
 * Copyright (c) 2002-2013 Gargoyle Software Inc.
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

import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.IE;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.base.testing.EventCatcher;
import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.Browsers;
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
     * Regression test to reproduce a known bug.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("about:blank")
    public void openWindow_emptyUrl() throws Exception {
        final String html
            = "<html><head><script>\n"
            + "var w = window.open('');\n"
            + "alert(w ? w.document.location : w);\n"
            + "</script></head>\n"
            + "<body></body></html>";

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
    public void opener() throws Exception {
        final WebClient webClient = getWebClient();
        final MockWebConnection webConnection = new MockWebConnection();
        final List<String> collectedAlerts = new ArrayList<String>();

        webClient.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        final String firstContent
            = "<html><head><title>First</title><script>\n"
            + "function runtest() {\n"
            + "    alert(window.opener)\n"
            + "    alert('one')\n"
            + "    open('" + URL_SECOND + "', 'foo')\n"
            + "}\n"
            + "function callAlert(text) {\n"
            + "    alert(text)\n"
            + "}\n"
            + "</script></head><body onload='runtest()'>\n"
            + "</body></html>";
        final String secondContent
            = "<html><head><title>Second</title><script>\n"
            + "function runtest() {\n"
            + "    opener.callAlert('two')\n"
            + "    document.form1.submit()\n"
            + "}\n"
            + "</script></head><body onload='runtest()'>\n"
            + "<form name='form1' action='" + URL_THIRD + "' method='post'><input type='submit'></form>\n"
            + "</body></html>";
        final String thirdContent
            = "<html><head><title>Third</title><script>\n"
            + "function runtest() {\n"
            + "    opener.callAlert('three')\n"
            + "}\n"
            + "</script></head><body onload='runtest()'>\n"
            + "</body></html>";

        webConnection.setResponse(URL_FIRST, firstContent);
        webConnection.setResponse(URL_SECOND, secondContent);
        webConnection.setResponse(URL_THIRD, thirdContent);
        webClient.setWebConnection(webConnection);

        final HtmlPage firstPage = webClient.getPage(URL_FIRST);
        assertEquals("First", firstPage.getTitleText());

        final String[] expectedAlerts = {"null", "one", "two", "three"};
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("one")
    public void windowFrames() throws Exception {
        final String html =
            "<html><body><script language='JavaScript'>\n"
            + "if (typeof top.frames['anyXXXname'] == 'undefined') {\n"
            + "alert('one');\n"
            + "};\n"
            + "</script></body></html>";

        loadPageWithAlerts(html);
    }

    /**
     * Variables that are defined inside JavaScript should be accessible through the
     * window object (ie window.myVariable). Test that this works.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("foo")
    public void javascriptVariableFromWindow() throws Exception {
        final String html =
            "<html><head><title>first</title></head><body><script>\n"
            + "myVariable = 'foo';\n"
            + "alert(window.myVariable);\n"
            + "</script></body></head>";

        loadPageWithAlerts(html);
    }

    /**
     * Variables that are defined inside JavaScript should be accessible through the
     * window object (ie window.myVariable). Test that this works.
     * @throws Exception if the test fails
     */
    @Test
    public void javascriptVariableFromTopAndParentFrame() throws Exception {
        final WebClient webClient = getWebClient();
        final MockWebConnection webConnection = new MockWebConnection();
        final List<String> collectedAlerts = new ArrayList<String>();

        webClient.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        final String firstContent
            = "<html><head><title>First</title></head><body><script>\n"
            + "myVariable = 'first'"
            + "  </script><iframe name='left' src='" + URL_SECOND + "'></iframe>\n"
            + "</body></html>";
        webConnection.setResponse(URL_FIRST, firstContent);

        final String secondContent
            = "<html><head><title>Second</title></head><body><script>\n"
            + "myVariable = 'second'"
            + "  </script><iframe name='innermost' src='" + URL_THIRD + "'></iframe>\n"
            + "</body></html>";
        webConnection.setResponse(URL_SECOND, secondContent);

        final String thirdContent
            = "<html><head><title>Third</title><script>\n"
            + "myVariable = 'third';\n"
            + "function doTest() {\n"
            + "alert('parent.myVariable = ' + parent.myVariable);\n"
            + "alert('top.myVariable = ' + top.myVariable);\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='doTest()'></body></html>";

        webConnection.setResponse(URL_THIRD, thirdContent);

        webClient.setWebConnection(webConnection);

        final HtmlPage page = webClient.getPage(URL_FIRST);
        assertEquals("First", page.getTitleText());

        final String[] expectedAlerts = {
            "parent.myVariable = second",
            "top.myVariable = first",
        };
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * Variables that are defined inside JavaScript should be accessible through the
     * window object (ie window.myVariable). Test that this works.
     * @throws Exception if the test fails
     */
    @Test
    public void javascriptVariableFromNamedFrame() throws Exception {
        final WebClient webClient = getWebClient();
        final MockWebConnection webConnection = new MockWebConnection();
        final List<String> collectedAlerts = new ArrayList<String>();

        webClient.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        final String firstContent
            = "<html><head><title>first</title></head>\n"
            + "<frameset cols='20%,80%'>\n"
            + "    <frameset rows='30%,70%'>\n"
            + "        <frame src='" + URL_SECOND + "' name='second'>\n"
            + "        <frame src='" + URL_THIRD + "' name='third'>\n"
            + "    </frameset>\n"
            + "    <frame src='http://fourth' name='fourth'>\n"
            + "</frameset></html>";
        webConnection.setResponse(URL_FIRST, firstContent);

        final String secondContent
            = "<html><head><title>second</title></head><body><script>\n"
            + "myVariable = 'second';\n"
            + "</script><p>second</p></body></html>";
        webConnection.setResponse(URL_SECOND, secondContent);

        final String thirdContent
            = "<html><head><title>third</title></head><body><script>\n"
            + "myVariable = 'third';\n"
            + "</script><p>third</p></body></html>";
        webConnection.setResponse(URL_THIRD, thirdContent);

        final String fourthContent
            = "<html><head><title>fourth</title></head><body onload='doTest()'><script>\n"
            + "myVariable = 'fourth';\n"
            + "function doTest() {\n"
            + "alert('parent.second.myVariable = ' + parent.second.myVariable);\n"
            + "alert('parent.third.myVariable = ' + parent.third.myVariable);\n"
            + "}\n"
            + "</script></body></html>";
        webConnection.setResponse(new URL("http://fourth/"), fourthContent);

        webClient.setWebConnection(webConnection);

        final HtmlPage page = webClient.getPage(URL_FIRST);
        assertEquals("first", page.getTitleText());

        final String[] expectedAlerts = {
            "parent.second.myVariable = second",
            "parent.third.myVariable = third",
        };
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * Variables that have not been defined should return null when accessed.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void javascriptVariableFromWindow_NotFound() throws Exception {
        final String html =
            "<html><head><title>first</title></head><body><script>\n"
            + "myVariable = 'foo';\n"
            + "alert(window.myOtherVariable == null);\n"
            + "</script></body></head>";

        loadPageWithAlerts(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void getFrameByName() throws Exception {
        final WebClient webClient = getWebClient();
        final MockWebConnection webConnection = new MockWebConnection();
        final List<String> collectedAlerts = new ArrayList<String>();

        webClient.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        final String firstContent
            = "<html><head><title>first</title></head>\n"
            + "<frameset cols='20%,80%'>\n"
            + "    <frameset rows='30%,70%'>\n"
            + "        <frame src='" + URL_SECOND + "' name='second'>\n"
            + "        <frame src='" + URL_THIRD + "' name='third'>\n"
            + "    </frameset>\n"
            + "    <frame src='http://fourth' name='fourth'>\n"
            + "</frameset></html>";
        webConnection.setResponse(URL_FIRST, firstContent);

        final String secondContent = "<html><head><title>second</title></head><body><p>second</p></body></html>";
        webConnection.setResponse(URL_SECOND, secondContent);

        final String thirdContent = "<html><head><title>third</title></head><body><p>third</p></body></html>";
        webConnection.setResponse(URL_THIRD, thirdContent);

        final String fourthContent
            = "<html><head><title>fourth</title></head><body onload='doTest()'><script>\n"
            + "function doTest() {\n"
            + "alert('fourth-second='+parent.second.document.location);\n"
            + "alert('fourth-third='+parent.third.document.location);\n"
            + "}\n"
            + "</script></body></html>";
        webConnection.setResponse(new URL("http://fourth/"), fourthContent);

        webClient.setWebConnection(webConnection);

        final HtmlPage page = webClient.getPage(URL_FIRST);
        assertEquals("first", page.getTitleText());

        final String[] expectedAlerts = {"fourth-second=" + URL_SECOND, "fourth-third=" + URL_THIRD};
        assertEquals(expectedAlerts, collectedAlerts);
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
     * Test the <tt>window.closed</tt> property.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "false", "false", "true" })
    public void closed() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + "function test() {\n"
            + "  alert(window.closed);\n"
            + "  var newWindow = window.open('about:blank', 'foo');\n"
            + "  alert(newWindow.closed);\n"
            + "  newWindow.close();\n"
            + "  alert(newWindow.closed);\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "</body></html>";

        loadPageWithAlerts(html);
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
     * Test that Window.moveTo method gets correctly called and handled by the scripting engine.
     * @throws Exception if the test fails
     */
    @Test
    public void moveTo() throws Exception {
        final String html
            = "<html><head><title>foo</title><script>\n"
            + "window.moveTo(10, 20)\n"
            + "</script></head><body>\n"
            + "</body></html>";
        loadPageWithAlerts(html);
    }

    /**
     * Test that Window.moveBy method gets correctly called and handled by the scripting engine.
     * @throws Exception if the test fails
     */
    @Test
    public void moveBy() throws Exception {
        final String html
            = "<html><head><title>foo</title><script>\n"
            + "window.moveBy(10, 20)\n"
            + "</script></head><body>\n"
            + "</body></html>";
        loadPageWithAlerts(html);
    }

    /**
     * Tests that the Window.resizeTo method gets correctly called and handled by the scripting engine.
     * @throws Exception if the test fails
     */
    @Test
    public void resizeTo() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "window.resizeTo(10, 20);\n"
            + "window.resizeTo(-10, 20);\n"
            + "</script></head><body></body></html>";
        loadPageWithAlerts(html);
    }

    /**
     * Tests that the Window.resizeBy method gets correctly called and handled by the scripting engine.
     * @throws Exception if the test fails
     */
    @Test
    public void resizeBy() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "window.resizeBy(10, 20);\n"
            + "window.resizeBy(-10, 20);\n"
            + "</script></head><body></body></html>";
        loadPageWithAlerts(html);
    }

    /**
     * Test that Window.scroll method gets correctly called and handled by the scripting engine.
     * @throws Exception if the test fails
     */
    @Test
    public void scroll() throws Exception {
        final String html
            = "<html><head><title>foo</title><script>\n"
            + "window.scroll(10, 20);\n"
            + "</script></head><body>\n"
            + "</body></html>";
        loadPageWithAlerts(html);
    }

    /**
     * Test that Window.scrollBy method gets correctly called and handled by the scripting engine.
     * @throws Exception if the test fails
     */
    @Test
    public void scrollBy() throws Exception {
        final String html
            = "<html><head><title>foo</title><script>\n"
            + "window.scrollBy(10, 20);\n"
            + "</script></head><body>\n"
            + "</body></html>";
        loadPageWithAlerts(html);
    }

    /**
     * Test that Window.scrollByLines method gets correctly called and handled by the scripting engine.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE = "exception")
    public void scrollByLines() throws Exception {
        final String html
            = "<html><head><title>foo</title><script>\n"
            + "try {\n"
            + "  window.scrollByLines(2);\n"
            + "} catch (e) { alert('exception'); }\n"
            + "</script></head><body>\n"
            + "</body></html>";
        loadPageWithAlerts(html);
    }

    /**
     * Test that Window.scrollByPages method gets correctly called and handled by the scripting engine.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE = "exception")
    public void scrollByPages() throws Exception {
        final String html
            = "<html><head><title>foo</title><script>\n"
            + "try {\n"
            + "  window.scrollByPages(2);\n"
            + "} catch (e) { alert('exception'); }\n"
            + "</script></head><body>\n"
            + "</body></html>";
        loadPageWithAlerts(html);
    }

    /**
     * Test that Window.scrollTo method gets correctly called and handled by the scripting engine.
     * @throws Exception if the test fails
     */
    @Test
    public void scrollTo() throws Exception {
        final String html
            = "<html><head><title>foo</title><script>\n"
            + "window.scrollTo(10, 20);\n"
            + "</script></head><body>\n"
            + "</body></html>";
        loadPageWithAlerts(html);
    }

     /**
     * All elements should be accessible via the window object by their name, both in qualified
     * format (<tt>window.elementName</tt>) and unqualified format (<tt>elementName</tt>), if we
     * are emulating Internet Explorer. Both of these expressions are therefore equivalent
     * to <tt>document.getElementsByName(elementName)</tt> in IE, and should return a collection of
     * elements if there is more than one with the specified name.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "form1", "form2", "2", "4" })
    public void elementByNameFromWindow() throws Exception {
        final String html = "<html>\n"
            + "<head><title>test</title>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    alert(window.form1.name);\n"
            + "    alert(form2.name);\n"
            + "    alert(window.input1.length);\n"
            + "    alert(input2[1].value);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "<form name='form1'></form>\n"
            + "<form name='form2'></form>\n"
            + "<input type='text' name='input1' value='1'/>\n"
            + "<input type='text' name='input1' value='2'/>\n"
            + "<input type='text' name='input2' value='3'/>\n"
            + "<input type='text' name='input2' value='4'/>\n"
            + "</body>\n"
            + "</html>";

        loadPageWithAlerts(html);
    }

    /**
     * All elements should be accessible via the window object by their id.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "form1", "form2", "1", "DIV" })
    public void elementByIdFromWindow() throws Exception {
        final String html = "<html>\n"
            + "<head><title>test</title>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    alert(window.form1Id.name);\n"
            + "    alert(form2Id.name);\n"
            + "    alert(window.input1Id.value);\n"
            + "    alert(myDiv.tagName);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "<div id='myDiv'>\n"
            + "<form name='form1' id='form1Id'></form>\n"
            + "</div>\n"
            + "<form name='form2' id='form2Id'></form>\n"
            + "<input type='text' name='input1' id='input1Id' value='1'/>\n"
            + "</form>\n"
            + "</body>\n"
            + "</html>";

        loadPageWithAlerts(html);
    }

    /**
     * In FF 2 "foo" evaluates to the node with id or name "foo" when no local variable
     * has this name.
     * BUT strangely window.foo evaluates to undefined (not yet implemented in HTMLUnit.)
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "form1", "form2", "DIV" })
    public void ff_ElementByIdOrNameFromWindow() throws Exception {
        final String html = "<html>\n"
            + "<head><title>test</title>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    alert(form1Id.name);\n"
            + "    alert(form2Id.name);\n"
            + "    alert(myDiv.tagName);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "<div id='myDiv'>\n"
            + "<form name='form1' id='form1Id'></form>\n"
            + "</div>\n"
            + "<form name='form2' id='form2Id'></form>\n"
            + "<input type='text' name='input1' id='input1Id' value='1'/>\n"
            + "</form>\n"
            + "</body>\n"
            + "</html>";

        loadPageWithAlerts(html);
    }

    /**
     * Test that Window.execScript method gets called correctly.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(FF = "exception", IE = { "JavaScript", "JScript", "Invalid class string" })
    public void execScript() throws Exception {
        final String html = "<html>\n"
            + "<head><title>test</title>\n"
            + "<script>\n"
            + "  function test()\n"
            + "  {\n"
            + "    try {\n"
            + "      window.execScript('alert(\"JavaScript\")', 'JavaScript');\n"
            + "      window.execScript('alert(\"JScript\")',    'JScript');\n"
            + "      window.execScript('alert(\"VBScript\")',   'VBScript');\n"
            + "      try {\n"
            + "        window.execScript('alert(\"BadLanguage\")', 'BadLanguage');\n"
            + "      }\n"
            + "      catch (e) {\n"
            + "        alert(e.message.substr(0, 20)); // msg now contains info on error location\n"
            + "      }\n"
            + "    } catch (e) { alert('exception'); }\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <div id='div1'>blah</div>\n"
            + "</body>\n"
            + "</html>";

        loadPageWithAlerts(html);
    }

   /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "test2", "test" })
    public void onLoadFunction() throws Exception {
        final String html = "<html>\n"
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
            + "</html>";

        loadPageWithAlerts(html);
    }

    /**
     * Verifies that you can set window.onload to something else than a function.
     * See bug 1708532 & 1201561.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({ "a", "undefined" })
    public void onloadNotAFunction() throws Exception {
        final String html = "<html><body><script>\n"
            + "window.onload = new function() {alert('a')};\n"
            + "window.onload = undefined;\n"
            + "alert(window.onload);\n"
            + "</script></body></html>";

        loadPageWithAlerts(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "false", "false", "test1", "test2", "onload" },
            IE = { "true", "true", "exception", "onload" })
    public void addOnLoadEventListener() throws Exception {
        final String html = "<html>\n"
            + "<head><title>test</title>\n"
            + "<script>\n"
            + "  function test1() {alert('test1');}\n"
            + "  function test2() {alert('test2');}\n"
            + "  function test3() {alert('test3');}\n"
            + "alert(window.addEventListener == null);\n"
            + "alert(window.removeEventListener == null);\n"
            + "try {\n"
            + "  window.addEventListener('load', test1, true);\n"
            + "  window.addEventListener('load', test1, true);\n"
            + "  window.addEventListener('load', test2, true);\n"
            + "  window.addEventListener('load', test3, true);\n"
            + "  window.removeEventListener('load', test3, true);\n"
            + "} catch (e) { alert('exception'); }\n"
            + "</script></head>\n"
            + "<body onload='alert(\"onload\")'></body></html>";

        loadPageWithAlerts(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(FF = { "true", "true", "exception", "onload" },
            IE = { "false", "false", "onload", "test1, param null: false", "test2" })
    public void attachOnLoadEvent() throws Exception {
        final String html = "<html>\n"
            + "<head><title>test</title>\n"
            + "<script>\n"
            + "  function test1(_e) {alert('test1, param null: ' + (_e == null));}\n"
            + "  function test2() {alert('test2');}\n"
            + "  function test3() {alert('test3');}\n"
            + "alert(window.attachEvent == null);\n"
            + "alert(window.detachEvent == null);\n"
            + "try {\n"
            + "  window.attachEvent('onload', test1);\n"
            + "  window.attachEvent('onload', test1);\n"
            + "  window.attachEvent('onload', test2);\n"
            + "  window.attachEvent('onload', test3);\n"
            + "  window.detachEvent('onload', test3);\n"
            + "} catch (e) { alert('exception'); }\n"
            + "</script></head>\n"
            + "<body onload='alert(\"onload\")'></body></html>";

        loadPageWithAlerts(html);
    }

    /**
     * Regression test for bug 1596926.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(FF = "exception", IE = "detached")
    public void detachEventInAttachEvent() throws Exception {
        final String html = "<html>\n"
            + "<head><title>test</title>\n"
            + "<script>\n"
            + "function test() {\n"
            + "  window.detachEvent('onload', test);\n"
            + "  alert('detached');\n"
            + "}\n"
            + "try {\n"
            + "  window.attachEvent('onload', test);\n"
            + "} catch (e) { alert('exception'); }\n"
            + "</script></head>\n"
            + "<body></body></html>";

        loadPageWithAlerts(html);
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
     * Test <code>window.name</code>.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "window.name before: ", "window.name after: main" })
    public void windowName() throws Exception {
        final String html = "<html>\n"
            + "<head><title>window.name test</title></head>\n"
            + "<body>\n"
            + "<script>\n"
            + "alert('window.name before: ' + window.name);\n"
            + "window.name = 'main';\n"
            + "alert('window.name after: ' + window.name);\n"
            + "</script>\n"
            + "</body>\n"
            + "</html>";

        loadPageWithAlerts(html);
    }

    /**
     * Tests Mozilla viewport properties.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(FF = { "number", "number", "number", "number" },
            IE = { "undefined", "undefined", "undefined", "undefined" })
    public void mozillaViewport() throws Exception {
        final String html = "<html>\n"
            + "<head></head>\n"
            + "<body>\n"
            + "<script>\n"
            + "alert(typeof window.innerWidth);\n"
            + "alert(typeof window.innerHeight);\n"
            + "alert(typeof window.outerWidth);\n"
            + "alert(typeof window.outerHeight);\n"
            + "</script>\n"
            + "</body>\n"
            + "</html>";

        loadPageWithAlerts(html);
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
     * Test the <tt>Referer</tt> HTTP header by <tt>window.open</tt>.
     * @throws Exception if the test fails
     */
    @Test
    public void openWindow_refererHeader() throws Exception {
        final String expectedRefererHeader = getBrowserVersion().isFirefox() ? URL_FIRST.toString() : null;
        final String firstContent = "<html><head></head>\n"
            + "<body>\n"
            + "<button id='clickme' onClick='window.open(\"" + URL_SECOND + "\");'>Click me</a>\n"
            + "</body></html>";

        final String secondContent
            = "<html><head><title>Second</title></head><body></body></html>";

        final WebClient client = getWebClient();
        final MockWebConnection conn = new MockWebConnection();
        client.setWebConnection(conn);

        conn.setResponse(URL_FIRST, firstContent);
        conn.setResponse(URL_SECOND, secondContent);

        final HtmlPage firstPage = client.getPage(URL_FIRST);
        final HtmlButton buttonA = firstPage.getHtmlElementById("clickme");

        buttonA.click();
        final Map<String, String> lastAdditionalHeaders = conn.getLastAdditionalHeaders();
        assertEquals(expectedRefererHeader, lastAdditionalHeaders.get("Referer"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("1")
    public void evalScopeOtherWindow() throws Exception {
        final String html = "<html><body>\n"
            + "<iframe src='iframe.html'></iframe>\n"
            + "</body></html>";
        final String iframe = "<html><body>\n"
            + "<script>\n"
            + "window.parent.eval('var foo = 1');\n"
            + "alert(window.parent.foo)\n"
            + "</script>\n"
            + "</body></html>";

        getMockWebConnection().setDefaultResponse(iframe);
        loadPageWithAlerts(html);
    }

    /**
     * Regression test for [ 1608555 ] JavaScript: window.eval does evaluate local scope.
     * See https://sourceforge.net/tracker/index.php?func=detail&aid=1608555&group_id=47038&atid=448266.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "elementValue", "elementValue", "elementValue" })
    public void evalScopeLocal() throws Exception {
        final String html = "<html><body><form id='formtest'><input id='element' value='elementValue'/></form>\n"
            + "<script>\n"
            + "var docPatate = 'patate';\n"
            + "function test() {\n"
            + " var f = document.forms['formtest'];\n"
            + " alert(eval(\"document.forms['formtest'].element.value\"));\n"
            + " alert(f.element.value);\n"
            + " alert(eval('f.element.value'));\n"
            + "}\n"
            + "test();\n"
            + "</script>\n"
            + "</body></html>";

        loadPageWithAlerts(html);
    }

    /**
     * Verifies that eval() works correctly when triggered from an event handler. Event handlers are
     * executed in a child scope of the global window scope, so variables set from inside eval()
     * should go to this child scope, and not to the window scope.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("string")
    public void evalScopeEvent() throws Exception {
        final String html = "<html><body onload='test()'><script>\n"
            + "   function test() {\n"
            + "      var s = 'string';\n"
            + "      var f = 'initial';\n"
            + "      eval('f = function(){alert(s);}');\n"
            + "      invoke(f);\n"
            + "   };\n"
            + "   function invoke(fn) {\n"
            + "      fn();\n"
            + "   }\n"
            + "</script></body></html>";

        loadPageWithAlerts(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void functionEquality() throws Exception {
        final String html = "<html><body>\n"
            + "<script>\n"
            + "alert(window.focus == window.focus)\n"
            + "</script>\n"
            + "</body></html>";

        loadPageWithAlerts(html);
    }

    /**
     * Test for 1225021.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(FF = { "123", "captured" }, IE = { "exception", "123", "captured" })
    public void captureEvents() throws Exception {
        final String content = "<html><head><title>foo</title>\n"
            + "<script>\n"
            + "function t() { alert('captured'); }\n"
            + "try {\n"
            + "window.captureEvents(Event.CLICK);\n"
            + "} catch (e) { alert('exception'); }\n"
            + "window.onclick = t;\n"
            + "</script></head><body>\n"
            + "<div id='theDiv' onclick='alert(123)'>foo</div>\n"
            + "</body></html>";

        final List<String> collectedAlerts = new ArrayList<String>();
        final HtmlPage page = loadPage(getBrowserVersion(), content, collectedAlerts);
        page.getHtmlElementById("theDiv").click();

        assertEquals(getExpectedAlerts(), collectedAlerts);
    }

    /**
     * Verifies that the onload handler is executed with "this" referring to the window.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("true")
    public void onLoadContext() throws Exception {
        final String html = "<html><body><script>\n"
            + "var x = function() { alert(this==window) };\n"
            + "window.onload = x;\n"
            + "</script></body></html>";

        loadPageWithAlerts(html);
    }

    /**
     * Added test for [ 1727599 ] Bad context in evaluation of the JavaScript.
     * @throws Exception if the test fails
     */
    @Test
    public void eval() throws Exception {
        final String content = "<html><body>\n"
            + "<input type='button' id='myButton' value='Click Me' onclick='test(this)'>\n"
            + "<script>\n"
            + "function test(f) {\n"
            + "   alert(eval('f.tagName'));\n"
            + "}\n"
            + "</script>\n"
            + "</body></html>";

        final String[] expectedAlerts = {"INPUT"};

        final List<String> collectedAlerts = new ArrayList<String>();
        final HtmlPage page = loadPage(content, collectedAlerts);
        page.getHtmlElementById("myButton").click();
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "undefined", "undefined", "true" })
    public void undefinedProperty() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    alert(window['something']);\n"
            + "    alert(typeof window['something']);\n"
            + "    alert(typeof window['something']=='undefined');\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageWithAlerts(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void frames() throws Exception {
        final String html =
            "<html><head><title>First</title></head>\n"
            + "<frameset id='fs' rows='20%,*'>\n"
            + "    <frame name='top' src='" + URL_SECOND + "' />\n"
            + "    <frame name='bottom' src='about:blank' />\n"
            + "</frameset>\n"
            + "</html>";

        final String frameContent =
            "<html><head><title>TopFrame</title>\n"
            + "<script>\n"
            + "function doTest() {\n"
            + "    var bottomFrame = window.top.frames['bottom'];\n"
            + "    bottomFrame.location = 'about:blank';\n"
            + "}</script>\n"
            + "</head>\n"
            + "<body onload='doTest()'></body></html>";

        getMockWebConnection().setResponse(URL_SECOND, frameContent);

        loadPageWithAlerts(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void openWindow_numericName() throws Exception {
        final String html
            = "<html><head><script>\n"
            + "function test() {\n"
            + "  var w1 = window.open('about:blank', 1);\n"
            + "  alert(w1 != null);\n"
            + "}\n"
            + "</script></head><body onload='test()'>\n"
            + "<iframe name='myFrame' id='myFrame'></iframe>\n"
            + "</body></html>";

        loadPageWithAlerts(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE = "error", FF = "true")
    public void stop() throws Exception {
        final String html
            = "<html><head><script>\n"
            + "function test() {\n"
            + "  try {\n"
            + "    window.stop();\n"
            + "    alert(true);\n"
            + "  } catch (e) {\n"
            + "    alert('error');\n"
            + "  }\n"
            + "}\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageWithAlerts(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void open() throws Exception {
        final String firstHtml = "<html><head></head>\n"
            + "<body>\n"
            + "<button id='clickme' onClick='window.open(new String(\"" + URL_SECOND + "\"));'>Click me</a>\n"
            + "</body></html>";

        final String secondHtml
            = "<html><head><title>Second</title></head><body></body></html>";

        final WebClient client = getWebClient();
        final MockWebConnection conn = new MockWebConnection();
        client.setWebConnection(conn);

        conn.setResponse(URL_FIRST, firstHtml);
        conn.setResponse(URL_SECOND, secondHtml);

        final HtmlPage page = client.getPage(URL_FIRST);
        final HtmlButton buttonA = page.getHtmlElementById("clickme");

        buttonA.click();
    }

    /**
     * Basic test for the <tt>showModalDialog</tt> method. See bug 2124916.
     * @throws Exception if an error occurs
     */
    @Test
    public void showModalDialog() throws Exception {
        final String html1
            = "<html><head><script>\n"
            + "  function test() {\n"
            + "    alert(window.returnValue);\n"
            + "    var o = new Object();\n"
            + "    o.firstName = f.elements.firstName.value;\n"
            + "    o.lastName = f.elements.lastName.value;\n"
            + "    var ret = showModalDialog('myDialog.html', o, 'dialogHeight:300px; dialogLeft:200px;');\n"
            + "    alert(ret);\n"
            + "    alert('finished');\n"
            + "  }\n"
            + "</script></head><body>\n"
            + "  <button onclick='test()' id='b'>Test</button>\n"
            + "  <form id='f'>\n"
            + "    First Name: <input type='text' name='firstName' value='Jane'><br>\n"
            + "    Last Name: <input type='text' name='lastName' value='Smith'>\n"
            + "  </form>\n"
            + "</body></html>";

        final String html2
            = "<html><head><script>\n"
            + "  var o = window.dialogArguments;\n"
            + "  alert(o.firstName);\n"
            + "  alert(o.lastName);\n"
            + "  window.returnValue = 'sdg';\n"
            + "</script></head>\n"
            + "<body>foo</body></html>";

        final String[] expected = {"undefined", "Jane", "Smith", "sdg", "finished"};

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
        assertEquals(expected, actual);
    }

    /**
     * Basic test for the <tt>showModelessDialog</tt> method. See bug 2124916.
     * @throws Exception if an error occurs
     */
    @Test
    @Browsers(IE)
    public void showModelessDialog() throws Exception {
        final String html1
            = "<html><head><script>\n"
            + "  var userName = '';\n"
            + "  function test() {\n"
            + "    var newWindow = showModelessDialog('myDialog.html', window, 'status:false');\n"
            + "    alert(newWindow);\n"
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

        final String[] expected = {"[object]", "a"};

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

        final HtmlInput input = dialogPage.getHtmlElementById("name");
        input.setValueAttribute("a");

        final HtmlButtonInput button2 = (HtmlButtonInput) dialogPage.getHtmlElementById("b");
        button2.click();

        assertEquals(expected, actual);
    }

    /**
     * Regression test for https://sf.net/tracker/index.php?func=detail&aid=1153708&group_id=47038&atid=448266
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
    @Alerts(FF = { "10", "20", "30", "40" },
        IE = { "undefined", "undefined", "undefined", "undefined" })
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
