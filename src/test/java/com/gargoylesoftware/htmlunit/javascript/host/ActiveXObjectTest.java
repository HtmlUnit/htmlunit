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
package com.gargoylesoftware.htmlunit.javascript.host;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.CollectingAlertHandler;
import com.gargoylesoftware.htmlunit.MockWebConnection;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebTestCase;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.Browser;
import com.gargoylesoftware.htmlunit.BrowserRunner.Browsers;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;

/**
 * Tests for {@link ActiveXObject}.
 *
 * @version $Revision$
 * @author Marc Guillemot
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class ActiveXObjectTest extends WebTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(Browser.NONE)
    public void xmlHttpRequestFlavours() throws Exception {
        assertFalse(ActiveXObject.isXMLHttpRequest(null));
        assertFalse(ActiveXObject.isXMLHttpRequest("foo"));
        assertTrue(ActiveXObject.isXMLHttpRequest("Microsoft.XMLHTTP"));
        assertTrue(ActiveXObject.isXMLHttpRequest("Msxml2.XMLHTTP"));
        assertTrue(ActiveXObject.isXMLHttpRequest("Msxml2.XMLHTTP.3.0"));
        assertTrue(ActiveXObject.isXMLHttpRequest("Msxml2.XMLHTTP.4.0"));
        assertTrue(ActiveXObject.isXMLHttpRequest("Msxml2.XMLHTTP.5.0"));
        assertTrue(ActiveXObject.isXMLHttpRequest("Msxml2.XMLHTTP.6.0"));
        assertTrue(ActiveXObject.isXMLDocument("Microsoft.XmlDom"));
        assertTrue(ActiveXObject.isXMLDocument("MSXML4.DOMDocument"));
        assertTrue(ActiveXObject.isXMLDocument("Msxml2.DOMDocument.6.0"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(Browser.IE)
    public void xmlDocument() throws Exception {
        final String content = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var doc = new ActiveXObject('Microsoft.XMLDOM');\n"
            + "    alert(doc);\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        final String[] expectedAlerts = {"[object]"};
        final List<String> collectedAlerts = new ArrayList<String>();
        loadPage(getBrowserVersion(), content, collectedAlerts);
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(Browser.IE)
    @Alerts("exception: Automation server can't create object")
    @NotYetImplemented
    public void activex() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    try {\n"
            + "      var ie = new ActiveXObject('InternetExplorer.Application');\n"
            + "      alert(ie);\n"
            + "    } catch(e) {alert('exception: ' + e.message);}\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageWithAlerts(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(Browser.IE)
    public void activex2() throws Exception {
        if (!getBrowserVersion().isIE()) {
            throw new Exception();
        }
        if (!isJacobInstalled()) {
            return;
        }
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    try {\n"
            + "      var ie = new ActiveXObject('InternetExplorer.Application');\n"
            + "      alert(ie.FullName);\n"
            + "    } catch(e) {alert('exception: ' + e.message);}\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        final String[] expectedAlerts = {getProperty("InternetExplorer.Application", "FullName").toString()};
        createTestPageForRealBrowserIfNeeded(html, expectedAlerts);

        final WebClient client = getWebClient();
        client.setActiveXNative(true);
        final List<String> collectedAlerts = new ArrayList<String>();
        client.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        final MockWebConnection webConnection = new MockWebConnection();
        webConnection.setResponse(getDefaultUrl(), html);
        client.setWebConnection(webConnection);

        client.getPage(getDefaultUrl());
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * Returns true if Jacob is installed, so we can use {@link WebClient#setActiveXNative(boolean)}.
     * @return whether Jacob is installed or not
     */
    @SuppressWarnings("unchecked")
    public static boolean isJacobInstalled() {
        try {
            final Class clazz = Class.forName("com.jacob.activeX.ActiveXComponent");
            final Method method = clazz.getMethod("getProperty", String.class);
            final Object activXComponenet =
                clazz.getConstructor(String.class).newInstance("InternetExplorer.Application");
            method.invoke(activXComponenet, "Busy");
            return true;
        }
        catch (final Exception e) {
            return false;
        }
    }

    @SuppressWarnings("unchecked")
    private Object getProperty(final String activeXName, final String property) throws Exception {
        final Class clazz = Class.forName("com.jacob.activeX.ActiveXComponent");
        final Method method = clazz.getMethod("getProperty", String.class);
        final Object activXComponenet = clazz.getConstructor(String.class).newInstance(activeXName);
        return method.invoke(activXComponenet, property);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(Browser.IE)
    public void method() throws Exception {
        if (!getBrowserVersion().isIE()) {
            throw new Exception();
        }
        if (!isJacobInstalled()) {
            return;
        }
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    try {\n"
            + "      var ie = new ActiveXObject('InternetExplorer.Application');\n"
            + "      ie.PutProperty('Hello', 'There');\n"
            + "      alert(ie.GetProperty('Hello'));\n"
            + "    } catch(e) {alert('exception: ' + e.message);}\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        final String[] expectedAlerts = {"There"};
        createTestPageForRealBrowserIfNeeded(html, expectedAlerts);

        final WebClient client = getWebClient();
        client.setActiveXNative(true);
        final List<String> collectedAlerts = new ArrayList<String>();
        client.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        final MockWebConnection webConnection = new MockWebConnection();
        webConnection.setResponse(getDefaultUrl(), html);
        client.setWebConnection(webConnection);

        client.getPage(getDefaultUrl());
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(Browser.IE)
    public void setProperty() throws Exception {
        if (!getBrowserVersion().isIE()) {
            throw new Exception();
        }
        if (!isJacobInstalled()) {
            return;
        }
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    try {\n"
            + "      var ie = new ActiveXObject('InternetExplorer.Application');\n"
            + "      var full = ie.FullScreen;\n"
            + "      ie.FullScreen = true;\n"
            + "      alert(ie.FullScreen);\n"
            + "      ie.FullScreen = full;\n"
            + "    } catch(e) {alert('exception: ' + e.message);}\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        final String[] expectedAlerts = {"true"};
        createTestPageForRealBrowserIfNeeded(html, expectedAlerts);

        final WebClient client = getWebClient();
        client.setActiveXNative(true);
        final List<String> collectedAlerts = new ArrayList<String>();
        client.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        final MockWebConnection webConnection = new MockWebConnection();
        webConnection.setResponse(getDefaultUrl(), html);
        client.setWebConnection(webConnection);

        client.getPage(getDefaultUrl());
        assertEquals(expectedAlerts, collectedAlerts);
    }
}
