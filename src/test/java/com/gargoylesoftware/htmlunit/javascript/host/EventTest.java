/*
 * Copyright (c) 2002-2008 Gargoyle Software Inc.
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

import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.CollectingAlertHandler;
import com.gargoylesoftware.htmlunit.MockWebConnection;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebTestCase;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.Browser;
import com.gargoylesoftware.htmlunit.BrowserRunner.Browsers;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;
import com.gargoylesoftware.htmlunit.html.ClickableElement;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlButton;
import com.gargoylesoftware.htmlunit.html.HtmlButtonInput;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
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
 */
@RunWith(BrowserRunner.class)
public class EventTest extends WebTestCase {

    /**
     * Verify the "this" object refers to the Element being clicked when an
     * event handler is invoked.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("clickId")
    public void testThisDefined() throws Exception {
        final String content
            = "<html><head></head><body>\n"
            + "<input type='button' id='clickId'/>\n"
            + "<script>\n"
            + "function handler(event) { alert(this.getAttribute('id')); }\n"
            + "document.getElementById('clickId').onclick = handler;</script>\n"
            + "</body></html>";
        onClickPageTest(content);
    }

    /**
     * Verify setting a previously undefined/non-existent property on an Element
     * is accessible from inside an event handler.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("foo")
    public void testSetPropOnThisDefined() throws Exception {
        final String content
            = "<html><head></head><body>\n"
            + "<input type='button' id='clickId'/>\n"
            + "<script>\n"
            + "function handler(event) { alert(this.madeUpProperty); }\n"
            + "document.getElementById('clickId').onclick = handler;\n"
            + "document.getElementById('clickId').madeUpProperty = 'foo';\n"
            + "</script>\n"
            + "</body></html>";
        onClickPageTest(content);
    }

    /**
     * Verify that JavaScript snippets have a variable named 'event' available to them.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("defined")
    public void testEventArgDefinedByWrapper() throws Exception {
        final String content
            = "<html><head></head><body>\n"
            + "<input type='button' id='clickId' onclick=\"alert(event ? 'defined' : 'undefined')\"/>\n"
            + "</body></html>";
        onClickPageTest(content);
    }

    /**
     * Verify that when event handler is invoked an argument is passed in.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("defined")
    @Browsers({ Browser.FIREFOX_2, Browser.FIREFOX_3 })
    public void testEventArgDefined() throws Exception {
        final String content
            = "<html><head></head><body>\n"
            + "<input type='button' id='clickId'/>\n"
            + "<script>\n"
            + "function handler(event) { alert(event ? 'defined' : 'undefined'); }\n"
            + "document.getElementById('clickId').onclick = handler;</script>\n"
            + "</body></html>";
        onClickPageTest(content);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("pass")
    @Browsers({ Browser.FIREFOX_2, Browser.FIREFOX_3 })
    public void testEventTargetSameAsThis() throws Exception {
        final String content
            = "<html><head></head><body>\n"
            + "<input type='button' id='clickId'/>\n"
            + "<script>\n"
            + "function handler(event) {\n"
            + "alert(event.target == this ? 'pass' : event.target + '!=' + this); }\n"
            + "document.getElementById('clickId').onclick = handler;</script>\n"
            + "</body></html>";
        onClickPageTest(content);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("pass")
    @Browsers({ Browser.INTERNET_EXPLORER_6, Browser.INTERNET_EXPLORER_7 })
    public void testEventSrcElementSameAsThis() throws Exception {
        final String content
            = "<html><head></head><body>\n"
            + "<input type='button' id='clickId'/>\n"
            + "<script>\n"
            + "function handler(event) {\n"
            + "event = event ? event : window.event;\n"
            + "alert(event.srcElement == this ? 'pass' : event.srcElement + '!=' + this); }\n"
            + "document.getElementById('clickId').onclick = handler;</script>\n"
            + "</body></html>";
        onClickPageTest(content);
    }

    /**
     * Verify that <tt>event.currentTarget == this</tt> inside JavaScript event handler.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("pass")
    @Browsers({ Browser.FIREFOX_2, Browser.FIREFOX_3 })
    public void testEventCurrentTargetSameAsThis() throws Exception {
        final String content
            = "<html><head></head><body>\n"
            + "<input type='button' id='clickId'/>\n"
            + "<script>\n"
            + "function handler(event) {\n"
            + "alert(event.currentTarget == this ? 'pass' : event.currentTarget + '!=' + this); }\n"
            + "document.getElementById('clickId').onclick = handler;</script>\n"
            + "</body></html>";
        onClickPageTest(content);
    }

    /**
     * Tests that event fires on key press.
     * @throws Exception if the test fails
     */
    @Test
    public void testEventOnKeyDown() throws Exception {
        final String content
            = "<html><head></head><body>\n"
            + "<button type='button' id='clickId'/>\n"
            + "<script>\n"
            + "function handler(_e) {\n"
            + "  var e = _e ? _e : window.event;\n"
            + "if (e.keyCode == 65)\n"
            + "    alert('pass');\n"
            + "else\n"
            + "    alert('fail:' + e.keyCode);\n"
            + "}\n"
            + "document.getElementById('clickId').onkeydown = handler;\n"
            + "document.getElementById('clickId').onclick = handler;</script>\n"
            + "</body></html>";

        final List<String> collectedAlerts = new ArrayList<String>();
        final HtmlPage page = loadPage(getBrowserVersion(), content, collectedAlerts);
        final ClickableElement element = (ClickableElement) page.getHtmlElementById("clickId");
        element.type('A');
        element.type('B');
        element.click();
        final String[] expectedAlerts = {"pass", "fail:66", "fail:undefined"};
        assertEquals(expectedAlerts, collectedAlerts);
    }

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
            = "<html><head></head><body>\n"
            + "<button type='button' id='clickId'/>\n"
            + "<script>\n"
            + "function handler(_e) {\n"
            + "  var e = _e ? _e : window.event;\n"
            + "  alert(e.shiftKey + ',' + e.ctrlKey + ',' + e.altKey);\n"
            + "}\n"
            + "document.getElementById('clickId').onkeydown = handler;\n"
            + "</script>\n"
            + "</body></html>";

