/*
 * Copyright (c) 2002-2021 Gargoyle Software Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * https://www.apache.org/licenses/LICENSE-2.0
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
import com.gargoylesoftware.htmlunit.WebClientOptions;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * Tests for {@link ActiveXObject}.
 *
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
        final String html = "<html><head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    try {\n"
            + "      var ie = new ActiveXObject('InternetExplorer.Application');\n"
            + "      document.title = ie.FullName;\n"
            + "    } catch(e) {document.title = 'exception: ' + e.message;}\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        final WebClient client = getWebClient();
        client.getOptions().setActiveXNative(true);

        final MockWebConnection webConnection = new MockWebConnection();
        webConnection.setResponse(URL_FIRST, html);
        client.setWebConnection(webConnection);

        final String expectedAlerts = getProperty("InternetExplorer.Application", "FullName").toString();
        final HtmlPage page = client.getPage(URL_FIRST);
        assertEquals(expectedAlerts, page.getTitleText());
    }

    /**
     * Returns true if Jacob is installed, so we can use {@link WebClientOptions#setActiveXNative(boolean)}.
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

    private static Object getProperty(final String activeXName, final String property) throws Exception {
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
        final String html = "<html><head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    try {\n"
            + "      var ie = new ActiveXObject('InternetExplorer.Application');\n"
            + "      ie.PutProperty('Hello', 'There');\n"
            + "      document.tile = ie.GetProperty('Hello'));\n"
            + "    } catch(e) {document.title = 'exception: ' + e.message;}\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        final WebClient client = getWebClient();
        client.getOptions().setActiveXNative(true);
        final List<String> collectedAlerts = new ArrayList<>();
        client.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        final MockWebConnection webConnection = new MockWebConnection();
        webConnection.setResponse(URL_FIRST, html);
        client.setWebConnection(webConnection);

        final HtmlPage page = client.getPage(URL_FIRST);
        assertEquals("There", page.getTitleText());
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

        final WebClient client = getWebClient();
        client.getOptions().setActiveXNative(true);
        final List<String> collectedAlerts = new ArrayList<>();
        client.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        final MockWebConnection webConnection = new MockWebConnection();
        webConnection.setResponse(URL_FIRST, html);
        client.setWebConnection(webConnection);

        final HtmlPage page = client.getPage(URL_FIRST);
        assertEquals("True", page.getTitleText());
    }
}
