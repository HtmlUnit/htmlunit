/*
 * Copyright (c) 2002-2009 Gargoyle Software Inc.
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
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.NativeArray;
import net.sourceforge.htmlunit.corejs.javascript.Undefined;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.AfterClass;
import org.mortbay.jetty.Server;
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
   browsers=hu,ff2,ff3,ie8
   ff2.bin=c:\\location_to_firefox.exe              [Windows]
   ff3.bin=/use/bin/firefox                         [Unix-like]
 * </pre>
 * The file should contain three properties: "browsers", "ff2.bin" and "ff3.bin".
 * <ul>
 *   <li>browsers: is a comma separated list contains any combination of "hu" (for HtmlUnit),
 *   "ff2", "ff3", "ie6", "ie7", "ie8", which will be used to driver real browsers,
 *   note that you can't define more than one IE as there is no standard way
 *   to have multiple IEs on the same machine</li>
 *   <li>ff2.bin: is the location of the FF2 binary, in Windows use double back-slashes</li>
 *   <li>ff3.bin: is the location of the FF3 binary, in Windows use double back-slashes</li>
 * </ul>
 * </p>
 *
 * @version $Revision$
 * @author Marc Guillemot
 * @author Ahmed Ashour
 */
public abstract class WebDriverTestCase extends WebTestCase {

    private static final Log LOG = LogFactory.getLog(WebDriverTestCase.class);
    private static String BROWSERS_PROPERTY_;
    private static String FF2_BIN_;
    private static String FF3_BIN_;

    private static Map<BrowserVersion, WebDriver> WEB_DRIVERS_ = new HashMap<BrowserVersion, WebDriver>();
    private static Server STATIC_SERVER_;

    private static String JSON_;
    private boolean useWebDriver_;

    static String getBrowsersProperty() {
        if (BROWSERS_PROPERTY_ == null) {
            try {
                final Properties properties = new Properties();
                final File file = new File("test.properties");
                if (file.exists()) {
                    properties.load(new FileInputStream(file));
                    BROWSERS_PROPERTY_ = properties.getProperty("browsers", "hu").toLowerCase();
                    FF2_BIN_ = properties.getProperty("ff2.bin");
                    FF3_BIN_ = properties.getProperty("ff3.bin");
                }
            }
            catch (final Exception e) {
                LOG.info("Error reading htmlunit.properties", e);
            }
            if (BROWSERS_PROPERTY_ == null) {
                BROWSERS_PROPERTY_ = "hu";
            }
        }
        return BROWSERS_PROPERTY_;
    }

    /**
     * Configure the driver only once.
     * @return the driver
     */
    protected WebDriver getWebDriver() {
        final BrowserVersion browserVersion = getBrowserVersion();
        if (!WEB_DRIVERS_.containsKey(browserVersion)) {
            WEB_DRIVERS_.put(browserVersion, buildWebDriver());
        }
        return WEB_DRIVERS_.get(browserVersion);
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
        if (STATIC_SERVER_ != null) {
            STATIC_SERVER_.stop();
        }
        STATIC_SERVER_ = null;
    }

    void setUseWebDriver(final boolean useWebDriver) {
        useWebDriver_ = useWebDriver;
    }

