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
package org.htmlunit.javascript;

import org.htmlunit.WebDriverTestCase;
import org.htmlunit.junit.annotation.Alerts;
import org.junit.jupiter.api.Test;

/**
 * Tests for Functions Rest Parameters.
 *
 * @author Ronald Brill
 */
public class FunctionsRestParametersTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("1,abc,2,##")
    public void oneRestArg() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "</script></head>\n"
            + "<body>\n"
            + "<script>\n"
            + "function rest(...restArgs) {\n"
            + "  return restArgs;\n"
            + "}\n"
            + "try {\n"
            + "  log(rest(1, 'abc', 2, '##').toString());\n"
            + "} catch(e) { log(e.message) }"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true-0")
    public void oneRestArgNothingProvided() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "</script></head>\n"
            + "<body>\n"
            + "<script>\n"
            + "function rest(...restArgs) {\n"
            + "  return restArgs;\n"
            + "}\n"
            + "try {\n"
            + "  var r = rest();\n"
            + "  log('' + Array.isArray(r) + '-' + r.length);\n"
            + "} catch(e) { log(e.message) }"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true-1")
    public void oneRestArgOneProvided() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "</script></head>\n"
            + "<body>\n"
            + "<script>\n"
            + "function rest(...restArgs) {\n"
            + "  return restArgs;\n"
            + "}\n"
            + "try {\n"
            + "  var r = rest('xy');\n"
            + "  log('' + Array.isArray(r) + '-' + r.length);\n"
            + "} catch(e) { log(e.message) }"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("abc,2,##")
    public void twoRestArg() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "</script></head>\n"
            + "<body>\n"
            + "<script>\n"
            + "function rest(arg, ...restArgs) {\n"
            + "  return restArgs;\n"
            + "}\n"
            + "try {\n"
            + "  log(rest(1, 'abc', 2, '##').toString());\n"
            + "} catch(e) { log(e.message) }"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("undefined - true-0")
    public void twoRestArgNothingProvided() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "</script></head>\n"
            + "<body>\n"
            + "<script>\n"
            + "function rest(arg, ...restArgs) {\n"
            + "  return '' + typeof arg + ' - ' + Array.isArray(restArgs) + '-' + restArgs.length;\n"
            + "}\n"
            + "try {\n"
            + "  log(rest());\n"
            + "} catch(e) { log(e.message) }"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("77 - true-0")
    public void twoRestArgOneProvided() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "</script></head>\n"
            + "<body>\n"
            + "<script>\n"
            + "function rest(arg, ...restArgs) {\n"
            + "  return '' + arg + ' - ' + Array.isArray(restArgs) + '-' + restArgs.length;\n"
            + "}\n"
            + "try {\n"
            + "  log(rest('77'));\n"
            + "} catch(e) { log(e.message) }"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("1-4")
    public void arguments() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "</script></head>\n"
            + "<body>\n"
            + "<script>\n"
            + "function rest(arg, ...restArgs) {\n"
            + "  return arguments.length;\n"
            + "}\n"
            + "try {\n"
            + "  log('' + rest('77') + '-' + rest(1, 2, 3, 4));\n"
            + "} catch(e) { log(e.message) }"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0-1")
    public void length() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "</script></head>\n"
            + "<body>\n"
            + "<script>\n"
            + "function foo1(...theArgs) {}\n"
            + "function foo2(arg, ...theArgs) {}\n"
            + "try {\n"
            + "  log(foo1.length + '-' + foo2.length);\n"
            + "} catch(e) { log(e.message) }"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("2-1-0")
    public void argLength() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "</script></head>\n"
            + "<body>\n"
            + "<script>\n"
            + "function rest(...restArgs) {\n"
            + "  return restArgs.length;\n"
            + "}\n"
            + "try {\n"
            + "  log(rest(1,2) + '-' + rest(1) + '-' + rest());\n"
            + "} catch(e) { log(e.message) }"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("function rest(...restArgs) { return restArgs.length; }")
    public void string() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "</script></head>\n"
            + "<body>\n"
            + "<script>\n"
            + "function rest(...restArgs) {\n"
            + "  return restArgs.length;\n"
            + "}\n"
            + "try {\n"
            + "  log(rest.toString());\n"
            + "} catch(e) { log(e.message) }"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }
}
