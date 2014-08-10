/*
 * Copyright (c) 2002-2014 Gargoyle Software Inc.
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
import com.gargoylesoftware.htmlunit.SimpleWebTestCase;
import com.gargoylesoftware.htmlunit.WebClient;

/**
 * Tests for {@link ActiveXObject}.
 *
 * @version $Revision$
 * @author Marc Guillemot
 * @author Ahmed Ashour
 * @author Frank Danek
 */
@RunWith(BrowserRunner.class)
public class ActiveXObjectTest extends SimpleWebTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void activex2() throws Exception {
        if (!getBrowserVersion().isIE()) {
            return;
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
        client.getOptions().setActiveXNative(true);
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
    public static boolean isJacobInstalled() {
        try {
            final Class<?> clazz = Class.forName("com.jacob.activeX.ActiveXComponent");
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

    private Object getProperty(final String activeXName, final String property) throws Exception {
        final Class<?> clazz = Class.forName("com.jacob.activeX.ActiveXComponent");
        final Method method = clazz.getMethod("getProperty", String.class);
        final Object activXComponenet = clazz.getConstructor(String.class).newInstance(activeXName);
        return method.invoke(activXComponenet, property);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void method() throws Exception {
        if (!getBrowserVersion().isIE()) {
            return;
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
        client.getOptions().setActiveXNative(true);
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
    public void setProperty() throws Exception {
        if (!getBrowserVersion().isIE()) {
            return;
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
        client.getOptions().setActiveXNative(true);
        final List<String> collectedAlerts = new ArrayList<String>();
        client.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        final MockWebConnection webConnection = new MockWebConnection();
        webConnection.setResponse(getDefaultUrl(), html);
        client.setWebConnection(webConnection);

        client.getPage(getDefaultUrl());
        assertEquals(expectedAlerts, collectedAlerts);
    }
}
