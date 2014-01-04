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
package com.gargoylesoftware.htmlunit.javascript.host.xml;

import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.IE;
import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.IE8;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.Browsers;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * Tests for {@link XMLDocument}.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 * @author Ronald Brill
 * @author Frank Danek
 */
@RunWith(BrowserRunner.class)
public class XMLDocument2Test extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "myTarget,myData,7", "myTarget,myData", "<?myTarget myData?>" })
    public void createProcessingInstruction() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var doc = " + XMLDocumentTest.callCreateXMLDocument() + ";\n"
            + "    var d = doc.createElement('doc');\n"
            + "    d.setAttribute('fluffy', 'true');\n"
            + "    d.setAttribute('numAttributes', '2');\n"
            + "    doc.appendChild(d);\n"
            + "    var pi = doc.createProcessingInstruction('myTarget', 'myData');\n"
            + "    doc.insertBefore(pi, d);\n"
            + "    alert(pi.nodeName + ',' + pi.nodeValue + ',' + pi.nodeType);\n"
            + "    alert(pi.target + ',' + pi.data);\n"
            + "    alert(" + XMLDocumentTest.callSerializeXMLDocumentToString("pi") + ");\n"
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
    @Alerts(DEFAULT = { "#cdata-section,abcdefghij,4", "abcdefghij", "<![CDATA[abcdefghij]]>" },
            IE11 = { "#cdata-section,abcdefghij,4", "abcdefghij", "abcdefghij" })
    public void createCDATASection() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var doc = " + XMLDocumentTest.callCreateXMLDocument() + ";\n"
            + "    var d = doc.createElement('doc');\n"
            + "    doc.appendChild(d);\n"
            + "    var cdata = doc.createCDATASection('abcdefghij');\n"
            + "    d.appendChild(cdata);\n"
            + "    alert(cdata.nodeName + ',' + cdata.nodeValue + ',' + cdata.nodeType);\n"
            + "    alert(cdata.data);\n"
            + "    alert(" + XMLDocumentTest.callSerializeXMLDocumentToString("cdata") + ");\n"
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
    @Alerts(DEFAULT = { "#cdata-section,<>&?,4", "<>&?", "<![CDATA[<>&?]]>" },
            IE11 = { "#cdata-section,<>&?,4", "<>&?", "&lt;&gt;&amp;?" })
    public void createCDATASection_specialChars() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var doc = " + XMLDocumentTest.callCreateXMLDocument() + ";\n"
            + "    var d = doc.createElement('doc');\n"
            + "    doc.appendChild(d);\n"
            + "    var cdata = doc.createCDATASection('<>&?');\n"
            + "    d.appendChild(cdata);\n"
            + "    alert(cdata.nodeName + ',' + cdata.nodeValue + ',' + cdata.nodeType);\n"
            + "    alert(cdata.data);\n"
            + "    alert(" + XMLDocumentTest.callSerializeXMLDocumentToString("cdata") + ");\n"
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
    @Alerts("createNode not available")
    public void createNode() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var doc = " + XMLDocumentTest.callLoadXMLDocumentFromString("'<root><child/></root>'") + ";\n"
            + "    if (document.createNode) {\n"
            + "      var node = doc.createNode(2, 'Sci-Fi', '');\n"
            + "      doc.documentElement.childNodes.item(0).attributes.setNamedItem(node);\n"
            + "      alert(" + XMLDocumentTest.callSerializeXMLDocumentToString("doc.documentElement") + ");\n"
            + "    } else { alert('createNode not available'); }\n"
            + "  }\n"
            + XMLDocumentTest.LOAD_XML_DOCUMENT_FROM_STRING_FUNCTION
            + XMLDocumentTest.SERIALIZE_XML_DOCUMENT_TO_STRING_FUNCTION
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("createNode not available")
    public void createNode_element() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var doc = " + XMLDocumentTest.callCreateXMLDocument() + ";\n"
            + "    if (document.createNode) {\n"
            + "      var node = doc.createNode(1, 'test:element', 'uri:test');\n"
            + "      alert(node.localName);\n"
            + "      alert(node.prefix);\n"
            + "      alert(node.namespaceURI);\n"
            + "      alert(node.nodeName);\n"
            + "    } else { alert('createNode not available'); }\n"
            + "  }\n"
            + XMLDocumentTest.CREATE_XML_DOCUMENT_FUNCTION
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "a", "null", "b" })
    public void documentElementCaching() throws Exception {
        final String html = "<html><head><script>\n"
            + "  function test() {\n"
            + "    var doc = " + XMLDocumentTest.callCreateXMLDocument() + ";\n"
            + "    var a = doc.createElement('a');\n"
            + "    var b = doc.createElement('b');\n"
            + "    doc.appendChild(a);\n"
            + "    alert(doc.documentElement.tagName);\n"
            + "    doc.removeChild(a);\n"
            + "    alert(doc.documentElement);\n"
            + "    doc.appendChild(b);\n"
            + "    alert(doc.documentElement.tagName);\n"
            + "  }\n"
            + XMLDocumentTest.CREATE_XML_DOCUMENT_FUNCTION
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a:b")
    public void createElement_namespace() throws Exception {
        final String html = "<html><head><script>\n"
            + "  function test() {\n"
            + "    var doc = " + XMLDocumentTest.callCreateXMLDocument() + ";\n"
            + "    var a = doc.createElement('a:b');\n"
            + "    alert(a.tagName);\n"
            + "  }\n"
            + XMLDocumentTest.CREATE_XML_DOCUMENT_FUNCTION
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
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
        final String html = "<html><head><script>\n"
            + "  function test() {\n"
            + "    var xmldoc = new ActiveXObject('Microsoft.XMLDOM');\n"
            + "    var xml = '<Envelope><Body>content</Body></Envelope>';\n"
            + "    xmldoc.loadXML(xml);\n"

            + "    var expression = '/Envelope/Body';\n"
            + "    var body = xmldoc.documentElement.selectSingleNode(expression);\n"
            + "    alert(body.text);\n"
            + "    alert(body.firstChild.text);\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "foo", "foo" })
    public void firstChild_element() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var doc = " + XMLDocumentTest.callLoadXMLDocumentFromFile("'" + URL_SECOND + "'") + ";\n"
            + "    alert(doc.firstChild.nodeName);\n"
            + "    alert(doc.documentElement.nodeName);\n"
            + "  }\n"
            + XMLDocumentTest.LOAD_NATIVE_XML_DOCUMENT_FROM_FILE_FUNCTION
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        final String xml =
               "<foo>\n"
             + "    <foofoo name='first'>something</foofoo>\n"
             + "    <foofoo name='second'>something else</foofoo>\n"
             + "</foo>";

        getMockWebConnection().setResponse(URL_SECOND, xml, "text/xml");
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(IE)
    @Alerts({ "foo", "foo" })
    public void firstChild_element_activeX() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var doc = " + XMLDocumentTest.callLoadXMLDocumentFromFile("'" + URL_SECOND + "'") + ";\n"
            + "    alert(doc.firstChild.nodeName);\n"
            + "    alert(doc.documentElement.nodeName);\n"
            + "  }\n"
            + XMLDocumentTest.LOAD_ACTIVEX_XML_DOCUMENT_FROM_FILE_FUNCTION
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        final String xml =
               "<foo>\n"
             + "    <foofoo name='first'>something</foofoo>\n"
             + "    <foofoo name='second'>something else</foofoo>\n"
             + "</foo>";

        getMockWebConnection().setResponse(URL_SECOND, xml, "text/xml");
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "foo", "foo" },
            IE8 = { "xml", "foo" })
    @NotYetImplemented(IE8)
    // Xerces does not offer any way to access the XML declaration
    public void firstChild_xmlDeclaration() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var doc = " + XMLDocumentTest.callLoadXMLDocumentFromFile("'" + URL_SECOND + "'") + ";\n"
            + "    alert(doc.firstChild.nodeName);\n"
            + "    alert(doc.documentElement.nodeName);\n"
            + "  }\n"
            + XMLDocumentTest.LOAD_NATIVE_XML_DOCUMENT_FROM_FILE_FUNCTION
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        final String xml =
               "<?xml version=\"1.0\"?>\n"
             + "<foo>\n"
             + "    <foofoo name='first'>something</foofoo>\n"
             + "    <foofoo name='second'>something else</foofoo>\n"
             + "</foo>";

        getMockWebConnection().setResponse(URL_SECOND, xml, "text/xml");
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(IE)
    @Alerts({ "xml", "foo" })
    @NotYetImplemented(IE)
    // Xerces does not offer any way to access the XML declaration
    public void firstChild_xmlDeclaration_activeX() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var doc = " + XMLDocumentTest.callLoadXMLDocumentFromFile("'" + URL_SECOND + "'") + ";\n"
            + "    alert(doc.firstChild.nodeName);\n"
            + "    alert(doc.documentElement.nodeName);\n"
            + "  }\n"
            + XMLDocumentTest.LOAD_ACTIVEX_XML_DOCUMENT_FROM_FILE_FUNCTION
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        final String xml =
               "<?xml version=\"1.0\"?>\n"
             + "<foo>\n"
             + "    <foofoo name='first'>something</foofoo>\n"
             + "    <foofoo name='second'>something else</foofoo>\n"
             + "</foo>";

        getMockWebConnection().setResponse(URL_SECOND, xml, "text/xml");
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "apache", "foo" })
    public void firstChild_processingInstruction() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var doc = " + XMLDocumentTest.callLoadXMLDocumentFromFile("'" + URL_SECOND + "'") + ";\n"
            + "    alert(doc.firstChild.nodeName);\n"
            + "    alert(doc.documentElement.nodeName);\n"
            + "  }\n"
            + XMLDocumentTest.LOAD_NATIVE_XML_DOCUMENT_FROM_FILE_FUNCTION
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        final String xml =
               "<?apache include file=\"header.html\" ?>\n"
             + "<foo>\n"
             + "    <foofoo name='first'>something</foofoo>\n"
             + "    <foofoo name='second'>something else</foofoo>\n"
             + "</foo>";

        getMockWebConnection().setResponse(URL_SECOND, xml, "text/xml");
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(IE)
    @Alerts({ "apache", "foo" })
    public void firstChild_processingInstruction_activeX() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var doc = " + XMLDocumentTest.callLoadXMLDocumentFromFile("'" + URL_SECOND + "'") + ";\n"
            + "    alert(doc.firstChild.nodeName);\n"
            + "    alert(doc.documentElement.nodeName);\n"
            + "  }\n"
            + XMLDocumentTest.LOAD_ACTIVEX_XML_DOCUMENT_FROM_FILE_FUNCTION
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        final String xml =
               "<?apache include file=\"header.html\" ?>\n"
             + "<foo>\n"
             + "    <foofoo name='first'>something</foofoo>\n"
             + "    <foofoo name='second'>something else</foofoo>\n"
             + "</foo>";

        getMockWebConnection().setResponse(URL_SECOND, xml, "text/xml");
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "dtd", "a" })
    public void firstChild_documentType() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var doc = " + XMLDocumentTest.callLoadXMLDocumentFromFile("'" + URL_SECOND + "'") + ";\n"
            + "    alert(doc.firstChild.nodeName);\n"
            + "    alert(doc.documentElement.nodeName);\n"
            + "  }\n"
            + XMLDocumentTest.LOAD_NATIVE_XML_DOCUMENT_FROM_FILE_FUNCTION
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        final String xml =
              "<!DOCTYPE dtd [ <!ELEMENT a (b+)> <!ELEMENT b (#PCDATA)> ]>\n"
            + "<a><b>1</b><b>2</b></a>";

        getMockWebConnection().setResponse(URL_SECOND, xml, "text/xml");
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(IE)
    @Alerts({ "dtd", "a" })
    public void firstChild_documentType_activeX() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var doc = " + XMLDocumentTest.callLoadXMLDocumentFromFile("'" + URL_SECOND + "'") + ";\n"
            + "    alert(doc.firstChild.nodeName);\n"
            + "    alert(doc.documentElement.nodeName);\n"
            + "  }\n"
            + XMLDocumentTest.LOAD_ACTIVEX_XML_DOCUMENT_FROM_FILE_FUNCTION
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        final String xml =
              "<!DOCTYPE dtd [ <!ELEMENT a (b+)> <!ELEMENT b (#PCDATA)> ]>\n"
            + "<a><b>1</b><b>2</b></a>";

        getMockWebConnection().setResponse(URL_SECOND, xml, "text/xml");
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "#comment", "foo" })
    public void firstChild_comment() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var doc = " + XMLDocumentTest.callLoadXMLDocumentFromFile("'" + URL_SECOND + "'") + ";\n"
            + "    alert(doc.firstChild.nodeName);\n"
            + "    alert(doc.documentElement.nodeName);\n"
            + "  }\n"
            + XMLDocumentTest.LOAD_NATIVE_XML_DOCUMENT_FROM_FILE_FUNCTION
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        final String xml =
              "<!--comment-->\n"
            + "<foo>\n"
            + "    <foofoo name='first'>something</foofoo>\n"
            + "    <foofoo name='second'>something else</foofoo>\n"
            + "</foo>";

        getMockWebConnection().setResponse(URL_SECOND, xml, "text/xml");
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(IE)
    @Alerts({ "#comment", "foo" })
    public void firstChild_comment_activeX() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var doc = " + XMLDocumentTest.callLoadXMLDocumentFromFile("'" + URL_SECOND + "'") + ";\n"
            + "    alert(doc.firstChild.nodeName);\n"
            + "    alert(doc.documentElement.nodeName);\n"
            + "  }\n"
            + XMLDocumentTest.LOAD_ACTIVEX_XML_DOCUMENT_FROM_FILE_FUNCTION
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        final String xml =
              "<!--comment-->\n"
            + "<foo>\n"
            + "    <foofoo name='first'>something</foofoo>\n"
            + "    <foofoo name='second'>something else</foofoo>\n"
            + "</foo>";

        getMockWebConnection().setResponse(URL_SECOND, xml, "text/xml");
        loadPageWithAlerts2(html);
    }
}
