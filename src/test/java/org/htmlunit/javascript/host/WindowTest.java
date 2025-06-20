/*
 * Copyright (c) 2002-2025 Gargoyle Software Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.htmlunit.javascript.host;

import static org.junit.jupiter.api.Assertions.fail;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.htmlunit.CollectingAlertHandler;
import org.htmlunit.ConfirmHandler;
import org.htmlunit.DialogWindow;
import org.htmlunit.MockWebConnection;
import org.htmlunit.OnbeforeunloadHandler;
import org.htmlunit.Page;
import org.htmlunit.PrintHandler;
import org.htmlunit.SimpleWebTestCase;
import org.htmlunit.StatusHandler;
import org.htmlunit.WebClient;
import org.htmlunit.WebConsole;
import org.htmlunit.WebConsole.Logger;
import org.htmlunit.WebWindow;
import org.htmlunit.WebWindowEvent;
import org.htmlunit.WebWindowListener;
import org.htmlunit.WebWindowNotFoundException;
import org.htmlunit.html.HtmlAnchor;
import org.htmlunit.html.HtmlButton;
import org.htmlunit.html.HtmlButtonInput;
import org.htmlunit.html.HtmlElement;
import org.htmlunit.html.HtmlInlineFrame;
import org.htmlunit.html.HtmlInput;
import org.htmlunit.html.HtmlPage;
import org.htmlunit.junit.annotation.Alerts;
import org.htmlunit.junit.annotation.HtmlUnitNYI;
import org.htmlunit.util.MimeType;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link Window}.
 *
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
 * @author Ronald Brill
 */
public class WindowTest extends SimpleWebTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void openWindow() throws Exception {
        final WebClient webClient = getWebClient();
        final MockWebConnection webConnection = new MockWebConnection();

        final List<String> collectedAlerts = new ArrayList<>();
        webClient.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        final String firstContent = DOCTYPE_HTML
            + "<html><head><title>First</title></head><body>\n"
            + "<form name='form1'>\n"
            + "  <a id='link' onClick='window.open(\"" + URL_SECOND + "\", \"MyNewWindow\").focus(); "
            + "return false;'>Click me</a>\n"
            + "</form>\n"
            + "</body></html>";
        final String secondContent = DOCTYPE_HTML
            + "<html><head><title>Second</title></head><body>\n"
            + "<script>alert(self.name)</script>\n"
            + "</body></html>";

        final List<WebWindowEvent> events = new LinkedList<>();
        webClient.addWebWindowListener(new WebWindowListener() {
            @Override
            public void webWindowOpened(final WebWindowEvent event) {
                events.add(event);
            }

            @Override
            public void webWindowContentChanged(final WebWindowEvent event) {
                events.add(event);
            }

            @Override
            public void webWindowClosed(final WebWindowEvent event) {
                events.add(event);
            }
        });

        webConnection.setResponse(URL_FIRST, firstContent);
        webConnection.setResponse(URL_SECOND, secondContent);
        webClient.setWebConnection(webConnection);

        final HtmlPage firstPage = webClient.getPage(URL_FIRST);
        assertEquals("First", firstPage.getTitleText());

        final HtmlAnchor anchor = firstPage.getHtmlElementById("link");
        final HtmlPage secondPage = anchor.click();
        assertNotSame(firstPage, secondPage);

        // Expecting contentChanged, opened, contentChanged
        assertEquals(3, events.size());

        final WebWindow firstWebWindow = (WebWindow) events.get(0).getSource();
        final WebWindow secondWebWindow = (WebWindow) events.get(2).getSource();
        assertSame(webClient.getCurrentWindow(), secondWebWindow);
        assertEquals("MyNewWindow", secondWebWindow.getName());

        assertEquals("First", ((HtmlPage) firstWebWindow.getEnclosedPage()).getTitleText());
        assertEquals("Second", ((HtmlPage) secondWebWindow.getEnclosedPage()).getTitleText());

        final WebWindowEvent changedEvent = events.get(2);
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

        final List<String> collectedAlerts = new ArrayList<>();
        webClient.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        final String firstContent = DOCTYPE_HTML
            + "<html><head><title>First</title><base target='MyNewWindow'></head><body>\n"
            + "<form name='form1'>\n"
            + "  <a id='link' href='" + URL_SECOND + "'>Click me</a>\n"
            + "</form>\n"
            + "</body></html>";
        final String secondContent = DOCTYPE_HTML
            + "<html><head><title>Second</title></head><body>\n"
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

