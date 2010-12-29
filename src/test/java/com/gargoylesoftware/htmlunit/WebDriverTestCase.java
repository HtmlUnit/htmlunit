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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.htmlunit.corejs.javascript.Context;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.After;
import org.junit.AfterClass;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.security.Constraint;
import org.mortbay.jetty.security.ConstraintMapping;
import org.mortbay.jetty.security.HashUserRealm;
import org.mortbay.jetty.security.SecurityHandler;
import org.mortbay.jetty.servlet.DefaultServlet;
import org.mortbay.jetty.webapp.WebAppClassLoader;
import org.mortbay.jetty.webapp.WebAppContext;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.htmlunit.HtmlUnitWebElement;
import org.openqa.selenium.ie.InternetExplorerDriver;

import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.util.NameValuePair;

/**
 * Base class for tests using WebDriver.
 * <p>
 * By default, this test runs with HtmlUnit, but this behavior can be changed by having a property file named
 * "test.properties" in the HtmlUnit root directory.
 * Sample:
 * <pre>
   browsers=hu,ff3,ff3.6,ie8
   ff3.bin=c:\\location_to_firefox.exe              [Windows]
   ff3.6.bin=/use/bin/firefox                         [Unix-like]
 * </pre>
 * The file should contain three properties: "browsers", "ff3.bin" and "ff3.6.bin".
 * <ul>
 *   <li>browsers: is a comma separated list contains any combination of "hu" (for HtmlUnit with all browser versions),
 *   "hu-ie6", "hu-ie7", "hu-ie8", "hu-ff3", "hu-ff3.6",
 *   "ff3", "ff3.6", "ie6", "ie7", "ie8", which will be used to driver real browsers,
 *   note that you can't define more than one IE as there is no standard way
 *   to have multiple IEs on the same machine</li>
 *   <li>ff3.bin: is the location of the FF3 binary, in Windows use double back-slashes</li>
 *   <li>ff3.6.bin: is the location of the FF3.6 binary, in Windows use double back-slashes</li>
 * </ul>
 * </p>
 *
 * @version $Revision$
 * @author Marc Guillemot
 * @author Ahmed Ashour
 */
public abstract class WebDriverTestCase extends WebTestCase {

    private static final Log LOG = LogFactory.getLog(WebDriverTestCase.class);
    private static List<String> BROWSERS_PROPERTIES_;
    private static String FF3_BIN_;
    private static String FF3_6_BIN_;

    private static Map<BrowserVersion, WebDriver> WEB_DRIVERS_ = new HashMap<BrowserVersion, WebDriver>();
    private static Server STATIC_SERVER_;

    private static String JSON_;
    private boolean useRealBrowser_;
    private boolean writeContentAsBytes_ = false;
    private static Boolean LAST_TEST_MockWebConnection_;

    static List<String> getBrowsersProperties() {
        if (BROWSERS_PROPERTIES_ == null) {
            try {
                final Properties properties = new Properties();
                final File file = new File("test.properties");
                if (file.exists()) {
                    properties.load(new FileInputStream(file));
                    BROWSERS_PROPERTIES_
                        = Arrays.asList(properties.getProperty("browsers", "hu")
                            .replaceAll(" ", "").toLowerCase().split(","));
                    FF3_BIN_ = properties.getProperty("ff3.bin");
                    FF3_6_BIN_ = properties.getProperty("ff3.6.bin");
                }
            }
            catch (final Exception e) {
                LOG.info("Error reading htmlunit.properties", e);
            }
            if (BROWSERS_PROPERTIES_ == null) {
                BROWSERS_PROPERTIES_ = Arrays.asList(new String[] {"hu"});
            }
        }
        return BROWSERS_PROPERTIES_;
    }

    /**
     * Configure the driver only once.
     * @return the driver
     */
    protected WebDriver getWebDriver() {
        final BrowserVersion browserVersion = getBrowserVersion();
        WebDriver driver = WEB_DRIVERS_.get(browserVersion);
        if (driver == null) {
            driver = buildWebDriver();
            // cache driver instances for real browsers but not for HtmlUnit
            if (!(driver instanceof HtmlUnitDriver)) {
                WEB_DRIVERS_.put(browserVersion, driver);
            }
        }
        return driver;
    }

