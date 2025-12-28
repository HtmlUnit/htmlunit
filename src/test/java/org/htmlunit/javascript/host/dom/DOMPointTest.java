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
package org.htmlunit.javascript.host.dom;

import org.htmlunit.WebDriverTestCase;
import org.htmlunit.junit.annotation.Alerts;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link DOMPoint}.
 *
 * @author Ronald Brill
 */
public class DOMPointTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("function")
    public void type() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head></head>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  log(typeof DOMPoint);\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"0", "0", "0", "1"})
    public void constructorNoArgs() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head></head>\n"
            + "<body>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  let point = new DOMPoint();\n"
            + "  log(point.x);\n"
            + "  log(point.y);\n"
            + "  log(point.z);\n"
            + "  log(point.w);\n"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"4", "0", "0", "1"})
    public void constructorOneArg() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head></head>\n"
            + "<body>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  let point = new DOMPoint(4);\n"
            + "  log(point.x);\n"
            + "  log(point.y);\n"
            + "  log(point.z);\n"
            + "  log(point.w);\n"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"4", "7", "0", "1"})
    public void constructorTwoArgs() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head></head>\n"
            + "<body>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  let point = new DOMPoint(4, 7);\n"
            + "  log(point.x);\n"
            + "  log(point.y);\n"
            + "  log(point.z);\n"
            + "  log(point.w);\n"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"4", "7", "11", "1"})
    public void constructorThreeArgs() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head></head>\n"
            + "<body>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  let point = new DOMPoint(4, 7, 11);\n"
            + "  log(point.x);\n"
            + "  log(point.y);\n"
            + "  log(point.z);\n"
            + "  log(point.w);\n"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"4", "7", "11", "42"})
    public void constructorFourArgs() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head></head>\n"
            + "<body>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  let point = new DOMPoint(4, 7, 11, 42);\n"
            + "  log(point.x);\n"
            + "  log(point.y);\n"
            + "  log(point.z);\n"
            + "  log(point.w);\n"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"4", "7", "11", "42"})
    public void constructorFiveArgs() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head></head>\n"
            + "<body>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  let point = new DOMPoint(4, 7, 11, 42, 13);\n"
            + "  log(point.x);\n"
            + "  log(point.y);\n"
            + "  log(point.z);\n"
            + "  log(point.w);\n"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"-4", "7.2", "-11", "-42"})
    public void negativeAndFloatingPointValues() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head></head>\n"
            + "<body>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  let point = new DOMPoint(-4, 7.2, -11, -42);\n"
            + "  log(point.x);\n"
            + "  log(point.y);\n"
            + "  log(point.z);\n"
            + "  log(point.w);\n"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"ReferenceError", "0", "42"})
    public void ctorWrongValTypes() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head></head>\n"
            + "<body>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  try {\n"
            + "    let point = new DOMPoint(undefind);\n"
            + "    log(point.x);\n"
            + "  } catch(e) { logEx(e);}\n"
            + "  try {\n"
            + "    let point = new DOMPoint(null);\n"
            + "    log(point.x);\n"
            + "  } catch(e) { logEx(e);}\n"
            + "  try {\n"
            + "    let point = new DOMPoint('42');\n"
            + "    log(point.x);\n"
            + "  } catch(e) { logEx(e);}\n"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"4", "7", "11", "42", "2", "5", "100", "-8"})
    public void setter() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head></head>\n"
            + "<body>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  let point = new DOMPoint(4, 7, 11, 42, 13);\n"
            + "  log(point.x);\n"
            + "  log(point.y);\n"
            + "  log(point.z);\n"
            + "  log(point.w);\n"

            + "  point.x = 2;\n"
            + "  point.y = 5;\n"
            + "  point.z = 100;\n"
            + "  point.w = -8;\n"

            + "  log(point.x);\n"
            + "  log(point.y);\n"
            + "  log(point.z);\n"
            + "  log(point.w);\n"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"{\"x\":0,\"y\":0,\"z\":0,\"w\":1}",
             "{\"x\":0,\"y\":0,\"z\":0,\"w\":1}",
             "false",
             "{\"x\":4,\"y\":7,\"z\":11,\"w\":42}",
             "{\"x\":4,\"y\":7,\"z\":11,\"w\":42}",
             "false"})
    public void toJson() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head></head>\n"
            + "<body>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  let point = new DOMPoint();\n"
            + "  log(JSON.stringify(point));\n"
            + "  log(JSON.stringify(point.toJSON()));\n"
            + "  log(point === point.toJSON());\n"

            + "  point = new DOMPoint(4, 7, 11, 42);\n"
            + "  log(JSON.stringify(point));\n"
            + "  log(JSON.stringify(point.toJSON()));\n"
            + "  log(point === point.toJSON());\n"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }
}