        final List<String> collectedAlerts = new ArrayList<String>();
        final HtmlPage page = loadPage(getBrowserVersion(), content, collectedAlerts);
        final ClickableElement element = (ClickableElement) page.getHtmlElementById("clickId");
        element.type('A', shiftKey, ctrlKey, altKey);
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void testTyping() throws Exception {
        testTyping("<input type='text'", "");
        testTyping("<input type='password'", "");
        testTyping("<textarea", "</textarea>");
    }

    private void testTyping(final String opening, final String closing) throws Exception {
        final String html =
              "<html><body>\n"
            + "<script>var x = '';\n"
            + "function log(s) { x += s; }</script>\n"
            + "<form>\n"
            + opening + " id='t' onkeydown='log(1 + this.value)' "
            + "onkeypress='log(2 + this.value)' onkeyup='log(3 + this.value)'>" + closing
            + "</form>\n"
            + "<div id='d' onclick='alert(x); x=\"\"'>abc</div>\n"
            + "</body></html>";

        final String[] expected = {"123A", "1A2A3AB1AB2AB3ABC"};
        final List<String> actual = new ArrayList<String>();
        final HtmlPage page = loadPage(getBrowserVersion(), html, actual);
        page.<HtmlElement>getHtmlElementById("t").type('A');
        ((HtmlDivision) page.getHtmlElementById("d")).click();

        page.<HtmlElement>getHtmlElementById("t").type("BC");
        ((HtmlDivision) page.getHtmlElementById("d")).click();

        assertEquals(expected, actual);
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
        final HtmlButton button = (HtmlButton) page.getHtmlElementById("button");

        final HtmlPage secondPage = (HtmlPage) button.click(shiftKey, ctrlKey, altKey);

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
        page.<HtmlElement>getHtmlElementById("textField").focus();
        page.<HtmlElement>getHtmlElementById("otherField").focus();
        final String[] expectedAlerts = {"true"};
        assertEquals(expectedAlerts, collectedAlerts);
    }

