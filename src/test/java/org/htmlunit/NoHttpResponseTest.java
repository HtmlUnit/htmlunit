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
package org.htmlunit;

import java.net.URL;
import java.nio.charset.StandardCharsets;

import org.htmlunit.html.HtmlPage;
import org.htmlunit.junit.annotation.Alerts;
import org.htmlunit.junit.annotation.HtmlUnitNYI;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;

/**
 * Tests for handling HttpClient's {@link org.apache.http.NoHttpResponseException}.
 *
 * @author Marc Guillemot
 * @author Frank Danek
 * @author Ronald Brill
 */
public class NoHttpResponseTest {
    private static final String HTML
        = "<html><body><script>\n"
        + "  function fillField() {\n"
        + "    document.forms.loginform.textfield.value = 'new value';\n"
        + "  }\n"
        + "</script>\n"
        + "<form name='loginform' action='page2' method='get'>\n"
        + "  <input type='text' name='textfield' value='' />\n"

        + "  <input type='submit' onclick='this.form.submit(); fillField(); return true;' id='jsSubmit'/>\n"
        + "  <input type='submit' id='inputSubmit'>Submit</input>\n"
        + "</form>\n"
        + "</body></html>";

    /**
     * Test using WebDriver.
     */
    @Nested
    public class WithWebDriverTest extends WebDriverTestCase {

        /**
         * Resets the {@link MiniServer}.
         *
         * @throws Exception in case of error
         */
        @AfterEach
        public void after() throws Exception {
            MiniServer.resetDropRequests();
        }

        /**
         * @throws Exception if the test fails
         */
        @Test
        @Alerts(DEFAULT = "§§URL§§page2?textfield=",
                FF = "WebDriverException",
                FF_ESR = "WebDriverException")
        @HtmlUnitNYI(FF = "§§URL§§page2?textfield=",
                FF_ESR = "§§URL§§page2?textfield=")
        public void submit() throws Exception {
            final MockWebConnection mockWebConnection = getMockWebConnection();
            mockWebConnection.setResponse(URL_FIRST, HTML);
            MiniServer.configureDropRequest(new URL(URL_FIRST, "page2?textfield="));
            final URL urlRightSubmit = new URL(URL_FIRST, "page2?textfield=new+value");
            mockWebConnection.setResponse(urlRightSubmit,
                    DOCTYPE_HTML + "<html><head><title>right submit</title></head></html>");

            expandExpectedAlertsVariables(URL_FIRST);
            final WebDriver driver = getWebDriver();

            try (MiniServer miniServer = new MiniServer(PORT, mockWebConnection)) {
                miniServer.start();

                driver.get(URL_FIRST.toString());
                driver.findElement(By.id("inputSubmit")).click();
                if (useRealBrowser()) {
                    Thread.sleep(400);
                }
                assertEquals(getExpectedAlerts()[0], driver.getCurrentUrl());
            }
            catch (final WebDriverException e) {
                assertEquals(getExpectedAlerts()[0], "WebDriverException");
            }
        }

        /**
         * @throws Exception if the test fails
         */
        @Test
        @Alerts("right submit")
        public void callSubmitInButtonAndReturnTrue() throws Exception {
            final MockWebConnection mockWebConnection = getMockWebConnection();
            mockWebConnection.setResponse(URL_FIRST, HTML);
            MiniServer.configureDropRequest(new URL(URL_FIRST, "page2?textfield="));
            final URL urlRightSubmit = new URL(URL_FIRST, "page2?textfield=new+value");
            mockWebConnection.setResponse(urlRightSubmit,
                    DOCTYPE_HTML + "<html><head><title>right submit</title></head></html>");

            final WebDriver driver = getWebDriver();

            try (MiniServer miniServer = new MiniServer(PORT, mockWebConnection)) {
                miniServer.start();

                driver.get(URL_FIRST.toString());
                driver.findElement(By.id("jsSubmit")).click();
                assertTitle(driver, getExpectedAlerts()[0]);
            }
        }
    }

    /**
     * Test using WebClient with default configuration allowing to throw exception.
     */
    @Nested
    public class WithWebClientTest extends SimpleWebTestCase {

        /**
         * Resets the {@link MiniServer}.
         *
         * @throws Exception in case of error
         */
        @AfterEach
        public void after() throws Exception {
            MiniServer.resetDropRequests();
        }

        /**
         * @throws Throwable if the test fails
         */
        @Test
        @Alerts("§§URL§§")
        public void submit() throws Throwable {
            expandExpectedAlertsVariables(URL_FIRST);

            final MockWebConnection mockWebConnection = getMockWebConnection();
            mockWebConnection.setResponse(URL_FIRST, HTML);

            MiniServer.configureDropRequest(new URL(URL_FIRST, "page2?textfield="));
            final URL urlRightSubmit = new URL(URL_FIRST, "page2?textfield=new+value");
            mockWebConnection.setResponse(urlRightSubmit,
                    DOCTYPE_HTML + "<html><head><title>right submit</title></head></html>");

            expandExpectedAlertsVariables(URL_FIRST);

            try (MiniServer miniServer = new MiniServer(PORT, mockWebConnection)) {
                miniServer.start();

                final HtmlPage page = getWebClient().getPage(URL_FIRST);

                Assertions.assertThrows(FailingHttpStatusCodeException.class,
                            () -> page.getElementById("inputSubmit").click());

                assertEquals(getExpectedAlerts()[0], page.getUrl());
            }
        }

        /**
         * @throws Throwable if the test fails
         */
        @Test
        public void callSubmitInButtonAndReturnTrue() throws Throwable {
            final MockWebConnection mockWebConnection = getMockWebConnection();
            mockWebConnection.setResponse(URL_FIRST, HTML);
            MiniServer.configureDropRequest(new URL(URL_FIRST, "page2?textfield="));
            final URL urlRightSubmit = new URL(URL_FIRST, "page2?textfield=new+value");
            mockWebConnection.setResponse(urlRightSubmit,
                    DOCTYPE_HTML + "<html><head><title>right submit</title></head></html>");

            try (MiniServer miniServer = new MiniServer(PORT, mockWebConnection)) {
                miniServer.start();

                HtmlPage page = getWebClient().getPage(URL_FIRST);
                page = page.getElementById("jsSubmit").click();
                assertEquals("right submit", page.getTitleText());
            }
        }

        /**
         * @throws Exception if the test fails
         */
        @Test
        public void htmlUnitDriverUsesGetPage() throws Exception {
            final MockWebConnection mockWebConnection = getMockWebConnection();
            MiniServer.configureDropRequest(URL_FIRST);

            try (MiniServer miniServer = new MiniServer(PORT, mockWebConnection)) {
                miniServer.start();

                final WebRequest request = new WebRequest(new URL(URL_FIRST.toString()),
                        getBrowserVersion().getHtmlAcceptHeader(), getBrowserVersion().getAcceptEncodingHeader());
                request.setCharset(StandardCharsets.UTF_8);

                try {
                    getWebClient().getPage(getWebClient().getCurrentWindow().getTopWindow(), request);
                    Assertions.fail("FailingHttpStatusCodeException expected");
                }
                catch (final FailingHttpStatusCodeException e) {
                    // expected
                    assertEquals(0, e.getStatusCode());
                    assertEquals("0 No HTTP Response for " + URL_FIRST.toString(), e.getMessage());
                }
            }
        }
    }
}
