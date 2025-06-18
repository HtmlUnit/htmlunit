/*
 * Copyright (c) 2002-2025 Gargoyle Software Inc.
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
package org.htmlunit.javascript.host.xml;

import org.htmlunit.WebDriverTestCase;
import org.htmlunit.junit.annotation.Alerts;
import org.htmlunit.util.MimeType;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link XMLDocument}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 * @author Frank Danek
 */
public class XMLDocument2Test extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"myTarget,myData,7", "myTarget,myData", "<?myTarget myData?>"})
    public void createProcessingInstruction() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var doc = document.implementation.createDocument('', '', null);\n"
            + "    var d = doc.createElement('doc');\n"
            + "    d.setAttribute('fluffy', 'true');\n"
            + "    d.setAttribute('numAttributes', '2');\n"
            + "    doc.appendChild(d);\n"
            + "    var pi = doc.createProcessingInstruction('myTarget', 'myData');\n"
            + "    doc.insertBefore(pi, d);\n"
            + "    log(pi.nodeName + ',' + pi.nodeValue + ',' + pi.nodeType);\n"
            + "    log(pi.target + ',' + pi.data);\n"
            + "    log(" + XMLDocumentTest.callSerializeXMLDocumentToString("pi") + ");\n"
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
    @Alerts({"#cdata-section,abcdefghij,4", "abcdefghij", "<![CDATA[abcdefghij]]>"})
    public void createCDATASection() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var doc = document.implementation.createDocument('', '', null);\n"
            + "    var d = doc.createElement('doc');\n"
            + "    doc.appendChild(d);\n"
            + "    var cdata = doc.createCDATASection('abcdefghij');\n"
            + "    d.appendChild(cdata);\n"
            + "    log(cdata.nodeName + ',' + cdata.nodeValue + ',' + cdata.nodeType);\n"
            + "    log(cdata.data);\n"
            + "    log(" + XMLDocumentTest.callSerializeXMLDocumentToString("cdata") + ");\n"
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
    @Alerts({"#cdata-section,<>&?,4", "<>&?", "<![CDATA[<>&?]]>"})
    public void createCDATASection_specialChars() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var doc = document.implementation.createDocument('', '', null);\n"
            + "    var d = doc.createElement('doc');\n"
            + "    doc.appendChild(d);\n"
            + "    var cdata = doc.createCDATASection('<>&?');\n"
            + "    d.appendChild(cdata);\n"
            + "    log(cdata.nodeName + ',' + cdata.nodeValue + ',' + cdata.nodeType);\n"
            + "    log(cdata.data);\n"
            + "    log(" + XMLDocumentTest.callSerializeXMLDocumentToString("cdata") + ");\n"
            + "  }\n"
            + XMLDocumentTest.SERIALIZE_XML_DOCUMENT_TO_STRING_FUNCTION
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("createNode not available")
    public void createNode() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var doc = " + XMLDocumentTest.callLoadXMLDocumentFromString("'<root><child/></root>'") + ";\n"
            + "    if (document.createNode) {\n"
            + "      var node = doc.createNode(2, 'Sci-Fi', '');\n"
            + "      doc.documentElement.childNodes.item(0).attributes.setNamedItem(node);\n"
            + "      log(" + XMLDocumentTest.callSerializeXMLDocumentToString("doc.documentElement") + ");\n"
            + "    } else { log('createNode not available'); }\n"
            + "  }\n"
            + XMLDocumentTest.LOAD_XML_DOCUMENT_FROM_STRING_FUNCTION
            + XMLDocumentTest.SERIALIZE_XML_DOCUMENT_TO_STRING_FUNCTION
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("createNode not available")
    public void createNode_element() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var doc = document.implementation.createDocument('', '', null);\n"
            + "    if (document.createNode) {\n"
            + "      var node = doc.createNode(1, 'test:element', 'uri:test');\n"
            + "      log(node.localName);\n"
            + "      log(node.prefix);\n"
            + "      log(node.namespaceURI);\n"
            + "      log(node.nodeName);\n"
            + "    } else { log('createNode not available'); }\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"a", "null", "b"})
    public void documentElementCaching() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var doc = document.implementation.createDocument('', '', null);\n"
            + "    var a = doc.createElement('a');\n"
            + "    var b = doc.createElement('b');\n"
            + "    doc.appendChild(a);\n"
            + "    log(doc.documentElement.tagName);\n"
            + "    doc.removeChild(a);\n"
            + "    log(doc.documentElement);\n"
            + "    doc.appendChild(b);\n"
            + "    log(doc.documentElement.tagName);\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a:b")
    public void createElement_namespace() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var doc = document.implementation.createDocument('', '', null);\n"
            + "    var a = doc.createElement('a:b');\n"
            + "    log(a.tagName);\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Test for issue #1523.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("ReferenceError")
    public void text() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    try {\n"
            + "      new ActiveXObject('Microsoft.XMLDOM');\n"
            + "    } catch(e) {\n"
            + "      logEx(e);\n"
            + "      return;\n"
            + "    }\n"
            + "    var xmldoc = new ActiveXObject('Microsoft.XMLDOM');\n"
            + "    var xml = '<Envelope><Body>content</Body></Envelope>';\n"
            + "    xmldoc.loadXML(xml);\n"

            + "    var expression = '/Envelope/Body';\n"
            + "    var body = xmldoc.documentElement.selectSingleNode(expression);\n"
            + "    log(body.text);\n"
            + "    log(body.firstChild.text);\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"foo", "foo"})
    public void firstChild_element() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var doc = " + XMLDocumentTest.callLoadXMLDocumentFromFile("'" + URL_SECOND + "'") + ";\n"
            + "    log(doc.firstChild.nodeName);\n"
            + "    log(doc.documentElement.nodeName);\n"
            + "  }\n"
            + XMLDocumentTest.LOAD_NATIVE_XML_DOCUMENT_FROM_FILE_FUNCTION
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "</body></html>";

        final String xml =
               "<foo>\n"
             + "    <foofoo name='first'>something</foofoo>\n"
             + "    <foofoo name='second'>something else</foofoo>\n"
             + "</foo>";

        getMockWebConnection().setResponse(URL_SECOND, xml, MimeType.TEXT_XML);
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"foo", "foo"})
    // Xerces does not offer any way to access the XML declaration
    public void firstChild_xmlDeclaration() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var doc = " + XMLDocumentTest.callLoadXMLDocumentFromFile("'" + URL_SECOND + "'") + ";\n"
            + "    log(doc.firstChild.nodeName);\n"
            + "    log(doc.documentElement.nodeName);\n"
            + "  }\n"
            + XMLDocumentTest.LOAD_NATIVE_XML_DOCUMENT_FROM_FILE_FUNCTION
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        final String xml =
               "<?xml version=\"1.0\"?>\n"
             + "<foo>\n"
             + "  <foofoo name='first'>something</foofoo>\n"
             + "  <foofoo name='second'>something else</foofoo>\n"
             + "</foo>";

        getMockWebConnection().setResponse(URL_SECOND, xml, MimeType.TEXT_XML);
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"apache", "foo"})
    public void firstChild_processingInstruction() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var doc = " + XMLDocumentTest.callLoadXMLDocumentFromFile("'" + URL_SECOND + "'") + ";\n"
            + "    log(doc.firstChild.nodeName);\n"
            + "    log(doc.documentElement.nodeName);\n"
            + "  }\n"
            + XMLDocumentTest.LOAD_NATIVE_XML_DOCUMENT_FROM_FILE_FUNCTION
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "</body></html>";

        final String xml =
               "<?apache include file=\"header.html\" ?>\n"
             + "<foo>\n"
             + "  <foofoo name='first'>something</foofoo>\n"
             + "  <foofoo name='second'>something else</foofoo>\n"
             + "</foo>";

        getMockWebConnection().setResponse(URL_SECOND, xml, MimeType.TEXT_XML);
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"dtd", "a"})
    public void firstChild_documentType() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var doc = " + XMLDocumentTest.callLoadXMLDocumentFromFile("'" + URL_SECOND + "'") + ";\n"
            + "    log(doc.firstChild.nodeName);\n"
            + "    log(doc.documentElement.nodeName);\n"
            + "  }\n"
            + XMLDocumentTest.LOAD_NATIVE_XML_DOCUMENT_FROM_FILE_FUNCTION
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "</body></html>";

        final String xml =
              "<!DOCTYPE dtd [ <!ELEMENT a (b+)> <!ELEMENT b (#PCDATA)> ]>\n"
            + "<a><b>1</b><b>2</b></a>";

        getMockWebConnection().setResponse(URL_SECOND, xml, MimeType.TEXT_XML);
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"#comment", "foo"})
    public void firstChild_comment() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var doc = " + XMLDocumentTest.callLoadXMLDocumentFromFile("'" + URL_SECOND + "'") + ";\n"
            + "    log(doc.firstChild.nodeName);\n"
            + "    log(doc.documentElement.nodeName);\n"
            + "  }\n"
            + XMLDocumentTest.LOAD_NATIVE_XML_DOCUMENT_FROM_FILE_FUNCTION
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "</body></html>";

        final String xml =
              "<!--comment-->\n"
            + "<foo>\n"
            + "  <foofoo name='first'>something</foofoo>\n"
            + "  <foofoo name='second'>something else</foofoo>\n"
            + "</foo>";

        getMockWebConnection().setResponse(URL_SECOND, xml, MimeType.TEXT_XML);
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"foo", "fooc1", "null"})
    public void firstElementChild() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var doc = " + XMLDocumentTest.callLoadXMLDocumentFromFile("'" + URL_SECOND + "'") + ";\n"

            + "    if (doc.firstElementChild == null) { log('not available'); return };\n"

            + "    log(doc.firstElementChild.nodeName);\n"
            + "    log(doc.firstElementChild.firstElementChild.nodeName);\n"
            + "    log(doc.firstElementChild.firstElementChild.firstElementChild);\n"
            + "  }\n"
            + XMLDocumentTest.LOAD_NATIVE_XML_DOCUMENT_FROM_FILE_FUNCTION
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "</body></html>";

        final String xml =
               "<foo>\n"
            + "    <fooc1 name='first'>something</fooc1>\n"
            + "    <fooc2 name='second'>something else</fooc2>\n"
            + "</foo>";

        getMockWebConnection().setResponse(URL_SECOND, xml, MimeType.TEXT_XML);
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"foo", "fooc2", "null"})
    public void lastElementChild() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var doc = " + XMLDocumentTest.callLoadXMLDocumentFromFile("'" + URL_SECOND + "'") + ";\n"

            + "    if (doc.firstElementChild == null) { log('not available'); return };\n"

            + "    log(doc.lastElementChild.nodeName);\n"
            + "    log(doc.firstElementChild.lastElementChild.nodeName);\n"
            + "    log(doc.firstElementChild.firstElementChild.lastElementChild);\n"
            + "  }\n"
            + XMLDocumentTest.LOAD_NATIVE_XML_DOCUMENT_FROM_FILE_FUNCTION
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "</body></html>";

        final String xml =
               "<foo>\n"
             + "    <fooc1 name='first'>something</fooc1>\n"
             + "    <fooc2 name='second'>something else</fooc2>\n"
             + "</foo>";

        getMockWebConnection().setResponse(URL_SECOND, xml, MimeType.TEXT_XML);
        loadPageVerifyTitle2(html);
    }

    /**
     * Test case for issue #1576.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"name: item1", "id: 1", "id: 2", "name: item2", "name: item3", "id: 3"})
    public void attributeOrder() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var doc = " + XMLDocumentTest.callLoadXMLDocumentFromString(
                    "'<items>"
                        + "<item name=\"item1\" id=\"1\">value1</item>"
                        + "<item id=\"2\" name=\"item2\">value2</item>"
                        + "<item name=\"item3\" id=\"3\">value3</item>"
                    + "</items>'") + ";\n"
            + "    var items = doc.getElementsByTagName('item');\n"
            + "    for(var i = 0; i < items.length; i++) {\n"
            + "      var attribs = items[i].attributes;\n"
            + "      for(var j = 0; j < attribs.length; j++) {\n"
            + "        log(attribs[j].name + ': ' + attribs[j].value);\n"
            + "      }\n"
            + "    }\n"
            + "  }\n"
            + XMLDocumentTest.LOAD_XML_DOCUMENT_FROM_STRING_FUNCTION
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }
}