    private void onClickPageTest(final String content) throws Exception {
        final List<String> collectedAlerts = new ArrayList<String>();
        final HtmlPage page = loadPage(getBrowserVersion(), content, collectedAlerts);
        final ClickableElement clickable = (ClickableElement) page.getHtmlElementById("clickId");
        clickable.click();
        assertEquals(getExpectedAlerts(), collectedAlerts);
    }

    /**
     * Test that "this" refers to the element on which the event applies.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("frame1")
    public void testEventScope() throws Exception {
        final String html
            = "<html><head></head>\n"
            + "<body>\n"
            + "<button name='button1' id='button1' onclick='alert(this.name)'>1</button>\n"
            + "<iframe src='about:blank' name='frame1' id='frame1'></iframe>\n"
            + "<script>\n"
            + "document.getElementById('frame1').onload = document.getElementById('button1').onclick;\n"
            + "</script>\n"
            + "</body></html>";

        loadPageWithAlerts(html);
    }

    /**
     * Test event transmission to event handler.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(FF = { "false", "true", "SPAN" },
            IE = { "true", "false", "SPAN" })
    public void testEventTransmission() throws Exception {
        final String content =
            "<html><body><span id='clickMe'>foo</span>\n"
            + "<script>\n"
            + "function handler(e) {\n"
            + "  alert(e == null);\n"
            + "  alert(window.event == null);\n"
            + "  var theEvent = (e != null) ? e : window.event;\n"
            + "  var target = theEvent.target ? theEvent.target : theEvent.srcElement;\n"
            + "  alert(target.tagName);\n"
            + "}\n"
            + "document.getElementById('clickMe').onclick = handler;\n"
            + "</script></body></html>";

        final List<String> collectedAlerts = new ArrayList<String>();
        final HtmlPage page = loadPage(getBrowserVersion(), content, collectedAlerts);
        ((ClickableElement) page.getHtmlElementById("clickMe")).click();

        assertEquals(getExpectedAlerts(), collectedAlerts);
    }

    /**
     * Test that the event property of the window is available.
     * @throws Exception if the test fails
     */
    @Test
    @Browsers({ Browser.INTERNET_EXPLORER_6, Browser.INTERNET_EXPLORER_7 })
    @Alerts({ "false", "false" })
    public void testIEWindowEvent() throws Exception {
        final String html =
            "<html><head>\n"
            + "<script>\n"
            + "function test() {\n"
            + "  alert(window.event == null);\n"
            + "  alert(event == null);\n"
            + "}\n"
            + "</script>\n"
            + "</head><body onload='test()'></body></html>";

        loadPageWithAlerts(html);
    }

