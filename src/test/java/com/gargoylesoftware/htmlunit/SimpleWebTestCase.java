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

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;

import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.javascript.JavaScriptEngine;

/**
 * A simple WebTestCase which doesn't require server to run, and doens't use WebDriver.
 *
 * It depends on {@link MockWebConnection} to simulate sending requests to the server.
 *
 * <b>Note that {@link WebDriverTestCase} should be used unless HtmlUnit-specific feature
 * is needed and Selenium does not support it.</b>
 *
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author David D. Kilzer
 * @author Marc Guillemot
 * @author Chris Erskine
 * @author Michael Ottati
 * @author Daniel Gredler
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
public abstract class SimpleWebTestCase extends WebTestCase {

    private WebClient webClient_;

    /**
     * Load a page with the specified HTML using the default browser version.
     * @param html the HTML to use
     * @return the new page
     * @throws Exception if something goes wrong
     */
    public final HtmlPage loadPage(final String html) throws Exception {
        return loadPage(html, null);
    }

    /**
     * User the default browser version to load a page with the specified HTML
     * and collect alerts into the list.
     * @param html the HTML to use
     * @param collectedAlerts the list to hold the alerts
     * @return the new page
     * @throws Exception if something goes wrong
     */
    public final HtmlPage loadPage(final String html, final List<String> collectedAlerts) throws Exception {
        generateTest_browserVersion_.set(FLAG_ALL_BROWSERS);
        return loadPage(getBrowserVersion(), html, collectedAlerts, URL_FIRST);
    }

    /**
     * Loads a page with the specified HTML and collect alerts into the list.
     * @param html the HTML to use
     * @param collectedAlerts the list to hold the alerts
     * @param url the URL that will use as the document host for this page
     * @return the new page
     * @throws Exception if something goes wrong
     */
    protected final HtmlPage loadPage(final String html, final List<String> collectedAlerts,
            final URL url) throws Exception {

        return loadPage(BrowserVersion.getDefault(), html, collectedAlerts, url);
    }

    /**
     * Load a page with the specified HTML and collect alerts into the list.
     * @param browserVersion the browser version to use
     * @param html the HTML to use
     * @param collectedAlerts the list to hold the alerts
     * @param url the URL that will use as the document host for this page
     * @return the new page
     * @throws Exception if something goes wrong
     */
    protected final HtmlPage loadPage(final BrowserVersion browserVersion,
            final String html, final List<String> collectedAlerts, final URL url) throws Exception {

        if (webClient_ == null) {
            webClient_ = new WebClient(browserVersion);
        }
        return loadPage(webClient_, html, collectedAlerts, url);
    }

    /**
     * Load a page with the specified HTML and collect alerts into the list.
     * @param client the WebClient to use (webConnection and alertHandler will be configured on it)
     * @param html the HTML to use
     * @param collectedAlerts the list to hold the alerts
     * @param url the URL that will use as the document host for this page
     * @return the new page
     * @throws Exception if something goes wrong
     */
    protected final HtmlPage loadPage(final WebClient client,
            final String html, final List<String> collectedAlerts, final URL url) throws Exception {

        if (collectedAlerts != null) {
            client.setAlertHandler(new CollectingAlertHandler(collectedAlerts));
        }

        final MockWebConnection webConnection = getMockWebConnection();
        webConnection.setDefaultResponse(html);
        client.setWebConnection(webConnection);

        return client.getPage(url);
    }

    /**
     * Load a page with the specified HTML and collect alerts into the list.
     * @param client the WebClient to use (webConnection and alertHandler will be configured on it)
     * @param html the HTML to use
     * @param collectedAlerts the list to hold the alerts
     * @return the new page
     * @throws Exception if something goes wrong
     */
    protected final HtmlPage loadPage(final WebClient client,
            final String html, final List<String> collectedAlerts) throws Exception {

        return loadPage(client, html, collectedAlerts, URL_FIRST);
    }

    /**
     * Convenience method to pull the MockWebConnection out of an HtmlPage created with
     * the loadPage method.
     * @param page HtmlPage to get the connection from
     * @return the MockWebConnection that served this page
     */
    protected static final MockWebConnection getMockConnection(final HtmlPage page) {
        return (MockWebConnection) page.getWebClient().getWebConnection();
    }

    /**
     * Returns the WebClient instance for the current test with the current {@link BrowserVersion}.
     * @return a WebClient with the current {@link BrowserVersion}
     */
    protected WebClient createNewWebClient() {
        final WebClient webClient = new WebClient(getBrowserVersion());
        return webClient;
    }

    /**
     * Returns the WebClient instance for the current test with the current {@link BrowserVersion}.
     * @return a WebClient with the current {@link BrowserVersion}
     */
    protected final WebClient getWebClient() {
        if (webClient_ == null) {
            webClient_ = createNewWebClient();
        }
        return webClient_;
    }

    /**
     * Returns the WebClient instance for the current test with the current {@link BrowserVersion}.
     * @return a WebClient with the current {@link BrowserVersion}
     */
    protected final WebClient getWebClientWithMockWebConnection() {
        if (webClient_ == null) {
            webClient_ = createNewWebClient();
            webClient_.setWebConnection(getMockWebConnection());
        }
        return webClient_;
    }

    /**
     * Defines the provided HTML as the response of the MockWebConnection for {@link WebTestCase#URL_FIRST}
     * and loads the page with this URL using the current browser version; finally, asserts that the
     * alerts equal the expected alerts (in which "§§URL§§" has been expanded to the default URL).
     * @param html the HTML to use
     * @return the new page
     * @throws Exception if something goes wrong
     */
    protected final HtmlPage loadPageWithAlerts(final String html) throws Exception {
        return loadPageWithAlerts(html, URL_FIRST, -1);
    }

    /**
     * Defines the provided HTML as the response of the MockWebConnection for {@link WebTestCase#URL_FIRST}
     * and loads the page with this URL using the current browser version; finally, asserts the alerts
     * equal the expected alerts.
     * @param html the HTML to use
     * @param url the URL from which the provided HTML code should be delivered
     * @param waitForJS the milliseconds to wait for background JS tasks to complete. Ignored if -1.
     * @return the new page
     * @throws Exception if something goes wrong
     */
    protected final HtmlPage loadPageWithAlerts(final String html, final URL url, final int waitForJS)
        throws Exception {
        if (getExpectedAlerts() == null) {
            throw new IllegalStateException("You must annotate the test class with '@RunWith(BrowserRunner.class)'");
        }

        // expand variables in expected alerts
        expandExpectedAlertsVariables(url);

        createTestPageForRealBrowserIfNeeded(html, getExpectedAlerts());

        final WebClient client = getWebClientWithMockWebConnection();
        final List<String> collectedAlerts = new ArrayList<>();
        client.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        final MockWebConnection webConnection = getMockWebConnection();
        webConnection.setResponse(url, html);

        final HtmlPage page = client.getPage(url);
        if (waitForJS > 0) {
            assertEquals(0, client.waitForBackgroundJavaScriptStartingBefore(waitForJS));
        }
        assertEquals(getExpectedAlerts(), collectedAlerts);
        return page;
    }

    /**
     * Reads the number of JS threads remaining from unit tests run before.
     * This should be always 0.
     */
    @Before
    public void before() {
        if (webClient_ != null && webClient_.getJavaScriptEngine() instanceof JavaScriptEngine) {
            assertTrue(getJavaScriptThreads().isEmpty());
        }
    }

    /**
     * Cleanup after a test.
     */
    @Override
    @After
    public void releaseResources() {
        super.releaseResources();
        boolean rhino = false;
        if (webClient_ != null) {
            rhino = webClient_.getJavaScriptEngine() instanceof JavaScriptEngine;
            webClient_.close();
            webClient_.getCookieManager().clearCookies();
        }
        webClient_ = null;

        if (rhino) {
            final List<Thread> jsThreads = getJavaScriptThreads();
            assertEquals(0, jsThreads.size());

            // collect stack traces
            // caution: the threads may terminate after the threads have been returned by getJavaScriptThreads()
            // and before stack traces are retrieved
            if (jsThreads.size() > 0) {
                final Map<String, StackTraceElement[]> stackTraces = new HashMap<>();
                for (final Thread t : jsThreads) {
                    final StackTraceElement[] elts = t.getStackTrace();
                    if (elts != null) {
                        stackTraces.put(t.getName(), elts);
                    }
                }

                if (!stackTraces.isEmpty()) {
                    System.err.println("JS threads still running:");
                    for (final Map.Entry<String, StackTraceElement[]> entry : stackTraces.entrySet()) {
                        System.err.println("Thread: " + entry.getKey());
                        final StackTraceElement[] elts = entry.getValue();
                        for (final StackTraceElement elt : elts) {
                            System.err.println(elt);
                        }
                    }
                    throw new RuntimeException("JS threads are still running: " + jsThreads.size());
                }
            }
        }
    }
}
