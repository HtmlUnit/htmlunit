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
package com.gargoylesoftware.htmlunit.javascript.host;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * Tests for {@link History}.
 *
 * @author Marc Guillemot
 * @author Ahmed Ashour
 * @author Ronald Brill
 * @author Adam Afeltowicz
 * @author Carsten Steul
 */
@RunWith(BrowserRunner.class)
public class History2Test extends WebDriverTestCase {

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("here")
    public void goShouldIgnoreOutOfBoundIndex() throws Exception {
        final String html = "<html><body><script>"
                + "history.go(1);\n"
                + "alert('here');\n"
                + "</script></body></html>";

        loadPageWithAlerts2(html);
        assertEquals(1, getMockWebConnection().getRequestCount());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({ "[object PopStateEvent]", "null" })
    public void pushStateSimple() throws Exception {
        final String html = "<html>\n"
                + "<head>\n"
                + "<title></title>\n"
                + "<script>\n"
                + "  function test() {\n"
                + "    if (!window.history.pushState) { alert('no pushState'); return }\n"
                + "    var stateObj = { hi: 'there' };\n"
                + "    window.history.pushState(stateObj, 'page 2', 'bar.html');\n"
                + "  }\n"

                + "  function popMe(event) {\n"
                + "    var e = event ? event : window.event;\n"
                + "    alert(e);\n"
                + "    alert(e.state);\n"
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
        verifyAlerts(driver, expectedAlerts);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({ "[object PopStateEvent]", "{\"hi\":\"there\"}",
                "[object PopStateEvent]", "{\"hi\":\"there\"}",
                "[object PopStateEvent]", "null",
                "[object PopStateEvent]", "null",
                "[object PopStateEvent]", "{\"hi\":\"there\"}",
                "[object PopStateEvent]", "{\"hi\":\"there\"}",
                "[object PopStateEvent]", "{\"hi2\":\"there2\"}",
                "[object PopStateEvent]", "{\"hi2\":\"there2\"}" })
    public void pushState() throws Exception {
        final String html = "<html>\n"
                + "<head>\n"
                + "<script>\n"
                + "  function test() {\n"
                + "    if (window.history.pushState) {\n"
                + "      var stateObj = { hi: 'there' };\n"
                + "      window.history.pushState(stateObj, 'page 2', 'bar.html');\n"
                + "    }\n"
                + "  }\n"

                + "  function test2() {\n" + "    if (window.history.pushState) {\n"
                + "      var stateObj = { hi2: 'there2' };\n"
                + "      window.history.pushState(stateObj, 'page 3', 'bar2.html');\n"
                + "    }\n"
                + "  }\n"

                + "  function popMe(event) {\n"
                + "    var e = event ? event : window.event;\n"
                + "    alert(e);\n"
                + "    alert(JSON.stringify(e.state));\n"
                + "  }\n"

                + "  function setWindowName() {\n"
                + "    window.name = window.name + 'a';\n"
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
        final WebDriver driver = loadPage2(html);
        assertEquals("a", ((JavascriptExecutor) driver).executeScript("return window.name"));

        final long start = (Long) ((JavascriptExecutor) driver).executeScript("return window.history.length");

        driver.findElement(By.id("myId")).click();
        assertEquals("a", ((JavascriptExecutor) driver).executeScript("return window.name"));
        assertEquals(start + 1, ((JavascriptExecutor) driver).executeScript("return window.history.length"));
        assertEquals(URL_FIRST + "bar.html", driver.getCurrentUrl());
        driver.findElement(By.id("myId2")).click();
        assertEquals("a", ((JavascriptExecutor) driver).executeScript("return window.name"));
        assertEquals(start + 2, ((JavascriptExecutor) driver).executeScript("return window.history.length"));
        assertEquals(URL_FIRST + "bar2.html", driver.getCurrentUrl());
        driver.navigate().back();
        assertEquals("a", ((JavascriptExecutor) driver).executeScript("return window.name"));
        assertEquals(start + 2, ((JavascriptExecutor) driver).executeScript("return window.history.length"));
        assertEquals(URL_FIRST + "bar.html", driver.getCurrentUrl());
        driver.navigate().back();
        assertEquals("a", ((JavascriptExecutor) driver).executeScript("return window.name"));
        assertEquals(start + 2, ((JavascriptExecutor) driver).executeScript("return window.history.length"));
        assertEquals(URL_FIRST.toString(), driver.getCurrentUrl());
        driver.navigate().forward();
        assertEquals("a", ((JavascriptExecutor) driver).executeScript("return window.name"));
        assertEquals(start + 2, ((JavascriptExecutor) driver).executeScript("return window.history.length"));
        assertEquals(URL_FIRST + "bar.html", driver.getCurrentUrl());
        driver.navigate().forward();
        assertEquals("a", ((JavascriptExecutor) driver).executeScript("return window.name"));
        assertEquals(start + 2, ((JavascriptExecutor) driver).executeScript("return window.history.length"));
        assertEquals(URL_FIRST + "bar2.html", driver.getCurrentUrl());

        assertEquals(1, getMockWebConnection().getRequestCount());
        verifyAlerts(driver, expectedAlerts);

        // because we have changed the window name
        shutDownAll();
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = { "[object PopStateEvent]", "{\"hi\":\"there\"}", "true",
                        "[object PopStateEvent]", "{\"hi\":\"there\"}", "true",
                        "[object PopStateEvent]", "null", "true",
                        "[object PopStateEvent]", "null", "true",
                        "[object PopStateEvent]", "{\"hi\":\"there\"}", "true",
                        "[object PopStateEvent]", "{\"hi\":\"there\"}", "true",
                        "[object PopStateEvent]", "{\"hi2\":\"there2\"}", "true",
                        "[object PopStateEvent]", "{\"hi2\":\"there2\"}", "true" },
            CHROME = {  "[object PopStateEvent]", "{\"hi\":\"there\"}", "false",
                        "[object PopStateEvent]", "{\"hi\":\"there\"}", "false",
                        "[object PopStateEvent]", "null", "true",
                        "[object PopStateEvent]", "null", "true",
                        "[object PopStateEvent]", "{\"hi\":\"there\"}", "false",
                        "[object PopStateEvent]", "{\"hi\":\"there\"}", "false",
                        "[object PopStateEvent]", "{\"hi2\":\"there2\"}", "false",
                        "[object PopStateEvent]", "{\"hi2\":\"there2\"}", "false" },
            IE = {    "[object PopStateEvent]", "{\"hi\":\"there\"}", "false",
                        "[object PopStateEvent]", "{\"hi\":\"there\"}", "false",
                        "[object PopStateEvent]", "null", "true",
                        "[object PopStateEvent]", "null", "true",
                        "[object PopStateEvent]", "{\"hi\":\"there\"}", "false",
                        "[object PopStateEvent]", "{\"hi\":\"there\"}", "false",
                        "[object PopStateEvent]", "{\"hi2\":\"there2\"}", "false",
                        "[object PopStateEvent]", "{\"hi2\":\"there2\"}", "false" })
    public void pushStateClone() throws Exception {
        final String html = "<html>\n"
                + "<head>\n"
                + "<script>\n"
                + "  function test() {\n"
                + "    if (window.history.pushState) {\n"
                + "      var stateObj = { hi: 'there' };\n"
                + "      window.history.pushState(stateObj, 'page 2', 'bar.html');\n"
                + "    }\n"
                + "  }\n"

                + "  function test2() {\n" + "    if (window.history.pushState) {\n"
                + "      var stateObj = { hi2: 'there2' };\n"
                + "      window.history.pushState(stateObj, 'page 3', 'bar2.html');\n"
                + "    }\n"
                + "  }\n"

                + "  function popMe(event) {\n"
                + "    var e = event ? event : window.event;\n"
                + "    alert(e);\n"
                + "    alert(JSON.stringify(e.state));\n"
                + "    alert(e.state == history.state);\n"
                + "  }\n"

                + "  function setWindowName() {\n"
                + "    window.name = window.name + 'a';\n"
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
        final WebDriver driver = loadPage2(html);
        assertEquals("a", ((JavascriptExecutor) driver).executeScript("return window.name"));

        final long start = (Long) ((JavascriptExecutor) driver).executeScript("return window.history.length");

        if (expectedAlerts.length != 0) {
            driver.findElement(By.id("myId")).click();
            assertEquals("a", ((JavascriptExecutor) driver).executeScript("return window.name"));
            assertEquals(start + 1, ((JavascriptExecutor) driver).executeScript("return window.history.length"));
            assertEquals(URL_FIRST + "bar.html", driver.getCurrentUrl());
            driver.findElement(By.id("myId2")).click();
            assertEquals("a", ((JavascriptExecutor) driver).executeScript("return window.name"));
            assertEquals(start + 2, ((JavascriptExecutor) driver).executeScript("return window.history.length"));
            assertEquals(URL_FIRST + "bar2.html", driver.getCurrentUrl());
            driver.navigate().back();
            assertEquals("a", ((JavascriptExecutor) driver).executeScript("return window.name"));
            assertEquals(start + 2, ((JavascriptExecutor) driver).executeScript("return window.history.length"));
            assertEquals(URL_FIRST + "bar.html", driver.getCurrentUrl());
            driver.navigate().back();
            assertEquals("a", ((JavascriptExecutor) driver).executeScript("return window.name"));
            assertEquals(start + 2, ((JavascriptExecutor) driver).executeScript("return window.history.length"));
            assertEquals(URL_FIRST.toString(), driver.getCurrentUrl());
            driver.navigate().forward();
            assertEquals("a", ((JavascriptExecutor) driver).executeScript("return window.name"));
            assertEquals(start + 2, ((JavascriptExecutor) driver).executeScript("return window.history.length"));
            assertEquals(URL_FIRST + "bar.html", driver.getCurrentUrl());
            driver.navigate().forward();
            assertEquals("a", ((JavascriptExecutor) driver).executeScript("return window.name"));
            assertEquals(start + 2, ((JavascriptExecutor) driver).executeScript("return window.history.length"));
            assertEquals(URL_FIRST + "bar2.html", driver.getCurrentUrl());
        }

        assertEquals(1, getMockWebConnection().getRequestCount());
        verifyAlerts(driver, expectedAlerts);

        // because we have changed the window name
        shutDownAll();
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"true", "true"})
    public void pushStateLocationHref() throws Exception {
        final String html = "<html>\n"
                + "<head>\n"
                + "<script>\n"
                + "  function test() {\n"
                + "    if (!window.history.pushState) { alert('no pushState'); return }\n"
                + "    try {\n"
                + "      var stateObj = { hi: 'there' };\n"
                + "      window.history.pushState(stateObj, 'page 2', 'bar.html');\n"
                + "      alert(location.href.indexOf('bar.html') > -1);\n"
                + "    } catch(e) { alert('exception'); }\n"
                + "  }\n"

                + "  function test2() {\n"
                + "    if (!window.history.pushState) { alert('no pushState'); return }\n"
                + "    try {\n"
                + "      var stateObj = { hi2: 'there2' };\n"
                + "      window.history.pushState(stateObj, 'page 3', 'bar2.html');\n"
                + "      alert(location.href.indexOf('bar2.html') > -1);\n"
                + "    } catch(e) { alert('exception'); }\n"
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
        assertEquals(URL_FIRST + "bar.html", driver.getCurrentUrl());
        assertEquals(URL_FIRST + "bar.html", ((JavascriptExecutor) driver).executeScript("return location.href"));
        driver.findElement(By.id("myId2")).click();
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

        verifyAlerts(driver, expectedAlerts);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({ "[object PopStateEvent]", "null",
                "[object PopStateEvent]", "null",
                "[object PopStateEvent]", "{\"hi2\":\"there2\"}",
                "[object PopStateEvent]", "{\"hi2\":\"there2\"}" })
    public void replaceState() throws Exception {
        final String html = "<html>\n"
                + "<head>\n"
                + "<script>\n"
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
                + "    alert(e);\n"
                + "    alert(JSON.stringify(e.state));\n"
                + "  }\n"

                + "  function setWindowName() {\n"
                + "    window.name = window.name + 'a';\n"
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
        final WebDriver driver = loadPage2(html);

        assertEquals("a", ((JavascriptExecutor) driver).executeScript("return window.name"));
        final long start = (Long) ((JavascriptExecutor) driver).executeScript("return window.history.length");

        if (expectedAlerts.length != 0) {
            driver.findElement(By.id("myId")).click();
            assertEquals("a", ((JavascriptExecutor) driver).executeScript("return window.name"));
            assertEquals(start + 1, ((JavascriptExecutor) driver).executeScript("return window.history.length"));
            assertEquals(URL_FIRST + "bar.html", driver.getCurrentUrl());
            driver.findElement(By.id("myId2")).click();
            assertEquals("a", ((JavascriptExecutor) driver).executeScript("return window.name"));
            assertEquals(start + 1, ((JavascriptExecutor) driver).executeScript("return window.history.length"));
            assertEquals(URL_FIRST + "bar2.html", driver.getCurrentUrl());
            driver.navigate().back();
            assertEquals("a", ((JavascriptExecutor) driver).executeScript("return window.name"));
            assertEquals(start + 1, ((JavascriptExecutor) driver).executeScript("return window.history.length"));
            assertEquals(URL_FIRST.toString(), driver.getCurrentUrl());
            driver.navigate().forward();
            assertEquals("a", ((JavascriptExecutor) driver).executeScript("return window.name"));
            assertEquals(start + 1, ((JavascriptExecutor) driver).executeScript("return window.history.length"));
            assertEquals(URL_FIRST + "bar2.html", driver.getCurrentUrl());
        }

        assertEquals(1, getMockWebConnection().getRequestCount());
        verifyAlerts(driver, expectedAlerts);

        // because we have changed the window name
        shutDownAll();
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = { "[object PopStateEvent]", "null", "true",
                        "[object PopStateEvent]", "null", "true",
                        "[object PopStateEvent]", "{\"hi2\":\"there2\"}", "true",
                        "[object PopStateEvent]", "{\"hi2\":\"there2\"}", "true" },
            CHROME = {  "[object PopStateEvent]", "null", "true",
                        "[object PopStateEvent]", "null", "true",
                        "[object PopStateEvent]", "{\"hi2\":\"there2\"}", "false",
                        "[object PopStateEvent]", "{\"hi2\":\"there2\"}", "false" },
            IE = {    "[object PopStateEvent]", "null", "true",
                        "[object PopStateEvent]", "null", "true",
                        "[object PopStateEvent]", "{\"hi2\":\"there2\"}", "false",
                        "[object PopStateEvent]", "{\"hi2\":\"there2\"}", "false" })
    public void replaceStateClone() throws Exception {
        final String html = "<html>\n"
                + "<head>\n"
                + "<script>\n"
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
                + "    alert(e);\n"
                + "    alert(JSON.stringify(e.state));\n"
                + "    alert(e.state == history.state);\n"
                + "  }\n"

                + "  function setWindowName() {\n"
                + "    window.name = window.name + 'a';\n"
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
        final WebDriver driver = loadPage2(html);

        assertEquals("a", ((JavascriptExecutor) driver).executeScript("return window.name"));
        final long start = (Long) ((JavascriptExecutor) driver).executeScript("return window.history.length");

        if (expectedAlerts.length != 0) {
            driver.findElement(By.id("myId")).click();
            assertEquals("a", ((JavascriptExecutor) driver).executeScript("return window.name"));
            assertEquals(start + 1, ((JavascriptExecutor) driver).executeScript("return window.history.length"));
            assertEquals(URL_FIRST + "bar.html", driver.getCurrentUrl());
            driver.findElement(By.id("myId2")).click();
            assertEquals("a", ((JavascriptExecutor) driver).executeScript("return window.name"));
            assertEquals(start + 1, ((JavascriptExecutor) driver).executeScript("return window.history.length"));
            assertEquals(URL_FIRST + "bar2.html", driver.getCurrentUrl());
            driver.navigate().back();
            assertEquals("a", ((JavascriptExecutor) driver).executeScript("return window.name"));
            assertEquals(start + 1, ((JavascriptExecutor) driver).executeScript("return window.history.length"));
            assertEquals(URL_FIRST.toString(), driver.getCurrentUrl());
            driver.navigate().forward();
            assertEquals("a", ((JavascriptExecutor) driver).executeScript("return window.name"));
            assertEquals(start + 1, ((JavascriptExecutor) driver).executeScript("return window.history.length"));
            assertEquals(URL_FIRST + "bar2.html", driver.getCurrentUrl());
        }

        assertEquals(1, getMockWebConnection().getRequestCount());
        verifyAlerts(driver, expectedAlerts);

        // because we have changed the window name
        shutDownAll();
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void length() throws Exception {
        final String second = "<html>\n"
                + "<head></head>\n"
                + "<body>\n"
                + "<a name='length' href='' onclick='alert(history.length);return false;'>length</a><br>\n"
                + "<a name='x' href='#x'>x</a><br>\n"
                + "</body></html>\n";

        getMockWebConnection().setResponse(URL_SECOND, second);

        final String html = "<html>\n"
                + "<head></head>\n"
                + "<body>\n"
                + "<a name='length' href='' onclick='alert(history.length);return false;'>length</a><br>\n"
                + "<a name='b' href='" + URL_SECOND.toExternalForm() + "'>b</a><br>\n"
                + "</body></html>\n";

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.name("length")).click();

        // when testing with real browsers we are facing different offsets
        final int start = Integer.parseInt(getCollectedAlerts(driver).get(0));

        driver.findElement(By.name("b")).click();
        driver.findElement(By.name("length")).click();

        driver.findElement(By.name("x")).click();
        driver.findElement(By.name("length")).click();

        assertEquals(new String[] {"" + (start + 1), "" + (start + 2)}, getCollectedAlerts(driver));
    }

    /**
     * History.previous was defined in old FF versions.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("undefined")
    public void previous() throws Exception {
        final String html = "<html>\n"
                + "<head></head>\n"
                + "<body>\n"
                + "<a name='itemZero' href='' onclick='alert(history.previous); return false;'>item zero</a>\n"
                + "</body></html>\n";

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.name("itemZero")).click();

        verifyAlerts(driver, getExpectedAlerts());
    }

    /**
     * History.current was defined in old FF versions.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("undefined")
    public void current() throws Exception {
        final String html = "<html>\n"
                + "<head></head>\n"
                + "<body>\n"
                + "<a name='itemZero' href='' onclick='alert(history.current); return false;'>item zero</a>\n"
                + "</body></html>\n";

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.name("itemZero")).click();

        verifyAlerts(driver, getExpectedAlerts());
    }

    /**
     * History.next was defined in old FF versions.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("undefined")
    public void next() throws Exception {
        final String html = "<html>\n"
                + "<head></head>\n"
                + "<body>\n"
                + "<a name='itemZero' href='' onclick='alert(history.next); return false;'>item zero</a>\n"
                + "</body></html>\n";

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.name("itemZero")).click();

        verifyAlerts(driver, getExpectedAlerts());
    }

    /**
     * History.item was defined in old FF versions.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("undefined")
    public void item() throws Exception {
        final String html = "<html>\n"
                + "<head></head>\n"
                + "<body>\n"
                + "<a name='itemZero' href='' onclick='alert(history.item); return false;'>item zero</a>\n"
                + "</body></html>\n";

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.name("itemZero")).click();

        verifyAlerts(driver, getExpectedAlerts());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({ "false", "false", "false", "false", "false", "false" })
    public void byIndex() throws Exception {
        final String html = "<html>\n"
                + "<head></head>\n"
                + "<body>\n"
                + "<a name='hasNegativeOne' href='' onclick="
                    + "'alert(\"-1\" in history);alert(-1 in history);return false;'>has negative one</a><br>\n"
                + "<a name='hasZero' href='' onclick="
                    + "'alert(\"0\" in history);alert(0 in history);return false;'>has zero</a><br>\n"
                + "<a name='hasPositiveOne' href='' onclick="
                    + "'alert(\"1\" in history);alert(1 in history);return false;'>has positive one</a><br>\n"
                + "</body></html>\n";

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.name("hasNegativeOne")).click();
        driver.findElement(By.name("hasZero")).click();
        driver.findElement(By.name("hasPositiveOne")).click();

        verifyAlerts(driver, getExpectedAlerts());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("undefined")
    public void arrayAccess() throws Exception {
        final String html = "<html>\n"
                + "<head></head>\n"
                + "<body>\n"
                + "<a name='arrayAccess' href='' onclick='alert(history[0]);return false;'>array access</a><br>\n"
                + "</body></html>\n";

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.name("arrayAccess")).click();

        verifyAlerts(driver, getExpectedAlerts());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("null")
    public void state() throws Exception {
        final String html = "<html><head><script>\n"
                + "  function test() {\n"
                + "    alert(history.state);\n"
                + "  }\n"
                + "</script></head>\n"
                + "<body onload='test()'>\n"
                + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = { "back", "forward", "go", "length", "pushState", "replaceState", "state" },
            CHROME = { "back", "forward", "go", "length", "pushState", "replaceState",
                        "scrollRestoration", "state" })
    public void properties() throws Exception {
        final String html = "<html><head><script>\n"
                + "  function test() {\n"
                + "    var props = [];\n"
                + "    var i = 0;\n"
                + "    for (prop in window.history) {\n"
                + "      props[i++] = prop;\n"
                + "    }\n"
                + "    props.sort();\n"
                + "    for (i = 0; i < props.length; i++) {\n"
                + "      alert(props[i]);\n"
                + "    }\n"
                + "  }\n"
                + "</script></head>\n"
                + "<body onload='test()'>\n"
                + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * Test that a new page loads after history.pushState() is called.
     * @throws Exception if test fails
     */
    @Test
    public void loadPageAfterPushState() throws Exception {
        final String html = "<html>\n"
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
        final String html2 = "<html>\n"
                + "<head>\n"
                + "<title>page2</title>\n"
                + "</head>\n"
                + "<body>\n"
                + "</body></html>";

        final WebDriver driver = loadPage2(html);
        assertEquals("page1", driver.getTitle());

        loadPage2(html2, URL_SECOND);
        assertEquals("page2", driver.getTitle());
    }
}
