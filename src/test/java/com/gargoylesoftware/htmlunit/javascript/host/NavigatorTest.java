/*
 * Copyright (c) 2002-2007 Gargoyle Software Inc. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 3. The end-user documentation included with the redistribution, if any, must
 *    include the following acknowledgment:
 *
 *       "This product includes software developed by Gargoyle Software Inc.
 *        (http://www.GargoyleSoftware.com/)."
 *
 *    Alternately, this acknowledgment may appear in the software itself, if
 *    and wherever such third-party acknowledgments normally appear.
 * 4. The name "Gargoyle Software" must not be used to endorse or promote
 *    products derived from this software without prior written permission.
 *    For written permission, please contact info@GargoyleSoftware.com.
 * 5. Products derived from this software may not be called "HtmlUnit", nor may
 *    "HtmlUnit" appear in their name, without prior written permission of
 *    Gargoyle Software Inc.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL GARGOYLE
 * SOFTWARE INC. OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
 * OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.gargoylesoftware.htmlunit.javascript.host;

import java.util.ArrayList;
import java.util.List;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.CollectingAlertHandler;
import com.gargoylesoftware.htmlunit.MockWebConnection;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebTestCase;

/**
 * Tests for Navigator.
 *
 * @version $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author Daniel Gredler
 * @author Marc Guillemot
 * @author Ahmed Ashour
 */
public class NavigatorTest extends WebTestCase {

    /**
     * Create an instance.
     * @param name The name of the test.
     */
    public NavigatorTest( final String name ) {
        super(name);
    }

    /**
     * Tests the "appCodeName" property.
     * @throws Exception on test failure.
     */
    public void testAppCodeName() throws Exception {
        testAttribute("appCodeName", BrowserVersion.getDefault().getApplicationCodeName());
    }

    /**
     * Tests the "appMinorVersion" property.
     * @throws Exception on test failure.
     */
    public void testAppMinorVersion() throws Exception {
        testAttribute("appMinorVersion", BrowserVersion.getDefault().getApplicationMinorVersion());
    }

    /**
     * Tests the "appName" property.
     * @throws Exception on test failure.
     */
    public void testAppName() throws Exception {
        testAttribute("appName", BrowserVersion.getDefault().getApplicationName());
    }

    /**
     * Tests the "appVersion" property.
     * @throws Exception on test failure.
     */
    public void testAppVersion() throws Exception {
        testAttribute("appVersion", BrowserVersion.getDefault().getApplicationVersion());
    }

    /**
     * Tests the "browserLanguage" property.
     * @throws Exception on test failure.
     */
    public void testBrowserLanguage() throws Exception {
        testAttribute("browserLanguage", BrowserVersion.getDefault().getBrowserLanguage());
    }

    /**
     * Tests the "cookieEnabled" property.
     * @throws Exception on test failure.
     */
    public void testCookieEnabled() throws Exception {
        testCookieEnabled(true);
        testCookieEnabled(false);
    }

