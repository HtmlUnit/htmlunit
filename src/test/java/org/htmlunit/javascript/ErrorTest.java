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
import org.htmlunit.junit.BrowserRunner;
import org.htmlunit.junit.annotation.Alerts;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Test for error handling.
 *
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class ErrorTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"Error", "Whoops!", "undefined", "undefined", "undefined",
                       "undefined", "true", "Error/Error"},
            FF = {"Error", "Whoops!", "undefined", "11", "undefined", "25", "true", "Error/Error"},
            FF_ESR = {"Error", "Whoops!", "undefined", "11", "undefined", "25", "true", "Error/Error"})
    public void error() throws Exception {
        final String html
            = "<html>\n"
            + "<head>\n"
            + "<script>" + LOG_TITLE_FUNCTION + "</script>\n"
            + "</head>\n"
            + "<body>\n"
            + "<script>"
            + "  try {\n"
            + "    throw new Error('Whoops!');\n"
            + "  } catch (e) {"
            + "    log(e.name);\n"
            + "    log(e.message);\n"
            + "    log(e.cause);\n"
            + "    log(e.columnNumber);\n"
            + "    log(e.filename);\n"
            + "    log(e.lineNumber);\n"
            + "    log(e instanceof Error);\n"
            + "    logEx(e);\n"
            + "  }\n"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"EvalError", "Whoops!", "undefined", "undefined", "undefined",
                       "undefined", "true", "true", "EvalError"},
            FF = {"EvalError", "Whoops!", "undefined", "11", "undefined", "25",
                  "true", "true", "EvalError"},
            FF_ESR = {"EvalError", "Whoops!", "undefined", "11", "undefined", "25",
                      "true", "true", "EvalError"})
    public void evalError() throws Exception {
        final String html
            = "<html>\n"
            + "<head>\n"
            + "<script>" + LOG_TITLE_FUNCTION + "</script>\n"
            + "</head>\n"
            + "<body>\n"
            + "<script>"
            + "  try {\n"
            + "    throw new EvalError('Whoops!');\n"
            + "  } catch (e) {"
            + "    log(e.name);\n"
            + "    log(e.message);\n"
            + "    log(e.cause);\n"
            + "    log(e.columnNumber);\n"
            + "    log(e.filename);\n"
            + "    log(e.lineNumber);\n"
            + "    log(e instanceof Error);\n"
            + "    log(e instanceof EvalError);\n"
            + "    logEx(e);\n"
            + "  }\n"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"RangeError", "Whoops!", "undefined", "undefined", "undefined",
                       "undefined", "true", "true", "RangeError"},
            FF = {"RangeError", "Whoops!", "undefined", "11", "undefined", "25",
                  "true", "true", "RangeError"},
            FF_ESR = {"RangeError", "Whoops!", "undefined", "11", "undefined", "25",
                      "true", "true", "RangeError"})
    public void rangeError() throws Exception {
        final String html
            = "<html>\n"
            + "<head>\n"
            + "<script>" + LOG_TITLE_FUNCTION + "</script>\n"
            + "</head>\n"
            + "<body>\n"
            + "<script>"
            + "  try {\n"
            + "    throw new RangeError('Whoops!');\n"
            + "  } catch (e) {"
            + "    log(e.name);\n"
            + "    log(e.message);\n"
            + "    log(e.cause);\n"
            + "    log(e.columnNumber);\n"
            + "    log(e.filename);\n"
            + "    log(e.lineNumber);\n"
            + "    log(e instanceof Error);\n"
            + "    log(e instanceof RangeError);\n"
            + "    logEx(e);\n"
            + "  }\n"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"ReferenceError", "Whoops!", "undefined", "undefined", "undefined",
                       "undefined", "true", "true", "ReferenceError"},
            FF = {"ReferenceError", "Whoops!", "undefined", "11", "undefined", "25",
                  "true", "true", "ReferenceError"},
            FF_ESR = {"ReferenceError", "Whoops!", "undefined", "11", "undefined", "25",
                      "true", "true", "ReferenceError"})
    public void referenceError() throws Exception {
        final String html
            = "<html>\n"
            + "<head>\n"
            + "<script>" + LOG_TITLE_FUNCTION + "</script>\n"
            + "</head>\n"
            + "<body>\n"
            + "<script>"
            + "  try {\n"
            + "    throw new ReferenceError('Whoops!');\n"
            + "  } catch (e) {"
            + "    log(e.name);\n"
            + "    log(e.message);\n"
            + "    log(e.cause);\n"
            + "    log(e.columnNumber);\n"
            + "    log(e.filename);\n"
            + "    log(e.lineNumber);\n"
            + "    log(e instanceof Error);\n"
            + "    log(e instanceof ReferenceError);\n"
            + "    logEx(e);\n"
            + "  }\n"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"SyntaxError", "Whoops!", "undefined", "undefined", "undefined",
                       "undefined", "true", "true", "SyntaxError"},
            FF = {"SyntaxError", "Whoops!", "undefined", "11", "undefined", "25",
                  "true", "true", "SyntaxError"},
            FF_ESR = {"SyntaxError", "Whoops!", "undefined", "11", "undefined", "25",
                      "true", "true", "SyntaxError"})
    public void syntaxError() throws Exception {
        final String html
            = "<html>\n"
            + "<head>\n"
            + "<script>" + LOG_TITLE_FUNCTION + "</script>\n"
            + "</head>\n"
            + "<body>\n"
            + "<script>"
            + "  try {\n"
            + "    throw new SyntaxError('Whoops!');\n"
            + "  } catch (e) {"
            + "    log(e.name);\n"
            + "    log(e.message);\n"
            + "    log(e.cause);\n"
            + "    log(e.columnNumber);\n"
            + "    log(e.filename);\n"
            + "    log(e.lineNumber);\n"
            + "    log(e instanceof Error);\n"
            + "    log(e instanceof SyntaxError);\n"
            + "    logEx(e);\n"
            + "  }\n"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"TypeError", "Whoops!", "undefined", "undefined", "undefined",
                       "undefined", "true", "true", "TypeError"},
            FF = {"TypeError", "Whoops!", "undefined", "11", "undefined", "25",
                  "true", "true", "TypeError"},
            FF_ESR = {"TypeError", "Whoops!", "undefined", "11", "undefined", "25",
                      "true", "true", "TypeError"})
    public void typeError() throws Exception {
        final String html
            = "<html>\n"
            + "<head>\n"
            + "<script>" + LOG_TITLE_FUNCTION + "</script>\n"
            + "</head>\n"
            + "<body>\n"
            + "<script>"
            + "  try {\n"
            + "    throw new TypeError('Whoops!');\n"
            + "  } catch (e) {"
            + "    log(e.name);\n"
            + "    log(e.message);\n"
            + "    log(e.cause);\n"
            + "    log(e.columnNumber);\n"
            + "    log(e.filename);\n"
            + "    log(e.lineNumber);\n"
            + "    log(e instanceof Error);\n"
            + "    log(e instanceof TypeError);\n"
            + "    logEx(e);\n"
            + "  }\n"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"URIError", "Whoops!", "undefined", "undefined", "undefined",
                       "undefined", "true", "true", "URIError"},
            FF = {"URIError", "Whoops!", "undefined", "11", "undefined", "25",
                  "true", "true", "URIError"},
            FF_ESR = {"URIError", "Whoops!", "undefined", "11", "undefined", "25",
                      "true", "true", "URIError"})
    public void uriError() throws Exception {
        final String html
            = "<html>\n"
            + "<head>\n"
            + "<script>" + LOG_TITLE_FUNCTION + "</script>\n"
            + "</head>\n"
            + "<body>\n"
            + "<script>"
            + "  try {\n"
            + "    throw new URIError('Whoops!');\n"
            + "  } catch (e) {"
            + "    log(e.name);\n"
            + "    log(e.message);\n"
            + "    log(e.cause);\n"
            + "    log(e.columnNumber);\n"
            + "    log(e.filename);\n"
            + "    log(e.lineNumber);\n"
            + "    log(e instanceof Error);\n"
            + "    log(e instanceof URIError);\n"
            + "    logEx(e);\n"
            + "  }\n"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"AggregateError", "Whoops!", "undefined", "Error: some error",
                       "undefined", "undefined",
                       "undefined", "true", "true", "AggregateError/AggregateError"},
            FF = {"AggregateError", "Whoops!", "undefined", "Error: some error",
                  "11", "undefined", "25",
                  "true", "true", "AggregateError/AggregateError"},
            FF_ESR = {"AggregateError", "Whoops!", "undefined", "Error: some error",
                      "11", "undefined", "25",
                      "true", "true", "AggregateError/AggregateError"})
    public void aggregateError() throws Exception {
        final String html
            = "<html>\n"
            + "<head>\n"
            + "<script>" + LOG_TITLE_FUNCTION + "</script>\n"
            + "</head>\n"
            + "<body>\n"
            + "<script>"
            + "  try {\n"
            + "    throw new AggregateError([new Error(\"some error\")], 'Whoops!');\n"
            + "  } catch (e) {"
            + "    log(e.name);\n"
            + "    log(e.message);\n"
            + "    log(e.cause);\n"
            + "    log(e.errors);\n"
            + "    log(e.columnNumber);\n"
            + "    log(e.filename);\n"
            + "    log(e.lineNumber);\n"
            + "    log(e instanceof Error);\n"
            + "    log(e instanceof AggregateError);\n"
            + "    logEx(e);\n"
            + "  }\n"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"ReferenceError", "InternalError is not defined",
                       "undefined", "undefined", "undefined",
                       "undefined", "true", "false", "ReferenceError"},
            FF = {"InternalError", "Whoops!", "undefined", "11", "undefined", "25",
                  "true", "true", "InternalError/InternalError"},
            FF_ESR = {"InternalError", "Whoops!", "undefined", "11", "undefined", "25",
                      "true", "true", "InternalError/InternalError"})
    public void internalError() throws Exception {
        final String html
            = "<html>\n"
            + "<head>\n"
            + "<script>" + LOG_TITLE_FUNCTION + "</script>\n"
            + "</head>\n"
            + "<body>\n"
            + "<script>"
            + "  try {\n"
            + "    throw new InternalError('Whoops!');\n"
            + "  } catch (e) {"
            + "    log(e.name);\n"
            + "    log(e.message);\n"
            + "    log(e.cause);\n"
            + "    log(e.columnNumber);\n"
            + "    log(e.filename);\n"
            + "    log(e.lineNumber);\n"
            + "    log(e instanceof Error);\n"
            + "    log(typeof InternalError == 'function' && e instanceof InternalError);\n"
            + "    logEx(e);\n"
            + "  }\n"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }
}
