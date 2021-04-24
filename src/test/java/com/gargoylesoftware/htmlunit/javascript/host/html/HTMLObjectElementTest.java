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
package com.gargoylesoftware.htmlunit.javascript.host.html;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.CollectingAlertHandler;
import com.gargoylesoftware.htmlunit.SimpleWebTestCase;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPageTest;
import com.gargoylesoftware.htmlunit.javascript.MockActiveXObject;

/**
 * Unit tests for {@link HTMLObjectElement}.
 *
 * @author Daniel Gredler
 * @author Ahmed Ashour
 * @author Ronald Brill
 * @author Frank Danek
 */
@RunWith(BrowserRunner.class)
public class HTMLObjectElementTest extends SimpleWebTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "undefined",
            IE = {"ActiveX is working!", "Javascript called this method!"})
    public void classid() throws Exception {
        final String clsid = "clsid:TESTING-CLASS-ID";
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var obj = document.all.id1;\n"
            + "    alert(obj.MESSAGE);\n"
            + "    if (obj.GetMessage) {\n"
            + "      alert(obj.GetMessage());\n"
            + "    }\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <object id='id1' classid='" + clsid + "'></object>\n"
            + "</body></html>";

        final WebClient client = getWebClientWithMockWebConnection();
        final Map<String, String> activeXObjectMap = new HashMap<>();
        activeXObjectMap.put(clsid, "com.gargoylesoftware.htmlunit.javascript.MockActiveXObject");
        client.setActiveXObjectMap(activeXObjectMap);

        loadPageWithAlerts(html);
    }

    /**
     * Simple hack to proof, that a test driver can manipulate
     * a activeX mock at runtime.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {},
            IE = {"Javascript called this method!", "ActiveX is still alive"})
    public void activeXInteraction() throws Exception {
        final String clsid = "clsid:TESTING-CLASS-ID";
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var obj = document.all.id1;\n"
            + "    if (obj.GetMessage) {\n"
            + "      alert(obj.GetMessage());\n"
            + "    }\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body>\n"
            + "  <object id='id1' classid='" + clsid + "'></object>\n"
            + "  <button id='myButton' onClick='test()'>Click Me</button>\n"
            + "</body></html>";

        final WebClient client = getWebClientWithMockWebConnection();
        final Map<String, String> activeXObjectMap = new HashMap<>();
        activeXObjectMap.put(clsid, "com.gargoylesoftware.htmlunit.javascript.MockActiveXObject");
        client.setActiveXObjectMap(activeXObjectMap);

        final List<String> collectedAlerts = new ArrayList<>();
        client.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        final HtmlPage page = loadPage(html);

        page.getHtmlElementById("myButton").click();

        final HtmlElement elem = page.getHtmlElementById("id1");
        final HTMLObjectElement jsElem = (HTMLObjectElement) elem.getScriptableObject();
        final MockActiveXObject activeX = (MockActiveXObject) jsElem.unwrap();
        if (null != activeX) {
            activeX.setMessage("ActiveX is still alive");
            page.getHtmlElementById("myButton").click();
        }

        assertEquals(getExpectedAlerts(), collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"undefined", "string", "test"})
    public void activeXUnknownProperty() throws Exception {
        final String clsid = "clsid:TESTING-CLASS-ID";
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var obj = document.all.id1;\n"
            + "    alert(typeof obj.unknown);\n"
            + "    obj.unknown = 'test';\n"
            + "    alert(typeof obj.unknown);\n"
            + "    alert(obj.unknown);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test();'>\n"
            + "  <object id='id1' classid='" + clsid + "'></object>\n"
            + "</body></html>";

        final WebClient client = getWebClientWithMockWebConnection();
        final Map<String, String> activeXObjectMap = new HashMap<>();
        activeXObjectMap.put(clsid, "com.gargoylesoftware.htmlunit.javascript.MockActiveXObject");
        client.setActiveXObjectMap(activeXObjectMap);

        loadPageWithAlerts(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"undefined", "function", "test"})
    public void activeXUnknownMethod() throws Exception {
        final String clsid = "clsid:TESTING-CLASS-ID";
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var obj = document.all.id1;\n"
            + "    alert(typeof obj.unknown);\n"
            + "    obj.unknown = function() { return 'test'; };\n"
            + "    alert(typeof obj.unknown);\n"
            + "    alert(obj.unknown());\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test();'>\n"
            + "  <object id='id1' classid='" + clsid + "'></object>\n"
            + "</body></html>";

        final WebClient client = getWebClientWithMockWebConnection();
        final Map<String, String> activeXObjectMap = new HashMap<>();
        activeXObjectMap.put(clsid, "com.gargoylesoftware.htmlunit.javascript.MockActiveXObject");
        client.setActiveXObjectMap(activeXObjectMap);

        loadPageWithAlerts(html);
    }
}
