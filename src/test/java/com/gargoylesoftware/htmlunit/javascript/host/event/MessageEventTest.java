/*
 * Copyright (c) 2002-2016 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript.host.event;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.html.HtmlPageTest;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * Tests for {@link MessageEvent}.
 *
 * @author Ahmed Ashour
 * @author Frank Danek
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class MessageEventTest extends WebDriverTestCase {

    private static final String DUMP_EVENT_FUNCTION = "  function dump(event) {\n"
        + "    if (event) {\n"
        + "      alert(event);\n"
        + "      alert(event.type);\n"
        + "      alert(event.bubbles);\n"
        + "      alert(event.cancelable);\n"
        + "      alert(event.data);\n"
        + "      alert(event.origin);\n"
        + "      alert(event.lastEventId);\n"
        + "      alert(event.source);\n"
        + "    } else {\n"
        + "      alert('no event');\n"
        + "    }\n"
        + "  }\n";

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"[object MessageEvent]", "type-message, false", "false", "null", "", "", "null" },
            FF = {"[object MessageEvent]", "type-message, false", "false", "undefined", "", "", "null" },
            IE = "exception")
    public void create_ctor() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    try {\n"
            + "      var event = new MessageEvent('type-message');\n"
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
    @Alerts(DEFAULT = {"[object MessageEvent]", "type-message", "false", "false",
                            "test-data", "test-origin", "42", "[object Window]" },
            IE = "exception")
    public void create_ctorWithDetails() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    try {\n"
            + "      var event = new MessageEvent('type-message', {\n"
            + "        'data': 'test-data',\n"
            + "        'origin': 'test-origin',\n"
            + "        'lastEventId': 42,\n"
            + "        'source': window\n"
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
    @Alerts({ "DOM2: exception", "DOM3: [object MessageEvent]" })
    public void createEvent() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    try {\n"
            + "      alert('DOM2: ' + document.createEvent('MessageEvents'));\n"
            + "    } catch(e) {alert('DOM2: exception')}\n"
            + "    try {\n"
            + "      alert('DOM3: ' + document.createEvent('MessageEvent'));\n"
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
    @Alerts(DEFAULT = { "[object MessageEvent]", "message", "true", "true", "hello",
                            "http://localhost:", "2", "[object Window]" },
            FF38 = "no initMessageEvent",
            IE = { "[object MessageEvent]", "message", "true", "true", "hello",
                            "http://localhost:", "undefined", "[object Window]" })
    public void initMessageEvent() throws Exception {
        final String[] expectedAlerts = getExpectedAlerts();
        if (expectedAlerts.length > 4) {
            expectedAlerts[5] += PORT;
            setExpectedAlerts(expectedAlerts);
        }
        final String origin = "http://localhost:" + PORT;
        final String html = "<html><body><script>\n"
            + "try {\n"
            + "  if (document.createEvent) {\n"
            + "    var e = document.createEvent('MessageEvent');\n"
            + "    if (e.initMessageEvent) {\n"
            + "      e.initMessageEvent('message', true, true, 'hello', '" + origin + "', 2, window, null);\n"
            + "      dump(e);\n"
            + "    } else {\n"
            + "      alert('no initMessageEvent');"
            + "    }\n"
            + "  } else {\n"
            + "    alert('no createEvent');"
            + "  }\n"
            + "} catch(e) { alert(e) }\n"
            + DUMP_EVENT_FUNCTION
            + "</script></body></html>";

        loadPageWithAlerts2(html);
    }

}
