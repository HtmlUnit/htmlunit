/*
 * Copyright (c) 2002-2022 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.activex.javascript.msxml;

import static com.gargoylesoftware.htmlunit.activex.javascript.msxml.MSXMLTestHelper.ACTIVEX_CHECK;
import static com.gargoylesoftware.htmlunit.activex.javascript.msxml.MSXMLTestHelper.CREATE_XMLDOMDOCUMENT_FUNCTION;
import static com.gargoylesoftware.htmlunit.activex.javascript.msxml.MSXMLTestHelper.LOAD_XMLDOMDOCUMENT_FROM_STRING_FUNCTION;
import static com.gargoylesoftware.htmlunit.activex.javascript.msxml.MSXMLTestHelper.SERIALIZE_XMLDOMDOCUMENT_TO_STRING_FUNCTION;
import static com.gargoylesoftware.htmlunit.activex.javascript.msxml.MSXMLTestHelper.callCreateXMLDOMDocument;
import static com.gargoylesoftware.htmlunit.activex.javascript.msxml.MSXMLTestHelper.callLoadXMLDOMDocumentFromString;
import static com.gargoylesoftware.htmlunit.activex.javascript.msxml.MSXMLTestHelper.callSerializeXMLDOMDocumentToString;
import static com.gargoylesoftware.htmlunit.activex.javascript.msxml.MSXMLTestHelper.createTestHTML;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.junit.BrowserRunner;
import com.gargoylesoftware.htmlunit.junit.BrowserRunner.Alerts;

/**
 * Tests for {@link com.gargoylesoftware.htmlunit.javascript.host.xml.XMLDocument}.
 * @see com.gargoylesoftware.htmlunit.javascript.host.xml.XMLDocument2Test
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 * @author Frank Danek
 */
@RunWith(BrowserRunner.class)
public class XMLDOMDocument3Test extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX",
            IE = {"myTarget,myData,7", "myTarget,myData", "abcdefghij",
                  "<?myTarget myData?>", "<![CDATA[abcdefghij]]>"})
    public void createProcessingInstruction() throws Exception {
        final String html = ""
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + ACTIVEX_CHECK
            + "    var doc = " + callCreateXMLDOMDocument() + ";\n"
            + "    var d = doc.createElement('doc');\n"
            + "    d.setAttribute('fluffy', 'true');\n"
            + "    d.setAttribute('numAttributes', '2');\n"
            + "    doc.appendChild(d);\n"
            + "    var pi = doc.createProcessingInstruction('myTarget', 'myData');\n"
            + "    doc.insertBefore(pi, d);\n"
            + "    log(pi.nodeName + ',' + pi.nodeValue + ',' + pi.nodeType);\n"
            + "    log(pi.target + ',' + pi.data);\n"
            + "    var cdata = doc.createCDATASection('abcdefghij');\n"
            + "    d.appendChild(cdata);\n"
            + "    log(cdata.data);\n"
            + "    log(" + callSerializeXMLDOMDocumentToString("pi") + ");\n"
            + "    log(" + callSerializeXMLDOMDocumentToString("cdata") + ");\n"
            + "  }\n"
            + CREATE_XMLDOMDOCUMENT_FUNCTION
            + SERIALIZE_XMLDOMDOCUMENT_TO_STRING_FUNCTION;
        loadPageVerifyTitle2(createTestHTML(html));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX",
            IE = "createNode not available")
    public void createNode() throws Exception {
        final String html = ""
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + ACTIVEX_CHECK
            + "    var doc = " + callLoadXMLDOMDocumentFromString("'<root><child/></root>'") + ";\n"
            + "    if (document.createNode) {\n"
            + "      var node = doc.createNode(2, 'Sci-Fi', '');\n"
            + "      doc.documentElement.childNodes.item(0).attributes.setNamedItem(node);\n"
            + "      log(" + callSerializeXMLDOMDocumentToString("doc.documentElement") + ");\n"
            + "    } else { log('createNode not available'); }\n"
            + "  }\n"
            + LOAD_XMLDOMDOCUMENT_FROM_STRING_FUNCTION
            + SERIALIZE_XMLDOMDOCUMENT_TO_STRING_FUNCTION;
        loadPageVerifyTitle2(createTestHTML(html));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX",
            IE = "createNode not available")
    public void createNode_element() throws Exception {
        final String html = ""
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + ACTIVEX_CHECK
            + "    var doc = " + callCreateXMLDOMDocument() + ";\n"
            + "    if (document.createNode) {\n"
            + "      var node = doc.createNode(1, 'test:element', 'uri:test');\n"
            + "      log(node.localName);\n"
            + "      log(node.prefix);\n"
            + "      log(node.namespaceURI);\n"
            + "      log(node.nodeName);\n"
            + "    } else { log('createNode not available'); }\n"
            + "  }\n"
            + CREATE_XMLDOMDOCUMENT_FUNCTION;
        loadPageVerifyTitle2(createTestHTML(html));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX",
            IE = {"a", "null", "b"})
    public void documentElementCaching() throws Exception {
        final String html = ""
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + ACTIVEX_CHECK
            + "    var doc = " + callCreateXMLDOMDocument() + ";\n"
            + "    var a = doc.createElement('a');\n"
            + "    var b = doc.createElement('b');\n"
            + "    doc.appendChild(a);\n"
            + "    log(doc.documentElement.tagName);\n"
            + "    doc.removeChild(a);\n"
            + "    log(doc.documentElement);\n"
            + "    doc.appendChild(b);\n"
            + "    log(doc.documentElement.tagName);\n"
            + "  }\n"
            + CREATE_XMLDOMDOCUMENT_FUNCTION;
        loadPageVerifyTitle2(createTestHTML(html));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX",
            IE = "a:b")
    public void createElement_namespace() throws Exception {
        final String html = ""
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + ACTIVEX_CHECK
            + "    var doc = " + callCreateXMLDOMDocument() + ";\n"
            + "    var a = doc.createElement('a:b');\n"
            + "    log(a.tagName);\n"
            + "  }\n"
            + CREATE_XMLDOMDOCUMENT_FUNCTION;
        loadPageVerifyTitle2(createTestHTML(html));
    }

    /**
     * Test for issue #1523.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX",
            IE = {"content", "content"})
    public void text() throws Exception {
        final String html = ""
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + ACTIVEX_CHECK
            + "    var xml = '<Envelope><Body>content</Body></Envelope>';\n"
            + "    var xmldoc = " + callLoadXMLDOMDocumentFromString("xml") + ";\n"

            + "    var expression = '/Envelope/Body';\n"
            + "    var body = xmldoc.documentElement.selectSingleNode(expression);\n"
            + "    log(body.text);\n"
            + "    log(body.firstChild.text);\n"
            + "  }\n"
            + LOAD_XMLDOMDOCUMENT_FROM_STRING_FUNCTION;
        loadPageVerifyTitle2(createTestHTML(html));
    }
}
