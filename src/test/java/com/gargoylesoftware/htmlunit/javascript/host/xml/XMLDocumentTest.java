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
package com.gargoylesoftware.htmlunit.javascript.host.xml;

import static java.nio.charset.StandardCharsets.ISO_8859_1;

import java.net.URL;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.HtmlUnitNYI;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.util.MimeType;

/**
 * Tests for {@link XMLDocument}.
 *
 * @author Ahmed Ashour
 * @author Marc Guillemot
 * @author Chuck Dumont
 * @author Frank Danek
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class XMLDocumentTest extends WebDriverTestCase {

    private static final String LOAD_XML_DOCUMENT_FROM_FILE_FUNCTION_NAME = "loadXMLDocumentFromFile";

    /** Helper. */
    public static final String LOAD_XML_DOCUMENT_FROM_FILE_FUNCTION = ""
            + "  function " + LOAD_XML_DOCUMENT_FROM_FILE_FUNCTION_NAME + "(file) {\n"
            + "    xhttp = new XMLHttpRequest();\n"
            + "    xhttp.open(\"GET\", file, false);\n"
            + "    xhttp.send();\n"
            + "    return xhttp.responseXML;\n"
            + "  }\n";

    /** Helper. */
    public static final String LOAD_NATIVE_XML_DOCUMENT_FROM_FILE_FUNCTION = ""
            + "  function " + LOAD_XML_DOCUMENT_FROM_FILE_FUNCTION_NAME + "(file) {\n"
            + "    xhttp = new XMLHttpRequest();\n"
            + "    xhttp.open(\"GET\", file, false);\n"
            + "    xhttp.send();\n"
            + "    return xhttp.responseXML;\n"
            + "  }\n";

    /** Helper. */
    public static final String LOAD_ACTIVEX_XML_DOCUMENT_FROM_FILE_FUNCTION = ""
            + "  function " + LOAD_XML_DOCUMENT_FROM_FILE_FUNCTION_NAME + "(file) {\n"
            + "    xhttp = new ActiveXObject(\"Microsoft.XMLHTTP\");\n"
            + "    xhttp.open(\"GET\", file, false);\n"
            + "    xhttp.send();\n"
            + "    return xhttp.responseXML;\n"
            + "  }\n";

    /**
     * Helper.
     * @param file the file parameter
     * @return xml helper
     */
    public static String callLoadXMLDocumentFromFile(final String file) {
        return LOAD_XML_DOCUMENT_FROM_FILE_FUNCTION_NAME + "(" + file + ")";
    }

    private static final String LOAD_XML_DOCUMENT_FROM_STRING_FUNCTION_NAME = "loadXMLDocumentFromString";

    /** Helper. */
    public static final String LOAD_XML_DOCUMENT_FROM_STRING_FUNCTION = ""
            + "  function " + LOAD_XML_DOCUMENT_FROM_STRING_FUNCTION_NAME + "(xml) {\n"
            + "    var parser = new DOMParser();\n"
            + "    return parser.parseFromString(xml,\"text/xml\");\n"
            + "  }\n";

    /**
     * Helper.
     * @param string the parameter
     * @return xml helper
     */
    public static String callLoadXMLDocumentFromString(final String string) {
        return LOAD_XML_DOCUMENT_FROM_STRING_FUNCTION_NAME + "(" + string + ")";
    }

    private static final String SERIALIZE_XML_DOCUMENT_TO_STRING_FUNCTION_NAME = "serializeXMLDocumentToString";

    /** Helper. */
    public static final String SERIALIZE_XML_DOCUMENT_TO_STRING_FUNCTION = ""
            + "  function " + SERIALIZE_XML_DOCUMENT_TO_STRING_FUNCTION_NAME + "(doc) {\n"
            + "    return new XMLSerializer().serializeToString(doc);\n"
            + "  }\n";

    /** Helper. */
    public static final String SERIALIZE_NATIVE_XML_DOCUMENT_TO_STRING_FUNCTION = ""
            + "  function " + SERIALIZE_XML_DOCUMENT_TO_STRING_FUNCTION_NAME + "(doc) {\n"
            + "    serializer = new XMLSerializer();\n"
            + "    return serializer.serializeToString(doc);\n"
            + "  }\n";

    /** Helper. */
    public static final String SERIALIZE_ACTIVEX_XML_DOCUMENT_TO_STRING_FUNCTION = ""
            + "  function " + SERIALIZE_XML_DOCUMENT_TO_STRING_FUNCTION_NAME + "(doc) {\n"
            + "    return doc.xml;\n"
            + "  }\n";

    /**
     * Helper.
     * @param doc the doc parameter
     * @return xml helper
     */
    public static String callSerializeXMLDocumentToString(final String doc) {
        return SERIALIZE_XML_DOCUMENT_TO_STRING_FUNCTION_NAME + "(" + doc + ")";
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"undefined", "undefined"})
    public void async() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var doc = document.implementation.createDocument('', '', null);\n"
            + "    alert(document.async);\n"
            + "    alert(doc.async);\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void load() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var doc = document.implementation.createDocument('', '', null);\n"
            + "    doc.async = false;\n"
            + "    try {\n"
            + "      alert(doc.load('" + URL_SECOND + "'));\n"
            + "      alert(doc.documentElement.nodeName);\n"
            + "      alert(doc.childNodes[0].nodeName);\n"
            + "      alert(doc.childNodes[0].childNodes.length);\n"
            + "      alert(doc.childNodes[0].childNodes[0].nodeName);\n"
            + "      alert(doc.getElementsByTagName('books').item(0).attributes.length);\n"
            + "    } catch(e) { alert('exception'); }\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        final String xml
            = "<books>\n"
            + "  <book>\n"
            + "    <title>Immortality</title>\n"
            + "    <author>John Smith</author>\n"
            + "  </book>\n"
            + "</books>";

        getMockWebConnection().setResponse(URL_SECOND, xml, MimeType.TEXT_XML);
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    // TODO what is the difference to load()?
    public void load_relativeURL() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var doc = document.implementation.createDocument('', '', null);\n"
            + "    doc.async = false;\n"
            + "    try {\n"
            + "      alert(doc.load('" + URL_SECOND + "'));\n"
            + "      alert(doc.documentElement.nodeName);\n"
            + "      alert(doc.childNodes[0].nodeName);\n"
            + "      alert(doc.childNodes[0].childNodes.length);\n"
            + "      alert(doc.childNodes[0].childNodes[0].nodeName);\n"
            + "      alert(doc.getElementsByTagName('books').item(0).attributes.length);\n"
            + "    } catch(e) { alert('exception'); }\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        final String xml
            = "<books>\n"
            + "  <book>\n"
            + "    <title>Immortality</title>\n"
            + "    <author>John Smith</author>\n"
            + "  </book>\n"
            + "</books>";

        getMockWebConnection().setResponse(URL_SECOND, xml, MimeType.TEXT_XML);
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("undefined")
    public void preserveWhiteSpace() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var doc = document.implementation.createDocument('', '', null);\n"
            + "    alert(doc.preserveWhiteSpace);\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void setProperty() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var doc = document.implementation.createDocument('', '', null);\n"
            + "    try {\n"
            + "      doc.setProperty('SelectionNamespaces', \"xmlns:xsl='http://www.w3.org/1999/XSL/Transform'\");\n"
            + "      doc.setProperty('SelectionLanguage', 'XPath');\n"
            + "    } catch(e) { alert('exception'); }\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void selectNodes() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var doc = " + callLoadXMLDocumentFromFile("'" + URL_SECOND + "'") + ";\n"
            + "    try {\n"
            + "      var nodes = doc.selectNodes('/books');\n"
            + "      alert(nodes.length);\n"
            + "      alert(nodes[0].tagName);\n"
            + "    } catch(e) { alert('exception'); }\n"
            + "  }\n"
            + LOAD_XML_DOCUMENT_FROM_FILE_FUNCTION
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        final String xml
            = "<books>\n"
            + "  <book>\n"
            + "    <title>Immortality</title>\n"
            + "    <author>John Smith</author>\n"
            + "  </book>\n"
            + "</books>";

        getMockWebConnection().setResponse(URL_SECOND, xml, MimeType.TEXT_XML);
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void selectNodes_caseSensitive() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var doc = " + callLoadXMLDocumentFromFile("'" + URL_SECOND + "'") + ";\n"
            + "    try {\n"
            + "      alert(doc.selectNodes('/bOoKs').length);\n"
            + "      alert(doc.selectNodes('/books').length);\n"
            + "    } catch(e) { alert('exception'); }\n"
            + "  }\n"
            + LOAD_XML_DOCUMENT_FROM_FILE_FUNCTION
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        final String xml
            = "<books>\n"
            + "  <book>\n"
            + "    <title>Immortality</title>\n"
            + "    <author>John Smith</author>\n"
            + "  </book>\n"
            + "</books>";

        getMockWebConnection().setResponse(URL_SECOND, xml, MimeType.TEXT_XML);
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void selectNodes_namespace() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var doc = " + callLoadXMLDocumentFromFile("'" + URL_SECOND + "'") + ";\n"
            + "    try {\n"
            + "      alert(doc.selectNodes('//ns1:title').length);\n"
            + "      alert(doc.selectNodes('//ns2:title').length);\n"
            + "    } catch(e) { alert('exception'); }\n"
            + "  }\n"
            + LOAD_XML_DOCUMENT_FROM_FILE_FUNCTION
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";
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

        getMockWebConnection().setResponse(URL_SECOND, xml, MimeType.TEXT_XML);
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void selectNodes_nextNodeAndReset() throws Exception {
        final String html = "<html><head><script>\n"
            + "  function test() {\n"
            + "    try {\n"
            + "      var doc = " + callLoadXMLDocumentFromFile("'" + URL_SECOND + "'") + ";\n"
            + "      var nodes = doc.selectNodes('//book');\n"
            + "      alert(nodes.nextNode().nodeName);\n"
            + "      alert(nodes.nextNode());\n"
            + "      nodes.reset();\n"
            + "      alert(nodes.nextNode().nodeName);\n"
            + "      alert(nodes.nextNode());\n"
            + "    } catch(e) { alert('exception'); }\n"
            + "  }\n"
            + LOAD_XML_DOCUMENT_FROM_FILE_FUNCTION
            + "</script></head><body onload='test()'>foo</body></html>";

        final String xml
            = "<books>\n"
            + "  <book>\n"
            + "    <title>Immortality</title>\n"
            + "    <author>John Smith</author>\n"
            + "  </book>\n"
            + "</books>";

        getMockWebConnection().setResponse(URL_SECOND, xml, MimeType.TEXT_XML);
        loadPageWithAlerts2(html);
    }

    /**
     * Test that element.selectNodes("/tagName") searches from root of the tree, not from that specific element.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"book", "exception /title", "exception title"})
    public void selectNodes_fromRoot() throws Exception {
        // IE works only if running alone
        shutDownRealIE();

        final String html = "<html><head><script>\n"
            + "  function test() {\n"
            + "    try {\n"
            + "      var doc = " + callLoadXMLDocumentFromFile("'" + URL_SECOND + "'") + ";\n"
            + "      var child = doc.documentElement.firstChild;\n"
            + "      alert(child.tagName);\n"

            + "      try {\n"
            + "        alert(child.selectNodes('/title').length);\n"
            + "      } catch(e) { alert('exception /title'); }\n"

            + "      try {\n"
            + "        alert(child.selectNodes('title').length);\n"
            + "      } catch(e) { alert('exception title'); }\n"
            + "    } catch(e) { alert('exception'); }\n"
            + "  }\n"
            + LOAD_XML_DOCUMENT_FROM_FILE_FUNCTION
            + "</script></head><body onload='test()'>foo</body></html>";

        final String xml = "<books><book><title>Immortality</title><author>John Smith</author></book></books>";

        getMockWebConnection().setResponse(URL_SECOND, xml, MimeType.TEXT_XML);
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void selectSingleNode() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var text='<book/>';\n"
            + "    try {\n"
            + "      var doc = " + callLoadXMLDocumentFromString("text") + ";\n"
            + "      alert(doc.selectNodes('*')[0].nodeName);\n"
            + "      alert(doc.selectNodes('/')[0].nodeName);\n"
            + "      alert(doc.selectSingleNode('*').nodeName);\n"
            + "      alert(doc.selectNodes('*')[0].selectSingleNode('/').nodeName);\n"
            + "    } catch(e) { alert('exception'); }\n"
            + "  }\n"
            + LOAD_XML_DOCUMENT_FROM_STRING_FUNCTION
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("someprefix:test")
    public void loadXML_Namespace() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var text='<someprefix:test xmlns:someprefix=\"http://myNS\"/>';\n"
            + "    var doc = " + callLoadXMLDocumentFromString("text") + ";\n"
            + "    alert(doc.documentElement.tagName);\n"
            + "  }\n"
            + LOAD_XML_DOCUMENT_FROM_STRING_FUNCTION
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
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
    @Alerts("7")
    public void loadXML_XMLSpaceAttribute() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var text='<root xml:space=\\'preserve\\'>This t"
            + "<elem>ext has</elem> <![CDATA[ CDATA ]]>in<elem /> it</root>';\n"
            + "    var doc = " + callLoadXMLDocumentFromString("text") + ";\n"
            + "    alert(doc.documentElement.childNodes.length);\n"
            + "  }\n"
            + LOAD_XML_DOCUMENT_FROM_STRING_FUNCTION
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            IE = {"true", "true", "true", "true", "true", "true", "true", "true",
                "false", "true", "true", "true", "true", "true", "true", "true", "true"})
    public void parseError() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    try {\n"
            + "      var doc = new ActiveXObject('Microsoft.XMLDOM');\n"
            + "      alert(doc.documentElement == null);\n"
            + "      alert(doc.parseError.errorCode === 0);\n"
            + "      alert(doc.parseError.filepos === 0);\n"
            + "      alert(doc.parseError.line === 0);\n"
            + "      alert(doc.parseError.linepos === 0);\n"
            + "      alert(doc.parseError.reason === '');\n"
            + "      alert(doc.parseError.srcText === '');\n"
            + "      alert(doc.parseError.url === '');\n"
            + "      doc.async = false;\n"
            + "      alert(doc.load('" + URL_SECOND + "'));\n"
            + "      alert(doc.documentElement == null);\n"
            + "      alert(doc.parseError.errorCode !== 0);\n"
            + "      alert(doc.parseError.filepos !== 0);\n"
            + "      alert(doc.parseError.line !== 0);\n"
            + "      alert(doc.parseError.linepos !== 0);\n"
            + "      alert(doc.parseError.reason !== '');\n"
            + "      alert(doc.parseError.srcText !== '');\n"
            + "      alert(doc.parseError.url !== '');\n"
            + "    } catch(e) { alert('exception'); }\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        final String xml
            = "<root>\n"
            + "  <element>\n"
            + "</root>";

        getMockWebConnection().setResponse(URL_SECOND, xml, MimeType.TEXT_XML);
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "http://myNS",
            IE = {})
    public void createNSResolver() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var text='<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\\n';\n"
            + "    text += '<xsl:stylesheet version=\"1.0\" xmlns:xsl=\"http://myNS\">\\n';\n"
            + "    text += '  <xsl:template match=\"/\">\\n';\n"
            + "    text += '  <html>\\n';\n"
            + "    text += '    <body>\\n';\n"
            + "    text += '    </body>\\n';\n"
            + "    text += '  </html>\\n';\n"
            + "    text += '  </xsl:template>\\n';\n"
            + "    text += '</xsl:stylesheet>';\n"
            + "    var doc = " + callLoadXMLDocumentFromString("text") + ";\n"
            + "    if (doc.createNSResolver) {\n"
            + "      alert(doc.createNSResolver(doc.documentElement).lookupNamespaceURI('xsl'));\n"
            + "    }\n"
            + "  }\n"
            + LOAD_XML_DOCUMENT_FROM_STRING_FUNCTION
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void xmlInsideHtml() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    try {\n"
            + "      alert(messageTableHeaders.documentElement.nodeName);\n"
            + "    } catch(e) {alert('exception'); }\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <xml id='messageTableHeaders'>\n"
            + "    <columns>\n"
            + "      <column name='_checkbox'/>\n"
            + "      <column name='itemStatus'/>\n"
            + "    </columns>\n"
            + "  </xml>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void instanceOf() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var x = " + callLoadXMLDocumentFromString("'<x/>'") + ";\n"
            + "    try {\n"
            + "      alert(x instanceof XMLDocument);\n"
            + "    }catch(e) {alert('exception')}\n"
            + "  }\n"
            + LOAD_XML_DOCUMENT_FROM_STRING_FUNCTION
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <xml id='messageTableHeaders'>\n"
            + "    <columns>\n"
            + "      <column name='_checkbox'/>\n"
            + "      <column name='itemStatus'/>\n"
            + "    </columns>\n"
            + "  </xml>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "button",
            IE = {})
    public void evaluate() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var s = '<toolbar><button id=\"compose_button\"/></toolbar>';\n"
            + "    var xDoc = " + callLoadXMLDocumentFromString("s") + ";\n"
            + "    if (xDoc.evaluate) {\n"
            + "      var r = xDoc.evaluate(\"button[@id='compose_button']\", xDoc.firstChild, null, 9, null);\n"
            + "      alert(r.singleNodeValue.tagName);\n"
            + "    }\n"
            + "  }\n"
            + LOAD_XML_DOCUMENT_FROM_STRING_FUNCTION
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"same doc: false", "in first: 3", "book", "ownerDocument: doc1",
            "in 2nd: 3", "ownerDocument: doc2", "first child ownerDocument: doc2", "in first: 2", "in 2nd: 4",
            "ownerDocument: doc1", "in first: 2", "in 2nd: 3",
            "ownerDocument: doc2", "in first: 1", "in 2nd: 4"})
    public void moveChildBetweenDocuments() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "function test() {\n"
            + "  var doc1 = " + callLoadXMLDocumentFromFile("'foo.xml'") + ";\n"
            + "  var doc2 = " + callLoadXMLDocumentFromFile("'foo.xml'") + ";\n"
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
            + LOAD_XML_DOCUMENT_FROM_FILE_FUNCTION
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        final String xml = "<order><book><title/></book><cd/><dvd/></order>";

        getMockWebConnection().setResponse(new URL(URL_FIRST, "foo.xml"), xml, MimeType.TEXT_XML);
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"1", "0", "1", "0"})
    public void getElementsByTagName() throws Exception {
        shutDownRealIE();

        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var doc = " + callLoadXMLDocumentFromFile("'" + URL_SECOND + "'") + ";\n"
            + "    alert(doc.getElementsByTagName('book').length);\n"
            + "    alert(doc.getElementsByTagName('soap:book').length);\n"
            + "    var elem = doc.getElementsByTagName('book')[0];\n"
            + "    alert(elem.getElementsByTagName('title').length);\n"
            + "    alert(elem.getElementsByTagName('soap:title').length);\n"
            + "  }\n"
            + LOAD_XML_DOCUMENT_FROM_FILE_FUNCTION
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        final String xml
            = "<soap:Envelope xmlns:soap='http://schemas.xmlsoap.org/soap/envelope/'>\n"
            + "  <books xmlns='http://www.example.com/ns1'>\n"
            + "    <book>\n"
            + "      <title>Immortality</title>\n"
            + "      <author>John Smith</author>\n"
            + "    </book>\n"
            + "  </books>\n"
            + "</soap:Envelope>";

        getMockWebConnection().setResponse(URL_SECOND, xml, MimeType.TEXT_XML);
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"0", "1", "0", "1"},
            IE = {"1", "0"})
    public void getElementsByTagNameWithNamespace() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var doc = " + callLoadXMLDocumentFromFile("'" + URL_SECOND + "'") + ";\n"
            + "    alert(doc.getElementsByTagName('book').length);\n"
            + "    alert(doc.getElementsByTagName('soap:book').length);\n"
            + "    if (doc.getElementsByTagName('soap:book').length != 0) {\n"
            + "      var elem = doc.getElementsByTagName('soap:book')[0];\n"
            + "      alert(elem.getElementsByTagName('title').length);\n"
            + "      alert(elem.getElementsByTagName('soap:title').length);\n"
            + "    }\n"
            + "  }\n"
            + LOAD_XML_DOCUMENT_FROM_FILE_FUNCTION
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        final String xml
            = "<soap:Envelope xmlns:soap='http://schemas.xmlsoap.org/soap/envelope/'>\n"
            + "  <books xmlns='http://www.example.com/ns1'>\n"
            + "    <soap:book>\n"
            + "      <soap:title>Immortality</soap:title>\n"
            + "      <author>John Smith</author>\n"
            + "    </soap:book>\n"
            + "  </books>\n"
            + "</soap:Envelope>";

        getMockWebConnection().setResponse(URL_SECOND, xml, MimeType.TEXT_XML);
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"false", "false", "true", "false"},
            IE = {"false", "false", "false", "false"})
    // XML ID handling not yet correctly implemented
    public void getElementById_xml() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var text='<?xml version=\"1.0\" encoding=\"utf-8\"?>\\n'\n"
            + "      + '<!DOCTYPE idTest [\\n'\n"
            + "      + '    <!ATTLIST item xId ID #IMPLIED>\\n'\n"
            + "      + ']>\\n'\n"
            + "      + '<idTest>\\n'\n"
            + "      + '    <item xId=\"item1\" />\\n'\n"
            + "      + '    <item xml:id=\"item2\" />\\n'\n"
            + "      + '    <item id=\"item3\" />\\n'\n"
            + "      + '    <item ID=\"item4\" />\\n'\n"
            + "      + '</idTest>';\n"
            + "    try {\n"
            + "      var doc = " + callLoadXMLDocumentFromString("text") + ";\n"
            + "      alert(doc.getElementById('item1') != null);\n"
            + "      alert(doc.getElementById('item2') != null);\n"
            + "      alert(doc.getElementById('item3') != null);\n"
            + "      alert(doc.getElementById('item4') != null);\n"
            + "    } catch(e) { alert('exception'); }\n"
            + "  }\n"
            + LOAD_XML_DOCUMENT_FROM_STRING_FUNCTION
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"true", "true"},
            IE = {"false", "false"})
    // XML ID handling not yet correctly implemented
    public void getElementById_html() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var text='<form id=\"form1\">\\n'\n"
            + "      + '    <div id=\"div1\"></div>\\n'\n"
            + "      + '</form>';\n"
            + "    try {\n"
            + "      var doc = " + callLoadXMLDocumentFromString("text") + ";\n"
            + "      alert(doc.getElementById('form1') != null);\n"
            + "      alert(doc.getElementById('div1') != null);\n"
            + "    } catch(e) { alert('exception'); }\n"
            + "  }\n"
            + LOAD_XML_DOCUMENT_FROM_STRING_FUNCTION
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"true", "true"})
    public void getElementById_xhtml() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var text='<form xmlns=\"http://www.w3.org/1999/xhtml\" id=\"form1\">\\n'\n"
            + "      + '    <div id=\"div1\"></div>\\n'\n"
            + "      + '</form>';\n"
            + "    try {\n"
            + "      var doc = " + callLoadXMLDocumentFromString("text") + ";\n"
            + "      alert(doc.getElementById('form1') != null);\n"
            + "      alert(doc.getElementById('div1') != null);\n"
            + "    } catch(e) { alert('exception'); }\n"
            + "  }\n"
            + LOAD_XML_DOCUMENT_FROM_STRING_FUNCTION
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "0",
            IE = "exception")
    public void xpathWithNamespaces() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var doc = " + callLoadXMLDocumentFromFile("'" + URL_SECOND + "'") + ";\n"
            + "    try {\n"
            + "      alert(doc.selectNodes('//soap:book').length);\n"
            + "    } catch (e) {\n"
            + "      try {\n"
            + "      alert(doc.evaluate('count(//book)', doc.documentElement, "
            + "null, XPathResult.NUMBER_TYPE, null).numberValue);\n"
            + "      } catch (e) {\n"
            + "        alert('exception');\n"
            + "      }\n"
            + "    }\n"
            + "  }\n"
            + LOAD_XML_DOCUMENT_FROM_FILE_FUNCTION
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        final String xml
            = "<soap:Envelope xmlns:soap='http://schemas.xmlsoap.org/soap/envelope/'>\n"
            + "  <books xmlns='http://www.example.com/ns1'>\n"
            + "    <soap:book>\n"
            + "      <title>Immortality</title>\n"
            + "      <author>John Smith</author>\n"
            + "    </soap:book>\n"
            + "  </books>\n"
            + "</soap:Envelope>";

        getMockWebConnection().setResponse(URL_SECOND, xml, MimeType.TEXT_XML);
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {},
            IE = "1")
    public void selectionNamespaces() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  var selectionNamespaces = 'xmlns:s=\"http://schemas.xmlsoap.org/soap/envelope/\" "
                            + "xmlns:ns1=\"http://www.example.com/ns1\"';\n"
            + "  function test() {\n"
            + "  if ('ActiveXObject' in window) {\n"
            + "    var doc = new ActiveXObject('Microsoft.XMLDOM');\n"
            + "    doc.setProperty('SelectionNamespaces', selectionNamespaces);\n"
            + "    doc.async = false;\n"
            + "    doc.load('" + URL_SECOND + "');\n"
            + "    try {\n"
            + "      alert(doc.selectNodes('/s:Envelope/ns1:books/s:book').length);\n"
            + "    } catch (e) {\n"
            + "      alert(doc.evaluate('count(//book)', doc.documentElement, "
            + "null, XPathResult.NUMBER_TYPE, null).numberValue);\n"
            + "    }}\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        final String xml
            = "<soap:Envelope xmlns:soap='http://schemas.xmlsoap.org/soap/envelope/'>\n"
            + "  <books xmlns='http://www.example.com/ns1'>\n"
            + "    <soap:book>\n"
            + "      <title>Immortality</title>\n"
            + "      <author>John Smith</author>\n"
            + "    </soap:book>\n"
            + "  </books>\n"
            + "</soap:Envelope>";

        getMockWebConnection().setResponse(URL_SECOND, xml, MimeType.TEXT_XML);
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("nodeFromID not available")
    public void nodeFromID() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var doc = " + callLoadXMLDocumentFromFile("'" + URL_SECOND + "'") + ";\n"
            + "    try {\n"
            + "      alert('nodeFromID ' + doc.nodeFromID('target'));\n"
            + "    } catch (e) {\n"
            + "      alert('nodeFromID not available');\n"
            + "    }\n"
            + "  }\n"
            + LOAD_XML_DOCUMENT_FROM_FILE_FUNCTION
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        final String xml
            = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
            + "<html xmlns=\"http://www.w3.org/1999/xhtml\">\n"
            + "  <body>\n"
            + "    <div id=\"target\"></div>\n"
            + "  </body>\n"
            + "</html>";

        getMockWebConnection().setResponse(URL_SECOND, xml, MimeType.TEXT_XML);
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"[object XMLDocument]", "OK"},
            IE = {"[object Document]", "OK"})
    @HtmlUnitNYI(IE = {"[object XMLDocument]", "OK"})
    // Real IE seems to generate always an (HTML)Document within an iframe.
    public void test() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var ifr = document.getElementById('ifr');\n"
            + "    ifr.onload = function() {\n"
            + "      var xml = ifr.contentWindow.document;\n"
            + "      alert(xml);\n"
            + "      alert(xml.getElementsByTagName('status')[0].textContent);\n"
            + "    };\n"
            + "    ifr.src = '" + URL_SECOND + "';\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "  <iframe id='ifr'></iframe>\n"
            + "</body></html>";

        final String xml
            = "<response>\n"
            + "  <status>OK</status>\n"
            + "</response>";

        getMockWebConnection().setResponse(URL_SECOND, xml, MimeType.TEXT_XML);
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "[object HTMLDocument]",
            IE = "[object Document]")
    @HtmlUnitNYI(IE = "[object HTMLDocument]")
    public void html() throws Exception {
        final String svg
            = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"
            + "<svg xmlns=\"http://www.w3.org/2000/svg\">\n"
            + "  <rect id=\"rect\" width=\"50\" height=\"50\" fill=\"green\" onclick=\"alert(document)\"/>\n"
            + "</svg>";
        final WebDriver driver = loadPage2(svg);
        driver.findElement(By.id("rect")).click();

        verifyAlerts(driver, getExpectedAlerts());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "[object XMLDocument]",
            IE = "[object Document]")
    @NotYetImplemented
    public void svg() throws Exception {
        final String svg
            = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"
            + "<svg xmlns=\"http://www.w3.org/2000/svg\">\n"
            + "  <rect id=\"rect\" width=\"50\" height=\"50\" fill=\"green\" onclick=\"alert(document)\"/>\n"
            + "</svg>";
        final WebDriver driver = loadPage2(svg, URL_FIRST, MimeType.TEXT_XML, ISO_8859_1);
        driver.findElement(By.id("rect")).click();

        verifyAlerts(driver, getExpectedAlerts());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"myAttr", ""})
    public void createAttributeNameValue() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var doc = document.implementation.createDocument('', '', null);\n"
            + "    var node = doc.createAttribute('myAttr');\n"
            + "    alert(node.name);\n"
            + "    alert(node.value);\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "  <div id='tester'></div>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "about:blank",
            IE = "§§URL§§")
    @HtmlUnitNYI(IE = "about:blank")
    public void url() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var doc = document.implementation.createDocument('', '', null);\n"
            + "    alert(doc.URL);\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "  <div id='tester'></div>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }
}
