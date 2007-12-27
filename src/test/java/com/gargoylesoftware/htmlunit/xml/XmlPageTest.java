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
package com.gargoylesoftware.htmlunit.xml;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.httpclient.HttpStatus;
import org.w3c.dom.Node;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.CollectingAlertHandler;
import com.gargoylesoftware.htmlunit.MockWebConnection;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebTestCase;

/**
 * Tests for {@link XmlPage}.
 *
 * @version $Revision$
 * @author Marc Guillemot
 * @author Ahmed Ashour
 */
public class XmlPageTest extends WebTestCase {
    /**
     * Create an instance
     * @param name The name of the test
     */
    public XmlPageTest(final String name) {
        super(name);
    }

    /**
     * Tests namespace
     * @throws Exception if the test fails
     */
    public void testNamespace() throws Exception {
        final String content
            = "<?xml version='1.0'?>\n"
            + "<RDF xmlns='http://www.w3.org/1999/02/22-rdf-syntax-ns#' "
            + "xmlns:em='http://www.mozilla.org/2004/em-rdf#'>"
            + "<Description about='urn:mozilla:install-manifest'>"
            + "<em:name>My Plugin</em:name>"
            + "</Description>\n"
            + "</RDF>";

        final XmlPage xmlPage = testXmlDocument(content, "text/xml");
        final Node node = xmlPage.getXmlDocument().getFirstChild().getFirstChild().getFirstChild();
        assertEquals("em:name", node.getNodeName());
        assertEquals("name", node.getLocalName());
        assertEquals("http://www.mozilla.org/2004/em-rdf#", node.getNamespaceURI());
    }

    /**
     * Tests a simple valid xml document
     * @throws Exception if the test fails
     */
    public void testValidDocument() throws Exception {
        final String content
            = "<?xml version=\"1.0\"?>\n"
             + "<foo>\n"
             + "    <foofoo name='first'>something</foofoo>\n"
             + "    <foofoo name='second'>something else</foofoo>\n"
             + "</foo>";

        final XmlPage xmlPage = testXmlDocument(content, "text/xml");
        assertEquals("foo", xmlPage.getXmlDocument().getFirstChild().getNodeName());
    }

    /**
     * Utility method to test xml page of different mime types
     * @param content the xml content
     * @param mimeType The mime type
     * @return the page returned by the WebClient
     * @throws Exception if a problem occurs
     */
    private XmlPage testXmlDocument(final String content, final String mimeType) throws Exception
    {
        final WebClient client = new WebClient();
        final MockWebConnection webConnection = new MockWebConnection(client);
        webConnection.setDefaultResponse(content, 200, "OK", mimeType);
        client.setWebConnection(webConnection);
        final Page page = client.getPage(URL_FIRST);
        assertEquals(URL_FIRST, page.getWebResponse().getUrl());
        assertEquals("OK", page.getWebResponse().getStatusMessage());
        assertEquals(HttpStatus.SC_OK, page.getWebResponse().getStatusCode());
        assertEquals(mimeType, page.getWebResponse().getContentType());
        assertInstanceOf(page, XmlPage.class);
        final XmlPage xmlPage = (XmlPage) page;
        assertEquals(content, xmlPage.getContent());
        assertNotNull(xmlPage.getXmlDocument());
        return xmlPage;
    }

    /**
     * Tests a simple invalid (badly formed) xml document
     * @throws Exception if the test fails
     */
    public void testInvalidDocument() throws Exception {
        final WebClient client = new WebClient();
        final MockWebConnection webConnection = new MockWebConnection(client);

        final String content
            = "<?xml version=\"1.0\"?>\n"
            + "<foo>\n"
            + "    <foofoo invalid\n"
            + "    <foofoo name='first'>something</foofoo>\n"
            + "    <foofoo name='second'>something else</foofoo>\n"
            + "</foo>";

        webConnection.setDefaultResponse(content, 200, "OK", "text/xml");
        client.setWebConnection(webConnection);

        final Page page = client.getPage(URL_FIRST);
        assertEquals(URL_FIRST, page.getWebResponse().getUrl());
        assertEquals("OK", page.getWebResponse().getStatusMessage());
        assertEquals(HttpStatus.SC_OK, page.getWebResponse().getStatusCode());
        assertEquals("text/xml", page.getWebResponse().getContentType());

        assertInstanceOf(page, XmlPage.class);
        final XmlPage xmlPage = (XmlPage) page;
        assertEquals(content, xmlPage.getContent());
        assertNull(xmlPage.getXmlDocument());
    }

