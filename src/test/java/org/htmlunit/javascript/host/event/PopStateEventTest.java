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
import org.htmlunit.junit.annotation.HtmlUnitNYI;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link PopStateEvent}.
 *
 * @author Ronald Brill
 */
public class PopStateEventTest extends WebDriverTestCase {

    private static final String DUMP_EVENT_FUNCTION =
          "  function dump(event) {\n"
        + "    if (event) {\n"
        + "      log(event);\n"
        + "      log(event.target);\n"
        + "      log(event.type);\n"
        + "      log(event.bubbles);\n"
        + "      log(event.cancelable);\n"
        + "      log(event.composed);\n"

        + "      log(event.state);\n"
        + "    } else {\n"
        + "      log('no event');\n"
        + "    }\n"
        + "  }\n";

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[object PopStateEvent]", "null", "popstate", "false", "false", "false", "null"})
    public void create_ctor() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    try {\n"
            + "      var event = new PopStateEvent('popstate');\n"
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
    @Alerts({"[object PopStateEvent]", "null", "popstate", "true", "false", "false", "2"})
    public void create_ctorWithDetails() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    try {\n"
            + "      var event = new PopStateEvent('popstate', {\n"
            + "        'bubbles': true,\n"
            + "        'state': 2,\n"
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
    @Alerts(DEFAULT = {"[object PopStateEvent]", "null", "", "false", "false", "false", "null"},
            FF = "NotSupportedError/DOMException",
            FF_ESR = "NotSupportedError/DOMException")
    public void create_createEvent() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    try {\n"
            + "      var event = document.createEvent('PopStateEvent');\n"
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
    @Alerts(DEFAULT = {"[object PopStateEvent]", "null", "", "false", "false", "false", "null"},
            FF = "NotSupportedError/DOMException",
            FF_ESR = "NotSupportedError/DOMException")
    public void setState() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    try {\n"
            + "      var event = document.createEvent('PopStateEvent');\n"
            + "      event.state = 'test';\n"
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
    @Alerts(DEFAULT = "dispatched",
            FF = "exception ctor",
            FF_ESR = "exception ctor")
    public void dispatchEvent() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    try {\n"
            + "      var event = document.createEvent('PopStateEvent');\n"
            + "      event.initEvent('', true, true);\n"
            + "    } catch(e) { log('exception ctor'); return; }\n"

            + "    try {\n"
            + "      dispatchEvent(event);\n"
            + "      log('dispatched');\n"
            + "    } catch(e) { log('exception' + e) }\n"
            + "  }\n"
            + DUMP_EVENT_FUNCTION
            + "  try {\n"
            + "    window.addEventListener('popstate',dump);\n"
            + "  } catch(e) { }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "InvalidStateError/DOMException",
            FF = "ctor NotSupportedError",
            FF_ESR = "ctor NotSupportedError")
    @HtmlUnitNYI(CHROME = "dispatched",
            EDGE = "dispatched")
    public void dispatchEventWithoutInit() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    try {\n"
            + "      var event = document.createEvent('PopStateEvent');\n"
            + "    } catch(e) { log('ctor ' + e.name); return; }\n"

            + "    try {\n"
            + "      dispatchEvent(event);\n"
            + "      log('dispatched');\n"
            + "    } catch(e) { logEx(e) }\n"
            + "  }\n"
            + DUMP_EVENT_FUNCTION
            + "  try {\n"
            + "    window.addEventListener('popstate',dump);\n"
            + "  } catch(e) { }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no initPopStateEvent",
            FF = "exception ctor",
            FF_ESR = "exception ctor")
    public void initPopStateEvent() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    try {\n"
            + "      var event = document.createEvent('PopStateEvent');\n"
            + "    } catch(e) { log('exception ctor'); return }\n"

            + "    if (event.initPopStateEvent) {\n"
            + "      event.initPopStateEvent('PopState', true, false, 'html');\n"
            + "      dump(event);\n"
            + "    } else { log('no initPopStateEvent'); }\n"
            + "  }\n"
            + DUMP_EVENT_FUNCTION
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }
}
