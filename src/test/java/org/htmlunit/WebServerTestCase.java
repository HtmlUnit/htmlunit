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

import static java.nio.charset.StandardCharsets.ISO_8859_1;

import java.net.URL;
import java.nio.charset.Charset;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.Servlet;

import org.eclipse.jetty.server.Server;
import org.htmlunit.WebDriverTestCase.MockWebConnectionServlet;
import org.htmlunit.html.HtmlPage;
import org.htmlunit.util.JettyServerUtils;
import org.htmlunit.util.MimeType;
import org.junit.jupiter.api.AfterEach;

/**
 * A WebTestCase which starts a local server, and doesn't use WebDriver.
 * <p>
 * <b>Note that {@link WebDriverTestCase} should be used unless HtmlUnit-specific feature
 * is needed and Selenium does not support it.</b>
 *
 * @author Ahmed Ashour
 * @author Marc Guillemot
 * @author Ronald Brill
 */
public abstract class WebServerTestCase extends WebTestCase {

    public enum SSLVariant {
        NONE,
        INSECURE,
        SELF_SIGNED
    }

    /** Timeout used when waiting for successful bind. */
    public static final int BIND_TIMEOUT = 1000;

    private Server server_;
    private static Server STATIC_SERVER_;
    private WebClient webClient_;
    private CollectingAlertHandler alertHandler_ = new CollectingAlertHandler();

    /**
     * Starts the web server on the default {@link #PORT}.
     * The given resourceBase is used to be the ROOT directory that serves the default context.
     * <p><b>Don't forget to stop the returned HttpServer after the test</b>
     *
     * @param resourceBase the base of resources for the default context
     * @throws Exception if the test fails
     */
    protected void startWebServer(final String resourceBase) throws Exception {
        if (server_ != null) {
            throw new IllegalStateException("startWebServer() can not be called twice");
        }

        server_ =  JettyServerUtils.startWebServer(PORT, resourceBase, null, null, isBasicAuthentication(), getSSLVariant());
    }

    /**
     * Starts the web server on the default {@link #PORT}.
     * The given resourceBase is used to be the ROOT directory that serves the default context.
     * <p><b>Don't forget to stop the returned HttpServer after the test</b>
     *
     * @param resourceBase the base of resources for the default context
     * @param classpath additional classpath entries to add (may be null)
     * @throws Exception if the test fails
     */
    protected void startWebServer(final String resourceBase, final String[] classpath) throws Exception {
        if (server_ != null) {
            throw new IllegalStateException("startWebServer() can not be called twice");
        }
        server_ = createWebServer(resourceBase, classpath);
    }

    /**
     * This is usually needed if you want to have a running server during many tests invocation.
     * <p>
     * Creates and starts a web server on the default {@link #PORT}.
     * The given resourceBase is used to be the ROOT directory that serves the default context.
     * <p><b>Don't forget to stop the returned Server after the test</b>
     *
     * @param resourceBase the base of resources for the default context
     * @param classpath additional classpath entries to add (may be null)
     * @return the newly created server
     * @throws Exception if an error occurs
     */
    public static Server createWebServer(final String resourceBase, final String[] classpath) throws Exception {
        return createWebServer(PORT, resourceBase, classpath, null);
    }

    /**
     * This is usually needed if you want to have a running server during many tests invocation.
     * <p>
     * Creates and starts a web server on the default {@link #PORT}.
     * The given resourceBase is used to be the ROOT directory that serves the default context.
     * <p><b>Don't forget to stop the returned Server after the test</b>
     *
     * @param port the port to which the server is bound
     * @param resourceBase the base of resources for the default context
     * @param classpath additional classpath entries to add (may be null)
     * @param servlets map of {String, Class} pairs: String is the path spec, while class is the class
     * @return the newly created server
     * @throws Exception if an error occurs
     */
    public static Server createWebServer(final int port, final String resourceBase, final String[] classpath,
            final Map<String, Class<? extends Servlet>> servlets) throws Exception {

        return JettyServerUtils.startWebServer(port, resourceBase, servlets, null, false, SSLVariant.NONE);
    }

    /**
     * Starts the web server on the default {@link #PORT}.
     * The given resourceBase is used to be the ROOT directory that serves the default context.
     * <p><b>Don't forget to stop the returned HttpServer after the test</b>
     *
     * @param resourceBase the base of resources for the default context
     * @param classpath additional classpath entries to add (may be null)
     * @param servlets map of {String, Class} pairs: String is the path spec, while class is the class
     * @throws Exception if the test fails
     */
    protected void startWebServer(final String resourceBase, final String[] classpath,
            final Map<String, Class<? extends Servlet>> servlets) throws Exception {
        if (server_ != null) {
            throw new IllegalStateException("startWebServer() can not be called twice");
        }

        server_ = createWebServer(PORT, resourceBase, classpath, servlets);
    }

    /**
     * Performs post-test deconstruction.
     * @throws Exception if an error occurs
     */
    @AfterEach
    public void tearDown() throws Exception {
        if (server_ != null) {
            server_.stop();
            server_.destroy();
            server_ = null;
        }

        stopWebServer();
    }

    /**
     * Defines the provided string as response for the provided URL and loads it using the currently
     * configured browser version. Finally it extracts the captured alerts and verifies them.
     * @param html the HTML to use
     * @param url the URL to use to load the page
     * @return the page
     * @throws Exception if something goes wrong
     */
    protected final HtmlPage loadPageWithAlerts(final String html, final URL url)
        throws Exception {
        return loadPageWithAlerts(html, url, Duration.ofSeconds(0));
    }

