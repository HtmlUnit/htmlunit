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

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.BuggyWebDriver;
import com.gargoylesoftware.htmlunit.BrowserRunner.HtmlUnitNYI;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.html.HtmlPageTest;

/**
 * Tests for mouse events support.
 *
 * @author Marc Guillemot
 * @author Ahmed Ashour
 * @author Frank Danek
 */
@RunWith(BrowserRunner.class)
public class MouseEventTest extends WebDriverTestCase {

    private static final String DUMP_EVENT_FUNCTION = "  function dump(event) {\n"
            + "    alert(event);\n"
            + "    alert(event.type);\n"
            + "    alert(event.bubbles);\n"
            + "    alert(event.cancelable);\n"
            + "    alert(event.composed);\n"

            + "    alert(event.view == window);\n"
            + "    alert(event.screenX);\n"
            + "    alert(event.screenY);\n"
            + "    alert(event.clientX);\n"
            + "    alert(event.clientY);\n"
            + "    alert(event.ctrlKey);\n"
            + "    alert(event.altKey);\n"
            + "    alert(event.shiftKey);\n"
            + "    alert(event.metaKey);\n"
            + "    alert(event.button);\n"
            + "    alert(event.buttons);\n"
            + "    alert(event.which);\n"
            + "  }\n";

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"[object MouseEvent]", "click", "false", "false", "false", "false",
                        "0", "0", "0", "0", "false", "false", "false", "false", "0", "0", "1"},
            IE = "exception")
    public void create_ctor() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    try {\n"
            + "      var event = new MouseEvent('click');\n"
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
    @Alerts("exception")
    @HtmlUnitNYI(CHROME = {"[object MouseEvent]", "undefined", "false", "false", "false", "false",
                        "0", "0", "0", "0", "false", "false", "false", "false", "0", "0", "1"},
                EDGE = {"[object MouseEvent]", "undefined", "false", "false", "false", "false",
                        "0", "0", "0", "0", "false", "false", "false", "false", "0", "0", "1"},
                FF = {"[object MouseEvent]", "undefined", "false", "false", "false", "false",
                        "0", "0", "0", "0", "false", "false", "false", "false", "0", "0", "1"},
                FF78 = {"[object MouseEvent]", "undefined", "false", "false", "false", "false",
                        "0", "0", "0", "0", "false", "false", "false", "false", "0", "0", "1"})
    public void create_ctorWithoutType() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    try {\n"
            + "      var event = new MouseEvent();\n"
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
    @Alerts(DEFAULT = {"[object MouseEvent]", "42", "false", "false", "false", "false",
                        "0", "0", "0", "0", "false", "false", "false", "false", "0", "0", "1"},
            IE = "exception")
    public void create_ctorNumericType() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    try {\n"
            + "      var event = new MouseEvent(42);\n"
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
    @Alerts(DEFAULT = {"[object MouseEvent]", "null", "false", "false", "false", "false",
                        "0", "0", "0", "0", "false", "false", "false", "false", "0", "0", "1"},
            IE = "exception")
//    @HtmlUnitNYI(CHROME = {"[object MouseEvent]", "null", "false", "false", "false", "false",
//                        "0", "0", "0", "0", "false", "false", "false", "false", "0", "0"},
//                EDGE = {"[object MouseEvent]", "null", "false", "false", "false", "false",
//                        "0", "0", "0", "0", "false", "false", "false", "false", "0", "0"},
//                FF = {"[object MouseEvent]", "null", "false", "false", "false", "false",
//                        "0", "0", "0", "0", "false", "false", "false", "false", "0", "0"},
//                FF78 = {"[object MouseEvent]", "null", "false", "false", "false", "false",
//                        "0", "0", "0", "0", "false", "false", "false", "false", "0", "0"})
    public void create_ctorNullType() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    try {\n"
            + "      var event = new MouseEvent(null);\n"
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
    @Alerts("exception")
    public void create_ctorUnknownType() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    try {\n"
            + "      var event = new MouseEvent(unknown);\n"
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
    @Alerts(DEFAULT = {"[object MouseEvent]", "HtmlUnitEvent", "false", "false", "false", "false",
                        "0", "0", "0", "0", "false", "false", "false", "false", "0", "0", "1"},
            IE = "exception")
    public void create_ctorArbitraryType() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    try {\n"
            + "      var event = new MouseEvent('HtmlUnitEvent');\n"
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
    @Alerts(DEFAULT = {"[object MouseEvent]", "click", "false", "false", "false", "false",
                        "7", "0", "13", "-15", "true", "true", "true", "true", "2", "4", "3"},
            IE = "exception")
    public void create_ctorAllDetails() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    try {\n"
            + "      var event = new MouseEvent('click', {\n"
            + "        'screenX': 7,\n"
            + "        'screenY': 0.4,\n"
            + "        'clientX': 13.007,\n"
            + "        'clientY': -15,\n"

            + "        'ctrlKey': true,\n"
            + "        'shiftKey': 'true',\n"
            + "        'altKey': 1,\n"
            + "        'metaKey': {},\n"

            + "        'button': 2,\n"
            + "        'buttons': 4\n"
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
    @Alerts({"DOM2: [object MouseEvent]", "DOM3: [object MouseEvent]"})
    public void createEvent() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    try {\n"
            + "      alert('DOM2: ' + document.createEvent('MouseEvents'));\n"
            + "    } catch(e) {alert('DOM2: exception')}\n"
            + "    try {\n"
            + "      alert('DOM3: ' + document.createEvent('MouseEvent'));\n"
            + "    } catch(e) {alert('DOM3: exception')}\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"click", "true", "true", "true", "1", "2", "3", "4", "true", "true", "true", "true"})
    public void initMouseEvent() throws Exception {
        final String html = "<html><body><script>\n"
            + "  var e = document.createEvent('MouseEvents');\n"
            + "  e.initMouseEvent('click', true, true, window, 0, 1, 2, 3, 4, true, true, true, true, 0, null);\n"
            + "  alert(e.type);\n"
            + "  alert(e.bubbles);\n"
            + "  alert(e.cancelable);\n"
            + "  alert(e.view == window);\n"
            + "  alert(e.screenX);\n"
            + "  alert(e.screenY);\n"
            + "  alert(e.clientX);\n"
            + "  alert(e.clientY);\n"
            + "  alert(e.ctrlKey);\n"
            + "  alert(e.altKey);\n"
            + "  alert(e.shiftKey);\n"
            + "  alert(e.metaKey);\n"
            + "</script></body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"1", "1"})
    public void dispatchEvent() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  var clickCount = 0;\n"
            + "  var dblClickCount = 0;\n"
            + "  function test() {\n"
            + "    var div = document.getElementById('myDiv');\n"
            + "    div.onclick = clickHandler;\n"
            + "    div.ondblclick = dblClickHandler;\n"
            + "    if (div.fireEvent) {\n"
            + "      var clickEvent = (evt = document.createEventObject(), evt.type = 'click', evt);\n"
            + "      div.fireEvent('onclick', clickEvent);\n"
            + "      var dblclickEvent = (evt_0 = document.createEventObject(), evt_0.type = 'dblclick', evt_0);\n"
            + "      div.fireEvent('ondblclick', dblclickEvent);\n"
            + "    }\n"
            + "    else {\n"
            + "      var clickEvent = $createMouseEvent(document, 'click', true, true, 0, 0, 0, 0, 0,"
            + " false, false, false, false, 1, null);\n"
            + "      div.dispatchEvent(clickEvent);\n"
            + "      var dblclickEvent = $createMouseEvent(document, 'dblclick', true, true, 0, 0, 0, 0, 0,"
            + " false, false, false, false, 1, null);\n"
            + "      div.dispatchEvent(dblclickEvent);\n"
            + "    }\n"
            + "    alert(clickCount);\n"
            + "    alert(dblClickCount);\n"
            + "  }\n"
            + "  function clickHandler() {\n"
            + "    clickCount++;\n"
            + "  }\n"
            + "  function dblClickHandler() {\n"
            + "    dblClickCount++;\n"
            + "  }\n"
            + "  function $createMouseEvent(doc, type, canBubble, cancelable, detail, screenX, screenY, clientX,"
            + " clientY, ctrlKey, altKey, shiftKey, metaKey, button, relatedTarget) {\n"
            + "    button == 1?(button = 0):button == 4?(button = 1):(button = 2);\n"
            + "    var evt = doc.createEvent('MouseEvents');\n"
            + "    evt.initMouseEvent(type, canBubble, cancelable, null, detail, screenX, screenY, clientX,"
            + " clientY, ctrlKey, altKey, shiftKey, metaKey, button, relatedTarget);\n"
            + "    return evt;\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "  <div id='myDiv'></div>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0")
    public void button_onclick() throws Exception {
        final String html = "<html><body>\n"
            + "<p id='clicker'>Click me!</p>\n"
            + "<script>\n"
            + "  function handler(event) {\n"
            + "    alert(event.button);\n"
            + "  }\n"
            + "  var p = document.getElementById('clicker');\n"
            + "  if (p.addEventListener ) {\n"
            + "    p.addEventListener('click', handler, false);\n"
            + "  } else if (p.attachEvent) {\n"
            + "    p.attachEvent('onclick', handler);\n"
            + "  }\n"
            + "</script></body></html>";

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("clicker")).click();

        verifyAlerts(driver, getExpectedAlerts());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0")
    public void button_onmousedown() throws Exception {
        final String html = "<html><body>\n"
            + "<p id='clicker'>Click me!</p>\n"
            + "<script>\n"
            + "  function handler(event) {\n"
            + "    alert(event.button);\n"
            + "  }\n"
            + "  var p = document.getElementById('clicker');\n"
            + "  if (p.addEventListener ) {\n"
            + "    p.addEventListener('mousedown', handler, false);\n"
            + "  } else if (p.attachEvent) {\n"
            + "    p.attachEvent('onmousedown', handler);\n"
            + "  }\n"
            + "</script></body></html>";

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("clicker")).click();

        verifyAlerts(driver, getExpectedAlerts());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Click on DIV(id=div1): true, true, false, false")
    @BuggyWebDriver(IE = "Click on SPAN(id=span1): true, true, true, false")
    public void eventCoordinates_div() throws Exception {
        eventCoordinates("div1");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Click on SPAN(id=span1): true, true, true, false")
    public void eventCoordinates_spanInsideDiv() throws Exception {
        eventCoordinates("span1");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Click on SPAN(id=span2): true, false, false, true")
    public void eventCoordinates_span() throws Exception {
        eventCoordinates("span2");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Click on TEXTAREA(id=myTextarea): true, false, false, false")
    public void eventCoordinates_textarea() throws Exception {
        eventCoordinates("myTextarea");
    }

    private void eventCoordinates(final String id) throws Exception {
        final String html = getFileContent("event_coordinates.html");

        final String[] expected = getExpectedAlerts();

        setExpectedAlerts();
        final WebDriver driver = loadPageWithAlerts2(html);
        assertTitle(driver, "Mouse Event coordinates");

        final WebElement textarea = driver.findElement(By.id("myTextarea"));
        assertEquals("", textarea.getText());

        driver.findElement(By.id(id)).click();
        assertEquals(expected[0], textarea.getAttribute("value").trim());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"0", "0"})
    public void pageX() throws Exception {
        final String html = "<html><body><script>\n"
            + "  var e = document.createEvent('MouseEvents');\n"
            + "  alert(e.pageX);\n"
            + "  alert(e.pageY);\n"
            + "</script></body></html>";

        loadPageWithAlerts2(html);
    }

}
