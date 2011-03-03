/*
 * Copyright (c) 2002-2011 Gargoyle Software Inc.
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
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;

/**
 * Tests for {@link DefaultCredentialsProvider}.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class DefaultCredentialsProvider2Test extends WebDriverTestCase {

    private static String XHRInstantiation_ = "(window.XMLHttpRequest ? "
        + "new XMLHttpRequest() : new ActiveXObject('Microsoft.XMLHTTP'))";

    /**
     * {@inheritDoc}
     */
    protected boolean isBasicAuthentication() {
        return getWebDriver() instanceof HtmlUnitDriver;
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void basicAuthenticationWrongUserName() throws Exception {
        getMockWebConnection().setResponse(URL_SECOND, "Hello World");

        // wrong user name
        getWebClient().getCredentialsProvider().clear();
        ((DefaultCredentialsProvider) getWebClient().getCredentialsProvider()).addCredentials("joe", "jetty");

        final WebDriver driver = loadPage2("Hi There");
        assertTrue(driver.getPageSource().contains("HTTP ERROR 401"));
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void basicAuthenticationWrongPassword() throws Exception {
        getMockWebConnection().setResponse(URL_SECOND, "Hello World");

        // wrong user name
        getWebClient().getCredentialsProvider().clear();
        ((DefaultCredentialsProvider) getWebClient().getCredentialsProvider()).addCredentials("jetty", "secret");

        final WebDriver driver = loadPage2("Hi There");
        assertTrue(driver.getPageSource().contains("HTTP ERROR 401"));
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void basicAuthenticationTwice() throws Exception {
        ((DefaultCredentialsProvider) getWebClient().getCredentialsProvider()).addCredentials("jetty", "jetty");

        getMockWebConnection().setResponse(URL_SECOND, "Hello World");
        final WebDriver driver = loadPage2("Hi There");
        assertTrue(driver.getPageSource().contains("Hi There"));
        driver.get(URL_SECOND.toExternalForm());
        assertTrue(driver.getPageSource().contains("Hello World"));
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("SecRet")
    public void basicAuthenticationUserFromUrl() throws Exception {
        final String html = "<html><body onload='alert(\"SecRet\")'></body></html>";
        getMockWebConnection().setDefaultResponse(html);

        getWebClient().getCredentialsProvider().clear();

        // no credentials
        final WebDriver driver = loadPage2(html, new URL("http://localhost:" + PORT + "/"));
        assertTrue(driver.getPageSource().contains("HTTP ERROR 401"));

        // now a url with credentials
        URL url = new URL("http://jetty:jetty@localhost:" + PORT + "/");
        loadPageWithAlerts2(url);

        // next step without credentials but the credentials are still known
        url = new URL("http://localhost:" + PORT + "/");
        loadPageWithAlerts2(url);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("SecRet")
    public void basicAuthenticationUserFromUrlUsedForNextSteps() throws Exception {
        final String html = "<html><body onload='alert(\"SecRet\")'></body></html>";
        getMockWebConnection().setDefaultResponse(html);

        getWebClient().getCredentialsProvider().clear();

        // no credentials
        final WebDriver driver = loadPage2(html, new URL("http://localhost:" + PORT + "/"));
        assertTrue(driver.getPageSource().contains("HTTP ERROR 401"));

        // now a url with credentials
        URL url = new URL("http://jetty:jetty@localhost:" + PORT + "/");
        loadPageWithAlerts2(url);

        // next step without credentials but the credentials are still known
        url = new URL("http://localhost:" + PORT + "/");
        loadPageWithAlerts2(url);

        // different path
        url = new URL("http://localhost:" + PORT + "/somewhere");
        loadPageWithAlerts2(url);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("SecRet")
    public void basicAuthenticationUserFromUrlOverwrite() throws Exception {
        final String html = "<html><body onload='alert(\"SecRet\")'></body></html>";
        getMockWebConnection().setDefaultResponse(html);

        getWebClient().getCredentialsProvider().clear();

        // no credentials
        final WebDriver driver = loadPage2(html, new URL("http://localhost:" + PORT + "/"));
        assertTrue(driver.getPageSource().contains("HTTP ERROR 401"));

        // now a url with credentials
        URL url = new URL("http://jetty:jetty@localhost:" + PORT + "/");
        loadPageWithAlerts2(url);

        // next step without credentials but the credentials are still known
        url = new URL("http://localhost:" + PORT + "/");
        loadPageWithAlerts2(url);

        // and now with wrong credentials
        url = new URL("http://jetty:wrong@localhost:" + PORT + "/");
        loadPage2(html, url);
        assertTrue(driver.getPageSource().contains("HTTP ERROR 401"));
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("SecRet")
    public void basicAuthenticationUserFromUrlOverwriteDefaultCredentials() throws Exception {
        final String html = "<html><body onload='alert(\"SecRet\")'></body></html>";
        getMockWebConnection().setDefaultResponse(html);

        getWebClient().getCredentialsProvider().clear();
        ((DefaultCredentialsProvider) getWebClient().getCredentialsProvider()).addCredentials("jetty", "jetty");

        // use default credentials
        URL url = new URL("http://localhost:" + PORT + "/");
        final WebDriver driver = loadPageWithAlerts2(url);

        // now a url with wrong credentials
        url = new URL("http://joe:jetty@localhost:" + PORT + "/");
        loadPage2(html, url);
        assertTrue(driver.getPageSource().contains("HTTP ERROR 401"));
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("SecRet")
    public void basicAuthenticationUserFromUrlOverwriteWrongDefaultCredentials() throws Exception {
        final String html = "<html><body onload='alert(\"SecRet\")'></body></html>";
        getMockWebConnection().setDefaultResponse(html);

        getWebClient().getCredentialsProvider().clear();
        ((DefaultCredentialsProvider) getWebClient().getCredentialsProvider()).addCredentials("joe", "hack");

        // use default wrong credentials
        URL url = new URL("http://localhost:" + PORT + "/");
        final WebDriver driver = loadPage2(html, url);
        assertTrue(driver.getPageSource().contains("HTTP ERROR 401"));

        // now a url with correct credentials
        url = new URL("http://jetty:jetty@localhost:" + PORT + "/");
        loadPageWithAlerts2(url);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("Hello World")
    public void basicAuthenticationXHR() throws Exception {
        final String html = "<html><head><script>\n"
            + "var xhr = " + XHRInstantiation_ + ";\n"
            + "var handler = function() {\n"
            + "  if (xhr.readyState == 4)\n"
            + "    alert(xhr.responseText);\n"
            + "}\n"
            + "xhr.onreadystatechange = handler;\n"
            + "xhr.open('GET', '" + URL_SECOND + "', true);\n"
            + "xhr.send('');\n"
            + "</script></head><body></body></html>";

        ((DefaultCredentialsProvider) getWebClient().getCredentialsProvider()).addCredentials("jetty", "jetty");
        getMockWebConnection().setDefaultResponse("Hello World");
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("HTTP ERROR 401")
    public void basicAuthenticationXHRWithUsername() throws Exception {
        final String html = "<html><head><script>\n"
            + "var xhr = " + XHRInstantiation_ + ";\n"
            + "var handler = function() {\n"
            + "  if (xhr.readyState == 4) {\n"
            + "    var s = xhr.responseText.replace(/[\\r\\n]/g, '')"
            + ".replace(/.*(HTTP ERROR \\d+).*/g, '$1');\n"
            + "    alert(s);\n"
            + "  }\n"
            + "}\n"
            + "xhr.onreadystatechange = handler;\n"
            + "xhr.open('GET', '/foo', true, 'joe');\n"
            + "xhr.send('');\n"
            + "</script></head><body></body></html>";

        ((DefaultCredentialsProvider) getWebClient().getCredentialsProvider()).addCredentials("jetty", "jetty");
        getMockWebConnection().setDefaultResponse("Hello World");
        loadPageWithAlerts2(html, 1000);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("HTTP ERROR 401")
    public void basicAuthenticationXHRWithUser() throws Exception {
        final String html = "<html><head><script>\n"
            + "var xhr = " + XHRInstantiation_ + ";\n"
            + "var handler = function() {\n"
            + "  if (xhr.readyState == 4) {\n"
            + "    var s = xhr.responseText.replace(/[\\r\\n]/g, '')"
            + ".replace(/.*(HTTP ERROR \\d+).*/g, '$1');\n"
            + "    alert(s);\n"
            + "  }\n"
            + "}\n"
            + "xhr.onreadystatechange = handler;\n"
            + "xhr.open('GET', '/foo', true, 'joe', 'secret');\n"
            + "xhr.send('');\n"
            + "</script></head><body></body></html>";

        ((DefaultCredentialsProvider) getWebClient().getCredentialsProvider()).addCredentials("jetty", "jetty");
        getMockWebConnection().setDefaultResponse("Hello World");
        loadPageWithAlerts2(html, 1000);
    }
}
