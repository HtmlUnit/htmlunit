/*
 * Copyright (c) 2002-2008 Gargoyle Software Inc.
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

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.CollectingAlertHandler;
import com.gargoylesoftware.htmlunit.MockWebConnection;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebTestCase;

/**
 * Tests for {@link Navigator}.
 *
 * @version $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author Daniel Gredler
 * @author Marc Guillemot
 * @author Ahmed Ashour
 */
public class NavigatorTest extends WebTestCase {

    /**
     * Tests the "appCodeName" property.
     * @throws Exception on test failure
     */
    @Test
    public void testAppCodeName() throws Exception {
        testAttribute("appCodeName", BrowserVersion.getDefault().getApplicationCodeName());
    }

    /**
     * Tests the "appMinorVersion" property.
     * @throws Exception on test failure
     */
    @Test
    public void testAppMinorVersion() throws Exception {
        testAttribute("appMinorVersion", BrowserVersion.getDefault().getApplicationMinorVersion());
    }

    /**
     * Tests the "appName" property.
     * @throws Exception on test failure
     */
    @Test
    public void testAppName() throws Exception {
        testAttribute("appName", BrowserVersion.getDefault().getApplicationName());
    }

    /**
     * Tests the "appVersion" property.
     * @throws Exception on test failure
     */
    @Test
    public void testAppVersion() throws Exception {
        testAttribute("appVersion", BrowserVersion.getDefault().getApplicationVersion());
    }

    /**
     * Tests the "browserLanguage" property.
     * @throws Exception on test failure
     */
    @Test
    public void testBrowserLanguage() throws Exception {
        testAttribute("browserLanguage", BrowserVersion.getDefault().getBrowserLanguage());
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
        final String content
            = "<html><head><title>First</title></head>\n"
            + "<script>\n"
            + "function test()\n"
            + "{\n"
            + "  alert(navigator.cookieEnabled);\n"
            + "}\n"
            + "</script>\n"
            + "<body onload='test()'></body>\n"
            + "</html>";

        final String[] expectedAlerts = {Boolean.toString(cookieEnabled)};
        final WebClient webClient = new WebClient();
        if (!cookieEnabled) {
            webClient.getCookieManager().setCookiesEnabled(cookieEnabled);
        }
        final MockWebConnection webConnection = new MockWebConnection(webClient);

        final List<String> collectedAlerts = new ArrayList<String>();
        webClient.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        webConnection.setDefaultResponse(content);
        webClient.setWebConnection(webConnection);

        webClient.getPage(URL_FIRST);
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * Tests the "cpuClass" property.
     * @throws Exception on test failure
     */
    @Test
    public void testCpuClass() throws Exception {
        testAttribute("cpuClass", BrowserVersion.getDefault().getCpuClass());
    }

    /**
     * Tests the "onLine" property.
     * @throws Exception on test failure
     */
    @Test
    public void testOnLine() throws Exception {
        testAttribute("onLine", String.valueOf(BrowserVersion.getDefault().isOnLine()));
    }

    /**
     * Tests the "platform" property.
     * @throws Exception on test failure
     */
    @Test
    public void testPlatform() throws Exception {
        testAttribute("platform", BrowserVersion.getDefault().getPlatform());
    }

    /**
     * Tests the "systemLanguage" property.
     * @throws Exception on test failure
     */
    @Test
    public void testSystemLanguage() throws Exception {
        testAttribute("systemLanguage", BrowserVersion.getDefault().getSystemLanguage());
    }

    /**
     * Tests the "userAgent" property.
     * @throws Exception on test failure
     */
    @Test
    public void testUserAgent() throws Exception {
        testAttribute("userAgent", BrowserVersion.getDefault().getUserAgent());
    }

    /**
     * Tests the "userLanguage" property.
     * @throws Exception on test failure
     */
    @Test
    public void testUserLanguage() throws Exception {
        testAttribute("userLanguage", BrowserVersion.getDefault().getUserLanguage());
    }

    /**
     * Tests the "plugins" property.
     * @throws Exception on test failure
     */
    @Test
    public void testPlugins() throws Exception {
        testAttribute("plugins.length", "0");
    }

    /**
     * Tests the "javaEnabled" method.
     * @throws Exception on test failure
     */
    @Test
    public void testJavaEnabled() throws Exception {
        testAttribute("javaEnabled()", "false");
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
        final String content = "<html>\n"
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
        final List<String> collectedAlerts = new ArrayList<String>();
        loadPage(content, collectedAlerts);
        final String[] expectedAlerts = {name + " = " + value};
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * Test closing using JavaScript.
     * @throws Exception if the test fails
     */
    @Test
    public void testUseConfiguredBrowser() throws Exception {
        final WebClient webClient = new WebClient(BrowserVersion.FIREFOX_2);
        final MockWebConnection webConnection = new MockWebConnection(webClient);

        final List<String> collectedAlerts = new ArrayList<String>();
        webClient.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        final String content
            = "<html><head><title>First</title></head>\n"
            + "<body onload='alert(window.navigator.appName)'></body>\n"
            + "</html>";

        webConnection.setDefaultResponse(content);
        webClient.setWebConnection(webConnection);

        webClient.getPage(URL_FIRST);

        final String[] expectedAlerts = {"Netscape"};
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * Test language property (only for Mozilla).
     * @throws Exception if the test fails
     */
    @Test
    public void testLanguage() throws Exception {
        final WebClient webClient = new WebClient(BrowserVersion.FIREFOX_2);
        final MockWebConnection webConnection = new MockWebConnection(webClient);

        final List<String> collectedAlerts = new ArrayList<String>();
        webClient.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        final String content
            = "<html><head><title>First</title></head>\n"
            + "<body onload='alert(window.navigator.language)'></body>\n"
            + "</html>";

        webConnection.setDefaultResponse(content);
        webClient.setWebConnection(webConnection);

        webClient.getPage(URL_FIRST);

        final String[] expectedAlerts = {BrowserVersion.FIREFOX_2.getBrowserLanguage()};
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * Test some Mozilla properties (minimal tests are support is not completed).
     * @throws Exception if the test fails
     */
    @Test
    public void testMozilla() throws Exception {
        final String content
            = "<html><head><title>First</title></head>\n"
            + "<script>\n"
            + "function test()\n"
            + "{\n"
            + "  alert(typeof window.navigator.mimeTypes.length);\n"
            + "  alert(typeof window.navigator.plugins.length);\n"
            + "}\n"
            + "</script>\n"
            + "<body onload='test()'></body>\n"
            + "</html>";

        final String[] expectedAlerts = {"number", "number"};
        createTestPageForRealBrowserIfNeeded(content, expectedAlerts);

        final List<String> collectedAlerts = new ArrayList<String>();
        loadPage(BrowserVersion.FIREFOX_2, content, collectedAlerts);

        assertEquals(expectedAlerts, collectedAlerts);
    }
    /**
     * Test some Mozilla properties (minimal tests are support is not completed).
     * @throws Exception if the test fails
     */
    @Test
    public void product() throws Exception {
        final String content
            = "<html><head><title>First</title></head>\n"
            + "<script>\n"
            + "function test() {\n"
            + "  alert(navigator.product);\n"
            + "}\n"
            + "</script>\n"
            + "<body onload='test()'></body>\n"
            + "</html>";

        final String[] expectedAlerts = {"Gecko"};
        final List<String> collectedAlerts = new ArrayList<String>();
        loadPage(BrowserVersion.FIREFOX_2, content, collectedAlerts);
        assertEquals(expectedAlerts, collectedAlerts);
    }
}
