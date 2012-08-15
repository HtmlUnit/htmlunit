/*
 * Copyright (c) 2002-2012 Gargoyle Software Inc.
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

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.ConnectException;
import java.net.SocketException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * A simple WebTestCase which doesn't require server to run, and doens't use WebDriver.
 *
 * It depends on {@link MockWebConnection} to simulate sending requests to the server.
 *
 * <b>Note that {@link WebDriverTestCase} should be used unless HtmlUnit-specific feature
 * is needed and Selenium does not support it.</b>
 *
 * @version $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author David D. Kilzer
 * @author Marc Guillemot
 * @author Chris Erskine
 * @author Michael Ottati
 * @author Daniel Gredler
 * @author Ahmed Ashour
 */
public abstract class WebTestCase extends AbstractWebTestCase {

    /** Logging support. */
    private static final Log LOG = LogFactory.getLog(WebTestCase.class);

    private WebClient webClient_;

    private int nbJSThreadsBeforeTest_;

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
     * Load a page with the specified HTML and collect alerts into the list.
     * @param browserVersion the browser version to use
     * @param html the HTML to use
     * @param collectedAlerts the list to hold the alerts
     * @return the new page
     * @throws Exception if something goes wrong
     */
    public final HtmlPage loadPage(final BrowserVersion browserVersion,
            final String html, final List<String> collectedAlerts) throws Exception {
        if (generateTest_browserVersion_.get() == null) {
            generateTest_browserVersion_.set(browserVersion);
        }
        return loadPage(browserVersion, html, collectedAlerts, getDefaultUrl());
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
        final BrowserVersion version =
                (getBrowserVersion() != null) ? getBrowserVersion() : BrowserVersion.getDefault();
        return loadPage(version, html, collectedAlerts, getDefaultUrl());
    }

