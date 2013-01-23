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
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.jetty.security.ConstraintMapping;
import org.eclipse.jetty.security.ConstraintSecurityHandler;
import org.eclipse.jetty.security.HashLoginService;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerWrapper;
import org.eclipse.jetty.util.security.Constraint;
import org.eclipse.jetty.webapp.WebAppContext;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.After;
import org.junit.AfterClass;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.htmlunit.HtmlUnitWebElement;
import org.openqa.selenium.ie.InternetExplorerDriver;

import com.gargoylesoftware.htmlunit.MockWebConnection.RawResponseData;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.util.NameValuePair;

/**
 * Base class for tests using WebDriver.
 * <p>
 * By default, this test runs with HtmlUnit, but this behavior can be changed by having a property file named
 * "test.properties" in the HtmlUnit root directory.
 * Sample:
 * <pre>
   browsers=hu,ff17,ie9
   ff3.6.bin=c:\\location_to_firefox.exe                [Windows]
   ff17.bin=/usr/bin/firefox                            [Unix-like]
   chrome.bin=/path/to/chromedriver                     [Unix-like]
 * </pre>
 * The file should contain four properties: "browsers", "ff3.6.bin", "ff17.bin", and "chrome.bin".
 * <ul>
 *   <li>browsers: is a comma separated list contains any combination of "hu" (for HtmlUnit with all browser versions),
 *   "hu-ie6", "hu-ie7", "hu-ie8", "hu-ie9", "hu-ff3.6", "hu-ff17",
 *   "ff3.6", "ff17", "ie6", "ie7", "ie8", "ie9", "chrome", which will be used to driver real browsers,
 *   note that you can't define more than one IE as there is no standard way
 *   to have multiple IEs on the same machine</li>
 *   <li>ff3.6.bin: is the location of the FF3.6 binary, in Windows use double back-slashes</li>
 *   <li>ff17.bin: is the location of the FF17 binary, in Windows use double back-slashes</li>
 *   <li>chrome.bin: is the location of the ChromeDriver binary (see
 *   <a href="http://code.google.com/p/chromedriver/downloads/list">Chrome Driver downloads</a></li>
 * </ul>
 * </p>
 * <p>For IE, please download <a href="http://code.google.com/p/selenium/downloads/list">IEDriverServer.exe</a>
 * and add it to the path.</p>
 *
 * @version $Revision$
 * @author Marc Guillemot
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
public abstract class WebDriverTestCase extends WebTestCase {

    private static final Log LOG = LogFactory.getLog(WebDriverTestCase.class);

    private static List<String> BROWSERS_PROPERTIES_;
    private static String FF3_6_BIN_;
    private static String FF10_BIN_;
    private static String FF17_BIN_;
    private static String CHROME_BIN_;

    /** The driver cache. */
    protected static final Map<BrowserVersion, WebDriver> WEB_DRIVERS_ = new HashMap<BrowserVersion, WebDriver>();
    private static Server STATIC_SERVER_;
    // second server for cross-origin tests.
    private static Server STATIC_SERVER2_;
    // third server for multi-origin cross-origin tests.
    private static Server STATIC_SERVER3_;
    private static ChromeDriverService CHROME_SERVICE_;

    private static String JSON_;
    private boolean useRealBrowser_;
    private static Boolean LAST_TEST_MockWebConnection_;

    private WebClient webClient_;

    /**
     * Override this function in a test class to ask for STATIC_SERVER2_ to be set up.
     * @return true if two servers are needed.
     */
    protected boolean needThreeConnections() {
        return false;
    }

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
                    FF3_6_BIN_ = properties.getProperty("ff3.6.bin");
                    FF10_BIN_ = properties.getProperty("ff10.bin");
                    FF17_BIN_ = properties.getProperty("ff17.bin");
                    CHROME_BIN_ = properties.getProperty("chrome.bin");
                }
            }
            catch (final Exception e) {
                LOG.error("Error reading htmlunit.properties. Ignoring!", e);
            }
            if (BROWSERS_PROPERTIES_ == null) {
                BROWSERS_PROPERTIES_ = Arrays.asList("hu");
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
            try {
                driver = buildWebDriver();
            }
            catch (final IOException e) {
                throw new RuntimeException(e);
            }
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
        stopWebServers();
        LAST_TEST_MockWebConnection_ = null;
    }

    /**
     * Stops all WebServers.
     * @throws Exception if it fails
     */
    protected static void stopWebServers() throws Exception {
        if (STATIC_SERVER_ != null) {
            STATIC_SERVER_.stop();
        }
        if (STATIC_SERVER2_ != null) {
            STATIC_SERVER2_.stop();
        }
        if (STATIC_SERVER3_ != null) {
            STATIC_SERVER3_.stop();
        }
        STATIC_SERVER_ = null;
        STATIC_SERVER2_ = null;
        STATIC_SERVER3_ = null;
    }

    void setUseRealBrowser(final boolean useWebDriver) {
        useRealBrowser_ = useWebDriver;
    }

    /**
     * Builds a new WebDriver instance.
     * @return the instance
     * @throws IOException in case of exception
     */
    protected WebDriver buildWebDriver() throws IOException {
        if (useRealBrowser_) {
            if (getBrowserVersion().isIE()) {
                return new InternetExplorerDriver();
            }
            if (BrowserVersion.CHROME.equals(getBrowserVersion())) {
                if (CHROME_SERVICE_ == null) {
                    CHROME_SERVICE_ = new ChromeDriverService.Builder()
                        .usingDriverExecutable(new File(CHROME_BIN_))
                        .usingAnyFreePort()
                        .build();
                    CHROME_SERVICE_.start();
                }
                return new ChromeDriver(CHROME_SERVICE_);
            }
            if (!getBrowserVersion().isFirefox()) {
                throw new RuntimeException("Unexpected BrowserVersion: " + getBrowserVersion());
            }

            String ffBinary = null;
            if (getBrowserVersion() == BrowserVersion.FIREFOX_3_6) {
                ffBinary = FF3_6_BIN_;
            }
            else if (getBrowserVersion() == BrowserVersion.FIREFOX_10) {
                ffBinary = FF10_BIN_;
            }
            else if (getBrowserVersion() == BrowserVersion.FIREFOX_17) {
                ffBinary = FF17_BIN_;
            }
            if (ffBinary != null) {
                return new FirefoxDriver(new FirefoxBinary(new File(ffBinary)), new FirefoxProfile());
            }
            return new FirefoxDriver();
        }
        if (webClient_ == null) {
            webClient_ = new WebClient(getBrowserVersion());
        }
        return new HtmlUnitDriver(true) {
            @Override
            protected WebClient newWebClient(final BrowserVersion browserVersion) {
                return webClient_;
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
    protected void startWebServer(final MockWebConnection mockConnection) throws Exception {
        if (Boolean.FALSE.equals(LAST_TEST_MockWebConnection_)) {
            stopWebServers();
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

                final ConstraintSecurityHandler handler = (ConstraintSecurityHandler) context.getSecurityHandler();
                handler.setLoginService(new HashLoginService("MyRealm", "./src/test/resources/realm.properties"));
                handler.setConstraintMappings(new ConstraintMapping[]{constraintMapping});
            }

            context.addServlet(MockWebConnectionServlet.class, "/*");
            STATIC_SERVER_.setHandler(context);
            STATIC_SERVER_.start();
        }
        MockWebConnectionServlet.MockConnection_ = mockConnection;

        if (STATIC_SERVER2_ == null && needThreeConnections()) {
            STATIC_SERVER2_ = new Server(PORT2);
            final WebAppContext context2 = new WebAppContext();
            context2.setContextPath("/");
            context2.setResourceBase("./");
            context2.addServlet(MockWebConnectionServlet.class, "/*");
            STATIC_SERVER2_.setHandler(context2);
            STATIC_SERVER2_.start();

            STATIC_SERVER3_ = new Server(PORT3);
            final WebAppContext context3 = new WebAppContext();
            context3.setContextPath("/");
            context3.setResourceBase("./");
            context3.addServlet(MockWebConnectionServlet.class, "/*");
            STATIC_SERVER3_.setHandler(context3);
            STATIC_SERVER3_.start();
            /*
             * The mock connection servlet call sit under both servers, so long as tests
             * keep the URLs distinct.
             */
        }
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
            final Map<String, Class<? extends Servlet>> servlets) throws Exception {
        startWebServer(resourceBase, classpath, servlets, null);
    }

    /**
     * Starts the <b>second</b> web server on the default {@link #PORT2}.
     * The given resourceBase is used to be the ROOT directory that serves the default context.
     * <p><b>Don't forget to stop the returned HttpServer after the test</b>
     *
     * @param resourceBase the base of resources for the default context
     * @param classpath additional classpath entries to add (may be null)
     * @param servlets map of {String, Class} pairs: String is the path spec, while class is the class
     * @throws Exception if the test fails
     */
    protected void startWebServer2(final String resourceBase, final String[] classpath,
            final Map<String, Class<? extends Servlet>> servlets) throws Exception {

        STATIC_SERVER2_ = WebServerTestCase.createWebServer(PORT2, resourceBase, classpath, servlets, null);
    }

    /**
     * Starts the web server on the default {@link #PORT}.
     * The given resourceBase is used to be the ROOT directory that serves the default context.
     * <p><b>Don't forget to stop the returned Server after the test</b>
     *
     * @param resourceBase the base of resources for the default context
     * @param classpath additional classpath entries to add (may be null)
     * @param servlets map of {String, Class} pairs: String is the path spec, while class is the class
     * @param handler wrapper for handler (can be null)
     * @throws Exception if the test fails
     */
    protected void startWebServer(final String resourceBase, final String[] classpath,
            final Map<String, Class<? extends Servlet>> servlets, final HandlerWrapper handler) throws Exception {
        stopWebServers();
        LAST_TEST_MockWebConnection_ = Boolean.FALSE;

        STATIC_SERVER_ = WebServerTestCase.createWebServer(PORT, resourceBase, classpath, servlets, handler);
    }

    /**
     * Servlet delivering content from a MockWebConnection.
     */
    public static class MockWebConnectionServlet extends HttpServlet {
        private static MockWebConnection MockConnection_;

        static void setMockconnection(final MockWebConnection connection) {
            MockConnection_ = connection;
        }

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

        private void doService(final HttpServletRequest request, final HttpServletResponse response)
            throws Exception {
            String url = request.getRequestURL().toString();
            if (LOG.isDebugEnabled()) {
                LOG.debug(request.getMethod() + " " + url);
            }

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

            // copy parameters
            final List<NameValuePair> requestParameters = new ArrayList<NameValuePair>();
            try {
                for (final Enumeration<String> paramNames = request.getParameterNames();
                        paramNames.hasMoreElements();) {
                    final String name = paramNames.nextElement();
                    final String[] values = request.getParameterValues(name);
                    for (final String value : values) {
                        requestParameters.add(new NameValuePair(name, value));
                    }
                }
            }
            catch (final IllegalArgumentException e) {
                // Jetty 8.1.7 throws it in getParameterNames for a query like "cb=%%RANDOM_NUMBER%%"
                // => we should use a more low level test server
                requestParameters.clear();
                final String query = request.getQueryString();
                if (query != null) {
                    url += "?" + query;
                }
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

            if ("PUT".equals(request.getMethod()) && request.getContentLength() > 0) {
                final byte[] buffer = new byte[request.getContentLength()];
                request.getInputStream().readLine(buffer, 0, buffer.length);
                webRequest.setRequestBody(new String(buffer));
            }
            else {
                webRequest.setRequestParameters(requestParameters);
            }

            final RawResponseData resp = MockConnection_.getRawResponse(webRequest);

            // write WebResponse to HttpServletResponse
            response.setStatus(resp.getStatusCode());

            for (final NameValuePair responseHeader : resp.getHeaders()) {
                response.addHeader(responseHeader.getName(), responseHeader.getValue());
            }

            if (resp.getByteContent() != null) {
                response.getOutputStream().write(resp.getByteContent());
            }
            else {
                final String newContent = getModifiedContent(resp.getStringContent());
                final String contentCharset = resp.getCharset();
                response.setCharacterEncoding(contentCharset);
                response.getWriter().print(newContent);
            }
            response.flushBuffer();
        }
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
        return loadPage2(html, url, "text/html;charset=ISO-8859-1", TextUtil.DEFAULT_CHARSET);
    }

    /**
     * Same as {@link #loadPageWithAlerts(String)}... but doesn't verify the alerts.
     * @param html the HTML to use
     * @param url the url to use to load the page
     * @param contentType the content type to return
     * @param charset the name of a supported charset
     * @return the web driver
     * @throws Exception if something goes wrong
     */
    protected final WebDriver loadPage2(final String html, final URL url,
            final String contentType, final String charset) throws Exception {
        final MockWebConnection mockWebConnection = getMockWebConnection();
        mockWebConnection.setResponse(url, html, contentType, charset);
        startWebServer(mockWebConnection);

        final WebDriver driver = getWebDriver();
        if (!(driver instanceof HtmlUnitDriver)) {
            driver.manage().window().setSize(new Dimension(1272, 768));
        }
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
        return loadPageWithAlerts2(html, URL_FIRST, DEFAULT_WAIT_TIME);
    }

    /**
     * Same as {@link #loadPageWithAlerts(String)}, but using WebDriver instead.
     * @param html the HTML to use
     * @param maxWaitTime the maximum time to wait to get the alerts (in millis)
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
        return loadPageWithAlerts2(html, url, DEFAULT_WAIT_TIME);
    }

    /**
     * Same as {@link #loadPageWithAlerts(String)}, but using WebDriver instead.
     * @param html the HTML to use
     * @param url the URL to use to load the page
     * @param maxWaitTime the maximum time to wait to get the alerts (in millis)
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

    /**
     * Verifies the captured alerts.
     * @param maxWaitTime the maximum time to wait for the expected alert to be found
     * @param expectedAlerts the expected alerts
     * @param driver the driver instance
     * @throws Exception in case of failure
     */
    protected void verifyAlerts(final long maxWaitTime, final String[] expectedAlerts, final WebDriver driver)
        throws Exception {
        // gets the collected alerts, waiting a bit if necessary
        List<String> actualAlerts = getCollectedAlerts(driver);
        final long maxWait = System.currentTimeMillis() + maxWaitTime;
        while (actualAlerts.size() != expectedAlerts.length && System.currentTimeMillis() < maxWait) {
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
        return loadPageWithAlerts2(html, getDefaultUrl(), DEFAULT_WAIT_TIME, servlets);
    }

    /**
     * Same as {@link #loadPageWithAlerts(String)}, but using WebDriver instead.
     * @param html the HTML to use for the default page
     * @param url the URL to use to load the page
     * @param maxWaitTime the maximum time to wait to get the alerts (in millis)
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
        return loadPageWithAlerts2(url, 0);
    }

    /**
     * Same as {@link #loadPageWithAlerts2(URL)}, but using with timeout.
     * @param url the URL to use to load the page
     * @param maxWaitTime the maximum time to wait to get the alerts (in millis)
     * @return the web driver
     * @throws Exception if something goes wrong
     */
    protected final WebDriver loadPageWithAlerts2(final URL url, final long maxWaitTime) throws Exception {
        expandExpectedAlertsVariables(url);
        final String[] expectedAlerts = getExpectedAlerts();

        startWebServer(getMockWebConnection());

        final WebDriver driver = getWebDriver();
        driver.get(url.toExternalForm());

        verifyAlerts(maxWaitTime, expectedAlerts, driver);
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
     * @see #getWebWindowOf(HtmlUnitDriver)
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
        if (webClient_ != null) {
            webClient_.closeAllWindows();
            webClient_.getCookieManager().clearCookies();
        }
        webClient_ = null;

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

    /**
     * Returns the underlying WebWindow of the specified driver.
     *
     * <b>Your test shouldn't depend primarily on WebClient</b>
     *
     * @param driver the driver
     * @return the current web window
     * @throws Exception if an error occurs
     * @see #toHtmlElement(WebElement)
     */
    protected WebWindow getWebWindowOf(final HtmlUnitDriver driver) throws Exception {
        final Field field = HtmlUnitDriver.class.getDeclaredField("currentWindow");
        field.setAccessible(true);
        return (WebWindow) field.get(driver);
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