    /**
     * Test that the event handler is correctly parsed even if it contains comments.
     * It seems that it is correctly parsed and stored in non public field
     * org.apache.xerces.util.XMLAttributesImpl#nonNormalizedValue
     * but that getValue(i) returns a normalized value. Furthermore access seems not possible as
     * we just see an org.apache.xerces.parsers.AbstractSAXParser.AttributesProxy
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "1", "2" })
    public void testCommentInEventHandlerDeclaration() throws Exception {
        final String html
            = "<html><head></head>\n"
            + "<body onload='alert(1);\n"
            + "// a comment within the onload declaration\n"
            + "alert(2)'>\n"
            + "</body></html>";

        loadPageWithAlerts(html);
    }

    /**
     * Test for event capturing and bubbling in FF.
     * @throws Exception if the test fails
     */
    @Test
    @Browsers({ Browser.FIREFOX_2, Browser.FIREFOX_3 })
    @Alerts({ "window capturing", "div capturing", "span capturing",
        "span bubbling", "div", "div bubbling", "window bubbling" })
    public void testFF_EventCapturingAndBubbling() throws Exception {
        final String content = "<html><head><title>foo</title>\n"
            + "<script>\n"
            + "function t(_s)\n"
            + "{\n"
            + "     return function() { alert(_s) };\n"
            + "}\n"
            + "function init()\n"
            + "{\n"
            + "  window.addEventListener('click', t('window capturing'), true);\n"
            + "  window.addEventListener('click', t('window bubbling'), false);\n"
            + "  var oDiv = document.getElementById('theDiv');\n"
            + "  oDiv.addEventListener('click', t('div capturing'), true);\n"
            + "  oDiv.addEventListener('click', t('div bubbling'), false);\n"
            + "  var oSpan = document.getElementById('theSpan');\n"
            + "  oSpan.addEventListener('click', t('span capturing'), true);\n"
            + "  oSpan.addEventListener('click', t('span bubbling'), false);\n"
            + "}\n"
            + "</script>\n"
            + "</head><body onload='init()'>\n"
            + "<div onclick=\"alert('div')\" id='theDiv'>\n"
            + "<span id='theSpan'>blabla</span>\n"
            + "</div>\n"
            + "</body></html>";

        final List<String> collectedAlerts = new ArrayList<String>();
        final HtmlPage page = loadPage(getBrowserVersion(), content, collectedAlerts);
        ((ClickableElement) page.getHtmlElementById("theSpan")).click();

        assertEquals(getExpectedAlerts(), collectedAlerts);
    }

    /**
     * Test for event bubbling in IE.
     * @throws Exception if the test fails
     */
    @Test
    @NotYetImplemented // TODO: in IE no click event can be registered for the window
    @Alerts({ "span bubbling", "div", "div bubbling" })
    public void testIE_EventBubbling() throws Exception {
        final String content = "<html><head><title>foo</title>\n"
            + "<script>\n"
            + "function t(_s)\n"
            + "{\n"
            + "     return function() { alert(_s) };\n"
            + "}\n"
            + "function init()\n"
            + "{\n"
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
        ((ClickableElement) page.getHtmlElementById("theSpan")).click();

        assertEquals(getExpectedAlerts(), collectedAlerts);
    }

    /**
     * Test for event capturing and bubbling in FF.
     * @throws Exception if the test fails
     */
    @Test
    @Browsers({ Browser.FIREFOX_2, Browser.FIREFOX_3 })
    public void testFF_StopPropagation() throws Exception {
        final String content = "<html><head><title>foo</title>\n"
            + "<script>\n"
            + "var counter = 0;\n"
            + "function t(_s)\n"
            + "{\n"
            + "     return function(e) { alert(_s); counter++; if (counter >= 4) e.stopPropagation(); };\n"
            + "}\n"
            + "function init()\n"
            + "{\n"
            + "  window.addEventListener('click', t('window capturing'), true);\n"
            + "  var oDiv = document.getElementById('theDiv');\n"
            + "  oDiv.addEventListener('click', t('div capturing'), true);\n"
            + "  var oSpan = document.getElementById('theSpan');\n"
            + "  oSpan.addEventListener('click', t('span capturing'), true);\n"
            + "}\n"
            + "</script>\n"
            + "</head><body onload='init()'>\n"
            + "<div onclick=\"alert('div')\" id='theDiv'>\n"
            + "<span id='theSpan'>blabla</span>\n"
            + "</div>\n"
            + "</body></html>";

        final List<String> collectedAlerts = new ArrayList<String>();
        final HtmlPage page = loadPage(getBrowserVersion(), content, collectedAlerts);
        ((ClickableElement) page.getHtmlElementById("theSpan")).click();
        final String[] expectedAlerts1 = {"window capturing", "div capturing", "span capturing", "div"};
        assertEquals(expectedAlerts1, collectedAlerts);
        collectedAlerts.clear();

        ((ClickableElement) page.getHtmlElementById("theSpan")).click();
        final String[] expectedAlerts2 = {"window capturing"};
        assertEquals(expectedAlerts2, collectedAlerts);
    }

