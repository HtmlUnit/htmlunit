/*
 * Copyright (c) 2002-2012 Gargoyle Software Inc.
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

import static org.junit.Assert.assertNotNull;

import java.net.URL;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTextArea;

/**
 * Tests for mouse events support.
 *
 * @version $Revision$
 * @author Marc Guillemot
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class MouseEventTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void eventCoordinates() throws Exception {
        final URL url = getClass().getClassLoader().getResource("event_coordinates.html");
        assertNotNull(url);

        final WebClient client = getWebClient();
        final HtmlPage page = client.getPage(url);
        assertEquals("Mouse Event coordinates", page.getTitleText());

        final HtmlTextArea textarea = page.getHtmlElementById("myTextarea");
        assertEquals("", textarea.asText());

        page.<HtmlElement>getHtmlElementById("div1").click();
        assertEquals("Click on DIV(id=div1): true, true, false, false" + LINE_SEPARATOR, textarea.asText());
        textarea.reset();

        page.<HtmlElement>getHtmlElementById("span1").click();
        assertEquals("Click on SPAN(id=span1): true, true, true, false" + LINE_SEPARATOR, textarea.asText());
        textarea.reset();

        page.<HtmlElement>getHtmlElementById("span2").click();
        assertEquals("Click on SPAN(id=span2): true, false, false, true" + LINE_SEPARATOR, textarea.asText());
        textarea.reset();

        textarea.click();
        assertEquals("Click on TEXTAREA(id=myTextarea): true, false, false, false" + LINE_SEPARATOR, textarea.asText());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = { "click", "true", "true", "true", "1", "2", "3", "4", "true", "true", "true", "true" },
            IE = "exception")
    public void initMouseEvent() throws Exception {
        final String html = "<html><body><script>\n"
            + "try {\n"
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
            + "} catch(e) { alert('exception') }\n"
            + "</script></body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "1", "1" })
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
        assertEquals(getExpectedAlerts(), getCollectedAlerts(driver));
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(IE = "1", FF = "0")
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
        assertEquals(getExpectedAlerts(), getCollectedAlerts(driver));
    }
}
