/*
 * Copyright (c) 2002-2015 Gargoyle Software Inc.
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

import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.CHROME;
import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.FF;
import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.IE11;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * Tests for {@link History}.
 *
 * @author Marc Guillemot
 * @author Ahmed Ashour
 * @author Ronald Brill
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
    @Alerts(DEFAULT = { "[object PopStateEvent]", "null" },
            IE8 = "no pushState")
    @NotYetImplemented({ CHROME, FF, IE11 })
    public void pushState() throws Exception {
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
    @Alerts(DEFAULT = "null",
            IE8 = "undefined")
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
            IE8 = "length")
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
}
