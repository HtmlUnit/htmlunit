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
package com.gargoylesoftware.htmlunit.javascript.host.event;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.html.HtmlPageTest;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * Unit tests for {@link UIEvent}.
 *
 * @author Daniel Gredler
 * @author Frank Danek
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class UIEventTest extends WebDriverTestCase {

    private static final String DUMP_EVENT_FUNCTION = "  function dump(event) {\n"
            + "    alert(event);\n"
            + "    alert(event.type);\n"
            + "    alert(event.bubbles);\n"
            + "    alert(event.cancelable);\n"
            + "    alert(event.composed);\n"

            + "    alert(event.view == window);\n"
            + "  }\n";

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"[object UIEvent]", "event", "false", "false", "false", "false"},
            IE = "exception")
    public void create_ctor() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    try {\n"
            + "      var event = new UIEvent('event');\n"
            + "      dump(event);\n"
            + "    } catch (e) { alert('exception') }\n"
            + "  }\n"
            + DUMP_EVENT_FUNCTION
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"[object UIEvent]", "event", "true", "false", "false", "true"},
            IE = "exception")
    public void create_ctorWithDetails() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    try {\n"
            + "      var event = new UIEvent('event', {\n"
            + "        'bubbles': true,\n"
            + "        'view': window\n"
            + "      });\n"
            + "      dump(event);\n"
            + "    } catch (e) { alert('exception') }\n"
            + "  }\n"
            + DUMP_EVENT_FUNCTION
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void create_ctorWithDetailsViewNotWindow() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    try {\n"
            + "      var event = new UIEvent('event', {\n"
            + "        'view': {}\n"
            + "      });\n"
            + "    } catch (e) { alert('exception') }\n"
            + "  }\n"
            + DUMP_EVENT_FUNCTION
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"DOM2: [object UIEvent]", "DOM3: [object UIEvent]"})
    public void createEvent() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    try {\n"
            + "      alert('DOM2: ' + document.createEvent('UIEvents'));\n"
            + "    } catch(e) {alert('DOM2: exception')}\n"
            + "    try {\n"
            + "      alert('DOM3: ' + document.createEvent('UIEvent'));\n"
            + "    } catch(e) {alert('DOM3: exception')}\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"[object UIEvent]", "click", "true", "true", "true", "7"})
    public void initUIEvent() throws Exception {
        final String html = "<html><body><script>\n"
            + "try {\n"
            + "  var e = document.createEvent('UIEvents');\n"
            + "  alert(e);\n"
            + "  e.initUIEvent('click', true, true, window, 7);\n"
            + "  alert(e.type);\n"
            + "  alert(e.bubbles);\n"
            + "  alert(e.cancelable);\n"
            + "  alert(e.view == window);\n"
            + "  alert(e.detail);\n"
            + "} catch(e) { alert('exception') }\n"
            + "</script></body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"[object Event]", "undefined", "[object MouseEvent]", "1", "[object MouseEvent]", "2"},
            IE = {"[object Event]", "undefined", "[object PointerEvent]", "0", "[object PointerEvent]", "0"})
    public void detail() throws Exception {
        final String html =
              "<html><head><script>\n"
            + "  function alertDetail(e) {\n"
            + "    alert(e);\n"
            + "    alert(e.detail);\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='alertDetail(event)'>\n"
            + "  <div id='a' onclick='alertDetail(event)'>abc</div>\n"
            + "  <div id='b' ondblclick='alertDetail(event)'>xyz</div>\n"
            + "</body></html>";

        final String[] alerts = getExpectedAlerts();
        int i = 0;

        final WebDriver driver = loadPage2(html);
        verifyAlerts(driver, alerts[i++], alerts[i++]);

        driver.findElement(By.id("a")).click();
        verifyAlerts(driver, alerts[i++], alerts[i++]);

        final Actions action = new Actions(driver);
        action.doubleClick(driver.findElement(By.id("b")));
        action.perform();
        verifyAlerts(driver, alerts[i++], alerts[i++]);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"[object Event]", "undefined", "[object MouseEvent]", "[object Window]"},
            IE = {"[object Event]", "undefined", "[object PointerEvent]", "[object Window]"})
    public void view() throws Exception {
        final String html =
              "<html><body onload='alertView(event)'><script>\n"
            + "  function alertView(e) {\n"
            + "    alert(e);\n"
            + "    alert(e.view);\n"
            + "  }\n"
            + "</script>\n"
            + "<form><input type='button' id='b' onclick='alertView(event)'></form>\n"
            + "</body></html>";

        final String[] alerts = getExpectedAlerts();
        int i = 0;

        final WebDriver driver = loadPage2(html);
        verifyAlerts(driver, alerts[i++], alerts[i++]);

        driver.findElement(By.id("b")).click();
        verifyAlerts(driver, alerts[i++], alerts[i++]);
    }
}