    /**
     * @throws Exception if the test fails
     */
    public void testVoiceXML() throws Exception {
        final String content =
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
            + "<vxml xmlns=\"http://www.w3.org/2001/vxml\""
            + "  xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\""
            + "  xsi:schemaLocation=\"http://www.w3.org/2001/vxml "
            + "   http://www.w3.org/TR/voicexml20/vxml.xsd\""
            + "   version=\"2.0\">\n"
            + "  <form>\n"
            + "    <block>Hello World!</block>\n"
            + "  </form>\n"
            + "</vxml>";

        final XmlPage xmlPage = testXmlDocument(content, "application/voicexml+xml");
        assertEquals("vxml", xmlPage.getXmlDocument().getFirstChild().getNodeName());
    }

    /**
     * @throws Exception if the test fails
     */
    public void testLoad_XMLComment() throws Exception {
        testLoad_XMLComment(BrowserVersion.INTERNET_EXPLORER_7_0, new String[] {"true", "8"});
        testLoad_XMLComment(BrowserVersion.FIREFOX_2, new String[] {"true", "8"});
    }
    
    private void testLoad_XMLComment(final BrowserVersion browserVersion, final String[] expectedAlerts)
        throws Exception {
        final URL firstURL = new URL("http://htmlunit/first.html");
        final URL secondURL = new URL("http://htmlunit/second.xml");
        
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var doc = createXmlDocument();\n"
            + "    doc.async = false;\n"
            + "    alert(doc.load('" + "second.xml" + "'));\n"
            + "    alert(doc.documentElement.childNodes[0].nodeType);\n"
            + "  }\n"
            + "  function createXmlDocument() {\n"
            + "    if (document.implementation && document.implementation.createDocument)\n"
            + "      return document.implementation.createDocument('', '', null);\n"
            + "    else if (window.ActiveXObject)\n"
            + "      return new ActiveXObject('Microsoft.XMLDOM');\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";
        
        final String xml = "<test><!-- --></test>";

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
    public void testCreateElement() throws Exception {
        testCreateElement(BrowserVersion.INTERNET_EXPLORER_7_0, new String[] {"true", "16"});
        testCreateElement(BrowserVersion.FIREFOX_2, new String[] {"true", "14"});
    }

    private void testCreateElement(final BrowserVersion browserVersion, final String[] expectedAlerts)
        throws Exception {
        final String content = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var doc = createXmlDocument();\n"
            + "    doc.appendChild(doc.createElement('elementName'));\n"
            + "    var xml;\n"
            + "    if (window.ActiveXObject)\n"
            + "      xml = doc.xml;\n"
            + "    else\n"
            + "      xml = new XMLSerializer().serializeToString(doc.documentElement);\n"
            + "    alert(xml.indexOf('<elementName/>') != -1);\n"
            + "    alert(xml.length);"
            + "  }\n"
            + "  function createXmlDocument() {\n"
            + "    if (document.implementation && document.implementation.createDocument)\n"
            + "      return document.implementation.createDocument('', '', null);\n"
            + "    else if (window.ActiveXObject)\n"
            + "      return new ActiveXObject('Microsoft.XMLDOM');\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        final List collectedAlerts = new ArrayList();
        loadPage(browserVersion, content, collectedAlerts);
        assertEquals(expectedAlerts, collectedAlerts);
    }
}
