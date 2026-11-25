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

/**
 * Tests for {@code Event.isTrusted}: events fired by the browser are trusted, events created
 * or dispatched by a script are not.
 *
 * @author Ronald Brill
 */
public class EventIsTrustedTest extends WebDriverTestCase {

    private void logExpression(final String expression) throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  try {\n"
            + "    log(" + expression + ");\n"
            + "  } catch(e) { logEx(e); }\n"
            + "</script></head><body></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void eventConstructor() throws Exception {
        logExpression("new Event('test').isTrusted");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void mouseEventConstructor() throws Exception {
        logExpression("new MouseEvent('click').isTrusted");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void pointerEventConstructor() throws Exception {
        logExpression("new PointerEvent('pointerdown').isTrusted");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void createEvent() throws Exception {
        logExpression("document.createEvent('Event').isTrusted");
    }

    /**
     * The property is an own property of the event (LegacyUnforgeable), not one of the prototype.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @HtmlUnitNYI(CHROME = "true",
            EDGE = "true",
            FF = "true",
            FF_ESR = "true")
    public void inPrototype() throws Exception {
        logExpression("'isTrusted' in Event.prototype");
    }

    /**
     * The load event of the window is fired by the browser.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void loadEvent() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  window.addEventListener('load', function(e) { log(e.isTrusted); });\n"
            + "</script></head><body></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void dispatchEvent() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var div = document.getElementById('d');\n"
            + "    div.addEventListener('test', function(e) { log(e.isTrusted); });\n"
            + "    div.dispatchEvent(new Event('test'));\n"
            + "  }\n"
            + "</script></head><body onload='test()'><div id='d'></div></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void dispatchEventWindow() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    window.addEventListener('test', function(e) { log(e.isTrusted); });\n"
            + "    window.dispatchEvent(new Event('test'));\n"
            + "  }\n"
            + "</script></head><body onload='test()'></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void dispatchEventDocument() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    document.addEventListener('test', function(e) { log(e.isTrusted); });\n"
            + "    document.dispatchEvent(new Event('test'));\n"
            + "  }\n"
            + "</script></head><body onload='test()'></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * The click() method of an element fires a synthetic click event.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void clickFromScript() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var b = document.getElementById('b');\n"
            + "    b.addEventListener('click', function(e) { log(e.isTrusted); });\n"
            + "    b.click();\n"
            + "  }\n"
            + "</script></head><body onload='test()'><button id='b'>b</button></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * The click() method of a checkbox fires a synthetic click event; the input and change
     * events of the state change are fired by the browser.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"click false", "input true", "change true"})
    public void clickFromScriptCheckbox() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var c = document.getElementById('c');\n"
            + "    c.addEventListener('click', function(e) { log('click ' + e.isTrusted); });\n"
            + "    c.addEventListener('input', function(e) { log('input ' + e.isTrusted); });\n"
            + "    c.addEventListener('change', function(e) { log('change ' + e.isTrusted); });\n"
            + "    c.click();\n"
            + "  }\n"
            + "</script></head><body onload='test()'><input type='checkbox' id='c'></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * The click() method of a radio button fires a synthetic click event; the input and change
     * events of the state change are fired by the browser.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"click false", "input true", "change true", "click false"})
    public void clickFromScriptRadio() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var r = document.getElementById('r');\n"
            + "    r.addEventListener('click', function(e) { log('click ' + e.isTrusted); });\n"
            + "    r.addEventListener('input', function(e) { log('input ' + e.isTrusted); });\n"
            + "    r.addEventListener('change', function(e) { log('change ' + e.isTrusted); });\n"
            + "    r.click();\n"
            + "    r.click();\n"
            + "  }\n"
            + "</script></head><body onload='test()'><input type='radio' name='n' id='r'></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * A click of the user (WebDriver) on a checkbox; the input event is a plain Event,
     * bubbles and is not cancelable.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"click true PointerEvent true true", "input true Event true false", "change true Event true false"})
    public void clickByUserCheckbox() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var c = document.getElementById('c');\n"
            + "    var handler = function(e) {\n"
            + "      log(e.type + ' ' + e.isTrusted + ' ' + e.constructor.name\n"
            + "          + ' ' + e.bubbles + ' ' + e.cancelable);\n"
            + "    };\n"
            + "    c.addEventListener('click', handler);\n"
            + "    c.addEventListener('input', handler);\n"
            + "    c.addEventListener('change', handler);\n"
            + "  }\n"
            + "</script></head><body onload='test()'><input type='checkbox' id='c'></body></html>";

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("c")).click();
        verifyTitle2(driver, getExpectedAlerts());
    }

    /**
     * A click of the user (WebDriver).
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void clickByUser() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "</script></head><body>\n"
            + "<button id='b' onclick='log(event.isTrusted)'>b</button>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("b")).click();
        verifyTitle2(driver, getExpectedAlerts());
    }

    /**
     * The abort event of an AbortSignal is fired by the browser, also if abort() is called by a script.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void abortEvent() throws Exception {
        logExpression("(function() { var c = new AbortController(); var t;"
                + " c.signal.onabort = function(e) { t = e.isTrusted; }; c.abort(); return t; })()");
    }
}