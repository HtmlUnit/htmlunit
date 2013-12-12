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
package com.gargoylesoftware.htmlunit.libraries;

import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.IE8;
import static org.junit.Assert.assertNotNull;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * <p>Tests for compatibility with <a href="http://tinymce.moxiecode.com/">TinyMCE</a>.</p>
 *
 * <p>TODO: fix "not yet implemented" tests</p>
 * <p>TODO: more tests to add</p>
 * <p>TODO: don't depend on external jQuery</p>
 *
 * @version $Revision$
 * @author Daniel Gredler
 * @author Marc Guillemot
 * @author Frank Danek
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class TinyMceTest extends WebDriverTestCase {

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = { "348", "0" },
            IE11 = { "348", "13" })
    @NotYetImplemented(IE8)
    // TODO [IE11]XML tinymce 3.2.7 is not compatible with IE11
    public void api() throws Exception {
        test("api", Integer.parseInt(getExpectedAlerts()[0]), Integer.parseInt(getExpectedAlerts()[1]));
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = { "89", "0" },
            IE11 = { "89", "4" })
    @NotYetImplemented
    // TODO [IE11]XML tinymce 3.2.7 is not compatible with IE11
    public void basic() throws Exception {
        test("basic", Integer.parseInt(getExpectedAlerts()[0]), Integer.parseInt(getExpectedAlerts()[1]));
    }

    private void test(final String fileName, final int expectedTotal, final int expectedFailed) throws Exception {
        final String url = "http://localhost:" + PORT + "/tests/" + fileName + ".html";
        assertNotNull(url);

        final WebDriver driver = getWebDriver();
        driver.get(url);

        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        final WebElement result = driver.findElement(By.id("testresult"));
        driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);

        final WebElement totalSpan = result.findElement(By.xpath("./span[@class='all']"));
        final int total = Integer.parseInt(totalSpan.getText());
        assertEquals(expectedTotal, total);

        final List<WebElement> failures = driver.findElements(By.xpath("//li[@class='fail']"));
        final StringBuilder msg = new StringBuilder();
        for (WebElement failure : failures) {
            msg.append(failure.getText());
            msg.append("\n\n");
        }

        final WebElement failedSpan = result.findElement(By.xpath("./span[@class='bad']"));
        final int failed = Integer.parseInt(failedSpan.getText());
        Assert.assertEquals(msg.toString(), expectedFailed, failed);
    }

    /**
     * Performs pre-test initialization.
     * @throws Exception if an error occurs
     */
    @Before
    public void setUp() throws Exception {
        startWebServer("src/test/resources/libraries/tinymce/3.2.7", null, null);
    }
}
