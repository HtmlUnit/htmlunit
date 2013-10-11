/*
 * Copyright (c) 2002-2013 Gargoyle Software Inc.
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

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.javascript.host.xml.XMLDocumentTest;

/**
 * Tests for {@link com.gargoylesoftware.htmlunit.javascript.NamedNodeMap}.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 * @author Frank Danek
 */
@RunWith(BrowserRunner.class)
public class NamedNodeMap2Test extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "myAttr", "myAttr" })
    public void setNamedItem() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var doc = " + XMLDocumentTest.callLoadXMLDocumentFromFile("'" + URL_SECOND + "'") + ";\n"
            + "    var node = doc.createAttribute('myAttr');\n"
            + "    doc.documentElement.attributes.setNamedItem(node);\n"
            + "    alert(doc.documentElement.attributes.getNamedItem('myAttr').nodeName);\n"
            + "\n"
            + "    node = document.createAttribute('myAttr');\n"
            + "    document.body.attributes.setNamedItem(node);\n"
            + "    alert(document.body.attributes.getNamedItem('myAttr').nodeName);\n"
            + "  }\n"
            + XMLDocumentTest.LOAD_XML_DOCUMENT_FROM_FILE_FUNCTION
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        final String xml = "<test></test>";

        getMockWebConnection().setResponse(URL_SECOND, xml, "text/xml");
        loadPageWithAlerts2(html);
    }
}
