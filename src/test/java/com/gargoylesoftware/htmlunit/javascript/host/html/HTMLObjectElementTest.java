/*
 * Copyright (c) 2002-2015 Gargoyle Software Inc.
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
import com.gargoylesoftware.htmlunit.javascript.MockActiveXObject;

/**
 * Unit tests for {@link HTMLObjectElement}.
 *
 * @version $Revision$
 * @author Daniel Gredler
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class HTMLObjectElementTest extends SimpleWebTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "undefined",
            IE = { "ActiveX is working!", "Javascript called this method!" })
    public void classid() throws Exception {
        final String clsid = "clsid:TESTING-CLASS-ID";
        final String html = "<html><head>\n"
            + "<object id='id1' classid='" + clsid + "'></object>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var obj = document.all.id1;"
            + "    alert(obj.MESSAGE);\n"
            + "    if (obj.GetMessage) {\n"
            + "      alert(obj.GetMessage());\n"
            + "    }\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
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
    @Alerts(IE = { "Javascript called this method!", "ActiveX is still alive" })
    public void activeXInteraction() throws Exception {
        final String clsid = "clsid:TESTING-CLASS-ID";
        final String html = "<html><head>\n"
            + "<object id='id1' classid='" + clsid + "'></object>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var obj = document.all.id1;"
            + "    if (obj.GetMessage) {\n"
            + "      alert(obj.GetMessage());\n"
            + "    }\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body>\n"
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
        final HTMLObjectElement jsElem = (HTMLObjectElement) elem.getScriptObject();
        final MockActiveXObject activeX = (MockActiveXObject) jsElem.unwrap();
        if (null != activeX) {
            activeX.setMessage("ActiveX is still alive");
            page.getHtmlElementById("myButton").click();
        }

        assertEquals(getExpectedAlerts(), collectedAlerts);
    }
}
