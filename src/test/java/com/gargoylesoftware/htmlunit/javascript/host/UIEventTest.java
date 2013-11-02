/*
 * Copyright (c) 2002-2013 Gargoyle Software Inc.
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

import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.IE10;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.BuggyWebDriver;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * Unit tests for {@link UIEvent}.
 *
 * @version $Revision$
 * @author Daniel Gredler
 */
@RunWith(BrowserRunner.class)
public class UIEventTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "DOM2: [object UIEvent]", "DOM3: [object UIEvent]" },
            IE8 = { "DOM2: exception", "DOM3: exception" })
    public void createEvent() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    try {\n"
            + "      alert('DOM2: ' + document.createEvent('UIEvents'));\n"
            + "    } catch(e) {alert('DOM2: exception')}\n"
            + "    try {\n"
            + "      alert('DOM3: ' + document.createEvent('UIEvent'));\n"
            + "    } catch(e) {alert('DOM3: exception')}\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = { "[object UIEvent]", "click", "true", "true", "true", "7" },
            IE8 = "exception")
    public void initUIEvent() throws Exception {
        final String html = "<html><body><script>\n"
            + "try {\n"
            + "  var e = document.createEvent('UIEvents');\n"
            + "  alert(e);\n"
            + "  e.initUIEvent('click', true, true, window, 7);\n"
            + "  alert(e.type);\n"
            + "  alert(e.bubbles);\n"
            + "  alert(e.cancelable);\n"
            + "  alert(e.view == window);\n"
            + "  alert(e.detail);\n"
            + "} catch(e) { alert('exception') }\n"
            + "</script></body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = { "[object Event]", "undefined", "[object MouseEvent]", "1", "[object MouseEvent]", "2" },
            IE8 = { "[object]", "undefined", "[object]", "undefined", "[object]", "undefined" })
    @BuggyWebDriver(IE10)
    // IEDriver has a detail of '3' for the double click but it is '2' when executed manually
    public void detail() throws Exception {
        final String html =
              "<html><head><script>\n"
            + "  function alertDetail(e) {\n"
            + "    alert(e);\n"
            + "    alert(e.detail);\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='alertDetail(event)'>\n"
            + "<div id='a' onclick='alertDetail(event)'>abc</div>\n"
            + "<div id='b' ondblclick='alertDetail(event)'>xyz</div>\n"
            + "</body></html>";
        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("a")).click();
        final Actions action = new Actions(driver);
        action.doubleClick(driver.findElement(By.id("b")));
        action.perform();
        assertEquals(getExpectedAlerts(), getCollectedAlerts(driver));
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = { "[object Event]", "undefined", "[object MouseEvent]", "[object Window]" },
            IE8 = { "[object]", "undefined", "[object]", "undefined" })    public void view() throws Exception {
        final String html =
              "<html><body onload='alertView(event)'><script>\n"
            + "  function alertView(e) {\n"
            + "    alert(e);\n"
            + "    alert(e.view);\n"
            + "  }\n"
            + "</script>\n"
            + "<form><input type='button' id='b' onclick='alertView(event)'></form>\n"
            + "</body></html>";
        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("b")).click();
        assertEquals(getExpectedAlerts(), getCollectedAlerts(driver));
    }
}
