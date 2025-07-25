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
package org.htmlunit.javascript.host.performance;

import org.htmlunit.WebDriverTestCase;
import org.htmlunit.junit.annotation.Alerts;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;

/**
 * Tests for {@link Performance}.
 *
 * @author Ronald Brill
 */
public class PerformanceNavigationTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void toJSON() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<head>\n"
                + "<script>\n"
                + "  function test() {\n"
                + "    alert(JSON.stringify(performance.navigation.toJSON()));\n"
                + "  }\n"
                + "  test();\n"
                + "</script>\n"
                + "</head>\n"
                + "<body></body>\n"
                + "</html>";

        final WebDriver driver = loadPage2(html);
        final String json = getCollectedAlerts(driver, 1).get(0);
        assertTrue(json, json.contains("\"type\":0"));
        assertTrue(json, json.contains("\"redirectCount\":0"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0")
    public void redirectCount() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    log(performance.navigation.redirectCount);\n"
                + "  }\n"
                + "  test();\n"
                + "</script>\n"
                + "</head>\n"
                + "<body></body>\n"
                + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0")
    public void type() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    log(performance.navigation.type);\n"
                + "  }\n"
                + "  test();\n"
                + "</script>\n"
                + "</head>\n"
                + "<body></body>\n"
                + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"number", "function"})
    public void methods() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  log(typeof performance.navigation.redirectCount);\n"
                + "  log(typeof performance.navigation.toJSON);\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }
}
