/*
 * Copyright (c) 2002-2021 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript.host.event;

import static com.gargoylesoftware.htmlunit.BrowserRunner.TestedBrowser.FF;
import static com.gargoylesoftware.htmlunit.BrowserRunner.TestedBrowser.FF78;
import static com.gargoylesoftware.htmlunit.BrowserRunner.TestedBrowser.IE;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.html.HtmlPageTest;

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
@RunWith(BrowserRunner.class)
public class EventTest extends WebDriverTestCase {

    private static final String DUMP_EVENT_FUNCTION = "  function dump(event) {\n"
        + "    alert(event);\n"
        + "    alert(event.type);\n"
        + "    alert(event.bubbles);\n"
        + "    alert(event.cancelable);\n"
        + "    alert(event.composed);\n"
        + "  }\n";

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"[object Event]", "event", "false", "false", "false"},
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
    @Alerts(DEFAULT = {"[object Event]", "event", "true", "false", "false"},
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
    @Alerts(DEFAULT = {"[object Event]", "event", "true", "false", "false"},
            IE = "exception")
    public void create_ctorWithDetailsBoolAsString() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    try {\n"
            + "      var event = new Event('event', {\n"
            + "        'bubbles': 'true'\n"
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
    @Alerts(DEFAULT = {"[object Event]", "event", "true", "false", "false"},
            IE = "exception")
    public void create_ctorWithDetailsBoolAsNumber() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    try {\n"
            + "      var event = new Event('event', {\n"
            + "        'bubbles': 1\n"
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
    @Alerts(DEFAULT = {"[object Event]", "event", "true", "false", "false"},
            IE = "exception")
    public void create_ctorWithDetailsBoolAsObject() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    try {\n"
            + "      var event = new Event('event', {\n"
            + "        'bubbles': {}\n"
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
    @Alerts(DEFAULT = {"[object Event]", "event", "false", "false", "false"},
            IE = "exception")
    public void create_ctorWithDetailsBoolAsUndefined() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    try {\n"
            + "      var event = new Event('event', {\n"
            + "        'bubbles': undefined\n"
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
    @Alerts(DEFAULT = {"[object Event]", "event", "false", "false", "false"},
            IE = "exception")
    public void create_ctorWithDetailsBoolAsNull() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    try {\n"
            + "      var event = new Event('event', {\n"
            + "        'bubbles': null\n"
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
    @Alerts(DEFAULT = {"[object Event]", "", "false", "false", "false"},
            IE = {"[object Event]", "", "false", "false", "undefined"})
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
    @Alerts({"DOM2: [object Event]", "DOM3: [object Event]", "vendor: [object Event]"})
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
    @Alerts(DEFAULT = {"[object Event]", "event", "true", "false", "false"},
            IE = {"[object Event]", "event", "true", "false", "undefined"})
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
    public void thisDefined() throws Exception {
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
    public void setPropOnThisDefined() throws Exception {
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
    public void eventArgDefinedByWrapper() throws Exception {
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
    public void eventArgDefined() throws Exception {
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
    @Alerts("pass")
    public void eventTargetSameAsThis() throws Exception {
        final String content
            = "<html><head></head>\n"
            + "<body>\n"
            + "<input type='button' id='clickId'/>\n"
            + "<script>\n"
            + "  function handler(event) {\n"
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
    @Alerts({"[object HTMLInputElement]", "true"})
    public void eventSrcElementSameAsThis() throws Exception {
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
    @Alerts("pass")
    public void eventCurrentTargetSameAsThis() throws Exception {
        final String content
            = "<html><head></head>\n"
            + "<body>\n"
            + "<input type='button' id='clickId'/>\n"
            + "<script>\n"
            + "  function handler(event) {\n"
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
    @Alerts({"[object Window]", "[object HTMLDivElement]"})
    public void currentTarget_sameListenerForEltAndWindow() throws Exception {
        final String content
            = "<html><head></head><body>\n"
            + "<div id='clickId'>click me</div>\n"
            + "<script>\n"
            + "  function handler(event) {\n"
            + "    alert(event.currentTarget);\n"
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
        final String content
            = "<html><head></head><body>\n"
            + "<script>\n"
            + "try {\n"
            + "  window.addEventListener('mousedown', null, true);\n"
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
    @Alerts({"123a4a", "1a2a3ab4ab1ab2ab3abc4abc"})
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
        verifyAlerts(driver, getExpectedAlerts()[0]);

        driver.findElement(By.id("t")).sendKeys("bc");
        driver.findElement(By.id("d")).click();
        verifyAlerts(driver, getExpectedAlerts()[1]);
    }

    private void onClickPageTest(final String html) throws Exception {
        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("clickId")).click();

        verifyAlerts(driver, getExpectedAlerts());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("frame1")
    public void thisInEventHandler() throws Exception {
        final String html
            = "<html>\n"
            + "<body>\n"
            + "  <button name='button1' id='button1' onclick='alert(this.name)'>1</button>\n"
            + "  <iframe src='default' name='frame1' id='frame1'></iframe>\n"
            + "  <script>\n"
            + "    var e = document.getElementById('frame1');\n"
            + "    e.onload = document.getElementById('button1').onclick;\n"
            + "  </script>\n"
            + "</body></html>";

        getMockWebConnection().setDefaultResponse("<html><body></body></html>");
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("called")
    public void iframeOnload() throws Exception {
        final String html
            = "<html><head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    alert('called');\n"
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
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"inline", "null"})
    public void iframeOnload2() throws Exception {
        final String html
            = "<html>\n"
            + "<body>\n"
            + "<iframe src='about:blank' name='frame1' id='frame1'></iframe>\n"
            + "<script>\n"
            + "  var e = document.getElementById('frame1');\n"
            + "  e.onload = alert('inline');\n"
            + "  alert(e.onload);\n"
            + "</script>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * Test that the event property of the window is available.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"false", "false"})
    public void ieWindowEvent() throws Exception {
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
    @Alerts({"1", "2"})
    public void commentInEventHandlerDeclaration() throws Exception {
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
    public void nullEventHandler() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
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
    @Alerts(DEFAULT = {"[object Event]", "load", "false", "false", "false"},
            IE = {"[object Event]", "load", "false", "false", "undefined"})
    public void onload() throws Exception {
        final String html =
              "<html><body onload='test(event)'><script>\n"
            + "    function test(e) {\n"
            + "      dump(e);\n"
            + "    }\n"
            + DUMP_EVENT_FUNCTION
            + "</script></body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"[object Event]", "number"})
    public void timeStamp() throws Exception {
        final String html =
              "<html><body onload='test(event)'><script>\n"
            + "  function test(e) {\n"
            + "    alert(e);\n"
            + "    alert(typeof e.timeStamp);\n"
            + "  }\n"
            + "</script></body></html>";

        loadPageWithAlerts2(html);
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
        final String html =
              "<html><head><script>\n"
            + "  function test() {\n"
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
            + "</script></head>\n"
            + "<body onload='test()'></body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"true", "I was here"})
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
            + "    x.foo = 'I was here';\n"
            + "  }\n"
            + "  \n"
            + "  e.addEventListener('click', listener, false);\n"
            + "  myEvent = document.createEvent('HTMLEvents');\n"
            + "  myEvent.initEvent('click', true, true);\n"
            + "  e.dispatchEvent(myEvent);\n"
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
            FF78 = {"e-0", "1", "e-2", "e-3", "e-4", "e-5",
                    "2", "e-7", "e-8", "e-9", "e-10", "e-11",
                    "e-12", "e-13", "e-14", "e-15", "e-16", "e-17", "8",
                    "e-19", "e-20", "e-21", "e-22", "e-23", "e-24",
                    "e-25", "e-26", "e-27", "e-28", "e-29", "4", "e-31", "e-32",
                    "e-33"})
    public void constants() throws Exception {
        final String html =
              "<html><body>\n"
            + "<script>\n"
            + "  var constants = [Event.ABORT, Event.ALT_MASK, Event.BACK, Event.BLUR, Event.CHANGE, Event.CLICK, "
            + "Event.CONTROL_MASK, Event.DBLCLICK, Event.DRAGDROP, Event.ERROR, Event.FOCUS, Event.FORWARD, "
            + "Event.HELP, Event.KEYDOWN, Event.KEYPRESS, Event.KEYUP, Event.LOAD, Event.LOCATE, Event.META_MASK, "
            + "Event.MOUSEDOWN, Event.MOUSEDRAG, Event.MOUSEMOVE, Event.MOUSEOUT, Event.MOUSEOVER, Event.MOUSEUP, "
            + "Event.MOVE, Event.RESET, Event.RESIZE, Event.SCROLL, Event.SELECT, Event.SHIFT_MASK, Event.SUBMIT, "
            + "Event.UNLOAD, Event.XFER_DONE];\n"
            + "  for (var x in constants) {\n"
            + "    try {\n"
            + "      alert(constants[x].toString(16));\n"
            + "    } catch(e) { alert('e-' + x); }\n"
            + "  }\n"
            + "</script>\n"
            + "</body></html>";

        loadPageWithAlerts2(html, 2 * DEFAULT_WAIT_TIME);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("exception")
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
    @Alerts({"form1 -> custom", "form2 -> [object HTMLFormElement]",
            "form1: [object HTMLFormElement]", "form2: [object HTMLFormElement]",
            "form1 -> custom", "form2 -> [object HTMLFormElement]"})
    public void nameResolution() throws Exception {
        final String html = "<html><head><script>\n"
            + "var form1 = 'custom';\n"
            + "function testFunc() {\n"
            + "  alert('form1 -> ' + form1);\n"
            + "  alert('form2 -> ' + form2);\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='testFunc()'>\n"
            + "<form name='form1'></form>\n"
            + "<form name='form2'></form>\n"
            + "<button onclick=\"alert('form1: ' + form1); alert('form2: ' + form2); testFunc()\">click me</button>\n"
            + "</body></html>";

        final String[] alerts = getExpectedAlerts();
        int i = 0;

        final WebDriver driver = loadPage2(html);
        verifyAlerts(driver, alerts[i++], alerts[i++]);

        driver.findElement(By.tagName("button")).click();
        verifyAlerts(driver, alerts[i++], alerts[i++], alerts[i++], alerts[i++]);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "activeElement BODY",
            FF = {"activeElement BODY", "focus #document", "handler: activeElement BODY"},
            FF78 = {"activeElement BODY", "focus #document", "handler: activeElement BODY"},
            IE = {"activeElement BODY", "focus BODY", "handler: activeElement BODY"})
    // http://code.google.com/p/selenium/issues/detail?id=4665
    @NotYetImplemented({IE, FF, FF78})
    public void document_focus() throws Exception {
        final String html = "<html>\n"
                + "<head>\n"
                + "<script>\n"
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
                + "  function log(x) {\n"
                + "    document.getElementById('log').value += x + '\\n';\n"
                + "  }\n"
                + "</script>\n"
                + "</head>\n"
                + "<body onload='test()'>\n"
                + "  <textarea id='log' cols='80' rows='40'></textarea>\n"
                + "</body></html>";

        final WebDriver driver = loadPage2(html);
        final String text = driver.findElement(By.id("log")).getAttribute("value").trim().replaceAll("\r", "");
        assertEquals(String.join("\n", getExpectedAlerts()), text);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"focus INPUT", "focus INPUT"})
    public void document_input_focus() throws Exception {
        document_input("focus");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "blur INPUT",
            IE = {"blur BODY", "blur INPUT"})
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
                + "    obj.addEventListener('" + event + "', handler, true);\n"
                + "  }\n"
                + "  function handler(e) {\n"
                + "    var src = e.srcElement;\n"
                + "    if (!src)\n"
                + "      src = e.target;\n"
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
        assertEquals(String.join("\n", getExpectedAlerts()), text);
    }

    /**
     * Test that the parent scope of the event handler defined in HTML attributes is "document".
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"2from window", "1from document"})
    public void eventHandlersParentScope() throws Exception {
        final String html = "<html><body>\n"
            + "<button name='button1' id='button1' onclick='alert(1 + foo)'>click me</button>\n"
            + "<script>\n"
            + "  window.addEventListener('click', function() { alert(2 + foo); }, true);\n"
            + "  document.foo = 'from document';\n"
            + "  var foo = 'from window';\n"
            + "</script>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("button1")).click();

        verifyAlerts(driver, getExpectedAlerts());
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

        final String[] alerts = getExpectedAlerts();
        final WebDriver driver = loadPage2(html);
        final WebElement field = driver.findElement(By.id("theField"));
        field.click();
        verifyAlerts(driver, alerts[0]);

        final JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;

        // remove property on field
        jsExecutor.executeScript("delete document.getElementById('theField').foo");
        field.click();
        verifyAlerts(driver, alerts[1]);

        // remove property on form
        jsExecutor.executeScript("delete document.getElementById('theForm').foo");
        field.click();
        verifyAlerts(driver, alerts[2]);

        // remove property on document
        jsExecutor.executeScript("delete document.foo");
        field.click();
        verifyAlerts(driver, alerts[3]);
    }

    /**
     * Test that the function open resolves to document.open within a handler defined by an attribute.
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

        verifyAlerts(driver, getExpectedAlerts());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"false", "boolean"})
    public void defaultPrevented() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    try {\n"
            + "      var event = document.createEvent('Event');\n"
            + "      alert(event.defaultPrevented);\n"
            + "      alert(typeof event.defaultPrevented);\n"
            + "    } catch (e) { alert('exception') }\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"true", "boolean"},
            IE = {"undefined", "undefined"})
    public void returnValue() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    try {\n"
            + "      var event = document.createEvent('Event');\n"
            + "      alert(event.returnValue);\n"
            + "      alert(typeof event.returnValue);\n"
            + "    } catch (e) { alert('exception') }\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"true", "boolean", "false - false",
                        "true", "true - false",
                        "false", "boolean",
                        "true", "boolean", "false - false",
                        "true", "boolean"},
            IE = {"undefined", "undefined", "false - false",
                        "undefined", "true - false",
                        "undefined", "undefined",
                        "undefined", "undefined", "false - false",
                        "undefined", "undefined"})
    public void returnValueSetter() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    try {\n"
            + "      var event = document.createEvent('Event');\n"
            + "      alert(event.returnValue);\n"
            + "      alert(typeof event.returnValue);\n"
            + "      alert(event.cancelable + ' - ' + event.defaultPrevented);\n"

            + "      event.initEvent('click', 'true', 'true');\n"
            + "      alert(event.returnValue);\n"
            + "      alert(event.cancelable + ' - ' + event.defaultPrevented);\n"

            + "      event.preventDefault();\n"
            + "      alert(event.returnValue);\n"
            + "      alert(typeof event.returnValue);\n"

            + "      event = document.createEvent('Event');\n"
            + "      alert(event.returnValue);\n"
            + "      alert(typeof event.returnValue);\n"
            + "      alert(event.cancelable + ' - ' + event.defaultPrevented);\n"

            + "      event.preventDefault();\n"
            + "      alert(event.returnValue);\n"
            + "      alert(typeof event.returnValue);\n"
            + "    } catch (e) { alert('exception') }\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"true", "boolean", "false - false",
                        "true", "true - false",
                        "false", "boolean", "false",
                        "true", "boolean", "false - false",
                        "true", "boolean", "true",
                        "true", "boolean", "true - false",
                        "false", "boolean", "false"},
            IE = {"undefined", "undefined", "false - false",
                        "undefined", "true - false",
                        "false", "boolean", "true",
                        "undefined", "undefined", "false - false",
                        "false", "boolean", "true",
                        "undefined", "undefined", "true - false",
                        "false", "boolean", "true"})
    public void returnValueSetterFalse() throws Exception {
        returnValueSetterUndefined("false");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"true", "boolean", "false - false",
                        "true", "true - false",
                        "true", "boolean", "false",
                        "true", "boolean", "false - false",
                        "true", "boolean", "true",
                        "true", "boolean", "true - false",
                        "true", "boolean", "false"},
            IE = {"undefined", "undefined", "false - false",
                        "undefined", "true - false",
                        "true", "boolean", "false",
                        "undefined", "undefined", "false - false",
                        "true", "boolean", "false",
                        "undefined", "undefined", "true - false",
                        "true", "boolean", "false"})
    public void returnValueSetterTrue() throws Exception {
        returnValueSetterUndefined("true");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"true", "boolean", "false - false",
                        "true", "true - false",
                        "true", "boolean", "false",
                        "true", "boolean", "false - false",
                        "true", "boolean", "true",
                        "true", "boolean", "true - false",
                        "true", "boolean", "false"},
            IE = {"undefined", "undefined", "false - false",
                        "undefined", "true - false",
                        "test", "string", "false",
                        "undefined", "undefined", "false - false",
                        "test", "string", "false",
                        "undefined", "undefined", "true - false",
                        "test", "string", "false"})
    public void returnValueSetterString() throws Exception {
        returnValueSetterUndefined("'test'");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"true", "boolean", "false - false",
                        "true", "true - false",
                        "false", "boolean", "false",
                        "true", "boolean", "false - false",
                        "true", "boolean", "true",
                        "true", "boolean", "true - false",
                        "false", "boolean", "false"},
            IE = {"undefined", "undefined", "false - false",
                        "undefined", "true - false",
                        "0", "number", "true",
                        "undefined", "undefined", "false - false",
                        "0", "number", "true",
                        "undefined", "undefined", "true - false",
                        "0", "number", "true"})
    public void returnValueSetterZero() throws Exception {
        returnValueSetterUndefined("0");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"true", "boolean", "false - false",
                        "true", "true - false",
                        "true", "boolean", "false",
                        "true", "boolean", "false - false",
                        "true", "boolean", "true",
                        "true", "boolean", "true - false",
                        "true", "boolean", "false"},
            IE = {"undefined", "undefined", "false - false",
                        "undefined", "true - false",
                        "1", "number", "false",
                        "undefined", "undefined", "false - false",
                        "1", "number", "false",
                        "undefined", "undefined", "true - false",
                        "1", "number", "false"})
    public void returnValueSetterOne() throws Exception {
        returnValueSetterUndefined("1");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"true", "boolean", "false - false",
                        "true", "true - false",
                        "true", "boolean", "false",
                        "true", "boolean", "false - false",
                        "true", "boolean", "true",
                        "true", "boolean", "true - false",
                        "true", "boolean", "false"},
            IE = {"undefined", "undefined", "false - false",
                        "undefined", "true - false",
                        "-1", "number", "false",
                        "undefined", "undefined", "false - false",
                        "-1", "number", "false",
                        "undefined", "undefined", "true - false",
                        "-1", "number", "false"})
    public void returnValueSetterMinusOne() throws Exception {
        returnValueSetterUndefined("-1");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"true", "boolean", "false - false",
                        "true", "true - false",
                        "false", "boolean", "false",
                        "true", "boolean", "false - false",
                        "true", "boolean", "true",
                        "true", "boolean", "true - false",
                        "false", "boolean", "false"},
            IE = {"undefined", "undefined", "false - false",
                        "undefined", "true - false",
                        "undefined", "undefined", "true",
                        "undefined", "undefined", "false - false",
                        "undefined", "undefined", "true",
                        "undefined", "undefined", "true - false",
                        "undefined", "undefined", "true"})
    public void returnValueSetterUndefined() throws Exception {
        returnValueSetterUndefined("undefined");
    }

    private void returnValueSetterUndefined(final String value) throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html>\n"
            + "  <head>\n"
            + "    <title></title>\n"
            + "  </head>\n"
            + "  <body onload='test()'>\n"
            + "    <div><a id='triggerClick' href='#'>click event</a></div>\n"

            + "    <script>\n"
            + "      function log(msg) {\n"
            + "        window.document.title += msg + ';';\n"
            + "      }\n"

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
            + "        } catch (e) { log('exception') }\n"
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

        final String text = driver.getTitle().trim().replaceAll(";", "\n").trim();
        assertEquals(String.join("\n", getExpectedAlerts()), text);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"false - false", "true - false", "true - true",
                        "false - false", "false - false", "false - false",
                        "false - false", "true - false"},
            IE = {"false - false", "true - false", "true - false",
                    "false - false", "false - false", "false - false",
                    "false - false", "true - false"})
    @NotYetImplemented(IE)
    public void preventDefault() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html>\n"
            + "  <head>\n"
            + "    <title></title>\n"
            + "    <script>\n"
            + "      function log(msg) {\n"
            + "        window.document.title += msg + ';';\n"
            + "      }\n"

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
            + "        } catch (e) { log('exception') }\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='test()'>\n"
            + "  </body>\n"
            + "</html>";

        final WebDriver driver = loadPage2(html);

        final String text = driver.getTitle().trim().replaceAll(";", "\n").trim();
        assertEquals(String.join("\n", getExpectedAlerts()), text);
    }
}
