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
 * Tests for {@link ErrorEvent}.
 *
 * @author Ronald Brill
 */
public class ErrorEventTest extends WebDriverTestCase {

    private static final String DUMP_EVENT_FUNCTION = "  function dump(event) {\n"
            + "    log(event);\n"
            + "    log(event.type);\n"
            + "    log(event.bubbles);\n"
            + "    log(event.cancelable);\n"
            + "    log(event.composed);\n"

            // TODO all properties
            + "  }\n";

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[object ErrorEvent]", "error", "false", "false", "false"})
    public void create_ctor() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    try {\n"
            + "      var event = new ErrorEvent('error');\n"
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
    @HtmlUnitNYI(CHROME = {"[object ErrorEvent]", "undefined", "false", "false", "false"},
            EDGE = {"[object ErrorEvent]", "undefined", "false", "false", "false"},
            FF = {"[object ErrorEvent]", "undefined", "false", "false", "false"},
            FF_ESR = {"[object ErrorEvent]", "undefined", "false", "false", "false"})
    public void create_ctorWithoutType() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    try {\n"
            + "      var event = new ErrorEvent();\n"
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
    @Alerts({"[object ErrorEvent]", "42", "false", "false", "false"})
    public void create_ctorNumericType() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    try {\n"
            + "      var event = new ErrorEvent(42);\n"
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
    @Alerts({"[object ErrorEvent]", "null", "false", "false", "false"})
    public void create_ctorNullType() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    try {\n"
            + "      var event = new ErrorEvent(null);\n"
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
            + "      var event = new ErrorEvent(unknown);\n"
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
    @Alerts({"[object ErrorEvent]", "HtmlUnitEvent", "false", "false", "false"})
    public void create_ctorArbitraryType() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    try {\n"
            + "      var event = new ErrorEvent('HtmlUnitEvent');\n"
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
    @Alerts({"[object ErrorEvent]", "error", "false", "false", "false"})
    public void create_ctorAllDetails() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    try {\n"
            + "      var event = new ErrorEvent('error', {\n"
            // + "        'data': 'mozart'\n"
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
    @Alerts({"[object ErrorEvent]", "error", "false", "false", "false"})
    public void create_ctorAllDetailsMissingData() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    try {\n"
            + "      var event = new ErrorEvent('error', {\n"
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
    @Alerts({"[object ErrorEvent]", "error", "false", "false", "false"})
    public void create_ctorAllDetailsWrongData() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    try {\n"
            + "      var event = new ErrorEvent('error', {\n"
            + "        'data': ['Html', 'Unit']\n"
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
    @Alerts("true")
    public void inWindow() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    function test() {\n"
            + "      log('ErrorEvent' in window);\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "</body>\n"
            + "</html>";

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
            + "      var event = document.createEvent('ErrorEvent');\n"
            + "      dump(event);\n"
            + "    } catch(e) { logEx(e) }\n"
            + "  }\n"
            + DUMP_EVENT_FUNCTION
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * The properties of an ErrorEvent created with details.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"m",
                      "f",
                      "3",
                      "4",
                      "true"})
    public void ctorDetails() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  var err = new Error('e');\n"
            + "  var e = new ErrorEvent('error', { message: 'm', filename: 'f', lineno: 3, colno: 4, error: err });\n"
            + "  log(e.message); log(e.filename); log(e.lineno); log(e.colno); log(e.error === err);\n"
            + "</script></head><body>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * The properties of an ErrorEvent created without details.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"",
                      "",
                      "0",
                      "0",
                      "undefined"})
    public void ctorDefaults() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  var e = new ErrorEvent('error');\n"
            + "  log(e.message); log(e.filename); log(e.lineno); log(e.colno); log(e.error);\n"
            + "</script></head><body>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * An uncaught script error fires an ErrorEvent at the window.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"error true true",
                                "false true true",
                                "boom number string"},
            FF = {"error true true",
                  "true true true",
                  "boom number string"},
            FF_ESR = {"error true true",
                      "true true true",
                      "boom number string"})
    public void scriptErrorListener() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  window.addEventListener('error', function(e) {\n"
            + "    log(e.type + ' ' + (e instanceof ErrorEvent) + ' ' + e.cancelable);\n"
            + "    log(e.bubbles + ' ' + e.isTrusted + ' ' + (e.target === window));\n"
            + "    log(e.error.message + ' ' + (typeof e.lineno) + ' ' + (typeof e.filename));\n"
            + "    e.preventDefault();\n"
            + "  });\n"
            + "</script></head><body>\n"
            + "<script>throw new Error('boom');</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * The onerror handler of the window gets message, source, lineno, colno and error.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"string string number number boom",
                      "5"})
    public void scriptErrorOnerrorArgs() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  window.onerror = function(msg, src, line, col, err) {\n"
            + "    log(typeof msg + ' ' + typeof src + ' ' + typeof line + ' ' + typeof col + ' ' + err.message);\n"
            + "    log(arguments.length);\n"
            + "    return true;\n"
            + "  };\n"
            + "</script></head><body>\n"
            + "<script>throw new Error('boom');</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * The onerror handler is called in its registration order between the listeners.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"listener1",
                      "onerror",
                      "listener2 true"})
    public void scriptErrorOrder() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  window.addEventListener('error', function(e) { log('listener1'); });\n"
            + "  window.onerror = function() { log('onerror'); return true; };\n"
            + "  window.addEventListener('error', function(e) { log('listener2 ' + e.defaultPrevented); });\n"
            + "</script></head><body>\n"
            + "<script>throw new Error('boom');</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Returning false from onerror does not cancel the error event.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void scriptErrorOnerrorReturnFalse() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  window.onerror = function() { return false; };\n"
            + "  window.addEventListener('error', function(e) { log(e.defaultPrevented); e.preventDefault(); });\n"
            + "</script></head><body>\n"
            + "<script>throw new Error('boom');</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * An error thrown by the onerror handler is not reported to it again.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"onerror 1",
                      "done 1"})
    public void scriptErrorInOnerror() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  var count = 0;\n"
            + "  window.onerror = function() { count++; log('onerror ' + count); throw new Error('again'); };\n"
            + "</script></head><body>\n"
            + "<script>throw new Error('boom');</script>\n"
            + "<script>log('done ' + count);</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }
}
