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
package com.gargoylesoftware.htmlunit.javascript.host.dom;

import static com.gargoylesoftware.htmlunit.BrowserRunner.TestedBrowser.IE;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * Tests for EventNode.
 *
 * @author Ahmed Ashour
 * @author Frank Danek
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class EventNodeTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("fireEvent not available")
    public void fireEvent() throws Exception {
        final String html
            = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var form = document.getElementById('myForm');\n"
            + "    if (!form.fireEvent) { log('fireEvent not available'); return }\n"
            + "    log(form.fireEvent('onsubmit'));\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "  <form id='myForm'>\n"
            + "  </form>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("createEventObject not available")
    public void fireEvent_initFromTemplate() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    function test() {\n"
            + "      if (!document.createEventObject) { log('createEventObject not available'); return }\n"
            + "      var myEvent = document.createEventObject();\n"
            + "      myEvent.eventType = 'onclick';\n"
            + "      myEvent.foo = 'hello';\n"
            + "      var butt = document.getElementById('theButton');\n"
            + "      butt.fireEvent('onclick', myEvent);\n"
            + "    }\n"
            + "  </script>\n"
            + "</head><body onload='test()'>\n"
            + "  <span id='theButton' onclick='log(event.foo)'>a span</span>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("mousedown span,mouseup span,click span,mousedown text,focus text,mouseup text,"
        + "click text,mousedown image,focus image,mouseup image,click image,mousedown textarea,focus textarea,"
        + "mouseup textarea,click textarea,")
    public void clickEvents() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + "    function log(text) {\n"
            + "      var textarea = document.getElementById('myTextarea');\n"
            + "      textarea.value += text + ',';\n"
            + "    }\n"
            + "  </script>\n"
            + "</head><body>\n"
            + "  <span id='testSpan' onfocus=\"log('will not be triggered')\" onmousedown=\"log('mousedown span')\""
            + " onclick=\"log('click span')\" onmouseup=\"log('mouseup span')\">test span</span>\n"
            + "  <form>\n"
            + "    <input type='text' id='testInput' onmousedown=\"log('mousedown text')\""
            + " onclick=\"log('click text')\" onmouseup=\"log('mouseup text')\" onfocus=\"log('focus text')\">\n"
            + "    <input type='image' id='testImage' onmousedown=\"log('mousedown image')\""
            + " onclick=\"log('click image'); return false;\" onmouseup=\"log('mouseup image')\""
            + " onfocus=\"log('focus image')\">\n"
            + "    <textarea id='testTextarea' onfocus=\"log('focus textarea')\""
            + " onmousedown=\"log('mousedown textarea')\" onclick=\"log('click textarea')\""
            + " onmouseup=\"log('mouseup textarea')\" onfocus=\"log('focus textarea')\"></textarea>\n"
            + "  </form>\n"
            + "  <textarea id='myTextarea' cols='80' rows='10'></textarea>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("testSpan")).click();
        driver.findElement(By.id("testInput")).click();
        driver.findElement(By.id("testImage")).click();
        driver.findElement(By.id("testTextarea")).click();
        final String expected = getExpectedAlerts()[0];
        assertEquals(expected, driver.findElement(By.id("myTextarea")).getAttribute("value"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "mousedown label,mouseup label,click label,focus text,click text,",
            IE = "mousedown label,mouseup label,click label,click text,focus text,")
    @NotYetImplemented(IE)
    public void clickEventsLabel() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + "    function log(text) {\n"
            + "      var textarea = document.getElementById('myTextarea');\n"
            + "      textarea.value += text + ',';\n"
            + "    }\n"
            + "  </script>\n"
            + "</head><body>\n"
            + "  <label id='testLabel' for='testInput'"
            + " onfocus=\"log('will not be triggered')\" onmousedown=\"log('mousedown label')\""
            + " onclick=\"log('click label')\" onmouseup=\"log('mouseup label')\">test label</label>\n"
            + "  <form>\n"
            + "    <input type='text' id='testInput' onmousedown=\"log('mousedown text')\""
            + " onclick=\"log('click text')\" onmouseup=\"log('mouseup text')\" onfocus=\"log('focus text')\">\n"
            + "  </form>\n"
            + "  <textarea id='myTextarea' cols='80' rows='10'></textarea>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("testLabel")).click();

        final String expected = getExpectedAlerts()[0];
        assertEquals(expected, driver.findElement(By.id("myTextarea")).getAttribute("value"));
    }

    /**
     * Test event order.
     * @throws Exception if the test fails
     */
    @Test
    public void eventOrder() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + "    function log(text) {\n"
            + "      var textarea = document.getElementById('myTextarea');\n"
            + "      textarea.value += text + ',';\n"
            + "    }\n"
            + "  </script>\n"
            + "</head><body>\n"
            + "<form>\n"
            + "  <input name='foo' id='foo' onfocus=\"log('focus')\" onblur=\"log('blur')\" onchange=\"log('change')\""
            + " onkeydown=\"log('keydown')\" onkeypress=\"log('keypress')\" onkeyup=\"log('keyup')\">\n"
            + "  <input name='other' id='other'>\n"
            + "</form>\n"
            + "  <textarea id='myTextarea' cols='80'></textarea>\n"
            + "</body></html>";

        final WebDriver webDriver = loadPage2(html);
        final WebElement textField = webDriver.findElement(By.id("foo"));
        textField.click(); // to give focus
        textField.sendKeys("a");
        webDriver.findElement(By.id("other")).click();

        final String expected = "focus,keydown,keypress,keyup,change,blur,";
        assertEquals(expected, webDriver.findElement(By.id("myTextarea")).getAttribute("value"));
    }
}
