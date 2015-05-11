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
 * @version $Revision$
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class History2Test extends WebDriverTestCase {

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = { "[object PopStateEvent]", "null" },
    		IE8 = { })
    @NotYetImplemented({ CHROME, FF, IE11 })
    public void pushState() throws Exception {
        final String html = "<html><head><script>\n"
                + "  function test() {\n"
        		+ "    if (window.history.pushState) {\n"
                + "      var stateObj = { hi: 'there' };\n"
                + "      window.history.pushState(stateObj, 'page 2', 'bar.html');\n"
                + "    }\n"
                + "  }\n"
                + "\n"
                + "  function popMe(event) {\n"
                + "    var e = event ? event : window.event;\n"
                + "    alert(e);\n"
                + "    alert(e.state);\n"
                + "  }\n"
                + "</script></head><body onpopstate='popMe(event)'>\n"
                + "  <button id=myId onclick='test()'>Click me</button>\n"
                + "</body></html>";

        final String[] expectedAlerts = getExpectedAlerts();
        final WebDriver driver = loadPage2(html);
        if (expectedAlerts.length != 0) {
        	driver.findElement(By.id("myId")).click();
        	assertEquals(URL_FIRST + "bar.html", driver.getCurrentUrl());
        	driver.navigate().back();
        }
        verifyAlerts(driver, expectedAlerts);
    }

}