    /**
     * Closes the drivers.
     * @throws Exception If an error occurs
     */
    @AfterClass
    public static void shutDownAll() throws Exception {
        for (WebDriver driver : WEB_DRIVERS_.values()) {
            driver.quit();
        }
        WEB_DRIVERS_.clear();
        stopWebServer();
        LAST_TEST_MockWebConnection_ = null;
    }

    /**
     * Stops the WebServer.
     * @throws Exception if it fails
     */
    protected static void stopWebServer() throws Exception {
        if (STATIC_SERVER_ != null) {
            STATIC_SERVER_.stop();
        }
        STATIC_SERVER_ = null;
    }

    void setUseRealBrowser(final boolean useWebDriver) {
        useRealBrowser_ = useWebDriver;
    }

    private WebDriver buildWebDriver() {
        if (useRealBrowser_) {
            if (getBrowserVersion().isIE()) {
                return new InternetExplorerDriver();
            }
            String ffBinary = null;
            if (getBrowserVersion() == BrowserVersion.FIREFOX_3) {
                ffBinary = FF3_BIN_;
            }
            else if (getBrowserVersion() == BrowserVersion.FIREFOX_3_6) {
                ffBinary = FF3_6_BIN_;
            }
            if (ffBinary != null) {
                return new FirefoxDriver(new FirefoxBinary(new File(ffBinary)), new FirefoxProfile());
            }
            return new FirefoxDriver();
        }
        final WebClient webClient = getWebClient();
        return new HtmlUnitDriver(true) {
            @Override
            protected WebClient newWebClient(final BrowserVersion browserVersion) {
                return webClient;
            }

            @Override
            protected WebElement newHtmlUnitWebElement(final HtmlElement element) {
                return new FixedWebDriverHtmlUnitWebElement(this, element);
            }
        };
    }

    /**
     * Starts the web server delivering response from the provided connection.
     * @param mockConnection the sources for responses
     * @throws Exception if a problem occurs
     */
    private void startWebServer(final MockWebConnection mockConnection) throws Exception {
        if (LAST_TEST_MockWebConnection_ == Boolean.FALSE) {
            stopWebServer();
        }
        LAST_TEST_MockWebConnection_ = Boolean.TRUE;
        if (STATIC_SERVER_ == null) {
            STATIC_SERVER_ = new Server(PORT);

            final WebAppContext context = new WebAppContext();
            context.setContextPath("/");
            context.setResourceBase("./");

            if (isBasicAuthentication()) {
                final Constraint constraint = new Constraint();
                constraint.setName(Constraint.__BASIC_AUTH);
                constraint.setRoles(new String[]{"user"});
                constraint.setAuthenticate(true);

                final ConstraintMapping constraintMapping = new ConstraintMapping();
                constraintMapping.setConstraint(constraint);
                constraintMapping.setPathSpec("/*");

                final SecurityHandler securityHandler = new SecurityHandler();
                securityHandler.setUserRealm(new HashUserRealm("MyRealm", "./src/test/resources/realm.properties"));
                securityHandler.setConstraintMappings(new ConstraintMapping[]{constraintMapping});
                context.addHandler(securityHandler);
            }

            context.addServlet(MockWebConnectionServlet.class, "/*");
            STATIC_SERVER_.setHandler(context);
            STATIC_SERVER_.start();
        }
        MockWebConnectionServlet.MockConnection_ = mockConnection;
        MockWebConnectionServlet.WriteContentAsBytes_ = writeContentAsBytes_;
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
            final Map<String, Class< ? extends Servlet>> servlets) throws Exception {
        stopWebServer();
        LAST_TEST_MockWebConnection_ = Boolean.FALSE;
        STATIC_SERVER_ = new Server(PORT);

        final WebAppContext context = new WebAppContext();
        context.setContextPath("/");
        context.setResourceBase(resourceBase);

        for (final Map.Entry<String, Class< ? extends Servlet>> entry : servlets.entrySet()) {
            final String pathSpec = entry.getKey();
            final Class< ? extends Servlet> servlet = entry.getValue();
            context.addServlet(servlet, pathSpec);

            // disable defaults if someone likes to register his own root servlet
            if ("/".equals(pathSpec)) {
                context.setDefaultsDescriptor(null);
                context.addServlet(DefaultServlet.class, "/favicon.ico");
            }
        }
        final WebAppClassLoader loader = new WebAppClassLoader(context);
        if (classpath != null) {
            for (final String path : classpath) {
                loader.addClassPath(path);
            }
        }
        context.setClassLoader(loader);
        STATIC_SERVER_.setHandler(context);
        STATIC_SERVER_.start();
    }

