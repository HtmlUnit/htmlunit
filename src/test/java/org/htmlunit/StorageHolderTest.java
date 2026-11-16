/*
 * Copyright (c) 2002-2026 Gargoyle Software Inc.
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
package org.htmlunit;

import java.net.URL;
import java.util.Arrays;
import java.util.Map;

import org.htmlunit.junit.annotation.Alerts;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

/**
 * Tests for {@link StorageHolder}.
 * <p>
 * The local storage of the real browsers survives the tests, therefore every test starts
 * with clearing the storage. The {@link StorageHolder} API is checked if running with HtmlUnit.
 *
 * @author Ronald Brill
 * @author Vidya m
 */
public class StorageHolderTest extends WebDriverTestCase {

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean needThreeConnections() {
        // the second server on PORT2 is used for the other origin
        return true;
    }

    /**
     * @throws Exception in case of error
     */
    @Test
    @Alerts({"1", "Tom", "1", "Tom", "0"})
    public void localStorage() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  localStorage.clear();\n"
            + "  localStorage.setItem('myCat', 'Tom');\n"
            + "  log(localStorage.length);\n"
            + "  log(localStorage.getItem('myCat'));\n"
            + "</script>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        verifyTitle2(driver, Arrays.copyOfRange(getExpectedAlerts(), 0, 2));

        if (driver instanceof HtmlUnitDriver) {
            final Map<String, String> localStorage = getWebClient().getStorageHolder().getLocalStorage(URL_FIRST);
            assertEquals(1, localStorage.size());
            assertEquals("Tom", localStorage.get("myCat"));
        }

        final String html2 = DOCTYPE_HTML
            + "<html><body>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  log(localStorage.length);\n"
            + "  log(localStorage.getItem('myCat'));\n"
            + "  localStorage.clear();\n"
            + "  log(localStorage.length);\n"
            + "</script>\n"
            + "</body></html>";
        getMockWebConnection().setResponse(URL_SECOND, html2);

        driver.get(URL_SECOND.toExternalForm());
        verifyTitle2(driver, Arrays.copyOfRange(getExpectedAlerts(), 2, 5));

        if (driver instanceof HtmlUnitDriver) {
            assertEquals(0, getWebClient().getStorageHolder().getLocalStorage(URL_FIRST).size());
        }
    }

    /**
     * The local storage is per origin, pages from the same host but another port
     * must not see the data.
     * @throws Exception in case of error
     */
    @Test
    @Alerts({"secret", "null"})
    public void localStorageOtherPort() throws Exception {
        final URL otherPortUrl = new URL(URL_CROSS_ORIGIN_BASE, "storage.html");

        final String html = DOCTYPE_HTML
            + "<html><body>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  localStorage.clear();\n"
            + "  localStorage.setItem('token', 'secret');\n"
            + "  log(localStorage.getItem('token'));\n"
            + "</script>\n"
            + "</body></html>";

        final String html2 = DOCTYPE_HTML
            + "<html><body>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  log('' + localStorage.getItem('token'));\n"
            + "</script>\n"
            + "</body></html>";
        getMockWebConnection().setResponse(otherPortUrl, html2);

        final WebDriver driver = loadPage2(html);
        verifyTitle2(driver, getExpectedAlerts()[0]);

        driver.get(otherPortUrl.toExternalForm());
        verifyTitle2(driver, getExpectedAlerts()[1]);

        if (driver instanceof HtmlUnitDriver) {
            final StorageHolder storageHolder = getWebClient().getStorageHolder();
            assertEquals("secret", storageHolder.getLocalStorage(URL_FIRST).get("token"));
            assertEquals(0, storageHolder.getLocalStorage(otherPortUrl).size());
        }
    }

    /**
     * @throws Exception in case of error
     */
    @Test
    @Alerts({"1", "Tom", "1", "Tom", "0"})
    public void sessionStorage() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  sessionStorage.clear();\n"
            + "  sessionStorage.setItem('myCat', 'Tom');\n"
            + "  log(sessionStorage.length);\n"
            + "  log(sessionStorage.getItem('myCat'));\n"
            + "</script>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        verifyTitle2(driver, Arrays.copyOfRange(getExpectedAlerts(), 0, 2));

        if (driver instanceof HtmlUnitDriver) {
            final WebClient webClient = getWebClient();
            final Map<String, String> sessionStorage =
                    webClient.getStorageHolder().getSessionStorage(webClient.getCurrentWindow());
            assertEquals(1, sessionStorage.size());
            assertEquals("Tom", sessionStorage.get("myCat"));
        }

        final String html2 = DOCTYPE_HTML
            + "<html><body>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  log(sessionStorage.length);\n"
            + "  log(sessionStorage.getItem('myCat'));\n"
            + "  sessionStorage.clear();\n"
            + "  log(sessionStorage.length);\n"
            + "</script>\n"
            + "</body></html>";
        getMockWebConnection().setResponse(URL_SECOND, html2);

        driver.get(URL_SECOND.toExternalForm());
        verifyTitle2(driver, Arrays.copyOfRange(getExpectedAlerts(), 2, 5));

        if (driver instanceof HtmlUnitDriver) {
            final WebClient webClient = getWebClient();
            assertEquals(0, webClient.getStorageHolder().getSessionStorage(webClient.getCurrentWindow()).size());
        }
    }
}