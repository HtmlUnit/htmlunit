/*
 * Copyright (c) 2002-2012 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.libraries;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.Browser;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * Tests for compatibility with web server loading of
 * version 1.7.3 of the <a href="http://jquery.com/">jQuery</a> JavaScript library.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class JQuery173GitTest extends WebDriverTestCase {

    static {
        try {
            startWebServer("src/test/resources/libraries/jquery/" + getVersion(), null, null);
        }
        catch(final Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns the jQuery version being tested.
     * @return the jQuery version being tested
     */
    protected static String getVersion() {
        return "1.7.3-git";
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("Unit Testing Environment (0, 2, 2)")
    @NotYetImplemented({ Browser.IE, Browser.FF3_6 })
    public void test_1() throws Exception {
        final WebDriver driver = getWebDriver();
        driver.get("http://localhost:" + PORT + "/test/index.html?testNumber=1");
        final WebElement element = driver.findElement(By.xpath("//ol[@id='qunit-tests']"));
        assertTrue(element.getText().contains(getExpectedAlerts()[0]));
    }

}
