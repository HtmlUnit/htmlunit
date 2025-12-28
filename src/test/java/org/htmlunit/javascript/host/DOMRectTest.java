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
package org.htmlunit.javascript.host;

import org.htmlunit.WebDriverTestCase;
import org.htmlunit.junit.annotation.Alerts;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link DOMRect}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
public class DOMRectTest extends WebDriverTestCase {

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
                + "  log(typeof DOMRect);\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"0", "0", "0", "0", "0", "0", "0", "0"})
    public void constructorNoArgs() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head></head>\n"
            + "<body>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  let rect = new DOMRect();\n"
            + "  log(rect.x);\n"
            + "  log(rect.y);\n"
            + "  log(rect.width);\n"
            + "  log(rect.height);\n"

            + "  log(rect.top);\n"
            + "  log(rect.left);\n"
            + "  log(rect.bottom);\n"
            + "  log(rect.right);\n"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"4", "0", "0", "0", "0", "4", "0", "4"})
    public void constructorOneArg() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head></head>\n"
            + "<body>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  let rect = new DOMRect(4);\n"
            + "  log(rect.x);\n"
            + "  log(rect.y);\n"
            + "  log(rect.width);\n"
            + "  log(rect.height);\n"

            + "  log(rect.top);\n"
            + "  log(rect.left);\n"
            + "  log(rect.bottom);\n"
            + "  log(rect.right);\n"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"4", "7", "0", "0", "7", "4", "7", "4"})
    public void constructorTwoArgs() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head></head>\n"
            + "<body>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  let rect = new DOMRect(4, 7);\n"
            + "  log(rect.x);\n"
            + "  log(rect.y);\n"
            + "  log(rect.width);\n"
            + "  log(rect.height);\n"

            + "  log(rect.top);\n"
            + "  log(rect.left);\n"
            + "  log(rect.bottom);\n"
            + "  log(rect.right);\n"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"4", "7", "11", "0", "7", "4", "7", "15"})
    public void constructorThreeArgs() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head></head>\n"
            + "<body>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  let rect = new DOMRect(4, 7, 11);\n"
            + "  log(rect.x);\n"
            + "  log(rect.y);\n"
            + "  log(rect.width);\n"
            + "  log(rect.height);\n"

            + "  log(rect.top);\n"
            + "  log(rect.left);\n"
            + "  log(rect.bottom);\n"
            + "  log(rect.right);\n"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"4", "7", "11", "42", "7", "4", "49", "15"})
    public void constructorFourArgs() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head></head>\n"
            + "<body>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  let rect = new DOMRect(4, 7, 11, 42);\n"
            + "  log(rect.x);\n"
            + "  log(rect.y);\n"
            + "  log(rect.width);\n"
            + "  log(rect.height);\n"

            + "  log(rect.top);\n"
            + "  log(rect.left);\n"
            + "  log(rect.bottom);\n"
            + "  log(rect.right);\n"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"4", "7", "11", "42", "7", "4", "49", "15"})
    public void constructorFiveArgs() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head></head>\n"
            + "<body>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  let rect = new DOMRect(4, 7, 11, 42, 13);\n"
            + "  log(rect.x);\n"
            + "  log(rect.y);\n"
            + "  log(rect.width);\n"
            + "  log(rect.height);\n"

            + "  log(rect.top);\n"
            + "  log(rect.left);\n"
            + "  log(rect.bottom);\n"
            + "  log(rect.right);\n"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"-4", "7.2", "-11", "-42", "-34.8", "-15", "7.2", "-4"})
    public void negativeAndFloatingPointValues() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head></head>\n"
            + "<body>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  let rect = new DOMRect(-4, 7.2, -11, -42, -13);\n"
            + "  log(rect.x);\n"
            + "  log(rect.y);\n"
            + "  log(rect.width);\n"
            + "  log(rect.height);\n"

            + "  log(rect.top);\n"
            + "  log(rect.left);\n"
            + "  log(rect.bottom);\n"
            + "  log(rect.right);\n"
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
            + "    let rect = new DOMRect(undefind);\n"
            + "    log(rect.x);\n"
            + "  } catch(e) { logEx(e);}\n"
            + "  try {\n"
            + "    let rect = new DOMRect(null);\n"
            + "    log(rect.x);\n"
            + "  } catch(e) { logEx(e);}\n"
            + "  try {\n"
            + "    let rect = new DOMRect('42');\n"
            + "    log(rect.x);\n"
            + "  } catch(e) { logEx(e);}\n"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"4", "7", "11", "42", "7", "4", "49", "15",
             "2", "5", "100", "42", "5", "2", "47", "102"})
    public void setter() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head></head>\n"
            + "<body>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  let rect = new DOMRect(4, 7, 11, 42, 13);\n"
            + "  log(rect.x);\n"
            + "  log(rect.y);\n"
            + "  log(rect.width);\n"
            + "  log(rect.height);\n"

            + "  log(rect.top);\n"
            + "  log(rect.left);\n"
            + "  log(rect.bottom);\n"
            + "  log(rect.right);\n"

            + "  rect.x = 2;\n"
            + "  rect.y = 5;\n"
            + "  rect.width = 100;\n"
            + "  rect.height = -8;\n"

            + "  log(rect.x);\n"
            + "  log(rect.y);\n"
            + "  log(rect.width);\n"
            + "  log(rect.height);\n"

            + "  log(rect.top);\n"
            + "  log(rect.left);\n"
            + "  log(rect.bottom);\n"
            + "  log(rect.right);\n"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"{\"x\":0,\"y\":0,\"width\":0,\"height\":0,\"top\":0,\"right\":0,\"bottom\":0,\"left\":0}",
             "{\"x\":0,\"y\":0,\"width\":0,\"height\":0,\"top\":0,\"right\":0,\"bottom\":0,\"left\":0}",
             "false",
             "{\"x\":4,\"y\":7,\"width\":11,\"height\":42,\"top\":7,\"right\":15,\"bottom\":49,\"left\":4}",
             "{\"x\":4,\"y\":7,\"width\":11,\"height\":42,\"top\":7,\"right\":15,\"bottom\":49,\"left\":4}",
             "false"})
    public void toJson() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head></head>\n"
            + "<body>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  let rect = new DOMRect();\n"
            + "  log(JSON.stringify(rect));\n"
            + "  log(JSON.stringify(rect.toJSON()));\n"
            + "  log(rect === rect.toJSON());\n"

            + "  rect = new DOMRect(4, 7, 11, 42);\n"
            + "  log(JSON.stringify(rect));\n"
            + "  log(JSON.stringify(rect.toJSON()));\n"
            + "  log(rect === rect.toJSON());\n"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }
}
