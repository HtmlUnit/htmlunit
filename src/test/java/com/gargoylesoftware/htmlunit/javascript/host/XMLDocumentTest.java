/*
 * Copyright (c) 2002-2007 Gargoyle Software Inc. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 3. The end-user documentation included with the redistribution, if any, must
 *    include the following acknowledgment:
 *
 *       "This product includes software developed by Gargoyle Software Inc.
 *        (http://www.GargoyleSoftware.com/)."
 *
 *    Alternately, this acknowledgment may appear in the software itself, if
 *    and wherever such third-party acknowledgments normally appear.
 * 4. The name "Gargoyle Software" must not be used to endorse or promote
 *    products derived from this software without prior written permission.
 *    For written permission, please contact info@GargoyleSoftware.com.
 * 5. Products derived from this software may not be called "HtmlUnit", nor may
 *    "HtmlUnit" appear in their name, without prior written permission of
 *    Gargoyle Software Inc.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL GARGOYLE
 * SOFTWARE INC. OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
 * OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.gargoylesoftware.htmlunit.javascript.host;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

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
 */
public class XMLDocumentTest extends WebTestCase {

    /**
     * Creates a new test instance.
     * @param name The name of the new test instance.
     */
    public XMLDocumentTest(final String name) {
        super(name);
    }

    /**
     * @throws Exception if the test fails
     */
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
        final List collectedAlerts = new ArrayList();
        loadPage(browserVersion, content, collectedAlerts);
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
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

        final List collectedAlerts = new ArrayList();
        final WebClient client = new WebClient(browserVersion);
        client.setAlertHandler(new CollectingAlertHandler(collectedAlerts));
        final MockWebConnection conn = new MockWebConnection(client);
        conn.setResponse(URL_FIRST, html);
        conn.setResponse(URL_SECOND, xml, "text/xml");
        client.setWebConnection(conn);

        client.getPage(URL_FIRST);
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
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

        final List collectedAlerts = new ArrayList();
        final WebClient client = new WebClient(browserVersion);
        client.setAlertHandler(new CollectingAlertHandler(collectedAlerts));
        final MockWebConnection conn = new MockWebConnection(client);
        conn.setResponse(firstURL, html);
        conn.setResponse(secondURL, xml, "text/xml");
        client.setWebConnection(conn);

        client.getPage(firstURL);
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    public void testPreserveWhiteSpace() throws Exception {
        final String content = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var doc = new ActiveXObject('MSXML2.DOMDocument');\n"
            + "    alert(doc.preserveWhiteSpace);\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";
        final String[] expectedAlerts = {"false"};
        final List collectedAlerts = new ArrayList();
        loadPage(BrowserVersion.INTERNET_EXPLORER_7_0, content, collectedAlerts);
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
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

        final List collectedAlerts = new ArrayList();
        final WebClient client = new WebClient(browserVersion);
        client.setAlertHandler(new CollectingAlertHandler(collectedAlerts));
        final MockWebConnection conn = new MockWebConnection(client);
        conn.setResponse(URL_FIRST, html);
        conn.setResponse(URL_SECOND, xml, "text/xml");
        client.setWebConnection(conn);

        client.getPage(URL_FIRST);
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
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
            + "      doc.async='false';\n"
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
        final List collectedAlerts = new ArrayList();
        loadPage(browserVersion, content, collectedAlerts);
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * Tests "xml:space" attribute.
     * @throws Exception if the test fails
     */
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
            + "      doc.async='false';\n"
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
        final List collectedAlerts = new ArrayList();
        loadPage(browserVersion, content, collectedAlerts);
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
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
        
        final List collectedAlerts = new ArrayList();
        final WebClient client = new WebClient(BrowserVersion.INTERNET_EXPLORER_7_0);
        client.setAlertHandler(new CollectingAlertHandler(collectedAlerts));
        final MockWebConnection conn = new MockWebConnection(client);
        conn.setResponse(URL_FIRST, html);
        conn.setResponse(URL_SECOND, xml, "text/xml");
        client.setWebConnection(conn);

        client.getPage(URL_FIRST);
        assertEquals(expectedAlerts, collectedAlerts);
    }
}
