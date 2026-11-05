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
import org.htmlunit.junit.annotation.HtmlUnitNYI;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;

/**
 * Tests for {@link InputEvent}.
 *
 * @author Ronald Brill
 */
public class InputEventTest extends WebDriverTestCase {

    private static final String DUMP_EVENT_FUNCTION = "  function dump(event) {\n"
            + "    log(event);\n"
            + "    log(event.type);\n"
            + "    log(event.bubbles);\n"
            + "    log(event.cancelable);\n"
            + "    log(event.composed);\n"

            + "    var details = [event.data, event.inputType, event.isComposing].join(',');\n"
            + "    log(details);\n"
            + "  }\n";

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[object InputEvent]", "type", "false", "false", "false", ",,false"})
    public void create_ctor() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head><script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    try {\n"
                + "      var event = new InputEvent('type');\n"
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
    @HtmlUnitNYI(CHROME = {"[object InputEvent]", "undefined", "false", "false", "false",
                           ",,false"},
            EDGE = {"[object InputEvent]", "undefined", "false", "false", "false",
                    ",,false"},
            FF = {"[object InputEvent]", "undefined", "false", "false", "false",
                  ",,false"},
            FF_ESR = {"[object InputEvent]", "undefined", "false", "false", "false",
                      ",,false"})
    public void create_ctorWithoutType() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    try {\n"
            + "      var event = new InputEvent();\n"
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
    @Alerts({"[object InputEvent]", "42", "false", "false", "false", ",,false"})
    public void create_ctorNumericType() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    try {\n"
            + "      var event = new InputEvent(42);\n"
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
    @Alerts({"[object InputEvent]", "null", "false", "false", "false", ",,false"})
    public void create_ctorNullType() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    try {\n"
            + "      var event = new InputEvent(null);\n"
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
            + "      var event = new InputEvent(unknown);\n"
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
    @Alerts({"[object InputEvent]", "HtmlUnitEvent", "false", "false", "false", ",,false"})
    public void create_ctorArbitraryType() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    try {\n"
            + "      var event = new InputEvent('HtmlUnitEvent');\n"
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
    @Alerts(DEFAULT = {"[object InputEvent]", "input", "false", "false", "false",
                       "data,inputType,true"},
            CHROME = {"[object InputEvent]", "input", "false", "false", "false",
                      "data,,true"},
            EDGE = {"[object InputEvent]", "input", "false", "false", "false",
                    "data,,true"})
    public void create_ctorAllDetails() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    try {\n"
            + "      var event = new InputEvent('input', "
                             + "{ inputType: 'inputType', data: 'data', isComposing: true });\n"
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
    @Alerts({"[object InputEvent]", "input", "false", "false", "false", ",,true"})
    public void create_ctorSomeDetails() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    try {\n"
            + "      var event = new InputEvent('input', "
                             + "{ isComposing: true });\n"
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
    @Alerts({"[object InputEvent]", "input", "false", "false", "false", ",,false"})
    public void create_ctorMissingData() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    try {\n"
            + "      var event = new InputEvent('input', {\n"
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
    @Alerts({"[object InputEvent]", "input", "false", "false", "false", ",,false"})
    public void create_ctorNullData() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    try {\n"
            + "      var event = new InputEvent('input', null);\n"
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
    @Alerts({"[object InputEvent]", "input", "false", "false", "false", ",,false"})
    public void create_ctorUndefinedData() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    try {\n"
            + "      var event = new InputEvent('input', undefined);\n"
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
    @Alerts({"[object InputEvent]", "input", "false", "false", "false", "Html,Unit,,false"})
    public void create_ctorWrongData() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    try {\n"
            + "      var event = new InputEvent('input', {\n"
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
    @Alerts("NotSupportedError/DOMException")
    public void create_createEvent() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    try {\n"
            + "      var event = document.createEvent('InputEvent');\n"
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
    @Alerts("true")
    public void inWindow() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    function test() {\n"
            + "      log('InputEvent' in window);\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "</body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Logs the input events for typing some chars.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[object InputEvent],insertText,a,true,false,false,a",
             "[object InputEvent],insertText,B,true,false,false,aB",
             "[object InputEvent],insertText,1,true,false,false,aB1"})
    public void typingText() throws Exception {
        typing("input", "", "aB1");
    }

    /**
     * Logs the input events for typing the space key.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("[object InputEvent],insertText, ,true,false,false,ab ")
    public void typingSpace() throws Exception {
        typing("input", "ab", Keys.SPACE);
    }

    /**
     * Logs the input events for backspace.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("[object InputEvent],deleteContentBackward,null,true,false,false,ab")
    public void typingBackspace() throws Exception {
        typing("input", "abc", Keys.BACK_SPACE);
    }

    /**
     * Logs the input events for backspace together with control.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("[object InputEvent],deleteWordBackward,null,true,false,false,abc ")
    public void typingCtrlBackspace() throws Exception {
        typing("input", "abc def", Keys.chord(Keys.CONTROL, Keys.BACK_SPACE));
    }

    /**
     * Logs the input events for delete.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("[object InputEvent],deleteContentForward,null,true,false,false,bc")
    public void typingDelete() throws Exception {
        typing("input", "abc", Keys.HOME, Keys.DELETE);
    }

    /**
     * Logs the input events for delete together with control.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("[object InputEvent],deleteWordForward,null,true,false,false,def")
    public void typingCtrlDelete() throws Exception {
        typing("input", "abc def", Keys.HOME, Keys.chord(Keys.CONTROL, Keys.DELETE));
    }

    /**
     * Logs the input events for enter in an input field.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({})
    public void typingEnterInput() throws Exception {
        typing("input", "abc", Keys.ENTER);
    }

    /**
     * Logs the input events for enter in a textarea.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("[object InputEvent],insertLineBreak,null,true,false,false,abc\\n")
    public void typingEnterTextArea() throws Exception {
        typing("textarea", "abc", Keys.ENTER);
    }

    /**
     * Logs the input events for the numpad keys.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[object InputEvent],insertText,1,true,false,false,1",
             "[object InputEvent],insertText,+,true,false,false,1+"})
    public void typingNumpad() throws Exception {
        typing("input", "", Keys.NUMPAD1, Keys.ADD);
    }

    /**
     * Logs the input events for the arrow keys (no input events).
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({})
    public void typingArrows() throws Exception {
        typing("input", "abc", Keys.ARROW_LEFT, Keys.ARROW_RIGHT);
    }

    private void typing(final String tagName, final String initialValue, final CharSequence... keys)
            throws Exception {
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
            + "  var msg = e + ',' + e.inputType + ',' + e.data + ',' + e.bubbles + ',' "
                            + "+ e.cancelable + ',' + e.isComposing + ',' + e.target.value;\n"
            + "  msg = msg.replace(/\\r/g, '\\\\r');\n"
            + "  msg = msg.replace(/\\n/g, '\\\\n');\n"
            + "  log(msg);\n"
            + "}\n"
            + "document.getElementById('t').addEventListener('input', handler);\n"
            + "</script>\n"
            + LOG_TEXTAREA
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("t")).sendKeys(keys);

        verifyTextArea2(driver, getExpectedAlerts());
    }
}
