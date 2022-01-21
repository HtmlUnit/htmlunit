/*
 * Copyright (c) 2002-2022 Gargoyle Software Inc.
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

import java.util.List;

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
    @Alerts(DEFAULT = "passes:412failures:0",
            IE = "passes:17failures:396")
    @HtmlUnitNYI(CHROME = "passes:408failures:4",
            EDGE = "passes:408failures:4",
            FF = "passes:408failures:4",
            FF_ESR = "passes:408failures:4",
            IE = "passes:16failures:397")
    public void htmx() throws Exception {
        startWebServer("src/test/resources/libraries/htmx/htmx-1.6.1", null, null);

        try {
            htmxRun(1);
        }
        catch (final Exception e) {
            // second try
            htmxRun(2);
        }
    }

    private void htmxRun(final int tryCount) throws Exception {
        final long runTime = 42 * DEFAULT_WAIT_TIME;
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
                    lastStats = "HtmxTest " + tryCount + " runs too long (longer than " + runTime / 1000 + "s) - "
                            + getResultElementText(webDriver);
                    break;
                }

                lastStats = getResultElementText(webDriver);
            }

            // bug hunting
            /*
            if (getWebDriver() instanceof HtmlUnitDriver) {
                final WebClient webClient = getWebWindowOf((HtmlUnitDriver) getWebDriver()).getWebClient();

                final Page page = webClient.getCurrentWindow().getEnclosedPage();
                System.out.println(((HtmlPage) page).asNormalizedText());
            }
            */

            assertTrue(lastStats + "\n\n" + getErrors(webDriver), lastStats.startsWith(getExpectedAlerts()[0]));
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

    private static String getErrors(final WebDriver webdriver) {
        final StringBuilder result = new StringBuilder();

        try {
            final List<WebElement> elements = webdriver.findElements(By.tagName("li"));
            for (final WebElement elem : elements) {
                final String cssClass = elem.getAttribute("class");
                if (cssClass != null && cssClass.contains(" fail")) {
                    result.append(elem.getText())
                        .append("\n-------------------------\n\n");
                }
            }
            return result.toString();
        }
        catch (final Exception e) {
            return e.getMessage();
        }
    }

}