    /**
     * Servlet delivering content from a MockWebConnection.
     */
    public static class MockWebConnectionServlet extends HttpServlet {
        private static MockWebConnection MockConnection_;
        private static boolean WriteContentAsBytes_ = false;

        /**
         * {@inheritDoc}
         */
        @Override
        protected void service(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException, IOException {

            try {
                doService(request, response);
            }
            catch (final ServletException e) {
                throw e;
            }
            catch (final IOException e) {
                throw e;
            }
            catch (final Exception e) {
                throw new ServletException(e);
            }
        }

        @SuppressWarnings("unchecked")
        private void doService(final HttpServletRequest request, final HttpServletResponse response)
            throws Exception {
            final String url = request.getRequestURL().toString();
            if (url.endsWith("/favicon.ico")) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                return;
            }

            if (url.contains("/delay")) {
                final String delay = StringUtils.substringBetween(url, "/delay", "/");
                final int ms = Integer.parseInt(delay);
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Sleeping for " + ms + " before to deliver " + url);
                }
                Thread.sleep(ms);
            }

            final URL requestedUrl = new URL(url);
            final WebRequest webRequest = new WebRequest(requestedUrl);
            webRequest.setHttpMethod(HttpMethod.valueOf(request.getMethod()));

            // copy headers
            for (final Enumeration<String> en = request.getHeaderNames(); en.hasMoreElements();) {
                final String headerName = en.nextElement();
                final String headerValue = request.getHeader(headerName);
                webRequest.setAdditionalHeader(headerName, headerValue);
            }

            // copy parameters
            final List<NameValuePair> requestParameters = new ArrayList<NameValuePair>();
            for (final Enumeration<String> paramNames = request.getParameterNames(); paramNames.hasMoreElements();) {
                final String name = paramNames.nextElement();
                final String[] values = request.getParameterValues(name);
                for (final String value : values) {
                    requestParameters.add(new NameValuePair(name, value));
                }
            }

            if ("PUT".equals(request.getMethod()) && request.getContentLength() > 0) {
                final byte[] buffer = new byte[request.getContentLength()];
                request.getInputStream().readLine(buffer, 0, buffer.length);
                webRequest.setRequestBody(new String(buffer));
            }
            else {
                webRequest.setRequestParameters(requestParameters);
            }

            final WebResponse resp = MockConnection_.getResponse(webRequest);

            // write WebResponse to HttpServletResponse
            response.setStatus(resp.getStatusCode());

            for (final NameValuePair responseHeader : resp.getResponseHeaders()) {
                response.addHeader(responseHeader.getName(), responseHeader.getValue());
            }