    private void testCookieEnabled(final boolean cookieEnabled) throws Exception {
        final String content
            = "<html><head><title>First</title></head>"
            + "<script>"
            + "function test()"
            + "{"
            + "  alert(navigator.cookieEnabled);"
            + "}"
            + "</script>"
            + "<body onload='test()'></body>"
            + "</html>";

        final String[] expectedAlerts = {Boolean.toString(cookieEnabled)};
        final WebClient webClient = new WebClient();
        if (!cookieEnabled) {
            webClient.setCookiesEnabled(cookieEnabled);
        }
        final MockWebConnection webConnection = new MockWebConnection(webClient);

        final List collectedAlerts = new ArrayList();
        webClient.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        webConnection.setDefaultResponse(content);
        webClient.setWebConnection(webConnection);

        webClient.getPage(URL_FIRST);
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * Tests the "cpuClass" property.
     * @throws Exception on test failure.
     */
    public void testCpuClass() throws Exception {
        testAttribute("cpuClass", BrowserVersion.getDefault().getCpuClass());
    }

    /**
     * Tests the "onLine" property.
     * @throws Exception on test failure.
     */
    public void testOnLine() throws Exception {
        testAttribute("onLine", String.valueOf(BrowserVersion.getDefault().isOnLine()));
    }

    /**
     * Tests the "platform" property.
     * @throws Exception on test failure.
     */
    public void testPlatform() throws Exception {
        testAttribute("platform", BrowserVersion.getDefault().getPlatform());
    }

    /**
     * Tests the "systemLanguage" property.
     * @throws Exception on test failure.
     */
    public void testSystemLanguage() throws Exception {
        testAttribute("systemLanguage", BrowserVersion.getDefault().getSystemLanguage());
    }

    /**
     * Tests the "userAgent" property.
     * @throws Exception on test failure.
     */
    public void testUserAgent() throws Exception {
        testAttribute("userAgent", BrowserVersion.getDefault().getUserAgent());
    }

    /**
     * Tests the "userLanguage" property.
     * @throws Exception on test failure.
     */
    public void testUserLanguage() throws Exception {
        testAttribute("userLanguage", BrowserVersion.getDefault().getUserLanguage());
    }

    /**
     * Tests the "plugins" property.
     * @throws Exception on test failure.
     */
    public void testPlugins() throws Exception {
        testAttribute("plugins.length", "0");
    }

    /**
     * Tests the "javaEnabled" method.
     * @throws Exception on test failure.
     */
    public void testJavaEnabled() throws Exception {
        testAttribute("javaEnabled()", "false");
    }

    /**
     * Tests the "taintEnabled" property.
     * @throws Exception on test failure.
     */
    public void testTaintEnabled() throws Exception {
        testAttribute("taintEnabled()", "false");
    }

    /**
     * Generic method for testing the value of a specific navigator attribute.
     * @param name the name of the attribute to test.
     * @param value the expected value for the named attribute.
     * @throws Exception on test failure.
     */
    private void testAttribute(final String name, final String value) throws Exception {
        final String content = "<html>\n" +
                "<head>\n" +
                "    <title>test</title>\n" +
                "    <script>\n" +
                "    function doTest(){\n" +
                "       alert('" + name + " = ' + window.navigator." + name + ");\n" +
                "    }\n" +
                "    </script>\n" +
                "</head>\n" +
                "<body onload=\'doTest()\'>\n" +
                "</body>\n" +
                "</html>";
        final List collectedAlerts = new ArrayList();
        loadPage(content, collectedAlerts);
        final String[] expectedAlerts = {name + " = " + value};
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * Test closing using javascript
     * @throws Exception if the test fails.
     */
    public void testUseConfiguredBrowser() throws Exception {

        final WebClient webClient = new WebClient(BrowserVersion.FIREFOX_2);
        final MockWebConnection webConnection = new MockWebConnection(webClient);

        final List collectedAlerts = new ArrayList();
        webClient.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        final String content
            = "<html><head><title>First</title></head>"
            + "<body onload='alert(window.navigator.appName)'></body>"
            + "</html>";

        webConnection.setDefaultResponse(content);
        webClient.setWebConnection(webConnection);

        webClient.getPage(URL_FIRST);

        final String[] expectedAlerts = {"Netscape"};
        assertEquals(expectedAlerts, collectedAlerts);
    }
    
    /**
     * Test language property (only for Mozilla)
     * @throws Exception if the test fails.
     */
    public void testLanguage() throws Exception {

        final WebClient webClient = new WebClient(BrowserVersion.FIREFOX_2);
        final MockWebConnection webConnection = new MockWebConnection(webClient);

        final List collectedAlerts = new ArrayList();
        webClient.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        final String content
            = "<html><head><title>First</title></head>"
            + "<body onload='alert(window.navigator.language)'></body>"
            + "</html>";

        webConnection.setDefaultResponse(content);
        webClient.setWebConnection(webConnection);

        webClient.getPage(URL_FIRST);

        final String[] expectedAlerts = {BrowserVersion.FIREFOX_2.getBrowserLanguage()};
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * Test some Mozilla properties (minimal tests are support is not completed)
     * @throws Exception if the test fails.
     */
    public void testMozilla() throws Exception {

        final String content
            = "<html><head><title>First</title></head>"
            + "<script>"
            + "function test()"
            + "{"
            + "  alert(typeof window.navigator.mimeTypes.length);"
            + "  alert(typeof window.navigator.plugins.length);"
            + "}"
            + "</script>"
            + "<body onload='test()'></body>"
            + "</html>";

        final String[] expectedAlerts = {"number", "number"};
        createTestPageForRealBrowserIfNeeded(content, expectedAlerts);

        final List collectedAlerts = new ArrayList();
        loadPage(BrowserVersion.FIREFOX_2, content, collectedAlerts);

        assertEquals(expectedAlerts, collectedAlerts);
    }
}
