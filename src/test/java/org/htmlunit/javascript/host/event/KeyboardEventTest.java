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
}
