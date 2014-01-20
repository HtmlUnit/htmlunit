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
package com.gargoylesoftware.htmlunit.activex.javascript.msxml;

import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.IE;
import static com.gargoylesoftware.htmlunit.activex.javascript.msxml.MSXMLTestUtil.CREATE_XMLDOMDOCUMENT_FUNCTION;
import static com.gargoylesoftware.htmlunit.activex.javascript.msxml.MSXMLTestUtil.LOAD_XMLDOMDOCUMENT_FROM_STRING_FUNCTION;
import static com.gargoylesoftware.htmlunit.activex.javascript.msxml.MSXMLTestUtil.SERIALIZE_XMLDOMDOCUMENT_TO_STRING_FUNCTION;
import static com.gargoylesoftware.htmlunit.activex.javascript.msxml.MSXMLTestUtil.callCreateXMLDOMDocument;
import static com.gargoylesoftware.htmlunit.activex.javascript.msxml.MSXMLTestUtil.callLoadXMLDOMDocumentFromString;
import static com.gargoylesoftware.htmlunit.activex.javascript.msxml.MSXMLTestUtil.callSerializeXMLDOMDocumentToString;
import static com.gargoylesoftware.htmlunit.activex.javascript.msxml.MSXMLTestUtil.createTestHTML;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.Browsers;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * Tests for {@link XMLDocument}.
 * @see com.gargoylesoftware.htmlunit.javascript.host.xml.XMLDocument2Test
 *
 * @version $Revision$
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
    @Browsers(IE)
    @Alerts({ "myTarget,myData,7", "myTarget,myData", "abcdefghij",
            "<?myTarget myData?>", "<![CDATA[abcdefghij]]>" })
    public void createProcessingInstruction() throws Exception {
        final String html = ""
            + "  function test() {\n"
            + "    var doc = " + callCreateXMLDOMDocument() + ";\n"
            + "    var d = doc.createElement('doc');\n"
            + "    d.setAttribute('fluffy', 'true');\n"
            + "    d.setAttribute('numAttributes', '2');\n"
            + "    doc.appendChild(d);\n"
            + "    var pi = doc.createProcessingInstruction('myTarget', 'myData');\n"
            + "    doc.insertBefore(pi, d);\n"
            + "    alert(pi.nodeName + ',' + pi.nodeValue + ',' + pi.nodeType);\n"
            + "    alert(pi.target + ',' + pi.data);\n"
            + "    var cdata = doc.createCDATASection('abcdefghij');\n"
            + "    d.appendChild(cdata);\n"
            + "    alert(cdata.data);\n"
            + "    alert(" + callSerializeXMLDOMDocumentToString("pi") + ");\n"
            + "    alert(" + callSerializeXMLDOMDocumentToString("cdata") + ");\n"
            + "  }\n"
            + CREATE_XMLDOMDOCUMENT_FUNCTION
            + SERIALIZE_XMLDOMDOCUMENT_TO_STRING_FUNCTION;
        loadPageWithAlerts2(createTestHTML(html));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(IE)
    @Alerts("createNode not available")
    public void createNode() throws Exception {
        final String html = ""
            + "  function test() {\n"
            + "    var doc = " + callLoadXMLDOMDocumentFromString("'<root><child/></root>'") + ";\n"
            + "    if (document.createNode) {\n"
            + "      var node = doc.createNode(2, 'Sci-Fi', '');\n"
            + "      doc.documentElement.childNodes.item(0).attributes.setNamedItem(node);\n"
            + "      alert(" + callSerializeXMLDOMDocumentToString("doc.documentElement") + ");\n"
            + "    } else { alert('createNode not available'); }\n"
            + "  }\n"
            + LOAD_XMLDOMDOCUMENT_FROM_STRING_FUNCTION
            + SERIALIZE_XMLDOMDOCUMENT_TO_STRING_FUNCTION;
        loadPageWithAlerts2(createTestHTML(html));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(IE)
    @Alerts("createNode not available")
    public void createNode_element() throws Exception {
        final String html = ""
            + "  function test() {\n"
            + "    var doc = " + callCreateXMLDOMDocument() + ";\n"
            + "    if (document.createNode) {\n"
            + "      var node = doc.createNode(1, 'test:element', 'uri:test');\n"
            + "      alert(node.localName);\n"
            + "      alert(node.prefix);\n"
            + "      alert(node.namespaceURI);\n"
            + "      alert(node.nodeName);\n"
            + "    } else { alert('createNode not available'); }\n"
            + "  }\n"
            + CREATE_XMLDOMDOCUMENT_FUNCTION;
        loadPageWithAlerts2(createTestHTML(html));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(IE)
    @Alerts({ "a", "null", "b" })
    public void documentElementCaching() throws Exception {
        final String html = ""
            + "  function test() {\n"
            + "    var doc = " + callCreateXMLDOMDocument() + ";\n"
            + "    var a = doc.createElement('a');\n"
            + "    var b = doc.createElement('b');\n"
            + "    doc.appendChild(a);\n"
            + "    alert(doc.documentElement.tagName);\n"
            + "    doc.removeChild(a);\n"
            + "    alert(doc.documentElement);\n"
            + "    doc.appendChild(b);\n"
            + "    alert(doc.documentElement.tagName);\n"
            + "  }\n"
            + CREATE_XMLDOMDOCUMENT_FUNCTION;
        loadPageWithAlerts2(createTestHTML(html));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(IE)
    @Alerts("a:b")
    public void createElement_namespace() throws Exception {
        final String html = ""
            + "  function test() {\n"
            + "    var doc = " + callCreateXMLDOMDocument() + ";\n"
            + "    var a = doc.createElement('a:b');\n"
            + "    alert(a.tagName);\n"
            + "  }\n"
            + CREATE_XMLDOMDOCUMENT_FUNCTION;
        loadPageWithAlerts2(createTestHTML(html));
    }

    /**
     * Test for issue #1523.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(IE)
    @Alerts({ "content", "content" })
    public void text() throws Exception {
        final String html = ""
            + "  function test() {\n"
            + "    var xml = '<Envelope><Body>content</Body></Envelope>';\n"
            + "    var xmldoc = " + callLoadXMLDOMDocumentFromString("xml") + ";\n"

            + "    var expression = '/Envelope/Body';\n"
            + "    var body = xmldoc.documentElement.selectSingleNode(expression);\n"
            + "    alert(body.text);\n"
            + "    alert(body.firstChild.text);\n"
            + "  }\n"
            + LOAD_XMLDOMDOCUMENT_FROM_STRING_FUNCTION;
        loadPageWithAlerts2(createTestHTML(html));
    }
}
