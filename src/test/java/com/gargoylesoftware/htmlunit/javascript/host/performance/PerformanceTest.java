/*
 * Copyright (c) 2002-2022 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript.host.performance;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;

import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.html.HtmlPageTest;
import com.gargoylesoftware.htmlunit.junit.BrowserRunner;
import com.gargoylesoftware.htmlunit.junit.BrowserRunner.Alerts;

/**
 * Tests for {@link Performance}.
 *
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class PerformanceTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void now() throws Exception {
        final String html =
                HtmlPageTest.STANDARDS_MODE_PREFIX_
                + "<html>\n"
                + "<head>\n"
                + "<script>\n"
                + "  function test() {\n"
                + "    alert(performance.now());\n"
                + "    alert(performance.now());\n"
                + "    alert(typeof performance.now());\n"
                + "  }\n"
                + "  test();\n"
                + "</script>\n"
                + "</head>\n"
                + "<body></body>\n"
                + "</html>";

        final WebDriver driver = loadPage2(html);
        final String now1 = getCollectedAlerts(driver, 1).get(0);
        assertTrue(Double.parseDouble(now1) > 0);

        final String now2 = getCollectedAlerts(driver, 1).get(0);
        assertTrue(Double.parseDouble(now2) > Double.parseDouble(now1));

        assertEquals("number", getCollectedAlerts(driver, 1).get(0));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("[object PerformanceTiming]")
    public void timing() throws Exception {
        final String html =
                HtmlPageTest.STANDARDS_MODE_PREFIX_
                + "<html>\n"
                + "<head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var performanceTiming = performance.timing;\n"
                + "    log(performanceTiming);\n"
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
    @Alerts({"function", "function", "function", "function"})
    public void methods() throws Exception {
        final String html
                = "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  log(typeof performance.now);\n"
                + "  log(typeof performance.getEntries);\n"
                + "  log(typeof performance.getEntriesByName);\n"
                + "  log(typeof performance.getEntriesByType);\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }
}
