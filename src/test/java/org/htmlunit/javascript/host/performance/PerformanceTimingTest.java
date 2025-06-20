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
import org.htmlunit.junit.annotation.HtmlUnitNYI;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Tests for {@link PerformanceTiming}.
 *
 * @author Ronald Brill
 */
public class PerformanceTimingTest extends WebDriverTestCase {
    private static final long NOE = System.currentTimeMillis();

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("[object PerformanceTiming]")
    public void available() throws Exception {
        final String html = DOCTYPE_HTML
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
    @Alerts("true")
    public void navigationStart() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var performanceTiming = performance.timing;\n"
                + "    log(performanceTiming.navigationStart > " + NOE + ");\n"
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
    public void secureConnectionStart() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var performanceTiming = performance.timing;\n"
                + "    log(performanceTiming.secureConnectionStart);\n"
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
    @Alerts({"0", "0"})
    public void unloadEvent() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var performanceTiming = performance.timing;\n"
                + "    log(performanceTiming.unloadEventStart);\n"
                + "    log(performanceTiming.unloadEventEnd);\n"
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
    @Alerts({"0", "0"})
    public void redirect() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var performanceTiming = performance.timing;\n"
                + "    log(performanceTiming.redirectStart);\n"
                + "    log(performanceTiming.redirectEnd);\n"
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
    @Alerts({"true", "true"})
    public void domainLookup() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var performanceTiming = performance.timing;\n"
                + "    var start = performanceTiming.domainLookupStart;\n"
                + "    log(start > " + NOE + ");\n"
                + "    log(performanceTiming.domainLookupEnd >= start);\n"
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
    @Alerts({"true", "true"})
    public void response() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var performanceTiming = performance.timing;\n"
                + "    var start = performanceTiming.responseStart;\n"
                + "    log(start > " + NOE + ");\n"
                + "    log(performanceTiming.responseEnd >= start);\n"
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
    @Alerts({"true", "true"})
    public void loadEvent() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var performanceTiming = performance.timing;\n"
                + "    var start = performanceTiming.loadEventStart;\n"
                + "    log(start > " + NOE + ");\n"
                + "    log(performanceTiming.loadEventEnd >= start);\n"
                + "  }\n"
                + "</script>\n"
                + "</head>\n"
                + "<body>\n"
                + "  <button id='clickMe' onClick='test()'>do it</button>\n"
                + "</body>\n"
                + "</html>";

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("clickMe")).click();
        verifyTitle2(driver, getExpectedAlerts());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"true", "true"})
    public void connect() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var performanceTiming = performance.timing;\n"
                + "    var start = performanceTiming.connectStart;\n"
                + "    log(start > " + NOE + ");\n"
                + "    log(performanceTiming.connectEnd >= start);\n"
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
    @Alerts("true")
    public void fetchStart() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var performanceTiming = performance.timing;\n"
                + "    log(performanceTiming.fetchStart > " + NOE + ");\n"
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
    @Alerts({"true", "true"})
    public void domContentLoadedEvent() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var performanceTiming = performance.timing;\n"
                + "    var start = performanceTiming.domContentLoadedEventStart;\n"
                + "    log(start > " + NOE + ");\n"
                + "    log(performanceTiming.domContentLoadedEventEnd >= start);\n"
                + "  }\n"
                + "</script>\n"
                + "</head>\n"
                + "<body>\n"
                + "  <button id='clickMe' onClick='test()'>do it</button>\n"
                + "</body>\n"
                + "</html>";

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("clickMe")).click();
        verifyTitle2(driver, getExpectedAlerts());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"true", "true", "true", "true", "true"})
    public void dom() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var performanceTiming = performance.timing;\n"
                + "    log(performanceTiming.domLoading > " + NOE + ");\n"
                + "    log(performanceTiming.domInteractive > " + NOE + ");\n"
                + "    log(performanceTiming.domContentLoadedEventStart > " + NOE + ");\n"
                + "    log(performanceTiming.domContentLoadedEventEnd > " + NOE + ");\n"
                + "    log(performanceTiming.domComplete > " + NOE + ");\n"
                + "  }\n"
                + "</script>\n"
                + "</head>\n"
                + "<body>\n"
                + "  <button id='clickMe' onClick='test()'>do it</button>\n"
                + "</body>\n"
                + "</html>";

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("clickMe")).click();
        verifyTitle2(driver, getExpectedAlerts());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("[object Object]")
    @HtmlUnitNYI(CHROME = {},
            EDGE = {},
            FF = {},
            FF_ESR = {})
    public void toJSON() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var performanceTiming = performance.timing;\n"
                + "    log(performanceTiming.toJSON());\n"
                + "  }\n"
                + "</script>\n"
                + "</head>\n"
                + "<body>\n"
                + "  <button id='clickMe' onClick='test()'>do it</button>\n"
                + "</body>\n"
                + "</html>";

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("clickMe")).click();
        verifyTitle2(driver, getExpectedAlerts());
    }
}
