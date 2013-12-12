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
package com.gargoylesoftware.htmlunit.activex.javascript.msxml;

import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.IE;
import static com.gargoylesoftware.htmlunit.activex.javascript.msxml.MSXMLTestUtil.CREATE_XMLDOMDOCUMENT_FUNCTION;
import static com.gargoylesoftware.htmlunit.activex.javascript.msxml.MSXMLTestUtil.
    LOAD_XMLDOMDOCUMENT_FROM_STRING_FUNCTION;
import static com.gargoylesoftware.htmlunit.activex.javascript.msxml.MSXMLTestUtil.
    LOAD_XMLDOMDOCUMENT_FROM_URL_FUNCTION;
import static com.gargoylesoftware.htmlunit.activex.javascript.msxml.MSXMLTestUtil.callCreateXMLDOMDocument;
import static com.gargoylesoftware.htmlunit.activex.javascript.msxml.MSXMLTestUtil.callLoadXMLDOMDocumentFromString;
import static com.gargoylesoftware.htmlunit.activex.javascript.msxml.MSXMLTestUtil.callLoadXMLDOMDocumentFromURL;
import static com.gargoylesoftware.htmlunit.activex.javascript.msxml.MSXMLTestUtil.createTestHTML;

import java.net.URL;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.Browsers;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * Tests for {@link XMLDOMDocument}.
 * @see com.gargoylesoftware.htmlunit.javascript.host.xml.XMLDocumentTest
 *
 * @version $Revision$
 * @author Ahmed Ashour
 * @author Marc Guillemot
 * @author Chuck Dumont
 * @author Frank Danek
 */
