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
 * Tests for {@link WebkitSpeechRecognitionError}.
 *
 * @author Ronald Brill
 */
public class WebkitSpeechRecognitionErrorTest extends WebDriverTestCase {

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
    @Alerts(DEFAULT = "ReferenceError",
            CHROME = {"[object SpeechRecognitionErrorEvent]", "error", "false", "false", "false"},
            EDGE = {"[object SpeechRecognitionErrorEvent]", "error", "false", "false", "false"})
    @HtmlUnitNYI(
            CHROME = {"[object webkitSpeechRecognitionError]", "", "true", "true", "false"},
            EDGE = {"[object webkitSpeechRecognitionError]", "", "true", "true", "false"})
    public void create_ctor() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    try {\n"
            + "      var event = new webkitSpeechRecognitionError('error');\n"
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
    @Alerts(DEFAULT = "ReferenceError",
            CHROME = "TypeError",
            EDGE = "TypeError")
    @HtmlUnitNYI(
            CHROME = {"[object webkitSpeechRecognitionError]", "", "true", "true", "false"},
            EDGE = {"[object webkitSpeechRecognitionError]", "", "true", "true", "false"})
    public void create_ctorWithoutType() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    try {\n"
            + "      var event = new webkitSpeechRecognitionError();\n"
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
    @Alerts(DEFAULT = "ReferenceError",
            CHROME = {"[object SpeechRecognitionErrorEvent]", "42", "false", "false", "false"},
            EDGE = {"[object SpeechRecognitionErrorEvent]", "42", "false", "false", "false"})
    @HtmlUnitNYI(
            CHROME = {"[object webkitSpeechRecognitionError]", "", "true", "true", "false"},
            EDGE = {"[object webkitSpeechRecognitionError]", "", "true", "true", "false"})
    public void create_ctorNumericType() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    try {\n"
            + "      var event = new webkitSpeechRecognitionError(42);\n"
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
    @Alerts(DEFAULT = "ReferenceError",
            CHROME = {"[object SpeechRecognitionErrorEvent]", "null", "false", "false", "false"},
            EDGE = {"[object SpeechRecognitionErrorEvent]", "null", "false", "false", "false"})
    @HtmlUnitNYI(
            CHROME = {"[object webkitSpeechRecognitionError]", "", "true", "true", "false"},
            EDGE = {"[object webkitSpeechRecognitionError]", "", "true", "true", "false"})
    public void create_ctorNullType() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    try {\n"
            + "      var event = new webkitSpeechRecognitionError(null);\n"
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
            + "      var event = new webkitSpeechRecognitionError(unknown);\n"
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
    @Alerts(DEFAULT = "ReferenceError",
            CHROME = {"[object SpeechRecognitionErrorEvent]", "HtmlUnitEvent", "false", "false", "false"},
            EDGE = {"[object SpeechRecognitionErrorEvent]", "HtmlUnitEvent", "false", "false", "false"})
    @HtmlUnitNYI(
            CHROME = {"[object webkitSpeechRecognitionError]", "", "true", "true", "false"},
            EDGE = {"[object webkitSpeechRecognitionError]", "", "true", "true", "false"})
    public void create_ctorArbitraryType() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    try {\n"
            + "      var event = new webkitSpeechRecognitionError('HtmlUnitEvent');\n"
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
    @Alerts(DEFAULT = "ReferenceError",
            CHROME = {"[object SpeechRecognitionErrorEvent]", "error", "false", "false", "false"},
            EDGE = {"[object SpeechRecognitionErrorEvent]", "error", "false", "false", "false"})
    @HtmlUnitNYI(
            CHROME = {"[object webkitSpeechRecognitionError]", "", "true", "true", "false"},
            EDGE = {"[object webkitSpeechRecognitionError]", "", "true", "true", "false"})
    public void create_ctorAllDetails() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    try {\n"
            + "      var event = new webkitSpeechRecognitionError('error', {\n"
            + "        'error': 'no-speech',\n"
            + "        'message': 'No speech detected'\n"
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
    @Alerts(DEFAULT = "ReferenceError",
            CHROME = {"[object SpeechRecognitionErrorEvent]", "error", "false", "false", "false"},
            EDGE = {"[object SpeechRecognitionErrorEvent]", "error", "false", "false", "false"})
    @HtmlUnitNYI(
            CHROME = {"[object webkitSpeechRecognitionError]", "", "true", "true", "false"},
            EDGE = {"[object webkitSpeechRecognitionError]", "", "true", "true", "false"})
    public void create_ctorAllDetailsMissingData() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    try {\n"
            + "      var event = new webkitSpeechRecognitionError('error', {\n"
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
            + "      var event = document.createEvent('webkitSpeechRecognitionError');\n"
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
    @Alerts(DEFAULT = "false",
            CHROME = "true",
            EDGE = "true")
    public void inWindow() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    function test() {\n"
            + "      log('webkitSpeechRecognitionError' in window);\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "</body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }
}
