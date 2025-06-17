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
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link TextEvent}.
 *
 * @author Ronald Brill
 */
public class TextEventTest extends WebDriverTestCase {

    private static final String DUMP_EVENT_FUNCTION = "  function dump(event) {\n"
            + "    log(event);\n"
            + "    log(event.type);\n"
            + "    log(event.bubbles);\n"
            + "    log(event.cancelable);\n"
            + "    log(event.composed);\n"
            + "  }\n";

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "TypeError",
            FF_ESR = "ReferenceError")
    public void create_ctor() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    try {\n"
            + "      var event = new TextEvent('text');\n"
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
    @Alerts(DEFAULT = "TypeError",
            FF_ESR = "ReferenceError")
    public void create_ctorWithoutType() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    try {\n"
            + "      var event = new TextEvent();\n"
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
    @Alerts(DEFAULT = "TypeError",
            FF_ESR = "ReferenceError")
    public void create_ctorNumericType() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    try {\n"
            + "      var event = new TextEvent(42);\n"
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
    @Alerts(DEFAULT = "TypeError",
            FF_ESR = "ReferenceError")
    public void create_ctorNullType() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    try {\n"
            + "      var event = new TextEvent(null);\n"
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
            + "      var event = new TextEvent(unknown);\n"
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
    @Alerts(DEFAULT = "TypeError",
            FF_ESR = "ReferenceError")
    public void create_ctorArbitraryType() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    try {\n"
            + "      var event = new TextEvent('HtmlUnitEvent');\n"
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
    @Alerts(DEFAULT = "TypeError",
            FF_ESR = "ReferenceError")
    public void create_ctorAllDetails() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    try {\n"
            + "      var debug = {hello: 'world'};\n"
            + "      var event = new TextEvent('text', {\n"
            + "        'detail': 7\n"
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
    @Alerts(DEFAULT = "TypeError",
            FF_ESR = "ReferenceError")
    public void create_ctorAllDetailsMissingData() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    try {\n"
            + "      var event = new TextEvent('text', {\n"
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
    @Alerts(DEFAULT = "TypeError",
            FF_ESR = "ReferenceError")
    public void create_ctorAllDetailsWrongData() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    try {\n"
            + "      var event = new TextEvent('text', {\n"
            + "        'detail': 'ten'\n"
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
    @Alerts(DEFAULT = "true",
            FF_ESR = "false")
    public void inWindow() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    function test() {\n"
            + "      log('TextEvent' in window);\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "</body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }
}