@RunWith(BrowserRunner.class)
public class XMLDOMDocument2Test extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(IE)
    @Alerts("true")
    public void async() throws Exception {
        final String html = ""
            + "  function test() {\n"
            + "    var doc = " + callCreateXMLDOMDocument() + ";\n"
            + "    alert(doc.async);\n"
            + "  }\n"
            + CREATE_XMLDOMDOCUMENT_FUNCTION;
        loadPageWithAlerts2(createTestHTML(html));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(IE)
    @Alerts({ "true", "books", "books", "1", "book", "0" })
    public void load() throws Exception {
        final String html = ""
            + "  function test() {\n"
            + "    var doc = " + callCreateXMLDOMDocument() + ";\n"
            + "    doc.async = false;\n"
            + "    alert(doc.load('" + URL_SECOND + "'));\n"
            + "    alert(doc.documentElement.nodeName);\n"
            + "    alert(doc.childNodes[0].nodeName);\n"
            + "    alert(doc.childNodes[0].childNodes.length);\n"
            + "    alert(doc.childNodes[0].childNodes[0].nodeName);\n"
            + "    alert(doc.getElementsByTagName('books').item(0).attributes.length);\n"
            + "  }\n"
            + CREATE_XMLDOMDOCUMENT_FUNCTION;

        final String xml
            = "<books>\n"
            + "  <book>\n"
            + "    <title>Immortality</title>\n"
            + "    <author>John Smith</author>\n"
            + "  </book>\n"
            + "</books>";

        getMockWebConnection().setResponse(URL_SECOND, xml, "text/xml");
        loadPageWithAlerts2(createTestHTML(html));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(IE)
    @Alerts({ "true", "books", "books", "1", "book", "0" })
    // TODO what is the difference to load()?
    public void load_relativeURL() throws Exception {
        final String html = ""
            + "  function test() {\n"
            + "    var doc = " + callCreateXMLDOMDocument() + ";\n"
            + "    doc.async = false;\n"
            + "    alert(doc.load('" + URL_SECOND + "'));\n"
            + "    alert(doc.documentElement.nodeName);\n"
            + "    alert(doc.childNodes[0].nodeName);\n"
            + "    alert(doc.childNodes[0].childNodes.length);\n"
            + "    alert(doc.childNodes[0].childNodes[0].nodeName);\n"
            + "    alert(doc.getElementsByTagName('books').item(0).attributes.length);\n"
            + "  }\n"
            + CREATE_XMLDOMDOCUMENT_FUNCTION;

        final String xml
            = "<books>\n"
            + "  <book>\n"
            + "    <title>Immortality</title>\n"
            + "    <author>John Smith</author>\n"
            + "  </book>\n"
            + "</books>";

        getMockWebConnection().setResponse(URL_SECOND, xml, "text/xml");
        loadPageWithAlerts2(createTestHTML(html));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(IE)
    @Alerts("false")
    public void preserveWhiteSpace() throws Exception {
        final String html = ""
            + "  function test() {\n"
            + "    try {\n"
            + "      var doc = " + callCreateXMLDOMDocument() + ";\n"
            + "      alert(doc.preserveWhiteSpace);\n"
            + "    } catch(e) { alert('exception'); }\n"
            + "  }\n"
            + CREATE_XMLDOMDOCUMENT_FUNCTION;
        loadPageWithAlerts2(createTestHTML(html));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(IE)
    @Alerts("")
    public void setProperty() throws Exception {
        final String html = ""
            + "  function test() {\n"
            + "    var doc = " + callCreateXMLDOMDocument() + ";\n"
            + "    try {\n"
            + "      doc.setProperty('SelectionNamespaces', \"xmlns:xsl='http://www.w3.org/1999/XSL/Transform'\");\n"
            + "      doc.setProperty('SelectionLanguage', 'XPath');\n"
            + "    } catch(e) { alert('exception'); }\n"
            + "  }\n"
            + CREATE_XMLDOMDOCUMENT_FUNCTION;
        loadPageWithAlerts2(createTestHTML(html));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(IE)
    @Alerts({ "1", "books" })
    public void selectNodes() throws Exception {
        final String html = ""
            + "  function test() {\n"
            + "    var doc = " + callLoadXMLDOMDocumentFromURL("'" + URL_SECOND + "'") + ";\n"
            + "    try {\n"
            + "      var nodes = doc.selectNodes('/books');\n"
            + "      alert(nodes.length);\n"
            + "      alert(nodes[0].tagName);\n"
            + "    } catch(e) { alert('exception'); }\n"
            + "  }\n"
            + LOAD_XMLDOMDOCUMENT_FROM_URL_FUNCTION;

        final String xml
            = "<books>\n"
            + "  <book>\n"
            + "    <title>Immortality</title>\n"
            + "    <author>John Smith</author>\n"
            + "  </book>\n"
            + "</books>";

        getMockWebConnection().setResponse(URL_SECOND, xml, "text/xml");
        loadPageWithAlerts2(createTestHTML(html));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(IE)
    @Alerts({ "0", "1" })
    public void selectNodes_caseSensitive() throws Exception {
        final String html = ""
            + "  function test() {\n"
            + "    var doc = " + callLoadXMLDOMDocumentFromURL("'" + URL_SECOND + "'") + ";\n"
            + "    try {\n"
            + "      alert(doc.selectNodes('/bOoKs').length);\n"
            + "      alert(doc.selectNodes('/books').length);\n"
            + "    } catch(e) { alert('exception'); }\n"
            + "  }\n"
            + LOAD_XMLDOMDOCUMENT_FROM_URL_FUNCTION;

        final String xml
            = "<books>\n"
            + "  <book>\n"
            + "    <title>Immortality</title>\n"
            + "    <author>John Smith</author>\n"
            + "  </book>\n"
            + "</books>";

        getMockWebConnection().setResponse(URL_SECOND, xml, "text/xml");
        loadPageWithAlerts2(createTestHTML(html));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(IE)
    @Alerts({ "2", "1" })
    public void selectNodes_namespace() throws Exception {
        final String html = ""
            + "  function test() {\n"
            + "    var doc = " + callLoadXMLDOMDocumentFromURL("'" + URL_SECOND + "'") + ";\n"
            + "    try {\n"
            + "      alert(doc.selectNodes('//ns1:title').length);\n"
            + "      alert(doc.selectNodes('//ns2:title').length);\n"
            + "    } catch(e) { alert('exception'); }\n"
            + "  }\n"
            + LOAD_XMLDOMDOCUMENT_FROM_URL_FUNCTION;
        final String xml
            = "<ns1:books xmlns:ns1=\"http://one\">\n"
            + "  <ns2:book xmlns:ns2=\"http://two\">\n"
            + "    <ns2:title>Immortality</ns2:title>\n"
            + "    <ns2:author>John Smith</ns2:author>\n"
            + "  </ns2:book>\n"
            + "  <ns1:book>\n"
            + "    <ns1:title>The Hidden Secrets</ns1:title>\n"
            + "    <ns1:author>William Adams</ns1:author>\n"
            + "  </ns1:book>\n"
            + "  <ns1:book>\n"
            + "    <ns1:title>So What?</ns1:title>\n"
            + "    <ns1:author>Tony Walas</ns1:author>\n"
            + "  </ns1:book>\n"
            + "</ns1:books>";

        getMockWebConnection().setResponse(URL_SECOND, xml, "text/xml");
        loadPageWithAlerts2(createTestHTML(html));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(IE)
    @Alerts({ "book", "null", "book", "null" })
    public void selectNodes_nextNodeAndReset() throws Exception {
        final String html = ""
            + "  function test() {\n"
            + "    try {\n"
            + "      var doc = " + callLoadXMLDOMDocumentFromURL("'" + URL_SECOND + "'") + ";\n"
            + "      var nodes = doc.selectNodes('//book');\n"
            + "      alert(nodes.nextNode().nodeName);\n"
            + "      alert(nodes.nextNode());\n"
            + "      nodes.reset();\n"
            + "      alert(nodes.nextNode().nodeName);\n"
            + "      alert(nodes.nextNode());\n"
            + "    } catch(e) { alert('exception'); }\n"
            + "  }\n"
            + LOAD_XMLDOMDOCUMENT_FROM_URL_FUNCTION;

        final String xml
            = "<books>\n"
            + "  <book>\n"
            + "    <title>Immortality</title>\n"
            + "    <author>John Smith</author>\n"
            + "  </book>\n"
            + "</books>";

        getMockWebConnection().setResponse(URL_SECOND, xml, "text/xml");
        loadPageWithAlerts2(createTestHTML(html));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(IE)
    @Alerts({ "book", "#document", "book", "#document" })
    public void selectSingleNode() throws Exception {
        final String html = ""
            + "  function test() {\n"
            + "    var text='<book/>';\n"
            + "    try {\n"
            + "      var doc = " + callLoadXMLDOMDocumentFromString("text") + ";\n"
            + "      alert(doc.selectNodes('*')[0].nodeName);\n"
            + "      alert(doc.selectNodes('/')[0].nodeName);\n"
            + "      alert(doc.selectSingleNode('*').nodeName);\n"
            + "      alert(doc.selectNodes('*')[0].selectSingleNode('/').nodeName);\n"
            + "    } catch(e) { alert('exception'); }\n"
            + "  }\n"
            + LOAD_XMLDOMDOCUMENT_FROM_STRING_FUNCTION;
        loadPageWithAlerts2(createTestHTML(html));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(IE)
    @Alerts("someprefix:test")
    public void loadXML_Namespace() throws Exception {
        final String html = ""
            + "  function test() {\n"
            + "    var text='<someprefix:test xmlns:someprefix=\"http://myNS\"/>';\n"
            + "    var doc = " + callLoadXMLDOMDocumentFromString("text") + ";\n"
            + "    alert(doc.documentElement.tagName);\n"
            + "  }\n"
            + LOAD_XMLDOMDOCUMENT_FROM_STRING_FUNCTION;
        loadPageWithAlerts2(createTestHTML(html));
    }

    /**
     * Tests "xml:space" attribute.
     *
     * Xalan team response:<br>
     * "See the DOM Level 3 recommendation for discussion of this. XPath returns the start of the XPath text node,
     * which spans multiple DOM nodes. It is the DOM user's responsibility to gather the additional nodes,
     * either manually or by retrieving wholeText rather than value.<br>
     * This is unavoidable since DOM and XPath define the concept of "node" differently."
     *
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(IE)
    @Alerts("7")
    public void loadXML_XMLSpaceAttribute() throws Exception {
        final String html = ""
            + "  function test() {\n"
            + "    var text='<root xml:space=\\'preserve\\'>This t"
            + "<elem>ext has</elem> <![CDATA[ CDATA ]]>in<elem /> it</root>';\n"
            + "    var doc = " + callLoadXMLDOMDocumentFromString("text") + ";\n"
            + "    alert(doc.documentElement.childNodes.length);\n"
            + "  }\n"
            + LOAD_XMLDOMDOCUMENT_FROM_STRING_FUNCTION;
        loadPageWithAlerts2(createTestHTML(html));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(IE)
    @Alerts({ "same doc: false", "in first: 3", "book", "ownerDocument: doc1",
            "in 2nd: 3", "ownerDocument: doc2", "first child ownerDocument: doc2", "in first: 2", "in 2nd: 4",
            "ownerDocument: doc1", "in first: 2", "in 2nd: 3",
            "ownerDocument: doc2", "in first: 1", "in 2nd: 4" })
    public void moveChildBetweenDocuments() throws Exception {
        final String html = ""
            + "function test() {\n"
            + "  var doc1 = " + callLoadXMLDOMDocumentFromURL("'foo.xml'") + ";\n"
            + "  var doc2 = " + callLoadXMLDOMDocumentFromURL("'foo.xml'") + ";\n"
            + "  alert('same doc: ' + (doc1 == doc2));\n"
            + "  var doc1Root = doc1.firstChild;\n"
            + "  alert('in first: ' + doc1Root.childNodes.length);\n"
            + "  var doc1RootOriginalFirstChild = doc1Root.firstChild;\n"
            + "  alert(doc1RootOriginalFirstChild.tagName);\n"
            + "  alert('ownerDocument: ' + (doc1RootOriginalFirstChild.ownerDocument == doc1 ? 'doc1' : 'doc2'));\n"
            + "\n"
            + "  var doc2Root = doc2.firstChild;\n"
            + "  alert('in 2nd: ' + doc2Root.childNodes.length);\n"
            + "  doc2Root.appendChild(doc1RootOriginalFirstChild);\n"
            + "  alert('ownerDocument: ' + (doc1RootOriginalFirstChild.ownerDocument == doc1 ? 'doc1' : 'doc2'));\n"
            + "  alert('first child ownerDocument: ' + "
            + "(doc1RootOriginalFirstChild.firstChild.ownerDocument == doc1 ? 'doc1' : 'doc2'));\n"
            + "  alert('in first: ' + doc1Root.childNodes.length);\n"
            + "  alert('in 2nd: ' + doc2Root.childNodes.length);\n"
            + "\n"
            + "  doc1Root.replaceChild(doc1RootOriginalFirstChild, doc1Root.firstChild);\n"
            + "  alert('ownerDocument: ' + (doc1RootOriginalFirstChild.ownerDocument == doc1 ? 'doc1' : 'doc2'));\n"
            + "  alert('in first: ' + doc1Root.childNodes.length);\n"
            + "  alert('in 2nd: ' + doc2Root.childNodes.length);\n"
            + "\n"
            + "  doc2Root.insertBefore(doc1RootOriginalFirstChild, doc2Root.firstChild);\n"
            + "  alert('ownerDocument: ' + (doc1RootOriginalFirstChild.ownerDocument == doc1 ? 'doc1' : 'doc2'));\n"
            + "  alert('in first: ' + doc1Root.childNodes.length);\n"
            + "  alert('in 2nd: ' + doc2Root.childNodes.length);\n"
            + "\n"
            + "}\n"
            + LOAD_XMLDOMDOCUMENT_FROM_URL_FUNCTION;

        final String xml = "<order><book><title/></book><cd/><dvd/></order>";

        getMockWebConnection().setResponse(new URL(URL_FIRST + "foo.xml"), xml, "text/xml");
        loadPageWithAlerts2(createTestHTML(html));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(IE)
    @Alerts({ "1", "0", "1", "0" })
    public void getElementsByTagName() throws Exception {
        final String html = ""
            + "  function test() {\n"
            + "    var doc = " + callLoadXMLDOMDocumentFromURL("'" + URL_SECOND + "'") + ";\n"
            + "    alert(doc.getElementsByTagName('book').length);\n"
            + "    alert(doc.getElementsByTagName('soap:book').length);\n"
            + "    var elem = doc.getElementsByTagName('book')[0];\n"
            + "    alert(elem.getElementsByTagName('title').length);\n"
            + "    alert(elem.getElementsByTagName('soap:title').length);\n"
            + "  }\n"
            + LOAD_XMLDOMDOCUMENT_FROM_URL_FUNCTION;

        final String xml
            = "<soap:Envelope xmlns:soap='http://schemas.xmlsoap.org/soap/envelope/'>\n"
            + "  <books xmlns='http://www.example.com/ns1'>\n"
            + "    <book>\n"
            + "      <title>Immortality</title>\n"
            + "      <author>John Smith</author>\n"
            + "    </book>\n"
            + "  </books>\n"
            + "</soap:Envelope>";

        getMockWebConnection().setResponse(URL_SECOND, xml, "text/xml");
        loadPageWithAlerts2(createTestHTML(html));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(IE)
    @Alerts({ "0", "1", "0", "1" })
    public void getElementsByTagNameWithNamespace() throws Exception {
        final String html = ""
            + "  function test() {\n"
            + "    var doc = " + callLoadXMLDOMDocumentFromURL("'" + URL_SECOND + "'") + ";\n"
            + "    alert(doc.getElementsByTagName('book').length);\n"
            + "    alert(doc.getElementsByTagName('soap:book').length);\n"
            + "    var elem = doc.getElementsByTagName('soap:book')[0];\n"
            + "    alert(elem.getElementsByTagName('title').length);\n"
            + "    alert(elem.getElementsByTagName('soap:title').length);\n"
            + "  }\n"
            + LOAD_XMLDOMDOCUMENT_FROM_URL_FUNCTION;

        final String xml
            = "<soap:Envelope xmlns:soap='http://schemas.xmlsoap.org/soap/envelope/'>\n"
            + "  <books xmlns='http://www.example.com/ns1'>\n"
            + "    <soap:book>\n"
            + "      <soap:title>Immortality</soap:title>\n"
            + "      <author>John Smith</author>\n"
            + "    </soap:book>\n"
            + "  </books>\n"
            + "</soap:Envelope>";

        getMockWebConnection().setResponse(URL_SECOND, xml, "text/xml");
        loadPageWithAlerts2(createTestHTML(html));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(IE)
    @Alerts("1")
    public void xpathWithNamespaces() throws Exception {
        final String html = ""
            + "  function test() {\n"
            + "    var doc = " + callLoadXMLDOMDocumentFromURL("'" + URL_SECOND + "'") + ";\n"
            + "    try {\n"
            + "      alert(doc.selectNodes('//soap:book').length);\n"
            + "    } catch (e) {\n"
            + "      alert(doc.evaluate('count(//book)', doc.documentElement, "
            + "null, XPathResult.NUMBER_TYPE, null).numberValue);\n"
            + "    }\n"
            + "  }\n"
            + LOAD_XMLDOMDOCUMENT_FROM_URL_FUNCTION;

        final String xml
            = "<soap:Envelope xmlns:soap='http://schemas.xmlsoap.org/soap/envelope/'>\n"
            + "  <books xmlns='http://www.example.com/ns1'>\n"
            + "    <soap:book>\n"
            + "      <title>Immortality</title>\n"
            + "      <author>John Smith</author>\n"
            + "    </soap:book>\n"
            + "  </books>\n"
            + "</soap:Envelope>";

        getMockWebConnection().setResponse(URL_SECOND, xml, "text/xml");
        loadPageWithAlerts2(createTestHTML(html));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(IE)
    @Alerts("1")
    public void selectionNamespaces() throws Exception {
        final String html = ""
            + "  var selectionNamespaces = 'xmlns:s=\"http://schemas.xmlsoap.org/soap/envelope/\" "
                            + "xmlns:ns1=\"http://www.example.com/ns1\"';\n"
            + "  function test() {\n"
            + "    var doc = " + callCreateXMLDOMDocument() + ";\n"
            + "    doc.setProperty('SelectionNamespaces', selectionNamespaces);"
            + "    doc.async = false;\n"
            + "    doc.load('" + URL_SECOND + "');\n"
            + "    try {\n"
            + "      alert(doc.selectNodes('/s:Envelope/ns1:books/s:book').length);\n"
            + "    } catch (e) {\n"
            + "      alert(doc.evaluate('count(//book)', doc.documentElement, "
            + "null, XPathResult.NUMBER_TYPE, null).numberValue);\n"
            + "    }\n"
            + "  }\n"
            + CREATE_XMLDOMDOCUMENT_FUNCTION;

        final String xml = ""
            + "<soap:Envelope xmlns:soap='http://schemas.xmlsoap.org/soap/envelope/'>\n"
            + "  <books xmlns='http://www.example.com/ns1'>\n"
            + "    <soap:book>\n"
            + "      <title>Immortality</title>\n"
            + "      <author>John Smith</author>\n"
            + "    </soap:book>\n"
            + "  </books>\n"
            + "</soap:Envelope>";

        getMockWebConnection().setResponse(URL_SECOND, xml, "text/xml");
        loadPageWithAlerts2(createTestHTML(html));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(IE)
    @Alerts("null")
    public void nodeFromID() throws Exception {
        final String html = ""
            + "  function test() {\n"
            + "    var doc = " + callLoadXMLDOMDocumentFromURL("'" + URL_SECOND + "'") + ";\n"
            + "    alert(doc.nodeFromID('target'));\n"
            + "  }\n"
            + LOAD_XMLDOMDOCUMENT_FROM_URL_FUNCTION;

        final String xml
            = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
            + "<html xmlns=\"http://www.w3.org/1999/xhtml\">\n"
            + "  <body>\n"
            + "    <div id=\"target\"></div>\n"
            + "  </body>\n"
            + "</html>";

        getMockWebConnection().setResponse(URL_SECOND, xml, "text/xml");
        loadPageWithAlerts2(createTestHTML(html));
    }
}