        final List<String> collectedAlerts = new ArrayList<>();
        webClient.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        final String firstContent = DOCTYPE_HTML
            + "<html><head><title>First</title></head><body>\n"
            + "  <iframe name='secondFrame' id='secondFrame' src='" + URL_SECOND + "'></iframe>\n"
            + "</body></html>";
        final String secondContent = DOCTYPE_HTML
            + "<html><head><title>Second</title></head><body>\n"
            + "  <a id='link' "
            + "onClick='window.open(\"" + URL_THIRD + "\", \"_blank\").focus(); '>\n"
            + "Click me</a>\n"
            + "</body></html>";
        final String thirdContent = DOCTYPE_HTML
            + "<html><head><title>Third</title></head><body>\n"
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
        catch (final WebWindowNotFoundException e) {
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
        catch (final WebWindowNotFoundException e) {
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

        final String firstContent = DOCTYPE_HTML
            + "<html><head><title>First</title></head><body>\n"
            + "<form name='form1'>\n"
            + "  <a id='link' onClick='window.open(\"" + URL_SECOND + "\", \"_self\"); "
            + "return false;'>Click me</a>\n"
            + "</form>\n"
            + "</body></html>";
        final String secondContent = DOCTYPE_HTML
                + "<html><head><title>Second</title></head><body></body></html>";

        webConnection.setResponse(URL_FIRST, firstContent);
        webConnection.setResponse(URL_SECOND, secondContent);
        webClient.setWebConnection(webConnection);

        final HtmlPage firstPage = webClient.getPage(URL_FIRST);
        assertEquals("First", firstPage.getTitleText());

        final List<WebWindowEvent> events = new LinkedList<>();
        webClient.addWebWindowListener(new WebWindowListener() {
            @Override
            public void webWindowOpened(final WebWindowEvent event) {
                events.add(event);
            }

            @Override
            public void webWindowContentChanged(final WebWindowEvent event) {
                events.add(event);
            }

            @Override
            public void webWindowClosed(final WebWindowEvent event) {
                events.add(event);
            }
        });

        final WebWindow firstWebWindow = firstPage.getEnclosingWindow();

        final HtmlAnchor anchor = firstPage.getHtmlElementById("link");
        final HtmlPage secondPage = anchor.click();
        assertEquals("First", firstPage.getTitleText());
        assertEquals("Second", secondPage.getTitleText());

        assertEquals(1, events.size());

        final WebWindow secondWebWindow = (WebWindow) events.get(0).getSource();
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

        final String firstContent = DOCTYPE_HTML
            + "<html><head><title>First</title></head><body>\n"
            + "  <iframe name='secondFrame' id='secondFrame' src='" + URL_SECOND + "'></iframe>\n"
            + "</body></html>";
        final String secondContent = DOCTYPE_HTML
            + "<html><head><title>Second</title></head><body>\n"
            + "  <iframe name='thirdFrame' id='thirdFrame' src='" + URL_THIRD + "'></iframe>\n"
            + "</body></html>";
        final String thirdContent = DOCTYPE_HTML
            + "<html><head><title>Third</title></head><body>\n"
            + "  <a id='link' onClick='window.open(\"http://fourth\", \"_top\"); "
            + "return false;'>Click me</a>\n"
            + "</body></html>";
        final String fourthContent = DOCTYPE_HTML
            + "<html><head><title>Fourth</title></head><body></body></html>";

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
        catch (final WebWindowNotFoundException e) {
            // Expected path
        }
        try {
            webClient.getWebWindowByName("thirdFrame");
            fail("Did not expect thirdFrame to still exist after click.");
        }
        catch (final WebWindowNotFoundException e) {
            // Expected path
        }
    }

    /**
     * {@code _parent} is a magic name. If we call open(url, '_parent') then the parent window must be reloaded.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void openWindow_parent() throws Exception {
        final WebClient webClient = getWebClient();
        final MockWebConnection webConnection = new MockWebConnection();

        final String firstContent = DOCTYPE_HTML
            + "<html><head><title>First</title></head><body>\n"
            + "  <iframe name='secondFrame' id='secondFrame' src='" + URL_SECOND + "'></iframe>\n"
            + "</body></html>";
        final String secondContent = DOCTYPE_HTML
            + "<html><head><title>Second</title></head><body>\n"
            + "  <iframe name='thirdFrame' id='thirdFrame' src='" + URL_THIRD + "'></iframe>\n"
            + "</body></html>";
        final String thirdContent = DOCTYPE_HTML
            + "<html><head><title>Third</title></head><body>\n"
            + "  <a id='link' onClick='window.open(\"http://fourth\", \"_parent\"); "
            + "return false;'>Click me</a>\n"
            + "</body></html>";
        final String fourthContent = DOCTYPE_HTML
            + "<html><head><title>Fourth</title></head><body></body></html>";

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
        catch (final WebWindowNotFoundException e) {
            fail("Expected secondFrame would be found after click.");
        }
        try {
            webClient.getWebWindowByName("thirdFrame");
            fail("Did not expect thirdFrame to still exist after click.");
        }
        catch (final WebWindowNotFoundException e) {
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
    @Alerts({"true", "true", "true"})
    public void openWindow_existingWindow() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
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
        final String html = DOCTYPE_HTML
            + "<html>\n"
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

        final List<String> actual = new ArrayList<>();
        final WebClient client = getWebClient();
        client.getOptions().setPopupBlockerEnabled(true);
        client.setAlertHandler(new CollectingAlertHandler(actual));

        final MockWebConnection webConnection = new MockWebConnection();
        webConnection.setDefaultResponse(html);
        client.setWebConnection(webConnection);

        final HtmlPage page = client.getPage("http://foo");
        page.getHtmlElementById("d").click();
        final String[] expected = {"null"};
        assertEquals(expected, actual);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void alert_NoAlertHandler() throws Exception {
        final String firstContent = DOCTYPE_HTML
            + "<html><head><title>First</title><script>function doTest() {alert('foo')}</script></head>\n"
            + "<body onload='doTest()'></body></html>";

        final HtmlPage firstPage = loadPage(firstContent);
        assertEquals("First", firstPage.getTitleText());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void parentAndTop() throws Exception {
        final String firstContent = DOCTYPE_HTML
            + "<html><head><title>First</title></head><body>\n"
            + "  <iframe name='left' src='" + URL_SECOND + "'></iframe>\n"
            + "</body></html>";
        final String secondContent = DOCTYPE_HTML
            + "<html><head><title>Second</title></head><body>\n"
            + "  <iframe name='innermost' src='" + URL_THIRD + "'></iframe>\n"
            + "</body></html>";
        final String thirdContent = DOCTYPE_HTML
            + "<html><head><title>Third</title><script>\n"
            + "function doAlert() {\n"
            + "  alert(parent != this);\n"
            + "  alert(top != this);\n"
            + "  alert(parent != top);\n"
            + "  alert(parent.parent == top);\n"
            + "  alert(parent.frames[0] == this);\n"
            + "  alert(top.frames[0] == parent);\n"
            + "}\n"
            + "</script></head>\n"
            + "<body><a id='clickme' onClick='doAlert()'>foo</a></body></html>";

        final WebClient webClient = getWebClient();
        final List<String> collectedAlerts = new ArrayList<>();
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
        final List<String> collectedAlerts = new ArrayList<>();
        final List<String> collectedConfirms = new ArrayList<>();

        webClient.setAlertHandler(new CollectingAlertHandler(collectedAlerts));
        webClient.setConfirmHandler(new ConfirmHandler() {
            @Override
            public boolean handleConfirm(final Page page, final String message) {
                collectedConfirms.add(message);
                return true;
            }
        });

        final String firstContent
            = "<html><head><title>First</title><script>function doTest() {alert(confirm('foo'))}</script>\n"
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
        final String html = DOCTYPE_HTML
            + "<html><head><title>First</title><script>function doTest() {alert(confirm('foo'))}</script>\n"
            + "</head><body onload='doTest()'></body></html>";

        final List<String> collectedAlerts = new ArrayList<>();
        loadPage(html, collectedAlerts);

        assertEquals(new String[] {"true"}, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void prompt() throws Exception {
        try (WebClient webClient = getWebClient()) {
            try (MockWebConnection webConnection = new MockWebConnection()) {
                final List<String> collectedAlerts = new ArrayList<>();
                final List<String> collectedPrompts = new ArrayList<>();

                webClient.setAlertHandler(new CollectingAlertHandler(collectedAlerts));
                webClient.setPromptHandler((page, message, defaultValue) -> {
                    collectedPrompts.add(message);
                    return "Flintstone";
                });

                final String html = DOCTYPE_HTML
                        + "<html><head><title>First</title>\n"
                        + "<script>function doTest() {alert(prompt('foo'))}</script>\n"
                        + "</head><body onload='doTest()'></body></html>";

                webConnection.setResponse(URL_FIRST, html);
                webClient.setWebConnection(webConnection);

                final HtmlPage firstPage = webClient.getPage(URL_FIRST);
                assertEquals("First", firstPage.getTitleText());

                assertEquals(new String[] {"foo"}, collectedPrompts);
                assertEquals(new String[] {"Flintstone"}, collectedAlerts);
            }
        }
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void promptWithDefault() throws Exception {
        try (WebClient webClient = getWebClient()) {
            try (MockWebConnection webConnection = new MockWebConnection()) {
                final List<String> collectedAlerts = new ArrayList<>();
                final List<String> collectedPrompts = new ArrayList<>();

                webClient.setAlertHandler(new CollectingAlertHandler(collectedAlerts));
                webClient.setPromptHandler((page, message, defaultValue) -> {
                    collectedPrompts.add(message);
                    collectedPrompts.add(defaultValue);
                    return defaultValue;
                });

                final String html = DOCTYPE_HTML
                        + "<html><head><title>First</title>\n"
                        + "<script>function doTest() {alert(prompt('foo', 'some default'))}</script>\n"
                        + "</head><body onload='doTest()'></body></html>";

                webConnection.setResponse(URL_FIRST, html);
                webClient.setWebConnection(webConnection);

                final HtmlPage firstPage = webClient.getPage(URL_FIRST);
                assertEquals("First", firstPage.getTitleText());

                assertEquals(new String[] {"foo", "some default"}, collectedPrompts);
                assertEquals(new String[] {"some default"}, collectedAlerts);
            }
        }
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void prompt_noPromptHandler() throws Exception {
        final WebClient webClient = getWebClient();
        final MockWebConnection webConnection = new MockWebConnection();
        final List<String> collectedAlerts = new ArrayList<>();
        final List<String> collectedPrompts = new ArrayList<>();

        webClient.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        final String firstContent = DOCTYPE_HTML
            + "<html><head><title>First</title><script>function doTest() {alert(prompt('foo'))}</script>\n"
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

        final String aContent = DOCTYPE_HTML
            + "<html><head><title>A</title></head><body>\n"
            + "<button id='clickme' onClick='window.open(\"b/b.html\");'>Click me</a>\n"
            + "</body></html>";
        final String bContent = DOCTYPE_HTML
            + "<html><head><title>B</title></head><body>\n"
            + "<button id='clickme' onClick='opener.location.href=\"../c.html\";'>Click me</a>\n"
            + "</body></html>";
        final String cContent = DOCTYPE_HTML
            + "<html><head><title>C</title></head><body></body></html>";
        final String failContent = DOCTYPE_HTML
            + "<html><head><title>FAILURE!!!</title></head><body></body></html>";

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

        final List<String> collectedAlerts = new ArrayList<>();
        webClient.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        final String firstContent = DOCTYPE_HTML
            + "<html><head><title>First</title></head><body>\n"
            + "<a href='" + URL_SECOND + "' id='link' target='_blank'>Link</a>\n"
            + "</body></html>";
        final String secondContent = DOCTYPE_HTML
            + "<html><head><title>Second</title></head><body>\n"
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

        final HtmlPage secondPage = firstPage.getHtmlElementById("link").click();
        assertEquals("Second", secondPage.getTitleText());
        assertEquals(2, webClient.getWebWindows().size());
        final WebWindow secondWindow = secondPage.getEnclosingWindow();

        assertNotSame(firstWindow, secondWindow);

        final List<WebWindowEvent> events = new LinkedList<>();
        webClient.addWebWindowListener(new WebWindowListener() {
            @Override
            public void webWindowOpened(final WebWindowEvent event) {
                events.add(event);
            }

            @Override
            public void webWindowContentChanged(final WebWindowEvent event) {
                events.add(event);
            }

            @Override
            public void webWindowClosed(final WebWindowEvent event) {
                events.add(event);
            }
        });

        secondPage.getHtmlElementById("button").click();

        final List<WebWindowEvent> expectedEvents = Arrays.asList(new WebWindowEvent[]{
            new WebWindowEvent(secondWindow, WebWindowEvent.CLOSE, secondPage, null)
        });
        assertEquals(expectedEvents, events);

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

        final String firstContent = DOCTYPE_HTML
            +  "<html><head><title>First</title><script>\n"
            + "function doTest() {\n"
            + "  alert(window.status);\n"
            + "  window.status = 'newStatus';\n"
            + "  alert(window.status);\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "</body></html>";

        final URL url = URL_FIRST;
        webConnection.setResponse(url, firstContent);
        webClient.setWebConnection(webConnection);

        final List<String> collectedAlerts = new ArrayList<>();
        webClient.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        final List<String> collectedStatus = new ArrayList<>();
        webClient.setStatusHandler(new StatusHandler() {
            @Override
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
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head></head>\n"
            + "<body>\n"
            + "<script>\n"
            + "  window.print();\n"
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

        final String firstContent = DOCTYPE_HTML
            + "<html><head><title>First</title></head><body>\n"
            + "<form name='form1'>\n"
            + "  <a id='link' onClick='window.open(\"" + URL_SECOND + "\", \"_blank\").focus(); return false;'"
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

        webConnection.setResponse(URL_FIRST, firstContent, 200, "OK", MimeType.TEXT_HTML, Collections.emptyList());
        webConnection.setResponse(URL_SECOND, secondContent, 200, "OK", MimeType.IMAGE_GIF, Collections.emptyList());
        webClient.setWebConnection(webConnection);

        final HtmlPage firstPage = webClient.getPage(URL_FIRST);
        assertEquals("First", firstPage.getTitleText());

        final List<WebWindowEvent> events = new LinkedList<>();
        webClient.addWebWindowListener(new WebWindowListener() {
            @Override
            public void webWindowOpened(final WebWindowEvent event) {
                events.add(event);
            }

            @Override
            public void webWindowContentChanged(final WebWindowEvent event) {
                events.add(event);
            }

            @Override
            public void webWindowClosed(final WebWindowEvent event) {
                events.add(event);
            }
        });

        final WebWindow firstWebWindow = firstPage.getEnclosingWindow();

        final HtmlAnchor anchor = firstPage.getHtmlElementById("link");
        final Page secondPage = anchor.click();
        assertEquals("First", firstPage.getTitleText());
        assertEquals(MimeType.IMAGE_GIF, secondPage.getWebResponse().getContentType());

        assertEquals(2, events.size());

        final WebWindow secondWebWindow = (WebWindow) events.get(0).getSource();

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

        final String firstContent = DOCTYPE_HTML
            + "<html><head><title>First</title></head><body>\n"
            + "<form name='form1'>\n"
            + "  <a id='link' onClick='window.open(\"" + URL_SECOND + "\", \"_blank\").focus(); return false;'"
            + "return false;'>Click me</a>\n"
            + "</form>\n"
            + "</body></html>";
        final String secondContent = "Hello World";

        webConnection.setResponse(URL_FIRST, firstContent, 200, "OK", MimeType.TEXT_HTML, Collections.emptyList());
        webConnection.setResponse(URL_SECOND, secondContent, 200, "OK", MimeType.TEXT_PLAIN, Collections.emptyList());
        webClient.setWebConnection(webConnection);

        final HtmlPage firstPage = webClient.getPage(URL_FIRST);
        assertEquals("First", firstPage.getTitleText());

        final List<WebWindowEvent> events = new LinkedList<>();
        webClient.addWebWindowListener(new WebWindowListener() {
            @Override
            public void webWindowOpened(final WebWindowEvent event) {
                events.add(event);
            }

            @Override
            public void webWindowContentChanged(final WebWindowEvent event) {
                events.add(event);
            }

            @Override
            public void webWindowClosed(final WebWindowEvent event) {
                events.add(event);
            }
        });

        final WebWindow firstWebWindow = firstPage.getEnclosingWindow();

        final HtmlAnchor anchor = firstPage.getHtmlElementById("link");
        final Page secondPage = anchor.click();
        assertEquals("First", firstPage.getTitleText());
        assertEquals(MimeType.TEXT_PLAIN, secondPage.getWebResponse().getContentType());

        assertEquals(2, events.size());

        final WebWindow secondWebWindow = (WebWindow) events.get(0).getSource();

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

        final String firstContent = DOCTYPE_HTML
            + "<html><head><title>First</title></head><body>\n"
            + "<form name='form1'>\n"
            + "  <a id='link' onClick='window.open(\"" + URL_SECOND + "\", \"_blank\").focus(); return false;'"
            + "return false;'>Click me</a>\n"
            + "</form>\n"
            + "</body></html>";
        final String secondContent = "<junk></junk>\n";

        webConnection.setResponse(URL_FIRST, firstContent, 200, "OK", MimeType.TEXT_HTML, Collections.emptyList());
        webConnection.setResponse(URL_SECOND, secondContent, 200, "OK", MimeType.TEXT_XML, Collections.emptyList());
        webClient.setWebConnection(webConnection);

        final HtmlPage firstPage = webClient.getPage(URL_FIRST);
        assertEquals("First", firstPage.getTitleText());

        final List<WebWindowEvent> events = new LinkedList<>();
        webClient.addWebWindowListener(new WebWindowListener() {
            @Override
            public void webWindowOpened(final WebWindowEvent event) {
                events.add(event);
            }

            @Override
            public void webWindowContentChanged(final WebWindowEvent event) {
                events.add(event);
            }

            @Override
            public void webWindowClosed(final WebWindowEvent event) {
                events.add(event);
            }
        });

        final WebWindow firstWebWindow = firstPage.getEnclosingWindow();

        final HtmlAnchor anchor = firstPage.getHtmlElementById("link");
        final Page secondPage = anchor.click();
        assertEquals("First", firstPage.getTitleText());
        assertEquals(MimeType.TEXT_XML, secondPage.getWebResponse().getContentType());

        assertEquals(2, events.size());

        final WebWindow secondWebWindow = (WebWindow) events.get(0).getSource();

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

        final String firstContent = DOCTYPE_HTML
            + "<html><head><title>First</title></head><body>\n"
            + "<form name='form1'>\n"
            + "  <a id='link' onClick='window.open(\"" + URL_SECOND + "\", \"_blank\").focus(); return false;'"
            + "return false;'>Click me</a>\n"
            + "</form>\n"
            + "</body></html>";
        final String secondContent = "var x=1;\n";

        webConnection.setResponse(URL_FIRST, firstContent, 200, "OK", MimeType.TEXT_HTML, Collections.emptyList());
        webConnection.setResponse(URL_SECOND, secondContent, 200, "OK", "text/javascript", Collections.emptyList());
        webClient.setWebConnection(webConnection);

        final HtmlPage firstPage = webClient.getPage(URL_FIRST);
        assertEquals("First", firstPage.getTitleText());

        final List<WebWindowEvent> events = new LinkedList<>();
        webClient.addWebWindowListener(new WebWindowListener() {
            @Override
            public void webWindowOpened(final WebWindowEvent event) {
                events.add(event);
            }

            @Override
            public void webWindowContentChanged(final WebWindowEvent event) {
                events.add(event);
            }

            @Override
            public void webWindowClosed(final WebWindowEvent event) {
                events.add(event);
            }
        });

        final WebWindow firstWebWindow = firstPage.getEnclosingWindow();

        final HtmlAnchor anchor = firstPage.getHtmlElementById("link");
        final Page secondPage = anchor.click();
        assertEquals("First", firstPage.getTitleText());
        assertEquals("text/javascript", secondPage.getWebResponse().getContentType());

        assertEquals(2, events.size());

        final WebWindow secondWebWindow = (WebWindow) events.get(0).getSource();

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

        final String firstContent = DOCTYPE_HTML
            + "<html><head><title>First</title></head><body>\n"
            + "<form name='form1'>\n"
            + "  <a id='link' onClick='window.open(\"" + URL_SECOND + "\", \"_blank\").focus(); return false;'"
            + "return false;'>Click me</a>\n"
            + "</form>\n"
            + "</body></html>";
        final String secondContent = DOCTYPE_HTML
            + "<html><head><title>Second</title></head><body>\n"
            + "<p>Hello World</p>\n"
            + "</body></html>";

        webConnection.setResponse(URL_FIRST, firstContent);
        webConnection.setResponse(URL_SECOND, secondContent);
        webClient.setWebConnection(webConnection);

        final HtmlPage firstPage = webClient.getPage(URL_FIRST);
        assertEquals("First", firstPage.getTitleText());

        final List<WebWindowEvent> events = new LinkedList<>();
        webClient.addWebWindowListener(new WebWindowListener() {
            @Override
            public void webWindowOpened(final WebWindowEvent event) {
                events.add(event);
            }

            @Override
            public void webWindowContentChanged(final WebWindowEvent event) {
                events.add(event);
            }

            @Override
            public void webWindowClosed(final WebWindowEvent event) {
                events.add(event);
            }
        });

        final WebWindow firstWebWindow = firstPage.getEnclosingWindow();

        final HtmlAnchor anchor = firstPage.getHtmlElementById("link");
        final Page secondPage = anchor.click();
        assertEquals("First", firstPage.getTitleText());
        assertEquals(MimeType.TEXT_HTML, secondPage.getWebResponse().getContentType());

        assertEquals(2, events.size());

        final WebWindow secondWebWindow = (WebWindow) events.get(0).getSource();

        assertSame(webClient.getCurrentWindow(), secondWebWindow);
        assertNotSame(firstWebWindow, secondWebWindow);
    }

    /**
     * Basic test for the <tt>showModalDialog</tt> method. See bug #703.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("not available")
    public void showModalDialog() throws Exception {
        final String html1 = DOCTYPE_HTML
            + "<html><head><script>\n"
            + "  function test() {\n"
            + "    if (!window.showModalDialog) {alert('not available'); return; }\n"
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

        final String html2 = DOCTYPE_HTML
            + "<html><head><script>\n"
            + "  var o = window.dialogArguments;\n"
            + "  alert(o.firstName);\n"
            + "  alert(o.lastName);\n"
            + "  window.returnValue = 'sdg';\n"
            + "</script></head>\n"
            + "<body>foo</body></html>";

        final WebClient client = getWebClient();
        final List<String> actual = new ArrayList<>();
        client.setAlertHandler(new CollectingAlertHandler(actual));

        final MockWebConnection conn = new MockWebConnection();
        conn.setResponse(URL_FIRST, html1);
        conn.setResponse(new URL(URL_FIRST, "myDialog.html"), html2);
        client.setWebConnection(conn);

        final HtmlPage page = client.getPage(URL_FIRST);
        final HtmlElement button = page.getHtmlElementById("b");
        final HtmlPage dialogPage = button.click();

        if (getExpectedAlerts().length > 1) {
            final DialogWindow dialog = (DialogWindow) dialogPage.getEnclosingWindow();
            dialog.close();
        }

        assertEquals(getExpectedAlerts(), actual);
    }

    /**
     * Test for the <tt>showModalDialog</tt> method.
     * This tests blocking until the window gets closed.
     * Can not currently be tested with WebDriver
     * https://github.com/seleniumhq/selenium-google-code-issue-archive/issues/284
     *
     * To fix this, we need to allow user to interact with the opened dialog before showModalDialog() returns
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"undefined", "not available"})
    public void showModalDialogWithButton() throws Exception {
        final String html1 = DOCTYPE_HTML
            + "<html><head>\n"
            + "  <script>\n"
            + "    function test() {\n"
            + "      alert(window.returnValue);\n"
            + "      if (!window.showModalDialog) {alert('not available'); return; }\n"
            + "      var res = showModalDialog('myDialog.html', null, 'dialogHeight:300px; dialogLeft:200px;');\n"
            + "      alert(res);\n"
            + "      alert('finished');\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body>\n"
            + "  <button onclick='test()' id='openDlg'>Test</button>\n"
            + "</body></html>";

        final String html2 = DOCTYPE_HTML
            + "<html><head>\n"
            + "</head>\n"
            + "<body>\n"
            + "  <button id='closeDlg' onclick='window.returnValue = \"result\"; window.close();'></button>\n"
            + "</body>\n"
            + "</html>";

        final WebClient client = getWebClient();
        final List<String> actual = new ArrayList<>();
        client.setAlertHandler(new CollectingAlertHandler(actual));

        final MockWebConnection conn = new MockWebConnection();
        conn.setResponse(URL_FIRST, html1);
        conn.setResponse(new URL(URL_FIRST, "myDialog.html"), html2);
        client.setWebConnection(conn);

        final HtmlPage page = getWebClient().getPage(URL_FIRST);
        final HtmlElement button = page.getHtmlElementById("openDlg");
        button.click();

        // TODO: <button id='closeDlg'> should be clicked
        assertEquals(getExpectedAlerts(), actual);
    }

    /**
     * Basic test for the <tt>showModelessDialog</tt> method. See bug #703.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("")
    public void showModelessDialog() throws Exception {
        final String html1 = DOCTYPE_HTML
            + "<html><head><script>\n"
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

        final String html2 = DOCTYPE_HTML
            + "<html><head><script>\n"
            + "function update() {\n"
            + "  var w = dialogArguments;\n"
            + "  w.userName = document.getElementById('name').value;\n"
            + "  w.update();\n"
            + "}\n"
            + "</script></head><body>\n"
            + "  Name: <input id='name'><input value='OK' id='b' type='button' onclick='update()'>\n"
            + "</body></html>";

        final WebClient client = getWebClient();
        final List<String> actual = new ArrayList<>();
        client.setAlertHandler(new CollectingAlertHandler(actual));

        final MockWebConnection conn = new MockWebConnection();
        conn.setResponse(URL_FIRST, html1);
        conn.setResponse(new URL(URL_FIRST, "myDialog.html"), html2);
        client.setWebConnection(conn);

        final HtmlPage page = client.getPage(URL_FIRST);
        final HtmlElement button = page.getHtmlElementById("b");
        final HtmlPage dialogPage = button.click();

        if (!dialogPage.getUrl().equals(URL_FIRST)) {
            final HtmlInput input = dialogPage.getHtmlElementById("name");
            input.setValue("a");

            final HtmlButtonInput button2 = (HtmlButtonInput) dialogPage.getHtmlElementById("b");
            button2.click();

            assertEquals(getExpectedAlerts(), actual);
        }
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"true", "[object Window]", "[object Window]"})
    public void overwriteProperty_top() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body><script>\n"
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
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"true", "[object Window]", "[object Window]"})
    public void overwriteProperty_top2() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body><script>\n"
            + "  alert(window.top == this);\n"
            + "  window.top = 123;\n"
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
        final String html = DOCTYPE_HTML
            + "<html><body><script>\n"
            + "  window.onbeforeunload = function() { alert('x'); return 'hello'; };\n"
            + "  window.location = 'foo.html';\n"
            + "</script></body></html>";

        final WebClient webClient = getWebClientWithMockWebConnection();
        getMockWebConnection().setDefaultResponse("");

        final OnbeforeunloadHandler handler = new OnbeforeunloadHandler() {
            @Override
            public boolean handleEvent(final Page page, final String returnValue) {
                final String[] expectedRequests = {""};
                assertEquals(expectedRequests, getMockWebConnection().getRequestedUrls(URL_FIRST));
                return true;
            }
        };
        webClient.setOnbeforeunloadHandler(handler);
        loadPageWithAlerts(html);

        final String[] expectedRequests = {"", "foo.html"};
        assertEquals(expectedRequests, getMockWebConnection().getRequestedUrls(URL_FIRST));
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void serialization() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head></head><body><iframe></iframe><script>window.frames</script></body></html>";
        final HtmlPage page = loadPageWithAlerts(html);
        clone(page.getEnclosingWindow());
    }

    /**
     * Regression test for bug #844.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("x")
    public void onbeforeunload_setToFunction() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body><script>\n"
            + "  window.onbeforeunload = function() { alert('x'); return 'x'; };\n"
            + "  window.location = 'about:blank';\n"
            + "</script></body></html>";
        loadPageWithAlerts(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"10", "20", "30", "40"})
    public void viewportSetters() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
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

        final List<String> collectedAlerts = new ArrayList<>();
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

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "",
            FF = "info: Dumper",
            FF_ESR = "info: Dumper")
    public void dump() throws Exception {
        final WebConsole console = getWebClient().getWebConsole();
        final List<String> messages = new ArrayList<>();
        console.setLogger(new Logger() {

            @Override
            public void warn(final Object message) {
            }

            @Override
            public void trace(final Object message) {
            }

            @Override
            public void info(final Object message) {
                messages.add("info: " + message);
            }

            @Override
            public void error(final Object message) {
            }

            @Override
            public void debug(final Object message) {
            }

            @Override
            public boolean isTraceEnabled() {
                return false;
            }

            @Override
            public boolean isDebugEnabled() {
                return false;
            }

            @Override
            public boolean isInfoEnabled() {
                return true;
            }

            @Override
            public boolean isWarnEnabled() {
                return true;
            }

            @Override
            public boolean isErrorEnabled() {
                return true;
            }
        });

        final String html = DOCTYPE_HTML
            + "<html><head><title>foo</title><script>\n"
            + "function test() {\n"
            + "  if (window.dump) {\n"
            + "    window.dump('Dumper');\n"
            + "  }\n"
            + "}\n"
            + "</script></head><body onload='test()'></body></html>";

        loadPage(html);
        assertEquals(getExpectedAlerts(), messages);
    }

    /**
     * Regression test for http://sourceforge.net/p/htmlunit/bugs/234/
     * and https://bugzilla.mozilla.org/show_bug.cgi?id=443491.
     * @throws Exception if the test fails
     */
    @Test
    public void overwriteFunctions_alert() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "  <script language='JavaScript'>\n"
            + "    function alert(x) {\n"
            + "      document.title = x;\n"
            + "    }\n"
            + "    alert('hello');\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body>\n"
            + "</body>\n"
            + "</html>";

        final HtmlPage page = loadPageWithAlerts(html);
        assertEquals("hello", page.getTitleText());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"before print§printed§from timeout§", "before print§print handled§printed§from timeout§"})
    public void printHandler() throws Exception {
        // we have to test this manually

        final WebClient webClient = getWebClient();
        final MockWebConnection webConnection = new MockWebConnection();

        final String firstContent = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + "  function log(msg) { window.document.title += msg + '§'; }\n"

            + "  function doTest() {\n"
            + "    setTimeout(() => { log('from timeout'); }, 100)\n"
            + "    log('before print');\n"
            + "    window.print();\n"
            + "    log('printed');\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body>\n"
            + "  <button id='click' onclick='doTest()'>Print</button>\n"
            + "</body></html>";

        final URL url = URL_FIRST;
        webConnection.setResponse(url, firstContent);
        webClient.setWebConnection(webConnection);

        HtmlPage page = webClient.getPage(URL_FIRST);
        page.getElementById("click").click();
        webClient.waitForBackgroundJavaScript(DEFAULT_WAIT_TIME.toMillis());

        assertEquals(getExpectedAlerts()[0], page.getTitleText());

        webClient.setPrintHandler(new PrintHandler() {
            @Override
            public void handlePrint(final HtmlPage pageToPrint) {
                try {
                    Thread.sleep(DEFAULT_WAIT_TIME.toMillis());
                }
                catch (final InterruptedException e) {
                    pageToPrint.executeJavaScript("log('" + e.getMessage() + "');");
                }
                pageToPrint.executeJavaScript("log('print handled');");
            }
        });

        page = webClient.getPage(URL_FIRST);
        page.getElementById("click").click();
        webClient.waitForBackgroundJavaScript(200000 * DEFAULT_WAIT_TIME.toMillis());

        assertEquals(getExpectedAlerts()[1], page.getTitleText());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("before print"
                    + "§event beforeprint"
                    + "§[object Event]beforeprint-false-false-false-[object Window]"
                        + "-false-2-true-true-[object Window]-[object Window]-beforeprint"
                    + "§event afterprint"
                    + "§[object Event]afterprint-false-false-false-[object Window]"
                        + "-false-2-true-true-[object Window]-[object Window]-afterprint"
                    + "§printed§")
    @HtmlUnitNYI(CHROME = "before print"
                    + "§event beforeprint"
                    + "§[object Event]beforeprint-false-false-false-[object Window]"
                        + "-false-2-undefined-true-[object Window]-[object Window]-beforeprint"
                    + "§event afterprint"
                    + "§[object Event]afterprint-false-false-false-[object Window]"
                        + "-false-2-undefined-true-[object Window]-[object Window]-afterprint"
                    + "§printed§",
            EDGE = "before print"
                    + "§event beforeprint"
                    + "§[object Event]beforeprint-false-false-false-[object Window]"
                        + "-false-2-undefined-true-[object Window]-[object Window]-beforeprint"
                    + "§event afterprint"
                    + "§[object Event]afterprint-false-false-false-[object Window]"
                        + "-false-2-undefined-true-[object Window]-[object Window]-afterprint"
                    + "§printed§",
            FF = "before print"
                    + "§event beforeprint"
                    + "§[object Event]beforeprint-false-false-false-[object Window]"
                        + "-false-2-undefined-true-[object Window]-[object Window]-beforeprint"
                    + "§event afterprint"
                    + "§[object Event]afterprint-false-false-false-[object Window]"
                        + "-false-2-undefined-true-[object Window]-[object Window]-afterprint"
                    + "§printed§",
            FF_ESR = "before print"
                    + "§event beforeprint"
                    + "§[object Event]beforeprint-false-false-false-[object Window]"
                        + "-false-2-undefined-true-[object Window]-[object Window]-beforeprint"
                    + "§event afterprint"
                    + "§[object Event]afterprint-false-false-false-[object Window]"
                        + "-false-2-undefined-true-[object Window]-[object Window]-afterprint"
                    + "§printed§")
    public void printEvent() throws Exception {
        // we have to test this manually

        final WebClient webClient = getWebClient();
        final MockWebConnection webConnection = new MockWebConnection();

        // without an print handler set the print method is a noop
        webClient.setPrintHandler(new PrintHandler() {
            @Override
            public void handlePrint(final HtmlPage page) {
            }
        });


        final String firstContent = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + "  function log(msg) { window.document.title += msg + '§'; }\n"

            + "  function dumpEvent(event) {\n"
            + "    var msg = event;\n"
            + "    msg = msg + event.type;\n"
            + "    msg = msg + '-' + event.bubbles;\n"
            + "    msg = msg + '-' + event.cancelable;\n"
            + "    msg = msg + '-' + event.composed;\n"
            + "    msg = msg + '-' + event.currentTarget;\n"
            + "    msg = msg + '-' + event.defaultPrevented;\n"
            + "    msg = msg + '-' + event.eventPhase;\n"
            + "    msg = msg + '-' + event.isTrusted;\n"
            + "    msg = msg + '-' + event.returnValue;\n"
            + "    msg = msg + '-' + event.srcElement;\n"
            + "    msg = msg + '-' + event.target;\n"
            // + "    msg = msg + '-' + event.timeStamp;\n"
            + "    msg = msg + '-' + event.type;\n"
            + "    log(msg);\n"
            + "  }\n"

            + "  function doTest() {\n"
            + "    addEventListener('beforeprint', function(e) { log('event beforeprint'); dumpEvent(e); })\n"
            + "    addEventListener('afterprint', function(e) { log('event afterprint'); dumpEvent(e); })\n"

            + "    log('before print');\n"
            + "    window.print();\n"
            + "    log('printed');\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body>\n"
            + "  <button id='click' onclick='doTest()'>Print</button>\n"
            + "</body></html>";

        final URL url = URL_FIRST;
        webConnection.setResponse(url, firstContent);
        webClient.setWebConnection(webConnection);

        final HtmlPage page = webClient.getPage(URL_FIRST);
        page.getElementById("click").click();
        webClient.waitForBackgroundJavaScript(DEFAULT_WAIT_TIME.toMillis());

        assertEquals(getExpectedAlerts()[0], page.getTitleText());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("block§none§block§")
    public void printCssMediaRule() throws Exception {
        // we have to test this manually

        final WebClient webClient = getWebClient();
        final MockWebConnection webConnection = new MockWebConnection();

        // without an print handler set the print method is a noop
        webClient.setPrintHandler(new PrintHandler() {
            @Override
            public void handlePrint(final HtmlPage page) {
                page.executeJavaScript(
                        "log(window.getComputedStyle(document.getElementById('tester') ,null)"
                                + ".getPropertyValue('display'))");
            }
        });


        final String firstContent = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + "  function log(msg) { window.document.title += msg + '§'; }\n"

            + "  function doTest() {\n"
            + "    log(window.getComputedStyle(document.getElementById('tester') ,null)"
                        + ".getPropertyValue('display'));\n"
            + "    window.print();\n"
            + "    log(window.getComputedStyle(document.getElementById('tester') ,null)"
                        + ".getPropertyValue('display'));\n"
            + "  }\n"
            + "</script>\n"
            + "<style type='text/css'>\n"
            + "  @media print { p { display: none }}\n"
            + "</style>"
            + "</head>\n"
            + "<body>\n"
            + "  <p id='tester'>HtmlUnit</p>\n"
            + "  <button id='click' onclick='doTest()'>Print</button>\n"
            + "</body></html>";

        final URL url = URL_FIRST;
        webConnection.setResponse(url, firstContent);
        webClient.setWebConnection(webConnection);

        final HtmlPage page = webClient.getPage(URL_FIRST);
        page.getElementById("click").click();
        webClient.waitForBackgroundJavaScript(DEFAULT_WAIT_TIME.toMillis());

        assertEquals(getExpectedAlerts()[0], page.getTitleText());
    }
}
