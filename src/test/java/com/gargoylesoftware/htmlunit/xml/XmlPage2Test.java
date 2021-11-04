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
package com.gargoylesoftware.htmlunit.xml;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.javascript.host.xml.XMLDocumentTest;
import com.gargoylesoftware.htmlunit.util.MimeType;

/**
 * Tests for {@link XmlPage}.
 *
 * @author Ahmed Ashour
 * @author Frank Danek
 */
@RunWith(BrowserRunner.class)
public class XmlPage2Test extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("8")
    public void load_XMLComment() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var doc = " + XMLDocumentTest.callLoadXMLDocumentFromFile("'" + URL_SECOND + "'") + ";\n"
            + "    log(doc.documentElement.childNodes[0].nodeType);\n"
            + "  }\n"
            + XMLDocumentTest.LOAD_XML_DOCUMENT_FROM_FILE_FUNCTION
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        final String xml = "<test><!-- --></test>";

        getMockWebConnection().setResponse(URL_SECOND, xml, MimeType.TEXT_XML);
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT =  { "true", "14"},
            IE = {"true", "15"})
    public void createElement() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var doc = document.implementation.createDocument('', '', null);\n"
            + "    doc.appendChild(doc.createElement('elementName'));\n"
            + "    var xml = " + XMLDocumentTest.callSerializeXMLDocumentToString("doc") + ";\n"
            + "    log(xml.indexOf('<elementName') != -1);\n"
            + "    log(xml.length);\n"
            + "  }\n"
            + XMLDocumentTest.SERIALIZE_XML_DOCUMENT_TO_STRING_FUNCTION
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("[object Element]")
    public void createElementNS() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var doc = document.implementation.createDocument('', '', null);\n"
            + "    try {\n"
            + "      log(doc.createElementNS('myNS', 'ppp:eee'));\n"
            + "    } catch(e) {log('exception')}\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

}
