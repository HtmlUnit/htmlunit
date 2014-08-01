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

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.html.HtmlPageTest;

/**
 * Tests for {@link HashChangeEvent}.
 *
 * @version $Revision$
 * @author Frank Danek
 */
@RunWith(BrowserRunner.class)
public class HashChangeEventTest extends WebDriverTestCase {

    private static final String DUMP_EVENT_FUNCTION = "  function dump(event) {\n"
        + "    if (event) {\n"
        + "      alert(event);\n"
        + "      alert(event.type);\n"
        + "      alert(event.bubbles);\n"
        + "      alert(event.cancelable);\n"
        + "      alert(event.oldURL);\n"
        + "      alert(event.newURL);\n"
        + "    } else {\n"
        + "      alert('no event');\n"
        + "    }\n"
        + "  }\n";

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "[object HashChangeEvent]", "hashchange", "false", "false", "", "" },
            IE = "exception")
    public void create_ctor() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    try {\n"
            + "      var event = new HashChangeEvent('hashchange');\n"
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
    @Alerts(DEFAULT = { "[object HashChangeEvent]", "hashchange", "true", "false", "null", "§§URL§§#1" },
            IE = "exception")
    public void create_ctorWithDetails() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    try {\n"
            + "      var event = new HashChangeEvent('hashchange', {\n"
            + "        'bubbles': true,\n"
            + "        'oldURL': null,\n"
            + "        'newURL': '" + URL_FIRST + "#1'\n"
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
    @Alerts(DEFAULT = { "[object HashChangeEvent]", "", "true", "true", "", "" },
            FF31 = { "[object HashChangeEvent]", "", "false", "false", "", "" },
            CHROME = { "[object HashChangeEvent]", "", "false", "false", "", "" },
            IE = "exception")
    public void create_createEvent() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    try {\n"
            + "      var event = document.createEvent('HashChangeEvent');\n"
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
    @Alerts(DEFAULT = { "[object HashChangeEvent]", "hashchange", "true", "false", "§§URL§§", "§§URL§§#1" },
            IE = "exception")
    public void initHashChangeEvent() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    try {\n"
            + "      var event = document.createEvent('HashChangeEvent');\n"
            + "      event.initHashChangeEvent('hashchange', true, false, '" + URL_FIRST + "', '"
            + URL_FIRST + "#1');\n"
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
    @Alerts(DEFAULT = { "[object HashChangeEvent]", "hashchange", "true", "false", "§§URL§§", "§§URL§§#1" },
            IE = "exception")
    public void dispatchEvent() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    try {\n"
            + "      var event = document.createEvent('HashChangeEvent');\n"
            + "      event.initHashChangeEvent('hashchange', true, false, '" + URL_FIRST + "', '"
            + URL_FIRST + "#1');\n"
            + "      dispatchEvent(event);\n"
            + "    } catch (e) { alert('exception') }\n"
            + "  }\n"
            + DUMP_EVENT_FUNCTION
            + "  window.onhashchange = dump;\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "[object Event]", "hashchange", "true", "false", "undefined", "undefined" },
            IE8 = "exception")
    public void dispatchEvent_event() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    try {\n"
            + "      var event = document.createEvent('Event');\n"
            + "      event.initEvent('hashchange', true, false);\n"
            + "      dispatchEvent(event);\n"
            + "    } catch (e) { alert('exception') }\n"
            + "  }\n"
            + DUMP_EVENT_FUNCTION
            + "  window.onhashchange = dump;\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("supported")
    public void onHashChange_supported() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    if ('onhashchange' in window) { alert('supported') }\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "[object HashChangeEvent]", "hashchange", "true", "false", "§§URL§§", "§§URL§§#1" },
            CHROME = { "[object HashChangeEvent]", "hashchange", "false", "false", "§§URL§§", "§§URL§§#1" },
            IE = { "[object Event]", "hashchange", "false", "false", "undefined", "undefined" },
            IE8 = { "no event" })
    public void onHashChange() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head><title>foo</title><script>\n"
            + DUMP_EVENT_FUNCTION
            + "  window.onhashchange = dump;\n"
            + "</script></head><body>\n"
            + "  <a id='click' href='#1'>change hash</a>\n"
            + "</body></html>";

        expandExpectedAlertsVariables(getDefaultUrl());
        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("click")).click();
        assertEquals(getExpectedAlerts(), getCollectedAlerts(driver));
    }
}
