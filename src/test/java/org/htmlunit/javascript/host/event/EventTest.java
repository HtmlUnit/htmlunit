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

import org.htmlunit.WebDriverTestCase;
import org.htmlunit.junit.annotation.Alerts;
import org.htmlunit.junit.annotation.HtmlUnitNYI;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * Tests that when DOM events such as "onclick" have access
 * to an {@link Event} object with context information.
 *
 * @author <a href="mailto:chriseldredge@comcast.net">Chris Eldredge</a>
 * @author Ahmed Ashour
 * @author Daniel Gredler
 * @author Marc Guillemot
 * @author Ronald Brill
 * @author Frank Danek
 */
public class EventTest extends WebDriverTestCase {

    private static final String DUMP_EVENT_FUNCTION = "  function dump(event) {\n"
        + "    log(event);\n"
        + "    log(event.type);\n"
        + "    log(event.bubbles);\n"
        + "    log(event.cancelable);\n"
        + "    log(event.composed);\n"
        + "  }\n";

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[object Event]", "event", "false", "false", "false"})
    public void create_ctor() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    try {\n"
            + "      var event = new Event('event');\n"
            + "      dump(event);\n"
            + "    } catch(e) { logEx(e) }\n"
            + "  }\n"
            + DUMP_EVENT_FUNCTION
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[object Event]", "event", "true", "false", "false"})
    public void create_ctorWithDetails() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    try {\n"
            + "      var event = new Event('event', {\n"
            + "        'bubbles': true\n"
            + "      });\n"
            + "      dump(event);\n"
            + "    } catch(e) { logEx(e) }\n"
            + "  }\n"
            + DUMP_EVENT_FUNCTION
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[object Event]", "event", "true", "false", "false"})
    public void create_ctorWithDetailsBoolAsString() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    try {\n"
            + "      var event = new Event('event', {\n"
            + "        'bubbles': 'true'\n"
            + "      });\n"
            + "      dump(event);\n"
            + "    } catch(e) { logEx(e) }\n"
            + "  }\n"
            + DUMP_EVENT_FUNCTION
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[object Event]", "event", "true", "false", "false"})
    public void create_ctorWithDetailsBoolAsNumber() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    try {\n"
            + "      var event = new Event('event', {\n"
            + "        'bubbles': 1\n"
            + "      });\n"
            + "      dump(event);\n"
            + "    } catch(e) { logEx(e) }\n"
            + "  }\n"
            + DUMP_EVENT_FUNCTION
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[object Event]", "event", "true", "false", "false"})
    public void create_ctorWithDetailsBoolAsObject() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    try {\n"
            + "      var event = new Event('event', {\n"
            + "        'bubbles': {}\n"
            + "      });\n"
            + "      dump(event);\n"
            + "    } catch(e) { logEx(e) }\n"
            + "  }\n"
            + DUMP_EVENT_FUNCTION
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[object Event]", "event", "false", "false", "false"})
    public void create_ctorWithDetailsBoolAsUndefined() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    try {\n"
            + "      var event = new Event('event', {\n"
            + "        'bubbles': undefined\n"
            + "      });\n"
            + "      dump(event);\n"
            + "    } catch(e) { logEx(e) }\n"
            + "  }\n"
            + DUMP_EVENT_FUNCTION
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[object Event]", "event", "false", "false", "false"})
    public void create_ctorWithDetailsBoolAsNull() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    try {\n"
            + "      var event = new Event('event', {\n"
            + "        'bubbles': null\n"
            + "      });\n"
            + "      dump(event);\n"
            + "    } catch(e) { logEx(e) }\n"
            + "  }\n"
            + DUMP_EVENT_FUNCTION
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[object Event]", "", "false", "false", "false"})
    public void create_createEvent() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    try {\n"
            + "      var event = document.createEvent('Event');\n"
            + "      dump(event);\n"
            + "    } catch(e) { logEx(e) }\n"
            + "  }\n"
            + DUMP_EVENT_FUNCTION
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"DOM2: [object Event]", "DOM3: [object Event]", "vendor: [object Event]"})
    public void create_createEventForDifferentTypes() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    try {\n"
            + "      log('DOM2: ' + document.createEvent('HTMLEvents'));\n"
            + "    } catch(e) {log('DOM2: exception')}\n"
            + "    try {\n"
            + "      log('DOM3: ' + document.createEvent('Event'));\n"
            + "    } catch(e) {log('DOM3: exception')}\n"
            + "    try {\n"
            + "      log('vendor: ' + document.createEvent('Events'));\n"
            + "    } catch(e) {log('vendor: exception')}\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[object Event]", "event", "true", "false", "false"})
    public void initEvent() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    try {\n"
            + "      var event = document.createEvent('Event');\n"
            + "      event.initEvent('event', true, false);\n"
            + "      dump(event);\n"
            + "    } catch(e) { logEx(e) }\n"
            + "  }\n"
            + DUMP_EVENT_FUNCTION
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Verify the "this" object refers to the Element being clicked when an
     * event handler is invoked.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("clickId")
    public void thisDefined() throws Exception {
        final String content = DOCTYPE_HTML
            + "<html><head></head><body>\n"
            + "<input type='button' id='clickId'/>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function handler(event) { log(this.getAttribute('id')); }\n"
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
    public void setPropOnThisDefined() throws Exception {
        final String content = DOCTYPE_HTML
            + "<html><head></head><body>\n"
            + "<input type='button' id='clickId'/>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function handler(event) { log(this.madeUpProperty); }\n"
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
    public void eventArgDefinedByWrapper() throws Exception {
        final String content = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "</script>\n"
            + "</head><body>\n"
            + "<input type='button' id='clickId' onclick=\"log(event ? 'defined' : 'undefined')\"/>\n"
            + "</body></html>";
        onClickPageTest(content);
    }

    /**
     * Verify that when event handler is invoked an argument is passed in.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("defined")
    public void eventArgDefined() throws Exception {
        final String content = DOCTYPE_HTML
            + "<html><head></head>\n"
            + "<body>\n"
            + "<input type='button' id='clickId'/>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function handler(event) { log(event ? 'defined' : 'undefined'); }\n"
            + "  document.getElementById('clickId').onclick = handler;\n"
            + "</script>\n"
            + "</body></html>";
        onClickPageTest(content);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("pass")
    public void eventTargetSameAsThis() throws Exception {
        final String content = DOCTYPE_HTML
            + "<html><head></head>\n"
            + "<body>\n"
            + "<input type='button' id='clickId'/>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function handler(event) {\n"
            + "    log(event.target == this ? 'pass' : event.target + '!=' + this);\n"
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
    @Alerts({"[object HTMLInputElement]", "true"})
    public void eventSrcElementSameAsThis() throws Exception {
        final String content = DOCTYPE_HTML
            + "<html><head></head><body>\n"
            + "<input type='button' id='clickId'/>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function handler(event) {\n"
            + "    event = event ? event : window.event;\n"
            + "    log(event.srcElement);\n"
            + "    log(event.srcElement == this);\n"
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
    @Alerts("pass")
    public void eventCurrentTargetSameAsThis() throws Exception {
        final String content = DOCTYPE_HTML
            + "<html><head></head>\n"
            + "<body>\n"
            + "<input type='button' id='clickId'/>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function handler(event) {\n"
            + "    log(event.currentTarget == this ? 'pass' : event.currentTarget + '!=' + this);\n"
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
    @Alerts({"[object Window]", "[object HTMLDivElement]"})
    public void currentTarget_sameListenerForEltAndWindow() throws Exception {
        final String content = DOCTYPE_HTML
            + "<html><head></head><body>\n"
            + "<div id='clickId'>click me</div>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function handler(event) {\n"
            + "    log(event.currentTarget);\n"
            + "  }\n"
            + "  document.getElementById('clickId').onmousedown = handler;\n"
            + "  window.addEventListener('mousedown', handler, true);\n"
            + "</script>\n"
            + "</body></html>";
        onClickPageTest(content);
    }

    /**
     * When adding a null event listener browsers react different.
     * @throws Exception if the test fails
     */
    @Test
    public void addEventListener_HandlerNull() throws Exception {
        final String content = DOCTYPE_HTML
            + "<html><head></head><body>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "try {\n"
            + "  window.addEventListener('mousedown', null, true);\n"
            + "} catch(e) { logEx(e); }\n"
            + "</script>\n"
            + "</body></html>";
        loadPageVerifyTitle2(content);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"123a4a", "1a2a3ab4ab1ab2ab3abc4abc"})
    public void typing_input_text() throws Exception {
        testTyping("<input type='text'", "");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"123a4a", "1a2a3ab4ab1ab2ab3abc4abc"})
    public void typing_input_password() throws Exception {
        testTyping("<input type='password'", "");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"123a4a", "1a2a3ab4ab1ab2ab3abc4abc"})
    public void typing_input_textarea() throws Exception {
        testTyping("<textarea", "</textarea>");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"123a4a", "1a2a3ab4ab1ab2ab3abc4abc"})
    public void typing_input_tel() throws Exception {
        testTyping("<input type='tel'", "");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"123a4a", "1a2a3ab4ab1ab2ab3abc4abc"})
    public void typing_input_search() throws Exception {
        testTyping("<input type='search'", "");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"124", "124124"},
            FF = {"1234", "12341234"},
            FF_ESR = {"1234", "12341234"})
    @HtmlUnitNYI(CHROME = {"1234", "12341234"},
            EDGE = {"1234", "12341234"})
    public void typing_input_number() throws Exception {
        testTyping("<input type='number'", "");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"123a4a", "1a2a3ab4ab1ab2ab3abc4abc"})
    public void typing_textara() throws Exception {
        testTyping("<textarea", "</textarea>");
    }

    private void testTyping(final String opening, final String closing) throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "var x = '';\n"
            + "function msg(s) { x += s; }</script>\n"
            + "<form>\n"
            + opening + " id='t' onkeydown='msg(1 + this.value)' "
            + "onkeypress='msg(2 + this.value)' "
            + "oninput='msg(3 + this.value)'"
            + "onkeyup='msg(4 + this.value)'>" + closing
            + "</form>\n"
            + "<div id='d' onclick='log(x); x=\"\"'>abc</div>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("t")).sendKeys("a");
        driver.findElement(By.id("d")).click();
        verifyTitle2(driver, getExpectedAlerts()[0]);

        driver.findElement(By.id("t")).sendKeys("bc");
        driver.findElement(By.id("d")).click();
        verifyTitle2(driver, getExpectedAlerts()[0], getExpectedAlerts()[1]);
    }

    private void onClickPageTest(final String html) throws Exception {
        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("clickId")).click();

        verifyTitle2(driver, getExpectedAlerts());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("frame1")
    public void thisInEventHandler() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "</script>\n"
            + "</head>\n"
            + "<body>\n"
            + "  <button name='button1' id='button1' onclick='log(this.name)'>1</button>\n"
            + "  <iframe src='default' name='frame1' id='frame1'></iframe>\n"
            + "  <script>\n"
            + "    var e = document.getElementById('frame1');\n"
            + "    e.onload = document.getElementById('button1').onclick;\n"
            + "  </script>\n"
            + "</body></html>";

        getMockWebConnection().setDefaultResponse("<html><body></body></html>");
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("called")
    public void iframeOnload() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    log('called');\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body>\n"
            + "<iframe src='default' name='frame1' id='frame1'></iframe>\n"
            + "<script>\n"
            + "  var e = document.getElementById('frame1');\n"
            + "  e.onload = test;\n"
            + "</script>\n"
            + "</body></html>";

        getMockWebConnection().setDefaultResponse("<html><body></body></html>");
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"inline", "null"})
    public void iframeOnload2() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<body>\n"
            + "<iframe src='about:blank' name='frame1' id='frame1'></iframe>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  var e = document.getElementById('frame1');\n"
            + "  e.onload = log('inline');\n"
            + "  log(e.onload);\n"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Test that the event property of the window is available.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"false", "false"})
    public void ieWindowEvent() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  log(window.event == null);\n"
            + "  try {\n"
            + "    log(event == null);\n"
            + "  } catch(e) { logEx(e); }\n"
            + "}\n"
            + "</script>\n"
            + "</head><body onload='test()'></body></html>";

        loadPageVerifyTitle2(html);
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
    @Alerts({"1", "2"})
    public void commentInEventHandlerDeclaration() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "</script>\n"
            + "</head>\n"
            + "<body onload='log(1);\n"
            + "// a comment within the onload declaration\n"
            + "log(2)'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Test value for null event handler: null for IE, while 'undefined' for Firefox.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("null")
    public void nullEventHandler() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var div = document.getElementById('myDiv');\n"
            + "    log(div.onclick);\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "<div id='myDiv'/>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"[object Event]", "load", "false", "false", "false"})
    public void onload() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body onload='test(event)'>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "    function test(e) {\n"
            + "      dump(e);\n"
            + "    }\n"
            + DUMP_EVENT_FUNCTION
            + "</script></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"[object Event]", "number"})
    public void timeStamp() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body onload='test(event)'>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test(e) {\n"
            + "    log(e);\n"
            + "    log(typeof e.timeStamp);\n"
            + "  }\n"
            + "</script></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"click", "true", "true", "click", "false", "false"})
    public void initEventClick() throws Exception {
        testInitEvent("click");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"dblclick", "true", "true", "dblclick", "false", "false"})
    public void initEventDblClick() throws Exception {
        testInitEvent("dblclick");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"unknown", "true", "true", "unknown", "false", "false"})
    public void initEventUnknown() throws Exception {
        testInitEvent("unknown");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"cASe", "true", "true", "cASe", "false", "false"})
    public void initEventCaseSensitive() throws Exception {
        testInitEvent("cASe");
    }

    private void testInitEvent(final String eventType) throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var e = document.createEvent('Event');\n"
            + "    try {\n"
            + "      e.initEvent('" + eventType + "', true, true);\n"
            + "      log(e.type);\n"
            + "      log(e.bubbles);\n"
            + "      log(e.cancelable);\n"
            + "    } catch(e) { log('e-' + '" + eventType + "'); logEx(e); }\n"

            + "    var e = document.createEvent('Event');\n"
            + "    try {\n"
            + "      e.initEvent('" + eventType + "', false, false);\n"
            + "      log(e.type);\n"
            + "      log(e.bubbles);\n"
            + "      log(e.cancelable);\n"
            + "    } catch(e) { log('e2-' + '" + eventType + "'); logEx(e); }\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='test()'></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"true", "I was here"})
    public void firedEvent_equals_original_event() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  var e = document.getElementById('myDiv');\n"
            + "  \n"
            + "  var myEvent;\n"
            + "  var listener = function(x) {\n"
            + "    log(x == myEvent);\n"
            + "    x.foo = 'I was here';\n"
            + "  }\n"
            + "  \n"
            + "  e.addEventListener('click', listener, false);\n"
            + "  myEvent = document.createEvent('HTMLEvents');\n"
            + "  myEvent.initEvent('click', true, true);\n"
            + "  e.dispatchEvent(myEvent);\n"
            + "  log(myEvent.foo);\n"
            + "}\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "<div id='myDiv'>toti</div>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"e-0", "e-1", "e-2", "e-3", "e-4", "e-5",
                       "e-6", "e-7", "e-8", "e-9", "e-10", "e-11",
                       "e-12", "e-13", "e-14", "e-15", "e-16", "e-17", "e-18",
                       "e-19", "e-20", "e-21", "e-22", "e-23", "e-24",
                       "e-25", "e-26", "e-27", "e-28", "e-29", "e-30", "e-31", "e-32",
                       "e-33"},
            FF = {"e-0", "1", "e-2", "e-3", "e-4", "e-5",
                  "2", "e-7", "e-8", "e-9", "e-10", "e-11",
                  "e-12", "e-13", "e-14", "e-15", "e-16", "e-17", "8",
                  "e-19", "e-20", "e-21", "e-22", "e-23", "e-24",
                  "e-25", "e-26", "e-27", "e-28", "e-29", "4", "e-31", "e-32",
                  "e-33"},
            FF_ESR = {"e-0", "1", "e-2", "e-3", "e-4", "e-5",
                      "2", "e-7", "e-8", "e-9", "e-10", "e-11",
                      "e-12", "e-13", "e-14", "e-15", "e-16", "e-17", "8",
                      "e-19", "e-20", "e-21", "e-22", "e-23", "e-24",
                      "e-25", "e-26", "e-27", "e-28", "e-29", "4", "e-31", "e-32",
                      "e-33"})
    public void constants() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  var constants = [Event.ABORT, Event.ALT_MASK, Event.BACK, Event.BLUR, Event.CHANGE, Event.CLICK, "
            + "Event.CONTROL_MASK, Event.DBLCLICK, Event.DRAGDROP, Event.ERROR, Event.FOCUS, Event.FORWARD, "
            + "Event.HELP, Event.KEYDOWN, Event.KEYPRESS, Event.KEYUP, Event.LOAD, Event.LOCATE, Event.META_MASK, "
            + "Event.MOUSEDOWN, Event.MOUSEDRAG, Event.MOUSEMOVE, Event.MOUSEOUT, Event.MOUSEOVER, Event.MOUSEUP, "
            + "Event.MOVE, Event.RESET, Event.RESIZE, Event.SCROLL, Event.SELECT, Event.SHIFT_MASK, Event.SUBMIT, "
            + "Event.UNLOAD, Event.XFER_DONE];\n"
            + "  for (var x in constants) {\n"
            + "    try {\n"
            + "      log(constants[x].toString(16));\n"
            + "    } catch(e) { log('e-' + x); }\n"
            + "  }\n"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("TypeError")
    public void text() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body onload='test(event)'><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test(e) {\n"
            + "    try {\n"
            + "      log(e.TEXT.toString(16));\n"// But Event.TEXT is undefined!!!
            + "    } catch(e) { logEx(e); }\n"
            + "  }\n"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Regression test for bug
     * <a href="http://sourceforge.net/p/htmlunit/bugs/898/">898</a>.
     * Name resolution doesn't work the same in inline handlers than in "normal" JS code!
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"form1 -> custom", "form2 -> [object HTMLFormElement]",
             "form1: [object HTMLFormElement]", "form2: [object HTMLFormElement]",
             "form1 -> custom", "form2 -> [object HTMLFormElement]"})
    public void nameResolution() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "var form1 = 'custom';\n"
            + "function testFunc() {\n"
            + "  log('form1 -> ' + form1);\n"
            + "  log('form2 -> ' + form2);\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='testFunc()'>\n"
            + "<form name='form1'></form>\n"
            + "<form name='form2'></form>\n"
            + "<button onclick=\"log('form1: ' + form1); log('form2: ' + form2); testFunc()\">click me</button>\n"
            + "</body></html>";

        final String[] alerts = getExpectedAlerts();
        int i = 0;

        final WebDriver driver = loadPage2(html);
        verifyTitle2(driver, alerts[i++], alerts[i++]);

        i = 0;
        driver.findElement(By.tagName("button")).click();
        verifyTitle2(driver, alerts[i++], alerts[i++], alerts[i++], alerts[i++], alerts[i++], alerts[i++]);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "activeElement BODY",
            FF = {"activeElement BODY", "focus #document", "handler: activeElement BODY"},
            FF_ESR = {"activeElement BODY", "focus #document", "handler: activeElement BODY"})
    // http://code.google.com/p/selenium/issues/detail?id=4665
    @HtmlUnitNYI(FF = {"activeElement BODY", "focus undefined", "handler: activeElement BODY"},
            FF_ESR = {"activeElement BODY", "focus undefined", "handler: activeElement BODY"})
    public void document_focus() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<head>\n"
                + "<script>\n"
                + LOG_TEXTAREA_FUNCTION
                + "  function test() {\n"
                + "    handle(document);\n"
                + "    log('activeElement ' + document.activeElement.nodeName);\n"
                + "  }\n"
                + "  function handle(obj) {\n"
                + "    obj.addEventListener('focus', handler, true);\n"
                + "  }\n"
                + "  function handler(e) {\n"
                + "    var src = e.srcElement;\n"
                + "    if (!src)\n"
                + "      src = e.target;\n"
                + "    log(e.type + ' ' + src.nodeName);\n"
                + "    log('handler: activeElement ' + document.activeElement.nodeName);\n"
                + "  }\n"
                + "</script>\n"
                + "</head>\n"
                + "<body onload='test()'>\n"
                + LOG_TEXTAREA
                + "</body></html>";

        loadPageVerifyTextArea2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"focus INPUT", "focus INPUT"})
    @HtmlUnitNYI(FF = {"focus undefined", "focus INPUT", "focus INPUT"},
            FF_ESR = {"focus undefined", "focus INPUT", "focus INPUT"})
    public void document_input_focus() throws Exception {
        document_input("focus");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("blur INPUT")
    public void document_input_blur() throws Exception {
        document_input("blur");
    }

    private void document_input(final String event) throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<head>\n"
                + "<script>\n"
                + LOG_TEXTAREA_FUNCTION
                + "  function test() {\n"
                + "    handle(document);\n"
                + "  }\n"
                + "  function handle(obj) {\n"
                + "    obj.addEventListener('" + event + "', handler, true);\n"
                + "  }\n"
                + "  function handler(e) {\n"
                + "    var src = e.srcElement;\n"
                + "    if (!src)\n"
                + "      src = e.target;\n"
                + "    log(e.type + ' ' + src.nodeName);\n"
                + "  }\n"
                + "</script>\n"
                + "</head>\n"
                + "<body onload='test()'>\n"
                + "  <div id=\"div\">\n"
                + "    <input id=\"input1\" type=\"text\">\n"
                + "    <input id=\"input2\" type=\"text\">\n"
                + "  </div>\n"
                + LOG_TEXTAREA
                + "</body></html>";

        final WebDriver driver = loadPage2(html);

        driver.findElement(By.id("input1")).click();
        driver.findElement(By.id("input2")).click();

        verifyTextArea2(driver, getExpectedAlerts());
    }

    /**
     * Test that the parent scope of the event handler defined in HTML attributes is "document".
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"2from window", "1from document"})
    public void eventHandlersParentScope() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "</script>\n"
            + "</head>\n"
            + "<body>\n"
            + "<button name='button1' id='button1' onclick='log(1 + foo)'>click me</button>\n"
            + "<script>\n"
            + "  window.addEventListener('click', function() { log(2 + foo); }, true);\n"
            + "  document.foo = 'from document';\n"
            + "  var foo = 'from window';\n"
            + "</script>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("button1")).click();

        verifyTitle2(driver, getExpectedAlerts());
    }

    /**
     * Test that the parent scopes chain for an event handler.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"from theField", "from theForm", "from document", "from window"})
    public void eventHandlersParentScopeChain_formFields() throws Exception {
        eventHandlersParentScopeChain("<button", "</button>");
        eventHandlersParentScopeChain("<select", "</select>");
        eventHandlersParentScopeChain("<textarea", "</textarea>");

        eventHandlersParentScopeChain("<input type='text'", "");
        eventHandlersParentScopeChain("<input type='password'", "");

        eventHandlersParentScopeChain("<input type='checkbox'", "");
        eventHandlersParentScopeChain("<input type='radio'", "");

        // eventHandlersParentScopeChain("<input type='file'", "");
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
    @Alerts({"from theField", "from document", "from document", "from window"})
    public void eventHandlersParentScopeChain_span() throws Exception {
        eventHandlersParentScopeChain("<span", "</span>");
    }

    private void eventHandlersParentScopeChain(final String startTag, final String endTag) throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "</script>\n"
            + "</head>\n"
            + "<body id='body'>\n"
            + "<form id='theForm'>\n"
            + "  <div id='theDiv'>\n"
            + "    " + startTag + " id='theField' onclick='log(foo); return false;'>click me" + endTag + "\n"
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

        final String[] alerts = getExpectedAlerts();
        final WebDriver driver = loadPage2(html);
        final WebElement field = driver.findElement(By.id("theField"));
        field.click();
        verifyTitle2(driver, alerts[0]);

        final JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;

        // remove property on field
        jsExecutor.executeScript("delete document.getElementById('theField').foo");
        field.click();
        verifyTitle2(driver, alerts[0], alerts[1]);

        // remove property on form
        jsExecutor.executeScript("delete document.getElementById('theForm').foo");
        field.click();
        verifyTitle2(driver, alerts[0], alerts[1], alerts[2]);

        // remove property on document
        jsExecutor.executeScript("delete document.foo");
        field.click();
        verifyTitle2(driver, alerts[0], alerts[1], alerts[2], alerts[3]);
    }

    /**
     * Test that the function open resolves to document.open within a handler defined by an attribute.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("from document")
    public void eventHandlers_functionOpen() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body>\n"
            + "<button id='button1' onclick='identify(open)'>click me</button>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function identify(fnOpen) {\n"
            + "  var origin = 'unknown';\n"
            + "  if (fnOpen === window.open) {\n"
            + "    origin = 'from window';\n"
            + "  }\n"
            + "  else if (fnOpen === document.open) {\n"
            + "    origin = 'from document';\n"
            + "  }\n"
            + "  log(origin);\n"
            + "}\n"
            + "</script>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("button1")).click();

        verifyTitle2(driver, getExpectedAlerts());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"false", "boolean"})
    public void defaultPrevented() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    try {\n"
            + "      var event = document.createEvent('Event');\n"
            + "      log(event.defaultPrevented);\n"
            + "      log(typeof event.defaultPrevented);\n"
            + "    } catch(e) { logEx(e) }\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"true", "boolean"})
    public void returnValue() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    try {\n"
            + "      var event = document.createEvent('Event');\n"
            + "      log(event.returnValue);\n"
            + "      log(typeof event.returnValue);\n"
            + "    } catch(e) { logEx(e) }\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"true", "boolean", "false - false",
             "true", "true - false",
             "false", "boolean",
             "true", "boolean", "false - false",
             "true", "boolean"})
    public void returnValueSetter() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    try {\n"
            + "      var event = document.createEvent('Event');\n"
            + "      log(event.returnValue);\n"
            + "      log(typeof event.returnValue);\n"
            + "      log(event.cancelable + ' - ' + event.defaultPrevented);\n"

            + "      event.initEvent('click', 'true', 'true');\n"
            + "      log(event.returnValue);\n"
            + "      log(event.cancelable + ' - ' + event.defaultPrevented);\n"

            + "      event.preventDefault();\n"
            + "      log(event.returnValue);\n"
            + "      log(typeof event.returnValue);\n"

            + "      event = document.createEvent('Event');\n"
            + "      log(event.returnValue);\n"
            + "      log(typeof event.returnValue);\n"
            + "      log(event.cancelable + ' - ' + event.defaultPrevented);\n"

            + "      event.preventDefault();\n"
            + "      log(event.returnValue);\n"
            + "      log(typeof event.returnValue);\n"
            + "    } catch(e) { logEx(e) }\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"true", "boolean", "false - false",
             "true", "true - false",
             "false", "boolean", "false",
             "true", "boolean", "false - false",
             "true", "boolean", "true",
             "true", "boolean", "true - false",
             "false", "boolean", "false"})
    public void returnValueSetterFalse() throws Exception {
        returnValueSetterUndefined("false");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"true", "boolean", "false - false",
             "true", "true - false",
             "true", "boolean", "false",
             "true", "boolean", "false - false",
             "true", "boolean", "true",
             "true", "boolean", "true - false",
             "true", "boolean", "false"})
    public void returnValueSetterTrue() throws Exception {
        returnValueSetterUndefined("true");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"true", "boolean", "false - false",
             "true", "true - false",
             "true", "boolean", "false",
             "true", "boolean", "false - false",
             "true", "boolean", "true",
             "true", "boolean", "true - false",
             "true", "boolean", "false"})
    public void returnValueSetterString() throws Exception {
        returnValueSetterUndefined("'test'");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"true", "boolean", "false - false",
             "true", "true - false",
             "false", "boolean", "false",
             "true", "boolean", "false - false",
             "true", "boolean", "true",
             "true", "boolean", "true - false",
             "false", "boolean", "false"})
    public void returnValueSetterZero() throws Exception {
        returnValueSetterUndefined("0");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"true", "boolean", "false - false",
             "true", "true - false",
             "true", "boolean", "false",
             "true", "boolean", "false - false",
             "true", "boolean", "true",
             "true", "boolean", "true - false",
             "true", "boolean", "false"})
    public void returnValueSetterOne() throws Exception {
        returnValueSetterUndefined("1");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"true", "boolean", "false - false",
             "true", "true - false",
             "true", "boolean", "false",
             "true", "boolean", "false - false",
             "true", "boolean", "true",
             "true", "boolean", "true - false",
             "true", "boolean", "false"})
    public void returnValueSetterMinusOne() throws Exception {
        returnValueSetterUndefined("-1");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"true", "boolean", "false - false",
             "true", "true - false",
             "false", "boolean", "false",
             "true", "boolean", "false - false",
             "true", "boolean", "true",
             "true", "boolean", "true - false",
             "false", "boolean", "false"})
    public void returnValueSetterUndefined() throws Exception {
        returnValueSetterUndefined("undefined");
    }

    private void returnValueSetterUndefined(final String value) throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "  <head></head>\n"
            + "  <body onload='test()'>\n"
            + "    <div><a id='triggerClick' href='#'>click event</a></div>\n"

            + "    <script>\n"
            + LOG_TITLE_FUNCTION

            + "      function test() {\n"
            + "        try {\n"
            + "          var event = document.createEvent('Event');\n"
            + "          log(event.returnValue);\n"
            + "          log(typeof event.returnValue);\n"
            + "          log(event.cancelable + ' - ' + event.defaultPrevented);\n"

            + "          event.initEvent('click', 'true', 'true');\n"
            + "          log(event.returnValue);\n"
            + "          log(event.cancelable + ' - ' + event.defaultPrevented);\n"

            + "          event.returnValue = " + value + ";\n"
            + "          log(event.returnValue);\n"
            + "          log(typeof event.returnValue);\n"

            + "          event.returnValue = !event.returnValue;\n"
            + "          log(event.returnValue);\n"

            + "          event = document.createEvent('Event');\n"
            + "          log(event.returnValue);\n"
            + "          log(typeof event.returnValue);\n"
            + "          log(event.cancelable + ' - ' + event.defaultPrevented);\n"

            + "          event.returnValue = " + value + ";\n"
            + "          log(event.returnValue);\n"
            + "          log(typeof event.returnValue);\n"

            + "          event.returnValue = !event.returnValue;\n"
            + "          log(event.returnValue);\n"
            + "        } catch(e) { logEx(e) }\n"
            + "      }\n"

            + "      triggerClick.addEventListener('click', function (event) {\n"
            + "          log(event.returnValue);\n"
            + "          log(typeof event.returnValue);\n"
            + "          log(event.cancelable + ' - ' + event.defaultPrevented);\n"

            + "          event.returnValue = " + value + ";\n"
            + "          log(event.returnValue);\n"
            + "          log(typeof event.returnValue);\n"

            + "          event.returnValue = !event.returnValue;\n"
            + "          log(event.returnValue);\n"
            + "        });\n"
            + "    </script>\n"
            + "  </body>\n"
            + "</html>";

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("triggerClick")).click();

        verifyTitle2(driver, getExpectedAlerts());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"false - false", "true - false", "true - true",
             "false - false", "false - false", "false - false",
             "false - false", "true - false"})
    public void preventDefault() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + LOG_TITLE_FUNCTION

            + "      function test() {\n"
            + "        try {\n"
            + "          var event = document.createEvent('Event');\n"
            + "          log(event.cancelable + ' - ' + event.defaultPrevented);\n"

            + "          event.initEvent('click', 'true', 'true');\n"
            + "          log(event.cancelable + ' - ' + event.defaultPrevented);\n"

            + "          event.preventDefault();\n"
            + "          log(event.cancelable + ' - ' + event.defaultPrevented);\n"

            + "          event = document.createEvent('Event');\n"
            + "          log(event.cancelable + ' - ' + event.defaultPrevented);\n"

            + "          event.preventDefault();\n"
            + "          log(event.cancelable + ' - ' + event.defaultPrevented);\n"

            + "          event = document.createEvent('Event');\n"
            + "          log(event.cancelable + ' - ' + event.defaultPrevented);\n"

            + "          event.preventDefault();\n"
            + "          log(event.cancelable + ' - ' + event.defaultPrevented);\n"

            + "          event.initEvent('click', 'true', 'true');\n"
            + "          log(event.cancelable + ' - ' + event.defaultPrevented);\n"
            + "        } catch(e) { logEx(e) }\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='test()'>\n"
            + "  </body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("OK")
    public void domEventNameUsedAsFunctionName() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function onclick() {\n"
            + "  log('OK');\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='onclick()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"[object Event]", "scroll", "false", "false", "false"},
            FF = {"[object UIEvent]", "scroll", "false", "true", "false"},
            FF_ESR = {"[object UIEvent]", "scroll", "false", "true", "false"})
    public void scrollEventFromScrollIntoView() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + DUMP_EVENT_FUNCTION
            + "</script>\n"
            + "</head>\n"

            + "<body>\n"
            + "  <div id='container' style='overflow-y: scroll; height: 100px;'>\n"
            + "    <div style='height: 1000px;'>spacer</div>\n"
            + "    <div id='target' style='background: red;'>Target</div>\n"
            + "  </div>\n"

            + "  <script>\n"
            + "    var c = document.getElementById('container');\n"
            + "    c.addEventListener('scroll', function(e) { dump(e); });\n"

            + "    var s = document.getElementById('target');"
            + "    s.scrollIntoView();\n"
            + "  </script>\n"
            + "</body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"[object Event]", "scroll", "true", "false", "false"},
            FF = {"[object UIEvent]", "scroll", "true", "true", "false"},
            FF_ESR = {"[object UIEvent]", "scroll", "true", "true", "false"})
    public void scrollEventWindowFromScrollIntoView() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + DUMP_EVENT_FUNCTION
            + "</script>\n"
            + "</head>\n"

            + "<body>\n"
            + "  <div id='container' style='height: 10000px;'>\n"
            + "  </div>\n"
            + "  <div id='target' style='background: red;'>Target</div>\n"

            + "  <script>\n"
            + "    window.addEventListener('scroll', function(e) { dump(e); });\n"

            + "    var s = document.getElementById('target');"
            + "    s.scrollIntoView();\n"
            + "  </script>\n"
            + "</body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"[object Event]", "scroll", "true", "false", "false"},
            FF = {"[object UIEvent]", "scroll", "true", "true", "false"},
            FF_ESR = {"[object UIEvent]", "scroll", "true", "true", "false"})
    public void scrollEventFromScrollBy() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + DUMP_EVENT_FUNCTION
            + "</script>\n"
            + "</head>\n"

            + "<body>\n"
            + "  <div style='height: 1000px;'></div>\n"

            + "  <script>\n"
            + "    document.addEventListener('scroll', function(e) { dump(e) });\n"
            + "    window.scrollBy(10, 20);\n"
            + "  </script>\n"
            + "</body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"[object Event]", "scroll", "true", "false", "false"},
            FF = {"[object UIEvent]", "scroll", "true", "true", "false"},
            FF_ESR = {"[object UIEvent]", "scroll", "true", "true", "false"})
    public void scrollEventWindowFromScrollBy() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + DUMP_EVENT_FUNCTION
            + "</script>\n"
            + "</head>\n"

            + "<body>\n"
            + "  <div style='height: 1000px;'></div>\n"

            + "  <script>\n"
            + "    window.addEventListener('scroll', function(e) { dump(e) });\n"
            + "    window.scrollBy(10, 20);\n"
            + "  </script>\n"
            + "</body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"[object Event]", "scroll", "true", "false", "false"},
            FF = {"[object UIEvent]", "scroll", "true", "true", "false"},
            FF_ESR = {"[object UIEvent]", "scroll", "true", "true", "false"})
    public void scrollEventFromScrollTo() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + DUMP_EVENT_FUNCTION
            + "</script>\n"
            + "</head>\n"

            + "<body>\n"
            + "  <div style='height: 1000px;'></div>\n"

            + "  <script>\n"
            + "    document.addEventListener('scroll', function(e) { dump(e) });\n"
            + "    window.scrollTo(10, 20);\n"
            + "  </script>\n"
            + "</body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"[object Event]", "scroll", "true", "false", "false"},
            FF = {"[object UIEvent]", "scroll", "true", "true", "false"},
            FF_ESR = {"[object UIEvent]", "scroll", "true", "true", "false"})
    public void scrollEventWindowFromScrollTo() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + DUMP_EVENT_FUNCTION
            + "</script>\n"
            + "</head>\n"

            + "<body>\n"
            + "  <div style='height: 1000px;'></div>\n"

            + "  <script>\n"
            + "    window.addEventListener('scroll', function(e) { dump(e) });\n"
            + "    window.scrollTo(10, 20);\n"
            + "  </script>\n"
            + "</body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }
}
