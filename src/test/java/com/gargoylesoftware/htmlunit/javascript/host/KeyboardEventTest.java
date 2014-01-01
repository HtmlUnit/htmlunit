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

import java.util.Arrays;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * Tests for {@link KeyboardEvent}.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 * @author Marc Guillemot
 * @author Frank Danek
 */
@RunWith(BrowserRunner.class)
public class KeyboardEventTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(FF = { "DOM3: [object KeyboardEvent]", "vendor: [object KeyboardEvent]" },
            IE = { "DOM3: exception", "vendor: exception" },
            IE11 = { "DOM3: [object KeyboardEvent]", "vendor: exception" })
    public void createEvent() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    try {\n"
            + "      alert('DOM3: ' + document.createEvent('KeyboardEvent'));\n"
            + "    } catch(e) {alert('DOM3: exception')}\n"
            + "    try {\n"
            + "      alert('vendor: ' + document.createEvent('KeyEvents'));\n"
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
    @Alerts(FF = { "0-0", "0-0", "undefined-undefined" },
            IE = { "exception", "exception", "exception" },
            IE11 = { "exception", "0-0", "undefined-undefined" })
    public void keyCode() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    try {\n"
            + "      var keyEvent = document.createEvent('KeyEvents');\n"
            + "      alert(keyEvent.keyCode + '-' + keyEvent.charCode);\n"
            + "    } catch(e) {alert('exception')}\n"
            + "    try {\n"
            + "      var keyEvent = document.createEvent('KeyboardEvent');\n"
            + "      alert(keyEvent.keyCode + '-' + keyEvent.charCode);\n"
            + "    } catch(e) {alert('exception')}\n"
            + "    try {\n"
            + "      var mouseEvent = document.createEvent('MouseEvents');\n"
            + "      alert(mouseEvent.keyCode + '-' + mouseEvent.charCode);\n"
            + "    } catch(e) {alert('exception')}\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(FF = { "keydown, true, true, true, true, true, true, 65, 0",
                "keyup, false, false, false, false, false, false, 32, 0",
                "keydown, true, true, true, true, true, true, 65, 0",
                "keyup, false, false, false, false, false, false, 32, 0" },
            IE = { "exception", "exception" })
    public void initKeyEvent() throws Exception {
        final String html = "<html><head><script>\n"
            + "  var properties = ['type', 'bubbles', 'cancelable', /*'view',*/ 'ctrlKey', 'altKey',\n"
            + "        'shiftKey', 'metaKey', 'keyCode', 'charCode'];\n"
            + "  function dumpEvent(e) {\n"
            + "    var str = '';\n"
            + "    for (var i=0; i<properties.length; ++i) str += ', ' + e[properties[i]];\n"
            + "    alert(str.substring(2));\n"
            + "  }\n"
            + "  function test() {\n"
            + "    try {\n"
            + "      var keyEvent = document.createEvent('KeyEvents');\n"
            + "      keyEvent.initKeyEvent('keydown', true, true, null, true, true, true, true, 65, 65);\n"
            + "      dumpEvent(keyEvent);\n"
            + "      keyEvent = document.createEvent('KeyEvents');\n"
            + "      keyEvent.initKeyEvent('keyup', false, false, null, false, false, false, false, 32, 32);\n"
            + "      dumpEvent(keyEvent);\n"
            + "    } catch(e) {alert('exception')}\n"
            + "    try {\n"
            + "      var keyEvent = document.createEvent('KeyboardEvent');\n"
            + "      keyEvent.initKeyEvent('keydown', true, true, null, true, true, true, true, 65, 65);\n"
            + "      dumpEvent(keyEvent);\n"
            + "      keyEvent = document.createEvent('KeyboardEvent');\n"
            + "      keyEvent.initKeyEvent('keyup', false, false, null, false, false, false, false, 32, 32);\n"
            + "      dumpEvent(keyEvent);\n"
            + "    } catch(e) {alert('exception')}\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "65", "66", "67", "68", "69", "70", "71", "72", "73", "74", "75", "76", "77", "78", "79", "80", "81",
        "82", "83", "84", "85", "86", "87", "88", "89", "90" })
    public void keyCodes() throws Exception {
        final String html = "<html><head>"
            + "<script>"
            + "function handleKey(e) {\n"
            + "  alert(e.keyCode);"
            + "}"
            + "</script>\n"
            + "</head><body>\n"
            + "<input id='t' onkeyup='handleKey(event)'/>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        final WebElement field = driver.findElement(By.id("t"));

        field.sendKeys("abcdefghijklmnopqrstuvwxyz");
        assertEquals(getExpectedAlerts(), getCollectedAlerts(driver));
    }

    /**
     * @throws Exception if the test fails
     */
    // FF seem to generate seperate events for the shift key (tested with
    // real FF17 and 24)
    @Test
    @Alerts(FF = { // "keydown:16,0,16",
                    "keydown:65,0,65",
                    "keypress:0,65,65",
                    "keyup:65,0,65",
                   // "keyup:16,0,16",
                    "keydown:65,0,65",
                    "keypress:0,97,97",
                    "keyup:65,0,65",
                    "keydown:190,0,190",
                    "keypress:0,46,46",
                    "keyup:190,0,190",
                    "keydown:13,0,13",
                    "keypress:13,0,13",
                    "keyup:13,0,13" },
            IE = { //"keydown:16,undefined,undefined",
                    "keydown:65,undefined,undefined",
                    "keypress:65,undefined,undefined",
                    "keyup:65,undefined,undefined",
                    //"keyup:16,undefined,undefined",
                    "keydown:65,undefined,undefined",
                    "keypress:97,undefined,undefined",
                    "keyup:65,undefined,undefined",
                    "keydown:190,undefined,undefined",
                    "keypress:46,undefined,undefined",
                    "keyup:190,undefined,undefined",
                    "keydown:13,undefined,undefined",
                    "keypress:13,undefined,undefined",
                    "keyup:13,undefined,undefined" },
           IE11 = { //"keydown:16,0,16",
                    "keydown:65,0,65",
                    "keypress:65,65,65",
                    "keyup:65,0,65",
                    //"keyup:16,0,16",
                    "keydown:65,0,65",
                    "keypress:97,97,97",
                    "keyup:65,0,65",
                    "keydown:190,0,190",
                    "keypress:46,46,46",
                    "keyup:190,0,190",
                    "keydown:13,0,13",
                    "keypress:13,13,13",
                    "keyup:13,0,13" })
    // WebDriver with real IE11 does not get the '\r' but real IE11 does
    // TODO [IE11] HtmlUnit (or WebDriver?) does not fire an own event for the shift key (down + up)
    public void which() throws Exception {
        final String html
            = "<html><head></head><body>\n"
            + "<input type='text' id='keyId'>\n"
            + "<script>\n"
            + "function handler(e) {\n"
            + "  e = e ? e : window.event;\n"
            + "  document.getElementById('myTextarea').value "
            + "+= e.type + ':' + e.keyCode + ',' + e.charCode + ',' + e.which + '\\n';\n"
            + "}\n"
            + "document.getElementById('keyId').onkeyup = handler;\n"
            + "document.getElementById('keyId').onkeydown = handler;\n"
            + "document.getElementById('keyId').onkeypress = handler;\n"
            + "</script>\n"
            + "<textarea id='myTextarea' cols=80 rows=20></textarea>\n"
            + "</body></html>";
        final String keysToSend = "Aa.\r";
        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("keyId")).sendKeys(keysToSend);

        final String[] actual = driver.findElement(By.id("myTextarea")).getAttribute("value").split("\r\n|\n");
        assertEquals(Arrays.asList(getExpectedAlerts()).toString(), Arrays.asList(actual).toString());
    }

}
