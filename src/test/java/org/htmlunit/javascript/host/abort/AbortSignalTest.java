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
package org.htmlunit.javascript.host.abort;

import org.htmlunit.WebDriverTestCase;
import org.htmlunit.junit.annotation.Alerts;
import org.htmlunit.junit.annotation.HtmlUnitNYI;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link AbortController} and {@link AbortSignal}.
 *
 * @author Ronald Brill
 */
public class AbortSignalTest extends WebDriverTestCase {

    /**
     * A new signal is not aborted.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"false", "undefined", "true", "true", "[object AbortSignal]"})
    public void initialState() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  const c = new AbortController();\n"
            + "  log(c.signal.aborted);\n"
            + "  log(c.signal.reason);\n"
            + "  log(c.signal === c.signal);\n"
            + "  log(c.signal instanceof EventTarget);\n"
            + "  log(Object.prototype.toString.call(c.signal));\n"
            + "</script></head><body></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Method abort() without a reason: the reason is an AbortError.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"true", "true", "AbortError", "20", "signal is aborted without reason"},
            FF = {"true", "true", "AbortError", "20", "The operation was aborted. "},
            FF_ESR = {"true", "true", "AbortError", "20", "The operation was aborted. "})
    @HtmlUnitNYI(FF = {"true", "true", "AbortError", "20", "signal is aborted without reason"},
            FF_ESR = {"true", "true", "AbortError", "20", "signal is aborted without reason"})
    public void abortWithoutReason() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  const c = new AbortController();\n"
            + "  c.abort();\n"
            + "  log(c.signal.aborted);\n"
            + "  const r = c.signal.reason;\n"
            + "  log(r instanceof DOMException);\n"
            + "  log(r.name);\n"
            + "  log(r.code);\n"
            + "  log(r.message);\n"
            + "</script></head><body></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Method abort() with a reason.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"true", "my reason"})
    public void abortWithReason() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  const c = new AbortController();\n"
            + "  c.abort('my reason');\n"
            + "  log(c.signal.aborted);\n"
            + "  log(c.signal.reason);\n"
            + "</script></head><body></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Method abort(undefined) is like abort().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("AbortError")
    public void abortWithUndefinedReason() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  const c = new AbortController();\n"
            + "  c.abort(undefined);\n"
            + "  log(c.signal.reason.name);\n"
            + "</script></head><body></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Method abort(null): null is a reason.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("null")
    public void abortWithNullReason() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  const c = new AbortController();\n"
            + "  c.abort(null);\n"
            + "  log(c.signal.reason);\n"
            + "</script></head><body></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * The second abort() changes nothing and fires no event.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"event", "first"})
    public void abortTwice() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  const c = new AbortController();\n"
            + "  c.signal.addEventListener('abort', () => log('event'));\n"
            + "  c.abort('first');\n"
            + "  c.abort('second');\n"
            + "  log(c.signal.reason);\n"
            + "</script></head><body></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * The abort event: handler and listeners.
     * <p>HtmlUnit does not support Event.isTrusted.</p>
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"function", "onabort abort true true", "false false true", "listener true", "done"})
    @HtmlUnitNYI(CHROME = {"function", "onabort abort true undefined", "false false true", "listener true", "done"},
            EDGE = {"function", "onabort abort true undefined", "false false true", "listener true", "done"},
            FF = {"function", "onabort abort true undefined", "false false true", "listener true", "done"},
            FF_ESR = {"function", "onabort abort true undefined", "false false true", "listener true", "done"})
    public void abortEvent() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  const c = new AbortController();\n"
            + "  c.signal.onabort = function(e) {\n"
            + "    log('onabort ' + e.type + ' ' + (e.target === c.signal) + ' ' + e.isTrusted);\n"
            + "    log(e.bubbles + ' ' + e.cancelable + ' ' + c.signal.aborted);\n"
            + "  };\n"
            + "  c.signal.addEventListener('abort', e => log('listener ' + (this === window)));\n"
            + "  log(typeof c.signal.onabort);\n"
            + "  c.abort();\n"
            + "  log('done');\n"
            + "</script></head><body></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Method throwIfAborted() throws the reason.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"not aborted", "my reason", "AbortError"})
    public void throwIfAborted() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  const c = new AbortController();\n"
            + "  c.signal.throwIfAborted();\n"
            + "  log('not aborted');\n"
            + "  c.abort('my reason');\n"
            + "  try {\n"
            + "    c.signal.throwIfAborted();\n"
            + "    log('no exception');\n"
            + "  } catch (e) { log(e); }\n"
            + "  const c2 = new AbortController();\n"
            + "  c2.abort();\n"
            + "  try {\n"
            + "    c2.signal.throwIfAborted();\n"
            + "  } catch (e) { log(e.name); }\n"
            + "</script></head><body></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * AbortSignal.abort() returns an aborted signal.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"true", "AbortError", "true", "r"})
    public void staticAbort() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  const s = AbortSignal.abort();\n"
            + "  log(s.aborted);\n"
            + "  log(s.reason.name);\n"
            + "  log(s instanceof AbortSignal);\n"
            + "  log(AbortSignal.abort('r').reason);\n"
            + "</script></head><body></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * AbortSignal can not be created by script.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("TypeError")
    public void constructor() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  try {\n"
            + "    new AbortSignal();\n"
            + "    log('no exception');\n"
            + "  } catch (e) { log(e.name); }\n"
            + "</script></head><body></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * AbortSignal.timeout() aborts with a TimeoutError.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"false", "true", "TimeoutError", "23", "true"})
    public void timeout() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  const s = AbortSignal.timeout(10);\n"
            + "  log(s.aborted);\n"
            + "  s.onabort = () => {\n"
            + "    log(s.aborted);\n"
            + "    log(s.reason.name);\n"
            + "    log(s.reason.code);\n"
            + "    log(s.reason instanceof DOMException);\n"
            + "  };\n"
            + "</script></head><body></body></html>";

        loadPage2(html);
        verifyTitle2(DEFAULT_WAIT_TIME, getWebDriver(), getExpectedAlerts());
    }

    /**
     * AbortSignal.timeout() with invalid values.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"TypeError", "TypeError", "TypeError"})
    public void timeoutInvalid() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  try {\n"
            + "    AbortSignal.timeout(-1);\n"
            + "    log('no exception');\n"
            + "  } catch (e) { log(e.name); }\n"
            + "  try {\n"
            + "    AbortSignal.timeout();\n"
            + "    log('no exception');\n"
            + "  } catch (e) { log(e.name); }\n"
            + "  try {\n"
            + "    AbortSignal.timeout('abc');\n"
            + "    log('no exception');\n"
            + "  } catch (e) { log(e.name); }\n"
            + "</script></head><body></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * AbortSignal.any() is aborted if one of the signals is aborted.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"false", "false", "any aborted r2", "true", "r2", "r2"})
    public void any() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  const c1 = new AbortController();\n"
            + "  const c2 = new AbortController();\n"
            + "  const s = AbortSignal.any([c1.signal, c2.signal]);\n"
            + "  s.onabort = () => log('any aborted ' + s.reason);\n"
            + "  log(s.aborted);\n"
            + "  log(s === c1.signal);\n"
            + "  c2.abort('r2');\n"
            + "  log(s.aborted);\n"
            + "  log(s.reason);\n"
            + "  c1.abort('r1');\n"
            + "  log(s.reason);\n"
            + "</script></head><body></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * AbortSignal.any() with an aborted signal.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"true", "r1"})
    public void anyAlreadyAborted() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  const c1 = new AbortController();\n"
            + "  c1.abort('r1');\n"
            + "  const s = AbortSignal.any([new AbortController().signal, c1.signal]);\n"
            + "  log(s.aborted);\n"
            + "  log(s.reason);\n"
            + "</script></head><body></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * AbortSignal.any() without signals.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void anyEmpty() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  const s = AbortSignal.any([]);\n"
            + "  log(s.aborted);\n"
            + "</script></head><body></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * AbortSignal.any() with invalid arguments.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"TypeError", "TypeError", "TypeError"})
    public void anyInvalid() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  try {\n"
            + "    AbortSignal.any();\n"
            + "    log('no exception');\n"
            + "  } catch (e) { log(e.name); }\n"
            + "  try {\n"
            + "    AbortSignal.any(['x']);\n"
            + "    log('no exception');\n"
            + "  } catch (e) { log(e.name); }\n"
            + "  try {\n"
            + "    AbortSignal.any(7);\n"
            + "    log('no exception');\n"
            + "  } catch (e) { log(e.name); }\n"
            + "</script></head><body></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * The order of the events of the signal and the signals created by any().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"signal", "any"})
    public void eventOrderDependent() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  const c = new AbortController();\n"
            + "  const s = AbortSignal.any([c.signal]);\n"
            + "  s.onabort = () => log('any');\n"
            + "  c.signal.onabort = () => log('signal');\n"
            + "  c.abort();\n"
            + "</script></head><body></body></html>";

        loadPageVerifyTitle2(html);
    }
}
