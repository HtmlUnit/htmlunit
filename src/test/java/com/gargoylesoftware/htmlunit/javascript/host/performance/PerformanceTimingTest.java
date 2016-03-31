/*
 * Copyright (c) 2002-2016 Gargoyle Software Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
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
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.html.HtmlPageTest;

/**
 * Tests for {@link PerformanceTiming}.
 *
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class PerformanceTimingTest extends WebDriverTestCase {
    private static final long now = System.currentTimeMillis();

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("[object PerformanceTiming]")
    public void available() throws Exception {
        final String html =
                HtmlPageTest.STANDARDS_MODE_PREFIX_
                + "<html>\n"
                + "<head>\n"
                + "<script>\n"
                + "  function test() {"
                + "    var performanceTiming = performance.timing;"
                + "    if (!performanceTiming) { alert('performanceTiming not available'); return; };\n"
                + "    alert(performanceTiming);\n"
                + "  }\n"
                + "  test();"
                + "</script>\n"
                + "</head>\n"
                + "<body></body>\n"
                + "</html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void navigationStart() throws Exception {
        final String html =
                HtmlPageTest.STANDARDS_MODE_PREFIX_
                + "<html>\n"
                + "<head>\n"
                + "<script>\n"
                + "  function test() {"
                + "    var performanceTiming = performance.timing;"
                + "    if (!performanceTiming) { alert('performanceTiming not available'); return; };\n"
                + "    alert(performanceTiming.navigationStart > " + now + ");\n"
                + "  }\n"
                + "  test();"
                + "</script>\n"
                + "</head>\n"
                + "<body></body>\n"
                + "</html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "undefined",
            CHROME = "0")
    public void secureConnectionStart() throws Exception {
        final String html =
                HtmlPageTest.STANDARDS_MODE_PREFIX_
                + "<html>\n"
                + "<head>\n"
                + "<script>\n"
                + "  function test() {"
                + "    var performanceTiming = performance.timing;"
                + "    if (!performanceTiming) { alert('performanceTiming not available'); return; };\n"
                + "    alert(performanceTiming.secureConnectionStart);\n"
                + "  }\n"
                + "  test();"
                + "</script>\n"
                + "</head>\n"
                + "<body></body>\n"
                + "</html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "0", "0" })
    public void unloadEvent() throws Exception {
        final String html =
                HtmlPageTest.STANDARDS_MODE_PREFIX_
                + "<html>\n"
                + "<head>\n"
                + "<script>\n"
                + "  function test() {"
                + "    var performanceTiming = performance.timing;"
                + "    if (!performanceTiming) { alert('performanceTiming not available'); return; };\n"
                + "    alert(performanceTiming.unloadEventStart);\n"
                + "    alert(performanceTiming.unloadEventEnd);\n"
                + "  }\n"
                + "  test();"
                + "</script>\n"
                + "</head>\n"
                + "<body></body>\n"
                + "</html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "0", "0" })
    public void redirect() throws Exception {
        final String html =
                HtmlPageTest.STANDARDS_MODE_PREFIX_
                + "<html>\n"
                + "<head>\n"
                + "<script>\n"
                + "  function test() {"
                + "    var performanceTiming = performance.timing;"
                + "    if (!performanceTiming) { alert('performanceTiming not available'); return; };\n"
                + "    alert(performanceTiming.redirectStart);\n"
                + "    alert(performanceTiming.redirectEnd);\n"
                + "  }\n"
                + "  test();"
                + "</script>\n"
                + "</head>\n"
                + "<body></body>\n"
                + "</html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "true", "true" })
    public void domainLookup() throws Exception {
        final String html =
                HtmlPageTest.STANDARDS_MODE_PREFIX_
                + "<html>\n"
                + "<head>\n"
                + "<script>\n"
                + "  function test() {"
                + "    var performanceTiming = performance.timing;"
                + "    if (!performanceTiming) { alert('performanceTiming not available'); return; };\n"
                + "    var start = performanceTiming.domainLookupStart;\n"
                + "    alert(start > " + now + ");\n"
                + "    alert(performanceTiming.domainLookupEnd >= start);\n"
                + "  }\n"
                + "  test();"
                + "</script>\n"
                + "</head>\n"
                + "<body></body>\n"
                + "</html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "true", "true" })
    public void response() throws Exception {
        final String html =
                HtmlPageTest.STANDARDS_MODE_PREFIX_
                + "<html>\n"
                + "<head>\n"
                + "<script>\n"
                + "  function test() {"
                + "    var performanceTiming = performance.timing;"
                + "    if (!performanceTiming) { alert('performanceTiming not available'); return; };\n"
                + "    var start = performanceTiming.responseStart;\n"
                + "    alert(start > " + now + ");\n"
                + "    alert(performanceTiming.responseEnd >= start);\n"
                + "  }\n"
                + "  test();"
                + "</script>\n"
                + "</head>\n"
                + "<body></body>\n"
                + "</html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "true", "true" })
    public void loadEvent() throws Exception {
        final String html =
                HtmlPageTest.STANDARDS_MODE_PREFIX_
                + "<html>\n"
                + "<head>\n"
                + "<script>\n"
                + "  function test() {"
                + "    var performanceTiming = performance.timing;"
                + "    if (!performanceTiming) { alert('performanceTiming not available'); return; };\n"
                + "    var start = performanceTiming.loadEventStart;\n"
                + "    alert(start > " + now + ");\n"
                + "    alert(performanceTiming.loadEventEnd >= start);\n"
                + "  }\n"
                + "</script>\n"
                + "</head>\n"
                + "<body>\n"
                + "  <button id='clickNe' onClick='test()'>do it</button>\n"
                + "</body>\n"
                + "</html>";

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("clickNe")).click();
        verifyAlerts(driver, getExpectedAlerts());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "true", "true" })
    public void connect() throws Exception {
        final String html =
                HtmlPageTest.STANDARDS_MODE_PREFIX_
                + "<html>\n"
                + "<head>\n"
                + "<script>\n"
                + "  function test() {"
                + "    var performanceTiming = performance.timing;"
                + "    if (!performanceTiming) { alert('performanceTiming not available'); return; };\n"
                + "    var start = performanceTiming.connectStart;\n"
                + "    alert(start > " + now + ");\n"
                + "    alert(performanceTiming.connectEnd >= start);\n"
                + "  }\n"
                + "  test();"
                + "</script>\n"
                + "</head>\n"
                + "<body></body>\n"
                + "</html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void fetchStart() throws Exception {
        final String html =
                HtmlPageTest.STANDARDS_MODE_PREFIX_
                + "<html>\n"
                + "<head>\n"
                + "<script>\n"
                + "  function test() {"
                + "    var performanceTiming = performance.timing;"
                + "    if (!performanceTiming) { alert('performanceTiming not available'); return; };\n"
                + "    alert(performanceTiming.fetchStart > " + now + ");\n"
                + "  }\n"
                + "  test();"
                + "</script>\n"
                + "</head>\n"
                + "<body></body>\n"
                + "</html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "true", "true" })
    public void domContentLoadedEvent() throws Exception {
        final String html =
                HtmlPageTest.STANDARDS_MODE_PREFIX_
                + "<html>\n"
                + "<head>\n"
                + "<script>\n"
                + "  function test() {"
                + "    var performanceTiming = performance.timing;"
                + "    if (!performanceTiming) { alert('performanceTiming not available'); return; };\n"
                + "    var start = performanceTiming.domContentLoadedEventStart;\n"
                + "    alert(start > " + now + ");\n"
                + "    alert(performanceTiming.domContentLoadedEventEnd >= start);\n"
                + "  }\n"
                + "</script>\n"
                + "</head>\n"
                + "<body>\n"
                + "  <button id='clickNe' onClick='test()'>do it</button>\n"
                + "</body>\n"
                + "</html>";

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("clickNe")).click();
        verifyAlerts(driver, getExpectedAlerts());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"true", "true", "true", "true", "true"})
    public void dom() throws Exception {
        final String html =
                HtmlPageTest.STANDARDS_MODE_PREFIX_
                + "<html>\n"
                + "<head>\n"
                + "<script>\n"
                + "  function test() {"
                + "    var performanceTiming = performance.timing;"
                + "    if (!performanceTiming) { alert('performanceTiming not available'); return; };\n"
                + "    alert(performanceTiming.domLoading > " + now + ");\n"
                + "    alert(performanceTiming.domInteractive > " + now + ");\n"
                + "    alert(performanceTiming.domContentLoadedEventStart > " + now + ");\n"
                + "    alert(performanceTiming.domContentLoadedEventEnd > " + now + ");\n"
                + "    alert(performanceTiming.domComplete > " + now + ");\n"
                + "  }\n"
                + "</script>\n"
                + "</head>\n"
                + "<body>\n"
                + "  <button id='clickNe' onClick='test()'>do it</button>\n"
                + "</body>\n"
                + "</html>";

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("clickNe")).click();
        verifyAlerts(driver, getExpectedAlerts());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("[object Object]")
    @NotYetImplemented
    public void toJSON() throws Exception {
        final String html =
                HtmlPageTest.STANDARDS_MODE_PREFIX_
                + "<html>\n"
                + "<head>\n"
                + "<script>\n"
                + "  function test() {"
                + "    var performanceTiming = performance.timing;"
                + "    if (!performanceTiming) { alert('performanceTiming not available'); return; };\n"
                + "    alert(performanceTiming.toJSON());\n"
                + "  }\n"
                + "</script>\n"
                + "</head>\n"
                + "<body>\n"
                + "  <button id='clickNe' onClick='test()'>do it</button>\n"
                + "</body>\n"
                + "</html>";

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("clickNe")).click();
        verifyAlerts(driver, getExpectedAlerts());
    }
}
