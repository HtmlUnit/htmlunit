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
package com.gargoylesoftware.htmlunit.javascript;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * Tests for NativeError.
 *
 * @author Ahmed Ashour
 * @author Marc Guillemot
 * @author Frank Danek
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class NativeErrorTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"string", "true"})
    public void stack() throws Exception {
        final String html
            = "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  try {\n"
            + "    null.method();\n"
            + "  } catch (e) {\n"
            + "    if (e.stack) {\n"
            + "      var s = e.stack;\n"
            + "      log(typeof s);\n"
            + "      log(s.length > 0);\n"
            + "    }\n"
            + "    else\n"
            + "      log('undefined');\n"
            + "  }\n"
            + "}\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"string", "true"})
    public void stackNewError() throws Exception {
        final String html
            = "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  try {\n"
            + "    throw new Error();\n"
            + "  } catch (e) {\n"
            + "    if (e.stack) {\n"
            + "      var s = e.stack;\n"
            + "      log(typeof s);\n"
            + "      log(s.length > 0);\n"
            + "    }\n"
            + "    else\n"
            + "      log('undefined');\n"
            + "  }\n"
            + "}\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"string", "true"},
            IE = "undefined")
    public void stackNewErrorWithoutThrow() throws Exception {
        final String html
            = "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  var e = new Error();\n"
            + "  if (e.stack) {\n"
            + "    var s = e.stack;\n"
            + "    log(typeof s);\n"
            + "    log(s.length > 0);\n"
            + "  }\n"
            + "  else\n"
            + "    log('undefined');\n"
            + "}\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void stackInNewError() throws Exception {
        final String html
            = "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  var e = new Error();\n"
            + "  log('stack' in e);\n"
            + "}\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "method (url)",
            FF = "method@url",
            FF78 = "method@url")
    @NotYetImplemented
    public void stackContent() throws Exception {
        final String html
            = "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  try {\n"
            + "    null.method();\n"
            + "  } catch (e) {\n"
            + "    if (e.stack) {\n"
            + "      var s = e.stack;\n"
            + "      if (s.indexOf('test()@') != -1) {\n"
            + "        log('method()@url');\n"
            + "      } else if (s.indexOf('test@') != -1) {\n"
            + "        log('method@url');\n"
            + "      } else if (s.indexOf('test (') != -1) {\n"
            + "        log('method (url)');\n"
            + "      } else if (s.indexOf('test() (') != -1) {\n"
            + "        log('method() (url)');\n"
            + "      }\n"
            + "    }\n"
            + "    else\n"
            + "      log('undefined');\n"
            + "  }\n"
            + "}\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "method (url)",
            FF = "method@url",
            FF78 = "method@url")
    @NotYetImplemented
    public void stackContentNewError() throws Exception {
        final String html
            = "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  try {\n"
            + "    throw new Error();\n"
            + "  } catch (e) {\n"
            + "    if (e.stack) {\n"
            + "      var s = e.stack;\n"
            + "      if (s.indexOf('test()@') != -1) {\n"
            + "        log('method()@url');\n"
            + "      } else if (s.indexOf('test@') != -1) {\n"
            + "        log('method@url');\n"
            + "      } else if (s.indexOf('test (') != -1) {\n"
            + "        log('method (url)');\n"
            + "      } else if (s.indexOf('test() (') != -1) {\n"
            + "        log('method() (url)');\n"
            + "      }\n"
            + "    }\n"
            + "    else\n"
            + "      log('undefined');\n"
            + "  }\n"
            + "}\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"true", "kcats"})
    public void stackOverwrite() throws Exception {
        final String html
            = "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  try {\n"
            + "    null.method();\n"
            + "  } catch (e) {\n"
            + "    if (e.stack) {\n"
            + "      var s = e.stack;\n"
            + "      log(s.length > 10);\n"

            + "      e.stack = 'kcats';\n"
            + "      var s = e.stack;\n"
            + "      log(s);\n"
            + "    }\n"
            + "    else\n"
            + "      log('undefined');\n"
            + "  }\n"
            + "}\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "10",
            FF = "undefined",
            FF78 = "undefined")
    public void stackTraceLimit() throws Exception {
        final String html
            = "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  log(Error.stackTraceLimit);\n"
            + "}\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "undefined",
            CHROME = "function captureStackTrace() { [native code] }",
            EDGE = "function captureStackTrace() { [native code] }")
    public void captureStackTrace() throws Exception {
        final String html
            = "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  log(Error.captureStackTrace);\n"
            + "}\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }
}
