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
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;

/**
 * Unit tests for {@link UIEvent}.
 *
 * @author Daniel Gredler
 * @author Frank Danek
 * @author Ronald Brill
 */
public class UIEventTest extends WebDriverTestCase {

    private static final String DUMP_EVENT_FUNCTION = "  function dump(event) {\n"
            + "    log(event);\n"
            + "    log(event.type);\n"
            + "    log(event.bubbles);\n"
            + "    log(event.cancelable);\n"
            + "    log(event.composed);\n"

            + "    log(event.detail);\n"
            + "    log(event.view == window);\n"
            + "    log(event.which);\n"
            + "  }\n";

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[object UIEvent]", "event", "false", "false", "false", "0", "false", "0"})
    public void create_ctor() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    try {\n"
            + "      var event = new UIEvent('event');\n"
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
    @Alerts({"[object UIEvent]", "event", "true", "false", "false", "0", "true", "0"})
    public void create_ctorWithDetails() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    try {\n"
            + "      var event = new UIEvent('event', {\n"
            + "        'bubbles': true,\n"
            + "        'view': window\n"
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
    @Alerts("TypeError")
    @HtmlUnitNYI(CHROME = {"[object UIEvent]", "undefined", "false", "false", "false", "0", "false", "0"},
            EDGE = {"[object UIEvent]", "undefined", "false", "false", "false", "0", "false", "0"},
            FF = {"[object UIEvent]", "undefined", "false", "false", "false", "0", "false", "0"},
            FF_ESR = {"[object UIEvent]", "undefined", "false", "false", "false", "0", "false", "0"})
    public void create_ctorWithoutType() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    try {\n"
            + "      var event = new UIEvent();\n"
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
    @Alerts({"[object UIEvent]", "42", "false", "false", "false", "0", "false", "0"})
    public void create_ctorNumericType() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    try {\n"
            + "      var event = new UIEvent(42);\n"
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
    @Alerts({"[object UIEvent]", "null", "false", "false", "false", "0", "false", "0"})
    public void create_ctorNullType() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    try {\n"
            + "      var event = new UIEvent(null);\n"
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
            + "      var event = new UIEvent(unknown);\n"
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
    @Alerts({"[object UIEvent]", "HtmlUnitEvent", "false", "false", "false", "0", "false", "0"})
    public void create_ctorArbitraryType() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    try {\n"
            + "      var event = new UIEvent('HtmlUnitEvent');\n"
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
    @Alerts({"[object UIEvent]", "event", "true", "false", "false", "0", "true", "0"})
    public void create_ctorAllDetails() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    try {\n"
            + "      var event = new UIEvent('event', {\n"
            + "        'bubbles': true,\n"
            + "        'view': window\n"
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
    @Alerts({"[object UIEvent]", "event", "false", "false", "false", "0", "false", "0"})
    public void create_ctorAllDetailsMissingData() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    try {\n"
            + "      var event = new UIEvent('event', {\n"
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
    @Alerts("TypeError")
    public void create_ctorWithDetailsViewNotWindow() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    try {\n"
            + "      var event = new UIEvent('event', {\n"
            + "        'view': {}\n"
            + "      });\n"
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
    @Alerts({"DOM2: [object UIEvent]", "DOM3: [object UIEvent]"})
    public void createEvent() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    try {\n"
            + "      log('DOM2: ' + document.createEvent('UIEvents'));\n"
            + "    } catch(e) {log('DOM2: exception')}\n"
            + "    try {\n"
            + "      log('DOM3: ' + document.createEvent('UIEvent'));\n"
            + "    } catch(e) {log('DOM3: exception')}\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"[object UIEvent]", "", "false", "false", "false", "0", "false", "0"},
            FF = {"[object UIEvent]", "", "false", "false", "false", "0", "true", "0"},
            FF_ESR = {"[object UIEvent]", "", "false", "false", "false", "0", "true", "0"})
    @HtmlUnitNYI(CHROME = {"[object UIEvent]", "", "false", "false", "false", "0", "true", "0"},
            EDGE = {"[object UIEvent]", "", "false", "false", "false", "0", "true", "0"})
    public void create_createEvent() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    try {\n"
            + "      var event = document.createEvent('UIEvent');\n"
            + "      dump(event);\n"
            + "    } catch(e) { logEx(e) }\n"
            + "  }\n"
            + DUMP_EVENT_FUNCTION
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"[object UIEvent]", "click", "true", "true", "true", "7"})
    public void initUIEvent() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body><script>\n"
            + LOG_TITLE_FUNCTION
            + "try {\n"
            + "  var e = document.createEvent('UIEvents');\n"
            + "  log(e);\n"
            + "  e.initUIEvent('click', true, true, window, 7);\n"
            + "  log(e.type);\n"
            + "  log(e.bubbles);\n"
            + "  log(e.cancelable);\n"
            + "  log(e.view == window);\n"
            + "  log(e.detail);\n"
            + "} catch(e) { logEx(e) }\n"
            + "</script></body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = {"[object Event]", "undefined", "[object PointerEvent]", "1",
                      "[object MouseEvent]", "2", "[object PointerEvent]", "0"},
            EDGE = {"[object Event]", "undefined", "[object PointerEvent]", "1",
                    "[object MouseEvent]", "2", "[object PointerEvent]", "0"},
            FF = {"[object Event]", "undefined", "[object PointerEvent]", "1",
                  "[object MouseEvent]", "2", "[object PointerEvent]", "1"},
            FF_ESR = {"[object Event]", "undefined", "[object PointerEvent]", "1",
                      "[object MouseEvent]", "2", "[object PointerEvent]", "1"})
    public void detail() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function alertDetail(e) {\n"
            + "    log(e);\n"
            + "    log(e.detail);\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='alertDetail(event)'>\n"
            + "  <div id='a' onclick='alertDetail(event)'>abc</div>\n"
            + "  <div id='b' ondblclick='alertDetail(event)'>xyz</div>\n"
            + "  <div id='c' oncontextmenu='alertDetail(event)'>xyz</div>\n"
            + "</body></html>";

        final String[] alerts = getExpectedAlerts();
        int i = 0;

        final WebDriver driver = loadPage2(html);
        verifyTitle2(driver, alerts[i++], alerts[i++]);

        i = 0;
        driver.findElement(By.id("a")).click();
        verifyTitle2(driver, alerts[i++], alerts[i++], alerts[i++], alerts[i++]);

        i = 0;
        Actions action = new Actions(driver);
        action.doubleClick(driver.findElement(By.id("b")));
        action.perform();
        verifyTitle2(driver, alerts[i++], alerts[i++], alerts[i++], alerts[i++], alerts[i++], alerts[i++]);

        action = new Actions(driver);
        action.contextClick(driver.findElement(By.id("c")));
        action.perform();
        verifyTitle2(driver, alerts);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = {"[object Event]", "undefined", "[object PointerEvent]", "1",
                      "[object MouseEvent]", "2", "[object PointerEvent]", "0"},
            EDGE = {"[object Event]", "undefined", "[object PointerEvent]", "1",
                    "[object MouseEvent]", "2", "[object PointerEvent]", "0"},
            FF = {"[object Event]", "undefined", "[object PointerEvent]", "1",
                  "[object MouseEvent]", "2", "[object PointerEvent]", "1"},
            FF_ESR = {"[object Event]", "undefined", "[object PointerEvent]", "1",
                      "[object MouseEvent]", "2", "[object PointerEvent]", "1"})
    public void detailInputText() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function alertDetail(e) {\n"
            + "    log(e);\n"
            + "    log(e.detail);\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='alertDetail(event)'>\n"
            + "  <input type='text' id='a' onclick='alertDetail(event)'>\n"
            + "  <input type='text' id='b' ondblclick='alertDetail(event)'>\n"
            + "  <input type='text' id='c' oncontextmenu='alertDetail(event)'>\n"
            + "</body></html>";

        final String[] alerts = getExpectedAlerts();
        int i = 0;

        final WebDriver driver = loadPage2(html);
        verifyTitle2(driver, alerts[i++], alerts[i++]);

        i = 0;
        driver.findElement(By.id("a")).click();
        verifyTitle2(driver, alerts[i++], alerts[i++], alerts[i++], alerts[i++]);


        i = 0;
        Actions action = new Actions(driver);
        action.doubleClick(driver.findElement(By.id("b")));
        action.perform();
        verifyTitle2(driver, alerts[i++], alerts[i++], alerts[i++], alerts[i++], alerts[i++], alerts[i++]);

        action = new Actions(driver);
        action.contextClick(driver.findElement(By.id("c")));
        action.perform();
        verifyTitle2(driver, alerts);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = {"[object Event]", "undefined", "[object PointerEvent]", "1",
                      "[object MouseEvent]", "2", "[object PointerEvent]", "0"},
            EDGE = {"[object Event]", "undefined", "[object PointerEvent]", "1",
                    "[object MouseEvent]", "2", "[object PointerEvent]", "0"},
            FF = {"[object Event]", "undefined", "[object PointerEvent]", "1",
                  "[object MouseEvent]", "2", "[object PointerEvent]", "1"},
            FF_ESR = {"[object Event]", "undefined", "[object PointerEvent]", "1",
                      "[object MouseEvent]", "2", "[object PointerEvent]", "1"})
    public void detailInputRadio() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function alertDetail(e) {\n"
            + "    log(e);\n"
            + "    log(e.detail);\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='alertDetail(event)'>\n"
            + "  <input type='radio' id='a' onclick='alertDetail(event)'>\n"
            + "  <input type='radio' id='b' ondblclick='alertDetail(event)'>\n"
            + "  <input type='radio' id='c' oncontextmenu='alertDetail(event)'>\n"
            + "</body></html>";

        final String[] alerts = getExpectedAlerts();
        int i = 0;

        final WebDriver driver = loadPage2(html);
        verifyTitle2(driver, alerts[i++], alerts[i++]);

        i = 0;
        driver.findElement(By.id("a")).click();
        verifyTitle2(driver, alerts[i++], alerts[i++], alerts[i++], alerts[i++]);


        i = 0;
        Actions action = new Actions(driver);
        action.doubleClick(driver.findElement(By.id("b")));
        action.perform();
        verifyTitle2(driver, alerts[i++], alerts[i++], alerts[i++], alerts[i++], alerts[i++], alerts[i++]);

        action = new Actions(driver);
        action.contextClick(driver.findElement(By.id("c")));
        action.perform();
        verifyTitle2(driver, alerts);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"[object Event]", "undefined", "[object PointerEvent]", "[object Window]"})
    public void view() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body onload='alertView(event)'><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function alertView(e) {\n"
            + "    log(e);\n"
            + "    log(e.view);\n"
            + "  }\n"
            + "</script>\n"
            + "<form><input type='button' id='b' onclick='alertView(event)'></form>\n"
            + "</body></html>";

        final String[] alerts = getExpectedAlerts();

        final WebDriver driver = loadPage2(html);
        verifyTitle2(driver, alerts[0], alerts[1]);

        driver.findElement(By.id("b")).click();
        verifyTitle2(driver, alerts);
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
            + "      log('UIEvent' in window);\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "</body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }
}
