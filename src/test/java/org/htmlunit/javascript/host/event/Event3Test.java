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
package org.htmlunit.javascript.host.event;

import java.util.ArrayList;
import java.util.List;

import org.htmlunit.CollectingAlertHandler;
import org.htmlunit.MockWebConnection;
import org.htmlunit.SimpleWebTestCase;
import org.htmlunit.WebClient;
import org.htmlunit.html.HtmlAnchor;
import org.htmlunit.html.HtmlButton;
import org.htmlunit.html.HtmlPage;
import org.htmlunit.html.Keyboard;
import org.htmlunit.junit.annotation.Alerts;
import org.junit.jupiter.api.Test;

/**
 * Tests that when DOM events such as "onclick" have access
 * to an {@link Event} object with context information.
 *
 * @author Chris Eldredge
 * @author Ahmed Ashour
 * @author Daniel Gredler
 * @author Marc Guillemot
 * @author Frank Danek
 * @author Ronald Brill
 */
public class Event3Test extends SimpleWebTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void eventOnKeyDown_Shift_Ctrl_Alt() throws Exception {
        testEventOnKeyDown_Shift_Ctrl_Alt(false, false, false, "false,false,false");
        testEventOnKeyDown_Shift_Ctrl_Alt(true,  false, false, "true,false,false", "true,false,false");
        testEventOnKeyDown_Shift_Ctrl_Alt(false, true,  false, "false,true,false", "false,true,false");
        testEventOnKeyDown_Shift_Ctrl_Alt(false, false, true, "false,false,true", "false,false,true");
        testEventOnKeyDown_Shift_Ctrl_Alt(true,  true,  true, "false,true,false", "false,true,true",
                "true,true,true", "true,true,true");
    }

    private void testEventOnKeyDown_Shift_Ctrl_Alt(
            final boolean shiftKey, final boolean ctrlKey, final boolean altKey,
            final String... expectedAlerts) throws Exception {
        final String content = DOCTYPE_HTML
            + "<html>\n"
            + "<head></head>\n"
            + "<body>\n"
            + "  <button type='button' id='clickId'/>\n"
            + "  <script>\n"
            + "    function handler(_e) {\n"
            + "      var e = _e ? _e : window.event;\n"
            + "      alert(e.shiftKey + ',' + e.ctrlKey + ',' + e.altKey);\n"
            + "    }\n"
            + "    document.getElementById('clickId').onkeydown = handler;\n"
            + "  </script>\n"
            + "</body></html>";

        final List<String> collectedAlerts = new ArrayList<>();
        final HtmlPage page = loadPage(content, collectedAlerts);
        final Keyboard keyboard = new Keyboard();
        if (ctrlKey) {
            keyboard.press(KeyboardEvent.DOM_VK_CONTROL);
        }
        if (altKey) {
            keyboard.press(KeyboardEvent.DOM_VK_ALT);
        }
        if (shiftKey) {
            keyboard.press(KeyboardEvent.DOM_VK_SHIFT);
        }
        keyboard.type('a');
        if (ctrlKey) {
            keyboard.release(KeyboardEvent.DOM_VK_CONTROL);
        }
        if (altKey) {
            keyboard.release(KeyboardEvent.DOM_VK_ALT);
        }
        if (shiftKey) {
            keyboard.release(KeyboardEvent.DOM_VK_SHIFT);
        }
        page.getHtmlElementById("clickId").type(keyboard);
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void eventOnClick_Shift_Ctrl_Alt() throws Exception {
        testEventOnClick_Shift_Ctrl_Alt(false, false, false, new String[] {"false,false,false"});
        testEventOnClick_Shift_Ctrl_Alt(true,  false, false, new String[] {"true,false,false"});
        testEventOnClick_Shift_Ctrl_Alt(false, true,  false, new String[] {"false,true,false"});
        testEventOnClick_Shift_Ctrl_Alt(false, false, true,  new String[] {"false,false,true"});
        testEventOnClick_Shift_Ctrl_Alt(true,  true,  true,  new String[] {"true,true,true"});
    }

    private void testEventOnClick_Shift_Ctrl_Alt(final boolean shiftKey,
            final boolean ctrlKey, final boolean altKey, final String[] expectedAlerts) throws Exception {
        final String htmlContent = DOCTYPE_HTML
            + "<html><head><title>foo</title></head><body>\n"
            + "<form id='form1'>\n"
            + "  <button name='button' type='button' id='button'>Push me</button>\n"
            + "</form>\n"
            + "<script>\n"
            + "function handler(_e) {\n"
            + "  var e = _e ? _e : window.event;\n"
            + "  alert(e.shiftKey + ',' + e.ctrlKey + ',' + e.altKey);\n"
            + "}\n"
            + "document.getElementById('button').onclick = handler;\n"
            + "</script>\n"
            + "</body></html>";
        final List<String> collectedAlerts = new ArrayList<>();
        final HtmlPage page = loadPage(htmlContent, collectedAlerts);
        final HtmlButton button = page.getHtmlElementById("button");

        final HtmlPage secondPage = button.click(shiftKey, ctrlKey, altKey);

        assertEquals(expectedAlerts, collectedAlerts);

        assertSame(page, secondPage);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void eventOnBlur() throws Exception {
        final String content = DOCTYPE_HTML
            + "<html><head></head><body>\n"
            + "<form action='foo'>\n"
            + "<input name='textField' id='textField' onblur='alert(event != null)'>\n"
            + "<input type='submit' id='otherField'>\n"
            + "</form>\n"
            + "</body></html>";

        final List<String> collectedAlerts = new ArrayList<>();
        final HtmlPage page = loadPage(content, collectedAlerts);
        page.getHtmlElementById("textField").focus();
        page.getHtmlElementById("otherField").focus();
        final String[] expectedAlerts = {"true"};
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * Test for bug 1976960: what happens with different return values at different levels?
     * @throws Exception if an error occurs
     */
    @Test
    public void eventBubblingReturns_1() throws Exception {
        testEventBubblingReturns("", "", "", true);
        testEventBubblingReturns("return false;", "             ", "             ", false);
        testEventBubblingReturns("             ", "return false;", "             ", false);
        testEventBubblingReturns("             ", "             ", "return false;", false);

        testEventBubblingReturns("return true; ", "return true; ", "return true; ", true);
        testEventBubblingReturns("return false;", "return true; ", "return true; ", false);

        testEventBubblingReturns("             ", "return false;", "return true; ", false);
        testEventBubblingReturns("return false;", "return true; ", "             ", false);
        testEventBubblingReturns("return false;", "             ", "return true; ", false);
    }

    /**
     * Test for bug 1976960: what happens with different return values at different levels?
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("false") // here not alerts! ;-)
    public void eventBubblingReturns_2() throws Exception {
        final boolean changesPage = Boolean.parseBoolean(getExpectedAlerts()[0]);
        testEventBubblingReturns("return true; ", "return false;", "return true; ", changesPage);
        testEventBubblingReturns("return true; ", "return true; ", "return false;", changesPage);

        testEventBubblingReturns("return true; ", "             ", "return false;", changesPage);
        testEventBubblingReturns("             ", "return true; ", "return false;", changesPage);
        testEventBubblingReturns("return true; ", "return false;", "             ", changesPage);
    }

    private void testEventBubblingReturns(final String onclick1,
        final String onclick2, final String onclick3, final boolean changesPage) throws Exception {

        final String html1 = DOCTYPE_HTML
            + "<html><head><title>First</title></head><body>\n"
            + "<div onclick='alert(\"d\"); " + onclick1 + "'>\n"
            + "<span onclick='alert(\"s\"); " + onclick2 + "'>\n"
            + "<a href='" + URL_SECOND + "' id='a' onclick='alert(\"a\"); " + onclick3 + "'>go</a>\n"
            + "</span>\n"
            + "</div>\n"
            + "</body></html>";

        final String html2 = DOCTYPE_HTML + "<html><head><title>Second</title></head><body></body></html>";

        final WebClient client = getWebClientWithMockWebConnection();
        final List<String> collectedAlerts = new ArrayList<>();
        client.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        final MockWebConnection webConnection = getMockWebConnection();
        webConnection.setResponse(URL_FIRST, html1);
        webConnection.setResponse(URL_SECOND, html2);

        final HtmlPage page = client.getPage(URL_FIRST);
        final HtmlAnchor anchor = page.getHtmlElementById("a");

        final HtmlPage secondPage = anchor.click();
        assertEquals(new String[] {"a", "s", "d"}, collectedAlerts);

        if (changesPage) {
            assertNotSame(page, secondPage);
        }
        else {
            assertSame(page, secondPage);
        }
    }

}
