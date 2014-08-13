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

import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.FF;
import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.IE11;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.BuggyWebDriver;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.html.HtmlPageTest;

/**
 * Tests that when DOM events such as "onclick" have access
 * to an {@link Event} object with context information.
 *
 * @version $Revision$
 * @author <a href="mailto:chriseldredge@comcast.net">Chris Eldredge</a>
 * @author Ahmed Ashour
 * @author Daniel Gredler
 * @author Marc Guillemot
 * @author Ronald Brill
 * @author Frank Danek
 */
@RunWith(BrowserRunner.class)
public class EventTest extends WebDriverTestCase {

    private static final String DUMP_EVENT_FUNCTION = "  function dump(event) {\n"
        + "    if (event) {\n"
        + "      alert(event);\n"
        + "      alert(event.type);\n"
        + "      alert(event.bubbles);\n"
        + "      alert(event.cancelable);\n"
        + "    } else {\n"
        + "      alert('no event');\n"
        + "    }\n"
        + "  }\n";

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "[object Event]", "event", "false", "false" },
            IE = "exception")
    public void create_ctor() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    try {\n"
            + "      var event = new Event('event');\n"
            + "      dump(event);\n"
            + "    } catch (e) { alert('exception') }\n"
            + "  }\n"
            + DUMP_EVENT_FUNCTION
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "[object Event]", "event", "true", "false" },
            IE = "exception")
    public void create_ctorWithDetails() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    try {\n"
            + "      var event = new Event('event', {\n"
            + "        'bubbles': true\n"
            + "      });\n"
            + "      dump(event);\n"
            + "    } catch (e) { alert('exception') }\n"
            + "  }\n"
            + DUMP_EVENT_FUNCTION
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "[object Event]", "", "false", "false" },
            FF24 = { "[object Event]", "", "true", "true" },
            IE8 = "exception")
    public void create_createEvent() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    try {\n"
            + "      var event = document.createEvent('Event');\n"
            + "      dump(event);\n"
            + "    } catch (e) { alert('exception') }\n"
            + "  }\n"
            + DUMP_EVENT_FUNCTION
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "DOM2: [object Event]", "DOM3: [object Event]", "vendor: [object Event]" },
            IE8 = { "DOM2: exception", "DOM3: exception", "vendor: exception" })
    public void create_createEventForDifferentTypes() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    try {\n"
            + "      alert('DOM2: ' + document.createEvent('HTMLEvents'));\n"
            + "    } catch(e) {alert('DOM2: exception')}\n"
            + "    try {\n"
            + "      alert('DOM3: ' + document.createEvent('Event'));\n"
            + "    } catch(e) {alert('DOM3: exception')}\n"
            + "    try {\n"
            + "      alert('vendor: ' + document.createEvent('Events'));\n"
            + "    } catch(e) {alert('vendor: exception')}\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "[object Event]", "event", "true", "false" },
            IE8 = "exception")
    public void initEvent() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    try {\n"
            + "      var event = document.createEvent('Event');\n"
            + "      event.initEvent('event', true, false);\n"
            + "      dump(event);\n"
            + "    } catch (e) { alert('exception') }\n"
            + "  }\n"
            + DUMP_EVENT_FUNCTION
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

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
            + "  function handler(event) { alert(this.getAttribute('id')); }\n"
            + "  document.getElementById('clickId').onclick = handler;</script>\n"
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
            + "  function handler(event) { alert(this.madeUpProperty); }\n"
            + "  document.getElementById('clickId').onclick = handler;\n"
            + "  document.getElementById('clickId').madeUpProperty = 'foo';\n"
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
    @Alerts(DEFAULT = "defined", IE8 = "undefined")
    public void testEventArgDefined() throws Exception {
        final String content
            = "<html><head></head>\n"
            + "<body>\n"
            + "<input type='button' id='clickId'/>\n"
            + "<script>\n"
            + "  function handler(event) { alert(event ? 'defined' : 'undefined'); }\n"
            + "  document.getElementById('clickId').onclick = handler;\n"
            + "</script>\n"
            + "</body></html>";
        onClickPageTest(content);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "pass", IE8 = "no event param")
    public void testEventTargetSameAsThis() throws Exception {
        final String content
            = "<html><head></head>\n"
            + "<body>\n"
            + "<input type='button' id='clickId'/>\n"
            + "<script>\n"
            + "  function handler(event) {\n"
            + "    if(!event) { alert('no event param'); return; };\n"
            + "    alert(event.target == this ? 'pass' : event.target + '!=' + this);\n"
            + "  }\n"
            + "  document.getElementById('clickId').onclick = handler;\n"
            + "</script>\n"
            + "</body></html>";
        onClickPageTest(content);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "[object HTMLInputElement]", "true" },
            FF = { "undefined", "false" },
            IE8 = { "[object]", "true" })
    public void testEventSrcElementSameAsThis() throws Exception {
        final String content
            = "<html><head></head><body>\n"
            + "<input type='button' id='clickId'/>\n"
            + "<script>\n"
            + "  function handler(event) {\n"
            + "    event = event ? event : window.event;\n"
            + "    alert(event.srcElement);\n"
            + "    alert(event.srcElement == this);\n"
            + "  }\n"
            + "  document.getElementById('clickId').onclick = handler;\n"
            + "</script>\n"
            + "</body></html>";
        onClickPageTest(content);
    }

    /**
     * Verify that <tt>event.currentTarget == this</tt> inside JavaScript event handler.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "pass", IE8 = "no event param")
    public void testEventCurrentTargetSameAsThis() throws Exception {
        final String content
            = "<html><head></head>\n"
            + "<body>\n"
            + "<input type='button' id='clickId'/>\n"
            + "<script>\n"
            + "  function handler(event) {\n"
            + "    if(!event) { alert('no event param'); return; };\n"
            + "    alert(event.currentTarget == this ? 'pass' : event.currentTarget + '!=' + this);\n"
            + "  }\n"
            + "  document.getElementById('clickId').onclick = handler;\n"
            + "</script>\n"
            + "</body></html>";
        onClickPageTest(content);
    }

    /**
     * Property currentTarget needs to be set again depending on the listener invoked.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "[object Window]", "[object HTMLDivElement]" },
            IE8 = { "no addEventListener", "no event param" })
    public void testCurrentTarget_sameListenerForEltAndWindow() throws Exception {
        final String content
            = "<html><head></head><body>\n"
            + "<div id='clickId'>click me</div>\n"
            + "<script>\n"
            + "  function handler(event) {\n"
            + "    if(!event) { alert('no event param'); return; };\n"
            + "    alert(event.currentTarget);\n"
            + "  }\n"
            + "  document.getElementById('clickId').onmousedown = handler;\n"
            + "  if(window.addEventListener) {\n"
            + "    window.addEventListener('mousedown', handler, true);\n"
            + "  } else {\n"
            + "    alert('no addEventListener');\n"
            + "  }\n"
            + "</script>\n"
            + "</body></html>";
        onClickPageTest(content);
    }

    /**
     * When adding a null event listener browsers react different.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "",
            IE8 = "true")
    public void testAddEventListener_HandlerNull() throws Exception {
        final String content
            = "<html><head></head><body>\n"
            + "<script>\n"
            + "try {\n"
            + "  if (window.addEventListener) {\n"
            + "    window.addEventListener('mousedown', null, true);\n"
            + "  }\n"
            + "  if (window.attachEvent) {\n"
            + "    alert(window.attachEvent('mousedown', null));\n"
            + "  }\n"
            + "} catch (err) {\n"
            + "  alert('error');\n"
            + "}\n"
            + "</script>\n"
            + "</body></html>";
        loadPageWithAlerts2(content);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = { "123a4a", "1a2a3ab4ab1ab2ab3abc4abc" },
            IE8 = { "124a", "1a2a4ab1ab2ab4abc" })
    public void testTyping_input() throws Exception {
        testTyping("<input type='text'", "");
        testTyping("<input type='password'", "");
        testTyping("<textarea", "</textarea>");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = { "123a4a", "1a2a3ab4ab1ab2ab3abc4abc" },
            IE8 = { "124a", "1a2a4ab1ab2ab4abc" })
    public void testTyping_textara() throws Exception {
        testTyping("<textarea", "</textarea>");
    }

    private void testTyping(final String opening, final String closing) throws Exception {
        final String html =
              "<html><body>\n"
            + "<script>var x = '';\n"
            + "function log(s) { x += s; }</script>\n"
            + "<form>\n"
            + opening + " id='t' onkeydown='log(1 + this.value)' "
            + "onkeypress='log(2 + this.value)' "
            + "oninput='log(3 + this.value)'"
            + "onkeyup='log(4 + this.value)'>" + closing
            + "</form>\n"
            + "<div id='d' onclick='alert(x); x=\"\"'>abc</div>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("t")).sendKeys("a");
        driver.findElement(By.id("d")).click();

        driver.findElement(By.id("t")).sendKeys("bc");
        driver.findElement(By.id("d")).click();

        assertEquals(getExpectedAlerts(), getCollectedAlerts(driver));
    }

    private void onClickPageTest(final String html) throws Exception {
        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("clickId")).click();

        assertEquals(getExpectedAlerts(), getCollectedAlerts(driver));
    }

    /**
     * Test that "this" refers to the element on which the event applies.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "frame1",
            CHROME = "")
    public void thisInEventHandler() throws Exception {
        final String html
            = "<html><head></head>\n"
            + "<body>\n"
            + "<button name='button1' id='button1' onclick='alert(this.name)'>1</button>\n"
            + "<iframe src='about:blank' name='frame1' id='frame1'></iframe>\n"
            + "<script>\n"
            + "document.getElementById('frame1').onload = document.getElementById('button1').onclick;\n"
            + "</script>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * Test that the event property of the window is available.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "false", "false" },
            FF = { "true", "exception" })
    public void testIEWindowEvent() throws Exception {
        final String html =
            "<html><head>\n"
            + "<script>\n"
            + "function test() {\n"
            + "  alert(window.event == null);\n"
            + "  try {\n"
            + "    alert(event == null);\n"
            + "  } catch(e) { alert('exception'); }\n"
            + "}\n"
            + "</script>\n"
            + "</head><body onload='test()'></body></html>";

        loadPageWithAlerts2(html);
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

        loadPageWithAlerts2(html);
    }

    /**
     * Test value for null event handler: null for IE, while 'undefined' for Firefox.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("null")
    public void testNullEventHandler() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var div = document.getElementById('myDiv');\n"
            + "    alert(div.onclick);\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "<div id='myDiv'/>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = { "[object Event]", "load", "false", "false" },
            FF = { "[object Event]", "load", "false", "true" },
            IE8 = { "[object]", "load", "undefined", "undefined" })
    public void onload() throws Exception {
        final String html =
              "<html><body onload='test(event)'><script>\n"
            + "    function test(e) {\n"
            + "        dump(e);\n"
            + "    }\n"
            + DUMP_EVENT_FUNCTION
            + "</script></body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = { "[object Event]", "number" },
            IE8 = { "[object]", "undefined" })
    public void testTimeStamp() throws Exception {
        final String html =
              "<html><body onload='test(event)'><script>\n"
            + "    function test(e) {\n"
            + "        alert(e);\n"
            + "        alert(typeof e.timeStamp);\n"
            + "    }\n"
            + "</script></body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = { "click", "true", "true", "click", "false", "false" },
            IE8 = "no createEvent")
    public void testInitEventClick() throws Exception {
        testInitEvent("click");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = { "dblclick", "true", "true", "dblclick", "false", "false" },
            IE8 = "no createEvent")
    public void testInitEventDblClick() throws Exception {
        testInitEvent("dblclick");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = { "unknown", "true", "true", "unknown", "false", "false" },
            IE8 = "no createEvent")
    public void testInitEventUnknown() throws Exception {
        testInitEvent("unknown");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = { "cASe", "true", "true", "cASe", "false", "false" },
            IE8 = "no createEvent")
    public void testInitEventCaseSensitive() throws Exception {
        testInitEvent("cASe");
    }

    private void testInitEvent(final String eventType) throws Exception {
        final String html =
              "<html><head><script>\n"
            + "  function test() {\n"
            + "    if(!document.createEvent) { alert('no createEvent'); return; };\n"

            + "    var e = document.createEvent('Event');\n"
            + "    try {\n"
            + "      e.initEvent('" + eventType + "', true, true);\n"
            + "      alert(e.type);\n"
            + "      alert(e.bubbles);\n"
            + "      alert(e.cancelable);\n"
            + "    } catch(e) { alert('e-' + '" + eventType + "'); }\n"

            + "    var e = document.createEvent('Event');\n"
            + "    try {\n"
            + "      e.initEvent('" + eventType + "', false, false);\n"
            + "      alert(e.type);\n"
            + "      alert(e.bubbles);\n"
            + "      alert(e.cancelable);\n"
            + "    } catch(e) { alert('e2-' + '" + eventType + "'); }\n"
            + "  }\n"
            + "</script></head>"
            + "<body onload='test()'></body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({ "true", "I was here" })
    public void firedEvent_equals_original_event() throws Exception {
        final String html =
            "<html><head><title>First</title>\n"
            + "<script>\n"
            + "function test() {\n"
            + "  var e = document.getElementById('myDiv');\n"
            + "  \n"
            + "  var myEvent;\n"
            + "  var listener = function(x) {\n"
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
            + "</head><body onload='test()'>\n"
            + "<div id='myDiv'>toti</div>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = { "e-0", "e-1", "e-2", "e-3", "e-4", "e-5",
                        "e-6", "e-7", "e-8", "e-9", "e-10", "e-11",
                        "e-12", "e-13", "e-14", "e-15", "e-16", "e-17", "e-18",
                        "e-19", "e-20", "e-21", "e-22", "e-23", "e-24",
                        "e-25", "e-26", "e-27", "e-28", "e-29", "e-30", "e-31", "e-32",
                         "e-33" },
            FF24 = { "400000", "1", "20000000", "2000", "8000", "40",
                     "2", "80", "800", "800000", "1000", "8000000",
                     "10000000", "100", "400", "200", "80000", "1000000", "8",
                     "1", "20", "10", "8", "4", "2",
                     "2000000", "10000", "4000000", "40000", "4000", "4", "20000", "100000",
                     "200000" },
            FF31 = { "e-0", "1", "e-2", "e-3", "e-4", "e-5",
                     "2", "e-7", "e-8", "e-9", "e-10", "e-11",
                     "e-12", "e-13", "e-14", "e-15", "e-16", "e-17", "8",
                     "e-19", "e-20", "e-21", "e-22", "e-23", "e-24",
                     "e-25", "e-26", "e-27", "e-28", "e-29", "4", "e-31", "e-32",
                     "e-33" },
            CHROME = { "e-0", "e-1", "e-2", "2000", "8000", "40",
                 "e-6", "80", "800", "e-9", "1000", "e-11",
                 "e-12", "100", "400", "200", "e-16", "e-17", "e-18",
                 "1", "20", "10", "8", "4", "2",
                 "e-25", "e-26", "e-27", "e-28", "4000", "e-30", "e-31", "e-32",
                 "e-33" },
            IE8 = "no Event")
    public void constants() throws Exception {
        final String html =
              "<html><body>\n"
            + "<script>\n"
            + "    if(!document.createEvent) { alert('no Event'); }\n"
            + "    else {\n"
            + "      var constants = [Event.ABORT, Event.ALT_MASK, Event.BACK, Event.BLUR, Event.CHANGE, Event.CLICK, "
            + "Event.CONTROL_MASK, Event.DBLCLICK, Event.DRAGDROP, Event.ERROR, Event.FOCUS, Event.FORWARD, "
            + "Event.HELP, Event.KEYDOWN, Event.KEYPRESS, Event.KEYUP, Event.LOAD, Event.LOCATE, Event.META_MASK, "
            + "Event.MOUSEDOWN, Event.MOUSEDRAG, Event.MOUSEMOVE, Event.MOUSEOUT, Event.MOUSEOVER, Event.MOUSEUP, "
            + "Event.MOVE, Event.RESET, Event.RESIZE, Event.SCROLL, Event.SELECT, Event.SHIFT_MASK, Event.SUBMIT, "
            + "Event.UNLOAD, Event.XFER_DONE];\n"
            + "      for (var x in constants) {\n"
            + "        try {\n"
            + "          alert(constants[x].toString(16));\n"
            + "        } catch(e) { alert('e-' + x); }\n"
            + "      }\n"
            + "    }\n"
            + "</script>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "exception", FF24 = "40000000")
    public void text() throws Exception {
        final String html =
              "<html><body onload='test(event)'><script>\n"
            + "  function test(e) {\n"
            + "    try {\n"
            + "      alert(e.TEXT.toString(16));\n"// But Event.TEXT is undefined!!!
            + "    } catch(e) { alert('exception'); }\n"
            + "  }\n"
            + "</script>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * Regression test for bug
     * <a href="http://sourceforge.net/p/htmlunit/bugs/898/">898</a>.
     * Name resolution doesn't work the same in inline handlers than in "normal" JS code!
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "form1 -> custom", "form2 -> [object HTMLFormElement]",
            "form1: [object HTMLFormElement]", "form2: [object HTMLFormElement]",
            "form1 -> custom", "form2 -> [object HTMLFormElement]" },
            IE8 = { "form1 -> custom", "form2 -> [object]",
            "form1: [object]", "form2: [object]",
            "form1 -> custom", "form2 -> [object]" })
    public void nameResolution() throws Exception {
        final String html = "<html><head><script>\n"
            + "var form1 = 'custom';\n"
            + "function testFunc() {\n"
            + " alert('form1 -> ' + form1);\n"
            + " alert('form2 -> ' + form2);\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='testFunc()'>\n"
            + "<form name='form1'></form>\n"
            + "<form name='form2'></form>\n"
            + "<button onclick=\"alert('form1: ' + form1); alert('form2: ' + form2); testFunc()\">click me</button>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.tagName("button")).click();

        assertEquals(getExpectedAlerts(), getCollectedAlerts(driver));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "activeElement BODY" },
            FF = { "activeElement BODY", "focus #document", "handler: activeElement BODY" },
            IE11 = { "activeElement BODY", "focus BODY", "handler: activeElement BODY" })
    @BuggyWebDriver(FF) // FFDriver doesn't behave like "manually driven" FF
    // http://code.google.com/p/selenium/issues/detail?id=4665
    @NotYetImplemented({ FF, IE11 })
    public void document_focus() throws Exception {
        final String html = "<html>\n"
                + "<head>\n"
                + "<script>\n"
                + "  function test() {\n"
                + "    handle(document);\n"
                + "    log('activeElement ' + document.activeElement.nodeName);\n"
                + "  }\n"
                + "  function handle(obj) {\n"
                + "    if (obj.addEventListener)\n"
                + "      obj.addEventListener('focus', handler, true);\n"
                + "    else\n"
                + "      obj.attachEvent('onfocus', handler);\n"
                + "  }\n"
                + "  function handler(e) {\n"
                + "    var src = e.srcElement;\n"
                + "    if (!src)\n"
                + "       src = e.target;\n"
                + "    log(e.type + ' ' + src.nodeName);\n"
                + "    log('handler: activeElement ' + document.activeElement.nodeName);\n"
                + "  }\n"
                + "  function log(x) {\n"
                + "      document.getElementById('log').value += x + '\\n';\n"
                + "  }\n"
                + "</script>\n"
                + "</head>\n"
                + "<body onload='test()'>\n"
                + "<textarea id='log' cols='80' rows='40'></textarea>\n"
                + "</body></html>";

        final WebDriver driver = loadPage2(html);
        final String text = driver.findElement(By.id("log")).getAttribute("value").trim().replaceAll("\r", "");
        assertEquals(StringUtils.join(getExpectedAlerts(), "\n"), text);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "focus INPUT", "focus INPUT" },
            IE8 = { })
    public void document_input_focus() throws Exception {
        document_input("focus");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "blur INPUT",
            IE8 = "",
            IE11 = { "blur BODY", "blur INPUT" })
    public void document_input_blur() throws Exception {
        document_input("blur");
    }

    private void document_input(final String event) throws Exception {
        final String html = "<html>\n"
                + "<head>\n"
                + "<script>\n"
                + "  function test() {\n"
                + "    handle(document);\n"
                + "  }\n"
                + "  function handle(obj) {\n"
                + "    if (obj.addEventListener)\n"
                + "      obj.addEventListener('" + event + "', handler, true);\n"
                + "    else\n"
                + "      obj.attachEvent('on" + event + "', handler);\n"
                + "  }\n"
                + "  function handler(e) {\n"
                + "    var src = e.srcElement;\n"
                + "    if (!src)\n"
                + "       src = e.target;\n"
                + "    log(e.type + ' ' + src.nodeName);\n"
                + "  }\n"
                + "  function log(x) {\n"
                + "    document.getElementById('log').value += x + '\\n';\n"
                + "  }\n"
                + "</script>\n"
                + "</head>\n"
                + "<body onload='test()'>\n"
                + "  <div id=\"div\">\n"
                + "    <input id=\"input1\" type=\"text\">\n"
                + "    <input id=\"input2\" type=\"text\">\n"
                + "  </div>\n"
                + "<textarea id='log' cols='80' rows='40'></textarea>\n"
                + "</body></html>";

        final WebDriver driver = loadPage2(html);
        final WebElement logElement = driver.findElement(By.id("log"));
        final String initialValue = logElement.getAttribute("value");
        driver.findElement(By.id("input1")).click();
        driver.findElement(By.id("input2")).click();
        final String addedValue = logElement.getAttribute("value").substring(initialValue.length());
        final String text = addedValue.trim().replaceAll("\r", "");
        assertEquals(StringUtils.join(getExpectedAlerts(), "\n"), text);
    }

    /**
     * Test that the parent scope of the event handler defined in HTML attributes is "document".
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "2from window", "1from document" },
            IE8 = { "1from document", "3from window" })
    public void eventHandlersParentScope() throws Exception {
        final String html = "<html><body>\n"
            + "<button name='button1' id='button1' onclick='alert(1 + foo)'>click me</button>\n"
            + "<script>\n"
            + "  if (window.addEventListener) {\n"
            + "    window.addEventListener('click', function() { alert(2 + foo); }, true);\n"
            + "  }\n"
            + "  else if (window.attachEvent) {\n"
            + "    window.attachEvent('onclick', function() { alert(3 + foo); });\n"
            + "  }\n"
            + "document.foo = 'from document';\n"
            + "var foo = 'from window';\n"
            + "</script>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("button1")).click();
        assertEquals(getExpectedAlerts(), getCollectedAlerts(driver));
    }

    /**
     * Test that the parent scopes chain for an event handler.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "from theField", "from theForm", "from document", "from window" })
    public void eventHandlersParentScopeChain_formFields() throws Exception {
        eventHandlersParentScopeChain("<button", "</button>");
        eventHandlersParentScopeChain("<select", "</select>");
        eventHandlersParentScopeChain("<textarea", "</textarea>");

        eventHandlersParentScopeChain("<input type='text'", "");
        eventHandlersParentScopeChain("<input type='password'", "");
        // IE11 cannot click on hidden fields
        // FF31 cannot click on hidden fields
        if (getBrowserVersion() != BrowserVersion.INTERNET_EXPLORER_11
                && getBrowserVersion() != BrowserVersion.FIREFOX_31
                && getBrowserVersion() != BrowserVersion.CHROME) {
            eventHandlersParentScopeChain("<input type='hidden'", "");
        }

        eventHandlersParentScopeChain("<input type='checkbox'", "");
        eventHandlersParentScopeChain("<input type='radio'", "");

        eventHandlersParentScopeChain("<input type='file'", "");
        eventHandlersParentScopeChain("<input type='image'", "");

        eventHandlersParentScopeChain("<input type='button'", "");

        eventHandlersParentScopeChain("<input type='submit' value='xxx'", "");
        // case without value attribute was failing first with IE due to the way the value attribute was added
        eventHandlersParentScopeChain("<input type='submit'", "");

        eventHandlersParentScopeChain("<input type='reset' value='xxx'", "");
        // case without value attribute was failing first with IE due to the way the value attribute was added
        eventHandlersParentScopeChain("<input type='reset'", "");
    }

    /**
     * Test that the parent scopes chain for an event handler.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "from theField", "from document", "from document", "from window" },
            CHROME = { "from theField", "from theForm", "from document", "from window" })
    public void eventHandlersParentScopeChain_span() throws Exception {
        eventHandlersParentScopeChain("<span", "</span>");
    }

    private void eventHandlersParentScopeChain(final String startTag, final String endTag) throws Exception {
        final String html = "<html><html>\n"
            + "<head><title>foo</title></head>\n"
            + "<body id='body'>\n"
            + "<form id='theForm'>\n"
            + "  <div id='theDiv'>\n"
            + "    " + startTag + " id='theField' onclick='alert(foo); return false;'>click me" + endTag + "\n"
            + "  </div>\n"
            + "</form>\n"
            + "<script>\n"
            + "  var foo = 'from window';\n"
            + "  document.foo = 'from document';\n"
            + "  document.body.foo = 'from body';\n"
            + "  document.getElementById('theForm').foo = 'from theForm';\n"
            + "  document.getElementById('theDiv').foo = 'from theDiv';\n"
            + "  document.getElementById('theField').foo = 'from theField';\n"
            + "</script>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        final WebElement field = driver.findElement(By.id("theField"));
        field.click();

        final JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;

        // remove property on field
        jsExecutor.executeScript("delete document.getElementById('theField').foo");
        field.click();

        // remove property on form
        jsExecutor.executeScript("delete document.getElementById('theForm').foo");
        field.click();

        // remove property on document
        jsExecutor.executeScript("delete document.foo");
        field.click();

        assertEquals(getExpectedAlerts(), getCollectedAlerts(driver));
    }

    /**
     * Test that the function open resolves to document.open within a handler defined by an attribute.
     * This was wrong (even in unit tests) up to HtmlUnit-2.12.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("from document")
    public void eventHandlers_functionOpen() throws Exception {
        final String html = "<html><body>\n"
            + "<button id='button1' onclick='identify(open)'>click me</button>\n"
            + "<script>\n"
            + "function identify(fnOpen) {\n"
            + "  var origin = 'unknown';\n"
            + "  if (fnOpen === window.open) {\n"
            + "    origin = 'from window';\n"
            + "  }\n"
            + "  else if (fnOpen === document.open) {\n"
            + "    origin = 'from document';\n"
            + "  }\n"
            + "  alert(origin);\n"
            + "}\n"
            + "</script>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("button1")).click();
        assertEquals(getExpectedAlerts(), getCollectedAlerts(driver));
    }
}
