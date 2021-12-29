/*
 * Copyright (c) 2002-2021 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.libraries;

import static org.junit.Assert.fail;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.junit.BrowserRunner;
import com.gargoylesoftware.htmlunit.junit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.junit.BrowserRunner.HtmlUnitNYI;

/**
 * Tests for <a href="https://htmx.org/">htmx</a>.
 *
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class HtmxTest extends WebDriverTestCase {

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "passes:394failures:0",
            IE = "passes:17failures:378")
    @HtmlUnitNYI(CHROME = "passes:380failures:15",
            EDGE = "passes:380failures:15",
            FF = "passes:380failures:15",
            FF_ESR = "passes:380failures:15",
            IE = "passes:16failures:379")
    public void htmx() throws Exception {
        startWebServer("src/test/resources/libraries/htmx/htmx-1.5.0", null, null);


        final long runTime = 40 * DEFAULT_WAIT_TIME;
        final long endTime = System.currentTimeMillis() + runTime;

        try {
            final WebDriver webDriver = getWebDriver();

            if (getWebDriver() instanceof HtmlUnitDriver) {
                getWebWindowOf((HtmlUnitDriver) getWebDriver()).getWebClient()
                    .getOptions().setThrowExceptionOnScriptError(false);
            }

            final String url = URL_FIRST + "test/index.html";
            webDriver.get(url);

            String lastStats = "";
            while (lastStats.length() == 0
                    || !lastStats.startsWith(getExpectedAlerts()[0])) {
                Thread.sleep(100);

                if (System.currentTimeMillis() > endTime) {
                    fail("HtmxTest runs too long (longer than " + runTime / 1000 + "s)");
                }

                lastStats = getResultElementText(webDriver);
            }

            assertTrue(lastStats, lastStats.startsWith(getExpectedAlerts()[0]));

            /* bug hunting
            if (getWebDriver() instanceof HtmlUnitDriver) {
                final WebClient webClient = getWebWindowOf((HtmlUnitDriver) getWebDriver()).getWebClient();

                final Page page = webClient.getCurrentWindow().getEnclosedPage();
                System.out.println(((HtmlPage) page).asNormalizedText());
            }
            */
        }
        catch (final Exception e) {
            e.printStackTrace();
            Throwable t = e;
            while ((t = t.getCause()) != null) {
                t.printStackTrace();
            }
            throw e;
        }
    }

    private static String getResultElementText(final WebDriver webdriver) {
        // if the elem is not available or stale we return an empty string
        // this will force a second try
        try {
            final WebElement elem = webdriver.findElement(By.cssSelector("#mocha-stats"));
            try {
                String result = elem.getText();
                result = result.replaceAll("\\s", "");
                return result;
            }
            catch (final StaleElementReferenceException e) {
                return "";
            }
        }
        catch (final NoSuchElementException e) {
            return "";
        }
    }

}
