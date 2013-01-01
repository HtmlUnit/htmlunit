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
package com.gargoylesoftware.htmlunit;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggingEvent;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * Tests for {@link DefaultCredentialsProvider}.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class DefaultCredentialsProvider2Test extends WebServerTestCase {

    private static String XHRInstantiation_ = "(window.XMLHttpRequest ? "
        + "new XMLHttpRequest() : new ActiveXObject('Microsoft.XMLHTTP'))";

    /**
     * {@inheritDoc}
     */
    protected boolean isBasicAuthentication() {
        return true;
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

        try {
            loadPage("Hi There");
            Assert.fail("Should not be authorized");
        }
        catch (final FailingHttpStatusCodeException e) {
            //success
        }
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

        try {
            loadPage("Hi There");
            Assert.fail("Should not be authorized");
        }
        catch (final FailingHttpStatusCodeException e) {
            //success
        }
    }

    /**
     * Tests that on calling the website twice, only the first time unauthorized response is returned.
     *
     * @throws Exception if an error occurs
     */
    @Test
    public void basicAuthentication_singleAuthenticaiton() throws Exception {
        final Logger logger = Logger.getLogger("org.apache.http.headers");
        final Level oldLevel = logger.getLevel();
        logger.setLevel(Level.DEBUG);

        final InMemoryAppender appender = new InMemoryAppender();
        logger.addAppender(appender);
        try {
            ((DefaultCredentialsProvider) getWebClient().getCredentialsProvider()).addCredentials("jetty", "jetty");

            loadPage("Hi There");
            int unauthorizedCount = 0;
            for (final String message : appender.getMessages()) {
                if (message.contains("HTTP/1.1 401")) {
                    unauthorizedCount++;
                }
            }
            assertEquals(1, unauthorizedCount);
        }
        finally {
            logger.removeAppender(appender);
            logger.setLevel(oldLevel);
        }
    }

    /**
     * An in memory appender, used to save all logged messages in memory.
     */
    public static class InMemoryAppender extends AppenderSkeleton {

        private List<String> messages_ = new ArrayList<String>();

        /**
         * {@inheritDoc}
         */
        @Override
        protected void append(final LoggingEvent event) {
            messages_.add(event.getMessage().toString());
        }

        /**
         * Returns the saved messages.
         * @return the saved messages
         */
        public List<String> getMessages() {
            return messages_;
        }

        /**
         * {@inheritDoc}
         */
        public void close() {
        }

        /**
         * {@inheritDoc}
         */
        public boolean requiresLayout() {
            return false;
        }
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = "SecRet")
    public void basicAuthenticationUserFromUrl() throws Exception {
        final String html = "<html><body onload='alert(\"SecRet\")'></body></html>";
        getMockWebConnection().setDefaultResponse(html);

        getWebClient().getCredentialsProvider().clear();

        try {
            loadPage(html, new URL("http://localhost:" + PORT + "/"));
            Assert.fail("Should not be authorized");
        }
        catch (final FailingHttpStatusCodeException e) {
            //success
        }

        try {
            //  now a url with credentials
            final URL url = new URL("http://jetty:jetty@localhost:" + PORT + "/");
            loadPageWithAlerts(html, url);
        }
        catch (final FailingHttpStatusCodeException e) {
            //  TODO: asashour: check real IE8, original test case didn't throw exception
            if (getBrowserVersion().isIE()) {
                //success
            }
            else {
                throw e;
            }
        }

        try {
            // next step without credentials but the credentials are still known
            final URL url = new URL("http://localhost:" + PORT + "/");
            loadPageWithAlerts(html, url);
        }
        catch (final FailingHttpStatusCodeException e) {
            //  TODO: asashour: check real IE8, original test case didn't throw exception
            if (getBrowserVersion().isIE()) {
                //success
            }
            else {
                throw e;
            }
        }
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = "SecRet")
    public void basicAuthenticationUserFromUrlUsedForNextSteps() throws Exception {
        final String html = "<html><body onload='alert(\"SecRet\")'></body></html>";
        getMockWebConnection().setDefaultResponse(html);

        getWebClient().getCredentialsProvider().clear();

        try {
            // no credentials
            loadPage(html, new URL("http://localhost:" + PORT + "/"));
            Assert.fail("Should not be authorized");
        }
        catch (final FailingHttpStatusCodeException e) {
            //success
        }

        try {
            // now a url with credentials
            final URL url = new URL("http://jetty:jetty@localhost:" + PORT + "/");
            loadPageWithAlerts(url);
        }
        catch (final FailingHttpStatusCodeException e) {
            //  TODO: asashour: check real IE8, original test case didn't throw exception
            if (getBrowserVersion().isIE()) {
                //success
            }
            else {
                throw e;
            }
        }

        try {
            // next step without credentials but the credentials are still known
            final URL url = new URL("http://localhost:" + PORT + "/");
            loadPageWithAlerts(url);
        }
        catch (final FailingHttpStatusCodeException e) {
            //  TODO: asashour: check real IE8, original test case didn't throw exception
            if (getBrowserVersion().isIE()) {
                //success
            }
            else {
                throw e;
            }
        }

        try {
            // different path
            final URL url = new URL("http://localhost:" + PORT + "/somewhere");
            loadPageWithAlerts(url);
        }
        catch (final FailingHttpStatusCodeException e) {
            //  TODO: asashour: check real IE8, original test case didn't throw exception
            if (getBrowserVersion().isIE()) {
                //success
            }
            else {
                throw e;
            }
        }
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = "SecRet")
    public void basicAuthenticationUserFromUrlOverwrite() throws Exception {
        final String html = "<html><body onload='alert(\"SecRet\")'></body></html>";
        getMockWebConnection().setDefaultResponse(html);

        getWebClient().getCredentialsProvider().clear();

        try {
            // no credentials
            loadPage(html, new URL("http://localhost:" + PORT + "/"));
            Assert.fail("Should not be authorized");
        }
        catch (final FailingHttpStatusCodeException e) {
            //success
        }

        try {
            // now a url with credentials
            final URL url = new URL("http://jetty:jetty@localhost:" + PORT + "/");
            loadPageWithAlerts(url);
        }
        catch (final FailingHttpStatusCodeException e) {
            //  TODO: asashour: check real IE8, original test case didn't throw exception
            if (getBrowserVersion().isIE()) {
                //success
            }
            else {
                throw e;
            }
        }

        try {
            // next step without credentials but the credentials are still known
            final URL url = new URL("http://localhost:" + PORT + "/");
            loadPageWithAlerts(url);
        }
        catch (final FailingHttpStatusCodeException e) {
            //  TODO: asashour: check real IE8, original test case didn't throw exception
            if (getBrowserVersion().isIE()) {
                //success
            }
            else {
                throw e;
            }
        }

        try {
            final URL url = new URL("http://jetty:wrong@localhost:" + PORT + "/");
            loadPage(html, url);
            Assert.fail("Should not be authorized");
        }
        catch (final FailingHttpStatusCodeException e) {
            //success
        }
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
        loadPageWithAlerts(url);

        try {
            url = new URL("http://joe:jetty@localhost:" + PORT + "/");
            final HtmlPage page = loadPage(html, url);
            if (getBrowserVersion().isIE()) {
                assertTrue(getCollectedAlerts(page).contains("SecRet"));
            }
            else {
                Assert.fail("Should not be authorized");
            }
        }
        catch (final FailingHttpStatusCodeException e) {
            if (getBrowserVersion().isIE()) {
                Assert.fail("Should be authorized");
            }
            else {
                //success
            }
        }
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = "SecRet")
    public void basicAuthenticationUserFromUrlOverwriteWrongDefaultCredentials() throws Exception {
        final String html = "<html><body onload='alert(\"SecRet\")'></body></html>";
        getMockWebConnection().setDefaultResponse(html);

        getWebClient().getCredentialsProvider().clear();
        ((DefaultCredentialsProvider) getWebClient().getCredentialsProvider()).addCredentials("joe", "hack");

        // use default wrong credentials
        URL url = new URL("http://localhost:" + PORT + "/");
        try {
            loadPage(html, url);
            Assert.fail("Should not be authorized");
        }
        catch (final FailingHttpStatusCodeException e) {
            //success
        }

        try {
            // now a url with correct credentials
            url = new URL("http://jetty:jetty@localhost:" + PORT + "/");
            loadPageWithAlerts(url);
        }
        catch (final FailingHttpStatusCodeException e) {
            //  TODO: asashour: check real IE8, original test case didn't throw exception
            if (getBrowserVersion().isIE()) {
                //success
            }
            else {
                throw e;
            }
        }
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("Hello World")
    //  TODO: asashour: check real IE8, original test case didn't have any alerts
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
        loadPageWithAlerts(html, URL_FIRST, 200);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("HTTP ERROR 401")
    //  TODO: asashour: check real IE8, original test case didn't have any alerts
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
        loadPageWithAlerts(html, URL_FIRST, 100);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("HTTP ERROR 401")
    //  TODO: asashour: check real IE8, original test case didn't have any alerts
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
        loadPageWithAlerts(html, URL_FIRST, 100);
    }
}
