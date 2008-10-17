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
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.CollectingAlertHandler;
import com.gargoylesoftware.htmlunit.MockWebConnection;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebTestCase;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;

/**
 * Tests for {@link HTMLDocument}.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class DocumentTypeTest extends WebTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @NotYetImplemented
    @Alerts(IE = { "null" }, FF = { "[object DocumentType]", "html,10,null,null,null,null",
            "html,-//W3C//DTD XHTML 1.0 Strict//EN,http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd,,null,null" })
    public void doctype() throws Exception {
        final String html = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\"\n"
            + "    \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">\n"
            + "<html xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en\" lang=\"en\">\n"
            + "<head>\n"
            + "  <title>Test</title>\n"
            + "  <script>\n"
            + "    function test() {\n"
            + "      var t = document.doctype;\n"
            + "      alert(t);\n"
            + "      if (t != null) {\n"
            + "        alert(t.nodeName + ',' + t.nodeType + ',' + t.nodeValue + ',' + t.prefix "
            + "+ ',' + t.localName + ',' + t.namespaceURI);\n"
            + "        alert(t.name + ',' + t.publicId + ',' + t.systemId + ',' + t.internalSubset"
            + " + ',' + t.entities + ',' + t.notations);\n"
            + "      }\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'>"
            + "</body>\n"
            + "</html>";

        loadPageWithAlerts(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @NotYetImplemented
    @Alerts(IE = { "[object]", "greeting,10,null,,undefined,", "greeting,undefined,undefined,undefined,," },
        FF = {
            "[object DocumentType]", "greeting,10,null,null,null,null", "greeting,MyIdentifier,hello.dtd,,null,null" })
    public void doctype_xml() throws Exception {
        final String html =
              "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + "      function test() {\n"
            + "        var request;\n"
            + "        if (window.XMLHttpRequest)\n"
            + "          request = new XMLHttpRequest();\n"
            + "        else if (window.ActiveXObject)\n"
            + "          request = new ActiveXObject('Microsoft.XMLHTTP');\n"
            + "        request.open('GET', '" + URL_SECOND + "', false);\n"
            + "        request.send('');\n"
            + "        var doc = request.responseXML;\n"
            + "        var t = document.doctype;\n"
            + "        alert(t);\n"
            + "        if (t != null) {\n"
            + "          alert(t.nodeName + ',' + t.nodeType + ',' + t.nodeValue + ',' + t.prefix "
            + "+ ',' + t.localName + ',' + t.namespaceURI);\n"
            + "          alert(t.name + ',' + t.publicId + ',' + t.systemId + ',' + t.internalSubset"
            + " + ',' + t.entities + ',' + t.notations);\n"
            + "        }\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='test()'>\n"
            + "  </body>\n"
            + "</html>";

        final String xml =
              "<!DOCTYPE greeting PUBLIC 'MyIdentifier' 'hello.dtd'>\n"
              + "<greeting/>";

        final WebClient client = getWebClient();
        final List<String> collectedAlerts = new ArrayList<String>();
        client.setAlertHandler(new CollectingAlertHandler(collectedAlerts));
        final MockWebConnection conn = new MockWebConnection();
        conn.setResponse(URL_FIRST, html);
        conn.setResponse(URL_SECOND, xml, "text/xml");
        client.setWebConnection(conn);
        client.getPage(URL_FIRST);

        assertEquals(getExpectedAlerts(), collectedAlerts);
    }
}
