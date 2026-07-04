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

/**
 * Tests for {@link PointerEvent}.
 *
 * @author Frank Danek
 * @author Ronald Brill
 */
public class PointerEventTest extends WebDriverTestCase {

    private static final String DUMP_EVENT_FUNCTION = "  function dump(event) {\n"
        + "    if (event) {\n"
        + "      log(event);\n"
        + "      log(event.type);\n"
        + "      log(event.bubbles);\n"
        + "      log(event.cancelable);\n"
        + "      log(event.composed);\n"

        + "      log(event.pointerId);\n"
        + "      log(event.width);\n"
        + "      log(event.height);\n"
        + "      log(event.pressure);\n"
        + "      log(event.tiltX);\n"
        + "      log(event.tiltY);\n"
        + "      log(event.pointerType);\n"
        + "      log(event.isPrimary);\n"
        + "      log(event.altitudeAngle);\n"
        + "      log(event.azimuthAngle);\n"
        + "      log(event.persistentDeviceId);\n"
        + "    } else {\n"
        + "      log('no event');\n"
        + "    }\n"
        + "  }\n";

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"[object PointerEvent]", "click", "false", "false", "false",
                       "0", "1", "1", "0", "0", "0", "", "false", "1.5707963267948966", "0", "0"},
            FF_ESR = {"[object PointerEvent]", "click", "false", "false", "false",
                      "0", "1", "1", "0", "0", "0", "", "false", "1.5707963267948966", "0", "undefined"})
    public void create_ctor() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    try {\n"
            + "      var event = new PointerEvent('click');\n"
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
    @Alerts(DEFAULT = {"[object PointerEvent]", "click", "true", "false", "false",
                       "2", "1", "1", "0", "0", "0", "mouse", "false", "1.5707963267948966", "0", "0"},
            FF_ESR = {"[object PointerEvent]", "click", "true", "false", "false",
                      "2", "1", "1", "0", "0", "0", "mouse", "false", "1.5707963267948966", "0", "undefined"})
    public void create_ctorWithDetails() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    try {\n"
            + "      var event = new PointerEvent('click', {\n"
            + "        'bubbles': true,\n"
            + "        'pointerId': 2,\n"
            + "        'pointerType': 'mouse'\n"
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
    @HtmlUnitNYI(CHROME = {"[object PointerEvent]", "", "false", "false", "false",
            "0", "0", "0", "0", "0", "0", "", "false", "1.5707963267948966", "0", "0"},
            EDGE = {"[object PointerEvent]", "", "false", "false", "false",
                "0", "0", "0", "0", "0", "0", "", "false", "1.5707963267948966", "0", "0"},
            FF = {"[object PointerEvent]", "", "false", "false", "false",
                "0", "0", "0", "0", "0", "0", "", "false", "1.5707963267948966", "0", "0"},
            FF_ESR = {"[object PointerEvent]", "", "false", "false", "false",
                "0", "0", "0", "0", "0", "0", "", "false", "1.5707963267948966", "0", "undefined"})
    public void create_ctorWithoutType() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    try {\n"
            + "      var event = new PointerEvent();\n"
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
    @Alerts(DEFAULT = {"[object PointerEvent]", "42", "false", "false", "false",
                       "0", "1", "1", "0", "0", "0", "", "false", "1.5707963267948966", "0", "0"},
            FF_ESR = {"[object PointerEvent]", "42", "false", "false", "false",
                      "0", "1", "1", "0", "0", "0", "", "false", "1.5707963267948966", "0", "undefined"})
    public void create_ctorNumericType() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    try {\n"
            + "      var event = new PointerEvent(42);\n"
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
    @Alerts(DEFAULT = {"[object PointerEvent]", "null", "false", "false", "false",
                       "0", "1", "1", "0", "0", "0", "", "false", "1.5707963267948966", "0", "0"},
            FF_ESR = {"[object PointerEvent]", "null", "false", "false", "false",
                      "0", "1", "1", "0", "0", "0", "", "false", "1.5707963267948966", "0", "undefined"})
    public void create_ctorNullType() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    try {\n"
            + "      var event = new PointerEvent(null);\n"
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
            + "      var event = new PointerEvent(unknown);\n"
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
    @Alerts(DEFAULT = {"[object PointerEvent]", "HtmlUnitEvent", "false", "false", "false",
                       "0", "1", "1", "0", "0", "0", "", "false", "1.5707963267948966", "0", "0"},
            FF_ESR = {"[object PointerEvent]", "HtmlUnitEvent", "false", "false", "false",
                      "0", "1", "1", "0", "0", "0", "", "false", "1.5707963267948966", "0", "undefined"})
    public void create_ctorArbitraryType() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    try {\n"
            + "      var event = new PointerEvent('HtmlUnitEvent');\n"
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
    @Alerts(DEFAULT = {"[object PointerEvent]", "click", "true", "false", "false",
                       "2", "3", "4", "5.5", "6", "7", "touch",
                       "true", "1.4105561004354874", "0.8628261898537035", "0"},
            FF_ESR = {"[object PointerEvent]", "click", "true", "false", "false",
                      "2", "3", "4", "5.5", "6", "7", "touch",
                      "true", "1.4105561004354874", "0.8628261898537035", "undefined"})
    public void create_ctorAllDetails() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    try {\n"
            + "      var event = new PointerEvent('click', {\n"
            + "        'bubbles': true,\n"
            + "        'pointerId': 2,\n"
            + "        'width': 3,\n"
            + "        'height': 4,\n"
            + "        'pressure': 5.5,\n"
            + "        'tiltX': 6,\n"
            + "        'tiltY': 7,\n"
            + "        'pointerType': 'touch',\n"
            + "        'isPrimary': true\n"
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
    @Alerts(DEFAULT = {"[object PointerEvent]", "click", "false", "false", "false",
                       "0", "1", "1", "0", "0", "0", "", "false", "1.5707963267948966", "0", "0"},
            FF_ESR = {"[object PointerEvent]", "click", "false", "false", "false",
                      "0", "1", "1", "0", "0", "0", "", "false", "1.5707963267948966", "0", "undefined"})
    public void create_ctorAllDetailsMissingData() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    try {\n"
            + "      var event = new PointerEvent('click', {\n"
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
            + "      var event = document.createEvent('PointerEvent');\n"
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
    public void initPointerEvent() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    try {\n"
            + "      var event = document.createEvent('PointerEvent');\n"
            + "      event.initPointerEvent('click', true, false, window, 3, 10, 11, 12, 13, true, true, true, false, "
            + "0, null, 14, 15, 4, 5, 6, 16, 17, 18, 123, 'mouse', 987, false);\n"
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
    public void dispatchEvent() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    try {\n"
            + "      var event = document.createEvent('PointerEvent');\n"
            + "      event.initPointerEvent('click', true, false, window, 3, 10, 11, 12, 13, true, true, true, false, "
            + "0, null, 14, 15, 4, 5, 6, 16, 17, 18, 123, 'mouse', 987, false);\n"
            + "      dispatchEvent(event);\n"
            + "    } catch(e) { logEx(e) }\n"
            + "  }\n"
            + DUMP_EVENT_FUNCTION
            + "  try {\n"
            + "    window.addEventListener('click',dump);\n"
            + "  } catch(e) { }\n"
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
            + "      log('PointerEvent' in window);\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "</body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }
}
