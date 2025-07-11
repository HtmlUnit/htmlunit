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

import static java.nio.charset.StandardCharsets.ISO_8859_1;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.htmlunit.WebDriverTestCase;
import org.htmlunit.junit.annotation.Alerts;
import org.htmlunit.junit.annotation.HtmlUnitNYI;
import org.htmlunit.util.NameValuePair;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

/**
 * Tests for {@link History}.
 *
 * @author Marc Guillemot
 * @author Ahmed Ashour
 * @author Ronald Brill
 * @author Adam Afeltowicz
 * @author Carsten Steul
 * @author Anton Demydenko
 * @author Lai Quang Duong
 */
public class History2Test extends WebDriverTestCase {

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("here")
    public void goShouldIgnoreOutOfBoundIndex() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "history.go(1);\n"
                + "log('here');\n"
                + "</script></body></html>";

        loadPageVerifyTitle2(html);
        assertEquals(1, getMockWebConnection().getRequestCount());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"[object PopStateEvent]", "null"})
    public void pushStateSimple() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    if (!window.history.pushState) { log('no pushState'); return }\n"
                + "    var stateObj = { hi: 'there' };\n"
                + "    window.history.pushState(stateObj, 'page 2', 'bar.html');\n"
                + "  }\n"

                + "  function popMe(event) {\n"
                + "    var e = event ? event : window.event;\n"
                + "    log(e);\n"
                + "    log(e.state);\n"
                + "  }\n"
                + "</script>\n"
                + "</head>\n"
                + "<body onpopstate='popMe(event)'>\n"
                + "  <button id=myId onclick='test()'>Click me</button>\n"
                + "</body></html>";

        final String[] expectedAlerts = getExpectedAlerts();
        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("myId")).click();

        if (expectedAlerts.length > 1) {
            assertEquals(URL_FIRST + "bar.html", driver.getCurrentUrl());
            driver.navigate().back();
        }
        verifyTitle2(driver, expectedAlerts);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"[object PopStateEvent]", "{\"hi\":\"there\"}",
                "[object PopStateEvent]", "{\"hi\":\"there\"}",
                "[object PopStateEvent]", "null",
                "[object PopStateEvent]", "null",
                "[object PopStateEvent]", "{\"hi\":\"there\"}",
                "[object PopStateEvent]", "{\"hi\":\"there\"}",
                "[object PopStateEvent]", "{\"hi2\":\"there2\"}",
                "[object PopStateEvent]", "{\"hi2\":\"there2\"}"})
    public void pushState() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    if (window.history.pushState) {\n"
                + "      var stateObj = { hi: 'there' };\n"
                + "      window.history.pushState(stateObj, 'page 2', 'bar.html');\n"
                + "    }\n"
                + "  }\n"

                + "  function test2() {\n"
                + "    if (window.history.pushState) {\n"
                + "      var stateObj = { hi2: 'there2' };\n"
                + "      window.history.pushState(stateObj, 'page 3', 'bar2.html');\n"
                + "    }\n"
                + "  }\n"

                + "  function popMe(event) {\n"
                + "    var e = event ? event : window.event;\n"
                + "    log(e);\n"
                + "    log(JSON.stringify(e.state));\n"
                + "  }\n"

                + "  function setWindowName() {\n"
                + "    window.name = window.name + 'a\\u00a7';\n"
                + "  }\n"

                + "  window.addEventListener('popstate', popMe);\n"
                + "</script>\n"
                + "</head>\n"
                + "<body onpopstate='popMe(event)' onload='setWindowName()' onbeforeunload='setWindowName()' "
                                                                + "onunload='setWindowName()'>\n"
                + "  <button id=myId onclick='test()'>Click me</button>\n"
                + "  <button id=myId2 onclick='test2()'>Click me</button>\n"
                + "</body></html>";

        final String[] expectedAlerts = getExpectedAlerts();
        int i = 0;

        final WebDriver driver = loadPage2(html);
        verifyWindowName2(driver, "a");

        final long start = (Long) ((JavascriptExecutor) driver).executeScript("return window.history.length");

        driver.findElement(By.id("myId")).click();
        verifyWindowName2(driver, "a");
        assertEquals(start + 1, ((JavascriptExecutor) driver).executeScript("return window.history.length"));
        assertEquals(URL_FIRST + "bar.html", driver.getCurrentUrl());

        driver.findElement(By.id("myId2")).click();
        verifyWindowName2(driver, "a");
        assertEquals(start + 2, ((JavascriptExecutor) driver).executeScript("return window.history.length"));
        assertEquals(URL_FIRST + "bar2.html", driver.getCurrentUrl());

        driver.navigate().back();
        i = i + 4;
        verifyTitle2(driver, Arrays.copyOfRange(expectedAlerts, 0, i));
        verifyWindowName2(driver, "a");
        assertEquals(start + 2, ((JavascriptExecutor) driver).executeScript("return window.history.length"));
        assertEquals(URL_FIRST + "bar.html", driver.getCurrentUrl());

        driver.navigate().back();
        i = i + 4;
        verifyTitle2(driver, Arrays.copyOfRange(expectedAlerts, 0, i));
        verifyWindowName2(driver, "a");
        assertEquals(start + 2, ((JavascriptExecutor) driver).executeScript("return window.history.length"));
        assertEquals(URL_FIRST.toString(), driver.getCurrentUrl());

        driver.navigate().forward();
        i = i + 4;
        verifyTitle2(driver, Arrays.copyOfRange(expectedAlerts, 0, i));
        verifyWindowName2(driver, "a");
        assertEquals(start + 2, ((JavascriptExecutor) driver).executeScript("return window.history.length"));
        assertEquals(URL_FIRST + "bar.html", driver.getCurrentUrl());

        driver.navigate().forward();
        i = i + 4;
        verifyTitle2(driver, Arrays.copyOfRange(expectedAlerts, 0, i));
        verifyWindowName2(driver, "a");
        assertEquals(start + 2, ((JavascriptExecutor) driver).executeScript("return window.history.length"));
        assertEquals(URL_FIRST + "bar2.html", driver.getCurrentUrl());

        assertEquals(1, getMockWebConnection().getRequestCount());

        // because we have changed the window name
        releaseResources();
        shutDownAll();
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"[object PopStateEvent]", "{\"hi\":\"there\"}", "true",
             "[object PopStateEvent]", "{\"hi\":\"there\"}", "true",
             "[object PopStateEvent]", "null", "true",
             "[object PopStateEvent]", "null", "true",
             "[object PopStateEvent]", "{\"hi\":\"there\"}", "true",
             "[object PopStateEvent]", "{\"hi\":\"there\"}", "true",
             "[object PopStateEvent]", "{\"hi2\":\"there2\"}", "true",
             "[object PopStateEvent]", "{\"hi2\":\"there2\"}", "true"})
    public void pushStateClone() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    if (window.history.pushState) {\n"
                + "      var stateObj = { hi: 'there' };\n"
                + "      window.history.pushState(stateObj, 'page 2', 'bar.html');\n"
                + "    }\n"
                + "  }\n"

                + "  function test2() {\n"
                + "    if (window.history.pushState) {\n"
                + "      var stateObj = { hi2: 'there2' };\n"
                + "      window.history.pushState(stateObj, 'page 3', 'bar2.html');\n"
                + "    }\n"
                + "  }\n"

                + "  function popMe(event) {\n"
                + "    var e = event ? event : window.event;\n"
                + "    log(e);\n"
                + "    log(JSON.stringify(e.state));\n"
                + "    log(e.state == history.state);\n"
                + "  }\n"

                + "  function setWindowName() {\n"
                + "    window.name = window.name + 'a\\u00a7';\n"
                + "  }\n"

                + "  window.addEventListener('popstate', popMe);\n"
                + "</script>\n"
                + "</head>\n"
                + "<body onpopstate='popMe(event)' onload='setWindowName()' onbeforeunload='setWindowName()' "
                                                                + "onunload='setWindowName()'>\n"
                + "  <button id=myId onclick='test()'>Click me</button>\n"
                + "  <button id=myId2 onclick='test2()'>Click me</button>\n"
                + "</body></html>";

        final String[] expectedAlerts = getExpectedAlerts();
        int i = 0;
        final WebDriver driver = loadPage2(html);
        verifyWindowName2(driver, "a");

        final long start = (Long) ((JavascriptExecutor) driver).executeScript("return window.history.length");

        final long waitTime = 4 * DEFAULT_WAIT_TIME.toMillis();
        if (expectedAlerts.length != 0) {
            driver.findElement(By.id("myId")).click();
            verifyWindowName2(driver, "a");
            assertEquals(start + 1, ((JavascriptExecutor) driver).executeScript("return window.history.length"));
            assertEquals(URL_FIRST + "bar.html", driver.getCurrentUrl());

            driver.findElement(By.id("myId2")).click();
            verifyWindowName2(driver, "a");
            assertEquals(start + 2, ((JavascriptExecutor) driver).executeScript("return window.history.length"));
            assertEquals(URL_FIRST + "bar2.html", driver.getCurrentUrl());

            driver.navigate().back();
            i = 6;
            verifyTitle2(Duration.ofMillis(waitTime), driver, Arrays.copyOfRange(expectedAlerts, 0, i));
            verifyWindowName2(driver, "a");
            assertEquals(start + 2, ((JavascriptExecutor) driver).executeScript("return window.history.length"));
            assertEquals(URL_FIRST + "bar.html", driver.getCurrentUrl());

            driver.navigate().back();
            i = i + 6;
            verifyTitle2(Duration.ofMillis(waitTime), driver, Arrays.copyOfRange(expectedAlerts, 0, i));
            verifyWindowName2(driver, "a");
            assertEquals(start + 2, ((JavascriptExecutor) driver).executeScript("return window.history.length"));
            assertEquals(URL_FIRST.toString(), driver.getCurrentUrl());

            driver.navigate().forward();
            i = i + 6;
            verifyTitle2(Duration.ofMillis(waitTime), driver, Arrays.copyOfRange(expectedAlerts, 0, i));
            verifyWindowName2(driver, "a");
            assertEquals(start + 2, ((JavascriptExecutor) driver).executeScript("return window.history.length"));
            assertEquals(URL_FIRST + "bar.html", driver.getCurrentUrl());

            driver.navigate().forward();
            i = i + 6;
            verifyTitle2(Duration.ofMillis(waitTime), driver, Arrays.copyOfRange(expectedAlerts, 0, i));
            verifyWindowName2(driver, "a");
            assertEquals(start + 2, ((JavascriptExecutor) driver).executeScript("return window.history.length"));
            assertEquals(URL_FIRST + "bar2.html", driver.getCurrentUrl());
        }

        assertEquals(1, getMockWebConnection().getRequestCount());

        // because we have changed the window name
        releaseResources();
        shutDownAll();
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"true", "true"})
    public void pushStateLocationHref() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    if (!window.history.pushState) { log('no pushState'); return }\n"
                + "    try {\n"
                + "      var stateObj = { hi: 'there' };\n"
                + "      window.history.pushState(stateObj, 'page 2', 'bar.html');\n"
                + "      log(location.href.indexOf('bar.html') > -1);\n"
                + "    } catch(e) { logEx(e); }\n"
                + "  }\n"

                + "  function test2() {\n"
                + "    if (!window.history.pushState) { log('no pushState'); return }\n"
                + "    try {\n"
                + "      var stateObj = { hi2: 'there2' };\n"
                + "      window.history.pushState(stateObj, 'page 3', 'bar2.html');\n"
                + "      log(location.href.indexOf('bar2.html') > -1);\n"
                + "    } catch(e) { logEx(e); }\n"
                + "  }\n"

                + "</script>\n"
                + "</head>\n"
                + "<body>\n"
                + "  <button id=myId onclick='test()'>Click me</button>\n"
                + "  <button id=myId2 onclick='test2()'>Click me</button>\n"
                + "</body></html>";

        final String[] expectedAlerts = getExpectedAlerts();
        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("myId")).click();
        verifyTitle2(driver, Arrays.copyOfRange(expectedAlerts, 0, 1));

        assertEquals(URL_FIRST + "bar.html", driver.getCurrentUrl());
        assertEquals(URL_FIRST + "bar.html", ((JavascriptExecutor) driver).executeScript("return location.href"));
        driver.findElement(By.id("myId2")).click();
        verifyTitle2(driver, Arrays.copyOfRange(expectedAlerts, 0, 2));

        assertEquals(URL_FIRST + "bar2.html", driver.getCurrentUrl());
        assertEquals(URL_FIRST + "bar2.html", ((JavascriptExecutor) driver).executeScript("return location.href"));
        driver.navigate().back();
        assertEquals(URL_FIRST + "bar.html", driver.getCurrentUrl());
        driver.navigate().back();
        assertEquals(URL_FIRST.toString(), driver.getCurrentUrl());
        driver.navigate().forward();
        assertEquals(URL_FIRST + "bar.html", driver.getCurrentUrl());
        driver.navigate().forward();
        assertEquals(URL_FIRST + "bar2.html", driver.getCurrentUrl());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"[object PopStateEvent]", "null",
             "[object PopStateEvent]", "null",
             "[object PopStateEvent]", "{\"hi2\":\"there2\"}",
             "[object PopStateEvent]", "{\"hi2\":\"there2\"}"})
    public void replaceState() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    if (window.history.pushState) {\n"
                + "      var stateObj = { hi: 'there' };\n"
                + "      window.history.pushState(stateObj, 'page 2', 'bar.html');\n"
                + "    }\n"
                + "  }\n"

                + "  function test2() {\n"
                + "    if (window.history.replaceState) {\n"
                + "      var stateObj = { hi2: 'there2' };\n"
                + "      window.history.replaceState(stateObj, 'page 3', 'bar2.html');\n"
                + "    }\n"
                + "  }\n"

                + "  function popMe(event) {\n"
                + "    var e = event ? event : window.event;\n"
                + "    log(e);\n"
                + "    log(JSON.stringify(e.state));\n"
                + "  }\n"

                + "  function setWindowName() {\n"
                + "    window.name = window.name + 'a\\u00a7';\n"
                + "  }\n"

                + "  window.addEventListener('popstate', popMe);\n"
                + "</script>\n"
                + "</head>\n"
                + "<body onpopstate='popMe(event)' onload='setWindowName()' onbeforeunload='setWindowName()' "
                                                        + "onunload='setWindowName()'>\n"
                + "  <button id='myId' onclick='test()'>Click me</button>\n"
                + "  <button id='myId2' onclick='test2()'>Click me</button>\n"
                + "</body></html>";

        final String[] expectedAlerts = getExpectedAlerts();
        int i = 0;
        final WebDriver driver = loadPage2(html);

        verifyWindowName2(driver, "a");
        final long start = (Long) ((JavascriptExecutor) driver).executeScript("return window.history.length");

        if (expectedAlerts.length != 0) {
            driver.findElement(By.id("myId")).click();
            verifyWindowName2(driver, "a");
            assertEquals(start + 1, ((JavascriptExecutor) driver).executeScript("return window.history.length"));
            assertEquals(URL_FIRST + "bar.html", driver.getCurrentUrl());

            driver.findElement(By.id("myId2")).click();
            verifyWindowName2(driver, "a");
            assertEquals(start + 1, ((JavascriptExecutor) driver).executeScript("return window.history.length"));
            assertEquals(URL_FIRST + "bar2.html", driver.getCurrentUrl());

            driver.navigate().back();
            i = i + 4;
            verifyTitle2(driver, Arrays.copyOfRange(expectedAlerts, 0, i));
            verifyWindowName2(driver, "a");
            assertEquals(start + 1, ((JavascriptExecutor) driver).executeScript("return window.history.length"));
            assertEquals(URL_FIRST.toString(), driver.getCurrentUrl());

            driver.navigate().forward();
            i = i + 4;
            verifyTitle2(driver, Arrays.copyOfRange(expectedAlerts, 0, i));
            verifyWindowName2(driver, "a");
            assertEquals(start + 1, ((JavascriptExecutor) driver).executeScript("return window.history.length"));
            assertEquals(URL_FIRST + "bar2.html", driver.getCurrentUrl());
        }

        assertEquals(1, getMockWebConnection().getRequestCount());

        // because we have changed the window name
        releaseResources();
        shutDownAll();
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"[object PopStateEvent]", "null", "true",
             "[object PopStateEvent]", "null", "true",
             "[object PopStateEvent]", "{\"hi2\":\"there2\"}", "true",
             "[object PopStateEvent]", "{\"hi2\":\"there2\"}", "true"})
    public void replaceStateClone() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    if (window.history.pushState) {\n"
                + "      var stateObj = { hi: 'there' };\n"
                + "      window.history.pushState(stateObj, 'page 2', 'bar.html');\n"
                + "    }\n"
                + "  }\n"

                + "  function test2() {\n"
                + "    if (window.history.replaceState) {\n"
                + "      var stateObj = { hi2: 'there2' };\n"
                + "      window.history.replaceState(stateObj, 'page 3', 'bar2.html');\n"
                + "    }\n"
                + "  }\n"

                + "  function popMe(event) {\n"
                + "    var e = event ? event : window.event;\n"
                + "    log(e);\n"
                + "    log(JSON.stringify(e.state));\n"
                + "    log(e.state == history.state);\n"
                + "  }\n"

                + "  function setWindowName() {\n"
                + "    window.name = window.name + 'a\\u00a7';\n"
                + "  }\n"

                + "  window.addEventListener('popstate', popMe);\n"
                + "</script>\n"
                + "</head>\n"
                + "<body onpopstate='popMe(event)' onload='setWindowName()' onbeforeunload='setWindowName()' "
                                                        + "onunload='setWindowName()'>\n"
                + "  <button id=myId onclick='test()'>Click me</button>\n"
                + "  <button id=myId2 onclick='test2()'>Click me</button>\n"
                + "</body></html>";

        final String[] expectedAlerts = getExpectedAlerts();
        int i = 0;
        final WebDriver driver = loadPage2(html);

        verifyWindowName2(driver, "a");
        final long start = (Long) ((JavascriptExecutor) driver).executeScript("return window.history.length");

        if (expectedAlerts.length != 0) {
            driver.findElement(By.id("myId")).click();
            verifyWindowName2(driver, "a");
            assertEquals(start + 1, ((JavascriptExecutor) driver).executeScript("return window.history.length"));
            assertEquals(URL_FIRST + "bar.html", driver.getCurrentUrl());

            driver.findElement(By.id("myId2")).click();
            verifyWindowName2(driver, "a");
            assertEquals(start + 1, ((JavascriptExecutor) driver).executeScript("return window.history.length"));
            assertEquals(URL_FIRST + "bar2.html", driver.getCurrentUrl());

            driver.navigate().back();
            i = i + 6;
            verifyTitle2(driver, Arrays.copyOfRange(expectedAlerts, 0, i));
            verifyWindowName2(driver, "a");
            assertEquals(start + 1, ((JavascriptExecutor) driver).executeScript("return window.history.length"));
            assertEquals(URL_FIRST.toString(), driver.getCurrentUrl());

            driver.navigate().forward();
            i = i + 6;
            verifyTitle2(driver, Arrays.copyOfRange(expectedAlerts, 0, i));
            verifyWindowName2(driver, "a");
            assertEquals(start + 1, ((JavascriptExecutor) driver).executeScript("return window.history.length"));
            assertEquals(URL_FIRST + "bar2.html", driver.getCurrentUrl());
        }

        assertEquals(1, getMockWebConnection().getRequestCount());

        // because we have changed the window name
        releaseResources();
        shutDownAll();
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"href=§§URL§§", "href=§§URL§§?p=%C3%84"},
            FF = {"href=§§URL§§", "href=§§URL§§?p=%C4"},
            FF_ESR = {"href=§§URL§§", "href=§§URL§§?p=%C4"})
    @HtmlUnitNYI(CHROME = {"href=§§URL§§", "href=§§URL§§?p=%C4"},
            EDGE = {"href=§§URL§§", "href=§§URL§§?p=%C4"})
    public void pushStateEncoding() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    log('href=' + location.href);\n"
                + "    window.history.pushState(null, '', '" + URL_FIRST + "?p=\u00c4');\n"
                + "    log('href=' + location.href);\n"
                + "  }\n"
                + "</script>\n"
                + "</head>\n"
                + "<body onload='test()'>\n"
                + "</body></html>";

        expandExpectedAlertsVariables(URL_FIRST);
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"href=§§URL§§", "hash=", "href=§§URL§§#foo", "hash=#foo", "onhashchange #proof"})
    public void pushStateChangeHash() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    log('href=' + location.href);\n"
                + "    log('hash=' + location.hash);\n"
                + "    window.history.pushState({ hi: 'there' }, '', '" + URL_FIRST + "#foo');\n"
                + "    log('href=' + location.href);\n"
                + "    log('hash=' + location.hash);\n"
                // to make sure we have the event handler registered
                + "    location.hash = 'proof';"
                + "  }\n"

                + " function locationHashChanged(event) {\n"
                + "   log('onhashchange ' + location.hash);\n"
                + " }\n"
                + " window.onhashchange = locationHashChanged;\n"
                + "</script>\n"
                + "</head>\n"
                + "<body onload='test()'>\n"
                + "</body></html>";

        expandExpectedAlertsVariables(URL_FIRST);
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"href=§§URL§§", "href=§§URL§§"})
    public void replaceStateNull() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    log('href=' + location.href);\n"
                + "    window.history.replaceState(null, '', null);\n"
                + "    log('href=' + location.href);\n"
                + "  }\n"
                + "</script>\n"
                + "</head>\n"
                + "<body onload='test()'>\n"
                + "</body></html>";

        expandExpectedAlertsVariables(URL_FIRST);
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"href=§§URL§§", "href=§§URL§§"})
    public void replaceStateUndefined() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    log('href=' + location.href);\n"
                + "    window.history.replaceState(null, '', undefined);\n"
                + "    log('href=' + location.href);\n"
                + "  }\n"
                + "</script>\n"
                + "</head>\n"
                + "<body onload='test()'>\n"
                + "</body></html>";

        expandExpectedAlertsVariables(URL_FIRST);
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"href=§§URL§§", "href=§§URL§§undefined"})
    public void replaceStateUndefinedString() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    log('href=' + location.href);\n"
                + "    window.history.replaceState(null, '', 'undefined');\n"
                + "    log('href=' + location.href);\n"
                + "  }\n"
                + "</script>\n"
                + "</head>\n"
                + "<body onload='test()'>\n"
                + "</body></html>";

        expandExpectedAlertsVariables(URL_FIRST);
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"href=§§URL§§", "href=§§URL§§[object%20Object]"})
    @HtmlUnitNYI(CHROME = {"href=§§URL§§", "href=§§URL§§%5Bobject%20Object%5D"},
                 EDGE = {"href=§§URL§§", "href=§§URL§§%5Bobject%20Object%5D"},
                 FF = {"href=§§URL§§", "href=§§URL§§%5Bobject%20Object%5D"},
                 FF_ESR = {"href=§§URL§§", "href=§§URL§§%5Bobject%20Object%5D"})
    public void replaceStateObj() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    log('href=' + location.href);\n"
                + "    "
                + "    window.history.replaceState(null, '', { val: 'abcd' });\n"
                + "    log('href=' + location.href);\n"
                + "  }\n"
                + "</script>\n"
                + "</head>\n"
                + "<body onload='test()'>\n"
                + "</body></html>";

        expandExpectedAlertsVariables(URL_FIRST);
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"href=§§URL§§", "href=§§URL§§"})
    public void pushStateNull() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    log('href=' + location.href);\n"
                + "    window.history.pushState(null, '', null);\n"
                + "    log('href=' + location.href);\n"
                + "  }\n"
                + "</script>\n"
                + "</head>\n"
                + "<body onload='test()'>\n"
                + "</body></html>";

        expandExpectedAlertsVariables(URL_FIRST);
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"href=§§URL§§", "href=§§URL§§"})
    public void pushStateUndefined() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    log('href=' + location.href);\n"
                + "    window.history.pushState(null, '', undefined);\n"
                + "    log('href=' + location.href);\n"
                + "  }\n"
                + "</script>\n"
                + "</head>\n"
                + "<body onload='test()'>\n"
                + "</body></html>";

        expandExpectedAlertsVariables(URL_FIRST);
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"href=§§URL§§", "href=§§URL§§undefined"})
    public void pushStateUndefinedString() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    log('href=' + location.href);\n"
                + "    window.history.pushState(null, '', 'undefined');\n"
                + "    log('href=' + location.href);\n"
                + "  }\n"
                + "</script>\n"
                + "</head>\n"
                + "<body onload='test()'>\n"
                + "</body></html>";

        expandExpectedAlertsVariables(URL_FIRST);
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"href=§§URL§§", "href=§§URL§§[object%20Object]"})
    @HtmlUnitNYI(CHROME = {"href=§§URL§§", "href=§§URL§§%5Bobject%20Object%5D"},
            EDGE = {"href=§§URL§§", "href=§§URL§§%5Bobject%20Object%5D"},
            FF = {"href=§§URL§§", "href=§§URL§§%5Bobject%20Object%5D"},
            FF_ESR = {"href=§§URL§§", "href=§§URL§§%5Bobject%20Object%5D"})
    public void pushStateObj() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    log('href=' + location.href);\n"
                + "    window.history.pushState(null, '', { val: 'abcd' });\n"
                + "    log('href=' + location.href);\n"
                + "  }\n"
                + "</script>\n"
                + "</head>\n"
                + "<body onload='test()'>\n"
                + "</body></html>";

        expandExpectedAlertsVariables(URL_FIRST);
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"href=§§URL§§", "hash=", "href=§§URL§§#foo", "hash=#foo", "onhashchange #proof"})
    public void pushStateChangeHashNoStore() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    log('href=' + location.href);\n"
                + "    log('hash=' + location.hash);\n"
                + "    window.history.pushState({ hi: 'there' }, '', '" + URL_FIRST + "#foo');\n"
                + "    log('href=' + location.href);\n"
                + "    log('hash=' + location.hash);\n"
                // to make sure we have the event handler registered
                + "    location.hash = 'proof';"
                + "  }\n"

                + " function locationHashChanged(event) {\n"
                + "   log('onhashchange ' + location.hash);\n"
                + " }\n"
                + " window.onhashchange = locationHashChanged;\n"
                + "</script>\n"
                + "</head>\n"
                + "<body onload='test()'>\n"
                + "</body></html>";

        expandExpectedAlertsVariables(URL_FIRST);

        final List<NameValuePair> headers = new ArrayList<>();
        headers.add(new NameValuePair("Cache-Control", "no-store"));
        getMockWebConnection().setResponse(URL_FIRST, html, 200, "OK", "text/html;charset=ISO-8859-1",
            ISO_8859_1, headers);

        final WebDriver driver = loadPage2(URL_FIRST, null);
        verifyTitle2(driver, getExpectedAlerts());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"href=§§URL§§", "href=§§URL§§?p=%C3%84"},
            FF = {"href=§§URL§§", "href=§§URL§§?p=%C4"},
            FF_ESR = {"href=§§URL§§", "href=§§URL§§?p=%C4"})
    @HtmlUnitNYI(CHROME = {"href=§§URL§§", "href=§§URL§§?p=%C4"},
            EDGE = {"href=§§URL§§", "href=§§URL§§?p=%C4"})
    public void replaceStateEncoding() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    log('href=' + location.href);\n"
                + "    window.history.replaceState(null, '', '" + URL_FIRST + "?p=\u00c4');\n"
                + "    log('href=' + location.href);\n"
                + "  }\n"
                + "</script>\n"
                + "</head>\n"
                + "<body onload='test()'>\n"
                + "</body></html>";

        expandExpectedAlertsVariables(URL_FIRST);
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"href=§§URL§§", "hash=", "href=§§URL§§#foo", "hash=#foo", "onhashchange #proof"})
    public void replaceStateChangeHash() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    log('href=' + location.href);\n"
                + "    log('hash=' + location.hash);\n"
                + "    window.history.replaceState({ hi: 'there' }, '', '" + URL_FIRST + "#foo');\n"
                + "    log('href=' + location.href);\n"
                + "    log('hash=' + location.hash);\n"

                // to make sure we have the event handler registered
                + "    location.hash = 'proof';"
                + "  }\n"

                + " function locationHashChanged(event) {\n"
                + "   log('onhashchange ' + location.hash);\n"
                + " }\n"
                + " window.onhashchange = locationHashChanged;\n"
                + "</script>\n"
                + "</head>\n"
                + "<body onload='test()'>\n"
                + "</body></html>";

        expandExpectedAlertsVariables(URL_FIRST);
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"href=§§URL§§", "hash=", "href=§§URL§§#foo", "hash=#foo", "onhashchange #proof"})
    public void replaceStateChangeHashNoStore() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    log('href=' + location.href);\n"
                + "    log('hash=' + location.hash);\n"
                + "    window.history.replaceState({ hi: 'there' }, '', '" + URL_FIRST + "#foo');\n"
                + "    log('href=' + location.href);\n"
                + "    log('hash=' + location.hash);\n"

                // to make sure we have the event handler registered
                + "    location.hash = 'proof';"
                + "  }\n"

                + " function locationHashChanged(event) {\n"
                + "   log('onhashchange ' + location.hash);\n"
                + " }\n"
                + " window.onhashchange = locationHashChanged;\n"
                + "</script>\n"
                + "</head>\n"
                + "<body onload='test()'>\n"
                + "</body></html>";

        expandExpectedAlertsVariables(URL_FIRST);

        final List<NameValuePair> headers = new ArrayList<>();
        headers.add(new NameValuePair("Cache-Control", "no-store"));
        getMockWebConnection().setResponse(URL_FIRST, html, 200, "OK", "text/html;charset=ISO-8859-1",
            ISO_8859_1, headers);

        final WebDriver driver = loadPage2(URL_FIRST, null);
        verifyTitle2(driver, getExpectedAlerts());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void length() throws Exception {
        final String second = DOCTYPE_HTML
                + "<html>\n"
                + "<head></head>\n"
                + "<body>\n"
                + "<a name='length' href='' onclick='alert(history.length);return false;'>length</a><br>\n"
                + "<a name='x' href='#x'>x</a><br>\n"
                + "</body></html>\n";

        getMockWebConnection().setResponse(URL_SECOND, second);

        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<head></head>\n"
                + "<body>\n"
                + "<a name='length' href='' onclick='alert(history.length);return false;'>length</a><br>\n"
                + "<a name='b' href='" + URL_SECOND + "'>b</a><br>\n"
                + "</body></html>\n";

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.name("length")).click();

        // when testing with real browsers we are facing different offsets
        final int start = Integer.parseInt(getCollectedAlerts(driver, 1).get(0));

        driver.findElement(By.name("b")).click();
        driver.findElement(By.name("length")).click();
        assertEquals(new String[] {"" + (start + 1)}, getCollectedAlerts(driver, 1));

        driver.findElement(By.name("x")).click();
        driver.findElement(By.name("length")).click();
        assertEquals(new String[] {"" + (start + 2)}, getCollectedAlerts(driver, 1));
    }

    /**
     * History.previous was defined in old FF versions.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("undefined")
    public void previous() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "</script>\n"
                + "</head>\n"
                + "<body>\n"
                + "<a name='itemZero' href='' onclick='log(history.previous); return false;'>item zero</a>\n"
                + "</body></html>\n";

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.name("itemZero")).click();

        verifyTitle2(driver, getExpectedAlerts());
    }

    /**
     * History.current was defined in old FF versions.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("undefined")
    public void current() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "</script>\n"
                + "</head>\n"
                + "<body>\n"
                + "<a name='itemZero' href='' onclick='log(history.current); return false;'>item zero</a>\n"
                + "</body></html>\n";

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.name("itemZero")).click();

        verifyTitle2(driver, getExpectedAlerts());
    }

    /**
     * History.next was defined in old FF versions.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("undefined")
    public void next() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "</script>\n"
                + "</head>\n"
                + "<body>\n"
                + "<a name='itemZero' href='' onclick='log(history.next); return false;'>item zero</a>\n"
                + "</body></html>\n";

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.name("itemZero")).click();

        verifyTitle2(driver, getExpectedAlerts());
    }

    /**
     * History.item was defined in old FF versions.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("undefined")
    public void item() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "</script>\n"
                + "</head>\n"
                + "<body>\n"
                + "<a name='itemZero' href='' onclick='log(history.item); return false;'>item zero</a>\n"
                + "</body></html>\n";

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.name("itemZero")).click();

        verifyTitle2(driver, getExpectedAlerts());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"false", "false", "false", "false", "false", "false"})
    public void byIndex() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "</script>\n"
                + "</head>\n"
                + "<body>\n"
                + "<a name='hasNegativeOne' href='' onclick="
                    + "'log(\"-1\" in history);log(-1 in history);return false;'>has negative one</a><br>\n"
                + "<a name='hasZero' href='' onclick="
                    + "'log(\"0\" in history);log(0 in history);return false;'>has zero</a><br>\n"
                + "<a name='hasPositiveOne' href='' onclick="
                    + "'log(\"1\" in history);log(1 in history);return false;'>has positive one</a><br>\n"
                + "</body></html>\n";

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.name("hasNegativeOne")).click();
        verifyTitle2(driver, Arrays.copyOfRange(getExpectedAlerts(), 0, 2));
        driver.findElement(By.name("hasZero")).click();
        verifyTitle2(driver, Arrays.copyOfRange(getExpectedAlerts(), 0, 4));
        driver.findElement(By.name("hasPositiveOne")).click();
        verifyTitle2(driver, Arrays.copyOfRange(getExpectedAlerts(), 0, 6));
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("undefined")
    public void arrayAccess() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "</script>\n"
                + "</head>\n"
                + "<body>\n"
                + "<a name='arrayAccess' href='' onclick='log(history[0]);return false;'>array access</a><br>\n"
                + "</body></html>\n";

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.name("arrayAccess")).click();

        verifyTitle2(driver, getExpectedAlerts());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("null")
    public void state() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head><script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    log(history.state);\n"
                + "  }\n"
                + "</script></head>\n"
                + "<body onload='test()'>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"back", "forward", "go", "length", "pushState", "replaceState",
             "scrollRestoration", "state"})
    public void properties() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head><script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var props = [];\n"
                + "    var i = 0;\n"
                + "    for (prop in window.history) {\n"
                + "      props[i++] = prop;\n"
                + "    }\n"
                + "    props.sort();\n"
                + "    for (i = 0; i < props.length; i++) {\n"
                + "      log(props[i]);\n"
                + "    }\n"
                + "  }\n"
                + "</script></head>\n"
                + "<body onload='test()'>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Test that a new page loads after history.pushState() is called.
     * @throws Exception if test fails
     */
    @Test
    public void loadPageAfterPushState() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<head>\n"
                + "<title>page1</title>\n"
                + "<script>\n"
                + "  function pushState() {\n"
                + "    window.history.pushState({'key':'value'});\n"
                + "  }\n"
                + "</script>\n"
                + "</head>\n"
                + "<body onload='pushState()'>\n"
                + "</body></html>";
        final String html2 = DOCTYPE_HTML
                + "<html>\n"
                + "<head>\n"
                + "<title>page2</title>\n"
                + "</head>\n"
                + "<body>\n"
                + "</body></html>";

        final WebDriver driver = loadPage2(html);
        assertTitle(driver, "page1");

        loadPage2(html2, URL_SECOND);
        assertTitle(driver, "page2");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"auto", "manual", "auto", "auto", "auto", "auto"})
    public void scrollRestoration() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head><script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    log(history.scrollRestoration);\n"

                + "    history.scrollRestoration = 'manual';\n"
                + "    log(history.scrollRestoration);\n"

                + "    history.scrollRestoration = 'auto';\n"
                + "    log(history.scrollRestoration);\n"

                + "    history.scrollRestoration = 'MaNUaL';\n"
                + "    log(history.scrollRestoration);\n"

                + "    history.scrollRestoration = 'unknown';\n"
                + "    log(history.scrollRestoration);\n"

                + "    history.scrollRestoration = undefined;\n"
                + "    log(history.scrollRestoration);\n"

                + "  }\n"
                + "</script></head>\n"
                + "<body onload='test()'>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void testHistoryBackAndForwarWithNoStoreCacheControlHeader() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body>"
            + "<a id='startButton' href='" + URL_SECOND + "'>Start</a>\n"
            + "</body></html>";
        final String secondContent = DOCTYPE_HTML
            + "<html><head></head>\n"
            + "<body>\n"
            + "  <a id='nextButton' href='" + URL_THIRD + "'>Next</a>\n"
            + "  <a id='forwardButton' onclick='javascript:window.history.forward()'>Forward</a>\n"
            + "</body></html>";
        final String thirdContent = DOCTYPE_HTML
            + "<html><body>"
            + "<a id='backButton' onclick='javascript:window.history.back()'>Back</a>\n"
            + "</body></html>";

        final List<NameValuePair> headers = new ArrayList<>();
        headers.add(new NameValuePair("Cache-Control", "some-other-value, no-store"));
        getMockWebConnection().setResponse(URL_SECOND, secondContent, 200, "OK", "text/html;charset=ISO-8859-1",
            ISO_8859_1, headers);
        getMockWebConnection().setResponse(URL_THIRD, thirdContent, 200, "OK", "text/html;charset=ISO-8859-1",
            ISO_8859_1, headers);

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("startButton")).click();
        driver.findElement(By.id("nextButton")).click();
        driver.findElement(By.id("backButton")).click();

        assertEquals(URL_SECOND.toString(), driver.getCurrentUrl());
        assertEquals(4, getMockWebConnection().getRequestCount());

        driver.findElement(By.id("forwardButton")).click();
        assertEquals(URL_THIRD.toString(), driver.getCurrentUrl());
        assertEquals(5, getMockWebConnection().getRequestCount());
    }
}
