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
package com.gargoylesoftware.htmlunit.javascript.host;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.Browser;
import com.gargoylesoftware.htmlunit.BrowserRunner.Browsers;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * Tests for {@link Navigator}.
 *
 * @version $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author Daniel Gredler
 * @author Marc Guillemot
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class NavigatorTest extends WebDriverTestCase {

    /**
     * Tests the "appCodeName" property.
     * @throws Exception on test failure
     */
    @Test
    public void testAppCodeName() throws Exception {
        testAttribute("appCodeName", getBrowserVersion().getApplicationCodeName());
    }

    /**
     * Tests the "appMinorVersion" property.
     * @throws Exception on test failure
     */
    @Test
    public void testAppMinorVersion() throws Exception {
        testAttribute("appMinorVersion", getBrowserVersion().getApplicationMinorVersion());
    }

    /**
     * Tests the "appName" property.
     * @throws Exception on test failure
     */
    @Test
    public void testAppName() throws Exception {
        testAttribute("appName", getBrowserVersion().getApplicationName());
    }

    /**
     * Tests the "appVersion" property.
     * @throws Exception on test failure
     */
    @Test
    public void testAppVersion() throws Exception {
        testAttribute("appVersion", getBrowserVersion().getApplicationVersion());
    }

    /**
     * Tests the "browserLanguage" property.
     * @throws Exception on test failure
     */
    @Test
    @Browsers(Browser.IE)
    public void testBrowserLanguage_IE() throws Exception {
        testAttribute("browserLanguage", getBrowserVersion().getBrowserLanguage());
    }

    /**
     * Tests the "browserLanguage" property.
     * @throws Exception on test failure
     */
    @Test
    @Browsers(Browser.FF)
    public void testBrowserLanguage_FF() throws Exception {
        testAttribute("browserLanguage", "undefined");
    }

    /**
     * Tests the "productSub" property.
     * @throws Exception on test failure
     */
    @Test
    @Alerts(DEFAULT = { "string", "true" }, IE = { "undefined", "false" })
    public void productSub() throws Exception {
        final String html = "<html><head><script>\n"
            + "alert(typeof(navigator.productSub));\n"
            + "alert(parseInt(navigator.productSub) > 20000101);\n"
            + "</script>\n"
            + "</head><body></body>\n"
            + "</html>";

        loadPageWithAlerts2(html);
    }

    /**
     * Tests the "cookieEnabled" property.
     * @throws Exception on test failure
     */
    @Test
    public void testCookieEnabled() throws Exception {
        testCookieEnabled(true);
        testCookieEnabled(false);
    }

    private void testCookieEnabled(final boolean cookieEnabled) throws Exception {
        final String html
            = "<html><head><title>First</title>\n"
            + "<script>\n"
            + "function test() {\n"
            + "  alert(navigator.cookieEnabled);\n"
            + "}\n"
            + "</script>\n"
            + "</head><body onload='test()'></body>\n"
            + "</html>";

        setExpectedAlerts(Boolean.toString(cookieEnabled));
        if (!cookieEnabled) {
            getWebClient().getCookieManager().setCookiesEnabled(cookieEnabled);
        }

        loadPageWithAlerts2(html);
    }

    /**
     * Tests the "cpuClass" property.
     * @throws Exception on test failure
     */
    @Test
    public void testCpuClass() throws Exception {
        testAttribute("cpuClass", getBrowserVersion().getCpuClass());
    }

    /**
     * Tests the "onLine" property.
     * @throws Exception on test failure
     */
    @Test
    public void testOnLine() throws Exception {
        testAttribute("onLine", String.valueOf(getBrowserVersion().isOnLine()));
    }

    /**
     * Tests the "platform" property.
     * @throws Exception on test failure
     */
    @Test
    public void testPlatform() throws Exception {
        testAttribute("platform", getBrowserVersion().getPlatform());
    }

    /**
     * Tests the "systemLanguage" property.
     * @throws Exception on test failure
     */
    @Test
    public void testSystemLanguage() throws Exception {
        testAttribute("systemLanguage", getBrowserVersion().getSystemLanguage());
    }

    /**
     * Tests the "userAgent" property.
     * @throws Exception on test failure
     */
    @Test
    public void testUserAgent() throws Exception {
        testAttribute("userAgent", getBrowserVersion().getUserAgent());
    }

    /**
     * Tests the "userLanguage" property.
     * @throws Exception on test failure
     */
    @Test
    public void testUserLanguage() throws Exception {
        testAttribute("userLanguage", getBrowserVersion().getUserLanguage());
    }

    /**
     * Tests the "plugins" property.
     * @throws Exception on test failure
     */
    @Test
    public void testPlugins() throws Exception {
        testAttribute("plugins.length", String.valueOf(getBrowserVersion().getPlugins().size()));
    }

    /**
     * Tests the "javaEnabled" method.
     * @throws Exception on test failure
     */
    @Test
    public void testJavaEnabled() throws Exception {
        testAttribute("javaEnabled()", "false");
        final WebClient webClient = getWebClient();
        webClient.setAppletEnabled(true);
        testAttribute(webClient, "javaEnabled()", "true");
    }

    /**
     * Tests the "taintEnabled" property.
     * @throws Exception on test failure
     */
    @Test
    public void testTaintEnabled() throws Exception {
        testAttribute("taintEnabled()", "false");
    }

    /**
     * Generic method for testing the value of a specific navigator attribute.
     * @param name the name of the attribute to test
     * @param value the expected value for the named attribute
     * @throws Exception on test failure
     */
    private void testAttribute(final String name, final String value) throws Exception {
        testAttribute(getWebClient(), name, value);
    }

    /**
     * Generic method for testing the value of a specific navigator attribute.
     * @param webClient the web client to use to load the page
     * @param name the name of the attribute to test
     * @param value the expected value for the named attribute
     * @throws Exception on test failure
     */
    private void testAttribute(final WebClient webClient, final String name, final String value) throws Exception {
        final String html = "<html>\n"
                + "<head>\n"
                + "    <title>test</title>\n"
                + "    <script>\n"
                + "    function doTest(){\n"
                + "       alert('" + name + " = ' + window.navigator." + name + ");\n"
                + "    }\n"
                + "    </script>\n"
                + "</head>\n"
                + "<body onload=\'doTest()\'>\n"
                + "</body>\n"
                + "</html>";

        setExpectedAlerts(name + " = " + value);
        loadPageWithAlerts2(html);
    }

    /**
     * Test language property (only for Mozilla).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(FF = "en-us", IE = "undefined", CHROME = "undefined")
    public void testLanguage() throws Exception {
        final String html
            = "<html><head><title>First</title></head>\n"
            + "<body onload='alert(window.navigator.language)'></body>\n"
            + "</html>";

        loadPageWithAlerts2(html);
    }

    /**
     * Test some Mozilla properties (minimal tests are support is not completed).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "number", "number" })
    public void testMozilla() throws Exception {
        final String html
            = "<html><head><title>First</title>\n"
            + "<script>\n"
            + "function test() {\n"
            + "  alert(typeof window.navigator.mimeTypes.length);\n"
            + "  alert(typeof window.navigator.plugins.length);\n"
            + "}\n"
            + "</script>\n"
            + "</head><body onload='test()'></body>\n"
            + "</html>";

        loadPageWithAlerts2(html);
    }

    /**
     * Test some Mozilla properties (minimal tests are support is not completed).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(FF = "Gecko", IE = "undefined", CHROME = "Gecko")
    public void product() throws Exception {
        final String html
            = "<html><head><title>First</title>\n"
            + "<script>\n"
            + "function test() {\n"
            + "  alert(navigator.product);\n"
            + "}\n"
            + "</script>\n"
            + "</head><body onload='test()'></body>\n"
            + "</html>";

        loadPageWithAlerts2(html);
    }
}