    /**
     * Loads an external URL, accounting for the fact that the remote server may be down or the
     * machine running the tests may not be connected to the internet.
     * @param url the URL to load
     * @return the loaded page, or <tt>null</tt> if there were connectivity issues
     * @throws Exception if an error occurs
     */
    protected static final HtmlPage loadUrl(final String url) throws Exception {
        try {
            final WebClient client = new WebClient();
            client.getOptions().setUseInsecureSSL(true);
            return client.getPage(url);
        }
        catch (final ConnectException e) {
            // The remote server is probably down.
            System.out.println("Connection could not be made to " + url);
            return null;
        }
        catch (final SocketException e) {
            // The local machine may not be online.
            System.out.println("Connection could not be made to " + url);
            return null;
        }
        catch (final UnknownHostException e) {
            // The local machine may not be online.
            System.out.println("Connection could not be made to " + url);
            return null;
        }
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

        return loadPage(client, html, collectedAlerts, getDefaultUrl());
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
     * Runs the calling JUnit test again and fails only if it already runs.<br/>
     * This is helpful for tests that don't currently work but should work one day,
     * when the tested functionality has been implemented.<br/>
     * The right way to use it is:
     * <pre>
     * public void testXXX() {
     *   if (notYetImplemented()) {
     *       return;
     *   }
     *
     *   ... the real (now failing) unit test
     * }
     * </pre>
     * @return <tt>false</tt> when not itself already in the call stack
     */
    protected boolean notYetImplemented() {
        setGenerateTest_notYetImplemented(true);
        if (notYetImplementedFlag.get() != null) {
            return false;
        }
        notYetImplementedFlag.set(Boolean.TRUE);

        final Method testMethod = findRunningJUnitTestMethod();
        try {
            LOG.info("Running " + testMethod.getName() + " as not yet implemented");
            testMethod.invoke(this, (Object[]) new Class[] {});
            Assert.fail(testMethod.getName() + " is marked as not implemented but already works");
        }
        catch (final Exception e) {
            LOG.info(testMethod.getName() + " fails which is normal as it is not yet implemented");
            // method execution failed, it is really "not yet implemented"
        }
        finally {
            notYetImplementedFlag.set(null);
        }

        return true;
    }

    /**
     * Finds from the call stack the active running JUnit test case
     * @return the test case method
     * @throws RuntimeException if no method could be found
     */
    private Method findRunningJUnitTestMethod() {
        final Class<?> cl = getClass();
        final Class<?>[] args = new Class[] {};

        // search the initial junit test
        final Throwable t = new Exception();
        for (int i = t.getStackTrace().length - 1; i >= 0; i--) {
            final StackTraceElement element = t.getStackTrace()[i];
            if (element.getClassName().equals(cl.getName())) {
                try {
                    final Method m = cl.getMethod(element.getMethodName(), args);
                    if (isPublicTestMethod(m)) {
                        return m;
                    }
                }
                catch (final Exception e) {
                    // can't access, ignore it
                }
            }
        }

        throw new RuntimeException("No JUnit test case method found in call stack");
    }

    /**
     * From Junit. Test if the method is a junit test.
     * @param method the method
     * @return <code>true</code> if this is a junit test
     */
    private boolean isPublicTestMethod(final Method method) {
        return method.getParameterTypes().length == 0
            && (method.getName().startsWith("test") || method.getAnnotation(Test.class) != null)
            && method.getReturnType() == Void.TYPE
            && Modifier.isPublic(method.getModifiers());
    }

    private static final ThreadLocal<Boolean> notYetImplementedFlag = new ThreadLocal<Boolean>();

    /**
     * Load the specified resource for the supported browsers and tests
     * that the generated log corresponds to the expected one for this browser.
     *
     * @param fileName the resource name which resides in /resources folder and
     *        belongs to the same package as the test class.
     *
     * @throws Exception if the test fails
     */
    protected void testHTMLFile(final String fileName) throws Exception {
        final String resourcePath = getClass().getPackage().getName().replace('.', '/') + '/' + fileName;
        final URL url = getClass().getClassLoader().getResource(resourcePath);

        final String browserKey = getBrowserVersion().getNickname().substring(0, 2);

        final WebClient client = getWebClient();

        final HtmlPage page = client.getPage(url);
        final HtmlElement want = page.getHtmlElementById(browserKey);

        final HtmlElement got = page.getHtmlElementById("log");

        final List<String> expected = readChildElementsText(want);
        final List<String> actual = readChildElementsText(got);

        Assert.assertEquals(expected, actual);
    }

    private List<String> readChildElementsText(final HtmlElement elt) {
        final List<String> list = new ArrayList<String>();
        for (final DomElement child : elt.getChildElements()) {
            list.add(child.asText());
        }
        return list;
    }

    /**
     * Returns the WebClient instance for the current test with the current {@link BrowserVersion}.
     * @return a WebClient with the current {@link BrowserVersion}
     */
    protected WebClient createNewWebClient() {
        return new WebClient(getBrowserVersion());
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
     * Defines the provided HTML as the response of the MockWebConnection for {@link #getDefaultUrl()}
     * and loads the page with this URL using the current browser version; finally, asserts that the
     * alerts equal the expected alerts (in which "§§URL§§" has been expanded to the default URL).
     * @param html the HTML to use
     * @return the new page
     * @throws Exception if something goes wrong
     */
    protected final HtmlPage loadPageWithAlerts(final String html) throws Exception {
        return loadPageWithAlerts(html, getDefaultUrl(), -1);
    }

    /**
     * Defines the provided HTML as the response of the MockWebConnection for {@link #getDefaultUrl()}
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
        final List<String> collectedAlerts = new ArrayList<String>();
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
     * Reads the number of JS threads remaining from unit tests run before the current one.
     * Ideally it should be always 0.
     */
    @Before
    public void readJSThreadsBeforeTest() {
        nbJSThreadsBeforeTest_ = getJavaScriptThreads().size();
    }

    /**
     * Cleanup after a test.
     */
    @After
    public void releaseResources() {
        super.releaseResources();
        if (webClient_ != null) {
            webClient_.closeAllWindows();
            webClient_.getCookieManager().clearCookies();
        }
        webClient_ = null;

        final List<Thread> jsThreads = getJavaScriptThreads();
        // collect stack traces
        // caution: the threads may terminate after the threads have been returned by getJavaScriptThreads()
        // and before stack traces are retrieved
        if (jsThreads.size() > nbJSThreadsBeforeTest_) {
            final Map<String, StackTraceElement[]> stackTraces = new HashMap<String, StackTraceElement[]>();
            for (final Thread t : jsThreads) {
                final StackTraceElement elts[] = t.getStackTrace();
                if (elts != null) {
                    stackTraces.put(t.getName(), elts);
                }
            }

            if (!stackTraces.isEmpty()) {
                System.err.println("JS threads still running:");
                for (final Map.Entry<String, StackTraceElement[]> entry : stackTraces.entrySet()) {
                    System.err.println("Thread: " + entry.getKey());
                    final StackTraceElement elts[] = entry.getValue();
                    for (final StackTraceElement elt : elts) {
                        System.err.println(elt);
                    }
                }
                throw new RuntimeException("JS threads are still running: " + jsThreads.size());
            }
        }
    }

    /**
     * Gets the active JavaScript threads.
     * @return the threads
     */
    protected List<Thread> getJavaScriptThreads() {
        final Thread[] threads = new Thread[Thread.activeCount() + 10];
        Thread.enumerate(threads);
        final List<Thread> jsThreads = new ArrayList<Thread>();
        for (final Thread t : threads) {
            if (t != null && t.getName().startsWith("JS executor for")) {
                jsThreads.add(t);
            }
        }

        return jsThreads;
    }
}
