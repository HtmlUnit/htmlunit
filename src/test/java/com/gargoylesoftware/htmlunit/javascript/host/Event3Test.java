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

import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.IE;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.Browsers;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;
import com.gargoylesoftware.htmlunit.CollectingAlertHandler;
import com.gargoylesoftware.htmlunit.MockWebConnection;
import com.gargoylesoftware.htmlunit.SimpleWebTestCase;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlButton;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * Tests that when DOM events such as "onclick" have access
 * to an {@link Event} object with context information.
 *
 * @version $Revision$
 * @author <a href="mailto:chriseldredge@comcast.net">Chris Eldredge</a>
 * @author Ahmed Ashour
 * @author Daniel Gredler
 * @author Marc Guillemot
 * @author Frank Danek
 */
@RunWith(BrowserRunner.class)
public class Event3Test extends SimpleWebTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testEventOnKeyDown_Shift_Ctrl_Alt() throws Exception {
        testEventOnKeyDown_Shift_Ctrl_Alt(false, false, false, new String[] {"false,false,false"});
        testEventOnKeyDown_Shift_Ctrl_Alt(true,  false, false, new String[] {"true,false,false"});
        testEventOnKeyDown_Shift_Ctrl_Alt(false, true,  false, new String[] {"false,true,false"});
        testEventOnKeyDown_Shift_Ctrl_Alt(false, false, true,  new String[] {"false,false,true"});
        testEventOnKeyDown_Shift_Ctrl_Alt(true,  true,  true,  new String[] {"true,true,true"});
    }

    private void testEventOnKeyDown_Shift_Ctrl_Alt(final boolean shiftKey,
            final boolean ctrlKey, final boolean altKey, final String[] expectedAlerts) throws Exception {
        final String content
            = "<html>\n"
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

        final List<String> collectedAlerts = new ArrayList<String>();
        final HtmlPage page = loadPage(getBrowserVersion(), content, collectedAlerts);
        page.getHtmlElementById("clickId").type('A', shiftKey, ctrlKey, altKey);
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testEventOnClick_Shift_Ctrl_Alt() throws Exception {
        testEventOnClick_Shift_Ctrl_Alt(false, false, false, new String[] {"false,false,false"});
        testEventOnClick_Shift_Ctrl_Alt(true,  false, false, new String[] {"true,false,false"});
        testEventOnClick_Shift_Ctrl_Alt(false, true,  false, new String[] {"false,true,false"});
        testEventOnClick_Shift_Ctrl_Alt(false, false, true,  new String[] {"false,false,true"});
        testEventOnClick_Shift_Ctrl_Alt(true,  true,  true,  new String[] {"true,true,true"});
    }

    private void testEventOnClick_Shift_Ctrl_Alt(final boolean shiftKey,
            final boolean ctrlKey, final boolean altKey, final String[] expectedAlerts) throws Exception {
        final String htmlContent
            = "<html><head><title>foo</title></head><body>\n"
            + "<form id='form1'>\n"
            + "    <button name='button' type='button' id='button'>Push me</button>\n"
            + "</form>\n"
            + "<script>\n"
            + "function handler(_e) {\n"
            + "  var e = _e ? _e : window.event;\n"
            + "  alert(e.shiftKey + ',' + e.ctrlKey + ',' + e.altKey);\n"
            + "}\n"
            + "document.getElementById('button').onclick = handler;\n"
            + "</script>\n"
            + "</body></html>";
        final List<String> collectedAlerts = new ArrayList<String>();
        final HtmlPage page = loadPage(getBrowserVersion(), htmlContent, collectedAlerts);
        final HtmlButton button = page.getHtmlElementById("button");

        final HtmlPage secondPage = button.click(shiftKey, ctrlKey, altKey);

        assertEquals(expectedAlerts, collectedAlerts);

        assertSame(page, secondPage);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testEventOnBlur() throws Exception {
        final String content
            = "<html><head></head><body>\n"
            + "<form action='foo'>\n"
            + "<input name='textField' id='textField' onblur='alert(event != null)'>\n"
            + "<input type='submit' id='otherField'>\n"
            + "</form>\n"
            + "</body></html>";

        final List<String> collectedAlerts = new ArrayList<String>();
        final HtmlPage page = loadPage(getBrowserVersion(), content, collectedAlerts);
        page.getHtmlElementById("textField").focus();
        page.getHtmlElementById("otherField").focus();
        final String[] expectedAlerts = {"true"};
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * Test for event bubbling in IE.
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(IE)
    @NotYetImplemented // TODO: in IE no click event can be registered for the window
    @Alerts({ "span bubbling", "div", "div bubbling" })
    public void testIE_EventBubbling() throws Exception {
        final String content = "<html><head><title>foo</title>\n"
            + "<script>\n"
            + "function t(_s) {\n"
            + "     return function() { alert(_s) };\n"
            + "}\n"
            + "function init() {\n"
            + "  window.attachEvent('onclick', t('window bubbling'));\n"
            + "  var oDiv = document.getElementById('theDiv');\n"
            + "  oDiv.attachEvent('onclick', t('div bubbling'));\n"
            + "  var oSpan = document.getElementById('theSpan');\n"
            + "  oSpan.attachEvent('onclick', t('span bubbling'));\n"
            + "}\n"
            + "</script>\n"
            + "</head><body onload='init()'>\n"
            + "<div onclick=\"alert('div')\" id='theDiv'>\n"
            + "<span id='theSpan'>blabla</span>\n"
            + "</div>\n"
            + "</body></html>";

        final List<String> collectedAlerts = new ArrayList<String>();
        final HtmlPage page = loadPage(getBrowserVersion(), content, collectedAlerts);
        page.getHtmlElementById("theSpan").click();

        assertEquals(getExpectedAlerts(), collectedAlerts);
    }

    /**
     * Test for bug 1976960: what happens with different return values at different levels?
     * @throws Exception if an error occurs
     */
    @Test
    public void testEventBubblingReturns_1() throws Exception {
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
    @Alerts(FF = "false", IE = "true") // here not alerts! ;-)
    public void testEventBubblingReturns_2() throws Exception {
        final boolean changesPage = Boolean.parseBoolean(getExpectedAlerts()[0]);
        testEventBubblingReturns("return true; ", "return false;", "return true; ", changesPage);
        testEventBubblingReturns("return true; ", "return true; ", "return false;", changesPage);

        testEventBubblingReturns("return true; ", "             ", "return false;", changesPage);
        testEventBubblingReturns("             ", "return true; ", "return false;", changesPage);
        testEventBubblingReturns("return true; ", "return false;", "             ", changesPage);
    }

    private void testEventBubblingReturns(final String onclick1,
        final String onclick2, final String onclick3, final boolean changesPage) throws Exception {

        final String html1
            = "<html><head><title>First</title></head><body>\n"
            + "<div onclick='alert(\"d\"); " + onclick1 + "'>\n"
            + "<span onclick='alert(\"s\"); " + onclick2 + "'>\n"
            + "<a href='" + URL_SECOND + "' id='a' onclick='alert(\"a\"); " + onclick3 + "'>go</a>\n"
            + "</span>\n"
            + "</div>\n"
            + "</body></html>";

        final String html2 = "<html><head><title>Second</title></head><body></body></html>";

        final WebClient client = getWebClient();
        final List<String> collectedAlerts = new ArrayList<String>();
        client.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        final MockWebConnection webConnection = new MockWebConnection();
        webConnection.setResponse(URL_FIRST, html1);
        webConnection.setResponse(URL_SECOND, html2);
        client.setWebConnection(webConnection);

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
