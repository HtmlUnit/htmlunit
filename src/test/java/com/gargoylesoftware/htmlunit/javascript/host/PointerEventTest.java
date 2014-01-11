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

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.html.HtmlPageTest;

/**
 * Tests for {@link PointerEvent}.
 *
 * @version $Revision$
 * @author Frank Danek
 */
@RunWith(BrowserRunner.class)
public class PointerEventTest extends WebDriverTestCase {

    private static final String DUMP_EVENT_FUNCTION = "  function dump(event) {\n"
        + "    if (event) {\n"
        + "      alert(event);\n"
        + "      alert(event.type);\n"
        + "      alert(event.bubbles);\n"
        + "      alert(event.cancelable);\n"
        + "      alert(event.pointerId);\n"
        + "      alert(event.width);\n"
        + "      alert(event.height);\n"
        + "      alert(event.pressure);\n"
        + "      alert(event.tiltX);\n"
        + "      alert(event.tiltY);\n"
        + "      alert(event.pointerType);\n"
        + "      alert(event.isPrimary);\n"
        + "    } else {\n"
        + "      alert('no event');\n"
        + "    }\n"
        + "  }\n";

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void create_ctor() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    try {\n"
            + "      var event = new PointerEvent('click');\n"
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
    public void create_ctorWithDetails() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    try {\n"
            + "      var event = new PointerEvent('click', {\n"
            + "        'bubbles': true,\n"
            + "        'pointerId': 2,\n"
            + "        'pointerType': 'mouse'\n"
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
    @Alerts(DEFAULT = "exception",
            IE11 = { "[object PointerEvent]", "", "false", "false", "0", "0", "0", "0", "0", "0", "", "false" })
    public void create_createEvent() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    try {\n"
            + "      var event = document.createEvent('PointerEvent');\n"
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
    @Alerts(DEFAULT = "exception",
            IE11 = { "[object PointerEvent]", "click", "true", "false", "123", "4", "5", "6", "17", "18",
                "mouse", "false" })
    public void initPointerEvent() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    try {\n"
            + "      var event = document.createEvent('PointerEvent');\n"
            + "      event.initPointerEvent('click', true, false, window, 3, 10, 11, 12, 13, true, true, true, false, "
            + "0, null, 14, 15, 4, 5, 6, 16, 17, 18, 123, 'mouse', 987, false);\n"
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
    @Alerts(DEFAULT = "exception",
            IE11 = { "[object PointerEvent]", "click", "true", "false", "123", "4", "5", "6", "17", "18",
                "mouse", "false" })
    public void dispatchEvent() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    try {\n"
            + "      var event = document.createEvent('PointerEvent');\n"
            + "      event.initPointerEvent('click', true, false, window, 3, 10, 11, 12, 13, true, true, true, false, "
            + "0, null, 14, 15, 4, 5, 6, 16, 17, 18, 123, 'mouse', 987, false);\n"
            + "      dispatchEvent(event);\n"
            + "    } catch (e) { alert('exception') }\n"
            + "  }\n"
            + DUMP_EVENT_FUNCTION
            + "  try {\n"
            + "    window.addEventListener('click',dump);\n"
            + "  } catch (e) { }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }
}