    /**
     * Same as {@link #loadPageWithAlerts(String, URL)}, but configuring the max wait time.
     * @param html the HTML to use
     * @param url the URL to use to load the page
     * @param maxWaitTime to wait to get the alerts (in ms)
     * @return the page
     * @throws Exception if something goes wrong
     */
    protected final HtmlPage loadPageWithAlerts(final String html, final URL url, final Duration maxWaitTime)
        throws Exception {
        alertHandler_.clear();
        expandExpectedAlertsVariables(URL_FIRST);

        final String[] expectedAlerts = getExpectedAlerts();
        final HtmlPage page = loadPage(html, url);

        List<String> actualAlerts = getCollectedAlerts(page);
        final long maxWait = System.currentTimeMillis() + maxWaitTime.toMillis();
        while (actualAlerts.size() < expectedAlerts.length && System.currentTimeMillis() < maxWait) {
            Thread.sleep(30);
            actualAlerts = getCollectedAlerts(page);
        }

        assertEquals(expectedAlerts, getCollectedAlerts(page));
        return page;
    }

    /**
     * Defines the provided string as response for the default URL and loads it using the currently
     * configured browser version.
     * @param html the HTML to use
     * @return the page
     * @throws Exception if something goes wrong
     */
    protected final HtmlPage loadPage(final String html) throws Exception {
        return loadPage(html, URL_FIRST);
    }

    /**
     * Same as {@link #loadPage(String)}... but defining the default URL.
     * @param html the HTML to use
     * @param url the url to use to load the page
     * @return the page
     * @throws Exception if something goes wrong
     */
    protected final HtmlPage loadPage(final String html, final URL url) throws Exception {
        return loadPage(html, url, MimeType.TEXT_HTML, ISO_8859_1);
    }

    /**
     * Same as {@link #loadPage(String, URL)}... but defining content type and charset as well.
     * @param html the HTML to use
     * @param url the url to use to load the page
     * @param contentType the content type to return
     * @param charset the charset
     * @return the page
     * @throws Exception if something goes wrong
     */
    private HtmlPage loadPage(final String html, final URL url,
            final String contentType, final Charset charset) throws Exception {
        final MockWebConnection mockWebConnection = getMockWebConnection();
        mockWebConnection.setResponse(url, html, contentType, charset);
        startWebServer(mockWebConnection);

        return getWebClient().getPage(url);
    }

    /**
     * Starts the web server delivering response from the provided connection.
     * @param mockConnection the sources for responses
     * @throws Exception if a problem occurs
     */
    protected void startWebServer(final MockWebConnection mockConnection) throws Exception {
        if (STATIC_SERVER_ == null) {
            final Map<String, Class<? extends Servlet>> servlets = new HashMap<>();
            servlets.put("/*", MockWebConnectionServlet.class);

            STATIC_SERVER_ = JettyServerUtils.startWebServer(PORT, "./", servlets, null, isBasicAuthentication(), getSSLVariant());
        }
        MockWebConnectionServlet.setMockconnection(mockConnection);
    }

    /**
     * Stops the WebServer.
     * @throws Exception if it fails
     */
    protected static void stopWebServer() throws Exception {
        if (STATIC_SERVER_ != null) {
            STATIC_SERVER_.stop();
            STATIC_SERVER_.destroy();
            STATIC_SERVER_ = null;
        }
    }

    /**
     * Loads the provided URL serving responses from {@link #getMockWebConnection()}
     * and verifies that the captured alerts are correct.
     * @param url the URL to use to load the page
     * @return the web driver
     * @throws Exception if something goes wrong
     */
    protected final HtmlPage loadPageWithAlerts(final URL url) throws Exception {
        alertHandler_.clear();
        expandExpectedAlertsVariables(url);
        final String[] expectedAlerts = getExpectedAlerts();

        startWebServer(getMockWebConnection());

        final HtmlPage page = getWebClient().getPage(url);

        assertEquals(expectedAlerts, getCollectedAlerts(page));
        return page;
    }

    /**
     * Returns the collected alerts.
     * @param page the page
     * @return the alerts
     */
    protected List<String> getCollectedAlerts(final HtmlPage page) {
        return alertHandler_.getCollectedAlerts();
    }

    /**
     * Returns whether to use basic authentication for all resources or not.
     * The default implementation returns false.
     * @return whether to use basic authentication or not
     */
    protected boolean isBasicAuthentication() {
        return false;
    }

    /**
     * @return the {@link SSLVariant} to be used
     */
    public SSLVariant getSSLVariant() {
        return SSLVariant.NONE;
    }

    /**
     * Returns the WebClient instance for the current test with the current {@link BrowserVersion}.
     * @return a WebClient with the current {@link BrowserVersion}
     */
    protected WebClient getWebClient() {
        if (webClient_ == null) {
            webClient_ = new WebClient(getBrowserVersion());
            webClient_.setAlertHandler(alertHandler_);
        }
        return webClient_;
    }

    /**
     * Cleanup after a test.
     */
    @Override
    @AfterEach
    public void releaseResources() {
        super.releaseResources();
        if (webClient_ != null) {
            webClient_.close();
            webClient_.getCookieManager().clearCookies();
        }
        webClient_ = null;
        alertHandler_ = null;
    }
}
