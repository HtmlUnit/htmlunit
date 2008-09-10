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

import static org.junit.Assert.fail;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.httpclient.NameValuePair;
import org.junit.Test;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.CollectingAlertHandler;
import com.gargoylesoftware.htmlunit.MockWebConnection;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebTestCase;

/**
 * Tests for {@link XMLDocument}.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 * @author Marc Guillemot
 */
public class XMLDocumentTest extends WebTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testAsync() throws Exception {
        testAsync(BrowserVersion.INTERNET_EXPLORER_7_0);
        testAsync(BrowserVersion.FIREFOX_2);
    }

    private void testAsync(final BrowserVersion browserVersion) throws Exception {
        final String content = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var doc = createXmlDocument();\n"
            + "    alert(document.async);\n"
            + "    alert(doc.async);\n"
            + "  }\n"
            + "  function createXmlDocument() {\n"
            + "    if (document.implementation && document.implementation.createDocument)\n"
            + "      return document.implementation.createDocument('', '', null);\n"
            + "    else if (window.ActiveXObject)\n"
            + "      return new ActiveXObject('Microsoft.XMLDOM');\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        final String[] expectedAlerts = {"undefined", "true"};
        final List<String> collectedAlerts = new ArrayList<String>();
        loadPage(browserVersion, content, collectedAlerts);
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testLoad() throws Exception {
        testLoad(BrowserVersion.INTERNET_EXPLORER_7_0, new String[] {"true", "books", "books", "1", "book", "0"});
        testLoad(BrowserVersion.FIREFOX_2, new String[] {"true", "books", "books", "3", "#text", "0"});
    }

    private void testLoad(final BrowserVersion browserVersion, final String[] expectedAlerts) throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var doc = createXmlDocument();\n"
            + "    doc.async = false;\n"
            + "    alert(doc.load('" + URL_SECOND + "'));\n"
            + "    alert(doc.documentElement.nodeName);\n"
            + "    alert(doc.childNodes[0].nodeName);\n"
            + "    alert(doc.childNodes[0].childNodes.length);\n"
            + "    alert(doc.childNodes[0].childNodes[0].nodeName);\n"
            + "    alert(doc.getElementsByTagName('books').item(0).attributes.length);\n"
            + "  }\n"
            + "  function createXmlDocument() {\n"
            + "    if (document.implementation && document.implementation.createDocument)\n"
            + "      return document.implementation.createDocument('', '', null);\n"
            + "    else if (window.ActiveXObject)\n"
            + "      return new ActiveXObject('Microsoft.XMLDOM');\n"
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

        final List<String> collectedAlerts = new ArrayList<String>();
        final WebClient client = new WebClient(browserVersion);
        client.setAlertHandler(new CollectingAlertHandler(collectedAlerts));
        final MockWebConnection conn = new MockWebConnection();
        conn.setResponse(URL_FIRST, html);
        conn.setResponse(URL_SECOND, xml, "text/xml");
        client.setWebConnection(conn);

        client.getPage(URL_FIRST);
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testLoad_relativeURL() throws Exception {
        testLoad_relativeURL(BrowserVersion.INTERNET_EXPLORER_7_0,
                new String[] {"true", "books", "books", "1", "book"});
        testLoad_relativeURL(BrowserVersion.FIREFOX_2,
                new String[] {"true", "books", "books", "3", "#text"});
    }

    private void testLoad_relativeURL(final BrowserVersion browserVersion, final String[] expectedAlerts)
        throws Exception {
        final URL firstURL = new URL("http://htmlunit/first.html");
        final URL secondURL = new URL("http://htmlunit/second.xml");

        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var doc = createXmlDocument();\n"
            + "    doc.async = false;\n"
            + "    alert(doc.load('" + "second.xml" + "'));\n"
            + "    alert(doc.documentElement.nodeName);\n"
            + "    alert(doc.childNodes[0].nodeName);\n"
            + "    alert(doc.childNodes[0].childNodes.length);\n"
            + "    alert(doc.childNodes[0].childNodes[0].nodeName);\n"
            + "  }\n"
            + "  function createXmlDocument() {\n"
            + "    if (document.implementation && document.implementation.createDocument)\n"
            + "      return document.implementation.createDocument('', '', null);\n"
            + "    else if (window.ActiveXObject)\n"
            + "      return new ActiveXObject('Microsoft.XMLDOM');\n"
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

        final List<String> collectedAlerts = new ArrayList<String>();
        final WebClient client = new WebClient(browserVersion);
        client.setAlertHandler(new CollectingAlertHandler(collectedAlerts));
        final MockWebConnection conn = new MockWebConnection();
        conn.setResponse(firstURL, html);
        conn.setResponse(secondURL, xml, "text/xml");
        client.setWebConnection(conn);

        client.getPage(firstURL);
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testPreserveWhiteSpace() throws Exception {
        final String content = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var doc = new ActiveXObject('MSXML2.DOMDocument');\n"
            + "    alert(doc.preserveWhiteSpace);\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";
        final String[] expectedAlerts = {"false"};
        final List<String> collectedAlerts = new ArrayList<String>();
        loadPage(BrowserVersion.INTERNET_EXPLORER_7_0, content, collectedAlerts);
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testSetProperty() throws Exception {
        final String content = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var doc = new ActiveXObject('MSXML2.DOMDocument');\n"
            + "    doc.setProperty('SelectionNamespaces', \"xmlns:xsl='http://www.w3.org/1999/XSL/Transform'\");\n"
            + "    doc.setProperty('SelectionLanguage', 'XPath');\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";
        loadPage(BrowserVersion.INTERNET_EXPLORER_7_0, content, null);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testSelectNodes() throws Exception {
        testSelectNodes(BrowserVersion.INTERNET_EXPLORER_7_0, new String[] {"true", "1", "books"});
        try {
            testSelectNodes(BrowserVersion.FIREFOX_2, new String[] {"true", "1", "books"});
            fail("selectNodes is not supported in Firefox.");
        }
        catch (final Exception e) {
            //expected
        }
    }

    private void testSelectNodes(final BrowserVersion browserVersion, final String[] expectedAlerts) throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var doc = createXmlDocument();\n"
            + "    doc.async = false;\n"
            + "    alert(doc.load('" + URL_SECOND + "'));\n"
            + "    var nodes = doc.selectNodes('/books');\n"
            + "    alert(nodes.length);\n"
            + "    alert(nodes[0].tagName);\n"
            + "  }\n"
            + "  function createXmlDocument() {\n"
            + "    if (document.implementation && document.implementation.createDocument)\n"
            + "      return document.implementation.createDocument('', '', null);\n"
            + "    else if (window.ActiveXObject)\n"
            + "      return new ActiveXObject('Microsoft.XMLDOM');\n"
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

        final List<String> collectedAlerts = new ArrayList<String>();
        final WebClient client = new WebClient(browserVersion);
        client.setAlertHandler(new CollectingAlertHandler(collectedAlerts));
        final MockWebConnection conn = new MockWebConnection();
        conn.setResponse(URL_FIRST, html);
        conn.setResponse(URL_SECOND, xml, "text/xml");
        client.setWebConnection(conn);

        client.getPage(URL_FIRST);
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testSelectNodes_Namespace() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var doc = createXmlDocument();\n"
            + "    doc.async = false;\n"
            + "    alert(doc.load('" + URL_SECOND + "'));\n"
            + "    alert(doc.selectNodes('//ns1:title').length)\n"
            + "    alert(doc.selectNodes('//ns2:title').length)\n"
            + "  }\n"
            + "  function createXmlDocument() {\n"
            + "    if (document.implementation && document.implementation.createDocument)\n"
            + "      return document.implementation.createDocument('', '', null);\n"
            + "    else if (window.ActiveXObject)\n"
            + "      return new ActiveXObject('Microsoft.XMLDOM');\n"
            + "  }\n"
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

        final String[] expectedAlerts = {"true", "2", "1"};
        final List<String> collectedAlerts = new ArrayList<String>();
        final WebClient client = new WebClient(BrowserVersion.INTERNET_EXPLORER_7_0);
        client.setAlertHandler(new CollectingAlertHandler(collectedAlerts));
        final MockWebConnection conn = new MockWebConnection();
        conn.setResponse(URL_FIRST, html);
        conn.setResponse(URL_SECOND, xml, "text/xml");
        client.setWebConnection(conn);

        client.getPage(URL_FIRST);
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testSelectSingleNode() throws Exception {
        final String content = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var text='<book/>';\n"
            + "    var doc=new ActiveXObject('Microsoft.XMLDOM');\n"
            + "    doc.async=false;\n"
            + "    doc.loadXML(text);\n"
            + "    alert(doc.selectNodes('*')[0].nodeName);\n"
            + "    alert(doc.selectNodes('/')[0].nodeName);\n"
            + "    alert(doc.selectSingleNode('*').nodeName);\n"
            + "    alert(doc.selectNodes('*')[0].selectSingleNode('/').nodeName);\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        final String[] expectedAlerts = {"book", "#document", "book", "#document"};
        final List<String> collectedAlerts = new ArrayList<String>();
        loadPage(BrowserVersion.INTERNET_EXPLORER_7_0, content, collectedAlerts);
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testLoadXML_Namespace() throws Exception {
        testLoadXML_Namespace(BrowserVersion.INTERNET_EXPLORER_7_0);
        testLoadXML_Namespace(BrowserVersion.FIREFOX_2);
    }

    private void testLoadXML_Namespace(final BrowserVersion browserVersion) throws Exception {
        final String content = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var text='<someprefix:test xmlns:someprefix=\"http://myNS\"/>';\n"
            + "    if (window.ActiveXObject) {\n"
            + "      var doc=new ActiveXObject('Microsoft.XMLDOM');\n"
            + "      doc.async=false;\n"
            + "      doc.loadXML(text);\n"
            + "    } else {\n"
            + "      var parser=new DOMParser();\n"
            + "      var doc=parser.parseFromString(text,'text/xml');\n"
            + "    }\n"
            + "    alert(doc.documentElement.tagName);\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        final String[] expectedAlerts = {"someprefix:test"};
        final List<String> collectedAlerts = new ArrayList<String>();
        loadPage(browserVersion, content, collectedAlerts);
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * Tests "xml:space" attribute.
     * Not yet implemented.
     *
     * Xalan team response:
     * <quote>See the DOM Level 3 recommendation for discussion of this. XPath returns the start of the XPath text node,
     * which spans multiple DOM nodes. It is the DOM user's responsibility to gather the additional nodes,
     * either manually or by retrieving wholeText rather than value.
     *
     * This is unavoidable since DOM and XPath define the concept of "node" differently.</quote>
     *
     * @throws Exception if the test fails
     */
    @Test
    public void testLoadXML_XMLSpaceAttribute() throws Exception {
        testLoadXML_XMLSpaceAttribute(BrowserVersion.INTERNET_EXPLORER_7_0);
        testLoadXML_XMLSpaceAttribute(BrowserVersion.FIREFOX_2);
    }

    private void testLoadXML_XMLSpaceAttribute(final BrowserVersion browserVersion) throws Exception {
        final String content = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var text='<root xml:space=\\'preserve\\'>This t"
            + "<elem>ext has</elem> <![CDATA[ CDATA ]]>in<elem /> it</root>';\n"
            + "    if (window.ActiveXObject) {\n"
            + "      var doc=new ActiveXObject('Microsoft.XMLDOM');\n"
            + "      doc.async=false;\n"
            + "      doc.loadXML(text);\n"
            + "    } else {\n"
            + "      var parser=new DOMParser();\n"
            + "      var doc=parser.parseFromString(text,'text/xml');\n"
            + "    }\n"
            + "    alert(doc.documentElement.childNodes.length);\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        final String[] expectedAlerts = {"7"};
        final List<String> collectedAlerts = new ArrayList<String>();
        loadPage(browserVersion, content, collectedAlerts);
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testParseError() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var doc = new ActiveXObject('Microsoft.XMLDOM');\n"
            + "    alert(doc.documentElement == null);\n"
            + "    alert(doc.parseError.errorCode === 0);\n"
            + "    alert(doc.parseError.filepos === 0);\n"
            + "    alert(doc.parseError.line === 0);\n"
            + "    alert(doc.parseError.linepos === 0);\n"
            + "    alert(doc.parseError.reason === '');\n"
            + "    alert(doc.parseError.srcText === '');\n"
            + "    alert(doc.parseError.url === '');\n"
            + "    doc.async = false;\n"
            + "    alert(doc.load('" + URL_SECOND + "'));\n"
            + "    alert(doc.documentElement == null);\n"
            + "    alert(doc.parseError.errorCode !== 0);\n"
            + "    alert(doc.parseError.filepos !== 0);\n"
            + "    alert(doc.parseError.line !== 0);\n"
            + "    alert(doc.parseError.linepos !== 0);\n"
            + "    alert(doc.parseError.reason !== '');\n"
            + "    alert(doc.parseError.srcText !== '');\n"
            + "    alert(doc.parseError.url !== '');\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        final String xml
            = "<root>\n"
            + "  <element>\n"
            + "</root>";

        final String[] expectedAlerts = {"true", "true", "true", "true", "true", "true", "true", "true",
            "false",
            "true", "true", "true", "true", "true", "true", "true", "true"};

        final List<String> collectedAlerts = new ArrayList<String>();
        final WebClient client = new WebClient(BrowserVersion.INTERNET_EXPLORER_7_0);
        client.setAlertHandler(new CollectingAlertHandler(collectedAlerts));
        final MockWebConnection conn = new MockWebConnection();
        conn.setResponse(URL_FIRST, html);
        conn.setResponse(URL_SECOND, xml, "text/xml");
        client.setWebConnection(conn);

        client.getPage(URL_FIRST);
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testCreateNSResolver() throws Exception {
        testCreateNSResolver(BrowserVersion.FIREFOX_2, new String[] {"http://myNS"});
        try {
            testCreateNSResolver(BrowserVersion.INTERNET_EXPLORER_7_0, new String[] {"http://myNS"});
            fail("'createNSResolver' is not supported in IE.");
        }
        catch (final Exception e) {
            //expected
        }
    }
    private void testCreateNSResolver(final BrowserVersion browserVersion, final String[] expectedAlerts)
        throws Exception {
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
            + "    if (window.ActiveXObject) {\n"
            + "      var doc=new ActiveXObject('Microsoft.XMLDOM');\n"
            + "      doc.async=false;\n"
            + "      doc.loadXML(text);\n"
            + "    } else {\n"
            + "      var parser=new DOMParser();\n"
            + "      var doc=parser.parseFromString(text,'text/xml');\n"
            + "    }\n"
            + "    alert(doc.createNSResolver(doc.documentElement).lookupNamespaceURI('xsl'));\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        final List<String> collectedAlerts = new ArrayList<String>();
        loadPage(browserVersion, html, collectedAlerts);
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testLoad_Encoding() throws Exception {
        final String[] expectedAlerts =
        {"1610", "1575", "32", "1604", "1610", "1610", "1610", "1610", "1610", "1610", "1604"};
        testLoad_Encoding(BrowserVersion.INTERNET_EXPLORER_7_0, expectedAlerts);
        testLoad_Encoding(BrowserVersion.FIREFOX_2, expectedAlerts);
    }

    private void testLoad_Encoding(final BrowserVersion browserVersion, final String[] expectedAlerts)
        throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var doc = createXmlDocument();\n"
            + "    doc.async = false;\n"
            + "    doc.load('" + URL_SECOND + "');\n"
            + "    var value = doc.documentElement.firstChild.nodeValue;\n"
            + "    for (var i=0; i < value.length; i++ ) {\n"
            + "      alert(value.charCodeAt(i));\n"
            + "    }\n"
            + "  }\n"
            + "  function createXmlDocument() {\n"
            + "    if (document.implementation && document.implementation.createDocument)\n"
            + "      return document.implementation.createDocument('', '', null);\n"
            + "    else if (window.ActiveXObject)\n"
            + "      return new ActiveXObject('Microsoft.XMLDOM');\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        final String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
            + "<something>\u064A\u0627 \u0644\u064A\u064A\u064A\u064A\u064A\u064A\u0644</something>";

        final List<String> collectedAlerts = new ArrayList<String>();
        final WebClient client = new WebClient(browserVersion);
        client.setAlertHandler(new CollectingAlertHandler(collectedAlerts));
        final MockWebConnection conn = new MockWebConnection();
        conn.setResponse(URL_FIRST, html);
        final List< ? extends NameValuePair> emptyList = Collections.emptyList();
        conn.setResponse(URL_SECOND, xml.getBytes("UTF-8"), 200, "OK", "text/xml", emptyList);
        client.setWebConnection(conn);

        client.getPage(URL_FIRST);
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testXmlInsideHtml() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    alert(messageTableHeaders.documentElement.nodeName);\n"
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

        final String[] expectedAlerts = {"columns"};
        final List<String> collectedAlerts = new ArrayList<String>();
        loadPage(BrowserVersion.INTERNET_EXPLORER_7_0, html, collectedAlerts);
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testInstanceOf() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var x = (new DOMParser()).parseFromString('<x/>','text/xml');\n"
            + "    alert(x instanceof XMLDocument);;\n"
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

        final String[] expectedAlerts = {"true"};
        final List<String> collectedAlerts = new ArrayList<String>();
        loadPage(BrowserVersion.FIREFOX_2, html, collectedAlerts);
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testEvaluate() throws Exception {
        final String content = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var s = '<toolbar><button id=\"compose_button\"/></toolbar>';\n"
            + "    var xDoc = (new DOMParser()).parseFromString(s,'text/xml');\n"
            + "    var r = xDoc.evaluate(\"button[@id='compose_button']\", xDoc.firstChild, null, 9, null);\n"
            + "    alert(r.singleNodeValue.tagName);\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";
        final String[] expectedAlerts = {"button"};
        final List<String> collectedAlerts = new ArrayList<String>();
        loadPage(BrowserVersion.FIREFOX_2, content, collectedAlerts);
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testMoveChildBetweenDocuments() throws Exception {
        final String content = "<html><head><title>foo</title><script>\n"
            + "function test() {\n"
            + "  var doc1 = loadXmlDocument();\n"
            + "  var doc2 = loadXmlDocument();\n"
            + "  alert('same doc: ' + (doc1 == doc2));\n"
            + "  var doc1Root = doc1.firstChild;\n"
            + "  alert('in first: ' + doc1Root.childNodes.length)\n"
            + "  var doc1RootOriginalFirstChild = doc1Root.firstChild\n"
            + "  alert(doc1RootOriginalFirstChild.tagName)\n"
            + "  alert('ownerDocument: ' + (doc1RootOriginalFirstChild.ownerDocument == doc1 ? 'doc1' : 'doc2'))\n"
            + "\n"
            + "  var doc2Root = doc2.firstChild;\n"
            + "  alert('in 2nd: ' + doc2Root.childNodes.length)\n"
            + "  doc2Root.appendChild(doc1RootOriginalFirstChild)\n"
            + "  alert('ownerDocument: ' + (doc1RootOriginalFirstChild.ownerDocument == doc1 ? 'doc1' : 'doc2'))\n"
            + "  alert('first child ownerDocument: ' + "
            + "(doc1RootOriginalFirstChild.firstChild.ownerDocument == doc1 ? 'doc1' : 'doc2'))\n"
            + "  alert('in first: ' + doc1Root.childNodes.length)\n"
            + "  alert('in 2nd: ' + doc2Root.childNodes.length)\n"
            + "\n"
            + "  doc1Root.replaceChild(doc1RootOriginalFirstChild, doc1Root.firstChild);\n"
            + "  alert('ownerDocument: ' + (doc1RootOriginalFirstChild.ownerDocument == doc1 ? 'doc1' : 'doc2'))\n"
            + "  alert('in first: ' + doc1Root.childNodes.length)\n"
            + "  alert('in 2nd: ' + doc2Root.childNodes.length)\n"
            + "\n"
            + "  doc2Root.insertBefore(doc1RootOriginalFirstChild, doc2Root.firstChild);\n"
            + "  alert('ownerDocument: ' + (doc1RootOriginalFirstChild.ownerDocument == doc1 ? 'doc1' : 'doc2'))\n"
            + "  alert('in first: ' + doc1Root.childNodes.length)\n"
            + "  alert('in 2nd: ' + doc2Root.childNodes.length)\n"
            + "\n"
            + "}\n"
            + "function loadXmlDocument() {\n"
            + " var doc;\n"
            + " if (document.implementation && document.implementation.createDocument)\n"
            + "   doc = document.implementation.createDocument('', '', null);\n"
            + " else if (window.ActiveXObject)\n"
            + "   doc = new ActiveXObject('Microsoft.XMLDOM');\n"
            + " doc.async = false;\n"
            + " doc.load('foo.xml');\n"
            + " return doc\n"
            + "}\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        final String xml = "<order><book><title/></book><cd/><dvd/></order>";

        final List<String> collectedAlerts = new ArrayList<String>();
        final WebClient client = new WebClient();
        client.setAlertHandler(new CollectingAlertHandler(collectedAlerts));
        final MockWebConnection conn = new MockWebConnection();
        conn.setResponse(URL_FIRST, content);
        conn.setResponse(new URL(URL_FIRST + "foo.xml"), xml, "text/xml");
        client.setWebConnection(conn);

        client.getPage(URL_FIRST);
        final String[] expectedAlerts = {"same doc: false", "in first: 3", "book", "ownerDocument: doc1",
            "in 2nd: 3", "ownerDocument: doc2", "first child ownerDocument: doc2", "in first: 2", "in 2nd: 4",
            "ownerDocument: doc1", "in first: 2", "in 2nd: 3",
            "ownerDocument: doc2", "in first: 1", "in 2nd: 4"};
        assertEquals(Arrays.asList(expectedAlerts).toString(), collectedAlerts.toString());
    }
}
