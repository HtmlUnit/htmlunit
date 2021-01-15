/*
 * Copyright (c) 2002-2021 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript.host.event;

import static com.gargoylesoftware.htmlunit.BrowserRunner.TestedBrowser.FF;
import static com.gargoylesoftware.htmlunit.BrowserRunner.TestedBrowser.FF78;

import java.util.Arrays;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.BuggyWebDriver;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * Tests for {@link KeyboardEvent}.
 *
 * @author Ahmed Ashour
 * @author Marc Guillemot
 * @author Frank Danek
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class KeyboardEventTest extends WebDriverTestCase {

    /**
     * Checks that after creating a keyboard event via constructor all event properties have the expected values,
     * either the default values or the ones passed in the event init object.
     * 
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"type,,,0,false,false,false,false,false,false,0,0",
                       "type,,,0,false,false,false,false,false,false,0,0",
                       "type,,,0,false,false,false,false,false,false,0,0",
                       "type,,,0,false,false,false,false,false,false,0,0",
                       "type,null,,0,true,false,false,false,false,false,456,0",
                       "type,key,code,123,true,true,true,true,true,true,456,789"},
            IE      = {"exception",
                       "exception",
                       "exception",
                       "exception",
                       "exception",
                       "exception"})
    public void constructor() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    function doTest(f) {\n"
            + "      try {\n"
            + "        var e = f();\n"
            + "        var log = [e.type, e.key, e.code, e.location, e.ctrlKey, e.shiftKey, e.altKey, e.metaKey,\n"
            + "                   e.repeat, e.isComposing, e.charCode, e.which].join(',');\n"
            + "        alert(log);\n"
            + "      } catch(e) {alert('exception')}\n"
            + "    }\n"
            + "    doTest(() => new KeyboardEvent('type'));\n"
            + "    doTest(() => new KeyboardEvent('type', null));\n"
            + "    doTest(() => new KeyboardEvent('type', undefined));\n"
            + "    doTest(() => new KeyboardEvent('type', {}));\n"
            + "    doTest(() => new KeyboardEvent('type', { key: null, code: undefined, ctrlKey: true, charCode: 456 }));\n"
            + "    doTest(() => new KeyboardEvent('type', { key: 'key', code: 'code', location: 123,\n" 
            + "                                             ctrlKey: true, shiftKey: true, altKey: true, metaKey: true,\n" 
            + "                                             repeat: true, isComposing: true, charCode: 456, which: 789 }));\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"DOM3: [object KeyboardEvent]", "vendor: exception"})
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
    @Alerts({"exception", "0-0", "undefined-undefined"})
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
    @Alerts(DEFAULT = {"exception", "exception"},
            FF = {"exception",
                "keydown, true, true, true, true, true, true, 65, 0",
                "keyup, false, false, false, false, false, false, 32, 0"},
            FF78 = {"exception",
                "keydown, true, true, true, true, true, true, 65, 0",
                "keyup, false, false, false, false, false, false, 32, 0"})
    public void initKeyEvent() throws Exception {
        final String html = "<html><head><script>\n"
            + "  var properties = ['type', 'bubbles', 'cancelable', /*'view',*/ 'ctrlKey', 'altKey',\n"
            + "        'shiftKey', 'metaKey', 'keyCode', 'charCode'];\n"
            + "  function dumpEvent(e) {\n"
            + "    var str = '';\n"
            + "    for (var i = 0; i < properties.length; i++) str += ', ' + e[properties[i]];\n"
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
    @Alerts("32, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, ")
    public void keyCodes_keyup() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + "function handleKey(e) {\n"
            + "  document.getElementById('log').value += e.keyCode + ', ';\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body>\n"
            + "  <input id='t' onkeyup='handleKey(event)'/>\n"
            + "  <textarea id='log' rows=40 cols=80></textarea>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        final WebElement field = driver.findElement(By.id("t"));

        field.sendKeys(" 0123456789");

        final String log = driver.findElement(By.id("log")).getAttribute("value");
        assertEquals(getExpectedAlerts()[0], log);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, "
        + "82, 83, 84, 85, 86, 87, 88, 89, 90, ")
    public void keyCodes2_keyup() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + "function handleKey(e) {\n"
            + "  document.getElementById('log').value += e.keyCode + ', ';\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body>\n"
            + "  <input id='t' onkeyup='handleKey(event)'/>\n"
            + "  <textarea id='log' rows=40 cols=80></textarea>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        final WebElement field = driver.findElement(By.id("t"));

        field.sendKeys("abcdefghijklmnopqrstuvwxyz");

        final String log = driver.findElement(By.id("log")).getAttribute("value");
        assertEquals(getExpectedAlerts()[0], log);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("32, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, ")
    public void keyCodes_keydown() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + "function handleKey(e) {\n"
            + "  document.getElementById('log').value += e.keyCode + ', ';\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body>\n"
            + "  <input id='t' onkeydown='handleKey(event)'/>\n"
            + "  <textarea id='log' rows=40 cols=80></textarea>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        final WebElement field = driver.findElement(By.id("t"));

        field.sendKeys(" 0123456789");

        final String log = driver.findElement(By.id("log")).getAttribute("value");
        assertEquals(getExpectedAlerts()[0], log);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, "
                + "80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, ")
    public void keyCodes2_keydown() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + "function handleKey(e) {\n"
            + "  document.getElementById('log').value += e.keyCode + ', ';\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body>\n"
            + "  <input id='t' onkeydown='handleKey(event)'/>\n"
            + "  <textarea id='log' rows=40 cols=80></textarea>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        final WebElement field = driver.findElement(By.id("t"));

        field.sendKeys("abcdefghijklmnopqrstuvwxyz");

        final String log = driver.findElement(By.id("log")).getAttribute("value");
        assertEquals(getExpectedAlerts()[0], log);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("32, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, ")
    public void keyCodes_keypress() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + "function handleKey(e) {\n"
            + "  document.getElementById('log').value += e.charCode + ', ';\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body>\n"
            + "  <input id='t' onkeypress='handleKey(event)'/>\n"
            + "  <textarea id='log' rows=40 cols=80></textarea>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        final WebElement field = driver.findElement(By.id("t"));

        field.sendKeys(" 0123456789");

        final String log = driver.findElement(By.id("log")).getAttribute("value");
        assertEquals(getExpectedAlerts()[0], log);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("97, 98, 99, "
            + "100, 101, 102, 103, 104, 105, 106, 107, 108, 109, "
            + "110, 111, 112, 113, 114, 115, 116, 117, 118, 119, "
            + "120, 121, 122, ")
    public void keyCodes2_keypress() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + "function handleKey(e) {\n"
            + "  document.getElementById('log').value += e.charCode + ', ';\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body>\n"
            + "  <input id='t' onkeypress='handleKey(event)'/>\n"
            + "  <textarea id='log' rows=40 cols=80></textarea>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        final WebElement field = driver.findElement(By.id("t"));

        field.sendKeys("abcdefghijklmnopqrstuvwxyz");

        final String log = driver.findElement(By.id("log")).getAttribute("value");
        assertEquals(getExpectedAlerts()[0], log);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"13", "13", "13"})
    @NotYetImplemented({FF, FF78})
    public void keyCodeEnter_keypress() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + "function handleKey(e) {\n"
            + "  alert(e.charCode);\n"
            + "  alert(e.keyCode);\n"
            + "  alert(e.which);\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body>\n"
            + "  <textarea id='t' onkeypress='handleKey(event)'></textarea>\n"
            + "</body>\n"
            + "</html>";

        final WebDriver driver = loadPage2(html);
        final WebElement field = driver.findElement(By.id("t"));

        field.sendKeys(Keys.ENTER);

        verifyAlerts(driver, getExpectedAlerts());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"keydown:16,0,16,Shift,undefined,ShiftLeft,true",
                    "keydown:65,0,65,A,undefined,KeyA,true",
                    "keypress:65,65,65,A,undefined,KeyA,true",
                    "keyup:65,0,65,A,undefined,KeyA,true",
                    "keyup:16,0,16,Shift,undefined,ShiftLeft,false",
                    "keydown:65,0,65,a,undefined,KeyA,false",
                    "keypress:97,97,97,a,undefined,KeyA,false",
                    "keyup:65,0,65,a,undefined,KeyA,false",
                    "keydown:190,0,190,.,undefined,Period,false",
                    "keypress:46,46,46,.,undefined,Period,false",
                    "keyup:190,0,190,.,undefined,Period,false",
                    "keydown:13,0,13,Enter,undefined,Enter,false",
                    "keypress:13,13,13,Enter,undefined,Enter,false",
                    "keyup:13,0,13,Enter,undefined,Enter,false"},
            FF = {  "keydown:16,0,16,Shift,undefined,ShiftLeft,true",
                    "keydown:65,0,65,A,undefined,KeyA,true",
                    "keypress:0,65,65,A,undefined,KeyA,true",
                    "keyup:65,0,65,A,undefined,KeyA,true",
                    "keyup:16,0,16,Shift,undefined,ShiftLeft,false",
                    "keydown:65,0,65,a,undefined,KeyA,false",
                    "keypress:0,97,97,a,undefined,KeyA,false",
                    "keyup:65,0,65,a,undefined,KeyA,false",
                    "keydown:190,0,190,.,undefined,Period,false",
                    "keypress:0,46,46,.,undefined,Period,false",
                    "keyup:190,0,190,.,undefined,Period,false",
                    "keydown:13,0,13,Enter,undefined,Enter,false",
                    "keypress:13,0,13,Enter,undefined,Enter,false",
                    "keyup:13,0,13,Enter,undefined,Enter,false"},
            FF78 = {  "keydown:16,0,16,Shift,undefined,ShiftLeft,true",
                    "keydown:65,0,65,A,undefined,KeyA,true",
                    "keypress:0,65,65,A,undefined,KeyA,true",
                    "keyup:65,0,65,A,undefined,KeyA,true",
                    "keyup:16,0,16,Shift,undefined,ShiftLeft,false",
                    "keydown:65,0,65,a,undefined,KeyA,false",
                    "keypress:0,97,97,a,undefined,KeyA,false",
                    "keyup:65,0,65,a,undefined,KeyA,false",
                    "keydown:190,0,190,.,undefined,Period,false",
                    "keypress:0,46,46,.,undefined,Period,false",
                    "keyup:190,0,190,.,undefined,Period,false",
                    "keydown:13,0,13,Enter,undefined,Enter,false",
                    "keypress:13,0,13,Enter,undefined,Enter,false",
                    "keyup:13,0,13,Enter,undefined,Enter,false"},
            IE = {  "keydown:16,0,16,Shift,,undefined,true",
                    "keydown:65,0,65,A,A,undefined,true",
                    "keypress:65,65,65,A,A,undefined,true",
                    "keyup:65,0,65,A,A,undefined,true",
                    "keyup:16,0,16,Shift,,undefined,false",
                    "keydown:65,0,65,a,a,undefined,false",
                    "keypress:97,97,97,a,a,undefined,false",
                    "keyup:65,0,65,a,a,undefined,false",
                    "keydown:190,0,190,.,.,undefined,false",
                    "keypress:46,46,46,.,.,undefined,false",
                    "keyup:190,0,190,.,.,undefined,false",
                    "keydown:13,0,13,Enter,\\n,undefined,false",
                    "keypress:13,13,13,Enter,\\n,undefined,false",
                    "keyup:13,0,13,Enter,\\n,undefined,false"}
            )
    // https://github.com/SeleniumHQ/selenium/issues/2531
    @BuggyWebDriver(CHROME = {"keydown:16,0,16,Shift,undefined,ShiftLeft,false",
                            "keydown:65,0,65,A,undefined,KeyA,true",
                            "keypress:65,65,65,A,undefined,KeyA,true",
                            "keyup:65,0,65,A,undefined,KeyA,true",
                            "keyup:16,0,16,Shift,undefined,ShiftLeft,false",
                            "keydown:65,0,65,a,undefined,KeyA,false",
                            "keypress:97,97,97,a,undefined,KeyA,false",
                            "keyup:65,0,65,a,undefined,KeyA,false",
                            "keydown:190,0,190,.,undefined,Period,false",
                            "keypress:46,46,46,.,undefined,Period,false",
                            "keyup:190,0,190,.,undefined,Period,false",
                            "keydown:13,0,13,Enter,undefined,Enter,false",
                            "keypress:13,13,13,Enter,undefined,Enter,false",
                            "keyup:13,0,13,Enter,undefined,Enter,false"},
                    EDGE = {"keydown:16,0,16,Shift,undefined,ShiftLeft,false",
                            "keydown:65,0,65,A,undefined,KeyA,true",
                            "keypress:65,65,65,A,undefined,KeyA,true",
                            "keyup:65,0,65,A,undefined,KeyA,true",
                            "keyup:16,0,16,Shift,undefined,ShiftLeft,false",
                            "keydown:65,0,65,a,undefined,KeyA,false",
                            "keypress:97,97,97,a,undefined,KeyA,false",
                            "keyup:65,0,65,a,undefined,KeyA,false",
                            "keydown:190,0,190,.,undefined,Period,false",
                            "keypress:46,46,46,.,undefined,Period,false",
                            "keyup:190,0,190,.,undefined,Period,false",
                            "keydown:13,0,13,Enter,undefined,Enter,false",
                            "keypress:13,13,13,Enter,undefined,Enter,false",
                            "keyup:13,0,13,Enter,undefined,Enter,false"},
                    FF78 = {  "keydown:65,0,65,A,undefined,,false",
                            "keypress:65,65,65,A,undefined,,false",
                            "keyup:65,0,65,A,undefined,,false",
                            "keydown:65,0,65,a,undefined,,false",
                            "keypress:97,97,97,a,undefined,,false",
                            "keyup:65,0,65,a,undefined,,false",
                            "keydown:190,0,190,.,undefined,,false",
                            "keypress:46,46,46,.,undefined,,false",
                            "keyup:190,0,190,.,undefined,,false",
                            "keydown:13,0,13,Enter,undefined,,false",
                            "keypress:13,13,13,Enter,undefined,,false",
                            "keyup:13,0,13,Enter,undefined,,false"},
                    FF = {  "keydown:65,0,65,A,undefined,,false",
                            "keypress:65,65,65,A,undefined,,false",
                            "keyup:65,0,65,A,undefined,,false",
                            "keydown:65,0,65,a,undefined,,false",
                            "keypress:97,97,97,a,undefined,,false",
                            "keyup:65,0,65,a,undefined,,false",
                            "keydown:190,0,190,.,undefined,,false",
                            "keypress:46,46,46,.,undefined,,false",
                            "keyup:190,0,190,.,undefined,,false",
                            "keydown:13,0,13,Enter,undefined,,false",
                            "keypress:13,13,13,Enter,undefined,,false",
                            "keyup:13,0,13,Enter,undefined,,false"},
                    IE = {  "keydown:16,0,16,Shift,,undefined,false",
                            "keydown:65,0,65,A,A,undefined,false",
                            "keypress:65,65,65,A,A,undefined,false",
                            "keyup:65,0,65,a,a,undefined,false",
                            "keyup:16,0,16,Shift,,undefined,false",
                            "keydown:65,0,65,a,a,undefined,false",
                            "keypress:97,97,97,a,a,undefined,false",
                            "keyup:65,0,65,a,a,undefined,false",
                            "keydown:190,0,190,.,.,undefined,false",
                            "keypress:46,46,46,.,.,undefined,false",
                            "keyup:190,0,190,.,.,undefined,false",
                            "keydown:13,0,13,Enter,\\n,undefined,false",
                            "keypress:13,13,13,\\r,\\r,undefined,false",
                            "keyup:13,0,13,Enter,\\n,undefined,false"})
    public void which() throws Exception {
        final String html
            = "<html><head></head><body>\n"
            + "<input type='text' id='keyId'>\n"
            + "<script>\n"
            + "function handler(e) {\n"
            + "  e = e ? e : window.event;\n"
            + "  var log = e.type + ':' + e.keyCode + ',' + e.charCode + ',' + e.which + ',' "
                            + "+ e.key + ',' + e.char + ',' + e.code + ',' + e.shiftKey;\n"
            + "  log = log.replace(/\\r/g, '\\\\r');\n"
            + "  log = log.replace(/\\n/g, '\\\\n');\n"
            + "  document.getElementById('myTextarea').value += log + '\\n';"
            + "}\n"
            + "document.getElementById('keyId').onkeyup = handler;\n"
            + "document.getElementById('keyId').onkeydown = handler;\n"
            + "document.getElementById('keyId').onkeypress = handler;\n"
            + "</script>\n"
            + "<textarea id='myTextarea' cols=80 rows=20></textarea>\n"
            + "</body></html>";

        final String keysToSend = "Aa." + Keys.RETURN;
        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("keyId")).sendKeys(keysToSend);

        final String[] actual = driver.findElement(By.id("myTextarea")).getAttribute("value").split("\n");
        assertEquals(Arrays.asList(getExpectedAlerts()).toString(), Arrays.asList(actual).toString());
    }
}
