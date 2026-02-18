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

import static org.junit.jupiter.api.Assertions.fail;

import java.io.ByteArrayOutputStream;
import java.net.URL;

import org.apache.commons.lang3.StringUtils;
import org.htmlunit.html.HtmlPage;
import org.htmlunit.junit.annotation.Alerts;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.OutputStreamAppender;

/**
 * Tests for {@link DefaultCredentialsProvider}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
public class DefaultCredentialsProvider2Test extends WebServerTestCase {

    /**
     * {@inheritDoc}
     */
    @Override
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
        ((DefaultCredentialsProvider) getWebClient().getCredentialsProvider())
                                        .addCredentials("joe", "jetty".toCharArray());

        try {
            loadPage("Hi There");
            fail("Should not be authorized");
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
        ((DefaultCredentialsProvider) getWebClient().getCredentialsProvider())
                                        .addCredentials("jetty", "secret".toCharArray());

        try {
            loadPage("Hi There");
            fail("Should not be authorized");
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
        final Logger logger = (Logger) LoggerFactory.getLogger("org.apache.http.headers");
        final Level oldLevel = logger.getLevel();
        logger.setLevel(Level.DEBUG);

        final LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();

        final PatternLayoutEncoder encoder = new PatternLayoutEncoder();
        encoder.setContext(context);
        encoder.setPattern("%msg%n");
        encoder.start();

        final OutputStreamAppender<ILoggingEvent> appender = new OutputStreamAppender<>();
        appender.setContext(context);
        appender.setEncoder(encoder);
        appender.setOutputStream(baos);
        appender.start();

        logger.addAppender(appender);
        try {
            ((DefaultCredentialsProvider) getWebClient().getCredentialsProvider())
                                            .addCredentials("jetty", "jetty".toCharArray());

            loadPage("Hi There");
            int unauthorizedCount = StringUtils.countMatches(baos.toString(), "HTTP/1.1 401");
            assertEquals(1, unauthorizedCount);

            // and again
            loadPage("Hi There");
            unauthorizedCount = StringUtils.countMatches(baos.toString(), "HTTP/1.1 401");
            assertEquals(1, unauthorizedCount);
        }
        finally {
            logger.detachAppender(appender);
            appender.stop();
            logger.setLevel(oldLevel);
        }
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("SecRet")
    public void basicAuthenticationUserFromUrl() throws Exception {
        final String html = DOCTYPE_HTML + "<html><body onload='alert(\"SecRet\")'></body></html>";
        getMockWebConnection().setDefaultResponse(html);

        getWebClient().getCredentialsProvider().clear();

        try {
            loadPage(html, URL_FIRST);
            fail("Should not be authorized");
        }
        catch (final FailingHttpStatusCodeException e) {
            //success
        }

        final boolean urlWithCredentials = true;

        try {
            //  now a url with credentials
            final URL url = new URL("http://jetty:jetty@localhost:" + PORT + "/");
            loadPageWithAlerts(html, url);
            if (!urlWithCredentials) {
                fail("Should not be authorized");
            }
        }
        catch (final FailingHttpStatusCodeException e) {
            if (urlWithCredentials) {
                throw e;
            }
        }

        try {
            // next step without credentials but the credentials are still known
            loadPageWithAlerts(html, URL_FIRST);
            if (!urlWithCredentials) {
                fail("Should not be authorized");
            }
        }
        catch (final FailingHttpStatusCodeException e) {
            if (urlWithCredentials) {
                throw e;
            }
        }
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("SecRet")
    public void basicAuthenticationUserFromUrlUsedForNextSteps() throws Exception {
        final String html = DOCTYPE_HTML + "<html><body onload='alert(\"SecRet\")'></body></html>";
        getMockWebConnection().setDefaultResponse(html);

        getWebClient().getCredentialsProvider().clear();

        try {
            // no credentials
            loadPage(html, URL_FIRST);
            fail("Should not be authorized");
        }
        catch (final FailingHttpStatusCodeException e) {
            //success
        }

        final boolean urlWithCredentials = true;

        try {
            // now a url with credentials
            final URL url = new URL("http://jetty:jetty@localhost:" + PORT + "/");
            loadPageWithAlerts(url);
            if (!urlWithCredentials) {
                fail("Should not be authorized");
            }
        }
        catch (final FailingHttpStatusCodeException e) {
            if (urlWithCredentials) {
                throw e;
            }
        }

        try {
            // next step without credentials but the credentials are still known
            loadPageWithAlerts(URL_FIRST);
            if (!urlWithCredentials) {
                fail("Should not be authorized");
            }
        }
        catch (final FailingHttpStatusCodeException e) {
            if (urlWithCredentials) {
                throw e;
            }
        }

        try {
            // different path
            final URL url = new URL(URL_FIRST, "somewhere");
            loadPageWithAlerts(url);
            if (!urlWithCredentials) {
                fail("Should not be authorized");
            }
        }
        catch (final FailingHttpStatusCodeException e) {
            if (urlWithCredentials) {
                throw e;
            }
        }
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("SecRet")
    public void basicAuthenticationUserFromUrlOverwrite() throws Exception {
        final String html = DOCTYPE_HTML + "<html><body onload='alert(\"SecRet\")'></body></html>";
        getMockWebConnection().setDefaultResponse(html);

        getWebClient().getCredentialsProvider().clear();

        try {
            // no credentials
            loadPage(html, URL_FIRST);
            fail("Should not be authorized");
        }
        catch (final FailingHttpStatusCodeException e) {
            //success
        }

        final boolean urlWithCredentials = true;

        try {
            // now a url with credentials
            final URL url = new URL("http://jetty:jetty@localhost:" + PORT + "/");
            loadPageWithAlerts(url);
            if (!urlWithCredentials) {
                fail("Should not be authorized");
            }
        }
        catch (final FailingHttpStatusCodeException e) {
            if (urlWithCredentials) {
                throw e;
            }
        }

        try {
            // next step without credentials but the credentials are still known
            loadPageWithAlerts(URL_FIRST);
            if (!urlWithCredentials) {
                fail("Should not be authorized");
            }
        }
        catch (final FailingHttpStatusCodeException e) {
            if (urlWithCredentials) {
                throw e;
            }
        }

        try {
            final URL url = new URL("http://jetty:wrong@localhost:" + PORT + "/");
            loadPage(html, url);
            fail("Should not be authorized");
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
        final String html = DOCTYPE_HTML + "<html><body onload='alert(\"SecRet\")'></body></html>";
        getMockWebConnection().setDefaultResponse(html);

        getWebClient().getCredentialsProvider().clear();
        ((DefaultCredentialsProvider) getWebClient().getCredentialsProvider())
                                        .addCredentials("jetty", "jetty".toCharArray());

        // use default credentials
        loadPageWithAlerts(URL_FIRST);

        try {
            final URL url = new URL("http://joe:jetty@localhost:" + PORT + "/");
            final HtmlPage page = loadPage(html, url);
            fail("Should not be authorized");
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
    public void basicAuthenticationUserFromUrlOverwriteWrongDefaultCredentials() throws Exception {
        final String html = DOCTYPE_HTML + "<html><body onload='alert(\"SecRet\")'></body></html>";
        getMockWebConnection().setDefaultResponse(html);

        getWebClient().getCredentialsProvider().clear();
        ((DefaultCredentialsProvider) getWebClient().getCredentialsProvider())
                                        .addCredentials("joe", "hack".toCharArray());

        // use default wrong credentials
        try {
            loadPage(html, URL_FIRST);
            fail("Should not be authorized");
        }
        catch (final FailingHttpStatusCodeException e) {
            //success
        }

        final boolean urlWithCredentials = true;

        try {
            // now a url with correct credentials
            final URL url = new URL("http://jetty:jetty@localhost:" + PORT + "/");
            loadPageWithAlerts(url);
            if (!urlWithCredentials) {
                fail("Should not be authorized");
            }
        }
        catch (final FailingHttpStatusCodeException e) {
            if (urlWithCredentials) {
                throw e;
            }
        }
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("Hello World")
    public void basicAuthenticationXHR() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + "var xhr = new XMLHttpRequest();\n"
            + "var handler = function() {\n"
            + "  if (xhr.readyState == 4)\n"
            + "    alert(xhr.responseText);\n"
            + "}\n"
            + "xhr.onreadystatechange = handler;\n"
            + "xhr.open('GET', '" + URL_SECOND + "', true);\n"
            + "xhr.send('');\n"
            + "</script></head><body></body></html>";

        ((DefaultCredentialsProvider) getWebClient().getCredentialsProvider())
                                        .addCredentials("jetty", "jetty".toCharArray());
        getMockWebConnection().setResponse(URL_FIRST, html);
        getMockWebConnection().setResponse(URL_SECOND, "Hello World");
        loadPageWithAlerts(html, URL_FIRST, DEFAULT_WAIT_TIME);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("HTTP ERROR 401")
    public void basicAuthenticationXHRWithUsername() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + "var xhr = new XMLHttpRequest();\n"
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

        ((DefaultCredentialsProvider) getWebClient().getCredentialsProvider())
                                        .addCredentials("jetty", "jetty".toCharArray());
        getMockWebConnection().setResponse(URL_FIRST, html);
        getMockWebConnection().setResponse(URL_SECOND, "Hello World");
        loadPageWithAlerts(html, URL_FIRST, DEFAULT_WAIT_TIME);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("HTTP ERROR 401")
    public void basicAuthenticationXHRWithUser() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + "var xhr = new XMLHttpRequest();\n"
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

        ((DefaultCredentialsProvider) getWebClient().getCredentialsProvider())
                                        .addCredentials("jetty", "jetty".toCharArray());
        getMockWebConnection().setResponse(URL_FIRST, html);
        getMockWebConnection().setResponse(URL_SECOND, "Hello World");
        loadPageWithAlerts(html, URL_FIRST, DEFAULT_WAIT_TIME);
    }
}
