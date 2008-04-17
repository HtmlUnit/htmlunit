/*
 * Copyright (c) 2002-2008 Gargoyle Software Inc. All rights reserved.
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

import static org.junit.Assert.assertSame;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebTestCase;
import com.gargoylesoftware.htmlunit.html.ClickableElement;
import com.gargoylesoftware.htmlunit.html.HtmlButton;
import com.gargoylesoftware.htmlunit.html.HtmlButtonInput;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;

/**
 * Tests that when DOM events such as "onclick" have access
 * to an {@link Event} object with context information.
 *
 * @version $Revision$
 * @author <a href="mailto:chriseldredge@comcast.net">Chris Eldredge</a>
 * @author Ahmed Ashour
 * @author Daniel Gredler
 */
public class EventTest extends WebTestCase {

    /**
     * Verify the "this" object refers to the Element being clicked when an
     * event handler is invoked.
     * @throws Exception if the test fails
     */
    @Test
    public void testThisDefined() throws Exception {
        final String[] expectedAlerts = {"clickId"};

        final String content
            = "<html><head></head><body>\n"
            + "<input type='button' id='clickId'/>\n"
            + "<script>\n"
            + "function handler(event) { alert(this.getAttribute('id')); }\n"
            + "document.getElementById('clickId').onclick = handler;</script>\n"
            + "</body></html>";
        onClickPageTest(content, expectedAlerts);
    }

    /**
     * Verify setting a previously undefined/non-existent property on an Element
     * is accessible from inside an event handler.
     * @throws Exception if the test fails
     */
    @Test
    public void testSetPropOnThisDefined() throws Exception {
        final String[] expectedAlerts = {"foo"};

        final String content
            = "<html><head></head><body>\n"
            + "<input type='button' id='clickId'/>\n"
            + "<script>\n"
            + "function handler(event) { alert(this.madeUpProperty); }\n"
            + "document.getElementById('clickId').onclick = handler;\n"
            + "document.getElementById('clickId').madeUpProperty = 'foo';\n"
            + "</script>\n"
            + "</body></html>";
        onClickPageTest(content, expectedAlerts);
    }

    /**
     * Verify that JavaScript snippets have a variable named 'event' available to them.
     * @throws Exception if the test fails
     */
    @Test
    public void testEventArgDefinedByWrapper() throws Exception {
        final String[] expectedAlerts = {"defined"};

        final String content
            = "<html><head></head><body>\n"
            + "<input type='button' id='clickId' onclick=\"alert(event ? 'defined' : 'undefined')\"/>\n"
            + "</body></html>";
        onClickPageTest(BrowserVersion.FIREFOX_2, content, expectedAlerts);
        onClickPageTest(BrowserVersion.INTERNET_EXPLORER_6_0, content, expectedAlerts);
    }