    /**
     * Test value for null event handler: null for IE, while 'undefined' for Firefox.
     * @throws Exception if the test fails
     */
    @Test
    @NotYetImplemented({ Browser.FIREFOX_2, Browser.FIREFOX_3 })
    @Alerts(FF = { "undefined" },
            IE = { "null" })
    public void testNullEventHandler() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var div = document.getElementById('myDiv');\n"
            + "    alert(div.onclick);\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "<div id='myDiv'/>\n"
            + "</body></html>";

        loadPageWithAlerts(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = {"object", "true" },
            IE = {"object", "undefined" })
    public void testBubbles() throws Exception {
        final String html =
              "<html><body onload='test(event)'><script>\n"
            + "    function test(e) {\n"
            + "        alert(typeof e);\n"
            + "        alert(e.bubbles);\n"
            + "    }\n"
            + "</script></body></html>";

        loadPageWithAlerts(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = {"object", "true" },
            IE = {"object", "undefined" })
    public void testCancelable() throws Exception {
        final String html =
              "<html><body onload='test(event)'><script>\n"
            + "    function test(e) {\n"
            + "        alert(typeof e);\n"
            + "        alert(e.cancelable);\n"
            + "    }\n"
            + "</script></body></html>";

        loadPageWithAlerts(html);
    }

    /**
     * Verifies that in IE, the <tt>shiftKey</tt>, <tt>ctrlKey</tt> and <tt>altKey</tt>
     * event attributes are defined for all events, but <tt>metaKey</tt> is not defined
     * for any events.<br/>
     * Verifies that in FF, the <tt>shiftKey</tt>, <tt>ctrlKey</tt>, <tt>altKey</tt> and
     * <tt>metaKey</tt> attributes are defined for mouse events only.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = {"object", "undefined", "undefined", "undefined", "undefined",
            "object", "false", "false", "false", "false" },
            IE = {"object", "false", "false", "false", "undefined",
            "object", "false", "false", "false", "undefined" })
    public void testKeys() throws Exception {
        final String html =
              "<html><body onload='test(event)'><script>\n"
            + "    function test(e) {\n"
            + "        alert(typeof e);\n"
            + "        alert(e.shiftKey);\n"
            + "        alert(e.ctrlKey);\n"
            + "        alert(e.altKey);\n"
            + "        alert(e.metaKey);\n"
            + "    }\n"
            + "</script>\n"
            + "<div id='div' onclick='test(event)'>abc</div>\n"
            + "</body></html>";
        final List<String> actual = new ArrayList<String>();
        final HtmlPage page = loadPage(getBrowserVersion(), html, actual);
        final HtmlDivision div = (HtmlDivision) page.getHtmlElementById("div");
        div.click();
        assertEquals(Arrays.toString(getExpectedAlerts()), actual.toString());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = {"object", "number" },
            IE = {"object", "undefined" })
    public void testTimeStamp() throws Exception {
        final String html =
              "<html><body onload='test(event)'><script>\n"
            + "    function test(e) {\n"
            + "        alert(typeof e);\n"
            + "        alert(typeof e.timeStamp);\n"
            + "    }\n"
            + "</script></body></html>";

        loadPageWithAlerts(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Browsers({ Browser.FIREFOX_2, Browser.FIREFOX_3 })
    @Alerts({ "capturing", "at target", "bubbling" })
    public void testEventPhase() throws Exception {
        final String html =
              "<html><head><script>\n"
            + "  function init() {\n"
            + "    var form = document.forms[0];\n"
            + "    form.addEventListener('click', alertPhase, true);\n"
            + "    form.addEventListener('click', alertPhase, false);\n"
            + "  }\n"
            + "  function alertPhase(e) {\n"
            + "    switch (e.eventPhase) {\n"
            + "      case 1: alert('capturing'); break;\n"
            + "      case 2: alert('at target'); break;\n"
            + "      case 3: alert('bubbling'); break;\n"
            + "      default: alert('unknown');\n"
            + "    }\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='init()'>\n"
            + "<form><input type='button' onclick='alertPhase(event)' id='b'></form>\n"
            + "</body></html>";

        final List<String> actual = new ArrayList<String>();
        final HtmlPage page = loadPage(getBrowserVersion(), html, actual);
        final HtmlButtonInput button = (HtmlButtonInput) page.getHtmlElementById("b");
        button.click();
        assertEquals(getExpectedAlerts(), actual);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Browsers(Browser.NONE)
    public void testSetEventPhaseToInvalidValue() throws Exception {
        boolean thrown = false;
        try {
            new Event().setEventPhase((short) 777);
        }
        catch (final IllegalArgumentException e) {
            thrown = true;
        }
        assertTrue(thrown);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Browsers({ Browser.FIREFOX_2, Browser.FIREFOX_3 })
    @Alerts({ "click", "true", "true", "dblclick", "false", "false" })
    public void testInitEvent() throws Exception {
        final String html =
              "<html><body onload='test()'><script>\n"
            + "  function test() {\n"
            + "    var e = document.createEvent('Event');\n"
            + "    e.initEvent('click', true, true);\n"
            + "    doAlerts(e);\n"
            + "    e.initEvent('dblclick', false, false);\n"
            + "    doAlerts(e);\n"
            + "  }\n"
            + "  function doAlerts(e) {\n"
            + "    alert(e.type);\n"
            + "    alert(e.bubbles);\n"
            + "    alert(e.cancelable);\n"
            + "  }\n"
            + "</script></body></html>";

        loadPageWithAlerts(html);
    }

    /**
     * Test Mozilla DOMContentLoaded event.
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(Browser.NONE) // TODO: use browsers to test for all
    public void testDOMContentLoaded() throws Exception {
        testHTMLFile("EventTest_DOMContentLoaded.html");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(Browser.NONE) // TODO: use browsers to test for all
    public void testPreventDefault() throws Exception {
        testHTMLFile("EventTest_preventDefault.html");
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

        final HtmlPage secondPage = (HtmlPage) anchor.click();
        assertEquals(new String[] {"a", "s", "d"}, collectedAlerts);

        if (changesPage) {
            assertNotSame(page, secondPage);
        }
        else {
            assertSame(page, secondPage);
        }
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({ "true", "I was here" })
    public void firedEvent_equals_original_event() throws Exception {
        final String html =
            "<html><head><title>First</title></head>\n"
            + "<script>\n"
            + "function test()\n"
            + "{\n"
            + "  var e = document.getElementById('myDiv');\n"
            + "  \n"
            + "  var myEvent\n"
            + "  var listener = function(x) { \n"
            + "    alert(x == myEvent);\n"
            + "    x.foo = 'I was here'\n"
            + "  }\n"
            + "  \n"
            + "  if (document.createEvent) {\n"
            + "    e.addEventListener('click', listener, false);\n"
            + "    myEvent = document.createEvent('HTMLEvents');\n"
            + "    myEvent.initEvent('click', true, true);\n"
            + "    e.dispatchEvent(myEvent);\n"
            + "  }\n"
            + "  else {\n"
            + "    e.attachEvent('onclick', listener);\n"
            + "    myEvent = document.createEventObject();\n"
            + "    myEvent.eventType = 'onclick';\n"
            + "    e.fireEvent(myEvent.eventType, myEvent);\n"
            + "  }\n"
            + "  alert(myEvent.foo);\n"
            + "}\n"
            + "</script>\n"
            + "<body onload='test()'>\n"
            + "<div id='myDiv'>toti</div>\n"
            + "</body></html>";

        loadPageWithAlerts(html);
    }
}
