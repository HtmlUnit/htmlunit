/*
 * Copyright (c) 2002-2020 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit;

import java.net.URL;

import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

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
        + "  <input type='submit' onclick='this.form.submit(); fillField(); return true;' id='loginButton'/>\n"
        + "</form>\n"
        + "</body></html>";

    /**
     * Test using WebDriver.
     * @author Marc Guillemot
     */
    @RunWith(BrowserRunner.class)
    public static class WithWebDriverTest extends WebDriverTestCase {
        /**
         * @throws Exception if the test fails
         */
        @Test
        // TODO [IE] does not run in real IE (browser waits for a looooooong time)
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
                driver.findElement(By.id("loginButton")).click();
                assertTitle(driver, "right submit");
            }
            finally {
                miniServer.shutDown();
            }
        }
    }

    /**
     * Test using WebClient with default configuration allowing to throw exception.
     * @author Marc Guillemot
     */
    @RunWith(BrowserRunner.class)
    public static class WithWebClientTest extends SimpleWebTestCase {
        /**
         * @throws Throwable if the test fails
         */
        @Test(expected = FailingHttpStatusCodeException.class)
        public void callSubmitInButtonAndReturnTrue() throws Throwable {
            final MockWebConnection mockWebConnection = getMockWebConnection();
            mockWebConnection.setResponse(URL_FIRST, html);
            MiniServer.configureDropRequest(new URL(URL_FIRST, "page2?textfield="));

            final MiniServer miniServer = new MiniServer(PORT, mockWebConnection);
            miniServer.start();
            try {
                final HtmlPage page = getWebClient().getPage(URL_FIRST);
                page.getElementById("loginButton").click();
            }
            finally {
                miniServer.shutDown();
            }
        }
    }
}