    /**
     * Verify that when event handler is invoked an argument is passed in.
     * @throws Exception if the test fails
     */
    @Test
    public void testEventArgDefined() throws Exception {
        final String[] expectedAlerts = {"defined"};
        final String content
            = "<html><head></head><body>\n"
            + "<input type='button' id='clickId'/>\n"
            + "<script>\n"
            + "function handler(event) { alert(event ? 'defined' : 'undefined'); }\n"
            + "document.getElementById('clickId').onclick = handler;</script>\n"
            + "</body></html>";
        onClickPageTest(BrowserVersion.FIREFOX_2, content, expectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testEventTargetSameAsThis() throws Exception {
        final String[] expectedAlerts = {"pass"};
        final String content
            = "<html><head></head><body>\n"
            + "<input type='button' id='clickId'/>\n"
            + "<script>\n"
            + "function handler(event) {\n"
            + "alert(event.target == this ? 'pass' : event.target + '!=' + this); }\n"
            + "document.getElementById('clickId').onclick = handler;</script>\n"
            + "</body></html>";
        onClickPageTest(BrowserVersion.FIREFOX_2, content, expectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testEventSrcElementSameAsThis() throws Exception {
        final String[] expectedAlerts = {"pass"};
        final String content
            = "<html><head></head><body>\n"
            + "<input type='button' id='clickId'/>\n"
            + "<script>\n"
            + "function handler(event) {\n"
            + "event = event ? event : window.event;\n"
            + "alert(event.srcElement == this ? 'pass' : event.srcElement + '!=' + this); }\n"
            + "document.getElementById('clickId').onclick = handler;</script>\n"
            + "</body></html>";
        onClickPageTest(BrowserVersion.INTERNET_EXPLORER_6_0, content, expectedAlerts);
    }

    /**
     * Verify that <tt>event.currentTarget == this</tt> inside JavaScript event handler.
     * @throws Exception if the test fails
     */
    @Test
    public void testEventCurrentTargetSameAsThis() throws Exception {
        final String[] expectedAlerts = {"pass"};
        final String content
            = "<html><head></head><body>\n"
            + "<input type='button' id='clickId'/>\n"
            + "<script>\n"
            + "function handler(event) {\n"
            + "alert(event.currentTarget == this ? 'pass' : event.currentTarget + '!=' + this); }\n"
            + "document.getElementById('clickId').onclick = handler;</script>\n"
            + "</body></html>";
        onClickPageTest(BrowserVersion.FIREFOX_2, content, expectedAlerts);
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
        final HtmlPage page = loadPage(content, collectedAlerts);
        final ClickableElement element = page.getHtmlElementById("clickId");
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
        final HtmlPage page = loadPage(content, collectedAlerts);
        final ClickableElement element = page.getHtmlElementById("clickId");
        element.type('A', shiftKey, ctrlKey, altKey);
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void testTyping() throws Exception {
        final String html =
              "<html><body>\n"
            + "<script>var x = '';</script>\n"
            + "<form><input type='text' id='t' onkeydown='x+=\"1\"' onkeypress='x+=\"2\"' onkeyup='x+=\"3\"'/></form>\n"
            + "<div id='d' onclick='alert(x)'>abc</div>\n"
            + "</body></html>";

        final String[] expected = {"123", "123123123"};
        final List<String> actual = new ArrayList<String>();
        final HtmlPage page = loadPage(BrowserVersion.FIREFOX_2, html, actual);
        ((HtmlTextInput) page.getHtmlElementById("t")).type('A');
        ((HtmlDivision) page.getHtmlElementById("d")).click();

        ((HtmlTextInput) page.getHtmlElementById("t")).type("BC");
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
            + "    <button name='button' id='button'>Push me</button>\n"
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
        final HtmlPage page = loadPage(htmlContent, collectedAlerts);
        final HtmlButton button = page.getHtmlElementById("button");

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
        final HtmlPage page = loadPage(content, collectedAlerts);
        page.getHtmlElementById("textField").focus();
        page.getHtmlElementById("otherField").focus();
        final String[] expectedAlerts = {"true"};
        assertEquals(expectedAlerts, collectedAlerts);
    }

    private void onClickPageTest(final String content, final String[] expectedAlerts) throws Exception, IOException {
        onClickPageTest(BrowserVersion.getDefault(), content, expectedAlerts);
    }

    private void onClickPageTest(final BrowserVersion version, final String content, final String[] expectedAlerts)
        throws Exception, IOException {

        final List<String> collectedAlerts = new ArrayList<String>();
        final HtmlPage page = loadPage(version, content, collectedAlerts);
        final ClickableElement clickable = page.getHtmlElementById("clickId");
        clickable.click();
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * Test that "this" refers to the element on which the event applies.
     * @throws Exception if the test fails
     */
    @Test
    public void testEventScope() throws Exception {
        final List<String> expectedAlerts = Collections.singletonList("frame1");
        final String content
            = "<html><head></head>\n"
            + "<body>\n"
            + "<button name='button1' id='button1' onclick='alert(this.name)'>1</button>\n"
            + "<iframe src='about:blank' name='frame1' id='frame1'></iframe>\n"
            + "<script>\n"
            + "document.getElementById('frame1').onload = document.getElementById('button1').onclick;\n"
            + "</script>\n"
            + "</body></html>";
        final List<String> collectedAlerts = new ArrayList<String>();
        loadPage(content, collectedAlerts);
        createTestPageForRealBrowserIfNeeded(content, expectedAlerts);

        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * Test event transmission to event handler.
     * @throws Exception if the test fails
     */
    @Test
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
        HtmlPage page = loadPage(BrowserVersion.FIREFOX_2, content, collectedAlerts);
        ((ClickableElement) page.getHtmlElementById("clickMe")).click();

        String[] expectedAlerts = {"false", "true", "SPAN"};
        assertEquals(expectedAlerts, collectedAlerts);

        collectedAlerts.clear();
        page = loadPage(BrowserVersion.INTERNET_EXPLORER_6_0, content, collectedAlerts);
        ((ClickableElement) page.getHtmlElementById("clickMe")).click();

        expectedAlerts = new String[] {"true", "false", "SPAN"};
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * Test that the event property of the window is available.
     * @throws Exception if the test fails
     */
    @Test
    public void testIEWindowEvent() throws Exception {
        final String content =
            "<html><head>\n"
            + "<script>\n"
            + "function test() {\n"
            + "  alert(window.event == null);\n"
            + "  alert(event == null);\n"
            + "}\n"
            + "</script>\n"
            + "</head><body onload='test()'></body></html>";

        final List<String> collectedAlerts = new ArrayList<String>();
        loadPage(BrowserVersion.INTERNET_EXPLORER_6_0, content, collectedAlerts);

        final String[] expectedAlerts = {"false", "false"};
        assertEquals(expectedAlerts, collectedAlerts);
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
    public void testCommentInEventHandlerDeclaration() throws Exception {
        final String content
            = "<html><head></head>\n"
            + "<body onload='alert(1);\n"
            + "// a comment within the onload declaration\n"
            + "alert(2)'>\n"
            + "</body></html>";
        final List<String> collectedAlerts = new ArrayList<String>();
        loadPage(content, collectedAlerts);
        final String[] expectedAlerts = {"1", "2"};
        createTestPageForRealBrowserIfNeeded(content, expectedAlerts);

        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * Test for event capturing and bubbling in FF.
     * @throws Exception if the test fails
     */
    @Test
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
        final HtmlPage page = loadPage(BrowserVersion.FIREFOX_2, content, collectedAlerts);
        ((ClickableElement) page.getHtmlElementById("theSpan")).click();

        final String[] expectedAlerts = {"window capturing", "div capturing", "span capturing",
            "span bubbling", "div", "div bubbling", "window bubbling"};
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * Test for event bubbling in IE.
     * @throws Exception if the test fails
     */
    @Test
    public void testIE_EventBubbling() throws Exception {
        // TODO: in IE no click event can be registered for the window
        if (notYetImplemented()) {
            return;
        }
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
        final HtmlPage page = loadPage(BrowserVersion.INTERNET_EXPLORER_6_0, content, collectedAlerts);
        ((ClickableElement) page.getHtmlElementById("theSpan")).click();

        final String[] expectedAlerts = {"span bubbling", "div", "div bubbling"};
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * Test for event capturing and bubbling in FF.
     * @throws Exception if the test fails
     */
    @Test
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
        final HtmlPage page = loadPage(BrowserVersion.FIREFOX_2, content, collectedAlerts);
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
    public void testNullEventHandler() throws Exception {
        if (notYetImplemented()) {
            return;
        }
        testNullEventHandler(BrowserVersion.INTERNET_EXPLORER_7_0, "null");
        testNullEventHandler(BrowserVersion.FIREFOX_2, "undefined");
    }
    
    private void testNullEventHandler(final BrowserVersion browserVersion, final String expectedAlert)
        throws Exception {
        final String content = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var div = document.getElementById('myDiv');\n"
            + "    alert(div.onclick);\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "<div id='myDiv'/>\n"
            + "</body></html>";

        final List<String> collectedAlerts = new ArrayList<String>();
        loadPage(browserVersion, content, collectedAlerts);
        assertEquals(expectedAlert, collectedAlerts.get(0));
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void testBubbles_IE() throws Exception {
        testBubbles(BrowserVersion.INTERNET_EXPLORER_7_0, new String[] {"object", "undefined"});
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void testBubbles_FF() throws Exception {
        testBubbles(BrowserVersion.FIREFOX_2, new String[] {"object", "true"});
    }

    private void testBubbles(final BrowserVersion browser, final String[] expected) throws Exception {
        final String html =
              "<html><body onload='test(event)'><script>\n"
            + "    function test(e) {\n"
            + "        alert(typeof e);\n"
            + "        alert(e.bubbles);\n"
            + "    }\n"
            + "</script></body></html>";
        final List<String> actual = new ArrayList<String>();
        loadPage(browser, html, actual);
        assertEquals(expected, actual);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void testCancelable_IE() throws Exception {
        testCancelable(BrowserVersion.INTERNET_EXPLORER_7_0, new String[] {"object", "undefined"});
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void testCancelable_FF() throws Exception {
        testCancelable(BrowserVersion.FIREFOX_2, new String[] {"object", "true"});
    }

    private void testCancelable(final BrowserVersion browser, final String[] expected) throws Exception {
        final String html =
              "<html><body onload='test(event)'><script>\n"
            + "    function test(e) {\n"
            + "        alert(typeof e);\n"
            + "        alert(e.cancelable);\n"
            + "    }\n"
            + "</script></body></html>";
        final List<String> actual = new ArrayList<String>();
        loadPage(browser, html, actual);
        assertEquals(expected, actual);
    }

    /**
     * Verifies that in IE, the <tt>shiftKey</tt>, <tt>ctrlKey</tt> and <tt>altKey</tt>
     * event attributes are defined for all events, but <tt>metaKey</tt> is not defined
     * for any events.
     * @throws Exception if an error occurs
     */
    @Test
    public void testKeys_IE() throws Exception {
        testKeys(BrowserVersion.INTERNET_EXPLORER_7_0, new String[] {
            "object", "false", "false", "false", "undefined",
            "object", "false", "false", "false", "undefined" });
    }

    /**
     * Verifies that in FF, the <tt>shiftKey</tt>, <tt>ctrlKey</tt>, <tt>altKey</tt> and
     * <tt>metaKey</tt> attributes are defined for mouse events only.
     * @throws Exception if an error occurs
     */
    @Test
    public void testKeys_FF() throws Exception {
        testKeys(BrowserVersion.FIREFOX_2, new String[] {
            "object", "undefined", "undefined", "undefined", "undefined",
            "object", "false", "false", "false", "false" });
    }

    private void testKeys(final BrowserVersion browser, final String[] expected) throws Exception {
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
        final HtmlPage page = loadPage(browser, html, actual);
        final HtmlDivision div = page.getHtmlElementById("div");
        div.click();
        assertEquals(expected, actual);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void testTimeStamp_IE() throws Exception {
        testTimeStamp(BrowserVersion.INTERNET_EXPLORER_7_0, new String[] {"object", "undefined"});
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void testTimeStamp_FF() throws Exception {
        testTimeStamp(BrowserVersion.FIREFOX_2, new String[] {"object", "number"});
    }

    private void testTimeStamp(final BrowserVersion browser, final String[] expected) throws Exception {
        final String html =
              "<html><body onload='test(event)'><script>\n"
            + "    function test(e) {\n"
            + "        alert(typeof e);\n"
            + "        alert(typeof e.timeStamp);\n"
            + "    }\n"
            + "</script></body></html>";
        final List<String> actual = new ArrayList<String>();
        loadPage(browser, html, actual);
        assertEquals(expected, actual);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
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
        final String[] expected = {"capturing", "at target", "bubbling"};
        final List<String> actual = new ArrayList<String>();
        final HtmlPage page = loadPage(BrowserVersion.FIREFOX_2, html, actual);
        final HtmlButtonInput button = page.getHtmlElementById("b");
        button.click();
        assertEquals(expected, actual);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
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
        final String[] expected = {"click", "true", "true", "dblclick", "false", "false"};
        final List<String> actual = new ArrayList<String>();
        loadPage(BrowserVersion.FIREFOX_2, html, actual);
        assertEquals(expected, actual);
    }

    /**
     * Test Mozilla DOMContentLoaded event.
     * @throws Exception if the test fails
     */
    @Test
    public void testDOMContentLoaded() throws Exception {
        testHTMLFile("EventTest_DOMContentLoaded.html");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testPreventDefault() throws Exception {
        testHTMLFile("EventTest_preventDefault.html");
    }
}
