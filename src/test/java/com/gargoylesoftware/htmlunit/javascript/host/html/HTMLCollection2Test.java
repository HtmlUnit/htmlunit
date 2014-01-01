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

import static com.gargoylesoftware.htmlunit.javascript.host.xml.XMLDocumentTest.LOAD_XML_DOCUMENT_FROM_FILE_FUNCTION;
import static com.gargoylesoftware.htmlunit.javascript.host.xml.XMLDocumentTest.callLoadXMLDocumentFromFile;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.CollectingAlertHandler;
import com.gargoylesoftware.htmlunit.MockWebConnection;
import com.gargoylesoftware.htmlunit.SimpleWebTestCase;
import com.gargoylesoftware.htmlunit.WebClient;

/**
 * Tests for {@link HTMLCollection}.
 *
 * @version $Revision$
 * @author Marc Guillemot
 * @author Ahmed Ashour
 * @author Frank Danek
 */
@RunWith(BrowserRunner.class)
public class HTMLCollection2Test extends SimpleWebTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("1")
    public void testChildNodes() throws Exception {
        final String firstContent = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var doc = " + callLoadXMLDocumentFromFile("'" + URL_SECOND + "'") + ";\n"
            + "    alert(doc.documentElement.childNodes.length);\n"
            + "  }\n"
            + LOAD_XML_DOCUMENT_FROM_FILE_FUNCTION
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        final String secondContent = "<title>Immortality</title>";

        final List<String> collectedAlerts = new ArrayList<String>();
        final WebClient client = getWebClient();
        client.setAlertHandler(new CollectingAlertHandler(collectedAlerts));
        final MockWebConnection conn = new MockWebConnection();
        conn.setResponse(URL_FIRST, firstContent);
        conn.setResponse(URL_SECOND, secondContent, "text/xml");
        client.setWebConnection(conn);

        client.getPage(URL_FIRST);
        assertEquals(getExpectedAlerts(), collectedAlerts);
    }
}
