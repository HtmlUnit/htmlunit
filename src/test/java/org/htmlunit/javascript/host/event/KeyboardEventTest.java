/*
 * Copyright (c) 2002-2026 Gargoyle Software Inc.
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
import org.htmlunit.junit.annotation.BuggyWebDriver;
import org.htmlunit.junit.annotation.HtmlUnitNYI;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * Tests for {@link KeyboardEvent}.
 *
 * @author Ahmed Ashour
 * @author Marc Guillemot
 * @author Frank Danek
 * @author Ronald Brill
 * @author Joerg Werner
 */
public class KeyboardEventTest extends WebDriverTestCase {

    private static final String DUMP_EVENT_FUNCTION = "  function dump(event) {\n"
            + "    log(event);\n"
            + "    log(event.type);\n"
            + "    log(event.bubbles);\n"
            + "    log(event.cancelable);\n"
            + "    log(event.composed);\n"

            + "    var details = [event.key, event.code, event.location, event.ctrlKey,\n"
            + "                 event.shiftKey, event.altKey, event.metaKey, event.repeat, \n"
            + "                 event.isComposing, event.charCode, event.which].join(',');\n"
            + "    log(details);\n"
            + "  }\n";

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[object KeyboardEvent]", "type", "false", "false", "false",
             ",,0,false,false,false,false,false,false,0,0"})
    public void create_ctor() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head><script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    try {\n"
                + "      var event = new KeyboardEvent('type');\n"
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
    @Alerts("TypeError")
    @HtmlUnitNYI(CHROME = {"[object KeyboardEvent]", "undefined", "false", "false", "false",
                           ",,0,false,false,false,false,false,false,0,0"},
            EDGE = {"[object KeyboardEvent]", "undefined", "false", "false", "false",
                    ",,0,false,false,false,false,false,false,0,0"},
            FF = {"[object KeyboardEvent]", "undefined", "false", "false", "false",
                  ",,0,false,false,false,false,false,false,0,0"},
            FF_ESR = {"[object KeyboardEvent]", "undefined", "false", "false", "false",
                      ",,0,false,false,false,false,false,false,0,0"})
    public void create_ctorWithoutType() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    try {\n"
            + "      var event = new KeyboardEvent();\n"
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
    @Alerts({"[object KeyboardEvent]", "42", "false", "false", "false",
             ",,0,false,false,false,false,false,false,0,0"})
    public void create_ctorNumericType() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    try {\n"
            + "      var event = new KeyboardEvent(42);\n"
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
    @Alerts({"[object KeyboardEvent]", "null", "false", "false", "false",
             ",,0,false,false,false,false,false,false,0,0"})
    public void create_ctorNullType() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    try {\n"
            + "      var event = new KeyboardEvent(null);\n"
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
    @Alerts("ReferenceError")
    public void create_ctorUnknownType() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    try {\n"
            + "      var event = new KeyboardEvent(unknown);\n"
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
    @Alerts({"[object KeyboardEvent]", "HtmlUnitEvent", "false", "false", "false",
             ",,0,false,false,false,false,false,false,0,0"})
    public void create_ctorArbitraryType() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    try {\n"
            + "      var event = new KeyboardEvent('HtmlUnitEvent');\n"
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
    @Alerts(DEFAULT = {"[object KeyboardEvent]", "keyboard", "false", "false", "false",
                       "key,code,123,true,true,true,true,true,true,456,789"},
            CHROME = {"[object KeyboardEvent]", "keyboard", "false", "false", "false",
                      "key,code,123,true,true,true,true,true,true,456,0"},
            EDGE = {"[object KeyboardEvent]", "keyboard", "false", "false", "false",
                    "key,code,123,true,true,true,true,true,true,456,0"})
    public void create_ctorAllDetails() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    try {\n"
            + "      var event = new KeyboardEvent('keyboard', "
                             + "{ key: 'key', code: 'code', location: 123, "
                             + "ctrlKey: true, shiftKey: true, altKey: true, metaKey: true,"
                             + "repeat: true, isComposing: true, charCode: 456, which: 789 });\n"
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
    @Alerts({"[object KeyboardEvent]", "keyboard", "false", "false", "false",
             "null,,0,true,false,false,false,false,false,456,0"})
    public void create_ctorSomeDetails() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    try {\n"
            + "      var event = new KeyboardEvent('keyboard', "
                             + "{ key: null, code: undefined, ctrlKey: true, charCode: 456 });\n"
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
    @Alerts({"[object KeyboardEvent]", "keyboard", "false", "false", "false",
             ",,0,false,false,false,false,false,false,0,0"})
    public void create_ctorMissingData() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    try {\n"
            + "      var event = new KeyboardEvent('keyboard', {\n"
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
    @Alerts({"[object KeyboardEvent]", "keyboard", "false", "false", "false",
             ",,0,false,false,false,false,false,false,0,0"})
    public void create_ctorNullData() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    try {\n"
            + "      var event = new KeyboardEvent('keyboard', null);\n"
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
    @Alerts({"[object KeyboardEvent]", "keyboard", "false", "false", "false",
             ",,0,false,false,false,false,false,false,0,0"})
    public void create_ctorUndefinedData() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    try {\n"
            + "      var event = new KeyboardEvent('keyboard', undefined);\n"
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
    @Alerts({"[object KeyboardEvent]", "keyboard", "false", "false", "false",
             ",,0,false,false,false,false,false,false,0,0"})
    public void create_ctorWrongData() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    try {\n"
            + "      var event = new KeyboardEvent('keyboard', {\n"
            + "        'data': ['Html', 'Unit']\n"
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
    @Alerts({"DOM3: [object KeyboardEvent]", "vendor: exception"})
    public void createEvent() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    try {\n"
            + "      log('DOM3: ' + document.createEvent('KeyboardEvent'));\n"
            + "    } catch(e) {log('DOM3: exception')}\n"
            + "    try {\n"
            + "      log('vendor: ' + document.createEvent('KeyEvents'));\n"
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
    @Alerts({"NotSupportedError/DOMException", "0-0", "undefined-undefined"})
    public void keyCode() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    try {\n"
            + "      var keyEvent = document.createEvent('KeyEvents');\n"
            + "      log(keyEvent.keyCode + '-' + keyEvent.charCode);\n"
            + "    } catch(e) { logEx(e) }\n"
            + "    try {\n"
            + "      var keyEvent = document.createEvent('KeyboardEvent');\n"
            + "      log(keyEvent.keyCode + '-' + keyEvent.charCode);\n"
            + "    } catch(e) { logEx(e) }\n"
            + "    try {\n"
            + "      var mouseEvent = document.createEvent('MouseEvents');\n"
            + "      log(mouseEvent.keyCode + '-' + mouseEvent.charCode);\n"
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
    @Alerts({"NotSupportedError/DOMException", "TypeError"})
    public void initKeyEvent() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  var properties = ['type', 'bubbles', 'cancelable', /*'view',*/ 'ctrlKey', 'altKey',\n"
            + "        'shiftKey', 'metaKey', 'keyCode', 'charCode'];\n"
            + "  function dumpEvent(e) {\n"
            + "    var str = '';\n"
            + "    for (var i = 0; i < properties.length; i++) str += ', ' + e[properties[i]];\n"
            + "    log(str.substring(2));\n"
            + "  }\n"
            + "  function test() {\n"
            + "    try {\n"
            + "      var keyEvent = document.createEvent('KeyEvents');\n"
            + "      keyEvent.initKeyEvent('keydown', true, true, null, true, true, true, true, 65, 65);\n"
            + "      dumpEvent(keyEvent);\n"
            + "      keyEvent = document.createEvent('KeyEvents');\n"
            + "      keyEvent.initKeyEvent('keyup', false, false, null, false, false, false, false, 32, 32);\n"
            + "      dumpEvent(keyEvent);\n"
            + "    } catch(e) { logEx(e) }\n"
            + "    try {\n"
            + "      var keyEvent = document.createEvent('KeyboardEvent');\n"
            + "      keyEvent.initKeyEvent('keydown', true, true, null, true, true, true, true, 65, 65);\n"
            + "      dumpEvent(keyEvent);\n"
            + "      keyEvent = document.createEvent('KeyboardEvent');\n"
            + "      keyEvent.initKeyEvent('keyup', false, false, null, false, false, false, false, 32, 32);\n"
            + "      dumpEvent(keyEvent);\n"
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
    @Alerts(DEFAULT = {"NotSupportedError/DOMException",
                       "keydown, true, true, , 0, true, true, true, true, 0, 0",
                       "keyup, false, false, , 7, false, false, false, false, 0, 0"},
            FF = {"NotSupportedError/DOMException",
                  "keydown, true, true, Fn, 0, true, true, true, true, 0, 0",
                  "keyup, false, false, , 7, false, false, false, false, 0, 0"},
            FF_ESR = {"NotSupportedError/DOMException",
                      "keydown, true, true, Fn, 0, true, true, true, true, 0, 0",
                      "keyup, false, false, , 7, false, false, false, false, 0, 0"})
    @HtmlUnitNYI(CHROME = {"NotSupportedError/DOMException",
                           "keydown, true, true, Fn, 0, true, true, true, true, 0, 0",
                           "keyup, false, false, , 7, false, false, false, false, 0, 0"},
                 EDGE = {"NotSupportedError/DOMException",
                         "keydown, true, true, Fn, 0, true, true, true, true, 0, 0",
                         "keyup, false, false, , 7, false, false, false, false, 0, 0"})
    public void initKeyboardEvent() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  var properties = ['type', 'bubbles', 'cancelable', /*'view',*/ 'key', 'location',"
            + "        'ctrlKey', 'altKey', 'shiftKey', 'metaKey', 'keyCode', 'charCode'];\n"
            + "  function dumpEvent(e) {\n"
            + "    var str = '';\n"
            + "    for (var i = 0; i < properties.length; i++) str += ', ' + e[properties[i]];\n"
            + "    log(str.substring(2));\n"
            + "  }\n"
            + "  function test() {\n"
            + "    try {\n"
            + "      var keyEvent = document.createEvent('KeyEvents');\n"
            + "      keyEvent.initKeyboardEvent('keydown', true, true, null, 'Fn', 0, true, true, true, true);\n"
            + "      dumpEvent(keyEvent);\n"
            + "      keyEvent = document.createEvent('KeyEvents');\n"
            + "      keyEvent.initKeyboardEvent('keyup', false, false, null, '', 7, false, false, false, false);\n"
            + "      dumpEvent(keyEvent);\n"
            + "    } catch(e) { logEx(e) }\n"
            + "    try {\n"
            + "      var keyEvent = document.createEvent('KeyboardEvent');\n"
            + "      keyEvent.initKeyboardEvent('keydown', true, true, null, 'Fn', 0, true, true, true, true);\n"
            + "      dumpEvent(keyEvent);\n"
            + "      keyEvent = document.createEvent('KeyboardEvent');\n"
            + "      keyEvent.initKeyboardEvent('keyup', false, false, null, '', 7, false, false, false, false);\n"
            + "      dumpEvent(keyEvent);\n"
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
    @Alerts({"32", "48", "49", "50", "51", "52", "53", "54", "55", "56", "57"})
    public void keyCodes_keyup() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TEXTAREA_FUNCTION
            + "</script>\n"
            + "</head>\n"
            + "<body>\n"
            + "  <input id='t' onkeyup='log(event.keyCode)'/>\n"
            + LOG_TEXTAREA
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        final WebElement field = driver.findElement(By.id("t"));

        field.sendKeys(" 0123456789");
        verifyTextArea2(driver, getExpectedAlerts());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"65", "66", "67", "68", "69",
             "70", "71", "72", "73", "74", "75", "76", "77", "78", "79",
             "80", "81", "82", "83", "84", "85", "86", "87", "88", "89", "90"})
    public void keyCodes2_keyup() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TEXTAREA_FUNCTION
            + "</script>\n"
            + "</head>\n"
            + "<body>\n"
            + "  <input id='t' onkeyup='log(event.keyCode)'/>\n"
            + LOG_TEXTAREA
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        final WebElement field = driver.findElement(By.id("t"));

        field.sendKeys("abcdefghijklmnopqrstuvwxyz");

        verifyTextArea2(driver, getExpectedAlerts());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"32", "48", "49", "50", "51", "52", "53", "54", "55", "56", "57"})
    public void keyCodes_keydown() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TEXTAREA_FUNCTION
            + "</script>\n"
            + "</head>\n"
            + "<body>\n"
            + "  <input id='t' onkeydown='log(event.keyCode)'/>\n"
            + LOG_TEXTAREA
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        final WebElement field = driver.findElement(By.id("t"));

        field.sendKeys(" 0123456789");

        verifyTextArea2(driver, getExpectedAlerts());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"65", "66", "67", "68", "69",
             "70", "71", "72", "73", "74", "75", "76", "77", "78", "79",
             "80", "81", "82", "83", "84", "85", "86", "87", "88", "89", "90"})
    public void keyCodes2_keydown() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TEXTAREA_FUNCTION
            + "</script>\n"
            + "</head>\n"
            + "<body>\n"
            + "  <input id='t' onkeydown='log(event.keyCode)'/>\n"
            + LOG_TEXTAREA
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        final WebElement field = driver.findElement(By.id("t"));

        field.sendKeys("abcdefghijklmnopqrstuvwxyz");

        verifyTextArea2(driver, getExpectedAlerts());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"32", "48", "49", "50", "51", "52", "53", "54", "55", "56", "57"})
    public void keyCodes_keypress() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TEXTAREA_FUNCTION
            + "</script>\n"
            + "</head>\n"
            + "<body>\n"
            + "  <input id='t' onkeypress='log(event.charCode)'/>\n"
            + LOG_TEXTAREA
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        final WebElement field = driver.findElement(By.id("t"));

        field.sendKeys(" 0123456789");

        verifyTextArea2(driver, getExpectedAlerts());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"97", "98", "99",
             "100", "101", "102", "103", "104", "105", "106", "107", "108", "109",
             "110", "111", "112", "113", "114", "115", "116", "117", "118", "119",
             "120", "121", "122"})
    public void keyCodes2_keypress() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TEXTAREA_FUNCTION
            + "</script>\n"
            + "</head>\n"
            + "<body>\n"
            + "  <input id='t' onkeypress='log(event.charCode)'/>\n"
            + LOG_TEXTAREA
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        final WebElement field = driver.findElement(By.id("t"));

        field.sendKeys("abcdefghijklmnopqrstuvwxyz");

        verifyTextArea2(driver, getExpectedAlerts());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"13", "13", "13"})
    public void keyCodeEnter_keypress() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function handleKey(e) {\n"
            + "  log(e.charCode);\n"
            + "  log(e.keyCode);\n"
            + "  log(e.which);\n"
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

        verifyTitle2(driver, getExpectedAlerts());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"keydown:16,0,16,Shift,undefined,ShiftLeft,true",
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
             "keyup:13,0,13,Enter,undefined,Enter,false"})
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
                    FF_ESR = {"keydown:65,0,65,A,undefined,KeyA,false",
                              "keypress:65,65,65,A,undefined,KeyA,false",
                              "keyup:65,0,65,A,undefined,KeyA,false",
                              "keydown:65,0,65,a,undefined,KeyA,false",
                              "keypress:97,97,97,a,undefined,KeyA,false",
                              "keyup:65,0,65,a,undefined,KeyA,false",
                              "keydown:190,0,190,.,undefined,Period,false",
                              "keypress:46,46,46,.,undefined,Period,false",
                              "keyup:190,0,190,.,undefined,Period,false",
                              "keydown:13,0,13,Enter,undefined,Enter,false",
                              "keypress:13,13,13,Enter,undefined,Enter,false",
                              "keyup:13,0,13,Enter,undefined,Enter,false"},
                    FF = {"keydown:65,0,65,A,undefined,KeyA,false",
                          "keypress:65,65,65,A,undefined,KeyA,false",
                          "keyup:65,0,65,A,undefined,KeyA,false",
                          "keydown:65,0,65,a,undefined,KeyA,false",
                          "keypress:97,97,97,a,undefined,KeyA,false",
                          "keyup:65,0,65,a,undefined,KeyA,false",
                          "keydown:190,0,190,.,undefined,Period,false",
                          "keypress:46,46,46,.,undefined,Period,false",
                          "keyup:190,0,190,.,undefined,Period,false",
                          "keydown:13,0,13,Enter,undefined,Enter,false",
                          "keypress:13,13,13,Enter,undefined,Enter,false",
                          "keyup:13,0,13,Enter,undefined,Enter,false"})
    public void which() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head></head><body>\n"
            + "<input type='text' id='keyId'>\n"
            + "<script>\n"
            + LOG_TEXTAREA_FUNCTION
            + "function handler(e) {\n"
            + "  e = e ? e : window.event;\n"
            + "  var msg = e.type + ':' + e.keyCode + ',' + e.charCode + ',' + e.which + ',' "
                            + "+ e.key + ',' + e.char + ',' + e.code + ',' + e.shiftKey;\n"
            + "  msg = msg.replace(/\\r/g, '\\\\r');\n"
            + "  msg = msg.replace(/\\n/g, '\\\\n');\n"
            + "  log(msg);"
            + "}\n"
            + "document.getElementById('keyId').onkeyup = handler;\n"
            + "document.getElementById('keyId').onkeydown = handler;\n"
            + "document.getElementById('keyId').onkeypress = handler;\n"
            + "</script>\n"
            + LOG_TEXTAREA
            + "</body></html>";

        final String keysToSend = "Aa." + Keys.RETURN;
        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("keyId")).sendKeys(keysToSend);

        verifyTextArea2(driver, getExpectedAlerts());
    }

    /**
     * Logs the key events for the arrow keys.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"keydown:ArrowUp,ArrowUp,38,0,38,0", "keyup:ArrowUp,ArrowUp,38,0,38,0",
             "keydown:ArrowDown,ArrowDown,40,0,40,0", "keyup:ArrowDown,ArrowDown,40,0,40,0",
             "keydown:ArrowLeft,ArrowLeft,37,0,37,0", "keyup:ArrowLeft,ArrowLeft,37,0,37,0",
             "keydown:ArrowRight,ArrowRight,39,0,39,0", "keyup:ArrowRight,ArrowRight,39,0,39,0"})
    public void specialKeysArrows() throws Exception {
        keyEvents("input", "abc", false, Keys.ARROW_UP, Keys.ARROW_DOWN, Keys.ARROW_LEFT, Keys.ARROW_RIGHT);
    }

    /**
     * Logs the key events for the navigation keys.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"keydown:Home,Home,36,0,36,0", "keyup:Home,Home,36,0,36,0", "keydown:End,End,35,0,35,0",
             "keyup:End,End,35,0,35,0", "keydown:PageUp,PageUp,33,0,33,0", "keyup:PageUp,PageUp,33,0,33,0",
             "keydown:PageDown,PageDown,34,0,34,0", "keyup:PageDown,PageDown,34,0,34,0"})
    public void specialKeysNavigation() throws Exception {
        keyEvents("input", "abc", false, Keys.HOME, Keys.END, Keys.PAGE_UP, Keys.PAGE_DOWN);
    }

    /**
     * Logs the key events for backspace at the end of the text.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"keydown:Backspace,Backspace,8,0,8,0", "input:ab", "keyup:Backspace,Backspace,8,0,8,0"})
    public void specialKeysBackspace() throws Exception {
        keyEvents("input", "abc", false, Keys.BACK_SPACE);
    }

    /**
     * Logs the key events for backspace in an empty field.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"keydown:Backspace,Backspace,8,0,8,0", "keyup:Backspace,Backspace,8,0,8,0"})
    public void specialKeysBackspaceEmpty() throws Exception {
        keyEvents("input", "", false, Keys.BACK_SPACE);
    }

    /**
     * Logs the key events for backspace if the keydown event is canceled.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"keydown:Backspace,Backspace,8,0,8,0", "keyup:Backspace,Backspace,8,0,8,0"})
    public void specialKeysBackspacePrevented() throws Exception {
        keyEvents("input", "abc", true, Keys.BACK_SPACE);
    }

    /**
     * Logs the key events for delete at the end of the text.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"keydown:Delete,Delete,46,0,46,0", "keyup:Delete,Delete,46,0,46,0"})
    public void specialKeysDeleteAtEnd() throws Exception {
        keyEvents("input", "abc", false, Keys.DELETE);
    }

    /**
     * Logs the key events for delete at the start of the text.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"keydown:Home,Home,36,0,36,0", "keyup:Home,Home,36,0,36,0", "keydown:Delete,Delete,46,0,46,0", "input:bc",
             "keyup:Delete,Delete,46,0,46,0"})
    public void specialKeysDeleteAtStart() throws Exception {
        keyEvents("input", "abc", false, Keys.HOME, Keys.DELETE);
    }

    /**
     * Logs the key events for the insert and the escape key.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"keydown:Insert,Insert,45,0,45,0", "keyup:Insert,Insert,45,0,45,0", "keydown:Escape,Escape,27,0,27,0",
             "keyup:Escape,Escape,27,0,27,0"})
    public void specialKeysInsertEscape() throws Exception {
        keyEvents("input", "abc", false, Keys.INSERT, Keys.ESCAPE);
    }

    /**
     * Logs the key events for some function keys.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"keydown:F1,F1,112,0,112,0", "keyup:F1,F1,112,0,112,0", "keydown:F2,F2,113,0,113,0",
             "keyup:F2,F2,113,0,113,0", "keydown:F12,F12,123,0,123,0", "keyup:F12,F12,123,0,123,0"})
    public void specialKeysFunction() throws Exception {
        keyEvents("input", "abc", false, Keys.F1, Keys.F2, Keys.F12);
    }

    /**
     * Logs the key events for the tab key.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("keydown:Tab,Tab,9,0,9,0")
    // in real browsers the keyup event is fired at the newly focused element
    @HtmlUnitNYI(CHROME = {"keydown:Tab,Tab,9,0,9,0", "keyup:Tab,Tab,9,0,9,0"},
            EDGE = {"keydown:Tab,Tab,9,0,9,0", "keyup:Tab,Tab,9,0,9,0"},
            FF = {"keydown:Tab,Tab,9,0,9,0", "keyup:Tab,Tab,9,0,9,0"},
            FF_ESR = {"keydown:Tab,Tab,9,0,9,0", "keyup:Tab,Tab,9,0,9,0"})
    public void specialKeysTab() throws Exception {
        keyEvents("input", "abc", false, Keys.TAB);
    }

    /**
     * Logs the key events for enter in an input field.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"keydown:Enter,Enter,13,0,13,0", "keypress:Enter,Enter,13,13,13,0",
                       "keyup:Enter,Enter,13,0,13,0"},
            FF = {"keydown:Enter,NumpadEnter,13,0,13,1", "keypress:Enter,NumpadEnter,13,13,13,1",
                  "keyup:Enter,NumpadEnter,13,0,13,1"},
            FF_ESR = {"keydown:Enter,NumpadEnter,13,0,13,1", "keypress:Enter,NumpadEnter,13,13,13,1",
                      "keyup:Enter,NumpadEnter,13,0,13,1"})
    // geckodriver sends the enter key as numpad enter
    @HtmlUnitNYI(FF = {"keydown:Enter,Enter,13,0,13,0", "keypress:Enter,Enter,13,13,13,0",
                       "keyup:Enter,Enter,13,0,13,0"},
            FF_ESR = {"keydown:Enter,Enter,13,0,13,0", "keypress:Enter,Enter,13,13,13,0",
                      "keyup:Enter,Enter,13,0,13,0"})
    public void specialKeysEnterInput() throws Exception {
        keyEvents("input", "abc", false, Keys.ENTER);
    }

    /**
     * Logs the key events for enter in a textarea.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"keydown:Enter,Enter,13,0,13,0", "keypress:Enter,Enter,13,13,13,0", "input:abc\\n",
                       "keyup:Enter,Enter,13,0,13,0"},
            FF = {"keydown:Enter,NumpadEnter,13,0,13,1", "keypress:Enter,NumpadEnter,13,13,13,1", "input:abc\\n",
                  "keyup:Enter,NumpadEnter,13,0,13,1"},
            FF_ESR = {"keydown:Enter,NumpadEnter,13,0,13,1", "keypress:Enter,NumpadEnter,13,13,13,1", "input:abc\\n",
                      "keyup:Enter,NumpadEnter,13,0,13,1"})
    // geckodriver sends the enter key as numpad enter
    @HtmlUnitNYI(FF = {"keydown:Enter,Enter,13,0,13,0", "keypress:Enter,Enter,13,13,13,0", "input:abc\\n",
                       "keyup:Enter,Enter,13,0,13,0"},
            FF_ESR = {"keydown:Enter,Enter,13,0,13,0", "keypress:Enter,Enter,13,13,13,0", "input:abc\\n",
                      "keyup:Enter,Enter,13,0,13,0"})
    public void specialKeysEnterTextArea() throws Exception {
        keyEvents("textarea", "abc", false, Keys.ENTER);
    }

    /**
     * Logs the key events for the numpad keys.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"keydown:0,Numpad0,96,0,96,0", "keypress:0,Numpad0,48,48,48,0", "input:0",
                       "keyup:0,Numpad0,96,0,96,0", "keydown:5,Numpad5,101,0,101,0", "keypress:5,Numpad5,53,53,53,0",
                       "input:05", "keyup:5,Numpad5,101,0,101,0", "keydown:9,Numpad9,105,0,105,0",
                       "keypress:9,Numpad9,57,57,57,0", "input:059", "keyup:9,Numpad9,105,0,105,0",
                       "keydown:*,NumpadMultiply,106,0,106,0", "keypress:*,NumpadMultiply,42,42,42,0", "input:059*",
                       "keyup:*,NumpadMultiply,106,0,106,0", "keydown:+,NumpadAdd,107,0,107,0",
                       "keypress:+,NumpadAdd,43,43,43,0", "input:059*+", "keyup:+,NumpadAdd,107,0,107,0",
                       "keydown:-,NumpadSubtract,109,0,109,0", "keypress:-,NumpadSubtract,45,45,45,0", "input:059*+-",
                       "keyup:-,NumpadSubtract,109,0,109,0", "keydown:.,NumpadDecimal,110,0,110,0",
                       "keypress:.,NumpadDecimal,46,46,46,0", "input:059*+-.", "keyup:.,NumpadDecimal,110,0,110,0",
                       "keydown:/,NumpadDivide,111,0,111,0", "keypress:/,NumpadDivide,47,47,47,0", "input:059*+-./",
                       "keyup:/,NumpadDivide,111,0,111,0"},
            FF = {"keydown:0,Numpad0,96,0,96,3", "keypress:0,Numpad0,48,48,48,3", "input:0",
                  "keyup:0,Numpad0,96,0,96,3", "keydown:5,Numpad5,101,0,101,3", "keypress:5,Numpad5,53,53,53,3",
                  "input:05", "keyup:5,Numpad5,101,0,101,3", "keydown:9,Numpad9,105,0,105,3",
                  "keypress:9,Numpad9,57,57,57,3", "input:059", "keyup:9,Numpad9,105,0,105,3",
                  "keydown:*,NumpadMultiply,106,0,106,3", "keypress:*,NumpadMultiply,42,42,42,3", "input:059*",
                  "keyup:*,NumpadMultiply,106,0,106,3", "keydown:+,NumpadAdd,107,0,107,3",
                  "keypress:+,NumpadAdd,43,43,43,3", "input:059*+", "keyup:+,NumpadAdd,107,0,107,3",
                  "keydown:-,NumpadSubtract,109,0,109,3", "keypress:-,NumpadSubtract,45,45,45,3", "input:059*+-",
                  "keyup:-,NumpadSubtract,109,0,109,3", "keydown:.,NumpadDecimal,110,0,110,3",
                  "keypress:.,NumpadDecimal,46,46,46,3", "input:059*+-.", "keyup:.,NumpadDecimal,110,0,110,3",
                  "keydown:/,NumpadDivide,111,0,111,3", "keypress:/,NumpadDivide,47,47,47,3", "input:059*+-./",
                  "keyup:/,NumpadDivide,111,0,111,3"},
            FF_ESR = {"keydown:0,Numpad0,96,0,96,3", "keypress:0,Numpad0,48,48,48,3", "input:0",
                      "keyup:0,Numpad0,96,0,96,3", "keydown:5,Numpad5,101,0,101,3", "keypress:5,Numpad5,53,53,53,3",
                      "input:05", "keyup:5,Numpad5,101,0,101,3", "keydown:9,Numpad9,105,0,105,3",
                      "keypress:9,Numpad9,57,57,57,3", "input:059", "keyup:9,Numpad9,105,0,105,3",
                      "keydown:*,NumpadMultiply,106,0,106,3", "keypress:*,NumpadMultiply,42,42,42,3", "input:059*",
                      "keyup:*,NumpadMultiply,106,0,106,3", "keydown:+,NumpadAdd,107,0,107,3",
                      "keypress:+,NumpadAdd,43,43,43,3", "input:059*+", "keyup:+,NumpadAdd,107,0,107,3",
                      "keydown:-,NumpadSubtract,109,0,109,3", "keypress:-,NumpadSubtract,45,45,45,3", "input:059*+-",
                      "keyup:-,NumpadSubtract,109,0,109,3", "keydown:.,NumpadDecimal,110,0,110,3",
                      "keypress:.,NumpadDecimal,46,46,46,3", "input:059*+-.", "keyup:.,NumpadDecimal,110,0,110,3",
                      "keydown:/,NumpadDivide,111,0,111,3", "keypress:/,NumpadDivide,47,47,47,3", "input:059*+-./",
                      "keyup:/,NumpadDivide,111,0,111,3"})
    // chromedriver does not set the location of the key events
    @HtmlUnitNYI(CHROME = {"keydown:0,Numpad0,96,0,96,3", "keypress:0,Numpad0,48,48,48,3", "input:0",
                           "keyup:0,Numpad0,96,0,96,3", "keydown:5,Numpad5,101,0,101,3",
                           "keypress:5,Numpad5,53,53,53,3", "input:05", "keyup:5,Numpad5,101,0,101,3",
                           "keydown:9,Numpad9,105,0,105,3", "keypress:9,Numpad9,57,57,57,3", "input:059",
                           "keyup:9,Numpad9,105,0,105,3", "keydown:*,NumpadMultiply,106,0,106,3",
                           "keypress:*,NumpadMultiply,42,42,42,3", "input:059*", "keyup:*,NumpadMultiply,106,0,106,3",
                           "keydown:+,NumpadAdd,107,0,107,3", "keypress:+,NumpadAdd,43,43,43,3", "input:059*+",
                           "keyup:+,NumpadAdd,107,0,107,3", "keydown:-,NumpadSubtract,109,0,109,3",
                           "keypress:-,NumpadSubtract,45,45,45,3", "input:059*+-",
                           "keyup:-,NumpadSubtract,109,0,109,3", "keydown:.,NumpadDecimal,110,0,110,3",
                           "keypress:.,NumpadDecimal,46,46,46,3", "input:059*+-.", "keyup:.,NumpadDecimal,110,0,110,3",
                           "keydown:/,NumpadDivide,111,0,111,3", "keypress:/,NumpadDivide,47,47,47,3",
                           "input:059*+-./", "keyup:/,NumpadDivide,111,0,111,3"},
            EDGE = {"keydown:0,Numpad0,96,0,96,3", "keypress:0,Numpad0,48,48,48,3", "input:0",
                    "keyup:0,Numpad0,96,0,96,3", "keydown:5,Numpad5,101,0,101,3", "keypress:5,Numpad5,53,53,53,3",
                    "input:05", "keyup:5,Numpad5,101,0,101,3", "keydown:9,Numpad9,105,0,105,3",
                    "keypress:9,Numpad9,57,57,57,3", "input:059", "keyup:9,Numpad9,105,0,105,3",
                    "keydown:*,NumpadMultiply,106,0,106,3", "keypress:*,NumpadMultiply,42,42,42,3", "input:059*",
                    "keyup:*,NumpadMultiply,106,0,106,3", "keydown:+,NumpadAdd,107,0,107,3",
                    "keypress:+,NumpadAdd,43,43,43,3", "input:059*+", "keyup:+,NumpadAdd,107,0,107,3",
                    "keydown:-,NumpadSubtract,109,0,109,3", "keypress:-,NumpadSubtract,45,45,45,3", "input:059*+-",
                    "keyup:-,NumpadSubtract,109,0,109,3", "keydown:.,NumpadDecimal,110,0,110,3",
                    "keypress:.,NumpadDecimal,46,46,46,3", "input:059*+-.", "keyup:.,NumpadDecimal,110,0,110,3",
                    "keydown:/,NumpadDivide,111,0,111,3", "keypress:/,NumpadDivide,47,47,47,3", "input:059*+-./",
                    "keyup:/,NumpadDivide,111,0,111,3"})
    public void specialKeysNumpad() throws Exception {
        keyEvents("input", "", false, Keys.NUMPAD0, Keys.NUMPAD5, Keys.NUMPAD9, Keys.MULTIPLY, Keys.ADD,
                Keys.SUBTRACT, Keys.DECIMAL, Keys.DIVIDE);
    }

    /**
     * Logs the key events for the modifier keys.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"keydown:Shift,ShiftLeft,16,0,16,0", "keydown:Control,ControlLeft,17,0,17,0",
                       "keydown:Alt,AltLeft,18,0,18,0", "keydown:Meta,MetaLeft,91,0,91,0",
                       "keyup:Shift,ShiftLeft,16,0,16,0", "keyup:Control,ControlLeft,17,0,17,0",
                       "keyup:Alt,AltLeft,18,0,18,0", "keyup:Meta,MetaLeft,91,0,91,0"},
            FF = {"keydown:Shift,ShiftLeft,16,0,16,1", "keyup:Shift,ShiftLeft,16,0,16,1",
                  "keydown:Control,ControlLeft,17,0,17,1", "keyup:Control,ControlLeft,17,0,17,1",
                  "keydown:Alt,AltLeft,18,0,18,1", "keyup:Alt,AltLeft,18,0,18,1", "keydown:Meta,MetaLeft,91,0,91,1",
                  "keyup:Meta,MetaLeft,91,0,91,1"},
            FF_ESR = {"keydown:Shift,ShiftLeft,16,0,16,1", "keyup:Shift,ShiftLeft,16,0,16,1",
                      "keydown:Control,ControlLeft,17,0,17,1", "keyup:Control,ControlLeft,17,0,17,1",
                      "keydown:Alt,AltLeft,18,0,18,1", "keyup:Alt,AltLeft,18,0,18,1",
                      "keydown:Meta,MetaLeft,91,0,91,1", "keyup:Meta,MetaLeft,91,0,91,1"})
    // chromedriver does not set the location of the key events
    // the modifiers are released at the end of sendKeys in a different order
    @HtmlUnitNYI(CHROME = {"keydown:Shift,ShiftLeft,16,0,16,1", "keydown:Control,ControlLeft,17,0,17,1",
                           "keydown:Alt,AltLeft,18,0,18,1", "keydown:Meta,MetaLeft,91,0,91,1",
                           "keyup:Meta,MetaLeft,91,0,91,1", "keyup:Shift,ShiftLeft,16,0,16,1",
                           "keyup:Alt,AltLeft,18,0,18,1", "keyup:Control,ControlLeft,17,0,17,1"},
            EDGE = {"keydown:Shift,ShiftLeft,16,0,16,1", "keydown:Control,ControlLeft,17,0,17,1",
                    "keydown:Alt,AltLeft,18,0,18,1", "keydown:Meta,MetaLeft,91,0,91,1",
                    "keyup:Meta,MetaLeft,91,0,91,1", "keyup:Shift,ShiftLeft,16,0,16,1", "keyup:Alt,AltLeft,18,0,18,1",
                    "keyup:Control,ControlLeft,17,0,17,1"},
            FF = {"keydown:Shift,ShiftLeft,16,0,16,1", "keydown:Control,ControlLeft,17,0,17,1",
                  "keydown:Alt,AltLeft,18,0,18,1", "keydown:Meta,MetaLeft,91,0,91,1", "keyup:Meta,MetaLeft,91,0,91,1",
                  "keyup:Shift,ShiftLeft,16,0,16,1", "keyup:Alt,AltLeft,18,0,18,1",
                  "keyup:Control,ControlLeft,17,0,17,1"},
            FF_ESR = {"keydown:Shift,ShiftLeft,16,0,16,1", "keydown:Control,ControlLeft,17,0,17,1",
                      "keydown:Alt,AltLeft,18,0,18,1", "keydown:Meta,MetaLeft,91,0,91,1",
                      "keyup:Meta,MetaLeft,91,0,91,1", "keyup:Shift,ShiftLeft,16,0,16,1",
                      "keyup:Alt,AltLeft,18,0,18,1", "keyup:Control,ControlLeft,17,0,17,1"})
    public void specialKeysModifiers() throws Exception {
        keyEvents("input", "", false, Keys.SHIFT, Keys.CONTROL, Keys.ALT, Keys.META);
    }

    /**
     * Logs the key events for an arrow key pressed together with shift.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"keydown:Shift,ShiftLeft,16,0,16,0", "keydown:ArrowLeft,ArrowLeft,37,0,37,0",
                       "keyup:ArrowLeft,ArrowLeft,37,0,37,0", "keyup:Shift,ShiftLeft,16,0,16,0"},
            FF = {"keydown:Shift,ShiftLeft,16,0,16,1", "keyup:Shift,ShiftLeft,16,0,16,1",
                  "keydown:ArrowLeft,ArrowLeft,37,0,37,0", "keyup:ArrowLeft,ArrowLeft,37,0,37,0",
                  "keydown:Unidentified,,0,0,0,0", "keyup:Unidentified,,0,0,0,0"},
            FF_ESR = {"keydown:Shift,ShiftLeft,16,0,16,1", "keyup:Shift,ShiftLeft,16,0,16,1",
                      "keydown:ArrowLeft,ArrowLeft,37,0,37,0", "keyup:ArrowLeft,ArrowLeft,37,0,37,0",
                      "keydown:Unidentified,,0,0,0,0", "keyup:Unidentified,,0,0,0,0"})
    // chromedriver does not set the location of the key events
    // geckodriver releases the shift key first and sends an additional unidentified key
    @HtmlUnitNYI(CHROME = {"keydown:Shift,ShiftLeft,16,0,16,1", "keydown:ArrowLeft,ArrowLeft,37,0,37,0",
                           "keyup:ArrowLeft,ArrowLeft,37,0,37,0", "keyup:Shift,ShiftLeft,16,0,16,1"},
            EDGE = {"keydown:Shift,ShiftLeft,16,0,16,1", "keydown:ArrowLeft,ArrowLeft,37,0,37,0",
                    "keyup:ArrowLeft,ArrowLeft,37,0,37,0", "keyup:Shift,ShiftLeft,16,0,16,1"},
            FF = {"keydown:Shift,ShiftLeft,16,0,16,1", "keydown:ArrowLeft,ArrowLeft,37,0,37,0",
                  "keyup:ArrowLeft,ArrowLeft,37,0,37,0", "keyup:Shift,ShiftLeft,16,0,16,1"},
            FF_ESR = {"keydown:Shift,ShiftLeft,16,0,16,1", "keydown:ArrowLeft,ArrowLeft,37,0,37,0",
                      "keyup:ArrowLeft,ArrowLeft,37,0,37,0", "keyup:Shift,ShiftLeft,16,0,16,1"})
    public void specialKeysShiftArrow() throws Exception {
        keyEvents("input", "abc", false, Keys.chord(Keys.SHIFT, Keys.ARROW_LEFT));
    }

    /**
     * Logs the key events for digits and space.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"keydown:0,Digit0,48,0,48,0", "keypress:0,Digit0,48,48,48,0", "input:0", "keyup:0,Digit0,48,0,48,0",
             "keydown: ,Space,32,0,32,0", "keypress: ,Space,32,32,32,0", "input:0 ", "keyup: ,Space,32,0,32,0",
             "keydown:9,Digit9,57,0,57,0", "keypress:9,Digit9,57,57,57,0", "input:0 9", "keyup:9,Digit9,57,0,57,0"})
    public void keysDigitsAndSpace() throws Exception {
        keyEvents("input", "", false, "0 9");
    }

    /**
     * Logs the key events for punctuation chars.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"keydown:,,Comma,188,0,188,0", "keypress:,,Comma,44,44,44,0", "input:,",
                       "keyup:,,Comma,188,0,188,0", "keydown:.,Period,190,0,190,0", "keypress:.,Period,46,46,46,0",
                       "input:,.", "keyup:.,Period,190,0,190,0", "keydown:-,Minus,189,0,189,0",
                       "keypress:-,Minus,45,45,45,0", "input:,.-", "keyup:-,Minus,189,0,189,0",
                       "keydown:/,Slash,191,0,191,0", "keypress:/,Slash,47,47,47,0", "input:,.-/",
                       "keyup:/,Slash,191,0,191,0", "keydown:;,Semicolon,186,0,186,0",
                       "keypress:;,Semicolon,59,59,59,0", "input:,.-/;", "keyup:;,Semicolon,186,0,186,0",
                       "keydown:=,Equal,187,0,187,0", "keypress:=,Equal,61,61,61,0", "input:,.-/;=",
                       "keyup:=,Equal,187,0,187,0", "keydown:[,BracketLeft,219,0,219,0",
                       "keypress:[,BracketLeft,91,91,91,0", "input:,.-/;=[", "keyup:[,BracketLeft,219,0,219,0",
                       "keydown:],BracketRight,221,0,221,0", "keypress:],BracketRight,93,93,93,0", "input:,.-/;=[]",
                       "keyup:],BracketRight,221,0,221,0", "keydown:\\,Backslash,220,0,220,0",
                       "keypress:\\,Backslash,92,92,92,0", "input:,.-/;=[]\\", "keyup:\\,Backslash,220,0,220,0",
                       "keydown:',Quote,222,0,222,0", "keypress:',Quote,39,39,39,0", "input:,.-/;=[]\\'",
                       "keyup:',Quote,222,0,222,0", "keydown:`,Backquote,192,0,192,0",
                       "keypress:`,Backquote,96,96,96,0", "input:,.-/;=[]\\'`", "keyup:`,Backquote,192,0,192,0"},
            FF = {"keydown:,,Comma,188,0,188,0", "keypress:,,Comma,44,44,44,0", "input:,", "keyup:,,Comma,188,0,188,0",
                  "keydown:.,Period,190,0,190,0", "keypress:.,Period,46,46,46,0", "input:,.",
                  "keyup:.,Period,190,0,190,0", "keydown:-,Minus,173,0,173,0", "keypress:-,Minus,45,45,45,0",
                  "input:,.-", "keyup:-,Minus,173,0,173,0", "keydown:/,Slash,191,0,191,0",
                  "keypress:/,Slash,47,47,47,0", "input:,.-/", "keyup:/,Slash,191,0,191,0",
                  "keydown:;,Semicolon,59,0,59,0", "keypress:;,Semicolon,59,59,59,0", "input:,.-/;",
                  "keyup:;,Semicolon,59,0,59,0", "keydown:=,Equal,61,0,61,0", "keypress:=,Equal,61,61,61,0",
                  "input:,.-/;=", "keyup:=,Equal,61,0,61,0", "keydown:[,BracketLeft,219,0,219,0",
                  "keypress:[,BracketLeft,91,91,91,0", "input:,.-/;=[", "keyup:[,BracketLeft,219,0,219,0",
                  "keydown:],BracketRight,221,0,221,0", "keypress:],BracketRight,93,93,93,0", "input:,.-/;=[]",
                  "keyup:],BracketRight,221,0,221,0", "keydown:\\,Backslash,220,0,220,0",
                  "keypress:\\,Backslash,92,92,92,0", "input:,.-/;=[]\\", "keyup:\\,Backslash,220,0,220,0",
                  "keydown:',Quote,222,0,222,0", "keypress:',Quote,39,39,39,0", "input:,.-/;=[]\\'",
                  "keyup:',Quote,222,0,222,0", "keydown:`,Backquote,192,0,192,0", "keypress:`,Backquote,96,96,96,0",
                  "input:,.-/;=[]\\'`", "keyup:`,Backquote,192,0,192,0"},
            FF_ESR = {"keydown:,,Comma,188,0,188,0", "keypress:,,Comma,44,44,44,0", "input:,",
                      "keyup:,,Comma,188,0,188,0", "keydown:.,Period,190,0,190,0", "keypress:.,Period,46,46,46,0",
                      "input:,.", "keyup:.,Period,190,0,190,0", "keydown:-,Minus,173,0,173,0",
                      "keypress:-,Minus,45,45,45,0", "input:,.-", "keyup:-,Minus,173,0,173,0",
                      "keydown:/,Slash,191,0,191,0", "keypress:/,Slash,47,47,47,0", "input:,.-/",
                      "keyup:/,Slash,191,0,191,0", "keydown:;,Semicolon,59,0,59,0", "keypress:;,Semicolon,59,59,59,0",
                      "input:,.-/;", "keyup:;,Semicolon,59,0,59,0", "keydown:=,Equal,61,0,61,0",
                      "keypress:=,Equal,61,61,61,0", "input:,.-/;=", "keyup:=,Equal,61,0,61,0",
                      "keydown:[,BracketLeft,219,0,219,0", "keypress:[,BracketLeft,91,91,91,0", "input:,.-/;=[",
                      "keyup:[,BracketLeft,219,0,219,0", "keydown:],BracketRight,221,0,221,0",
                      "keypress:],BracketRight,93,93,93,0", "input:,.-/;=[]", "keyup:],BracketRight,221,0,221,0",
                      "keydown:\\,Backslash,220,0,220,0", "keypress:\\,Backslash,92,92,92,0", "input:,.-/;=[]\\",
                      "keyup:\\,Backslash,220,0,220,0", "keydown:',Quote,222,0,222,0", "keypress:',Quote,39,39,39,0",
                      "input:,.-/;=[]\\'", "keyup:',Quote,222,0,222,0", "keydown:`,Backquote,192,0,192,0",
                      "keypress:`,Backquote,96,96,96,0", "input:,.-/;=[]\\'`", "keyup:`,Backquote,192,0,192,0"})
    public void keysPunctuation() throws Exception {
        keyEvents("input", "", false, ",.-/;=[]\\'`");
    }

    /**
     * Logs the key events for shifted punctuation chars.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"keydown:Shift,ShiftLeft,16,0,16,0", "keydown:!,Digit1,49,0,49,0",
                       "keypress:!,Digit1,33,33,33,0", "input:!", "keyup:!,Digit1,49,0,49,0",
                       "keyup:Shift,ShiftLeft,16,0,16,0", "keydown:Shift,ShiftLeft,16,0,16,0",
                       "keydown:@,Digit2,50,0,50,0", "keypress:@,Digit2,64,64,64,0", "input:!@",
                       "keyup:@,Digit2,50,0,50,0", "keyup:Shift,ShiftLeft,16,0,16,0",
                       "keydown:Shift,ShiftLeft,16,0,16,0", "keydown:#,Digit3,51,0,51,0",
                       "keypress:#,Digit3,35,35,35,0", "input:!@#", "keyup:#,Digit3,51,0,51,0",
                       "keyup:Shift,ShiftLeft,16,0,16,0", "keydown:Shift,ShiftLeft,16,0,16,0",
                       "keydown:$,Digit4,52,0,52,0", "keypress:$,Digit4,36,36,36,0", "input:!@#$",
                       "keyup:$,Digit4,52,0,52,0", "keyup:Shift,ShiftLeft,16,0,16,0",
                       "keydown:Shift,ShiftLeft,16,0,16,0", "keydown:%,Digit5,53,0,53,0",
                       "keypress:%,Digit5,37,37,37,0", "input:!@#$%", "keyup:%,Digit5,53,0,53,0",
                       "keyup:Shift,ShiftLeft,16,0,16,0", "keydown:Shift,ShiftLeft,16,0,16,0",
                       "keydown:^,Digit6,54,0,54,0", "keypress:^,Digit6,94,94,94,0", "input:!@#$%^",
                       "keyup:^,Digit6,54,0,54,0", "keyup:Shift,ShiftLeft,16,0,16,0",
                       "keydown:Shift,ShiftLeft,16,0,16,0", "keydown:&,Digit7,55,0,55,0",
                       "keypress:&,Digit7,38,38,38,0", "input:!@#$%^&", "keyup:&,Digit7,55,0,55,0",
                       "keyup:Shift,ShiftLeft,16,0,16,0", "keydown:Shift,ShiftLeft,16,0,16,0",
                       "keydown:*,Digit8,56,0,56,0", "keypress:*,Digit8,42,42,42,0", "input:!@#$%^&*",
                       "keyup:*,Digit8,56,0,56,0", "keyup:Shift,ShiftLeft,16,0,16,0",
                       "keydown:Shift,ShiftLeft,16,0,16,0", "keydown:(,Digit9,57,0,57,0",
                       "keypress:(,Digit9,40,40,40,0", "input:!@#$%^&*(", "keyup:(,Digit9,57,0,57,0",
                       "keyup:Shift,ShiftLeft,16,0,16,0", "keydown:Shift,ShiftLeft,16,0,16,0",
                       "keydown:),Digit0,48,0,48,0", "keypress:),Digit0,41,41,41,0", "input:!@#$%^&*()",
                       "keyup:),Digit0,48,0,48,0", "keyup:Shift,ShiftLeft,16,0,16,0",
                       "keydown:Shift,ShiftLeft,16,0,16,0", "keydown:_,Minus,189,0,189,0",
                       "keypress:_,Minus,95,95,95,0", "input:!@#$%^&*()_", "keyup:_,Minus,189,0,189,0",
                       "keyup:Shift,ShiftLeft,16,0,16,0", "keydown:Shift,ShiftLeft,16,0,16,0",
                       "keydown:+,Equal,187,0,187,0", "keypress:+,Equal,43,43,43,0", "input:!@#$%^&*()_+",
                       "keyup:+,Equal,187,0,187,0", "keyup:Shift,ShiftLeft,16,0,16,0",
                       "keydown:Shift,ShiftLeft,16,0,16,0", "keydown:{,BracketLeft,219,0,219,0",
                       "keypress:{,BracketLeft,123,123,123,0", "input:!@#$%^&*()_+{",
                       "keyup:{,BracketLeft,219,0,219,0", "keyup:Shift,ShiftLeft,16,0,16,0",
                       "keydown:Shift,ShiftLeft,16,0,16,0", "keydown:},BracketRight,221,0,221,0",
                       "keypress:},BracketRight,125,125,125,0", "input:!@#$%^&*()_+{}",
                       "keyup:},BracketRight,221,0,221,0", "keyup:Shift,ShiftLeft,16,0,16,0",
                       "keydown:Shift,ShiftLeft,16,0,16,0", "keydown:|,Backslash,220,0,220,0",
                       "keypress:|,Backslash,124,124,124,0", "input:!@#$%^&*()_+{}|", "keyup:|,Backslash,220,0,220,0",
                       "keyup:Shift,ShiftLeft,16,0,16,0", "keydown:Shift,ShiftLeft,16,0,16,0",
                       "keydown::,Semicolon,186,0,186,0", "keypress::,Semicolon,58,58,58,0", "input:!@#$%^&*()_+{}|:",
                       "keyup::,Semicolon,186,0,186,0", "keyup:Shift,ShiftLeft,16,0,16,0",
                       "keydown:Shift,ShiftLeft,16,0,16,0", "keydown:\",Quote,222,0,222,0",
                       "keypress:\",Quote,34,34,34,0", "input:!@#$%^&*()_+{}|:\"", "keyup:\",Quote,222,0,222,0",
                       "keyup:Shift,ShiftLeft,16,0,16,0", "keydown:Shift,ShiftLeft,16,0,16,0",
                       "keydown:<,Comma,188,0,188,0", "keypress:<,Comma,60,60,60,0", "input:!@#$%^&*()_+{}|:\"<",
                       "keyup:<,Comma,188,0,188,0", "keyup:Shift,ShiftLeft,16,0,16,0",
                       "keydown:Shift,ShiftLeft,16,0,16,0", "keydown:>,Period,190,0,190,0",
                       "keypress:>,Period,62,62,62,0", "input:!@#$%^&*()_+{}|:\"<>", "keyup:>,Period,190,0,190,0",
                       "keyup:Shift,ShiftLeft,16,0,16,0", "keydown:Shift,ShiftLeft,16,0,16,0",
                       "keydown:?,Slash,191,0,191,0", "keypress:?,Slash,63,63,63,0", "input:!@#$%^&*()_+{}|:\"<>?",
                       "keyup:?,Slash,191,0,191,0", "keyup:Shift,ShiftLeft,16,0,16,0",
                       "keydown:Shift,ShiftLeft,16,0,16,0", "keydown:~,Backquote,192,0,192,0",
                       "keypress:~,Backquote,126,126,126,0", "input:!@#$%^&*()_+{}|:\"<>?~",
                       "keyup:~,Backquote,192,0,192,0", "keyup:Shift,ShiftLeft,16,0,16,0"},
            FF = {"keydown:!,Digit1,49,0,49,0", "keypress:!,Digit1,33,33,33,0", "input:!", "keyup:!,Digit1,49,0,49,0",
                  "keydown:@,Digit2,50,0,50,0", "keypress:@,Digit2,64,64,64,0", "input:!@", "keyup:@,Digit2,50,0,50,0",
                  "keydown:#,Digit3,51,0,51,0", "keypress:#,Digit3,35,35,35,0", "input:!@#",
                  "keyup:#,Digit3,51,0,51,0", "keydown:$,Digit4,52,0,52,0", "keypress:$,Digit4,36,36,36,0",
                  "input:!@#$", "keyup:$,Digit4,52,0,52,0", "keydown:%,Digit5,53,0,53,0",
                  "keypress:%,Digit5,37,37,37,0", "input:!@#$%", "keyup:%,Digit5,53,0,53,0",
                  "keydown:^,Digit6,54,0,54,0", "keypress:^,Digit6,94,94,94,0", "input:!@#$%^",
                  "keyup:^,Digit6,54,0,54,0", "keydown:&,Digit7,55,0,55,0", "keypress:&,Digit7,38,38,38,0",
                  "input:!@#$%^&", "keyup:&,Digit7,55,0,55,0", "keydown:*,Digit8,56,0,56,0",
                  "keypress:*,Digit8,42,42,42,0", "input:!@#$%^&*", "keyup:*,Digit8,56,0,56,0",
                  "keydown:(,Digit9,57,0,57,0", "keypress:(,Digit9,40,40,40,0", "input:!@#$%^&*(",
                  "keyup:(,Digit9,57,0,57,0", "keydown:),Digit0,48,0,48,0", "keypress:),Digit0,41,41,41,0",
                  "input:!@#$%^&*()", "keyup:),Digit0,48,0,48,0", "keydown:_,Minus,173,0,173,0",
                  "keypress:_,Minus,95,95,95,0", "input:!@#$%^&*()_", "keyup:_,Minus,173,0,173,0",
                  "keydown:+,Equal,61,0,61,0", "keypress:+,Equal,43,43,43,0", "input:!@#$%^&*()_+",
                  "keyup:+,Equal,61,0,61,0", "keydown:{,BracketLeft,219,0,219,0",
                  "keypress:{,BracketLeft,123,123,123,0", "input:!@#$%^&*()_+{", "keyup:{,BracketLeft,219,0,219,0",
                  "keydown:},BracketRight,221,0,221,0", "keypress:},BracketRight,125,125,125,0",
                  "input:!@#$%^&*()_+{}", "keyup:},BracketRight,221,0,221,0", "keydown:|,Backslash,220,0,220,0",
                  "keypress:|,Backslash,124,124,124,0", "input:!@#$%^&*()_+{}|", "keyup:|,Backslash,220,0,220,0",
                  "keydown::,Semicolon,59,0,59,0", "keypress::,Semicolon,58,58,58,0", "input:!@#$%^&*()_+{}|:",
                  "keyup::,Semicolon,59,0,59,0", "keydown:\",Quote,222,0,222,0", "keypress:\",Quote,34,34,34,0",
                  "input:!@#$%^&*()_+{}|:\"", "keyup:\",Quote,222,0,222,0", "keydown:<,Comma,188,0,188,0",
                  "keypress:<,Comma,60,60,60,0", "input:!@#$%^&*()_+{}|:\"<", "keyup:<,Comma,188,0,188,0",
                  "keydown:>,Period,190,0,190,0", "keypress:>,Period,62,62,62,0", "input:!@#$%^&*()_+{}|:\"<>",
                  "keyup:>,Period,190,0,190,0", "keydown:?,Slash,191,0,191,0", "keypress:?,Slash,63,63,63,0",
                  "input:!@#$%^&*()_+{}|:\"<>?", "keyup:?,Slash,191,0,191,0", "keydown:~,Backquote,192,0,192,0",
                  "keypress:~,Backquote,126,126,126,0", "input:!@#$%^&*()_+{}|:\"<>?~",
                  "keyup:~,Backquote,192,0,192,0"},
            FF_ESR = {"keydown:!,Digit1,49,0,49,0", "keypress:!,Digit1,33,33,33,0", "input:!",
                      "keyup:!,Digit1,49,0,49,0", "keydown:@,Digit2,50,0,50,0", "keypress:@,Digit2,64,64,64,0",
                      "input:!@", "keyup:@,Digit2,50,0,50,0", "keydown:#,Digit3,51,0,51,0",
                      "keypress:#,Digit3,35,35,35,0", "input:!@#", "keyup:#,Digit3,51,0,51,0",
                      "keydown:$,Digit4,52,0,52,0", "keypress:$,Digit4,36,36,36,0", "input:!@#$",
                      "keyup:$,Digit4,52,0,52,0", "keydown:%,Digit5,53,0,53,0", "keypress:%,Digit5,37,37,37,0",
                      "input:!@#$%", "keyup:%,Digit5,53,0,53,0", "keydown:^,Digit6,54,0,54,0",
                      "keypress:^,Digit6,94,94,94,0", "input:!@#$%^", "keyup:^,Digit6,54,0,54,0",
                      "keydown:&,Digit7,55,0,55,0", "keypress:&,Digit7,38,38,38,0", "input:!@#$%^&",
                      "keyup:&,Digit7,55,0,55,0", "keydown:*,Digit8,56,0,56,0", "keypress:*,Digit8,42,42,42,0",
                      "input:!@#$%^&*", "keyup:*,Digit8,56,0,56,0", "keydown:(,Digit9,57,0,57,0",
                      "keypress:(,Digit9,40,40,40,0", "input:!@#$%^&*(", "keyup:(,Digit9,57,0,57,0",
                      "keydown:),Digit0,48,0,48,0", "keypress:),Digit0,41,41,41,0", "input:!@#$%^&*()",
                      "keyup:),Digit0,48,0,48,0", "keydown:_,Minus,173,0,173,0", "keypress:_,Minus,95,95,95,0",
                      "input:!@#$%^&*()_", "keyup:_,Minus,173,0,173,0", "keydown:+,Equal,61,0,61,0",
                      "keypress:+,Equal,43,43,43,0", "input:!@#$%^&*()_+", "keyup:+,Equal,61,0,61,0",
                      "keydown:{,BracketLeft,219,0,219,0", "keypress:{,BracketLeft,123,123,123,0",
                      "input:!@#$%^&*()_+{", "keyup:{,BracketLeft,219,0,219,0", "keydown:},BracketRight,221,0,221,0",
                      "keypress:},BracketRight,125,125,125,0", "input:!@#$%^&*()_+{}",
                      "keyup:},BracketRight,221,0,221,0", "keydown:|,Backslash,220,0,220,0",
                      "keypress:|,Backslash,124,124,124,0", "input:!@#$%^&*()_+{}|", "keyup:|,Backslash,220,0,220,0",
                      "keydown::,Semicolon,59,0,59,0", "keypress::,Semicolon,58,58,58,0", "input:!@#$%^&*()_+{}|:",
                      "keyup::,Semicolon,59,0,59,0", "keydown:\",Quote,222,0,222,0", "keypress:\",Quote,34,34,34,0",
                      "input:!@#$%^&*()_+{}|:\"", "keyup:\",Quote,222,0,222,0", "keydown:<,Comma,188,0,188,0",
                      "keypress:<,Comma,60,60,60,0", "input:!@#$%^&*()_+{}|:\"<", "keyup:<,Comma,188,0,188,0",
                      "keydown:>,Period,190,0,190,0", "keypress:>,Period,62,62,62,0", "input:!@#$%^&*()_+{}|:\"<>",
                      "keyup:>,Period,190,0,190,0", "keydown:?,Slash,191,0,191,0", "keypress:?,Slash,63,63,63,0",
                      "input:!@#$%^&*()_+{}|:\"<>?", "keyup:?,Slash,191,0,191,0", "keydown:~,Backquote,192,0,192,0",
                      "keypress:~,Backquote,126,126,126,0", "input:!@#$%^&*()_+{}|:\"<>?~",
                      "keyup:~,Backquote,192,0,192,0"})
    // chromedriver does not set the location of the key events
    // geckodriver does not send the shift key for shifted chars
    @HtmlUnitNYI(CHROME = {"keydown:Shift,ShiftLeft,16,0,16,1", "keydown:!,Digit1,49,0,49,0",
                           "keypress:!,Digit1,33,33,33,0", "input:!", "keyup:!,Digit1,49,0,49,0",
                           "keyup:Shift,ShiftLeft,16,0,16,1", "keydown:Shift,ShiftLeft,16,0,16,1",
                           "keydown:@,Digit2,50,0,50,0", "keypress:@,Digit2,64,64,64,0", "input:!@",
                           "keyup:@,Digit2,50,0,50,0", "keyup:Shift,ShiftLeft,16,0,16,1",
                           "keydown:Shift,ShiftLeft,16,0,16,1", "keydown:#,Digit3,51,0,51,0",
                           "keypress:#,Digit3,35,35,35,0", "input:!@#", "keyup:#,Digit3,51,0,51,0",
                           "keyup:Shift,ShiftLeft,16,0,16,1", "keydown:Shift,ShiftLeft,16,0,16,1",
                           "keydown:$,Digit4,52,0,52,0", "keypress:$,Digit4,36,36,36,0", "input:!@#$",
                           "keyup:$,Digit4,52,0,52,0", "keyup:Shift,ShiftLeft,16,0,16,1",
                           "keydown:Shift,ShiftLeft,16,0,16,1", "keydown:%,Digit5,53,0,53,0",
                           "keypress:%,Digit5,37,37,37,0", "input:!@#$%", "keyup:%,Digit5,53,0,53,0",
                           "keyup:Shift,ShiftLeft,16,0,16,1", "keydown:Shift,ShiftLeft,16,0,16,1",
                           "keydown:^,Digit6,54,0,54,0", "keypress:^,Digit6,94,94,94,0", "input:!@#$%^",
                           "keyup:^,Digit6,54,0,54,0", "keyup:Shift,ShiftLeft,16,0,16,1",
                           "keydown:Shift,ShiftLeft,16,0,16,1", "keydown:&,Digit7,55,0,55,0",
                           "keypress:&,Digit7,38,38,38,0", "input:!@#$%^&", "keyup:&,Digit7,55,0,55,0",
                           "keyup:Shift,ShiftLeft,16,0,16,1", "keydown:Shift,ShiftLeft,16,0,16,1",
                           "keydown:*,Digit8,56,0,56,0", "keypress:*,Digit8,42,42,42,0", "input:!@#$%^&*",
                           "keyup:*,Digit8,56,0,56,0", "keyup:Shift,ShiftLeft,16,0,16,1",
                           "keydown:Shift,ShiftLeft,16,0,16,1", "keydown:(,Digit9,57,0,57,0",
                           "keypress:(,Digit9,40,40,40,0", "input:!@#$%^&*(", "keyup:(,Digit9,57,0,57,0",
                           "keyup:Shift,ShiftLeft,16,0,16,1", "keydown:Shift,ShiftLeft,16,0,16,1",
                           "keydown:),Digit0,48,0,48,0", "keypress:),Digit0,41,41,41,0", "input:!@#$%^&*()",
                           "keyup:),Digit0,48,0,48,0", "keyup:Shift,ShiftLeft,16,0,16,1",
                           "keydown:Shift,ShiftLeft,16,0,16,1", "keydown:_,Minus,189,0,189,0",
                           "keypress:_,Minus,95,95,95,0", "input:!@#$%^&*()_", "keyup:_,Minus,189,0,189,0",
                           "keyup:Shift,ShiftLeft,16,0,16,1", "keydown:Shift,ShiftLeft,16,0,16,1",
                           "keydown:+,Equal,187,0,187,0", "keypress:+,Equal,43,43,43,0", "input:!@#$%^&*()_+",
                           "keyup:+,Equal,187,0,187,0", "keyup:Shift,ShiftLeft,16,0,16,1",
                           "keydown:Shift,ShiftLeft,16,0,16,1", "keydown:{,BracketLeft,219,0,219,0",
                           "keypress:{,BracketLeft,123,123,123,0", "input:!@#$%^&*()_+{",
                           "keyup:{,BracketLeft,219,0,219,0", "keyup:Shift,ShiftLeft,16,0,16,1",
                           "keydown:Shift,ShiftLeft,16,0,16,1", "keydown:},BracketRight,221,0,221,0",
                           "keypress:},BracketRight,125,125,125,0", "input:!@#$%^&*()_+{}",
                           "keyup:},BracketRight,221,0,221,0", "keyup:Shift,ShiftLeft,16,0,16,1",
                           "keydown:Shift,ShiftLeft,16,0,16,1", "keydown:|,Backslash,220,0,220,0",
                           "keypress:|,Backslash,124,124,124,0", "input:!@#$%^&*()_+{}|",
                           "keyup:|,Backslash,220,0,220,0", "keyup:Shift,ShiftLeft,16,0,16,1",
                           "keydown:Shift,ShiftLeft,16,0,16,1", "keydown::,Semicolon,186,0,186,0",
                           "keypress::,Semicolon,58,58,58,0", "input:!@#$%^&*()_+{}|:",
                           "keyup::,Semicolon,186,0,186,0", "keyup:Shift,ShiftLeft,16,0,16,1",
                           "keydown:Shift,ShiftLeft,16,0,16,1", "keydown:\",Quote,222,0,222,0",
                           "keypress:\",Quote,34,34,34,0", "input:!@#$%^&*()_+{}|:\"", "keyup:\",Quote,222,0,222,0",
                           "keyup:Shift,ShiftLeft,16,0,16,1", "keydown:Shift,ShiftLeft,16,0,16,1",
                           "keydown:<,Comma,188,0,188,0", "keypress:<,Comma,60,60,60,0", "input:!@#$%^&*()_+{}|:\"<",
                           "keyup:<,Comma,188,0,188,0", "keyup:Shift,ShiftLeft,16,0,16,1",
                           "keydown:Shift,ShiftLeft,16,0,16,1", "keydown:>,Period,190,0,190,0",
                           "keypress:>,Period,62,62,62,0", "input:!@#$%^&*()_+{}|:\"<>", "keyup:>,Period,190,0,190,0",
                           "keyup:Shift,ShiftLeft,16,0,16,1", "keydown:Shift,ShiftLeft,16,0,16,1",
                           "keydown:?,Slash,191,0,191,0", "keypress:?,Slash,63,63,63,0", "input:!@#$%^&*()_+{}|:\"<>?",
                           "keyup:?,Slash,191,0,191,0", "keyup:Shift,ShiftLeft,16,0,16,1",
                           "keydown:Shift,ShiftLeft,16,0,16,1", "keydown:~,Backquote,192,0,192,0",
                           "keypress:~,Backquote,126,126,126,0", "input:!@#$%^&*()_+{}|:\"<>?~",
                           "keyup:~,Backquote,192,0,192,0", "keyup:Shift,ShiftLeft,16,0,16,1"},
            EDGE = {"keydown:Shift,ShiftLeft,16,0,16,1", "keydown:!,Digit1,49,0,49,0", "keypress:!,Digit1,33,33,33,0",
                    "input:!", "keyup:!,Digit1,49,0,49,0", "keyup:Shift,ShiftLeft,16,0,16,1",
                    "keydown:Shift,ShiftLeft,16,0,16,1", "keydown:@,Digit2,50,0,50,0", "keypress:@,Digit2,64,64,64,0",
                    "input:!@", "keyup:@,Digit2,50,0,50,0", "keyup:Shift,ShiftLeft,16,0,16,1",
                    "keydown:Shift,ShiftLeft,16,0,16,1", "keydown:#,Digit3,51,0,51,0", "keypress:#,Digit3,35,35,35,0",
                    "input:!@#", "keyup:#,Digit3,51,0,51,0", "keyup:Shift,ShiftLeft,16,0,16,1",
                    "keydown:Shift,ShiftLeft,16,0,16,1", "keydown:$,Digit4,52,0,52,0", "keypress:$,Digit4,36,36,36,0",
                    "input:!@#$", "keyup:$,Digit4,52,0,52,0", "keyup:Shift,ShiftLeft,16,0,16,1",
                    "keydown:Shift,ShiftLeft,16,0,16,1", "keydown:%,Digit5,53,0,53,0", "keypress:%,Digit5,37,37,37,0",
                    "input:!@#$%", "keyup:%,Digit5,53,0,53,0", "keyup:Shift,ShiftLeft,16,0,16,1",
                    "keydown:Shift,ShiftLeft,16,0,16,1", "keydown:^,Digit6,54,0,54,0", "keypress:^,Digit6,94,94,94,0",
                    "input:!@#$%^", "keyup:^,Digit6,54,0,54,0", "keyup:Shift,ShiftLeft,16,0,16,1",
                    "keydown:Shift,ShiftLeft,16,0,16,1", "keydown:&,Digit7,55,0,55,0", "keypress:&,Digit7,38,38,38,0",
                    "input:!@#$%^&", "keyup:&,Digit7,55,0,55,0", "keyup:Shift,ShiftLeft,16,0,16,1",
                    "keydown:Shift,ShiftLeft,16,0,16,1", "keydown:*,Digit8,56,0,56,0", "keypress:*,Digit8,42,42,42,0",
                    "input:!@#$%^&*", "keyup:*,Digit8,56,0,56,0", "keyup:Shift,ShiftLeft,16,0,16,1",
                    "keydown:Shift,ShiftLeft,16,0,16,1", "keydown:(,Digit9,57,0,57,0", "keypress:(,Digit9,40,40,40,0",
                    "input:!@#$%^&*(", "keyup:(,Digit9,57,0,57,0", "keyup:Shift,ShiftLeft,16,0,16,1",
                    "keydown:Shift,ShiftLeft,16,0,16,1", "keydown:),Digit0,48,0,48,0", "keypress:),Digit0,41,41,41,0",
                    "input:!@#$%^&*()", "keyup:),Digit0,48,0,48,0", "keyup:Shift,ShiftLeft,16,0,16,1",
                    "keydown:Shift,ShiftLeft,16,0,16,1", "keydown:_,Minus,189,0,189,0", "keypress:_,Minus,95,95,95,0",
                    "input:!@#$%^&*()_", "keyup:_,Minus,189,0,189,0", "keyup:Shift,ShiftLeft,16,0,16,1",
                    "keydown:Shift,ShiftLeft,16,0,16,1", "keydown:+,Equal,187,0,187,0", "keypress:+,Equal,43,43,43,0",
                    "input:!@#$%^&*()_+", "keyup:+,Equal,187,0,187,0", "keyup:Shift,ShiftLeft,16,0,16,1",
                    "keydown:Shift,ShiftLeft,16,0,16,1", "keydown:{,BracketLeft,219,0,219,0",
                    "keypress:{,BracketLeft,123,123,123,0", "input:!@#$%^&*()_+{", "keyup:{,BracketLeft,219,0,219,0",
                    "keyup:Shift,ShiftLeft,16,0,16,1", "keydown:Shift,ShiftLeft,16,0,16,1",
                    "keydown:},BracketRight,221,0,221,0", "keypress:},BracketRight,125,125,125,0",
                    "input:!@#$%^&*()_+{}", "keyup:},BracketRight,221,0,221,0", "keyup:Shift,ShiftLeft,16,0,16,1",
                    "keydown:Shift,ShiftLeft,16,0,16,1", "keydown:|,Backslash,220,0,220,0",
                    "keypress:|,Backslash,124,124,124,0", "input:!@#$%^&*()_+{}|", "keyup:|,Backslash,220,0,220,0",
                    "keyup:Shift,ShiftLeft,16,0,16,1", "keydown:Shift,ShiftLeft,16,0,16,1",
                    "keydown::,Semicolon,186,0,186,0", "keypress::,Semicolon,58,58,58,0", "input:!@#$%^&*()_+{}|:",
                    "keyup::,Semicolon,186,0,186,0", "keyup:Shift,ShiftLeft,16,0,16,1",
                    "keydown:Shift,ShiftLeft,16,0,16,1", "keydown:\",Quote,222,0,222,0",
                    "keypress:\",Quote,34,34,34,0", "input:!@#$%^&*()_+{}|:\"", "keyup:\",Quote,222,0,222,0",
                    "keyup:Shift,ShiftLeft,16,0,16,1", "keydown:Shift,ShiftLeft,16,0,16,1",
                    "keydown:<,Comma,188,0,188,0", "keypress:<,Comma,60,60,60,0", "input:!@#$%^&*()_+{}|:\"<",
                    "keyup:<,Comma,188,0,188,0", "keyup:Shift,ShiftLeft,16,0,16,1",
                    "keydown:Shift,ShiftLeft,16,0,16,1", "keydown:>,Period,190,0,190,0",
                    "keypress:>,Period,62,62,62,0", "input:!@#$%^&*()_+{}|:\"<>", "keyup:>,Period,190,0,190,0",
                    "keyup:Shift,ShiftLeft,16,0,16,1", "keydown:Shift,ShiftLeft,16,0,16,1",
                    "keydown:?,Slash,191,0,191,0", "keypress:?,Slash,63,63,63,0", "input:!@#$%^&*()_+{}|:\"<>?",
                    "keyup:?,Slash,191,0,191,0", "keyup:Shift,ShiftLeft,16,0,16,1",
                    "keydown:Shift,ShiftLeft,16,0,16,1", "keydown:~,Backquote,192,0,192,0",
                    "keypress:~,Backquote,126,126,126,0", "input:!@#$%^&*()_+{}|:\"<>?~",
                    "keyup:~,Backquote,192,0,192,0", "keyup:Shift,ShiftLeft,16,0,16,1"},
            FF = {"keydown:Shift,ShiftLeft,16,0,16,1", "keydown:!,Digit1,49,0,49,0", "keypress:!,Digit1,33,33,33,0",
                  "input:!", "keyup:!,Digit1,49,0,49,0", "keyup:Shift,ShiftLeft,16,0,16,1",
                  "keydown:Shift,ShiftLeft,16,0,16,1", "keydown:@,Digit2,50,0,50,0", "keypress:@,Digit2,64,64,64,0",
                  "input:!@", "keyup:@,Digit2,50,0,50,0", "keyup:Shift,ShiftLeft,16,0,16,1",
                  "keydown:Shift,ShiftLeft,16,0,16,1", "keydown:#,Digit3,51,0,51,0", "keypress:#,Digit3,35,35,35,0",
                  "input:!@#", "keyup:#,Digit3,51,0,51,0", "keyup:Shift,ShiftLeft,16,0,16,1",
                  "keydown:Shift,ShiftLeft,16,0,16,1", "keydown:$,Digit4,52,0,52,0", "keypress:$,Digit4,36,36,36,0",
                  "input:!@#$", "keyup:$,Digit4,52,0,52,0", "keyup:Shift,ShiftLeft,16,0,16,1",
                  "keydown:Shift,ShiftLeft,16,0,16,1", "keydown:%,Digit5,53,0,53,0", "keypress:%,Digit5,37,37,37,0",
                  "input:!@#$%", "keyup:%,Digit5,53,0,53,0", "keyup:Shift,ShiftLeft,16,0,16,1",
                  "keydown:Shift,ShiftLeft,16,0,16,1", "keydown:^,Digit6,54,0,54,0", "keypress:^,Digit6,94,94,94,0",
                  "input:!@#$%^", "keyup:^,Digit6,54,0,54,0", "keyup:Shift,ShiftLeft,16,0,16,1",
                  "keydown:Shift,ShiftLeft,16,0,16,1", "keydown:&,Digit7,55,0,55,0", "keypress:&,Digit7,38,38,38,0",
                  "input:!@#$%^&", "keyup:&,Digit7,55,0,55,0", "keyup:Shift,ShiftLeft,16,0,16,1",
                  "keydown:Shift,ShiftLeft,16,0,16,1", "keydown:*,Digit8,56,0,56,0", "keypress:*,Digit8,42,42,42,0",
                  "input:!@#$%^&*", "keyup:*,Digit8,56,0,56,0", "keyup:Shift,ShiftLeft,16,0,16,1",
                  "keydown:Shift,ShiftLeft,16,0,16,1", "keydown:(,Digit9,57,0,57,0", "keypress:(,Digit9,40,40,40,0",
                  "input:!@#$%^&*(", "keyup:(,Digit9,57,0,57,0", "keyup:Shift,ShiftLeft,16,0,16,1",
                  "keydown:Shift,ShiftLeft,16,0,16,1", "keydown:),Digit0,48,0,48,0", "keypress:),Digit0,41,41,41,0",
                  "input:!@#$%^&*()", "keyup:),Digit0,48,0,48,0", "keyup:Shift,ShiftLeft,16,0,16,1",
                  "keydown:Shift,ShiftLeft,16,0,16,1", "keydown:_,Minus,173,0,173,0", "keypress:_,Minus,95,95,95,0",
                  "input:!@#$%^&*()_", "keyup:_,Minus,173,0,173,0", "keyup:Shift,ShiftLeft,16,0,16,1",
                  "keydown:Shift,ShiftLeft,16,0,16,1", "keydown:+,Equal,61,0,61,0", "keypress:+,Equal,43,43,43,0",
                  "input:!@#$%^&*()_+", "keyup:+,Equal,61,0,61,0", "keyup:Shift,ShiftLeft,16,0,16,1",
                  "keydown:Shift,ShiftLeft,16,0,16,1", "keydown:{,BracketLeft,219,0,219,0",
                  "keypress:{,BracketLeft,123,123,123,0", "input:!@#$%^&*()_+{", "keyup:{,BracketLeft,219,0,219,0",
                  "keyup:Shift,ShiftLeft,16,0,16,1", "keydown:Shift,ShiftLeft,16,0,16,1",
                  "keydown:},BracketRight,221,0,221,0", "keypress:},BracketRight,125,125,125,0",
                  "input:!@#$%^&*()_+{}", "keyup:},BracketRight,221,0,221,0", "keyup:Shift,ShiftLeft,16,0,16,1",
                  "keydown:Shift,ShiftLeft,16,0,16,1", "keydown:|,Backslash,220,0,220,0",
                  "keypress:|,Backslash,124,124,124,0", "input:!@#$%^&*()_+{}|", "keyup:|,Backslash,220,0,220,0",
                  "keyup:Shift,ShiftLeft,16,0,16,1", "keydown:Shift,ShiftLeft,16,0,16,1",
                  "keydown::,Semicolon,59,0,59,0", "keypress::,Semicolon,58,58,58,0", "input:!@#$%^&*()_+{}|:",
                  "keyup::,Semicolon,59,0,59,0", "keyup:Shift,ShiftLeft,16,0,16,1",
                  "keydown:Shift,ShiftLeft,16,0,16,1", "keydown:\",Quote,222,0,222,0", "keypress:\",Quote,34,34,34,0",
                  "input:!@#$%^&*()_+{}|:\"", "keyup:\",Quote,222,0,222,0", "keyup:Shift,ShiftLeft,16,0,16,1",
                  "keydown:Shift,ShiftLeft,16,0,16,1", "keydown:<,Comma,188,0,188,0", "keypress:<,Comma,60,60,60,0",
                  "input:!@#$%^&*()_+{}|:\"<", "keyup:<,Comma,188,0,188,0", "keyup:Shift,ShiftLeft,16,0,16,1",
                  "keydown:Shift,ShiftLeft,16,0,16,1", "keydown:>,Period,190,0,190,0", "keypress:>,Period,62,62,62,0",
                  "input:!@#$%^&*()_+{}|:\"<>", "keyup:>,Period,190,0,190,0", "keyup:Shift,ShiftLeft,16,0,16,1",
                  "keydown:Shift,ShiftLeft,16,0,16,1", "keydown:?,Slash,191,0,191,0", "keypress:?,Slash,63,63,63,0",
                  "input:!@#$%^&*()_+{}|:\"<>?", "keyup:?,Slash,191,0,191,0", "keyup:Shift,ShiftLeft,16,0,16,1",
                  "keydown:Shift,ShiftLeft,16,0,16,1", "keydown:~,Backquote,192,0,192,0",
                  "keypress:~,Backquote,126,126,126,0", "input:!@#$%^&*()_+{}|:\"<>?~",
                  "keyup:~,Backquote,192,0,192,0", "keyup:Shift,ShiftLeft,16,0,16,1"},
            FF_ESR = {"keydown:Shift,ShiftLeft,16,0,16,1", "keydown:!,Digit1,49,0,49,0",
                      "keypress:!,Digit1,33,33,33,0", "input:!", "keyup:!,Digit1,49,0,49,0",
                      "keyup:Shift,ShiftLeft,16,0,16,1", "keydown:Shift,ShiftLeft,16,0,16,1",
                      "keydown:@,Digit2,50,0,50,0", "keypress:@,Digit2,64,64,64,0", "input:!@",
                      "keyup:@,Digit2,50,0,50,0", "keyup:Shift,ShiftLeft,16,0,16,1",
                      "keydown:Shift,ShiftLeft,16,0,16,1", "keydown:#,Digit3,51,0,51,0",
                      "keypress:#,Digit3,35,35,35,0", "input:!@#", "keyup:#,Digit3,51,0,51,0",
                      "keyup:Shift,ShiftLeft,16,0,16,1", "keydown:Shift,ShiftLeft,16,0,16,1",
                      "keydown:$,Digit4,52,0,52,0", "keypress:$,Digit4,36,36,36,0", "input:!@#$",
                      "keyup:$,Digit4,52,0,52,0", "keyup:Shift,ShiftLeft,16,0,16,1",
                      "keydown:Shift,ShiftLeft,16,0,16,1", "keydown:%,Digit5,53,0,53,0",
                      "keypress:%,Digit5,37,37,37,0", "input:!@#$%", "keyup:%,Digit5,53,0,53,0",
                      "keyup:Shift,ShiftLeft,16,0,16,1", "keydown:Shift,ShiftLeft,16,0,16,1",
                      "keydown:^,Digit6,54,0,54,0", "keypress:^,Digit6,94,94,94,0", "input:!@#$%^",
                      "keyup:^,Digit6,54,0,54,0", "keyup:Shift,ShiftLeft,16,0,16,1",
                      "keydown:Shift,ShiftLeft,16,0,16,1", "keydown:&,Digit7,55,0,55,0",
                      "keypress:&,Digit7,38,38,38,0", "input:!@#$%^&", "keyup:&,Digit7,55,0,55,0",
                      "keyup:Shift,ShiftLeft,16,0,16,1", "keydown:Shift,ShiftLeft,16,0,16,1",
                      "keydown:*,Digit8,56,0,56,0", "keypress:*,Digit8,42,42,42,0", "input:!@#$%^&*",
                      "keyup:*,Digit8,56,0,56,0", "keyup:Shift,ShiftLeft,16,0,16,1",
                      "keydown:Shift,ShiftLeft,16,0,16,1", "keydown:(,Digit9,57,0,57,0",
                      "keypress:(,Digit9,40,40,40,0", "input:!@#$%^&*(", "keyup:(,Digit9,57,0,57,0",
                      "keyup:Shift,ShiftLeft,16,0,16,1", "keydown:Shift,ShiftLeft,16,0,16,1",
                      "keydown:),Digit0,48,0,48,0", "keypress:),Digit0,41,41,41,0", "input:!@#$%^&*()",
                      "keyup:),Digit0,48,0,48,0", "keyup:Shift,ShiftLeft,16,0,16,1",
                      "keydown:Shift,ShiftLeft,16,0,16,1", "keydown:_,Minus,173,0,173,0",
                      "keypress:_,Minus,95,95,95,0", "input:!@#$%^&*()_", "keyup:_,Minus,173,0,173,0",
                      "keyup:Shift,ShiftLeft,16,0,16,1", "keydown:Shift,ShiftLeft,16,0,16,1",
                      "keydown:+,Equal,61,0,61,0", "keypress:+,Equal,43,43,43,0", "input:!@#$%^&*()_+",
                      "keyup:+,Equal,61,0,61,0", "keyup:Shift,ShiftLeft,16,0,16,1",
                      "keydown:Shift,ShiftLeft,16,0,16,1", "keydown:{,BracketLeft,219,0,219,0",
                      "keypress:{,BracketLeft,123,123,123,0", "input:!@#$%^&*()_+{", "keyup:{,BracketLeft,219,0,219,0",
                      "keyup:Shift,ShiftLeft,16,0,16,1", "keydown:Shift,ShiftLeft,16,0,16,1",
                      "keydown:},BracketRight,221,0,221,0", "keypress:},BracketRight,125,125,125,0",
                      "input:!@#$%^&*()_+{}", "keyup:},BracketRight,221,0,221,0", "keyup:Shift,ShiftLeft,16,0,16,1",
                      "keydown:Shift,ShiftLeft,16,0,16,1", "keydown:|,Backslash,220,0,220,0",
                      "keypress:|,Backslash,124,124,124,0", "input:!@#$%^&*()_+{}|", "keyup:|,Backslash,220,0,220,0",
                      "keyup:Shift,ShiftLeft,16,0,16,1", "keydown:Shift,ShiftLeft,16,0,16,1",
                      "keydown::,Semicolon,59,0,59,0", "keypress::,Semicolon,58,58,58,0", "input:!@#$%^&*()_+{}|:",
                      "keyup::,Semicolon,59,0,59,0", "keyup:Shift,ShiftLeft,16,0,16,1",
                      "keydown:Shift,ShiftLeft,16,0,16,1", "keydown:\",Quote,222,0,222,0",
                      "keypress:\",Quote,34,34,34,0", "input:!@#$%^&*()_+{}|:\"", "keyup:\",Quote,222,0,222,0",
                      "keyup:Shift,ShiftLeft,16,0,16,1", "keydown:Shift,ShiftLeft,16,0,16,1",
                      "keydown:<,Comma,188,0,188,0", "keypress:<,Comma,60,60,60,0", "input:!@#$%^&*()_+{}|:\"<",
                      "keyup:<,Comma,188,0,188,0", "keyup:Shift,ShiftLeft,16,0,16,1",
                      "keydown:Shift,ShiftLeft,16,0,16,1", "keydown:>,Period,190,0,190,0",
                      "keypress:>,Period,62,62,62,0", "input:!@#$%^&*()_+{}|:\"<>", "keyup:>,Period,190,0,190,0",
                      "keyup:Shift,ShiftLeft,16,0,16,1", "keydown:Shift,ShiftLeft,16,0,16,1",
                      "keydown:?,Slash,191,0,191,0", "keypress:?,Slash,63,63,63,0", "input:!@#$%^&*()_+{}|:\"<>?",
                      "keyup:?,Slash,191,0,191,0", "keyup:Shift,ShiftLeft,16,0,16,1",
                      "keydown:Shift,ShiftLeft,16,0,16,1", "keydown:~,Backquote,192,0,192,0",
                      "keypress:~,Backquote,126,126,126,0", "input:!@#$%^&*()_+{}|:\"<>?~",
                      "keyup:~,Backquote,192,0,192,0", "keyup:Shift,ShiftLeft,16,0,16,1"})
    public void keysShiftedPunctuation() throws Exception {
        keyEvents("input", "", false, "!@#$%^&*()_+{}|:\"<>?~");
    }

    /**
     * Logs the key events for upper case chars.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"keydown:Shift,ShiftLeft,16,0,16,0", "keydown:A,KeyA,65,0,65,0", "keypress:A,KeyA,65,65,65,0",
                       "input:A", "keyup:A,KeyA,65,0,65,0", "keyup:Shift,ShiftLeft,16,0,16,0",
                       "keydown:b,KeyB,66,0,66,0", "keypress:b,KeyB,98,98,98,0", "input:Ab", "keyup:b,KeyB,66,0,66,0"},
            FF = {"keydown:A,KeyA,65,0,65,0", "keypress:A,KeyA,65,65,65,0", "input:A", "keyup:A,KeyA,65,0,65,0",
                  "keydown:b,KeyB,66,0,66,0", "keypress:b,KeyB,98,98,98,0", "input:Ab", "keyup:b,KeyB,66,0,66,0"},
            FF_ESR = {"keydown:A,KeyA,65,0,65,0", "keypress:A,KeyA,65,65,65,0", "input:A", "keyup:A,KeyA,65,0,65,0",
                      "keydown:b,KeyB,66,0,66,0", "keypress:b,KeyB,98,98,98,0", "input:Ab", "keyup:b,KeyB,66,0,66,0"})
    // chromedriver does not set the location of the key events
    // geckodriver does not send the shift key for upper case chars
    @HtmlUnitNYI(CHROME = {"keydown:Shift,ShiftLeft,16,0,16,1", "keydown:A,KeyA,65,0,65,0",
                           "keypress:A,KeyA,65,65,65,0", "input:A", "keyup:A,KeyA,65,0,65,0",
                           "keyup:Shift,ShiftLeft,16,0,16,1", "keydown:b,KeyB,66,0,66,0", "keypress:b,KeyB,98,98,98,0",
                           "input:Ab", "keyup:b,KeyB,66,0,66,0"},
            EDGE = {"keydown:Shift,ShiftLeft,16,0,16,1", "keydown:A,KeyA,65,0,65,0", "keypress:A,KeyA,65,65,65,0",
                    "input:A", "keyup:A,KeyA,65,0,65,0", "keyup:Shift,ShiftLeft,16,0,16,1", "keydown:b,KeyB,66,0,66,0",
                    "keypress:b,KeyB,98,98,98,0", "input:Ab", "keyup:b,KeyB,66,0,66,0"},
            FF = {"keydown:Shift,ShiftLeft,16,0,16,1", "keydown:A,KeyA,65,0,65,0", "keypress:A,KeyA,65,65,65,0",
                  "input:A", "keyup:A,KeyA,65,0,65,0", "keyup:Shift,ShiftLeft,16,0,16,1", "keydown:b,KeyB,66,0,66,0",
                  "keypress:b,KeyB,98,98,98,0", "input:Ab", "keyup:b,KeyB,66,0,66,0"},
            FF_ESR = {"keydown:Shift,ShiftLeft,16,0,16,1", "keydown:A,KeyA,65,0,65,0", "keypress:A,KeyA,65,65,65,0",
                      "input:A", "keyup:A,KeyA,65,0,65,0", "keyup:Shift,ShiftLeft,16,0,16,1",
                      "keydown:b,KeyB,66,0,66,0", "keypress:b,KeyB,98,98,98,0", "input:Ab", "keyup:b,KeyB,66,0,66,0"})
    public void keysUppercase() throws Exception {
        keyEvents("input", "", false, "Ab");
    }

    /**
     * Logs the modifier state for some key events.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"function", "keydown:a,false,false,false,false,false,false,false",
                       "keyup:a,false,false,false,false,false,false,false",
                       "keydown:Shift,true,false,false,false,false,false,false",
                       "keydown:B,true,false,false,false,false,false,false",
                       "keyup:B,true,false,false,false,false,false,false",
                       "keyup:Shift,false,false,false,false,false,false,false",
                       "keydown:Control,false,true,false,false,false,false,false",
                       "keydown:Home,false,true,false,false,false,false,false",
                       "keyup:Home,false,true,false,false,false,false,false",
                       "keyup:Control,false,false,false,false,false,false,false"},
            FF = {"function", "keydown:a,false,false,false,false,false,false,false",
                  "keyup:a,false,false,false,false,false,false,false",
                  "keydown:Shift,true,false,false,false,false,false,false",
                  "keyup:Shift,false,false,false,false,false,false,false",
                  "keydown:B,true,false,false,false,false,false,false",
                  "keyup:B,true,false,false,false,false,false,false",
                  "keydown:Unidentified,true,false,false,false,false,false,false",
                  "keyup:Unidentified,true,false,false,false,false,false,false",
                  "keydown:Control,true,true,false,false,false,false,false",
                  "keyup:Control,true,false,false,false,false,false,false",
                  "keydown:Home,true,true,false,false,false,false,false",
                  "keyup:Home,true,true,false,false,false,false,false",
                  "keydown:Unidentified,true,true,false,false,false,false,false",
                  "keyup:Unidentified,true,true,false,false,false,false,false"},
            FF_ESR = {"function", "keydown:a,false,false,false,false,false,false,false",
                      "keyup:a,false,false,false,false,false,false,false",
                      "keydown:Shift,true,false,false,false,false,false,false",
                      "keyup:Shift,false,false,false,false,false,false,false",
                      "keydown:B,true,false,false,false,false,false,false",
                      "keyup:B,true,false,false,false,false,false,false",
                      "keydown:Unidentified,true,false,false,false,false,false,false",
                      "keyup:Unidentified,true,false,false,false,false,false,false",
                      "keydown:Control,true,true,false,false,false,false,false",
                      "keyup:Control,true,false,false,false,false,false,false",
                      "keydown:Home,true,true,false,false,false,false,false",
                      "keyup:Home,true,true,false,false,false,false,false",
                      "keydown:Unidentified,true,true,false,false,false,false,false",
                      "keyup:Unidentified,true,true,false,false,false,false,false"})
    // geckodriver keeps the shift key pressed and sends additional unidentified keys
    @HtmlUnitNYI(FF = {"function", "keydown:a,false,false,false,false,false,false,false",
                       "keyup:a,false,false,false,false,false,false,false",
                       "keydown:Shift,true,false,false,false,false,false,false",
                       "keydown:B,true,false,false,false,false,false,false",
                       "keyup:B,true,false,false,false,false,false,false",
                       "keyup:Shift,false,false,false,false,false,false,false",
                       "keydown:Control,false,true,false,false,false,false,false",
                       "keydown:Home,false,true,false,false,false,false,false",
                       "keyup:Home,false,true,false,false,false,false,false",
                       "keyup:Control,false,false,false,false,false,false,false"},
            FF_ESR = {"function", "keydown:a,false,false,false,false,false,false,false",
                      "keyup:a,false,false,false,false,false,false,false",
                      "keydown:Shift,true,false,false,false,false,false,false",
                      "keydown:B,true,false,false,false,false,false,false",
                      "keyup:B,true,false,false,false,false,false,false",
                      "keyup:Shift,false,false,false,false,false,false,false",
                      "keydown:Control,false,true,false,false,false,false,false",
                      "keydown:Home,false,true,false,false,false,false,false",
                      "keyup:Home,false,true,false,false,false,false,false",
                      "keyup:Control,false,false,false,false,false,false,false"})
    public void getModifierState() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head></head><body>\n"
            + "<input type='text' id='t'>\n"
            + LOG_TEXTAREA
            + "<script>\n"
            + LOG_TEXTAREA_FUNCTION
            + "function handler(e) {\n"
            + "  var states = ['Shift', 'Control', 'Alt', 'Meta', 'CapsLock', 'NumLock', 'unknown']"
                                + ".map(function(m) { return e.getModifierState(m); });\n"
            + "  log(e.type + ':' + e.key + ',' + states.join(','));\n"
            + "}\n"
            + "log(typeof KeyboardEvent.prototype.getModifierState);\n"
            + "var t = document.getElementById('t');\n"
            + "t.addEventListener('keydown', handler);\n"
            + "t.addEventListener('keyup', handler);\n"
            + "</script>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("t")).sendKeys("a", Keys.chord(Keys.SHIFT, "b"), Keys.chord(Keys.CONTROL, Keys.HOME));

        verifyTextArea2(driver, getExpectedAlerts());
    }

    private void keyEvents(final String tagName, final String initialValue, final boolean preventKeyDown,
            final CharSequence... keys) throws Exception {
        final String field;
        if ("textarea".equals(tagName)) {
            field = "<textarea id='t'>" + initialValue + "</textarea>\n";
        }
        else {
            field = "<input type='text' id='t' value='" + initialValue + "'>\n";
        }

        final String html = DOCTYPE_HTML
            + "<html><head></head><body>\n"
            + field
            + "<script>\n"
            + LOG_TEXTAREA_FUNCTION
            + "function handler(e) {\n"
            + "  var msg = e.type + ':' + e.key + ',' + e.code + ',' + e.keyCode + ',' "
                            + "+ e.charCode + ',' + e.which + ',' + e.location;\n"
            + "  if (e.type == 'input') {\n"
            + "    msg = e.type + ':' + e.target.value;\n"
            + "  }\n"
            + "  msg = msg.replace(/\\r/g, '\\\\r');\n"
            + "  msg = msg.replace(/\\n/g, '\\\\n');\n"
            + "  log(msg);\n"
            + "  if (" + preventKeyDown + " && e.type == 'keydown') {\n"
            + "    e.preventDefault();\n"
            + "  }\n"
            + "}\n"
            + "var t = document.getElementById('t');\n"
            + "t.addEventListener('keydown', handler);\n"
            + "t.addEventListener('keypress', handler);\n"
            + "t.addEventListener('input', handler);\n"
            + "t.addEventListener('keyup', handler);\n"
            + "</script>\n"
            + LOG_TEXTAREA
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        final WebElement elem = driver.findElement(By.id("t"));
        elem.sendKeys(keys);

        verifyTextArea2(driver, getExpectedAlerts());
    }
}