    private WebDriver buildWebDriver() {
        if (useWebDriver_) {
            if (getBrowserVersion().isIE()) {
                return new InternetExplorerDriver();
            }
            if (getBrowserVersion().getNickname().equals("FF2")) {
                return new FirefoxDriver(new FirefoxBinary(new File(FF2_BIN_)), new FirefoxProfile());
            }
            return new FirefoxDriver(new FirefoxBinary(new File(FF3_BIN_)), new FirefoxProfile());
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
        if (STATIC_SERVER_ == null) {
            STATIC_SERVER_ = new Server(PORT);

            final WebAppContext context = new WebAppContext();
            context.setContextPath("/");
            context.setResourceBase("./");

            context.addServlet(MockWebConnectionServlet.class, "/*");
            STATIC_SERVER_.setHandler(context);
            STATIC_SERVER_.start();
        }
        MockWebConnectionServlet.MockConnection_ = mockConnection;
    }

    /**
     * Servlet delivering content from a MockWebConnection.
     */
    public static class MockWebConnectionServlet extends HttpServlet {
        private static final long serialVersionUID = -3417522859381706421L;
        private static MockWebConnection MockConnection_;

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
                LOG.debug("Sleeping for " + ms + " before to deliver " + url);
                Thread.sleep(ms);
            }

            final URL requestedUrl = new URL(url);
            final WebRequestSettings settings = new WebRequestSettings(requestedUrl);
            settings.setHttpMethod(HttpMethod.valueOf(request.getMethod()));

            // copy headers
            for (final Enumeration<String> en = request.getHeaderNames(); en.hasMoreElements();) {
                final String headerName = en.nextElement();
                final String headerValue = request.getHeader(headerName);
                settings.setAdditionalHeader(headerName, headerValue);
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
            settings.setRequestParameters(requestParameters);

            final WebResponse resp = MockConnection_.getResponse(settings);

            // write WebResponse to HttpServletResponse
            for (final NameValuePair responseHeader : resp.getResponseHeaders()) {
                response.addHeader(responseHeader.getName(), responseHeader.getValue());
            }

            final String newContent = StringUtils.replace(resp.getContentAsString(), "alert(",
                "(function(t){var x = top.__huCatchedAlerts; x = x ? x : []; "
                + "top.__huCatchedAlerts = x; x.push(String(t))})(");

            response.getWriter().print(newContent);
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
        return loadPageWithAlerts2(html, URL_FIRST);
    }

    /**
     * Same as {@link #loadPageWithAlerts(String)}, but using WebDriver instead.
     * @param html the HTML to use
     * @param url the URL to use to load the page
     * @return the web driver
     * @throws Exception if something goes wrong
     */
    protected final WebDriver loadPageWithAlerts2(final String html, final URL url) throws Exception {
        expandExpectedAlertsVariables(URL_FIRST);
        final String[] expectedAlerts = getExpectedAlerts();
        createTestPageForRealBrowserIfNeeded(html, expectedAlerts); // still useful sometimes

        final WebDriver driver = loadPage2(html, url);

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
    protected List<String> getCollectedAlerts(final WebDriver driver) throws Exception {
        final List<String> collectedAlerts = new ArrayList<String>();
        if (driver instanceof HtmlUnitDriver) {
            final Object result = ((JavascriptExecutor) driver) .executeScript("return window.__huCatchedAlerts");
            if (result != Undefined.instance) {
                final NativeArray resp = (NativeArray) result;
                for (int i = 0; i < resp.getLength(); ++i) {
                    collectedAlerts.add(Context.toString(resp.get(i, resp)));
                }
            }
        }
        else if (driver instanceof InternetExplorerDriver) {
            final String jsonResult = (String) ((JavascriptExecutor) driver)
                .executeScript(getJSON() + ";return JSON.stringify(window.__huCatchedAlerts)");
            if (jsonResult  != null) {
                final JSONArray array = new JSONArray(jsonResult);
                for (int i = 0; i < array.length(); i++) {
                    collectedAlerts.add(Context.toString(array.get(i)));
                }
            }
        }
        else {
            final Object object = ((JavascriptExecutor) driver) .executeScript("return window.__huCatchedAlerts");

            if (object instanceof JSONObject) {
                final JSONObject jsonObject = (JSONObject) object;
                for (int i = 0; i < jsonObject.length(); i++) {
                    collectedAlerts.add(Context.toString(jsonObject.get(String.valueOf(i))));
                }
            }
            else {
                final JSONArray array = (JSONArray) object;
                if (array != null) {
                    for (int i = 0; i < array.length(); i++) {
                        collectedAlerts.add(Context.toString(array.get(i)));
                    }
                }
            }
        }
        return collectedAlerts;
    }

    /**
     * Same as {@link #loadPageWithAlerts2(String)}, but using a MockConnection instead.
     * @param conn the connection to use
     * @return the web driver
     * @throws Exception if something goes wrong
     */
    protected final WebDriver loadPageWithAlerts2(final MockWebConnection conn) throws Exception {
        final String[] expectedAlerts = getExpectedAlerts();
        startWebServer(conn);

        final WebDriver driver = getWebDriver();
        driver.get(URL_FIRST.toExternalForm());

        assertEquals(expectedAlerts, getCollectedAlerts(driver));
        return driver;
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
