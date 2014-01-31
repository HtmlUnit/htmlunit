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
import com.gargoylesoftware.htmlunit.MockWebConnection;
import com.gargoylesoftware.htmlunit.SimpleWebTestCase;
import com.gargoylesoftware.htmlunit.WebClient;

/**
 * Unit tests for {@link HTMLObjectElement}.
 *
 * @version $Revision$
 * @author Daniel Gredler
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class HTMLObjectElementTest extends SimpleWebTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE = { "[object]", "null" }, FF = { "[object HTMLFormElement]", "null" })
    public void form() throws Exception {
        final String html
            = "<html><body><form><object id='o1'></object></form><object id='o2'></object><script>\n"
            + "alert(document.getElementById('o1').form);\n"
            + "alert(document.getElementById('o2').form);\n"
            + "</script></body></html>";
        loadPageWithAlerts(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "undefined", IE = "ActiveX is working!")
    public void classid() throws Exception {
        final String clsid = "clsid:TESTING-CLASS-ID";
        final String html = "<html><head>\n"
            + "<object id='id1' classid='" + clsid + "'></object>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    alert(document.all.id1.MESSAGE);\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "</body></html>";

        final WebClient client = getWebClient();
        final Map<String, String> activeXObjectMap = new HashMap<String, String>();
        activeXObjectMap.put(clsid,
                "com.gargoylesoftware.htmlunit.javascript.MockActiveXObject");
        client.setActiveXObjectMap(activeXObjectMap);

        final List<String> collectedAlerts = new ArrayList<String>();
        client.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        final MockWebConnection webConnection = new MockWebConnection();
        webConnection.setResponse(getDefaultUrl(), html);
        client.setWebConnection(webConnection);

        client.getPage(getDefaultUrl());
        assertEquals(getExpectedAlerts(), collectedAlerts);
    }
}
