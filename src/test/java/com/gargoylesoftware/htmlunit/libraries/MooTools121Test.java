/*
 * Copyright (c) 2002-2014 Gargoyle Software Inc.
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

import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * Tests for compatibility with version 1.2.1 of the <a href="http://mootools.net/">MooTools JavaScript library</a>.
 *
 * @version $Revision$
 * @author Daniel Gredler
 * @author Marc Guillemot
 * @author Frank Danek
 */
@RunWith(BrowserRunner.class)
public class MooTools121Test extends WebDriverTestCase {

    /**
     * @throws Exception if an error occurs
     */
    @Alerts(DEFAULT = { "364", "0", "0" },
            CHROME = { "364", "1", "0",
                    "should return the function bound to an object with multiple arguments" },
            FF17 = { "364", "1", "0",
                    "should return the function bound to an object with multiple arguments" },
            FF24 = { "364", "2", "0",
                    "should return true if the string constains the string and separator otherwise false",
                    "should return the function bound to an object with multiple arguments" },
            IE11 = { "364", "2", "0",
                    "should return the function bound to an object with multiple arguments",
                    "should return a CSS string representing the Element's styles" })
    @Test
    public void mooTools() throws Exception {
        final String resource = "libraries/mootools/1.2.1/Specs/index.html";
        final URL url = getClass().getClassLoader().getResource(resource);
        assertNotNull(url);

        final WebDriver driver = getWebDriver();
        driver.get(url.toExternalForm());

        driver.manage().timeouts().implicitlyWait(60 * DEFAULT_WAIT_TIME_FACTOR, TimeUnit.SECONDS);
        driver.findElement(By.xpath("id('progress')[text() = '100']"));
        // usually this need 40s but sometimes our build machine is slower
        // this is not an performance test, we only like to ensure that all
        // functionality is running

        final List<WebElement> failed = driver.findElements(By.xpath("//li[@class = 'exception']/h4"));
        final List<String> failures = new ArrayList<String>();
        for (final WebElement elt : failed) {
            failures.add(elt.getText());
        }
        FileUtils.writeStringToFile(new File("/tmp/mootols.html"), driver.getPageSource());
        assertEquals(Arrays.copyOfRange(getExpectedAlerts(), 3, getExpectedAlerts().length), failures);

        assertEquals(getExpectedAlerts()[0], driver.findElement(By.id("total_examples")).getText());
        assertEquals(getExpectedAlerts()[1], driver.findElement(By.id("total_failures")).getText());
        assertEquals(getExpectedAlerts()[2], driver.findElement(By.id("total_errors")).getText());
    }
}
