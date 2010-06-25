/*
 * Copyright (c) 2002-2010 Gargoyle Software Inc.
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

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

/**
 * Tests for {@link DefaultCredentialsProvider}.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class DefaultCredentialsProvider2Test extends WebDriverTestCase {

    /**
     * {@inheritDoc}
     */
    protected boolean isBasicAuthentication() {
        return getWebDriver() instanceof HtmlUnitDriver;
    }

    /**
     * {@inheritDoc}
     */
    protected WebClient createNewWebClient() {
        final WebClient webClient = super.createNewWebClient();
        ((DefaultCredentialsProvider) webClient.getCredentialsProvider()).addCredentials("jetty", "jetty");
        return webClient;
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void basicAuthenticationTwice() throws Exception {
        getMockWebConnection().setResponse(URL_SECOND, "Hello World");
        final WebDriver driver = loadPage2("Hi There");
        assertTrue(driver.getPageSource().contains("Hi There"));
        driver.get(URL_SECOND.toExternalForm());
        assertTrue(driver.getPageSource().contains("Hello World"));
    }
}
