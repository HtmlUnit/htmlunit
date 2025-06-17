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
package org.htmlunit.libraries;

import java.util.List;

import org.htmlunit.Page;
import org.htmlunit.WebClient;
import org.htmlunit.WebDriverTestCase;
import org.htmlunit.html.HtmlPage;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

/**
 * Tests for <a href="https://htmx.org/">htmx</a>.
 *
 * @author Ronald Brill
 */
public abstract class HtmxTest extends WebDriverTestCase {

    private static final boolean BUG_HUNTING = false;

    private static final int RETRIES = 2;
    private static final long RUN_TIME = 42 * DEFAULT_WAIT_TIME.toMillis();

    protected void htmx(final String subDir) throws Exception {
        startWebServer("src/test/resources/libraries/htmx/" + subDir, null, null);

        try {
            final String url = URL_FIRST + "test/index.html";
            final WebDriver webDriver = getWebDriver();

            if (webDriver instanceof HtmlUnitDriver) {
                setupWebClient(((HtmlUnitDriver) webDriver).getWebClient());
            }

            int tries = 0;
            String lastStats = "";
            webDriver.get(url);
            long endTime = System.currentTimeMillis() + RUN_TIME;

            while (true) {
                lastStats = getResultElementText(webDriver);
                if (lastStats.startsWith(getExpectedAlerts()[0])) {
                    break;
                }
                Thread.sleep(100);

                if (System.currentTimeMillis() > endTime) {
                    tries++;

                    if (tries < RETRIES) {
                        lastStats = "";
                        webDriver.get(url);
                        endTime = System.currentTimeMillis() + RUN_TIME;
                    }
                    else {
                        lastStats = "HtmxTest runs too long (longer than " + RUN_TIME / 1000 + "s) - "
                                + getResultElementText(webDriver);
                        break;
                    }
                }
            }

            if (BUG_HUNTING && getWebDriver() instanceof HtmlUnitDriver) {
                final WebClient webClient = ((HtmlUnitDriver) getWebDriver()).getWebClient();

                final Page page = webClient.getCurrentWindow().getEnclosedPage();
                System.out.println(((HtmlPage) page).asNormalizedText());
            }

            assertTrue(lastStats + "\n\n" + getErrors(webDriver), lastStats.startsWith(getExpectedAlerts()[0]));
        }
        catch (final Exception e) {
            if (BUG_HUNTING && getWebDriver() instanceof HtmlUnitDriver) {
                e.printStackTrace();
                Throwable t = e;
                while ((t = t.getCause()) != null) {
                    t.printStackTrace();
                }
            }
            throw e;
        }
    }

    protected void setupWebClient(final WebClient webClient) {
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
