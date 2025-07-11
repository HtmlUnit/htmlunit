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
package org.htmlunit.javascript;

import org.htmlunit.WebDriverTestCase;
import org.htmlunit.junit.annotation.Alerts;
import org.htmlunit.junit.annotation.HtmlUnitNYI;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;

/**
 * Tests for NativeError.
 *
 * @author Ahmed Ashour
 * @author Marc Guillemot
 * @author Frank Danek
 * @author Ronald Brill
 */
public class NativeErrorTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"string", "true"})
    public void stack() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  try {\n"
            + "    null.method();\n"
            + "  } catch(e) {\n"
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
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  try {\n"
            + "    throw new Error();\n"
            + "  } catch(e) {\n"
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
    public void stackNewErrorWithoutThrow() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
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
    @Alerts("true")
    public void stackInNewError() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
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
            FF_ESR = "method@url")
    @HtmlUnitNYI(CHROME = "method()@url",
            EDGE = "method()@url",
            FF = "method()@url",
            FF_ESR = "method()@url")
    public void stackContent() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  try {\n"
            + "    null.method();\n"
            + "  } catch(e) {\n"
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
            FF_ESR = "method@url")
    @HtmlUnitNYI(CHROME = "method()@url",
            EDGE = "method()@url",
            FF = "method()@url",
            FF_ESR = "method()@url")
    public void stackContentNewError() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  try {\n"
            + "    throw new Error();\n"
            + "  } catch(e) {\n"
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
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  try {\n"
            + "    null.method();\n"
            + "  } catch(e) {\n"
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
            FF_ESR = "undefined")
    public void stackTraceLimit() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
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
    @Alerts(DEFAULT = "function captureStackTrace() { [native code] }",
            FF_ESR = "undefined")
    public void captureStackTrace() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  log(Error.captureStackTrace);\n"
            + "}\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void stackLineSeparator() throws Exception {
        final String lineSeparator = System.getProperty("line.separator");
        try {
            System.setProperty("line.separator", "\r\n");

            final String html = DOCTYPE_HTML
                + "<html><head><script>\n"
                + LOG_TITLE_FUNCTION
                + "function test() {\n"
                + "  try {\n"
                + "    throw new Error();\n"
                + "  } catch(e) {\n"
                + "    log(e.stack.replace('\\r', '\\\\r').replace('\\n', '\\\\n'));\n"
                + "  }"
                + "}\n"
                + "</script></head><body onload='test()'>\n"
                + "</body></html>";

            final WebDriver driver = loadPage2(html);
            final String title = driver.getTitle();
            assertTrue(title, title.contains("\\n"));
            assertFalse(title, title.contains("\\r"));
        }
        finally {
            System.setProperty("line.separator", lineSeparator);
        }
    }
}
