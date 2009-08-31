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
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.htmlunit.corejs.javascript.NativeArray;
import net.sourceforge.htmlunit.corejs.javascript.Undefined;

import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.junit.AfterClass;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.webapp.WebAppContext;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.htmlunit.HtmlUnitWebElement;
import org.openqa.selenium.ie.InternetExplorerDriver;

import com.gargoylesoftware.htmlunit.html.HtmlElement;

/**
 * Base class for tests using WebDriver.
 * <p>
 * This test runs with HtmlUnit unless the system property "htmlunit.webdriver" is set to "ff2"
 * in which case the test will run in the "real" firefox browser.
 * </p>
 * <p>
 * You can set the property in maven by modifying the POM value of the system properties in maven-surefire-plugin.
 * In eclipse you can change the "Run configurations" -> "Arguments" tab -> "VM arguments"
 * -> "-Dhtmlunit.webdriver=ff2"
 * </p>
 * <p>To change the firefox binary, you can also setup the system property "webdriver.firefox.bin".</p>
 * @version $Revision$
 * @author Marc Guillemot
 * @author Ahmed Ashour
 */
public abstract class WebDriverTestCase extends WebTestCase {

    static final String PROPERTY = "htmlunit.webdriver";

    private static WebDriver WEB_DRIVER_;
    private static Server STATIC_SERVER_;

    private static String JSON_;

    /**
     * Configure the driver only once.
     * @return the driver
     */
    protected WebDriver getWebDriver() {
        if (WEB_DRIVER_ == null) {
            WEB_DRIVER_ = buildWebDriver();
        }
        return WEB_DRIVER_;
    }

    /**
     * Closes the drivers.
     * @throws Exception If an error occurs
     */
    @AfterClass
    public static void shutDownAll() throws Exception {
        if (WEB_DRIVER_ != null) {
            WEB_DRIVER_.close();
        }
        WEB_DRIVER_ = null;
        if (STATIC_SERVER_ != null) {
            STATIC_SERVER_.stop();
        }
        STATIC_SERVER_ = null;
    }

    private WebDriver buildWebDriver() {
        final String property = System.getProperty(PROPERTY, "").toLowerCase();
        if (property.contains("ff2") || property.contains("ff3")) {
            return new FirefoxDriver();
        }
        if (property.contains("ie6") || property.contains("ie7")) {
            return new InternetExplorerDriver();
        }
        final WebClient webClient = getWebClient();
        final HtmlUnitDriver driver = new HtmlUnitDriver(true) {
            @Override
            protected WebClient newWebClient(final BrowserVersion browserVersion) {
                return webClient;
            }

            @Override
            protected WebElement newHtmlUnitWebElement(final HtmlElement element) {
                return new FixedWebDriverHtmlUnitWebElement(this, element);
            }
        };
        return driver;
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
     * @author Marc Guillemot
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
            final URL requestedUrl = new URL(request.getRequestURL().toString());
            final WebRequestSettings settings = new WebRequestSettings(requestedUrl);
            settings.setHttpMethod(HttpMethod.valueOf(request.getMethod()));
            for (final Enumeration<String> en = request.getHeaderNames(); en.hasMoreElements();) {
                final String headerName = en.nextElement();
                final String headerValue = request.getHeader(headerName);
                settings.setAdditionalHeader(headerName, headerValue);
            }
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
     * Same as {@link #loadPageWithAlerts(String)}, but using WebDriver instead.
     * @param html the HTML to use
     * @return the web driver
     * @throws Exception if something goes wrong
     */
    protected final WebDriver loadPageWithAlerts2(final String html) throws Exception {
        final String[] expectedAlerts = getExpectedAlerts();
        final MockWebConnection mockWebConnection = new MockWebConnection();
        mockWebConnection.setDefaultResponse(html);
        startWebServer(mockWebConnection);

        final WebDriver driver = getWebDriver();
        driver.get(URL_FIRST.toExternalForm());

        assertEquals(expectedAlerts, getCollectedAlerts(driver));
        return driver;
    }

    private List<String> getCollectedAlerts(final WebDriver driver) throws Exception {
        final List<String> collectedAlerts = new ArrayList<String>();
        if (driver instanceof HtmlUnitDriver) {
            final Object result = ((JavascriptExecutor) driver) .executeScript("return window.__huCatchedAlerts");
            if (result != Undefined.instance) {
                final NativeArray resp = (NativeArray) result;
                for (int i = 0; i < resp.getLength(); ++i) {
                    collectedAlerts.add(
                            net.sourceforge.htmlunit.corejs.javascript.Context.toString(resp.get(i, resp)));
                }
            }
        }
        else if (driver instanceof InternetExplorerDriver) {
            final String jsonResult = (String) ((JavascriptExecutor) driver)
                .executeScript(getJSON() + ";return JSON.stringify(window.__huCatchedAlerts)");
            if (jsonResult  != null) {
                final JSONArray array = new JSONArray(jsonResult);
                for (int i = 0; i < array.length(); i++) {
                    collectedAlerts.add(net.sourceforge.htmlunit.corejs.javascript.Context.toString(array.get(i)));
                }
            }
        }
        else {
            final JSONArray array = (JSONArray) ((JavascriptExecutor) driver)
                .executeScript("return window.__huCatchedAlerts");
            if (array != null) {
                for (int i = 0; i < array.length(); i++) {
                    collectedAlerts.add(net.sourceforge.htmlunit.corejs.javascript.Context.toString(array.get(i)));
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
