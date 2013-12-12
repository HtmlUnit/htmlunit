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
package com.gargoylesoftware.htmlunit.xml;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.javascript.host.xml.XMLDocumentTest;

/**
 * Tests for {@link XmlPage}.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 * @author Frank Danek
 */
@RunWith(BrowserRunner.class)
public class XmlPage2Test extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "8",
            IE11 = "")
    // TODO [IE11]XML real IE11 does not support document.load
    public void load_XMLComment() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var doc = " + XMLDocumentTest.callCreateXMLDocument() + ";\n"
            + "    doc.async = false;\n"
            + "    doc.load('" + URL_SECOND + "');\n"
            + "    alert(doc.documentElement.childNodes[0].nodeType);\n"
            + "  }\n"
            + XMLDocumentTest.CREATE_XML_DOCUMENT_FUNCTION
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        final String xml = "<test><!-- --></test>";

        getMockWebConnection().setResponse(URL_SECOND, xml, "text/xml");
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(FF =  { "true", "14" },
            IE = { "true", "16" },
            IE11 = { "true", "15" })
    public void createElement() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var doc = " + XMLDocumentTest.callCreateXMLDocument() + ";\n"
            + "    doc.appendChild(doc.createElement('elementName'));\n"
            + "    var xml = " + XMLDocumentTest.callSerializeXMLDocumentToString("doc") + ";\n"
            + "    alert(xml.indexOf('<elementName') != -1);\n"
            + "    alert(xml.length);\n"
            + "  }\n"
            + XMLDocumentTest.CREATE_XML_DOCUMENT_FUNCTION
            + XMLDocumentTest.SERIALIZE_XML_DOCUMENT_TO_STRING_FUNCTION
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "[object Element]",
            IE8 = "exception")
    public void createElementNS() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var doc = " + XMLDocumentTest.callCreateXMLDocument() + ";\n"
            + "    try {\n"
            + "      alert(doc.createElementNS('myNS', 'ppp:eee'));\n"
            + "    } catch(e) {alert('exception')}\n"
            + "  }\n"
            + XMLDocumentTest.CREATE_XML_DOCUMENT_FUNCTION
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

}
