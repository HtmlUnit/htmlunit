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
import org.openqa.selenium.WebDriver;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.html.HtmlPageTest;

/**
 * Tests for {@link BeforeUnloadEvent}.
 *
 * @author Frank Danek
 */
@RunWith(BrowserRunner.class)
public class BeforeUnloadEventTest extends WebDriverTestCase {

    private static final String DUMP_EVENT_FUNCTION = "  function dump(event) {\n"
        + "    if (event) {\n"
        + "      document.title += ' -' + event;\n"
        + "      document.title += ' -' + event.type;\n"
        + "      document.title += ' -' + event.bubbles;\n"
        + "      document.title += ' -' + event.cancelable;\n"
        + "      document.title += ' -' + event.composed;\n"
        + "      document.title += ' -' + event.returnValue;\n"
        + "    } else {\n"
        + "      document.title += ' ' + 'no event';\n"
        + "    }\n"
        + "  }\n";

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void create_ctor() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    try {\n"
            + "      var event = new BeforeUnloadEvent('beforeunload');\n"
            + "    } catch (e) { log('exception') }\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"-[object BeforeUnloadEvent]", "-", "-false", "-false", "-false", "-"},
            IE = "exception")
    public void create_createEvent() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head><script>\n"
            + "  function test() {\n"
            + "    try {\n"
            + "      var event = document.createEvent('BeforeUnloadEvent');\n"
            + "      dump(event);\n"
            + "    } catch (e) { document.title += 'exception' }\n"
            + "  }\n"
            + DUMP_EVENT_FUNCTION
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        assertTitle(driver, String.join(" ", getExpectedAlerts()));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"-[object BeforeUnloadEvent]", "-beforeunload", "-true", "-false", "-false", "-"},
            IE = "exception")
    public void initEvent() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head><script>\n"
            + "  function test() {\n"
            + "    try {\n"
            + "      var event = document.createEvent('BeforeUnloadEvent');\n"
            + "      event.initEvent('beforeunload', true, false);\n"
            + "      dump(event);\n"
            + "    } catch (e) { document.title += 'exception' }\n"
            + "  }\n"
            + DUMP_EVENT_FUNCTION
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        assertTitle(driver, String.join(" ", getExpectedAlerts()));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"-[object BeforeUnloadEvent]", "-beforeunload", "-true", "-false", "-false", "-"},
            IE = "exception")
    public void dispatchEvent() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head><script>\n"
            + "  function test() {\n"
            + "    try {\n"
            + "      var event = document.createEvent('BeforeUnloadEvent');\n"
            + "      event.initEvent('beforeunload', true, false);\n"
            + "      dispatchEvent(event);\n"
            + "    } catch (e) { document.title += 'exception' }\n"
            + "  }\n"
            + DUMP_EVENT_FUNCTION
            + "  window.onbeforeunload  = dump;\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        assertTitle(driver, String.join(" ", getExpectedAlerts()));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"-[object Event]", "-beforeunload", "-true", "-false", "-false", "-true"},
            IE = {"-[object Event]", "-beforeunload", "-true", "-false", "-undefined", "-undefined"})
    public void dispatchEvent_event() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head><script>\n"
            + "  function test() {\n"
            + "    try {\n"
            + "      var event = document.createEvent('Event');\n"
            + "      event.initEvent('beforeunload', true, false);\n"
            + "      dispatchEvent(event);\n"
            + "    } catch (e) { document.title = 'exception'; }\n"
            + "  }\n"
            + DUMP_EVENT_FUNCTION
            + "  window.onbeforeunload = dump;\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        assertTitle(driver, String.join(" ", getExpectedAlerts()));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("supported")
    public void onBeforeUnload_supported() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    if ('onbeforeunload' in window) { log('supported') }\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }
}
