/*
 * Copyright (c) 2002-2011 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.Browser;
import com.gargoylesoftware.htmlunit.BrowserRunner.Browsers;

/**
 * Proof of concept for using WebDriver to run (some) HtmlUnit tests and have the possibility
 * to check in "real" browsers if our expectations are correct.
 *
 * @version $Revision$
 * @author Marc Guillemot
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class SimpleWebDriverTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE = "<SPAN onclick=\"var f = &quot;hello&quot; + 'world'\">test span</SPAN>",
            FF = "<span onclick=\"var f = &quot;hello&quot; + 'world'\">test span</span>")
    public void innerHTMLwithQuotes() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + "    function test() {\n"
            + "      alert(document.getElementById('foo').innerHTML);\n"
            + "    }\n"
            + "  </script>\n"
            + "</head><body onload='test()'>\n"
            + "  <div id='foo'><span onclick=\"var f = &quot;hello&quot; + 'world'\">test span</span></div>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE = { "true", "[object]", "true" },
            FF = { "true", "undefined", "false" })
    public void document_xxx_formAccess() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + "    function test() {\n"
            + "      alert(document.foo == document.forms.foo);\n"
            + "      alert(document.blah);\n"
            + "      alert(document.blah == document.forms.foo)\n"
            + "    }\n"
            + "  </script>\n"
            + "</head><body onload='test()'>\n"
            + "  <div id='foo'>the div 1</div>\n"
            + "  <form name='foo' id='blah'>\n"
            + "    <input name='foo'>\n"
            + "  </form>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(Browser.IE)
    @Alerts("hello")
    public void fireEvent_initFromTemplate() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + "    function test() {\n"
            + "      var myEvent = document.createEventObject();\n"
            + "      myEvent.eventType = 'onclick';\n"
            + "      myEvent.foo = 'hello';\n"
            + "      document.getElementById('theButton').fireEvent('onclick', myEvent);\n"
            + "    }\n"
            + "  </script>\n"
            + "</head><body onload='test()'>\n"
            + "  <span id='theButton' onclick='alert(event.foo)'>a span</span>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
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
            + " onclick=\"log('click span')\"  onmouseup=\"log('mouseup span')\">test span</span>\n"
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

        final WebDriver driver = loadPageWithAlerts2(html);
        driver.findElement(By.id("testSpan")).click();
        driver.findElement(By.id("testInput")).click();
        driver.findElement(By.id("testImage")).click();
        driver.findElement(By.id("testTextarea")).click();
        final String expected = "mousedown span,mouseup span,click span,mousedown text,focus text,mouseup text,"
            + "click text,mousedown image,focus image,mouseup image,click image,mousedown textarea,focus textarea,"
            + "mouseup textarea,click textarea,";
        assertEquals(expected, driver.findElement(By.id("myTextarea")).getValue());
    }

    /**
     * Firefox should not run scripts with "event" and "for" attributes.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE = "onload for window,onclick for div1,", FF = "onload for window,")
    public void scriptEventFor() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + "    function log(text) {\n"
            + "      var textarea = document.getElementById('myTextarea');\n"
            + "      textarea.value += text + ',';\n"
            + "    }\n"
            + "  </script>\n"
            + "</head><body>\n"
            + "  <textarea id='myTextarea' cols='80' rows='10'></textarea>\n"
            + "  <script event='onload' for='window'>\n"
            + "    log('onload for window')\n"
            + "  </script>\n"
            + "  <div id='div1'>the div 1</div>\n"
            + "  <div id='div2'>the div 2</div>\n"
            + "  <script event='onclick' for='div1'>\n"
            + "    log('onclick for div1')\n"
            + "  </script>\n"
            + "  <script event='onclick' for='document.all.div2'>\n"
            + "    log('onclick for div2')\n"
            + "  </script>\n"
            + "</body></html>";

        final WebDriver webDriver = loadPage2(html);
        webDriver.findElement(By.id("div1")).click();
        webDriver.findElement(By.id("div2")).click();
        assertEquals(getExpectedAlerts()[0], webDriver.findElement(By.id("myTextarea")).getValue());
    }

    /**
     * Test event order.
     * @throws Exception if the test fails
     */
    @Test
    //Fails with InternetExplorerDriver, but works in independent IE :(
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

        final WebDriver webDriver = loadPageWithAlerts2(html);
        final WebElement textField = webDriver.findElement(By.id("foo"));
        textField.click(); // to give focus
        textField.sendKeys("a");
        webDriver.findElement(By.id("other")).click();

        final String expected;
        if (getBrowserVersion().isFirefox() && getBrowserVersion().getBrowserVersionNumeric() == 2) {
            expected = "focus,keydown,keypress,keyup,blur,change,";
        }
        else {
            expected = "focus,keydown,keypress,keyup,change,blur,";
        }

        assertEquals(expected, webDriver.findElement(By.id("myTextarea")).getValue());
    }
}
