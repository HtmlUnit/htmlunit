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
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link MessageEvent}.
 *
 * @author Ahmed Ashour
 * @author Frank Danek
 * @author Ronald Brill
 */
public class MessageEventTest extends WebDriverTestCase {

    private static final String DUMP_EVENT_FUNCTION = "  function dump(event) {\n"
            + "    if (event) {\n"
            + "      log(event);\n"
            + "      log(event.type);\n"
            + "      log(event.bubbles);\n"
            + "      log(event.cancelable);\n"
            + "      log(event.composed);\n"
            + "      log(event.data);\n"
            + "      log(event.origin);\n"
            + "      log(event.lastEventId);\n"
            + "      log(event.source);\n"
            + "    } else {\n"
            + "      log('no event');\n"
            + "    }\n"
            + "  }\n";

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[object MessageEvent]", "type-message", "false", "false", "false",
             "null", "", "", "null"})
    public void create_ctor() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    try {\n"
            + "      var event = new MessageEvent('type-message');\n"
            + "      dump(event);\n"
            + "    } catch(e) { logEx(e); }\n"
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
    @Alerts({"[object MessageEvent]", "type-message", "false", "false", "false",
             "test-data", "test-origin", "42", "[object Window]"})
    public void create_ctorWithDetails() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    try {\n"
            + "      var event = new MessageEvent('type-message', {\n"
            + "        'data': 'test-data',\n"
            + "        'origin': 'test-origin',\n"
            + "        'lastEventId': 42,\n"
            + "        'source': window\n"
            + "      });\n"
            + "      dump(event);\n"
            + "    } catch(e) { logEx(e); }\n"
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
    @Alerts({"DOM2: exception", "DOM3: [object MessageEvent]"})
    public void createEvent() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    try {\n"
            + "      log('DOM2: ' + document.createEvent('MessageEvents'));\n"
            + "    } catch(e) {log('DOM2: exception')}\n"
            + "    try {\n"
            + "      log('DOM3: ' + document.createEvent('MessageEvent'));\n"
            + "    } catch(e) {log('DOM3: exception')}\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("TypeError")
    public void initMessageEventPortsNull() throws Exception {
        final String[] expectedAlerts = getExpectedAlerts();
        if (expectedAlerts.length > 4) {
            expectedAlerts[6] += PORT;
            setExpectedAlerts(expectedAlerts);
        }

        final String origin = "http://localhost:" + PORT;
        final String html = DOCTYPE_HTML
            + "<html><body><script>\n"
            + LOG_TITLE_FUNCTION
            + "var e = document.createEvent('MessageEvent');\n"
            + "if (e.initMessageEvent) {\n"
            + "  try {\n"
            + "    e.initMessageEvent('message', true, true, 'hello', '" + origin + "', 2, window, null);\n"
            + "    dump(e);\n"
            + "  } catch(e) { logEx(e); }\n"
            + "} else {\n"
            + "  log('no initMessageEvent');\n"
            + "}\n"
            + DUMP_EVENT_FUNCTION
            + "</script></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"[object MessageEvent]", "message", "true", "true", "false", "hello",
             "http://localhost:", "2", "[object Window]"})
    public void initMessageEventPortsUndefined() throws Exception {
        final String[] expectedAlerts = getExpectedAlerts();
        if (expectedAlerts.length > 4) {
            expectedAlerts[6] += PORT;
            setExpectedAlerts(expectedAlerts);
        }

        final String origin = "http://localhost:" + PORT;
        final String html = DOCTYPE_HTML
            + "<html><body><script>\n"
            + LOG_TITLE_FUNCTION
            + "var e = document.createEvent('MessageEvent');\n"
            + "if (e.initMessageEvent) {\n"
            + "  try {\n"
            + "    e.initMessageEvent('message', true, true, 'hello', '" + origin + "', 2, window, undefined);\n"
            + "    dump(e);\n"
            + "  } catch(e) { logEx(e); }\n"
            + "} else {\n"
            + "  log('no initMessageEvent');\n"
            + "}\n"
            + DUMP_EVENT_FUNCTION
            + "</script></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"[object MessageEvent]", "message", "true", "true", "false", "hello",
             "http://localhost:", "2", "[object Window]"})
    public void initMessageEvent() throws Exception {
        final String[] expectedAlerts = getExpectedAlerts();
        expectedAlerts[6] += PORT;
        setExpectedAlerts(expectedAlerts);

        final String origin = "http://localhost:" + PORT;
        final String html = DOCTYPE_HTML
            + "<html><body><script>\n"
            + LOG_TITLE_FUNCTION
            + "var e = document.createEvent('MessageEvent');\n"
            + "if (e.initMessageEvent) {\n"
            + "  try {\n"
            + "    e.initMessageEvent('message', true, true, 'hello', '" + origin + "', 2, window, []);\n"
            + "    dump(e);\n"
            + "  } catch(e) { logEx(e); }\n"
            + "} else {\n"
            + "  log('no initMessageEvent');\n"
            + "}\n"
            + DUMP_EVENT_FUNCTION
            + "</script></body></html>";

        loadPageVerifyTitle2(html);
    }
}
