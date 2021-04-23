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
package com.gargoylesoftware.htmlunit;

import static org.junit.Assert.fail;

import java.net.URL;
import java.nio.charset.StandardCharsets;

import org.junit.After;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;

import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.HtmlUnitNYI;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * Tests for handling HttpClient's {@link org.apache.http.NoHttpResponseException}.
 *
 * @author Marc Guillemot
 * @author Frank Danek
 * @author Ronald Brill
 */
@RunWith(Enclosed.class)
public class NoHttpResponseTest {
    private static final String html
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
    @RunWith(BrowserRunner.class)
    public static class WithWebDriverTest extends WebDriverTestCase {

        @After
        public void after() throws Exception {
            MiniServer.resetDropRequests();
        }

        /**
         * @throws Exception if the test fails
         */
        @Test
        @Alerts(DEFAULT = "§§URL§§page2?textfield=",
                FF = "WebDriverException",
                FF78 = "WebDriverException")
        // TODO [IE] does not run in real IE (browser waits for a looooooong time)
        @HtmlUnitNYI(FF = "§§URL§§page2?textfield=",
                FF78 = "§§URL§§page2?textfield=")
        public void submit() throws Exception {
            final MockWebConnection mockWebConnection = getMockWebConnection();
            mockWebConnection.setResponse(URL_FIRST, html);
            MiniServer.configureDropRequest(new URL(URL_FIRST, "page2?textfield="));
            final URL urlRightSubmit = new URL(URL_FIRST, "page2?textfield=new+value");
            mockWebConnection.setResponse(urlRightSubmit, "<html><head><title>right submit</title></head></html>");

            expandExpectedAlertsVariables(URL_FIRST);
            final WebDriver driver = getWebDriver();

            final MiniServer miniServer = new MiniServer(PORT, mockWebConnection);
            miniServer.start();
            try {
                driver.get(URL_FIRST.toString());
                driver.findElement(By.id("inputSubmit")).click();
                assertEquals(getExpectedAlerts()[0], driver.getCurrentUrl());
            }
            catch (final WebDriverException e) {
                assertEquals(getExpectedAlerts()[0], "WebDriverException");
            }
            finally {
                miniServer.shutDown();
            }
        }

        /**
         * @throws Exception if the test fails
         */
        @Test
        @Alerts(DEFAULT = "right submit",
                IE = "Can’t reach this page")
        @HtmlUnitNYI(IE = "right submit")
        public void callSubmitInButtonAndReturnTrue() throws Exception {
            final MockWebConnection mockWebConnection = getMockWebConnection();
            mockWebConnection.setResponse(URL_FIRST, html);
            MiniServer.configureDropRequest(new URL(URL_FIRST, "page2?textfield="));
            final URL urlRightSubmit = new URL(URL_FIRST, "page2?textfield=new+value");
            mockWebConnection.setResponse(urlRightSubmit, "<html><head><title>right submit</title></head></html>");

            final WebDriver driver = getWebDriver();

            final MiniServer miniServer = new MiniServer(PORT, mockWebConnection);
            miniServer.start();
            try {
                driver.get(URL_FIRST.toString());
                driver.findElement(By.id("jsSubmit")).click();
                assertTitle(driver, getExpectedAlerts()[0]);
            }
            finally {
                miniServer.shutDown();
            }
        }
    }

    /**
     * Test using WebClient with default configuration allowing to throw exception.
     */
    @RunWith(BrowserRunner.class)
    public static class WithWebClientTest extends SimpleWebTestCase {

        @After
        public void after() throws Exception {
            MiniServer.resetDropRequests();
        }

        /**
         * @throws Throwable if the test fails
         */
        @Test(expected = FailingHttpStatusCodeException.class)
        public void submit() throws Throwable {
            final MockWebConnection mockWebConnection = getMockWebConnection();
            mockWebConnection.setResponse(URL_FIRST, html);
            MiniServer.configureDropRequest(new URL(URL_FIRST, "page2?textfield="));
            final URL urlRightSubmit = new URL(URL_FIRST, "page2?textfield=new+value");
            mockWebConnection.setResponse(urlRightSubmit, "<html><head><title>right submit</title></head></html>");

            expandExpectedAlertsVariables(URL_FIRST);

            final MiniServer miniServer = new MiniServer(PORT, mockWebConnection);
            miniServer.start();

            try {
                HtmlPage page = getWebClient().getPage(URL_FIRST);
                page = page.getElementById("inputSubmit").click();
                assertEquals(getExpectedAlerts()[0], page.getUrl());
            }
            finally {
                miniServer.shutDown();
            }
        }

        /**
         * @throws Throwable if the test fails
         */
        @Test
        public void callSubmitInButtonAndReturnTrue() throws Throwable {
            final MockWebConnection mockWebConnection = getMockWebConnection();
            mockWebConnection.setResponse(URL_FIRST, html);
            MiniServer.configureDropRequest(new URL(URL_FIRST, "page2?textfield="));
            final URL urlRightSubmit = new URL(URL_FIRST, "page2?textfield=new+value");
            mockWebConnection.setResponse(urlRightSubmit, "<html><head><title>right submit</title></head></html>");

            final MiniServer miniServer = new MiniServer(PORT, mockWebConnection);
            miniServer.start();
            try {
                HtmlPage page = getWebClient().getPage(URL_FIRST);
                page = page.getElementById("jsSubmit").click();
                assertEquals("right submit", page.getTitleText());
            }
            finally {
                miniServer.shutDown();
            }
        }

        /**
         * @throws Exception if the test fails
         */
        @Test
        public void htmlUnitDriverUsesGetPage() throws Exception {
            final MockWebConnection mockWebConnection = getMockWebConnection();
            MiniServer.configureDropRequest(URL_FIRST);

            final MiniServer miniServer = new MiniServer(PORT, mockWebConnection);
            miniServer.start();
            try {
                final WebRequest request = new WebRequest(new URL(URL_FIRST.toString()),
                        getBrowserVersion().getHtmlAcceptHeader(), getBrowserVersion().getAcceptEncodingHeader());
                request.setCharset(StandardCharsets.UTF_8);

                try {
                    getWebClient().getPage(getWebClient().getCurrentWindow().getTopWindow(), request);
                    fail("FailingHttpStatusCodeException expected");
                }
                catch (final FailingHttpStatusCodeException e) {
                    // expected
                    assertEquals(0, e.getStatusCode());
                    assertEquals("0 No HTTP Response for " + URL_FIRST.toString(), e.getMessage());
                }
            }
            finally {
                miniServer.shutDown();
            }
        }
    }
}