            if (WriteContentAsBytes_) {
                IOUtils.copy(resp.getContentAsStream(), response.getOutputStream());
            }
            else {
                final String newContent = getModifiedContent(resp.getContentAsString());
                response.getWriter().print(newContent);
            }
            response.flushBuffer();
        }
    }

    /**
     * Indicates that MockWebConnectionServlet should send the configured content's bytes directly
     * without modification.
     * @param b the new value
     */
    public void setWriteContentAsBytes_(final boolean b) {
        writeContentAsBytes_ = b;
    }

    /**
     * Returns the modified JavaScript after changing how 'alerts' are called.
     * @param html the html
     * @return the modified html
     */
    protected static String getModifiedContent(final String html) {
        return StringUtils.replace(html, "alert(",
                "(function(t){var x = top.__huCatchedAlerts; x = x ? x : []; "
                + "top.__huCatchedAlerts = x; x.push(String(t))})(");
    }

    /**
     * Same as {@link #loadPageWithAlerts2(String)}... but doesn't verify the alerts.
     * @param html the HTML to use
     * @return the web driver
     * @throws Exception if something goes wrong
     */
    protected final WebDriver loadPage2(final String html) throws Exception {
        return loadPage2(html, URL_FIRST);
    }

    /**
     * Same as {@link #loadPageWithAlerts(String)}... but doesn't verify the alerts.
     * @param html the HTML to use
     * @param url the url to use to load the page
     * @return the web driver
     * @throws Exception if something goes wrong
     */
    protected final WebDriver loadPage2(final String html, final URL url) throws Exception {
        final MockWebConnection mockWebConnection = getMockWebConnection();
        mockWebConnection.setResponse(url, html);
        startWebServer(mockWebConnection);

        final WebDriver driver = getWebDriver();
        driver.get(url.toExternalForm());

        return driver;
    }

    /**
     * Same as {@link #loadPageWithAlerts(String)}, but using WebDriver instead.
     * @param html the HTML to use
     * @return the web driver
     * @throws Exception if something goes wrong
     */
    protected final WebDriver loadPageWithAlerts2(final String html) throws Exception {
        return loadPageWithAlerts2(html, URL_FIRST, 1000);
    }

    /**
     * Same as {@link #loadPageWithAlerts(String)}, but using WebDriver instead.
     * @param html the HTML to use
     * @param maxWaitTime the maximum time to wait to get the alerts (im ms)
     * @return the web driver
     * @throws Exception if something goes wrong
     */
    protected final WebDriver loadPageWithAlerts2(final String html, final long maxWaitTime) throws Exception {
        return loadPageWithAlerts2(html, URL_FIRST, maxWaitTime);
    }

    /**
     * Same as {@link #loadPageWithAlerts(String)}, but using WebDriver instead.
     * @param html the HTML to use
     * @param url the URL to use to load the page
     * @return the web driver
     * @throws Exception if something goes wrong
     */
    protected final WebDriver loadPageWithAlerts2(final String html, final URL url) throws Exception {
        return loadPageWithAlerts2(html, url, 1000);
    }

    /**
     * Same as {@link #loadPageWithAlerts(String)}, but using WebDriver instead.
     * @param html the HTML to use
     * @param url the URL to use to load the page
     * @param maxWaitTime the maximum time to wait to get the alerts (im ms)
     * @return the web driver
     * @throws Exception if something goes wrong
     */
    protected final WebDriver loadPageWithAlerts2(final String html, final URL url, final long maxWaitTime)
        throws Exception {
        expandExpectedAlertsVariables(URL_FIRST);
        final String[] expectedAlerts = getExpectedAlerts();

        final WebDriver driver = loadPage2(html, url);

        verifyAlerts(maxWaitTime, expectedAlerts, driver);
        return driver;
    }

    private void verifyAlerts(final long maxWaitTime, final String[] expectedAlerts, final WebDriver driver)
        throws Exception {
        // gets the collected alerts, waiting a bit if necessary
        List<String> actualAlerts = getCollectedAlerts(driver);
        final long maxWait = System.currentTimeMillis() + maxWaitTime;
        while (actualAlerts.size() < expectedAlerts.length && System.currentTimeMillis() < maxWait) {
            Thread.sleep(30);
            actualAlerts = getCollectedAlerts(driver);
        }

        assertEquals(expectedAlerts, actualAlerts);
    }

    /**
     * Same as {@link #loadPageWithAlerts(String)}, but using WebDriver instead.
     * @param html the HTML to use for the default response
     * @param servlets the additional servlets to configure with their mapping
     * @return the web driver
     * @throws Exception if something goes wrong
     */
    protected final WebDriver loadPageWithAlerts2(final String html,
            final Map<String, Class<? extends Servlet>> servlets) throws Exception {
        return loadPageWithAlerts2(html, getDefaultUrl(), 1000, servlets);
    }

    /**
     * Same as {@link #loadPageWithAlerts(String)}, but using WebDriver instead.
     * @param html the HTML to use for the default page
     * @param url the URL to use to load the page
     * @param maxWaitTime the maximum time to wait to get the alerts (im ms)
     * @param servlets the additional servlets to configure with their mapping
     * @return the web driver
     * @throws Exception if something goes wrong
     */
    protected final WebDriver loadPageWithAlerts2(final String html, final URL url, final long maxWaitTime,
            final Map<String, Class<? extends Servlet>> servlets) throws Exception {

        expandExpectedAlertsVariables(getDefaultUrl());
        final String[] expectedAlerts = getExpectedAlerts();

        servlets.put("/*", MockWebConnectionServlet.class);
        getMockWebConnection().setResponse(url, html);
        MockWebConnectionServlet.MockConnection_ = getMockWebConnection();

        startWebServer("./", null, servlets);

        final WebDriver driver = getWebDriver();
        driver.get(url.toExternalForm());

        verifyAlerts(maxWaitTime, expectedAlerts, driver);
        return driver;
    }

    /**
     * Loads the provided URL serving responses from {@link #getMockWebConnection()}
     * and verifies that the captured alerts are correct.
     * @param url the URL to use to load the page
     * @return the web driver
     * @throws Exception if something goes wrong
     */
    protected final WebDriver loadPageWithAlerts2(final URL url) throws Exception {
        expandExpectedAlertsVariables(url);
        final String[] expectedAlerts = getExpectedAlerts();

        startWebServer(getMockWebConnection());

        final WebDriver driver = getWebDriver();
        driver.get(url.toExternalForm());

        assertEquals(expectedAlerts, getCollectedAlerts(driver));
        return driver;
    }

    /**
     * Gets the alerts collected by the driver.
     * Note: it currently works only if no new page has been loaded in the window
     * @param driver the driver
     * @return the collected alerts
     * @throws Exception in case of problem
     */
    @SuppressWarnings("unchecked")
    protected List<String> getCollectedAlerts(final WebDriver driver) throws Exception {
        final List<String> collectedAlerts = new ArrayList<String>();
        final JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
        if (driver instanceof HtmlUnitDriver) {
            final Object result = jsExecutor.executeScript("return top.__huCatchedAlerts");
            if (result != null) {
                return (List<String>) result;
            }
        }
        else if (driver instanceof InternetExplorerDriver) {
            final String jsonResult = (String) jsExecutor
                .executeScript(getJSON() + ";return JSON.stringify(top.__huCatchedAlerts)");
            if (jsonResult != null) {
                final JSONArray array = new JSONArray(jsonResult);
                for (int i = 0; i < array.length(); i++) {
                    collectedAlerts.add(Context.toString(array.get(i)));
                }
            }
        }
        else {
            final Object object = jsExecutor.executeScript("return top.__huCatchedAlerts");

            if (object != null) {
                if (object instanceof JSONObject) {
                    final JSONObject jsonObject = (JSONObject) object;
                    for (int i = 0; i < jsonObject.length(); i++) {
                        collectedAlerts.add(Context.toString(jsonObject.get(String.valueOf(i))));
                    }
                }
                else if (object instanceof JSONArray) {
                    final JSONArray array = (JSONArray) object;
                    for (int i = 0; i < array.length(); i++) {
                        collectedAlerts.add(Context.toString(array.get(i)));
                    }
                }
                else {
                    for (final Object alert : (List<Object>) object) {
                        collectedAlerts.add(Context.toString(alert));
                    }
                }
            }
        }
        return collectedAlerts;
    }

    private String getJSON() {
        if (JSON_ == null) {
            try {
                final StringBuilder builder = new StringBuilder();
                final File file = new File(getClass().getClassLoader().getResource("json2.js").toURI());
                for (final Object line : FileUtils.readLines(file)) {
                    builder.append(line).append('\n');
                }
                JSON_ = builder.toString();
            }
            catch (final Exception e) {
                throw new RuntimeException(e);
            }
        }
        return JSON_;
    }

    /**
     * Returns the HtmlElement of the specified WebElement.
     * @param webElement the webElement
     * @return the HtmlElement
     */
    protected HtmlElement toHtmlElement(final WebElement webElement) {
        try {
            final Field field = HtmlUnitWebElement.class.getDeclaredField("element");
            field.setAccessible(true);
            return (HtmlElement) field.get(webElement);
        }
        catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Release resources but DON'T close the browser if we are running with a real browser.
     * Note that HtmlUnitDriver instances are not cached.
     */
    @After
    @Override
    public void releaseResources() {
        super.releaseResources();

        if (useRealBrowser_) {
            final WebDriver driver = getWebDriver();
            final String currentWindow = driver.getWindowHandle();

            // close all windows except the current one
            for (final String handle : driver.getWindowHandles()) {
                if (!currentWindow.equals(handle)) {
                    driver.switchTo().window(handle).close();
                }
            }

            // reset cookies to have a clean state
            driver.manage().deleteAllCookies();

            // in the remaining window, load a blank page
            driver.get("about:blank");
        }
    }
}

/**
 * As HtmlUnit didn't generate the right events, WebDriver did it for us, but now that we do it correctly,
 * WebDriver shouldn't do it anymore
 * http://code.google.com/p/webdriver/issues/detail?id=93
 */
class FixedWebDriverHtmlUnitWebElement extends HtmlUnitWebElement {

    public FixedWebDriverHtmlUnitWebElement(final HtmlUnitDriver parent, final HtmlElement element) {
        super(parent, element);
    }

    @Override
    public void click() {
        try {
            getElement().click();
        }
        catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }
}
